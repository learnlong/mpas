Ext.namespace('ext_areaAnalyze');
Ext.onReady(function() {
	treeRootNode = new Ext.tree.AsyncTreeNode({
		text : '区域',
		id : '0'
	});
	var title11;
	var isHidden = false;
	var gridColViewFlag = false;
	if (type == 'report') {// 版本管理页面
		title11 = "L/HIRF报告管理";
		gridColViewFlag = false;
	}
	if (type == 'choose') {// HSI生成页面
		title11 = "HSI选择";
		gridColViewFlag = false;
		isHidden = true;
	}
	if (type == 'analysis') {// HSI分析页面
		title11 = "L/HIRF分析";
		gridColViewFlag = true;
	}
	var treeLoader = new Ext.tree.TreeLoader({
		url : contextPath + "/lhirf/tree/getAreaOrHsiTree.do"
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
		title : 'L/HIRF层次结构图',
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
		url : contextPath + "/lhirf/tree/getAreaOrHsiGrid.do",
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
				name : 'isRef',
				type : 'string'
			}, {
				name : 'openStatus',
				type : 'string'
			}]
		}),
			sortInfo : {
				field : 'areaCode',
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
			id : 'areaCode',
			header : areaOrHsi,
			dataIndex : 'areaCode',
			width : isHidden==true?90:80
		}, {
			id : 'areaName',
			header : '名称',
			hidden : gridColViewFlag,
			dataIndex : 'areaName',
			width : isHidden==true?120:70
		}, {
			id : 'status',
			header : '当前状态',
			dataIndex : 'status',
			hidden : isHidden,
			width : 60
		}, {
			id : 'openStatus',
			header : '<div align="center">解锁操作</div>',
			dataIndex : 'openStatus',
			align : 'center',
			hidden : !gridColViewFlag,
			width : gridColViewFlag==true?70:60
		}]
	});

	openAnalysisStratus = function(lhId){
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
							url : contextPath + '/lhirf/tree/openAnalysisStatus.do',
							params : {
								lhId : lhId
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
		title : '可维护区域或HSI',
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
		var areaId = n.attributes.id;
		var level = n.attributes.level;
		var isMaintain = n.attributes.isMaintain;
		var isRef = n.attributes.isRef;
		var leaf = n.attributes.leaf;
		var status = n.attributes.status;

		if (status == statusHOlD || status == statusAPPROVED) {
			isMaintain = 0;
		}

		if (type == 'report') {// 版本管理页面
			if (areaId == 0) {// 点击根目录，系统不做任何反应
				return;
			} else if (leaf == true) {// 点击除根目录之外的所有目录，都跳转到版本管理界面；点击HSI没有反应
				goToReport(areaId, isMaintain);
			}
		}
		if (type == 'choose') {// HSI生成页面
			if (areaId == 0 || level == 1) {// 点击根目录和第一级目录，系统不做任何反应
				return;
			} else if (leaf == false) {// 点击除根目录、第一级目录之外的所有目录，都跳转到HSI生成界面；点击HSI没有反应
				goToChoose(areaId, isMaintain);
			}
		}
		if (type == 'analysis') {// HSI分析页面
			if (areaId == 0) {// 点击根目录，系统不做任何反应
				return;
			}/* else if (leaf == false) {// 点击除根目录之外的所有目录，跳转到生成测试PDF的界面
				goToPdf(areaId, isMaintain);
			}*/ else if (isMaintain == 1&&leaf == true) {// 点击登录用户有维护权限的HSI
				if (isRef == 0) {// 点击HSI，判断如果是不参考的HSI，则跳转到LH1
					goToLh1(areaId, isMaintain);
				} else if (isRef == 1) {// 点击HSI，判断如果是参考的HSI，则跳转到LH1A
					goToLh1A(areaId, isMaintain);
				}
			} else if (status != statusNew && status != statusMaintain&&leaf == true) {// 点击登录用户只有查看权限的HSI，那么需要判断当前HSI只有是分析完成之后的状态才能查看
				if (isRef == 0) {// 点击HSI，判断如果是不参考的HSI，则跳转到LH1
					goToLh1(areaId, isMaintain);
				} else if (isRef == 1) {// 点击HSI，判断如果是参考的HSI，则跳转到LH1A
					goToLh1A(areaId, isMaintain);
				}
			}
		}
	}

	function celldbclick(ownGrid, rowIndex, columnIndex, e) {
		currentRecord = ownGrid.getStore().getAt(rowIndex);
		var areaId = currentRecord.get('areaId');
		var isMaintain = 1;
		var status = currentRecord.get('status');
		if (status == statusHOlDSHOW || status == statusAPPROVEDSHOW) {
			isMaintain = 0;
		}
		var isRef = currentRecord.get('isRef');
		if (type == 'report') {// 版本管理页面，跳转到版本管理界面
			goToReport(areaId, isMaintain);
		} else if (type == 'choose') {// HSI生成页面，跳转到HSI生成界面
			goToChoose(areaId, isMaintain);
		} else if (type == 'analysis' && isRef == '0') {// 判断如果是不参考的HSI，则跳转到LH1
			goToLh1(areaId, isMaintain);
		} else if (type == 'analysis' && isRef == '1') {// 判断如果是参考的HSI，则跳转到LH1A
			goToLh1A(areaId, isMaintain);
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
	function goToVersion(areaId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/lhirf/version/initVersion.do?areaId='
						+ areaId + '&isMaintain=' + isMaintain + '"></iframe>');
		check();
	}

	//报告管理
	function goToReport(hsiId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/com/report/init.do?reportType=' + reportType + '&generateId='
						+ hsiId + '&isMaintain=' + isMaintain + '"></iframe>');
		check();
	}
	
	// HSI生成页面，跳转到HSI生成界面
	function goToChoose(areaId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/lhirf/lhSelect/init.do?areaId='
						+ areaId
						+ '&isMaintain=' + isMaintain + '"></iframe>');
		check();				
	}


	// HSI分析页面，跳转跳转到LH1
	function goToLh1(areaId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/lhirf/lh_1/init.do?hsiId='
						+ areaId
						+ '&isMaintain=' + isMaintain + '"></iframe>');
		check();				
	}

	// HSI分析页面，跳转跳转到LH1A
	function goToLh1A(areaId, isMaintain) {
		myMask.show();
		Ext.getCmp('rightcenter').body
		
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/lhirf/lh_1a/init.do?hsiId='
						+ areaId
						+ '&isMaintain=' + isMaintain + '"></iframe>');
		check();				
	}

	var win = new Ext.Window({
		layout : 'border',
		border : false,
		id : 'cnm',
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
			width : 215,
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
