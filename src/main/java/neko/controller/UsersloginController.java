package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Userslogin;
import neko.service.IUsersloginService;
import neko.utils.generalMethod;
import neko.utils.ip.Geocoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 用户登录信息
 */
@RestController
@RequestMapping("/usersLogin")
public class UsersloginController {

    @Autowired
    private IUsersloginService usersloginService;

    //获取登录信息
    @RequestMapping(value = "/getLast")
    public Map<String, String> getLast(HttpSession session) {
        Map<String, String> map = generalMethod.getSuccessMap();

        Users users = (Users) session.getAttribute("user");

        //查询登录记录
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", users.getUid());
        queryWrapper.orderByDesc("logintime");
        List<Userslogin> usersloginList = usersloginService.list(queryWrapper);

        //处理时间格式
        Userslogin u = usersloginList.get(0);
        String time = u.getLogintime().toString().replace("T", " ");
        time.substring(0, time.length() - 4);
        map.put("time", time);
        map.put("ip", u.getLoginip());
        map.put("area", u.getLoginlocation());
        map.put("type", u.getLogintype().toString());

        //判断是否是首次登录
        if (usersloginList.size() < 2) {
            map.put("isfirst", "true");
        } else {
            u = usersloginList.get(1);
            time = u.getLogintime().toString().replace("T", " ");
            time.substring(0, time.length() - 4);
            map.put("lasttime", time);
            map.put("lastip", u.getLoginip());
            map.put("lastarea", u.getLoginlocation());
            map.put("lasttype", u.getLogintype().toString());
            map.put("isfirst", "false");
        }

        return map;
    }

    //获取定位信息
    @RequestMapping(value = "/location")
    public Map<String, String> location(String lat, String lng) {
        String addr = Geocoder.getAddrInfo(lat, lng);
        Map<String, String> map = generalMethod.getSuccessMap();
        map.put("data", addr);
        return map;
    }
}