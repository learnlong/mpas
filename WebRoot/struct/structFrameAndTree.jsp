<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script src="${contextPath}/struct/structFrameAndTree.js"></script>
<script type="text/javascript">
// 当前页面的查询参数
var searchType;
var ataOrMsi;
var type = '${type}';
var reportType = '<%=ComacConstants.STRUCTURE_CODE%>';
if (type == 'report'){
	searchType = '<%=ComacConstants.REPORT%>';
	ataOrMsi = 'MSI';
} else if (type == 'analysis'){
	searchType = '<%=ComacConstants.ANALYSIS%>';
	ataOrMsi = 'SSI';
} else if (type == 'choose'){
	searchType = '<%=ComacConstants.CHOOSE%>';
	ataOrMsi = 'ATA';
} 
var statusNew = '<%=ComacConstants.ANALYZE_STATUS_NEW%>';
var statusMaintain = '<%=ComacConstants.ANALYZE_STATUS_MAINTAIN%>';
var statusHOlD = '<%=ComacConstants.ANALYZE_STATUS_HOLD%>'; ///冻结
var statusAPPROVED = '<%=ComacConstants.ANALYZE_STATUS_APPROVED%>'; //审批完成

function refreshTreeNode(){
	if(Ext.getCmp("editorGridPanelId")){
		Ext.getCmp("editorGridPanelId").getStore().reload();
	}
	var treePanel = Ext.getCmp('areaTreePanel');
	if(treePanel){
		if(treePanel.getSelectionModel().getSelectedNode()){
			var path = treePanel.getSelectionModel().getSelectedNode().getPath('id');
			treePanel.getLoader().load(treePanel.getRootNode(),function(treeNode){
				treePanel.expandPath(path,'id',function(bSucess,oLastNode){
					treePanel.getSelectionModel().select(oLastNode);
				});
			},this);
		}
	}
}

</script>
