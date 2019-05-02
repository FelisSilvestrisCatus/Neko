package neko.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsermessageService;
import neko.utils.generalMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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

    //获取未读消息数量
    @RequestMapping(value = "/getUnReadNum")
    public Map<String, String> getUnReadNum(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        Map<String, String> map = generalMethod.getSuccessMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", u.getUid());
        queryWrapper.eq("mstate", 0);
        Integer count = usermessage.count(queryWrapper);
        map.put("data", count.toString());
        return map;
    }

    @RequestMapping(value = "/getMsg")
    public Map<String, String> getMsg(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        return usermessage.getMsg(u.getUid());
    }

    @RequestMapping(value = "/setMsgRead")
    public Map<String, String> setMsgRead(HttpSession session, int umid) {
        Users u = (Users) session.getAttribute("user");
        return usermessage.setMsgRead(u.getUid(), umid);
    }

    @RequestMapping(value = "/setAllMsgRead")
    public Map<String, String> setAllMsgRead(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        return usermessage.setAllMsgRead(u.getUid());
    }
}
