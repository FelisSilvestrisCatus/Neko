package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Class;
import neko.entity.Classstudents;
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
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@RestController
@RequestMapping("/classstudents")
public class ClassstudentsController {
    @Autowired
    private IClassService classService;
    @Autowired
    private IClassteacherService classteacherServic;
    @Autowired
    private IClassstudentsService classstudentsService;
    @Autowired
    private IUsersService usersService;

    //加入班级
    @RequestMapping(value = "/joinClass")
    public Map<String, String> joinClass(HttpSession session, String cid) {

        int _cid = Integer.valueOf(cid);
        Map<String, String> map = generalMethod.getErrorMap();
        Users users = (Users) session.getAttribute("user");

        if (users.getType() != 2) {
            //用户不合法
            map.put("msg", "用户不合法");
            return map;
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cid", _cid);
        Class c = classService.getOne(queryWrapper);

        if (null == c) {
            map.put("msg", "班级不存在");
            return map;
        }

        Classstudents cs = new Classstudents();
        cs.setCid(_cid);
        cs.setUid(users.getUid());

        classstudentsService.save(cs);
        map = generalMethod.getSuccessMap();
        map.put("msg", "加入班级成功");
        return map;
    }

    //获取已加入的班级
    @RequestMapping(value = "/getJoinedClass")
    public Map<String, String> getJoinedClass(HttpSession session, String name) {
        Map<String, String> map = new HashMap<>();
        Users users = (Users) session.getAttribute("user");
        List<ClassWithTeacherName> classes = classService.getJoinedclass(users.getUid());

        map.put("state", "200");
        map.put("msg", "ok");
        map.put("data", JSON.toJSONString(classes));
        return map;
    }

    //获取已加入班级的学生
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getStudents")
    public Map<String, String> getJoinedStudent(HttpSession session, String cid, String state) {
        int cid_ = Integer.valueOf(cid);
        int flag_ = Integer.valueOf(state);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cid", cid_);
        queryWrapper.eq("state", flag_);
        Map<String, String> map = new HashMap<>();
        Users users = (Users) session.getAttribute("user");
        List<Classstudents> list = classstudentsService.list(queryWrapper);
        System.out.println("学生数=" + list.size());
        Iterator it = list.iterator();
        List<Integer> uid_list = new ArrayList<>();
        while (it.hasNext()) {
            int uid = ((Classstudents) it.next()).getUid();
            uid_list.add(uid);
        }
        if (uid_list.size() == 0) {
            map.put("data", JSON.toJSONString(null));
        } else {
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.in("uid", uid_list);
            List<Users> usersList = usersService.list(queryWrapper1);
            map.put("data", JSON.toJSONString(usersList));


        }

        return map;
    }

    //删除指定班级id 指定uid的学生
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/deleteStudent")
    public Map<String, String> deleteStudent(HttpSession session, String cid, String uid) {
        int cid_ = Integer.valueOf(cid);
        int uid_ = Integer.valueOf(uid);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cid", cid_);
        queryWrapper.eq("uid", uid_);
        Map<String, String> map = generalMethod.getSuccessMap();
        if (classstudentsService.remove(queryWrapper)) {
            return map;
        } else {
            map = generalMethod.getErrorMap();
            return map;
        }


    }

    //同意学生加入该班级的申请
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/agreeApply")
    public Map<String, String> agreeApply(HttpSession session, String cid, String uid) {
        int cid_ = Integer.valueOf(cid);
        int uid_ = Integer.valueOf(uid);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cid", cid_);
        queryWrapper.eq("uid", uid_);
        List<Classstudents> list = classstudentsService.list(queryWrapper);

        Map<String, String> map = generalMethod.getSuccessMap();
        Iterator it = list.iterator();
        List<Classstudents> list_state = new ArrayList<>();
        Classstudents classstudents = new Classstudents();
        while (it.hasNext()) {
            classstudents = ((Classstudents) it.next()).setState(1);
            list_state.add(classstudents);
        }
        try {
            classstudentsService.updateBatchById(list_state);
            return map;
        } catch (Exception e) {
            map = generalMethod.getErrorMap();
            return map;
        }


    }
}
