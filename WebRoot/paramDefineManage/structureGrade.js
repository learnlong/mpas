// 创建命名空间
Ext.namespace('structureGrade');

// 创建应用程序
structureGrade.app = function() {
	return{
		init:function(){
			Ext.form.Field.prototype.msgTarget = 'qtip';
			Ext.QuickTips.init(); 
			
		
			
		    /*
			 * 初始化"自定义评级列表S6区域"的数据
			 */
		    var storeS6 = new Ext.data.Store({
			url : structureGrade.app.loadCusIntSUrl,
			pruneModifiedRecords : true,
			reader : new Ext.data.JsonReader({
				root : "rootS6",
				fields : [
				 	{name : 'levelS6'},
				 	{name : 'valueInt'},
				 	{name : 'valueOut'},
				 	{name : 'intervalIntId'},
				 	{name : 'intervalOutId'}
			 	]
			})
		});
		storeS6.load();

	       /*
			 * 用于绘制"自定义评级列表S6"的表样式和字段
			 */
		 var colMS6 = new Ext.grid.ColumnModel([  
		                              	    	{
											    	header : "interiorID", 
											    	dataIndex : "intervalIntId", 
											    	hidden : true
										    	},
										    	{
											    	header : "externalID", 
											    	dataIndex : "intervalOutId", 
											    	hidden : true
										    	},
		                              			{
		                              				header : "<div align='center'>"+"等级" + commonality_mustInput+"</div>",
		                              				dataIndex : "levelS6",
		                              				align :'center',
		                              				width : 100
		                              	    	},{
		                              				header : '内部' + commonality_mustInput, 
		                              				dataIndex : "valueInt",
		                              				width : 420 ,
		                              				align :'center',
		                              				editor : new Ext.form.TextField({
		                              				    allowBlank : false,
		                        						maxLength : 50
		                        						///maxLengthText : commonality_MaxLengthText+" 50!"
		                        					})
		                              	    	},{
		                        					header : '外部' + commonality_mustInput,
		                              				dataIndex : "valueOut",
		                              				width : 420,
		                              				align :'center',
		                              				editor : new Ext.form.TextField({
		                              				    allowBlank : false,
		                        						maxLength : 50
		                        						///maxLengthText : commonality_MaxLengthText+" 50!"
		                        					})
		                              	    	}
		                              		]);			
	
		

		/**
		 * 用于绘制"自定义评级表S6区域"的表头样式和追加,保存,删除按钮.
		 */
		var gridS6 = new Ext.grid.EditorGridPanel({
			title:'结构评级  内部/外部',
			sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
			clicksToEdit : 2, 
			cm:colMS6,
			border:true,
			stripeRows : true,
			store:storeS6,
			region:'center',
			tbar:[
			  	new Ext.Toolbar.TextItem("&nbsp;&nbsp;"),		
			      new Ext.Button({
				          text:commonality_add,
				          iconCls:"icon_gif_add",
				          handler:addS6
			}),
			  "-",
			  new Ext.Button({
		          text:commonality_del,
		          iconCls:"icon_gif_delete",
		          handler:deleteS6
			}),
			  "-", 
		          new Ext.Button({
			          text:commonality_save,
			          iconCls:"icon_gif_save",
			          handler:saves6
				})

			]
		});
		 /*
		  *  初始化画面
		  */var win = new Ext.Window(
		       {
		    	 renderTo: Ext.getBody(),
		    	 animateTarget : Ext.getBody(),
		    	    constrain : true,//保证窗口不会越过浏览器的边界
				layout : 'border',
				border : false, 
				resizable : true,
				closable : false,
				maximized : true,
				plain : false,
				//bodyStyle : 'padding:0px;',
				//buttonAlign : 'center',
				title :  returnPageTitle('自定义结构评级表','structureGrade'),
				items:[gridS6]
				
			});
			win.show();	
			
		
			/**
		      * 追加
		      * "自定义评级表S6"追加事件执行,追加一条记录
		      */ 
			function addS6(){
				var index = storeS6.getCount();
				var rec = gridS6.getStore().recordType;
				var p = new rec({
					levelS6:index+1,
				    valueInt:'',
				 	valueOut:'',
				 	intervalIntId:'',
				 	intervalOutId:''
			        });
				storeS6.insert(index, p);
				storeS6.modified.push(p);
			}
			
	
		
			/**
			 * 保存
			 * "自定义评级表S6"提交的合理数据信息
			 */
			function saves6(){
				Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,
				    buttons : Ext.Msg.YESNO,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){
							if(storeS6.modified.length == 0){
								alert(commonality_alertUpdate);
							}else{
									if (!tests6()){
										return false;
									}
									doSaveDatas6();
							}
						}
				    }
				});		
			}
			/**
			 * 验证
			 * "自定义评级表S6"提交的数据信息是否为空,是否重复
			 * @returns boolean
			 */
	
			function tests6() {
				var temp = 0;
				// 检测输入的内容是否存在空值和是否存在重复的敏感级别
				for (var i = 0; i < storeS6.modified.length; i++) {
					var record = storeS6.modified[i];
					if (record.get('levelS6') == null || record.get('levelS6') == '') {
						temp = 1;
						alert('请填写等级');
						return false;
					}
					if (record.get('valueInt') == null || record.get('valueInt') == '') {
						temp = 1;
						alert('请填写完整内部间隔值 !' );
						return false;
					}
					if (record.get('valueOut') == null || record.get('valueOut') == '') {
						temp = 1;
						alert('请填写完整外部间隔值 !');
						return false;
					}
					
				}
				
				if (temp == 0) {
					return true;
				} else {
					return false;
				}
			}

			function doSaveDatas6(){
				var json = [];
				Ext.each(storeS6.modified, function(item) {
					json.push(item.data);
				});
				if (json.length > 0){
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true// 完成后移除
						});
					 waitMsg.show();	
					Ext.Ajax.request( {
						url : structureGrade.app.saveCusIntervalUrl,
						params : {
							jsonData : Ext.util.JSON.encode(json)
						},
						method : "POST",
						success : function(form,action) {
							waitMsg.hide();
							storeS6.load();
							storeS6.modified = [];
							alert( commonality_messageSaveMsg);
						},
						failure : function(form,action) {
							waitMsg.hide();
							alert(commonality_cautionMsg);
						}
					});
				} else {
					waitMsg.hide();
					alert(commonality_alertSave);
					return;
				}
			}
		
		
	       /**
			 * 删除
			 * "自定义评级表S6"选择一条数据记录
			 */
	       	function deleteS6(){
				var record = gridS6.getSelectionModel().getSelected();
				var IntId ;
				var OutId;
				if (record == null){
					alert(commonality_alertDel);
					return;
				}else{
					IntId = record.get('intervalIntId');
					OutId = record.get('intervalOutId');
				}
				
				Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmDelMsg,
				    buttons : Ext.Msg.YESNO,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){
							var record = gridS6.getSelectionModel().getSelected();
							if (record.get('intervalIntId') == '' && record.get('intervalOutId') == ''){
								storeS6.remove(record);
								//直接删除后 也要排序
								for (var i = 0; i < storeS6.getCount(); i++){
									 var record = storeS6.getAt(i);
									  record.set('levelS6', i+1);
								}
								return;
							} else {   ///删除操作  ajax
								
								var json = [];    //重新排序
								var re = gridS6.getSelectionModel().getSelected();
								storeS6.remove(re);
								for (var i = 0; i < storeS6.getCount(); i++){
									 var record = storeS6.getAt(i);
									  record.set('levelS6', i+1);
									json.push(record.data);
								}
								var waitMsg = new Ext.LoadMask(Ext.getBody(), {
								    msg : commonality_waitMsg,
								    removeMask : true// 完成后移除
									});	
								 waitMsg.show();	
								Ext.Ajax.request( {
									url :  structureGrade.app.delCusIntlUrl,
									params : {						
										deleteIdAint : IntId,
										deleteIdBout : OutId,
										jsonData : Ext.util.JSON.encode(json)
										
									},
									method : "POST",
									success : function(form,action) {
										waitMsg.hide();
										storeS6.load();
										storeS6.modified = [];
										alert( commonality_messageDelMsg);
										
									},
									failure : function(form,action) {
										waitMsg.hide();
										alert( commonality_cautionMsg);
									}
								});
							}							
						}
				    }
				});				
			}
	

	////
	}
	}
}();

	
	
	
