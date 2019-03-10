package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.*;
import neko.entity.Class;
import neko.service.IClassService;
import neko.service.IClassteacherService;
import neko.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

        try {
            QueryWrapper queryWrapper = new QueryWrapper();

            queryWrapper.eq("cid", cid);
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
}
