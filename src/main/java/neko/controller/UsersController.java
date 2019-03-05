package neko.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Userslogin;
import neko.service.IUsersService;
import neko.service.IUsersloginService;
import neko.utils.ip.Juhe;
import neko.utils.ip.LoginInfo;
import neko.utils.redis.RedisUtil;
import neko.utils.token.Token;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-01-14
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    //Redis过期时间
    private static final long expire = 7;
    //Redis过期时间单位
//    private static final TimeUnit expireTimeUnit = TimeUnit.DAYS;
    private static final TimeUnit expireTimeUnit = TimeUnit.MINUTES;

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

    @RequestMapping(value = "/login")
    public Map<String, String> login(HttpServletRequest request, String username, String password, Integer loginType) throws IOException {

        Map<String, String> map = new HashMap<>();
        map.put("state", "400");
        map.put("msg", "error");
        map.put("token", "");

        //查询用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", username);
        Users user = usersService.getOne(queryWrapper);

        if (password.equals(user.getPwd())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            String token = Token.getJwtToken(user);
            redisUtil.set(token, user.getUid().toString());
            redisUtil.expire(token, expire, expireTimeUnit);

            map.put("state", "200");
            map.put("msg", "ok");
            map.put("token", token);


            //保存本次登录信息
            Userslogin userslogin = new Userslogin();
            userslogin.setUid(user.getUid());
            userslogin.setLoginip(loginInfo.getIpAddr(request));
            userslogin.setLogintype(1);
            userslogin.setLogintime(LocalDateTime.now());
            String area = loginInfo.getIpLocation(loginInfo.getIpAddr(request));
            if (area.equals("未知地址")) {
                //在淘宝api未得到数据或超时的情况下调用聚合api
                area = juhe.getValue(loginInfo.getIpAddr(request));
            }
            userslogin.setLoginlocation(area);
            usersloginService.save(userslogin);

            user.setPwd("");
            map.put("user", JSON.toJSONString(user));
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


    @RequestMapping(value = "/registe")
    public Map<String, String> registe(HttpServletRequest request) {
        String username = request.getParameter("username");

        String userphone = request.getParameter("userphone");
        String validatecode = request.getParameter("validatecode");
        Map<String, String> map = new HashMap<>();
        //手机号有记录且获取验证码匹配
        if (redisUtil.hasKey(userphone) && redisUtil.get(userphone).equalsIgnoreCase(validatecode)) {
            //插入数据库
            Users user = new Users();
            user.setPhone(userphone);
            user.setUname(username);
            user.setType(2);

            usersService.save(user);
            map.put("state", "200");

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


}