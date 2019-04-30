package neko.controller;


import neko.entity.Classteacher;
import neko.entity.Users;
import neko.service.IClassteacherService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/classteacher")
public class ClassteacherController {
    @Autowired
    IClassteacherService classteacherService;

    //修改班级信息
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getMyCreateClass")
    public Map<String, String> getMyCreateClass(HttpSession session) {
        Map<String, String> map = new HashMap<>();
        Users users = (Users) session.getAttribute("user");
        int uid = users.getUid();
        System.out.println("老师" + uid);
        //获取老师创建的班级

        List<Classteacher> classteacherList = classteacherService.list();

        return map;
    }
}
