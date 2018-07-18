<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/js/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="${contextPath}/js/extjs/css/icon.css">
<script type="text/javascript" src="${contextPath}/js/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="${contextPath}/js/extjs/ext-all.js"></script>
<script type="text/javascript" src="${contextPath}/common/textfieldOverride.js"></script>
<script type="text/javascript" src="${contextPath}/js/extjs/ext-basex.js"></script>

<script type="text/javascript">
	var contextPath='${contextPath}';
	Ext.BLANK_IMAGE_URL="${contextPath}/js/extjs/resources/images/default/s.gif";
	<c:if test='${SESSION_USER_KEY == null}'>
		alert("会话过期，请重新登录! ,Session expiration, please log back in !");
  		top.window.location.href = '${contextPath}/login.jsp'
	</c:if>
  	
	// 监听所有请求失败事件，如果响应状态为999时表示ajax请求过程中session失效，系统跳转至登录画面
	Ext.Ajax.on('requestexception', function(conn, response, options) {
		if (response.status == "999") {
			alert("会话过期，请重新登录! ,Session expiration, please log back in !");					
			top.window.location.href = '${contextPath}/login.jsp';
		}
	});	
</script>
<script type="text/javascript" src="${contextPath}/js/extjs/other/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="${contextPath}/js/commonality_language.js"></script>
<script type="text/javascript" src="${contextPath}/js/commonUtility.js"></script>