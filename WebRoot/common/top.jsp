<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<c:set var="contextPath" value="<%=request.getContextPath()%>" />
<link href="${contextPath}/css/css.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${contextPath}/css/menu/style.css" type="text/css" />
<link rel="stylesheet" href="${contextPath}/css/headermenu.css" type="text/css" />
<%@page import="com.rskytech.ComacConstants"%>
<script type="text/javascript">
	var curUserId="${SESSION_USER_KEY.userId}";
	//任务类型下拉框数据，多加个是为了避免字段冲突
	var baoyang = '<%=ComacConstants.BAOYANG%>';
	var jiancha = '<%=ComacConstants.JIANCHA%>';
	var jiankong = '<%=ComacConstants.JIANKONG%>';
	var jiance = '<%=ComacConstants.JIANCE%>';
	var chaixiu = '<%=ComacConstants.CHAIXIU%>';
	var baofei = '<%=ComacConstants.BAOFEI%>';
	var zonghe = '<%=ComacConstants.ZONGHE%>';
	var GVI = '<%=ComacConstants.GVI%>';
	var DET = '<%=ComacConstants.DET%>';
	var SDI = '<%=ComacConstants.SDI%>';
	var FNC = '<%=ComacConstants.FNC%>';
	var DIS = '<%=ComacConstants.DIS%>';
</script>