package com.zhuika.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import com.zhuika.entity.RtPosition;
import com.zhuika.entity.SerialNumber;
import com.zhuika.util.DBUtil;


public class JdbcSerialNumberDAOImpl  implements ISerialNumberDao{
	public void addSerialNumber(SerialNumber serialNumber) {
		try {
			String sql2="insert into serialnumber (serialnumber,ef,funiqueid) values(?,?,?);";
			String sql="insert into locationinfo (serialnumber) values(?);";
			Connection con=DBUtil.getConnection();
			PreparedStatement ps2=con.prepareStatement(sql2);
			PreparedStatement ps=con.prepareStatement(sql);
			ps2.setObject(1, serialNumber.getSerialNumber());
			ps2.setObject(2, serialNumber.getEf());
			
			String distributorid = UUID.randomUUID().toString();
			ps2.setObject(3, distributorid);
			
			ps.setObject(1, serialNumber.getSerialNumber());
			ps2.executeUpdate();
			ps.executeUpdate();
			RtPosition rtp=new RtPosition();
			rtp.setSerialNumber(serialNumber.getSerialNumber());
			rtp.setQuery("0");
			rtp.setStatus("0");
			IRtPositionDao rtpDao=DAOFactory.getIRtPositionDao();
			rtpDao.addRtPosition(rtp);
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		} 
	}
	public static void main(String[] args) throws DAOException {
		String serialNumber="11111111111111";
		ISerialNumberDao sndao = DAOFactory.getISerialNumberDao();
		SerialNumber serialNum=sndao.findBySerialNumber(serialNumber);
		serialNum.setGpsStatus("0");
		serialNum.setSerialNumber(serialNumber);
		serialNum.setLbs(serialNum.getLbs());
		serialNum.setSetGps(serialNum.getSetGps());
		serialNum.setEf(serialNum.getEf());
		serialNum.setStatus(serialNum.getStatus());
		sndao.updateSerialNumber(serialNum);
		System.out.println(sndao.findBySerialNumber(serialNumber).toString());
	}
	public SerialNumber findBySerialNumber(String serialNumber) {
		String sql="select *from serialnumber where serialnumber=?";
		try {
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, serialNumber);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				SerialNumber sn=new SerialNumber();
				sn.setId(rs.getInt("id"));
				sn.setSerialNumber(serialNumber);
				sn.setEf(rs.getString("ef"));
				sn.setStatus(rs.getString("status"));
				sn.setSetGps(rs.getString("setgps"));
				sn.setGpsStatus(rs.getString("gpsstatus"));
				sn.setIsReg(rs.getString("isReg"));
				sn.setLbs(rs.getString("lbs"));
				sn.setListenStatus(rs.getString("listenstatus"));
				sn.setOnline(rs.getString("online"));
				return sn;
			}
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		}	
		return null;
	}
	public void updateSerialNumber(SerialNumber serialNumber)
			throws DAOException {
		System.out.println(123456789);
		String sql="update serialnumber set status=?,ef=?,gpsstatus=?,setgps=?,lbs=?,listenstatus=?,online=? where serialnumber=?";
		try {
			System.out.println(serialNumber.toString());
			Connection con=DBUtil.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, serialNumber.getStatus());
			ps.setString(2, serialNumber.getEf());
			ps.setString(3, serialNumber.getGpsStatus());
			ps.setString(4, serialNumber.getSetGps());
			ps.setString(5, serialNumber.getLbs());
			ps.setString(6, serialNumber.getListenStatus());
			ps.setString(7, serialNumber.getOnline());
			ps.setString(8, serialNumber.getSerialNumber());	
			ps.executeUpdate();
		} catch (Exception e) {
			
		} finally{
			DBUtil.close();
		}	
	}
	@Override
	public Integer getOnlineNo() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer getNotOnlineNo() {
		// TODO Auto-generated method stub
		return null;
	}
}
