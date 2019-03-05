package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Userslogin;
import neko.service.IUsersService;
import neko.service.IUsersloginService;
import neko.utils.ip.LoginInfo;
import neko.utils.message.Message;
import neko.utils.redis.RedisUtil;
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
@RequestMapping("/usersRegiste")
public class UsersregisteController {

    @Autowired
    private IUsersloginService usersloginService;
    @Autowired
    private IUsersService userslService;
    @Autowired
    private LoginInfo loginInfo;
    //使用工具类
    @Autowired
    private Message message;
    //使用redis
    private RedisUtil edisUtil;

    @RequestMapping(value = "/phoneIsOrNotExist")
    public Map<String, String> phoneIsOrNotExist(HttpServletRequest request) {
        String userphone = request.getParameter("userphone");
        Map<String, String> map = new HashMap<>();
        System.out.println("用户输入手机号后接受手机号" + userphone);
        //查询该手机号用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", userphone);

        List<Users> userlist = userslService.listObjs(queryWrapper);
        System.out.println("拥有该手机号的用户有" + userlist.size() + "个");
        if (userlist.size() == 0) {
            System.out.println("拥有该手机号的用户有0个");
            map.put("state", "0");
        } else {
            map.put("state", "1");

        }
        return map;
    }

    @RequestMapping(value = "/getValidatecode")
    public Map<String, String> getValidatecode(HttpServletRequest request) {
        String userphone = request.getParameter("userphone");
        Map<String, String> map = new HashMap<>();
        System.out.println("获取验证码的手机号" + userphone);
        //查询该手机号用户是否存在
        map = message.getCode(userphone);
        //获取手机号成功
        if (map.get("state").equalsIgnoreCase("0")) {
            //获取验证码存入redis
            System.out.println("控制层获取的验证码" + map.get("state"));

            edisUtil.set("userphone", map.get("state"));

            try {
                edisUtil.set("userphone", map.get("state"));
                map.put("state","ok");
            } catch (Exception e) {
                map.put("state","false");

            }


        } else {

            map.put("state","false");
        }
return map;
    }
}