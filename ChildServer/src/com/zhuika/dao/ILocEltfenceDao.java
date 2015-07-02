
package com.zhuika.dao;

import java.util.HashMap;
import java.util.List;

import com.zhuika.entity.LocElectfence;


public interface ILocEltfenceDao {

	void addLocEltfence(LocElectfence locElectfenceInfo) throws Exception;
	
	List<LocElectfence> listLocElectfence(HashMap<String, String> map) throws Exception; 

}
