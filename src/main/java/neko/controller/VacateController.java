package neko.controller;


import neko.entity.Users;
import neko.entity.Vacate;
import neko.service.IUsersService;
import neko.service.IVacateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    public Map<String, String> getValidatecode(HttpServletRequest request,
                                               String vtype,
                                               String vreason,
                                               @RequestParam(value = "files", required = false) MultipartFile[] vfile) {
        System.out.println("file = " + vfile);
        for (MultipartFile mf : vfile) {
            if (!mf.isEmpty()) {

                System.out.println("mf = " + mf.getName());
            }
        }
        System.out.println("vtype = " + vtype);
        System.out.println("vreason = " + vreason);



        int vacatetype = Integer.valueOf(vtype);

        Map<String, String> map = new HashMap<>();

        Users users = (Users) request.getSession().getAttribute("user");

        Vacate vacate = new Vacate();
        vacate.setUid(users.getUid());
        vacate.setVtype(vacatetype);
        // vacate.set
        //  vacateService.save()


        return map;
    }

}
