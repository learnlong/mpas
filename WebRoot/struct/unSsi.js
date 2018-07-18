Ext.onReady(function (){
	Ext.QuickTips.init(); 
	Ext.form.Field.prototype.msgTarget='qtip';//快速提示方式
	
	var store = new Ext.data.Store({
		url :  contextPath + "/struct/unSsi/getUnSsiList.do?ssiId=" + ssiId,
		method : "post",
		reader : new Ext.data.JsonReader({
			root : "unSsi", 
			fields : [
				{name : 'taskId'},
				{name : 'ssiCode'},
				{name : 'ssiName'},
		 	    {name : 'taskCode'},
		 	    {name : 'componentName'},
		 	    {name : 'picCode'},
		 	    {name : 'markingCode'},
		 	    {name : 'ownArea'},
		 	    {name : 'taskType'},
		   	    {name : 'taskInterval'},
		 	    {name : 'taskDesc'},
		        {name : 'remark'}
					 ]
		})
	})
	store.load();
	var typeCombo = new Ext.form.ComboBox({
				allowBlank : false,
				xtype : 'combo',
				name : 'chooseType',
				id : 'chooseType',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [[GVI, GVI], [DET, DET],
									[SDI, SDI]]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				mode : 'local',
				editable : false,
				triggerAction : 'all',
				width : 50,
				emptyText : '请选择'
			});
	var column = new Ext.grid.ColumnModel([
	   {
	    	header : "ID", 
	    	dataIndex : "taskId", 
	    	hidden : true
    	},
		{
			header : "项目标识码",
			dataIndex : "ssiCode"
		},
		{
			header : "项目名称",
			dataIndex : "ssiName"
		},
		{
			header : "维修任务编号"+commonality_mustInput,
			dataIndex : "taskCode",
			editor : new Ext.form.TextField({
				maxLength:50,
				maxLengthText:commonality_MaxLengthText
			})
		},
		{
			header : "部件名称",
			dataIndex : "componentName",
			editor : new Ext.form.TextField({
				maxLength:200,
				maxLengthText:commonality_MaxLengthText
			})
		},
		{
			header : "图号",
			dataIndex : "picCode",
			editor : new Ext.form.TextField({
				maxLength:50,
				maxLengthText:commonality_MaxLengthText
			})
		},
		{
			header : "标识码",
			dataIndex : "markingCode",
			editor : new Ext.form.TextField({
				maxLength:50,
				maxLengthText:commonality_MaxLengthText
			})
		},
		{
			header : "区域号",
			dataIndex : "ownArea",
			editor : new Ext.form.TextField({
				regex : /^(\d+,)*\d+$/,
				regexText : "请输入正确的区域号并用逗号隔开",
				maxLength : 500,
				maxLengthText : commonality_MaxLengthText
			})
		},
		{
			header : "维修任务种类",
			dataIndex : "taskType",
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
		},
		{
			header : "检查周期",
			dataIndex : "taskInterval",
			editor : new Ext.form.TextArea({
				maxLength:200,
				maxLengthText:commonality_MaxLengthText,
				grow : true
			})
		},
		{
			header : "根据外场确定<br>非重要结构项目<br>检查任务说明",
			dataIndex : "taskDesc",
			width : 100,
			renderer : changeBR,
			editor : new Ext.form.TextArea({
				maxLength:4000,
				maxLengthText:commonality_MaxLengthText,
				grow : true
			})
		},
		{
			header : "备注",
			dataIndex : "remark",
			renderer : changeBR,
			editor : new Ext.form.TextArea({
				maxLength:4000,
				maxLengthText:commonality_MaxLengthText,
				grow : true
			})
		}]);
		
		var grid = new Ext.grid.EditorGridPanel({
				title :  returnPageTitle('非重要结构项目的检查要求','unSsi'),
				cm : column,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				region : 'center',
				store : store,
				clicksToEdit : 2,   
				stripeRows : true,
				tbar : [new Ext.Button({
							text : commonality_add,
							disabled : isMaintain == 0 ? true : false,
							iconCls : 'icon_gif_add',
							handler : add
						}),'-',new Ext.Button({
							text : commonality_del,
							disabled : isMaintain == 0 ? true : false,
							iconCls : 'icon_gif_delete',
							handler : del
						}),'-',new Ext.Button({
							text : commonality_save,
							disabled : isMaintain == 0 ? true : false,
							iconCls : 'icon_gif_save',
							handler : save
						}) ]
			});
			
			
		function save(){
			var status=false;
			for (var i = 0; i < store.getCount(); i++){
				var re = store.getAt(i);
				if(re.get('taskCode')==null||re.get('taskCode')==""){
					status=true;
					break;
				}
			}
			if(status){
				alert('维修任务编号不能为空');
				return;
			}
			var json = [];
			Ext.each(store.modified, function(item) {
				json.push(item.data);
			});
			if(json.length==0){
				alert("没有需要更新或保存的数据");
				return;
			}
			Ext.MessageBox.show({
			 	    title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,				    
				    buttons : Ext.Msg.YESNO,
			    	fn : function(id){
				    	 if (id == 'yes'){	
							var waitMsg = new Ext.LoadMask(Ext.getBody(), {
							    msg : commonality_waitMsg,
							    removeMask : true// 完成后移除
							});
							waitMsg.show();						
							Ext.Ajax.request( {
								url : contextPath+'/struct/unSsi/saveUnSsi.do',
								waitMsg :'正在保存.....',
								params : {
									jsonData : Ext.util.JSON.encode(json), 
									ssiId : ssiId
								},
								method : "POST",
								success : function(response) {
									waitMsg.hide();
									alert(commonality_messageSaveMsg);
									store.reload();
									parent.refreshTreeNode();
									store.modified=[];
								},
								failure : function(response) {
									waitMsg.hide();
									alert(commonality_cautionMsg);
									return;
								}
							});
						} 
				    }
				});
		}
		
		
	function add() {
		var rec = grid.getStore().recordType;
		var p = new rec({
			    taskId :"",
				ssiCode : ssiCode,
				ssiName : ssiName,
				taskCode : "",
				componentName:"",
				picCode:picCode,
				markingCode:"",
				ownArea:"",
				taskType:"",
				taskInterval:"",
				taskDesc:"",
				remark:""
				});
			store.add(p);
	}	
	
	function del() {
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		var record = grid.getSelectionModel().getSelected();
		if (!record) {
			alert(commonality_alertDel);
			return;
		}
		Ext.Msg.confirm(commonality_caution, commonality_affirmDelMsg,
				function(btn) {
					if ('yes' == btn) {
						if (record.get('taskId') == '') {
							store.remove(record);
							store.modified.remove(record);
							return;
						}
						waitMsg.show();
						Ext.Ajax.request({
									url : contextPath
											+ "/struct/unSsi/delUnSsiReocrd.do",
									success : function() {
										waitMsg.hide();
										alert(commonality_messageDelMsg);
										parent.refreshTreeNode();
										store.load();
										store.modified=[];
									},
									failure : function() {
										waitMsg.hide();
										alert(commonality_messageDelMsgFail);
									},
									params : {
										delId : record.get('taskId'),
										ssiId : ssiId
									}
								});
					}
				});
	}
		grid.addListener("afteredit",afteredit);
		function afteredit(val){
			if (val.field == 'ownArea'){
				var nowRecord  = val.record;
				var zone = val.value.split(",");
				Ext.Ajax.request({
					url : contextPath+"/struct/verifyArea.do?",
					params:{
						verifyStr:val.value
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
				nowRecord = val.record;
				var count = 0;
				for (var i = 0; i < store.getCount(); i++) {
					record = store.getAt(i);
					if (record.get('taskCode') == nowRecord.get('taskCode')) {
						count++;
					}
				}
				if (count > 1) {
					nowRecord.set('taskCode', val.originalValue);
					alert("该任务编号已被使用，请重新填写！");
					return;
				}
			// 检查任务编号是否重复
			Ext.Ajax.request({
						url : contextPath+"/struct/s6/verifyTaskCode.do?",
						async : false,
						params : {
							verifyStr : val.value
						},
						success : function(response) {
							if (response.responseText) {
								nowRecord.set('taskCode', val.originalValue);
								alert("该任务编号已被使用，请重新填写！");
							}
						}
					})
				}
		}
			
		var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});

		var viewport = new Ext.Viewport({
				id : 'viewportId',
				layout : 'border',
				items : [{
							region : 'north',
							height : 30,
							frame : true,
							items : [headerStepForm]
						}, {
							layout : 'fit',
							region : 'center',
							items : [grid]
						}]
			});	
})