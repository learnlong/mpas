<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.rskytech.pojo.ComModelSeries"%>
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<script type="text/javascript" src="${contextPath }/baseData/comModelSeries.js"></script>
<link rel="stylesheet" type="text/css" href="${contextPath}/js/extjs/css/icon.css">
 <script type="text/javascript">
 	<%ComModelSeries nowCm = (ComModelSeries)session.getAttribute(ComacConstants.SESSION_NOW_MODEL_SERIES);%>
 	var nowModelSeriesId = '<%=nowCm.getModelSeriesId()%>';
	function addItemmonth(value,text) {
        var tOption = document.createElement("Option");
        tOption.value = value;
        tOption.text = text;
        if(value == nowModelSeriesId){
	        tOption.selected = true;
        }
        parent.document.getElementById('selectId').add(tOption);
    }
 </script>
<body> 
	<div id="windowDiv">
		<div id="modelSeriesGrid" align="left"></div>
	</div>
</body>