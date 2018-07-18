Ext.namespace('areaTaskTrackSearch');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/taskTrackSearch/";	

	var taskCode = new Ext.form.TextField ({
		name : 'taskCode',
		id : 'taskCode',
		width : 100,
		emptyText : '--全部--',
		editable : false
	});		

	var store = new Ext.data.Store({
		url : urlPrefix + 'findTaskTrackList.do',
		reader : new Ext.data.JsonReader({
			root : "tasks",
			fields : [
				{name : "taskId"},
				{name : 'taskCode'},
				{name : 'taskType'},
				{name : 'reachWay'},
				{name : 'taskDesc'},
				{name : 'taskInterval'},
				{name : 'showGo'},
				{name : 'showDestTask'},
				{name : 'areaCode'}
			]
		})
	});

	store.on("beforeload", function() {
		store.baseParams = {
			start : 0,
			limit : 20
		};
	});
	store.load();

	var colM = new Ext.grid.ColumnModel([
		{
			header : "ID", 
			dataIndex : "taskId",
			hidden : true
		}, {
			header : '所属区域', 
			dataIndex : "areaCode",
			width : 120,
			align : 'center'
		}, {
			header : '任务编号', 
			dataIndex : "taskCode",
			width : 120,
			align : 'center'
		}, {
			header : '任务类型', 
			dataIndex : "taskType",
			width : 120,
			align : 'center'
		}, {
			header : '任务间隔', 
			dataIndex : "taskInterval",
			width : 120,
			align : 'center'
		}, {
			header : '接近方式', 
			dataIndex : "reachWay",
			width : 150,
			align : 'center',
			renderer: changeBR
		}, {
			header : '任务描述', 
			dataIndex : "taskDesc",
			width : 150,
			align : 'center',
			renderer: changeBR
		}, {
			header : '任务去向', 
			dataIndex : "showGo",
			width : 150,
			align : 'center'
		}, {
			header : '合并的任务', 
			dataIndex : "showDestTask",
			width : 150,
			align : 'center',
			renderer: changeBR
		}
	]);

	var grid = new Ext.grid.GridPanel({
		title : returnPageTitle('区域任务追踪查询', 'areaTaskTrackSearch'),
		region : 'center',
		loadMask: {msg : '加载数据中,请等待......'},
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
		store : store,
		stripeRows : true,				
		tbar : new Ext.Toolbar({
			items : [
				'任务编号：', taskCode, '-',
				{
					text : commonality_search,
					iconCls : "icon_gif_search",
					handler : function() {
						store.on("beforeload", function() {
							store.baseParams = {
								start : 0,
								limit : 20,
								taskCode : Ext.getCmp('taskCode').getValue().trim()
							}
						});	
						store.load();
					}
				}, '-',
				{//重置
					text : commonality_reset,
					xtype : "button",
					iconCls : "icon_gif_reset",
					handler : function() {
						Ext.getCmp('taskCode').setValue('');
					}
				
				}
			]
		}),
		bbar : new Ext.PagingToolbar({
			pageSize : 20,
			store : store,
			displayInfo : true,
			displayMsg : commonality_turnPage,
			emptyMsg : commonality_noRecords
		})
	});

	var viewport = new Ext.Viewport( {
		id : 'viewportId',
		layout : 'fit',
		items : [grid]
	});
});