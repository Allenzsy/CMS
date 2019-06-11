package test;

import com.zsy.cms.utils.DBUtil;

import java.sql.*;
import java.util.Random;

public class AddChannelTest {

    public static void main(String[] args) {

        Connection conn = DBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("insert into t_channel(name,description) values(?,?)");
//            rs = pstmt.executeQuery();
            Random rand = new Random();
            pstmt.setString(1, "这是频道名称"+rand.nextInt(Integer.MAX_VALUE));
            pstmt.setString(2,"这是频道描述啊啊啊啊");
            pstmt.executeUpdate();
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
