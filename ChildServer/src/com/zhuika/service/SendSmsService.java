package com.zhuika.service;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
	//���µ����к�λ�ü�Χ����Ϣ
	private static HashMap<String, LocElectfence> m_hashLocElt = new HashMap<String, LocElectfence>();	  
	static Logger logger = Logger.getLogger(SendSmsService.class);
	
	//���仯״̬
	private static int CheckExist(LocElectfence locElt,ElectFence efItem){
		
		String skey = locElt.getFserialnumber()+"_"+efItem.getAreaNum();		
		
		if(m_hashLocElt.containsKey(skey))
		{
			LocElectfence locEltHs = m_hashLocElt.get(skey);
			
			
			Double d1 = locEltHs.getFdistance() - locEltHs.getFeltscope();
			Double d2 = locElt.getFdistance() - locElt.getFeltscope();
			
			//��Χ�����Χ����
			if(d1>0 && d2<0)
			{
				return 1;
			}
			//��Χ���ڵ�Χ����
			else if(d1<0 && d2>0)
			{
				return 3;
			}
			//һֱ��Χ����
			else if(d1>0 && d2>0)
			{
				return 4;
			}
			//һֱ��Χ����
			else if(d1<0 && d2<0)
			{
				return 5;
			}
			
		}
		return 0;
		
	}
	
	//����λ�ã�ʹ�ö�����,yangqinxu
	private static void SaveLocationSTable(List<ElectFence> list, String serialNumber,String Address,String mlon,String mlat)
	{
		if (list.isEmpty())
		{
			try{
				
				//��ȡ֮ǰ���µ������������仯�Ա�
				HashMap<String, String> mapCondition = new HashMap<String, String>();
				mapCondition.put("FSerialnumber", serialNumber);
				List<LocElectfence> listLocElt =  locEltdao.listLocElectfence(mapCondition);
				
				String premlon = "";
				String premlat="";
				Double DistanceItem = 0D;
				//��Χ�����������¼ǰ�����εľ����
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
				locEltdao.addLocEltfence(locElt);
			}
			catch(Exception ex)
			{
				logger.error("isEmpty"+ex.getMessage());
				System.out.println("isEmpty"+ex.getMessage());
			}
			
			return ;
		}
		//yangqinxu ��������Χ����¼  begin 
		try{
			
			for(int i=0;i<list.size();i++)
			{
				ElectFence efItem = list.get(i);
				
				//��ȡ֮ǰ���µ������������仯�Ա�
				HashMap<String, String> mapCondition = new HashMap<String, String>();
				mapCondition.put("FSerialnumber", serialNumber);
				List<LocElectfence> listLocElt =  locEltdao.listLocElectfence(mapCondition);
				if(listLocElt !=null && listLocElt.size()>0)
				{
					String skey = serialNumber+"_"+efItem.getAreaNum();						
					m_hashLocElt.put(skey, listLocElt.get(0));						
				}
				
				//ȡ�õ���Χ����γ��
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
					locElt.setFremark(String.format("ǰ�����ζ�λ��Χ�����Χ����,Χ�����:%s,Χ������:%s", efItem.getAreaNum(),efItem.getName()));
				}
				else if(datastatus==3)
				{
					locElt.setFremark(String.format("ǰ�����ζ�λ��Χ���ڵ�Χ����,Χ�����:%s,Χ������:%s", efItem.getAreaNum(),efItem.getName()));
				}
				else if(datastatus==4)
				{
					locElt.setFremark(String.format("ǰ�����ζ�λһֱ��Χ����,Χ�����:%s,Χ������:%s", efItem.getAreaNum(),efItem.getName()));
				}
				else if(datastatus==5)
				{
					locElt.setFremark(String.format("ǰ�����ζ�λһֱ��Χ����,Χ�����:%s,Χ������:%s", efItem.getAreaNum(),efItem.getName()));
				}									
				
				locElt.setFdatastatus(datastatus);
				
				locEltdao.addLocEltfence(locElt);										
			}					
		} 
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
			System.out.println(ex.getMessage());
		}
		//yangqinxu ��������Χ����¼  end 
	}
	public static void controlSMS(ChannelHandlerContext ctx, String serialNumber,
			SerialNumber serialNum, String locationbd, String end,String Address)
			throws DAOException {
		List<ElectFence> list = electFenceDao.findBySerialNumber(serialNumber);
		
		String mlon = locationbd.split(",")[0];
		String mlat = locationbd.split(",")[1];
		
		///yangqinxu λ�ü���������Χ����¼  
		SaveLocationSTable(list,serialNumber,Address,mlon,mlat);
		
		if (list.isEmpty()) {
			System.out.println("û�����õ���Χ��");
			
			
		} else {
//			String mlon = locationbd.split(",")[0];
//			String mlat = locationbd.split(",")[1];
			String info = "40400012" + serialNumber + "9993";
			
			//yangqinxu ��������Χ����¼  begin 
			
			//SaveLocationSTable(list,serialNumber,Address,mlon,mlat);
			
//			try{
//				for(int i=0;i<list.size();i++)
//				{
//					ElectFence efItem = list.get(i);
//					
//					//��ȡ֮ǰ���µ������������仯�Ա�
//					HashMap<String, String> mapCondition = new HashMap<String, String>();
//					mapCondition.put("FSerialnumber", serialNumber);
//					List<LocElectfence> listLocElt =  locEltdao.listLocElectfence(mapCondition);
//					if(listLocElt !=null && listLocElt.size()>0)
//					{
//						String skey = serialNumber+"_"+efItem.getAreaNum();						
//						m_hashLocElt.put(skey, listLocElt.get(0));						
//					}
//					
//					//ȡ�õ���Χ����γ��
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
//						locElt.setFremark(String.format("��Χ�����Χ����,Χ�����:%s,Χ������:%s", efItem.getAreaNum(),efItem.getName()));
//					}
//					else if(datastatus==3)
//					{
//						locElt.setFremark(String.format("��Χ���ڵ�Χ����,Χ�����:%s,Χ������:%s", efItem.getAreaNum(),efItem.getName()));
//					}
//					else if(datastatus==4)
//					{
//						locElt.setFremark(String.format("һֱ��Χ����,Χ�����:%s,Χ������:%s", efItem.getAreaNum(),efItem.getName()));
//					}
//					else if(datastatus==5)
//					{
//						locElt.setFremark(String.format("һֱ��Χ����,Χ�����:%s,Χ������:%s", efItem.getAreaNum(),efItem.getName()));
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
			//yangqinxu ��������Χ����¼  end 
			if (list.size() == 1) {
				ElectFence ef = list.get(0);
				System.out.println("areaNum" + ef.getAreaNum());
				String locationInfo = ef.getLocationbd();
				if (1 == ef.getAreaNum()) {
					// ��
					System.out.println("locationInfo:" + locationInfo);
					if ("3".equals(serialNum.getEf())) {
						if (FNUtil.GetShortDistance(
								Double.parseDouble(locationInfo.split(",")[0]),
								Double.parseDouble(locationInfo.split(",")[1]),
								Double.parseDouble(mlon),
								Double.parseDouble(mlat)) < ef.getScope()) {
							// �ڼ�
							serialNum.setEf("0");
							serialNum.setSerialNumber(serialNumber);
							serialNum.setStatus(serialNum.getStatus());
							sndao.updateSerialNumber(serialNum);
						} else {
							// ��Σ������
							serialNum.setEf("2");
							serialNum.setSerialNumber(serialNumber);
							serialNum.setStatus(serialNum.getStatus());
							sndao.updateSerialNumber(serialNum);
						}
					} else if ("0".equals(serialNum.getEf())) {
						// �Ѿ��ڼ�
						// 0 �뿪����ģʽ 1�뿪ģʽ 2����ģʽ
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
								// �������

							} else {
								// �뿪����
								serialNum.setEf("2");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
								info = info + "01";
								// ������Ϣ���ն�
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
								// �������

							} else {
								// �뿪����
								serialNum.setEf("2");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
							}
						}
					} else if ("2".equals(serialNum.getEf())) {
						// �Ѿ���Σ������
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
								// �������
								serialNum.setEf("0");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
								info = info + "04";
								// ������Ϣ���ն�
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
								// �������
								serialNum.setEf("0");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
							}
						}
					}
				} else if (2 == ef.getAreaNum()) {
					// ѧУ
					if ("3".equals(serialNum.getEf())) {
						if (FNUtil.GetShortDistance(
								Double.parseDouble(locationInfo.split(",")[0]),
								Double.parseDouble(locationInfo.split(",")[1]),
								Double.parseDouble(mlon),
								Double.parseDouble(mlat)) < ef.getScope()) {
							// ������ѧУ��ָ����ն�
							serialNum.setEf("1");
							serialNum.setSerialNumber(serialNumber);
							serialNum.setStatus(serialNum.getStatus());
							sndao.updateSerialNumber(serialNum);
						} else {
							// ������Σ�������ָ����ն�
							serialNum.setEf("2");
							serialNum.setSerialNumber(serialNumber);
							serialNum.setStatus(serialNum.getStatus());
							sndao.updateSerialNumber(serialNum);
						}
					} else if ("1".equals(serialNum.getEf())) {
						// ��ѧУ
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
								// ����ѧУ

							} else {
								// �뿪ѧУ
								serialNum.setEf("2");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
								info = info + "03";
								// ������Ϣ���ն�
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
								// ����ѧУ
							} else {
								// �뿪ѧУ
								serialNum.setEf("2");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
							}
						}
					} else if ("2".equals(serialNum.getEf())) {
						// ��Σ������
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
								// ����ѧУ
								serialNum.setEf("1");
								serialNum.setSerialNumber(serialNumber);
								serialNum.setStatus(serialNum.getStatus());
								sndao.updateSerialNumber(serialNum);
								info = info + "02";
								// ������Ϣ���ն�
								returnMessage(info, ctx, end);
							} else {
								// �뿪ѧУ
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
								// ����ѧУ
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
				// ��
				ElectFence ef1 = list.get(0);
				// ѧУ
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
						// �����ڼҵ�ָ����ն�
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
						// ������ѧУ��ָ����ն�
						serialNum.setEf("1");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
					} else {
						// ������Σ�������ָ����ն�
						serialNum.setEf("2");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
					}
				} else if ("0".equals(serialNum.getEf())) {
					// �ڼ�
					if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
						// �����ڼҵ�ָ����ն�
					} else if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
						// ������ѧУ��ָ����ն�
						serialNum.setEf("1");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
					} else {
						// ������Σ�������ָ����ն�
						serialNum.setEf("2");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
						if ("0".equals(lists.get(0).getModel())) {
							// �뿪����ģʽ
							info = info + "01";
							returnMessage(info, ctx, end);
						} else if ("1".equals(lists.get(0).getModel())) {
							// �뿪ģʽ
							info = info + "01";
							returnMessage(info, ctx, end);
						} else if ("2".equals(lists.get(0).getModel())) {
							// ����ģʽ
						}
					}
				} else if ("1".equals(serialNum.getEf())) {
					// ��ѧУ
					if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
						// �����ڼҵ�ָ����ն�
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
						// ������ѧУ��ָ����ն�

					} else {
						// ������Σ�������ָ����ն�
						serialNum.setEf("2");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
						if ("0".equals(lists.get(1).getModel())) {
							// �뿪����ģʽ
							info = info + "03";
							returnMessage(info, ctx, end);
						} else if ("1".equals(lists.get(1).getModel())) {
							// �뿪ģʽ
							info = info + "03";
							returnMessage(info, ctx, end);
						} else if ("2".equals(lists.get(1).getModel())) {
							// ����ģʽ
						}
					}
				} else if ("2".equals(serialNum.getEf())) {
					// ��Σ������
					if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(0).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(0).getScope()) {
						// �����ڼҵ�ָ����ն�
						serialNum.setEf("0");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
						if ("0".equals(lists.get(0).getModel())) {
							// �뿪����ģʽ
							info = info + "04";
							returnMessage(info, ctx, end);
						} else if ("1".equals(lists.get(0).getModel())) {
							// �뿪ģʽ

						} else if ("2".equals(lists.get(0).getModel())) {
							// ����ģʽ
							info = info + "04";
							returnMessage(info, ctx, end);
						}
					} else if (FNUtil.GetShortDistance(
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[0]),
							Double.parseDouble(lists.get(1).getLocationbd()
									.split(",")[1]), Double.parseDouble(mlon),
							Double.parseDouble(mlat)) < lists.get(1).getScope()) {
						// ������ѧУ��ָ����ն�
						serialNum.setEf("1");
						serialNum.setSerialNumber(serialNumber);
						serialNum.setStatus(serialNum.getStatus());
						sndao.updateSerialNumber(serialNum);
						if ("0".equals(lists.get(1).getModel())) {
							// �뿪����ģʽ
							info = info + "02";
							returnMessage(info, ctx, end);
						} else if ("1".equals(lists.get(1).getModel())) {
							// �뿪ģʽ
						} else if ("2".equals(lists.get(1).getModel())) {
							// ����ģʽ
							info = info + "02";
							returnMessage(info, ctx, end);
						}
					} else {
						// ������Σ�������ָ����ն�
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
		 * ������Ϣ���ͣ�ͨ����Ҫ�û��Լ�����ByteBuf�����룬 ����ͨ�����¹�����Unpooled������Ϣ���ͻ�����
		 * ʹ�÷Ƕ��ڴ������������ֱ���ڴ滺����
		 */
		// ByteBuf buf=Unpooled.buffer(b.length);
		// buf.writeBytes(b);
		// ctx.writeAndFlush(buf);
		/*
		 * ʹ���ڴ�ط���������ֱ���ڴ滺����
		 */
		// ByteBuf buf=PooledByteBufAllocator.DEFAULT.directBuffer(b.length);
		// buf.writeBytes(b);
		// buf.release();
		// ctx.writeAndFlush(buf);

		logger.info(String.format("Yang Send SMS sent:%s", info + checksum + end));
		
		ctx.writeAndFlush(b);
	}
	// ��ʮ�������ַ������ֽ�����ת��
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