<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/struct_headerstep1.jsp"%> 
<script type="text/javascript" src="${contextPath}/js/extjs/multicombo.js"></script>
<script src="${contextPath}/common/coordinationStrToSys.js"></script>
<style type="text/css">
	#edittable td { 
	border-right: 1px solid #C1DAD7; 
	border-bottom: 1px solid #C1DAD7; 
	font-size:10px; 
	padding: 4px 4px 4px 4px; 
	color: #4f6b72; 
} 
	#edittable table{
	border-left: 1px solid #C1DAD7; 
	border-top: 1px solid #C1DAD7;
}
</style>


<script type="text/javascript">
	var ssiCode='${siCode }';
	var isMaintain=${isMaintain};
	var s6Id ='${s6Id}';
	var ssiId ='${ssiId}';
	var unInOrOutSsiList ='${unInOrOutSsiList}';
	var ataId = '${ataId}';
	var ssiList ="${ssiList}";
	var s2Id = '${s2Id}';

</script>
<script src="${contextPath}/struct/s6_in.js"></script>
