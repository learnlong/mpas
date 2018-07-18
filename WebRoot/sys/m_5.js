Ext.namespace('m5');

m5.app = function() {
	return {
		g_Url : '',

		init : function() {
   Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'qtip';
			var store = new Ext.data.Store({
						url : m5.app.loadM5,
						reader : new Ext.data.JsonReader({
									root : "m5",
									fields : [{
												name : 'taskId'
											}, {
												name : 'taskCode'
											}, {
												name : 'taskType'
											}, {
												name : 'ownArea'
											}, {
												name : 'taskDesc'
											}, {
												name : 'reachWay'
											}, {
												name : 'taskInterval'
											}, {
												name : 'effectiveness'
											}, {
												name : 'needTransfer'
											}, {
												name : 'hasAccept'
											}, {
												name : 'rejectReason'
											}, {
												name : 'isHeBing'//1：合并:任务
											}, {
												name : 'causeCode'
											}, {
												name : 'causeType'
											}, {
												name : 'merge'//1:被合并的任务
											}, {
												name : 'whyTransfer'
											}

									]
								}),
						sortInfo : {
							field : 'merge',
							direction : 'ASC'
						}
					});
			chooseAreaCombo1 = function() {
				var chooseAreaCombo = new Ext.form.ComboBox({
							xtype : 'combo',
							name : 'chooseArea',
						//	id : 'chooseArea',
							store : new Ext.data.SimpleStore({
										fields : ["retrunValue", "displayText"],
										data : [['1', commonality_shi],
												['0', commonality_fou]]
									}),
							valueField : "retrunValue",
							displayField : "displayText",
							typeAhead : true,
							mode : 'local',
							triggerAction : 'all',
							width : 50,
							editable : false,
							selectOnFocus : true
						});
				return chooseAreaCombo;
			}
		chooseAreaCombo = chooseAreaCombo1();
			var typeCombo = new Ext.form.ComboBox({
						xtype : 'combo',
					name : 'chooseType',
					id : 'chooseType',
					minListWidth : 50,
					store : new Ext.data.SimpleStore({
								fields : ["retrunValue", "displayText"],
								data : [[baoyang, baoyang],[jiancha,jiancha], [jiankong, jiankong], [jiance,jiance],
										[chaixiu, chaixiu], [baofei, baofei], [zonghe, zonghe]]
							}),
					valueField : "retrunValue",
					displayField : "displayText",
					mode : 'local',
					editable : false,
					triggerAction : 'all',
					width : 50
				});
			var colM = new Ext.grid.ColumnModel([
					{
						header : 'MSG-3任务号' + commonality_mustInput,
						dataIndex : "taskCode",
						width : 90,
						editor : new Ext.form.TextField({
									regex : /[\A-Za-z0-9\-]+$/,
									regexText :  '编号格式不正确，只能输入字母，数字，横杠',
									maxLength : 50,
									maxLengthText : commonality_MaxLengthText
								})
					}, {
						header : '任务说明',
						dataIndex : "taskDesc",
						width : 100,
						renderer : changeBR,
						editor : new Ext.form.TextArea({})
					}, {
						header : '接近方式',
						dataIndex : "reachWay",
						width : 100,
						renderer : changeBR,
						editor : new Ext.form.TextArea({})
					}, {
						header : "F-FF-FE-FC",
						dataIndex : "causeCode",
						width : 65,
						renderer : changeBR
					}, {
						header : '故障影响类别',
						dataIndex : "causeType",
						width : 85,
						renderer : changeBR
					}, {
						header : '任务类型'+ commonality_mustInput,
						dataIndex : "taskType",
						width : 70,
						editor : typeCombo,
						renderer : function(value, cellmeta, record) {
							var index = typeCombo.store.find(Ext
											.getCmp('chooseType').valueField,
									value);
							var record = typeCombo.store.getAt(index);
							var returnvalue = "";
							if (record) {
								returnvalue = record.data.displayText;
							}
							return returnvalue;
						}
					}, {
						header : '间隔',
						dataIndex : "taskInterval",
						width : 100,
						maxLength :200,
						editor : new Ext.form.TextArea({
							        maxLength :200,
									allowBlank : false
								})
					}, {
						header :  '区域',
						dataIndex : "ownArea",
						width : 60,
						editor : new Ext.form.TextField({
									maxLength : 500,
									maxLengthText : commonality_MaxLengthText
								})
					}, {
						header :  '适用性',
						dataIndex : "effectiveness",
						width : 70,
						editor : new Ext.form.TextField({
									maxLength : 200,
									maxLengthText : commonality_MaxLengthText
								})
					}, {
						header : '是否转到区域' + commonality_mustInput,
						dataIndex : "needTransfer",
						width : 90,
						editor : chooseAreaCombo1(),
						renderer : function(value, cellmeta, record) {
							var index = chooseAreaCombo.store.find(chooseAreaCombo.valueField,
									value);
							var record = chooseAreaCombo.store.getAt(index);
							var returnvalue = "";
							if (record) {
								returnvalue = record.data.displayText;
							}
							return returnvalue;
						}
					},{
						header : "协调单",
						dataIndex : "taskId",
						renderer : function(value, cellmeta, record) {
							var returnvalue = "";
							if (value!=""&&value!=null){
								returnvalue = "<a href='javascript:void(0)' title='新增协调单'>"
								+ "<img src='"+ contextPath+ "/images/toAuditBtn.gif' onclick='tempRecord()'/></a>"
								+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' title='协调单详情'>"
								+ "<img src='"+ contextPath+ "/images/toCheckBtn.gif'"
								+"onclick='getRecord()'/></a>";
							}
							return returnvalue;
						}
					}, {
						header : '转移原因',
						dataIndex : "whyTransfer",
						width : 100,
						editor : new Ext.form.TextArea({}),
						renderer :changeBR
					}, {
						header : '区域是否接收',
						dataIndex : "hasAccept",
						width : 90,
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
						header : '退回原因',
						dataIndex : "rejectReason",
						width : 100
					}, {
						header : '是否合并任务',
						dataIndex : "isHeBing",
						width : 90,
						editor : chooseAreaCombo1(),
						renderer : function(value, cellmeta, record) {
							var index = chooseAreaCombo.store.find(chooseAreaCombo.valueField,
									value);
							var record = chooseAreaCombo.store.getAt(index);
							var returnvalue = "";
							if (record) {
								returnvalue = record.data.displayText;
							}
							return returnvalue;
						}
					}, {
						header : '工作间隔确定',
						dataIndex : "merge",
						hidden : true
					}]);

			var grid = new Ext.grid.EditorGridPanel({
						cm : colM,
						sm : new Ext.grid.RowSelectionModel({
									singleSelect : true
								}),
						region : 'center',
						store : store,
						clicksToEdit : 2,
						stripeRows : true,
						tbar : [new Ext.Button({
											text : '添加合并任务',
											iconCls : 'icon_gif_add',
											disabled : !flag,
											handler : addRow
										}), "-", new Ext.Button({
											text : '删除合并任务',
											iconCls : 'icon_gif_delete',
											disabled : !flag,
											handler : delRow
										})]
					});

			var headerStepForm = new Ext.form.Label({
						applyTo : 'headerStepDiv'
					});

			var view = new Ext.Viewport({
						layout : 'border',
						items : [{
									region : 'north',
									height : 60,
									frame : true,
									items : [headerStepForm]
								}, {
									title : returnPageTitle('维修工作汇总表', 'm_5'),
									region : 'center',
									layout : 'fit',
									items : [grid]
								}]
					});

			grid.addListener("beforeedit", beforeedit);
			grid.addListener("afteredit", afteredit);
			grid.addListener("rowclick", rowclick);
				
			function rowclick(grid, rowIndex,e) {
				nowRecord = grid.getStore().getAt(rowIndex);
				rId = nowRecord.get("taskId");
			}			
			function beforeedit(val) {
				var merge = val.record.get('merge');
				var temp = val.record.get('isHeBing');
				var needTransfer = val.record.get('needTransfer');
				var field = val.field;
				if ((field == 'whyTransfer')&& needTransfer != '1') {
					val.cancel = true;
				}
				if (merge != "1"
						&& (field == 'taskInterval' ||field == 'effectiveness'
						|| field == 'ownArea' || field == 'taskType'
						|| field == 'taskCode' || field == 'causeCode'||
						field == 'causeType'|| field =='taskDesc'|| field == 'reachWay')) {
					val.cancel = true;
					return;
				}
				if (merge == "1" && field == 'isHeBing') {
					val.cancel = true;
					return;
				}
				//				
			}
			function afteredit(val) {
				var nowRecord = val.record;
				if (val.field == 'isHeBing') {
					if (val.value == 1 && nowRecord.get("needTransfer") == 1) {
						nowRecord.set('isHeBing', val.originalValue);
						alert("任务已转到区域不能被合并!");
						return ;
					}
				} else if (val.field == 'needTransfer') {
					if (val.value == 1 && nowRecord.get("isHeBing") == 1) {
						nowRecord.set('needTransfer', val.originalValue);
						alert("任务已被合并不能转到区域!");
						return ;
					} else if (val.value == 1 && nowRecord.get("ownArea") == '') {
						nowRecord.set('needTransfer', val.originalValue);
						alert("请先填写区域，再转移任务！");
						return ;
					}

				}
				if (val.field == 'ownArea') {
					if(nowRecord.get('needTransfer')==1&&(val.value==''||val.value==null)){
						alert('是否是否转到区域选择 "是" 时,必须填写转入区域 !');
						nowRecord.set('ownArea',val.originalValue);
						return;
					}
					// 检查区域编号是否重复
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
				if (val.field == 'taskCode') {
					Ext.Ajax.request({
								url : contextPath + "/sys/m5/verifyTaskCode.do",
								params : {
									taskCode : val.value,
									taskId : nowRecord.get('taskId')
								},
								success : function(response) {
									if (response.responseText) {
										alert('该任务编号已被使用，请重新填写！');
										nowRecord.set('taskCode',
												val.originalValue);
									}
								}
							})
				}
			}
			// 在指定位置添加空白行
			function addRow() {
				var ff = "";
				var type = "";
				var count = 0;
				for (var i = 0; i < store.getCount(); i++) {
					var record = store.getAt(i);
					if (record.get('merge') == 1) {
						alert('只能添加一条合并任务');
						return;
					}
					if (record.get('isHeBing') == 1) {
						ff = ff + record.get('causeCode');
						type = type + record.get('causeType');
						count++;
					}
				}
				if(count<=1){
					alert("当存在两条或者两条以上需要合并的任务时，才需要合并任务");
					return;
				}	
				var rec = grid.getStore().recordType;
				var p = new rec({
							taskId : '',
							taskCode : '',
							taskType : '',
							ownArea : '',
							taskDesc : '',
							taskInterval : '',
							effectiveness : mEff,
							reachWay : '',
							needTransfer : '0',
							hasAccept : '',
							rejectReason : '',
							isHeBing : '',
							causeCode : ff,
							causeType : type,
							merge : '1',
							whyTransfer : ''
						});
				store.add(p);
				store.modified.push(p);
			}

			function delRow() {

				var record = grid.getSelectionModel().getSelected();
				if (record == null) {
					alert(commonality_alertDel);
					return;
				}

				if (record.get('taskId') == '') {
					store.remove(record);
					store.modified.remove(record);
				} else if (record.get('merge') == 1) {
					Ext.MessageBox.show({
								title : commonality_affirm,
								msg : commonality_affirmDelMsg,
								buttons : Ext.Msg.YESNOCANCEL,
								fn : function(id) {
									if (id == 'cancel') {
										return;
									} else if (id == 'yes') {
										doDelete(record);
										store.modified = [];
									} else if (id == 'no') {
										return;
									}
								}
							});
				} else {
					alert('该任务不是合并任务，不能删除');
				}
			}

			function doDelete(record) {
				waitMsg.show();
				Ext.Ajax.request({
							url : m5.app.deleteM5HeBing,
							params : {
								deleteId : record.get('taskId')
							},
							method : "POST",
							success : function(form, action) {
								waitMsg.hide();
								parent.refreshTreeNode();
								successNext(7);
								alert(commonality_messageDelMsg);
							},
							failure : function(form, action) {
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});
			}
			function save(value, json) {
				waitMsg.show();
				Ext.Ajax.request({
							url : m5.app.saveM5,
							params : {
								jsonData : Ext.util.JSON.encode(json),
								msiId : msiId
							},
							method : "POST",
							success : function(form, action) {
								waitMsg.hide();

								var msg = Ext.util.JSON
										.decode(form.responseText);
								parent.refreshTreeNode();
								if (msg.success == false) {
									alert('任务编号已被使用，请重新填写！');
									return;
								}
								alert(commonality_messageSaveMsg + '分析已完成！');
								if (value == 8) {
									store.modified = [];
									store.reload();
									return false;
								}

								goNext(value);

							},
							failure : function(form, action) {
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});

			}
			store.on("beforeload", function() {
						store.baseParams = {
							msiId : msiId
						};
					});
			store.load();

			// 点击下一步执行的方法
			nextOrSave = function(value) {
				if (value == null) {
					value = 8;
				}
				var json = [];

				if (store.modified.length > 0) {
					if (isMaintain != '1') {
						// alert("你没有修改权限，无权修改！");
						goNext(value);
						waitMsg.hide();
						return false;
					}
					Ext.MessageBox.show({
						title : commonality_affirm,
						msg : commonality_affirmSaveMsg,
						buttons : Ext.Msg.YESNOCANCEL,
						fn : function(yesNo) {
							if (yesNo == 'cancel') {
								return;
							} else if (yesNo == 'yes') {
								var flage = true;
								var isHeBingCount = 0;
								var isMergeCount = 0;
								for(var i=0;i<store.getCount();i++){
									var record = store.getAt(i);
									if (record.get('merge') == 1) {
										isMergeCount++;
									}
									if (record.get('isHeBing') == 1) {
										isHeBingCount++;
									}
								}	
								if(isMergeCount==0&&isHeBingCount>0){
									alert('不存在合并任务，不能进行任务合并');	
									return;
								}
								Ext.each(store.modified, function(item) {
											if (item.get("taskCode") == ""
													|| item.get("taskCode") == null) {
												alert('MSG-3任务号不能为空');
												flage = false;
												return false;
											}
											if(item.get("taskType")==null||item.get("taskType")==""){
												alert('任务类型不能为空');
												flage = false;
												return false;
											}
											json.push(item.data);
										});
								if (flage) {
									save(value, json);
								}
							} else if (yesNo == 'no') {
								if (value == 8) {
									alert('分析已完成！');
									return false;
								}
								goNext(value);
							}
						}
					});

				} else {
					for (var i = 0; i < store.getCount(); i++) {
						if (store.getAt(i).get('needTransfer') == null) {
							alert('分析已完成！');
							return false;
						}
					}
					if (value == 8) {
						parent.refreshTreeNode();
						alert('分析已完成！');
						return false;
					}
					goNext(value);

				}

			}
		}
	};
}();