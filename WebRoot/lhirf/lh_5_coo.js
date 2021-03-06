	function coordination1(type,taskDesc,area,taskCode,taskType){
		function getNowDate(){
			var myDate = new Date();
			var year = myDate.getFullYear();  
			var month = myDate.getMonth();
			var day = myDate.getDate(); 
			if(month<9){
				month = month+1
				month ="0"+ month;
			}
			if(day<10){
				day ="0"+day;
			}
			return date = year+"-"+month+"-"+day;
		}
			function testCooValue(){
				if(Ext.getCmp("comcoordinationCode").getValue()==null||Ext.getCmp("comcoordinationCode").getValue().trim()==""){
						alert( "协调单编号不能为空");
						return false;
				}
				else if(Ext.getCmp('comsendWg').getValue()==null||Ext.getCmp('comsendWg').getValue().trim()==""){
					alert("请填写发送专业室");
					return false;
				 }			
				else if(Ext.getCmp('comreceiveWg').getValue()==null||Ext.getCmp('comreceiveWg').getValue().trim()==""){
					alert("请填写接收接收专业室");
					return false;
				}
				else if(Ext.getCmp('comsendUser').getValue()==null||Ext.getCmp('comsendUser').getValue().trim()==""){
						 alert("请填写发送专业室执行组长");
						 return false;
					 }			
				else if(Ext.getCmp('comreceiveUser').getValue()==null||Ext.getCmp('comreceiveUser').getValue().trim()==""){
					alert("请填写接收专业室执行组长");
					return false;
				 }else{
					return true;
				}		
			}
			var nowUserProfessionStore = new Ext.data.Store({
				 proxy : new Ext.data.HttpProxy({
			            url :  contextPath + "/com/coordination/getProfessionByUserId.do"
					}),
			      reader : new Ext.data.JsonReader({
							root : 'ownProfession',
							fields : [{
										name : 'professionId',
										type : 'string'
									}, {
										name : 'professionName',
										type : 'string'
									}]
						})
				   });
			nowUserProfessionStore.load();
			
			var nowProStore = new Ext.data.Store({
				 proxy : new Ext.data.HttpProxy({
			            url :  contextPath + "/com/coordination/loadUserByProfessonId.do"
					}),
			      reader : new Ext.data.JsonReader({
							root : 'comUsers',
							fields : [{
										name : 'userId',
										type : 'string'
									}, {
										name : 'userName',
										type : 'string'
									}]
						})
				   });
		//	 nowProStore.load();  
			
			var nowUserCombo  = new Ext.form.ComboBox({
		//		renderTo : Ext.getBody(),
				xtype : 'combo',
				name : 'comsendWg',
				id : 'comsendWg',
				store : nowUserProfessionStore,
				valueField : "professionId",
				displayField : "professionName",
				fieldLabel : "发送专业室",
				typeAhead : true,
				model : 'local',
				triggerAction : 'all',
				emptyText : ' --全部--',// /请选择
				editable : false,
				selectOnFocus : true,
				width : 78,
				listeners : {
						select : function(combo, record, index) {
							Ext.getCmp('comsendUser').setValue('');
							nowProStore.removeAll();
							nowProCombo.store.baseParams = {
								professionId : combo.value
							};
							nowProCombo.store.load();
						}
					}
			})
			
			var allProfessionStore = new Ext.data.Store({
				 proxy : new Ext.data.HttpProxy({
			            url :  contextPath + "/com/coordination/jsonLoadAllProfession.do"
					}),
			      reader : new Ext.data.JsonReader({
							root : 'comProfession',
							fields : [{
										name : 'comProfessionId',
										type : 'string'
									}, {
										name : 'comProfessionName',
										type : 'string'
									}]
						})
				   });
			allProfessionStore.on("beforeload",function(){
				allProfessionStore.baseParams={
					comreceiveArea:area
				}
			})
			allProfessionStore.load();
			
			var allProStore = new Ext.data.Store({
				 proxy : new Ext.data.HttpProxy({
			            url :  contextPath + "/com/coordination/loadUserByProfessonId.do"
					}),
			      reader : new Ext.data.JsonReader({
							root : 'comUsers',
							fields : [{
										name : 'userId',
										type : 'string'
									}, {
										name : 'userName',
										type : 'string'
									}]
						})
				   });
		//	allProStore.load();
			
			var allProfessionCombo  = new Ext.form.ComboBox({
		//		renderTo : Ext.getBody(),
				xtype : 'combo',
				name : 'comreceiveWg',
				id : 'comreceiveWg',
				store : allProfessionStore,
				valueField : "comProfessionId",
				displayField : "comProfessionName",
				fieldLabel : "接收专业室",
				typeAhead : true,
				model : 'local',
				triggerAction : 'all',
				emptyText : ' --全部--',// /请选择
				editable : false,
				selectOnFocus : true,
				width : 100,
				listeners : {
							select : function(combo, record, index) {
								Ext.getCmp('comreceiveUser').setValue('');
								allProStore.removeAll();
								allProCombo.store.baseParams = {
									professionId : combo.value
								};
								allProCombo.store.load();
							}
					}
			})
			
			var nowProCombo  = new Ext.form.ComboBox({
		//		renderTo : Ext.getBody(),
				xtype : 'combo',
				name : 'comsendUser',
				id : 'comsendUser',
				store : nowProStore,
				valueField : "userId",
				displayField : "userName",
				fieldLabel : "专业室执行组长",
				typeAhead : true,
				model : 'local',
				triggerAction : 'all',
				emptyText : ' --全部--',// /请选择
				editable : false,
				selectOnFocus : true,
				width : 100,
				listeners : {
					focus : function(){
						if(Ext.getCmp('comsendWg').getValue()==null||Ext.getCmp('comsendWg').getValue()==""){
							alert("请先选择发送专业室");
							return;
						}
					}
				}
			})
		
		var allProCombo  = new Ext.form.ComboBox({
	//		renderTo : Ext.getBody(),
			xtype : 'combo',
			name : 'comreceiveUser',
			id : 'comreceiveUser',
			store : allProStore,
			valueField : "userId",
			displayField : "userName",
			fieldLabel : "专业室执行组长",
			typeAhead : true,
			model : 'local',
			triggerAction : 'all',
			emptyText : ' --全部--',// /请选择
			editable : false,
			selectOnFocus : true,
			width : 100,
			listeners : {
				focus : function(){
					if(Ext.getCmp('comreceiveWg').getValue()==null||Ext.getCmp('comreceiveWg').getValue()==""){
						alert("请先选择接收专业室");
						return;
					}
				}
			}
		})
		
		var coform = new Ext.form.FormPanel({
			id : 'formAdd',
			border : false,
			items :[{
				layout:'fit', 
		        border : false,
		        bodyStyle : "padding:10px 20px 15px 15px",//"上、右、下、左"
		        items :[{
					xtype : 'textfield',
					id : 'comcoordinationCode',
					name : 'comcoordinationCode',
					width :150 ,
					maxLength : 32,
					maxLengthText : "最大长度为32",
					emptyText : "请输入协调单编号",
					allowBlank : false,
					style:{'text-align':'center'},
					value : " "
		        }]
			},{
				layout:'fit', 
		        border : false,
		        bodyStyle : "padding:0 20px 15px 15px",//"上、右、下、左"
		        items :[{
					xtype : 'textfield',
					id : 'comcoordinationName',
					name : 'comcoordinationName',
					width :150 ,
					readOnly : true,
					style:{'text-align':'center'},
					value : "协调单"
		        }]
			},{
				layout:'form', 
		        border : false,
		        labelAlign : "left",
		        labelWidth : 32,
		        bodyStyle : "padding:0 5px 0 5px",//"上、右、下、左"
		        items :[{
					xtype : 'textfield',
					id : 'comtheme',
					name : 'comtheme',
					width :496 ,
					maxLength : 200,
					maxLengthText :"最大长度为 200",
					fieldLabel :"主题"
		        }]
			},{
				layout : "table",
				border : false,
				bodyStyle : "padding:5px 5px 0 5px",//"上、右、下、左"
				layoutConfig : {columns:3},
				items : [{
				        layout:'form', 
				        border : false,
				        labelWidth : 78,
				        items :[nowUserCombo]
		        	},{
						layout:'form',
						border : false,
						labelWidth : 78,
						labelAlign : "right",
						items: [allProfessionCombo]						
			         },{
						layout:'form',
						border : false,
						labelWidth : 78,
						labelAlign : "right",
						items: [{
							xtype : 'textfield',
							id : 'comreceiveArea',
							name : 'comreceiveArea',
							width :100 ,
							value : area,
							readOnly : true,
							fieldLabel : "接受区域"
						}]						
			         }]
			},{
				layout:'form', 
		        border : false,
		        labelAlign : "top",
		        bodyStyle : "padding:0 5px 5px 5px",//"上、右、下、左"
		        items :[{
		        	xtype : 'textarea',
		        	id : 'comcontent',
		        	name : 'comcontent',
		        	width :535 ,
		        	value :taskDesc,
		        	maxLength : 2000,
					maxLengthText : "最大长度为 2000",
		        	fieldLabel : "内容"
		        }]
			},{
				layout : "table",
				border : false,
				bodyStyle : "padding:0 5px 0px 5px",
				layoutConfig : {columns:2},
				items : [{
				        layout:'form', 
				        border : false,
				        labelWidth : 92,
				        bodyStyle : "padding:0 10px 0px 10px",
				        items :[nowProCombo]
		        	},{
						layout:'form',
						border : false,
						labelAlign : "right",
						bodyStyle : "padding:0 5px 0px 80px",
						items: [{
							xtype : 'textfield',
							id : 'comcreateDate',
							name : 'comcreateDate',
							width :128 ,
							readOnly : true,
							style:{'text-align':'center'},
							//format: 'Y-m-d',
							value : getNowDate(),
							fieldLabel :"日期" 
						}]						
	        	 }]
			},{
				layout:"form",
				border : false,
				labelAlign : "top",
				bodyStyle : 'padding : 0 5px 5px 5px',
				items :[{
					xtype : 'textarea',
					id : 'comreContent',
					name : 'comreContent',
					disabled : true,
					width :535,
					fieldLabel :"答复"
				}]			
			},{
				layout : "table",
				border : false,
				bodyStyle : "padding:0 5px 0px 5px",
				layoutConfig : {columns:2},
				items : [{
				        layout:'form', 
				        border : false,
				        labelWidth : 92,
				        bodyStyle : "padding:0 10px 0px 10px",
				        items :[allProCombo]
		        	},{
						layout:'form',
						border : false,
						labelAlign : "right",
						bodyStyle : "padding:0 10px 0px 80px",
						items: [{
							xtype : 'textfield',
							id : 'comreceiveDate',
							name : 'comreceiveDate',
							//format: 'Y-m-d',
							width :128 ,
							style:{'text-align':'center'},
							disabled:true,
							fieldLabel : "日期"
						}]						
		         }]
			}]
		})
		var window = new Ext.Window({
			layout : 'form',
			width : 575,
			height : 480,
			resizable : true,
			draggable : true,
//			closeAction : 'close',
			closable : false,
			autoScroll : true,
			title : "协调单",
			modal : true,
			titleCollapse : true,
			buttonAlign : 'center',
			constrain : true,
			items : [coform],
			buttons : [{
				text : "暂存",
				handler : function(){	
					if(testCooValue()){
						if(taskType==GVI){
							jsonGVI=[];
							jsonGVI.push(coform.getForm().getValues());
							jsonGVI.push({"isReceive":"2","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
								 "comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
								"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":"nul"});
						}
						if(taskType==DIS){
							jsonDIS=[];
							jsonDIS.push(coform.getForm().getValues());
							jsonDIS.push({"isReceive":"2","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
								"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
								"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":"nul"});
						}
						if(taskType==DET){
							jsonDET=[];
							jsonDET.push(coform.getForm().getValues());
							jsonDET.push({"isReceive":"2","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
								"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
								"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":"nul"});
						}
						if(taskType==FNC){
							jsonFNC=[];
							jsonFNC.push(coform.getForm().getValues());
							jsonFNC.push({"isReceive":"2","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
								"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
								"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":"nul"});
						}
						window.close();
					}
				}
			},{
				text : "提交",
				handler : function(){
					if(testCooValue()){
						Ext.Msg.confirm(commonality_affirm,commonality_affirmSaveMsg,function(btn){
				    		if(btn==='yes'){
				    			if(taskType==GVI){
				    				jsonGVI=[];
									jsonGVI.push(coform.getForm().getValues());
									jsonGVI.push({"isReceive":"1","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),	
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":"nul"});
								}
								if(taskType==DIS){
									jsonDIS=[];
									jsonDIS.push(coform.getForm().getValues());
									jsonDIS.push({"isReceive":"1","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":"nul"});
								}
								if(taskType==DET){
									jsonDET=[];
									jsonDET.push(coform.getForm().getValues());
									jsonDET.push({"isReceive":"1","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":"nul"});
								}
								if(taskType==FNC){
									jsonFNC=[];
									jsonFNC.push(coform.getForm().getValues());
									jsonFNC.push({"isReceive":"1","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":"nul"});
								}
								window.close();
				    	   	}
						  })
					}
					}
				},{
				text : "重置",
				handler : function() {
					coform.getForm().reset();
					allProStore.removeAll();
					nowProStore.removeAll();
				}
			},{
				text : "关闭",
				handler : function() {
					window.close();
				}
			}]
		});
		window.show();
	}
	
	
	function coordination2(id,type,taskDesc,area,taskCode,taskType){
		function getNowDate(){
			var myDate = new Date();
			var year = myDate.getFullYear();  
			var month = myDate.getMonth();
			var day = myDate.getDate(); 
			if(month<9){
				month = month+1
				month ="0"+ month;
			}
			if(day<10){
				day ="0"+day;
			}
			return date = year+"-"+month+"-"+day;
		}
		function testCooValue(){
				if(Ext.getCmp("comcoordinationCode").getValue()==null||Ext.getCmp("comcoordinationCode").getValue().trim()==""){
						alert( "协调单编号不能为空");
						return false;
				}
				else if(Ext.getCmp('comsendWg').getValue()==null||Ext.getCmp('comsendWg').getValue().trim()==""){
					alert("请填写发送专业室");
					return false;
				 }			
				else if(Ext.getCmp('comreceiveWg').getValue()==null||Ext.getCmp('comreceiveWg').getValue().trim()==""){
					alert("请填写接收接收专业室");
					return false;
				}
				else if(Ext.getCmp('comsendUser').getValue()==null||Ext.getCmp('comsendUser').getValue().trim()==""){
						 alert("请填写发送专业室执行组长");
						 return false;
					 }			
				else if(Ext.getCmp('comreceiveUser').getValue()==null||Ext.getCmp('comreceiveUser').getValue().trim()==""){
					alert("请填写接收专业室执行组长");
					return false;
				 }else{
					return true;
				}		
			}
			var nowUserProfessionStore = new Ext.data.Store({
			 	 proxy : new Ext.data.HttpProxy({
			            url :  contextPath + "/com/coordination/getProfessionByUserId.do"
				  }),
			      reader : new Ext.data.JsonReader({
							root : 'ownProfession',
							fields : [{
										name : 'professionId',
										type : 'string'
									}, {
										name : 'professionName',
										type : 'string'
									}]
						})
			   });
			nowUserProfessionStore.load();
			
			var nowProStore = new Ext.data.Store({
				 proxy : new Ext.data.HttpProxy({
			            url :  contextPath + "/com/coordination/loadUserByProfessonId.do"
					}),
			      reader : new Ext.data.JsonReader({
							root : 'comUsers',
							fields : [{
										name : 'userId',
										type : 'string'
									}, {
										name : 'userName',
										type : 'string'
									}]
						})
				   });
		//	 nowProStore.load();  
			
			var nowUserCombo  = new Ext.form.ComboBox({
		//		renderTo : Ext.getBody(),
				xtype : 'combo',
				name : 'comsendWg',
				id : 'comsendWg',
				store : nowUserProfessionStore,
				valueField : "professionId",
				displayField : "professionName",
				fieldLabel : "发送专业室",
				typeAhead : true,
				model : 'local',
				triggerAction : 'all',
				emptyText : ' --全部--',// /请选择
				editable : false,
				selectOnFocus : true,
				width : 78,
				listeners : {
						select : function(combo, record, index) {
							Ext.getCmp('comsendUser').setValue('');
							nowProStore.removeAll();
							nowProCombo.store.baseParams = {
								professionId : combo.value
							};
							nowProCombo.store.load();
						}
					}
			})
			
			var allProfessionStore = new Ext.data.Store({
				 proxy : new Ext.data.HttpProxy({
			            url :  contextPath + "/com/coordination/jsonLoadAllProfession.do"
					}),
			      reader : new Ext.data.JsonReader({
							root : 'comProfession',
							fields : [{
										name : 'comProfessionId',
										type : 'string'
									}, {
										name : 'comProfessionName',
										type : 'string'
									}]
						})
				   });
			allProfessionStore.on("beforeload",function(){
				allProfessionStore.baseParams={
					comreceiveArea:area
				}
			})
			allProfessionStore.load();
			
			var allProStore = new Ext.data.Store({
				 proxy : new Ext.data.HttpProxy({
			            url :  contextPath + "/com/coordination/loadUserByProfessonId.do"
					}),
			      reader : new Ext.data.JsonReader({
							root : 'comUsers',
							fields : [{
										name : 'userId',
										type : 'string'
									}, {
										name : 'userName',
										type : 'string'
									}]
						})
				   });
		//	allProStore.load();
			
			var allProfessionCombo  = new Ext.form.ComboBox({
		//		renderTo : Ext.getBody(),
				xtype : 'combo',
				name : 'comreceiveWg',
				id : 'comreceiveWg',
				store : allProfessionStore,
				valueField : "comProfessionId",
				displayField : "comProfessionName",
				fieldLabel : "接收专业室",
				typeAhead : true,
				model : 'local',
				triggerAction : 'all',
				emptyText : ' --全部--',// /请选择
				editable : false,
				selectOnFocus : true,
				width : 100,
				listeners : {
							select : function(combo, record, index) {
								Ext.getCmp('comreceiveUser').setValue('');
								allProStore.removeAll();
								allProCombo.store.baseParams = {
									professionId : combo.value
								};
								allProCombo.store.load();
							}
					}
			})
			
			var nowProCombo  = new Ext.form.ComboBox({
		//		renderTo : Ext.getBody(),
				xtype : 'combo',
				name : 'comsendUser',
				id : 'comsendUser',
				store : nowProStore,
				valueField : "userId",
				displayField : "userName",
				fieldLabel : "专业室执行组长",
				typeAhead : true,
				model : 'local',
				triggerAction : 'all',
				emptyText : ' --全部--',// /请选择
				editable : false,
				selectOnFocus : true,
				width : 100,
				listeners : {
					focus : function(){
						if(Ext.getCmp('comsendWg').getValue()==null||Ext.getCmp('comsendWg').getValue()==""){
							alert("请先选择发送专业室");
							return;
						}
					}
				}
			})
		
		var allProCombo  = new Ext.form.ComboBox({
	//		renderTo : Ext.getBody(),
			xtype : 'combo',
			name : 'comreceiveUser',
			id : 'comreceiveUser',
			store : allProStore,
			valueField : "userId",
			displayField : "userName",
			fieldLabel : "专业室执行组长",
			typeAhead : true,
			model : 'local',
			triggerAction : 'all',
			emptyText : ' --全部--',// /请选择
			editable : false,
			selectOnFocus : true,
			width : 100,
			listeners : {
				focus : function(){
					if(Ext.getCmp('comreceiveWg').getValue()==null||Ext.getCmp('comreceiveWg').getValue()==""){
						alert("请先选择接收专业室");
						return;
					}
				}
			}
		})
		
		var comstore = new Ext.data.Store({
			 proxy : new Ext.data.HttpProxy({
		           url : contextPath + "/com/coordination/loadCoordination.do"
				}),
		    reader : new Ext.data.JsonReader({
					root : 'coordination',
					fields : [{
								name : 'comCoordinationId'
							},{
								name : 'comTaskId'
							}, {
								name : 'comcoordinationCode'
							},{
								name : 'comcontent'
							}, {
								name : 'comtheme'
							},{
								name : 'comsendWg'
							}, {
								name : 'comreceiveWg'
							},{
								name : 'comsendWgId'
							}, {
								name : 'comreceiveWgId'
							},{
								name : 'comreceiveArea'
							}, {
								name : 'comsendUser'
							},{
								name : 'comcreateDate'
							}, {
								name : 'comreContent'
							},{
								name : 'comreceiveUser'
							}, {
								name : 'comreceiveDate'
							},{
								name : 'type'
							},{
								name : 'isreceive'
							},{
								name : 'comreceiveUserId'
							},{
								name : 'comsendUserId'
							}]	    
						})
					});
		comstore.on("beforeload",function(){
			comstore.baseParams = {
				comTaskId : id
				};
		})
		comstore.load();
		comstore.on("load",function(comstore){
			var comcoordinationCode1;
			var comtheme1;
			var comsendWg1;
			var comreceiveWg1;
			var comsendWgId1;
			var comreceiveWgId1;
			var comreceiveArea1;
			var comcontent1;
			var comsendUser1;
			var comsendUserId1;
			var comcreateDate1;
			var comreContent1;
			var comreceiveUser1;
			var comreceiveDate1;
			var isreceive1;
			var comTaskId;
			var comreceiveUserId1;
			var comCoordinationId;
			var type1;
			for(var i = 0;i<comstore.getCount();i++){
				 var record = comstore.getAt(i);
				 comcoordinationCode1 = record.get('comcoordinationCode');
				 comtheme1 = record.get('comtheme');
				 comsendWg1 = record.get('comsendWg');
				 comreceiveWg1 = record.get('comreceiveWg');
				 comsendWgId1 = record.get('comsendWgId');
				 comreceiveWgId1 = record.get('comreceiveWgId');
				 comreceiveArea1 = record.get('comreceiveArea');
				 comcontent1 = record.get('comcontent');
				 comsendUser1 = record.get('comsendUser');
				 comcreateDate1 = record.get('comcreateDate');
				 comreContent1 = record.get('comreContent');
				 comreceiveUser1 = record.get('comreceiveUser');
				 comreceiveDate1 = record.get('comreceiveDate');
				 isreceive1 = record.get('isreceive');
				 comTaskId = record.get('comTaskId');
				 comCoordinationId = record.get('comCoordinationId');
				 type1 = record.get('type');
				 comsendUserId1 = record.get('comsendUserId');
				 comreceiveUserId1 = record.get('comreceiveUserId');
			}
			if(comreceiveArea1!=area){
				if(comCoordinationId!='nul'){
					var coLenth = arryDelCoo.length;
					if(arryDelCoo.length>0){
						for(var i=0;i<arryDelCoo.length;i++){
							if(arryDelCoo[i]==comCoordinationId){
								continue;
							}else{
								arryDelCoo[coLenth] = comCoordinationId;
							}
						}
					}else{
						arryDelCoo[coLenth] = comCoordinationId;
					}
					comCoordinationId='nul';
				}
			}
			//新创建一个协调单
		     if(comCoordinationId =='nul'){
				var coform = new Ext.form.FormPanel({
					id : 'formAdd',
					border : false,
					items :[{
						layout:'fit', 
				        border : false,
				        bodyStyle : "padding:10px 20px 15px 15px",//"上、右、下、左"
				        items :[{
							xtype : 'textfield',
							id : 'comcoordinationCode',
							name : 'comcoordinationCode',
							width :150 ,
							maxLength : 32,
							maxLengthText : "最大长度为32",
							emptyText : "请输入协调单编号",
							allowBlank : false,
							//fieldLable : "",
							style:{'text-align':'center'}
							//value : " "
				        }]
					},{
						layout:'fit', 
				        border : false,
				        bodyStyle : "padding:0 20px 15px 15px",//"上、右、下、左"
				        items :[{
							xtype : 'textfield',
							id : 'comcoordinationName',
							name : 'comcoordinationName',
							width :150 ,
							readOnly : true,
							style:{'text-align':'center'},
							value :  "协调单"
				        }]
					},{
						layout:'form', 
				        border : false,
				        labelAlign : "left",
				        labelWidth : 32,
				        bodyStyle : "padding:0 5px 0 5px",//"上、右、下、左"
				        items :[{
							xtype : 'textfield',
							id : 'comtheme',
							name : 'comtheme',
							width :496 ,
							maxLength : 200,
							maxLengthText : "最大长度为 200",
							fieldLabel :  "主题"
				        }]
					},{
						layout : "table",
						border : false,
						bodyStyle : "padding:5px 5px 0 5px",//"上、右、下、左"
						layoutConfig : {columns:3},
						items : [{
							        layout:'form', 
							        border : false,
							        labelWidth : 78,
							        items :[nowUserCombo]
					        	},{
									layout:'form',
									border : false,
									labelWidth : 78,
									labelAlign : "right",
									items: [allProfessionCombo]						
						         },{
									layout:'form',
									border : false,
									labelWidth : 78,
									labelAlign : "right",
									items: [{
										xtype : 'textfield',
										id : 'comreceiveArea',
										name : 'comreceiveArea',
										width :100 ,
										value : area,
										readOnly : true,
										fieldLabel : "接受区域"
									}]						
					         }]
					},{
						layout:'form', 
				        border : false,
				        labelAlign : "top",
				        bodyStyle : "padding:0 5px 5px 5px",//"上、右、下、左"
				        items :[{
				        	xtype : 'textarea',
				        	id : 'comcontent',
				        	name : 'comcontent',
				        	width :535 ,
				        	value :taskDesc,
				        	maxLength : 2000,
							maxLengthText : "最大长度为 2000",
				        	fieldLabel : "内容"
				        }]
					},{
						layout : "table",
						border : false,
						bodyStyle : "padding:0 5px 0px 5px",
						layoutConfig : {columns:2},
						items : [{
							        layout:'form', 
							        border : false,
							        labelWidth : 92,
							        bodyStyle : "padding:0 10px 0px 10px",
							        items :[nowProCombo]
					        	},{
									layout:'form',
									border : false,
									labelAlign : "right",
									bodyStyle : "padding:0 5px 0px 80px",
									items: [{
										xtype : 'textfield',
										id : 'comcreateDate',
										name : 'comcreateDate',
										width :128 ,
										readOnly : true,
										style:{'text-align':'center'},
										value : getNowDate(),
										fieldLabel :"日期" 
									}]						
				         	}]
					},{
						layout:"form",
						border : false,
						labelAlign : "top",
						bodyStyle : 'padding : 0 5px 5px 5px',
						items :[{
							xtype : 'textarea',
							id : 'comreContent',
							name : 'comreContent',
							disabled : true,
							width :535,
							fieldLabel :"答复"
						}]			
					},{
						layout : "table",
						border : false,
						bodyStyle : "padding:0 5px 0px 5px",
						layoutConfig : {columns:2},
						items : [{
							        layout:'form', 
							        border : false,
							        labelWidth : 92,
							        bodyStyle : "padding:0 10px 0px 10px",
							        items :[allProCombo]
					        	},{
									layout:'form',
									border : false,
									labelAlign : "right",
									bodyStyle : "padding:0 10px 0px 80px",
									items: [{
										xtype : 'textfield',
										id : 'comreceiveDate',
										name : 'comreceiveDate',
										//format: 'Y-m-d',
										width :128 ,
										style:{'text-align':'center'},
										disabled:true,
										fieldLabel : "日期"
								}]						
				         }]
					}]
				})
				var window = new Ext.Window({
					layout : 'form',
					width : 575,
					height : 480,
					resizable : true,
					draggable : true,
					closable : false,
					autoScroll : true,
					title : "协调单",
					modal : true,
					titleCollapse : true,
					buttonAlign : 'center',
					constrain : true,
					items : [coform],
					buttons : [{
						text : "暂存",
						handler : function(){	
						if(testCooValue()){
								if(taskType==GVI){
									jsonGVI=[];
									jsonGVI.push(coform.getForm().getValues());
									jsonGVI.push({"isReceive":"2","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
								}
								if(taskType==DIS){
									jsonDIS=[];
									jsonDIS.push(coform.getForm().getValues());
									jsonDIS.push({"isReceive":"2","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),	
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
								}
								if(taskType==DET){
									jsonDET=[];
									jsonDET.push(coform.getForm().getValues());
									jsonDET.push({"isReceive":"2","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
								}
								if(taskType==FNC){
									jsonFNC=[];
									jsonFNC.push(coform.getForm().getValues());
									jsonFNC.push({"isReceive":"2","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
								}
								window.close();
							}
						}
					},{
						text : "提交",
						handler : function(){
								if(testCooValue()){
									Ext.Msg.confirm(commonality_affirm,commonality_affirmSaveMsg,function(btn){
							    		if(btn==='yes'){
							    			if(taskType==GVI){
							    				jsonGVI=[];
												jsonGVI.push(coform.getForm().getValues());
												jsonGVI.push({"isReceive":"1","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
													"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
													"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
											}
											if(taskType==DIS){
												jsonDIS=[];
												jsonDIS.push(coform.getForm().getValues());
												jsonDIS.push({"isReceive":"1","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
													"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
													"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
											}
											if(taskType==DET){
												jsonDET=[];
												jsonDET.push(coform.getForm().getValues());
												jsonDET.push({"isReceive":"1","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
													"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
													"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
											}
											if(taskType==FNC){
												jsonFNC=[];
												jsonFNC.push(coform.getForm().getValues());
												jsonFNC.push({"isReceive":"1","type":type,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
													"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
													"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
											}
											window.close();
							    	   	}
									  })
								}
							}
						},{
						text : "重置",
						handler : function() {
							coform.getForm().reset();
							allProStore.removeAll();
							nowProStore.removeAll();
						}
					},{
						text : "关闭",
						handler : function() {
							window.close();
						}
					}]
				});
				window.show();
		     }else{	     
		    //协调单已存在
		    	 //暂存的协调单
		    		 if(isreceive1 == '2'){
		    			 var coform = new Ext.form.FormPanel({
			    	 			id : 'formAdd',
			    	 			border : false,
			    	 			items :[{
			    	 				layout:'fit', 
			    	 		        border : false,
			    	 		        bodyStyle : "padding:10px 20px 15px 15px",//"上、右、下、左"
			    	 		        items :[{
				    	 				xtype : 'textfield',
				    	 				id : 'comcoordinationCode',
				    	 				name : 'comcoordinationCode',
				    	 				width :150 ,
				    	 				maxLength : 32,
				    					maxLengthText : "最大长度为 32",
				    	 				style:{'text-align':'center'},
				    	 				value : comcoordinationCode1
			    	 		        }]
			    	 			},{
			    	 				layout:'fit', 
			    	 		        border : false,
			    	 		        bodyStyle : "padding:0 20px 15px 15px",//"上、右、下、左"
			    	 		        items :[{
				    	 				xtype : 'textfield',
				    	 				id : 'comcoordinationName',
				    	 				name : 'comcoordinationName',
				    	 				width :150 ,
				    	 				readOnly : true,
				    	 				style:{'text-align':'center'},
				    	 				value : "协调单"
			    	 		        }]
			    	 			},{
			    	 				layout:'form', 
			    	 		        border : false,
			    	 		        labelAlign : "left",
			    	 		        labelWidth : 32,
			    	 		        bodyStyle : "padding:0 5px 0 5px",//"上、右、下、左"
			    	 		        items :[{
				    	 				xtype : 'textfield',
				    	 				id : 'comtheme',
				    	 				name : 'comtheme',
				    	 				width :496 ,
				    	 				maxLength : 200,
				    					maxLengthText : "最大长度为 200",
				    	 				fieldLabel : "主题",
				    	 				value : comtheme1
			    	 		        }]
			    	 			},{
			    	 				layout : "table",
			    	 				border : false,
			    	 				bodyStyle : "padding:5px 5px 0 5px",//"上、右、下、左"
			    	 				layoutConfig : {columns:3},
			    	 				items : [{
		    	 					        layout:'form', 
		    	 					        border : false,
		    	 					        labelWidth : 78,
		    	 					        items :[nowUserCombo]
		    	 			        	},{
		    	 							layout:'form',
		    	 							border : false,
		    	 							labelWidth : 78,
		    	 							labelAlign : "right",
		    	 							items: [allProfessionCombo]						
		    	 				         },{
		    	 								layout:'form',
		    	 								border : false,
		    	 								labelWidth : 78,
		    	 								labelAlign : "right",
		    	 								items: [{
		    	 									xtype : 'textfield',
		    	 									id : 'comreceiveArea',
		    	 									name : 'comreceiveArea',
		    	 									width :100 ,
		    	 									readOnly : true,
		    	 									value : comreceiveArea1,
		    	 									fieldLabel : "接受区域"
    	 										}]						
	    	 					         }]
			    	 			},{
			    	 				layout:'form', 
			    	 		        border : false,
			    	 		        labelAlign : "top",
			    	 		        bodyStyle : "padding:0 5px 5px 5px",//"上、右、下、左"
			    	 		        items :[{
			    	 		        	xtype : 'textarea',
			    	 		        	id : 'comcontent',
			    	 		        	name : 'comcontent',
			    	 		        	width :535 ,
			    	 		        	value :comcontent1,
			    	 		        	maxLength : 2000,
			    						maxLengthText : "最大长度为 2000",
			    	 		        	fieldLabel :  "内容"
			    	 		        }]
			    	 			},{
			    	 				layout : "table",
			    	 				border : false,
			    	 				bodyStyle : "padding:0 5px 0px 5px",
			    	 				layoutConfig : {columns:2},
			    	 				items : [{
			    	 					        layout:'form', 
			    	 					        border : false,
			    	 					        labelWidth : 92,
			    	 					        bodyStyle : "padding:0 10px 0px 10px",
			    	 					        items :[nowProCombo]
			    	 			        	},{
			    	 							layout:'form',
			    	 							border : false,
			    	 							labelAlign : "right",
			    	 							bodyStyle : "padding:0 5px 0px 80px",
			    	 							items: [{
			    	 								xtype : 'textfield',
			    	 								id : 'comcreateDate',
			    	 								name : 'comcreateDate',
			    	 								width :128 ,
			    	 								value : comcreateDate1,
			    	 								readOnly :true,
			    	 								style:{'text-align':'center'},
			    	 								fieldLabel : "日期"
	    	 									}]						
		    	 				         }]
			    	 			},{
			    	 				layout:"form",
			    	 				border : false,
			    	 				labelAlign : "top",
			    	 				bodyStyle : 'padding : 0 5px 5px 5px',
			    	 				items :[{
			    	 					xtype : 'textarea',
			    	 					id : 'comreContent',
			    	 					name : 'comreContent',
			    	 					width :535 ,
			    	 					disabled :true,
			    	 					value :comreContent1, 
			    	 					fieldLabel : "答复"
			    	 				}]			
			    	 			},{
			    	 				layout : "table",
			    	 				border : false,
			    	 				bodyStyle : "padding:0 5px 0px 5px",
			    	 				layoutConfig : {columns:2},
			    	 				items : [{
			    	 					        layout:'form', 
			    	 					        border : false,
			    	 					        labelWidth : 92,
			    	 					        bodyStyle : "padding:0 10px 0px 10px",
			    	 					        items :[allProCombo]
			    	 			        	},{
			    	 							layout:'form',
			    	 							border : false,
			    	 							labelAlign : "right",
			    	 							bodyStyle : "padding:0 10px 0px 80px",
			    	 							items: [{
			    	 								xtype : 'textfield',
			    	 								id : 'comreceiveDate',
			    	 								name : 'comreceiveDate',
			    	 								//format: 'Y-m-d',
			    	 								width :128 ,
			    	 								disabled :true,
			    	 								style:{'text-align':'center'},
			    	 								fieldLabel : "日期"
    	 										}]						
	    	 				         }]
			    	 			}]
			    	 		})
		    				var window = new Ext.Window({
		        	 			layout : 'form',
		        	 			width : 575,
		        	 			height : 480,
		        	 			resizable : true,
		        	 			draggable : true,
		        	 			closeAction : 'close',
		        	 			closable : true,
		        	 			autoScroll : true,
		        	 			title : "协调单",
		        	 			modal : true,
		        	 			titleCollapse : true,
		        	 			buttonAlign : 'center',
		        	 			constrain : true,
		        	 			items : [coform],
		        	 			buttons : [{
									text : "暂存",
									handler : function(){
										if(testCooValue()){
										   if(taskType==GVI){
												jsonGVI=[];
												jsonGVI.push(coform.getForm().getValues());
												jsonGVI.push({"isReceive":"2","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
													"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
													"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId,
													"comreceiveDate":"","comreContent":""});
											}
											if(taskType==DIS){
												jsonDIS=[];
												jsonDIS.push(coform.getForm().getValues());
												jsonDIS.push({"isReceive":"2","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
													"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
													"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId,
													"comreceiveDate":"","comreContent":""});
											}
											if(taskType==DET){
												jsonDET=[];
												jsonDET.push(coform.getForm().getValues());
												jsonDET.push({"isReceive":"2","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
													"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
													"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId,
													"comreceiveDate":"","comreContent":""});
											}
											if(taskType==FNC){
												jsonFNC=[];
												jsonFNC.push(coform.getForm().getValues());
												jsonFNC.push({"isReceive":"2","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
													"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
													"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId,
													"comreceiveDate":"","comreContent":""});
											}
											window.close();
										}
									
									}
								},{
									text : "提交",
									handler : function(){
										if(testCooValue()){
											Ext.Msg.confirm(commonality_affirm,commonality_affirmSaveMsg,function(btn){
									    		if(btn==='yes'){
									    			if(taskType==GVI){
									    				jsonGVI=[];
														jsonGVI.push(coform.getForm().getValues());
														jsonGVI.push({"isReceive":"1","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
															"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
															"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId,
															"comreceiveDate":"","comreContent":""});
													}
													if(taskType==DIS){
														jsonDIS=[];
														jsonDIS.push(coform.getForm().getValues());
														jsonDIS.push({"isReceive":"1","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
															"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
															"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId,
															"comreceiveDate":"","comreContent":""});
													}
													if(taskType==DET){
														jsonDET=[];
														jsonDET.push(coform.getForm().getValues());
														jsonDET.push({"isReceive":"1","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
															"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
															"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId,
															"comreceiveDate":"","comreContent":""});
													}
													if(taskType==FNC){
														jsonFNC=[];
														jsonFNC.push(coform.getForm().getValues());
														jsonFNC.push({"isReceive":"1","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
															"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
															"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId,
															"comreceiveDate":"","comreContent":""});
													}
													window.close();
									    	   	}
											  })
										}
										}
									},{
									text : "重置",
									handler : function() {
										coform.getForm().reset();
										allProStore.removeAll();
										nowProStore.removeAll();
										if(comsendWgId1!=null&&comsendWgId1!=""){
								            Ext.getCmp('comsendWg').setValue(comsendWgId1);
								            Ext.getCmp('comsendWg').setRawValue(comsendWg1);	 
								            nowProCombo.store.baseParams = {
												professionId : comsendWgId1
											};
											nowProCombo.store.load();
										}
										if(comreceiveWgId1!=null&&comreceiveWgId1!=""){
								            Ext.getCmp('comreceiveWg').setValue(comreceiveWgId1);
								            Ext.getCmp('comreceiveWg').setRawValue(comreceiveWg1);	   
								            allProCombo.store.baseParams = {
												professionId : comreceiveWgId1
											};
											allProCombo.store.load();
										}
										if(comreceiveUserId1!=null&&comreceiveUserId1!=""){
											 Ext.getCmp('comreceiveUser').setValue(comreceiveUserId1);
								      		 Ext.getCmp('comreceiveUser').setRawValue(comreceiveUser1);	
										}
										if(comsendUserId1!=null&&comsendUserId1!=""){
								            Ext.getCmp('comsendUser').setValue(comsendUserId1);
								            Ext.getCmp('comsendUser').setRawValue(comsendUser1);	    	 					           
										}
									}
								},{
									text : "关闭",
									handler : function() {
										window.close();
									}
								}]
		        	 		});
		        	 		window.addListener("beforeshow",function(){
			        	 			if(comsendWgId1!=null&&comsendWgId1!=""){
							            Ext.getCmp('comsendWg').setValue(comsendWgId1);
							            Ext.getCmp('comsendWg').setRawValue(comsendWg1);	 
							            nowProCombo.store.baseParams = {
											professionId : comsendWgId1
										};
										nowProCombo.store.load();
									}
									if(comreceiveWgId1!=null&&comreceiveWgId1!=""){
							            Ext.getCmp('comreceiveWg').setValue(comreceiveWgId1);
							            Ext.getCmp('comreceiveWg').setRawValue(comreceiveWg1);	   
							             allProCombo.store.baseParams = {
											professionId : comreceiveWgId1
										};
										allProCombo.store.load();
									}
									if(comreceiveUserId1!=null&&comreceiveUserId1!=""){
										 Ext.getCmp('comreceiveUser').setValue(comreceiveUserId1);
							      		 Ext.getCmp('comreceiveUser').setRawValue(comreceiveUser1);	
									}
									if(comsendUserId1!=null&&comsendUserId1!=""){
							            Ext.getCmp('comsendUser').setValue(comsendUserId1);
							            Ext.getCmp('comsendUser').setRawValue(comsendUser1);	    	 					           
									}
	        	 			 });
		        	 		window.show();
		    		 }
		    		 
		    		 //未回复的协调单 可以进行回复操作
		    		if(isreceive1 == '1'){
		    			 var coform = new Ext.form.FormPanel({
			    	 			id : 'formAdd',
			    	 			border : false,
			    	 			items :[{
			    	 				layout:'fit', 
			    	 		        border : false,
			    	 		        bodyStyle : "padding:10px 20px 15px 15px",//"上、右、下、左"
			    	 		        items :[{
				    	 				xtype : 'textfield',
				    	 				id : 'comcoordinationCode',
				    	 				name : 'comcoordinationCode',
				    	 				width :150 ,
				    	 				readOnly :true,
				    	 				//fieldLable : "",
				    	 				style:{'text-align':'center'},
				    	 				value : comcoordinationCode1
			    	 		        }]
			    	 			},{
			    	 				layout:'fit', 
			    	 		        border : false,
			    	 		        bodyStyle : "padding:0 20px 15px 15px",//"上、右、下、左"
			    	 		        items :[{
				    	 				xtype : 'textfield',
				    	 				id : 'comcoordinationName',
				    	 				name : 'comcoordinationName',
				    	 				width :150 ,
				    	 				readOnly : true,
				    	 				style:{'text-align':'center'},
				    	 				value : "协调单"
			    	 		        }]
			    	 			},{
			    	 				layout:'form', 
			    	 		        border : false,
			    	 		        labelAlign : "left",
			    	 		        labelWidth : 32,
			    	 		        bodyStyle : "padding:0 5px 0 5px",//"上、右、下、左"
			    	 		        items :[{
				    	 				xtype : 'textfield',
				    	 				id : 'comtheme',
				    	 				name : 'comtheme',
				    	 				readOnly :true,
				    	 				width :496 ,
				    	 				fieldLabel : "主题",
				    	 				value : comtheme1
			    	 		        }]
			    	 			},{
			    	 				layout : "table",
			    	 				border : false,
			    	 				bodyStyle : "padding:5px 5px 0 5px",//"上、右、下、左"
			    	 				layoutConfig : {columns:3},
			    	 				items : [{
			    	 					        layout:'form', 
			    	 					        border : false,
			    	 					        labelWidth : 78,
			    	 					        items :[{
				    	 							xtype : 'textfield',
				    	 							id : 'comsendWg',
				    	 							name : 'comsendWg',
				    	 							width :80 ,
				    	 							readOnly :true,
				    	 							value : comsendWg1,
				    	 							fieldLabel :"发送专业室"
			    	 					        }]
			    	 			        	},{
			    	 							layout:'form',
			    	 							border : false,
			    	 							labelWidth : 78,
			    	 							labelAlign : "right",
			    	 							items: [{
			    	 								xtype : 'textfield',
			    	 								id : 'comreceiveWg',
			    	 								name : 'comreceiveWg',
			    	 								width :100 ,
			    	 								border : false, 
			    	 								readOnly :true,
			    	 								value : comreceiveWg1,
			    	 								fieldLabel : "接收专业室"
	    	 										}]						
			    	 				         },{
			    	 								layout:'form',
			    	 								border : false,
			    	 								labelWidth : 78,
			    	 								labelAlign : "right",
			    	 								items: [{
			    	 									xtype : 'textfield',
			    	 									id : 'comreceiveArea',
			    	 									name : 'comreceiveArea',
			    	 									width :100 ,
			    	 									readOnly : true,
			    	 									value : comreceiveArea1,
			    	 									fieldLabel : "接受区域"
	    	 										}]						
		    	 					         }]
			    	 			},{
			    	 				layout:'form', 
			    	 		        border : false,
			    	 		        labelAlign : "top",
			    	 		        bodyStyle : "padding:0 5px 5px 5px",//"上、右、下、左"
			    	 		        items :[{
			    	 		        	xtype : 'textarea',
			    	 		        	id : 'comcontent',
			    	 		        	name : 'comcontent',
			    	 		        	width :535 ,
			    	 		        	readOnly :true,
			    	 		        	value :comcontent1,
			    	 		        	fieldLabel :  "内容"
			    	 		        }]
			    	 			},{
			    	 				layout : "table",
			    	 				border : false,
			    	 				bodyStyle : "padding:0 5px 0px 5px",
			    	 				layoutConfig : {columns:2},
			    	 				items : [{
			    	 					        layout:'form', 
			    	 					        border : false,
			    	 					        labelWidth : 92,
			    	 					        bodyStyle : "padding:0 10px 0px 10px",
			    	 					        items :[{
				    	 							xtype : 'textfield',
				    	 							id : 'comsendUser',
				    	 							name : 'comsendUser',
				    	 							fieldLabel : "专业室执行组长",
				    	 							readOnly : true,
				    	 							value :comsendUser1,
				    	 							width : 100
			    	 					        }]
			    	 			        	},{
			    	 							layout:'form',
			    	 							border : false,
			    	 							labelAlign : "right",
			    	 							bodyStyle : "padding:0 5px 0px 80px",
			    	 							items: [{
			    	 								xtype : 'textfield',
			    	 								id : 'comcreateDate',
			    	 								name : 'comcreateDate',
			    	 								width :128 ,
			    	 								//format: 'Y-m-d',
			    	 								value : comcreateDate1,
			    	 								style:{'text-align':'center'},
			    	 								//value : getNowDate(),
			    	 								fieldLabel : "日期"
//			    	 								 anchor:'95%'   
	    	 									}]						
		    	 				         }]
			    	 			},{
			    	 				layout:"form",
			    	 				border : false,
			    	 				labelAlign : "top",
			    	 				bodyStyle : 'padding : 0 5px 5px 5px',
			    	 				items :[{
			    	 					xtype : 'textarea',
			    	 					id : 'comreContent',
			    	 					name : 'comreContent',
			    	 					width :535 ,
			    	 					value :comreContent1, 
			    	 					maxLength : 2000,
			    						maxLengthText : "最大长度为 2000",
			    	 					fieldLabel : "答复"
			    	 				}]			
			    	 			},{
			    	 				layout : "table",
			    	 				border : false,
			    	 				bodyStyle : "padding:0 5px 0px 5px",
			    	 				layoutConfig : {columns:2},
			    	 				items : [{
			    	 					        layout:'form', 
			    	 					        border : false,
			    	 					        labelWidth : 92,
			    	 					        bodyStyle : "padding:0 10px 0px 10px",
			    	 					        items :[{
				    	 							xtype : 'textfield',
				    	 							id : 'comreceiveUser',
				    	 							name : 'comreceiveUser',
				    	 							width :100 ,
				    	 							fieldLabel : "专业室执行组长",
				    	 							readOnly : true,
				    	 							value :comreceiveUser1
			    	 					        }]
			    	 			        	},{
			    	 							layout:'form',
			    	 							border : false,
			    	 							labelAlign : "right",
			    	 							bodyStyle : "padding:0 10px 0px 80px",
			    	 							items: [{
			    	 								xtype : 'textfield',
			    	 								id : 'comreceiveDate',
			    	 								name : 'comreceiveDate',
			    	 								width :128 ,
			    	 								style:{'text-align':'center'},
			    	 								readOnly :true,
			    	 								value : getNowDate(),
			    	 								fieldLabel : "日期"
			    	 									}]						
			    	 				         }]
			    	 			}]
			    	 		})
		    			var window = new Ext.Window({
		    	 			layout : 'form',
		    	 			width : 575,
		    	 			height : 480,
		    	 			resizable : true,
		    	 			draggable : true,
		    	 			closeAction : 'close',
		    	 			closable : true,
		    	 			autoScroll : true,
		    	 			title : "协调单",
		    	 			modal : true,
		    	 			titleCollapse : true,
		    	 			buttonAlign : 'center',
		    	 			constrain : true,
		    	 			items : [coform],
		    	 			buttons : [{
		        	 			text : "同意",
								handler : function(){	
									if(taskType==GVI){
										jsonGVI=[];
										jsonGVI.push(coform.getForm().getValues());
										jsonGVI.push({"isReceive":"4","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
									}
									if(taskType==DIS){
										jsonDIS=[];
										jsonDIS.push(coform.getForm().getValues());
										jsonDIS.push({"isReceive":"4","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
									}
									if(taskType==DET){
										jsonDET=[];
										jsonDET.push(coform.getForm().getValues());
										jsonDET.push({"isReceive":"4","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
									}
									if(taskType==FNC){
										jsonFNC=[];
										jsonFNC.push(coform.getForm().getValues());
										jsonFNC.push({"isReceive":"4","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
										"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
										"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
									}
									window.close();
								}
							},{
								text : "不同意",
								handler : function(){
									Ext.Msg.confirm(commonality_affirm,commonality_affirmSaveMsg,function(btn){
							    		if(btn==='yes'){
							    			if(taskType==GVI){
							    				jsonGVI=[];
												jsonGVI.push(coform.getForm().getValues());
												jsonGVI.push({"isReceive":"3","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
												"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
												"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
											}
											if(taskType==DIS){
												jsonDIS=[];
												jsonDIS.push(coform.getForm().getValues());
												jsonDIS.push({"isReceive":"3","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
												"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
												"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
											}
											if(taskType==DET){
												jsonDET=[];
												jsonDET.push(coform.getForm().getValues());
												jsonDET.push({"isReceive":"3","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
												"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
												"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
											}
											if(taskType==FNC){
												jsonFNC=[];
												jsonFNC.push(coform.getForm().getValues());
												jsonFNC.push({"isReceive":"3","type":type1,"taskCode":taskCode,"comsendUser1":Ext.getCmp('comsendUser').getValue(),
												"comsendWg1":Ext.getCmp('comsendWg').getValue(),"comreceiveWg1":Ext.getCmp('comreceiveWg').getValue(),
												"comreceiveUser1":Ext.getCmp('comreceiveUser').getValue(),"comCoordinationId":comCoordinationId});
											}
											window.close();
							    	   	}
									  })
									}
								},{
									text : "关闭",
									handler : function() {
										window.close();
									}
							}]
		    	 		});
		    	 		window.show();
		    		}
		    		
		    		//已经回复的协调单，仅供查看
		    	if(isreceive1 == '3'||isreceive1 == 'noPermission'||isreceive1 == '4'){	  
		    		 var coform = new Ext.form.FormPanel({
		    	 			id : 'formAdd',
		    	 			border : false,
		    	 			items :[{
		    	 				layout:'fit', 
		    	 		        border : false,
		    	 		        bodyStyle : "padding:10px 20px 15px 15px",//"上、右、下、左"
		    	 		        items :[{
			    	 				xtype : 'textfield',
			    	 				id : 'comcoordinationCode',
			    	 				name : 'comcoordinationCode',
			    	 				width :150 ,
			    	 				readOnly : true,
			    	 				style:{'text-align':'center'},
			    	 				value : comcoordinationCode1
		    	 		        }]
		    	 			},{
		    	 				layout:'fit', 
		    	 		        border : false,
		    	 		        bodyStyle : "padding:0 20px 15px 15px",//"上、右、下、左"
		    	 		        items :[{
			    	 				xtype : 'textfield',
			    	 				id : 'comcoordinationName',
			    	 				name : 'comcoordinationName',
			    	 				width :150 ,
			    	 				readOnly : true,
			    	 				style:{'text-align':'center'},
			    	 				value : "协调单"
		    	 		        }]
		    	 			},{
		    	 				layout:'form', 
		    	 		        border : false,
		    	 		        labelAlign : "left",
		    	 		        labelWidth : 32,
		    	 		        bodyStyle : "padding:0 5px 0 5px",//"上、右、下、左"
		    	 		        items :[{
			    	 				xtype : 'textfield',
			    	 				id : 'comtheme',
			    	 				name : 'comtheme',
			    	 				width :496 ,
			    	 				readOnly :true,
			    	 				fieldLabel : "主题",
			    	 				value : comtheme1
		    	 		        }]
		    	 			},{
		    	 				layout : "table",
		    	 				border : false,
		    	 				bodyStyle : "padding:5px 5px 0 5px",//"上、右、下、左"
		    	 				layoutConfig : {columns:3},
		    	 				items : [{
		    	 					        layout:'form', 
		    	 					        border : false,
		    	 					        labelWidth : 78,
		    	 					        items :[{
			    	 							xtype : 'textfield',
			    	 							id : 'comsendWg',
			    	 							name : 'comsendWg',
			    	 							width :80 ,
			    	 							value : comsendWg1,
			    	 							readOnly :true,
			    	 							fieldLabel :"发送专业室"
		    	 					        }]
		    	 			        	},{
		    	 							layout:'form',
		    	 							border : false,
		    	 							labelWidth : 78,
		    	 							labelAlign : "right",
		    	 							items: [{
		    	 								xtype : 'textfield',
		    	 								id : 'comreceiveWg',
		    	 								name : 'comreceiveWg',
		    	 								width :100 ,
		    	 								border : false, 
		    	 								readOnly :true,
		    	 								value : comreceiveWg1,
		    	 								fieldLabel : "接收专业室"
    	 									}]						
		    	 				         },{
		    	 								layout:'form',
		    	 								border : false,
		    	 								labelWidth : 78,
		    	 								labelAlign : "right",
		    	 								items: [{
		    	 									xtype : 'textfield',
		    	 									id : 'comreceiveArea',
		    	 									name : 'comreceiveArea',
		    	 									width :100 ,
		    	 									readOnly : true,
		    	 									value : comreceiveArea1,
		    	 									fieldLabel : "接受区域"
    	 										}]						
	    	 					         }]
		    	 			},{
		    	 				layout:'form', 
		    	 		        border : false,
		    	 		        labelAlign : "top",
		    	 		        bodyStyle : "padding:0 5px 5px 5px",//"上、右、下、左"
		    	 		        items :[{
		    	 		        	xtype : 'textarea',
		    	 		        	id : 'comcontent',
		    	 		        	name : 'comcontent',
		    	 		        	width :535 ,
		    	 		        	readOnly :true,
		    	 		        	value :comcontent1,
		    	 		        	fieldLabel :  "内容"
		    	 		        }]
		    	 			},{
		    	 				layout : "table",
		    	 				border : false,
		    	 				bodyStyle : "padding:0 5px 0px 5px",
		    	 				layoutConfig : {columns:2},
		    	 				items : [{
		    	 					        layout:'form', 
		    	 					        border : false,
		    	 					        labelWidth : 92,
		    	 					        bodyStyle : "padding:0 10px 0px 10px",
		    	 					        items :[{
			    	 							xtype : 'textfield',
			    	 							id : 'comsendUser',
			    	 							name : 'comsendUser',
			    	 							fieldLabel : "专业室执行组长",
			    	 							readOnly : true,
			    	 							value :comsendUser1,
			    	 							width : 100
		    	 					        }]
		    	 			        	},{
		    	 							layout:'form',
		    	 							border : false,
		    	 							labelAlign : "right",
		    	 							bodyStyle : "padding:0 5px 0px 80px",
		    	 							items: [{
		    	 								xtype : 'textfield',
		    	 								id : 'comcreateDate',
		    	 								name : 'comcreateDate',
		    	 								width :128 ,
		    	 								//format: 'Y-m-d',
		    	 								readOnly :true,
		    	 								style:{'text-align':'center'},
		    	 								value : comcreateDate1,
		    	 								//value : getNowDate(),
		    	 								fieldLabel : "日期"
//		    	 								 anchor:'95%'   
    	 									}]						
	    	 				         }]
		    	 			},{
		    	 				layout:"form",
		    	 				border : false,
		    	 				labelAlign : "top",
		    	 				bodyStyle : 'padding : 0 5px 5px 5px',
		    	 				items :[{
		    	 					xtype : 'textarea',
		    	 					id : 'comreContent',
		    	 					name : 'comreContent',
		    	 					width :535 ,
		    	 					value :comreContent1, 
		    	 					readOnly :true,
		    	 					fieldLabel : "答复"
		    	 				}]			
		    	 			},{
		    	 				layout : "table",
		    	 				border : false,
		    	 				bodyStyle : "padding:0 5px 0px 5px",
		    	 				layoutConfig : {columns:2},
		    	 				items : [{
		    	 					        layout:'form', 
		    	 					        border : false,
		    	 					        labelWidth : 92,
		    	 					        bodyStyle : "padding:0 10px 0px 10px",
		    	 					        items :[{
			    	 							xtype : 'textfield',
			    	 							id : 'comreceiveUser',
			    	 							name : 'comreceiveUser',
			    	 							width :100 ,
			    	 							fieldLabel : "专业室执行组长",
			    	 							readOnly : true,
			    	 							value : comreceiveUser1
		    	 					        }]
		    	 			        	},{
		    	 							layout:'form',
		    	 							border : false,
		    	 							labelAlign : "right",
		    	 							bodyStyle : "padding:0 10px 0px 80px",
		    	 							items: [{
		    	 								xtype : 'textfield',
		    	 								id : 'comreceiveDate',
		    	 								name : 'comreceiveDate',
		    	 								width : 128 ,
		    	 								readOnly : true,
		    	 								style:{'text-align':'center'},
		    	 								value : comreceiveDate1,
		    	 								fieldLabel : "日期"
    	 									}]						
	    	 				         }]
		    	 			}]
		    	 		});
		    		   var window = new Ext.Window({
		    	 			layout : 'form',
		    	 			width : 575,
		    	 			height : 480,
		    	 			resizable : true,
		    	 			draggable : true,
		    	 			closeAction : 'close',
		    	 			autoScroll : true,
		    	 			closable : true,
		    	 			title : "协调单",
		    	 			modal : true,
		    	 			titleCollapse : true,
		    	 			buttonAlign : 'center',
		    	 			constrain : true,
		    	 			items : [coform],
		    	 			buttons : [{
		    	 				text : "关闭",
		    	 				handler : function() {
		    	 					window.close();
		    	 				}
		    	 			}]
		    	 		});
		    	 		window.show();
		    		}
		    	
		      	}	
			})

	}