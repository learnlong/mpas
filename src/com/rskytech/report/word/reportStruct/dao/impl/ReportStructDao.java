package com.rskytech.report.word.reportStruct.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.word.reportStruct.dao.IReportStructDao;

public class ReportStructDao extends BaseDAO implements IReportStructDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskMsg> getTaskByS1Id(String s1Id,String step,String msId,String inOrOut) {
		String sql="";
		List<TaskMsg> returnTask=new ArrayList<TaskMsg>();
		if("S3".equals(step)){
			sql = "SELECT * FROM Task_Msg t WHERE t.any_content1='"+s1Id+"FD"+"' AND t.source_step='S3' and t.model_series_id='"+msId+"'";
		}
		if("S6".equals(step)){
			sql = "SELECT * FROM Task_Msg t WHERE t.any_content1 ='"+s1Id+"AD/ED"+"' AND t.source_step ='AD/ED' " +
					"and t.model_series_id='"+msId+"' and t.any_Content3='"+inOrOut+"'";
		}
		List<TaskMsg> list = this.executeQueryBySql(sql, TaskMsg.class);
		if(list!=null&&list.size()>0){
			TaskMsg task = list.get(0);
			if(task!=null&&task.getAnyContent2()!=null&&!"".equals(task.getAnyContent2())){
				sql = "SELECT * FROM Task_Msg t WHERE t.task_code='"+task.getAnyContent2()+"' and t.model_series_id='"+msId+"'";
				list = this.executeQueryBySql(sql, TaskMsg.class);
				if(list!=null&&list.size()>0){
					returnTask.addAll(list);
				}
			}
		}
		return returnTask;
	}

}
