Ext.namespace('ext_areaAnalyze');
Ext.onReady(function() {
	var treeRootNode = new Ext.tree.AsyncTreeNode({
		text : '区域',
		id : '0'
	});
	
	var title11;
	var gridColViewFlag = false;
	if (type == 'report') {// 版本管理页面
		title11 = "区域报告管理";
		gridColViewFlag = false;
	}
	if (type == 'analysis') {//区域分析页面
		title11 = "区域分析";
		gridColViewFlag = true;
	}	
	
	var treeLoader = new Ext.tree.TreeLoader({
		url : contextPath + "/area/tree/getAreaTree.do"
	});

	treeLoader.on('beforeload', function(treeLoader, node) {
		node.on('append', function(tree, thiz, newNode, index) {
			if (newNode.isLeaf()) {
				newNode.on('click', function(thiz, e) {
				});
			}
		});
		if (!node.isLeaf()) {
			treeLoader.baseParams['id'] = node.id;
			treeLoader.baseParams['searchType'] = searchType;
		}
	});

	var treePanel = new Ext.tree.TreePanel({
		title : '区域树状列表',
		id : 'areaTreePanel',
		header : false,
		loader : treeLoader,
		root : treeRootNode,
		autoScroll : true,
		listeners : {
			"click" : function(node) {
				treeOnclick(node);
			}
		}
	});
	
	treeRootNode.expand();

	ownGridStore = new Ext.data.Store({
		url : contextPath + "/area/tree/getAreaGrid.do",
		reader : new Ext.data.JsonReader({
			root : 'areas',
			fields : [{
				name : 'areaId',
				type : 'string'
			}, {
				name : 'areaCode',
				type : 'string'
			}, {
				name : 'areaName',
				type : 'string'
			}, {
				name : 'status',
				type : 'string'
			}, {
				name : 'zaId',
				type : 'string'
			}, {
				name : 'openStatus',
				type : 'string'
			}]
		})
	});

	ownGridStore.load({
		params : {
			searchType : searchType
		}
	});

	var cm = new Ext.grid.ColumnModel({
		columns : [{
			id : 'areaCode',
			header : '区域',
			dataIndex : 'areaCode',
			width : 35
		}, {
			id : 'areaName',
			header : '名称',
			hidden : gridColViewFlag,
			dataIndex : 'areaName',
			width : 80
		}, {
			id : 'status',
			header : '当前状态',
			dataIndex : 'status',
			width : 70
		}, {
			id : 'zaID',
			header : 'zaID',
			dataIndex : 'zaID',
			hidden : true
		}, {
			id : 'openStatus',
			header : '<div align="center">解锁操作</div>',
			dataIndex : 'openStatus',
			align : 'center',
			hidden : !gridColViewFlag,
			width : gridColViewFlag==true?80:60
		}]
	});

	openAnalysisStratus = function(zaId){
		Ext.Msg.confirm(commonality_affirm,
				'确认解锁状态吗？',
				function(btn) {
					if (btn === 'yes') {
						var waitMsg = new Ext.LoadMask(
								Ext.getCmp('cnm').body, {
									msg : commonality_waitMsg,
									// 完成后移除
									removeMask : true
								});
						waitMsg.show();
						Ext.Ajax.request({
							url : contextPath + '/area/tree/openAnalysisStatus.do',
							params : {
								zaId : zaId
							},
							method : "POST",
							// /waitMsg : commonality_waitMsg,
							success : function(response) {
								waitMsg.hide();
								var result = Ext.util.JSON.decode(response.responseText);
								if (result.success == 'true' || result.success === true) {
									//alert('解锁成功');
									ownGridStore.reload();
								} else {
									alert(commonality_cautionMsg);
								}
							},
							failure : function(response) {
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});
				}
		});
	};
	
	var ownGrid = new Ext.grid.GridPanel({
		id : 'editorGridPanelId',
		title : '可维护区域',
		store : ownGridStore,
		cm : cm,
		header : false
	});

	ownGrid.addListener("celldblclick", celldbclick);

	var rightPanel = new Ext.Panel({
		region : 'center',
		id : "rightcenter",
		autoScroll : true,
		style : "background-image:url(../images/252-1)",
		html : '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'
				+ contextPath + '/analysWelcome.jsp"> </iframe>'
	});

	function treeOnclick(n) {
		var areaId = n.attributes.id;
		var level = n.attributes.level;
		var isMaintain = n.attributes.isMaintain;
		var status = n.attributes.status;
		var zaId = n.attributes.zaId;
		if (type == 'report') {// 版本管理页面
			if (zaId == null) {// 点击根目录，系统不做任何反应
				return;
			} else {// 除点击根目录外，所有的都跳转到版本管理界面
				goToReport(zaId, isMaintain);
			}
		} else if (type == 'analysis') {// 区域分析页面
			if (areaId == 0 || level == 1) {// 点击根目录或第一级目录时，系统不做任何反应
				return;
			} else if (isMaintain == 1) {// 当点击第三级时，并且当前区域是登录用户维护的话，跳转区域分析界面
				goToAna(areaId, isMaintain);
			} else if (status != statusNew) {// 当点击第三级时，并且登录用户只有查看权限，那么需要判断当前区域只有是分析之后的状态才能查看
				goToAna(areaId, isMaintain);
			}
		}
	}

	function celldbclick(ownGrid, rowIndex, columnIndex, e) {
		currentRecord = ownGrid.getStore().getAt(rowIndex);
		var areaId = currentRecord.get('areaId');
		var zaId = currentRecord.get('zaId');
		var isMaintain = 1;
		if (type == 'report') {// 版本管理页面
			goToReport(zaId, isMaintain);
		} else if (type == 'analysis') {// 区域分析页面
			goToAna(areaId, isMaintain);
		}
	}

	var myMask = new Ext.LoadMask(Ext.getBody(), {// 等待提示信息
		msg : commonality_waitMsg
	});
	function check() {
		setTimeout(function() {
			if (window.frames["ifrm_area"].document.readyState == "complete") {
				myMask.hide();
			} else {
				setTimeout(function() {
					check();
				}, 100);
			}

		}, 100);
	}
	
	//报告管理
	function goToReport(zaId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/com/report/init.do?reportType=' + reportType + '&generateId='
						+ zaId + '&isMaintain=' + isMaintain + '"></iframe>');
		check();
	}
	
	// 区域分析页面，跳转区域分析界面
	function goToAna(areaId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/area/za1/init.do?areaId='
						+ areaId
						+ '&isMaintain=' + isMaintain + '"></iframe>');
		check();				
	}

	var win = new Ext.Window({
		layout : 'border',
		id : 'cnm',
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : false,
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		items : [{
			region : 'west',
			layout : 'fit',
			title : title11,
			collapsible : true,// 面板可收缩参数,默认是false
			width : 190,
			split : true,
			items : [new Ext.TabPanel({
				activeTab : 0,
				split : true,
				border : false,
				items : [treePanel, ownGrid]
			})]
		}, {
			region : 'center',
			layout : 'border',
			items : [rightPanel]
		}]
	});

	win.show();
});