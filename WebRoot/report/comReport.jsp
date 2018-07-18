<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<!-- 导入js文件 -->
<script type="text/javascript" src="${contextPath }/report/comReport.js"></script>
<script type="text/javascript">
	var generateId = '${generateId}';
	var reportType = '${reportType}';
	var isMaintain = '${isMaintain}';
	var isMaintainFlage = false;
	if(isMaintain != null && "1" == isMaintain ){
		isMaintainFlage = true;
	}
	var wordOrExcel = "word";
	var showTitle = '报告管理';
	if(reportType == 'MPD' || reportType == 'MRB'){
		showTitle = reportType + showTitle;
		wordOrExcel = "excel";
	}
	var curUserId = '${SESSION_USER_KEY.userId}';
	var curUserName = '${SESSION_USER_KEY.userName}';
	var status_code_new = '<%=ComacConstants.REPORT_STATUS_NEW%>';
	var status_show_new = '<%=ComacConstants.REPORT_STATUS_NEW_SHOW%>';
	function downloadReport(value, reportNow){
		if(value==null||value.trim()==""){
			alert("在下载报告之前，请先保存当前记录");
			return;
		}
		Ext.Ajax.request({
			url : contextPath + "/com/report/reportIsExist.do",
			method : "post",
			params:{
				reportId : value,
				reportType : reportNow
			},
			success:function(response, action) {
				if(response.responseText){
					alert("文件不存在");
					return ;
				}
				location.href=contextPath + "/com/report/downloadReport.do?reportId="+value+"&reportType="+reportNow;
			},
			failure : function(response, action) {
				alert("下载文件超时");
			}
		});
	}
</script>
<body> 
  <div id="windowDiv">
	<div id="reportGrid" align="left"></div>
  </div>
</body>
   