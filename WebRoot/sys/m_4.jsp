<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/sys_headerstep.jsp"%>
<script type="text/javascript" src="${contextPath}/js/extjs/multicombo.js"></script>
<script type="text/javascript">
	var msiId = '${msiId}';
	var isCreateUser = '${isCreateUser}';
	var taskId;
	var m4Id;

</script>
<script src="${contextPath}/sys/sysintertree.js"></script>
<style type="text/css">
#myDiv table {
	border-left: 1px solid #C1DAD7;
	border-top: 1px solid #C1DAD7;
	background-color: #DFE8F6;
}

#myDiv td {
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	background-color: #DFE8F6;
	font-size: 12px;
	padding: 3px 3px 3px 4px;
	color: #4f6b72;
}
</style>
<div id="myDiv" style="overflow: auto;">
	<table width="100%" border="1" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="2" align="left">
				<b id="M_4_effectiveness">有效性：</b><b id="effectiveness"></b>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="left">
				<b id="M_4_taskCode">工作号：</b><b id="taskCode"></b>
			</td>
		</tr>
		<tr>
			<td  width="20%" align="center">
				<b id="M_4_ana">分析结果</b>
			</td>
			<td width="80%">
				<font id= "ana_td"></font>
			</td>
		</tr>
		<tr>
			<td align="center"  >
				<b id="M_4_similar">类似机型情况</b>
			</td>
			<td id= "similar_td">
		
			</td>
		</tr>
		<tr>
			<td align="center">
				<b id="M_4_engineerReview">系统工程师评论结果</b>
			</td>
			<td>
				<textarea name="engineerReview" id="engineerReview" rows="3" cols="50"></textarea>
			</td>
		</tr>
		<tr>
			<td align="center">
				<b id="M_4_engineerSuggest">系统工程师建议</b>
			</td>
			<td>
				<textarea name="engineerSuggest" id="engineerSuggest" rows="3" cols="50"></textarea>
			</td>
		</tr>
		<tr>
			<td align="center">
				<b id="M_4_groupReview">局方建议</b>
			</td>
			<td>
				<textarea name="groupReview" id="groupReview"rows="3" cols="50"></textarea>
			</td>
		</tr>
		<tr>
			<td align="center">
				<b id="M_4_other">其他</b>
			</td>
			<td>
				<textarea name="other" id="other" rows="3" cols="50" ></textarea>
			</td>
		</tr>
		<tr>
			<td align="center">
				<b id="M_4_remark">备注</b>
			</td>
			<td>
				<textarea name="remark" id="remark" rows="3" cols="50" ></textarea>
			</td>
		</tr>
		<tr>
			<td align="center">
				<b id="M_4_taskDesc">任务说明</b>
			</td>
			<td id="taskDesc">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td align="center">
				<b id="M_4_result">故障影响类别</b>
			</td>
			<td id="result">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td align="center">
				<b id="M_4_taskInterval">间隔<font style='font-weight: bold;color : red' >&nbsp;*</font></b>
			</td>
			<td  id = "taskInterval_td">
				
			</td>
		</tr>
	</table>
</div>
<script src="${contextPath}/sys/m_4.js"></script>
