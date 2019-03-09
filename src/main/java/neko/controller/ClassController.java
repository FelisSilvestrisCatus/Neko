package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Class;
import neko.entity.Users;
import neko.entity.Vacate;
import neko.service.IClassService;
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
    @RequestMapping(value = "/createClass")
    public Map<String, String> getValidatecode(HttpServletRequest request, String name) {

        Map<String, String> map = new HashMap<>();
        Users users = (Users) request.getSession().getAttribute("user");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",users.getUid());
        queryWrapper.eq("flag",0);
        queryWrapper.eq("type",1);
        Users user =userslService.getOne(queryWrapper);
        if(null==users||null==user){
            //用户状态不合法 ： 或权限不够 或状态不正常
            map.put("state","401");
            map.put("msg","用户状态不合法 ： 或权限不够 或状态不正常");

        }
        else{
             //创建班级表
            Class  newclass=new Class();
            newclass.setCname(name);
            classService.save(newclass);
            map.put("state","200");
            map.put("msg","创建成功");
        }
        return map;
    }

}
