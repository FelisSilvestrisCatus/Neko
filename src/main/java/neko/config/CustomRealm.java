package neko.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsersService;
import neko.utils.token.PwdUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/*
 * 用于shiro的权限认证实现
 * */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private IUsersService usersService;

    /**
     * 获取身份验证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        // 获取用户输入的用户名和密码
        String phone = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        // 通过用户名到数据库查询用户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", phone);
        Users user = usersService.getOne(queryWrapper);

        if (user == null) {
            throw new UnknownAccountException();
        }
        boolean pwd = false;
        pwd = PwdUtil.PwdMd5(password).equals(user.getPwd());
        System.out.println("PwdUtil.PwdMd5(password) = " + PwdUtil.PwdMd5(password));
        if (!pwd) {
            throw new IncorrectCredentialsException();
        }
        if (user.getFlag().equals(1)) {
            throw new LockedAccountException();
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    /**
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Users user = (Users) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String role = user.getType().equals(1) ? "teacher" : "student";

        Set<String> set = new HashSet<>();
        set.add(role);

        //设置该用户拥有的角色
        info.setStringPermissions(set);
        return info;
    }
}