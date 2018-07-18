<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<%
String msg=(String)request.getAttribute("msg");
%>
<script src="${contextPath}/baseData/comAta.js"></script>
<!-- 定义全局变量 -->
<script type="text/javascript">
	var msg='<%=msg%>';
	function refreshTreeNode(){
		var treePanel = Ext.getCmp('ataTreePanel');
		var path = treePanel.getSelectionModel().getSelectedNode().getPath('id');
		treePanel.getLoader().load(treePanel.getRootNode(),function(treeNode){
			treePanel.expandPath(path,'id',function(bSucess,oLastNode){
				treePanel.getSelectionModel().select(oLastNode);
			});
		},this);
	}
</script>
<body>
<div><form id="ataDownloadForm" action="/mpas/baseData/download/ataDownloadFile.do"></form></div>
<div><form id="ataExportForm" action="/mpas/baseData/export/ataExport.do"></form></div>
</body>