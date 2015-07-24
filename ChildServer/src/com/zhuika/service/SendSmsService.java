package com.zhuika.service;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.users.ejb.EjbEnum.EltLocEnum;
import com.zhuika.dao.DAOException;
import com.zhuika.dao.DAOFactory;
import com.zhuika.dao.IElectFenceDao;
import com.zhuika.dao.ILocEltfenceDao;
import com.zhuika.dao.ISerialNumberDao;
import com.zhuika.entity.ElectFence;
import com.zhuika.entity.LocElectfence;
import com.zhuika.entity.SerialNumber;
import com.zhuika.util.FNUtil;
import com.zhuika.util.Hex;

import org.apache.log4j.Logger;

public class SendSmsService {
	private static IElectFenceDao electFenceDao = DAOFactory.getIElectFenceDao();
	private static ISerialNumberDao sndao = DAOFactory.getISerialNumberDao();
	private static ILocEltfenceDao locEltdao = DAOFactory.getILocEltfenceDao();
	//最新的序列号位置及围栏信息
	private static HashMap<String, LocElectfence> m_hashLocElt = new HashMap<String, LocElectfence>();	  
	static Logger logger = Logger.getLogger(SendSmsService.class);
	
	//检查变化状态
	private static int CheckExist(LocElectfence locElt,ElectFence efItem){
		
		String skey = locElt.getFserialnumber()+"_"+efItem.getId();		
		
		if(m_hashLocElt.containsKey(skey))
		{
			LocElectfence locEltHs = m_hashLocElt.get(skey);
			
			Double locescope = locEltHs.getFeltscope();
			
			if(locescope == null){
				locescope = 0D;
			}
			
			//历史-距离（当前gps与围栏中心的距离）离半径的距离；
			Double d1 = locEltHs.getFdistance() - locescope;
			
			//这次-距离（当前gps与围栏中心的距离）离半径的距离；
			Double d2 = locElt.getFdistance() - locElt.getFeltscope();
			
			//从围栏外进围栏内
			if(d1>0 && d2<0)
			{
				return 1;
			}
			//从围栏内到围栏外
			else if(d1<0 && d2>0)
			{
				return 3;
			}
			//一直在围栏外
			else if(d1>0 && d2>0)
			{
				return 4;
			}
			//一直在围栏内
			else if(d1<0 && d2<0)
			{
				return 5;
			}
			
		}
		return 0;
		
	}
	
	//保留位置，使用独立表,yangqinxu
	private static void SaveLocationSTable(List<ElectFence> list, String serialNumber,String Address,String mlon,String mlat)
	{
		if (list.isEmpty())
		{
			try{
				
				//获取之前最新的数据用来做变化对比
				HashMap<String, String> mapCondition = new HashMap<String, String>();
				mapCondition.put("FSerialnumber", serialNumber);
				List<LocElectfence> listLocElt =  locEltdao.listLocElectfence(mapCondition);
				
				String premlon = "";
				String premlat="";
				Double DistanceItem = 0D;
				//无围栏的情况，记录前后两次的距离差
				if(listLocElt !=null && listLocElt.size()>0)
				{
					premlon = listLocElt.get(0).getFlongitude();
					premlat = listLocElt.get(0).getFlatitude();
					
					DistanceItem = FNUtil.GetShortDistance(
							Double.parseDouble(premlon),
							Double.parseDouble(premlat),
							Double.parseDouble(mlon),
							Double.parseDouble(mlat)) ;					
				}
										
				LocElectfence locElt = new LocElectfence();				
				locElt.setFdistance(DistanceItem);				
				locElt.setFeltfenceid(null);
				locElt.setFeltlongitude(null);
				locElt.setFeltlatitude(null);					
				locElt.setFeltscope(null);				
				locElt.setFlongitude(mlon);
				locElt.setFlatitude(mlat);										
				locElt.setFserialnumber(serialNumber);				
				locElt.setFaddress(Address);				
				locElt.setFdatastatus(null);	
				locElt.setFfieldstatus(EltLocEnum.IsSelected.value());
				
				locEltdao.addLocEltfence(locElt);
			}
			catch(Exception ex)
			{
				logger.error("isEmpty"+ex.getMessage());
				System.out.println("isEmpty"+ex.getMessage());
			}
			
			return ;
		}
		//yangqinxu 进出电子围栏记录  begin 
		try{
			boolean isSetSelected = false;
			
			for(int i=0;i<list.size();i++)
			{
				ElectFence efItem = list.get(i);
				
				//获取之前最新的数据用来做变化对比
				HashMap<String, String> mapCondition = new HashMap<String, String>();
				mapCondition.put("FSerialnumber", serialNumber);
				mapCondition.put("FAreaNum", Integer.toString(efItem.getId()));
				List<LocElectfence> listLocElt =  locEltdao.listLocElectfence(mapCondition);
				if(listLocElt !=null && listLocElt.size()>0)
				{
					String skey = serialNumber+"_"+efItem.getId();						
					m_hashLocElt.put(skey, listLocElt.get(0));						
				}
				
				//取得电子围栏经纬度
				String locationInfoItem = efItem.getLocationbd();
				//GetShortDistance
				Double DistanceItem = FNUtil.GetShortDistance(
						Double.parseDouble(locationInfoItem.split(",")[0]),
						Double.parseDouble(locationInfoItem.split(",")[1]),
						Double.parseDouble(mlon),
						Double.parseDouble(mlat)) ;
				
				LocElectfence locElt = new LocElectfence();
				
				locElt.setFdistance(DistanceItem);
				
				locElt.setFeltfenceid(efItem.getId());
				locElt.setFeltlongitude(locationInfoItem.split(",")[0]);
				locElt.setFeltlatitude(locationInfoItem.split(",")[1]);					
				locElt.setFeltscope((double)efItem.getScope());
				
				locElt.setFlongitude(mlon);
				locElt.setFlatitude(mlat);										
				locElt.setFserialnumber(serialNumber);
				
				locElt.setFaddress(Address);
				
				int datastatus = CheckExist(locElt,efItem);

				if(datastatus==1)
				{
					locElt.setFremark(String.format("从外到内,围栏编号:%s,围栏名称:%s", efItem.getAreaNum(),efItem.getName()));
				}
				else if(datastatus==3)
				{
					locElt.setFremark(String.format("从内到外,围栏编号:%s,围栏名称:%s", efItem.getAreaNum(),efItem.getName()));
				}
				else if(datastatus==4)
				{
					locElt.setFremark(String.format("保持在外,围栏编号:%s,围栏名称:%s", efItem.getAreaNum(),efItem.getName()));
				}
				else if(datastatus==5)
				{
					locElt.setFremark(String.format("保持在内,围栏编号:%s,围栏名称:%s", efItem.getAreaNum(),efItem.getName()));
				}									
				
				locElt.setFdatastatus(datastatus);
				
				if(isSetSelected == false){
					locElt.setFfieldstatus(EltLocEnum.IsSelected.value());
					isSetSelected = true;
				}
				
				locEltdao.addLocEltfence(locElt);										
			}					
		} 
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
			System.out.println(ex.getMessage());
		}
		//yangqinxu 进出电子围栏记录  end 
	}
	public static void controlSMS(ChannelHandlerContext ctx, String serialNumber,
			SerialNumber serialNum, String locationbd, String end,String Address,String location)
			throws DAOException {
		List<ElectFence> list = electFenceDao.findBySerialNumber(serialNumber);
		
		String mlon = locationbd.split(",")[0];
		String mlat = locationbd.split(",")[1];
		
		//取高德地图：
		String mlongd = location.split(",")[0];
		String mlatgd = location.split(",")[1];
		
		
		///yangqinxu 位置及进出电子围栏记录  
		SaveLocationSTable(list,serialNumber,Address,mlongd,mlatgd);
		
		if (list.isEmpty()) {
			System.out.println("没有设置电子围栏");
						
		} else {
//			String mlon = locationbd.split(",")[0];
//			String mlat = locationbd.split(",")[1];
			String info = "40400012" + serialNumber + "9993";
			
			//yangqinxu 进出电子围栏记录  begin 
			
			//SaveLocationSTable(list,serialNumber,Address,mlon,mlat);
			
//			try{
//				for(int i=0;i<list.size();i++)
//				{
//					ElectFence efItem = list.get(i);
//					
//					//获取之前最新的数据用来做变化对比
//					HashMap<String, String> mapCondition = new HashMap<String, String>();
//					mapCondition.put("FSerialnumber", serialNumber);
//					List<LocElectfence> listLocElt =  locEltdao.listLocElectfence(mapCondition);
//					if(listLocElt !=null && listLocElt.size()>0)
//					{
//						String skey = serialNumber+"_"+efItem.getAreaNum();						
//						m_hashLocElt.put(skey, listLocElt.get(0));						
//					}
//					
//					//取得电子围栏经纬度
//					String locationInfoItem = efItem.getLocationbd();
//					//GetShortDistance
//					Double DistanceItem = FNUtil.GetShortDistance(
//							Double.parseDouble(locationInfoItem.split(",")[0]),
//							Double.parseDouble(locationInfoItem.split(",")[1]),
//							Double.parseDouble(mlon),
//							Double.parseDouble(mlat)) ;
//					
//					LocElectfence locElt = new LocElectfence();
//					
//					locElt.setFdistance(DistanceItem);
//					
//					locElt.setFeltfenceid(efItem.getId());
//					locElt.setFeltlongitude(locationInfoItem.split(",")[0]);
//					locElt.setFeltlatitude(locationInfoItem.split(",")[1]);					
//					locElt.setFeltscope((double)efItem.getScope());
//					
//					locElt.setFlongitude(mlon);
//					locElt.setFlatitude(mlat);										
//					locElt.setFserialnumber(serialNumber);
//					
//					locElt.setFaddress(Address);
//					
//					int datastatus = CheckExist(locElt,efItem);
//
//					if(datastatus==1)
//					{
//						locElt.setFremark(String.format("从围栏外进围栏内,围栏编号:%s,围栏名称:%s", efItem.getAreaNum(),efItem.getName()));
//					}
//					else if(datastatus==3)
//					{
//						locElt.setFremark(String.format("从围栏内到围栏外,围栏编号:%s,围栏名称:%s", efItem.getAreaNum(),efItem.getName()));
//					}
//					else if(datastatus==4)
//					{
//						locElt.setFremark(String.format("一直在围栏外,围栏编号:%s,围栏名称:%s", efItem.getAreaNum(),efItem.getName()));
//					}
//					else if(datastatus==5)
//					{
//						locElt.setFremark(String.format("一直在围栏内,围栏编号:%s,围栏名称:%s", efItem.getAreaNum(),efItem.getName()));
//					}									
//					
//					locElt.setFdatastatus(datastatus);
//					
//					locEltdao.addLocEltfence(locElt);										
//				}					
//			} 
//			catch(Exception ex)
//			{
//				logger.error(ex.getMessage());
//				System.out.println(ex.getMessage());
//			}
			//yangqinxu 进出电子围栏记录  end 
			if (list.size() == 1) {
				ElectFence ef = list.get(0);
				System.out.println("areaNum" + ef.getAreaNum());
				String locationInfo = ef.getLocationbd();
				if (1 == ef.getAreaNum()) {
					// 家
					System.out.println("locationInfo:" + locationInfo);
					if ("3".equals(serialNum.getEf())) {
						if (FNUtil.GetShortDistance(
								Double.parseDouble(locationInfo.split(",")[0]),
								Double.parseDouble(locationInfo.split(",")[1]),
								Double.parseDouble(mlon),
								Double.parseDouble(mlat)) < ef.getScope()) {
							// 在家
							serialNum.setEf("0");
							serialNum.setSerialNumber(serialNumber);
							serialNum.setStatus(serialNum.getStatus());
							sndao.updateSerialNumber(serialNum);
						} else {
							// 在危险区域
							serialNum.setEf("2");
							serialNum.setSerialNumber(serialNumber);
							serialNum.setStatus(serialNum.getStatus());
							sndao.updateSerialNumber(serialNum);
						}
					} else if ("0".equals(serialNum.getEf())) {
						// 已经在家
						// 0 离开进入模式 1离开模式 2进入模式
						if ("0".equals(ef.getModel())
								|| "1".equals(ef.getModel())) {
							if (FNUtil
									.GetShortDistance(Double
											.parseDouble(locationInfo
													.split(",")[0]), Double
											.parseDouble(locationInfo
													.split(",")[1]), Double
											.parseDouble(mlon), Double
											.parseDouble(mlat)) < ef.getScope()) {
								// 进入家了

							} else {
								// 离开家了
								serialNum.setEf("2");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
								info = info + "01";
								// 返回消息给终端
								returnMessage(info, ctx, end);
							}
						} else if ("2".equals(ef.getModel())) {
							if (FNUtil
									.GetShortDistance(Double
											.parseDouble(locationInfo
													.split(",")[0]), Double
											.parseDouble(locationInfo
													.split(",")[1]), Double
											.parseDouble(mlon), Double
											.parseDouble(mlat)) < ef.getScope()) {
								// 进入家了

							} else {
								// 离开家了
								serialNum.setEf("2");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
							}
						}
					} else if ("2".equals(serialNum.getEf())) {
						// 已经在危险区域
						if ("0".equals(ef.getModel())
								|| "2".equals(ef.getModel())) {
							if (FNUtil
									.GetShortDistance(Double
											.parseDouble(locationInfo
													.split(",")[0]), Double
											.parseDouble(locationInfo
													.split(",")[1]), Double
											.parseDouble(mlon), Double
											.parseDouble(mlat)) < ef.getScope()) {
								// 进入家了
								serialNum.setEf("0");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
								info = info + "04";
								// 返回消息给终端
								returnMessage(info, ctx, end);
							}
						} else if ("1".equals(ef.getModel())) {
							if (FNUtil
									.GetShortDistance(Double
											.parseDouble(locationInfo
													.split(",")[0]), Double
											.parseDouble(locationInfo
													.split(",")[1]), Double
											.parseDouble(mlon), Double
											.parseDouble(mlat)) < ef.getScope()) {
								// 进入家了
								serialNum.setEf("0");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
							}
						}
					}
				} else if (2 == ef.getAreaNum()) {
					// 学校
					if ("3".equals(serialNum.getEf())) {
						if (FNUtil.GetShortDistance(
								Double.parseDouble(locationInfo.split(",")[0]),
								Double.parseDouble(locationInfo.split(",")[1]),
								Double.parseDouble(mlon),
								Double.parseDouble(mlat)) < ef.getScope()) {
							// 发送在学校的指令给终端
							serialNum.setEf("1");
							serialNum.setSerialNumber(serialNumber);
							serialNum.setStatus(serialNum.getStatus());
							sndao.updateSerialNumber(serialNum);
						} else {
							// 发送在危险区域的指令给终端
							serialNum.setEf("2");
							serialNum.setSerialNumber(serialNumber);
							serialNum.setStatus(serialNum.getStatus());
							sndao.updateSerialNumber(serialNum);
						}
					} else if ("1".equals(serialNum.getEf())) {
						// 在学校
						if ("0".equals(ef.getModel())
								|| "1".equals(ef.getModel())) {
							if (FNUtil
									.GetShortDistance(Double
											.parseDouble(locationInfo
													.split(",")[0]), Double
											.parseDouble(locationInfo
													.split(",")[1]), Double
											.parseDouble(mlon), Double
											.parseDouble(mlat)) < ef.getScope()) {
								// 进入学校

							} else {
								// 离开学校
								serialNum.setEf("2");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
								info = info + "03";
								// 返回消息给终端
								returnMessage(info, ctx, end);
							}
						} else if ("2".equals(ef.getModel())) {
							if (FNUtil
									.GetShortDistance(Double
											.parseDouble(locationInfo
													.split(",")[0]), Double
											.parseDouble(locationInfo
													.split(",")[1]), Double
											.parseDouble(mlon), Double
											.parseDouble(mlat)) < ef.getScope()) {
								// 进入学校
							} else {
								// 离开学校
								serialNum.setEf("2");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
							}
						}
					} else if ("2".equals(serialNum.getEf())) {
						// 在危险区域
						if ("0".equals(ef.getModel())
								|| "2".equals(ef.getModel())) {
							if (FNUtil
									.GetShortDistance(Double
											.parseDouble(locationInfo
													.split(",")[0]), Double
											.parseDouble(locationInfo
													.split(",")[1]), Double
											.parseDouble(mlon), Double
											.parseDouble(mlat)) < ef.getScope()) {
								// 进入学校
								serialNum.setEf("1");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
								info = info + "02";
								// 返回消息给终端
								returnMessage(info, ctx, end);
							} else {
								// 离开学校
							}
						} else if ("1".equals(ef.getModel())) {
							if (FNUtil
									.GetShortDistance(Double
											.parseDouble(locationInfo
													.split(",")[0]), Double
											.parseDouble(locationInfo
													.split(",")[1]), Double
											.parseDouble(mlon), Double
											.parseDouble(mlat)) < ef.getScope()) {
								// 进入学校
								serialNum.setEf("1");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
							}
						}
					}
				}
			} else if (list.size() == 2) {
				List<ElectFence> lists = new ArrayList<ElectFence>();
				// 家
				ElectFence ef1 = list.get(0);
				// 学校
				ElectFence ef2 = list.get(1);
				if (ef1.getAreaNum() == 1) {
					lists.add(ef1);
					lists.add(ef2);
				} else {
					lists.add(ef2);
					lists.add(ef1);
				}
				if ("3".equals(serialNum.getEf())) {
					System.out.println("okokokokok");
					if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
						// 发送在家的指令给终端
						serialNum.setEf("0");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
					} else if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
						// 发送在学校的指令给终端
						serialNum.setEf("1");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
					} else {
						// 发送在危险区域的指令给终端
						serialNum.setEf("2");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
					}
				} else if ("0".equals(serialNum.getEf())) {
					// 在家
					if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
						// 发送在家的指令给终端
					} else if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
						// 发送在学校的指令给终端
						serialNum.setEf("1");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
					} else {
						// 发送在危险区域的指令给终端
						serialNum.setEf("2");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
						if ("0".equals(lists.get(0).getModel())) {
							// 离开进入模式
							info = info + "01";
							returnMessage(info, ctx, end);
						} else if ("1".equals(lists.get(0).getModel())) {
							// 离开模式
							info = info + "01";
							returnMessage(info, ctx, end);
						} else if ("2".equals(lists.get(0).getModel())) {
							// 进入模式
						}
					}
				} else if ("1".equals(serialNum.getEf())) {
					// 在学校
					if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
						// 发送在家的指令给终端
						serialNum.setEf("0");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
					} else if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
						// 发送在学校的指令给终端

					} else {
						// 发送在危险区域的指令给终端
						serialNum.setEf("2");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
						if ("0".equals(lists.get(1).getModel())) {
							// 离开进入模式
							info = info + "03";
							returnMessage(info, ctx, end);
						} else if ("1".equals(lists.get(1).getModel())) {
							// 离开模式
							info = info + "03";
							returnMessage(info, ctx, end);
						} else if ("2".equals(lists.get(1).getModel())) {
							// 进入模式
						}
					}
				} else if ("2".equals(serialNum.getEf())) {
					// 在危险区域
					if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
						// 发送在家的指令给终端
						serialNum.setEf("0");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
						if ("0".equals(lists.get(0).getModel())) {
							// 离开进入模式
							info = info + "04";
							returnMessage(info, ctx, end);
						} else if ("1".equals(lists.get(0).getModel())) {
							// 离开模式

						} else if ("2".equals(lists.get(0).getModel())) {
							// 进入模式
							info = info + "04";
							returnMessage(info, ctx, end);
						}
					} else if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
						// 发送在学校的指令给终端
						serialNum.setEf("1");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
						if ("0".equals(lists.get(1).getModel())) {
							// 离开进入模式
							info = info + "02";
							returnMessage(info, ctx, end);
						} else if ("1".equals(lists.get(1).getModel())) {
							// 离开模式
						} else if ("2".equals(lists.get(1).getModel())) {
							// 进入模式
							info = info + "02";
							returnMessage(info, ctx, end);
						}
					} else {
						// 发送在危险区域的指令给终端
					}
				}
			}
		}

	}

	private static void returnMessage(String info, ChannelHandlerContext ctx,
			String end) {
		byte[] b2 = HexString2Bytes(info);
		String checksum = Integer.toHexString(CRC_XModem(b2)).toUpperCase();
		System.out.println("checksum1:" + checksum);
		if (checksum.length() == 4) {

		} else if (checksum.length() == 3) {
			checksum = "0" + checksum;
		} else if (checksum.length() == 2) {
			checksum = "00" + checksum;
		} else if (checksum.length() == 1) {
			checksum = "000" + checksum;
		} else {
			checksum = checksum.substring(0, 4);
		}
		System.out.println("checksum2:" + checksum);
		byte b[] = HexString2Bytes(info + checksum + end);
		System.out.println("b.toString:" + Arrays.toString(b));
		System.out.println("b.length:" + b.length);
		System.out.println("send_message:" + Hex.byteToArray(b));
		/*
		 * 对于消息发送，通常需要用户自己构造ByteBuf并编码， 例如通过如下工具类Unpooled创建消息发送缓冲区
		 * 使用非堆内存分配器创建的直接内存缓冲区
		 */
		// ByteBuf buf=Unpooled.buffer(b.length);
		// buf.writeBytes(b);
		// ctx.writeAndFlush(buf);
		/*
		 * 使用内存池分配器创建直接内存缓冲区
		 */
		// ByteBuf buf=PooledByteBufAllocator.DEFAULT.directBuffer(b.length);
		// buf.writeBytes(b);
		// buf.release();
		// ctx.writeAndFlush(buf);

		logger.info(String.format("Yang Send SMS sent:%s", info + checksum + end));
		
		ctx.writeAndFlush(b);
	}
	// 从十六进制字符串到字节数组转换
		private static byte[] HexString2Bytes(String hexstr) {
			byte[] b = new byte[hexstr.length() / 2];
			int j = 0;
			for (int i = 0; i < b.length; i++) {
				char c0 = hexstr.charAt(j++);
				char c1 = hexstr.charAt(j++);
				b[i] = (byte) ((parse(c0) << 4) | parse(c1));
			}
			return b;
		}

		private static int parse(char c) {
			if (c >= 'a')
				return (c - 'a' + 10) & 0x0f;
			if (c >= 'A')
				return (c - 'A' + 10) & 0x0f;
			return (c - '0') & 0x0f;
		}

		public static int CRC_XModem(byte[] bytes) {
			int crc = 0x00; // initial value
			int polynomial = 0x1021;
			for (int index = 0; index < bytes.length; index++) {
				byte b = bytes[index];
				for (int i = 0; i < 8; i++) {
					boolean bit = ((b >> (7 - i) & 1) == 1);
					boolean c15 = ((crc >> 15 & 1) == 1);
					crc <<= 1;
					if (c15 ^ bit)
						crc ^= polynomial;
				}
			}
			crc &= 0xffff;
			return crc;
		}
}
