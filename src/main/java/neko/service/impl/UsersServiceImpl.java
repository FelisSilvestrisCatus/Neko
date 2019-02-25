package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Users;
import neko.mapper.UsersMapper;
import neko.service.IUsersService;
import org.springframework.stereotype.Service;

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

}
