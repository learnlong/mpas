Ext.namespace('ext_areaAnalyze');
Ext.onReady(function(){
	var treeRootNode = new Ext.tree.AsyncTreeNode({
		text : 'ATA',
		id : '0'
	});
	var treeLoader=new Ext.tree.TreeLoader({
		url:contextPath+"/struct/tree/getAtaOrSsiTree.do"
	});
	var title11;
	var isHidden = false;
	var gridColViewFlag = false;
	if (type == 'report') {// 版本管理页面
		title11 = "结构报告管理";
		gridColViewFlag = false;
	}
	if (type == 'choose') {// Ssi选择页面
		title11 = "SSI选择";
		gridColViewFlag = false;
		isHidden = true;
	}
	if (type == 'analysis') {//结构分析页面
		title11 = "SSI分析";
		gridColViewFlag = true;
	}
	treeLoader.on('beforeload',function(treeLoader, node){
		node.on('append',function(tree, thiz, newNode, index){
			if (newNode.isLeaf()){
				newNode.on('click',function(thiz, e){
				});
			}
		});
		if (!node.isLeaf()){
			treeLoader.baseParams['id'] = node.id;
			treeLoader.baseParams['level'] = node.attributes.level;
			treeLoader.baseParams['searchType'] = searchType;
		}
	});
			
	var treePanel = new Ext.tree.TreePanel({
		title :'结构树状列表',/// '结构树状列表',
		id : 'areaTreePanel',
		header : false,
		loader : treeLoader,
		root : treeRootNode,
		autoScroll : true,
		listeners : {
			"click" : function(node){
				treeOnclick(node);
			}
		}
	});  
			// 定义右键菜单
			var rightClick = new Ext.menu.Menu({
				id : 'rightClickCont',
				items : [{
					id : 'playflash',
					text : commonality_playFlash,
					listeners:{
					"click" : function(field) {
							var treeR=treePanel.getSelectionModel().getSelectedNode();
							var id=treeR.id;
							var isSsi_T=treeR.attributes.isSsi;
								Ext.Ajax.request({
									url:contextPath+"/struct/tree/getFlashUrl.do",
									waitTitle : commonality_waitTitle,
									waitMsg : commonality_waitMsg,
									params : {
										isSsi :isSsi_T,
										id:id
									},
									method : "POST",
									success : function(response) {
										palyFalsh(response.responseText);
									},
									failure : function() { } 
								});
						}
					}
				}]
			});	
			
				// 增加右键弹出事件
			treePanel.on('contextmenu', function(node, event) {// 声明菜单类型
				//alert(node.value);
				if(node.id==undefined||""==node.id){
					return;
				}
				
				event.preventDefault();// 这行是必须的，使用preventDefault方法可防止浏览器的默认事件操作发生。
				node.select();
				rightClick.showAt(event.getXY());// 取得鼠标点击坐标，展示菜单
			});
		treeRootNode.expand(); 
			///treeRootNode.on('onload',function(){
			///})
	var ownGridStore = new Ext.data.Store({
		url : contextPath+"/struct/tree/getAtaOrSsiGrid.do",
		reader : new Ext.data.JsonReader({
			root : 'ata',
			fields : [
				{
					name : 'ssiId',
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
					name : 'statusDB',
					type : 'string'
				}, {
					name : 'isSsi',
					type : 'string'
				}, {
					name : 'ataId',
					type : 'string'
				}, {
					name : 'level',
					type : 'int'
				}, {
					name : 'openStatus',
					type : 'string'
				}
			]
		})
	});
	
	ownGridStore.load({
		params:{
			searchType : searchType
		}
	});
	
	var cm = new Ext.grid.ColumnModel({
		columns : [ 
			{
				id : 'ataCode',
				header : ataOrMsi,
				dataIndex : 'ataCode',
				width : 80
			},{
				header : '名称',
				id : 'ataName',
				hidden : gridColViewFlag,
				dataIndex : 'ataName',
				width : isHidden==true?120:70
			},{
				header : '状态',
				hidden : isHidden,
				id : 'status',
				dataIndex : 'status',
				width : 62
			}, {
			   header : 'statusDB',
			   hidden:true,
				id : 'statusDB',
				dataIndex : 'statusDB',
				width : 20
			
			},{
				id : 'ataId',
				hidden:true,
				header : 'ataId',
				dataIndex : 'ataId',
				width : 35
			}, {
				id : 'level',
				hidden:true,
				header : 'level',
				dataIndex : 'level',
				width : 35
			}, {
				id : 'openStatus',
				header : '<div align="center">解锁操作</div>',
				dataIndex : 'openStatus',
				align : 'center',
				hidden : !gridColViewFlag,
				width : gridColViewFlag==true?70:60
			}]
	});

	openAnalysisStratus = function(ssiId){
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
							url : contextPath + '/struct/tree/openAnalysisStatus.do',
							params : {
								ssiId : ssiId
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
			
	var ownGrid = new Ext.grid.GridPanel( {
		id : 'editorGridPanelId',
		title : '可维护ATA或SSI',
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
		html :'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="' + contextPath + '/analysWelcome.jsp"> </iframe>'
	});
	
	function treeOnclick(n) {
		var ataId = n.attributes.id;
		var ssiId = n.attributes.ssiId;
		var level = n.attributes.level;
		var isMaintain = n.attributes.isMaintain;
		var isRef = n.attributes.isRef;
		var leaf = n.attributes.leaf;
		var status = n.attributes.status;
		var isSsi = n.attributes.isSsi;
		var ssiId = n.attributes.ssiId;
		if (type == 'report'){//版本管理页面
			if (ssiId == null){//点击根目录，系统不做任何反应
				return;
			} else {//点击除根目录之外的目录，都跳转到版本管理界面
				goToReport(ssiId, isMaintain);
			}
		}
		if (type == 'choose'){//选择生成页面
			if (ataId == 0 || level == 1){//点击根目录、第一级目录、第四级目录，系统不做任何反应
				return;
			} else {//点击第三级目录，跳转到SSI选择界面
				goToChoose(ataId, isMaintain,ssiId);
			}
		}
		if (type == 'analysis'){//MSI分析页面
			if (ataId == 0 || level == 1 ){//点击根目录、第一级目录、第二级目录，系统不做任何反应
				return;
			} /*else if (level == 3){//点击第三级目录，跳转到生成测试PDF的界面
				return;
//				goToPdf(ataId, isMaintain,level);
			}*/ else if (leaf == true){//是叶子节点
				if (isMaintain == 1){//登录用户拥有维护权限
					if (isSsi == 1){//跳转SSI分析界面
						if(status== statusHOlD||status== statusAPPROVED){
							isMaintain=0;
						}
						goToSsi(ataId, isMaintain,ssiId);
					} else if(isSsi == 2){//跳转非SSI分析界面
						if(status== statusHOlD||status== statusAPPROVED){
							isMaintain=0;
						}
						goToNoSsi(ataId, isMaintain,ssiId);
					}
				} else if (status != statusNew && status != statusMaintain){//登录用户只有查看权限的MSI，那么需要判断当前SSI只有是分析完成之后的状态才能查看
					if (isSsi == 1){//跳转SSI分析界面
						if(status==statusHOlD||status== statusAPPROVED){
							isMaintain=0;
						}
						goToSsi(ataId, isMaintain,ssiId);
					} else if(isSsi == 2){//跳转非SSI分析界面
						if(status==statusHOlD||status==statusAPPROVED){
							isMaintain=0;
						}
						goToNoSsi(ataId, isMaintain,ssiId);
					}
				}
			} else {//点击第四级目录，系统不做任何反应
				return;
			}
		}
	}
	
	function celldbclick(ownGrid, rowIndex, columnIndex, e) {
		currentRecord = ownGrid.getStore().getAt(rowIndex);
		var ataId = currentRecord.get('ataId');
		var isSsi = currentRecord.get('isSsi');
		var level= currentRecord.get('level');
		var status= currentRecord.get('statusDB');
		var ssiId= currentRecord.get('ssiId');
		var isMaintain = 1;

		if (type == 'report'){//版本管理页面，跳转到版本管理界面
			goToReport(ssiId, isMaintain);
		} else if (type == 'choose'){//跳转到SSI选择界面
				if(status== statusHOlD||status==statusAPPROVED){
						isMaintain=0;
					}
			if(level==4){
				alert("当前节点为最终节点，不能继续追加！！！");
				return;
			}
			goToChoose(ataId, isMaintain,ssiId);
		} else if (type == 'analysis'){//跳转SSI分析界面
			if (isSsi == 1){
					if(status==statusHOlD||status==statusAPPROVED){
						isMaintain=0;
					}
				goToSsi(ataId, isMaintain,ssiId);
			} else {
					if(status==statusHOlD||status==statusAPPROVED){
						isMaintain=0;
					}
				goToNoSsi(ataId, isMaintain,ssiId);
			}
		} 
	}
	var myMask=new Ext.LoadMask(Ext.getBody(),{//等待提示信息
	msg:commonality_waitMsg
});
function check() {
	setTimeout(function () {
		if(window.frames["ifrm_area"].document.readyState=="complete") {
			myMask.hide();
		}else {
			setTimeout(function () {
				check();
			},100);
		}
	},100);
}
	//版本管理页面，跳转到版本管理界面
	function goToVersion(ataId, isMaintain,level){
	    myMask.show();
		Ext.getCmp('rightcenter').body.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'+contextPath+'/struct/version/initVersion.do?ataId='+ataId+'&isMaintain='+isMaintain+'&level='+level+'"></iframe>');
	    check();
	}
	//报告管理
	function goToReport(ssiId, isMaintain){
		myMask.show();
		Ext.getCmp('rightcenter').body
				.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'
						+ contextPath
						+ '/com/report/init.do?reportType=' + reportType + '&generateId='
						+ ssiId + '&isMaintain=' + isMaintain + '"></iframe>');
		check();
	}
	//SSI选择页面，跳转到SSI选择界面
	function goToChoose(ataId, isMaintain,ssiId){
		myMask.show();
		Ext.getCmp('rightcenter').body.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'+contextPath+'/struct/ssiSelect/init.do?ataId='+ataId+'&isMaintain='+isMaintain+'+&ssiId='+ssiId+'"></iframe>');
	    check();
	}
	
	
	//SSI分析页面，跳转跳转到S1
	function goToSsi(ataId, isMaintain,ssiId){
	    myMask.show();
		Ext.getCmp('rightcenter').body.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'+contextPath+'/struct/initS1.do?ssiId='+ssiId+'&isMaintain='+isMaintain+'"></iframe>');
	    check();
	}
	
	//SSI分析页面，跳转跳转到非ssi分析界面
	function goToNoSsi(ataId, isMaintain,ssiId){
		myMask.show();
		Ext.getCmp('rightcenter').body.update('<iframe name="ifrm_area" id="ifrm_area" marginwidth=0 marginheight=0 frameborder="0" scrolling="no" style="width: 100%; height: 100%;" src="'+contextPath+'/struct/unSsi/init.do?ssiId='+ssiId+'&isMaintain='+isMaintain+'"></iframe>');
	    check();
	}
	
	var win = new Ext.Window({
		layout : 'border',
		id : 'cnm',
		resizable : true,
		border:false, 
     	 frame :false,
		closable : false,
		maximized : true,
		plain : false,
		//bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		items : [
			{
				region : 'west',
				layout : 'fit',
				title : title11,
				collapsible : true,//面板可收缩参数,默认是false
				width : 215,
				split : true,
				//border : false, 
				items : [ 
					new Ext.TabPanel({
						activeTab : 0,
						split : true,
						//border : false,						
						items : [treePanel, ownGrid]
					}) 
				]
			},
			{ 
				region : 'center',
				//border : false, 
				layout : 'border',
				items : [rightPanel]
			}
		]
	});

	win.show();
});
function palyFalsh(a){
	if(!a){
		alert(commonality_uploadFlash);
		return null;
	}
	var win = new Ext.Window({
				//layout : 'border',
				border : false,
				resizable : true,
				closable : true,
				//maximized : true,
				plain : false,
				layout:'fit',
//				height:400,
//				width:600,
				modal:true,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
//				html:'<embed src="/mpas/'+a+'" width="600" height="400"></embed>'
				html:'<table width="60px"><tr><td><embed src="'+contextPath+a+'" width="600" height="400"></embed></td></tr></table>'
				
			});
			win.show();
}
		