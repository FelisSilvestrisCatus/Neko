package neko.controller;

import neko.service.IUsersService;
import neko.service.IUsersloginService;
import neko.utils.ip.Juhe;
import neko.utils.ip.LoginInfo;
import neko.utils.message.Message;
import neko.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
 * 验证码相关
 * */
@RestController
@RequestMapping("/validatecode")
public class ValidatecodeController {

    //Redis过期时间
    private static final long codeExpire = 5;
    //Redis过期时间单位
    private static final TimeUnit codeExpireTimeUnit = TimeUnit.MINUTES;

    @Autowired
    private IUsersService usersService;
    @Autowired
    private IUsersloginService usersloginService;
    @Autowired
    private LoginInfo loginInfo;
    @Autowired
    private Juhe juhe;
    @Autowired
    private Message message;
    @Autowired
    private RedisUtil redisUtil;

    //获取注册验证码
    @RequestMapping(value = "/getValidatecode")
    public Map<String, String> getValidatecode(HttpServletRequest request, String userphone) {
        System.out.println("userphone = " + userphone);
        Map<String, String> map = new HashMap<>();
        boolean redis = redisUtil.hasKey(userphone + "code");
        boolean hasRedis = redis && redisUtil.getExpire(userphone + "code", TimeUnit.SECONDS) < ((codeExpire - 1) * 60);
        //查询该手机号用户是否存在,判断上次验证码发送时间,发送验证码
        if (!usersService.checkUser(userphone)) {
            checkRedis(userphone, map, redis, hasRedis);
        } else {
            map.put("state", "400");
            map.put("msg", "手机号已存在");
        }

        return map;
    }

    //获取登录验证码
    @RequestMapping(value = "/getLoginValidatecode")
    public Map<String, String> getLoginValidatecode(HttpServletRequest request, String userphone) {
        Map<String, String> map = new HashMap<>();
        //是否存在验证码
        boolean redis = redisUtil.hasKey(userphone + "code");
        //距离上次发送验证码是否超过60秒
        boolean hasRedis = redis && redisUtil.getExpire(userphone + "code", TimeUnit.SECONDS) < ((codeExpire - 1) * 60);
        //查询该手机号用户是否存在,判断上次验证码发送时间,发送验证码
        if (usersService.checkUser(userphone)) {
            checkRedis(userphone, map, redis, hasRedis);
        } else {
            map.put("state", "400");
            map.put("msg", "手机号未注册");
        }

        return map;
    }

    //检查redis中的验证码
    private void checkRedis(String userphone, Map<String, String> map, boolean redis, boolean hasRedis) {
        if (!redis || hasRedis) {
//                int code = message.getCode(userphone);
            int code = 1111;

            if (code != 0) {
                //获取验证码存入redis
                redisUtil.set(userphone + "code", code + "");
                redisUtil.expire(userphone + "code", codeExpire, codeExpireTimeUnit);
                map.put("state", "200");
                map.put("msg", "验证码发送成功");
            } else {
                map.put("state", "400");
                map.put("msg", "验证码发送失败");
            }
        } else {
            map.put("state", "400");
            map.put("msg", "验证码发送频繁");
        }
    }

    //验证码登录
    @RequestMapping(value = "/loginByCode")
    public Map<String, String> loginByCode(HttpServletRequest request, String phone, String code, String loginType) {

        Map<String, String> map = new HashMap<>();
        map.put("state", "400");
        map.put("msg", "error");
        map.put("token", "");


        if (!redisUtil.hasKey(phone + "code")) {
            map.put("msg", "验证码已过期，请重新获取");
            return map;
        }

        String redisCode = redisUtil.get(phone + "code");
        if (redisCode.equals(code)) {
            usersService.login(request, phone, map, loginType);
        } else {
            map.put("msg", "验证码错误");
            return map;
        }

        return map;
    }
}
