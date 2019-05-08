package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsermessageService;
import neko.utils.generalMethod;
import neko.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-04-20
 */
@RestController
@RequestMapping("/usermessage")
public class UsermessageController {

    @Autowired
    private IUsermessageService usermessage;

    @Autowired
    private RedisUtil redisUtil;

    //Redis过期时间
    private static final long tokenExpire = 12;
    //Redis过期时间单位
    private static final TimeUnit tokenExpireTimeUnit = TimeUnit.HOURS;


    //获取未读消息数量
    @RequestMapping(value = "/getUnReadNum")
    public Map<String, String> getUnReadNum(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        Map<String, String> map = generalMethod.getSuccessMap();
        int count = 0;

        String key = "Message:" + u.getUid();
        //检查redis中是否有缓存
        if (redisUtil.hasKey(key)) {
            count = Integer.parseInt(redisUtil.get(key));
        } else {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("uid", u.getUid());
            queryWrapper.eq("mstate", 0);
            count = usermessage.count(queryWrapper);
            redisUtil.set(key, Integer.toString(count));
            redisUtil.expire(key, tokenExpire, tokenExpireTimeUnit);
        }
        map.put("data", Integer.toString(count));

        return map;
    }

    @RequestMapping(value = "/getMsg")
    public Map<String, String> getMsg(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        return usermessage.getMsg(u.getUid());
    }

    @RequestMapping(value = "/setMsgRead")
    public Map<String, String> setMsgRead(HttpSession session, int umid) {
        Users u = (Users) session.getAttribute("user");
        String key = "Message:" + u.getUid();
        redisUtil.delete(key);
        return usermessage.setMsgRead(u.getUid(), umid);
    }

    @RequestMapping(value = "/setAllMsgRead")
    public Map<String, String> setAllMsgRead(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        String key = "Message:" + u.getUid();
        redisUtil.delete(key);
        return usermessage.setAllMsgRead(u.getUid());
    }
}
