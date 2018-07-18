<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<script src="${contextPath}/baseData/comVendor.js"></script>
<%
String msg=(String)request.getAttribute("msg");
%>
<script type="text/javascript">
	var msg='<%=msg%>';	
</script>
<body>  
  <div><form id="vendorDownloadForm" action="/mpas/baseData/download/vendorDownloadFile.do"></form></div>
  <div><form id="vendorExportForm" action="/mpas/baseData/export/vendorExport.do"></form></div>
</body>