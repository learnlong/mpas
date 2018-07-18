package com.rskytech.struct.bo;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;

public interface IUnSsiBo extends IBo  {
	
	
	/**
	 *  获取不需要分析的ssi任务数据
	 * @param ssiId
	 * @param modelSeriesId
	 * @return
	 */
	public JSONObject getUnssiRecords(String ssiId,String modelSeriesId);
	
	public void saveUnSsiData(String jsonData,String ssiId,String modelSeriesId,String userId);
}
