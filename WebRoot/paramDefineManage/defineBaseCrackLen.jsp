<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<!-- 
author: hanjinlun
createdate: 2014-11-11
use: 自定义基本裂纹长度
 -->
<style type="text/css">
.inputTxt {
	margin: -2px;
	padding: 0px;
	width: 40px;
	border-color: transparent;
	background-color: transparent;
	text-align:center;
}

.grade {
	margin: 0px;
	padding: 0px;
	width: 80px;
	color: black;
	font-family: "宋体";
	font-size: 24;
	font-weight: 800;
}

.gradeCol {
	margin: 0px;
	padding: 0px;
	color: black;
	font-size: 18px;
	font-weight: 800;
	text-align: center;
	border-color: transparent;
	background-color: transparent;
}

.gradeColCn {
	font-family: "宋体";
	letter-spacing: 8px;
}

.gradeColEn {/*-- 未使用 --*/
	font-family: "宋体";
	letter-spacing: 0px;
}

.gradeRow {
	margin: 0px;
	padding: 0px;
	width: 40px;
	color: black;
	font-weight: 800;
	text-align: center;
	border-color: transparent;
	background-color: transparent;
	writing-mode: tb-rl;/*--使字符串,竖排"从上往下，从右向左"--*/
	word-wrap: break-word;/*--强制换行 --*/
	word-break: nomal;
}

.gradeRowCn {
	margin-right: 14px;
	font-family: "宋体";
	font-size: 18px;
	letter-spacing: 12px;
}

.gradeRowEn {
	margin-right: 32px;
	font-family: "宋体";
	font-size: 20px;
}

.matrixDiv {
	position: absolute;
	top: 75px;
	left: 48px;
}

.matrixTable {
	padding: 0px;
	margin: 0px;
	width: 300px;
	height: 280px;
	text-align: center;	
	background-color: #DFE8F6;
}
</style>

<script src="${contextPath}/paramDefineManage/defineBaseCrackLen.js"></script>

<!-- 定义全局变量 -->
<script type="text/javascript">
	/**
	 * 删除左右两端的空格
	 */
	String.prototype.trim=function(){
	     return this.replace(/(^\s*)|(\s*$)/g, ""); 
	}

	Ext.apply(cusBaseCrackLen.app, {
			 loadGradeListUrl : "${contextPath}/paramDefineManage/defineBaseCrackLen/loadGradeList.do",
			 loadMatrixUrl : "${contextPath}/paramDefineManage/defineBaseCrackLen/loadMatrix.do",
			 saveGradeListUrl : "${contextPath}/paramDefineManage/defineBaseCrackLen/saveGradeList.do",
			 saveMatrixUrl : "${contextPath}/paramDefineManage/defineBaseCrackLen/saveMatrix.do"
	});
	
	//开始运行  
	Ext.onReady(cusBaseCrackLen.app.init,cusBaseCrackLen.app);
</script>
<body>
</body>