Ext.namespace('comVendor');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/baseData/vendor/";
	
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();
	
	var store = new Ext.data.Store({
		url : urlPrefix + "loadVendor.do",
		reader : new Ext.data.JsonReader({
			root : "ComVendor",
			fields : [{
				name : 'vendorId'
			}, {
				name : 'vendorCode'
			}, {
				name : 'vendorName'
			}, {
				name : 'remark'
			}]
		})
	});
	store.load();
	
	var colM = new Ext.grid.ColumnModel([
	    {
			header : "vendorId",
			dataIndex : "vendorId",
			hidden : true
		}, {
			header : "供应商编号" + commonality_mustInput,
			dataIndex : "vendorCode",
			width : 150,
			editor : new Ext.form.TextField({
				maxLength : 20,
				maxLengthText : "输入信息长度超过20个字符",
				allowBlank : false
			})
		}, {
			header : "供应商名称" + commonality_mustInput,
			dataIndex : "vendorName",
			width : 300,
			renderer: changeBR,
			editor : new Ext.form.TextArea({
				maxLength : 500,
				maxLengthText : "输入信息长度超过500个字符",
				allowBlank : false,
				grow : true
			})
		},{
			header : "备注",
			dataIndex : 'remark',
			width : 300,
			renderer: changeBR,
			editor : new Ext.form.TextArea({
				grow : true,
				maxLength : 4000,
		    	maxLengthText : "输入信息长度超过4000个字符"
			})
		}
	]);
	
	var grid = new Ext.grid.EditorGridPanel({
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({
				 singleSelect : true
			 }),
		region : 'center',
		store : store,
		loadMask : { msg : commonality_waitMsg },
		clicksToEdit : 2,
		stripeRows : true,
		tbar : [
		    new Ext.Button({
				text : commonality_add,
				iconCls : "icon_gif_add",
				handler : addDate
			}), "-", 
			new Ext.Button({
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
							if (record.get("vendorId") == ''){
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
				text : commonality_save,
				iconCls : "icon_gif_save",
				handler : function() {
					var flag = true;
					if (store.modified.length > 0){
						Ext.each(store.modified, function(item) {
							if (item.data.vendorCode == '' || item.data.vendorCode == null){
								flag = false;
								alert("供应商编号不能为空");
								return false;
							}
							if (item.data.vendorName == '' || item.data.vendorName == null){
								flag = false;
								alert("供应商名称不能为空");
								return false;
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
			}),"-",
			{
				text : commonality_template,
				iconCls : 'icon_gif_template',
				handler : function() {
					document.getElementById("vendorDownloadForm").submit();
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
					document.getElementById("vendorExportForm").submit();
				}
			}
		]
	});
	
	grid.addListener("afteredit", afteredit);
	function afteredit(val){
		if (val.field == 'vendorCode'){
			var length = 0;
			for (var i = 0; i < store.getCount(); i++){
	            var record = store.getAt(i);
	            if (record.get(val.field) == val.value){
	            	length++;
	            }
			}
			if (length > 1){
            	alert("供应商编号" + val.value+ "已存在,请重新填写");
             	val.record.set(val.field, val.originalValue);
             	return ;
            }
		}
	}
	
	function addDate() {
		var index = store.getCount();
		var rec = store.recordType;
		var p = new rec({
			vendorId : '',
			vendorCode : '',
			vendorName : '',
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
				url : urlPrefix + "saveVendor.do",
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
						var message = result.msg;
						
						if (message == 'exits'){
							alert("供应商编号已存在");
							return;
						}	
						alert(commonality_messageSaveMsg);
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
			url : urlPrefix + "saveVendor.do",
			params : {
				vendorId : record.get('vendorId'),
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
			html:'<div style="padding-top:20px;padding-left:8px"><form action="/mpas/baseData/import/importVendor.do" method="post" ENCTYPE="multipart/form-data">'+
				'<input name="excelFile" type="file"/><input style="height:18px;width:50px" value="导入" type="submit" /></form></div>'
		});
		
		winImport.show();
	}
	
	var win = new Ext.Window({
		layout : 'fit',
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : true,
		buttonAlign : 'center',
		title : returnPageTitle("供应商信息管理", 'comVendor'),
		items : [grid]
	});
	win.show();

	//导入报错
	if(msg!='null'){
		Ext.MessageBox.alert("提示",msg);
	}
});
