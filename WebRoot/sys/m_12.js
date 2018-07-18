Ext.namespace('m12');
m12.app = function() {
	return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';

			// 定义数据
			var store = new Ext.data.Store({
						url : m12.app.loadM12Url,
						reader : new Ext.data.JsonReader({
									root : "m12",
									fields : [{
												name : 'm12Id'
											}, {
												name : 'msiId'
											}, {
												name : 'proCode'
											}, {
												name : 'proName'
											}, {
												name : 'quantity'
											}, {
												name : 'vendor'
											}, {
												name : 'partNo'
											}, {
												name : 'similar'
											}, {
												name : 'zonalChannel'
											}, {
												name : 'historicalMtbf'
											}, {
												name : 'predictedMtbf'
											}, {
												name : 'zonal'
											}, {
												name : 'mmel'
											}, {
												name : 'isAddAta'
											}]
								})
					});

			// 是否下拉框
			var chooseCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'choose',
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

			// 供应商列表
			var vendorCombo = new Ext.form.ComboBox({
						name : 'vendorCom',
						id : 'vendorCom',
						store : new Ext.data.Store({
									url : m12.app.searchVendorUrl,
									reader : new Ext.data.JsonReader({
												root : 'vendor',
												fields : [{
															name : 'id'
														}, {
															name : 'name'
														}]
											})
								}),
						valueField : "id",
						displayField : "name",
						mode : 'local',
						triggerAction : 'all',
						width : 50,
						editable : true,
						listeners:{
		                      specialKey:function(field,e){
		                            if(e.getKey() == Ext.EventObject.ENTER){
		                                                   return;
		                                        }
		                         }
		                      }
					});
			// 加载store
			store.on("beforeload", function() {
						store.baseParams = {
							msiId : msiId
						};
					});
			vendorCombo.store.on("load", function() {
						store.load();
					});

			vendorCombo.store.load();
			var colM = new Ext.grid.ColumnModel([
			{
				header : "<div align='center'>" + "项目编号" + commonality_mustInput + "</div>",
				dataIndex : "proCode",
				width : 80,
				editor : new Ext.form.TextField({
							maxLength : 20,
							regex :/^\d{2}-\d{2}-\d{2}-\d{2}$/,
							maxLengthText : commonality_MaxLengthText
						})
			}, {
				header : "<div align='center'>" + "项目名称" + "</div>",
				dataIndex : "proName",
				hidden : commonality_hidden,
				width : 100,
				editor : new Ext.form.TextArea({
					allowBlank : false,
					maxLength : 500,
					maxLengthText : commonality_MaxLengthText,
					grow : true
				})

			}, {
				header : "数量",
				dataIndex : "quantity",
				align: 'center',
				width : 100,
				editor : new Ext.form.NumberField({
							maxLength : 10,
							maxLengthText : commonality_MaxLengthText,
							fieldLabel : "整数",
							allowDecimals : false, // 不允许输入小数
							nanText : "请输入有效整数", // 无效数字提示
							allowNegative : false
						})
				}, {
				header : "供应商" + commonality_mustInput,
				dataIndex : "vendor",
				width : 150,
				editor : vendorCombo,
				renderer : function(value, cellmeta, record) {
					var index = vendorCombo.store.findBy(function(record, id) {
								if (record.get('id') == value) {
									return true;
								}
							});
					var record = vendorCombo.store.getAt(index);
					var returnvalue = "";
					if (record) {
						returnvalue = record.data.name;
					}
					return returnvalue;
				}
			}, {
				header : "件号（制造商/供应商）",
				dataIndex : "partNo",
				width : 150,
				editor : new Ext.form.TextArea({
							maxLength : 200,
							maxLengthText : commonality_MaxLengthText,
							grow : true
						})
			}, {
				header : "类似",
				dataIndex : "similar",
				width : 100,
				editor : new Ext.form.TextArea({
							maxLength : 200,
							maxLengthText : commonality_MaxLengthText,
							grow : true
						})
			}, {
				header : "区域通道",
				dataIndex : "zonalChannel",
				width : 100,
				editor : new Ext.form.TextArea({
							maxLength : 50,
							maxLengthText : commonality_MaxLengthText,
							grow : true
						})
			}, {
				header : "过去的MTBF",
				dataIndex : "historicalMtbf",
				width : 100,
				editor : new Ext.form.TextArea({
							maxLength : 50,
							maxLengthText : commonality_MaxLengthText,
							grow : true
						})
			}, {
				header : "预测的MTBF",
				dataIndex : "predictedMtbf",
				width : 100,
				editor : new Ext.form.TextArea({
							maxLength : 50,
							maxLengthText : commonality_MaxLengthText,
							grow : true
						})
			}, {
				header : "区域",
				dataIndex : "zonal",
				width : 100,
				editor : new Ext.form.TextArea({
							regex : /^(\d+,)*\d+$/,
							regexText : "请输入正确的区域号并用逗号隔开",
							maxLength : 200,
							maxLengthText : commonality_MaxLengthText,
							grow : true
						})
			}, {
				header : "MMEL",
				dataIndex : "mmel",
				width : 100,
				editor : chooseCombo,
				renderer : function(value, cellmeta, record) {
					var index = chooseCombo.store.find(
							Ext.getCmp('choose').valueField, value);
					var record = chooseCombo.store.getAt(index);
					var returnvalue = "";
					if (record) {
						returnvalue = record.data.displayText;
					}
					return returnvalue;
				}
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
											text : commonality_add,
											iconCls : "icon_gif_add",
											disabled : !flag,
											handler : function() {
												add();
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
									title : returnPageTitle("系统组成和相关信息", 'm_12'),
									items : [grid]
								}]
					});
			
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
			function add() {
				var rec = grid.getStore().recordType;
				var p = new rec({
							m12Id : '',
							msiId : msiId,
							proCode : '',
							proName : '',
							quantity : '',
							vendor : '',
							partNo : '',
							similar : '',
							zonalChannel : '',
							historicalMtbf : '',
							predictedMtbf : '',
							zonal : '',
							mmel : '',
							isAddAta : '1'
						});
				store.add(p);
			}
			// 检查区域编号是否存在
			grid.addListener("afteredit", afteredit);
			function afteredit(val) {
					var nowRecord = val.record;
				if (val.field == 'zonal') {
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
											nowRecord.set('zonal',val.originalValue);
											return ;
										}
										if(msg.unExists){
											alert("区域:"+ msg.unExists+"不存在,请重新修改")
											nowRecord.set('zonal',val.originalValue);
											return ;
										}
										if (msg.success) {
											alert("不能转入区域:"+ msg.success+"，该区域已被冻结或者已经审批完成，请重新修改")
											nowRecord.set('zonal',val.originalValue);
											return ;
										}
									}
								}
							});
				}
			
				if (val.field == 'proCode') {
					var cou = 0;
					for (var i = 0; i < store.getCount(); i++) {
						if (store.getAt(i).get('proCode') == val.value) {
							cou = cou + 1;
						}
						if (cou > 1) {
							alert("编号重复，请重新输入！");
							nowRecord.set('proCode', val.originalValue);
							return;
						}
					}
				}
			}
			function del() {
				var record = grid.getSelectionModel().getSelected();
				if (record == null) {
					alert(commonality_alertDel);
					return;
				}
				if (record.get('m12Id') != '') {
					Ext.MessageBox.show({
								title : commonality_affirm,
								msg : commonality_affirmDelMsg,
								buttons : Ext.Msg.YESNOCANCEL,
								fn : function(id) {
									if (id == 'cancel') {
										return;
									} else if (id == 'yes') {
										doDelete(record);
										// store.modified = [];
									} else if (id == 'no') {
										return;
									}
								}
							});
				} else {
					store.remove(record);
					store.modified.remove(record);
				}
			}

			function doDelete(record) {
				if (record.get('isAddAta') != 1) {
					alert("非手动添加，不能删除！");
					return;
				}
				waitMsg.show();
				Ext.Ajax.request({
							url : m12.app.delM12Url,
							params : {
								delId : record.get('m12Id')
							},
							method : "POST",
							success : function(form, action) {
								store.load();
								store.modified = [];
								waitMsg.hide();
								alert(commonality_messageDelMsg);
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
					for (var i = 0; i < store.getCount(); i++) {
						var record = store.getAt(i);
						if (record.get("proCode").trim() == '' || record.get("proCode") == null) {
							alert("编号不能为空");
							return false;
						}
						if (record.get("vendor").trim() == '' || record.get("vendor") == null) {
							alert("供应商不能为空");
							return false;
						}
					}
					var json = [];
					Ext.each(store.modified, function(item) {
						var data = item.data;
						// 去除空格
						if (item.data.proNameEn != null) {
							item.data.proNameEn = item.data.proNameEn.trim();
						}
						if (item.data.proName != null) {
							item.data.proName = item.data.proName.trim();
						}
						if (item.data.proCode != null) {
							item.data.proCode = item.data.proCode.trim();
						}
						json.push(item.data);
					});
					if(json.length < 1){
						alert('没有需要暂存的数据');
						return;
					}
					
					Ext.MessageBox.show({
						title : commonality_affirm,
						msg : "是否需要暂存",
						buttons : Ext.Msg.YESNOCANCEL,
						fn : function(yesNo) {
							if (yesNo == 'cancel') {
								return;
							} else if (yesNo == 'yes') {
								waitMsg.show();
								Ext.Ajax.request({
									url : m12.app.saveZanUrl,
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
							} else if (yesNo == 'no') {
								return;
							}
						}
					});
				}				
			}
			function saveDate(value) {
				Ext.MessageBox.show({
					title : commonality_affirm,
					msg : commonality_affirmSaveMsg,
					buttons : Ext.Msg.YESNOCANCEL,
					fn : function(yesNo) {
						if (yesNo == 'cancel') {
							return;
						} else if (yesNo == 'yes') {
							var json = [];
							var result = true;
							for (var i = 0; i < store.getCount(); i++) {
								var record = store.getAt(i);
								if (record.get("vendor") == ''
										|| record.get("vendor") == null) {

									alert("供应商不能为空");
									result = false;
									return false;

								}
							}
							Ext.each(store.modified, function(item) {
										var data = item.data;
										// 去除空格
										if (item.data.proNameEn != null) {
											item.data.proNameEn = item.data.proNameEn.trim();
										}
										if (item.data.proName != null) {
											item.data.proName = item.data.proName.trim();
										}
										if (item.data.proCode != null) {
											item.data.proCode = item.data.proCode.trim();
										}
										if (item.data.proCode == '') {
											alert("编号不能为空!");
											result = false
											return false;
										}
										json.push(item.data);
									});
										if(!result){
											return;
										}
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
				waitMsg.show();
				Ext.Ajax.request({
							url : m12.app.saveM12Url,
							params : {
								jsonData : Ext.util.JSON.encode(json),
								msiId : msiId
							},
							method : "POST",
							success : function(form, action) {
								waitMsg.hide();
								alert(commonality_messageSaveMsg);
								parent.refreshTreeNode();
								step[8] = 1;
								goNext(value);
								// store.reload();

							},
							failure : function(form, action) {
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});
			}

			// 点击下一步执行的方法
			nextOrSave = function(value) {
				if (value == null) {
					value = 10;
				}
				if (store.modified.length == 0) {
					goNext(value);
					return false;
				}
				// 判断是否有权限修改
				if (isMaintain != '1') {
					// alert("您没有修改权限，无权修改！");
					goNext(value);
				} else {
					saveDate(value);
				}
			}

		}
	};
}();