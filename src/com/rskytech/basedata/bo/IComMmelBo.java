package com.rskytech.basedata.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComMmel;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public interface IComMmelBo extends IBo {

	/**
	 * 通过机型ID，查询MMEL信息
	 * @param msId 机型ID
	 * @return MMEL列表
	 * @author zhangjianmin
	 */
	public List<ComMmel> getMmelList(String msId);
	
	/**
	 * 通过机型ID，查询MMEL信息
	 * @param msId 机型ID
	 * @return MMEL信息
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> loadMmelList(String msId);
	
	/**
	 * 新增和修改MMEL操作
	 * @param user 用户实例
	 * @param ms 机型实例
	 * @param jsonData 需要操作的数据集合
	 * @return 
	 * @author zhangjianmin
	 */
	public String newOrUpdateMmel(ComUser user, ComModelSeries ms, String jsonData);
}
