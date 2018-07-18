/**
 * 此页面用来显示用户信息 可进行 增 删 改 查操作。
 * 
 * @authour zhouli createDate 2012-08-1
 * 
 * 
 */
Ext.namespace('comUser');
Ext.onReady(function() {

	Ext.form.Field.prototype.msgTarget = 'qtip'; // 用来显示验证信息
	Ext.QuickTips.init();
	
	var urlPrefix = contextPath + "/com/user/";
	var loadDataUrl = "getDataComUserList.do";
	var updateDataUrl = "jsonComUserUpdate.do";
	var changeUserPassWordUrl = "changeUserPassWord.do";
	var checkUserCode = "checkUserCode.do";
	var deleteComUser = 'deleteComUserByUserId.do';
	var jsonResetUserPassWordUrl = "jsonResetUserPassWord.do";
	var rendToDivName = 'userGrid';

	var currentRowIndex = -1;
	var currentRecord;

	function cellclick(grid, rowIndex, columnIndex, e) {
		if (currentRowIndex != rowIndex) {
			currentRowIndex = rowIndex;
			currentRecord = grid.getStore().getAt(rowIndex);
		}
	}
	
	Ext.apply(Ext.form.VTypes, {
				password : function(val, field) {// val指这里的文本框值，field指这个文本框组件，大家要明白这个意思
					if (field.confirmTo) {// confirmTo是我们自定义的配置参数，一般用来保存另外的组件的id值
						var pwd = Ext.get(field.confirmTo);// 取得confirmTo的那个id的值
						return (val == pwd.getValue());
					}
					return true;
				}
			});

	var fieldDatas = [{
				fieldLabel : '新密码' + commonality_mustInput,
				name : 'newPassWord',
				id : 'newPassWord',
				allowBlank : false,
				blankText : '新密码不能为空',
				inputType : 'password',
				anchor : '90%',
				maxLength : 10,
				minLengthText : '输入信息超过10长度!'

			}, {
				fieldLabel : '确认新密码' + commonality_mustInput,
				name : 'comFirmNewPassWord',
				id : 'comFirmNewPassWord',
				inputType : 'password',
				anchor : '90%',
				maxLength : 10,
				minLengthText : '输入信息超过10长度!'
			}];

	var chooseCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'validFlag',
				id : 'validFlag',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [["0", '是'], ["1", '否']]

						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				triggerAction : 'all',
				width : 50,
				// value:1,
				editable : false,
				selectOnFocus : true
			});

	// 删除记录
	function deleteDatas(aurl, astore, arecord) {
		var json = [];
		var result = 0;
		json.push(arecord);
		if (json.length > 0) {
			var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : commonality_waitMsg,
						removeMask : true
					});
			myMask.show();
			Ext.Ajax.request({
						url : aurl,
						params : {
							processEntityId : arecord.get('userId'),
							method : 'delete'
						},
						method : "POST",
						waitMsg : commonality_waitMsg,
						success : function(response) {
							myMask.hide();
							if (response.responseText !== null && response.responseText !== '') {
								var result = Ext.util.JSON.decode(response.responseText);
								if (result.failure === false || result.failure == 'false' || result.success === true || result.success == 'true') {
									alert(commonality_messageDelMsg);
									astore.reload();
									result = 0;
								} else {
									alert(commonality_messageDelMsgFail + '用户信息正在使用,不能退职，但其它信息已保存成功!');
									result = 1;
								}
							} else {
								alert(commonality_messageDelMsg);
								astore.reload();
								result = 0;
							}
						},
						failure : function(response) {
							myMask.hide();
							alert(commonality_messageDelMsgFail);
							result = 1;
						}
					});
		} else {
			alert(commonality_alertDel);
			result = 1;
		}
		return result;

	}

	// 修改与新增保存
	function updateData(aurl, astore, json) {
		var result = 0;
		if (json.length > 0) {
			var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : commonality_waitMsg,
						removeMask : true
					});
			myMask.show();
			Ext.Ajax.request({
						url : aurl,
						params : {
							jsonData : Ext.util.JSON.encode(json),
							method : 'update'
						},
						async : false, // 是否为异步请求
						method : "POST",
						waitMsg : commonality_waitMsg,
						success : function(response) {
							myMask.hide();
							if (response.responseText !== null
									&& response.responseText !== '') {
								var result = Ext.util.JSON.decode(response.responseText);
								if (result.success == true) {
									if (result.noQuitCode != '' && result.noQuitCode != null) {
										alert(result.noQuitCode + '用户信息正在使用,不能退职，但其它信息已保存成功!');
									} else {
										alert(commonality_messageSaveMsg);
									}
									astore.reload();
									astore.modified = [];
								} else {
									if (result.success == 'exits') {
										alert('编号有重复，保存失败，请修改');
									}
								}
							}
						},
						failure : function(response) {
							myMask.hide();
							alert('登录名不能为空');
						}
					});
		}
	}

	// shorthand alias
	var fm = Ext.form;
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [{
					header : 'ID',
					dataIndex : 'userId',
					width : 50,
					hidden : true,
					resizable : false,
					fixed : true,
					sortable : false,
					editor : new fm.TextField({
								allowBlank : false
							})
				}, {
					header : '登录名' + commonality_mustInput,
					dataIndex : 'userCode',
					width : 100,
					editor : new fm.TextField({
								allowBlank : false,
								maxLength : 20,
								maxLengthText : '输入信息超过20长度!'
							})

				}, {
					header : '用户名' + commonality_mustInput,
					dataIndex : 'userName',
					width : 150,
					editor : new fm.TextField({
								allowBlank : false,
								maxLength : 50,
								maxLengthText : '输入信息超过50长度!'
							})
				}, {
					header : '职位',
					dataIndex : 'post',
					width : 100,
					editor : new fm.TextField({
								maxLength : 25,
								allowBlank : true,
								maxLengthText : '输入信息超过25长度!'
							})
				}, {
					header : '电话',
					dataIndex : 'plone',
					width : 100,
					editor : new fm.NumberField({
								maxLength : 20,
								allowBlank : true,
								maxLengthText : '输入信息超过20长度!'
							})
				}, {
					header : '邮箱',
					dataIndex : 'EMail',
					width : 150,
					editor : new fm.TextField({
								maxLength : 50,
								allowBlank : true,
								maxLengthText : '输入信息超过50长度!',
								regex : /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/,
								regexText : '邮箱格式不正确'
							})
				}, {
					header : '是否退职' + commonality_mustInput,
					dataIndex : "validFlag",
					width : 70,
					editor : chooseCombo,
					renderer : function(value, cellmeta, record) {
						var index = chooseCombo.store.find(Ext.getCmp('validFlag').valueField, value);
						var record = chooseCombo.store.getAt(index);
						var returnvalue = '否';
						if (record) {
							returnvalue = record.data.displayText;
						}
						if (value == 1) {
							return '否';
						}

						return returnvalue;
					}
				}]
	});

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : urlPrefix + loadDataUrl
						}),
				reader : new Ext.data.JsonReader({
							root : 'comUser',
							fields : [{
										name : 'userId',
										type : 'string'
									}, {
										name : 'userCode',
										type : 'string'
									}, {
										name : 'userName',
										type : 'string'
									}, {
										name : 'password',
										type : 'string'
									}, {
										name : 'post',
										type : 'string'
									}, {
										name : 'plone',
										type : 'string'
									}, {
										name : 'EMail',
										type : 'string'
									}, {
										name : 'validFlag',
										type : 'string'
									}]
						}),
				sortInfo : {
					field : 'userCode',
					direction : 'ASC'
				}
			});

	store.on("beforeload", function() {
				store.baseParams = {
					start : 0,
					limit : 20,
					queryValidFlag : Ext.getCmp('queryValidFlag').getValue(),
					userCode : Ext.getCmp('userCode').getValue(),
					keyword : Ext.getCmp('keyword').getValue()
				};
			});
	
	var queryValidFlagCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'queryValidFlag',
				id : 'queryValidFlag',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [['', '全部'], ['0', '是'],
									['1', '否']]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				fieldLabel : '查看用户是否退职：',
				triggerAction : 'all',
				width : 70,
				emptyText : '--请选择--',
				editable : false,
				selectOnFocus : true
			});
	queryValidFlagCombo.setValue("1");
	// create the editor grid
	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		cm : cm,
		sm : sm,
		header : false,
		id : 'grid',
		stripeRows : true,
		renderTo : rendToDivName,
		split : true,
		collapsible : true,
		frame : true,
		clicksToEdit : 2,
		bbar : new Ext.PagingToolbar({
					pageSize : 20,
					store : store,
					displayInfo : true,
					displayMsg : commonality_turnPage,
					emptyMsg : commonality_noRecords
				}),
		tbar : new Ext.Toolbar({

			items : [
					'登录名：',
					{
						xtype : 'textfield',
						id : 'userCode',
						width : 100
					},
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'用户名：',
					{
						xtype : 'textfield',
						id : 'keyword',
						width : 100
					},
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'查看用户是否退职：',
					queryValidFlagCombo,
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;"),
					{
						text : commonality_search,
						iconCls : "icon_gif_search",
						id : 'sericon',
						handler : function() {
							if (currentRecord != undefined || currentRecord != null) {
								currentRecord = null;
							}
							store.proxy = new Ext.data.HttpProxy({
										url : urlPrefix + loadDataUrl
									});
							store.modified = [];
							store.load();
						}
					},
					'-',
					{
						text : commonality_add,
						iconCls : "icon_gif_add",
						handler : function() {
							var index = store.getCount();
							var rec = grid.getStore().recordType;
							var p = new rec({
										userId : '',
										userCode : '',
										userName : '',
										password : '',
										post : '',
										plone : '',
										EMail : '',
										validFlag : 1
									});
							store.insert(index, p);
						}
					},
					'-',
					{
						text : commonality_save,
						iconCls : "icon_gif_save",
						waitMsg : commonality_waitMsg,
						handler : function() {
							var json = [];
							var temp = 1;
							Ext.each(store.modified, function(item) {
								if (LTrim(item.data.userCode) == '' || LTrim(item.data.userCode) == null) {
									alert('登录名不能为空');
									temp = 2;
									return false;
								}
								if (LTrim(item.data.userName) == '' || LTrim(item.data.userName) == null) {
									alert('用户名称不能为空');
									temp = 2;
									return false;
								}
								json.push(item.data);
							});
							if (temp == 1) {
								if (json <= 0) {
									result = 1;
									alert(commonality_alertUpdate);
									store.load();
									return;
								}
								Ext.Msg.confirm(commonality_affirm,
										commonality_affirmSaveMsg,
										function(btn) {
											if (btn === 'yes') {
												updateData(urlPrefix + updateDataUrl, store, json);
											}
										});
							}
							currentRecord = null;
						}
					},
					'-',
					{
						text : commonality_del,
						iconCls : "icon_gif_delete",
						waitMsg : commonality_waitMsg,
						handler : function() {
							currentRecord = grid.getSelectionModel().getSelected();
							if (currentRecord == null) {
								alert(commonality_alertDel);
								return;
							}
							Ext.MessageBox.show({
										title : commonality_affirm,
										msg : commonality_affirmDelMsg,
										buttons : Ext.Msg.YESNO,
										fn : function(id) {
											if (id == 'cancel') {
												return;
											} else if (id == 'yes') {
												var flag = currentRecord.get('validFlag');// 判断是否是离职员工
												if (flag == '0') {
													alert('该员工已经退职');
													return;
												}
												if (currentRecord.get('userId') == '') {
													store.remove(currentRecord);
													store.modified.remove(currentRecord);
													return;
												} else {
													doDelete(currentRecord);
													store.modified = [];
												//store.remove(currentRecord);
												}
												store.modified.remove();
											}
										}
									});
						}
					},
					'-',
					{
						text : '密码重置',
						iconCls : "icon_gif_update",
						waitMsg : commonality_waitMsg,
						handler : function() {
							currentRecord = grid.getSelectionModel().getSelected();
							if (currentRecord != null) {
								Ext.Msg.confirm(commonality_affirm,
										'确认要密码重置吗？', function(btn) {
											if (btn === 'yes') {
												Ext.Ajax.request({
													url : urlPrefix + jsonResetUserPassWordUrl,
													params : {
														userId : currentRecord.get("userId")
													},
													method : "POST",
													success : function(response) {
														alert('密码初始化成功，初始密码为：000000！');
														store.reload();
													},
													failure : function(response) {
														alert(commonality_cautionMsg);
													}
												});
												currentRecord = null;
											}
										});
							} else {
								alert('请先选择用户！');
							}

						}
					},
					'-',
					{
						text : '修改密码',
						iconCls : "icon_gif_logout",
						waitMsg : commonality_waitMsg,
						handler : function() {
							currentRecord = grid.getSelectionModel().getSelected();
							if (currentRecord == undefined) {
								alert('请先选择用户');
								return;
							}
							var userId = currentRecord.data.userId;// 得到选中行用户id
							var addForm = new Ext.form.FormPanel({
								labelWidth : 80,
								defaultType : 'textfield',
								fileUpload : true,
								method : 'POST',
								enctype : 'multipart/form-data',
								frame : true,
								items : [fieldDatas],
								buttonAlign : 'center',
								buttons : [{
									text : commonality_save,
									id : 'saveBtn',
									handler : function() {
										if (Ext.get('newPassWord').dom.value == "") {
											alert('新密码不能为空');
											Ext.getCmp('newPassWord').focus(false, true);
											return;
										}
										if (Ext.get('newPassWord').dom.value.length > 10) {
											alert('新密码输入信息超过10长度!');
											return;
										}
										if (Ext.get('comFirmNewPassWord').dom.value == "") {
											alert('确认新密码不能为空!');
											Ext.getCmp('newPassWord').focus(false, true);
											return;
										}
										if (Ext.get('comFirmNewPassWord').dom.value.length > 10) {
											alert('确认新密码入信息超过10长度!');
											return;
										}
										if (Ext.get('newPassWord').dom.value != Ext.get('comFirmNewPassWord').dom.value) {
											alert('新密码和确认密码不一致');
											Ext.getCmp('comFirmNewPassWord').setValue('');
											Ext.getCmp('comFirmNewPassWord').focus(false, true);
											return;
										}
										Ext.Msg.confirm(commonality_affirm,
												commonality_affirmSaveMsg,
												function(btn) {
													if (btn === 'yes') {
														addForm.form.submit({
															params : {
																userId : userId
															},
															url : urlPrefix + changeUserPassWordUrl,
															method : 'post',
															success : function(form, action) {
																alert(commonality_messageSaveMsg);
																win.destroy();
															},
															failure : function(form, action) {
																alert('修改密码失败，请联系供应商！');
																// win.destroy();
																// currentRecord=null;
															}
														});
														currentRecord = null;
													}
												})
									}
								}, {
									text : commonality_close,
									handler : function() {
										win.destroy(); // 销毁窗口
									}
								}]
							});
							var win = new Ext.Window({
										title : '修改密码',
										width : 400,
										autoHeight : true,
										draggable : false,
										resizable : false,
										bodyStyle : 'padding: 5px;',
										items : [addForm],
										modal : true
									});
							win.show();
						}

					},
					"->",
					new Ext.Toolbar.TextItem("<font  color='red' style='font-weight:bold' >"
							+ '新建用户默认密码"000000"'
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>")]
		})
			// tbar over
	}

	// grid function over
	); // grid over

	grid.addListener("afteredit", afteredit);
	// 修改后操作
	function afteredit(val) {
		var count = 0;
		if (val.field == 'userCode') {
			var nowRecord = val.record;
			var items = grid.getStore().data.items;
			for (var i = 0; i < items.length; i++) {
				if (items[i].get('userCode') == val.value) {
					count++;
				}
				if (count == 2) {
					alert('该编号已存在，请更改');
					nowRecord.set('userCode', val.originalValue);
					return false;
				}
			}

			Ext.Ajax.request({
						url : urlPrefix + checkUserCode,
						params : {
							userCode : val.value,
							userId : nowRecord.get("userId")
						},
						success : function(response) {
							if (response.responseText == "false") {
								alert('该编号已存在，请更改');
								nowRecord.set('userCode', val.originalValue);
							}
						}
					});
		}

	}
	// 删除方法
	function doDelete(record) {
		Ext.Ajax.request({
					url : urlPrefix + deleteComUser,
					params : {
						userId : record.get('userId')
					},
					method : "POST",
					success : function(form, action) {
						// myMask.hide();
						if(record.get('userId')==form.responseText){
						 alert(record.get('userCode')+ ',用户信息正在使用,不能删除!');
						}else{
							alert(commonality_messageDelMsg);
						}
					     store.reload();
					},
					failure : function(form, action) {
						// myMask.hide();
						alert(commonality_cautionMsg);
					}
				});

	}

	grid.render();
	grid.addListener("cellclick", cellclick);
	store.load({
				params : {
					start : 0,
					limit : 20,
					queryValidFlag : Ext.getCmp('queryValidFlag').getValue()
				}
			});

	var window = new Ext.Window({
				layout : 'fit',
				border : false,
				resizable : true,
				closable : false,
				maximized : true,
				plain : true,
				buttonAlign : 'center',
				title : returnPageTitle('用户管理', 'userManage'),
				items : [grid]
			});
	window.show();
});
/* 去前后空格 */
function LTrim(str) {
	// return str.replace(/^\s*/g,"");
	return str.replace(/(^\s*)|(\s*$)/g, "");
}