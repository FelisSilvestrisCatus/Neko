package neko.controller;


import com.alibaba.fastjson.JSON;
import neko.entity.Users;
import neko.entity.Vacate;
import neko.service.IUsersService;
import neko.service.IVacateService;
import neko.service.IVacatefilesService;
import neko.utils.generalMap;
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
        Map<String, String> map = generalMap.getSuccessMap();
        Users users = (Users) session.getAttribute("user");

        Vacate vacate = new Vacate();
        vacate.setUid(users.getUid());
        vacate.setVtype(Integer.parseInt(vtype));
        vacate.setCourseid(Integer.parseInt(vcourse));
        vacate.setVname(vreason);
        if ((vdatetimeBegin.equals("undefined")) && (vdatetimeEnd.equals("undefined"))) {
            vacate.setVtime("未定时间");
        } else {
            if (vdatetimeBegin.equals("undefined"))
                vdatetimeBegin = "未定时间";
            if (vdatetimeEnd.equals("undefined"))
                vdatetimeEnd = "未定时间";
            vacate.setVtime(vdatetimeBegin + " - " + vdatetimeEnd);
        }


        System.out.println("vacate = " + vacate);

        if (vacateService.save(vacate)) {
            session.setAttribute("vid", vacate.getVid());
            map.put("vid", vacate.getVid().toString());
            map.put("msg", "请假申请已提交");
        } else {
            map = generalMap.getErrorMap();
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

        Map<String, String> map = generalMap.getSuccessMap();
        map.put("data", JSON.toJSONString(vacateService.getMyVacate(uid)));
        return map;
    }
}
