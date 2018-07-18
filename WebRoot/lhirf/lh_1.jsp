<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.util.JavaScriptUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/lh_headerstep.jsp" %>

<!-- 导入js文件 -->
<script charset="utf-8" src="${contextPath}/kindeditor/kindeditor.js"></script>
<link rel="stylesheet" href="${contextPath}/kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="${contextPath}/kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="${contextPath}/kindeditor/plugins/code/prettify.js"></script>
<script src="${contextPath}/lhirf/lh_1.js"></script>


<script type="text/javascript">  
	var hsiId='${hsiId}';
	var lheff='${lheff}';
	
      //参数 设置
  	Ext.apply(lh1.app, {
			// g_lh0ListUrl : "${contextPath}/lhirf/lh_1/sesrchLhHsiList.do",			
	});
	//开始运行
	Ext.onReady(lh1.app.init,lh1.app);		  
</script>
