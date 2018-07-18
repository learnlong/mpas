/**
 * 此页面用来编辑专业室，并且分配专业室权限。
 * @authour samual
 * createDate 2014-11-03
 * 
 */
Ext.namespace('professionAndAuth');
	var waitMsg;
	var numAjax = 0;
	var waitMsgHide = function(){
			if(--numAjax == 0){		
			  if (waitMsg != undefined && waitMsg != null){
			  	waitMsg.hide();
			  }
			}
	}
	
Ext.onReady(function() {
	Ext.form.Field.prototype.msgTarget = 'qtip'; // 用来显示验证信息
	Ext.QuickTips.init();
	
			var urlPrefix=contextPath+"/userManage/profession/";
			var loadDataUrl="showProfessionList.do";
			var updateDataUrl = "jsonPofessionUpdate.do";
			var delComProfession = 'delComProfessionById.do';
			var checkProfessionCodeUrl = 'jsonCheckProfessionCode';
			
			var rendToDivName="professionGrid";
			var currentRowIndex = -1;
			var currentRecord;
			
			function cellclick(grid,rowIndex, columnIndex, e) {
				if (currentRowIndex != rowIndex) {
					currentRowIndex = rowIndex;
					currentRecord = grid.getStore().getAt(rowIndex);
				}
			}
			

			var fm = Ext.form;
			var professionList = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
			
			var cm = new Ext.grid.ColumnModel({
				defaults : {
					sortable : true
				},
			columns : [{
					header : '',
					dataIndex : 'profession_id',
					width : 10,
					hidden : true,
					resizable : false,
					fixed : true,
					sortable : false,
					editor : new fm.TextField({
						allowBlank : false
					})
				},{
					header : '专业室编号' + commonality_mustInput,
					dataIndex : 'professionCode',
					width : 200,
					sortable : true,
					editor : new fm.TextField({
						allowBlank : false,
						maxLength : 10,
						maxLengthText : '输入信息超过10长度!'
					})
				}, {
					header : '专业室名称' + commonality_mustInput,
					dataIndex : 'professionName',
					width : 400,
					sortable : true,
					editor : new fm.TextField({
						allowBlank : false,
						maxLength : 50,
						maxLengthText : '输入信息超过50长度!'
					})
				},{
					header : '',
					dataIndex : 'validFlag',
					width : 10,
					hidden : true,
					resizable : false,
					fixed : true,
					sortable : false,
					editor : new fm.TextField({
						allowBlank : false
					})
				}]
			});

			var store = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : urlPrefix+loadDataUrl
				}),
				reader : new Ext.data.JsonReader( {
					root : 'comProfession',
					fields : [ {
						name : 'professionId',
						type : 'string'
					}, {
						name : 'professionCode',
						type : 'string'
					}, {
						name : 'professionName',
						type : 'string'
					}, {
						name : 'validFlag',
						type : 'string'
					}]
				}),
				sortInfo : {
					field : 'professionCode',
					direction : 'ASC'
				}
			});

			// create the editor grid
			var grid = new Ext.grid.EditorGridPanel( {
				store : store,
				cm : cm,
				sm : professionList,
				//header:true,
				id:'grid',
				renderTo : rendToDivName,
				split:true,
				//collapsible : true,
				//title : gridTitle,
				stripeRows : true,
				frame : true,
				clicksToEdit : 2,
				bbar: new Ext.PagingToolbar({ 
					pageSize: 13, 
					store: store, 
					displayInfo: true, 
					displayMsg: commonality_turnPage, 
					emptyMsg: commonality_noRecords }),
					tbar : new Ext.Toolbar(
							{
							items : ['专业室编号：',
										{
								xtype : 'textfield',
								id : 'professionCode',
								width : 100
							},
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							'专业室名称：',
							{
								xtype : 'textfield',
								id : 'keyword',
								width : 100
							},
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							{
								text : commonality_search,
								iconCls : "icon_gif_search",
								id : 'sericon',
								handler : function() {
									if (currentRecord != undefined || currentRecord != null) {
										currentRecord = null;
									}
									store.on("beforeload", function() {
												store.baseParams = {
													start : 0,
													limit : 20,
													professionCode : Ext.getCmp('professionCode').getValue(),
													keyword : Ext.getCmp('keyword').getValue()//,
													//userCode : Ext.get('userCode').dom.value,
													//keyword : Ext.get('keyword').dom.value
												};
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
												professionId : '',
												professionCode : '',
												professionName : '',
												validFlag : '1'
											});
									store.insert(index, p);
									//store.modified.push(p);
								}
							},'-',{
								text:commonality_save,
								iconCls : "icon_gif_save",
								handler: function() {
									var json = [];
									var temp = 1;
									Ext.each(store.modified, function(item) {
										if (LTrim(item.data.professionCode) == ''
												|| LTrim(item.data.professionCode) == null) {
											alert('专业室编号不能为空!');
											temp = 2;
											return false;
										}
										if (LTrim(item.data.professionName) == ''
												|| LTrim(item.data.professionName) == null) {
											alert('专业室名称不能为空!');
											temp = 2;
											return false;
										}
										json.push(item.data);
									});
									if (temp == 1) {
										if (json <= 0) {
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
							},'-',{
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
															alert(comUser_userIsFalse);
															return;
														}
														if (currentRecord.get('professionId') == '') {
															store.remove(currentRecord);
															store.modified.remove(currentRecord);
															return;
														} else {
															doDelete(currentRecord);
															store.modified = [];
														}
														store.modified.remove();
													}
												}
											});
								}
							}]
			})
			} // grid function over
			); // grid over
			grid.render();	
			grid.addListener("cellmousedown", cellclick);
			grid.addListener("afteredit", afteredit);
			store.load();

			// 修改后操作
			function afteredit(val) {
				var count = 0;
				if (val.field == 'professionCode') {
					var nowRecord = val.record;
					var items = grid.getStore().data.items;
					for (var i = 0; i < items.length; i++) {
						if (items[i].get('professionCode') == val.value) {
							count++;
						}
						if (count == 2) {
							alert('该编号已存在，请更改');
							nowRecord.set('professionCode', val.originalValue);
							return false;
						}
					}
					Ext.Ajax.request({
								url : urlPrefix + checkProfessionCodeUrl,
								params : {
									professionCode : val.value,
									professionId : nowRecord.get("professionId")
								},
								success : function(response) {
									if (response.responseText == "false") {
										alert('该编号已存在，请更改');
										nowRecord.set('professionCode', val.originalValue);
									}
								}
							});
				}

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
									if (response.responseText !== null && response.responseText !== '') {
										var result = Ext.util.JSON.decode(response.responseText);
										if (result.success == true) {
											astore.reload();
											astore.modified = [];
										} else {
											if (result.success == 'exits') {
												alert('用户编号已存在 !');
											}
										}
									}
								},
								failure : function(response) {
									myMask.hide();
									alert('用户编号已存在 !');
								}
							});
				}
			}

			// 删除方法
			function doDelete(record) {
				Ext.Ajax.request({
							url : urlPrefix + delComProfession,
							params : {
								professionId : record.get('professionId')
							},
							method : "POST",
							success : function(form, action) {
								// myMask.hide();
								if(record.get('professionId') == form.responseText){
									alert(record.get('professionName')+ '不能删除!');
								}else{
									alert(commonality_messageDelMsg);
								}
							     store.reload();
							},
							failure : function(form, action) {
								alert(commonality_cautionMsg);
							}
						});

			}
		
		    var win = new Ext.Window({
				title : returnPageTitle('专业室管理', 'professionManage'),
		        layout: 'fit',
		        border : false,
		        closable:false,
		        resizable:false,
		        maximized : true,
		        bodyStyle:'padding:0px;',
		        plain:true,
		        buttonAlign:'center',
		        id : 'cnm',
		        items: [grid]
		    });
		    win.show(); 
	});

/* 去前后空格 */
function LTrim(str) {
	// return str.replace(/^\s*/g,"");
	return str.replace(/(^\s*)|(\s*$)/g, "");
}