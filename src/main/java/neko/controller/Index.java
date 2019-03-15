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
        return "此路不通\n" + "Your ip:" + loginInfo.getIpAddr(request) + "\n" + loginInfo.getIpLocation(loginInfo.getIpAddr(request));
    }

    //测试可用性
    @RequestMapping(value = "/alive")
    public String alive(HttpServletRequest request) {
        return "200";
    }

}
