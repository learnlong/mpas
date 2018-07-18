Ext.namespace('comMmel');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/baseData/mmel/";
	
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();
	
	var store = new Ext.data.Store({
		url : urlPrefix + "loadMmel.do",
		reader : new Ext.data.JsonReader({
			root : "ComMmel",
			fields : [{
				name : 'mmelId'
			}, {
				name : 'mmelCode'
			}, {
				name : 'mmelName'
			}, {
				name : 'remark'
			}]
		})
	});
	store.load();
	
	var colM = new Ext.grid.ColumnModel([
	    {
			header : "mmelId",
			dataIndex : "mmelId",
			hidden : true
		}, {
			header : "MMEL编号" + commonality_mustInput,
			dataIndex : "mmelCode",
			width : 150,
			editor : new Ext.form.TextField({
				maxLength : 50,
				maxLengthText : "输入信息长度超过50个字符",
				allowBlank : false
			})
		}, {
			header : "MMEL名称" + commonality_mustInput,
			dataIndex : "mmelName",
			width : 300,
			renderer: changeBR,
			editor : new Ext.form.TextArea({
				maxLength : 200,
				maxLengthText : "输入信息长度超过200个字符",
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
							if (record.get("mmelId") == ''){
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
							if (item.data.mmelCode == '' || item.data.mmelCode == null){
								flag = false;
								alert("MMEL编号不能为空");
								return false;
							}
							if (item.data.mmelName == '' || item.data.mmelName == null){
								flag = false;
								alert("MMEL名称不能为空");
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
			})
		]
	});
	
	grid.addListener("afteredit", afteredit);
	function afteredit(val){
		if (val.field == 'mmelCode'){
			var length = 0;
			for (var i = 0; i < store.getCount(); i++){
	            var record = store.getAt(i);
	            if (record.get(val.field) == val.value){
	            	length++;
	            }
			}
			if (length > 1){
            	alert("mmel编号" + val.value+ "已存在,请重新填写");
             	val.record.set(val.field, val.originalValue);
             	return ;
            }
		}
	}
	
	function addDate() {
		var index = store.getCount();
		var rec = store.recordType;
		var p = new rec({
			mmelId : '',
			mmelCode : '',
			mmelName : '',
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
				url : urlPrefix + "saveMmel.do",
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
							alert("MMEL编号已存在");
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
			url : urlPrefix + "saveMmel.do",
			params : {
				mmelId : record.get('mmelId'),
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
	
	var win = new Ext.Window({
		layout : 'fit',
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : true,
		buttonAlign : 'center',
		title : returnPageTitle("MMEL管理", 'comMmel'),
		items : [grid]
	});
	win.show();
});
