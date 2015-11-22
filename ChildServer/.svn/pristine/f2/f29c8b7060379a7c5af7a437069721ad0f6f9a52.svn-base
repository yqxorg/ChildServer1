package com.zhuika.dao;

public class DAOFactory {
	private static ISerialNumberDao sndao=new JdbcSerialNumberDAOImpl();
	private static ILocationInfoDao lidao=new JdbcLocationInfoDaoImpl();
	private static IElectFenceDao electFenceDao=new JdbcElectFenceDaoImpl();	
    private static IPedoMeterDao pedoMeterDao=new JdbcPedoMeterDaoImpl();
    private static IRtPositionDao rtpDao=new JdbcRtPositionDaoImpl();
    
    private static ILocEltfenceDao locTltDao=new JdbcLocEltfenceDaoImpl();
    
    public static IRtPositionDao  getIRtPositionDao(){
    	return  rtpDao;
    }
    public static IPedoMeterDao getIPedoMeterDao(){
    	return pedoMeterDao;
    }
  	
	public static ISerialNumberDao getISerialNumberDao() {
		return sndao;
	}
	
	public static ILocationInfoDao getILocationInfoDao(){
		return lidao;
	}
	public static IElectFenceDao getIElectFenceDao(){
		return electFenceDao;
	}
	//yangqinxu
	public static ILocEltfenceDao getILocEltfenceDao(){
		return locTltDao;
	}		 
}
