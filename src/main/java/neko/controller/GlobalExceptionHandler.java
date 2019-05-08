package neko.controller;


import neko.utils.generalMethod;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

/*
 * shiro的全局异常捕获处理
 * */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public Map<String, String> handleShiroException() {
        Map<String, String> map = get401Map();
        map.put("msg", "鉴权或授权过程出错");
        return map;
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public Map<String, String> UnauthenticatedException() {
        Map<String, String> map = get401Map();
        map.put("msg", "认证失败");
        return map;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Map<String, String> UnauthorizedException() {
        Map<String, String> map = get401Map();
        map.put("msg", "授权失败");
        return map;
    }

    @ExceptionHandler(LockedAccountException.class)
    @ResponseBody
    public Map<String, String> LockedAccountException() {
        Map<String, String> map = generalMethod.getErrorMap();
        map.put("msg", "该账号已被禁用");
        return map;
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    @ResponseBody
    public Map<String, String> IncorrectCredentialsException() {
        Map<String, String> map = generalMethod.getErrorMap();
        map.put("msg", "用户名或密码错误");
        return map;
    }

    @ExceptionHandler(UnknownAccountException.class)
    @ResponseBody
    public Map<String, String> UnknownAccountException() {
        Map<String, String> map = generalMethod.getErrorMap();
        map.put("msg", "未注册的用户");
        return map;
    }


    private Map<String, String> get401Map() {
        Map<String, String> map = generalMethod.getErrorMap();
        map.put("state", "401");
        return map;
    }
}