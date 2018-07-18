<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script src="${contextPath}/lhirf/lhFrameAndTree.js"></script>
<script type="text/javascript">
// 当前页面的查询参数
var searchType;
var areaOrHsi;
var type = '${type}';
var reportType = '<%=ComacConstants.LHIRF_CODE%>';
if (type == 'report'){
	searchType = '<%=ComacConstants.REPORT%>';
	areaOrHsi = '区域';
	flagHidden = false ;
} else if (type == 'analysis'){
	searchType = '<%=ComacConstants.ANALYSIS%>';
	areaOrHsi = 'HSI';
	flagHidden = true ;
} else if (type == 'choose'){
	searchType = '<%=ComacConstants.CHOOSE%>';
	areaOrHsi = '区域';
	flagHidden = false ;
} 
var statusNew = '<%=ComacConstants.ANALYZE_STATUS_NEW%>';
var statusHOlD = '<%=ComacConstants.ANALYZE_STATUS_HOLD%>';
var statusAPPROVED = '<%=ComacConstants.ANALYZE_STATUS_APPROVED%>'; //审批完成
var statusMaintain = '<%=ComacConstants.ANALYZE_STATUS_MAINTAIN%>';
var statusHOlDSHOW = '<%=ComacConstants.ANALYZE_STATUS_HOLD_SHOW%>';
var statusAPPROVEDSHOW = '<%=ComacConstants.ANALYZE_STATUS_APPROVED_SHOW%>'; //审批完成
</script>
<script type="text/javascript">
	function refreshTreeNode(){
		if(Ext.getCmp("editorGridPanelId")){
			Ext.getCmp("editorGridPanelId").getStore().reload();
		}
		if(Ext.getCmp('areaTreePanel').getSelectionModel().getSelectedNode()){///刷新选中 树
		var treePanel = Ext.getCmp('areaTreePanel');
		var path = treePanel.getSelectionModel().getSelectedNode().getPath('id');
		treePanel.getLoader().load(treePanel.getRootNode(),function(treeNode){
			treePanel.expandPath(path,'id',function(bSucess,oLastNode){
				treePanel.getSelectionModel().select(oLastNode);
			});
		},this);
		}else{
	        //grid中  刷新选中 的整个树 
		   treeRootNode.expand(); 
		}
		   ownGridStore.load({///刷树的同时  grid也要刷新
		       params:{searchType : searchType}
	       });
	}
</script>