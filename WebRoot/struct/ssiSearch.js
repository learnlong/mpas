Ext.onReady(function(){
			var colM = new Ext.grid.ColumnModel([
				{
			    	header : "ID", 
			    	dataIndex : "ssiId", 
			    	hidden : true
		    	},{
			    	header : '编号', 
			    	dataIndex : "ssiCode"
		    	},{
			    	header : '机型系列', 
			    	dataIndex : "model"
		    	},{
			    	header :'SSI名称', 
			    	dataIndex : "ssiName"
		    	},{
			    	header :'是否需要分析', 
			    	dataIndex : "isAna",
			    	renderer: function(value, meta, record) {     
						if(value==1){
						return commonality_shi;
						}else{
						return commonality_fou;
						}     
					}
		    	},{
			    	header :'是否自增', 
			    	dataIndex : "isOwn",
			    	renderer: function(value, meta, record) {     
						if(value==1){
						return commonality_shi;
						}else{
						return commonality_fou;
						}     
					}
		    	}
			]);
			var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy( {
					url : contextPath+"/struct/ssiSearch/getRecords.do"
				}),
				reader : new Ext.data.JsonReader({
					 totalProperty : "totleCount",
					 root : "ssi",
					 fields : ["ssiId","ssiCode","ssiName","isOwn","isAna","model"]
				})
			});

		var isSsi = new Ext.form.ComboBox({   
			xtype : 'combo', 
			name : 'isSSi', 
			id : 'isSSi',
			store : new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
					data : [['2','全部'],['0','非SSI'],['1','SSI']]
			}),  
			valueField : "retrunValue", 
			displayField : "displayText",
			typeAhead : true, 
			mode : 'local',
			triggerAction : 'all', 
			width : 100,
			emptyText :'---全部---',
			editable : false,
			selectOnFocus : true
		});
		
		var ssiName = new Ext.form.TextField ({   
			xtype : 'combo', 
			name : 'ssiName', 
			id : 'ssiName',
			width : 100,
			emptyText : '---全部---',
			editable : false
		});
		
		
		var isOwn = new Ext.form.ComboBox({   
			xtype : 'combo', 
			name : 'isOwn', 
			id : 'isOwn',
			store : new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
					data : [['2','全部'],['0','非自增'],['1','自增']]
			}),  
			valueField : "retrunValue", 
			displayField : "displayText",
			typeAhead : true, 
			mode : 'local',
			triggerAction : 'all', 
			width : 100,
			emptyText : '---全部---',
			editable : false, 
			selectOnFocus : true
		});
		var grid = new Ext.grid.GridPanel({
			title :returnPageTitle('SSI查询','ssiSearch'),
			region: 'center',
			loadMask : {
						msg : commonality_waitMsg
					},
			cm : colM,
			sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
			store : store,
			stripeRows: true,				
			tbar : new Ext.Toolbar({
				items : [
				        'SSI',"：",isSsi, 
				        '-',
				        new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"),
						 '是否自增',"：",isOwn,
						 '-',
						 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"), 
						'ssi名称',"：",ssiName,
						'-',
						new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"), 
						 {
							text : commonality_search,
							iconCls : "icon_gif_search",
							handler : function() {
									store.load({
									params:{
										isSsi:isSsi.getValue(),
										isOwn:isOwn.getValue(),
										ssiName:ssiName.getValue(),
										limit:20
									}})
						
								}
						},
						'-',	
						new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"), {
							text : commonality_reset,  //重置
							xtype : "button",
							iconCls : "icon_gif_reset",
							handler : function() {
								Ext.getCmp('isSSi').clearValue() ;
								Ext.getCmp('isOwn').clearValue();
								Ext.getCmp('ssiName').setValue('');
							}
						}]
				}),
				bbar : new Ext.PagingToolbar({
				pageSize : 20,
				store : store,
				displayInfo : true,
				displayMsg : commonality_turnPage,
				emptyMsg :commonality_noRecords
			})
		});
	
		var viewport = new Ext.Viewport( {
				id:'viewportId',
				layout : 'fit',
				items : [grid]
			});
		store.on('beforeload',function(s,options){
				var newParams={
						isSsi:isSsi.getValue(),
						isOwn:isOwn.getValue(),
						ssiName:ssiName.getValue(),
						limit:20
					};
			Ext.apply(options.params,newParams);
		});
			
			store.load({params:{
							limit : 20	
						}});
		})