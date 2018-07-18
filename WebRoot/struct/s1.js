var tempStr=null;
Ext.onReady(function(){
			var newGrid = null;
			var oldGrid = null;	
			Ext.form.Field.prototype.msgTarget = 'qtip';
			Ext.QuickTips.init(); 
			var centerForm = new Ext.form.FormPanel({
					labelWidth: 100,
				    autoScroll : true,  
				    labelAlign : "left",	  
				    frame:true,
				    items: [
				    		{
				    			xtype:'textarea',
				    			id:'s1Remark',
				    			value:s1Remark,
				    			width:660,
				    			height:60,
				    			fieldLabel:"备注"
				    		},{
					    		xtype:'textfield',
					    		id:'eff',
					    		value:ssiEff,
					    		fieldLabel:commonality_eff,
					    		width:660
				    		}			
					]
			});	
			var store = new Ext.data.Store({
				url : contextPath+"/struct/getS1Record.do?ssiId="+ssiId,
				reader : new Ext.data.JsonReader({
					 totalProperty : "totleCount",
					 root : "s1",
					 fields : [
					 	{name : 'id'},
					 	{name : 'ssiName'},
					 	{name : 'material'},
					 	{name : 'surface'},
					 	{name : 'ownArea'},
					 	{name : 'isMetal'},
					 	{name : 'internal'},
					 	{name : 'outernal'},
					 	{name : 'designPri'},
					 	{name : 'isFD'},
					 	{name : 'repairPassageway'}
				 	]
				})
			});
			
			store.on('load', function(){
					oldGrid = createGridJSON();
			});
			var colM = new Ext.grid.ColumnModel([
				{
			    	header : "ID", 
			    	dataIndex : "id", 
			    	hidden : true
		    	},{
					header : 'SSI   组成'+ commonality_mustInput, 
					dataIndex : "ssiName",
					width : 120,
			    	editor : new Ext.form.TextArea({ grow : true}),
			    	renderer : changeBR
		    	},{
					header :'材料',
					width : 120,
					dataIndex : "material",
					editor : new Ext.form.TextArea({ grow : true}),
					renderer : changeBR
		    	},
		    	{
					header :'表面处理',
					width : 120,
					dataIndex : "surface",
					renderer : changeBR,
					editor : new Ext.form.TextArea({ grow : true})
		    	},
		    	{
					header : '所属区域'+ commonality_mustInput,
					width : 80,
					dataIndex : "ownArea",
					editor : new Ext.form.TextField({
						maxLength:500,
			    		allowBlank : false
			    	})
		    	},{
		    		header :'是否为金属'+ commonality_mustInput,
		    		dataIndex : "isMetal",
		    		width : 80,
					editor : new Ext.form.ComboBox({
						id:"metal",
						xtype : 'combo',
						store : new Ext.data.SimpleStore({
						fields : ["retrunValue", "displayText"],
						data : [['1',commonality_shi],['0',commonality_fou]]
						}),  
						valueField : "retrunValue", displayField : "displayText",
						typeAhead : true, mode : 'local',   
						triggerAction : 'all', emptyText : '请选择',   
						editable : false, selectOnFocus : true,
						width : 70
		    		}),
					renderer: function(value, meta, record) {
						    var index = Ext.getCmp("metal").store.find(Ext.getCmp('metal').valueField,value);				    
						    var record = Ext.getCmp("metal").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						}
		    	},{
		    		header : '内部'+ commonality_mustInput,
		    		dataIndex : "internal",
		    		width : 60,
					editor : new Ext.form.ComboBox({
					id:"internal",
					xtype : 'combo',
					store : new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
					data : [['1',commonality_shi],['0',commonality_fou]]
					}),  
					valueField : "retrunValue", displayField : "displayText",
					typeAhead : true, mode : 'local',   
					triggerAction : 'all', emptyText : '请选择',   
					editable : false, selectOnFocus : true,
					width : 70
		    		}),
					renderer: function(value, meta, record) {
						    var index = Ext.getCmp("internal").store.find(Ext.getCmp('internal').valueField,value);				    
						    var record = Ext.getCmp("internal").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						}
		    	},{
		    		header : '外部'+ commonality_mustInput,
		    		dataIndex : "outernal",
		    		width : 60,
					editor : new Ext.form.ComboBox({
					id:"outernal",
					xtype : 'combo',
					store : new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
					data : [['1',commonality_shi],['0',commonality_fou]]
					}),  
					valueField : "retrunValue", 
					displayField : "displayText",
					typeAhead : true, 
					mode : 'local',   
					triggerAction : 'all', 
					emptyText : '请选择',   
					editable : false,
					selectOnFocus : true,
					width : 70
		    		}),
					renderer: function(value, meta, record) {
						    var index = Ext.getCmp("outernal").store.find(Ext.getCmp('outernal').valueField,value);				    
						    var record = Ext.getCmp("outernal").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						}
		    	},{
					header : '设计原则'+ commonality_mustInput,
					dataIndex : "designPri",
					width : 140,
					renderer: function(value, meta, record) {
						    var index = Ext.getCmp("designPri").store.find(Ext.getCmp('designPri').valueField,value);				    
						    var record = Ext.getCmp("designPri").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						},
					editor : new Ext.form.ComboBox({
						id:"designPri",
						xtype : 'combo',
						store : new Ext.data.SimpleStore({
						fields : ["retrunValue", "displayText"],
						data : [['1','安全寿命设计'],['2','损伤容限设计'],['3','其他']]
						}),  
						valueField : "retrunValue",
						displayField : "displayText",
						typeAhead : true, 
						mode : 'local',   
						triggerAction : 'all', 
						emptyText : '请选择',   
						editable : false, 
						selectOnFocus : true,
						width : 70
		    		})
		    	},{
		    		header :'是否FD分析'+ commonality_mustInput,
		    		dataIndex : "isFD",
		    		width : 80,
					editor : new Ext.form.ComboBox({
						id:"isFD",
						xtype : 'combo',
						store : new Ext.data.SimpleStore({
						fields : ["retrunValue", "displayText"],
						data : [['1',commonality_shi],['0',commonality_fou]]
						}),  
						valueField : "retrunValue", displayField : "displayText",
						typeAhead : true, mode : 'local',   
						triggerAction : 'all', emptyText : '请选择',   
						editable : false, selectOnFocus : true,
						width : 70
		    		}),
					renderer: function(value, meta, record) {
						    var index = Ext.getCmp("isFD").store.find(Ext.getCmp('isFD').valueField,value);				    
						    var record = Ext.getCmp("isFD").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						}
		    	},{
		    		header :'维修通道',
					width : 120,
					dataIndex : "repairPassageway",
					editor : new Ext.form.TextArea({ grow : true}),
					renderer : changeBR
		    	}
			]);

			var grid = new Ext.grid.EditorGridPanel({
				title :  returnPageTitle('结构维修项目编辑','s1'),
				region: 'center',
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				store : store,
				loadMask: {msg:commonality_transferWaitMsg},
				clicksToEdit :2,   
				stripeRows: true,				
				tbar : [new Ext.Button({
							text : '追加',
							disabled:isMaintain==0?true:false,
							iconCls : 'icon_gif_add',
							handler : addRow
				        }),
				        new Ext.Button({
							text : commonality_del,
							disabled:isMaintain==0?true:false,
							iconCls : 'icon_gif_delete',
							handler : del
				        })
					]
			});
			store.on('load', function(){
					oldGrid = createGridJSON();
			});
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			
			var viewport = new Ext.Viewport( {
				id:'viewportId',
				layout : 'border',
				items : [
					{ 
						region : 'north',
						height : 60,
						frame : true,
						items : [headerStepForm]
					},{ 
						layout: 'border',
					    region : 'center',
			    	    items : [
							{
								layout: 'fit',
								region : 'center',
								items: [grid]
							},{
								layout: 'fit',
								height :120,
								region : 'north',
								items: [centerForm]
							}
			    	    ]
					}
				]
			});
			
			//创建GRIDPANEL的JSON
			function createGridJSON(){
				var json = [];
				for (var i = 0; i < store.getCount(); i++){
					json.push(store.getAt(i).data);
				}
				return Ext.util.JSON.encode(json);
			}
			
			function testRecord(){
				if (store.getCount() == 0){
					alert('请填写相关信息');
					return false;
				}
				for(var i=0;i<store.getCount();i++){
					var rec = store.getAt(i);
					if(rec.get('ssiName')==''||rec.get('ssiName')==null){
						alert("请输入SSI组成");
						return false;
					}
					if(rec.get('ownArea')==''||rec.get('ownArea')==null){
						alert('请输入所属区域');
						return false;
					}
					if(rec.get('outernal')===''||rec.get('outernal')==null){
						alert('请选择外部');
						return false;
					}else if(rec.get('internal')===''||rec.get('internal')==null){
						alert('请选择内部');
						return false;
					}else if(rec.get('outernal')==0&&rec.get('internal')==0){
						alert('请选择内部或者外部');
						return false;
					}
					if(rec.get('designPri')==''||rec.get('designPri')==null){
						alert("请输入设计原则");
						return false;
					}
				}
				return true;
			}
			
			
			nextOrSave=function updateDataDetail(number) {
				newGrid = createGridJSON();
				if(number){
					if (step[number] == 0){
						alert('请先完成之前的步骤！');
						return false;
					}
				}
				tempStr=Ext.getCmp("s1Remark").getValue();
				
				if (isMaintain==0||(ssiEff==Ext.getCmp('eff').getValue())&&((newGrid == oldGrid )&&(tempStr==s1Remark))){
					if(number){
						goNext(number);
						return null;
					}
					goNext(2);
					return null;
				}
				
				if (!testRecord()){
					return false;
				}else{
					Ext.MessageBox.show({
					    title : commonality_affirm,
					    msg : commonality_affirmSaveMsg,				    
					    buttons : Ext.Msg.YESNOCANCEL,
					    fn : function(id){
					    	if (id == 'cancel'){
								return;
							} else if (id == 'yes'){
								save(number);
							} else if (id == 'no'){
								if(number){
									goNext(number);
									return null;
								}
								goNext(2);
						}
					    }
					});	
				}
			};
			
			function getJsonStr(){
				var jsonStr=[];
				Ext.each(store.data.items, function(item) {
					var data = item.data;
					jsonStr.push(item.data);
				});
				return jsonStr;
			}
			
			function save(number){
				var json=getJsonStr();
				var modifiedJson = [];
				Ext.each(store.modified,function(item){
					modifiedJson.push(item.data);
				});
				if(json!=null){
					if(Ext.getCmp("s1Remark")){
						s1Remark=Ext.getCmp("s1Remark").getValue();
					};
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true// 完成后移除
						});
						
						waitMsg.show();
						//alert(ssiId);//测试
						Ext.Ajax.request( {
							url :contextPath+"/struct/saveS1Records.do",
							waitTitle : commonality_waitTitle,
							waitMsg : commonality_waitMsg,
							//async: false,//测试
							params : {
								jsonData : Ext.util.JSON.encode(json),
								modifiedJson : Ext.util.JSON.encode(modifiedJson),
								ssiId:ssiId,
								defaultEff:Ext.getCmp('eff').getValue(),
								s1Remark:s1Remark
							},
							method : "POST",
							success : function(response) {
									store.modified=[];
									store.reload();
									waitMsg.hide();
									alert(commonality_messageSaveMsg);	
									parent.refreshTreeNode();
									//alert(number);//测试
									if(number){
										successNext(number);
										return null;
									}
								successNext(2);
							},
							failure : function() { 
							  waitMsg.hide();
						      alert(commonality_cautionMsg); 
					     	} 
						});
				}
			}
			
			//验证数字
			function testNum(value){
				var re = /^[\d]+$/;
				if (re.test(value)){
					return true;
				} else {
					return false;
				}
				return true;
			}
			
			grid.addListener("beforeedit", beforeedit);			
			function beforeedit(val){
				if(isMaintain==0){
					val.cancel = true;
				}
			}
			function del(){
				var record  = grid.getSelectionModel().getSelected();
				if (!record){
					Ext.Msg.alert(commonality_waitTitle, commonality_alertDel);
					return;
				}
				
				Ext.Msg.confirm(commonality_caution, commonality_affirmDelMsg, function(btn){
					if('yes' == btn){
						if (record.get('id') == ''){
							store.modified.remove(record);
							store.remove(record);	
							return;
						}
						
						Ext.Ajax.request({
						url : contextPath+"/struct/delS1Record.do",
							params : {
								id : record.get('id'),
								ssiId:ssiId
							},
							success : function(){
								store.reload();
								parent.refreshTreeNode();
								location.replace(location.href);
								Ext.Msg.alert(commonality_waitTitle, commonality_messageDelMsg);
							},
							failure : function(){
								Ext.Msg.alert(commonality_waitTitle, commonality_messageDelMsgFail);
							}
						});
					}
				});
			}
			
			function afteredit(val){
				if (val.field == 'ownArea'){
					var nowRecord=val.record;
					var zone = val.value.split(",");
					if (val.value == ''){
						return true;
					}
					var zone = val.value.split(",");			
					for (var i = 0; i < zone.length; i++){
						if (zone[i] == ''){
							continue;
						}
					if (!testNum(zone[i])){
							alert('区域输入错误，请用‘,’分隔');
							nowRecord.set('ownArea', val.originalValue);
							return false;
						}
					}
				Ext.Ajax.request({
					url : contextPath+"/struct/verifyS1Area.do?",
					params:{
						verifyStr:val.value,
						id : nowRecord.get("id")
					},
					success : function(response) {
						if(response.responseText){
							var msg= Ext.util.JSON.decode(response.responseText);
							if(msg.exists){
								alert("区域:"+ msg.exists +"不能重复,请重新修改");
								nowRecord.set('ownArea',val.originalValue);
								return ;
							}
							if(msg.unExists){
								alert("区域:"+ msg.unExists+"不存在,请重新修改");
								nowRecord.set('ownArea',val.originalValue);
								return ;
							}
							if (msg.success) {
								alert("不能转入区域:"+ msg.success+"，该区域已被冻结或者已经审批完成，请重新修改");
								nowRecord.set('ownArea',val.originalValue);
								return ;
							}
							if(msg.modify){
								alert("当前S1已经产生任务数据，区域不能修改");
								nowRecord.set('ownArea',val.originalValue);
								return ;
							}
						}
					}
				});
			}
		}
			
			
			//增加空白行
			function addRow(){
				var rec = grid.getStore().recordType;
				var p = new rec({
					id:'',
					ssiName:'',
					material:'',
					surface:'',
					ownArea:'',
					isMetal:1,
					internal:'',
					outernal:'',
					designPri : '',
					isFD : 0,
					repairPassageway : ''
				});
				store.insert(grid.getStore().getCount(), p);
			}	
			
			grid.addListener("afteredit", afteredit);
			store.load();
});

