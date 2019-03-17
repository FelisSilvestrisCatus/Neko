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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

    @RequestMapping(value = "/createClass")
    public Map<String, String> getValidatecode(HttpServletRequest request, String name) {

        Map<String, String> map = new HashMap<>();
        Users users = (Users) request.getSession().getAttribute("user");

        if (users.getFlag() != 0) {
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state", "401");
            map.put("msg", "用户状态不合法(疑似注销)");
            return map;

        }
        if (users.getType() != 1) {
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state", "401");
            map.put("msg", "用户权限不够");
            return map;
        }

        try {//创建班级表
            Class newclass = new Class();
            newclass.setCname(name);
            classService.save(newclass);

            //老师绑定课程

            QueryWrapper queryWrapper = new QueryWrapper();

            queryWrapper.orderByAsc("cid");

            Classteacher classteacher = new Classteacher();

            classteacher.setCid((((Class) classService.list(queryWrapper).get(classService.count() - 1)).getCid()));
            classteacher.setUid(users.getUid());
            classteacherServic.save(classteacher);
            map.put("state", "200");
            map.put("msg", "创建成功");
            return map;


        } catch (Exception e) {
            map.put("state", "400");
            map.put("msg", "创建失败");
            return map;
        }


    }
    //修改班级信息(改名字)

    @RequestMapping(value = "/changeClass")
    public Map<String, String> changeClass(HttpServletRequest request, String name, String cid) {
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("cid", cid);
        Map<String, String> map = new HashMap<>();
        Users users = (Users) request.getSession().getAttribute("user");

        if (users.getFlag() != 0) {
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state", "401");
            map.put("msg", "用户状态不合法(疑似注销)");
            return map;

        }
        if (users.getType() != 1 || classteacherServic.getOne(queryWrapper).getUid() != users.getUid()) {
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state", "401");
            map.put("msg", "用户权限不够");
            return map;
        }

        try {

            Class _class = classService.getOne(queryWrapper);
            _class.setCname(name);

            if (classService.updateById(_class)) {


                map.put("state", "200");
                map.put("msg", "修改班级名字成功");
                return map;


            }
            map.put("state", "400");
            map.put("msg", "修改班级名字失败");
            return map;

        } catch (Exception e) {
            map.put("state", "400");
            map.put("msg", "修改班级名字失败");
            return map;

        }

        //
    }
    //修改班级信息(改名字)

    /**
     * @param:flag 选择显示列表类型 0显示全部  1 显示已经加入 2 显示待审核
     **/
    @RequestMapping(value = "/auditClass")
    public Map<String, String> auditClass(HttpServletRequest request, String flag, String cid) {
        Map<String, String> map = new HashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        Users users = (Users) request.getSession().getAttribute("user");
        queryWrapper.eq("cid", cid);
        if (users.getFlag() != 0) {
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state", "401");
            map.put("msg", "用户状态不合法(疑似注销)");
            return map;

        }
        if (users.getType() != 1 || classteacherServic.getOne(queryWrapper).getUid() != users.getUid()) {
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state", "401");
            map.put("msg", "用户权限不够");
            return map;
        }

        if (flag.equals("0")) {
            //显示全部该课程的学生
            List<Users> userlist = classstudentsService.list(queryWrapper);
            //消除用戶敏感信息 去掉密码等信息
            Iterator<Users> itr = userlist.iterator();

            while (itr.hasNext()) {
                itr.next().setPwd("");
//               itr.next().


            }


        }


        try {

            Class _class = classService.getOne(queryWrapper);
//            _class.setCname(name);

            if (classService.updateById(_class)) {


                map.put("state", "200");
                map.put("msg", "修改班级名字成功");
                return map;


            }
            map.put("state", "400");
            map.put("msg", "修改班级名字失败");
            return map;

        } catch (Exception e) {
            map.put("state", "400");
            map.put("msg", "修改班级名字失败");
            return map;

        }

    }

    //获取所有班级
    @RequestMapping(value = "/getAllClass")
    public Map<String, String> getAllClass(HttpServletRequest request, String name) {
        Map<String, String> map = new HashMap<>();
        List<ClassWithTeacherName> classes = classService.getAllclass();

        map.put("state", "200");
        map.put("msg", "ok");
        map.put("data", JSON.toJSONString(classes));
        return map;
    }

}
