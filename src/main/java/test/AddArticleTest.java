package test;

import com.zsy.cms.utils.DBUtil;

import java.sql.*;
import java.util.Random;

public class AddArticleTest {

    public static void main(String[] args) {

        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("insert into t_article(title,content,createtime) values(?,?,?)");
            for (int i = 0; i < 102; i++) {
                Random rand = new Random();
                pstmt.setString(1, "这是文章标题"+rand.nextInt(Integer.MAX_VALUE));
                pstmt.setString(2,"这是文章内容啊啊啊啊啊");
                pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                pstmt.executeUpdate();
            }

            conn.commit();
        }catch (SQLException e) {
            e.printStackTrace();
            DBUtil.rollBack(conn);
        } finally {
//            DBUtil.close(rs);
            DBUtil.close(pstmt);
            DBUtil.close(conn);
        }

    }
}
