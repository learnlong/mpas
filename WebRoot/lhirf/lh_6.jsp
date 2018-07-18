<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/lh_headerstep.jsp" %>
<script src="${contextPath}/lhirf/lh_6.js"></script>

<!-- 导入js文件 -->


<head>
   
   <title>lh6Page</title>
  </head>
  <script type="text/javascript">
  var areaId = '${areaId}';
  var hsiId = '${hsiId}';
  var  NA = '<%=ComacConstants.EMPTY%>';

      //参数 设置
  	Ext.apply(lh6.app, {
			//////   g_lh1aSaveUrl : "${contextPath}/lhirf/lh_6/loadLh6Msg.do",
			
	});
	   //开始运行
	  Ext.onReady(lh6.app.init,lh6.app);	
	  
   </script>
  <body>

  </body>
