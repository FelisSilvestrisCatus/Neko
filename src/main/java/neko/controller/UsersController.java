package neko.controller;


import neko.entity.Users;
import neko.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-01-07
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private IUsersService usersService;

    @RequestMapping("/getall")
    public List<Users> getall() {
        return usersService.list();
    }

    @RequestMapping("/login")
    public Map<String, String> login(HttpServletRequest request, String username, String password) {
        System.out.println(username + "\n" + password);
        Map<String, String> map = new HashMap<>();
        map.put("state", "200");
        map.put("msg", "ok");
        return map;
    }
}
