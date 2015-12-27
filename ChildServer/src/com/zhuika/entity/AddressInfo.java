package com.zhuika.entity;

import java.sql.Timestamp;

public class AddressInfo {

	private String address;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Timestamp getTs() {
		return ts;
	}
	public void setTs(Timestamp ts) {
		this.ts = ts;
	}
	private Timestamp ts ;
}
