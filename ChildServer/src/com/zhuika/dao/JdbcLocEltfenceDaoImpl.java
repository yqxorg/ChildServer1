package com.zhuika.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.zhuika.entity.LocElectfence;

import com.zhuika.util.CommonUtil;
import com.zhuika.util.DBUtil;

public class JdbcLocEltfenceDaoImpl implements ILocEltfenceDao {

	public void addLocEltfence(LocElectfence locElectfenceInfo)
			throws Exception {
		try {
			// TODO 自动生成方法存根
			StringBuffer sql = new StringBuffer();

			sql.append(" INSERT INTO T_LOC_ELECTFENCE( FLocFenID,FEltFenceID,FSerialnumber,FDataStatus,FFieldStatus,FEltLongitude,FEltLatitude,FEltScope,FEltAddress,FLongitude,FLatitude,FAddress,FDistance,FAddTime,FUpdateTime,FRemark,battery,frecordcount ) ");
			sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			Connection con = DBUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			// / insert 的入参处理
			locElectfenceInfo.setFlocfenid(UUID.randomUUID().toString());

			ps.setObject(1, locElectfenceInfo.getFlocfenid());
			ps.setObject(2, locElectfenceInfo.getFeltfenceid());
			ps.setObject(3, locElectfenceInfo.getFserialnumber());
			ps.setObject(4, locElectfenceInfo.getFdatastatus());
			ps.setObject(5, locElectfenceInfo.getFfieldstatus());
			ps.setObject(6, locElectfenceInfo.getFeltlongitude());
			ps.setObject(7, locElectfenceInfo.getFeltlatitude());
			ps.setObject(8, locElectfenceInfo.getFeltscope());
			ps.setObject(9, locElectfenceInfo.getFeltaddress());
			ps.setObject(10, locElectfenceInfo.getFlongitude());
			ps.setObject(11, locElectfenceInfo.getFlatitude());
			ps.setObject(12, locElectfenceInfo.getFaddress());
			ps.setObject(13, locElectfenceInfo.getFdistance());

			Timestamp datetime = CommonUtil.getTimeStamp();

			ps.setObject(14, datetime);
			ps.setObject(15, datetime);
			ps.setObject(16, locElectfenceInfo.getFremark());
			ps.setObject(17, locElectfenceInfo.getBattery());
			ps.setObject(18, locElectfenceInfo.getFrecordcount());
			ps.executeUpdate();
			
			ISerialNumberDao snDao = new  JdbcSerialNumberDAOImpl();
			snDao.updateSerialNumberBattery(locElectfenceInfo.getFserialnumber(), locElectfenceInfo.getBattery());
			

		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close();
		}
	}
	
	public void addLocEltfence_Single(LocElectfence locElectfenceInfo)
			throws Exception {
		try {
			// TODO 自动生成方法存根
			StringBuffer sql = new StringBuffer();

			sql.append(" INSERT INTO t_loc_electfence_single( FLocFenID,FEltFenceID,FSerialnumber,FDataStatus,FFieldStatus,FEltLongitude,FEltLatitude,FEltScope,FEltAddress,FLongitude,FLatitude,FAddress,FDistance,FAddTime,FUpdateTime,FRemark,battery,frecordcount ) ");
			sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			Connection con = DBUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			// / insert 的入参处理
			locElectfenceInfo.setFlocfenid(UUID.randomUUID().toString());

			ps.setObject(1, locElectfenceInfo.getFlocfenid());
			ps.setObject(2, locElectfenceInfo.getFeltfenceid());
			ps.setObject(3, locElectfenceInfo.getFserialnumber());
			ps.setObject(4, locElectfenceInfo.getFdatastatus());
			ps.setObject(5, locElectfenceInfo.getFfieldstatus());
			ps.setObject(6, locElectfenceInfo.getFeltlongitude());
			ps.setObject(7, locElectfenceInfo.getFeltlatitude());
			ps.setObject(8, locElectfenceInfo.getFeltscope());
			ps.setObject(9, locElectfenceInfo.getFeltaddress());
			ps.setObject(10, locElectfenceInfo.getFlongitude());
			ps.setObject(11, locElectfenceInfo.getFlatitude());
			ps.setObject(12, locElectfenceInfo.getFaddress());
			ps.setObject(13, locElectfenceInfo.getFdistance());

			Timestamp datetime = CommonUtil.getTimeStamp();

			ps.setObject(14, datetime);
			ps.setObject(15, datetime);
			ps.setObject(16, locElectfenceInfo.getFremark());
			ps.setObject(17, locElectfenceInfo.getBattery());
			ps.setObject(18, locElectfenceInfo.getFrecordcount());
			ps.executeUpdate();
			
			ISerialNumberDao snDao = new  JdbcSerialNumberDAOImpl();
			snDao.updateSerialNumberBattery(locElectfenceInfo.getFserialnumber(), locElectfenceInfo.getBattery());
			

		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close();
		}
	}

	public List<LocElectfence> listLocElectfence(HashMap<String, String> map)
			throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.FLocFenID,a.FIncreaseID,a.FEltFenceID,a.FSerialnumber,a.FDataStatus,a.FFieldStatus,a.FEltLongitude,a.FEltLatitude,a.FEltScope,a.FEltAddress,a.FLongitude,a.FLatitude,a.FAddress,a.FDistance,a.FAddTime,a.FUpdateTime,a.FRemark ");
		sql.append(" FROM T_LOC_ELECTFENCE a ");
		sql.append(" WHERE 1 = 1 ");
		
		String where = "";
		if(map!=null && map.size()>0)
		{
			if(map.containsKey("FSerialnumber"))
			{
				where += " and a.FSerialnumber = '"+ map.get("FSerialnumber")+"'" ;
			}
			if(map.containsKey("FAreaNum") && map.get("FAreaNum")!=null&& !map.get("FAreaNum").toString().equals(""))
			{
				where += " and a.FEltFenceID = "+ map.get("FAreaNum")+"" ;
			}
			
			
		}
		sql.append(where);
		sql.append(" order by a.FIncreaseID desc ");
		sql.append(" limit 1");
		
		List<LocElectfence> LocElectfences = new ArrayList<LocElectfence>();

		try {
			Connection con = DBUtil.getConnection();
			PreparedStatement ps = con.prepareStatement(sql.toString());
			//ps.setString(1, serialNumber);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				LocElectfence item = new LocElectfence();
	            item.setFlocfenid((String)rs.getObject(1));            
	            item.setFincreaseid((Integer)rs.getObject(2));            
	            item.setFeltfenceid((Integer)rs.getObject(3));            
	            item.setFserialnumber((String)rs.getObject(4));            
	            item.setFdatastatus((Integer)rs.getObject(5));            
	            item.setFfieldstatus((Integer)rs.getObject(6));            
	            item.setFeltlongitude((String)rs.getObject(7));            
	            item.setFeltlatitude((String)rs.getObject(8));            
	            item.setFeltscope((Double)rs.getObject(9));            
	            item.setFeltaddress((String)rs.getObject(10));            
	            item.setFlongitude((String)rs.getObject(11));            
	            item.setFlatitude((String)rs.getObject(12));            
	            item.setFaddress((String)rs.getObject(13));            
	            item.setFdistance((Double)rs.getObject(14));            
	            item.setFaddtime((java.sql.Timestamp)rs.getObject(15));            
	            item.setFupdatetime((java.sql.Timestamp)rs.getObject(16));            
	            item.setFremark((String)rs.getObject(17)); 
				LocElectfences.add(item);

			}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close();
		}
		return LocElectfences;
	}
}
