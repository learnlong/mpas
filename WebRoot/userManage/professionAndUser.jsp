<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>

<script type="text/javascript" src="${contextPath }/userManage/professionAndUser.js"></script>
<body>
	<div id="thisUsersGrid" align="left"></div>
	<div id="AllUsersGrid" align="left"></div>
</body>
<script language="javascript">
var posintionAdminId = '<%=ComacConstants.POSITION_ID_PROFESSION_ADMIN%>';
var posintionEngineerId = '<%=ComacConstants.POSITION_ID_PROFESSION_ENGINEER%>';
var posintionAnalystId = '<%=ComacConstants.POSITION_ID_PROFESSION_ANAYIST%>';
var professionIdForAdmin = '<%=ComacConstants.PROEFSSION_ID_ADMIN%>';
</script> 