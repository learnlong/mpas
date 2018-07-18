<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/sys_headerstep.jsp" %>
<!-- 导入js文件 -->

<script src="${contextPath}/sys/m_11.js"></script>
<script charset="utf-8" src="${contextPath}/kindeditor/kindeditor.js"></script>
<link rel="stylesheet" href="${contextPath}/kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="${contextPath}/kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="${contextPath}/kindeditor/plugins/code/prettify.js"></script>
<!-- 定义全局变量 -->
<script type="text/javascript">
    var msiId='${msiId}';
    var picContent='${picContent}';
    var m11Id='${m11.m11Id}';
    
	Ext.apply(m11.app, {
		 loadM11Url : "${contextPath}/sys/m11/loadM11.do",
		 saveM11Url : "${contextPath}/sys/m11/saveM11.do"
	
	});
	//开始运行  
	Ext.onReady(m11.app.init,m11.app);
</script>