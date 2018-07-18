Ext.namespace('mrbMaintain');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/task/mrbMaintain/";

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
			header : 'MRB编号' + commonality_mustInput,
			dataIndex : "mrbCode",
			width : 120,
            align : 'center',
			editor : new Ext.form.TextField({
				allowBlank : false,
				maxLength : 50,
				maxLengthText : commonality_MaxLengthText,
				regex : /[\w\-]+$/,
				regexText : '编号格式不正确，只能输入字母，数字，横杠 !'
			})
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

	var grid = new Ext.grid.EditorGridPanel({
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		loadMask : {
			msg : commonality_waitMsg
		},
		store : store,
		clicksToEdit : 2,
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
				}), '-', 
				new Ext.Button({
					text : commonality_save,
					iconCls : 'icon_gif_save',
					handler : save
				})
			]
		})
	});

	grid.addListener("afteredit", afteredit);
	// 修改后操作
	function afteredit(val) {
		var count = 0;
		if (val.field == 'mrbCode') {
			var nowRecord = val.record;

			if (val.value == null || val.value == '') {
				alert('MRB编号不能为空');
				nowRecord.set('mrbCode', val.originalValue);
				return;
			}
			
			var items = store.data.items;
			for ( var i = 0; i < items.length; i++) {
				if (items[i].get('mrbCode') == val.value) {
					count++;
				}
				if (count == 2) {
					alert('MRB任务编号已经存在 !');
					nowRecord.set('mrbCode', val.originalValue);
					return;
				}
			}

			Ext.Ajax.request({
				url : urlPrefix + 'checkTaskMrbCode.do',
				params : {
					mrbId : val.record.data.mrbId,
					mrbCode : val.value
				},
				success : function(response) {
					if (response.responseText == "false") {
						alert('MRB任务编号已经存在 !');
						nowRecord.set('mrbCode', val.originalValue);
					}
				}
			});
		}
	}

	var win = new Ext.Window({
		layout : 'fit',
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : false,
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		title : returnPageTitle('MRB维护', 'mrbMaintain'),
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

		store.modified = [];
	}

	function save() {
		var json = [];
		Ext.each(store.modified, function(item) {			
			json.push(item.data);
		});
		
		if (json.length > 0) {
			Ext.MessageBox.show({
				title : commonality_affirm,
				msg : commonality_affirmSaveMsg,
				buttons : Ext.Msg.YESNO,
				fn : function(id) {
					if (id == 'yes') {						
						saveData(json);
					}
				}
			});
		} else {
			alert(commonality_alertSave);
			return;
		}		
	}

	function saveData(json) {
		var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg
		});
		myMask.show();

		Ext.Ajax.request({
			url : urlPrefix + 'saveMrb.do',
			params : {
				jsonData : Ext.util.JSON.encode(json)
			},
			method : 'POST',
			success : function(form, action) {
				myMask.hide();
				store.modified = [];
				store.reload();
				alert(commonality_messageSaveMsg);
			},
			failure : function(form, action) {
				myMask.hide();
				alert(commonality_cautionMsg);
			}
		});		
	}
});