<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/lh_headerstep.jsp" %>

<script charset="utf-8" src="${contextPath}/kindeditor/kindeditor.js"></script>
	<link rel="stylesheet" href="${contextPath}/kindeditor/themes/default/default.css" />
	<link rel="stylesheet" href="${contextPath}/kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="${contextPath}/kindeditor/plugins/code/prettify.js"></script>
<script src="${contextPath}/lhirf/lh_1a.js"></script>
<head>
   
   <title>lh1aPage</title>
  </head>
  <script type="text/javascript">
    var  areaId = '${comArea.areaId}';
	var hsiId='${hsiId}';
	var picId='${picId}';
	var isneedRep ='${isneedRep}';
    var content1 = '${content1}';
	var content2 = '${content2}';
	var content3 = '${content3}';
	var picContent ='${picContent}';
	var lheff ='${lheff}';
      //参数 设置
  	Ext.apply(lh1a.app, {
			 g_lh1aSaveUrl : "${contextPath}/lhirf/lh_1a/saveLh1a.do"
			
	});
	   //开始运行
	  Ext.onReady(lh1a.app.init,lh1a.app);	
	  
   </script>
  <body>

  </body>
