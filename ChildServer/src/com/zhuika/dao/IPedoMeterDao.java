package com.zhuika.dao;

import com.zhuika.entity.PedoMeter;

public interface IPedoMeterDao {
	void addPedoMeter(PedoMeter pedoMeter)throws DAOException;
	void updatePedoMeter(PedoMeter pedoMeter) throws DAOException;
	PedoMeter findBySerialNumber(String serialNumber) throws DAOException;
}
