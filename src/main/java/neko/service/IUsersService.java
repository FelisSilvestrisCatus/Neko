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

    void login(HttpServletRequest request, String phone, Map<String, String> map);
}
