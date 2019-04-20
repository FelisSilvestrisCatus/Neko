package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Class;
import neko.entity.Course;
import neko.entity.Users;
import neko.entity.vo.StudentCourseName;
import neko.service.IClassService;
import neko.service.ICourseService;
import neko.utils.generalMethod;
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
    @Autowired
    private IClassService classService;

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

    //修改课程信息
    @RequestMapping(value = "/alertCourse")
    public Map<String, String> auditCourse(HttpSession session, String courseid, String cname) {
        Users users = (Users) session.getAttribute("user");
        Map<String, String> map = generalMethod.getSuccessMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("tid", users.getUid());
        queryWrapper.eq("courseid", courseid);
        Course course = courseService.getOne(queryWrapper);

        course.setCname(cname);
        courseService.updateById(course);
        String data = JSON.toJSONString(course);
        map.put("data", data);
        return map;
    }

    //创建课程
    @RequestMapping(value = "/createCourse")
    public Map<String, String> createCourse(HttpSession session, String cid, String cname, String ctime) {
        Users users = (Users) session.getAttribute("user");

        int cday = Integer.valueOf(ctime);
        int cid_ = Integer.valueOf(cid);

        Course course = new Course();
        course.setCname(cname);
        course.setCday(cday);
        course.setCid(cid_);
        course.setTid(users.getUid());

        if (courseService.save(course)) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("cid", cid);

            Class class_ = classService.getOne(queryWrapper);
            class_.setCstate(1);
            System.out.println(class_.getCid());
            System.out.println(class_.getCstate());
            try{
                classService.saveOrUpdate(class_);
                Map<String, String> map = generalMethod.getSuccessMap();
                System.out.println("创建课程");
                return map;
            }catch (Exception e){
                Map<String, String> map = generalMethod.getErrorMap();
                System.out.println("创建失败");
                return map;

            }


        } else {
            Map<String, String> map = generalMethod.getErrorMap();
            return map;
        }


    }
    //获取指定id的课程
    @RequestMapping(value = "/getCourseByCid")
        public Map<String, String> getTeacherCourse(HttpSession session,String cid) {
      int  cid_=Integer.valueOf(cid);
        Map<String, String> map = generalMethod.getSuccessMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cid", cid_);
        List<Course> courselist = courseService.list(queryWrapper);
        String data = JSON.toJSONString(courselist);
        map.put("data", data);
        return map;
    }
}
