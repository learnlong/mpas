	function addChange(causeId,record){
		var refId = '';
		//var funCode = '';
		var refMsiId ='';
		var refFunctionId ='';
		var refFailureId ='';
		var refEffectId ='';
		var refCauseId ='';
		var nowId = "";
	
		    //原因下拉框
		var causeCombo = new Ext.form.ComboBox({
			name : 'causeCom', id : 'causeCom',
			fieldLabel : "故障原因"+"<font color='red'>*</font>",
			store : new Ext.data.Store({
				url : "${contextPath}/sys/m13/causeCombo.do",
				reader : new Ext.data.JsonReader({
					root : 'cause',
					fields : [
					 	{name : 'id'},
					 	{name : 'name'}
				 	]
				})
			}),
			valueField : "id", displayField : "name",
			mode : 'local', triggerAction : 'all', width : 300, editable : false
		});
			causeCombo.store.on("beforeload", function() {
			if(record){
				nowId= record.get("m13cId");
			}
			causeCombo.store.baseParams = {
				id : refEffectId,
				nowId:nowId
			};
		});
		causeCombo.store.on("load", function() {
			Ext.getCmp('causeCom').setValue(refCauseId);	
		});
		
			    //影响下拉框
		var effectCombo = new Ext.form.ComboBox({
			name : 'effectCom', id : 'effectCom',
			fieldLabel : "故障影响"+"<font color='red'>*</font>",
			store : new Ext.data.Store({
				url : "${contextPath}/sys/m13/effectCombo.do",
				reader : new Ext.data.JsonReader({
					root : 'effect',
					fields : [
					 	{name : 'id'},
					 	{name : 'name'}
				 	]
				})
			}),
			valueField : "id", displayField : "name",
			mode : 'local', triggerAction : 'all', width : 300, editable : false,
			listeners : {
      			"change" : function(field, newValue, oldValue) {
      				refEffectId = newValue;
		            refCauseId ='';
      				causeCombo.store.load();      				
				}
			}
		});
			effectCombo.store.on("beforeload", function() {
			effectCombo.store.baseParams = {
				id : refFailureId
			};
		});
		effectCombo.store.on("load", function() {
			Ext.getCmp('effectCom').setValue(refEffectId);	
		});
		
	       //功能故障下拉框
		var failureCombo = new Ext.form.ComboBox({
			name : 'failureCom', id : 'failureCom',
			fieldLabel : "功能故障"+"<font color='red'>*</font>",
			store : new Ext.data.Store({
				url : "${contextPath}/sys/m13/failureCombo.do",
				reader : new Ext.data.JsonReader({
					root : 'failure',
					fields : [
					 	{name : 'id'},
					 	{name : 'name'}
				 	]
				})
			}),
			valueField : "id", displayField : "name",
			mode : 'local', triggerAction : 'all', width : 300, editable : false,
			listeners : {
      			"change" : function(field, newValue, oldValue) {
      				 refFailureId =newValue;
		            refEffectId ='';
		            refCauseId ='';
      				effectCombo.store.load();    
      				Ext.getCmp('causeCom').setValue('');	  				
				}
			}
		});
			failureCombo.store.on("beforeload", function() {
			failureCombo.store.baseParams = {
				id : refFunctionId
			};
		});
		failureCombo.store.on("load", function() {
			Ext.getCmp('failureCom').setValue(refFailureId);	
		});
		//功能下拉框
		var funCombo = new Ext.form.ComboBox({
			name : 'funCom', id : 'funCom',
			fieldLabel : "功能"+"<font color='red'>*</font>",
			store : new Ext.data.Store({
				url : "${contextPath}/sys/m13/functionCombo.do",
				reader : new Ext.data.JsonReader({
					root : 'function',
					fields : [
					 	{name : 'id'},
					 	{name : 'name'}
				 	]
				})
			}),
			valueField : "id", displayField : "name",
			mode : 'local', triggerAction : 'all', width : 300, editable : false,
			listeners : {
      			"change" : function(field, newValue, oldValue) {
      				refFunctionId = newValue;
		            refFailureId ='';
		            refEffectId ='';
		            refCauseId ='';
      				failureCombo.store.load();   
      				Ext.getCmp('causeCom').setValue('');
      				Ext.getCmp('effectCom').setValue('');	   				
				}
			}
		});
		funCombo.store.on("beforeload", function() {
			funCombo.store.baseParams = {
				id : refMsiId
			};
		});
		funCombo.store.on("load", function() {
			Ext.getCmp('funCom').setValue(refFunctionId);	
		});
				//MSI下拉框		
		var msiCombo = new Ext.form.ComboBox({
			name : 'msiCom', id : 'msiCom',
			fieldLabel : "MSI号"+"<font color='red'>*</font>",
			store : new Ext.data.Store({
				url : "${contextPath}/sys/m13/msiCombo.do",
				reader : new Ext.data.JsonReader({
					root : 'msi',
					fields : [
					 	{name : 'id'},
					 	{name : 'name'}
				 	]
				})
			}),
			valueField : "id", displayField : "name",
			mode : 'local', triggerAction : 'all', width : 100, editable : false,
			listeners : {
      			"change" : function(field, newValue, oldValue) {
      				refMsiId = newValue;
      			    refFunctionId ='';
		            refFailureId ='';
		            refEffectId ='';
		            refCauseId ='';;
      				funCombo.store.load(); 
      				Ext.getCmp('causeCom').setValue('');
      				Ext.getCmp('effectCom').setValue('');	
      				Ext.getCmp('failureCom').setValue('');     				
				}
			}
		});
					
		var msiForm = new Ext.form.FormPanel({
			baseCls : 'x-plain',
			labelWidth : 100,
			labelAlign : "right",
			buttonAlign : "center",
			fileUpload : true,  
			items : [
				msiCombo,
				funCombo,
				failureCombo,
				effectCombo,
				causeCombo,
				new Ext.form.TextArea({
				   fieldLabel : "被参考的MSI是否已进行分析",
					name : 'isAna',
					id : 'isAna',
					width : 300,
					height : 50
				}),
				new Ext.form.TextArea({
				    fieldLabel : "备注",
					name : 'remark',
					id : 'remark',
					width : 300,
					height : 50
				})
			],
			buttons : [
				new Ext.Button({
					text : commonality_save,
					disabled : !flag,
					handler : function(){
						winSave();
					}
				}),
				new Ext.Button({
					text : commonality_del,
					disabled : !flag,
					handler : function(){
				
						deleteRef();
					}
				}),
				new Ext.Button({
					text : commonality_close,
					handler : function(){
						if (refId == ''){
							record.set('isRef',0);
						}
						win.close();
					}
				})
			]
		});

		msiCombo.store.on("load", function() {
			Ext.Ajax.request( {
				url : "${contextPath}/sys/m13/searchRef.do",
				params : {						
					id : causeId
				},
				method : "POST",
				callback: function(options, success, response){
					var all = Ext.util.JSON.decode(response.responseText);
					if (all.ref[0] == null){
						refId = '';
					} else {
						Ext.getCmp('msiCom').setValue(all.ref[0].refMsiId);
						if (all.ref[0].refMsiId != '' && all.ref[0].refMsiId != null){
							refMsiId = all.ref[0].refMsiId;
							refFunctionId = all.ref[0].refFunctionId;
							refFailureId = all.ref[0].refFailureId;
							refEffectId = all.ref[0].refEffectId;
							refCauseId = all.ref[0].refCauseId;
							funCombo.store.load();
							failureCombo.store.load();
							effectCombo.store.load();
							causeCombo.store.load();
						}
					    Ext.getCmp('isAna').setValue(all.ref[0].isAna);
					    Ext.getCmp('remark').setValue(all.ref[0].remark);
						refId = all.ref[0].refId;
					}				
				}
			});
		});
		msiCombo.store.load();		
		
		var win = new Ext.Window({
			title : "添加参考MSI",
			x : 100,
			y : 30,
			closable : false,
			autoWidht : true,
			autoHeight : true,
			resizable : false,
			bodyStyle : 'padding:5px;',
			modal : true,
			items : [msiForm]
		});
		win.show();		
		function winSave(){
			var json = [];
			var temp = {};
			temp['refId'] = refId;
			temp['refMsiId'] = Ext.getCmp('msiCom').getValue();
			temp['refFunctionId'] = Ext.getCmp('funCom').getValue();
			temp['refFailureId'] = Ext.getCmp('failureCom').getValue();
			temp['refEffectId'] = Ext.getCmp('effectCom').getValue();
			temp['refCauseId'] = Ext.getCmp('causeCom').getValue();
			temp['isAna'] = Ext.getCmp('isAna').getValue();
			temp['remark'] = Ext.getCmp('remark').getValue();
			
			if (temp['refMsiId'] == null || temp['refMsiId'] == ''){
				alert("请选择MSI");
				return;
			}
			if (temp['refCauseId'] == null || temp['refCauseId'] == ''){
				alert("故障原因不能为空！");
				return;
			}
			json.push(temp);
			
			Ext.Ajax.request( {
				url : "${contextPath}/sys/m13/saveRef.do",
				params : {						
					jsonData : Ext.util.JSON.encode(json),
					m13cId : causeId,
					msiId : msiId
				},
				method : "POST",
				success : function(form,action) {
					alert( commonality_messageSaveMsg);
					successNext(3);
					parent.refreshTreeNode();
					win.close();
				},
				failure : function(form,action) {
					alert(commonality_cautionMsg);
				}
			});			
		}
		
		function deleteRef(){
			if (refId == ''){
				alert("没有数据可以删除");
				return;
			}
			Ext.Ajax.request( {
				url : "${contextPath}/sys/m13/delRef.do",
				params : {
				    id : refId,	
				    msiId :msiId,
					m13cId : causeId
		
				},
				method : "POST",
				success : function(form,action) {
					successNext(3);
					parent.refreshTreeNode();
					alert(commonality_messageDelMsg);
					win.close();
				},
				failure : function(form,action) {
				alert(commonality_cautionMsg);
				}
			});
		}
	}