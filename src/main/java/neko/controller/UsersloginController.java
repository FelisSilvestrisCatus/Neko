package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Userslogin;
import neko.service.IUsersloginService;
import neko.utils.LoginInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
        System.out.println("dddddddddd");
        HttpSession session = request.getSession();
        Users users = (Users) session.getAttribute("user");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", users.getUid());
        queryWrapper.orderByDesc("logintime");
        List<Userslogin> usersloginList = usersloginService.listObjs(queryWrapper);
        /*
         * 这里需要判断list中是否有两个对象，取时间最近的两个对象
         * 如果只有一个对象则为第一次登录，last信息全部置NULL
         * */
     //   JsonConfig jsonConfig = new JsonConfig();
        JSONArray json = JSONArray.fromObject(usersloginList);
        System.out.println(json.toString());
        Map<String, String> map = new HashMap<>();
        map.put("state", "200");
        map.put("msg", "ok");
        System.out.println("能获取到的当前用户的登录记录条数"+json.size());
       // map.put("data",((JSONObject )(json.get(0))).toString());
//        System.out.println( ( (JSONObject)json.get(0)).toString());    ;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        return map;
    }
}