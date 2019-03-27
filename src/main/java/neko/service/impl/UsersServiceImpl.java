package neko.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Users;
import neko.entity.Userslogin;
import neko.mapper.UsersMapper;
import neko.service.IUsersService;
import neko.service.IUsersloginService;
import neko.utils.ip.Juhe;
import neko.utils.ip.LoginInfo;
import neko.utils.redis.RedisUtil;
import neko.utils.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-01-14
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    //Redis过期时间
    private static final long tokenExpire = 7;
    //Redis过期时间单位
    private static final TimeUnit tokenExpireTimeUnit = TimeUnit.MINUTES;

    @Autowired
    private IUsersloginService usersloginService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginInfo loginInfo;
    @Autowired
    private Juhe juhe;

    //密码和验证码登录
    @Override
    public void login(HttpServletRequest request, String phone, Map<String, String> map) {
        //查询用户
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        Users user = getOne(queryWrapper);

        request.getSession().setAttribute("user", user);

        String token = Token.getJwtToken(user);
        redisUtil.set(user.getUid().toString(), token);
        redisUtil.expire(user.getUid().toString(), tokenExpire, tokenExpireTimeUnit);

        map.put("state", "200");
        map.put("msg", "ok");
        map.put("token", token);

        //保存本次登录信息
        Userslogin userslogin = new Userslogin();
        userslogin.setUid(user.getUid());
        userslogin.setLoginip(loginInfo.getIpAddr(request));
        userslogin.setLogintype(1);
        userslogin.setLogintime(LocalDateTime.now());
        String area = loginInfo.getIpLocation(loginInfo.getIpAddr(request));
        if (area.equals("未知地址")) {
            //在淘宝api未得到数据或超时的情况下调用聚合api
            area = juhe.getValue(loginInfo.getIpAddr(request));
        }
        userslogin.setLoginlocation(area);
        usersloginService.save(userslogin);

        user.setPwd("");
        map.put("user", JSON.toJSONString(user));
    }
}
