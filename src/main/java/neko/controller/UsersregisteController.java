package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户注册信息
 */
@RestController
@RequestMapping("/usersRegiste")
public class UsersregisteController {

    @Autowired
    private IUsersService usersService;

    //判断手机号是否已注册
    @RequestMapping(value = "/phoneIsOrNotExist")
    public Map<String, String> phoneIsOrNotExist(HttpServletRequest request, String userphone) {
        Map<String, String> map = new HashMap<>();
        if (checkUser(userphone)) {
            map.put("state", "400");
            map.put("msg", "手机号已存在");
        } else {
            map.put("state", "200");
            map.put("msg", "手机号不存在");
        }
        return map;
    }

    //查询该手机号用户是否存在
    private boolean checkUser(String userphone) {
        Map<String, String> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", userphone);
        return usersService.count(queryWrapper) != 0 ? true : false;
    }
}