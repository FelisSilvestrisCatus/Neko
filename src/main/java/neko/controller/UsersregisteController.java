package neko.controller;


import neko.entity.Users;
import neko.service.IUsersService;
import neko.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户注册信息
 */
@RestController
@RequestMapping("/usersRegiste")
public class UsersregisteController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IUsersService usersService;

    //判断手机号是否已注册
    @RequestMapping(value = "/phoneIsOrNotExist")
    public Map<String, String> phoneIsOrNotExist(HttpServletRequest request, String userphone) {
        Map<String, String> map = new HashMap<>();
        if (usersService.checkUser(userphone)) {
            map.put("state", "400");
            map.put("msg", "手机号已存在");
        } else {
            map.put("state", "200");
            map.put("msg", "手机号不存在");
        }
        return map;
    }

    //判断学号是否已经存在
    @RequestMapping(value = "/idNumberIsOrNotExist")
    public Map<String, String> idNumberIsOrNotExist(HttpServletRequest request, String idnumber) {
        Map<String, String> map = new HashMap<>();
        if (usersService.checkUserByIdnumber(idnumber)) {
            map.put("state", "400");
            map.put("msg", "学号已存在");
        } else {
            map.put("state", "200");
            map.put("msg", "学号不存在");
        }
        return map;

    }

    //用户注册
    @RequestMapping(value = "/registe")
    public Map<String, String> registe(String idnumber, String username, String userphone, String validatecode) {
        Map<String, String> map = new HashMap<>();

        //检查用户是否已存在
        if (usersService.checkUser(userphone)) {
            map.put("state", "400");
            map.put("msg", "用户已存在");
            return map;
        }
        //手机号有记录且获取验证码匹配
        if (redisUtil.hasKey(userphone + "code") && redisUtil.get(userphone + "code").equalsIgnoreCase(validatecode)) {
            //删除验证码
            redisUtil.delete(userphone + "code");
            //插入数据库
            Users user = new Users();
            user.setPhone(userphone);
            user.setUname(username);
            user.setType(2);
            user.setIdnumber(idnumber);
            usersService.save(user);
            map.put("state", "200");
            map.put("msg", "注册成功");
        } else {
            map.put("state", "400");
            map.put("msg", "验证码错误");
        }
        return map;
    }
}