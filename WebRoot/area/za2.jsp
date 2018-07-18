<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/za_headerstep.jsp"%>
<link rel="stylesheet" href="${contextPath}/kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="${contextPath}/kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="${contextPath}/kindeditor/kindeditor.js"></script>
<script charset="utf-8" src="${contextPath}/kindeditor/plugins/code/prettify.js"></script>
<script type="text/javascript">
	var za2Id = '${za2Id}';
	var position = '${position == null ? 0 : position}';
	var innerPos = 0;
	var outerPos = 0;
	if (position == 1){
		innerPos = 1;
		outerPos = 0;
	} else if (position == 2){
		innerPos = 0;
		outerPos = 1;
	} else if (position == 3){
		innerPos = 1;
		outerPos = 1;
	}	
</script>
<script type="text/javascript" src="${contextPath}/area/za2.js"></script>
<body style="overflow-x:hidden">
</body>