package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Class;
import neko.entity.Users;
import neko.utils.generalMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@RestController
@RequestMapping("/classteacher")
public class ClassteacherController {
    //修改班级信息
    @RequestMapping(value = "/getMyCreateClass")
    public Map<String, String> getMyCreateClass(HttpSession session) {
        Map<String, String> map = new HashMap<>();
        Users users = (Users) session.getAttribute("user");
        int uid=users.getUid();
        System.out.println("老师"+uid);
        //获取老师创建的班级






        return map;
    }
}
