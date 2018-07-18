<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/lh_headerstep.jsp" %>
<script src="${contextPath}/lhirf/lh_3.js"></script>
<!-- 导入js文件 -->
<head>
   <title>lh3Page</title>
 </head>
  <script type="text/javascript">
  
  var hsiId = '${hsiId}';
	
	
      ////参数 设置
  	Ext.apply(lh3.app, {
			 g_lh3ListUrl : "${contextPath}/lhirf/lh_3/loadLh3List.do",
			 g_lh3SaveUrl :  "${contextPath}/lhirf/lh_3/saveLh3List.do",
			 g_lh3DeleteUrl : "${contextPath}/lhirf/lh_3/deletelh3.do"
	});
	   //开始运行
	  Ext.onReady(lh3.app.init,lh3.app);	
	  
   </script>
  <body>

  </body>
