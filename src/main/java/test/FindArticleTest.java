package test;

import com.zsy.cms.utils.DBUtil;
import com.zsy.cms.utils.PropertiesBeanFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FindArticleTest {

    public static void main(String[] args) {

//        Connection conn = DBUtil.getConn();
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            pstmt = conn.prepareStatement("select * from t_article");
//            rs = pstmt.executeQuery();
//            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
//            while(rs.next()) {
//                String title = rs.getString("title");
//                String content = rs.getString("content");
//                Date date = rs.getTimestamp("createtime");
//
//                System.out.println(title + "  " + content + "  " + dateformat.format(date));
//            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DBUtil.close(rs);
//            DBUtil.close(pstmt);
//            DBUtil.close(conn);
//        }

        new PropertiesBeanFactory().getArticleDao();
    }

}
