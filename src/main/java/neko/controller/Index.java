package neko.controller;

import neko.utils.ip.LoginInfo;
import neko.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Index {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginInfo loginInfo;

    @RequestMapping(value = "/")
    public String index(HttpServletRequest request) {
//
//        System.out.println("redis test");
//        redisUtil.set("testkey1","testvalue1");
//        String value = redisUtil.get("testkey1");
//
//        System.out.println(value);
//        System.out.println("redis test end");
        String ip = loginInfo.getIpAddr(request);

        String local = "";
        local = loginInfo.getIpLocation(ip);

        System.out.println("local " + local);
        return ip + "\n" + local;
    }


}
