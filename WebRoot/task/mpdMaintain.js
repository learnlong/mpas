Ext.namespace('mpdMaintain');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/task/mpdMaintain/";

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
				['LHIRF', 'L/HIRF'],
				['ONESELFADD', '自加']
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

	// 任务类型
	var typeCombo1 = new Ext.form.ComboBox({
		name : 'chooseType1',
		id : 'chooseType1',
		store : new Ext.data.SimpleStore({
			fields : [ "retrunValue", "displayText" ],
			data : [
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
		url : urlPrefix + 'searchTaskMpd.do',
		reader : new Ext.data.JsonReader({
			root : 'taskMpd',
			fields : [ 
				{name : 'mpdId'},
				{name : 'mpdCode'},
				{name : 'sourceSystem'},
				{name : 'taskType'},
				{name : 'taskDesc'},
				{name : 'reachWay'},
				{name : 'taskIntervalOriginal'},
				{name : 'ownArea'},
				{name : 'effectiveness'},
				{name : 'amm'},
				{name : 'workTime'}
			]
		})
	});

	store.load({
		params : {
			start : 0,
			limit : 20,
			sourceSystem : "",
			taskType : "",
			mpdCode : ""
		}
	});

	var colM = new Ext.grid.ColumnModel([
		{
			header : 'ID',
			dataIndex : "mpdId",
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
				}else if (value == 'ONESELFADD'){
					return '自加';
				}
			}
		}, {
			header : 'MPD编号' + commonality_mustInput,
			dataIndex : "mpdCode",
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
			width : 90,
            align : 'center',
			editor : typeCombo1,
			renderer : function(value, cellmeta, record) {
				var index = typeCombo1.store.find(Ext.getCmp('chooseType1').valueField, value);
				var record = typeCombo1.store.getAt(index);
				var returnvalue = "";
				if (record) {
					returnvalue = record.data.displayText;
				}
				return returnvalue;
			}
		}, {
			header : '标准任务间隔',
			dataIndex : "taskIntervalOriginal",
			width : 120,
            align : 'center',
			renderer : changeBR,
			editor : new Ext.form.TextField({
				allowBlank : true,
				maxLength : 200,
				maxLengthText : '输入信息长度超过200个字符!'
			})
		}, {
			header : '任务描述',
			dataIndex : "taskDesc",
			width : 230,
            align : 'center',
			renderer : changeBR,
			editor : new Ext.form.TextArea({
				allowBlank : true,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!',
				grow : true
			})
		}, {
			header : '接近方式',
			dataIndex : "reachWay",
			width : 230,
            align : 'center',
			renderer : changeBR,
			editor : new Ext.form.TextArea({
				allowBlank : true,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!',
				grow : true
			})
		}, {
			header : '所属区域',
			dataIndex : "ownArea",
			width : 100,
            align : 'center',
			renderer : changeBR,
			editor : new Ext.form.TextField({
				regex : /^(\d+,)*\d+$/,
				regexText : '请输入区域号并用逗号隔开 !',
				maxLength : 40,
				maxLengthText : '输入信息长度超过40个字符!'
			})
		}, {
			header : '适用性',
			dataIndex : "effectiveness",
			width : 120,
            align : 'center',
			renderer : changeBR,
			editor : new Ext.form.TextArea({
				allowBlank : true,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!',
				grow : true
			})
		}, {
			header : 'AMM参考' + commonality_mustInput,
			dataIndex : "amm",
			width : 120,
            align : 'center',
			renderer : changeBR,
			editor : new Ext.form.TextArea({
				allowBlank : false,
				maxLength : 500,
				maxLengthText : '输入信息长度超过500个字符!',
				grow : true
			})
		}, {
			header : '工时' + commonality_mustInput,
			dataIndex : "workTime",
			width : 70,
            align : 'center',
			editor : new Ext.form.NumberField({
				fieldLabel : '两位小数',
				nanText : '输入格式不正确', // 无效数字提示
				maxLength : 10,// 输入字段允许的最大字符数
				maxValue : 9999999999,// 允许最大值
				allowNegative : false
			})
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
				'MPD编号：', codeCombo, '-',
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
					text : commonality_add,
					iconCls : 'icon_gif_add',
					handler : add
				}), '-', 
				new Ext.Button({
					text : commonality_save,
					iconCls : 'icon_gif_save',
					handler : save
				}), '-',
				new Ext.Button({
					text : '删除自加任务',
					iconCls : 'icon_gif_delete',
					handler : del
				})
			]
		})
	});

	grid.addListener("beforeedit", beforeedit);
	// 判断是否是自加MPD，
	function beforeedit(val) {
		var sourceSystem = val.record.get('sourceSystem');
		var field = val.field;		
		
		if (sourceSystem != 'ONESELFADD' && (field != 'workTime' && field != 'amm' && field != 'mpdCode')) {
			val.cancel = true;
			return;
		}		
	}

	grid.addListener("afteredit", afteredit);
	// 修改后操作
	function afteredit(val) {
		var count = 0;
		var nowRecord = val.record;

		if (val.field == 'mpdCode') {
			if (val.value == null || val.value == '') {
				alert('MPD编号不能为空');
				nowRecord.set('mpdCode', val.originalValue);
				return;
			}
			
			var items = store.data.items;
			for ( var i = 0; i < items.length; i++) {
				if (items[i].get('mpdCode') == val.value) {
					count++;
				}
				if (count == 2) {
					alert('MPD任务编号已经存在 !');
					nowRecord.set('mpdCode', val.originalValue);
					return;
				}
			}

			Ext.Ajax.request({
				url : urlPrefix + 'checkTaskMpdCode.do',
				params : {
					mpdId : val.record.data.mpdId,
					mpdCode : val.value
				},
				success : function(response) {
					if (response.responseText == "false") {
						alert('MPD任务编号已经存在 !');
						nowRecord.set('mpdCode', val.originalValue);
					}
				}
			});
		}

		if (val.field == 'ownArea') {
			Ext.Ajax.request({
				url : contextPath + "/struct/verifyArea.do",
				params : {
					verifyStr : val.value
				},
				method : "POST",
				success : function(response) {
					if(response.responseText){
						var msg= Ext.util.JSON.decode(response.responseText);
						if(msg.exists){
							alert("区域:"+ msg.exists +"不能重复,请重新修改");
							nowRecord.set('ownArea',val.originalValue);
							return ;
						}
						if(msg.unExists){
							alert("区域:"+ msg.unExists+"不存在,请重新修改");
							nowRecord.set('ownArea',val.originalValue);
							return ;
						}
						if (msg.success) {
							alert("不能转入区域:"+ msg.success+"，该区域已被冻结或者已经审批完成，请重新修改");
							nowRecord.set('ownArea',val.originalValue);
							return ;
						}	
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
		title : returnPageTitle('MPD维护', 'mpdMaintain'),
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
				mpdCode : Ext.getCmp('chooseCode').getValue()
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
			url : urlPrefix + 'saveMpd.do',
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

	function add() {
		var index = store.getCount();
		var rec = grid.getStore().recordType;
		var p = new rec({
			mpdId : '',
			mpdCode : '',
			sourceSystem : 'ONESELFADD',
			taskType : '',
			taskDesc : '',
			reachWay : '',
			taskIntervalOriginal : '',
			ownArea : '',
			effectiveness : '',
			amm : '',
			workTime : ''
		});
		store.insert(index, p);
	}

	function del() {
		var record = grid.getSelectionModel().getSelected();
		if (record == null) {
			alert(commonality_alertDel);
			return;
		}
		if (record.get('sourceSystem') != 'ONESELFADD') {
			alert('非自加任务不能删除！');
			return;
		}

		Ext.MessageBox.show({
			title : commonality_affirm,
			msg : commonality_affirmDelMsg,
			buttons : Ext.Msg.YESNO,
			fn : function(yesNo) {
				if (yesNo == 'cancel') {
					return;
				} else if (yesNo == 'yes') {
					if (record.get('mpdId') == '') {
						store.remove(record);
						store.modified.remove(record);
						return;
					} else {
						doDelete(record);
					}
				}
			}
		});
	}

	function doDelete(record) {
		Ext.Ajax.request({
			url : urlPrefix + 'delTaskMpd.do',
			params : {
				mpdId : record.get('mpdId')
			},
			method : 'POST',
			success : function(form, action) {
				store.modified = [];
				store.reload();
				alert(commonality_messageDelMsg);
			},
			failure : function(form, action) {
				alert(commonality_cautionMsg);
			}
		});
	}
});