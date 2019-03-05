package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.service.IUsersService;
import neko.utils.message.Message;
import neko.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@RestController
@RequestMapping("/usersRegiste")
public class UsersregisteController {

    //Redis过期时间
    private static final long expire = 5;
    //Redis过期时间单位
    private static final TimeUnit expireTimeUnit = TimeUnit.MINUTES;


    @Autowired
    private IUsersService userslService;
    //使用工具类
    @Autowired
    private Message message;
    //使用redis
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/phoneIsOrNotExist")
    public Map<String, String> phoneIsOrNotExist(HttpServletRequest request, String userphone) {
        Map<String, String> map = new HashMap<>();
        if (checkUser(userphone)) {
            map.put("state", "400");
            map.put("msg", "手机号已存在");
        } else {
            map.put("state", "200");
            map.put("msg", "手机号不存在");
        }
        return map;
    }

    @RequestMapping(value = "/getValidatecode")
    public Map<String, String> getValidatecode(HttpServletRequest request, String userphone) {
        Map<String, String> map = new HashMap<>();
        boolean redis = redisUtil.hasKey(userphone);
        boolean hasRedis = redis && redisUtil.getExpire(userphone, TimeUnit.SECONDS) < ((expire - 1) * 60);
        //查询该手机号用户是否存在,判断上次验证码发送时间,发送验证码
        if (!checkUser(userphone)) {
            if (!redis || hasRedis) {
//                int code = message.getCode(userphone);
                int code = 1111;
                if (code != 0) {
                    //获取验证码存入redis
                    redisUtil.set(userphone, code + "");
                    redisUtil.expire(userphone, expire, expireTimeUnit);
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
        } else {
            map.put("state", "400");
            map.put("msg", "手机号已存在");
        }

        return map;
    }

    //查询该手机号用户是否存在
    private boolean checkUser(String userphone) {
        Map<String, String> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", userphone);
        return userslService.count(queryWrapper) != 0 ? true : false;
    }
}