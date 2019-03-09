package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Vacate;
import neko.service.IUsersService;
import neko.service.IVacateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 请假表 前端控制器
 * </p>
 *
 * @author z9961
 * @param :uid 学生id vtype  请假类型 vreason 请假原因
 * @return：map
 * @since 2019-03-03
 */


@RestController
@RequestMapping("/vacate")
public class VacateController {
    @Autowired
    private IUsersService userslService;
    @Autowired
    private IVacateService vacateService;
    //学生请假插入数据
    @RequestMapping(value = "/createVacate")
    public Map<String, String> getValidatecode(HttpServletRequest request,String  vtype, String vreason) {
        int vacatetype=Integer.valueOf(vtype);
        Map<String, String> map = new HashMap<>();
        //检查用户是否存在：1 状态是否正常（ 用户是否存在，或者被注销）
        QueryWrapper queryWrapper = new QueryWrapper();
        Users users = (Users) request.getSession().getAttribute("user");
        queryWrapper.eq("uid",users.getUid());
        queryWrapper.eq("flag",0);
        queryWrapper.eq("type",2);
        Users user=userslService.getOne(queryWrapper);
        if(null==user){
          //用户状态不正常
          map.put("state","401");
          map.put("msg","用户信息不合法");
        }else{
            Vacate vacate=new Vacate();
            vacate.setUid(users.getUid());
            vacate.setVtype(vacatetype);
           // vacate.set
          //  vacateService.save()

        }


        return map;
    }

}
