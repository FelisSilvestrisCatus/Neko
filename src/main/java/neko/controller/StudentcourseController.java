package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.vo.ClassWithTeacherName;
import neko.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-03-27
 */
@RestController
@RequestMapping("/neko/studentcourse")
public class StudentcourseController {

    @Autowired
    private ICourseService courseService;

    //获取学生的课程
    @RequestMapping(value = "/getMyCourse")
    public Map<String, String> getMyCourse(HttpServletRequest request, String name) {
        Map<String, String> map = new HashMap<>();

        Users users = (Users) request.getSession().getAttribute("user");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("sid", users.getUid());
        List<ClassWithTeacherName> course = courseService.list(queryWrapper);

        map.put("state", "200");
        map.put("msg", "ok");
        map.put("data", JSON.toJSONString(course));
        return map;
    }
}
