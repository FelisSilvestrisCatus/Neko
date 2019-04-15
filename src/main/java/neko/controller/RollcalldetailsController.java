package neko.controller;


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
 * @since 2019-03-03
 */
@RestController
@RequestMapping("/rollcalldetails")
public class RollcalldetailsController {
    //获取我的考勤信息
    @RequestMapping(value = "/myInfo")
    public Map<String, String> myInfo(HttpSession session, HttpServletRequest request, String name) {
        Map<String, String> map = new HashMap<>();
//        Users users = (Users) session.getAttribute("user");
//        String uid=users.get
//        map.put("state", "200");
//        map.put("msg", "ok");
//        map.put("data", JSON.toJSONString(classes));
        return map;
    }
}
