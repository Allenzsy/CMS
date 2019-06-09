package com.zsy.cms.backend.view;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {

    String filterPattern = null;
    @Override
    public void init(FilterConfig config) throws ServletException {
        filterPattern = config.getInitParameter("filterPattern");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String requestURI = request.getRequestURI();
//        System.out.println(requestURI);
        String page = requestURI.substring(request.getContextPath().length());
        String loginAdmin = (String)request.getSession().getAttribute("LOGIN_ADMIN");
        // 由于filter 在配置的时候使用的是/backend/* 所以只要是/backend/开头的请求都会进到
        // 这个doFilter方法中，意味着其他请求也会被拦截（老师演示的时候所有图片请求都被拦截了，
        // 但是我只有验证码被拦截，还有jsp，servlet不知道新的javax进行改进了）
//        System.out.println(filterPattern);
        if(page.matches(filterPattern)) {
            if(loginAdmin == null && !page.equals("/backend/login.jsp") && !page.equals("/backend/LoginServlet")) {
                response.sendRedirect(request.getContextPath()+"/backend/login.jsp");
                return;
            }
        }


        // 继续之前的请求
        filterChain.doFilter(request, response);
    }



    @Override
    public void destroy() {

    }
}
