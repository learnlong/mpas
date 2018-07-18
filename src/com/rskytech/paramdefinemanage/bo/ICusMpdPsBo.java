package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.CusMpdPs;

public interface ICusMpdPsBo extends IBo{
	
	public CusMpdPs findById(String id) throws BusinessException ;

	public void deletePsFile(String urlString) throws BusinessException;
	public List<CusMpdPs> findByModelSeriesId(String id) throws BusinessException;
	public boolean isPsFlgUnique(String modelSeriesId, String cusMpdPsId) throws BusinessException ;
	public boolean saveMpdPs(CusMpdPs ps, String operateFlg, String userid) throws BusinessException;
	public boolean deleteMpdPsById(Class clazz, String id, String userid) throws BusinessException;
}
