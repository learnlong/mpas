<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/za_headerstep.jsp"%>
<link rel="stylesheet" href="${contextPath}/css/matrix.css" type="text/css" />
<style>
.firstMatrixTableStyle{
	padding: 2px;
	margin: 0px auto;
	width: 500px;
	height: 100px;
	text-align: center;
	border: 1px solid #C1DAD7;
	border-collapse:0px;
	border-spacing:0px;
	background-color: white;
}
.firstMatrix12TdClass{
	padding: 4px;
	margin: 0px;
	color: #4F6B72;
	text-align: center;
	font-size: 13px;
	font-weight: 800;
	border: 1px solid #C1DAD7;
	background-color: #DFE8F6;
}
.firstMatrix34TdClass {
	padding: 0px;
	margin: 0px;
	color: black;
	border: 1px solid #C1DAD7;
	background-color: white;
	text-align: center;
	cursor: pointer;
}
/*-- 根据屏幕像素,设计的不同输入框的宽样式 --*/
/*-- approach_taskDes --*/
.approach_taskDes800{
	width:120px;
}
.approach_taskDes1024{
	width:186px;
}
.approach_taskDes1152{
	width:225px;
}
.approach_taskDes1280{
	width:264px;
}
.approach_taskDes1360{
	width:288px;
}
.approach_taskDes1366{
	width:289px;
}
.approach_taskDes1400{
	width:300px;
}
.approach_taskDes1440{
	width:312px;
}
/*-- taskDes_taskInterval --*/
.taskDes_taskInterval800{
	width:120px;
}
.taskDes_taskInterval1024{
	width:186px;
}
.taskDes_taskInterval1152{
	width:225px;
}
.taskDes_taskInterval1280{
	width:264px;
}
.taskDes_taskInterval1360{
	width:288px;
}
.taskDes_taskInterval1366{
	width:289px;
}
.taskDes_taskInterval1400{
	width:300px;
}
.taskDes_taskInterval1440{
	width:312px;
}
/*-- panelSeal_taskDesc --*/
.panelSeal_taskDesc800{
	width:px;
}
.panelSeal_taskDesc1024{
	width:74px;
}
.panelSeal_taskDesc1152{
	width:113px;
}
.panelSeal_taskDesc1280{
	width:152px;
}
.panelSeal_taskDesc1360{
	width:176px;
}
.panelSeal_taskDesc1366{
	width:177px;
}
.panelSeal_taskDesc1400{
	width:188px;
}
.panelSeal_taskDesc1440{
	width:200px;
}
/*-- taskDesc_taskInterval --*/
.taskDesc_taskInterval800{
	width:px;
}
.taskDesc_taskInterval1024{
	width:74px;
}
.taskDesc_taskInterval1152{
	width:113px;
}
.taskDesc_taskInterval1280{
	width:152px;
}
.taskDesc_taskInterval1360{
	width:176px;
}
.taskDesc_taskInterval1366{
	width:177px;
}
.taskDesc_taskInterval1400{
	width:188px;
}
.taskDesc_taskInterval1440{
	width:198px;
}
/*-- rstPanelSeal_rstTaskDesc --*/
.rstPanelSeal_rstTaskDesc800{
	width:px;
}
.rstPanelSeal_rstTaskDesc1024{
	width:74px;
}
.rstPanelSeal_rstTaskDesc1152{
	width:113px;
}
.rstPanelSeal_rstTaskDesc1280{
	width:152px;
}
.rstPanelSeal_rstTaskDesc1360{
	width:176px;
}
.rstPanelSeal_rstTaskDesc1366{
	width:177px;
}
.rstPanelSeal_rstTaskDesc1400{
	width:188px;
}
.rstPanelSeal_rstTaskDesc1440{
	width:200px;
}
/*-- rstTaskDesc_rstTaskInterval --*/
.rstTaskDesc_rstTaskInterval800{
	width:px;
}
.rstTaskDesc_rstTaskInterval1024{
	width:74px;
}
.rstTaskDesc_rstTaskInterval1152{
	width:113px;
}
.rstTaskDesc_rstTaskInterval1280{
	width:152px;
}
.rstTaskDesc_rstTaskInterval1360{
	width:176px;
}
.rstTaskDesc_rstTaskInterval1366{
	width:177px;
}
.rstTaskDesc_rstTaskInterval1400{
	width:188px;
}
.rstTaskDesc_rstTaskInterval1440{
	width:198px;
}
</style>
<script type="text/javascript">
// 矩阵html内容  
var MATRIX_HTML = "${MATRIX_HTML}";
var selectRecord;//将选中的记录设为全局变量
var rstReachWayOld;//RST任务的原先数据库内的接近方式,定义全局变量
var rstTaskDescOld;//RST任务的原先数据库内的任务描述,定义全局变量
var rstTaskIntervalOld;//RST任务的原先数据库内的任务间隔,定义全局变量
</script>
<script type="text/javascript" src="${contextPath}/common/array.js"></script>
<script type="text/javascript" src="${contextPath}/area/za43.js"></script>
<body style="overflow-x:hidden">
</body>