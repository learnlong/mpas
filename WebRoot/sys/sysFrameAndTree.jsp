<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script src="${contextPath}/sys/sysFrameAndTree.js"></script>
<script type="text/javascript">
// 当前页面的查询参数
var searchType;
var ataOrMsi;
var type = '${type}';
var reportType = '<%=ComacConstants.SYSTEM_CODE%>';
if (type == 'analysis'){
	searchType = '<%=ComacConstants.ANALYSIS%>';
	ataOrMsi = 'MSI';
} else if (type == 'choose'){
	searchType = '<%=ComacConstants.CHOOSE%>';
	ataOrMsi = 'ATA';
} else if (type == 'report'){
	searchType = '<%=ComacConstants.REPORT%>';
	ataOrMsi = 'MSI';
} 
var statusNew = '<%=ComacConstants.ANALYZE_STATUS_NEW%>';
var statusMaintain = '<%=ComacConstants.ANALYZE_STATUS_MAINTAIN%>';
</script>
<script type="text/javascript">
	function refreshTreeNode(){
		if(Ext.getCmp("editorGridPanelId")){
			Ext.getCmp("editorGridPanelId").getStore().reload();
		}
		var treePanel = Ext.getCmp('sysTreePanel');
		if(Ext.getCmp('sysTreePanel').getSelectionModel().getSelectedNode()){
		///刷新选中 树
		var path = treePanel.getSelectionModel().getSelectedNode().getPath('id');
		treePanel.getLoader().load(treePanel.getRootNode(),function(treeNode){
			treePanel.expandPath(path,'id',function(bSucess,oLastNode){
				treePanel.getSelectionModel().select(oLastNode);
			});
		},this);
		
		}else{
	        //grid中  刷新选中 的整个树 
		   treePanel.expand(); 
		}
			  
		   ///刷树的同时  grid也要刷新
		   ownGridStore.load({
		       params:{searchType : searchType}
	       });
	       
	
	
	}
</script>