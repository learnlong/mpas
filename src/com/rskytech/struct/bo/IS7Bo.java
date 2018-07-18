package com.rskytech.struct.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComUser;
/**
 * 
 * @author 赵涛
 *
 */
public interface IS7Bo extends IBo {
	
	/**
	 * 得到S7grid页面需要的数据
	 * @param ssiId 组成ID
	 * @return 任务列表
	 * @throws BusinessException
	 *  @author 赵涛
	 *  @createdate 2012年8月30日
	 */
	public List<HashMap> getS7Records(String ssiId) throws BusinessException;
	
	/**
	 *解析字符串里面的数字
	 * @param str 传进来的字符串
	 * @return
	 */
	 public  Integer getNumsFromStr(String str) throws BusinessException;
	 
	 
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
	 /**
	  * 保存s7数据
	  * @param jsonData
	  * @param ssiId
	  * @param user
	  * @param remarkId
	  * @param remark
	  * @param modelSeriesId
	  * @return
	  * @throws BusinessException
	  */
	 public ArrayList<String> saveS7Records(String jsonData,String ssiId,ComUser user,String remarkId,String remark,String modelSeriesId) throws BusinessException;
}
