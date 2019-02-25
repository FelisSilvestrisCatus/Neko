package neko.filter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Users;
import neko.service.IUsersService;
import neko.utils.Token;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebFilter(filterName = "sessionFilter", urlPatterns = {"/*"})
public class SessionFilter implements Filter {

    @Autowired
    private IUsersService usersService;


    //不需要登录就可以访问的路径(比如:注册登录等)
    String[] includeUrls = new String[]{"/users/login"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        System.out.println("filter url:" + uri);
        //是否需要过滤
        boolean needFilter = isNeedFilter(uri);
        try {
            System.out.println("session:" + session.toString());
            System.out.println("user is:" + session.getAttribute("user"));
        } catch (Exception e) {
            System.out.println("NULL USER");
        }


        if (!needFilter) { //不需要过滤直接传给下一个过滤器
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            //需要过滤器
            String token = request.getHeader("Authorization");
            if (null == token || Objects.equals(token, "")) {
                //无token信息,销毁session
                session.invalidate();
                response.setStatus(401);
                response.getWriter().write("error filter");
                return;
            }
            // session中包含user对象,则是登录状态
            if (session != null && session.getAttribute("user") != null) {
                // System.out.println("user:"+session.getAttribute("user"));
                filterChain.doFilter(request, response);
            } else {
                try {
                    //存在token但不存在session

                    Map<String, String> info = Token.parseJwtToken(token);
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("phone", info.get("Phone"));
                    Users user = usersService.getOne(queryWrapper);
                    //重新赋值session
                    session.setAttribute("user", user);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    //未登陆或登录失效
                    response.setStatus(401);
                    response.getWriter().write("error filter");
                }
            }
        }
    }

    /**
     * @param uri
     * @Author: xxxxx
     * @Description: 是否需要过滤
     * @Date: 2018-03-12 13:20:54
     */
    public boolean isNeedFilter(String uri) {

        for (String includeUrl : includeUrls) {
            if (includeUrl.equals(uri)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
