<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>

<!-- 导入js文件 -->
<script type="text/javascript"> 
</script>
<script src="${contextPath}/paramDefineManage/structureGrade.js"></script>
<head>
   
   <title>自定义结构评级</title>
  </head>
  <script type="text/javascript">
  	Ext.apply(structureGrade.app, {
		 loadCusIntSUrl : "${contextPath}/paramDefineManage/structureGrade/loadS6.do",
		 saveCusIntervalUrl :"${contextPath}/paramDefineManage/structureGrade/saveS6.do",
		 delCusIntlUrl : "${contextPath}/paramDefineManage/structureGrade/delete.do"
	});
	   //开始运行
	  Ext.onReady(structureGrade.app.init,structureGrade.app);	
</script>
  <body>

  </body>

