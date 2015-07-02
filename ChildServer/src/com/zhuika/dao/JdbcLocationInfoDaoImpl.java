package com.zhuika.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zhuika.entity.LocationInfo;
import com.zhuika.util.DBUtil;

public class JdbcLocationInfoDaoImpl implements ILocationInfoDao {

	public LocationInfo findBySeriaNumber(String serialNumber) {
		try {
			String sql="select *from locationinfo where serialNumber=?";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setObject(1, serialNumber);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				LocationInfo li=new LocationInfo();
				li.setId(rs.getInt("id"));
				li.setSerialNumber(serialNumber);
				li.setText(rs.getString("text"));
				li.setLocation(rs.getString("location"));
				li.setLat(rs.getString("lat"));
				li.setLng(rs.getString("lng"));
				li.setBattery(rs.getString("battery"));
				return li;
			}
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		}
		return null;
	}

	public void save(LocationInfo li) {
		
	}

	public void update(LocationInfo li) {
		try {
			String sql="update locationinfo set text=?,location=?,lng=?,lat=?,battery=? where serialNumber=?";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setObject(1, li.getText());
			ps.setObject(2, li.getLocation());
			ps.setObject(3, li.getLng());
			ps.setObject(4, li.getLat());
			ps.setObject(5, li.getBattery());
			ps.setObject(6, li.getSerialNumber());
			ps.executeUpdate();
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		}
	}
	public static void main(String[] args) {
		System.out.println(new JdbcLocationInfoDaoImpl().findBySeriaNumber("806800000000010").getText());
	}
}
