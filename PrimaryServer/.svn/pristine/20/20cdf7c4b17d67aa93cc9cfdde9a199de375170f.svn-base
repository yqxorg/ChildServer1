package com.zhuika.dao;

public class DAOFactory {
	private static ISerialNumberDao snDao=new JdbcSerialNumberDAOImpl();
	
    private static IInfoDao infoDao=new JdbcInfoDaoImpl();
    
    private static IRtPositionDao rtpDao=new JdbcRtPositionDaoImpl();
    
    public static IInfoDao  getInfoDao(){
    	return  infoDao;
    }
   
	public static ISerialNumberDao getSerialNumberDao() {
		return snDao;
	}
	
	public static IRtPositionDao getRtPositionDao(){
		return rtpDao;
	}
}
