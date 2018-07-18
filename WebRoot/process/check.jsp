<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script type="text/javascript" src="${contextPath}/language/commonality_language.js"></script>
<script type="text/javascript" src="${contextPath}/js/DatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="${contextPath}/js/extjs/css/icon.css">
<script type="text/javascript" src="${contextPath }/process/check.js"></script>

<script type="text/javascript">
	//四大分析
	var zonalCode = '<%=ComacConstants.ZONAL_CODE%>';
	var zonalCodeCn = '<%=ComacConstants.ZONAL_SHOW%>';
	var lhrifCode = '<%=ComacConstants.LHIRF_CODE%>';
	var lhrifCodeCn = '<%=ComacConstants.LHIRF_SHOW%>';
	var structureCode = '<%=ComacConstants.STRUCTURE_CODE%>';
	var structureCodeCn = '<%=ComacConstants.STRUCTURE_SHOW%>';
	var systemCode = '<%=ComacConstants.SYSTEM_CODE%>';
	var systemCodeCn = '<%=ComacConstants.SYSTEM_SHOW%>';
	//流程状态
	var conWaitCheckCode = '<%=ComacConstants.PROCESS_STATUS_WAIT_CHECK%>';
	var conWaitCheckName = '<%=ComacConstants.PROCESS_STATUS_WAIT_CHECK_SHOW%>';
	var conCheckingCode = '<%=ComacConstants.PROCESS_STATUS_CHECKING%>';
	var conCheckingName = '<%=ComacConstants.PROCESS_STATUS_CHECKING_SHOW%>';
	var conFinishCheckCode = '<%=ComacConstants.PROCESS_STATUS_FINISH_CHECK%>';
	var conFinishCheckName = '<%=ComacConstants.PROCESS_STATUS_FINISH_CHECK_SHOW%>';
	var conCancelCheckCode = '<%=ComacConstants.PROCESS_STATUS_CANCEL_CHECK%>';
	var conCancelCheckName = '<%=ComacConstants.PROCESS_STATUS_CANCEL_CHECK_SHOW%>';
	//是否通过
	var conIsOkNullCode = '<%=ComacConstants.PROCESS_CHECK_STATUS_NULL%>';
	var conIsOkNullName = '<%=ComacConstants.PROCESS_CHECK_STATUS_NULL_SHOW%>';
	var conIsOkYesCode = '<%=ComacConstants.PROCESS_CHECK_STATUS_YES%>';
	var conIsOkYesName = '<%=ComacConstants.PROCESS_CHECK_STATUS_YES_SHOW%>';
	var conIsOkNoCode = '<%=ComacConstants.PROCESS_CHECK_STATUS_NO%>';
	var conIsOkNoName = '<%=ComacConstants.PROCESS_CHECK_STATUS_NO_SHOW%>';
	var conIsOkCancelCode = '<%=ComacConstants.PROCESS_CHECK_STATUS_CANCEL%>';
	var conIsOkCancelName = '<%=ComacConstants.PROCESS_CHECK_STATUS_CANCEL_SHOW%>';
</script>
<style type="text/css">
.fixedZIndex{  
   z-index: 15000;
   border:solid 1px red;
}
</style>
<body>
	<div id="windowDiv">
		<div id="processCheckGrid" align="left"></div>
	</div>
</body>
