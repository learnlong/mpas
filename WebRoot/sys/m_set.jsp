<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/sys_headerstep.jsp" %>
<!-- 导入js文件 -->

<script src="${contextPath}/sys/m_set.js"></script>
<script type="text/javascript">
   var isMaintain;

    var msiId='${msiId}';
	Ext.apply(mset.app, {
		 loadMsetUrl : "${contextPath}/sys/mset/loadMset.do",
		 saveMsetUrl : "${contextPath}/sys/mset/saveMset.do",
		 saveZanUrl : "${contextPath}/sys/mset/saveZan.do",
		 delMsetUrl : "${contextPath}/sys/mset/delMset.do"
	});
	//开始运行  
	Ext.onReady(mset.app.init,mset.app);
</script>