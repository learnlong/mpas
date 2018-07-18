package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsg;

public interface IUnSsiDao extends IDAO {
	
	/**
	 * 查询非重要结构分析的ssi数据
	 * @param ssiId
	 * @param modelSeriesId
	 * @return
	 */
	public List<TaskMsg> searchUnSsiList(String ssiId,String modelSeriesId);
}
