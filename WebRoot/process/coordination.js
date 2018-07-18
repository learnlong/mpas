/**
 * 此页面用来查询协调单信息。
 * 
 * 
 */
Ext.namespace('coordination');
coordination.app = function(){
	return{
		    init : function(){
						    store = new Ext.data.Store({
							url : coordination.app.loadData,
							reader : new Ext.data.JsonReader({
			 				root : 'coorData',
			 				fields : [{
					 					name :"type"
					 				},{
					 					name :"id"
					 				},{
					 					name : "receiveArea"	
					 				},{
					 					name : "sendUser"
					 				},{
					 					name : "receiveUser"	
					 				},{
					 					name : "isReceive"
					 				},{
					 					name : "sendDate"
					 				},{
					 					name : "permission"
					 				},{
					 					name : "s6OutOrIn"
					 				},{
					 				    name : "taskId"
					 				}]
			 				})
			      		});
			      		store.on("beforeload",function(){
							store.baseParams = {
								start : 0,
								limit : 16						
									}
								});
						store.load();
			var com = new Ext.grid.ColumnModel([{
			         header : "协调单id",
			         hidden : true,
			         width : 100,
			         dataIndex : 'id'
			 		 },{
			  	    header : "来源",
			  	    width : 100,
			  	    dataIndex : 'type'
			 		 },{
			  	    header : "去向",
			  	    width : 100,
			  	    dataIndex : 'receiveArea'
			 		 },{
			  		header : "转出人",
			  		width : 100,
			  		dataIndex : 'sendUser'
			  		},{
			  		header : "接收人",
			  		width : 100,
			  		dataIndex : 'receiveUser'
			  		},{
			  		header : "状态",
			  		width : 100,
			  		dataIndex : 'isReceive'	
			  		},{
			  		header : "发起日期",
			  		width : 100,
			  		dataIndex : 'sendDate'
			  		},{
			  		header :"协调单",
			  		dataIndex : 'taskId',
			  		renderer : function(value, cellmeta, record){
								var returnvalue = value;
								if(record.get('permission') == 3){									
									if (record.get('s6OutOrIn') != "" && record.get('s6OutOrIn') != null) {
										returnvalue = "<a href='javascript:void(0)' title='"+"协调单详情"+"'>"
												+ "<img src='"+ contextPath+ "/images/toCheckBtn.gif'"
												+"onclick=\"strToSysCoordination('" +value+"','"+record.get('s6OutOrIn')+"')\"/></a>";
									}else{									
										returnvalue = "<a href='javascript:void(0)' title='"+"协调单详情"+"'>"
												+ "<img src='"+ contextPath+ "/images/toCheckBtn.gif'"
												+"onclick='coordination(\"" +value+"\")'/></a>";
									}																
								}else if(record.get('permission') == 2){
									if (record.get('s6OutOrIn') != "" && record.get('s6OutOrIn') != null) {
										returnvalue = "<a href='javascript:void(0)' title='"+"修改协调单"+"'>"
												+ "<img src='"+ contextPath+ "/images/toAuditBtn.gif'"
												+"onclick=\"strToSysCoordination('" +value+"','"+record.get('s6OutOrIn')+"')\"/></a>";
									}else{									
										returnvalue = "<a href='javascript:void(0)' title='"+"修改协调单"+"'>"
												+ "<img src='"+ contextPath+ "/images/toAuditBtn.gif'"
												+"onclick='coordination(\"" +value+"\")'/></a>";
									}			
								}else{
									if (record.get('s6OutOrIn') != "" && record.get('s6OutOrIn') != null) {
										returnvalue = "<a href='javascript:void(0)' title='"+"答复"+"'>"
												+ "<img src='"+ contextPath+ "/images/maintain.gif'"
												+"onclick=\"strToSysCoordination('" +value+"','"+record.get('s6OutOrIn')+"')\"/></a>";
									}else{									
										returnvalue = "<a href='javascript:void(0)' title='"+"答复"+"'>"
												+ "<img src='"+ contextPath+ "/images/maintain.gif'"
												+"onclick='coordination(\"" +value+"\")'/></a>";
									}		
								}
								return returnvalue;
							}
			  		}
			  	]);
	// 报告来源下拉框
	var coo_DataComeFromCombo = new Ext.form.ComboBox({
		xtype : 'combo',
		name : 'type1',
		id : 'type1',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [[null, '全部'], ["SYS_TO_Z", "系统分析"],
					["STR_TO_Z", "结构分析"],
					["LH_TO_Z", "L/H分析"]]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		typeAhead : true,
		mode : 'local',
		fieldLabel : "来源",
		triggerAction : 'all',
		width : 100,
		emptyText : '--全部--',
		editable : false,
		selectOnFocus : true
	});
	// 状态下拉框
	var coo_StatusCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'isReceive1',
				id : 'isReceive1',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [[null, '全部'],
									["1", "未审批"],
									["2", "未提交"],
									["3", "审批未通过"],
									["4", "审批通过"]]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				fieldLabel : "状态",
				triggerAction : 'all',
				width : 100,
				emptyText : '--全部--',
				editable : false,
				selectOnFocus : true
			});

			 var grid = new Ext.grid.EditorGridPanel({
			    store : store,
			    cm : com,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
			    region : 'center',
				clicksToEdit : 2,
				stripeRows : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 16,
							store : store,
							displayInfo : true,
							displayMsg : commonality_turnPage,
							emptyMsg : commonality_noRecords
						}),
				tbar : new Ext.Toolbar({
						items : [
								"来源",
								"：",coo_DataComeFromCombo,
								'-',
								new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"),
								"去向",
								"：",new Ext.form.TextField({
										name :'comreceiveArea1',
										id:'comreceiveArea1'
									}),
								'-',
								new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"),
								"状态",
							    "：",coo_StatusCombo,
							    '-',
							    new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"),
									{
										text : commonality_search,
										iconCls : "icon_gif_search",
										id : 'sericon',
										handler : function() {
											var type = Ext.getCmp('type1').getValue();
											var comreceiveArea = Ext.getCmp('comreceiveArea1').getValue().trim();
											var isReceive = Ext.getCmp('isReceive1').getValue();
											store.on("beforeload", function() {
														store.baseParams = {
															start : 0,
															limit : 16,
															type : type,
															comreceiveArea : comreceiveArea,
															isReceive : isReceive
														}
													});
											store.load();
										}
									},
									'-',	
									new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"), 
										{   
											text : commonality_reset,///重置
													xtype : "button",
													iconCls : "icon_gif_reset",
													handler : function() {
														Ext.getCmp('type1').clearValue();
														Ext.getCmp('comreceiveArea1').setValue("");
														Ext.getCmp('isReceive1').clearValue();
													}
									}
							    
								]
							})
			  });
			  var win = new Ext.Window({
			  		    layout : 'fit',
						border : false,
						resizable : true,
						closable : false,
						maximized : true,
					/*	plain : true,
						bodyStyle : 'padding:1px;',*/
						title : returnPageTitle('协调单查询', 'coordinationSearch'),
						items : [grid]
			  });
			  win.show();	     	
			}
		}
}();

