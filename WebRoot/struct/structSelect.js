Ext.namespace('ext_structSelect');
Ext.onReady(function(){
			Ext.form.Field.prototype.msgTarget = 'qtip';
			Ext.QuickTips.init(); 
			var record = Ext.data.Record.create( [ {
						name : 'areaName',
						type : 'string'
					}, {
						name : 'areaCode',
						type : 'string'
					} ]);
			
			var ssiStore = new Ext.data.Store({
				url : contextPath+"/struct/ssiSelect/getSsiSelectList.do",
				pruneModifiedRecords:true,
				reader : new Ext.data.JsonReader({
					 totalProperty : "totleCount",
					 root : "ata",
					 fields : [
					 	{name : 'id'},
					 	{name : 'ataCode'},
					 	{name : 'ataName'},
					 	{name : 'isSsi'},
					 	{name : 'isAna'},
					 	{name : 'isAdd'},
					 	{name : 'anaUser'}
				 	]
				})
			});
			
			ssiStore.load({
				params : {
					ataId : ataId,
					ssiId : ssiId
					}
				});

			
				var colM = new Ext.grid.ColumnModel([
				{
			    	header : "ID", 
			    	dataIndex : "id", 
			    	hidden : true
		    	},{
					header : 'SSI 编号'+ commonality_mustInput, 
					dataIndex : "ataCode",
					width : 70,
					renderer: function(value, meta, record) {     
						meta.attr = 'style="white-space:normal;word-wrap:break-word;"'; 
						return value;      
					},
			    	editor : new Ext.form.TextField({
			    		allowBlank : false,
			    		maxLength : 11,
			    		maxLengthText :'长度不能超过11！'
			    	})
		    	},{
					header :'SSI中文名称'+ commonality_mustInput,
					width : 200,
					dataIndex : "ataName",
					renderer: function(value, meta, record) {     
						meta.attr = 'style="white-space:normal;word-wrap:break-word;"';  
						return value;      
					},
					editor : new Ext.form.TextArea({
			    		allowBlank : false,
			    		blankText : "不允许为空",
			    		maxLength : 200,
			    		maxLengthText : '长度不能超过200!',
			    		grow : true
			    	})
		    	},
		    	{
		    		header :'是否为重要结构'+ commonality_mustInput,
		    		dataIndex : "isSsi",
		    		width :120,
					editor : new Ext.form.ComboBox({
						id:"isSsi",
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
						width : 70 ,
						listeners:{          
	                       select : function(combo, record,index){  
		                       	if(record.json[0]==1){   
		                               grid.getSelectionModel().getSelected().set("isAna","");
		                       } 
		                      	if(record.json[0]==0){   
		                             	grid.getSelectionModel().getSelected().set("isAna","0");
		                             
		                       }
	                       } 
	                	 }   
		    		}),
					renderer:function (value, cellmeta, record){
						    var index = Ext.getCmp("isSsi").store.find(Ext.getCmp('isSsi').valueField,value);				    
						    var record = Ext.getCmp("isSsi").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						}
		    	},{
		    		header :'是否分析非SSI',
		    		dataIndex : "isAna",
		    		width : 100,
					editor : new Ext.form.ComboBox({
						id:"isAna",
						xtype : 'combo',
						emptyText :'请选择',
						store : new Ext.data.SimpleStore({
						fields : ["retrunValue", "displayText"],
							data : [['1',commonality_shi],['0',commonality_fou]]
						}),  
						valueField : "retrunValue", 
						displayField : "displayText",
						typeAhead : true,
						mode : 'local',   
						triggerAction : 'all', 
						emptyText :'请先选择前一项',   
						editable : false, 
						selectOnFocus : true,
						width : 70 
		    		}),
		    		renderer:function (value, cellmeta, record){
		    			if(value==""||value==null||value=="null"){
		    				return '';
		    			}
						    var index = Ext.getCmp("isAna").store.find(Ext.getCmp('isAna').valueField,value);				    
						    var record = Ext.getCmp("isAna").store.getAt(index);
						    var returnvalue = "";
						    if (record){
						        returnvalue = record.data.displayText;
						    }
						    return returnvalue;
						}
		    	},{
					header :'分析人',
					width : 70,
					dataIndex : "anaUser",
					renderer: function(value, meta, record) {     
						if(!value){
							return ''; 
						}else{
							return value;
						}
					}
		    	},{
					header : '是否自增',
					width : 80,
					dataIndex : "isAdd",
					renderer: function(value, meta, record) {     
						if(value==1){
							return commonality_shi;
						}else{
							return commonality_fou; 
						}
					}
		    	}
			]);
				var grid = new Ext.grid.EditorGridPanel({
				title :returnPageTitle('SSI列表维护','structSelect'),
				region: 'center',
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				store : ssiStore,
				stripeRows: true,		
				clicksToEdit : 2,   
				tbar : [new Ext.Button({
							text : commonality_add,
							disabled:isMaintain==0?true:false,
							iconCls : 'icon_gif_add',
							handler : add
				        }),
						new Ext.Button({
							text : commonality_save,
							disabled:isMaintain==0?true:false,
							iconCls : 'icon_gif_save',
							handler : updateDataDetail
				        }),
				        new Ext.Button({
							text : commonality_del,
							disabled:isMaintain==0?true:false,
							iconCls : 'icon_gif_delete',
							handler : del
				        }),
				        new Ext.Button({
				        	id : "claim",
							text : '认领',
							hidden : true,
							disabled:isMaintain==0?true:false,
							iconCls : 'icon_gif_edit',
							handler : get
				        })
					] 
			});
			grid.addListener("beforeedit", beforeedit);			
			grid.addListener("click",click);
			function click(){
				var rec = grid.getSelectionModel().getSelected();
				if(rec){
					if(rec.get("id")!=null&&rec.get("id")!=""&&rec.get("isAdd")==1){
						Ext.getCmp("claim").setVisible(true);
					}else{
						Ext.getCmp("claim").setVisible(false);
					}
				}
			}
			function beforeedit(val){
				if(isMaintain==0){
				val.cancel = true;
				return null;
				}
				var field=val.field;
				var record=val.record;
				var isSsi=record.get("isSsi");
				if(isSsi==1&&field=='isAna'){
					val.cancel = true;
					return;
					}
				if(record.get("isAdd")!=1){
					if(field=='ataCode'||field=='ataName'){
						val.cancel = true;
					}
				}
				if(field=='isAna'){
					if(val.record.get('isSsi')==0&&val.record.get('isSsi')!=''){
						val.cancel=false;
						return;
						}else if(val.record.get('isSsi')==1&&val.record.get('isSsi')!=''){
						val.cancel=false;
						return;
						}else{
							val.cancel=true;
						return;
						}
					}
				}
				
				function afteredit(val){
				var count =0;
				if (val.field == 'ataCode'){
					var nowRecord  = val.record;
					var re=/^\d{2}-\d{2}-\d{2}-\d{2}$/;
					if (re.test(val.value) == false) {
						alert('ATA编号必须是XX-XX-XX-XX格式，并且XX必须是数字!');
						nowRecord.set('ataCode', val.originalValue);
						return false;
					}
					 var items=grid.getStore().data.items;
					 for(var i=0;i<items.length;i++){
					 	if(items[i].get('ataCode')==val.value){
					 		count++;
					 	}
				 	if(count==2){
				 		alert('SSI 编号:'+val.value+'已存在,请重新修改');
						nowRecord.set('ataCode', val.originalValue);
						return false;
				 	}
				 }
				 
			     Ext.Ajax.request({
					url : contextPath+"/struct/ssiSelect/verifySsiCode.do?",
					params:{
						verifyStr:val.value
							},
					success : function(response) {
								if(response.responseText){
									alert('SSI 编号:'+val.value+'已存在,请重新修改');
									nowRecord.set('ataCode', val.originalValue);
									}	
								}
						})
					}

			}
			grid.addListener("afteredit", afteredit);
			 var win = new Ext.Window({
			        layout: 'border',
			        border:false, 
			      	 frame :false,
			        resizable: true,
					closable:false,
					maximized:true,
			        plain:false,
			        buttonAlign:'center',
			        items: [
			        	 { region: 'center',
			        	   layout : 'border',
			        	   items : [grid]
			        	 }
			        ]
			    });
				
			    win.show();
			    
		function add(){
				var index = ssiStore.getCount();
				var rec = grid.getStore().recordType;
				var p = new rec({
					id : '',
					ataCode : '',
					ataName : '',
					isSsi : '',
					isAna : '',
					isAdd : ''
				});
				ssiStore.insert(index, p);
				ssiStore.getAt(ssiStore.getCount()-1).set('isAdd',"1");
			}
			//创建GRIDPANEL的JSON
			function createGridJSON(){
				var json = [];
				Ext.each(ssiStore.modified, function(item) {
					json.push(item.data);
				});
				return Ext.util.JSON.encode(json);
			}
			
			function updateDataDetail() {
			 var flag=false;
				if(ssiStore.modified.length==0)
					{	
						alert("没有需要更新或保存的数据");
						return;
					}	
				Ext.each(ssiStore.modified, function(item) {
					if(flag){
						return;
					}	
					if(!item.data.ataCode){
						alert("请维护SSI编号");
						flag=true;//报错.需要返回
						return;
					}
					if(item.data.isAdd==1&&item.data.ataCode!=null&&item.data.ataCode!=""){
						if(item.data.isSsi==null||item.data.isSsi==""){
							return;
						}
					}else if(item.data.isAdd!=1){
						if(item.data.isSsi==null||item.data.isSsi==""){
							return;
						}
					}
					if(!item.data.ataName){
						alert('请维护SSI名称');
						flag=true;//报错.需要返回
						return;
					}
					if(!item.data.isSsi){
						alert('请维护是否为重要结构');
						flag=true;//报错.需要返回
						return;
					}
				});		
				if(flag){
					return;
				}	
				var json = [];
				Ext.each(ssiStore.modified, function(item) {
					if(item.data.isAdd==1&&item.data.ataCode!=null&&item.data.ataCode!=""){
						if(item.data.isSsi==null||item.data.isSsi==""){
							return;
						}
					}else if(item.data.isAdd!=1){
						if(item.data.isSsi==null||item.data.isSsi==""){
							return;
						}
					}
					json.push(item.data);
				});
				if(json.length==0){
					alert("没有需要更新或保存的数据");
					return;
				}
				Ext.MessageBox.show({
				    title :commonality_affirm,
				    msg :commonality_affirmSaveMsg,				    
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){
							save(json);
						} 
				    }
				});	
				return;
			}
			
			var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					msg : commonality_waitMsg,
					removeMask : true
						// 完成后移除
				});
			
			function save(json){
				var	newGrid= Ext.util.JSON.encode(json);
				waitMsg.show();
				Ext.Ajax.request( {
					url : contextPath+"/struct/ssiSelect/saveSsilist.do",
					params : {
						jsonData : newGrid,
						ataId:ataId
					},
					method : "POST",
					success : function(response) {
						waitMsg.hide();
						alert(commonality_messageSaveMsg);
						parent.refreshTreeNode();
						ssiStore.reload();
					},
					failure : function(response) {
						waitMsg.hide();
						alert(commonality_cautionMsg);
					}
				});
			}
			
			function get(){
					var record=grid.getSelectionModel().getSelected()
					if(!record){
						alert('请选择一条数据');
						return null;
					}
					var id =record.get("id");
					if(id==null){
						alert('请先保存当前修改后的数据再进行操作');
						return;
					}else{
						Ext.Ajax.request( {
							url : contextPath+"/struct/ssiSelect/claimTask.do",
							params : {
								ssiId:id
							},
							method : "POST",
							success : function(response) {
								var msg = Ext.util.JSON.decode(response.responseText);
								if(msg.exist){
									alert(commonality_messageSaveMsg);
									ssiStore.reload();
								}else{
									alert("您没有认领权限!!!");
								}
							},
							failure : function(response) {
								alert(commonality_cautionMsg);
							}
						});
					}
				
			}
			
			function del(){
					var record=grid.getSelectionModel().getSelected()
						if(!record){
							alert(commonality_alertDel);
							return;
						}
						if(record.get('isAdd')!=1){
							alert('此数据是ATA数据,如需要删除,请删除ATA');
							return null;
						}
					Ext.Msg.confirm(commonality_caution, commonality_affirmDelMsg, function(btn){
						if('yes' == btn){
							if (!record.get('id')){
								ssiStore.modified.remove(record);
								ssiStore.remove(record);	
								return;
							}
							Ext.Ajax.request( {
								url : contextPath+"/struct/ssiSelect/delRecord.do",
								params : {
									ssiId:record.get("id")
								},
								method : "POST",
								success : function(response) {
									alert(commonality_messageDelMsg);
									ssiStore.reload();
									parent.refreshTreeNode();
								},
								failure : function(response) {
									alert(commonality_cautionMsg);
								}
							});
						}
		    		});
	    	}
});
		