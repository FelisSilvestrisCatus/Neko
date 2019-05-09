package neko.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Users;
import neko.entity.Userslogin;
import neko.mapper.UsersMapper;
import neko.service.IUsersService;
import neko.service.IUsersloginService;
import neko.utils.generalMethod;
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
    private static final long tokenExpire = 24 * 7;
    //Redis过期时间单位
    private static final TimeUnit tokenExpireTimeUnit = TimeUnit.HOURS;

    @Autowired
    private IUsersloginService usersloginService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginInfo loginInfo;
    @Autowired
    private Juhe juhe;

    //查询该手机号用户是否存在
    public boolean checkUser(String userphone) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", userphone);
        return count(queryWrapper) != 0 ? true : false;
    }


    //密码和验证码登录
    @Override
    public boolean login(HttpServletRequest request, String phone, Map<String, String> map, String logintype) {

        try {
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

            user.setPwd("");
            map.put("user", JSON.toJSONString(user));

            //保存本次登录信息
            Userslogin userslogin = new Userslogin();
            userslogin.setUid(user.getUid());
            userslogin.setLoginip(loginInfo.getIpAddr(request));
            userslogin.setLogintype(Integer.parseInt(logintype));
            userslogin.setLogintime(LocalDateTime.now());
            String area = loginInfo.getIpLocation(loginInfo.getIpAddr(request));
            if (area.equals("未知地址")) {
                //在淘宝api未得到数据或超时的情况下调用聚合api
                area = juhe.getValue(loginInfo.getIpAddr(request));
            }

            userslogin.setLoginlocation(area);
            return usersloginService.save(userslogin);
        } catch (Exception e) {
            map = generalMethod.getErrorMap();
            return false;
        }

    }

    @Override
    public boolean checkUserByIdnumber(String idnumber) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("idnumber", idnumber);
        return count(queryWrapper) != 0 ? true : false;

    }
}
