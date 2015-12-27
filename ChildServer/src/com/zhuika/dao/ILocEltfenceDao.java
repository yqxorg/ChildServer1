
package com.zhuika.dao;

import java.util.HashMap;
import java.util.List;

import com.zhuika.entity.LocElectfence;


public interface ILocEltfenceDao {

	void addLocEltfence(LocElectfence locElectfenceInfo) throws Exception;
	
	//不做改变，记录每次每次的上传
	void addLocEltfence_Single(LocElectfence locElectfenceInfo) throws Exception;
		
	List<LocElectfence> listLocElectfence(HashMap<String, String> map) throws Exception; 

}
