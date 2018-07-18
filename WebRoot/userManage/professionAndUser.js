Ext.namespace('professionAndUser');
Ext.onReady(function() {
	
			var professionUrlPrefix = contextPath + "/userManage/profession/";
			var jsonLoadProfessionByUserId = "jsonLoadProfessionByUserId.do";
			var jsonLoadUserByProfessonIdUrl = "jsonLoadUserByProfessonId.do";
			var jsonLoadUserByProfessonIdOtherUrl = "jsonLoadUserByProfessonIdOther.do";
			var jsonUpdatePositionByParmUrl = "jsonUpdatePositionByParm.do";
			var jsonDelUserInPofessionUrl = "jsonDelUserInPofession.do";
			var jsonAddUserToPofessionUrl = "jsonAddUserToPofession.do";
			
			var rendToDivName = 'thisUsersGrid';

			/** 取机型系列名称 * */
			var owerProfessionCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						fieldLabel : "专业室",
						name : 'professionType',
						id : 'professionType',
						store : new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
								url : professionUrlPrefix + jsonLoadProfessionByUserId
							}),
							reader : new Ext.data.JsonReader({
								root : 'ownProfession',
								fields : [ {
									name : 'professionId',
									type : 'string'
								}, {
									name : 'professionName',
									type : 'string'
								} ]
							})
						}),
						valueField : "professionId",
						displayField : "professionName",
						typeAhead : true,
						mode : 'local',
						triggerAction : 'all',
						emptyText : "",
						editable : false,
						selectOnFocus : true,
						width : 150,
						listeners : {
							select : owerProfessionComboClick
						}
					});
			owerProfessionCombo.store.on("load",function(){   //对 ComboBox 的数据源加上 load 事件就好  
				owerProfessionCombo.setValue(this.getAt(0).get('professionId'));
				owerProfessionComboClick();
		    }); 
			owerProfessionCombo.store.load();

			// shorthand alias
			var sm = new Ext.grid.RowSelectionModel({
				singleSelect : false
			});
			var cm = new Ext.grid.ColumnModel({
				// specify any defaults for each column
				defaults : {
					sortable : true
				// columns are not sortable by default
				},
				columns : [ {
					header : '',
					dataIndex : 'userId',
					width : 50,
					hidden : true,
					resizable : false,
					fixed : true,
					sortable : false
				}, {
					header : '用户编号',
					dataIndex : 'userCode',
					width : 100
				}, {
					header : '用户名',
					dataIndex : 'userName',
					width : 100
				}, {
					header : '是否专业室管理员',
					width : 120,
					sortable : true,
					align : 'center',
					dataIndex : 'positionAdmin',
					renderer:positionAdminCheck
				}, {
					header : '是否专业室总工程师',
					width : 120,
					sortable : true,
					align : 'center',
					dataIndex : 'positionEngineer',
					renderer:positionEngineerCheck
				}, {
					header : '是否专业室分析人员',
					width : 120,
					sortable : true,
					align : 'center',
					dataIndex : 'positionAnalyst',
					renderer:positionAnalystCheck
				}]
			});
			
			updatePostionByUserId = function(obj,userId,posintionId){
				Ext.Ajax.request({
							url : professionUrlPrefix + jsonUpdatePositionByParmUrl,
							params : {
								insertOrDelteFlag : obj.checked,
								userId : userId,
								positionId: posintionId,
								professionId : owerProfessionCombo.getValue()
							},
							async : false, // 是否为异步请求
							method : "POST",
							waitMsg : commonality_waitMsg,
							success : function(response) {
								if (response.responseText !== null && response.responseText !== '') {
									var result = Ext.util.JSON.decode(response.responseText);
									if (result.success == true) {
										
									} else {
										if (result.success == 'exits') {
											alert('设置职务失败!');
										}
									}
								}
							},
							failure : function(response) {
								alert('设置职务失败 !');
							}
						});			     
			}
			
			function positionAdminCheck(value, cellmeta, record, rowIndex, columnIndex, store){
				var checkFlag = "";
				if(value != '' && value == 1){
					checkFlag = "checked";
				}
				//当前用户的管理权限不能修改
				var thisUserId = record.get("userId");
				var strReadOnly = "";
				if(curUserId == thisUserId){
					strReadOnly = "  disabled='true'";
				}
				//如果专业是是超级
				if(professionIdForAdmin == owerProfessionCombo.getValue()){
					strReadOnly = "  disabled='true'";
				}
		        return "<input type='checkbox' name='positionAdmin' id='positionAdmin' onclick='updatePostionByUserId(this,\"" + record.get("userId") + "\",\"" + posintionAdminId + "\")' " + checkFlag + strReadOnly + "></input>";
		    }
			function positionEngineerCheck(value, cellmeta, record, rowIndex, columnIndex, store){
				var checkFlag = "";
				if(value != '' && value == 1){
					checkFlag = "checked";
				}
				//如果专业是是超级
				var strReadOnly = "";
				if(professionIdForAdmin == owerProfessionCombo.getValue()){
					strReadOnly = "  disabled='true'";
				}
		        return "<input type='checkbox' name='positionAdmin' id='positionAdmin' onclick='updatePostionByUserId(this,\"" + record.get("userId") + "\",\"" + posintionEngineerId + "\")' " + checkFlag + strReadOnly + "></input>";
		    } 
			function positionAnalystCheck(value, cellmeta, record, rowIndex, columnIndex, store){
				var checkFlag = "";
				if(value != '' && value == 1){
					checkFlag = "checked";
				}
				//如果专业是是超级
				var strReadOnly = "";
				if(professionIdForAdmin == owerProfessionCombo.getValue()){
					strReadOnly = "  disabled='true'";
				}
		        return "<input type='checkbox' name='positionAdmin' id='positionAdmin' onclick='updatePostionByUserId(this,\"" + record.get("userId") + "\",\"" + posintionAnalystId + "\")' " + checkFlag + strReadOnly + "></input>";
		    } 
			// create the Data Store
			var storeOwnerUser = new Ext.data.Store({
				// load remote data using HTTP
				baseParams: {professionId: owerProfessionCombo.getValue()}, 
				proxy : new Ext.data.HttpProxy({
					url : professionUrlPrefix + jsonLoadUserByProfessonIdUrl
				}),
				reader : new Ext.data.JsonReader({
					root : 'comUsers',
					// a Record constructor
					fields : [ {
						name : 'userId',
						type : 'string'
					}, {
						name : 'userCode',
						mapping : 'userCode'
					}, {
						name : 'userName',
						mapping : 'userName'
					}, {
						name : 'positionAdmin',
						mapping : 'positionAdmin'
					}, {
						name : 'positionEngineer',
						mapping : 'positionEngineer'
					}, {
						name : 'positionAnalyst',
						mapping : 'positionAnalyst'
					}]
				}),
				sortInfo : {
					field : 'userCode',
					direction : 'ASC'
				}
			});

			// create the editor grid
			var grid = new Ext.grid.GridPanel(
					{
						id : 'grid',
						store : storeOwnerUser,
						title : '已分配用户',
						cm : cm,
						sm : sm,
						// header : false,
						stripeRows : true,
						// loadMask : false,
						renderTo : rendToDivName,
						split : true,
						// collapsible : true,
						frame : true,
						clicksToEdit : 1,
						bbar : new Ext.PagingToolbar({
							store : storeOwnerUser,
							pageSize : 18,
							displayInfo : true,
							displayMsg : commonality_turnPage,
							emptyMsg : commonality_noRecords
						}),
						tbar : new Ext.Toolbar(
								{
									items : [	'用户编号：',
												{
										xtype : 'textfield',
										id : 'userCodeOwnerInput',
										width : 100
									},
									new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
									'用户名：',
									{
										xtype : 'textfield',
										id : 'userNameOwnerInput',
										width : 100
									}, '-',
									new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
									{
										text : commonality_search,
										iconCls : "icon_gif_search",
										id : 'sericonOwner',
										handler : ownerUserLoad
									}, '-',
									new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"),
									{
										text : commonality_reset, // 重置
										xtype : "button",
										iconCls : "icon_gif_reset",
										handler : function() {
										//	Ext.getCmp('model').clearValue();
											Ext.getCmp('userCodeOwnerInput').setValue('') ;
											Ext.getCmp('userNameOwnerInput').setValue('') ;
										}
									} ]
								}),
						listeners : {
							rowdblclick : function(grid,row){
								if(curUserId == grid.getSelectionModel().getSelected().get("userId")){
									alert('不能移除当前用户!');
									return;
								}
									Ext.Ajax.request({
										url : professionUrlPrefix + jsonDelUserInPofessionUrl,
										params : {
											userId : grid.getSelectionModel().getSelected().get("userId"),
											professionId : owerProfessionCombo.getValue()
										},
										async : false, // 是否为异步请求
										method : "POST",
										waitMsg : commonality_waitMsg,
										success : function(response) {
											if (response.responseText !== null && response.responseText !== '') {
												var result = Ext.util.JSON.decode(response.responseText);
												if (result.success == true) {
													owerProfessionComboClick();
												} else {
													if (result.success == 'exits') {
														alert('删除人员失败!');
													}
												}
											}
										},
										failure : function(response) {
											alert('删除人员失败 !');
										}
									});
						    }
						}
					}); // grid over
			grid.render();
			
			function owerProfessionComboClick(){
				ownerUserLoad();
				otherUserLoad();
			}

			storeOwnerUser.on("beforeload", function() {
				storeOwnerUser.baseParams = {
							start : 0,
							limit : 18,
							professionId : owerProfessionCombo.getValue(),
							userCode : Ext.getCmp('userCodeOwnerInput').getValue(),
							userName : Ext.getCmp('userNameOwnerInput').getValue()
						};
					});
			function ownerUserLoad(){
				storeOwnerUser.load({
					params : {
						start : 0,
						limit : 18,
						professionId : owerProfessionCombo.getValue(),
						userCode : Ext.getCmp('userCodeOwnerInput').getValue(),
						userName : Ext.getCmp('userNameOwnerInput').getValue()
					}
				});
			}
			
			function otherUserLoad(){
				storeOtherUsers.load({
					params : {
						start : 0,
						limit : 18,
						professionId : owerProfessionCombo.getValue(),
						userCode : Ext.getCmp('userCodeInput').getValue(),
						userName : Ext.getCmp('userNameInput').getValue()
					}
				});
			}
			
			// *-----其他用户
			var smUsersOther = new Ext.grid.RowSelectionModel({
				singleSelect : false
			});
			
			var cmUserOther = new Ext.grid.ColumnModel([ {
				header : '',
				hidden : true,
				dataIndex : 'userId'
			}, {
				header : '用户编号',
				width : 70,
				sortable : true,
				dataIndex : 'userCode'
			}, {
				header : '用户名',
				width : 120,
				sortable : true,
				dataIndex : 'userName'
			}]);
			cmUserOther.defaultSortable = true;

			storeOtherUsers = new Ext.data.Store({
				baseParams: {professionId: owerProfessionCombo.getValue()}, 
				proxy : new Ext.data.HttpProxy({
					url : professionUrlPrefix + jsonLoadUserByProfessonIdOtherUrl
				}),
				// create reader that reads the Topic records
				reader : new Ext.data.JsonReader({
					root : 'comUsers',
					totalProperty : 'total',
					fields : [ {
						name : 'userId',
						mapping : 'userId',
						type : 'String'
					}, {
						name : 'userCode',
						mapping : 'userCode',
						type : 'string'
					}, {
						name : 'userName',
						mapping : 'userName',
						type : 'string'
					}]
				}),
				sortInfo : {
					field : 'userCode',
					direction : 'ASC'
				}
			});

			storeOtherUsers.on("beforeload", function() {
				storeOtherUsers.baseParams = {
					start : 0,
					limit : 18,
					professionId : owerProfessionCombo.getValue(),
					userCode : Ext.getCmp('userCodeInput').getValue(),
					userName : Ext.getCmp('userNameInput').getValue()
				};
			});
			
			var userOtherGrid = new Ext.grid.GridPanel({
				store : storeOtherUsers,
				layout : 'fit',
				region : 'east',
				frame : true,
				id : 'AllUsersGrid',
				stripeRows : true,
				title : '未选择用户',
				cm : cmUserOther,
				sm : smUsersOther,
				trackMouseOver : false,
				loadMask : false,
				bbar : new Ext.PagingToolbar({
					store : storeOtherUsers,
					pageSize : 18,
					displayInfo : true,
					displayMsg : commonality_turnPage,
					emptyMsg : commonality_noRecords
				}),
				tbar : new Ext.Toolbar(
						{
							items : [	'用户编号：',
										{
											xtype : 'textfield',
											id : 'userCodeInput',
											width : 100
										},
										new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
										'用户名：',
										{
											xtype : 'textfield',
											id : 'userNameInput',
											width : 100
										}, '-',
										new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
										{
											text : commonality_search,
											iconCls : "icon_gif_search",
											id : 'sericon',
											handler : otherUserLoad
										}, '-',
										new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"),
										{
											text : commonality_reset, // 重置
											xtype : "button",
											iconCls : "icon_gif_reset",
											handler : function() {
											//	Ext.getCmp('model').clearValue();
												Ext.getCmp('userCodeInput').setValue('') ;
												Ext.getCmp('userNameInput').setValue('') ;
											}
										} ]
						}),
				listeners : {
					rowdblclick : function(grid,row){
							Ext.Ajax.request({
								url : professionUrlPrefix + jsonAddUserToPofessionUrl,
								params : {
									userId : userOtherGrid.getSelectionModel().getSelected().get("userId"),
									professionId : owerProfessionCombo.getValue()
								},
								async : false, // 是否为异步请求
								method : "POST",
								waitMsg : commonality_waitMsg,
								success : function(response) {
									if (response.responseText !== null && response.responseText !== '') {
										var result = Ext.util.JSON.decode(response.responseText);
										if (result.success == true) {
											owerProfessionComboClick();
										} else {
											if (result.success == 'exits') {
												alert('添加人员失败!');
											}
										}
									}
								},
								failure : function(response) {
									alert('添加人员失败 !');
								}
							});
				    }
				}
			});			
			
			var window = new Ext.Window({
				layout : 'border',
				border : false,
				resizable : false,
				closable : false,
				maximized : true,
				title : returnPageTitle('专业室用户分配', 'professionUser'),
				plain : true,
				id : 'cnm',
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				tbar : new Ext.Toolbar({
							items : ['请选择专业室',':',owerProfessionCombo ]
				}),
				items : [{
					region : 'west',
					layout : 'fit',
					split : true,
					width : 600,
					items : [ grid ]
				}, {
					region : 'center',
					layout : 'fit',
					split : true,
					width : 160,
					items : [ userOtherGrid ]
				} ]
			});
			window.show();

			var waitMsg = new Ext.LoadMask(Ext.getCmp('cnm').body, {
				msg : commonality_waitMsg,
				// 完成后移除
				removeMask : true
			});
			//urGrid.getEl().select('div.x-grid3-hd-checker').hide();
		});