Ext.namespace('userAndAuth');
var treePanel;
var waitMsg;
var numAjax = 0;
var waitMsgHide = function(){
		if(--numAjax == 0){		
		  if (waitMsg != undefined && waitMsg != null){
		  	waitMsg.hide();
		  }
		}
};
Ext.onReady(function() {
	
			var urlPrefix = contextPath + "/userManage/authority/";
			var jsonLoadProfessionByUserId = "jsonLoadProfessionByUserId.do";
			var jsonLoadUserByProfessonIdUrl = "jsonLoadUserByProfessonId.do";
			var loadTreeForSelectsByUserIdUrl = "loadTreeForSelectsByUserId.do";
			var updateTreeForSelectsByUserIdUrl = "updateTreeForSelectsByUserId.do";
			var getTreeIsCheckBeforeLoadForUserUrl = "getTreeIsCheckBeforeLoadForUser.do";
			var rendToDivName = 'usersGrid';
			var currentRowIndex = -1;
			var currentRecord;
			var treeIsCheckBeforeLoad = false;
			
			function cellclick(grid,rowIndex, columnIndex, e) {
				if (currentRowIndex != rowIndex) {
					currentRowIndex = rowIndex;
					currentRecord = grid.getStore().getAt(rowIndex);
					// currentRecord = grid.getSelectionModel().getSelected();
					treePanel.root.reload();
					//treePanel.root.expand(true);
				}
				treeIsCheckBeforeLoad = getTreeIsCheckInLoad();
			}

			/** 机型选择框 * */
			var owerProfessionCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						fieldLabel : "专业室",
						name : 'professionType',
						id : 'professionType',
						store : new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
								url : urlPrefix + jsonLoadProfessionByUserId
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
					header : '职务',
					width : 120,
					sortable : true,
					dataIndex : 'positionNames'
				}]
			});
			
			// create the Data Store
			var storeOwnerUser = new Ext.data.Store({
				// load remote data using HTTP
				baseParams: {professionId: owerProfessionCombo.getValue()}, 
				proxy : new Ext.data.HttpProxy({
					url : urlPrefix + jsonLoadUserByProfessonIdUrl
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
						name : 'positionNames',
						mapping : 'positionNames'
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
							pageSize : 17,
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
								})
					}); // grid over
			grid.render();
			grid.addListener("cellmousedown", cellclick);
			
			function owerProfessionComboClick(){
				ownerUserLoad();
				authTreeLoad();
			}
			
			function ownerUserLoad(){
				storeOwnerUser.load({
					params : {
						start : 0,
						limit : 20,
						professionId : owerProfessionCombo.getValue(),
						userCode : Ext.getCmp('userCodeOwnerInput').getValue(),
						userName : Ext.getCmp('userNameOwnerInput').getValue()
					}
				});
				currentRowIndex = -1;
				currentRecord = null;
				treePanel.root.reload();
			}

			storeOwnerUser.on("beforeload", function() {
				storeOwnerUser.baseParams = {
					start : 0,
					limit : 20,
					professionId : owerProfessionCombo.getValue(),
					userCode : Ext.getCmp('userCodeOwnerInput').getValue(),
					userName : Ext.getCmp('userNameOwnerInput').getValue()
				};
			});
//----------------------------tree--------------------------------------

			// ATA树显示窗口
			var rootNode = new Ext.tree.AsyncTreeNode({
						text : 'ATA',
						id : '0'
					});

			var loader = new Ext.tree.TreeLoader({
						baseAttrs : {
							uiProvider : Ext.tree.TreeCheckNodeUI
						},
						url : urlPrefix + loadTreeForSelectsByUserIdUrl
					});

			loader.on('beforeload', beforeLoadEvent);

			function beforeLoadEvent(loader, node) {
				if (!node.isLeaf()) {
					loader.baseParams['professionId'] = owerProfessionCombo.getValue();// 专业组id
					loader.baseParams['userId'] = currentRecord==null?"":currentRecord.get('userId');// 用户id
					loader.baseParams['nodeId'] = node.id;// ataid
					loader.baseParams['analysisType'] = analysisTypeCombo.getValue();//
				}
				if (++numAjax == 1) {
					waitMsg = new Ext.LoadMask(Ext.getCmp('cnm').body, {
								msg : commonality_waitMsg,
								// 完成后移除
								removeMask : true
							});
					waitMsg.show();
				}
			}
			
			function authTreeLoad(){
				loader.on("load", function() {
					setTimeout("waitMsgHide()", 500);
				});
			}

			var analysisTypeCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'analysisType',
						id : 'analysisType',
						store : new Ext.data.SimpleStore({
									fields : ["retrunValue", "displayText"],
									data : [[systemCode, systemCodeCn],
											[structureCode, structureCodeCn],
											[zonalCode, zonalCodeCn],
											[lhrifCode, lhrifCodeCn]]
								}),
						valueField : "retrunValue",
						displayField : "displayText",
						typeAhead : true,
						mode : 'local',
						fieldLabel : zonalCodeCn,
						triggerAction : 'all',
						width : 100,
						emptyText : "请选择",
						editable : false,
						selectOnFocus : true,
						listeners : {
							"select" : function() {
								comboSelect();
							}
						}
					});
			analysisTypeCombo.setValue(systemCode);

			function comboSelect() {
				if(analysisTypeCombo.getValue() == systemCode || analysisTypeCombo.getValue() == structureCode){
					treePanel.getRootNode().setText("ATA");
				}else{
					treePanel.getRootNode().setText("区域");
				}
				treePanel.root.reload();
				if (currentRecord != undefined || currentRecord != null) {
					//treePanel.root.expand(true);
				}
				treeIsCheckBeforeLoad = getTreeIsCheckInLoad();
			}
			
			treePanel = new Ext.tree.TreePanel({
						title : '权限选择',
						id : 'treePanel',
						onlyLeafCheckable : false,//对树所有结点都可选   
						checkModel : 'cascade', //对树的级联多选 
						loader : loader,
						root : rootNode,
						autoScroll : true,
						bodyStyle : "background-color:#ffe8f6;padding:10 0 10 10",
						tbar:["分析类型",":",analysisTypeCombo,'-',
						     {text:'展开',  
				                handler:function(){  
				                	treePanel.expandAll();  
				                }  
						     },'-',{  
				                text:'折叠',  
				                handler:function(){  
				                	treePanel.collapseAll();  
				                	treePanel.root.expand();  
				                }  
				            },
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"),
							{
								text : commonality_save,
								iconCls : "icon_gif_save",
								handler: function() {
									if(currentRecord == null || currentRecord.get("userId") == null || currentRecord.get("userId") == ''){
										alert("请选择人员!");
									} else {
										var checkedNodes = Ext.getCmp('treePanel').getChecked();
										if(checkedNodes.length == 0 && treeIsCheckBeforeLoad == false){
											alert("请选择节点!");
										}else{
											Ext.Msg.confirm(commonality_affirm,
													commonality_affirmSaveMsg,
													function(btn) {
														if (btn === 'yes') {
															saveCheckTree();
														}
													});
										}
									}
								}
							}]
					});
			
			function getTreeIsCheckInLoad(){
				var flag = false;
				if(currentRecord == null || currentRecord == ''){
					return false;
				}
				Ext.Ajax.request({
					url : urlPrefix + getTreeIsCheckBeforeLoadForUserUrl,
					params : {
						analysisType : analysisTypeCombo.getValue(),
						professionId : owerProfessionCombo.getValue(),
						userId : currentRecord.get("userId")
					},
					async: false, 
					method : "POST",
					waitMsg : commonality_waitMsg,
					success : function(response) {
						if (response.responseText != null && response.responseText != '') {
							var result = Ext.util.JSON.decode(response.responseText);
							if (result.success == true || result.success == 'true') {
								flag = true;
							}
						}
					}
				});
				return flag;
			}
			
			function saveCheckTree() {
				var checkedNodes = Ext.getCmp('treePanel').getChecked();
				var s = [];
				var noHaschirld=[];//用来存储还没有渲染子节点且选中的节点
				for (var i = 0; i < checkedNodes.length; i++) {
					if(checkedNodes[i].firstChild==null){//没有展开过该节点下的子节点，即该节点下的子节点还没动态生成
						noHaschirld.push(checkedNodes[i].id);
					}else{
						s.push(checkedNodes[i].id);
					}
				}
				waitMsg = new Ext.LoadMask(Ext.getCmp('cnm').body, {
							msg : commonality_waitMsg,
							// 完成后移除
							removeMask : true
						});
				waitMsg.show();
				setTimeout(function () {
					Ext.Ajax.request({
						url : urlPrefix + updateTreeForSelectsByUserIdUrl,
						params : {
							analysisType : analysisTypeCombo.getValue(),
							professionId : owerProfessionCombo.getValue(),
							userId : currentRecord.get("userId"),
							choosedIds : s,
							choosedNoChilrdIds : noHaschirld
						},
						method : "POST",
						async:false,
						waitMsg : commonality_waitMsg,
						success : function(response) {
							if (response.responseText != null && response.responseText != '') {
								var result = Ext.util.JSON.decode(response.responseText);
								if (result.success == true || result.success == 'true') {
									alert(commonality_messageSaveMsg);
								} else {
									alert(commonality_saveMsg_fail);
								}
							} else {
								alert(commonality_messageSaveMsg);
							}
							waitMsg.hide();
						},
						failure : function(response) {
							waitMsg.hide();
							alert(commonality_cautionMsg);
						}
					});
					},1000);

			}
			
			Ext.QuickTips.init();
			
			//
			treePanel.on('checkchange', function(node, checked) {
				if (node.hasChildNodes()) {
					node.eachChild(function(child) {
								child.ui.toggleCheck(checked);
								child.attributes.checked = checked;
								child.fireEvent('checkChildren', child, checked);//递归调用   
							});
				}
			});
			   
			treePanel.on('checkChildren', function(node, checked) {
						if (node.hasChildNodes()) {
							node.eachChild(function(child) {
										child.ui.toggleCheck(checked);
										child.attributes.checked = checked;
										child.fireEvent('checkChildren', child, checked);//递归调用   
									});
						}
					});

			//定义树的右键菜单
			var treeMenu = new Ext.menu.Menu({
						items : [{
									text : '选中全部下级',
									handler : selectAllChildrenT
								}, {
									text : '取消全部下级',
									handler : selectAllChildrenF
								}]
					});

			//绑定Tree和右键菜单
			treePanel.on('contextmenu', treeContextHandler);

			function treeContextHandler(node, event) {
				event.preventDefault();
				node.select();
				treeMenu.show(node.ui.getAnchor());
			}

			function selectAllChildrenT() {
				var selectedNode = treePanel.getSelectionModel().getSelectedNode();
				autoCheck = true;
				if (selectedNode.id != "0") {
					selectedNode.ui.toggleCheck(true);
					selectedNode.attributes.checked = true;
				}
				selectedNode.fireEvent('checkChildren', selectedNode, true);
				autoCheck = false;
			}

			function selectAllChildrenF() {
				var selectedNode = treePanel.getSelectionModel().getSelectedNode();
				autoCheck = true;
				if (selectedNode.id != "0") {
					selectedNode.ui.toggleCheck(false);
					selectedNode.attributes.checked = false;
				}
				selectedNode.fireEvent('checkChildren', selectedNode, false);
				autoCheck = false;
			}
			
//----------------------------window--------------------------------------						
			var window = new Ext.Window({
				layout : 'border',
				border : false,
				resizable : false,
				closable : false,
				maximized : true,
				title : returnPageTitle('用户权限分配', 'userAuthority'),
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
					width : 500,
					items : [ grid ]
				}, {
					region : 'center',
					layout : 'fit',
					split : true,
					items : [ treePanel ]
				} ]
			});
			window.show();
			rootNode.expand();
			//urGrid.getEl().select('div.x-grid3-hd-checker').hide();
		});