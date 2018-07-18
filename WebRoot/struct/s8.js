var selectValue = '';
var oldValue = null;
var thisId = null;
var isFlag = false;
var delId = new Array();
Ext.override(Ext.grid.RowSelectionModel, {
    onEditorKey : function(field, e) {
        var k = e.getKey(), newCell, g = this.grid,l = g.lastEdit, ed = g.activeEditor;
        var shift = e.shiftKey;
        if(ed){
        }else{
            ed=l;
        }
        
        if (k == e.ENTER) {
            e.stopEvent();
            if(ed){
                //ed.completeEdit();
            }
            if (shift) {
                newCell = g.walkCells(ed.row, ed.col - 1, -1,
                        this.acceptsNav, this);
            } else {
                newCell = g.walkCells(ed.row, ed.col + 1, 1,
                        this.acceptsNav, this);
            }
        } else if (k == e.TAB) {
            e.stopEvent();
            ed.completeEdit();
            if (this.moveEditorOnEnter !== false) {
                if (shift) {
                    newCell = g.walkCells(ed.row - 1, ed.col, -1,
                            this.acceptsNav, this);
                } else {
                    newCell = g.walkCells(ed.row, ed.col + 1, 1,
                        this.acceptsNav, this);
                }
            }
        } 
        if (newCell) {
            g.startEditing(newCell[0], newCell[1]);
        }
    }
});
Ext.onReady(function() {
	var newGrid = null;
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();
	var oldGrid = null;
	var tempStore = new Ext.data.Store();// 用来存放原始的store
	var comboStore = new Ext.data.Store({
				url : contextPath + "/struct/s8/getComboMrb.do?ssiId=" + ssiId,
				reader : new Ext.data.JsonReader({
							root : 'mrbs',
							fields : [{
										name : 'mrbCode'
									}, {
										name : 'mrbId'
									}, {
										name : 'taskType'
									}]
						})
			});

	var modelSelect = new Ext.form.ComboBox({
				width : 120,
				name : 'mrbTaskCodeComb',
				id : 'mrbTaskCodeComb',
				store : comboStore,
				mode : 'local',
				triggerAction : 'all',
				valueField : 'mrbId',
				vtype : 'Cn',
				displayField : 'mrbCode',
				editable : true,
				selectOnFocus : true,
				tpl : '<tpl for=".">'
						+ '<div class="x-combo-list-item" style="height:12px;">'
						+ '{mrbCode}' + '</div>' + '</tpl>'
			});

	comboStore.on('load', function(e) {
				Ext.each(comboStore.data.items, function(item) {
							tempStore.add(item);// 原始的值放在tempstore
						});
			});


	modelSelect.on('specialkey', function(f, e) {
				if (e.getKey() == e.ENTER) {
					var record = grid.getSelectionModel().getSelected();
					if (f.lastQuery) {
						f.setValue(f.lastQuery)
					} else {
						f.setValue(f.lastQuery)
						delRecord(oldValue);
					}

				}

			});

	modelSelect.on('select', function(f, e) {
		isFlag = true;
		var record = grid.getSelectionModel().getSelected();
		selectValue = e.get('mrbCode');
		record.set("mrbTaskCode", e.get('mrbCode'));
		});

	modelSelect.on('beforequery', function(e) {
				record = grid.getSelectionModel().getSelected();
				modelSelect.getStore().removeAll();
				Ext.each(tempStore.data.items, function(item) {
							if (item.get("taskType") == record.get("tempType")) {
								modelSelect.getStore().addSorted(item);
							}
						});
				if (modelSelect.getStore().getCount() > 0) {
					var rec = modelSelect.getStore().recordType;
					var p = new rec({
								taskType : '',
								mrbId : '',
								mrbCode : ''
							});

					modelSelect.getStore().insert(0, p);
				}
			});
	comboStore.load();

	modelSelect.on('blur', function(newValue) {

			});

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : contextPath
									+ "/struct/s8/getS8Records.do?ssiId="
									+ ssiId
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totleCount",
							root : "msg3Task",
							fields : ["taskId", "ownArea", "taskCode",
										"taskDesc", "occures",
										"reachWay", "mrbTaskCode",
										"tempType", "temp","eff"]
						})
			});

	var colM = new Ext.grid.ColumnModel([
			{
				header : "ID",
				dataIndex : "taskId",
				hidden : true
			}, {
				header : 'MSG-3任务号' + commonality_mustInput,
				width : 150,
				dataIndex : "taskCode",
				renderer : function(value, meta, record) {
					meta.attr = 'style="white-space:normal;word-wrap:break-word;"';
					value = value.replace(/\n/g, '<br/>');
					return value;
				},
				editor : new Ext.form.TextField({
							allowBlank : false

						})
			}, {
				header : '任务描述',
				width : 200,
				dataIndex : "taskDesc",
				renderer : changeBR,
				editor : new Ext.form.TextArea({
					grow : true
				})
			},{
				header : '所属区域' + commonality_mustInput,
				dataIndex : "ownArea",
				editor : new Ext.form.TextField({
							allowBlank : false,
							maxLength : 250
						})
			}, {
				header : '检查间隔',
				width : 120,
				dataIndex : "occures",
				renderer : function(value, meta, record) {
					meta.attr = 'style="white-space:normal;word-wrap:break-word;"';
					return value;
				},
				editor : new Ext.form.TextArea({
					grow:true,
							maxLength : 200
						})
			}, {
				header : '接近方式',
				width : 200,
				dataIndex : "reachWay",
				renderer : changeBR,
				editor : new Ext.form.TextArea({
					grow : true
				})
			}, {
				header : '任务类型' + commonality_mustInput,
				width : 80,
				dataIndex : "tempType",
				editor : new Ext.form.ComboBox({
							fieldLabel : '任务类型',
							name : 'msg3Task.taskType',
							id : 'taskType',
							store : new Ext.data.SimpleStore({
										fields : ['countryCode', 'countryName'],
										data : [[GVI, GVI], [DET, DET],
												[SDI, SDI]]
									}),
							valueField : 'countryCode',
							displayField : 'countryName',
							lazyRender : true,
							triggerAction : 'all',
							mode : 'local',
							editable : false,
							blankText : '不能为空',
							emptyText : '请选择',
							listeners : {
								select : function(combo, record, index) {

								}

							}
						})
			}, {
				header :commonality_eff,
				dataIndex : "eff",
				editor : new Ext.form.TextField({
							maxLength : 250
						})
				 
			},{
				header : 'MRB任务号',
				width : 120,
				dataIndex : "mrbTaskCode",
				editor : modelSelect,
				renderer : function(value, meta, record) {
					var index = Ext.getCmp("mrbTaskCodeComb").store.find(Ext
									.getCmp('mrbTaskCodeComb').valueField,
							value);
					if (index = -1) {
						if (isFlag) {
							isFlag = false;
							return selectValue;
						} else {
							isFlag = false;
							return value;
						}
					}
					var record = Ext.getCmp("mrbTaskCodeComb").store
							.getAt(index);
					var returnvalue = "";
					if (record) {
						returnvalue = record.data.mrbCode;
					} else {
						returnvalue = value;
					}
					isFlag = false;
					return returnvalue;
				}
			}, {
				header : '任务产生方式',
				width : 100,
				dataIndex : "temp",
				renderer : function(value, meta, record) {
					if (value == 1 || value == 0 || value == null
							|| value == 'null') {
						return 'MSG-3分析任务';
					}
					if (value == 2) {
						return '附加任务';
					}
				}
			}]);

	var grid = new Ext.grid.EditorGridPanel({
		title : returnPageTitle('结构维修任务汇总和合并成MRB任务', 's8'),
		region : 'center',
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		store : store,
		clicksToEdit : 2,
		stripeRows : true,
		tbar : [new Ext.Button({
							text : commonality_del,
							disabled : isMaintain == 0 ? true : false,
							iconCls : 'icon_gif_delete',
							handler : del
						}), '-', new Ext.Button({
							text : commonality_add,
							disabled : isMaintain == 0 ? true : false,
							iconCls : 'icon_gif_add',
							handler : add
						})]
		});
	grid.addListener("beforeedit", beforeedit);
	function afteredit(val) {
		var count = 0;
		if (val.field == 'ownArea') {
			var nowRecord = val.record;
			var zone = val.value.split(",");
			Ext.Ajax.request({
						url : contextPath + "/struct/verifyArea.do?",
						params : {
							verifyStr : val.value
						},
						success : function(response) {
							if(response.responseText){
								var msg= Ext.util.JSON.decode(response.responseText);
								if(msg.exists){
									alert("区域:"+ msg.exists +"不能重复,请重新修改")
									nowRecord.set('ownArea',val.originalValue);
									return ;
								}
								if(msg.unExists){
									alert("区域:"+ msg.unExists+"不存在,请重新修改")
									nowRecord.set('ownArea',val.originalValue);
									return ;
								}
								if (msg.success) {
									alert("不能转入区域:"+ msg.success+"，该区域已被冻结或者已经审批完成，请重新修改")
									nowRecord.set('ownArea',val.originalValue);
									return ;
								}
							}
						}
					})
		}
		if (val.field == 'mrbTaskCode') {
			isNotNull = true;
			if (val.value == '') {
				delRecord(oldValue);
				isNotNull = false;
			}
			var index = Ext.getCmp("mrbTaskCodeComb").store.find(Ext
							.getCmp('mrbTaskCodeComb').valueField, val.value);
			if (index != -1) {
				var record = Ext.getCmp("mrbTaskCodeComb").store.getAt(index);
				val.value = record.data.mrbCode;
				val.record.set("mrbTaskCode", val.value);
			}

			if (val.value == '' || val.value == null) {
				return false;
			}

			if (!/[u4e00-u9fa5]/.test(val.value)) {
				alert("不可输入中文");
				return false;
			}

			op(val.value, val);
		}
		if (val.field == 'taskCode') {
			var nowRecord = val.record;
			var items = grid.getStore().data.items;
			for (var i = 0; i < items.length; i++) {
				if (items[i].get('taskCode') == val.value) {
					count++;
				}
				if (count == 2) {
					alert('任务编号已存在');
					nowRecord.set('taskCode', val.originalValue);
					return false;
				}
			}
			Ext.Ajax.request({
						url : contextPath + "/struct/s6/verifyTaskCode.do?",
						async : false,
						params : {
							verifyStr : val.value
						},
						success : function(response) {
							if (response.responseText) {
								alert('任务编号' + ":" + response.responseText
										+ '在其他系统已存在 !');// /任务编号在其他系统已存在!
								val.record.set('taskCode', val.originalValue);
								return null;
							}
						}
					})
		}

	}
	grid.addListener("afteredit", afteredit);
	function beforeedit(val) {
		if (val.field == 'mrbTaskCode') {
			val.record;
			oldValue = val.value;
			thisId = val.record.get('taskId');
		}
		if ((val.record.get('temp') ==1)&&(val.field == 'taskDesc'|| val.field == 'reachWay' )) {
			val.cancel = true;
			return;

		}
		if (isMaintain == 0) {
			val.cancel = true;
		}
		if (val.record.get('temp') != 2 && val.field != 'mrbTaskCode') {
			val.cancel = true;
			return;
		}
		if (val.record.get('temp') == 2 && val.field == 'mrbTaskCode') {
			if (!val.record.get('tempType')) {
				alert('请先维护好任务类型');
				val.cancel = true;
				return;
			}
		}
	}

	function add() {
		var rec = grid.getStore().recordType;
		var p = new rec({
					taskId : '',
					taskCode : '',
					taskDesc : '',
					occures : '',
					reachWay : '',
					eff:ssiEff,
					ownArea : '',
					tempType : '',
					mrbTaskCode : '',
					temp : 2
				});

		store.add(p);
	}

	var typeCom = new Ext.form.ComboBox({
				fieldLabel : '任务类型',
				name : 'msg3Task.taskType',
				id : 'taskType',
				store : new Ext.data.SimpleStore({
							fields : ['countryCode', 'countryName'],
							data : [[GVI, GVI], [DET, DET],
									[SDI, SDI]]
						}),
				valueField : 'countryCode',
				displayField : 'countryName',
				lazyRender : true,
				triggerAction : 'all',
				mode : 'local',
				allowBlank : false,
				blankText : '不能为空',
				emptyText : '请选择'
			});

	var colM1 = new Ext.grid.ColumnModel([{
				header : "ID",
				dataIndex : "id",
				hidden : true
			}, {
				header : "zone",
				dataIndex : "zone",
				hidden : true
			}, {
				header : '任务编号',
				dataIndex : "taskMrbCode"
			}, {
				header : '任务类型',
				width : 200,
				dataIndex : "taskType",
				editor : typeCom
			}, {
				header : '检查间隔',
				width : 120,
				dataIndex : "occures",
				editor : new Ext.form.TextField({
							allowBlank : false,
							maxLength : 200
						})
			}, {
				header : commonality_eff,
				width : 120,
				dataIndex : "eff",
				editor : new Ext.form.TextField({
							maxLength : 250
						})
			}, {
				header : '接近方式',
				width : 200,
				dataIndex : "reachWay",
				renderer : changeBR,
				editor : new Ext.form.TextArea({grow : true})
			}, {
				header : '任务描述',
				width : 200,
				dataIndex : "taskDesc",
				renderer : changeBR,
				editor : new Ext.form.TextArea({grow : true})
			}]);

	var mrbStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : contextPath
									+ "/struct/s8/getMrbRecords.do?ssiId="
									+ ssiId
						}),
				reader : new Ext.data.JsonReader({
							root : "mrbTask",
							fields : [{
										name : 'id'
									}, {
										name : 'zone'
									}, {
										name : 'taskMrbCode'
									}, {
										name : 'taskType'
									}, {
										name : 'occures'
									}, {
										name : 'reachWay'
									}, {
										name : 'taskDesc'
									},{
										name:'eff'
									}
							]
						})
			});

	mrbStore.load();


	var mrbGrid = new Ext.grid.EditorGridPanel({
				title : 'MRB任务列表',
				cm : colM1,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				store : mrbStore,
				clicksToEdit : 2,
				stripeRows : true
			});

	var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
	var h=window.innerHeight
				|| document.documentElement.clientHeight
				|| document.body.clientHeight;
	var viewport = new Ext.Viewport({
				id : 'viewportId',
				layout : 'border',
				items : [{
							region : 'north',
							height : 60,
							frame : true,
							items : [headerStepForm]
						}, {
							layout : 'fit',
							region : 'center',
							items : [grid]
						}, {
							layout : 'fit',
							height : h/4,
							region : 'south',
							split : true,
							items : [mrbGrid]
						}]
			});
	var topRecord = Ext.data.Record.create([{
				name : 'mrbCode',
				type : 'string'
			}, {
				name : 'mrbId',
				type : 'string'
			}, {
				name : 'taskType',
				type : 'string'
			}]);

	// MRB列表增加空白行
	function addMrbRow(taskCode, taskTemp, zone,seff,occures) {
		var rec = mrbGrid.getStore().recordType;
		var p = new rec({
					id : '',
					zone : zone,
					taskMrbCode : '',
					taskType : taskTemp,
					occures : occures,
					reachWay : '',
					taskDesc : '',
					eff:seff
				});
		mrbStore.add(p);
		mrbStore.getAt(mrbStore.getCount() - 1).set('taskMrbCode', taskCode);
		var tempstore = new topRecord({
					taskType : taskTemp,
					mrbId : '',
					mrbCode : taskCode

				});
		tempStore.add(tempstore);
	}

	// MRB号框失去焦点时进行的操作
	function op(mrbCode, e) {
		var nowRecord = e.record;
		var mrbRecord = mrbGrid.getStore();
		var taskTemp = '';
		var zone = '';
		if (nowRecord != null) {
			zone = nowRecord.get('ownArea');
			taskTemp = nowRecord.get('tempType');
		}
		var idArray = new Array();
		var j = 0;
		for (var i = 0; i < mrbRecord.data.length; i++) {
			if (mrbRecord.data.items[i].get("taskMrbCode") == mrbCode
					&& mrbRecord.data.items[i].get("taskType") == taskTemp) {
				if (isNotNull) {
					delRecord(oldValue);
				}
				return null;
			}
		}
		for (var i = 0; i < mrbRecord.data.length; i++) {
			if (mrbRecord.data.items[i].get("taskMrbCode") == mrbCode
					&& mrbRecord.data.items[i].get("taskType") != taskTemp) {
				alert('不是同种类型的任务不能合并');
				nowRecord.set("mrbTaskCode", e.originalValue);
				return null;
			}
		}
		if (confirm('是否创建MRB任务' + mrbCode)) {
			verifyMrbCode(mrbCode, taskTemp, zone, e);
		} else {
			nowRecord.set("mrbTaskCode", e.originalValue);
		}

	}

	function verifyMrbCode(mrbCode, taskTemp, zone, e) {
		record = grid.getSelectionModel().getSelected();
		Ext.Ajax.request({
			url : contextPath + "/struct/s8/verifyMrbCode.do?ssiId=" + ssiId,
			params : {
				mrbCode : mrbCode
			},
			method : "POST",
			success : function(response) {
				var text = Ext.decode(response.responseText);
				if (text.success) {
					addMrbRow(mrbCode, taskTemp, zone,e.record.get('eff'),e.record.get('occures'));
					if (oldValue) {
						delRecord(oldValue);
						isNotNull = false;
					}
				} else {
					alert('此任务编号可能在其他系统已存在,请修改!');
					record.set("mrbTaskCode", e.originalValue);
				}
			}
			});
	}
	// 检测必填项是否都填写
	var checkBlank = function(modified/* 所有编辑过的和新增加的Record */, cm) {
		var result = true;
		Ext.each(modified, function(record) {
					var keys = record.fields.keys;// 获取Record的所有名称
					Ext.each(keys, function(name) {
								// 根据名称获取相应的值
								var value = record.data[name];
								// 找出指定名称所在的列索引
								var colIndex = cm.findColumnIndex(name);
								if (!name == "taskDesc"&& !name == "reachWay") {

									// 根据行列索引找出组件编辑器
									if (cm.getCellEditor(colIndex)) {
										if (Ext.isEmpty(value) && value != null) {
											alert('请完整维护数据');
											result = false;
											return result;
										}
										var editor = cm.getCellEditor(colIndex).field;
										// 验证是否合法
										var r = editor.validateValue(value);
										if (!r) {
											alert('对不起，请完整输入信息');
											result = false;
											return result;
										}
									}
								}
							});
				});
		return result;
	}
	// 创建GRIDPANEL的JSON
	function createGridJSON() {
		var json = [];
		for (var i = 0; i < store.getCount(); i++) {
			json.push(store.getAt(i).data);
		}
		return Ext.util.JSON.encode(json);
	}
	function test() {
		var sItems = store.modified;
		var flag = false;
		for (var i = 0; i < sItems.length; i++) {
			if (sItems[i].get('ownArea') == ''
					|| sItems[i].get('tempType') == '') {
				flag = true;
			}
		}

		return flag
	}
	nextOrSave = function updateDataDetail(number) {
		if (number) {
			if (step[number] == 0) {
				alert('请先完成之前的步骤！');
				return false;
			}
		}
		if (!checkBlank(mrbStore.modified, colM1)
				|| !checkBlank(store.modified, colM)) {
			return;
		}
		if (test()) {
			alert('请完整维护数据');
			return null
		}
		newGrid = createGridJSON();
		if (newGrid == oldGrid && mrbStore.modified == '') {
			if (number) {
				goNext(number);
				return null;
			}
			alert('分析已完成!');
		} else {
			Ext.MessageBox.show({
						title : commonality_affirm,
						msg : commonality_affirmSaveMsg,
						buttons : Ext.Msg.YESNOCANCEL,
						fn : function(id) {
							if (id == 'cancel') {
								return;
							} else if (id == 'yes') {
								save(number);
								return null;
							} else if (id == 'no') {
								if (number) {
									goNext(number);
									return null;
								}
								alert('分析已完成!');
								return null;
							}
						}
					});
		}
	}

	function save(number) {
		var mrbJsonList = [];
		var jsonData = [];
		Ext.each(mrbStore.modified, function(item) {
					mrbJsonList.push(item.data);
				});
		Ext.each(store.modified, function(item) {
					jsonData.push(item.data);
				});
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});

		if(mrbJsonList.length>0||jsonData.length>0){
			waitMsg.show();
			Ext.Ajax.request({
						url : contextPath + "/struct/s8/saveS8Records.do",
						params : {
							mrbJsonData : Ext.util.JSON.encode(mrbJsonList),
							jsonData : Ext.util.JSON.encode(jsonData),
							ssiId : ssiId,
							delId : delId
						},
						method : "POST",
						success : function(response) {
							waitMsg.hide();
							alert('分析完成');
							parent.refreshTreeNode();
							if (number) {
								goNext(number);
								return null;
							}
							store.load();
							mrbStore.load();
							store.modified = [];
							mrbStore.modified = [];
							delId = [];
							tempStore = new Ext.data.Store();
							comboStore.load();
						},
						failure : function(response) {
							waitMsg.hide();
							alert(commonality_cautionMsg);
						}
					});
		}else{
			alert("没有需要更新或保存的数据");
			return;
		}
	}

	function del() {
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		var record = grid.getSelectionModel().getSelected();

		if (!record) {
			alert(commonality_alertDel);
			return;
		}
		if (!record.get("temp")) {
			alert('非自增任务无法删除');
			return;
		}
		Ext.Msg.confirm(commonality_caution, commonality_affirmDelMsg,
				function(btn) {
					if ('yes' == btn) {
						if (record.get('taskId') == '') {
							store.remove(record);
							store.modified.remove(record);
							if(record.get('mrbTaskCode')){
								delRecord(record.get('mrbTaskCode'));}
							return;
						}
						waitMsg.show();

						Ext.Ajax.request({
									url : contextPath
											+ "/struct/s8/delS8Reocrd.do",
									success : function() {
										waitMsg.hide();
										alert(commonality_messageDelMsg);
										location.replace(location.href);
//										store.load();
									},
									failure : function() {
										waitMsg.hide();
										alert(commonality_messageDelMsgFail);
									},
									params : {
										msg3Id : record.get('taskId')
									}
								});
					}
				});
	}

	store.on("beforeload", function() {
				store.baseParams = {
					id : id
				};
			});

	store.on('load', function(st) {
				oldGrid = createGridJSON();
			});

	store.load();

	// 删除一行的record
	function delRecord(oldValue) {
		if (oldValue) {
			var count1 = 0
			for (var cc = 0; cc < store.getCount(); cc++) {
				if (oldValue == store.getAt(cc).get("mrbTaskCode")) {
					count1++;
				}
			}
			if (count1 == 0) {
				for (var cc = 0; cc < mrbStore.getCount(); cc++) {
					if (oldValue == mrbStore.getAt(cc).get("taskMrbCode")) {
						delId.push(mrbStore.getAt(cc).get("id"));
						mrbStore.remove(mrbStore.getAt(cc));
						for (var c = 0; c < tempStore.getCount(); c++) {
							if (oldValue == tempStore.getAt(c).get('mrbCode')) {
								tempStore.remove(tempStore.getAt(c))
							}
						}
						for (var c = 0; c < modelSelect.getStore().getCount(); c++) {
							if (oldValue == modelSelect.getStore().getAt(c)
									.get('mrbCode')) {
								modelSelect.getStore().remove(modelSelect
										.getStore().getAt(c))
							}
						}
					}
				}
				for (var cc = 0; cc < mrbStore.modified.length; cc++) {
					if (oldValue == mrbStore.modified[cc].get('taskMrbCode')) {
						mrbStore.modified.remove(mrbStore.modified[cc]);
						mrbStore.modified;
					}
				}
			}
		}
	}

})
