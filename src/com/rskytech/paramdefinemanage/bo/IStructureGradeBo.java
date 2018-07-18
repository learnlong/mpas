package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public interface IStructureGradeBo extends IBo{

	public List<Object[]> getCusIntervalList(Object[] obs)
	throws BusinessException;
	
	public void saveS6All( ComUser user,ComModelSeries comModelSeries, String jsonData) throws BusinessException;
	
	public void deleteAll(String deleteIdAint, String deleteIdBout,
			String jsonData, ComUser user,ComModelSeries comModelSeries) throws BusinessException;
	
	
	
	
}
