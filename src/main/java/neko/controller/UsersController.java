package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Userslogin;
import neko.service.IUsersService;
import neko.service.IUsersloginService;
import neko.utils.ip.Juhe;
import neko.utils.ip.LoginInfo;
import neko.utils.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
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
    @Autowired
    private IUsersloginService usersloginService;
    @Autowired
    private LoginInfo loginInfo;
    @Autowired
    private Juhe juhe;


    @RequestMapping(value = "/login")
    public Map<String, String> login(HttpServletRequest request, String username, String password, Integer loginType) throws IOException {

        //token
        System.out.println(request.getHeader("Authorization"));
        System.out.println("类型" + loginType);
        Map<String, String> map = new HashMap<>();
        map.put("state", "400");
        map.put("msg", "error");
        map.put("token", "");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", username);
        Users user = usersService.getOne(queryWrapper);


        System.out.println(user.toString());
        if (password.equals(user.getPwd())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            map.put("state", "200");
            map.put("msg", "ok");
            map.put("token", Token.getJwtToken(user));

            //保存本次登录信息
            Userslogin userslogin = new Userslogin();
            userslogin.setUid(user.getUid());
            userslogin.setLoginip(loginInfo.getIpAddr(request));
            userslogin.setLogintype(1);
            userslogin.setLogintime(LocalDateTime.now());
            String area = loginInfo.getIpLocation(loginInfo.getIpAddr(request));
            if (area.equals("未知地址")) {
                //在淘宝api未得到数据或超时的情况下调用聚合api
                area = juhe.getValue(loginInfo.getIpAddr(request));
            }
            userslogin.setLoginlocation(area);
            usersloginService.save(userslogin);
        }

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