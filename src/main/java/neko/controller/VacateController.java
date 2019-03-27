package neko.controller;


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

    //学生请假插入数据
    @RequestMapping(value = "/createVacate")
    public Map<String, String> getValidatecode(HttpServletRequest request, String vtype, String vreason) {
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
