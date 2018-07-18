<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp"%>
<!-- 导入js文件 -->
<script src="${contextPath}/sys/is_NoMsi.js"></script>

<!-- msi查询界面-->
<!-- 定义全局变量 -->
<script type="text/javascript">
	Ext.apply(is_NoMsi.app, {
		 loadMsi : "${contextPath}/sys/queryIsMsi/loadMsi.do"
	});
	//开始运行  
	Ext.onReady(is_NoMsi.app.init,is_NoMsi.app);
</script>