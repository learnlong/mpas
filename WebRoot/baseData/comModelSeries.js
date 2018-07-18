Ext.namespace('comModelSeries');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/com/modelSeries/";
	var currentRowIndex = -1;
	var currentRecord;
		
	Ext.form.Field.prototype.msgTarget = 'qtip';     //用来显示验证信息 
	Ext.QuickTips.init();
	
	var store = new Ext.data.Store( {
		proxy : new Ext.data.HttpProxy( {
			url : urlPrefix + "loadModelSeriesList.do"
		}),
		reader : new Ext.data.JsonReader( {
			root : 'ComModelSeries',
			fields : [ {
				name : 'modelSeriesId',
				type : 'string'
			}, {
				name : 'modelSeriesCode',
				type : 'string'
			}, {
				name : 'modelSeriesName',
				type : 'string'
			}, {
				name : 'defaultModelSeries',
				type : 'string'
			} ]
		}),
		sortInfo : {
			field : 'modelSeriesCode',
			direction : 'ASC'
		}
	});
	store.load({
		params : {
			start : 0,
			limit : 18
		} 
	});
	
	var sm = new Ext.grid.RowSelectionModel({singleSelect:true});
	var cm = new Ext.grid.ColumnModel( {
		defaults : {
			sortable : true
		},
		columns : [ {
			header : "机型系列ID",
			dataIndex : 'modelSeriesId',
			width : 50,
			hidden : true
		}, {
			header : "机型系列编号" + commonality_mustInput,
			dataIndex : 'modelSeriesCode',
			width : 150,
			editor : new Ext.form.TextField( {
				allowBlank : false,
				maxLength : 50,
				maxLengthText : '输入信息长度超过50个字符!'
			})
		}, {
			header : "机型系列名称" + commonality_mustInput,
			width : 150,
			dataIndex : 'modelSeriesName',
			renderer: changeBR,
			editor : new Ext.form.TextField( {
				allowBlank : false,
				maxLength : 100,
				maxLengthText : '输入信息长度超过100个字符!'
			})
		}, {
			header : "默认机型",
			width : 100,
			dataIndex : 'defaultModelSeries'
		}]
	});
	
	var grid = new Ext.grid.EditorGridPanel( {
		store : store,
		cm : cm,
		sm : sm,
		header : false,
		id : 'grid',
		renderTo : 'modelSeriesGrid',
		split : true,
		stripeRows : true,
		collapsible : true,
		frame : true,
		clicksToEdit : 2,
		bbar: new Ext.PagingToolbar({ 
			pageSize : 18, 
			store : store, 
			displayInfo : true, 
			displayMsg : commonality_turnPage, 
			emptyMsg : commonality_noRecords }),
		tbar : new Ext.Toolbar({
			items : [
			         '机型系列编号：', 
			         {
			        	 xtype : 'textfield',
			        	 id : 'msCode',
			        	 width : 100
			         },
			         new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
			         '机型系列名称：',
			         {
			        	 xtype :'textfield',
			        	 id : 'msName',
			        	 width : 100
			         },
			         new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
			         {
			        	 text : commonality_search,
			        	 iconCls : "icon_gif_search",
			        	 handler : function() {
			        	 	store.load({
			        	 		params : {
			        	 			start : 0,
			        	 			limit : 18,
			        	 			msCode : Ext.get('msCode').dom.value,
			        	 			msName : Ext.get('msName').dom.value
			        	 		}
			        	 	});
			         	 }
　　　　　　　　　          }, '-',
                   	 {
　　　　　　　　　        	  	 text : commonality_add,
                 	  	 iconCls : "icon_gif_add",
                 	  	 handler : function() {
　　　　　　　　　        	  		var index = store.getCount();
							var rec = store.recordType;
							var p = new rec( {
								modelSeriesId : '',
								modelSeriesCode : '',
								modelSeriesName : ''
							});
							store.insert(index, p);
						}
					},'-',
					{
						text : commonality_del,
						iconCls : "icon_gif_delete",
						waitMsg : commonality_waitMsg,
						handler : function() {
							var record = grid.getSelectionModel().getSelected();
							if (record === undefined || record === null){
								alert(commonality_alertDel);
								return;
							}
							Ext.Msg.confirm(commonality_affirm, commonality_affirmDelMsg, function(btn){
								if (btn === 'yes'){
									if (record.get("modelSeriesId") == ''){
										store.remove(record);
										store.modified.remove(record);
									} else {
										deleteData(record);
									}
								}
							});
						}
					},'-',
					{
						text : commonality_save,
						iconCls : "icon_gif_save",
						waitMsg : commonality_waitMsg,
						handler : function() {
							var flag = true;
							if (store.modified.length > 0){
								Ext.each(store.modified, function(item) {									
									if (item.data.modelSeriesCode == ''||item.data.modelSeriesCode == null){
										flag = false;
										alert("机型系列编号不能为空");
										return false;
									}
									if (item.data.modelSeriesName == ''||item.data.modelSeriesName == null){
										flag = false;
										alert("机型系列名称不能为空");
										return false;
									}
								});
								
								if (flag){
									  Ext.Msg.buttonText.yes='保存并复制';
   									  Ext.Msg.buttonText.no="保存";
   									  Ext.Msg.buttonText.cancel="取消";
									  Ext.MessageBox.show({
									        title: "提示",
									        msg: "请选择保存时是否需要复制</br>默认自定义参数到新添加的机型？",
									        buttons: Ext.MessageBox.YESNOCANCEL,
									        icon: Ext.MessageBox.QUESTION,
									        fn:function(btn){
									            if(btn=="yes"){
									                updateData("yes");
									            }else if(btn=="no"){
									            	updateData("no");
									            }
									        }
									    })
								/*	Ext.Msg.confirm(commonality_affirm, commonality_affirmSaveMsg, function(btn) {
										if (btn === 'yes') {
											updateData();
										}
									});*/
								      Ext.Msg.buttonText.yes='是';
   									  Ext.Msg.buttonText.no="否";
   									  Ext.Msg.buttonText.cancel="取消";
								}
							} else {
								alert(commonality_alertUpdate);
							}								
						}
					},'-',
					{
						text : "确定默认机型",
						iconCls : "icon_gif_view",
						waitMsg : commonality_waitMsg,
						handler : function() {
							var record = grid.getSelectionModel().getSelected();
							if (record === undefined || record === null){
								alert("请选择要默认的机型");
								return;
							}
							Ext.Msg.confirm(commonality_affirm, "确认要默认该机型吗？", function(btn){
								if (btn === 'yes'){
									defaultMs(record);
								}
							});
						}
					}
		]})
	});
	
	grid.render();	
	grid.addListener("cellclick", cellclick);	
	grid.addListener("afteredit", afteredit);
	
	function cellclick(grid, rowIndex, columnIndex, e) {
		if (currentRowIndex != rowIndex){
			currentRowIndex = rowIndex;
			currentRecord = store.getAt(rowIndex);
		}
	}
	
	function afteredit(val){
		var count = 0;
		if (val.field == 'modelSeriesCode'){
			var nowRecord = val.record;
			var items = store.data.items;
			for (var i = 0; i < items.length; i++){
			 	if (items[i].get('modelSeriesCode') == val.value){
			 		count++;
			 	}
		 	}
			
			if (count == 2){
		 		alert("该机型编号已存在，请更改 !");
				nowRecord.set('modelSeriesCode', val.originalValue);
				return false;
			}
		 
			Ext.Ajax.request({
				url : urlPrefix + "checkModelSeries.do",
				params : {
					msCode : val.value,
					msId : nowRecord.get("modelSeriesId")
				},
				success : function(response) {
					if (response.responseText == "false"){
						alert("该机型编号已存在，请更改 !");
						nowRecord.set('modelSeries', val.originalValue);
					}	
				}
			})
		}
	}
	
	function defaultMs(record){
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:commonality_waitMsg, removeMask: true});
		myMask.show();
		
		Ext.Ajax.request( {
			url : urlPrefix + "defaultModelSeries.do",
			params : {
				msId : record.get('modelSeriesId')
			},
			method : "POST",
			waitMsg : commonality_waitMsg,
			success : function(response) {
				myMask.hide();
				if (response.responseText !== null && response.responseText !== ''){
					var result = Ext.util.JSON.decode(response.responseText);
					var message = result.msg;
					if (message == "false"){
					    alert("默认机型失败!");
						return;
					}
					
					alert(commonality_messageSaveMsg)
					store.reload();
				} else {
					alert(commonality_messageSaveMsg);
					store.reload();
				}
			},
			failure : function(response) {
				myMask.hide();
				alert("默认机型失败!");
			}
		});
	}
			
	//删除记录
	function deleteData(record) {
		var json = [];
		json.push(record);
		
		var myMask = new Ext.LoadMask(Ext.getBody(), {msg:commonality_waitMsg, removeMask: true});
		myMask.show();
		
		Ext.Ajax.request( {
			url : urlPrefix + "updateModelSeries.do",
			params : {
				msId : record.get('modelSeriesId'),
				method : 'delete'
			},
			method : "POST",
			waitMsg : commonality_waitMsg,
			success : function(response) {
				myMask.hide();
				if (response.responseText !== null && response.responseText !== ''){
					var result = Ext.util.JSON.decode(response.responseText);
					var message = result.msg;
					if (message == "nowMs"){
					    alert("该机型系列信息正在使用，不能删除!");
						return;
					}
					if (message == "defaultMs"){
					    alert("该机型系列是默认机型，不能删除!");
						return;
					}
					if (message == "false"){
					    alert("删除失败!");
						return;
					}
					
					alert(commonality_messageDelMsg)
					store.reload();
					var msList = result.ms;
					if(msList){//更新下拉框的显示数据
							parent.document.getElementById('selectId').options.length=0;
							for(var i=0;i<msList.length;i++){
								addItemmonth(msList[i].msId,msList[i].msCode);
							}
						}
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
	 
	// 修改与新增保存
	function updateData(val) {
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
				url : urlPrefix + "updateModelSeries.do",
				params : {
					jsonData : Ext.util.JSON.encode(json),
					method : 'update',
					needCopy : val
				},
				async : false, // 是否为异步请求
				method : "POST",
				waitMsg : commonality_waitMsg,
				success : function(response) {
					myMask.hide();
					if (response.responseText !== null && response.responseText !== '') {
						var result = Ext.util.JSON.decode(response.responseText);
						var message = result.msg;
						if (message == "nowMs"){
							//机型正在使用
						    alert("该机型系列信息正在使用，不能修改!");
						    store.reload();
							store.modified = [];
							return;
						}
						if (message == 'exits'){
							//机型编号重复
							alert("机型系列编号已存在,请确认");
							return;
						}	
						alert(commonality_messageSaveMsg);
						store.reload();
						store.modified = [];
						var msList = result.ms;
						if(msList){//更新下拉框的显示数据
							parent.document.getElementById('selectId').options.length=0;
							for(var i=0;i<msList.length;i++){
								addItemmonth(msList[i].msId,msList[i].msCode);
							}
						}
					}
				},
				failure : function(response) {
					myMask.hide();
					alert("机型系列编号已存在,请确认");
				}
			});
		} else {
			alert(commonality_alertUpdate);
		}
	}

	var window = new Ext.Window({
		layout: 'fit',
		border : false, 
		resizable : true,
		closable : false,
		maximized : true,
		buttonAlign : 'center',
		title : returnPageTitle("机型管理",'comModelSeries'),
		plain : true,
		items : [grid]
	});
    window.show();
});
