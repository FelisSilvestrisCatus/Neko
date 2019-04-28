package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import neko.entity.Users;
import neko.entity.vo.StudentRollcall;
import neko.service.IRollcalldetailsService;
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
 * @since 2019-03-03
 */
@RestController
@RequestMapping("/rollcalldetails")
public class RollcalldetailsController {

    @Autowired
    private IRollcalldetailsService rs;

    //获取我的信息
    @RequestMapping(value = "/myInfo")
    public Map<String, String> myInfo(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        return rs.myInfo(u);
    }

    //我的出勤记录
    @RequestMapping(value = "/myAttendance")
    public Map<String, String> myAttendance(HttpSession session, Integer currentPage) {
        Map<String, String> map = generalMethod.getSuccessMap();
        Users u = (Users) session.getAttribute("user");

        System.out.println("page = " + currentPage);

        if (currentPage == null || currentPage.equals(0)) {
            currentPage = 1;
        }

        IPage<StudentRollcall> list = rs.myAttendance(u.getUid(), currentPage);

        map.put("data", JSON.toJSONString(list.getRecords()));
        map.put("total", list.getTotal() + "");
        return map;
    }

    //我的出勤记录(时间查询)
    @RequestMapping(value = "/myAttendanceByTime")
    public Map<String, String> myAttendanceByTime(HttpSession session, String vdatetimeBegin,
                                                  String vdatetimeEnd) {
        Map<String, String> map = generalMethod.getSuccessMap();
        Users u = (Users) session.getAttribute("user");
        if ((vdatetimeBegin.equals("undefined")) || (vdatetimeEnd.equals("undefined"))) {
//            rs.myAttendance(1);
        } else {
//            rs.myAttendanceByTime(vdatetimeBegin, vdatetimeEnd);
        }

        return rs.myInfo(u);
    }

}
