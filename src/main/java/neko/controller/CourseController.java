package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Course;
import neko.entity.Users;
import neko.entity.vo.StudentCourseName;
import neko.service.ICourseService;
import neko.utils.generalMethod;
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
        System.out.println("map = " + map);
        return map;
    }

    //获取老师所有的课程
    @RequestMapping(value = "/getTeacherCourse")
    public Map<String, String> getTeacherCourse(HttpServletRequest request) {
        Users users = (Users) request.getSession().getAttribute("user");
        Map<String, String> map = generalMethod.getSuccessMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", users.getUid());

        List<Course> courselist = courseService.list(queryWrapper);


        String data = JSON.toJSONString(courselist);
        map.put("data", data);
        return map;
    }

}
