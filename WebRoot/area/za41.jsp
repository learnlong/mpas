<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/za_headerstep.jsp"%>
<style type="text/css" media="all">   
    .table-one {float:left;padding:0 0 0 20px;color:red;border:1px #cccccc solid;} 
    .hang {color:red;border:1px #cccccc solid;line-height:22px;height:22px} 
	.bgColor{background-color:#00FF99}
	.bgNoColor{background-color:#dfe8f7}
	.questionHighlight{	
		font-size:12px;
	    background-color: #ffffa0;
	    border-color::#000000;
	    color:black;
	}
	.optionHighlight{	
		font-size:13px;
		font-weight:bold;
	    background-color: #ffffa0;
	    border-color::#000000;
	    color:#ff6000;
	}
	
	.imageHtml td{
		text-align:center;
		font-size: 12px;
		/*width:10px;*/
	}
	.td4{
		border-style:solid;
		border-width:1px 1px 1px 1px;
		border-color::#000000;
	}
	.td1{
		border-style:solid;
		border-width:0px 1px 0px 0px;
		border-color::#000000;
	}
</style>	
<script type="text/javascript" src="${contextPath}/area/za41.js"></script>