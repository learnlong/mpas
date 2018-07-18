package com.rskytech.sys.bo;


import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M11;

public interface IM11Bo extends IBo {
	/**
	 * 根据MSI查询M11
	 * @param msiId
	 * @return
	 * @author chendexu
	 * createdate 2012-08-21
	 */
	public M11 getM11ByMsiId(String msiId)throws BusinessException ;

    public void saveM11(M11 m11,ComUser user,String operateFlg,String pageId,String msiId,ComModelSeries comModelSeries);
}
