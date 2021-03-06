package neko.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.entity.Vacate;
import neko.entity.vo.AuditVacateByTeacher;
import neko.entity.vo.TeacherRollCall;
import neko.entity.vo.VacateWithTeacherName;
import neko.service.IUsersService;
import neko.service.IVacateService;
import neko.service.IVacatefilesService;
import neko.utils.generalMethod;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
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

        //请假时间检查
        if (checkVacateTime(vdatetimeBegin, vdatetimeEnd, users, vcourse)) return returnErrorTime();

        //拼接请假时间
        vacate.setVtime(vdatetimeBegin + " 至 " + vdatetimeEnd);

        //赋值请假信息
        vacate.setUid(users.getUid()).setVtype(Integer.parseInt(vtype))
                .setCourseid(Integer.parseInt(vcourse)).setVname(vreason);

        if (vacateService.save(vacate)) {
            session.setAttribute("vid", vacate.getVid());
            map.put("vid", vacate.getVid().toString());
            map.put("msg", "请假申请已提交");
        } else {
            map = generalMethod.getErrorMap();
        }
        return map;
    }

    //请假时间检查
    private boolean checkVacateTime(String vdatetimeBegin, String vdatetimeEnd, Users users, String vcourse) {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long vBegin;
        long vEnd;
        try {
            vBegin = sdf.parse(vdatetimeBegin).getTime();
            vEnd = sdf.parse(vdatetimeEnd).getTime();
        } catch (ParseException e) {
            return true;
        }

        List<VacateWithTeacherName> allVacate = vacateService.getMyVacateByCourse(users.getUid(), vcourse);
        for (VacateWithTeacherName vacateWithTeacherName : allVacate) {
            String[] timestr = vacateWithTeacherName.getVtime().split("至");
            try {
                long begin = sdf.parse(timestr[0]).getTime();
                long end = sdf.parse(timestr[1]).getTime();
                if ((begin <= vBegin && vBegin <= end) || (begin <= vEnd && vEnd <= end)) {
                    return true;
                }
            } catch (ParseException e) {
                return true;
            }
        }
        return false;
    }

    //错误的时间段
    private Map<String, String> returnErrorTime() {
        Map<String, String> map;
        map = generalMethod.getErrorMap();
        map.put("msg", "请假时间范围错误，请检查是否有冲突的时间段");
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

    //学生查看请假信息
    @RequestMapping(value = "/getDetails")
    public Map<String, String> getDetails(HttpSession session, String vid) {
        int uid = ((Users) session.getAttribute("user")).getUid();
        return vacateService.getDetails(Integer.parseInt(vid), uid);
    }

    //老师查看请假信息
    @RequestMapping(value = "/getDetailsByTeacher")
    public Map<String, String> getDetailsByTeacher(String vid) {
        return vacateService.getDetailsByTeacher(Integer.parseInt(vid));
    }

    //学生修改请假申请
    @RequestMapping(value = "/alterVacate")
    public Map<String, String> alterVacate(HttpSession session, String vid, String vreason, String vdatetimeBegin,
                                           String vdatetimeEnd) {

        Users users = (Users) session.getAttribute("user");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("vid", Integer.parseInt(vid));
        Vacate vacate = vacateService.getOne(queryWrapper);

        //请假时间检查
        if (checkVacateTime(vdatetimeBegin, vdatetimeEnd, users, vacate.getCourseid().toString()))
            return returnErrorTime();


        Map<String, String> map = generalMethod.getSuccessMap();
        if (vacateService.updateById(vacate)) {
            map.put("msg", "请假申请已修改");
        } else {
            map = generalMethod.getErrorMap();
            map.put("msg", "请假申请修改失败");
        }
        return map;
    }

    //请假审批
    @RequestMapping(value = "/auditVacate")
    public Map<String, String> auditVacate(HttpSession session, Integer vid, Integer state, String remark) {
        Users u = (Users) session.getAttribute("user");
        return vacateService.auditVacate(vid, state, remark);
    }

    //老师查看学生请假时附件的详细信息（弹框形式 显示请假类型 以及 附件下载）
    @RequestMapping(value = "/getVacateFile")
    public Map<String, String> getVacateDetail(String vid) {
        return vacatefilesService.getVacateFile(Integer.valueOf(vid));
    }

    //根据状态筛选请假申请
    @RequestMapping(value = "/VacateList")
    public Map<String, String> VacateList(HttpSession session, String state) {
        Users users = (Users) session.getAttribute("user");
        int uid = users.getUid();
        int stateint = Integer.valueOf(state);
        Map<String, String> map = generalMethod.getSuccessMap();
        List<AuditVacateByTeacher> list = vacateService.VacateList(uid, stateint);
        map.put("data", JSON.toJSONString(list));
        return map;
    }

    /*
     * 下载附件
     * */
    @RequestMapping(value = "/getFile", method = RequestMethod.POST)
    public Map<String, String> getFile(HttpServletResponse res, String vfid) {
        Map<String, String> map = generalMethod.getSuccessMap();
        try {
            vacatefilesService.getFile(vfid, res);
        } catch (Exception e) {
            map = generalMethod.getErrorMap();
        }

        return map;
    }

    /*
     * 下载附件
     * */
    @RequestMapping(value = "/getFiles", method = RequestMethod.POST)
    public Map<String, String> getFiles(HttpServletResponse res, String vid) {
        Map<String, String> map = generalMethod.getSuccessMap();
        try {
            vacatefilesService.getFiles(vid, res);
        } catch (Exception e) {
            map = generalMethod.getErrorMap();
        }

        return map;
    }

    //根据courseid获取请假的学生
    @RequiresPermissions("teacher")
    @RequestMapping(value = "/getCourseStudentWhoVacate")
    public Map<String, String> getCourseStudentWhoVacate(HttpSession session, String courseid) {

        int courseid_ = Integer.valueOf(courseid);
        Map<String, String> map = generalMethod.getSuccessMap();
        List<TeacherRollCall> list = vacateService.getCourseStudentWhoVacate(courseid_);
        map.put("data", JSON.toJSONString(list));
        return map;
    }


}
