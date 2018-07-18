/**
 * 此页面用来流程审批操作。
 */
Ext.namespace('processCheck');

var urlPrefix = contextPath + "/com/process/";
var rendToDivName = 'processCheckGrid';
var selectProcessId = '';
var selectAnalysisType = '';
Ext.onReady(function() {
	
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init(); 
	
	var currentRowIndex = -1;
	var currentRecord;

	function cellclick(grid, rowIndex, columnIndex, e) {
		if (currentRowIndex != rowIndex) {
			currentRowIndex = rowIndex;
			currentRecord = grid.getStore().getAt(rowIndex);
		}
	}
	
	//分析系统
	var analysisTypeCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'queryAnalysisType',
				id : 'queryAnalysisType',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [[null, '全部'],
							        [systemCode, systemCodeCn],
									[structureCode, structureCodeCn],
									[zonalCode, zonalCodeCn],
									[lhrifCode, lhrifCodeCn]]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				fieldLabel : zonalCodeCn,
				triggerAction : 'all',
				width : 100,
				emptyText : "请选择",
				editable : false,
				selectOnFocus : true,
				listeners : {
					"select" : function() {
						//comboSelect();
					}
				}
			});

	//分析系统
	var processStatusCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'queryProcessStatus',
				id : 'queryProcessStatus',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [[null, '全部'],
							        [conWaitCheckCode, conWaitCheckName],
									[conCheckingCode, conCheckingName],
									[conFinishCheckCode, conFinishCheckName],
									[conCancelCheckCode, conCancelCheckName]]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				fieldLabel : zonalCodeCn,
				triggerAction : 'all',
				width : 100,
				emptyText : "请选择",
				editable : false,
				selectOnFocus : true,
				listeners : {
					"select" : function() {
						//comboSelect();
					}
				}
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
				listeners : {
					beforerowselect : function(SelectionModel, rowIndex, keepExisting, record) {
						
					}
				}
			});
	
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			ortable : true
		},
		columns : [sm,{
					header : '流程ID',//id
					hidden : true,
					dataIndex : 'processId'
				}, {
					header : '分析系统',// 四大分析
					width : 100,
					dataIndex : 'analysisType',
					renderer : function(value, cellmeta, record) {// approval_ReportTypeChoose
						var index = analysisTypeCombo.store.find(Ext.getCmp('queryAnalysisType').valueField,value);
						var record = analysisTypeCombo.store.getAt(index);
						var returnvalue = "";
						if (record) {
							returnvalue = record.data.displayText;
						}
						return returnvalue;
					}
				}, {
					header : '流程状态',//流程状态
					width : 100,
					dataIndex : 'processStatus',
					renderer : function(value, cellmeta, record) {
						var index = processStatusCombo.store.find(Ext.getCmp('queryProcessStatus').valueField,value);
						var record = processStatusCombo.store.getAt(index);
						var returnvalue = "";
						if (record) {
							returnvalue = record.data.displayText;
						}
						return returnvalue;
					}
				}, {
					header : '对象编号', //用逗号分开
					width : 250,
					dataIndex : 'ataorareaCode'
				}, {
					header : '发起人', //发起人
					width : 150,
					dataIndex : 'launchUser'
				}, {
					header : '流程发起时间', //流程发起时间
					width : 150,
					dataIndex : 'launchDate'
				}, {
					header : "<div align='left'>审核</div>", //审核操作
					width : 60,
					align : 'center',
					dataIndex : 'checkOperate'
				}]
	});
	
	var checkListStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : urlPrefix + 'loadCheckProcess.do'
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

	// create the editor grid
	var grid = new Ext.grid.EditorGridPanel({
		store : checkListStore,
		cm : cm,
		sm : sm,
		loadMask : {
			msg : commonality_waitMsg
		},
		header : false,
		id : 'grid1',
		renderTo : rendToDivName,
		split : true,
		collapsible : true,
		frame : true,
		stripeRows : true,
		clicksToEdit : 1,
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : checkListStore,
					displayInfo : true,
					displayMsg : commonality_turnPage,
					emptyMsg : commonality_noRecords
				}),
		tbar : new Ext.Toolbar({
			items : []
		})
			// tbar over
	}		// grid function over
	); // grid over
 this.tbar1 =new Ext.Toolbar({
 	        renderTo : grid.tbar,
			items : [
					"分析系统：",
					analysisTypeCombo,
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'-',
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					"流程状态：",
					processStatusCombo,
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'-',
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'发起人：',
					{
						xtype : 'textfield',
						id : 'launchUserName',
						width : 100
					}]
		});
	this.tbar2 = new Ext.Toolbar({
		renderTo : grid.tbar,
		items : [
				"开始时间：",
				new Ext.form.DateField({
							name : 'fromDate',
							id : 'fromDate',
							format : 'Y-m-d'
						}),
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				'-',
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				"结束时间：",
				new Ext.form.DateField({
							name : 'toDate',
							id : 'toDate',
							format : 'Y-m-d'
						}),
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				'-',
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				{
					text : commonality_search,
					iconCls : "icon_gif_search",
					id : 'sericon',
					handler : function() {
						var fromDate = Ext.getCmp('fromDate').getRawValue();
						var toDate = Ext.getCmp('toDate').getRawValue();
						var a=/^(\d{4})-(\d{2})-(\d{2})$/; 
						if(fromDate!=''&&fromDate!=null){
							if (!a.test(fromDate)){
	                            Ext.getCmp('fromDate').setRawValue("");
	                            return;
							}
							
						}
						if(toDate!=''&&toDate!=null){
							if (!a.test(toDate)){ 
								Ext.getCmp('toDate').setRawValue("");
	                            return;
							}
						}
						if(fromDate!=''&&fromDate!=null&&toDate!=''&&toDate!=null){
						    if(toDate<fromDate){
							    alert('结束时间不能小于开始时间!');
							    return;
						    }
						}
						checkListStore.load();
					}
				},
				new Ext.Toolbar.TextItem("&nbsp;"),
				{   
					text : commonality_reset,///重置
							xtype : "button",
							iconCls : "icon_gif_reset",
							handler : function() {
								Ext.getCmp('queryAnalysisType').clearValue();
								Ext.getCmp('queryProcessStatus').clearValue();
								Ext.getCmp('launchUserName').reset();
								Ext.getCmp('fromDate').reset();
								Ext.getCmp('toDate').reset();
							}
				},
				new Ext.Toolbar.TextItem("&nbsp;"),
				{   
						text : '通过',//'通过',///
								xtype : "button",
								iconCls : "icon_gif_pass",
								handler : function(){
									var sel = sm.getSelections();// 多选框取值问题....!
									var choosedIds = '';
									var statusFlag = false;
									if (sel != '' && sel != null) {
										Ext.each(sel, function(s) {
											//只处理待审批和审批中的
											if(conWaitCheckCode == s.get('processStatus') || conCheckingCode == s.get('processStatus')){
												choosedIds = choosedIds + s.get('processId') + ',';
											}else{
												statusFlag = true;
											}
										});
										if(statusFlag){
											alert('审核的流程状态只允许为待审核和审核中！');
											return;
										}
										if (choosedIds == "" || choosedIds == null) {
											alert('请选择要审核的数据 !');
											return;
										}else{
											choosedIds = choosedIds.substring(0, choosedIds.length -1);
										}
										Ext.Msg.confirm(commonality_affirm,
												'确认要批量审核通过吗?',
												function(btn) {
													if (btn == 'yes') {
														var waitMsg = new Ext.LoadMask(
																Ext.getCmp('cnm').body, {
																	msg : commonality_waitMsg,
																	// 完成后移除
																	removeMask : true
																});
														waitMsg.show();
														Ext.Ajax.request({
															url : urlPrefix + 'batchCheckPass.do',
															params : {
																choosedIds : choosedIds
															},
															method : "POST",
															// /waitMsg : commonality_waitMsg,
															success : function(response) {
																waitMsg.hide();
																var result = Ext.util.JSON.decode(response.responseText);
																if (result.success == 'true' || result.success === true) {
																	alert('审核完成');
																	sm.clearSelections();
																	checkListStore.reload();
																	// 清空全选框
																	var hd_checker = Ext
																			.getCmp("grid")
																			.getEl()
																			.select('div.x-grid3-hd-checker');
																	var hd = hd_checker.first();
																	// 清空表格头的checkBox
																	if (hd.hasClass('x-grid3-hd-checker-on')) {
																		hd.removeClass('x-grid3-hd-checker-on');
																	}
																	//grid重新加载
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
									} else {
										alert('请选择要审核的数据 !');
									}
								}
				},
				new Ext.Toolbar.TextItem("&nbsp;"),
				{   
						text : '不通过',//'不通过',///
								xtype : "button",
								iconCls : "icon_gif_noPass",
								handler : function() {
									var sel = sm.getSelections();// 多选框取值问题....!
									var choosedIds = '';
									var statusFlag = false;
									if (sel != '' && sel != null) {
										Ext.each(sel, function(s) {
											//只处理待审批和审批中的
											if(conWaitCheckCode == s.get('processStatus') || conCheckingCode == s.get('processStatus')){
												choosedIds = choosedIds + s.get('processId') + ',';
											}else{
												statusFlag = true;
											}
										});
										if(statusFlag){
											alert('审核的流程状态只允许为待审核和审核中！');
											return;
										}
										if (choosedIds == "" || choosedIds == null) {
											alert('请选择要审核的数据 !');
											return;
										}else{
											choosedIds = choosedIds.substring(0, choosedIds.length -1);
										}
										Ext.Msg.confirm(commonality_affirm,
												'确认要批量审核不通过吗?',
												function(btn) {
													if (btn == 'yes') {
														var waitMsg = new Ext.LoadMask(
																Ext.getCmp('cnm').body, {
																	msg : commonality_waitMsg,
																	// 完成后移除
																	removeMask : true
																});
														waitMsg.show();
														Ext.Ajax.request({
															url : urlPrefix + 'batchCheckNotPass.do',
															params : {
																choosedIds : choosedIds
															},
															method : "POST",
															// /waitMsg : commonality_waitMsg,
															success : function(response) {
																waitMsg.hide();
																var result = Ext.util.JSON.decode(response.responseText);
																if (result.success == 'true' || result.success === true) {
																	alert('审核完成');
																	sm.clearSelections();
																	checkListStore.reload();
																	// 清空全选框
																	var hd_checker = Ext
																			.getCmp("grid")
																			.getEl()
																			.select('div.x-grid3-hd-checker');
																	var hd = hd_checker.first();
																	// 清空表格头的checkBox
																	if (hd.hasClass('x-grid3-hd-checker-on')) {
																		hd.removeClass('x-grid3-hd-checker-on');
																	}
																	//grid重新加载
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
									} else {
										alert('请选择要审核的数据 !');
									}
								}
				},
				new Ext.Toolbar.TextItem("&nbsp;"),
				{   
						text : '取消审核',//'取消审核',///
								xtype : "button",
								iconCls : "icon_gif_delete",
								handler : function() {
									var sel = sm.getSelections();// 多选框取值问题....!
									var choosedIds = '';
									var statusFlag = false;
									if (sel != '' && sel != null) {
										Ext.each(sel, function(s) {
											//只处理待审批和审批中的
											if(conWaitCheckCode == s.get('processStatus') || conCheckingCode == s.get('processStatus')){
												choosedIds = choosedIds + s.get('processId') + ',';
											}else{
												statusFlag = true;
											}
										});
										if(statusFlag){
											alert('取消审核的流程状态只允许为待审核和审核中！');
											return;
										}
										if (choosedIds == "" || choosedIds == null) {
											alert('请选择要取消审核的数据 !');
											return;
										}else{
											choosedIds = choosedIds.substring(0, choosedIds.length -1);
										}
										Ext.Msg.confirm(commonality_affirm,
												'确认要批量取消审核?',
												function(btn) {
													if (btn == 'yes') {
														var waitMsg = new Ext.LoadMask(
																Ext.getCmp('cnm').body, {
																	msg : commonality_waitMsg,
																	// 完成后移除
																	removeMask : true
																});
														waitMsg.show();
														Ext.Ajax.request({
															url : urlPrefix + 'batchCheckCancel.do',
															params : {
																choosedIds : choosedIds
															},
															method : "POST",
															// /waitMsg : commonality_waitMsg,
															success : function(response) {
																waitMsg.hide();
																var result = Ext.util.JSON.decode(response.responseText);
																if (result.success == 'true' || result.success === true) {
																	alert('取消审核完成');
																	sm.clearSelections();
																	checkListStore.reload();
																	// 清空全选框
																	var hd_checker = Ext
																			.getCmp("grid")
																			.getEl()
																			.select('div.x-grid3-hd-checker');
																	var hd = hd_checker.first();
																	// 清空表格头的checkBox
																	if (hd.hasClass('x-grid3-hd-checker-on')) {
																		hd.removeClass('x-grid3-hd-checker-on');
																	}
																	//grid重新加载
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
									} else {
										alert('请选择要取消审核的数据 !');
									}
								}
				}
				]
	});
	grid.render();
	grid.addListener("cellclick", cellclick);
	dblclickListgrid = function(grid,row){
		var record = grid.store.getAt(row);
		var processId = record.get("processId");
		var analysisType = record.get("analysisType");
		var processStatus = record.get("processStatus");
		checkSingleOpenWindow(processId, analysisType, processStatus);
	};
	grid.addListener("rowdblclick", dblclickListgrid);
	
	checkListStore.on("beforeload", function() {
				checkListStore.baseParams = {
					start : 0,
					limit : 18,
					analysisType : analysisTypeCombo.getValue(),
					processStatus : processStatusCombo.getValue(),
					launchUserName : Ext.getCmp('launchUserName').getValue(),
					fromDate : Ext.getCmp('fromDate').getValue(),
					toDate : Ext.getCmp('toDate').getValue()
				};
			});
	checkListStore.load();

	var win = new Ext.Window({
				title : returnPageTitle('审批', 'approve'),
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
							split : true,
							items : [grid]
						}]

			});
	win.show();
	//------------------处理弹出审核----------------------------------------------------------------------------
	
	//点审核按钮，操作
	checkSingleOpenWindow = function(processId, analysisType, processStatus){
		//alert(processId + '--' + analysisType + '--' + processStatus);
		//alert(isOkCombo.store.getCount());
		//alert((Ext.get('grid1').dom.outerHTML).substring(1000));
		//得到流程信息
		selectProcessId = '';
		selectAnalysisType = '';
		opensm.clearSelections();
		Ext.getCmp('isOkType').clearValue();
		Ext.getCmp('isOkCheckpinion').setValue("");
		if(conWaitCheckCode == processStatus || conCheckingCode == processStatus){
			Ext.getCmp('isOkType').setDisabled(false);
			Ext.getCmp('isOkCheckpinion').setDisabled(false);
			Ext.getCmp('singleCheckDetailBtn').setDisabled(false);
			Ext.getCmp('allCheckDetailBtn').setDisabled(false);
		} else {
			Ext.getCmp('isOkType').setDisabled(true);
			Ext.getCmp('isOkCheckpinion').setDisabled(true);
			Ext.getCmp('singleCheckDetailBtn').setDisabled(true);
			Ext.getCmp('allCheckDetailBtn').setDisabled(true);
		}
		Ext.Ajax.request({
			url : urlPrefix + 'getComPrcessInfo.do',
			params : {
				processId : processId
			},
			method : "POST",
			// /waitMsg : commonality_waitMsg,
			success : function(response) {
				var result = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp('launchOpinion').setValue(result.launchOpinion);
				Ext.getCmp('startShenPiUser').setValue(result.startShenPiUser);
				Ext.getCmp('startShenPiData').setValue(result.startShenPiData);
				selectProcessId = result.processId;
				selectAnalysisType = result.analysisType;
			}
		});
		openstore.load({
					params : {
						processId : processId,
						analysisType : analysisType
					}
				});
		openWin.show();
		document.getElementById('ifrm_area').src = "";
	};
	
	Ext.MessageBox.getDialog().getEl().setStyle('z-index', '9013');
	
	//是否通过
	 isOkCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'isOkType',
				id : 'isOkType',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [[null, '请选择'],
							        [conIsOkYesCode, conIsOkYesName],
									[conIsOkNoCode, conIsOkNoName],
									[conIsOkCancelCode, conIsOkCancelName]]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				fieldLabel : conIsOkYesName,
				triggerAction : 'all',
				width : 100,
				emptyText : "请选择",
				editable : false,
				selectOnFocus : true,
				listeners : {
					"select" : function() {
						//comboSelect();
						//alert(isOkCombo.store.getCount());
					}
				}
			});
	
	//顶部的panel
	launchOpinionPanel = new Ext.FormPanel({
	    labelAlign: 'left',
	    title: '审批发起信息',
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    width: 600,
	    frame:true,
	    labelWidth:80,
	    items: [{
	        layout:'column',   //定义该元素为布局为列布局方式
	        border:false,
	        labelSeparator:'：',
	        items:[{
	            columnWidth:.6,  //该列占用的宽度，标识为50％
	            layout: 'form',
	            border:false,
	            items: [{xtype : "textarea",
					fieldLabel : '发起审批意见',// 意见
					width : 500,
					height : 60,
         			disabled : true,
					name : 'launchOpinion',
					id : 'launchOpinion',
					listeners : {
					}
				}]
	        },{
	            columnWidth:.15,
	            layout: 'form',
	            border:false,
	            items: [{
         			xtype: 'textfield',   
         			fieldLabel: '发起审批人 ',  ///审批发起人 
         			name: 'startShenPiUser'  ,
         			id:'startShenPiUser',
         			disabled : true,
         			width: 60
                	}]
	        },{
	            columnWidth:.25,
	            layout: 'form',
	            border:false,
	            items: [{
                	xtype: 'textfield',   
         			fieldLabel: '发起审批时间',  ///审批发起人 
         			name: 'startShenPiData'  ,
         			id:'startShenPiData',
         			disabled : true,
         			width: 130
                	}]
	        }]
	    }]
	});
	
	//中间grid
	opensm = new Ext.grid.CheckboxSelectionModel({
				//handleMouseDown:Ext.emptyFn,
				listeners : {
					beforerowselect : function(SelectionModel, rowIndex, keepExisting, record) {
//						var status = rstatus = record.get("ProofUserNameCn");
//						if (status = record.get("ProofUserNameCn") == '' || record.get("ProofUserNameEn") == '') {
//							alert(startProcess_msg);
//							return false;
//						}
					}
				}
			});

	opencm = new Ext.grid.ColumnModel([opensm, {
				header : '',
				hidden : true,
				dataIndex : 'detailId'
			}, {
				header : '',
				hidden : true,
				dataIndex : 'mainId'
			}, {
				header : "<div align='center'>分析系统</div>",
				width : 150,
				align : 'center',
				dataIndex : 'analysisType',
				renderer : function(value, cellmeta, record) {// approval_ReportTypeChoose
					var index = analysisTypeCombo.store.find(Ext.getCmp('queryAnalysisType').valueField,value);
					var record = analysisTypeCombo.store.getAt(index);
					var returnvalue = "";
					if (record) {
						returnvalue = record.data.displayText;
					}
					return returnvalue;
				}
			}, {
				header : "<div align='center'>对象编号</div>",
				width : 150,
				align : 'center',
				dataIndex : 'mainCode'
			}, {
				header : "<div align='center'>对象名称</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'mainName'
			}, {
				header : "<div align='center'>是否通过</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'isOk'
			}, {
				header : "<div align='center'>审批意见</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'checkOpinion'
			}, {
				header : '',
				hidden : true,
				dataIndex : 'urlParam'
			}]);
	opencm.defaultSortable = true;

	openstore = new Ext.data.Store({
				//baseParams: {analysisType: analysisTypeCombo.getValue()},
				proxy : new Ext.data.HttpProxy({
							url : urlPrefix + 'getProcessDetail.do'
						}),
				reader : new Ext.data.JsonReader({
							root : 'comProcessDetail',
							totalProperty : 'total',
							fields : [{
										name : 'detailId',
										mapping : 'detailId',
										type : 'string'
									}, {
										name : 'mainId',
										mapping : 'mainId',
										type : 'string'
									}, {
										name : 'analysisType',
										mapping : 'analysisType',
										type : 'string'
									}, {
										name : 'mainCode',
										mapping : 'mainCode',
										type : 'string'
									}, {
										name : 'mainName',
										mapping : 'mainName',
										type : 'string'
									}, {
										name : 'isOk',
										mapping : 'isOk',
										type : 'string'
									}, {
										name : 'checkOpinion',
										mapping : 'checkOpinion',
										type : 'string'
									}, {
										name : 'urlParam',
										mapping : 'urlParam',
										type : 'string'
									}]
						})
			});
	//openstore.load();
	
	// create the editor grid
	var opengrid = new Ext.grid.EditorGridPanel({
		store:openstore,
		cm : opencm,
		sm : opensm,
		header : false,
		id : 'opengrid',
		renderTo : rendToDivName,
		split : true,
		collapsible : true,
		frame : true,
		stripeRows : true,
		clicksToEdit : 1,
		tbar : new Ext.Toolbar({
					items : ['是否通过'+ commonality_mustInput+'：',
							 isOkCombo,
							 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							 '审批意见：',
							 {xtype : "textarea",
								width : 250,
								height : 40,
								name : 'isOkCheckpinion',
								id : 'isOkCheckpinion'
							 },
							 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							 {
									text : '选中审核',
									iconCls : "icon_gif_save",
									id : "singleCheckDetailBtn",
									handler: function() {
										var sel = opensm.getSelections();// 多选框取值问题....!
										var choosedIds = '';
										if (sel != '' && sel != null) {
											Ext.each(sel, function(s) {
												choosedIds = choosedIds + s.get('detailId') + ',';
											});
											if (choosedIds == "" || choosedIds == null) {
												alert('请选择要审核的数据 !');
												return;
											}else{
												choosedIds = choosedIds.substring(0, choosedIds.length -1);
											}
											if (Ext.getCmp('isOkType').getValue() == "" || Ext.getCmp('isOkType').getValue() == null) {
												alert('是否通过不能为空!');
												return;
											}
											Ext.Msg.confirm(commonality_affirm,
													'确认审核选中分析吗？',
													function(btn) {
														if (btn == 'yes') {
															var waitMsg = new Ext.LoadMask(
																	Ext.getCmp('opencnm').body, {
																		msg : commonality_waitMsg,
																		// 完成后移除
																		removeMask : true
																	});
															waitMsg.show();
															Ext.Ajax.request({
																url : urlPrefix + 'singleCheckProcessDetail.do',
																params : {
																	isOkType : Ext.getCmp('isOkType').getValue(),
																	isOkCheckpinion : Ext.getCmp('isOkCheckpinion').getValue(),
																	processId :selectProcessId,
																	analysisType : selectAnalysisType,
																	choosedIds : choosedIds
																},
																method : "POST",
																// /waitMsg : commonality_waitMsg,
																success : function(response) {
																	waitMsg.hide();
																	var result = Ext.util.JSON.decode(response.responseText);

																	if (result.success == 'true' || result.success === true) {
																		alert('审核成功');
																		opensm.clearSelections();
																		openstore.reload();
																		Ext.getCmp('isOkType').clearValue();
																		Ext.getCmp('isOkCheckpinion').setValue("");
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
										} else {
											alert('请选择要审核的数据 !');
										}
									}
								},
							 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							 {
									text : '全部审核',
									iconCls : "icon_gif_save",
									id : "allCheckDetailBtn",
									handler: function() {
										if (Ext.getCmp('isOkType').getValue() == "" || Ext.getCmp('isOkType').getValue() == null) {
											alert('是否通过不能为空!');
											return;
										}
										Ext.Msg.confirm(commonality_affirm,
												'确认审核全部分析吗？',
												function(btn) {
													if (btn == 'yes') {
														var waitMsg = new Ext.LoadMask(
																Ext.getCmp('opencnm').body, {
																	msg : commonality_waitMsg,
																	// 完成后移除
																	removeMask : true
																});
														waitMsg.show();
														Ext.Ajax.request({
															url : urlPrefix + 'allCheckProcessDetail.do',
															params : {
																isOkType : Ext.getCmp('isOkType').getValue(),
																isOkCheckpinion : Ext.getCmp('isOkCheckpinion').getValue(),
																processId :selectProcessId,
																analysisType : selectAnalysisType
															},
															method : "POST",
															// /waitMsg : commonality_waitMsg,
															success : function(response) {
																waitMsg.hide();
																var result = Ext.util.JSON.decode(response.responseText);

																if (result.success == 'true' || result.success === true) {
																	alert('审核成功');
																	opensm.clearSelections();
																	openstore.reload();
																	Ext.getCmp('isOkType').clearValue();
																	Ext.getCmp('isOkCheckpinion').setValue("");
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
									
									}
								}]
					})
		});
	opengrid.render();
	opengrid.addListener("cellclick", cellclick);
	dblclickOpengrid = function(grid,row){
		var record = grid.store.getAt(row);
		var analysisType = record.get("analysisType");
		var urlParam = record.get("urlParam");
		//alert(urlParam);
		var openUrl = contextPath;
		if(zonalCode == analysisType){//区域
			openUrl = openUrl + '/area/za1/init.do?isMaintain=0&areaId=' + urlParam ;
		} else if(lhrifCode == analysisType){//
			var parms = urlParam.split(',');
			if(parms[0] == null || parms[0] == '' || parms[0] == 'N/A'){
				openUrl = openUrl + '/lhirf/lh_1/init.do?isMaintain=0&hsiId=' + parms[1] ;
			}else{
				openUrl = openUrl + '/lhirf/lh_1a/init.do?isMaintain=0&hsiId=' + parms[1] ;
			}
		} else if(systemCode == analysisType){
			var parms = urlParam.split(',');
			openUrl = openUrl + '/sys/m0/init.do?isMaintain=0&msiId=' + parms[0] + '&ataId=' + parms[1];
		} else if(structureCode == analysisType){
			var parms = urlParam.split(',');
			if('0' == parms[0]){
				openUrl = openUrl + '/struct/unSsi/init.do?isMaintain=0&ssiId=' + parms[1] ;
			}else if('1' == parms[0]){
				openUrl = openUrl + '/struct/initS1.do?isMaintain=0&ssiId=' + parms[1] ;
			}
		} else{
			openUrl = openUrl + '/analysWelcome.jsp';
		}
		document.getElementById('ifrm_area').src = openUrl;
	};
	opengrid.addListener("rowdblclick", dblclickOpengrid);
	
	//底部的panel
	openBottomPanel = new Ext.Panel({
		region : 'center',
		id : "openBottomPanel",
		autoScroll : true,
		width : '100%',
		height : 300,
		Style : "background-image:url(../images/252-1)", 
		html :'<iframe id="ifrm_area" name="ifrm_area" scrolling="auto" frameborder="0" width="100%" height="100%" src=""></iframe>'
	});

	openWin = new Ext.Window({
        title: '审批',
        layout: 'border',
        modal : true,
        draggable : true,
        resizable : false,
        width : 1080,
		height : 600,
        plain:true,
        bodyStyle:'padding:0px;',
        buttonAlign:'center',
		closeAction:'hide',
		id:'opencnm',
        items: [{
        		region:'north',
      			layout:'fit',
      			split:true,
      			height:100,
  	      		items:[launchOpinionPanel]},
  	      		{
        		region:'center',
      			layout:'fit',
      			split:true,
      			height:200,
  	      		items:[opengrid]},
  	      		{
  	      		region:'south',
  	      		layout:'fit',
  	      		split:true,
  	      		height:300,
  	      		items:[openBottomPanel]
	      		  }]
   
    });
	openWin.on('hide',function(){
		checkListStore.reload();
		//alert(isOkCombo.store.getCount());
	});
});