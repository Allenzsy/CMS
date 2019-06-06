package com.zsy.cms.backend.view;

import com.zsy.cms.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 这里用了注解，就可以不在web.xml配置了
@WebServlet("/backend/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 客户端post提交的参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String checkcode = request.getParameter("checkcode");

        // CheckcodeServlet 生成验证码后放入session
        String genCode = (String)request.getSession().getAttribute("genCode");

        // 判断客户端提交的checkcode和genCode是否一致
//        if(!checkcode.equalsIgnoreCase(genCode)) {
//            // 重定向到登录页面
//            request.setAttribute("error", "验证码错误");
//            request.getRequestDispatcher("/backend/login.jsp").forward(request, response);
//            return;
//        } //调试先取消验证码验证

        // 判断用户名是否存在，判断密码是否正确
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select * from t_admin where username=?");
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                // 判断密码是否正确
                if(!password.equals(rs.getString("password"))) {
                    // forward 到 login.jsp 并提示密码不正确
                    request.setAttribute("error", "用户名【"+username+"】密码不正确");
                    request.getRequestDispatcher("/backend/login.jsp").forward(request, response);
                    return;
                }
            }
            else {
                //forward 到 login.jsp 并且提示用户名不存在
                request.setAttribute("error", "用户名【"+username+"】不存在");
                request.getRequestDispatcher("/backend/login.jsp").forward(request, response);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }


        // 全部匹配，转向后台页面
        request.getSession().setAttribute("LOGIN_ADMIN", username);
        response.sendRedirect(request.getContextPath()+"/backend/main.jsp");


    }

}
