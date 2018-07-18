/**
 * 此页面用来显示用户信息 可进行 增 删 改 查操作。
 * 
 * @authour zhouli createDate 2012-08-1
 * 
 * 
 */
Ext.namespace('waitTask');
Ext.onReady(function() {
	var rendToDivName = 'waitTaskGrid';
	
	Ext.form.Field.prototype.msgTarget = 'qtip'; // 用来显示验证信息
	Ext.QuickTips.init();

	//----------------分析开始--------------------------------------------------------------------------------------	
	function getAnalysisStatusNameBycode(analysisStatusCode){
		if(analysisStatusCode == null || analysisStatusCode == ""){
			return '未建';
		}else if(conAnalysisStatusNewCode == analysisStatusCode){
			return conAnalysisStatusNewName;
		}else if(conAnalysisStatusMaintainCode == analysisStatusCode){
			return conAnalysisStatusMaintainName;
		}else{
			return "";
		}
	}
	
	var analysissm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
	var analysiscm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [{
					header : 'mainId',
					dataIndex : 'mainId',
					hidden : true
				}, {
					header : 'ataorareaId',
					dataIndex : 'ataorareaId',
					hidden : true
				}, {
					header : '<div align="center">系统</div>',
					dataIndex : 'analysisType',
					align : 'center',
					width : 100,
					renderer : function(value, cellmeta, record) {// approval_ReportTypeChoose
						return getAnalysisNameBycode(value);
					}
				}, {
					header : '<div align="center">编号</div>',
					dataIndex : 'ataorareaCode',
					width : 120

				}, {
					header : '<div align="center">名称</div>',
					dataIndex : 'ataorareaName',
					width : 150
				}, {
					header : '<div align="center">状态</div>',
					dataIndex : 'status',
					align : 'center',
					width : 100,
					renderer : function(value, cellmeta, record) {
						return getAnalysisStatusNameBycode(value);
					}
				}, {
					header : 'urlParam',
					dataIndex : 'urlParam',
					hidden : true
				}, {
					header : '<div align="center">操作</div>',
					dataIndex : 'checkOperate',
					align : 'center',
					width : 100
				}]
	});

	var analysisstore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : contextPath + '/homepage/getWaitAnalysisLis.do'
						}),
				reader : new Ext.data.JsonReader({
							root : 'analysis',
							fields : [{
										name : 'mainId',
										type : 'string'
									}, {
										name : 'ataorareaId',
										type : 'string'
									}, {
										name : 'ataorareaCode',
										type : 'string'
									}, {
										name : 'ataorareaName',
										type : 'string'
									}, {
										name : 'status',
										type : 'string'
									}, {
										name : 'createDate',
										type : 'string'
									}, {
										name : 'urlParam',
										type : 'string'
									}, {
										name : 'analysisType',
										type : 'string'
									}, {
										name : 'checkOperate',
										type : 'string'
									}]
						})
			});

	analysisstore.on("beforeload", function() {
		analysisstore.baseParams = {
					start : 0,
					limit : 10
				};
			});
	
	// create the editor grid
	var analysisgrid = new Ext.grid.EditorGridPanel({
		title : '分析',
		store : analysisstore,
		cm : analysiscm,
		sm : analysissm,
		header : false,
		id : 'analysisgrid',
		stripeRows : true,
//		renderTo : rendToDivName,
		split : true,
		height : '333',
		collapsible : true,
		frame : true,
		clicksToEdit : 2,
		bbar : new Ext.PagingToolbar({
					pageSize : 10,
					store : analysisstore,
					displayInfo : true,
					displayMsg : commonality_turnPage,
					emptyMsg : commonality_noRecords
				}),
		listeners:{'beforeshow':function(){
//			alert(1);
//			document.body.focus();
		  }
		}
	}

	// grid function over
	); // grid over
//	analysisgrid.render();
	analysisgrid.addListener("rowdblclick", rowdblclickanalysis);
	analysisstore.load();
	//---审核弹出开始---------------------------------------------
	function rowdblclickanalysis(grid,row){
		var dbrow = grid.store.getAt(row);
		doAnalysisOpenWindow(dbrow.get("analysisType"), dbrow.get("urlParam"));
	}
	//点审核按钮，操作
	doAnalysisOpenWindow = function(analysisType, urlParam){
		parent.window.doAnalysisOpenWindow(analysisType, urlParam);
	};
	//刷新操作
	reFlashAnalysisGrid = function(){
		analysisstore.reload();
	};
	//---审核弹出结束---------------------------------------------
	//----------------分析结束--------------------------------------------------------------------------------------
	//----------------审核开始--------------------------------------------------------------------------------------
	function getAnalysisNameBycode(analysisCode){
		if(analysisCode == null || analysisCode == ""){
			return "";
		}else if(zonalCode == analysisCode){
			return zonalCodeCn;
		}else if(lhrifCode == analysisCode){
			return lhrifCodeCn;
		}else if(structureCode == analysisCode){
			return structureCodeCn;
		}else if(systemCode == analysisCode){
			return systemCodeCn;
		}else{
			return "";
		}
	}
	
	function getProcessStatusNameBycode(processStatusCode){
		if(processStatusCode == null || processStatusCode == ""){
			return "";
		}else if(conWaitCheckCode == processStatusCode){
			return conWaitCheckName;
		}else if(conCheckingCode == processStatusCode){
			return conCheckingName;
		}else if(conFinishCheckCode == processStatusCode){
			return conFinishCheckName;
		}else if(conCancelCheckCode == processStatusCode){
			return conCancelCheckName;
		}else{
			return "";
		}
	}
	
	var checksm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	var checkcm = new Ext.grid.ColumnModel({
	defaults : {
		sortable : true
	},
	columns : [{
		header : '',//id
		hidden : true,
		dataIndex : 'processId'
	}, {
		header : '<div align="center">分析系统</div>',// 四大分析
		width : 100,
		align : 'center',
		dataIndex : 'analysisType',
		renderer : function(value, cellmeta, record) {// approval_ReportTypeChoose
			return getAnalysisNameBycode(value);
		}
	}, {
		header : '<div align="center">流程状态</div>',//流程状态
		width : 100,
		align : 'center',
		dataIndex : 'processStatus',
		renderer : function(value, cellmeta, record) {
			return getProcessStatusNameBycode(value);
		}
	}, {
		header : '对象编号', //用逗号分开
		width : 150,
		dataIndex : 'ataorareaCode'
	}, {
		header : '<div align="center">发起人</div>', //发起人
		width : 100,
		align : 'center',
		dataIndex : 'launchUser'
	}, {
		header : '<div align="center">流程发起时间</div>', //流程发起时间
		width : 150,
		align : 'center',
		dataIndex : 'launchDate'
	}, {
		header : "<div align='center'>审核</div>", //审核操作
		width : 60,
		align : 'center',
		dataIndex : 'checkOperate'
	}]
	});
	
	var checkstore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : contextPath + '/com/process/loadCheckProcessForHomePage.do'
		}),
		reader : new Ext.data.JsonReader({
			root : 'comProcess',
			fields : [{
						name : 'processId',
						mapping : 'processId',
						type : 'string'
					}, {
						name : 'analysisType',
						mapping : 'analysisType',
						type : 'string'
					}, {
						name : 'processStatus',
						mapping : 'processStatus',
						type : 'string'
					}, {
						name : 'launchUser',
						mapping : 'launchUser',
						type : 'string'
					}, {
						name : 'launchDate',
						mapping : 'launchDate',
						type : 'string'
					}, {
						name : 'checkOperate',
						mapping : 'checkOperate',
						type : 'string'
					}, {
						name : 'ataorareaCode',
						mapping : 'ataorareaCode',
						type : 'string'
					}]
		})
	});
	
	checkstore.on("beforeload", function() {
			checkstore.baseParams = {
				start : 0,
				limit : 10
			};
	});
	
	// create the editor grid
	var checkgrid = new Ext.grid.EditorGridPanel({
	title : '审核',
	store : checkstore,
	cm : checkcm,
	sm : checksm,
	header : false,
	id : 'checkgrid',
	stripeRows : true,
//	renderTo : rendToDivName,
	split : true,
	height : '333',
	collapsible : true,
	frame : true,
	clicksToEdit : 2,
	bbar : new Ext.PagingToolbar({
				pageSize : 10,
				store : checkstore,
				displayInfo : true,
				displayMsg : commonality_turnPage,
				emptyMsg : commonality_noRecords
			}),
	listeners:{'beforeshow':function(){
//				alert(2);
//				document.body.focus();
			  }
		}
	}
	
	// grid function over
	); // grid over
//	checkgrid.render();
	checkgrid.addListener("rowdblclick", rowdblclickcheck);
	checkstore.load();
	function rowdblclickcheck(grid,row){
		var dbrow = grid.store.getAt(row);
//		alert(grid.store.getAt(row).get("analysisType"));
//		alert(grid.store.getAt(row).get("processStatus"));
		checkSingleOpenWindow(dbrow.get("processId"), dbrow.get("analysisType"), dbrow.get("processStatus"));
	}
	//---审核弹出开始---------------------------------------------
	//点审核按钮，操作
	checkSingleOpenWindow = function(processId, analysisType, processStatus){
		parent.window.checkSingleOpenWindow(processId, analysisType, processStatus);
	};
	//刷新操作
	reFlashCheckGrid = function(){
		checkstore.reload();
	};
	//---审核弹出结束---------------------------------------------
	//----------------审核结束--------------------------------------------------------------------------------------
	var tabPanel = new Ext.TabPanel({
		//activeTab : 0,
//		layout : 'border',
		split : true,
		border : false,
		id : 'tabpanel',
		deferredRender:false,
		renderTo : Ext.getBody(),
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
//		plain : true,
		//items : [analysisgrid, checkgrid],
		listeners : { 
			'beforetabchange' : function(tab, newTab, currentTab){
//				alert(document.activeElement.id);
//				out.write(document.activeElement.id);
//				this.onFocus=this.blur();
				document.body.focus();
				//alert(document.getElementsByTagName("A").length);
//				var objs = document.getElementsByTagName("A");
//				for(var i=0; i<objs.length; i++){
////					objs[i].removeAttribute("id");
//					var tmp = objs[i].attributes.onFocus;
//					if(tmp != null && tmp.value == 'undefined'){
//						//alert(tmp.value);
//						objs[i].onFocus = null;
//					}
////					if(tmp == 'undefined'){
////						objs[i].removeAttribute("onFocus");
////					}
//				}
//				alert(1);
//				setTimeout("console.log('5秒!')",500);
			}
		} 
	});
	if(isProfessionAnalysis || isAdmin){
		tabPanel.add(analysisgrid);
		tabPanel.setActiveTab(analysisgrid); 
	}
	if(isProfessionEngineer || isAdmin){
		tabPanel.add(checkgrid);
		tabPanel.setActiveTab(checkgrid);
	}
	tabPanel.doLayout();
	
	var window = new Ext.Window({
				layout : 'border',
				id : 'cnm',
				closable : false,
				resizable : false,
				maximized : true,
				plain : true,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				items : [{
					region : 'center',
					layout : 'fit',
					collapsible : false,// 面板可收缩参数,默认是false
					width : '100%',
					split : true,
					items : [tabPanel]
				}]
			});
	window.show();
});
