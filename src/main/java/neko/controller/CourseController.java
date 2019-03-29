package neko.controller;


import com.alibaba.fastjson.JSON;
import neko.entity.Users;
import neko.entity.vo.StudentCourseName;
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
 * 课程表 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    //获取学生的课程
    @RequestMapping(value = "/getMyCourse")
    public Map<String, String> getMyCourse(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Users users = (Users) request.getSession().getAttribute("user");
        List<StudentCourseName> course = courseService.getMyCourse(users.getUid());

        map.put("state", "200");
        map.put("msg", "ok");
        map.put("data", JSON.toJSONString(course));
        return map;
    }
}
