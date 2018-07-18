Ext.namespace('m13');
m13.app = function() {
	return {
		init : function() {

			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';

			// 定义数据
			var store = new Ext.data.Store({
						url : m13.app.loadM13Url,
						reader : new Ext.data.JsonReader({
									root : "m13",
									fields : [{
												name : 'm13Id'
											}, {
												name : 'functionCode'
											}, {
												name : 'functionDesc'
											}, {
												name : 'm13fId'
											}, {
												name : 'failureCode'
											}, {
												name : 'failureDesc'
											}, {
												name : 'effectCode'
											}, {
												name : 'effectDesc'
											}, {
												name : 'm13cId'
											}, {
												name : 'causeCode'
											}, {
												name : 'causeDesc'
											}, {
												name : 'isRef'
											}, {
												name : 'level'
											},{
												name : 'msetfId'
											}]
								})
					});

			// 是否下拉框
			var chooseCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'choose',
						minListWidth : 50,
						id : 'choose',
						store : new Ext.data.SimpleStore({
									fields : ["retrunValue", "displayText"],
									data : [['1', commonality_shi],
											['0', commonality_fou]]
								}),
						valueField : "retrunValue",
						displayField : "displayText",
						mode : 'local',
						editable : false,
						triggerAction : 'all',
						width : 50
					});
			
			// 故障原因列表
			var caseCombo = new Ext.form.ComboBox({
						name : 'caseCom',
						id : 'caseCom',
						store : new Ext.data.Store({
									url : m13.app.searchCauseUrl,
									reader : new Ext.data.JsonReader({
												root : 'case',
												fields : [{
															name : 'caseValue'
														}, {
															name : 'caseDisplay'
														},{
															name : 'id'
														}]
											})
								}),
						valueField : "caseDisplay",
						displayField : "caseDisplay",
						mode : 'local',
						triggerAction : 'all',
						width : 50,
						editable : false
					});
			// 加载store
			store.on("beforeload", function() {
						store.baseParams = {
							msiId : msiId
						};
					});
			caseCombo.store.on("beforeload", function() {
				caseCombo.store.baseParams = {
					msiId : msiId
				};
			});
			
			/*caseCombo.addListener("change", function(field,newValue,oldValue ) {
				var index = caseCombo.store.findBy(function(record, id) {
					//if (record.get('caseDisplay') == newValue) {
						return true;
					//}
				});
				//index=store.indexOf(grid.getSelectionModel().getSelected());//获取选中行的索引
				var record = caseCombo.store.getAt(index);
				grid.getSelectionModel().getSelected().set('msetfId',record.data.id);
			});*/
			caseCombo.addListener("select", function(combo,record,opts) {
				grid.getSelectionModel().getSelected().set('msetfId',record.data.id);
			});
//			caseCombo.store.on("load", function() {
//						store.load();
//					});

			caseCombo.store.load();
			
			var baColor1 = "#b2dfee";
			var baColor2 = "#ffffa0";
			var m13_color = baColor1;
			// 加载store之后，不同功能显示不同颜色
			// load.defer(10000,store);
			setStoreClass = function() {
				for (var i = 0; i < store.getCount(); i++) {
					var record = store.getAt(i);
					if (record.get('functionCode') != null
							&& record.get('functionCode') != '') {
						if (m13_color == baColor1) {
							m13_color = baColor2;

						} else {
							m13_color = baColor1;
						}
					}
					grid.getView().getRow(i).style.backgroundColor = m13_color;
				}
			}
			store.on("load", function() {
				setTimeout("setStoreClass()", 1);
					// setStoreClass();
				});
			// 加载store之前
			store.on("beforeload", function() {
						store.baseParams = {
							msiId : msiId
						};
					});
			

			var colM = new Ext.grid.ColumnModel([{
						header : "功能ID",
						dataIndex : "m13Id",
						hidden : true
					}, {
						header : "编号" + commonality_mustInput,
						dataIndex : "functionCode",
						width : 60
					}, {
						header : "功能" + commonality_mustInput,
						dataIndex : "functionDesc",
						width : 200,
						editor : new Ext.form.TextArea({grow : true}),
						renderer : changeBR
					}, {
						header : "功能故障ID",
						dataIndex : "m13fId",
						hidden : true
					}, {
						header : "编号" + commonality_mustInput,
						dataIndex : "failureCode",
						width : 60
					}, {
						header : "功能故障" + commonality_mustInput,
						dataIndex : "failureDesc",
						width : 200,
						editor : new Ext.form.TextArea({grow : true}),
						renderer : changeBR
					
					}, {
						header : "编号" + commonality_mustInput,
						dataIndex : "effectCode",
						width : 60
					}, {
						header : "故障影响" + commonality_mustInput,
						dataIndex : "effectDesc",
						width : 200,
						editor : new Ext.form.TextArea({grow : true}),
						renderer : changeBR
					}, {
						header : "故障原因ID",
						dataIndex : "m13cId",
						hidden : true
					}, {
						header : "编号" + commonality_mustInput,
						dataIndex : "causeCode",
						width : 60
					}, {
						header : "故障原因" + commonality_mustInput,
						dataIndex : "causeDesc",
						width : 300,
						editor : caseCombo,
						renderer : function(value, cellmeta, gridRecord) {
							var index = caseCombo.store.findBy(function(record, id) {
										if (record.get('caseDisplay') == value) {
											return true;
										}
									});
							var record = caseCombo.store.getAt(index);
							var returnvalue = "";
							if (record) {
								returnvalue = record.data.caseDisplay;
								var msetfId=record.data.id;
							}
							return returnvalue;
						}
					}, {
						header : "是否参考" + commonality_mustInput,
						dataIndex : "isRef",
						width : 70,
						editor : chooseCombo,
						renderer : function(value, cellmeta, record) {
							var index = chooseCombo.store
									.find(Ext.getCmp('choose').valueField,
											value);
							var record = chooseCombo.store.getAt(index);
							var returnvalue = "";
							if (record) {
								returnvalue = record.data.displayText;
							}
							return returnvalue;
						}
					}, {
						header : "设置的故障原因ID",
						dataIndex : "msetfId",
						hidden : true
					}]);
			var grid = new Ext.grid.EditorGridPanel({
						cm : colM,
						sm : new Ext.grid.RowSelectionModel({
									singleSelect : true
								}),
						// region : 'center',
						store : store,
						clicksToEdit : 2,
						stripeRows : true,
						tbar : [new Ext.Button({
											text : "添加功能",
											iconCls : "icon_gif_add",
											disabled : !flag,
											handler : function() {
												addFunction();
											}
										}), new Ext.Button({
											text : "添加故障及影响",
											iconCls : "icon_gif_add",
											disabled : !flag,
											handler : function() {
												addFailure();
											}
										}), new Ext.Button({
											text : "添加原因",
											iconCls : "icon_gif_add",
											disabled : !flag,
											handler : function() {
												addCause();
											}
										}), new Ext.Button({
											text : commonality_del,
											iconCls : "icon_gif_delete",
											disabled : !flag,
											handler : function() {
												del();
											}
										}), new Ext.Button({
											text : "暂存",
											iconCls : "icon_gif_save",
											disabled : !flag,
											handler : function() {
												zancun();
											}
										})]
					});

			function refShow() {
				var record = grid.getSelectionModel().getSelected();
				if (record.get('isRef') == 1) {
					addChange(record.get('m13cId'), record);
				}
			}

			function selectTest(combo, record, index) {
				var record = grid.getSelectionModel().getSelected();
				if (record.get('m13cId') == '') {
					combo.setValue('');
					alert("请先保存后再做参考");
					return;
				}
				if (index == 0) {
					addChange(record.get('m13cId'), record);
				}
			}

			chooseCombo.addListener("focus", refShow);
			chooseCombo.addListener("select", selectTest);

			var headerStepForm = new Ext.form.Label({
						applyTo : 'headerStepDiv'
					});
			var viewport = new Ext.Viewport({
						id : 'viewportId',
						layout : 'border',
						items : [{
									region : 'north',
									height : 60,
									frame : true,
									items : [headerStepForm]
								}, {
									region : 'center',
									layout : 'fit',
									title : returnPageTitle("功能，故障，故障影响和原因分析", 'm_13'),
									items : [grid]
								}]
					});
			store.load();
			grid.addListener("beforeedit", beforeedit);
			function beforeedit(val) {
				var functionCode = val.record.get('functionCode');
				var field = val.field;
				if (functionCode == '' || functionCode == null) {
					if (field == 'functionDesc') {
						val.cancel = true;
					}
				}
				var failureCode = val.record.get('failureCode');
				if (failureCode == '' || failureCode == null) {
					if (field == 'failureDesc'|| field == 'effectDesc') {
						val.cancel = true;
					}
				}
			}
			
			function doChoose(value, cellmeta, record) {
				var index = chooseCombo.store.find(
						Ext.getCmp('choose').valueField, value);
				var record = chooseCombo.store.getAt(index);
				var returnvalue = "";
				if (record) {
					returnvalue = record.data.displayText;
				}
				return returnvalue;
			}
			// 添加功能行
			function addFunction() {
				var num = 0;
				for (var i = store.getCount() - 1; i >= 0; i--) {
					var re = store.getAt(i);
					if (re.get('functionCode') != ""
							&& re.get('functionCode') != null) {
						num = re.get('functionCode');
						break;
					}
				}

				if (num == 0) {
					num = 1;
				} else {
					num++;
				}
				if (num > 9) {
					alert("所添加的功能总数已超过编码要求");
					return;
				}
				addRow(store.getCount(), num, num.toString() + 'A', num
								.toString()
								+ 'A1', num.toString() + 'A1a');
			}
			// 添加故障及影响行
			function addFailure() {
				var record = grid.getSelectionModel().getSelected();
				if (record == null) {
					alert("请选择所要添加的位置");
					return;
				}
				var functionCode = record.get('functionCode');
				if (functionCode == null || functionCode == '') {
					alert("该记录下不能插入功能故障及影响");
					return;
				}
				var index = store.indexOf(record);
				var insertIndex = -1;
				var num = 2;
				for (var i = index + 1; i < store.getCount(); i++) {
					var re = store.getAt(i);
					if (re.get('functionCode') != null
							&& re.get('functionCode') != '') {
						insertIndex = store.indexOf(re);
						break;
					}
					if (re.get('failureCode') != null
							&& re.get('failureCode') != '') {
						num++;
					}
				}

				if (insertIndex == -1) {
					insertIndex = store.getCount();
				}

				if (num > 26) {
					alert("所添加的功能故障总数已超过编码要求");
					return;
				}
				var failure = functionCode.toString()
						+ String.fromCharCode(64 + num);
				addRow(insertIndex, '', failure, failure.toString() + '1',
						failure.toString() + '1a');
			}
			// 添加原因行
			function addCause() {
				var record = grid.getSelectionModel().getSelected();
				if (record == null) {
					alert("请选择所要添加的位置");
					return;
				}

				var effectCode = record.get('effectCode');
				if (effectCode == null || effectCode == '') {
					alert("该记录下不能插入功能原因");
					return;
				}
				var index = store.indexOf(record);
				var insertIndex = -1;
				var num = 2;
				for (var i = index + 1; i < store.getCount(); i++) {
					var re = store.getAt(i);
					if (re.get('effectCode') != null
							&& re.get('effectCode') != '') {
						insertIndex = store.indexOf(re);
						break;
					}
					if (re.get('causeCode') != null
							&& re.get('causeCode') != '') {
						num++;
					}
				}

				if (insertIndex == -1) {
					insertIndex = store.getCount();
				}

				if (num > 26) {
					alert("所添加的原因总数已超过编码要求");
					return;
				}
				// var effect = parentNum.toString() + String.fromCharCode(96 +
				// num);
				addRow(insertIndex, '', '', '', effectCode.toString()
								+ String.fromCharCode(96 + num), 4);
			}
			// 在指定位置添加空白行
			function addRow(index, func, failure, effect, cause) {
				var rec = grid.getStore().recordType;
				var p = new rec({
							m13Id : '',
							functionCode : func,
							functionDesc : '',
							m13fId : '',
							failureCode : failure,
							failureDesc : '',
							effectCode : effect,
							effectDesc : '',
							m13cId : '',
							causeCode : cause,
							causeDesc : '',
							msetfId:'',
							isRef : '0'
						});
				store.insert(index, p);
				store.modified.push(p);
				if (index > 0) {
					m13_color = grid.getView().getRow(index - 1).style.backgroundColor;
					// alert(m13_color);
				} else {
					m13_color = baColor1;
				}
				if (func != null && func != '') {
					// 新加的一行是功能，则变更颜色
					if (m13_color == baColor1
							|| m13_color == 'rgb(178, 223, 238)') {
						// IE||Firefox
						m13_color = baColor2;
					} else {
						m13_color = baColor1;
					}
				}
				// alert(m13_color);
				grid.getView().getRow(index).style.backgroundColor = m13_color;
			}
			function del() {
				var record = grid.getSelectionModel().getSelected();
				if (record == null) {
					alert(commonality_alertDel);
					return;
				}
				if (record.get('functionCode') == '1') {
					var functionLen = 0;
					for (var i = 0; i < store.getCount(); i++) {
						if (store.getAt(i).get('functionCode') != '') {
							functionLen++;
						}
					}
					if (functionLen < 2) {
						alert("不能删除，当前页面至少要保留一个完整的功能分析!");
						return;
					}
				}
				if (record.get('m13Id') != '' || record.get('m13fId') != ''
						|| record.get('m13cId') != '') {
					Ext.MessageBox.show({
								title : commonality_affirm,
								msg : "该记录已保存，如要删除，页面上修改内容都将保存。确认要删除吗？",
								buttons : Ext.Msg.YESNOCANCEL,
								fn : function(id) {
									if (id == 'cancel') {
										return;
									} else if (id == 'yes') {
										changeNext(record);
										store.modified.remove(record)
										store.remove(record);
										doDelete(record);
										// store.modified = [];
									} else if (id == 'no') {
										return;
									}
								}
							});
				} else {
					changeNext(record);
					store.modified.remove(record)
					store.remove(record);
				}
			}
			function doDelete(record) {
				var json = [];
				Ext.each(store.modified, function(item) {
							json.push(item.data);
						});
				var deljson = [];
				deljson.push(record.data);
				waitMsg.show();
				Ext.Ajax.request({
							url : m13.app.delM13Url,
							params : {
								deljson : Ext.util.JSON.encode(deljson),
								jsonData : Ext.util.JSON.encode(json),
								msiId : msiId
							},
							method : "POST",
							success : function(form, action) {
								loadStep();
								store.reload();
								store.modified = [];
								waitMsg.hide();
								parent.refreshTreeNode();
								alert(commonality_messageDelMsg);
							},
							failure : function(form, action) {
								store.reload();
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});

			}
			// 改变删除行后的与其同级或下级的编号,
			function changeNext(record) {
				var level = getLevel(record);
				var index = store.indexOf(record);
				var tage = false;
				store.remove(record);
				for (var i = index; i < store.getCount(); i++) {
					var re = store.getAt(i);
					var newlevel = getLevel(re);
					if (newlevel > level && tage == false) {
						store.remove(re);
						i--;
						// tage = true;
					} else {
						tage = true;
					}
					if (newlevel >= level && tage == true) {
						getChangeRecord(re, newlevel, i);
					}
				}
				store.remove(record);
			}
			function getChangeRecord(re, newlevel, index) {
				if (index > 0) {
					var re1 = store.getAt(index - 1);
					str = re1.get('causeCode');
				} else {
					str = '0';
				}
				code = '';
				if (newlevel == 1) {
					code = String.fromCharCode(str.substring(0, 1)
							.charCodeAt(0)
							+ 1);
					re.set('functionCode', code);
					code += 'A';
					re.set('failureCode', code);
					code += '1';
					re.set('effectCode', code);
					code += 'a';
					re.set('causeCode', code);
				}
				if (newlevel == 2) {
					code = str.substring(0, 1);
					code += String.fromCharCode((str.substring(1, 2)
							.charCodeAt(0) + 1));
					re.set('failureCode', code);
					code += '1';
					re.set('effectCode', code);
					code += 'a';
					re.set('causeCode', code);
				}
				if (newlevel == 4) {
					code = str.substring(0, 3).toString();
					code += String.fromCharCode((str.substring(3, 4)
							.charCodeAt(0) + 1));
					re.set('causeCode', code);
				}
				return re;
			}
			function getLevel(record) {
				var level;
				if (record.get('functionCode') != ''
						&& record.get('functionCode') != null) {
					level = 1;
				} else if (record.get('failureCode') != ''
						&& record.get('failureCode') != null) {
					level = 2;
				} else if (record.get('causeCode') != ''
						&& record.get('causeCode') != null) {
					level = 4;
				}
				return level;
			}

			function saveDate(json, value) {
				Ext.MessageBox.show({
							title : commonality_affirm,
							msg : commonality_affirmSaveMsg,
							buttons : Ext.Msg.YESNOCANCEL,
							fn : function(yesNo) {
								if (yesNo == 'cancel') {
									return;
								} else if (yesNo == 'yes') {
									save(json, value);
									// store.modified = [];
								} else if (yesNo == 'no') {
									goNext(value);
									return;
								}
							}
						});
			}

			function save(json, value) {
				for (var i = 0; i < store.getCount(); i++) {

					var record = store.getAt(i);
			
					if (record.get("functionCode") != ""&& record.get("functionDesc") == "" ) {
						alert("请填写第" + (i + 1) + "行的功能说明！");
						return;
					}
					if (record.get("failureCode") != ""&& record.get("failureDesc") == "" ) {
						alert("请填写第"+ (i + 1) + "行的功能故障！");
						return;
					}
					if (record.get("effectCode") != ""&& record.get("effectDesc") == "" ) {
						alert("请填写第" + (i + 1) + "行的故障影响！");
						return;
					}
					if (record.get("causeCode") != ""&& record.get("causeDesc") == "" ) {
						alert("请填写第"  + (i + 1) + "行的故障原因！");
						return;
					}

				}
				waitMsg.show();
				Ext.Ajax.request({
							url : m13.app.saveM13Url,
							params : {
								jsonData : Ext.util.JSON.encode(json),
								msiId : msiId
							},
							method : "POST",
							success : function(form, action) {
								parent.refreshTreeNode();
								store.modified = [];
								step[4] = 2;
								// Ext.Msg.alert("信 息", "保存成功！");
								waitMsg.hide();
								alert(commonality_messageSaveMsg);
								goNext(value);
								store.reload();

							},
							failure : function(form, action) {
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});
			}
			function zancun() {
				// 判断是否有权限修改
				if (isMaintain != '1') {					
					alert('没有暂存权限');
				} else {
					var json = [];
					Ext.each(store.modified, function(item) {
						// 去除首尾空格
						item.data.functionDesc = dislodge(item.data.functionDesc);
						item.data.failureDesc = dislodge(item.data.failureDesc);
						item.data.effectDesc = dislodge(item.data.effectDesc);
						item.data.causeDesc = dislodge(item.data.causeDesc);
						json.push(item.data);
					});
					if (json.length > 0) {
						Ext.MessageBox.show({
							title : commonality_affirm,
							msg : "是否需要暂存",
							buttons : Ext.Msg.YESNOCANCEL,
							fn : function(yesNo) {
								if (yesNo == 'cancel') {
									return;
								} else if (yesNo == 'yes') {
									zancunSave(json);
								} else if (yesNo == 'no') {
									goNext(value);
									return;
								}
							}
						});
					} else {
						alert('没有需要暂存的数据');
					}
				}				
			}

			function zancunSave(json) {
				waitMsg.show();
				Ext.Ajax.request({
					url : m13.app.saveZanUrl,
					params : {
						jsonData : Ext.util.JSON.encode(json),
						msiId : msiId
					},
					method : "POST",
					success : function(form, action) {
						store.modified = [];
						waitMsg.hide();
						alert(commonality_messageSaveMsg);
						store.reload();
					},
					failure : function(form, action) {
						waitMsg.hide();
						alert(commonality_cautionMsg);
					}
				});
			}

			// 去除首尾空格
			function dislodge(data) {
				if (data != null) {
					return data.trim();
				}
				return '';

			}

			// 点击下一步执行的方法
			nextOrSave = function(value) {
				if (value == null) {
					value = 4;
				}
				if (store.modified.length == 0) {
					goNext(value);
					return false;
				}
				// 判断是否有权限修改
				if (isMaintain != '1') {
					// alert("您没有修改权限，无权修改！");
					goNext(value);
					return;
				}
				var result = true;
				var json = [];
				var highestLevel = store.getAt(0).get('proCode');
				// alert(highestLevel);
				Ext.each(store.modified, function(item) {
					// 去除首尾空格
					item.data.functionDesc = dislodge(item.data.functionDesc);
					item.data.failureDesc = dislodge(item.data.failureDesc);
					item.data.effectDesc = dislodge(item.data.effectDesc);
					item.data.causeDesc = dislodge(item.data.causeDesc);
					json.push(item.data);
				});
				if (json.length > 0 && result == true) {
					saveDate(json, value);
				}
			}

		}
	};
}();