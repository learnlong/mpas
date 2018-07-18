if (step[0] == 'M2'){
	var tree_url = contextPath + "/sys/tree/m2Tree.do";
	var goUrl = contextPath + "/sys/m2/loadM2.do";
	var rootNode = new Ext.tree.AsyncTreeNode({text:"故障影响", id:'0'});	
} else if (step[0] == 'M3'){
	var tree_url = contextPath + "/sys/tree/m3Tree.do";
	var goUrl = contextPath + "/sys/m3/searchM3.do";
	var rootNode = new Ext.tree.AsyncTreeNode({text:"故障原因", id:'0'});
} else {
	var tree_url = contextPath + "/sys/tree/m4Tree.do";
	var goUrl = contextPath + "/sys/m4/searchM4.do";
	var rootNode = new Ext.tree.AsyncTreeNode({text:"任务", id:'0'});
}

var loader = new Ext.tree.TreeLoader({
	url : tree_url
});

loader.on('beforeload', function(loader, node){
	node.on('append', function(tree, thiz, newNode, index){
		if (newNode.isLeaf()){
			newNode.on('click', function(thiz,e){});
		}
	});
	if (!node.isLeaf()){
		loader.baseParams['cid'] = node.id;
		loader.baseParams['msiId'] = msiId;
	}
});

 treePanelTwo = new Ext.tree.TreePanel({
	header : false,
	id:'treePanelTwo',
	loader : loader,
	root : rootNode,
	autoScroll : true,
	bodyStyle : "background-color:#ffe8f6;padding:10 0 10 10",
	region : 'west',
	split : true,
	listeners : {
		"click" : function(node){			
		 	readData(node);			
		}
	}
});  
 
setTimeout(function()
	{
		rootNode.expand();
		treePanelTwo.expandAll();
	},100
);
 

function refreshTreeNode2(){
		var treePanel = Ext.getCmp('treePanelTwo');
		var path = treePanel.getSelectionModel().getSelectedNode().getPath('id');
		treePanel.getLoader().load(treePanel.getRootNode(),function(treeNode){
			treePanelTwo.expandAll();
			treePanel.expandPath(path,'id',function(bSucess,oLastNode){
			treePanel.getSelectionModel().select(oLastNode);
			});
		},this);
	}