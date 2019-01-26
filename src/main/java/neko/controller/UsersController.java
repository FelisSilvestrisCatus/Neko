package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-01-14
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private IUsersService usersService;

    @RequestMapping(value = "/login")
    public Map<String, String> login(HttpServletRequest request, String username, String password) {

        HttpSession session = request.getSession();
//        System.out.println("Session:"+session.toString());

        Map<String, String> map = new HashMap<>();
        map.put("state", "400");
        map.put("msg", "error");
        map.put("token", "");
//        System.out.println("u:" + username + "\np:" + password);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", username);
        Users user = usersService.getOne(queryWrapper);

        try {
            System.out.println(user.toString());
            if (password.equals(user.getPwd())) {
                session.setAttribute("user", user);
                System.out.println("session1:" + session.toString());
                map.put("state", "200");
                map.put("msg", "ok");
                map.put("token", "this is a token");
            }
        } catch (Exception e) {

        }

//        System.out.println();
        return map;
    }


    /*
     * 权限测试使用，完成后删除
     * */
    @RequestMapping(value = "/getall")
    public Map<String, String> getall(HttpServletRequest request) {

        HttpSession session = request.getSession();
        String jsonString = JSON.toJSONString(usersService.list());


        Map<String, String> map = new HashMap<>();
        map.put("state", "200");
        map.put("msg", "ok");
        map.put("data", jsonString);
        return map;
    }
}
