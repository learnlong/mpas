Ext.namespace('ext_areaAnalyze');
Ext.onReady(function() {
	var treeRootNode = new Ext.tree.AsyncTreeNode({
		text : 'ATA',
		id : '0'
	});
	var title11;
	var isHidden = false;
	var gridColViewFlag = false;
	if (type == 'choose') {// MSI选择页面
		title11 = "MSI选择";
		gridColViewFlag = false;
		isHidden = true;
	} else if (type == 'analysis') {//MSI分析页面
		title11 = "MSI分析";
		gridColViewFlag = true;
	} else if (type == 'report') {//MSI分析页面
		title11 = "系统报告管理";
		gridColViewFlag = false;
	}
	var treeLoader = new Ext.tree.TreeLoader({
		url : contextPath + "/sys/tree/getAtaOrMsiTree.do"
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
		title : '系统树状列表',
		id : 'sysTreePanel',
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
		url : contextPath + "/sys/tree/getAtaOrMsiGrid.do",
		reader : new Ext.data.JsonReader({
			root : 'ata',
			fields : [{
				name : 'ataId',
				type : 'string'
			}, {
				name : 'ataCode',
				type : 'string'
			}, {
				name : 'ataName',
				type : 'string'
			}, {
				name : 'status',
				type : 'string'
			}, {
				name : 'msiId',
				type : 'string'
			}, {
				name : 'openStatus',
				type : 'string'
			}]
		}),
		sortInfo : {
				field : 'ataCode',
				direction : 'ASC'
			}
	});

	ownGridStore.load({
		params : {
			searchType : searchType
		}
	});

	var cm = new Ext.grid.ColumnModel({
		columns : [{
			id : 'ataCode',
			header : ataOrMsi,
			dataIndex : 'ataCode',
			width : 60
		}, {
			id : 'ataName',
			header : '名称',
			dataIndex : 'ataName',
			hidden : gridColViewFlag,
			width : isHidden==true?145:80
		}, {
			id : 'status',
			header : '当前状态',
			hidden : isHidden,
			dataIndex : 'status',
			width : 65
		}, {
			id : 'openStatus',
			header : '<div align="center">解锁操作</div>',
			dataIndex : 'openStatus',
			align : 'center',
			hidden : !gridColViewFlag,
			width : gridColViewFlag==true?82:60
		}]
	});

	openAnalysisStratus = function(msiId){
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
							url : contextPath + '/sys/tree/openAnalysisStatus.do',
							params : {
								msiId : msiId
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
		title : '可维护ATA或MSI',
		store : ownGridStore,
		cm : cm,
		header : false
	});

	ownGrid.addListener("celldblclick", celldbclick);

	var rightPanel = new Ext.Panel({
		region : 'center',
		id : "rightcenter",
		autoScroll : true,
		Style : "background-image:url(../images/252-1)",
		html : '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'
				+ contextPath + '/analysWelcome.jsp"> </iframe>'
	});

	function treeOnclick(n) {
		var ataId = n.attributes.id;
		var msiId = n.attributes.msiId;
		var level = n.attributes.level;
		var isMaintain = n.attributes.isMaintain;
		var isRef = n.attributes.isRef;
		var leaf = n.attributes.leaf;
		var status = n.attributes.status;
		if (type == 'report') {// 版本管理页面
			if (msiId == null) {// 点击根目录，系统不做任何反应
				return;
			} else {// 点击除根目录之外的目录，都跳转到版本管理界面
				//goToVersion(ataId, isMaintain);
				gotoReport(msiId, isMaintain);
			}
		}else if (type == 'choose') {// MSI选择生成页面
			if (ataId == 0
				  /*||level=="1"*/
				) {// 点击根目录，系统不做任何反应
				return;
			} else {// 点击除根目录之外的目录，都跳转到MSI选择界面
				goToChoose(ataId, isMaintain);
			}
		}else if (type == 'analysis') {// MSI分析页面
			if (ataId == 0
					/*||level=="1"*/
				) {// 点击根目录，系统不做任何反应
				return;
			}/* else if (leaf == true) {// 点击除根目录之外的所有目录，跳转到生成测试PDF的界面
				goToPdf(ataId, isMaintain);
			} */else if (isMaintain == 1) {// 当点击MSI时，并且登录用户拥有维护权限，跳转MSI分析界面
				goToAna(ataId, msiId, isMaintain);
			} else if (status != statusNew && status != statusMaintain) {// 点击登录用户只有查看权限的MSI，那么需要判断当前MSI只有是分析完成之后的状态才能查看
				goToAna(ataId, msiId, isMaintain);
			}
		}
	}

	function celldbclick(ownGrid, rowIndex, columnIndex, e) {
		currentRecord = ownGrid.getStore().getAt(rowIndex);
		var ataId = currentRecord.get('ataId');
		var msiId = currentRecord.get('msiId');
		var isMaintain = 1;
		if (type == 'report') {// 版本管理页面，跳转到版本管理界面
			//goToVersion(ataId, isMaintain);
			gotoReport(msiId, isMaintain);
		} else if (type == 'choose') {// 跳转到MSI选择界面
			goToChoose(ataId, isMaintain);
		} else if (type == 'analysis') {// 跳转MSI分析界面
			goToAna(ataId, msiId, isMaintain);
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

	// 版本管理页面，跳转到版本管理界面
	/**
	function gotoReport(ataId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/sys/version/initVersion.do?ataId='
						+ ataId + '&isMaintain=' + isMaintain + '"></iframe>');
		check();
	}
	**/
	//报告管理
	function gotoReport(msiId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/com/report/init.do?reportType=' + reportType + '&generateId='
						+ msiId + '&isMaintain=' + isMaintain + '"></iframe>');
		check();
	}

	// MSI选择页面，跳转到MSI选择界面
	function goToChoose(ataId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/sys/msiSelect/init.do?ataId='
						+ ataId
						+ '&isMaintain=' + isMaintain + '"></iframe>');
		check();
	}

	// MSI分析页面，跳转跳转到LH1
	function goToAna(ataId, msiId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/sys/m0/init.do?msiId='
						+ msiId
						+ '&ataId='
						+ ataId
						+ '&isMaintain='
						+ isMaintain
						+ '"></iframe>');
		check();
	}

	var win = new Ext.Window({
		layout : 'border',
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : false,
		id : "cnm",
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		items : [{
			region : 'west',
			layout : 'fit',
			title : title11,
			collapsible : true,// 面板可收缩参数,默认是false
			width : 213,
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
