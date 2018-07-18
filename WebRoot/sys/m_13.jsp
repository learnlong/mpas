<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/sys_headerstep.jsp" %>
<!-- 导入js文件 -->

<script src="${contextPath}/sys/m_13.js"></script>
<script src="${contextPath}/sys/m_13_1.js"></script>
<script type="text/javascript">
   var isMaintain;

    var msiId='${msiId}';
	Ext.apply(m13.app, {
		 loadM13Url : "${contextPath}/sys/m13/loadM13.do",
		 saveM13Url : "${contextPath}/sys/m13/saveM13.do",
		 saveZanUrl : "${contextPath}/sys/m13/saveZan.do",
		 delM13Url : "${contextPath}/sys/m13/delM13.do",
		 searchCauseUrl : "${contextPath}/sys/mset/searchCase.do"
	});
	//开始运行  
	Ext.onReady(m13.app.init,m13.app);
</script>