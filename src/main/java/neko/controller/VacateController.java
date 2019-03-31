package neko.controller;


import neko.entity.Users;
import neko.entity.Vacate;
import neko.entity.Vacatefiles;
import neko.service.IUsersService;
import neko.service.IVacateService;
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

    //学生请假
    @RequestMapping(value = "/createVacate")
    public Map<String, String> createVacate(HttpSession session, String vtype, String vreason,
                                            String vcourse, String vtime) {
        Map<String, String> map = generalMap.getSuccessMap();
        Users users = (Users) session.getAttribute("user");

        Vacate vacate = new Vacate();
        vacate.setUid(users.getUid());
        vacate.setVtype(Integer.parseInt(vtype));
        vacate.setCourseid(Integer.parseInt(vcourse));
        vacate.setRemark(vreason);
        vacate.setVtime(vtime);

        if (vacateService.save(vacate)) {
            session.setAttribute("vid", vacate.getVid());
            map.put("vid", vacate.getVid().toString());
            map.put("msg", "请假申请已提交");
        } else {
            map = generalMap.getErrorMap();
        }

        return map;
    }

    //学生请假
    @RequestMapping(value = "/createVacateFile")
    public Map<String, String> createVacateFile(HttpSession session, String id,
                                                @RequestParam(value = "files", required = false) MultipartFile[] vfile) {
        Map<String, String> map = generalMap.getSuccessMap();

        int vid = Integer.parseInt(id);

        System.out.println("file = " + vfile);
        System.out.println("file,length = " + vfile.length);
        for (MultipartFile mf : vfile) {
            System.out.println("mf = " + mf);
            if (!mf.isEmpty()) {
                System.out.println("mf = " + mf.getName());

                String path = "";


                Vacatefiles vacatefiles = new Vacatefiles();
                vacatefiles.setVid(vid);
                vacatefiles.setFilepath(path);
            }
        }

        map.put("msg", "请假申请已提交");
        return map;
    }
}
