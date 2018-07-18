package com.rskytech.paramdefinemanage.bo.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.basedata.dao.IComAreaDao;
import com.rskytech.paramdefinemanage.bo.ITaskMpdBo;
import com.rskytech.paramdefinemanage.dao.ITaskMpdDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.TaskMpd;

public class TaskMpdBo  extends BaseBO implements ITaskMpdBo{

	private ITaskMpdDao taskMpdDao;
	
	private IComAreaDao comAreaDao;
	
	public ITaskMpdDao getTaskMpdDao() {
		return taskMpdDao;
	}

	public void setTaskMpdDao(ITaskMpdDao taskMpdDao) {
		this.taskMpdDao = taskMpdDao;
	}

	public IComAreaDao getComAreaDao() {
		return comAreaDao;
	}

	public void setComAreaDao(IComAreaDao comAreaDao) {
		this.comAreaDao = comAreaDao;
	}

	@SuppressWarnings({"unchecked" })
	@Override
	public Map<TaskMpd, String> findTaskMpdList(String modelSeiesId, String SOURCE_SYSTEM) throws BusinessException {
		List list = this.getTaskMpdDao().findByHql(modelSeiesId, SOURCE_SYSTEM);
		Map<TaskMpd, String> rtnMap = new TreeMap<TaskMpd, String>(new Comparator<TaskMpd>() {
			@Override
			public int compare(TaskMpd o1, TaskMpd o2) {
				return (int) (o1.getCreateDate().getTime() - o2.getCreateDate().getTime());
				// return o1.getMpdId() - o2.getMpdId();
			}
		});
		for (Object obj : list) {
			TaskMpd taskMpd = (TaskMpd) obj;
			String areaIds = taskMpd.getOwnArea();
			StringBuffer sbIds = new StringBuffer();
			if(areaIds!=null){
				String[] ids = areaIds.split(",");
				for (int i = 0; i < ids.length; i++) {
					sbIds.append("'"+ids[i]+"',");
				}
			}else{
				sbIds.append("'',");
			}
			List<ComArea> listArea = (List<ComArea>) this.getComAreaDao().getAreasByIds(sbIds.substring(0, sbIds.length()-1));
			String areaCodes = joinAreaCode(listArea);
			rtnMap.put(taskMpd, areaCodes);
		}
		return rtnMap;
	}
	
	/**
	 * 将区域编号用字符串拼接，使用","隔开。
	 * 
	 * @param list
	 * @return
	 */
	private String joinAreaCode(List<ComArea> list) {
		StringBuffer sb = new StringBuffer();
		for (ComArea area : list) {
			sb.append(area.getAreaCode()).append(",");
		}
		if (sb.length() > 0) {
			sb = new StringBuffer(sb.substring(0, sb.length() - 1));
		}
		return sb.toString();
	}
	
}
