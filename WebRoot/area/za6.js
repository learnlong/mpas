Ext.namespace('za6');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/za6/";	
	var currentRowIndex = -1;
	var currentRecord;

	// 检查必输项长度错误提示信息
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();
	
	var colM = new Ext.grid.ColumnModel([
		{
			header : "id",
			dataIndex : "taskId",
			hidden : true
		}, {
			header : '任务编号',
			dataIndex : "taskCode",
			width : 100,
			align : 'center'
		}, {
			header : '任务类型',
			dataIndex : "taskType",
			width : 90,
			align : 'center'
		}, {
			header : '接近方式',
			dataIndex : "reachWay",
			width : 240,
			align : 'center',
			renderer: changeBR
		}, {
			header : '任务描述',
			dataIndex : "taskDesc",
			width : 240,
			align : 'center',
			renderer: changeBR
		}, {
			header : '任务间隔',
			dataIndex : "taskInterval",
			width : 150,
			align : 'center'
		}, {
			header : '合并后任务间隔',
			dataIndex : "taskIntervalMerge",
			width : 150,
			align : 'center'
		}, {
			header : '处理方式',
			dataIndex : "transfer",
			width : 130,
			align : 'center',
			renderer : function(value, cellmeta, record) {
				if (value == 'pleaseSelect') {
					return '请选择';
				} else if (value == 'transferToAta20') {
					return '转移到ATA20章';
				} else if (value == 'toMpd') {
					return '进入维修大纲';
				} else if (value == 'N/A') {
					return 'N/A';
				} else if (value != null && value != "") {
					return value;
				}
			}
		}]
	);

	//ZA6页面列表的数据源
	var store = new Ext.data.Store({
		url : urlPrefix + "getZa6List.do",
		reader : new Ext.data.JsonReader({
			root : 'tasks',
			fields : [
				{name : 'taskId'}, 
				{name : 'taskCode'}, 
				{name : 'taskType'}, 
				{name : 'reachWay'}, 
				{name : 'taskDesc'}, 
				{name : 'taskInterval'}, 
				{name : 'taskIntervalMerge'}, 
				{name : 'destTask'}, 
				{name : 'transfer'}
			]
		})
	});

	store.on('beforeload', function(store) {
		store.baseParams['zaId'] = zaId;
		store.baseParams['areaId'] = areaId;
	});
	store.load();

	//弹出框中的选择框的数据源
	var taskStore = new Ext.data.Store({
		url : urlPrefix + 'getStandardTaskList.do?zaId=' + zaId,
		reader : new Ext.data.JsonReader({
			root : 'taskStore',
			fields : [
				{name : 'taskId'}, 
				{name : 'taskCode'}
			]
		})
	});
	taskStore.load();

	var grid = new Ext.grid.EditorGridPanel({
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
		store : store,
		stripeRows : true,
		clicksToEdit : 2,
		title : returnPageTitle('区域检查任务汇总表', 'za6')
	});
			
	grid.addListener("cellclick", function(grid, rowIndex, columnIndex, e) {
		currentRowIndex = rowIndex;
		currentRecord = store.getAt(rowIndex);

		if (columnIndex == 7 && currentRecord.get('transfer') != 'N/A') {
			if (analysisFlag) {
				openWindow();		
			}				
		}
	});

	var headerStepForm = new Ext.form.Label({
		applyTo : 'headerStepDiv'
	});

	var viewport = new Ext.Viewport( {
		layout : 'border',
		items : [
			{ 
				region : 'north',
				height : 60,
				frame : true,
				items : [headerStepForm]
			},{ 
				layout : 'fit',
				region : 'center',
				items : [grid]
			}
		]
	});

	function openWindow() {
		var transfer = currentRecord.get('transfer');
		var taskCode = currentRecord.get('taskCode');
		var taskInterval = currentRecord.get('taskInterval');
		var destTask = currentRecord.get('destTask');
		var taskType = currentRecord.get('taskType');

		// "处理方法"下拉框
		var chooseCombo = new Ext.form.ComboBox({
			name : 'chooseFun',
			id : 'chooseFun',
			fieldLabel : '处理方法' + commonality_mustInput,
			store : new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
				data : [['1', '转移到ATA20'],
						['2', '合并到标准任务'],
						['3', '进入维修大纲']]
			}),
			valueField : "retrunValue",
			displayField : "displayText",
			mode : 'local',
			editable : false,
			triggerAction : 'all',
			width : 120,
			listeners : {
				select : function(combo) {
					if (combo.value == '1' || combo.value == '3') {
						stdTaskCombo.clearValue();//清除"标准任务"下拉框
						Ext.getCmp('taskInterval1').setValue('');//清除"标准间隔参考时间"输入框
						Ext.getCmp('taskIntervalMerge').setValue('');//清除"填写合并后任务间隔"输入框	
						doDisable();
					} else {
						doEnable();
					}
				}
			}
		});

		//"标准任务"下拉选择框
		var stdTaskCombo = new Ext.form.ComboBox({
			fieldLabel : "标准任务" + commonality_mustInput,
			id : 'stdTask',
			name : 'stdTask',
			store : taskStore,
			editable : false,
			valueField : "taskId",
			displayField : "taskCode",
			mode : 'local',
			triggerAction : 'all',
			emptyText : '--请选择--',
			width : 128,
			listeners : {
				select : function(combo, recordA) {
					var index = store.findBy(function(record, id) {
						if (record.get('taskId') == combo.value) {
							return true;
						}
					});
					var record = store.getAt(index);
					if (record != undefined){
						Ext.getCmp('taskInterval1').setValue(record.get('taskInterval'));
						Ext.getCmp('taskIntervalMerge').setValue(record.get('taskIntervalMerge'));
					}
				}
			}
		});

		//弹出框内部表单
		var selectForm = new Ext.form.FormPanel({
			labelWidth : 150,
			labelAlign : "right",
			buttonAlign : 'center',
			frame : true,
			items : [
				chooseCombo,
				stdTaskCombo, 
				{
					xtype : 'textfield',
					fieldLabel : '标准任务的任务间隔',
					id : 'taskInterval1',
					readOnly : true
				}, {
					xtype : 'textfield',
					fieldLabel : taskCode + '任务间隔',//增强任务
					id : 'taskInterval2',
					value : taskInterval,
					readOnly : true
				}, {//"填写合并后任务间隔"输入框
					xtype : 'textfield',
					fieldLabel : '合并后的任务间隔' + commonality_mustInput,
					id : 'taskIntervalMerge',
					maxLength : 200,
					maxLengthText : '输入信息长度超过200个字符!'
				}]
		});
		
		//弹出框
		var winInt = new Ext.Window({
			title : '选择增强任务的处理方式',
			width : 320,
			region : 'center',
			resizable : false,
			closable : false,
			bodyStyle : 'padding:0px;',
			buttonAlign : 'center',
			modal : true,
			items : [selectForm],
			buttons : [
				new Ext.Button({
					text : commonality_affirm,
					handler : function() {
						if (chooseCombo.getValue() == 1) {
							if (taskType == 'GVI') {
								alert('GVI任务不能转移ATA20和进入维修大纲');
								return;
							}
							saveTask();
						} else if (chooseCombo.getValue() == (3)){
							if (taskType == 'GVI') {
								alert('GVI任务不能转移ATA20和进入维修大纲');
								return;
							}
							saveTask();
						} else if (chooseCombo.getValue() == (2)){
							if (taskType != 'GVI') {
								alert('非GVI任务不能合并到标准任务');
								return;
							}
							if((stdTaskCombo.getValue() == null) || (stdTaskCombo.getValue() == "")) {
								Ext.Msg.alert(commonality_caution, '请选择标准任务');
								return;
							}
							if (Ext.getCmp('taskIntervalMerge').getValue() == '' || Ext.getCmp('taskIntervalMerge').getValue() == null) {
								Ext.Msg.alert(commonality_caution, '请填写合并后的任务间隔');
								return;
							}							

							saveTask();		
						}else{//没有对"是否转移到ATA20章？"做出选择的状态下希望保存,提示操作有误
							Ext.Msg.alert(commonality_caution, '请选择处理方式');
						}
					}
				}), 
				new Ext.Button({
					text : commonality_close,
					handler : function() {
						winInt.destroy();
					}
				})
			]
		});
		winInt.show();

		function doEnable() {
			stdTaskCombo.enable();
			Ext.getCmp('taskInterval1').disable();
			Ext.getCmp('taskInterval2').disable();
			Ext.getCmp('taskIntervalMerge').enable();
		}

		function doDisable() {
			stdTaskCombo.disable();
			Ext.getCmp('taskInterval1').disable();
			Ext.getCmp('taskInterval2').disable();
			Ext.getCmp('taskIntervalMerge').disable();
		}

		if (transfer == 'transferToAta20') {
			chooseCombo.setValue(1);
			doDisable();
		} else if (transfer == 'pleaseSelect') {
			chooseCombo.setValue(null);
			doDisable();
		} else if (transfer == 'toMpd') {
			chooseCombo.setValue(3);
			doDisable();
		} else {
			chooseCombo.setValue(2);
			doEnable();
			stdTaskCombo.setValue(destTask);
			if((destTask != null) && (destTask != undefined)) {
				var index = store.findBy(function(record, id) {
					if (record.get('taskId') == destTask) {
						return true;
					}
				});
				var record = store.getAt(index);
				if(record != undefined){
					Ext.getCmp('taskInterval1').setValue(record.get('taskInterval'));
					Ext.getCmp('taskIntervalMerge').setValue(record.get('taskIntervalMerge'));
				}
			}
		}

		function saveTask() {
			Ext.MessageBox.confirm(commonality_affirm, commonality_affirmSaveMsg, function(id) {
				if (id == 'yes') {
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
						msg : commonality_waitMsg,
						removeMask : true// 完成后移除
					});
					waitMsg.show();
					Ext.Ajax.request({
						url : urlPrefix + 'saveZa6.do',
						waitMsg : commonality_waitMsg,
						params : {
							zaId : zaId,
							doSelect : chooseCombo.getValue(),
							taskId : currentRecord.get('taskId'),
							destTask : stdTaskCombo.getValue(),
							taskIntervalMerge : Ext.getCmp('taskIntervalMerge').getValue()
						},
						method : "POST",
						success : function(response) {
							waitMsg.hide();
							parent.refreshTreeNode();
							alert(commonality_messageSaveMsg);
							store.reload();
							winInt.destroy();	
							var all = Ext.util.JSON.decode(response.responseText);

							if(all.za7 != null && all.za7 != 0){
								step[7] = 1;
								document.getElementById("step7").className = "x-rc-form-table-td-sys-complete";

								if (all.za7 == 2){
									step[8] = 2;
									document.getElementById("step8").className = "x-rc-form-table-td-sys-maintain";
								}
								if (all.za7 == 1){
									step[8] = 1;
									document.getElementById("step8").className = "x-rc-form-table-td-sys-complete";
								}
							}
						},
						failure : function(response) {
							waitMsg.hide();
							Ext.Msg.alert(commonality_caution, commonality_cautionMsg);
						}
					});
				}
			});
		}
	}

	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			value = 8;
			goNext(value);
		} else {
			goNext(value);
		}		
	}
});