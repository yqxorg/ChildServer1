package com.zhuika.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zhuika.entity.Info;
import com.zhuika.util.DBUtil;

public class JdbcInfoDaoImpl implements IInfoDao {

	public void addInfo(Info info) {
		try {
			String sql="insert into info (serialnumber,ip,port) values(?,?,?)";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setObject(1, info.getSerialNumber());
			ps.setObject(2, info.getIp());
			ps.setObject(3, info.getPort());
			ps.executeUpdate();
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		}

	}

	public void updateInfo(Info info) {
		try {
			String sql="update info set ip=?,port=? where serialnumber=?";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setObject(1, info.getIp());
			ps.setObject(2, info.getPort());
			ps.setObject(3, info.getSerialNumber());
			ps.executeUpdate();
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		}
	}

	public Info findBySerialNumber(String serialNumber) {
		try {
			String sql="select * from info where serialnumber=?";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setObject(1, serialNumber);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				Info info=new Info();
				info.setId(rs.getInt("id"));
				info.setIp(rs.getString("ip"));
				info.setPort(rs.getInt("port"));
				info.setSerialNumber(serialNumber);
				return info;
			}
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		}
		return null;
	}

}
