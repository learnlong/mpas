package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.TaskMsg;

public interface IS7Dao extends IDAO {
	/**
	 * 得到S7grid页面需要的数据
	 * @param ssiId 组成ID
	 * @return 任务列表
	 * @throws BusinessException
	 */
	public List<TaskMsg> getS7Records(String ssiId) throws BusinessException;
	
	 /**
	  * 查询结构专区与的数据
	  * @param needTransfer 是否转区域
	  * @param hasAcceptt 区域是否接受
	  * @param Page 分页信息
	  * @param modelId 机型系列ID
	  * @return
	  * @throws BusinessException
	  * @author zhaotao
	  * @createTIme 2012年10月9日
	  */
	 public Page getStructToAreaRecords(Integer needTransfer,Integer hasAccept,Page page,String modelId) throws BusinessException;
}
