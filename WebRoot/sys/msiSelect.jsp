<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.rskytech.pojo.ComAta"%>
<%@page import="org.springframework.web.util.JavaScriptUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<!-- 导入js文件 -->
<script type="text/javascript">
var ataId='${comAta.ataId}';
var ataCode = '${comAta.ataCode}';
var ataTitle;
var ataName;
 ataTitle = '<%=JavaScriptUtils.javaScriptEscape(((ComAta)request.getAttribute("comAta")).getAtaName())%>';
 if(ataTitle.length > 13){
	 ataName=ataTitle.substr(0,13)+"...";
	 }else{
	   ataName  = ataTitle;
	}
var isMaintain;
  if('${isMaintain}'=='1'){
     isMaintain = true;
   }else{
    isMaintain = false;
   }
</script>
<script src="${contextPath}/sys/msiSelect.js"></script>
<script type="text/javascript" src="${contextPath}/js/extjs/multicombo.js"></script>
<!-- 定义全局变量 -->
<script type="text/javascript">
	Ext.apply(msiSelect.app, {
		 loadMSelect : "${contextPath}/sys/msiSelect/loadMSelectList.do",
		 saveMSelect: "${contextPath}/sys/msiSelect/saveMSelect.do"
	});
	//开始运行  
	Ext.onReady(msiSelect.app.init,msiSelect.app);
</script>