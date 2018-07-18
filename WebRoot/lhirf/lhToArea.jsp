<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp"%>
<script src="${contextPath}/lhirf/lhToArea.js"></script>
<!-- 定义全局变量 -->
<script type="text/javascript">
	//开始运行  
		Ext.apply(extjsui.app, {
			 findSysTaskUrl : "${contextPath}/task/transfer/findLhirfTask.do"
	});
	Ext.onReady(extjsui.app.init,extjsui.app);
</script>