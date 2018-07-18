<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script type="text/javascript">
		 var tempAlgs = '${jsonAlg}';		//接受算法的Json字符串
		 var lh4Isfull = ${lh4Isfull};
		 if(lh4Isfull == 1){
			lh4Isfull == true;
		 }else{
			lh4Isfull == false;
		 }
		 var algs = Ext.util.JSON.decode(tempAlgs);	//转换成Json类型
		 var LH4MMA = algs.LH4.ROOT; 	//根节点算法
		 var EDMMA = algs.LH4.ED;
		 var ADMMA = algs.LH4.AD;
		 var loadCusIntUrl = "${contextPath}/paramDefineManage/lhirfParam/loadLh5.do";
		 var saveCusIntUrl = "${contextPath}/paramDefineManage/lhirfParam/saveLh5.do";
		 var finalBackUrl = "${contextPath}/paramDefineManage/lhirfParam/finalBackUrl.do";
</script>
<script src="${contextPath}/paramDefineManage/lhirfParam.js"></script>