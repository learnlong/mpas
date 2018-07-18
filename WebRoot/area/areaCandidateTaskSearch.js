Ext.namespace('areaCandidateTaskSearch');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/candidateTaskSearch/";	

	var sourceSystem = new Ext.form.ComboBox({
		name : 'sourceSystem',
		id : 'sourceSystem',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [
				['ALL', '全部'],
				['LHIRF', 'L/HIRF'],
				['SYS', '系统'],
				['STRUCTURE', '结构']
			]
		}),  
		valueField : "retrunValue",
		displayField : "displayText",
		typeAhead : true,
		mode : 'local',
		triggerAction : 'all',
		width : 100,
		emptyText : '--全部--',
		editable : false,
		selectOnFocus : true
	});

	var taskType = new Ext.form.ComboBox({
		name : 'taskType',
		id : 'taskType',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [
				['ALL', '全部'],
				['保养', '保养'],
				['使用检查', '使用检查'],
				['操作人员监控', '操作人员监控'],
				['功能检测', '功能检测'],
				['定时拆修', '定时拆修'],
				['定时报废', '定时报废'],
				['综合工作', '综合工作'],
				['GVI', 'GVI'],
				['DET', 'DET'],
				['SDI', 'SDI'],
				['FNC', 'FNC'],
				['DIS', 'DIS'],
				['RST', 'RST']				
			]
		}),  
		valueField : "retrunValue",
		displayField : "displayText",
		typeAhead : true,
		mode : 'local',
		triggerAction : 'all',
		width : 100,
		emptyText : '--全部--',
		editable : false,
		selectOnFocus : true
	});

	var taskCode = new Ext.form.TextField ({
		name : 'taskCode',
		id : 'taskCode',
		width : 100,
		emptyText : '--全部--',
		editable : false
	});		

	var store = new Ext.data.Store({
		url : urlPrefix + 'showAreaCandidateTaskList.do',
		reader : new Ext.data.JsonReader({
			root : "tasks",
			fields : [
				{name : "taskId"},
				{name : 'sourceSystem'},
				{name : 'taskCode'},
				{name : 'taskType'},
				{name : 'reachWay'},
				{name : 'taskDesc'},
				{name : 'taskInterval'},
				{name : 'showDestTask'}
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
			header : '所属系统', 
			dataIndex : "sourceSystem",
			width : 120,
			align : 'center',
			renderer : function(value, cellmeta, record){
				if (value == 'LHIRF'){
					return 'L/HIRF';
				}else if(value == 'STRUCTURE'){
					return '结构';
				}else if (value == 'SYS'){
					return '系统';
				}
			}
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
			header : '合并此任务的区域任务', 
			dataIndex : "showDestTask",
			width : 150,
			align : 'center',
			renderer: changeBR
		}
	]);

	var grid = new Ext.grid.GridPanel({
		title : returnPageTitle('区域合并任务查询', 'areaCandidate'),
		region : 'center',
		loadMask: {msg : '加载数据中,请等待......'},
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
		store : store,
		stripeRows : true,				
		tbar : new Ext.Toolbar({
			items : [
				'所属系统：', sourceSystem, '-',
				'任务类型：', taskType, '-',
				'任务编号：', taskCode, '-',
				{
					text : commonality_search,
					iconCls : "icon_gif_search",
					handler : function() {
						store.on("beforeload", function() {
							store.baseParams = {
								start : 0,
								limit : 20,								    
								sourceSystem : Ext.getCmp('sourceSystem').getValue(),
								taskType : Ext.getCmp('taskType').getValue().trim(),
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
						Ext.getCmp('sourceSystem').clearValue() ;
						Ext.getCmp('taskType').clearValue();
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