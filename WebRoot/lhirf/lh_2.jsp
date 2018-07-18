<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/lh_headerstep.jsp" %>

<script charset="utf-8" src="${contextPath}/kindeditor/kindeditor.js"></script>
	<link rel="stylesheet" href="${contextPath}/kindeditor/themes/default/default.css" />
	<link rel="stylesheet" href="${contextPath}/kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="${contextPath}/kindeditor/plugins/code/prettify.js"></script>
<script src="${contextPath}/lhirf/lh_2.js"></script>
<!-- 导入js文件 -->


<head>
   
   <title>lh2Page</title>
  </head>
  <script type="text/javascript">
  
	
	var hsiId = '${hsiId}';
	var env = '${env}';

      ////参数 设置
  	Ext.apply(lh2.app, {
			// g_lh0ListUrl : "${contextPath}/lhirf/lh_1/sesrchLhHsiList.do",
			
	});
	   //开始运行
	  Ext.onReady(lh2.app.init,lh2.app);	
	  
   </script>
  <body>

  </body>
