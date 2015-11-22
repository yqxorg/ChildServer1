package com.zhuika.dao;

import com.zhuika.entity.Info;

public interface IInfoDao {
	void addInfo(Info info);
	void updateInfo(Info info);
	Info findBySerialNumber(String serialNumber);
}
