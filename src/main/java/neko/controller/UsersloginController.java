package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Userslogin;
import neko.service.IUsersloginService;
import neko.utils.LoginInfo;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@RestController
@RequestMapping("/usersLogin")
public class UsersloginController {

    @Autowired
    private IUsersloginService usersloginService;
    @Autowired
    private LoginInfo loginInfo;

    @RequestMapping(value = "/getLast")
    public Map<String, String> getLast(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Users users = (Users) session.getAttribute("user");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", users.getUid());
        queryWrapper.orderByDesc("logintime");
        Map<String, String> map = new HashMap<>();
        List<Userslogin> usersloginList = usersloginService.list(queryWrapper);
        //判断是否是首次登录
        if(usersloginList.size()<2){
            map.put("state", "200");
            map.put("msg", "ok");


        }
        List<Userslogin> getUsersLastTwoLoginList = usersloginService.list(queryWrapper).subList(0,1);

        JSONArray json = JSONArray.fromObject(getUsersLastTwoLoginList);

        map.put("state", "200");
        map.put("msg", "ok");
        System.out.println(json.size());

        map.put("data", json.toString());


        return map;
    }
}