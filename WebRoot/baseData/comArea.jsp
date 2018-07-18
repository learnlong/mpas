<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script src="${contextPath}/baseData/comArea.js"></script>
<%
String msg=(String)request.getAttribute("msg");
%>

<!-- 定义全局变量 -->
<script type="text/javascript">
var msg='<%=msg%>';
	function refreshTreeNode(){
		var treePanel = Ext.getCmp('areaTreePanel');
		var path = treePanel.getSelectionModel().getSelectedNode().getPath('id');
		treePanel.getLoader().load(treePanel.getRootNode(),function(treeNode){
			treePanel.expandPath(path,'id',function(bSucess,oLastNode){
				treePanel.getSelectionModel().select(oLastNode);
			});
		},this);
	}
</script>
<body >
<div id="top_div"></div>
<div><form id="zoneDownloadForm" action="/mpas/baseData/download/zoneDownloadFile.do"></form></div>
<div><form id="zoneExportForm" action="/mpas/baseData/export/zoneExport.do"></form></div>
</body>