/**
 * 此页面用来编辑专业室，并且分配专业室权限。
 * @authour samual
 * createDate 2014-11-03
 * 
 */
Ext.namespace('professionAndAuth');
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
			var urlPrefix=contextPath+"/userManage/authority/";
			var loadAllProfessionUrl="jsonLoadAllProfession.do";
			var loadTreeForSelectsByProfessionIdUrl = "loadTreeForSelectsByProfessionId.do";
			var updateTreeForSelectsByProfessionIdUrl = "updateTreeForSelectsByProfessionId.do";
			var getTreeIsCheckBeforeLoadUrl = "getTreeIsCheckBeforeLoad.do";
			
			var rendToDivName="professionGrid";
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
			
			var professionList = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
			
			var cm = new Ext.grid.ColumnModel({
				defaults : {
					ortable : true
				},
			columns : [{
					header : 'ID',
					dataIndex : 'professionId',
					width : 50,
					hidden : true,
					resizable : false,
					fixed : true,
					sortable : false
				},{
					header : '专业室编号',
					dataIndex : 'professionCode',
					width : 100,
					sortable : true
				}, {
					header : '专业室名称',
					dataIndex : 'professionName',
					width : 150,
					sortable : true
				}]
			});

			var store = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : urlPrefix+loadAllProfessionUrl
				}),
				reader : new Ext.data.JsonReader( {
					root : 'comProfession',
					fields : [ {
						name : 'professionId',
						type : 'string'
					}, {
						name : 'professionCode',
						type : 'string'
					}, {
						name : 'professionName',
						type : 'string'
					}]
				}),
				sortInfo : {
					field : 'professionCode',
					direction : 'ASC'
				}
			});

			// create the editor grid
			var grid = new Ext.grid.EditorGridPanel( {
				store : store,
				cm : cm,
				sm : professionList,
				title:'专业室信息',
				//header:false,
				id:'grid',
				renderTo : rendToDivName,
				split:true,
				//collapsible : true,
				width : gridWidth,
				height : gridHeight,
				//title : gridTitle,
				stripeRows : true,
				frame : true,
				//clicksToEdit : 1,
				bbar: new Ext.PagingToolbar({ 
					pageSize: 13, 
					store: store, 
					displayInfo: true, 
					displayMsg: commonality_turnPage, 
					emptyMsg: commonality_noRecords }),
					tbar : new Ext.Toolbar({
							items : ['编号：',
							{
								xtype : 'textfield',
								id : 'professionCode',
								width : 80
							},
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							'名称：',
							{
								xtype : 'textfield',
								id : 'keyword',
								width : 80
							},
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							{
								text : commonality_search,
								iconCls : "icon_gif_search",
								id : 'sericon',
								handler : function() {
									if (currentRecord != undefined || currentRecord != null) {
										currentRecord = null;
									}
									store.on("beforeload", function() {
												store.baseParams = {
													start : 0,
													limit : 20,
													professionCode : Ext.getCmp('professionCode').getValue(),
													keyword : Ext.getCmp('keyword').getValue()//,
													//userCode : Ext.get('userCode').dom.value,
													//keyword : Ext.get('keyword').dom.value
												};
											});
									store.load();
									currentRowIndex = -1;
									currentRecord = null;
									treePanel.root.reload();
								}
							}, '-',
							new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"),
							{
								text : commonality_reset, // 重置
								xtype : "button",
								iconCls : "icon_gif_reset",
								handler : function() {
								//	Ext.getCmp('model').clearValue();
									Ext.getCmp('professionCode').setValue('') ;
									Ext.getCmp('keyword').setValue('') ;
								}
							}]
			})
			} // grid function over
			); // grid over
			grid.render();	
			grid.addListener("cellmousedown", cellclick);
			store.load();

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
						url : urlPrefix + loadTreeForSelectsByProfessionIdUrl
					});

			loader.on('beforeload', beforeLoadEvent);

			function beforeLoadEvent(loader, node) {
				if (!node.isLeaf()) {
					loader.baseParams['professionId'] = currentRecord==null?"":currentRecord.get('professionId');// 专业组id
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
			
			loader.on("load", function() {
						setTimeout("waitMsgHide()", 500);
					});
			

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
			
			var treePanel = new Ext.tree.TreePanel({
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
									if(currentRecord == null || currentRecord.get("professionId") == null || currentRecord.get("professionId") == ''){
										alert("请选择专业组!");
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
					url : urlPrefix + getTreeIsCheckBeforeLoadUrl,
					params : {
						analysisType : analysisTypeCombo.getValue(),
						professionId : currentRecord.get("professionId")
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
				waitMsg = new Ext.LoadMask(Ext.getCmp('cnm').body, {
					msg : commonality_waitMsg,
					// 完成后移除
					removeMask : true
				});
				waitMsg.show();
				var checkedNodes = Ext.getCmp('treePanel').getChecked();
				var s = [];
				var noHaschirld=[];//用来存储还没有渲染子节点且选中的节点
				for (var i = 0; i < checkedNodes.length; i++) {
					//alert(checkedNodes[i].firstChild==null);
					if(checkedNodes[i].firstChild==null){//没有展开过该节点下的子节点，即该节点下的子节点还没动态生成
						noHaschirld.push(checkedNodes[i].id);
					}else{
						s.push(checkedNodes[i].id);
					}
					
				}
				setTimeout(function () {
					Ext.Ajax.request({
						url : urlPrefix + updateTreeForSelectsByProfessionIdUrl,
						params : {
							analysisType : analysisTypeCombo.getValue(),
							professionId : currentRecord.get("professionId"),
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
			
		    var win = new Ext.Window({
				title : returnPageTitle('专业室权限分配', 'professionAuthority'),
		        layout: 'border',
		        border : false,
		        closable:false,
		        resizable:false,
		        maximized : true,
		        bodyStyle:'padding:0px;',
		        plain:true,
		        buttonAlign:'center',
		        id : 'cnm',
		        items: [{
	      	      		 region:'center',
	      	      		 layout:'border',
	      	      		 split:true,      	      		 
	      	      		 items:[{
	          	      		 region:'west',
	          	      		 layout:'fit',
	          	      		 split:true,    
							 width : 500,  	      		 
	          	      		 items:[grid]
	      	      			}, {
								region : 'center',
								layout : 'fit',
								split : true,
								items : [treePanel]
							}]
	  	      			}]
		    });
		    win.show(); 
			rootNode.expand();
	});

/* 去前后空格 */
function LTrim(str) {
	// return str.replace(/^\s*/g,"");
	return str.replace(/(^\s*)|(\s*$)/g, "");
}