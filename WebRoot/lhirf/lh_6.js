/*
 * @author  wangyueli
 * createdate 2012-08-27
 */

// 创建命名空间

Ext.namespace('lh6');

   lh6.app=function(){ 
           return{

              init:function(){
               Ext.QuickTips.init();
			  Ext.form.Field.prototype.msgTarget = 'qtip';
			//导航
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			 //转移区域 combobox
			var orMoveCombo = new Ext.form.ComboBox({   
				name : 'OrMove', id : 'OrMove',
				store : new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
				data : [['1',commonality_shi],['0',commonality_fou]]
				}),  
				valueField : "retrunValue", displayField : "displayText",
				typeAhead : true, mode : 'local',   
				triggerAction : 'all', width : 45,  
				editable : false, selectOnFocus : true
			});
			var colM = new Ext.grid.ColumnModel([
				{
				    header : "<div align='center'>id</div>",
				    hidden:true,
				    dataIndex : 'taskId',
					  width : 100
				 },
			       {
			          header : "<div align='center'>"+'HSI 编号'+"</div>",
			          dataIndex : 'hsiCode',
					  width : 100
			       },
					{
						header : "<div align='center'>"+'MSG-3任务号'+"</div>",
						dataIndex : 'taskCode',
						width : 130
					},{
						header : "<div align='center'>"+'任务类型'+"</div>",
						align : 'center',
						dataIndex : 'taskType',
						width : 65
					},{
						header : "IPV/OPVP/OPVE",
						dataIndex : 'ipvOpvpOpve',
						align:'center',
						width : 90
					},{
					     header : "<div align='center'>"+'接近方式'+"</div>",
						 dataIndex : 'reachWay',
						 width : 350,
						 renderer : changeBR
					},
					{
					    header : "<div align='center'>"+'任务描述'+"</div>",
						dataIndex : 'taskDesc',
						renderer : changeBR,
						width : 350
					},
					{
					    header : "<div align='center'>"+'任务间隔'+"</div>",
					    dataIndex : 'taskInterval',
					    width : 80
					},{
					    header : "<div align='center'>"+'是否转移任务'+"</div>",
					    align:'center',
					    dataIndex : 'needTransfer',
					    width : 110,
						renderer : function(value, cellmeta, record){
				     	var index = orMoveCombo.store.find(Ext.getCmp('OrMove').valueField,value);				    
				     	var record = orMoveCombo.store.getAt(index);
				    	var returnvalue = "";
					    	if (record){
					        	returnvalue = record.data.displayText;
							}
				    	return returnvalue;
				        }
					},{
					    header : "<div align='center'>"+'区域是否接受'+"</div>",
					    align:'center',
					    dataIndex : 'hasAccept',
					    width : 125,
					    renderer : function(value, cellmeta, record){
							if (value == '1') {
								return commonality_shi;
							} else if (value == '0') {
								return commonality_fou;
							} else {
								return "";
							}
						}
					},{
					    header : "<div align='center'>"+'退回原因'+"</div>",
					    dataIndex : 'rejectReason',
					    renderer : changeBR,
					    width : 200
					},{
					    header : "<div align='center'>"+'适用性'+"</div>",
					    dataIndex : 'lheff',
						width : 200,
						editor : new Ext.form.TextArea({}),
						renderer : changeBR
					},{ header : "<div align='center'>hsiId</div>",
						hidden :true,
					    dataIndex : 'hsiId',
						width : 200
					}
				]);
			
			var store = new Ext.data.Store({
				url : "${contextPath}/lhirf/lh_6/loadLh6Msg.do",
				reader : new Ext.data.JsonReader({
					 totalProperty : "totalCount",
					 root : "lh6",
					 fields : [
			            {name:'taskId'},
					 	{name : 'hsiCode'},
					 	{name : 'taskCode'},
					 	{name : 'taskType'},
					 	{name : 'ipvOpvpOpve'},
					 	{name : 'reachWay'},
					 	{name : 'taskDesc'},
					 	{name : 'taskInterval'},
					 	{name : 'needTransfer'},
					 	{name : 'hasAccept'},
					 	{name : 'rejectReason'},
					 	{name : 'lheff'},
					 	{name : 'hsiId'}
				 	]
				})
				
			});
			var grid = new Ext.grid.EditorGridPanel({
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({
									singleSelect : true
								}),
				store : store,
				height:100,		
				stripeRows : true,
				tbar : [   new Ext.Button({
						text : commonality_save,
						iconCls : "icon_gif_save",
						disabled : !authorityFlag,
						handler : saveLh6
			        })
					],
				bbar : new Ext.PagingToolbar({
						pageSize: 15, 
					store : store,
					displayInfo : true,
					displayMsg :commonality_turnPage,
					emptyMsg : commonality_noRecords
				})
			});

			store.on("beforeload", function() {
							store.baseParams = {
							    start :0,
							    limit :15,
							    hsiId : hsiId ,
								areaId : areaId
							};
						});
						 store.load({
						　　     params:{
						              start:0,
			                          limit:15
				　　　　　　   	}
　　　　　　　　　　		 });
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~画面初始化  ~~~~~~~~~~~~~~~~
			//*****
			   var viewport = new Ext.Viewport( {
				id : 'viewportId',
				layout : 'border',
				items : [
					{
						region : 'north',
			    		height : 60,
			    		frame : true,
			    		items : [headerStepForm]
					},
					{
						region : 'center',
						layout : 'fit',
						 title : returnPageTitle('任务汇总','lh_6'),
						split : true,  
						items : [grid]
					}
				]
			});
			///保存操作, 
			function saveLh6(){
			     Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,
				    buttons : Ext.Msg.YESNO,
				    fn : function(id){
				    	if (id == 'cancel' || id == 'no' ){
							return;
						} else if (id == 'yes'){
							var modified = store.modified;											
							var result = updateDataDetail(modified);
							if (result == 0 ){
							 store.modified = [];
							 }
						}
						
				    }
				});	
				
			}
				// 保存具体操作
			  function updateDataDetail(modified) {
				var json = [];
				var result = 0;
				Ext.each(modified, function(item) {
					json.push(item.data);
				});
				if (json.length > 0) {
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true// 完成后移除
						});		
					waitMsg.show();
					
					Ext.Ajax.request( {
						url : "${contextPath}/lhirf/lh_6/saveLh6List.do",
						params : {
							jsonData : Ext.util.JSON.encode(json)
						},
						method : "POST",
						success : function(response, action) {
							waitMsg.hide();
							var text = Ext.util.JSON.decode(response.responseText);
							if (text.success == true) {
								alert(commonality_messageSaveMsg);
								 store.modified = [];
								grid.getStore().load();
								parent.refreshTreeNode();
							}else{
								alert( commonality_saveMsg_fail);
							}
						},
						failure : function(response, action) {
							waitMsg.hide();
							alert(commonality_cautionMsg);
							result = 1;
						}
					});
				} else {
					alert( commonality_alertUpdate);
					result = 1;
				}
				return result;
			}
			
			
				nextOrSave = function (value){
				   if(value == null){
				    Ext.MessageBox.alert(commonality_caution,'分析已完成 !');
				   }else{
				     goNext(value);
				   }
				
				}
          ////    
           }
           }
     }();