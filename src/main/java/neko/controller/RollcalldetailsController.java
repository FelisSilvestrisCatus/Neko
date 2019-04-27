package neko.controller;


import neko.entity.Users;
import neko.service.IRollcalldetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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

    @Autowired
    private IRollcalldetailsService rs;

    //获取我的信息
    @RequestMapping(value = "/myInfo")
    public Map<String, String> myInfo(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        return rs.myInfo(u);
    }
}
