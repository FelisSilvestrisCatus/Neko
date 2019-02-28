package neko.controller;

import neko.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Index {
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/")
    public String index() {
//
//        System.out.println("redis test");
//        redisUtil.set("testkey1","testvalue1");
//        String value = redisUtil.get("testkey1");
//
//        System.out.println(value);
//        System.out.println("redis test end");

        return "This is api";
    }


}
