<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<!-- 导入js文件 -->
<script src="${contextPath}/sys/recheckmmel.js"></script>
<!-- 定义全局变量 -->
<script type="text/javascript">
    isMaintain = '${isMaintain}';
	//判断是否有修改权限
	if(isMaintain =='1'){
	      flag = true;
	}else{
		flag = false;
	}
	Ext.apply(ext_sys_index.app, {
		 g_loadReferMmelUrl : "${contextPath}/sys/recheckmmel/loadReferMmel.do",
		 g_saveReferMmelUrl : "${contextPath}/sys/recheckmmel/saveReferMmel.do",
		 g_mmelComboUrl : "${contextPath}/sys/m2/mmelCombo.do"
	});
	//开始运行  
	Ext.onReady(ext_sys_index.app.init,ext_sys_index.app);
</script>