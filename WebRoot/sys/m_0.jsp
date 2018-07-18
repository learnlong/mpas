<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/sys_headerstep.jsp" %>
<!-- 导入js文件 -->

<script src="${contextPath}/sys/m_0.js"></script>

<!-- 定义全局变量 -->
<script type="text/javascript">
    var msiId='${msiId}';
    var sEff='${defaultEff}';
  
	Ext.apply(m0.app, {
		 loadM0 : "${contextPath}/sys/m0/loadM0.do",
		 saveM0 : "${contextPath}/sys/m0/saveM0.do",
		 checkProCodeExist : "${contextPath}/sys/m0/checkProCodeExist.do",
		 delM0 : "${contextPath}/sys/m0/delM0.do"
	});
	//开始运行  
	Ext.onReady(m0.app.init,m0.app);
</script>