Ext.onReady(function(){
			var newGrid = null;
			var oldGrid = null;	
			Ext.form.Field.prototype.msgTarget = 'qtip';
			Ext.QuickTips.init(); 
			var centerForm = new Ext.form.FormPanel({
					labelWidth: 200,
				    autoScroll : true,  
				    labelAlign : "left",	  
				    region : 'center',
				    layout:'fit',
				    frame:true,
				    items: [{
				            columnWidth:1,
				            layout: 'form',
				          	items: [{xtype: 'textarea',
							    	  id : 'remark',
							    	  name : 'remark',
							    	  // width : 500,
							    	  anchor : '100%',
							    	  height : 200,
							    	  fieldLabel:'零部件供应商分析评论',
							    	  value : remark,
							    	  renderer : changeBR
							    	  }]
					        }]
				});	
				
		
			var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy( {
					url : contextPath+"/struct/s7/getRecords.do?ssiId="+ssiId
				}),
				reader : new Ext.data.JsonReader({
					 root : "taskList",
					 fields : [
					 	{name : 'taskId'},
					 	{name : 'taskCode'},
					 	{name : 'oneZone'},
					 	{name : 'hasInter'},
					 	{name : 'taskDesc'},
					 	{name : 'needTransferStr'},
					 	{name : 'whyTransfer'},
					 	{name : 'hasAcceptStr'},
					 	{name : 'rejectReason'}
				 	]
				})
			});
			store.load();
			var colM = new Ext.grid.ColumnModel([
			    {
					header : "MSG-3任务号", 
					dataIndex : "taskCode",
					width : 100
		    	},
		    	{
		    		header : "是否内外部",
		    		dataIndex : "hasInter",
		    		width : 80,	
 		    		renderer: function(value, meta, record) {     
						if(value==0){
							return '内部'
						}
						if(value==1){
							return '外部'
						}
						if(value==2){
							return '内/外部'
						}
						return '';      
					}
		    	},{
		    		header :'区域',
		    		dataIndex : "oneZone",
		    		width : 80,
		    		renderer: function(value, meta, record) {     
						meta.attr = 'style="white-space:normal;word-wrap:break-word;"';
						if(value!=undefined){  
							value = value.replace(/\n/g, '<br/>');
						} 
						return value;      
					}/*,
					editor : new Ext.form.TextField({
			    		allowBlank : false
			    	})*/
		    	},
		    	{
		    		header : '任务描述',
		    		dataIndex : "taskDesc",
					width : 200,
					editor : new Ext.form.TextArea({})			
		    	},
		    	{
		    		header : '区域候选(是/否)'+ commonality_mustInput,
		    		dataIndex : "needTransferStr",
					width : 120,
					editor : new Ext.form.ComboBox(
					{
						xtype : 'combo',
						id:'needTransferStr',
						store : new Ext.data.SimpleStore({
						fields : ["retrunValue", "displayText"],
							data : [['1',commonality_shi],['0',commonality_fou]]
						}),  
						valueField : "retrunValue", displayField : "displayText",
						typeAhead : true, mode : 'local',   
						triggerAction : 'all', emptyText : '请选择',   
						editable : false, selectOnFocus : true,
						allowBlank : false,
						width : 70
		    		}),
					renderer: function(value, meta, record) {
						    var index = Ext.getCmp("needTransferStr").store.find(Ext.getCmp('needTransferStr').valueField,value);				    
						    var record = Ext.getCmp("needTransferStr").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						}  
		    	},{
					header : "协调单",
					dataIndex : "taskId",
					renderer : function(value, cellmeta, record) {
						var returnvalue = "";
						if (value!=""&&value!=null){
							returnvalue = "<a href='javascript:void(0)' title='新增协调单'>"
							+ "<img src='"+ contextPath+ "/images/toAuditBtn.gif' onclick='tempRecord()'/></a>"
							+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' title='协调单详情'>"
							+ "<img src='"+ contextPath+ "/images/toCheckBtn.gif'"
							+"onclick='getRecord()'/></a>";
						}
						return returnvalue;
					}
				},{
		    		header : '转区域原因',
		    		dataIndex : "whyTransfer",
		    		width : 120,
					renderer: changeBR,		
					editor : new Ext.form.TextArea({grow : true})
		    	},
		    	{
		    		header :'区域是否接受',
		    		dataIndex : "hasAcceptStr",
		    		width : 100,
		    		align : 'center',
					renderer : function(value, cellmeta, record) {
						if (value == '1') {
							return commonality_shi;
						} else if (value == '0') {
							return commonality_fou;
						} else {
							return "";
						}
					}
		    	},{
					header : '退回原因',
		    		dataIndex : "rejectReason",
		    		width : 100,
					renderer: function(value, meta, record) {     
						meta.attr = 'style="white-space:normal;word-wrap:break-word;"';
						if(value!=null)
							value = value.replace(/\n/g, '<br/>').replace(/<danyin>/g,"'");     
						return value;      
					}
				}
			]);			
			
			var grid = new Ext.grid.EditorGridPanel({
				title :  returnPageTitle('结构维修任务汇总和转区域分析','s7'),
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				region : 'center',
				store : store,
				clicksToEdit : 2,   
				stripeRows : true,
				height : 450
			});
						//创建GRIDPANEL的JSON
			function createGridJSON(){
				var json = [];
				for (var i = 0; i < store.getCount(); i++){
					json.push(store.getAt(i).data);
				}
				return Ext.util.JSON.encode(json);
			}
			store.on('load', function(){
					oldGrid = createGridJSON();
			});
			
			grid.addListener("rowclick",function(grid, rowindex, e){
			    tempRecords = store.getAt(rowindex);
				rId = tempRecords.get("taskId");
			})		
						
			grid.addListener("beforeedit", beforeedit);	
			function beforeedit(val){
				if(isMaintain==0){
					val.cancel = true;
				}
				var valRecord=val.record;
				if(val.field=='whyTransfer'){
					if(valRecord.get('needTransferStr')==0){
						val.cancel = true;
					}
				}
			}
			
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			
			
			
			
			nextOrSave=function(number){
			var modifiedList = store.modified;
				if(number){
					if (step[number] == 0){
						alert('请先完成之前的步骤！');
						return false;
					}
					if(modifiedList.length==0){
						goNext(number);
						return null;
					}
				}
			newGrid = createGridJSON();
				var status=false;
				for (var i = 0; i < store.getCount(); i++){
					var re = store.getAt(i);
					if(re.get('needTransferStr')!=1&&re.get('needTransferStr')!=0){
						status=true;
						break;
					}
				}
				if(status){
					alert('请选择是否需要区域候选');
					return;
				}
				if ((step[14]==1)&&(newGrid == oldGrid||isMaintain == '0')&&Ext.getCmp("remark").getValue()==remark){
					if(number){
						goNext(number);
						return null;
					}
					goNext(15);
					return false;
				}
				var json = [];
				var modifiedList = store.modified;
				remark=document.getElementById('remark').value;
				Ext.each(modifiedList, function(item) {
					json.push(item.data);
				});

				Ext.MessageBox.show({
				 	 title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,				    
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){	
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true// 完成后移除
						});
				
					waitMsg.show();						
							Ext.Ajax.request( {
								url : contextPath+'/struct/s7/saveS7.do',
								waitMsg :'正在保存.....',
								params : {
									jsonData : Ext.util.JSON.encode(json), 
									ssiId : ssiId,
									remarkId : remarkId,
									remark : remark
								},
								method : "POST",
								success : function(response) {
									waitMsg.hide();
									if(number){
										goNext(number);
										parent.refreshTreeNode();
										return null;
									}
									var text=Ext.util.JSON.decode(response.responseText);
									alert(commonality_messageSaveMsg);
									parent.refreshTreeNode();
									if(text=="100"){
										alert("分析已完成");
										successNext(14);
										return null;
									}
									if(text=="101"){
										alert("请完成前面的步骤");
										successNext(14);
										return null;
									}
									successNext(text);
								},
								failure : function(response) {
									waitMsg.hide();
									alert(commonality_cautionMsg);
									return;
								}
							});
						} else if (id == 'no'){
							if(number){
								goNext(number);
								return null;
							}
							goNext(15);
							return null;
						}
				    }
				});
			}
			var h=window.innerHeight
				|| document.documentElement.clientHeight
				|| document.body.clientHeight;
			var viewport = new Ext.Viewport({   
			    layout: 'border', 
			    items : [
			    		{ 
				    		region : 'north',
				    		height : 60,
				    		frame : true,
				    		items : [headerStepForm]
			    		},
						{ 
						  region : 'center',
				    	  items : [grid]
						}, 
						{ 
						  region : 'south',
						  height : h/4,
						  layout:'fit',
				    	  items : [centerForm]
						}
			    	]   
			});  
			//中英文编辑框
	
		})
