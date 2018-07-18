<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<script src="${contextPath}/lhirf/lh_mrb.js"></script>

<head>
   <title>lhMrbPage</title>
  </head>
  <script type="text/javascript">
  isMaintain = '${isMaintain}';
	 if(isMaintain =='1'){
	      authorityFlag = true;
	}else{
		authorityFlag = false;
	}
	var	lheff = '${lheff}'.replace(/<danyin>/g,"'");
   </script>
  <body>

  </body>
