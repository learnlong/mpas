Ext.namespace('za7');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/za7/";	

	var currentRowIndex = -1;
    var currentRecord;
    
    var colM = new Ext.grid.ColumnModel([
        {
            header : "id",
            dataIndex : "taskId",
            hidden : true
        }, {
            header : '类别',
            dataIndex : "sourceSystem",
            width : 60,
            align : 'center',
            renderer : function(value, cellmeta, record){
		     	if (value == 'ZONE'){
		     		return '区域';
		     	} else if (value == 'LHIRF'){
		     		return 'L/Hirf';
		     	}else if(value == 'STRUCTURE'){
		     		return '结构';
		     	}else if (value == 'SYS'){
		     		return '系统';
		     	}
			}
        }, {
            header : '项目编号',
            dataIndex : "areaCode",
            width : 100,
            align : 'center'
        }, {
            header : '项目名称',
            dataIndex : "areaName",
            width : 120,
            align : 'center'
        }, {
            header : 'MSG-3任务号',
            dataIndex : "taskCode",
            width : 120,
            align : 'center'
        }, {
            header : '任务类型',
            dataIndex : "taskType",
            width : 60,
            align : 'center'
        }, {
            header : '任务间隔',
            dataIndex : "taskInterval",
            width : 170,
            align : 'center'
        }, {
            header : '接近方式',
            dataIndex : "reachWay",
            width : 120,
            align : 'center',
			renderer: changeBR       
        }, {
            header : '任务描述',
            dataIndex : "taskDesc",
            width : 120,
            align : 'center',
			renderer: changeBR
        }, {
            header : '转移原因',
            dataIndex : "whyTransfer",
            width : 120,
            align : 'center',
			renderer: changeBR
        }, {
            header : '是否接收?',
            dataIndex : "show",
            width : 140,
            align : 'center',
            renderer : function(value, cellmeta, record){
				if (value == '0'){
					return '否';
				} else if (value == '2'){
					return '请选择';
				} else if(value == '3'){
					return '合并任务已经不存在';
				} else if(value == '4'){
					return '待定';
				} else if(value != null && value != ""){
					return value;
				}
			}
        }, {
            header : '拒绝原因',
            dataIndex : "rejectResion",
            width : 130,
            align : 'center',
			renderer: changeBR
        }
    ]);

	var store = new Ext.data.Store({
        url : urlPrefix + "getZa7List.do",
        reader : new Ext.data.JsonReader( {
			root : 'tasks',
			fields : [ 
				{name:'taskId'},
				{name:'sourceSystem'},
				{name:'areaCode'},
				{name:'areaName'},
				{name:'taskCode'},
				{name:'taskType'},
				{name:'taskInterval'},
				{name:'reachWay'},
				{name:'taskDesc'},
				{name:'whyTransfer'}, 
				{name:'hasAccept'},             
				{name:'rejectResion'},
				{name:'destTask'},
				{name:'show'}
			]
		})
	});

	store.on('beforeload',function(store){
        store.baseParams['areaId'] = areaId;
    });
    store.load();
    
    store.on('load',function(){
		
    });

	//弹出框中的选择框的数据源
	var taskStore = new Ext.data.Store({
		url :  contextPath + '/area/za6/getStandardTaskList.do?zaId=' + zaId,
		reader : new Ext.data.JsonReader({
			root : 'taskStore',
			fields : [
				{name : 'taskId'}, 
				{name : 'taskCode'},
				{name : 'taskInterval'},
				{name : 'taskIntervalMerge'}
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
		title : returnPageTitle('候选转移任务合并表', 'za7'),
		header : true,
		autoWidth : true
    });
    
    grid.addListener("cellclick", function(grid, rowIndex, columnIndex, e){
        currentRowIndex = rowIndex;
        currentRecord = grid.getStore().getAt(rowIndex);

		if (columnIndex == 10 && analysisFlag){
			openWindow();
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

	function openWindow(){
		var hasAccept = currentRecord.get('hasAccept');
        var destTask = currentRecord.get('destTask');
        var taskCode = currentRecord.get('taskCode');
        var taskInterval = currentRecord.get('taskInterval');
        var rejectResion = currentRecord.get('rejectResion');

		// 是否下拉框
        var chooseCombo = new Ext.form.ComboBox({
            fieldLabel : '是否接收?',
            store : new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
				data : [['1', '是'], ['0', '否'], ['2', '待定']]
			}),
            valueField : "retrunValue",
            displayField : "displayText",
            mode : 'local',
            editable : false,
            triggerAction : 'all',
            width : 65,
            listeners : {
                select : function(combo){
                    if (combo.value == '1'){
                        stdTaskCombo.enable();
                        Ext.getCmp('taskIntervalMerge').enable();
                        Ext.getCmp('rejectResion').setValue('');
			            Ext.getCmp('rejectResion').disable();
                    } else if (combo.value == '0'){
                    	stdTaskCombo.clearValue();
						stdTaskCombo.disable();
			            Ext.getCmp('taskIntervalMerge').setValue('');
                        Ext.getCmp('taskIntervalMerge').disable();
                        Ext.getCmp('rejectResion').enable();
                    } else if (combo.value == '2'){
                    	stdTaskCombo.clearValue();
						stdTaskCombo.disable();
			            Ext.getCmp('taskIntervalMerge').setValue('');
						Ext.getCmp('taskIntervalMerge').disable();
			            Ext.getCmp('rejectResion').setValue('');
                        Ext.getCmp('rejectResion').disable();
                    }
                }
            } 
        });

		var stdTaskCombo = new Ext.form.ComboBox({   
            fieldLabel : '标准任务',
            id : 'stdTask',
            name : 'stdTask',
            store : taskStore,
            valueField : "taskId", 
            displayField : "taskCode",
            editable : false,
            triggerAction : 'all',   
            emptyText : '--请选择--',   
            width : 128,  
            mode : 'local', 
            listeners : {
                select : function(combo, record, index){
					Ext.getCmp('taskInterval1').setValue(record.get('taskInterval'));
                    Ext.getCmp('taskIntervalMerge').setValue(record.get('taskIntervalMerge'));
                }
            } 
        });

		var selectForm = new Ext.form.FormPanel({
            labelWidth : 140,
            labelAlign : "right",
            buttonAlign : 'center',
            frame : true,      
            items: [
                chooseCombo,
                stdTaskCombo,
                {
                	xtype : 'textfield', 
                	fieldLabel : '标准任务的任务间隔', 
                	id : 'taskInterval1', 
                	disabled : true,
					anchor:'95%'
                }, {
                	xtype : 'textfield', 
                	fieldLabel : taskCode + '任务间隔', 
                	id : 'taskInterval2', 
                	value : taskInterval, 
                	disabled : true,
					anchor:'95%'
                }, {
                	xtype : 'textfield', 
                	fieldLabel : '填写合并后任务间隔' + commonality_mustInput, 
                	id : 'taskIntervalMerge',
                	maxLength : 200,
					maxLengthText : '输入信息长度超过200个字符!',
					anchor : '95%'
                }, {
                	xtype : 'textarea', 
                	fieldLabel : '拒绝原因', 
                	id : 'rejectResion', 
                	value : rejectResion, 
                	width : 230,
                	maxLength : 4000,
					maxLengthText : '输入信息长度超过4000个字符!',
					anchor:'95%'
                }
            ]
        });

		var winInt = new Ext.Window({
            title : '选择合并任务',
            width : 400,
            region : 'center',
            resizable : false,
            closable : false,
            bodyStyle : 'padding:0px;',
            buttonAlign : 'center',
            pageX : 200, 
			pageY : 100, 
            modal : true,
            items : [selectForm],
            buttons : [
                new Ext.Button({
                    text : commonality_affirm,
                    handler : function(){
                        if (chooseCombo.getValue() == 1){
                            if ((stdTaskCombo.getValue() == '') || (stdTaskCombo.getValue() == null)){
                                Ext.Msg.alert(commonality_caution, '请选择标准任务');
                            } else if (Ext.getCmp('taskIntervalMerge').getValue() == '' || Ext.getCmp('taskIntervalMerge').getValue() == null){
                                Ext.Msg.alert(commonality_caution, '请填写合并后的任务间隔');
                            } else {
                                saveTask();
                            }
                        } else if (chooseCombo.getValue() == 0){
                            if (Ext.getCmp('rejectResion').getValue() == null || Ext.getCmp('rejectResion').getValue() == ''){
                                Ext.Msg.alert(commonality_caution, '请填写拒绝原因');
                            } else {
                                saveTask();
                            }
                        } else if (chooseCombo.getValue() == 2){
                            saveTask();
                        } else {
                            Ext.Msg.alert(commonality_caution, '请选择是否接受?');
                        }
                    }
                }),
                new Ext.Button({
                    text : commonality_close,
                    handler : function(){
                        winInt.destroy();
                    }
                })
            ]
        });            
        winInt.show();

		if (hasAccept == '0'){//第一个下拉选择框有效,选择"否",其他操作框按业务需求显示
            chooseCombo.setValue(hasAccept);
            stdTaskCombo.disable();
            Ext.getCmp('taskIntervalMerge').disable();
            Ext.getCmp('rejectResion').enable();
        } else if (hasAccept == '1'){//第一个下拉选择框有效,选择"是",其他操作框按业务需求显示
            chooseCombo.setValue(hasAccept);
            stdTaskCombo.enable();
            Ext.getCmp('taskIntervalMerge').enable();
            Ext.getCmp('rejectResion').disable();

			stdTaskCombo.setValue(destTask);	
			if((destTask != null) && (destTask != undefined)) {
				var index = taskStore.findBy(function(record, id) {
					if (record.get('taskId') == destTask) {
						return true;
					}
				});
				var record = taskStore.getAt(index);
				if(record != undefined){
					Ext.getCmp('taskInterval1').setValue(record.get('taskInterval'));					
                    Ext.getCmp('taskIntervalMerge').setValue(record.get('taskIntervalMerge'));
				}
			}
        } else if (hasAccept == '2'){//第一个下拉选择框有效,选择"待定",其他操作框按业务需求显示
            chooseCombo.setValue(hasAccept);
            stdTaskCombo.disable();
            Ext.getCmp('taskIntervalMerge').disable();
            Ext.getCmp('rejectResion').disable();
        }else{//初始进来时,除了第一个下拉选择框有效,其他操作框都为无效状态
        	stdTaskCombo.disable();
            Ext.getCmp('taskIntervalMerge').disable();
            Ext.getCmp('rejectResion').disable();
        }

		function saveTask(){
            Ext.MessageBox.confirm(commonality_affirm, commonality_affirmSaveMsg, function(id){
                if (id == 'yes'){
                	var waitMsg = new Ext.LoadMask(Ext.getBody(), {
	                    msg : commonality_waitMsg,
	                    removeMask : true// 完成后移除
	              	});		
					waitMsg.show();

                    Ext.Ajax.request( {
                        url : urlPrefix + 'saveZa7.do',
                        waitMsg : commonality_waitMsg,
                        params : {
							areaId : areaId,
                            zaId : zaId,
                            hasAccept : chooseCombo.getValue(),
                            taskId : currentRecord.get('taskId'),
                            destTask : stdTaskCombo.getValue(),
                            taskIntervalMerge : Ext.getCmp('taskIntervalMerge').getValue(),
                            rejectResion : Ext.getCmp('rejectResion').getValue()
                        },
                        method : "POST",
                        success : function(response) {
                        	waitMsg.hide();
                            alert(commonality_messageSaveMsg);
                            parent.refreshTreeNode();
                            
                            store.load();
							taskStore.load();
                            winInt.destroy();

							var all = Ext.util.JSON.decode(response.responseText);

							if(all.za7 != null){
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
                            alert(commonality_cautionMsg);
							waitMsg.hide();
                        }
                    });
                }
            });
        }
	}

	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			value = 9;
			goNext(value);
		} else {
			goNext(value);
		}		
	}
});