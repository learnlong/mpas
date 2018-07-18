<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<script type="text/javascript">
var language="${SYSTEM_USER_SESSION_KEY.language}";
</script>
  <script src="${contextPath}/common/coordinationOfSign.js"></script>
  <script src="${contextPath}/common/coordinationStrToSys.js"></script>
  <script src="${contextPath}/process/coordination.js"></script>
  <script type="text/javascript">
	Ext.apply(coordination.app, {
		 loadData : "${contextPath}/com/coordination/loadCoordinationData.do"
	});	
	//开始运行  
	Ext.onReady(coordination.app.init,coordination.app);
	</script>
  <body>
  </body>

