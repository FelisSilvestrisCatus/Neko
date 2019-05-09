package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Users;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-01-14
 */
public interface IUsersService extends IService<Users> {
    //查询该手机号用户是否存在
    public boolean checkUser(String userphone);

    //密码和验证码登录
    boolean login(HttpServletRequest request, String phone, Map<String, String> map, String logintype);
    //查询该idnumber号是否存在
    public boolean checkUserByIdnumber (String idnumber);

}
