package util;

import entity.UserBean;

import java.sql.*;

/**
 * Created by wenc on 2017/5/10.
 */
public class UserBeanCl {
    private Connection ct = null;
    private PreparedStatement ps = null;
    private Statement sm = null;
    private ResultSet rs = null;

    boolean exist = false;//查看是否存在记录（查表）

    public boolean checkUserInfo(UserBean u) {
        try {
            conn();
            //得到连接
            sm = ct.createStatement();
            String querry = "select userName from register";
            rs = sm.executeQuery(querry);
            while (rs.next()) {
                String name = rs.getString(1);
                if (name.equals(u.getUsername())) {
                    exist = true;//若查找到用户名已经存在，则将rowCount置1
                    return exist;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return exist;
    }

    public void writeToDb(UserBean u) {
        conn();
        try {
            sm = ct.createStatement();
            String sql = "insert into register values('" + u.getUsername() + "','" + "',"
                    + "'" + u.getPassword() + "')";
            sm.executeUpdate(sql);
           /* rs=sm.executeQuery("select * from register");
            System.out.println("插入后的结果：\n");
            while(rs.next()){
                System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)
                        +"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6));
            }*/
        } catch (SQLException ex) {
//            Logger.getLogger(UserBeanCl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }


    public void conn() {
        //加载驱动
        try {
            Class.forName("");
            ct = DriverManager.getConnection("jdbc:mysql://localhost:3306/werewolves", "root", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //关闭资源
    public void close() {
        if (rs != null) try {
            rs.close();
            rs = null;
        } catch (SQLException ex) {
//            Logger.getLogger(UserBeanCl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ps != null) try {
            ps.close();
            ps = null;
        } catch (SQLException ex) {
//            Logger.getLogger(UserBeanCl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ct != null) try {
            ct.close();
            ct = null;
        } catch (SQLException ex) {
//            Logger.getLogger(UserBeanCl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
