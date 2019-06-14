package test;

import com.zsy.cms.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FindPartOfArticleTest {

    public static void main(String[] args) {

        int offset = 0;
        int pageSize = 5;
        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("select * from t_article limit ?,?");
            pstmt.setInt(1, offset);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            while(rs.next()) {
                String title = rs.getString("title");
                String content = rs.getString("content");
                Date date = rs.getTimestamp("createtime");

                System.out.println(title + "  " + content + "  " + date);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }

    }
}
