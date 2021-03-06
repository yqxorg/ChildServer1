
package com.zhuika.entity;

/**
 * <p>Title: ejb title </p>
 * <p>Description: t_loc_electfence MODEL 处理类</p>
 * @author yangqinxu 电话：137****5317
 * @version 1.0 时间  2015-6-23 18:48:43
 */
public class LocElectfence  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String flocfenid;
	private Integer fincreaseid;
	private Integer feltfenceid;
	private String fserialnumber;
	private Integer fdatastatus;
	private Integer ffieldstatus;
	private String feltlongitude;
	private String feltlatitude;
	private Double feltscope;
	private String flongitude;
	private String flatitude;
	private Double fdistance;
	private java.sql.Timestamp faddtime;
	private java.sql.Timestamp fupdatetime;
	private String fremark;	
	private String feltaddress;
	private String faddress;
	private String battery;
	private Integer frecordcount;
    

	public Integer getFrecordcount() {
		return frecordcount;
	}

	public void setFrecordcount(Integer frecordcount) {
		this.frecordcount = frecordcount;
	}

	public String getFlocfenid() {
		return flocfenid;
	}
    
	public void setFlocfenid(String flocfenid) {		     
         this.flocfenid = flocfenid;
	}
	public Integer getFincreaseid() {
		return fincreaseid;
	}
    
	public void setFincreaseid(Integer fincreaseid) {		     
         this.fincreaseid = fincreaseid;
	}
	public Integer getFeltfenceid() {
		return feltfenceid;
	}
    
	public void setFeltfenceid(Integer feltfenceid) {		     
         this.feltfenceid = feltfenceid;
	}
	public String getFserialnumber() {
		return fserialnumber;
	}
    
	public void setFserialnumber(String fserialnumber) {		     
         this.fserialnumber = fserialnumber;
	}
	public Integer getFdatastatus() {
		return fdatastatus;
	}
    
	public void setFdatastatus(Integer fdatastatus) {		     
         this.fdatastatus = fdatastatus;
	}
	public Integer getFfieldstatus() {
		return ffieldstatus;
	}
    
	public void setFfieldstatus(Integer ffieldstatus) {		     
         this.ffieldstatus = ffieldstatus;
	}
	public String getFeltlongitude() {
		return feltlongitude;
	}
    
	public void setFeltlongitude(String feltlongitude) {		     
         this.feltlongitude = feltlongitude;
	}
	public String getFeltlatitude() {
		return feltlatitude;
	}
    
	public void setFeltlatitude(String feltlatitude) {		     
         this.feltlatitude = feltlatitude;
	}
	public Double getFeltscope() {
		return feltscope;
	}
    
	public void setFeltscope(Double feltscope) {		     
         this.feltscope = feltscope;
	}
	public String getFlongitude() {
		return flongitude;
	}
    
	public void setFlongitude(String flongitude) {		     
         this.flongitude = flongitude;
	}
	public String getFlatitude() {
		return flatitude;
	}
    
	public void setFlatitude(String flatitude) {		     
         this.flatitude = flatitude;
	}
	public Double getFdistance() {
		return fdistance;
	}
    
	public void setFdistance(Double fdistance) {		     
         this.fdistance = fdistance;
	}
	public java.sql.Timestamp getFaddtime() {
		return faddtime;
	}
    
	public void setFaddtime(java.sql.Timestamp faddtime) {		     
         this.faddtime = faddtime;
	}
	public java.sql.Timestamp getFupdatetime() {
		return fupdatetime;
	}
    
	public void setFupdatetime(java.sql.Timestamp fupdatetime) {		     
         this.fupdatetime = fupdatetime;
	}
	public String getFremark() {
		return fremark;
	}
    
	public void setFremark(String fremark) {		     
         this.fremark = fremark;
	}
	
	public String getFeltaddress() {
		return feltaddress;
	}
    
	public void setFeltaddress(String feltaddress) {		     
         this.feltaddress = feltaddress;
	}
	
	public String getFaddress() {
		return faddress;
	}
    
	public void setFaddress(String faddress) {		     
         this.faddress = faddress;
	}
	
	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
}

