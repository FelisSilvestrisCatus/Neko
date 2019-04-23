package neko.controller;

import neko.utils.ip.LoginInfo;
import neko.utils.redis.RedisUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
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


    /*
     * shiro
     * */
    @RequiresPermissions("student")
    @RequestMapping(value = "/ss")
    public String student(HttpServletRequest request) {
        return "200+stu";
    }


    @RequiresPermissions("teacher")
    @RequestMapping(value = "/st")
    public String teacger(HttpServletRequest request) {
        return "200+tea";
    }

    @RequestMapping(value = "/s")
    public String s(HttpServletRequest request) {
        return "200+tea";
    }

}
