<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/za_headerstep.jsp"%>
<script type="text/javascript">	
	var effectiveness = '${effectiveness}';
	var analysisType = '${analysisType}';	
	var za1Id = '${za1.za1Id}';
	var border = '${za1.border}';
	var env = '${za1.env}';
	var reachWay = '${za1.reachWay}';
	var equStruc = '${za1.equStruc}';
	var hasStrucOnly = '${za1.hasStrucOnly}';
	var needAreaAnalyze = '${za1.needAreaAnalyze}';
	var hasPipe = '${za1.hasPipe}';
	var hasMaterial = '${za1.hasMaterial}';
	var closeToSystem = '${za1.closeToSystem}';
	var remark = '${za1.remark}';
</script>
<script type="text/javascript" src="${contextPath}/area/za1.js"></script>
<body style="overflow-x:hidden">
</body>