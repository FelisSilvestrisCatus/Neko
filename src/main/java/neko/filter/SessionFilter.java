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
//    String[] includeUrls = new String[]{"/users/login", "/favicon.ico", "/alive", "/"};
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
//        if ("OPTIONS".equals(request.getMethod())) {
//            //不需要过滤
//            System.out.println("OPTIONS no filter");
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
//
//        boolean needFilter = isNeedFilter(uri);
//        String token = "";
//        token = request.getHeader("Authorization");
//
//        System.out.println("filter url:" + uri);
//        System.out.println(token);
//        //是否需要过滤
//
//        if (!needFilter) {
//            //不需要过滤
//            System.out.println("no filter");
//            filterChain.doFilter(servletRequest, servletResponse);
//        } else {
//            checkState(filterChain, request, response, session, token);
//        }
//    }
//
//    private void checkState(FilterChain filterChain, HttpServletRequest request,
//                            HttpServletResponse response, HttpSession session, String token)
//            throws IOException, ServletException {
//        if (null == token || Objects.equals(token, "")) {
//            //无token信息,销毁session
//            try {
//                session.invalidate();
//            } catch (Exception ignored) {
//            }
//            filed(response, request);
//            return;
//        }
//
//        Map<String, String> info = Token.parseJwtToken(token);
//
//        if (!isInRedis(response, request, token, info)) {
//            return;
//        }
//
//        // session中包含user对象,则是登录状态
//        if ((session != null) && (session.getAttribute("user") != null)) {
//            Users sessionUsers = (Users) session.getAttribute("user");
//            if (sessionUsers.getFlag() == 0) {
//                filterChain.doFilter(request, response);
//            } else {
//                filed(response, request);
//            }
//        } else {
//            try {
//                //存在token但不存在session
//                QueryWrapper queryWrapper = new QueryWrapper();
//                queryWrapper.eq("phone", info.get("Phone"));
//                Users user = usersService.getOne(queryWrapper);
//                //重新赋值session
//                session.setAttribute("user", user);
//                filterChain.doFilter(request, response);
//            } catch (Exception e) {
//                //未登陆或登录失效
//                filed(response, request);
//            }
//        }
//    }
//
//    //检查token是否存在于redis中
//    private boolean isInRedis(HttpServletResponse response, HttpServletRequest request,
//                              String token, Map<String, String> info) throws IOException {
//        boolean isinredis = false;
//        isinredis = !redisUtil.hasKey(info.get("Uid"));
//        boolean tokenTrue = (!token.equals(redisUtil.get(info.get("Uid"))));
//        if (isinredis || tokenTrue) {
//            filed(response, request);
//            return false;
//        }
//        return true;
//    }
//
//    //认证失败
//    private void filed(HttpServletResponse response, HttpServletRequest request) throws IOException {
//        response.setHeader("Content-type", "text/html;charset=UTF-8");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(401);
//        response.getWriter().write("未授权的访问");
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
