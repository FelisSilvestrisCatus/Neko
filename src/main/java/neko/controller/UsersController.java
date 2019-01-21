package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("state", "400");
        map.put("msg", "error");

        System.out.println("u:" + username + "\np:" + password);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", username);
        Users users = usersService.getOne(queryWrapper);

        try {
            System.out.println(users.toString());
            if (password.equals(users.getPwd())) {
                map.put("state", "200");
                map.put("msg", "ok");
            }
        } catch (Exception e) {

        }
        return map;
    }
}
