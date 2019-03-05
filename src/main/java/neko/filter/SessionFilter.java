//package neko.filter;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import neko.entity.Users;
//import neko.service.IUsersService;
//import neko.utils.redis.RedisUtil;
//import neko.utils.token.Token;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Objects;
//
//@WebFilter(filterName = "sessionFilter", urlPatterns = {"/*"})
//public class SessionFilter implements Filter {
//
//    @Autowired
//    private IUsersService usersService;
//    @Autowired
//    private RedisUtil redisUtil;
//
//    //不需要登录就可以访问的路径
//    String[] includeUrls = new String[]{"/users/login", "/favicon.ico","/usersLogin/getLast"};
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpSession session = request.getSession(false);
//        String uri = request.getRequestURI();
//
//        boolean needFilter = isNeedFilter(uri);
//        String token = "";
//        token = request.getHeader("Authorization");
//
//        System.out.println("filter url:" + uri);
//        System.out.println(token);
//        //是否需要过滤
//
//        if (!needFilter) { //不需要过滤直接传给下一个过滤器
//            System.out.println("no filter");
//            filterChain.doFilter(servletRequest, servletResponse);
//        } else {
//            //需要过滤器WA
//
//
//            if (null == token || Objects.equals(token, "")) {
//                //无token信息,销毁session
//                try {
//                    session.invalidate();
//                } catch (Exception ignored) {
//
//                }
//                response.setStatus(401);
//                response.getWriter().write("error filter");
//                return;
//            }
//
//            //检查token是否存在于redis中
//            boolean isinredis = false;
//            isinredis = redisUtil.hasKey(token);
//            if (!isinredis) {
//                response.setStatus(401);
//                response.getWriter().write("认证失败");
//            }
//
//            // session中包含user对象,则是登录状态
//            if (session != null && session.getAttribute("user") != null) {
//                // System.out.println("user:"+session.getAttribute("user"));
//                filterChain.doFilter(request, response);
//            } else {
//                try {
//                    //存在token但不存在session
//
//                    Map<String, String> info = Token.parseJwtToken(token);
//                    QueryWrapper queryWrapper = new QueryWrapper();
//                    queryWrapper.eq("phone", info.get("Phone"));
//                    Users user = usersService.getOne(queryWrapper);
//                    //重新赋值session
//                    session.setAttribute("user", user);
//                    filterChain.doFilter(request, response);
//                } catch (Exception e) {
//                    //未登陆或登录失效
//                    response.setStatus(401);
//                    response.getWriter().write("认证失败");
//                }
//            }
//        }
//    }
//
//    public boolean isNeedFilter(String uri) {
//
//        //排除druid监控
//        if (uri.contains("druid")) {
//            return false;
//        }
//
//        for (String includeUrl : includeUrls) {
//            if (includeUrl.equals(uri)) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
