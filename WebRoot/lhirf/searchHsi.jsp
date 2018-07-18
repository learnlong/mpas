<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<!-- 导入js文件 -->
<script src="${contextPath}/lhirf/searchHsi.js"></script>
<head>
   
   <title>hsiSearch</title>
  </head>
  <script type="text/javascript">
     statusNew = '<%=ComacConstants.ANALYZE_STATUS_NEW%>';
     statusMaintain = '<%=ComacConstants.ANALYZE_STATUS_MAINTAIN%>';
     statusMinOK = '<%=ComacConstants.ANALYZE_STATUS_MAINTAINOK%>';
     statusAPPROVED = '<%=ComacConstants.ANALYZE_STATUS_APPROVED%>';
     statusHOLD = '<%=ComacConstants.ANALYZE_STATUS_HOLD%>';

///开始操作
 	Ext.apply(lhsearch.app, {
			lh_firstNodeListUrl : "${contextPath}/lhirf/lh_search/sesrchAreaNodeList.do",
			lh_getSearchHsiUrl : "${contextPath}/lhirf/lh_search/getSearchHsiList.do"
	});
	   //开始运行
	  Ext.onReady(lhsearch.app.init,lhsearch.app);
	  
   </script>
  <body>

  </body>
