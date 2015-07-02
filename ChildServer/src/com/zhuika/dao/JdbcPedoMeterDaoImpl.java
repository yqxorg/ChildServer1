package com.zhuika.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhuika.entity.PedoMeter;
import com.zhuika.util.DBUtil;

public class JdbcPedoMeterDaoImpl implements IPedoMeterDao {

	public void addPedoMeter(PedoMeter pedoMeter) throws DAOException {
		String sql="insert into pedometer (serialnumber,distance) values (?,?)";
		try {
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, pedoMeter.getSerialNumber());
			ps.setString(2, pedoMeter.getDistance());
			ps.executeUpdate();
		} catch (Exception e) {
			
		}finally{
			DBUtil.close();
		}	
	}

	public void updatePedoMeter(PedoMeter pedoMeter) throws DAOException {
		String sql="update  pedometer set distance=? where serialnumber=?";
		try {
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, pedoMeter.getDistance());
			ps.setString(2, pedoMeter.getSerialNumber());
			ps.executeUpdate();
		} catch (Exception e) {
			
		}finally{
			DBUtil.close();
		}	
	}

	public PedoMeter findBySerialNumber(String serialNumber)
			throws DAOException {
		String sql="select *from  pedometer where serialnumber=?";
		try {
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);			
			ps.setString(1, serialNumber);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				return createPedoMeter(serialNumber, rs);
			}
		} catch (Exception e) {
			
		}finally{
			DBUtil.close();
		}	
		return null;
	}

	private PedoMeter createPedoMeter(String serialNumber, ResultSet rs)
			throws SQLException {
		PedoMeter pedoMeter=new PedoMeter();
		pedoMeter.setId(rs.getInt("id"));
		pedoMeter.setSerialNumber(serialNumber);
		pedoMeter.setDistance(rs.getString("distance"));
		return pedoMeter;
	}

}
