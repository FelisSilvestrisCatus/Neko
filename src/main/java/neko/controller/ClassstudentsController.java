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
    //加入班级

    @RequestMapping(value = "/joinClass")
    public Map<String, String> joinClass(HttpServletRequest request, String cid) {
        System.out.println("cid = " + cid);
        int _cid = Integer.valueOf(cid);
        Map<String, String> map = new HashMap<>();
        Users users = (Users) request.getSession().getAttribute("user");

        if (users.getFlag() != 0) {
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state", "401");
            map.put("msg", "用户状态不合法(疑似注销)");
            return map;

        }
        if (users.getType() != 2) {
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state", "401");
            map.put("msg", "用户权限不够");
            return map;
        }
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("cid", _cid);
        Class _class = classService.getOne(queryWrapper);
        if (null == _class) {
            map.put("state", "400");
            map.put("msg", "班级不存在");
            return map;
        }
        Classstudents classstudents = new Classstudents();
        classstudents.setCid(_cid);
        classstudents.setUid(users.getUid());

        classstudentsService.save(classstudents);
        map.put("state", "200");
        map.put("msg", "加入班级成功");
        return map;

    }

    //获取已加入的班级
    @RequestMapping(value = "/getJoinedClass")
    public Map<String, String> getJoinedClass(HttpSession session, HttpServletRequest request, String name) {
        Map<String, String> map = new HashMap<>();
        Users users = (Users) session.getAttribute("user");
        List<ClassWithTeacherName> classes = classService.getJoinedclass(users.getUid());

        map.put("state", "200");
        map.put("msg", "ok");
        map.put("data", JSON.toJSONString(classes));
        return map;
    }
}
