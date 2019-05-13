package neko.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsersService;
import neko.utils.face.Face;
import neko.utils.face.NekoFace;
import neko.utils.generalMethod;
import neko.utils.redis.RedisUtil;
import neko.utils.token.PwdUtil;
import neko.utils.token.Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户操作
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private IUsersService usersService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private NekoFace nekoFace;

    //用户登录
    @RequestMapping(value = "/login")
    public Map<String, String> login(HttpServletRequest request, String phone, String password, String loginType) {

        Map<String, String> map = generalMethod.getErrorMap();

        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
        // 执行认证登陆
        subject.login(token);
        //查询用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        Users user = usersService.getOne(queryWrapper);

        if (PwdUtil.PwdMd5(password).equals(user.getPwd()) && usersService.login(request, phone, map, loginType)) {
            return map;
        } else {
            map.put("msg", "用户名或密码错误");
            map.put("token", "");
            return map;
        }
    }

    //修改用户信息
    @RequestMapping(value = "/changeInfo")
    public Map<String, String> changeInfo(HttpSession session, String email, String password) {
        Users user = (Users) session.getAttribute("user");
        redisUtil.delete(user.getUid().toString());
        Map<String, String> map = new HashMap<>();

        Users u = usersService.getById(user.getUid());
        if (!StringUtils.isBlank(email) && (!"undefined".equals(email)))
            u.setEmail(email);
        if (!StringUtils.isBlank(password)) {
            u.setPwd(PwdUtil.PwdMd5(password));
        }
        if (usersService.updateById(u)) {
            map.put("state", "200");
        } else {
            map.put("state", "400");
        }
        return map;
    }

    //获取用户信息
    @RequestMapping(value = "/getInfo")
    public Map<String, String> getInfo(HttpServletRequest request) {
        //获取token
        String token = request.getHeader("Authorization");
        Map<String, String> info = Token.parseJwtToken(token);
        //查询用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", info.get("Phone"));
        Users user = usersService.getOne(queryWrapper);
        //清除密码
        user.setPwd("");
        //返回数据
        Map<String, String> map = new HashMap<>();
        map.put("state", "200");
        map.put("user", JSON.toJSONString(user));
        return map;
    }

    /*
     * 401
     * */
    @RequestMapping(value = "/401")
    public void shirofalied(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setCharacterEncoding("UTF-8");
//        response.setStatus(401);
        response.getWriter().write("未授权的访问");
    }

    //获取自己已经上传的图片数目
    @RequestMapping(value = "/getAlreadyUploadImgNum")
    public Map<String, String> getAlreadyUploadImgNum(HttpSession session) throws IOException {
        Map<String, String> map = generalMethod.getSuccessMap();
        Users users = (Users) session.getAttribute("user");
        String uid = users.getUid() + "";
        int num = Face.getPhotoNum(uid);
        map.put("num", num + "");

        return map;
    }

    //上传自己的照片  用来点名
    @RequestMapping(value = "/uploadPhotoForRollCall")
    public Map<String, String> uploadPhotoForRollCall(HttpSession session, String imgCode) throws IOException {
        Map<String, String> map = generalMethod.getSuccessMap();
        Users users = (Users) session.getAttribute("user");
        System.out.println("用户" + users);
        String uid = users.getUid() + "";
        //告诉学生 有没有拍到脸  拍到了几个脸
        System.out.println("imgCode = " + imgCode);
        String temppath = Face.base64StrToImage(imgCode, uid);//用来保存临时图片
        JSONObject jsonObject = nekoFace.facedetection(temppath, uid, "0");
        String face_num = jsonObject.getString("flag");

        if (face_num.equals("0")) {
            map.put("state", "400");
            map.put("msg", "未检测到人脸，请重试");
        } else {
            map.put("msg", "有效图片");
        }


        int photo_train = Face.getPhotoNum(uid);        //已经有了几张有效图片
        //有效图片数量
        map.put("successnum", photo_train + "");
        //如果够十张便进行训练

//        if (photo_train >= 10) {
//            nekoFace.startTrain();
//        }
        return map;
    }

    //上传自己的照片  用来点名
    @RequestMapping(value = "/startTrain")
    public Map<String, String> startTrain() {
        nekoFace.startTrain();
        return generalMethod.getSuccessMap();
    }

}