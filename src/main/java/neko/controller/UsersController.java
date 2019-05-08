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

        if (password.equals(user.getPwd()) && usersService.login(request, phone, map, loginType)) {
            return map;
        } else {
            map.put("msg", "用户名或密码错误");
            map.put("token", "");
            return map;
        }
    }


    //用户注册
    @RequestMapping(value = "/registe")
    public Map<String, String> registe(String idnumber, String username, String userphone, String validatecode) {
        Map<String, String> map = new HashMap<>();

        //检查用户是否已存在
        if (usersService.checkUser(userphone)) {
            map.put("state", "400");
            map.put("msg", "用户已存在");
            return map;
        }

        //手机号有记录且获取验证码匹配
        if (redisUtil.hasKey(userphone + "code") && redisUtil.get(userphone + "code").equalsIgnoreCase(validatecode)) {
            //插入数据库
            Users user = new Users();
            user.setPhone(userphone);
            user.setUname(username);
            user.setType(2);
            user.setIdnumber(Integer.parseInt(idnumber));
            usersService.save(user);
            map.put("state", "200");
            map.put("msg", "注册成功");
            //删除验证码
            redisUtil.delete(userphone + "code");
        } else {
            map.put("state", "400");
            map.put("msg", "验证码错误");
        }
        return map;
    }

    //修改用户信息
    @RequestMapping(value = "/changeInfo")
    public Map<String, String> changeInfo(HttpServletRequest request, String email, String password) {

        Map<String, String> map = new HashMap<>();

        Users users = (Users) request.getSession().getAttribute("user");
        Users u = usersService.getById(users.getUid());
        if (!StringUtils.isBlank(email))
            u.setEmail(email);
        if (!StringUtils.isBlank(password)) {
            u.setPwd(password);
        }
        if (usersService.updateById(u)) {
            map.put("state", "200");
        } else {
            map.put("state", "400");
        }
        return map;
    }

    //修改用户信息
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


    //上传自己的照片  用来点名
    @RequestMapping(value = "/uploadPhotoForRollCall")
    public Map<String, String> uploadPhotoForRollCall(HttpSession session, String imgCode) throws IOException {
        Map<String, String> map = generalMethod.getSuccessMap();
        Users users = (Users) session.getAttribute("user");
        String uid = users.getUid() + "";
        //告诉学生 有没有拍到脸  拍到了几个脸

        String temppath = Face.base64StrToImage(imgCode, uid);//用来保存临时
        JSONObject jsonObject = nekoFace.facedetection(temppath, uid, "0");
        String face_num = jsonObject.getString("flag");
        int photo_train = Face.getPhotoNum(uid);        //已经有了几个脸
        if (face_num.equals("0")) {
            map.put("msg", "未检测到人脸，请重试");
        } else {
            map.put("msg", "有效图片");
        }
        //有效图片数量
        map.put("successnum", photo_train + "");

        return map;
    }
}