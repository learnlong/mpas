package com.rskytech.struct.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.bo.IS8Bo;
import com.rskytech.struct.dao.IS8Dao;
/**
 * 
 * @author 赵涛
 *
 */
public class S8Bo extends BaseBO implements IS8Bo {
	
	
	private IS8Dao s8Dao;
	@Override
	public List<TaskMrb> getMrbRecords(String ssiId,String modelId) throws BusinessException {
		List<TaskMrb> tmlist=this.s8Dao.getMrbRecords(ssiId, modelId);
		if(tmlist!=null&&!tmlist.isEmpty()){
			return tmlist;
		}
		return null;
	}

	
	/**
	 * 得到S7grid页面需要的数据
	 * @param ssiId 组成ID
	 * @return 任务列表
	 * @throws BusinessException
	 *  @author 赵涛
	 *  @createdate 2012年8月30日
	 */
	@Override
	public List<TaskMsg> getS8Records(String ssiId,String step) throws BusinessException {
		List<TaskMsg> list=this.s8Dao.getS8Records(ssiId, step);
		if(list!=null&&!list.isEmpty()){
			return list;
		}
		return null;
	}

	@Override
	public List<TaskMrb> getMrbCountByCode(String string,ComModelSeries model) throws BusinessException {
		return this.s8Dao.getTaskMrbByTaskCode(string, model);
	}

	@Override
	public TaskMpd getMpdByCode(String mpdCode,ComModelSeries model) throws BusinessException {
		List<TaskMpd> list = this.s8Dao.getMpdByCode(mpdCode, model);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public IS8Dao getS8Dao() {
		return s8Dao;
	}

	public void setS8Dao(IS8Dao s8Dao) {
		this.s8Dao = s8Dao;
	}

}
