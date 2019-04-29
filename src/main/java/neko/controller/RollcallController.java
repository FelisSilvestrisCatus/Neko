package neko.controller;


import com.alibaba.fastjson.JSON;
import neko.entity.Rollcalltype;
import neko.entity.Users;
import neko.entity.vo.StudentCourseName;
import neko.entity.vo.TeacherRollCall;
import neko.service.IRollcallService;
import neko.service.IRollcalltypeService;
import neko.utils.generalMethod;
import neko.utils.ip.Juhe;
import neko.utils.ip.LoginInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
@RequestMapping("/rollcall")
public class RollcallController {
    //修改班级信息
    @Autowired
    private LoginInfo loginInfo;
    @Autowired
    private Juhe juhe;
    @Autowired
    private IRollcallService rollcallService;
    @Autowired
    private IRollcalltypeService rollcalltypeService;

    @RequestMapping(value = "/getWeather")
    public Map<String, String> getIpCity(HttpServletRequest request) throws IOException {
        String ip = loginInfo.getIpAddr(request);
        Map<String, String> map = generalMethod.getSuccessMap();

        map.put("data", JSON.toJSONString(juhe.getWeather(ip)));

        return map;
    }

    //显示今天可以点名的班级
    @RequiresPermissions("teacher")

    @RequestMapping(value = "/getCourseToday")
    public Map<String, String> getCourseToday(HttpSession session) throws IOException {
        Users users = (Users) session.getAttribute("user");
        //获取当前周几
        Date date = new Date();
        int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int cday = weekDays[w];
        Map<String, String> map = generalMethod.getSuccessMap();

        List<StudentCourseName> list = rollcallService.getCourseToday(users.getUid(), 4);
        map.put("data", JSON.toJSONString(list));

        return map;
    }

    //读取名方式
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getRollType")
    public Map<String, String> getRollType(HttpSession session) throws IOException {

        Map<String, String> map = generalMethod.getSuccessMap();

        List<Rollcalltype> list = rollcalltypeService.list();
        map.put("data", JSON.toJSONString(list));
        System.out.println(map.get("data"));
        return map;
    }

    //根据课程id获取要点名的学生
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getCourseStudentWithoutVacate")
    public Map<String, String> getCourseStudentWithoutVacate(HttpSession session, String courseid) throws IOException {
        int courseid_ = Integer.valueOf(courseid);
        Map<String, String> map = generalMethod.getSuccessMap();
        List<TeacherRollCall> list = rollcallService.getCourseStudentWithoutVacate(courseid_);
        map.put("data", JSON.toJSONString(list));
        System.out.println(map.get("data"));
        return map;
    }
}
