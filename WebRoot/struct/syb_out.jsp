<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/struct_headerstep1.jsp"%>
<script type="text/javascript">

</script>
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
<style type="text/css">
	#edittable1 td { 
	border-right: 1px solid #C1DAD7; 
	border-bottom: 1px solid #C1DAD7; 
	font-size:10px; 
	padding: 4px 4px 4px 4px; 
	color: #4f6b72; 
} 
	#edittable1 table{
	border-left: 1px solid #C1DAD7; 
	border-top: 1px solid #C1DAD7;
}
</style>


<script type="text/javascript">
 	var ssiLength=${ssiLength};
 	var arrayValue=${arrayValue};
 	var isMaintain=${isMaintain};
 	var syIdArray='${syIdArray}';
	var matrix = "${matrix}";
	var ssiId = '${ssiId}';
	var remark = '${remark}';
	var s2Id = '${s2Id}';
	var levelCount ='${levelCount}';
	

</script>
<script src="${contextPath}/struct/syb_out.js"></script>
