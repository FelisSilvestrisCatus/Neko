package neko.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neko.entity.Rollcall;
import neko.entity.Rollcalldetails;
import neko.entity.Rollcalltype;
import neko.entity.Users;
import neko.entity.vo.StudentCourseName;
import neko.entity.vo.StudentRollCallRate;
import neko.entity.vo.TeacherRollCall;
import neko.service.IRollcallService;
import neko.service.IRollcalldetailsService;
import neko.service.IRollcalltypeService;
import neko.utils.face.Face;
import neko.utils.face.NekoFace;
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
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private NekoFace nekoFace;
    //修改班级信息
    @Autowired
    private LoginInfo loginInfo;
    @Autowired
    private Juhe juhe;
    @Autowired
    private IRollcallService rollcallService;
    @Autowired
    private IRollcalldetailsService rollcalldetailsService;
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
        System.out.println("cday = " + cday);
        List<StudentCourseName> list = rollcallService.getCourseToday(users.getUid(), cday);
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

    //存储一次点名的
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/insertOneRollcall")
    public Map<String, String> insertOneRollcall(HttpSession session, String courseid, String rollType, String datamap) throws IOException {
        System.out.println("datamap = " + datamap);
        int courseid_ = Integer.valueOf(courseid);
        int rollType_ = Integer.valueOf(rollType);

        Rollcall rollcall = new Rollcall();
        rollcall.setCid(courseid_);
        rollcall.setRtime(LocalDateTime.now());
        rollcall.setRtid(rollType_);
        if (rollcallService.save(rollcall)) {
            //存储学生点名信息
            List<Rollcalldetails> list = new ArrayList<>();
            JSONObject json = JSONObject.parseObject(datamap);
            System.out.println(json);
            for (Map.Entry<String, Object> entry : json.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
                Rollcalldetails rollcalldetails = new Rollcalldetails();
                rollcalldetails.setState(Integer.valueOf(entry.getValue() + ""));
                rollcalldetails.setUid(Integer.valueOf(entry.getKey() + ""));
                rollcalldetails.setRid(rollcall.getRid());
                list.add(rollcalldetails);

            }
            if (rollcalldetailsService.saveBatch(list)) {
                System.out.println("要插入的数据条数" + list.size());


                Map<String, String> map = generalMethod.getSuccessMap();
                return map;
            } else {
                Map<String, String> map = generalMethod.getErrorMap();

                return map;
            }


        } else {

            Map<String, String> map = generalMethod.getErrorMap();

            return map;
        }


    }
    //返回课程id下的全部点名信息  但仅仅返回指定的一条

    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getLastRollCallInfo")
    public Map<String, String> getLastRollCallInfo(HttpSession session, String courseid, String index) throws IOException {
        int index_ = Integer.valueOf(index);
        int courseid_ = Integer.valueOf(courseid);
        Map<String, String> map = generalMethod.getSuccessMap();
        List<StudentCourseName> list = rollcallService.getLastRollCallInfo(courseid_);
        System.out.println("当前查询的index" + index_);
        System.out.println("返回集合的四则" + list.size());
        if (index_ <= list.size()) {
            StudentCourseName studentCourseName = new StudentCourseName();
            studentCourseName = list.get(index_ - 1);
            System.out.println(studentCourseName.getRetime() + "" + "点名时间");
            map.put("data", JSON.toJSONString(studentCourseName));

            return map;

        } else {
            map = generalMethod.getErrorMap();

            return map;
        }

    }

    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getAllRollCallOneWeekHistory")
    public Map<String, String> getAllRollCallOneWeekHistory(HttpSession session, String courseid) throws IOException {

        int courseid_ = Integer.valueOf(courseid);
        Map<String, String> map = generalMethod.getSuccessMap();
        List<StudentCourseName> list = rollcallService.getLastRollCallInfo(courseid_);

        System.out.println("返回集合的四则" + list.size());

        System.out.println("要返回给前台的数据" + JSON.toJSONString(list));
        map.put("data", JSON.toJSONString(list));


        return map;
    }

    //根据出勤率筛选学生
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getStudentRollCallRate")
    public Map<String, String> getStudentRollCallRate(HttpSession session, String courseid, String rate) throws IOException {
        int rate_ = Integer.valueOf(rate);
        int courseid_ = Integer.valueOf(courseid);
        Map<String, String> map = generalMethod.getSuccessMap();
        List<StudentRollCallRate> list = rollcallService.getStudentRollCallRate(courseid_);
        List<StudentRollCallRate> listforRate = new ArrayList<>();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            StudentRollCallRate StudentRollCallRate = (StudentRollCallRate) iterator.next();
            System.out.println("当前学生的出勤" + StudentRollCallRate.getAttrate());
            if (StudentRollCallRate.getAttrate() < rate_) {

                listforRate.add(StudentRollCallRate);
            }


        }
        map.put("data", JSON.toJSONString(listforRate));
        System.out.println("出勤率小于" + rate_ + "的学生" + JSON.toJSONString(listforRate));


        return map;
    }

    //默认比率请假列表
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getStudentRollCallRandom")
    public Map<String, String> getStudentRollCallRandom(HttpSession session, String courseid) throws IOException {

        int courseid_ = Integer.valueOf(courseid);
        System.out.println("选择随机点名方式");
        Map<String, String> map = generalMethod.getSuccessMap();
        List<TeacherRollCall> list = rollcallService.getStudentRollCallRandom(courseid_);

        map.put("data", JSON.toJSONString(list));
        System.out.println("默认比率需要点名的学生" + JSON.toJSONString(list));


        return map;
    }

    //默认比率请假列表
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/faceRocall")
    /**
     * imgcode 前端图片信息
     */
    public Map<String, String> faceRocall(HttpSession session, String uid, String imgcode) throws IOException {


        System.out.println("开始为" + uid + "进行人脸点名");
        Map<String, String> map = generalMethod.getSuccessMap();
        String temppath = Face.base64StrToImage(imgcode, uid);//用来保存临时图片
        JSONObject jsonObject = nekoFace.faceRecognition(temppath, uid);

        System.out.println("该用户是否是本人" + jsonObject.getString("isThisGuy"));

        map.put("data", jsonObject.getString("isThisGuy"));
        return map;
    }

    //默认比率请假列表
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getPhotoNum")
    /**
     * imgcode 前端图片信息
     */
    public Map<String, String> getPhotoNum(HttpSession session, String uid) throws IOException {

        Map<String, String> map = generalMethod.getSuccessMap();
        int num;
        num = Face.getPhotoNum(uid);
        map.put("num", num + "");


        return map;
    }
}