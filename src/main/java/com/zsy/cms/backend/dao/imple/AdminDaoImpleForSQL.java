package com.zsy.cms.backend.dao.imple;

import com.zsy.cms.backend.dao.AdminDao;
import com.zsy.cms.backend.model.Admin;
import com.zsy.cms.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDaoImpleForSQL implements AdminDao {

    @Override
    public Admin getAdmin(String username) {

        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Admin admin = null;
        try {
            pstmt = conn.prepareStatement("select * from t_admin where username=?");
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                admin = new Admin();
                admin.setUsername(username);
                admin.setId(rs.getInt("id" ));
                admin.setPassword(rs.getString("password"));
                // 找到用户名，返回Admin对象
                return admin;
            }
            else {
                // 用户名不存在，返回Admin null
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }
        return admin;
    }
}
