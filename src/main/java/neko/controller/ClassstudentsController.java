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
import neko.utils.generalMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
