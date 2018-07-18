<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>

<script type="text/javascript" src="${contextPath }/userManage/userAndAuth.js"></script>
<body>
	<div id="usersGrid" align="left"></div>
	<div id="treePanel" align="left"></div>
</body>
<script language="javascript">
var zonalCode = '<%=ComacConstants.ZONAL_CODE%>';
var zonalCodeCn = '<%=ComacConstants.ZONAL_SHOW%>';
var lhrifCode = '<%=ComacConstants.LHIRF_CODE%>';
var lhrifCodeCn = '<%=ComacConstants.LHIRF_SHOW%>';
var structureCode = '<%=ComacConstants.STRUCTURE_CODE%>';
var structureCodeCn = '<%=ComacConstants.STRUCTURE_SHOW%>';
var systemCode = '<%=ComacConstants.SYSTEM_CODE%>';
var systemCodeCn = '<%=ComacConstants.SYSTEM_SHOW%>';
</script>