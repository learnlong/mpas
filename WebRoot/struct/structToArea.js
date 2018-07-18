Ext.namespace('extjsui');

extjsui.app = function() {
	return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
			// 每页显示条数
			var limit = 16;
			// ssi编号输入框
			var firstTextField = new Ext.form.TextField({
				id : "firstTextField",
				name : "firstTextField",
				width : 160,
				emptyText:"请输入您需要查询的SSI 编号"
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
									root : 'task',
									fields : [{
												name : 'taskId'
											}, {
												name : 'ownArea'
											}, {
												name : 'ssiCode'
											}, {
												name : 'ssiName'
											}, {
												name : 'taskCode'
											}, {
												name : 'workGroup'
											}, {
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
												name : 'whyTransfer'
											}, {
												name : 'areaTaskDesc'
											}, {
												name : 'areaReachWay'
											}, {
												name : 'areaTaskInterval'
											}, {
												name : 'areaTaskIntervalMerge'
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
						header : 'SSI 编号',
						dataIndex : "ssiCode",
						width : 80,
						align : 'center',
						renderer : changeBR
					}, {
						header : 'SSI 名称',
						dataIndex : "ssiName",
						width : 80,
						align : 'center',
						renderer : changeBR
					}, {
						header : 'MSG-3任务号',
						dataIndex : "taskCode",
						width : 80,
						align : 'center'
					}, {
						header : '任务类型',
						dataIndex : "taskType",
						width : 70,
						align : 'center'
					}, {
						header : '任务间隔',
						dataIndex : "taskInterval",
						width : 80,
						align : 'center'
					}, {
						header : '接近方式',
						dataIndex : "reachWay",
						width : 150,
						renderer : changeBR
					}, {
						header : '任务描述',
						dataIndex : "taskDesc",
						width : 150,
						renderer : changeBR
					}, {
						header : '转入区域',
						dataIndex : "ownArea",
						width : 100,
						renderer : changeBR,
						align : 'center'
					}, {
						header : '转移原因',
						dataIndex : "whyTransfer",
						width : 80,
						align : 'center',
						renderer : changeBR
					}, {
						header : '接受否？',
						dataIndex : "hasAccept",
						width : 100,
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
						header : '区域任务描述',
						dataIndex : "areaTaskDesc",
						width : 70,
						align : 'center',
						renderer : changeBR
					}, {
						header : '区域接近方式',
						dataIndex : "areaReachWay",
						width : 70,
						align : 'center',
						renderer : changeBR
					}, {
						header : '区域任务间隔',
						dataIndex : "areaTaskInterval",
						width : 70,
						align : 'center'
					}, {
						header : '合并后间隔',
						dataIndex : "areaTaskIntervalMerge",
						width : 70,
						align : 'center'
					}, {
						header : '不接受原因',// "不接受原因/Refused Reason",
						dataIndex : "rejectReason",
						width : 150,
						renderer : changeBR
					}]);

			var grid = new Ext.grid.EditorGridPanel({
						cm : colM,
						sm : new Ext.grid.RowSelectionModel({
									singleSelect : true
								}),
						region : 'center',
						store : store,
						clicksToEdit : 2,
						loadMask : {
							msg : commonality_waitMsg
						},
						stripeRows : true,
						bbar : new Ext.PagingToolbar({
									pageSize : limit,
									store : store,
									displayInfo : true,
									displayMsg : commonality_turnPage,
									emptyMsg : commonality_noRecords
								}),
						tbar : [
								' SSI 编号',
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
										}), '-',
								new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"),
								{
									text : commonality_reset, // 重置
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
						title : returnPageTitle('结构转区域任务查询', 'struct_task'),// task_title,
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