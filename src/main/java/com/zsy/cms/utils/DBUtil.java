package com.zsy.cms.utils;

import java.sql.*;

public class DBUtil {
    public static Connection getConn(){

        // 1、载入驱动包
        try { // 利用java的反射机制，forname创建一个类，加载到方法区里，对于类中的静态代码块会自动执行
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }


        // 2、创建数据库连接，url:数据库；连接协议
        String url = "jdbc:mysql://localhost/cms?useSSL=true";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, "root" ,"123456");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void close(ResultSet rs) {
        try {
            if(rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(PreparedStatement pstmt) {
        try {
            if(pstmt != null)
                pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void close(Connection conn) {
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void rollBack(Connection conn) {
        try {
            if(conn != null)
                conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
