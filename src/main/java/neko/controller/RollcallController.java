package neko.controller;


import com.alibaba.fastjson.JSON;
import neko.utils.generalMethod;
import neko.utils.ip.Juhe;
import neko.utils.ip.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
@RequestMapping("/rollcall")
public class RollcallController {
    //修改班级信息
    @Autowired
    private LoginInfo loginInfo;
    @Autowired
    private Juhe juhe;

    @RequestMapping(value = "/getWeather")
    public Map<String, String> getIpCity(HttpServletRequest request) throws IOException {
        String ip = loginInfo.getIpAddr(request);
        Map<String, String> map = generalMethod.getSuccessMap();

        map.put("data", JSON.toJSONString(juhe.getWeather(ip)));

        return map;
    }

}
