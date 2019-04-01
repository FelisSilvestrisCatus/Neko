package neko.controller;


import neko.entity.Users;
import neko.entity.Vacate;
import neko.entity.Vacatefiles;
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
import java.io.File;
import java.io.IOException;
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
    public Map<String, String> createVacate(HttpSession session, String vtype, String vreason, String vdatetime,
                                            String vcourse, String vtime) {
        Map<String, String> map = generalMap.getSuccessMap();
        Users users = (Users) session.getAttribute("user");

        Vacate vacate = new Vacate();
        vacate.setUid(users.getUid());
        vacate.setVtype(Integer.parseInt(vtype));
        vacate.setCourseid(Integer.parseInt(vcourse));
        vacate.setRemark(vreason);
        vacate.setVtime(vdatetime);
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

        //创建存储文件夹
        String dirpath = "c:\\vfiles\\" + Integer.parseInt(id);
        if (new File(dirpath).mkdirs()) {
            for (MultipartFile mf : vfile) {
                //文件路径
                String filePath = dirpath + "\\" + mf.getName();
                try {
                    //保存文件
                    mf.transferTo(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    map = generalMap.getErrorMap();
                    map.put("msg", "无法存储附件");
                }

                Vacatefiles vacatefiles = new Vacatefiles();
                vacatefiles.setVid(Integer.parseInt(id));
                vacatefiles.setFilepath(filePath);
                vacatefilesService.save(vacatefiles);
            }

            map.put("msg", "请假申请已提交");
        } else {
            map = generalMap.getErrorMap();
            map.put("msg", "无法存储附件");
        }

        return map;
    }
}
