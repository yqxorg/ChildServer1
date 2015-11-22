package com.zhuika.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class DBUtil {
    private static String user;
    private static String password;
    private static String url;
    private static String driver;
    private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
    static {
        try {
            Properties props = new Properties();
            props.load(DBUtil.class.getClassLoader().getResourceAsStream(
                    "db.properties"));
            user = props.getProperty("user");
            password = props.getProperty("password");
            url = props.getProperty("url");
            driver = props.getProperty("driver");
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException("数据库驱动加载错", e);
        }
    }
    public static Connection getConnection()  {
        Connection con = tl.get();
        if (con == null) {
            try {
				con = DriverManager.getConnection(url, user, password);
            	//con = DriverManager.getConnection(url, "dbusr", "watch123");
			} catch (SQLException e) {
				e.printStackTrace();
			}
            tl.set(con);
        }
        return con;
    }
    public static void close() {
        Connection con = tl.get();
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tl.set(null);
        }
    }
    
    public static void main(String[] args) {
        Connection con = getConnection();
        System.out.println(con);
    }
} 