<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script type="text/javascript">
</script>
<script type="text/javascript">
var s45Flag = '<%=ComacConstants.VALIDFLAG_TWO%>';
var welcomeIsHaveS45 = '${welcomeIsHaveS45}';

if(welcomeIsHaveS45 == s45Flag ){
   alert("请先维护自定义矩阵 S4,S5 !"); //维护自定义矩阵
}else{
	if( welcomeIsHaveS45 == s45Flag){
	  alert(s1_msgNoMatrix);//维护自定义 矩阵s4, s5
	}
}

</script>
