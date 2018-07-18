<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp"%>
<!-- 导入js文件 -->
<script src="${contextPath}/struct/structToArea.js"></script>
<!-- 定义全局变量 -->
<script type="text/javascript">
	//开始运行  
		Ext.apply(extjsui.app, {
		 findSysTaskUrl : "${contextPath}/task/transfer/findStructTask.do",
		 ssiComboUrl : "${contextPath}/task/transfer/ssiCombo.do"
	
	});
	Ext.onReady(extjsui.app.init,extjsui.app);
</script>