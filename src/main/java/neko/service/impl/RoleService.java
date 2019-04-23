package neko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private IUsersService usersService;

    /**
     * 模拟根据用户id查询返回用户的所有角色，实际查询语句参考：
     * SELECT r.rval FROM role r, user_role ur
     * WHERE r.rid = ur.role_id AND ur.user_id = #{userId}
     *
     * @param uid
     * @return
     */
    public Set<String> getRolesByUserId(Integer uid) {
        Set<String> roles = new HashSet<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid", uid);
        Users user = usersService.getOne(queryWrapper);
        roles.add("student");
        roles.add("teacher");
        return roles;
    }

}