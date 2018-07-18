/**
 * 此页面用来发起审批流程操作。
 * 
 * @authour zhouli createDate 2012-08-20
 * 
 * 
 */
Ext.namespace('startProcess');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/com/process/";
	var rendToDivName = 'processGrid';
	var systemLevel='0';
	
    Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';

	var currentRowIndex = -1;
	var currentRecord;
	function cellclick(grid, rowIndex, columnIndex, e) {
		if (currentRowIndex != rowIndex) {
			currentRowIndex = rowIndex;
			currentRecord = grid.getStore().getAt(rowIndex);
		}
	}
//------------------------------------------------------------------//专业室
				//分析类型
				var analysisTypeCombo = new Ext.form.ComboBox({
							xtype : 'combo',
							name : 'analysisType',
							id : 'analysisType',
//							store : new Ext.data.SimpleStore({
//										fields : ["retrunValue", "displayText"],
//										data : [[systemCode, systemCodeCn],
//												[structureCode, structureCodeCn],
//												[zonalCode, zonalCodeCn],
//												[lhrifCode, lhrifCodeCn]]
//									}),
							store : new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : urlPrefix + 'getAnalysisTypeByuserId.do'
								}),
								reader : new Ext.data.JsonReader({
									root : 'analysisType',
									fields : [ {
										name : 'analysisCode',
										type : 'string'
									}, {
										name : 'analysisName',
										type : 'string'
									} ]
								})
							}),
							valueField : "analysisCode",
							displayField : "analysisName",
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
									analysisTypeComboSelect();
								}
							}
						});
				analysisTypeCombo.store.on("load",function(){   //对 ComboBox 的数据源加上 load 事件就好  
					if(this.getAt(0) != null){
						analysisTypeCombo.setValue(this.getAt(0).get('analysisCode'));
						analysisTypeComboSelect();
					}
				});
				analysisTypeCombo.store.load();
				
				function analysisTypeComboSelect() {
					checkUserCombo.setValue("");
					professionCombo.setValue("");
					professionCombo.store.on("load",function(){   //对 ComboBox 的数据源加上 load 事件就好  
						if(this.getAt(0) != null){
							professionCombo.setValue(this.getAt(0).get('professionId'));
							professionComboClick();
						}
				    });
					professionCombo.store.load({
						params : {
							analysisType: analysisTypeCombo.getValue()
						}
					});
					store.load({
								params : {
									start : 0,
									limit : 18,
									analysisType: analysisTypeCombo.getValue()
								}
					});
				}

				//专业室
				var professionCombo = new Ext.form.ComboBox({
							xtype : 'combo',
							fieldLabel : "专业室",
							name : 'professionType',
							id : 'professionType',
							store : new Ext.data.Store({
								baseParams: {analysisType: analysisTypeCombo.getValue()}, 
								proxy : new Ext.data.HttpProxy({
									url : urlPrefix + 'getProfessionForAnalysisByuserId.do'
								}),
								reader : new Ext.data.JsonReader({
									root : 'profession',
									fields : [ {
										name : 'professionId',
										type : 'string'
									}, {
										name : 'professionName',
										type : 'string'
									} ]
								})
							}),
							valueField : "professionId",
							displayField : "professionName",
							typeAhead : true,
							mode : 'local',
							triggerAction : 'all',
							emptyText : "",
							editable : false,
							selectOnFocus : true,
							width : 150,
							listeners : {
								select : professionComboClick
							}
						});
				
				function professionComboClick(){
//					checkUserCombo.store.on("load",function(){   //对 ComboBox 的数据源加上 load 事件就好  
//						if(this.getAt(0) != null){
//							checkUserCombo.setValue(this.getAt(0).get('userId'));
//							checkUserComboClick();
//						}
//				    });
					checkUserCombo.store.load({
						params : {
							professionId: professionCombo.getValue()
						}
					});
				}
				
				var checkUserCombo = new Ext.form.ComboBox({
					xtype : 'combo',
					fieldLabel : "审核人员",
					name : 'checkUser',
					id : 'checkUser',
					store : new Ext.data.Store({
						baseParams: {professionId: professionCombo.getValue()}, 
						proxy : new Ext.data.HttpProxy({
							url : urlPrefix + 'getCheckUserByProfessionId.do'
						}),
						reader : new Ext.data.JsonReader({
							root : 'checkUser',
							fields : [ {
								name : 'userId',
								type : 'string'
							}, {
								name : 'userName',
								type : 'string'
							} ]
						})
					}),
					valueField : "userId",
					displayField : "userName",
					typeAhead : true,
					mode : 'local',
					triggerAction : 'all',
					emptyText : "",
					editable : false,
					selectOnFocus : true,
					width : 150,
					listeners : {
						select : checkUserComboClick
					}
				});
			function checkUserComboClick(){
			
			}
			

	var sm = new Ext.grid.CheckboxSelectionModel({
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

	var cm = new Ext.grid.ColumnModel([sm, {
				header : "<div align='center'>分析ID</div>",
				hidden : true,
				dataIndex : 'mainId'
			}, {
				header : "<div align='center'>对象编号</div>",
				width : 150,
				align : 'center',
				dataIndex : 'analysisCode'
			}, {
				header : "<div align='center'>对象名称</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'analysisName'
			}, {
				header : "<div align='center'>分析状态</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'status'
			}, {
				header : "<div align='center'>完成时间</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'endDate'
			}]);
	cm.defaultSortable = true;

	store = new Ext.data.Store({
				baseParams: {analysisType: analysisTypeCombo.getValue()},
				proxy : new Ext.data.HttpProxy({
							url : urlPrefix + 'getAnalysisOver.do'
						}),
				reader : new Ext.data.JsonReader({
							root : 'analysis',
							totalProperty : 'total',
							fields : [{
										name : 'mainId',
										mapping : 'mainId',
										type : 'string'
									}, {
										name : 'analysisCode',
										mapping : 'analysisCode',
										type : 'string'
									}, {
										name : 'status',
										mapping : 'status',
										type : 'string'
									}, {
										name : 'analysisName',
										mapping : 'analysisName',
										type : 'string'
									}, {
										name : 'endDate',
										mapping : 'endDate',
										type : 'string'
									}]
						})
			});

	// create the editor grid
	var grid = new Ext.grid.EditorGridPanel({
				store : store,
				cm : cm,
				sm : sm,
				loadMask : {
					msg : commonality_waitMsg
				},
				header : false,
				id : 'grid',
				renderTo : rendToDivName,
				split : true,
				collapsible : true,
				frame : true,
				stripeRows : true,
				clicksToEdit : 1,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : commonality_turnPage,
							emptyMsg : commonality_noRecords
						}),
				tbar : new Ext.Toolbar({
							items : ['分析系统：',
							         analysisTypeCombo,
									 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
									 '工作室：',
									 professionCombo,
									 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
									 '审核人员'+ commonality_mustInput+'：',
									 checkUserCombo,
									 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
									 {
											text : commonality_save,
											iconCls : "icon_gif_save",
											handler: function() {
												var sel = sm.getSelections();// 多选框取值问题....!
												var choosedIds = '';
												if (sel != '' && sel != null) {
													Ext.each(sel, function(s) {
														choosedIds = choosedIds + s.get('mainId') + ',';
															});
													if (choosedIds == "" || choosedIds == null) {
														alert('请选择要发起审核的数据 !');
														return;
													}else{
														choosedIds = choosedIds.substring(0, choosedIds.length -1);
													}
													if (Ext.getCmp('analysisType').getValue() == "" || Ext.getCmp('analysisType').getValue() == null) {
														alert('分析系统不能为空!');
														return;
													}
													if (Ext.getCmp('checkUser').getValue() == "" || Ext.getCmp('checkUser').getValue() == null) {
														alert('审核人员不能为空!');
														return;
													}
													if (Ext.getCmp('checkOpinion').getValue() == "" || Ext.getCmp('checkOpinion').getValue() == null){
														alert('审批意见不能为空!');
														return;
													}
													Ext.Msg.confirm(commonality_affirm,
															commonality_affirmSaveMsg,
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
																		url : urlPrefix + 'startProcess.do',
																		params : {
																			analysisType : Ext.getCmp('analysisType').getValue(),
																			checkUser : Ext.getCmp('checkUser').getValue(),
																			checkOpinion : Ext.getCmp('checkOpinion').getValue(),
																			choosedIds : choosedIds
																		},
																		method : "POST",
																		// /waitMsg : commonality_waitMsg,
																		success : function(response) {
																			waitMsg.hide();
																			var result = Ext.util.JSON.decode(response.responseText);

																			if (result.success == 'true' || result.success === true) {
																				alert(commonality_messageSaveMsg);
																				sm.clearSelections();
																				store.reload();
																				Ext.getCmp('checkUser').setValue("");
																				Ext.getCmp('checkOpinion').setValue("");
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
																				store.load({
																					params : {
																						start : 0,
																						limit : 18,
																						analysisType: analysisTypeCombo.getValue()
																					}
																				});
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
													alert('请选择要发起审核的数据 !');
												}
											}
										}]
						})
			});
	grid.render();
	grid.addListener("cellclick", cellclick);
	
	//
	var checkOpinionPanel = new Ext.FormPanel({
		layout : 'fit',
		id : 'checkOpinionPanel',
		style : 'padding:0px;margin: 0px ; border: 0px',
		border : false, // 没有边框
		//labelWidth : 100,
		//labelAlign : "right",
		frame : true,
		layout : "form", // 整个大的表单是form布局
		items : [{   xtype : "textarea",
			fieldLabel : '发起审批意见'+ commonality_mustInput,// 意见
			width : 697,
			height : 60,
			name : 'checkOpinion',
			id : 'checkOpinion',
			listeners : {
				
			}
		}]

	});

	var win = new Ext.Window({
				width : gridWidth,
				layout : 'border',
				closable : false,
				resizable : false,
				maximized : true,
				plain : true,
				 id : 'cnm',
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				items : [{
					region : 'north',
					height : 100,
					layout : 'form',
					title : returnPageTitle('发起审批', 'startApprove'),
					items : [checkOpinionPanel]
				}, {
					region : 'center',
					layout : "form",
					layout : 'fit',
					split : true,
					items : [grid]
				}]

			});
	win.show();
});