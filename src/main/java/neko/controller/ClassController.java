package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Class;
import neko.entity.Classteacher;
import neko.entity.Users;
import neko.entity.vo.ClassWithTeacherName;
import neko.service.IClassService;
import neko.service.IClassstudentsService;
import neko.service.IClassteacherService;
import neko.service.IUsersService;
import neko.utils.generalMethod;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
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
@RequestMapping("/class")
public class ClassController {
    @Autowired
    private IUsersService userslService;
    @Autowired
    private IClassService classService;
    @Autowired
    private IClassteacherService classteacherServic;
    @Autowired
    private IClassstudentsService classstudentsService;

    //创建班级
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/createClass")
    public Map<String, String> getValidatecode(HttpSession session, String name) {


        Users users = (Users) session.getAttribute("user");


        //创建班级表
        Class newclass = new Class();
        newclass.setCname(name);
        classService.save(newclass);

        //班级绑定老师
        Classteacher classteacher = new Classteacher();
        classteacher.setCid(newclass.getCid());
        classteacher.setUid(users.getUid());

        if (classteacherServic.save(classteacher)) {
            Map<String, String> map = generalMethod.getSuccessMap();
            map.put("msg", "创建班级成功");
            return map;
        } else {
            Map<String, String> map = generalMethod.getErrorMap();
            map.put("msg", "创建班级失败");
            return map;


        }


    }

    //修改班级信息
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/changeClass")
    public Map<String, String> changeClass(HttpSession session, String name, String cid) {


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cid", cid);
        Users users = (Users) session.getAttribute("user");
        Class classTeacher = classService.getOne(queryWrapper);
        classTeacher.setCname(name);
        if (classService.updateById(classTeacher)) {
            Map<String, String> map = generalMethod.getSuccessMap();
            return map;

        } else {
            Map<String, String> map = generalMethod.getErrorMap();
            return map;

        }

    }

    /**
     * @param:flag 选择显示列表类型 0显示全部  1 显示已经加入 2 显示待审核
     **/
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/auditClass")
    public Map<String, String> auditClass(HttpSession session, String flag, String cid) {
        Map<String, String> map = generalMethod.getSuccessMap();

        QueryWrapper queryWrapper = new QueryWrapper();
        Users users = (Users) session.getAttribute("user");
        queryWrapper.eq("cid", cid);

        if (users.getType() != 1 || !classteacherServic.getOne(queryWrapper).getUid().equals(users.getUid())) {
            //用户不合法
            map.put("state", "400");
            map.put("msg", "用户不合法");
            return map;
        }

        if (flag.equals("0")) {
            //显示全部该课程的学生
            List<Users> userlist = classstudentsService.list(queryWrapper);
            //消除用戶敏感信息 去掉密码等信息
            Iterator<Users> itr = userlist.iterator();

            while (itr.hasNext()) {
                itr.next().setPwd("");
            }
        }
        return map;
    }

    //获取所有班级
    @RequestMapping(value = "/getAllClass")
    public Map<String, String> getAllClass(HttpSession session) {
        Map<String, String> map = generalMethod.getSuccessMap();
        Users u = (Users) session.getAttribute("user");
        //排除已加入的班级
        List<ClassWithTeacherName> classes = classService.getAllclass(u.getUid());
        map.put("data", JSON.toJSONString(classes));
        return map;
    }

    //获取老师创建的班级
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getTeacherClass")
    public Map<String, String> getTeacherClass(HttpSession session) {
        Users users = (Users) session.getAttribute("user");
        Map<String, String> map = generalMethod.getSuccessMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", users.getUid());
        //获取老师创建的班级
        List<Classteacher> classlistbyuid = classteacherServic.list(queryWrapper);
        Iterator it = classlistbyuid.iterator();
        List<Integer> cid_list = new ArrayList<>();
        while (it.hasNext()) {
            int cid = ((Classteacher) it.next()).getCid();
            cid_list.add(cid);
        }
        System.out.println("该老师开的总共课程个数" + cid_list.size());
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.in("cid", cid_list);
        List<Class> classList = classService.list(queryWrapper1);
        System.out.println("打印" + classList.size());
        map.put("data", JSON.toJSONString(classList));
        return map;
    }

    //获取老师创建的所有班级（含学生数目）
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getClassStudent")
    public Map<String, String> getClassStudent(HttpSession session) {
        Users users = (Users) session.getAttribute("user");

        Map<String, String> map = generalMethod.getSuccessMap();
        List<ClassWithTeacherName> list = classService.getClassStudent(users.getUid());
        map.put("data", JSON.toJSONString(list));
        return map;
    }
}
