<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/sys_headerstep.jsp" %>
<!-- 导入js文件 -->

<script src="${contextPath}/sys/m_12.js"></script>
<script type="text/javascript">
    var msiId='${msiId}';

	Ext.apply(m12.app, {
		 loadM12Url : "${contextPath}/sys/m12/loadM12.do",
		 saveM12Url : "${contextPath}/sys/m12/saveM12.do",
		 saveZanUrl : "${contextPath}/sys/m12/saveZan.do",
		 delM12Url : "${contextPath}/sys/m12/delM12.do",
		 searchVendorUrl : "${contextPath}/sys/m12/searchVendor.do"
	});
	//开始运行  
	Ext.onReady(m12.app.init,m12.app);
</script>