<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<link rel="stylesheet" href="${contextPath}/css/matrix.css" type="text/css" />

<script src="${contextPath}/paramDefineManage/standardRegionParam.js"></script>

<script type="text/javascript">
// 主矩阵树是否可见 
var TREE_DISABLED = ${TREE_DISABLED};
// 第一副矩阵是否可见 
var FIRST_MATRIX_DISABLED = ${FIRST_MATRIX_DISABLED};	
// 第二副矩阵是否可见 
var SECOND_MATRIX_DISABLED = ${SECOND_MATRIX_DISABLED};
// 检查间隔矩阵是否可见 
var FINAL_MATRIX_DISABLED = ${FINAL_MATRIX_DISABLED};
// 第一矩阵html内容 
var FIRST_MATRIX_HTML = "${FIRST_MATRIX_HTML}";
// 第二矩阵html内容
var SECOND_MATRIX_HTML = "${SECOND_MATRIX_HTML}";
// 检查间隔矩阵html内容 
var FINAL_MATRIX_HTML = "${FINAL_MATRIX_HTML}";
</script>