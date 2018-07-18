<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<script src="${contextPath}/area/zaFrameAndTree.js"></script>
<script type="text/javascript">
	// 当前页面的查询参数
	var searchType;
	var type = '${type}';
	var reportType = '<%=ComacConstants.ZONAL_CODE%>';
	var statusNew = '<%=ComacConstants.ANALYZE_STATUS_NEW%>';
	var statusMaintain = '<%=ComacConstants.ANALYZE_STATUS_MAINTAIN%>';
	if(type == 'report'){
		searchType = '<%=ComacConstants.REPORT%>';
	}else if(type == 'analysis'){
		searchType = '<%=ComacConstants.ANALYSIS%>';
	}
</script>
<script type="text/javascript">
	function refreshTreeNode(){
		var treePanel = Ext.getCmp('areaTreePanel');
		if(Ext.getCmp('areaTreePanel').getSelectionModel().getSelectedNode()){
			var path = treePanel.getSelectionModel().getSelectedNode().getPath('id');
			treePanel.getLoader().load(treePanel.getRootNode(),function(treeNode){
				treePanel.expandPath(path,'id',function(bSucess,oLastNode){
					treePanel.getSelectionModel().select(oLastNode);
				});
			},this);
		}else{
		   treePanel.expand();
		}
	    ownGridStore.load({
	       params:{searchType : searchType}
        });
	}
</script>