var flagTable1_1 = null;
var flagTable1_2 = null;
var table1Cell=null;
var table1Row=null;
var table2Cell=null;
var table2Row=null;
var table3Cell=null;
var table3Row=null;
var table4Cell=null;
var table4Row=null;
var temp=null;
var temp1=null;
var table1Value=null;
var table2Value=null;
var table3Value=null;
var flagTable2_1 = null;
var flagTable2_2 = null;
var flagTable3_1 = null;
var flagTable3_2 = null;
var flagTable4_1 = null;
var flagTable4_2 = null;
var basicCrack=null;
var jsonStr=null;
Ext.onReady(function(){
		Ext.form.Field.prototype.msgTarget = 'qtip';
			Ext.QuickTips.init(); 
			var gdStore = new Ext.data.Store({
				url : contextPath+"/struct/s3/getGDRecords.do?ssiId="+ssiId,
				reader : new Ext.data.JsonReader({
					 root : "s3",
					 fields : [
					 	{name : 'id'},
					 	{name : 'ownArea'},
					 	{name : 's3cId'},
					 	{name : 'ssiName'},
					 	{name : 'isAdEffect'},
					 	{name : 'taskType'},
					 	{name : 'basicCrack'},
					 	{name : 'materialSize'},
					 	{name : 'edgeEffect'},
					 	{name : 'detectCrack'},
					 	{name : 'lc'},
					 	{name : 'lo'},
					 	{name : 'detailCrack'},
					 	{name : 'taskInterval'},
					 	{name : 'taskIntervalRepeat'},
					 	{name : 'isOk'},
					 	{name : 'remark'},
					 	{name : 'picUrl'},
					 	{name : 's1Id'},
					 	{name : 'intOut'},
					 	{name : 'densityLevel'},
					 	{name : 'lookLevel'},
					 	{name : 'reachLevel'},
					 	{name : 'lightLevel'},
					 	{name : 'conditionLevel'},
					 	{name : 'surfaceLevel'},
					 	{name : 'sizeLevel'},
					 	{name : 'doLevel'},
					 	{name:'s1Id'},
					 	{name:'taskId'},
					 	{name:'tempTaskId'},
					 	{name:'picUrlIsNull'}
				 	]
				})
			});
					
			var sStore = new Ext.data.Store({
				url : contextPath+"/struct/s3/getSdiRecords.do?ssiId="+ssiId,
				reader : new Ext.data.JsonReader({
					 root : "s3",
					 fields : [
					 	{name : 'id'},
					 	{name : 'ownArea'},
					 	{name : 'ssiName'},
					 	{name : 'taskType'},
					 	{name : 'detailSdi'},
					 	{name : 'taskInterval'},
					 	{name : 'taskIntervalRepeat'},
					 	{name : 'isOk'},
					 	{name : 'remark'},
					 	{name : 'intOut'},
					 	{name : 'detailCrack'},
					 	{name : 's1Id'},
					 	{name : 'taskId'}
				 	]
				})
			});
				
				var gdCm = new Ext.grid.ColumnModel([
				{
			    	header : "ownArea", 
			    	dataIndex : "ownArea", 
			    	hidden : true
		    	},{
			    	header : "ID", 
			    	dataIndex : "id", 
			    	hidden : true
		    	},{
			    	header : "s3cID", 
			    	dataIndex : "s3cId", 
			    	hidden : true
		    	},{
					header :'SSI 组成', 
					dataIndex : "ssiName",
					width : 120,
					renderer : changeBR
		    	},{
		    		header :'是否考虑AD影响'+ commonality_mustInput,
		    		dataIndex : "isAdEffect",
		    		width : 120,
					editor : new Ext.form.ComboBox({
					editable:false,
					id:"gdisAdEffect",
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
						    var index = Ext.getCmp("gdisAdEffect").store.find(Ext.getCmp('gdisAdEffect').valueField,value);				    
						    var record = Ext.getCmp("gdisAdEffect").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						}
		    	},{
					header : '任务类型'+ commonality_mustInput, 
					dataIndex : "taskType",
					width : 80
		    	},{
					header : '基本裂纹长度'+ commonality_mustInput, 
					dataIndex : "basicCrack",
					width : 108,
					editor : new Ext.form.NumberField({
			    	allowBlank : false,
			    	listeners : {
	               			"focus" : function(field) {
	               				var record  = gdGrid.getSelectionModel().getSelected();
	               				function beforeClose(){
	         	               					
	         	               				}
	               				showWin(record, 'material', beforeClose);
							}
			    	
						}
			    	})
		    	},{
					header : '材料尺寸因子'+ commonality_mustInput,
					width : 100,
					dataIndex : "materialSize",
					editor : new Ext.form.NumberField({
					    decimalPrecision:2,                 //精确到小数点后2位(执行4舍5入)   
            			allowDecimals:true,                //允许输入小数 
            			value:0,
            			maxValue:999,
            			enableKeyEvent:true,
			    		allowBlank : false
			    	})
		    	},{
		    		header :'边缘影响因子'+ commonality_mustInput,
		    		dataIndex : "edgeEffect",
		    		width :100,
					editor : new Ext.form.NumberField({
					    decimalPrecision:2,                 //精确到小数点后2位(执行4舍5入)   
              			allowDecimals:true,                //允许输入小数 
              			value:0,
              			maxValue:999,
			    		allowBlank : false,
			    		enableKeyEvent:true
			    	})
		    	},{
		    		header :'可检裂纹长度'+ commonality_mustInput,
		    		dataIndex : "detectCrack",
		    		width : 110
		    	},{
		    		header : "Lc"+ commonality_mustInput,
		    		dataIndex : "lc",
		    		width : 50,
					editor : new Ext.form.NumberField({
					    decimalPrecision:2,                 //精确到小数点后2位(执行4舍5入)   
              			allowDecimals:true,                //允许输入小数 
              			value:0,
              			maxValue:999,
			    		allowBlank : false
			    	})
		    	},{
					header : "Lo"+ commonality_mustInput, 
					dataIndex : "lo",
					width : 50 ,
					editor : new Ext.form.NumberField({
					    decimalPrecision:2,    
					    maxValue:999,             //精确到小数点后2位(执行4舍5入)   
           			    allowDecimals:true,                //允许输入小数 
			    		allowBlank : false
			    	})
				},{
					header : '详细检查可检裂纹长度'+ commonality_mustInput,
					dataIndex : "detailCrack" ,
				   width : 140
		    	},{
					header :'检查门槛值'+ commonality_mustInput,
					 width : 100,
					dataIndex : "taskInterval" ,
					editor : new Ext.form.TextField({
						maxLength:100,
			    		allowBlank : false
			    	})
		    	},{
					header : '重复检查间隔'+ commonality_mustInput,
					width : 100,
					dataIndex : "taskIntervalRepeat",
					editor : new Ext.form.TextField({
						maxLength:100,
			    		allowBlank : false
			    	}) 
		    	},{
					header : '检查任务是否合适有效'+ commonality_mustInput,
					width : 140,
					dataIndex : "isOk",
					editor : new Ext.form.ComboBox({
					editable:false,
					id:"gdisOk",
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
						    var index = Ext.getCmp("gdisOk").store.find(Ext.getCmp('gdisOk').valueField,value);				    
						    var record = Ext.getCmp("gdisOk").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						} 
		    	},{
					header : '内外/外部'+ commonality_mustInput,
					width : 120,
					dataIndex : "intOut",
					editor : new Ext.form.ComboBox({
					editable:false,
					allowBlank : false,
					id:"gdintOut",
					xtype : 'combo',
					store : new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
						data : [['1','外'],['0','内'],['2','内/外部']]
					}),  
					valueField : "retrunValue", displayField : "displayText",
					typeAhead : true, mode : 'local',   
					triggerAction : 'all', emptyText :'请选择',   
					editable : false, selectOnFocus : true,
					width : 70
		    		}),
					renderer: function(value, meta, record) {
						    var index = Ext.getCmp("gdintOut").store.find(Ext.getCmp('gdintOut').valueField,value);				    
						    var record = Ext.getCmp("gdintOut").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						} 
		    	},{
					header : '备注',
					width : 100,
					dataIndex : "remark" ,
					renderer : changeBR,
					editor : new Ext.form.TextArea({ grow : true})
		    	},{
					header :'裂纹扩展图片',
					width : 120,
					dataIndex : "picUrlIsNull",
					editor : new Ext.form.TextField({
			    	listeners : {
	               			"focus" : function(field) {
	               				var record  = gdGrid.getSelectionModel().getSelected();
	               				function beforeClose(){
	         	               					
	         	               				}
	               				picUp(record);
							}
			    	
						}
			    	})
		    	},{
					header : '稠密度等级',
					width : 120,
					dataIndex : "densityLevel" , 
			    	hidden : true
		    	},{
					header : '目视等级',
					width : 120,
					dataIndex : "lookLevel" , 
			    	hidden : true
		    	},{
					header : "s1Id",
					width : 120,
					dataIndex : "s1Id" , 
			    	hidden : true
		    	},{
					header :'接近等级',
					width : 120,
					dataIndex : "reachLevel" , 
			    	hidden : true
		    	},{
					header : '光照等级',
					width : 120,
					dataIndex : "lightLevel" , 
			    	hidden : true
		    	},{
					header : '条件等级',
					width : 120,
					dataIndex : "conditionLevel", 
			    	hidden : true 
		    	},{
					header : '表面等级',
					width : 120,
					dataIndex : "surfaceLevel", 
			    	hidden : true 
		    	},{
					header :'尺寸等级',
					width : 120,
					dataIndex : "sizeLevel", 
			    	hidden : true 
		    	},{
					header : '实用性等级',
					width : 120,
					dataIndex : "doLevel", 
			    	hidden : true 
		    	},{
		    		header : "taskId",
		    		dataIndex : "taskId",
		    		hidden : true
		    	},{
		    		header : "tempTaskId",
		    		dataIndex : "tempTaskId",
		    		hidden : true
		    	}
			]);

	

				var sCm = new Ext.grid.ColumnModel([
				{
			    	header : "ID", 
			    	dataIndex : "id", 
			    	hidden : true
		    	},{
			    	header : "ownArea", 
			    	dataIndex : "ownArea", 
			    	hidden : true
		    	},{
					header : 'SSI 组成', 
					dataIndex : "ssiName",
					width : 120
		    	},{
					header : '任务类型', 
					dataIndex : "taskType",
					width : 120
		    	},{
					header : '推荐的特别详细检查SDI'+ commonality_mustInput, 
					dataIndex : "detailSdi",
					width : 160,
					renderer : changeBR,
					editor : new Ext.form.TextArea({
						maxLength:250,
			    		allowBlank : false,
						grow : true
			    	})
		    	},{
		    		header : '详细检查可检裂纹长度'+ commonality_mustInput,
		    		dataIndex : "detailCrack",
		    		width :150,
					editor : new Ext.form.NumberField({
					    decimalPrecision:2,                 //精确到小数点后2位(执行4舍5入)   
           			    allowDecimals:true,                //允许输入小数 
			    		allowBlank : false
			    	})
		    	},{
					header : '检查门槛值'+ commonality_mustInput,
			    	width : 80,
					dataIndex : "taskInterval" ,
					editor : new Ext.form.TextField({
						maxLength:100,
			    		allowBlank : false
			    	})
		    	},{
					header : "s1Id",
					width : 80,
					dataIndex : "s1Id" , 
			    	hidden : true
		    	},{
					header : '重复检查间隔'+ commonality_mustInput,
					width : 100,
					dataIndex : "taskIntervalRepeat",
					editor : new Ext.form.TextField({
						maxLength:100,
			    		allowBlank : false
			    	}) 
		    	},{
					header :'检查任务是否合适有效',
						width : 140,
					dataIndex : "isOk",
					editor : new Ext.form.ComboBox({
					editable:false,
					id:"sisOk",
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
						    var index = Ext.getCmp("sisOk").store.find(Ext.getCmp('sisOk').valueField,value);				    
						    var record = Ext.getCmp("sisOk").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						} 
		    	},{
					header : '内外/外部'+ commonality_mustInput,
					width : 120,
					dataIndex : "intOut",
					editor : new Ext.form.ComboBox({
					editable:false,
					allowBlank : false,
					id:"sintOut",
					xtype : 'combo',
					store : new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
						data : [['1','外'],['0','内'],['2','内/外部']]
					}),  
					valueField : "retrunValue", displayField : "displayText",
					typeAhead : true, mode : 'local',   
					triggerAction : 'all', emptyText :'请选择',   
					editable : false, selectOnFocus : true,
					width : 70
		    		}),
					renderer: function(value, meta, record) {
						    var index = Ext.getCmp("sintOut").store.find(Ext.getCmp('sintOut').valueField,value);				    
						    var record = Ext.getCmp("sintOut").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						} 
		    	},{
					header :'备注',
					width : 120,
					dataIndex : "remark" ,
					renderer : changeBR,
					editor : new Ext.form.TextArea({ grow : true})
		    	},{
		    		header : "taskId",
		    		dataIndex : "taskId",
		    		hidden : true
		    	}
			]);






			var gdGrid = new Ext.grid.EditorGridPanel({
				title : 'GVI/DET分析',
				region: 'north',
				cm : gdCm,
				height:300,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				store : gdStore,
				clicksToEdit : 2,   
				stripeRows: true 
			
			})
			var sdiGrid = new Ext.grid.EditorGridPanel({
				title : 'SDI分析',
				region: 'center',
				//height:100,
				cm : sCm,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				store : sStore,
				clicksToEdit : 2,   
				stripeRows: true 
			})
			gdStore.load();
			sStore.load();
			sStore.on('load', function(){
					oldSdiGrid = createSdiGridJSON();
			});
			gdStore.on('load', function(){
					oldgdGrid = creategdGridJSON();
			});
			
			
			function createSdiGridJSON(){
				var json = [];
				for (var i = 0; i < sStore.getCount(); i++){
					json.push(sStore.getAt(i).data);
				}
				return Ext.util.JSON.encode(json);
			}
			
			function creategdGridJSON(){
				var json = [];
				for (var i = 0; i < gdStore.getCount(); i++){
					json.push(gdStore.getAt(i).data);
				}
				return Ext.util.JSON.encode(json);
			}
			
			gdGrid.addListener("beforeedit", beforeedit);	
			
			function beforeedit(val){
				if(isMaintain==0){
					val.cancel = true;
				}
			}
			gdGrid.addListener("afteredit", afteredit);	
			function afteredit(val){
			    		var record=val.record;
			    		var tempTaskId = record.get("taskId");
				    		if(record.get("edgeEffect")!=null&&record.get("basicCrack")!=null&&record.get("materialSize")!=null){
			    				record.set("detectCrack",(record.get("edgeEffect")*record.get("materialSize")*record.get("basicCrack")).toFixed(2));
			    			
			    			}
			    			if(record.get("lc")!=null&&record.get("lo")!=null&&record.get("detectCrack")!=null){
			    				var lc=record.get("lc")-0;
			    				var lo=record.get("lo")-0;
			    				var detectCrack=record.get("detectCrack")-0;
			    				record.set("detailCrack",(lc+lo+detectCrack).toFixed(2));
			    			
			    			}
			    			
			    			if(record.get("isOk")=='0'&&record.get("taskType")==GVI){
			    				record.set("isAdEffect","");
			    				record.set("basicCrack","");
			    				record.set("materialSize","");
			    				record.set("edgeEffect","");
			    				record.set("detectCrack","");
			    				record.set("lc","");
			    				record.set("lo","");
			    				record.set("isOk","");
			    				record.set("taskType",DET);
			    				record.set("detailCrack","");
			    				record.set("taskInterval","");
			    				record.set("taskIntervalRepeat","");
			    				record.set("remark","");
			    				record.set("densityLevel","");
								record.set("lookLevel","");
								record.set("reachLevel","");
								record.set("lightLevel","");
								record.set("conditionLevel","");
								record.set("surfaceLevel","");
								record.set("sizeLevel","");
								record.set("doLevel","");
								record.set("taskId","");
								record.set("tempTaskId",tempTaskId);
			    			}
			    			if(record.get("isOk")=='0'&&record.get("taskType")==DET){
			    					var rec = sdiGrid.getStore().recordType;
									var p = new rec({
										id:record.get("id"),
										ssiName:record.get("ssiName"),
										taskType:SDI,
										detailSdi:'',
										detectCrack:'',
										taskInterval:'',
										taskIntervalRepeat:'',
										isOk : '',
										remark:'',
										s1Id:record.get('s1Id'),
										ownArea:record.get('ownArea'),
										taskId : "",
										tempTaskId : tempTaskId
									});
									sStore.insert(sdiGrid.getStore().getCount(), p);
									gdStore.modified.remove(record);
			    					gdGrid.getStore().remove(record);
								}	
			    				
			}
			
			//导航
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			
			
			
			var view = new Ext.Viewport({
				layout : 'border',
			    items : [
			    	{ 
			    		region : 'north',
			    		height : 60,
			    		frame : true,
			    		items : [headerStepForm]
			    	},
					{
						title :  returnPageTitle('结构维修项目维修信息编辑','s3'),
						region : 'center',
						layout : 'border',
						items: [gdGrid,sdiGrid]
					}
				]
			});
			
			//点击下一步执行的方法
			nextOrSave = function (number){
				for(var i =0;i<gdStore.modified.length;i++){
					if((gdStore.modified[0].data.detectCrack+"").length>9){
						alert('第'+(i-0+1)+'行数据的可检测裂纹长度超长');
						return null;
					}
					
				}
				for(var i =0;i<gdStore.modified.length;i++){
					if((gdStore.modified[0].data.detailCrack+"").length>9){
						alert('第'+(i-0+1)+'行数据的详细检查可检测裂纹长度超长');
						return null;
					}
					
				}
				if ((sStore.modified.length==0&&gdStore.modified.length==0)||isMaintain==0){
					if(number){
						goNext(number);
						return null;
					}				
					if(step[4] !=3){
						goNext(4);
						return 
					}else if(step[6] !=3){
						goNext(6);return null;
					}else if(step[9] !=3){
						goNext(9);return null;
					}else if(step[11] !=3){
						goNext(11);return null;
					}
				}
			
				if(number&&gdStore.modified.length==0&&sStore.modified.length==0){
					goNext(number);
					return null;
				}
				if (!test(number)){ 
					return false;
				}
				Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg :commonality_affirmSaveMsg,
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
						if(step[4] !=3){
							goNext(4);
						}else if(step[6]!=3){
							goNext(6);
						}else if(step[9]!=3){
							goNext(9);
						}else if(step[11]!=3){
							goNext(11);
						}
						}
				    }
				});
			}
			function test(number){
				if((sStore.modified.length!=0||gdStore.modified.length!=0)){
					return true;
				}
				if(number){
					if (step[number] == 0){
						alert('请先完成之前的步骤！');
						return false;
					}
				}
				if(step[3]==2){
					return true;
				}
				if(step[number]!=1&&step[number]!=3&&!number){
					return true;
				}
				if(step[number] == 1&&step[number]==1){
					successNext(number);
					return false
				}else if(step[6] == 1&&step[3]==1){
					successNext(6);return false
				}else if(step[9] == 1&&step[3]==1){
					successNext(9);return false
				}else if(step[11] == 1&&step[3]==1){
					successNext(11);return false
				}
	
				return true;
			}
			
			function getJsonStr(){
				var jsonStr=[];
				Ext.each(sStore.modified, function(item) {
					var data = item.data;
					jsonStr.push(data);
				});
				Ext.each(gdStore.modified, function(item) {
					var data = item.data;
					jsonStr.push(data);
				});
				return jsonStr;
			}
			
			
	var checkBlank = function(modified/*所有编辑过的和新增加的Record*/,cm){  
        var result = true;  
        Ext.each(modified,function(record){  
            var keys = record.fields.keys;//获取Record的所有名称   
            Ext.each(keys,function(name){  
                //根据名称获取相应的值   
                var value = record.data[name];  
                //找出指定名称所在的列索引   
                var colIndex = cm.findColumnIndex(name);  
                 if(name=="materialSize"||name=="edgeEffect"||name=='picUrl'||name=='remark'){
                 	 return result;  
                 }
                //根据行列索引找出组件编辑器   
                if(cm.getCellEditor(colIndex)){
                 if(Ext.isEmpty(value)){
                	alert('对不起，请完整输入信息');  
                    result = false;  
                    return result;  
                }
                var editor = cm.getCellEditor(colIndex).field;  
                //验证是否合法   
                var r = editor.validateValue(value+"");  
                if(!r){  
                   	alert('对不起，请完整输入信息');  
                    result = false;  
                    return result;  
                }  
                  }
            });  
        });  
        return result;  
    }  
			
			
		function save(number){
			var json=getJsonStr();
			if(json!=null&&checkBlank(sStore.modified,sCm)&&checkBlank(gdStore.modified,gdCm)){
			var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : commonality_waitMsg,
				    removeMask : true// 完成后移除
					});
			
				waitMsg.show();
				Ext.Ajax.request( {
					url :contextPath+"/struct/s3/saveS3Records.do",
					params : {
						jsonData : Ext.util.JSON.encode(json),
						ssiId:ssiId
					},
					waitTitle : commonality_waitTitle,
					waitMsg : commonality_waitMsg,
					method : "POST",
					success : function(response) {
									waitMsg.hide();
									if(number){
										goNext(number);
										return null;
									}
									 alert(commonality_messageSaveMsg);	
									 if(response.responseText=='false'){
									 	alert('请先维护S4或者S5的矩阵');
									 }else{
										successNext(response.responseText);
										parent.refreshTreeNode();
									}
								},
					failure : function() { 
								  waitMsg.hide();
							      alert(commonality_cautionMsg); 
							      if(sStore.modified){
						         	 sStore.modified.removeAll();
							      }
							       if(gdStore.modified){
						         	     gdStore.modified.removeAll();
							      }
						      
								     } 
			});
				}
			}
			//矩阵弹窗开始
			function showWin(record, name, beforeClose){
				
				var ADForm = new Ext.form.FormPanel({
						labelWidth: 80, // label settings here cascade unless overridden
					    labelAlign : "right",
					    frame:true,	
					    items: [new Ext.form.Label({
					    	html : '<table id="editTable1" width="540" height="100"><tr>'+
						    	'<td  width="148"><table id="table1" border="1" height="130" width="148" cellpadding="1" cellspacing="0" style="text-align:center; background-color:#DFE8F6; padding:0; margin:0;" >'+
						    	'<tr><td colspan="2" rowspan="2">'+'评级1'+'</td><td colspan="3">'+'稠密度等级'+'</td></tr><tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable1Row(1,0);" width="21">1</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable1Row(1,1);" width="21">2</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable1Row(1,2);" width="26">3</td></tr>'+
						    	'<tr><td rowspan="4" width="17">'+'目视等级'+'</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable1Cell(2,1);" width="21">0</td><td>0</td><td>0</td><td>0</td></tr>'+
						    	'<tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable1Cell(3,0);">1</td><td >1</td><td>2</td><td>3</td></tr><tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable1Cell(4,0);">2</td><td>2</td><td >3</td><td>3</td></tr>'+
						    	'<tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable1Cell(5,0);">3</td><td>3</td><td>4</td><td>5</td></tr></table></td>'+
						    	'<td  width="80"><table><tr><td><td></tr></table></td>'+
						    	'<td  width="148"><table id="table2" width="130" height="130" border="1" cellpadding="1" cellspacing="0" style="text-align:center; background-color:#DFE8F6; padding:0; margin:0;" >'+
						    	'<tr><td colspan="2" rowspan="2">'+'评级3'+'</td><td colspan="3">'+'光照等级'+'</td></tr><tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable2Cell(1,0);" width="18">1</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable2Cell(1,1);" width="19">2</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable2Cell(1,2);" width="19">3</td></tr>'+
						    	'<tr><td width="16" rowspan="2">'+'表面等级'+'</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable2Row(2,1);"  width="20" height="30">1</td><td>1</td><td>2</td><td>3</td></tr>'+
						    	'<tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable2Row(3,0);">2</td><td>2</td><td>3</td><td>4</td></tr>'+
						    	'</table></td></tr>'+
						    	'<tr><table><tr><td><div style="float:left; margin-left:60px;display:inline;"><img src="/mpas/struct/down.gif" width="50" height="80"></img></div><div style="float:left; margin-left:280px;display:inline;"><img src="/mpas/struct/down.gif" width="50" height="80"></img></div></td></tr></table><tr>'+
						    	'<tr><td colspan="2"><div style="float:left "><table id="table3" border="1" height="170" width="180" cellpadding="1" cellspacing="0" style="text-align:center; background-color:#DFE8F6; padding:0; margin:0;" >'+ 
						    	'<tr><td colspan="2" rowspan="2">'+'评级2'+'</td><td colspan="5">'+'接近等级'+'</td></tr><tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" width="19">1</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" width="19">2</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" width="24">3</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" width="24">4</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" width="29">5</td></tr>'+
						    	'<tr><td rowspan="4" width="16">'+'尺寸等级'+'</td><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable3Row(2,1);" width="19">1</td><td>1</td><td>1</td><td>2</td><td>2</td><td>3</td></tr>'+
						    	'<tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable3Row(3,0);">2</td><td>1</td><td>2</td><td>2</td><td>3</td><td>3</td></tr><tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable3Row(4,0);" >3</td><td>1</td><td>2</td><td>3</td><td>4</td><td >4</td></tr>'+
						    	'<tr><td style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" onclick="selectTable3Row(5,0);" >4</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td></tr></table></div><div style="float:left; margin:60px; margin-left:50px"><img src="/mpas/struct/right.gif" width="80" height="50"></img></div><div style="float:left; margin:60px; margin-top:-4px;margin-left:-50px"> '+
						    	'<table  id="table4" border="1" height="170" width="180" cellpadding="1" cellspacing="0" style="text-align:center; background-color:#DFE8F6; padding:0; margin:0;" >'+
						    	'<tr><td colspan="2" rowspan="2">'+'评级4'+'</td><td id="rowName" colspan="4">'+'条件等级'+'</td></tr>'+
						    	'<tr><td width="19">1</td><td width="19">2</td><td width="24">3</td><td width="24">4</td></tr>'+
						    	'<tr><td id="cellName" rowspan="5" width="16">'+'实用性等级'+'</td>'+
						    	'<td width="25">1</td>'+
						    		'<td id="matrix1">0</td>'+
						    		'<td id="matrix2">0</td>'+
						    		'<td id="matrix3">0</td>'+
						    		'<td id="matrix4">0</td>'+
						    		'</tr>'+
						    	'<tr><td>2</td>'+
						    		'<td id="matrix5">0</td>'+
						    		'<td id="matrix6">0</td>'+
						    		'<td id="matrix7">0</td>'+
						    		'<td id="matrix8">0</td>'+
						    		'</tr>'+
						    	'<tr><td>3</td>'+
						    		'<td id="matrix9">0</td>'+
						    		'<td id="matrix10">0</td>'+
						    		'<td id="matrix11">0</td>'+
						    		'<td id="matrix12">0</td>'+
						    		'</tr>'+
						    	'<tr><td>4</td>'+
						    		'<td id="matrix13">0</td>'+
						    		'<td id="matrix14">0</td>'+
						    		'<td id="matrix15">0</td>'+
						    		'<td id="matrix16">0</td>'+
						    		'</tr>'+
						    	'<tr><td>5</td>'+
						    		'<td id="matrix17">0</td>'+
						    		'<td id="matrix18">0</td>'+
						    		'<td id="matrix19">0</td>'+
						    		'<td id="matrix20">0</td>'+
						    		'</tr>'+
						    	'</table></div></td></tr></table>'
					    })]
					});
				var showWin = new Ext.Window({
					title:'基本裂纹长度',
					width : 650,
					autoHeight : true,
					resizable : false,
					closable :false,
					bodyStyle : 'padding:5px;',
					items : [ADForm],
					x : 50,
					y : 50,
					modal: true,
					tbar:[new Ext.Button({
						              text:commonality_save,
						              iconCls:"icon_gif_save",
						              id:"saveButton",
						              handler:saveMatrix
			        	 }),new Ext.Button({
						              text:commonality_close,
						              iconCls:"icon_gif_back",
						              handler:function(){
						              	table1RowValue=null;//稠密度等级
										table1CellValue=null//目视等级
										table3RowValue=null//接近等级
										table2CellValue=null;//光照等级
										table4RowValue=null;//条件等级
										table2RowValue=null;//表面等级
										table3CellValue=null;//尺寸等级
										table4CellValue=null;//实用性等级
						              	showWin.close();
						              }
			        	 })]
				});
				Ext.Ajax.request( {
							url :  contextPath+"/struct/s3/getMatrix.do",
							method : 'post',
							params:{
								anaFlg:'s3'
							},
								success:function(options, success, response){
									all = Ext.util.JSON.decode(options.responseText);
									if(all.matrix.length==0){
									Ext.getCmp('saveButton').hide();
										alert('请先维护自定义裂纹长度');
										return false
									}
									for(var i =0;i<20;i++){
										document.getElementById('matrix' + (i+1)).innerHTML =all.matrix[i].matrixValue;
									}
									
								if(record.get("densityLevel")!=null&&record.get("densityLevel")==1){
									selectTable1Row(1,0);
								}else if(record.get("densityLevel")!=null&&record.get("densityLevel")==2){
									selectTable1Row(1,1);
								}else if(record.get("densityLevel")!=null&&record.get("densityLevel")==3){
									selectTable1Row(1,2);
								}
								if(record.get("lookLevel")!=null&&record.get("lookLevel")==0&&record.get("lookLevel")!=""&&record.get("lookLevel")!=null){
									selectTable1Cell(2,1);
								}else if(record.get("lookLevel")!=null&&record.get("lookLevel")==1){
									selectTable1Cell(3,0);
								}else if(record.get("lookLevel")!=null&&record.get("lookLevel")==2){
									selectTable1Cell(4,0);
								}else if(record.get("lookLevel")!=null&&record.get("lookLevel")==3){
									selectTable1Cell(5,0);
								}		
								if(record.get("sizeLevel")!=null&&record.get("sizeLevel")==1){
									selectTable3Row(2,1);
								}else if(record.get("sizeLevel")!=null&&record.get("sizeLevel")==2){
									selectTable3Row(3,0);
								}else if(record.get("sizeLevel")!=null&&record.get("sizeLevel")==3){
									selectTable3Row(4,0);
								}else if(record.get("sizeLevel")!=null&&record.get("sizeLevel")==4){
									selectTable3Row(5,0);
								}		
								if(record.get("lightLevel")!=null&&record.get("lightLevel")==1){
									selectTable2Cell(1,0);
								}else if(record.get("lightLevel")!=null&&record.get("lightLevel")==2){
									selectTable2Cell(1,1);
								}else if(record.get("lightLevel")!=null&&record.get("lightLevel")==3){
									selectTable2Cell(1,2);
								}	
								if(record.get("surfaceLevel")!=null&&record.get("surfaceLevel")==1){
									selectTable2Row(2,1);
								}else if(record.get("surfaceLevel")!=null&&record.get("surfaceLevel")==2){
									selectTable2Row(3,0);
								}
							}
						});
		var	record=gdGrid.getSelectionModel().getSelected();
	function saveMatrix(){
			if(all.matrix.length==0){
			alert('请先维护自定义裂纹长度');
			return false;
			}
			if(!basicCrack){
				alert('请选择完毕后保存');
				return false;
			}
			record.set("basicCrack", basicCrack);
			record.set("densityLevel",table1RowValue);
			record.set("lookLevel",table1CellValue);
			record.set("reachLevel",table3RowValue);
			record.set("lightLevel",table2CellValue);
			record.set("conditionLevel",table4RowValue);
			record.set("surfaceLevel",table2RowValue);
			record.set("sizeLevel",table3CellValue);
			record.set("doLevel",table4CellValue);
  			if(record.get("edgeEffect")!=null&&record.get("basicCrack")!=null&&record.get("materialSize")!=null){
  				record.set("detectCrack",record.get("edgeEffect")*record.get("materialSize")*record.get("basicCrack"));
  			
  			}	
  				flagTable1_1 = null;
				flagTable1_2 = null;
				table1Cell=null;
				table1Row=null;
				table2Cell=null;
				table2Row=null;
				table3Cell=null;
				table3Row=null;
				table4Cell=null;
				table4Row=null;
				temp=null;
				temp1=null;
				table1Value=null;
				table2Value=null;
				table3Value=null;
				flagTable2_1 = null;
				flagTable2_2 = null;
				flagTable3_1 = null;
				flagTable3_2 = null;
				flagTable4_1 = null;
				flagTable4_2 = null;
			showWin.close();
		}		
			
				flagTable1_1 = null;
				flagTable1_2 = null;
				table1Cell=null;
				table1Row=null;
				table2Cell=null;
				table2Row=null;
				table3Cell=null;
				table3Row=null;
				table4Cell=null;
				table4Row=null;
				temp=null;
				temp1=null;
				table1Value=null;
				table2Value=null;
				table3Value=null;
				flagTable2_1 = null;
				flagTable2_2 = null;
				flagTable3_1 = null;
				flagTable3_2 = null;
				flagTable4_1 = null;
				flagTable4_2 = null;
				showWin.show();
				
}
	
	
	
	
	

	
	
	
	
						
});	
	/***********************各种事件,各种方法**************************/
//矩阵1行的点击事件	
	var table1RowValue=null;//稠密度等级
		function selectTable1Row(row, cell){
				var table = document.getElementById('table1');
				var table3 = document.getElementById('table3');
				for(var i=0;i<3;i++){
				table.rows[row].cells[i].style.background = '#FFFFFF';
				}
				table.rows[row].cells[cell].style.background = 'red';
				table1RowValue=table.rows[row].cells[cell].innerHTML;
				flagTable1_1=1;
				table1Cell=cell;
				if(flagTable1_1==flagTable1_2){
					clearColor();
					if(table1Row=='3'||table1Row=='4'||table1Row=='5'){
						table.rows[table1Row].cells[table1Cell+1].style.background = 'red';
						table3RowValue=table.rows[table1Row].cells[table1Cell+1].innerHTML;
						table1Value=table.rows[table1Row].cells[table1Cell+1].innerHTML;
					}
					if(table1Row=='2'){
						table.rows[table1Row].cells[table1Cell+2].style.background = 'red';
						table3RowValue=table.rows[table1Row].cells[table1Cell+2].innerHTML;
						table1Value=table.rows[table1Row].cells[table1Cell+2].innerHTML;
					}
						if(table1Value=='0'){
							clearTable3RowColor();
							table3.rows[1].cells[table1Value].style.background = 'red';
							table3Cell=table1Value;
							flagTable3_1=1;
							if(flagTable3_1==flagTable3_2){
								  clearTable3Color();
								 table3CellClick();
								 flagTable4_2=1;
							}
						}
						if(table1Value=='1'){
							clearTable3RowColor();
							table3.rows[1].cells[table1Value-1].style.background = 'red';
							table3Cell=table1Value-1;
							flagTable3_1=1;
							if(flagTable3_1==flagTable3_2){
								  clearTable3Color()
								 table3CellClick()
								 flagTable4_2=1;
							}
						}
						if(table1Value=='2'){
							clearTable3RowColor();
							table3.rows[1].cells[table1Value-1].style.background = 'red';
							table3Cell=table1Value-1;
							flagTable3_1=1;
							if(flagTable3_1==flagTable3_2){
								  clearTable3Color();
								 table3CellClick();
								 flagTable4_2=1;
							}
						}
						if(table1Value=='3'){
							clearTable3RowColor();
							table3.rows[1].cells[table1Value-1].style.background = 'red';
							table3Cell=table1Value-1;
							flagTable3_1=1;
							if(flagTable3_1==flagTable3_2){
								  clearTable3Color();
								 table3CellClick();
								 flagTable4_2=1;
							}
						}
						if(table1Value=='4'){
							clearTable3RowColor();
							table3.rows[1].cells[table1Value-1].style.background = 'red';
							table3Cell=table1Value-1;
							flagTable3_1=1;
							if(flagTable3_1==flagTable3_2){
								  clearTable3Color();
								 table3CellClick();
								 flagTable4_2=1;
							}
						}
						if(table1Value=='5'){
							clearTable3RowColor();
							table3.rows[1].cells[table1Value-1].style.background = 'red';
							table3Cell=table1Value-1;
							flagTable3_1=1;
							if(flagTable3_1==flagTable3_2){
								  clearTable3Color();
								 table3CellClick();
								 flagTable4_2=1;
							}
						}
				}
						if(flagTable4_2!=null&&flagTable4_1!=null&&flagTable4_2==flagTable4_1){
						 	var table4 = document.getElementById('table4');
						 	clearTable4Color()
						 	if(table4Row=='2'){
						 		table4.rows[table4Row].cells[table4Cell+2].style.background = 'red';
						 		basicCrack=table4.rows[table4Row].cells[table4Cell+2].innerHTML;
						 		}else{
					 		table4.rows[table4Row].cells[table4Cell+1].style.background = 'red';
					 		basicCrack=table4.rows[table4Row].cells[table4Cell+1].innerHTML;}
					 	}
			}	
			
			//矩阵1列的点击事件	
			var table1CellValue=null//目视等级
			var table3RowValue=null//接近等级
		function selectTable1Cell(row, cell){
			var table = document.getElementById('table1');
				var table3 = document.getElementById('table3');
				if(row==2){
					for(var i =3;i<6;i++){
						table.rows[i].cells[0].style.background = '#FFFFFF';
					}
				}else{
				for(var i =3;i<6;i++){
				if(row!=2){
					table.rows[2].cells[1].style.background = '#FFFFFF';
					}
					table.rows[i].cells[cell].style.background = '#FFFFFF';
					}
				}
				table.rows[row].cells[cell].style.background = 'red';
				table1CellValue=table.rows[row].cells[cell].innerHTML;
				flagTable1_2=1;
				table1Row=row
				if(flagTable1_1==flagTable1_2){
					clearColor();
					if(table1Row=='3'||table1Row=='4'||table1Row=='5'){
						table.rows[table1Row].cells[table1Cell+1].style.background = 'red';
						table1Value=table.rows[table1Row].cells[table1Cell+1].innerHTML;
					}
					if(table1Row=='2'){
						table.rows[table1Row].cells[table1Cell+2].style.background = 'red';
						table1Value=table.rows[table1Row].cells[table1Cell+2].innerHTML;
					}
					
						if(table1Value=='0'){
							clearTable3RowColor();
							table3.rows[1].cells[table1Value].style.background = 'red';
							table3RowValue=table3.rows[1].cells[table1Value].innerHTML;
							table3Cell=table1Value;
							flagTable3_1=1;
							if(flagTable3_1==flagTable3_2){
								  clearTable3Color();
								  table3CellClick();
								  flagTable4_2=1;
							}
						}else{						
							clearTable3RowColor();
							table3.rows[1].cells[table1Value-1].style.background = 'red';
							table3RowValue=table3.rows[1].cells[table1Value-1].innerHTML;
							table3Cell=table1Value-1;
							flagTable3_1=1;
							if(flagTable3_1==flagTable3_2){
								  clearTable3Color();
								  table3CellClick();
								  flagTable4_2=1;
							}
						}
						if(flagTable4_2!=null&&flagTable4_1!=null&&flagTable4_2==flagTable4_1){
						 	var table4 = document.getElementById('table4');
						 	clearTable4Color()
						 	if(table4Row=='2'){
						 		table4.rows[table4Row].cells[table4Cell+2].style.background = 'red';
						 		basicCrack=table4.rows[table4Row].cells[table4Cell+2].innerHTML;
						 		}else{
					 		table4.rows[table4Row].cells[table4Cell+1].style.background = 'red';
					 		basicCrack=table4.rows[table4Row].cells[table4Cell+1].innerHTML;
					 		}
					 	}
				}
			}			
			
			//矩阵2列的点击事件	
			var table2CellValue=null;//光照等级
			var table4RowValue=null;//条件等级
			function selectTable2Cell(row, cell){
				var table2 = document.getElementById('table2');
				var table4 = document.getElementById('table4');
				for(var i=0;i<3;i++){
					table2.rows[row].cells[i].style.background = '#FFFFFF';
				}
				 table2.rows[row].cells[cell].style.background = 'red';
				 table2CellValue=table2.rows[row].cells[cell].innerHTML;
				 table2Cell=cell;
				flagTable2_1=1;
				if(flagTable2_1==flagTable2_2){
					clearTable2Color()
					if(table2Row=='3'){
						table2.rows[table2Row].cells[table2Cell+1].style.background = 'red';
						table2Value=table2.rows[table2Row].cells[table2Cell+1].innerHTML;
						clearTable4RowColor()
							table4.rows[1].cells[table2Value-1].style.background = 'red';
							table4RowValue=table4.rows[1].cells[table2Value-1].innerHTML;
						table4Cell=table2Value-1;
					}
					if(table2Row=='2'){
						table2.rows[table2Row].cells[table2Cell+2].style.background = 'red';
						table2Value=table2.rows[table2Row].cells[table2Cell+2].innerHTML;
						clearTable4RowColor()
						table4.rows[1].cells[table2Value-1].style.background = 'red';
						table4RowValue=table4.rows[1].cells[table2Value-1].innerHTML;
						table4Cell=table2Value-1;
					}
						flagTable4_1=1;
						if(flagTable4_2==flagTable4_1){
						 	var table4 = document.getElementById('table4');
						 	clearTable4Color()
						 	if(table4Row=='2'){
						 		table4.rows[table4Row].cells[table4Cell+2].style.background = 'red';
						 		basicCrack=table4.rows[table4Row].cells[table4Cell+2].innerHTML;
						 		}else{
					 		table4.rows[table4Row].cells[table4Cell+1].style.background = 'red';
					 		basicCrack=table4.rows[table4Row].cells[table4Cell+1].innerHTML;
					 		
					 		}
					 	}
				}
			}
			//矩阵2行的点击事件
			var table2RowValue=null;//表面等级
			function selectTable2Row(row, cell){
				var table = document.getElementById('table2');
				var table4 = document.getElementById('table4');
				if(row=='2'&&cell=='1'){
				 table.rows[row].cells[cell].style.background = 'red';
				 table2Value=table.rows[row].cells[cell].innerHTML;
				 table.rows[3].cells[0].style.background = '#FFFFFF'
				}
				if(row=='3'&&cell=='0'){
					table.rows[row].cells[cell].style.background = 'red';
					table2Value=table.rows[row].cells[cell].innerHTML;
					 table.rows[2].cells[1].style.background = '#FFFFFF'
				}
				 table2RowValue=table.rows[row].cells[cell].innerHTML;
				 table2Row=row;
				flagTable2_2=1;
				if(flagTable2_1!=null&&flagTable2_2!=null&&flagTable2_1==flagTable2_2){
					clearTable2Color()
					if(table2Row=='3'){
						table.rows[table2Row].cells[table2Cell+1].style.background = 'red';
						table2Value=table.rows[table2Row].cells[table2Cell+1].innerHTML;
						clearTable4RowColor()
							table4.rows[1].cells[table2Value-1].style.background = 'red';
							table4RowValue=table4.rows[1].cells[table2Value-1].innerHTML;
						table4Cell=table2Value-1;
					}
					if(table2Row=='2'){
						table.rows[table2Row].cells[table2Cell+2].style.background = 'red';
						table2Value=table.rows[table2Row].cells[table2Cell+2].innerHTML;
						clearTable4RowColor()
							table4.rows[1].cells[table2Value-1].style.background = 'red';
								table4RowValue=table4.rows[1].cells[table2Value-1].innerHTML;
					}
						table4Cell=table2Value-1;
						flagTable4_1=1;
						if(flagTable4_2==flagTable4_1){
						 	var table4 = document.getElementById('table4');
						 	clearTable4Color()
						 	if(table4Row=='2'){
						 		table4.rows[table4Row].cells[table4Cell+2].style.background = 'red';
						 		basicCrack=table4.rows[table4Row].cells[table4Cell+2].innerHTML;
						 		}else{
					 		table4.rows[table4Row].cells[table4Cell+1].style.background = 'red';
					 		basicCrack=table4.rows[table4Row].cells[table4Cell+1].innerHTML;
					 		}
					 	}
						
				}
			}
			
		//矩阵3行的点击事件	
		var table3CellValue=null;//尺寸等级

		function selectTable3Row(row, cell){
			var table = document.getElementById('table3');
			if(row==2){
			for(var i =3;i<6;i++){
				table.rows[i].cells[0].style.background = '#FFFFFF';
			}
			}else{
			for(var i =3;i<6;i++){
			if(row!=2){
				table.rows[2].cells[1].style.background = '#FFFFFF';
			}
				table.rows[i].cells[cell].style.background = '#FFFFFF';
			}
			}
			table.rows[row].cells[cell].style.background = 'red';
			table3CellValue=table.rows[row].cells[cell].innerHTML;
			table3Row=row;
			flagTable3_2=1;
			if(flagTable3_1==flagTable3_2){
				clearTable3Color();
				 table3CellClick();
				 flagTable4_2=1;
					if(flagTable4_2==flagTable4_1){
					 	var table4 = document.getElementById('table4');
					 	clearTable4Color()
					 	if(table4Row=='2'){
					 		table4.rows[table4Row].cells[table4Cell+2].style.background = 'red';
					 		basicCrack=table4.rows[table4Row].cells[table4Cell+2].innerHTML;
					 		}else{
				 			table4.rows[table4Row].cells[table4Cell+1].style.background = 'red';
				 			basicCrack=table4.rows[table4Row].cells[table4Cell+1].innerHTML;
				 		}
				 	}
			}
		}	
			
			//清空矩阵1颜色
		function clearColor(){
			var table = document.getElementById('table1');
				for(var i=2;i<5;i++){
				table.rows[2].cells[i].style.background = '#DFE8F6';
				
				}
			for(var i=3;i<6;i++){
				for(var j=1;j<4;j++){
				table.rows[i].cells[j].style.background = '#DFE8F6';
				}
			}
		}		
	
	//清空举证2颜色
		function clearTable2Color(){
			var table = document.getElementById('table2');
				for(var i=2;i<5;i++){
				table.rows[2].cells[i].style.background = '#DFE8F6';
				}
			for(var i=1;i<4;i++){
				table.rows[3].cells[i].style.background = '#DFE8F6';
			}
		}
		//清空矩阵3横排颜色
		function clearTable3RowColor(){
			var table3 = document.getElementById('table3');
				for(var i=0;i<5;i++){
				table3.rows[1].cells[i].style.background = '#FFFFFF';
				}
		}
		//矩阵3竖排点击触发事件
		var table4CellValue=null;//实用性等级
		function table3CellClick(){
			var table3 = document.getElementById('table3');
			var table4 = document.getElementById('table4');
			clearTable3Color()
					if(table3Row=='3'||table3Row=='4'||table3Row=='5'){
						if(table3Cell=='0'){
						table3.rows[table3Row].cells[1].style.background = 'red';
						table3Value=table3.rows[table3Row].cells[1].innerHTML;
						clearTable4CellColor();
						if(table3Value=='1'){
								table4.rows[2].cells[table3Value].style.background = 'red';
								table4CellValue=table4.rows[2].cells[table3Value].innerHTML;
							}else{
							
								table4.rows[table3Value-0+1].cells[0].style.background = 'red';
								table4CellValue=table4.rows[table3Value-0+1].cells[0].innerHTML;
							}
							table4Row=table3Value-0+1;
						}else{
						table3.rows[table3Row].cells[table3Cell+1].style.background = 'red';
						table3Value=table3.rows[table3Row].cells[table3Cell+1].innerHTML;
						clearTable4CellColor();
						if(table3Value=='1'){
								table4.rows[2].cells[table3Value].style.background = 'red';
								table4CellValue=table4.rows[2].cells[table3Value].innerHTML;
							}else{
								table4.rows[table3Value-0+1].cells[0].style.background = 'red';
								table4CellValue=table4.rows[table3Value-0+1].cells[0].innerHTML;
							
							}
							table4Row=table3Value-0+1;
						}
					}
					if(table3Row=='2'){
						if(table3Cell=='0'){
							table3.rows[table3Row].cells[2].style.background = 'red';
							table3Value=table3.rows[table3Row].cells[2].innerHTML;
							clearTable4CellColor();
							if(table3Value=='1'){
								table4.rows[2].cells[table3Value].style.background = 'red';
								table4CellValue=table4.rows[2].cells[table3Value].innerHTML;
							}else{
								table4.rows[table3Value-0+1].cells[0].style.background = 'red';
								table4CellValue=table4.rows[table3Value-0+1].cells[0].innerHTML;
							}
							table4Row=table3Value-0+1;
						}else{
							table3.rows[table3Row].cells[table3Cell+2].style.background = 'red';}
							table3Value=table3.rows[table3Row].cells[table3Cell-0+2].innerHTML;
							clearTable4CellColor();
							if(table3Value=='1'){
								table4.rows[2].cells[table3Value].style.background = 'red';
								table4CellValue=table4.rows[2].cells[table3Value].innerHTML;
							}else{
							
								table4.rows[table3Value-0+1].cells[0].style.background = 'red';
								table4CellValue=table4.rows[table3Value-0+1].cells[0].innerHTML;
							}
							table4Row=table3Value-0+1;
					}
					
		}
		//清空矩阵3的颜色
			function clearTable3Color(){
			var table3 = document.getElementById('table3');
				for(var i=2;i<7;i++){
				table3.rows[2].cells[i].style.background = '#DFE8F6';
				
				}
			for(var i=3;i<6;i++){
				for(var j=1;j<6;j++){
				table3.rows[i].cells[j].style.background = '#DFE8F6';
				}
			}
		}
			//清空矩阵4横排颜色
		function clearTable4RowColor(){
			var table4 = document.getElementById('table4');
				for(var i=0;i<4;i++){
				table4.rows[1].cells[i].style.background = '#DFE8F6';
			}
		}
		
	//清空矩阵4列颜色
		function clearTable4CellColor(){
			var table4 = document.getElementById('table4');
				table4.rows[2].cells[1].style.background = '#DFE8F6';
				for(var i=3;i<7;i++){
				table4.rows[i].cells[0].style.background = '#DFE8F6';
			}
		}
		function table4Red(row,cell){
			var table4 = document.getElementById('table4');
			
			table4.rows[row].cells[cell].style.background = 'red';
			
		}
		//清空表格4的颜色
		function clearTable4Color(){
			var table4 = document.getElementById('table4');
				for(var i=2;i<6;i++){
				table4.rows[2].cells[i].style.background = '#DFE8F6';
				
				}
			for(var i=3;i<7;i++){
				for(var j=1;j<5;j++){
				table4.rows[i].cells[j].style.background = '#DFE8F6';
				}
			}
			
		}
		
		function picUp(record){
			 var upForm=new Ext.form.FormPanel({  
					width:830,  
 		            height:50,
 		            labelAlign : 'right',  
 		            labelWidth : 70,  
 		            fileUpload:true,
 		            bodyStyle : 'padding:5px',  
 		            method:"post",  
 		         	 region : 'center',
 		            frame:true,  
 		            items:[{  
 		                fieldLabel:'图片上传',  
 		                xtype:'textfield',  
 		                id:'docFile',  
 		                name:'docFile',
 		                
 		                inputType: 'file'
 		            }]
 		        }) 
 		        
 		       var picPanel = new Ext.Panel({
 		       			id:"picPanel",
 		       			html:record.get('picUrl')==null?"":"<table width='60px'><tr><td><img height='360px' src='"+contextPath+record.get('picUrl')+"'></img></td></tr></table>"
 		       });
			 var upWin=new Ext.Window({
					title:'图片上传',
					width : 720,
					height : 480,
					closable :true,
					id:'upWin',
					items : [upForm,picPanel],
			 		modal:true,
					tbar:[new Ext.Button({
			              text:commonality_save,
			              iconCls:"icon_gif_save",
			              handler:function(){
			              	savePic(upForm,record);
			              }
			        	 })]
			 	
			 })
			 upWin.show();
		
		}
	function savePic(upForm,record){
		upForm.getForm().submit({
			url : contextPath+"/struct/s3/uploadPic.do",
			method:'POST',
			success : function(form, action) {
					record.set("picUrl",action.result.msg);
					record.set("picUrlIsNull","有");
					var picUrl=action.result.msg;
					Ext.getCmp("picPanel").body.update("<table width='60px'><tr><td><img  height='360px' src="+contextPath+picUrl+"></img></img></td></tr></table>");
					alert('图片上传成功');
					},
            failure : function(form, action) { 
                              alert('图片上传失败'); 
                              this.disabled = false; 
   
                          } 
		})
	}