Ext.namespace('mrbSearch');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/task/mrbSearch/";

	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();

	// 任务来源
	var sourceCombo = new Ext.form.ComboBox({
		name : 'chooseSource',
		id : 'chooseSource',
		store : new Ext.data.SimpleStore({
			fields : [ "retrunValue", "displayText" ],
			data : [
				['', '全部'],
				['SYS', '系统'],
				['STRUCTURE', '结构'],
				['ZONE', '区域'],
				['LHIRF', 'L/HIRF']					
			]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		emptyText : '--全部--',
		mode : 'local',
		editable : false,
		triggerAction : 'all',
		width : 100
	});

	// 任务类型
	var typeCombo = new Ext.form.ComboBox({
		name : 'chooseType',
		id : 'chooseType',
		store : new Ext.data.SimpleStore({
			fields : [ "retrunValue", "displayText" ],
			data : [
				['', '全部'],
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
		emptyText : '--全部--',
		mode : 'local',
		editable : false,
		triggerAction : 'all',
		width : 100
	});

	var codeCombo = new Ext.form.TextField({
		name : 'chooseCode',
		id : 'chooseCode',
		maxLength : 20,
		width : 100
	});

	var store = new Ext.data.Store({
		url : urlPrefix + 'searchTaskMrb.do',
		reader : new Ext.data.JsonReader({
			root : 'taskMrb',
			fields : [ 
				{name : 'mrbId'},
				{name : 'mrbCode'},
				{name : 'sourceSystem'},
				{name : 'taskType'},
				{name : 'taskDesc'},
				{name : 'reachWay'},
				{name : 'taskIntervalOriginal'},
				{name : 'ownArea'},
				{name : 'effectiveness'},				
				{name : 'msgTaskCode'}
			]
		})
	});

	store.load({
		params : {
			start : 0,
			limit : 20,
			sourceSystem : "",
			taskType : "",
			mrbCode : ""
		}
	});

	var colM = new Ext.grid.ColumnModel([
		{
			header : 'ID',
			dataIndex : "mrbId",
			hidden : true
		}, {
			header : '任务来源',
			dataIndex : "sourceSystem",
			width : 65,
            align : 'center',
			renderer : function(value, cellmeta, record){
				if (value == 'LHIRF'){
					return 'L/HIRF';
				}else if(value == 'STRUCTURE'){
					return '结构';
				}else if (value == 'SYS'){
					return '系统';
				}else if (value == 'ZONE'){
					return '区域';
				}
			}
		}, {
			header : 'MRB编号',
			dataIndex : "mrbCode",
			width : 120,
            align : 'center'
		}, {
			header : '任务类型',
			dataIndex : "taskType",
			width : 70,
            align : 'center'
		}, {
			header : '标准任务间隔',
			dataIndex : "taskIntervalOriginal",
			width : 120,
            align : 'center',
			renderer : changeBR
		}, {
			header : '任务描述',
			dataIndex : "taskDesc",
			width : 200,
            align : 'center',
			renderer : changeBR
		}, {
			header : '接近方式',
			dataIndex : "reachWay",
			width : 200,
            align : 'center',
			renderer : changeBR
		}, {
			header : '所属区域',
			dataIndex : "ownArea",
			width : 100,
            align : 'center',
			renderer : changeBR
		}, {
			header : '适用性',
			dataIndex : "effectiveness",
			width : 100,
            align : 'center',
			renderer : changeBR
		}, {
			header : 'MSG-3任务编号',
			dataIndex : "msgTaskCode",
			width : 120,
            align : 'center',
			renderer : changeBR
		}
	]);

	var grid = new Ext.grid.GridPanel({
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		loadMask : {
			msg : commonality_waitMsg
		},
		store : store,
		stripeRows : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 20,
			store : store,
			displayInfo : true,
			displayMsg : commonality_turnPage,
			emptyMsg : commonality_noRecords
		}),
		tbar :  new Ext.Toolbar({
			items : [
				'任务来源：', sourceCombo, '-',
				'任务类型：', typeCombo, '-',
				'MRB编号：', codeCombo, '-',
				new Ext.Button({
					text : commonality_search,
					iconCls : 'icon_gif_search',
					handler : search
				}), '-', 
				new Ext.Button({ // 重置
					text : commonality_reset,
					iconCls : "icon_gif_reset",
					handler : function() {
						Ext.getCmp('chooseCode').setValue('');
						Ext.getCmp('chooseType').clearValue();
						Ext.getCmp('chooseSource').clearValue();
					}
				})
			]
		})
	});

	var win = new Ext.Window({
		layout : 'fit',
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : false,
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		title : returnPageTitle('MRB查询', 'mrbSearch'),
		items : [ grid ]
	});
	win.show();

	function search() {
		store.load({
			params : {
				start : 0,
				limit : 20,
				sourceSystem : Ext.getCmp('chooseSource').getValue(),
				taskType : Ext.getCmp('chooseType').getValue(),
				mrbCode : Ext.getCmp('chooseCode').getValue()
			}
		});
	}
});