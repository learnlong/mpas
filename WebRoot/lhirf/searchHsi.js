
// 创建命名空间
Ext.namespace('lhsearch');

lhsearch.app = function() {
	return {

		init : function() {
			// 任务状态 lh_hsi combox
			var statusChooseCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'statusChoose',
						id : 'statusChoose',
						store : new Ext.data.SimpleStore({
									fields : ["retrunValue", "displayText"],
									data : [[statusNew,  '新建'],
											[statusMaintain, '正在分析'],
											[statusMinOK, '分析已完成'],
											[statusHOLD, '版本冻结'],
											[statusAPPROVED, "审批完成"]]
								}),
						valueField : "retrunValue",
						displayField : "displayText",
						typeAhead : true,
						model : 'local',
						triggerAction : 'all',
						width : 80,
						editable : false,
						selectOnFocus : true
					});

			// 数据源
			var store = new Ext.data.Store({
						url : lhsearch.app.lh_getSearchHsiUrl,
						reader : new Ext.data.JsonReader({
									root : "lhsearch",
									fields : [{
												name : 'hsiId'
											}, {
												name : 'areaCode'
											}, {
												name : 'ataCode'
											}, {
												name : 'hsiCode'
											}, {
												name : 'hsiName'
											}, {
												name : 'lhCompName'
											}, {
												name : 'ipvOpvpOpve'
											}, {
												name : 'refHsiCode'
											}, {
												name : 'anaUserName'
											}, {
												name : 'status'
											}, {
												name : 'modelName'
											}]
								})
					});
			// 数据列coM
			var colM = new Ext.grid.ColumnModel([{
						header : "LH_Hsi Id",
						dataIndex : "hsiId",
						hidden : true
					}, {
						header : "<div align='center'>" +  '区域'
						+ "</div>",
				dataIndex : "areaCode",
				align : 'center',
				width : 110
			},  {
						header : "<div align='center'>" +  'HSI 编号'
								+ "</div>",
						dataIndex : "hsiCode",
						align : 'center',
						width : 110
					}, {
						header : "<div align='center'>" + 'HSI 名称'
								+ "</div>",
						dataIndex : "hsiName", // hsi 名称(中文)
						renderer : changeBR,
						width : 150
					}, {
						header : "<div align='center'>" + '防护系统/部件名称'
								+ "</div>",
						dataIndex : "lhCompName", // 防护系统/部件名称中文
						renderer : changeBR,
						width : 300
					}, {
						header : "<div align='center'>" + 'ATA章节号'
								+ "</div>",
						dataIndex : "ataCode",
						align : 'center',
						width : 120
					}, {
						header : "IPV/OPVP/OPVE",
						dataIndex : "ipvOpvpOpve",
						align : 'center',
						width : 100
					}, {
						header : "<div align='center'>" + '参见的HSI编号'
								+ "</div>",
						dataIndex : "refHsiCode",
						align : 'center',
						width : 100
					}, {
						header : "<div align='center'>" + '状态'
								+ "</div>",
						dataIndex : "status",
						align : 'center',
						width : 90,
						renderer : function(value, cellmeta, record) {
							var index = statusChooseCombo.store.find(Ext
											.getCmp('statusChoose').valueField,
									value);
							var record = statusChooseCombo.store.getAt(index);
							var returnvalue = "";
							if (record) {
								returnvalue = record.data.displayText;
							}
							return returnvalue;
						}

					}, {
						header : "<div align='center'>" + '分析人'
								+ "</div>",// 分析人 中文名称
						dataIndex : "anaUserName",
						renderer : changeBR,
						width : 120
					}, {
						header : "<div align='center'>" + ' 机型名称'
								+ "</div>", // /机型 中文名称
						dataIndex : "modelName",
						renderer : changeBR,
						width : 133
					}]);
         
			
			// /区域节点第一节点combox
			var nodeFirstCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'nodeFirstComboxId',
						id : 'nodeFirstComboxId',
						store : new Ext.data.Store({
									proxy : new Ext.data.HttpProxy({
												url : lhsearch.app.lh_firstNodeListUrl
											}),
									reader : new Ext.data.JsonReader({
												root : 'nodeSearchList',
												fields : [{
															name : 'nodeId',
															type : 'string'
														}, {
															name : 'nodeCode',
															type : 'string'
														}]
											})
								}),
						valueField : "nodeId",
						displayField : "nodeCode",
						typeAhead : true,
						model : 'local',
						triggerAction : 'all',
						emptyText : ' --全部--',// /请选择
						editable : false,
						selectOnFocus : true,
						width : 100,
						listeners : {
							select : function(combo, record, index) { // 根据选中的机型选中旗下的区域节点有---
								Ext.getCmp('nodeTwoComboxId').clearValue();
								nodeTwoCombo.store.removeAll() ;  //清空第二节点 combox
								Ext.getCmp('nodeThreeComboId').clearValue();
								nodeThreeCombo.store.removeAll() ;///清空第三个节点的 combox 
								nodeTwoCombo.store.baseParams = {
									parentAreaId : combo.value,
									areaLevel : '2'
								};
								nodeTwoCombo.store.load();
							}
						}
					});
			nodeFirstCombo.store.on("beforeload", function() {
				nodeFirstCombo.store.baseParams = {
						areaLevel : '1'
				}
			});
			nodeFirstCombo.store.load();
			// 区域第二节点 ---
			var nodeTwoCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'nodeTwoComboxId',
						id : 'nodeTwoComboxId',
						store : new Ext.data.Store({
									proxy : new Ext.data.HttpProxy({
												url : lhsearch.app.lh_firstNodeListUrl
											}),
									reader : new Ext.data.JsonReader({
												root : 'nodeSearchList',
												fields : [{
															name : 'nodeId',
															type : 'string'
														}, {
															name : 'nodeCode',
															type : 'string'
														}]
											})
								}),
						valueField : "nodeId",
						displayField : "nodeCode",
						typeAhead : true,
						model : 'local',
						triggerAction : 'all',
						emptyText : ' --全部--',// /请选择
						editable : false,
						selectOnFocus : true,
						width : 100,
						listeners : {
							select : function(combo, record, index) { // 根据选中的机型选中旗下的区域节点有---
								Ext.getCmp('nodeThreeComboId').setValue('');
								nodeThreeCombo.store.removeAll() ;///清空第三个节点的 combox 
								nodeThreeCombo.store.baseParams = {
									parentAreaId : combo.value,
									areaLevel : '3'
								};
								nodeThreeCombo.store.load();
							}
						}
					});

			// /区域节点 3 级是 combox
			var nodeThreeCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'nodeThreeComboId',
						id : 'nodeThreeComboId',
						store : new Ext.data.Store({
									proxy : new Ext.data.HttpProxy({
												url : lhsearch.app.lh_firstNodeListUrl
											}),
									reader : new Ext.data.JsonReader({
												root : 'nodeSearchList',
												fields : [{
															name : 'nodeId',
															type : 'string'
														}, {
															name : 'nodeCode',
															type : 'string'
														}]
											})
								}),
						valueField : "nodeId",
						displayField : "nodeCode",
						typeAhead : true,
						model : 'local',
						triggerAction : 'all',
						emptyText : ' --全部--',// /请选择
						editable : false,
						selectOnFocus : true,
						width : 100
					});
         	// ***grid
			var grid = new Ext.grid.EditorGridPanel({
						cm : colM,
						sm : new Ext.grid.RowSelectionModel({
									singleSelect : true
								}),
						loadMask : {
							msg : commonality_waitMsg
						},
						region : 'center',
						store : store,
						tbar : [
						          '&nbsp;&nbsp;&nbsp;'
						   	   + '区域第一节点', "：",
				        	 nodeFirstCombo, '-',
							' 区域第二节点', "：", nodeTwoCombo, '-',
							' 区域第三节点', "：", nodeThreeCombo, '-',
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"), {
								text : commonality_search,
								xtype : "button",
								iconCls : "icon_gif_search",
								handler : function() {
								var firstComboxId = Ext.getCmp('nodeFirstComboxId').getValue();
								var twoComboxId = Ext.getCmp('nodeTwoComboxId').getValue();
								var threeComboId = Ext.getCmp('nodeThreeComboId').getValue();
								store.on("beforeload", function() {
									store.baseParams = {
											start : 0,
											limit : 14,
											parentNodeOneId :firstComboxId,
											parentNodeTwoId :twoComboxId,
											parentNodeThreeId : threeComboId
									}
								});
						   store.load();

								}
							}, 
							'-',	
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"), {
								text : commonality_reset,
								xtype : "button",
								iconCls : "icon_gif_reset",
								handler : function() {
								    nodeTwoCombo.getStore().removeAll()
									nodeThreeCombo.getStore().removeAll() ;///清空第三个节点的 combox 
									Ext.getCmp('nodeFirstComboxId').clearValue() ;
									Ext.getCmp('nodeTwoComboxId').clearValue();
									Ext.getCmp('nodeThreeComboId').clearValue();
								}
							}
									],
						stripeRows : true,
						bbar : new Ext.PagingToolbar({
									pageSize : 14,
									store : store,
									displayInfo : true,
									displayMsg : commonality_turnPage,
									emptyMsg : commonality_noRecords
								})
					});
			// /


			// //画面
			// *****
			var win = new Ext.Window({
						layout : 'border',
						border : false,
						resizable : true,
						closable : false,
						maximized : true,
						plain : false,
						bodyStyle : 'padding:0px;',
						buttonAlign : 'center',
						items : [{
									region : 'center',
									layout : 'fit',
									title : returnPageTitle('HSI 查询 ','searchHsi'),
									split : true,
									items : [grid]
								}

						]
					});
			win.show();
			store.on("beforeload", function() {
				store.baseParams = {
						start : 0,
						limit : 14
				}
			});
	        store.load();
		}
	}

}();
