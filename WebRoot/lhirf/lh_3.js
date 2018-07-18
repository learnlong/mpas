  /*
 * @author  wangyueli
 * createdate 2012-08-23
 */

// 创建命名空间
Ext.namespace('lh3');

   lh3.app=function(){ 
           return{
              
           init:function(){
               	Ext.form.Field.prototype.msgTarget = 'qtip';
			    Ext.QuickTips.init();   
			    var newGrid = null;
			   var oldGrid = null;           
              //中英文文本域
	    
              ///************
              
              
              	//导航
			  var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			    });
			    
              var store = new Ext.data.Store({
				url : lh3.app.g_lh3ListUrl,
				reader : new Ext.data.JsonReader({
					 totalProperty : "totleCount",
					 root : "lh3",
					 fields : [
					 	{name : 'lh3Id'},
					 	{name : 'defectModel'},
					 	{name : 'defectDesc'}
				 	]
				})
			});
			
			var colM = new Ext.grid.ColumnModel([
					{
				    	header : "LH3 ID", 
				    	dataIndex : "lh3Id", 
				    	hidden : true
			    	},{
						header : '退化模式' + commonality_mustInput, //退回模式中文
						dataIndex : "defectModel",
						hidden : commonality_hidden,
						width : 405,
				    	editor : new Ext.form.TextArea({ 
				    		allowBlank : false,
							grow : true
						}),
	         			renderer : changeBR
			    	},{
						header : '退化说明' + commonality_mustInput,  // 退化说明  中文
						dataIndex : "defectDesc",
						hidden : commonality_hidden,
						width : 385,
						editor : new Ext.form.TextArea({
							allowBlank : false,
							grow : true
						}),
	         			renderer : changeBR
			    	}
				]);
			
			var grid = new Ext.grid.EditorGridPanel({
				region: 'center',
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				store : store,
				clicksToEdit : 2,   
				stripeRows: true,				
				tbar : [   new Ext.Button({
						text : commonality_add,
						iconCls : "icon_gif_add",
						disabled : !authorityFlag,
						handler : addRow
			        }),
			        "-",
				        new Ext.Button({
							text : commonality_del,
							iconCls : 'icon_gif_delete',
							disabled : !authorityFlag,
							handler : del
						
				        })
					],
				bbar: new Ext.PagingToolbar({ 
				pageSize: 15, 
				store: store, 
				displayInfo: true, 
				displayMsg: commonality_turnPage, 
				emptyMsg: commonality_noRecords })
			});
				//
				//画面  
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
						title : returnPageTitle('L/HIRF防护项目功能退化模式分析','lh_3'),
						layout: 'border',
						region : 'center',
						items: [grid]
					}
				]
			});
				//创建GRIDPANEL的JSON
			function createGridJSON(){
				var json = [];
				for (var i = 0; i < store.getCount() - 1; i++){
					json.push(store.getAt(i).data);
				}
				return Ext.util.JSON.encode(json);
			}
			//验证  
				function testRecord(){
				
				for (var i = 0; i < (store.getCount()); i++){
					var record = store.getAt(i);
					      var newdefectModel = record.get('defectModel');
					       if(newdefectModel != null){
					       	  newdefectModel = newdefectModel.trim();
					       }
							if (newdefectModel == ''){
								alert('请填写第' + (i + 1) + '行的退化模式不可为空 !');
								return false; 
							}
							var newDefectDesc= record.get('defectDesc');
					       if(newDefectDesc != null){
					       	  newDefectDesc = newDefectDesc.trim();
					       }
							if (newDefectDesc == ''){
								alert('请填写第' + (i + 1) + '行的退化说明不可为空 !');
								return false;
							}
					//}
					}
				return true;
			}
					
				// 保存具体操作
			function updateDataDetail(modified ,value) {
				var json = [];
				var result = 0;
				Ext.each(modified, function(item) {
					json.push(item.data);
				});
				
				var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : commonality_waitMsg,
				    removeMask : true// 完成后移除
					});		
				waitMsg.show();
			
				Ext.Ajax.request( {
					url : lh3.app.g_lh3SaveUrl,
					method : "POST",
					params : {
						  jsonData : Ext.util.JSON.encode(json),
					      hsiId : hsiId 
					},
					success : function(response, action) {
						waitMsg.hide();
							alert(commonality_messageSaveMsg);
						    if(step[4] == 0){
						        step[4] = 2 ;
						    }
						    store.modified = [];
						    goNext(value);
					},
					failure : function(response,action) {
						waitMsg.hide();
						alert(commonality_cautionMsg);
					}
				});
			}
			
			nextOrSave = function (value){
				if(value == null){
					value = 4;
				}
				if(isMaintain!='1'){
					  goNext(value);
					  return false;
				       }
				if (store.modified.length == 0){ //没有修改时 直接跳转
					 goNext(value);
					 return null
				}
						Ext.MessageBox.show({
							    title : commonality_affirm,
							    msg : commonality_affirmSaveMsg,
							    buttons : Ext.Msg.YESNOCANCEL,
							    fn : function(id){
							    	if (id == 'cancel'){
										return;
									} else if (id == 'yes'){	
											if (!testRecord()){
												return ;
											}
										var modified = store.modified;	
										var result = updateDataDetail(modified,value);
										if (result == 0 ){
										 store.modified = [];
									     } 
							      }else if (id == 'no'){
									 goNext(value);
								}
							} 
						});

			}
					
		
			
		   /**
		   * 追加
		   * "LH_3"追加事件执行,追加一条记录
		   */ 
			// 添加一条空白行
		  function addRow(){
				var index = store.getCount();
				var rec = grid.getStore().recordType;
				var p = new rec({
				 	  	hsiId : '',
					 	lh3Id : '',
					    defectModel : '',
					 	defectDesc : ''
			        });
				  store.insert(index, p);
			}

				//传参数
				store.on("beforeload", function() {
				store.baseParams = {
					               start:0,
		                     	   limit:15,
			             		   hsiId : hsiId
							};
				});
			 store.load({
					　　params:{
				                start:0,
		                        limit:15
				　　　　　　}
　　　　　　　　　　 });

           //删除一条记录
             function del(){
				var record  = grid.getSelectionModel().getSelected();
				if (!record){
					alert(commonality_alertDel);
					return;
				}
				
				Ext.Msg.confirm(commonality_caution, commonality_affirmDelMsg, function(btn){
					if('yes' == btn){
						if (record.get('lh3Id') == ''){
							store.remove(record);	
							 store.modified = [];
							return;
						}
						var waitMsg = new Ext.LoadMask(Ext.getBody(), {
						    msg : commonality_waitMsg,
						    removeMask : true// 完成后移除
							});		
						 waitMsg.show();	
						Ext.Ajax.request({
							url : lh3.app.g_lh3DeleteUrl,
							params : {
								lh3Id : record.get('lh3Id')
							},
							success : function(response, action) {
								waitMsg.hide();
								var text = Ext.util.JSON.decode(response.responseText);
								if (text.success == true) {
									 alert( commonality_messageDelMsg);
									  grid.getStore().reload();
							　　              store.modified = [];
								}else{
									alert( commonality_messageDelMsgFail);
								}
							},
							failure :function(response, action){
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});
					}
				});
			}
			
			//=========
              }
           }
  }();