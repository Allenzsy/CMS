package com.zsy.cms.backend.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 清空http session中所有参数，并把http session对象销毁
        req.getSession().invalidate();
        // 重定向到登录页面
        resp.sendRedirect(req.getContextPath()+"backend/login.jsp");
    }
}
