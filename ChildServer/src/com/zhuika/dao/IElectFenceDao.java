package com.zhuika.dao;

import java.util.List;

import com.zhuika.entity.ElectFence;


public interface IElectFenceDao {
	void save(ElectFence ef);
	void update(ElectFence ef);
	List<ElectFence> findBySerialNumber(String serialNumber);
	void delete(ElectFence ef);
	ElectFence findBySerialNumber(String serialNumber,String areanum);
}
