package com.zhuika.entity;

public class ElectFence implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String serialNumber;
	private int areaNum;
	private String name;
	private int scope;
	private String model;
	private String locationbd;
	private String locationgd;
	private String createTime;
	private String updateTime;
	private String status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public int getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(int areaNum) {
		this.areaNum = areaNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getLocationbd() {
		return locationbd;
	}
	public void setLocationbd(String locationbd) {
		this.locationbd = locationbd;
	}
	public String getLocationgd() {
		return locationgd;
	}
	public void setLocationgd(String locationgd) {
		this.locationgd = locationgd;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ElectFence [id=" + id + ", serialNumber=" + serialNumber
				+ ", areaNum=" + areaNum + ", name=" + name + ", scope="
				+ scope + ", model=" + model + ", locationbd=" + locationbd
				+ ", locationgd=" + locationgd + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", status=" + status + "]";
	}
	
}
