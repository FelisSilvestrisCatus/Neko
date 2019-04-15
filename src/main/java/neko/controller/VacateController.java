package neko.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Vacate;
import neko.service.IUsersService;
import neko.service.IVacateService;
import neko.service.IVacatefilesService;
import neko.utils.generalMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 请假
 */

@RestController
@RequestMapping("/vacate")
public class VacateController {
    @Autowired
    private IUsersService userslService;
    @Autowired
    private IVacateService vacateService;
    @Autowired
    private IVacatefilesService vacatefilesService;


    //学生请假
    @RequestMapping(value = "/createVacate")
    public Map<String, String> createVacate(HttpSession session, String vtype, String vreason, String vdatetimeBegin,
                                            String vdatetimeEnd, String vcourse) {
        Map<String, String> map = generalMethod.getSuccessMap();
        Users users = (Users) session.getAttribute("user");

        Vacate vacate = new Vacate();
        vacate.setUid(users.getUid());
        vacate.setVtype(Integer.parseInt(vtype));
        vacate.setCourseid(Integer.parseInt(vcourse));
        handleTime(vreason, vdatetimeBegin, vdatetimeEnd, vacate);

        if (vacateService.save(vacate)) {
            session.setAttribute("vid", vacate.getVid());
            map.put("vid", vacate.getVid().toString());
            map.put("msg", "请假申请已提交");
        } else {
            map = generalMethod.getErrorMap();
        }
        return map;
    }

    //学生请假附件处理
    @RequestMapping(value = "/createVacateFile")
    public Map<String, String> createVacateFile(HttpSession session, String id,
                                                @RequestParam(value = "files", required = false) MultipartFile[] vfile) {
        return vacatefilesService.createVacateFile(id, vfile);
    }

    //学生请假列表
    @RequestMapping(value = "/myVacate")
    public Map<String, String> myVacate(HttpSession session, String id,
                                        @RequestParam(value = "files", required = false) MultipartFile[] vfile) {

        int uid = ((Users) session.getAttribute("user")).getUid();

        Map<String, String> map = generalMethod.getSuccessMap();
        map.put("data", JSON.toJSONString(vacateService.getMyVacate(uid)));
        return map;
    }

    //学生取消请假
    @RequestMapping(value = "/cancelVacate")
    public Map<String, String> cancelVacate(HttpSession session, String vid) {
        int uid = ((Users) session.getAttribute("user")).getUid();
        int vidint = Integer.parseInt(vid);
        return vacateService.cancelVacate(vidint, uid);
    }

    //学生取消请假
    @RequestMapping(value = "/getDetails")
    public Map<String, String> getDetails(HttpSession session, String vid) {
        int uid = ((Users) session.getAttribute("user")).getUid();
        System.out.println("vid = " + vid);
        int vidint = Integer.parseInt(vid);
        return vacateService.getDetails(vidint, uid);
    }


    //学生修改请假申请
    @RequestMapping(value = "/alterVacate")
    public Map<String, String> alterVacate(HttpSession session, String vid, String vreason, String vdatetimeBegin,
                                           String vdatetimeEnd) {

        Users users = (Users) session.getAttribute("user");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("vid", Integer.parseInt(vid));
        Vacate vacate = vacateService.getOne(queryWrapper);
        handleTime(vreason, vdatetimeBegin, vdatetimeEnd, vacate);

        Map<String, String> map = generalMethod.getSuccessMap();
        if (vacateService.saveOrUpdate(vacate)) {
            map.put("msg", "请假申请已修改");
        } else {
            map = generalMethod.getErrorMap();
            map.put("msg", "请假申请修改失败");
        }
        return map;
    }

    //处理时间格式和请假原因
    private void handleTime(String vreason, String vdatetimeBegin, String vdatetimeEnd, Vacate vacate) {
        vacate.setVname(vreason);
        if ((vdatetimeBegin.equals("undefined")) && (vdatetimeEnd.equals("undefined"))) {
            vacate.setVtime("未定时间");
        } else {
            if (vdatetimeBegin.equals("undefined"))
                vdatetimeBegin = "未定时间";
            if (vdatetimeEnd.equals("undefined"))
                vdatetimeEnd = "未定时间";
            vacate.setVtime(vdatetimeBegin + " 至 " + vdatetimeEnd);
        }
    }


}
