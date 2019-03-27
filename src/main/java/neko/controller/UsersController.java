package neko.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsersService;
import neko.service.IUsersloginService;
import neko.utils.ip.Juhe;
import neko.utils.ip.LoginInfo;
import neko.utils.redis.RedisUtil;
import neko.utils.token.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户操作
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    //Redis过期时间
    private static final long expire = 7;
    //Redis过期时间单位
    private static final TimeUnit expireTimeUnit = TimeUnit.DAYS;
//    private static final TimeUnit expireTimeUnit = TimeUnit.MINUTES;

    @Autowired
    private IUsersService usersService;
    @Autowired
    private IUsersloginService usersloginService;
    @Autowired
    private LoginInfo loginInfo;
    @Autowired
    private Juhe juhe;
    @Autowired
    private RedisUtil redisUtil;

    //用户登录
    @RequestMapping(value = "/login")
    public Map<String, String> login(HttpServletRequest request, String phone, String password, Integer loginType) throws IOException {

        Map<String, String> map = new HashMap<>();

        //查询用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        Users user = usersService.getOne(queryWrapper);

        if (password.equals(user.getPwd())) {
            usersService.login(request, phone, map);
        } else {
            map.put("state", "400");
            map.put("msg", "用户名或密码错误");
            map.put("token", "");
        }

        return map;
    }


    /*
     * 权限测试使用，完成后删除
     * */
    @RequestMapping(value = "/getall")
    public Map<String, String> getall(HttpServletRequest request) {

        HttpSession session = request.getSession();
        String jsonString = JSON.toJSONString(usersService.list());


        Map<String, String> map = new HashMap<>();
        map.put("state", "200");
        map.put("msg", "ok");
        map.put("data", jsonString);
        return map;
    }

    //用户注册
    @RequestMapping(value = "/registe")
    public Map<String, String> registe(HttpServletRequest request, String username, String userphone, String validatecode) {
        Map<String, String> map = new HashMap<>();
        //手机号有记录且获取验证码匹配
        if (redisUtil.hasKey(userphone) && redisUtil.get(userphone + "code").equalsIgnoreCase(validatecode)) {
            //插入数据库
            Users user = new Users();
            user.setPhone(userphone);
            user.setUname(username);
            user.setType(2);
            usersService.save(user);
            map.put("state", "200");
            //删除验证码
            redisUtil.delete(userphone + "code");
        } else {
            map.put("state", "400");
        }
        return map;
    }

    //修改用户信息
    @RequestMapping(value = "/changeInfo")
    public Map<String, String> changeInfo(HttpServletRequest request, String email, String password) {

        Map<String, String> map = new HashMap<>();

        Users users = (Users) request.getSession().getAttribute("user");
        Users u = usersService.getById(users.getUid());
        if (!StringUtils.isBlank(email))
            u.setEmail(email);
        if (!StringUtils.isBlank(password)) {
            u.setPwd(password);
        }
        if (usersService.updateById(u)) {
            map.put("state", "200");
        } else {
            map.put("state", "400");
        }
        return map;
    }

    //修改用户信息
    @RequestMapping(value = "/getInfo")
    public Map<String, String> getInfo(HttpServletRequest request) {
        //获取token
        String token = request.getHeader("Authorization");
        Map<String, String> info = Token.parseJwtToken(token);
        //查询用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", info.get("Phone"));
        Users user = usersService.getOne(queryWrapper);
        //清除密码
        user.setPwd("");
        //返回数据
        Map<String, String> map = new HashMap<>();
        map.put("state", "200");
        map.put("user", JSON.toJSONString(user));
        return map;
    }

}