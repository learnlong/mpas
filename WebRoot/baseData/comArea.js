Ext.namespace('comArea');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/baseData/area/";
	var nowAreaId = "";
	
	// 检查必输项长度错误提示信息
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();

	var rootNode = new Ext.tree.AsyncTreeNode({
		text : "区域",
		id : '0',
		areaLevel : 0
	});
	
	var loader = new Ext.tree.TreeLoader({
		dataUrl : urlPrefix + "loadAreaTree.do"
	});
	
	loader.on('beforeload', function(loader, node) {
		node.on('append', function(tree, thiz, newNode, index) {
			if (newNode.isLeaf()) {
				newNode.on('click', function(thiz, e) { });
			}
		});
		if (!node.isLeaf()) {
			loader.baseParams['treeId'] = node.id;
		}
	});
	
	var treePanel = new Ext.tree.TreePanel({
		width : 200,
		height : 400,
		id : 'areaTreePanel',
		header : false,
		loader : loader,
		root : rootNode,
		autoScroll : true,
		region : 'west',
		split : true,
		tbar:[{
			text : commonality_template,
			iconCls : 'icon_gif_template',
			handler : function() {
				document.getElementById("zoneDownloadForm").submit();
			}
		},"-",{
			text : commonality_import,
			iconCls : 'icon_gif_import',
			handler : function() {
				doImport();
			}
		},"-",{
			text : commonality_export,
			iconCls : 'icon_gif_export',
			handler : function() {
				document.getElementById("zoneExportForm").submit();
			}
		}],
		listeners : {
			"click" : function(node) {
				nowAreaId = node.id;
				store.load();

				// 获取当前树的深度(检查是否是第三级节点 如果是就把按钮屏蔽)
				var deep = node.getDepth();
				if (deep == 3) {
					Ext.getCmp("add").disable();
					Ext.getCmp("del").disable();
					Ext.getCmp("save").disable();
					Ext.getCmp("equip").disable();
				} else {
					Ext.getCmp("add").setDisabled(false);
					Ext.getCmp("del").setDisabled(false);
					Ext.getCmp("save").setDisabled(false);
					Ext.getCmp("equip").setDisabled(false);
				}
			}
		}
	});
	rootNode.expand();
	
	var store = new Ext.data.Store({
		url : urlPrefix + "loadArea.do",
		reader : new Ext.data.JsonReader({
			totalProperty : "total",
			root : "ComArea",
			fields : [{
				name : 'areaId'
			}, {
				name : 'areaCode'
			}, {
				name : 'areaName'
			}, {
				name : 'reachWay'
			}, {
				name : 'equipmentName'
			},{
				name : 'equipmentPicNo'
			},{
				name : 'equipmentTypeNo'
			},{
				name : 'wirePiping'
			}, {
				name : 'remark'
			}]
		}),
		sortInfo : {
			field : 'areaCode',
			direction : 'ASC'
		}
	});

	store.on("beforeload",function(){
		store.baseParams = {
			areaId : nowAreaId,
			start : 0,
			limit : 15
		}
	});
	
	// 定义数据
	var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
	
	var cm = new Ext.grid.ColumnModel( {
		defaults : {
			sortable : true
		},
		columns : [ {
			header : "AREA_ID",
			dataIndex : 'areaId',
			width : 50,
			hidden : true
		}, {
			header : "区域编号" + commonality_mustInput,
			dataIndex : 'areaCode',
			width : 90,
			editor : new Ext.form.TextField( {
				allowBlank : false,
				maxLength : 3,
				maxLengthText : '输入信息长度超过3个字符!'
			})
		}, {
			header : "区域名称" + commonality_mustInput,
			width : 150,
			dataIndex : 'areaName',
			renderer: changeBR,
			editor : new Ext.form.TextArea( {
				allowBlank : false,
				grow : true,
				maxLength : 1000,
				maxLengthText : '输入信息长度超过1000个字符!'
			})
		}, {
			header : "口盖",
			width : 150,
			dataIndex : 'reachWay',
			renderer: changeBR,
			editor : new Ext.form.TextArea( {
				allowBlank : false,
				grow : true,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			})
		}, {
			header : "设备名称",
			width : 150,
			dataIndex : 'equipmentName',
			renderer: changeBR
		}, {
			header : "设备图号",
			width : 150,
			dataIndex : 'equipmentPicNo',
			renderer: changeBR
		}, {
			header : "设备型号",
			width : 150,
			dataIndex : 'equipmentTypeNo',
			renderer: changeBR
		}, {
			header : "含何种电缆电线、何种管路",
			width : 150,
			dataIndex : 'wirePiping',
			renderer: changeBR,
			editor : new Ext.form.TextArea( {
				allowBlank : false,
				grow : true,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			})
		}, {
			header : "备注",
			width : 150,
			dataIndex : 'remark',
			renderer: changeBR,
			editor : new Ext.form.TextArea( {
				allowBlank : false,
				grow : true,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			})
		}]
	});	
	
	var grid = new Ext.grid.EditorGridPanel({
		cm : cm,
		sm : sm,
		region : 'center',
		store : store,
		clicksToEdit : 1,
		stripeRows : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 15,
			store : store,
			displayInfo : true,
			displayMsg : commonality_turnPage,
			emptyMsg : commonality_noRecords
		}),
		tbar : [
		    new Ext.Button({
				id : "add",
				text : commonality_add,
				iconCls : "icon_gif_add",
				handler : function() {
		    		addDate();
				}
			}), "-", 
			new Ext.Button({
				id : "del",
				text : commonality_del,
				iconCls : 'icon_gif_delete',
				handler : function() {
					var record = grid.getSelectionModel().getSelected();
					if (record === undefined || record === null){
						alert(commonality_alertDel);
						return;
					}
					Ext.Msg.confirm(commonality_affirm, commonality_affirmDelMsg, function(btn){
						if (btn === 'yes'){
							if (record.get("areaId") == ''){
								store.remove(record);
								store.modified.remove(record);
							} else {
								delDate(record);
							}
						}
					});
				}
			}), "-", 
			new Ext.Button({
				id : "save",
				text : commonality_save,
				iconCls : "icon_gif_save",
				handler : function() {
					var flag = true;
					if (store.modified.length > 0){
						Ext.each(store.modified, function(item) {
							var thisCode = item.data.areaCode;
							var upCode = treePanel.getSelectionModel().getSelectedNode().attributes.areaCode;
							var areaLevel = treePanel.getSelectionModel().getSelectedNode().attributes.areaLevel + 1;
							
							if (thisCode == '' || thisCode == null){
								flag = false;
								alert("区域编号不能为空");
								return false;
							}
							if (item.data.areaName == '' || item.data.areaName == null){
								flag = false;
								alert("区域名称不能为空");
								return false;
							}
							
							if (thisCode.length != 3) {
								alert("区域编号位数不正确，请重新输入三位数字");
								flag = false;
								return;
							} else {
								var rec = /^[1-9]*$/;
								switch (areaLevel) {
									case 1 :// 一级区域时
										// 第一位是1-9的数字，区域编号后两位必须是00
										if (rec.test(thisCode.substr(0, 1)) == false
												|| thisCode.substr(1, 2) != '00') {
											alert("区域编号错误,正确的格式第一位是1-9数字后两位必须是00");
											flag = false;
											return;
										}
										break;
									case 2 :// 二级区域时
										// 区域编号第一位必须和所属一级区域编号的第一位保持一致,第二位是1-9的数字，第三位必须是0
										if (thisCode.substr(0, 1) != upCode.substr(0, 1)
												|| rec.test(thisCode.substr(1, 1)) == false
												|| thisCode.substr(2, 1) != '0') {
											alert("区域编号错误,第一位必须和所属上级区域编号的第一位保持一致,第二位是1-9的数字，第三位必须是0");
											flag = false;
											return;
										}
										break;
									case 3 :// 三级区域时
										// 区域编号前两位必须和所属二级区域编号的前两位保持一致，第三位是1-9的数字
										if (rec.test(thisCode.substr(2, 1)) == false
												|| thisCode.substr(0, 2) != upCode.substr(0, 2)) {
											alert("区域编号错误,前两位必须和所属上级区域编号的前两位保持一致，第三位是1-9的数字");
											flag = false;
											return;
										}
										break;
								}
							}
						});
						
						if (flag){
							Ext.Msg.confirm(commonality_affirm, commonality_affirmSaveMsg, function(btn) {
								if (btn === 'yes') {
									saveDate();
								}
							});
						}
					} else {
						alert(commonality_alertUpdate);
					}
				}
			}), "-", 
			new Ext.Button({
				id : "equip",
				text : "修改设备",
				iconCls : "icon_gif_update",
				handler : function() {
					var record = grid.getSelectionModel().getSelected();
					if (record === undefined || record === null){
						alert("请选择需要修改设备的区域");
						return;
					}
					
					if (record.get('areaId') == '' || record.get('areaId') == null){
						alert("请先保存区域，再修改区域设备信息");
						return;
					}
					equipUpdate(record);
				}
			})
		]
	});
	
	var win = new Ext.Window({
		layout : 'border',
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : false,
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		title : returnPageTitle("区域管理", 'area'),
		items : [{
			region : 'west',
			layout : 'fit',
			width : 200,
			split : true,
			items : [treePanel]
		}, {
			region : 'center',
			id : 'grid',
			layout : 'fit',
			split : true,
			items : [grid]
		}]
	});
	win.show();
	
	function addDate(){
		var index = store.getCount();
		var rec = store.recordType;
		var p = new rec( {
			areaId : '',
			areaCode : '',
			areaName : '',
			reachWay : '',
			equipmentName : '',
			equipmentPicNo : '',
			equipmentTypeNo : '',
			wirePiping : '',
			remark : ''
		});
		store.insert(index, p);
	}
	
	function saveDate(){
		var json = [];
		Ext.each(store.modified, function(item) {
			json.push(item.data);
		});
		
		if (json.length > 0) {
			var myMask = new Ext.LoadMask(Ext.getBody(), {
				msg : commonality_waitMsg,
				removeMask : true
			});
			myMask.show();
			
			Ext.Ajax.request({
				url : urlPrefix + "saveArea.do",
				params : {
					jsonData : Ext.util.JSON.encode(json),
					parentId : nowAreaId,
					method : 'update'
				},
				async : false, // 是否为异步请求
				method : "POST",
				waitMsg : commonality_waitMsg,
				success : function(response) {
					myMask.hide();
					if (response.responseText !== null && response.responseText !== '') {
						var result = Ext.util.JSON.decode(response.responseText);
						var message = result.msg;
						
						if (message == 'exits'){
							alert("区域编号已存在");
							return;
						}	
						alert(commonality_messageSaveMsg);
						refreshTreeNode();
						store.reload();
						store.modified = [];
					}
				},
				failure : function(response) {
					myMask.hide();
					alert(commonality_saveMsg_fail);
				}
			});
		} else {
			alert(commonality_alertUpdate);
		}
	}
	
	function delDate(record){
		var json = [];
		json.push(record);
		
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:commonality_waitMsg, removeMask: true});
		myMask.show();
		
		Ext.Ajax.request( {
			url : urlPrefix + "saveArea.do",
			params : {
				areaId : record.get('areaId'),
				method : 'delete'
			},
			method : "POST",
			waitMsg : commonality_waitMsg,
			success : function(response) {
				myMask.hide();
				if (response.responseText !== null && response.responseText !== ''){
					var result = Ext.util.JSON.decode(response.responseText);
					var message = result.msg;
					if (message == "false"){
					    alert("删除失败!");
						return;
					}
					
					alert(commonality_messageDelMsg)
					refreshTreeNode();
					store.reload();
				} else {
					alert(commonality_messageDelMsg);
					store.reload();
				}
			},
			failure : function(response) {
				myMask.hide();
				alert(commonality_messageDelMsgFail);
			}
		});
	}
	
	//修改设备的弹出框
	function equipUpdate(nowRecord){
		var storeEquip = new Ext.data.Store({
			url : urlPrefix + "loadEquip.do?areaId=" + nowRecord.get('areaId'),
			reader : new Ext.data.JsonReader({
				totalProperty : "total",
				root : "equip",
				fields : [{
					name : 'detailId'
				}, {
					name : 'equipmentName'
				}, {
					name : 'equipmentPicNo'
				}, {
					name : 'equipmentTypeNo'
				}]
			})
		});
		storeEquip.load();
		
		var smEquip = new Ext.grid.RowSelectionModel({singleSelect:true});
		
		var cmEquip = new Ext.grid.ColumnModel({
			defaults : {
				sortable : true
			},
			columns : [ {
				header : "detailId",
				dataIndex : 'detailId',
				width : 50,
				hidden : true
			}, {
				header : "设备名称" + commonality_mustInput,
				dataIndex : 'equipmentName',
				width : 150,
				renderer: changeBR,
				editor : new Ext.form.TextArea( {
					allowBlank : false,
					grow : true,
					maxLength : 1000,
					maxLengthText : '输入信息长度超过1000个字符!'
				})
			}, {
				header : "设备图号",
				width : 150,
				dataIndex : 'equipmentPicNo',
				renderer: changeBR,
				editor : new Ext.form.TextArea( {
					allowBlank : false,
					grow : true,
					maxLength : 1000,
					maxLengthText : '输入信息长度超过1000个字符!'
				})
			}, {
				header : "设备型号",
				width : 150,
				dataIndex : 'equipmentTypeNo',
				renderer: changeBR,
				editor : new Ext.form.TextArea( {
					allowBlank : false,
					grow : true,
					maxLength : 4000,
					maxLengthText : '输入信息长度超过4000个字符!'
				})
			}]
		});	
		
		var gridEquip = new Ext.grid.EditorGridPanel({
			cm : cmEquip,
			sm : smEquip,
			region : 'center',
			store : storeEquip,
			clicksToEdit : 1,
			stripeRows : true,
			tbar : [
			    new Ext.Button({
					id : "addEquip",
					text : commonality_add,
					iconCls : "icon_gif_add",
					handler : function() {
			    		addEquip();
					}
				}), "-", 
				new Ext.Button({
					id : "delEquip",
					text : commonality_del,
					iconCls : 'icon_gif_delete',
					handler : function() {
						var record = gridEquip.getSelectionModel().getSelected();
						if (record === undefined || record === null){
							alert(commonality_alertDel);
							return;
						}
						Ext.Msg.confirm(commonality_affirm, commonality_affirmDelMsg, function(btn){
							if (btn === 'yes'){
								if (record.get("detailId") == ''){
									storeEquip.remove(record);
									storeEquip.modified.remove(record);
								} else {
									delEquip(record);
								}
							}
						});
					}
				}), "-", 
				new Ext.Button({
					id : "saveEquip",
					text : commonality_save,
					iconCls : "icon_gif_save",
					handler : function() {
						var flag = true;
						if (storeEquip.modified.length > 0){
							Ext.each(storeEquip.modified, function(item) {
								if (item.data.equipmentName == '' || item.data.equipmentName == null){
									flag = false;
									alert("设备名称不能为空");
									return false;
								}
								
								if (flag){
									Ext.Msg.confirm(commonality_affirm, commonality_affirmSaveMsg, function(btn) {
										if (btn === 'yes') {
											saveEquip();
										}
									});
								}
							})
						} else {
							alert(commonality_alertUpdate);
						}
					}
				})
			]
		});
		
		var winEquip = new Ext.Window({
			layout : 'fit',
			width : 490,
			height : 400,
			resizable : true,
			draggable : true,
			closable : true,
			title : "修改设备",
			modal : true,
			titleCollapse : true,
			align : 'center',
			buttonAlign : 'center',
			constrain : true,
			items : [gridEquip],
			buttons : [{
				text : "关闭",
				handler : function() {
					winEquip.destroy();
				}
			}]
		});
		winEquip.show();
		
		function addEquip(){
			var index = storeEquip.getCount();
			var rec = storeEquip.recordType;
			var p = new rec( {
				detailId : '',
				equipmentName : '',
				equipmentPicNo : '',
				equipmentTypeNo : ''
			});
			storeEquip.insert(index, p);
		}
		
		function saveEquip(){
			var json = [];
			Ext.each(storeEquip.modified, function(item) {
				json.push(item.data);
			});
			
			if (json.length > 0) {
				var myMask = new Ext.LoadMask(Ext.getBody(), {
					msg : commonality_waitMsg,
					removeMask : true
				});
				myMask.show();
				
				Ext.Ajax.request({
					url : urlPrefix + "saveEquip.do",
					params : {
						jsonData : Ext.util.JSON.encode(json),
						areaId : nowRecord.get('areaId'),
						method : 'update'
					},
					async : false, // 是否为异步请求
					method : "POST",
					waitMsg : commonality_waitMsg,
					success : function(response) {
						myMask.hide();
						if (response.responseText !== null && response.responseText !== '') {
							var result = Ext.util.JSON.decode(response.responseText);
							var message = result.msg;
							
							if (message == 'exits'){
								alert("区域编号已存在");
								return;
							}	
							alert(commonality_messageSaveMsg);
							storeEquip.reload();
							storeEquip.modified = [];
							store.reload();
						}
					},
					failure : function(response) {
						myMask.hide();
						alert(commonality_saveMsg_fail);
					}
				});
			} else {
				alert(commonality_alertUpdate);
			}
		}
		
		function delEquip(record){
			var json = [];
			json.push(record);
			
			var myMask = new Ext.LoadMask(Ext.getBody(), {msg:commonality_waitMsg, removeMask: true});
			myMask.show();
			
			Ext.Ajax.request( {
				url : urlPrefix + "saveEquip.do",
				params : {
					detailId : record.get('detailId'),
					method : 'delete'
				},
				method : "POST",
				waitMsg : commonality_waitMsg,
				success : function(response) {
					myMask.hide();
					if (response.responseText !== null && response.responseText !== ''){
						var result = Ext.util.JSON.decode(response.responseText);
						var message = result.msg;
						if (message == "false"){
						    alert("删除失败!");
							return;
						}
						
						alert(commonality_messageDelMsg)
						storeEquip.reload();
						store.reload();
					} else {
						alert(commonality_messageDelMsg);
						storeEquip.reload();
					}
				},
				failure : function(response) {
					myMask.hide();
					alert(commonality_messageDelMsgFail);
				}
			});
		}
	}

	// 执行批量导入
	function doImport() {
		//alert("批量导入");
		var winImport=new Ext.Window({
			border : false,
			resizable : true,
			closable : true,
			title:commonality_importTitle,
			plain : false,
			layout:'fit',
			height:100,
			width:300,
			modal:true,
			bodyStyle : 'padding:0px;',
			buttonAlign : 'center',
			html:'<div style="padding-top:20px;padding-left:8px"><form action="/mpas/baseData/import/importZone.do" method="post" ENCTYPE="multipart/form-data">'+
				'<input name="excelFile" type="file"/><input style="height:18px;width:50px" value="导入" type="submit" /></form></div>'
		});
		
		winImport.show();
	}

	//导入报错
	if(msg!='null'){
		Ext.MessageBox.alert("提示",msg);
	}
	
	Ext.getCmp("add").disable();
	Ext.getCmp("del").disable();
	Ext.getCmp("save").disable();
	Ext.getCmp("equip").disable();
});