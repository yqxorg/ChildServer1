package com.zhuika.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import com.zhuika.entity.RtPosition;
import com.zhuika.util.DBUtil;

public class JdbcRtPositionDaoImpl implements IRtPositionDao {

	public void addRtPosition(RtPosition rtp) {
		try {
			String sql="insert into rtposition (serialnumber,status,query) values (?,?,?);";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setObject(1, rtp.getSerialNumber());
			ps.setObject(2, rtp.getStatus());
			ps.setObject(3, rtp.getQuery());
			ps.executeUpdate();
		} catch (Exception e) {
			
		} finally{	
			DBUtil.close();
		}
	}

	public void updateRtPosition(RtPosition rtp) {
		try {
			String sql="update rtposition set lng=?,lat=?,status=?,query=? where serialnumber=?;";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setObject(1, rtp.getLng());
			ps.setObject(2, rtp.getLat());
			ps.setObject(3, rtp.getStatus());
			ps.setObject(4, rtp.getQuery());
			ps.setObject(5, rtp.getSerialNumber());
			ps.executeUpdate();
		} catch (Exception e) {
			
		} finally{	
			DBUtil.close();
		}
	}

	public RtPosition findBySerialNumber(String serialNumber) {
		try {
			String sql="select *from rtposition where serialnumber=?";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setObject(1, serialNumber);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
//				return createRtPosition(serialNumber, rs);
				RtPosition rtp=new RtPosition();
				rtp.setId(rs.getInt("id"));
				rtp.setLat(rs.getString("lat"));
				rtp.setLng(rs.getString("lng"));
				rtp.setQuery(rs.getString("query"));
				rtp.setSerialNumber(serialNumber);
				rtp.setStatus(rs.getString("status"));
				rtp.setCreateTime(rs.getString("createtime"));
				return rtp;
			}
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		}
		
		return null;
	}
}
