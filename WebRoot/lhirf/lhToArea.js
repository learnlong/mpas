Ext.namespace('extjsui');

extjsui.app = function() {
	return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
			// 每页显示条数
			var limit = 16;
			// /Hsi编号输入框
			var firstTextField = new Ext.form.TextField({
				id : "firstTextField",
				name : "firstTextField",
				width : 160,
				emptyText:"请输入您需要查询的HSI 编号"
			});
			// 区域编号输入框 ---
			var secondTextField = new Ext.form.TextField({
				id : "secondTextField",
				name : "secondTextField",
				width : 160,
				emptyText:"请输入转入的区域"
			});

			var store = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : extjsui.app.findSysTaskUrl
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : "totalCount",
									root : 'task',
									fields : [{
												name : 'taskId'
											}, {
												name : 'ownArea'
											}, {
												name : 'hsiCode'
											}, {
												name : 'hsiName'
											}, {
												name : 'taskCode'
											},{
												name : 'taskType'
											}, {
												name : 'taskDesc'
											}, {
												name : 'reachWay'
											}, {
												name : 'whyTransfer'
											}, {
												name : 'taskInterval'
											}, {
												name : 'hasAccept'
											}, {
												name : 'rejectReason'
											}, {
												name : 'whyTransfer'
											}, {
												name : 'areaTaskDesc'
											}, {
												name : 'areaReachWay'
											}, {
												name : 'areaTaskInterval'
											}, {
												name : 'areaTaskIntervalMerge'
											}, {
												name : 'taskArea'
											}]
								}),
						sortInfo : {
							field : 'taskCode',
							direction : 'ASC'
						}
					});

			var colM = new Ext.grid.ColumnModel([{
						header : "id",
						dataIndex : "taskId",
						width : 100,
						align : 'center',
						hidden : true
					}, {
						header : '区域编号',// "区域/Zone",
						dataIndex : "ownArea",
						width : 100,
						align : 'center',
						hidden : false
					}, {
						header : 'HSI 编号',
						dataIndex : "hsiCode",
						width : 80,
						align : 'center'
					}, {
						header : 'HSI 名称',// 'hsi名称(中文)',
						dataIndex : "hsiName",
						width : 120,
						renderer : changeBR
					}, {
						header : 'MSG-3任务号',// "MSG-3任务号/MSG-3 Task No.",
						dataIndex : "taskCode",
						width : 130
					}, {
						header : "任务类型",// "任务类型/Task Type",
						dataIndex : "taskType",
						width : 70,
						align : 'center'
					}, {
						header : "任务间隔",// "任务间隔/Task Interval",
						dataIndex : "taskInterval",
						width : 80,
						align : 'center'
					}, {
						header : "接近方式",// "接近方式/Access",
						dataIndex : "reachWay",
						width : 300,
						renderer : changeBR
					}, {
						header : "任务描述",// "任务描述/Task Description",
						dataIndex : "taskDesc",
						width : 300,
						renderer : changeBR
					}, {
						header : "转入区域",// "任务描述/Task Description",
						dataIndex : "taskArea",
						width : 100,
						renderer : changeBR
					}, {
						header : "转移原因",// "转移原因/Transferred// Reason",
						dataIndex : "whyTransfer",
						width : 300,
						renderer : changeBR
					}, {
						header : '区域是否接受',// "接受否？/Accepted?",
						dataIndex : "hasAccept",
						width : 80,
						align : 'center',
						renderer : function(value, cellmeta, record) {
							if (value == '1') {
								return commonality_shi;
							} else if (value == '0') {
								return commonality_fou;
							} else {
								return "";
							}
						}
					}, {
						header : '区域任务描述',// "区域任务描述
						dataIndex : "areaTaskDesc",
						width : 300,
						renderer : changeBR
					}, {
						header : '区域接近方式',// "区域接近方式/Zonal Task
						dataIndex : "areaReachWay",
						width : 300,
						renderer : changeBR
					}, {
						header : "区域任务间隔",// "区域任务间隔/Zonal Task
						dataIndex : "areaTaskInterval",
						width : 120,
						align : 'center'
					}, {
						header : '合并后任务间隔',// "合并后间隔/Final Task
						dataIndex : "areaTaskIntervalMerge",
						width : 150,
						align : 'center'
					}, {
						header : '退回原因',// "不接受原因/Refused Reason",
						dataIndex : "rejectReason",
						width : 150,
						renderer : changeBR
					}]);

			

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
						clicksToEdit : 1,
						stripeRows : true,
						bbar : new Ext.PagingToolbar({
									pageSize : limit,
									store : store,
									displayInfo : true,
									displayMsg : commonality_turnPage,
									emptyMsg : commonality_noRecords
								}),
						tbar : [
								' HSI 编号',
								" :", // 区域第一节点
								firstTextField,
								'-',

								' 转入区域',
								"：",// 区域第二节点
								secondTextField,
								'-',
								new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
								new Ext.Button({
											text : commonality_search,
											iconCls : 'icon_gif_search',
											handler : search
										}),
							'-',	
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"), {
								text : commonality_reset,   //重置
								xtype : "button",
								iconCls : "icon_gif_reset",
								handler : function() {
									Ext.getCmp('firstTextField').setValue("") ;
									Ext.getCmp('secondTextField').setValue("");
								}
							}
										]
					});

			function search() {
				var firstTextField = Ext.getCmp('firstTextField').getValue();
				var secondTextField = Ext.getCmp('secondTextField').getValue();
					store.on("beforeload", function() {
							store.baseParams = {
								start : 0,
								limit : limit,
								firstTextField : firstTextField,
								secondTextField : secondTextField
							}
						});
						store.load();
			}
			var win = new Ext.Window({
						layout : 'border',
						border : false,
						resizable : true,
						closable : false,
						maximized : true,
						plain : false,
						bodyStyle : 'padding:0px;',
						buttonAlign : 'center',
						title : returnPageTitle('L/HRIF转区域任务查询', 'lh_task'),// task_title,
						items : [grid]
					});

			win.show();
			store.load({
						params : {
							start : 0,
							limit : limit
						}
					});
		}
	};
}();