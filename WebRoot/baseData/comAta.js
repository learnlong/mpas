Ext.namespace('comAta');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/baseData/ata/";
	var nowAtaId = "";
	var ataLevel = 0;
	
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();
	
	//根目录
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : 'ATA',
		id : '0'
	});
	
	// 在菜单栏里加载树
	var loader = new Ext.tree.TreeLoader({
		url : urlPrefix + "loadAtaTree.do"
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
		width : 180,
		height : 400,
		id : 'ataTreePanel',
		header : false,
		loader : loader,
		root : rootNode,
		autoScroll : true,
		// 树的背景色
		bodyStyle : "background-color:#ffe8f6;padding:10 0 10 10",
		region : 'west',
		split : true,
		tbar : [{
			text : commonality_template,
			iconCls : 'icon_gif_template',
			handler : function() {
				document.getElementById("ataDownloadForm").submit();
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
				document.getElementById("ataExportForm").submit();
			}
		}],
		listeners : {
			"click" : function(node) {
				nowAtaId = node.id;
				store.load();
				var c = node.text.substr(node.text.length - 1);
				if (c == 0 || node.text == 'ATA') {
					Ext.getCmp("add").setDisabled(false);
					Ext.getCmp("del").setDisabled(false);
					Ext.getCmp("save").setDisabled(false);					
				} else {
					Ext.getCmp("add").disable();
					Ext.getCmp("del").disable();
					Ext.getCmp("save").disable();
				}
				var treeLevel = node.attributes.ataLevel;
				if (treeLevel == undefined) {
					ataLevel = 1;
				} else {
					ataLevel = treeLevel + 1;
				}
			}
		}
	});
	rootNode.expand();
		
	// ATA列表的数据源
	var store = new Ext.data.Store({
		url : urlPrefix + "loadAta.do",
		reader : new Ext.data.JsonReader({
			totalProperty : "total",
			root : "ComAta",
			fields : [{
				name : 'ataId'
			}, {
				name : 'ataCode'
			}, {
				name : 'ataName'
			},{
				name : 'equipmentName'
			},{
				name : 'equipmentPicNo'
			},{
				name : 'equipmentTypeNo'
			},{
				name : 'equipmentPosition'
			},{
				name : 'remark'
			}]
		}),
		sortInfo : {
			field : 'ataCode',
			direction : 'ASC'
		}
	});

	store.on("beforeload",function(){
		store.baseParams = {
			ataId : nowAtaId,
			start : 0,
			limit : 15
		};
	});
	
	// 定义数据
	var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
	
	var cm = new Ext.grid.ColumnModel( {
		defaults : {
			sortable : true
		},
		columns : [ {
			header : "ATA_ID",
			dataIndex : 'ataId',
			width : 50,
			hidden : true
		}, {
			header : "ATA编号" + commonality_mustInput,
			dataIndex : 'ataCode',
			width : 100,
			editor : new Ext.form.TextField( {
				allowBlank : false,
				maxLength : 11,
				maxLengthText : '输入信息长度超过11个字符!'
			})
		}, {
			header : "ATA名称" + commonality_mustInput,
			width : 150,
			dataIndex : 'ataName',
			renderer: changeBR,
			editor : new Ext.form.TextArea( {
				allowBlank : false,
				grow : true,
				maxLength : 1000,
				maxLengthText : '输入信息长度超过1000个字符!'
			})
		}, {
			header : "产品名称",
			width : 150,
			dataIndex : 'equipmentName',
			renderer: changeBR,
			editor : new Ext.form.TextArea( {
				allowBlank : false,
				grow : true,
				maxLength : 1000,
				maxLengthText : '输入信息长度超过1000个字符!'
			})
		}, {
			header : "产品图号",
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
			header : "产品名称型号",
			width : 150,
			dataIndex : 'equipmentTypeNo',
			renderer: changeBR,
			editor : new Ext.form.TextArea( {
				allowBlank : false,
				grow : true,
				maxLength : 1000,
				maxLengthText : '输入信息长度超过1000个字符!'
			})
		}, {
			header : "安装位置",
			width : 150,
			dataIndex : 'equipmentPosition',
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
		clicksToEdit : 2,
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
				id : 'del',
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
							if (record.get("ataId") == ''){
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
				id : 'save',
				text : commonality_save,
				iconCls : "icon_gif_save",
				handler : function() {
				var flag = true;
					if (store.modified.length > 0){
						Ext.each(store.modified, function(item) {
							var ataCode = item.data.ataCode;
							if (ataCode == ''|| ataCode == null){
								flag = false;
								alert("ATA编号不能为空");
								return false;
							}
							if (item.data.ataName == '' || item.data.ataName == null){
								flag = false;
								alert("ATA名称不能为空");
								return false;
							}
							//先验证XX-XX-XX-XX格式
							var allre=/^\d{2}-\d{2}-\d{2}-\d{2}$/;
							if (allre.test(ataCode) == false) {
								alert('ATA编号必须是XX-XX-XX-XX格式，并且XX必须是数字!');
								flag = false;
								return;
							}
							if (ataLevel == 1) {
								if (ataCode.substr(2, 9) != "-00-00-00") {
									alert('ATA编号不正确：ATA一级的最后6位数字均为0!');
									flag = false;
									return;
								}								
							} 
							/**else if (ataLevel == 2) {
								if (ataCode.substr(0, 2) != treePanel.getSelectionModel().getSelectedNode().attributes.text.substr(0, 2)
										|| ataCode.substr(3, 2) <= 0 || ataCode.substr(5, 6) != "-00-00") {
									alert("ATA编号不正确：ATA二级的前2位数字必须与上级ATA的前2位一致，第3位和第4位数字必须有一位大于0，最后4位数必须均为0!");
									flag = false;
									return;
								}
							} else if (ataLevel == 3) {
								if (ataCode.substr(0, 5) != treePanel.getSelectionModel().getSelectedNode().attributes.text.substr(0, 5)
										|| ataCode.substr(6, 2) <= 0 || ataCode.substr(8, 6) != "-00") {
									alert("ATA编号不正确：ATA三级的前4位数字必须与上级ATA的前4位一致，第5位和第6位数字必须有一位大于0，并且最后2位数字均为0!");
									flag = false;
									return;
								}
							} else if (ataLevel == 4) {
								if (ataCode.substr(0, 8) != treePanel.getSelectionModel().getSelectedNode().attributes.text.substr(0, 8)
										|| ataCode.substr(9, 2) <= 0) {
									alert("ATA编号不正确：ATA四级的前6位数字必须与上级ATA的前6位数字一致，第7位和第8位数字必须有一位大于0!");
									flag = false;
									return;
								}
							}*/
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
			})
		]
	});
	
	function addDate(){
		var index = store.getCount();
		var rec = store.recordType;
		var p = new rec( {
			ataId : '',
			ataCode : '',
			ataName : '',
			equipmentName : '',
			equipmentPicNo : '',
			equipmentTypeNo : '',
			equipmentPosition : '',
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
				url : urlPrefix + "saveAta.do",
				params : {
					jsonData : Ext.util.JSON.encode(json),
					parentId : nowAtaId,
					method : 'update'
				},
				async : false, // 是否为异步请求
				method : "POST",
				waitMsg : commonality_waitMsg,
				success : function(response) {
					myMask.hide();
					if (response.responseText !== null && response.responseText !== '') {
						var result = Ext.util.JSON.decode(response.responseText);
						/**var message = result.msg;
						
						if (message == 'exits'){
							alert("ATA编号已存在");
							return;
						}	
						alert(commonality_messageSaveMsg);*/
						alert(result.msg);
						refreshTreeNode();
						store.reload();
						store.modified = [];
					}
				},
				failure : function(response) {
					myMask.hide();
					store.modified = [];
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
			url : urlPrefix + "saveAta.do",
			params : {
				ataId : record.get('ataId'),
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
					
					alert(commonality_messageDelMsg);
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
	
	var win = new Ext.Window({
		layout : 'border',
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : false,
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		title : returnPageTitle("ATA管理", 'ata'),
		items : [{
			region : 'west',
			layout : 'fit',
			width : 200,
			split : true,
			items : [treePanel]
		}, {
			id : "grid",
			region : 'center',
			layout : 'fit',
			split : true,
			items : [grid]
		}]
	});
	win.show();

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
			html:'<div style="padding-top:20px;padding-left:8px"><form action="/mpas/baseData/import/importAta.do" method="post" ENCTYPE="multipart/form-data">'+
				'<input name="excelFile" type="file"/><input style="height:18px;width:50px" value="导入" type="submit"/></form></div>'
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
});
