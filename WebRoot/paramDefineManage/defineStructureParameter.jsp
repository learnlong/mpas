<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script type="text/javascript">
		var tempAlgs = '${jsonAlg}';		//接受算法的Json字符串
		var algs = Ext.util.JSON.decode(tempAlgs);	//转换成Json类型
/*************************S4A 金属ED参数**************************************************/
		var S4AROOTMMA = algs.S4A.ROOT;
/*************************S4B 非金属ED参数**************************************************/
		var S4BROOTMMA = algs.S4B.ROOT;
/*************************S5A 金属AD参数**************************************************/
		var S5AROOTMMA = algs.S5A.ROOT;
/*************************S5B 非金属AD参数**************************************************/
		var S5BROOTMMA = algs.S5B.ROOT;
/*************************SYB 金属应力参数**************************************************/
		var SYAROOTMMA = algs.SYA.ROOT;
/*************************SYB 非金属应力参数**************************************************/
		var SYBROOTMMA = algs.SYB.ROOT;
		
</script>
<script src="${contextPath}/paramDefineManage/metalEdParameter.js"></script>
<script src="${contextPath}/paramDefineManage/noMetalEdParameter.js"></script>
<script src="${contextPath}/paramDefineManage/metalAdParameter.js"></script>
<script src="${contextPath}/paramDefineManage/noMetalAdParameter.js"></script>
<script src="${contextPath}/paramDefineManage/metalStressParameter.js"></script>
<script src="${contextPath}/paramDefineManage/noMetalStressParameter.js"></script>

