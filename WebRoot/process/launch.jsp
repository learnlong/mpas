<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<!-- 导入js文件 -->
<script type="text/javascript">
var language="zh";
</script>
<script type="text/javascript" src="${contextPath }/process/launch.js" ></script>
<script type="text/javascript" src="${contextPath}/language/commonality_language.js"></script>
<link rel="stylesheet" type="text/css" href="${contextPath}/js/extjs/css/icon.css">

 <script type="text/javascript">
     analysis_statusNew = '<%=ComacConstants.ANALYZE_STATUS_NEW%>';
     analysis_statusMIN = '<%=ComacConstants.ANALYZE_STATUS_MAINTAIN%>';
     analysis_statusMINOk = '<%=ComacConstants.ANALYZE_STATUS_MAINTAINOK%>';
     analysis_statusHOLD = '<%=ComacConstants.ANALYZE_STATUS_HOLD%>';

     var zonalCode = '<%=ComacConstants.ZONAL_CODE%>';
     var zonalCodeCn = '<%=ComacConstants.ZONAL_SHOW%>';
     var lhrifCode = '<%=ComacConstants.LHIRF_CODE%>';
     var lhrifCodeCn = '<%=ComacConstants.LHIRF_SHOW%>';
     var structureCode = '<%=ComacConstants.STRUCTURE_CODE%>';
     var structureCodeCn = '<%=ComacConstants.STRUCTURE_SHOW%>';
     var systemCode = '<%=ComacConstants.SYSTEM_CODE%>';
     var systemCodeCn = '<%=ComacConstants.SYSTEM_SHOW%>';
 </script>
<body> 
  <div id="windowDiv">
	<div id="processGrid" align="left"></div>
  </div>
</body>
   