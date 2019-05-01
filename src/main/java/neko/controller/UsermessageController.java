package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.vo.UsersMsg;
import neko.service.IUsermessageService;
import neko.utils.generalMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author z9961
 * @since 2019-04-20
 */
@RestController
@RequestMapping("/usermessage")
public class UsermessageController {

    @Autowired
    private IUsermessageService usermessage;

    @RequestMapping(value = "/getUnReadNum")
    public Map<String, String> getUnReadNum(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        Map<String, String> map = generalMethod.getSuccessMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", u.getUid());
        Integer count = usermessage.count(queryWrapper);
        map.put("data", count.toString());
        return map;
    }

    @RequestMapping(value = "/getMsg")
    public Map<String, String> getMsg(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        Map<String, String> map = generalMethod.getSuccessMap();
        List<UsersMsg> msglist = usermessage.getMsg(u.getUid());
        map.put("data", JSON.toJSONString(msglist));
        return map;
    }
}
