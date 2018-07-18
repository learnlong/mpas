Ext.namespace('ext_CustomS4Matrix');
var ROOTMMA = ''; // 根节点算法
/**
 * S45算法下拉框数据
 */
var s45Algorithm = new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [[MAX, "最大值"], [MIN, "最小值"], [SUM, "求和"],
					[MULT, "乘积"], [AVG, "平均值"]]
		});
var s45ItemAlgorithm = new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [[MAX, "最大值"], [MIN, "最小值"], [SUM, "求和"],[AVG, "平均值"]]
		});
Ext.onReady(function() {
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();
	var s45BodyWidth = document.body.clientWidth;		//当前body 容器的宽度
	var s45BodyHeight = document.documentElement.clientHeight;		//当前body 容器的高度
	var allTreePanel = new Ext.Panel({
				// title : returnPageTitle(s45Paneltitle,'s45Matrix'),
				region : 'center',
				layout : 'border',
				width : 'auto',
				items : [{
							region : 'west',
							collapsible : true,
							minSize : 180,
							width : s45BodyWidth * (1/6),
							split : true, // 支持拖拽
							layout : 'fit',
							items : [s4a()]

						}, {
							region : 'center',
							border : false,
							layout : 'border',
							items : [{
										region : 'west',
										collapsible : true,
										width : s45BodyWidth * (1/6),
										minSize : 180,
										split : true, // 支持拖拽
										layout : 'fit',
										items : [s4b()]

									}, {
										region : 'center',
										border : false,
										split : true, // 支持拖拽
										border : false,
										layout : 'border',
										items : [{
													region : 'west',
													collapsible : true,
													width : s45BodyWidth * (1/6),
													minSize : 180,
													split : true, // 支持拖拽
													layout : 'fit',
													items : [s5a()]

												}, {
													region : 'center',
													border : false,
													layout : 'border',
													items : [{
																region : 'west',
																collapsible : true,
																width : s45BodyWidth * (1/6),
																minSize : 180,
																split : true, // 支持拖拽
																layout : 'fit',
																items : [s5b()]

															},{
																region : 'center',
																border : false,
																layout : 'border',
																items : [{
																			region : 'west',
																			collapsible : true,
																			width : s45BodyWidth * (1/6),
																			minSize : 180,
																			split : true, // 支持拖拽
																			layout : 'fit',
																			items : [sya()]

																		},{
																			region : 'center',
																			border : false,
																			layout : 'border',
																			items : [{
																						region : 'center',
																						collapsible : true,
																						width : s45BodyWidth * (1/6),
																						minSize : 180,
																						split : true, // 支持拖拽
																						layout : 'fit',
																						items : [syb()]

																					}]

																		}]

															}]

												}]
									}]
						}]
			});
	var window = new Ext.Window({ // 生成一个 ExtJS 视窗 组件对象
		renderTo : Ext.getBody(), // 呈现在 Html Body标签中
		layout : 'border', // 使用边界布局
		border : false,
		resizable : true,
		closable : false,
		maximized : true,
		plain : false,
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		title : returnPageTitle('自定义结构参数', 's45Matrix'),
		collapsible : true,
		items : [allTreePanel]
	});
	window.show();

	/** ********************************************S4A自定义矩阵树******************************************** */
	function s4a() {
		// 加载Ext树上面的节点数据
		var initTreeStore = contextPath
				+ "/paramDefineManage/defineStructureParameter/loadCustom.do?stepFlg=S4A";
		// 检测自定义矩阵树是否完整
		var checkTreeIsFull = contextPath
				+ "/paramDefineManage/defineStructureParameter/checkFull.do?stepFlg=S4A";
		// 保存S5B
		var saveS4A = contextPath
				+ "/paramDefineManage/defineStructureParameter/saveAll.do?stepFlg=S4A";
		// 保存/更新项目
		var saveOrUpdateItem = contextPath
				+ "/paramDefineManage/defineStructureParameter/saveS45Item.do?stepFlg=S4A";
		// 保存/更新级别
		var saveOrUpdateLevel = contextPath
				+ "/paramDefineManage/defineStructureParameter/saveS45Level.do?stepFlg=S4A";
		// 删除节点
		var deleteNode = contextPath
				+ "/paramDefineManage/defineStructureParameter/deleteNode.do?stepFlg=S4A";
		// 定义前两级的树节点（固定的）
		var root = new Ext.tree.AsyncTreeNode({
					id : "0",
					text : "金属ED参数"// 节点名称
				});
		var treeLoader = new Ext.tree.TreeLoader({
					baseAttrs : {},
					dataUrl : initTreeStore
				});
		var s4aMenuTree = new Ext.tree.TreePanel({
					loader : treeLoader,
					root : root,
					autoScroll : false,
					animate : false,
					useArrows : false,
					border : false,
					autoScroll : true,
					region : 'center',
					tbar : ['金属ED参数' + commonality_mustInput, {
								name : 's4a_algorithm',
								id : 's4a_algorithm',
								valueField : "retrunValue",
								displayField : "displayText",
								mode : 'local',
								triggerAction : 'all',
								anchor : '95%',
								xtype : "combo",
								editable : false,
								width : 90,
								store : s45Algorithm
							}, "->", {
								id : 'treeConfirm',
								text : "确认",
								xtype : "button",
								iconCls : "icon_gif_accept",
								handler : function() {
									saveS4aAllInit();
								}
							}]
				});

		// menuTree.getRootNode().reload();
		s4aMenuTree.expandAll();
		// menuTree.collapse(true);
		// 创建右键弹出的菜单
		var s4aContextMenu = new Ext.menu.Menu({
					id : 's4aMenuTreeContextMenu',
					items : [{
								text : "新增项目",
								id : 's4aAddItem',
								iconCls : 'page_addIcon',
								handler : function() {
									s4aAddItemInit();
								}

							}, {
								text : '新增级别',
								iconCls : 'page_addIcon',
								id : 's4aAddLevel',
								handler : function() {
									addLevelInit();
								}
							}, {
								text : '编辑项目',
								iconCls : 'page_edit_1Icon',
								handler : function() {
									s4aEditItemInit();
								}
							}, {
								text : '编辑级别',
								iconCls : 'page_edit_1Icon',
								handler : function() {
									editLevelInit();
								}
							}, {
								text : '删除',
								iconCls : 'page_delIcon',
								handler : function() {
									deleteInit();
								}
							}, {
								text : '刷新',
								iconCls : 'page_refreshIcon',
								handler : function() {
									var selectModel = s4aMenuTree
											.getSelectionModel();
									var selectNode = selectModel
											.getSelectedNode();
									if (selectNode.attributes.leaf) {
										selectNode.parentNode.reload();
									} else {
										selectNode.reload();
									}
								}
							}]
				});
		// 给节点添加点击右键的事件
		s4aMenuTree.on('contextmenu', function(node, e) {
					e.preventDefault();
					menuid = node.attributes.id;
					menuname = node.attributes.text;
					node.select();
					var arr = e.getXY();
					arr[1] = arr[1] - 50;
					//原先的3级菜单显示控制
					
					if (node.getDepth() == 0) { // 根节点右键
						s4aContextMenu.items.item(0).show();//新增项目
						s4aContextMenu.items.item(1).hide();
						s4aContextMenu.items.item(2).hide();
						s4aContextMenu.items.item(3).hide();
						s4aContextMenu.items.item(4).hide();
						s4aContextMenu.items.item(5).show();//刷新
						s4aContextMenu.showAt(e.getXY());
					} else if (node.getDepth() == 1) { // 第一级结点
						s4aContextMenu.items.item(0).show();//新增项目
						s4aContextMenu.items.item(1).hide();
						s4aContextMenu.items.item(2).show();//编辑项目
						s4aContextMenu.items.item(3).hide();
						s4aContextMenu.items.item(4).show();//删除
						s4aContextMenu.items.item(5).show();//刷新
					} else if (node.getDepth() == 2) { // 第二级结点
						s4aContextMenu.items.item(0).hide();
						s4aContextMenu.items.item(1).show();//新增级别
						s4aContextMenu.items.item(2).show();//编辑项目
						s4aContextMenu.items.item(3).hide();
						s4aContextMenu.items.item(4).show();//删除
						s4aContextMenu.items.item(5).show();//刷新
					} else if (node.getDepth() == 3) { // 第三级结点
						s4aContextMenu.items.item(0).hide();
						s4aContextMenu.items.item(1).hide();
						s4aContextMenu.items.item(2).hide();
						s4aContextMenu.items.item(3).show();//编辑级别
						s4aContextMenu.items.item(4).show();//删除
						s4aContextMenu.items.item(5).show();//刷新
					}
					
					
					
					
					
					s4aContextMenu.showAt(e.getXY());
				});
		// var node=;
		// 创建增加项目的面板
		function s4aAddItemFormPanel() {
			var s4aAddItemFormPanel = new Ext.form.FormPanel({
						id : 's4aAddItemFormPanel',
						name : 's4aAddItemFormPanel',
						defaultType : 'textfield',
						labelAlign : 'right',
						labelWidth : 100,
						frame : false,
						bodyStyle : "padding:10px 10px",
						// bodyStyle : 'padding:5 5 0',
						items : [{
									fieldLabel : '项目名'
											+ commonality_mustInput,
									name : 'itemName',
									allowBlank : false,
									maxLength : 50,
									maxLengthText : '输入信息超过规定长度',
									anchor : '95%'
								}, {
									fieldLabel : '算法' + commonality_mustInput,
									name : 'itemAlgorithm',
									id : 'itemAlgorithm',
									valueField : "retrunValue",
									displayField : "displayText",
									mode : 'local',
									triggerAction : 'all',
									hideLabel : algHidden,
									editable : false,
									allowBlank : algHidden ? true : false,
									forceSelection : true,
									hidden : algHidden,
									anchor : '91%',
									xtype : "combo",
									editable : false,
									store : s45ItemAlgorithm,
									listWidth : 245
								}, {
									name : 'itemFlg',
									allowBlank : true,
									hidden : true,
									anchor : '99%'
								}, {
									name : 'itemId',
									allowBlank : true,
									hidden : true,
									anchor : '99%'
								}, {
									name : 'itemSort',
									allowBlank : true,
									hidden : true,
									anchor : '99%'
								}]
					});
			return s4aAddItemFormPanel;
		}
		// 创建增加级别的面板
		function s4aAddLevelFormPanel() {
			var s4aAddLevelFormPanel = new Ext.form.FormPanel({
						id : 's4aAddLevelFormPanel',
						name : 's4aAddLevelFormPanel',
						// defaultType : 'textfield',
						labelAlign : 'right',
						labelWidth : 100,
						frame : false,
						bodyStyle : "padding:10px 10px",
						// bodyStyle : 'padding:5 5 0',
						items : [{
							border : false,
							items : [{
								columnWidth : .5, // 该列占用的宽度，标识为50％
								layout : 'form',
								border : false,
								items : [{ // 这里可以为多个Item，表现出来是该列的多行
									xtype : 'numberfield',
									fieldLabel : '级别值'
											+ commonality_mustInput,
									name : 'levelValue',
									emptyText : '最多允许输入两位数字',
									allowBlank : false,
									minValue : 0,
									minText : '级别值不能小于0',
									maxLength : 2,
									maxLengthText : '最多允许输入两位数字',
									anchor : '90%'
								}]
							}]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [{
								xtype : 'textfield',
								fieldLabel : '级别名'
										+ commonality_mustInput,
								allowBlank : false,
								maxLength : 9,
								maxLengthText : '输入信息超过规定长度',
								name : 'levelName',
								anchor : '90%'
							}]
						}, {
							xtype : "textfield",
							name : "itemId", // 上一级的结点的id（用于增加级别）
							hidden : true
						}, {
							xtype : "textfield",
							name : "levelId", // 该级别的id（用于编辑级别）
							hidden : true
						}]
					});
			return s4aAddLevelFormPanel;
		}
		// 创建项目的窗口
		function s4aAddItemWindowFunc() {
			s4aAddItemWindow = new Ext.Window({
						id : 's4aAddItemWindow',
						name : 's4aAddItemWindow',
						layout : 'fit',
						width : 420,
						height : 285,
						resizable : false,
						draggable : true,
						closeAction : 'close',
						title : '<span class="commoncss">' + '新增项目'
								+ '</span>',
						// iconCls : 'page_addIcon',
						modal : true,
						maximizable : false,
						buttonAlign : 'right',
						border : false,
						animCollapse : false,
						pageY : 20,
						pageX : document.body.clientWidth / 2 - 420 / 2,
						animateTarget : Ext.getBody(),
						constrain : true,
						items : [s4aAddItemFormPanel()],
						buttons : [{
									id : 's4aSaveItem',
									text : '保存',
									handler : function() {
										saveRecord("save");
									}
								}, {
									id : 's4aUpdateItem',
									text : '修改',
									handler : function() {
										saveRecord("update");
									}
								}, {
									text : '重置',
									id : 'btnReset',
									handler : function() {
										Ext.getCmp("s4aAddItemFormPanel")
												.getForm().getEl().dom.reset();
									}
								}, {
									text : '关闭',
									handler : function() {
										s4aAddItemWindow.close();
									}
								}]
					});
			return s4aAddItemWindow;
		}
		// 创建级别弹出的级别窗口
		function s4aAddLevelWindowFunc() {
			var s4aAddLevelWindow = new Ext.Window({
						name : 's4aAddLevelWindow',
						id : 's4aAddLevelWindow',
						layout : 'fit',
						width : 300,
						height : 200,
						resizable : false,
						draggable : true,
						closeAction : 'close',
						title : '<span class="commoncss">' + '新增级别'
								+ '</span>',
						// iconCls : 'page_addIcon',
						modal : true,
						maximizable : false,
						buttonAlign : 'right',
						border : false,
						animCollapse : false,
						pageY : 20,
						pageX : document.body.clientWidth / 2 - 300 / 2,
						animateTarget : Ext.getBody(),
						constrain : true,
						items : [s4aAddLevelFormPanel()],
						buttons : [{
									id : 's4aSaveLevel',
									pageX : 10,
									text : '保存',
									handler : function() {
										saveLevelRecord("save");
									}
								}, {
									id : 's4aUpdateLevel',
									pageX : 10,
									text : '修改',
									handler : function() {
										saveLevelRecord("update");
									}
								}, {
									text : '重置',
									id : 'btnReset',
									handler : function() {
										Ext.getCmp("s4aAddLevelFormPanel")
												.getForm().getEl().dom.reset();
									}
								}, {
									text : '关闭',
									handler : function() {
										s4aAddLevelWindow.close();
									}
								}]
					});
			return s4aAddLevelWindow;
		}

		// 定义保存S4a的初始化方法
		function saveS4aAllInit() {
			// 检测算法是否填写
			ROOTMMA = Ext.getCmp("s4a_algorithm").getValue();
			if (ROOTMMA == null || ROOTMMA == "") {
				alert('请填写金属ED的算法！');
				return;
			}
			var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				msg : '正在向服务器提交数据...',
				removeMask : true
					// 完成后移除
				});
			waitMsg.show();
			// 检测自定义矩阵树数据是否完整
			Ext.Ajax.request({
						url : checkTreeIsFull,
						method : "POST",
						success : function(response, action) {
							waitMsg.hide();
							var msg = Ext.util.JSON
									.decode(response.responseText);
							if (msg.success == false) {
								alert('您录入的数据不完整，请录入完整!');
								return;
							} else {
								saveS4aAll();
							}
						},
						failure : function(response, action) {
							waitMsg.hide();
							alert('检测数据完整性时，出现故障。请稍后再试！');
						}
					});
		}

		// 定义增加项目的初始化方法
		function s4aAddItemInit() {
			var node = s4aMenuTree.getSelectionModel().getSelectedNode();
			if (node.getDepth() != 0) { // 选中的0级节点
				algHidden = true;
			} else {
				algHidden = false;
			}
			s4aAddItemWindowFunc().show(); // 此方法放在前面不可后移
			Ext.getCmp("s4aSaveItem").show();
			Ext.getCmp("s4aUpdateItem").hide();
		}
		// 定义编辑项目的初始化方法
		function s4aEditItemInit() {
			var node = s4aMenuTree.getSelectionModel().getSelectedNode();
			if (node.getDepth() != 1) { // 选中的1级节点
				algHidden = true;
			} else {
				algHidden = false;
			}
			s4aAddItemWindowFunc().show();
			Ext.getCmp("s4aAddItemFormPanel").find("name", "itemName")[0]
					.setValue(node.attributes.itemName);
			Ext.getCmp("s4aAddItemFormPanel").find("name", "itemAlgorithm")[0]
					.setValue(node.attributes.itemAlgorithm);
			Ext.getCmp("s4aSaveItem").hide();
			// 将弹出的s4aAddItemWindow窗口的标题改成编辑项目
			Ext.getCmp("s4aAddItemWindow").setTitle("编辑项目");
			Ext.getCmp("s4aUpdateItem").show();

		}

		// 定义增加级别的初始化方法
		function addLevelInit() {
			var node = s4aMenuTree.getSelectionModel().getSelectedNode();
			s4aAddLevelWindowFunc().show(); // 此方法放在前面不可后移
			Ext.getCmp("s4aUpdateLevel").hide();
			Ext.getCmp("s4aSaveLevel").show();
		}
		// 定义编辑级别的初始化方法
		function editLevelInit() { // 编辑级别
			var node = s4aMenuTree.getSelectionModel().getSelectedNode();
			s4aAddLevelWindowFunc().show();
			var tempPanel = Ext.getCmp("s4aAddLevelFormPanel");
			tempPanel.find("name", "levelValue")[0]
					.setValue(node.attributes.sort);
			tempPanel.find("name", "levelName")[0]
					.setValue(node.attributes.levelName);
			
			tempPanel.find("name", "itemId")[0]
					.setValue(node.attributes.itemId);
			Ext.getCmp("s4aSaveLevel").hide();
			// 将弹出的s4aAddLevelWindow窗口的标题改成编辑级别
			Ext.getCmp("s4aAddLevelWindow").setTitle("编辑级别");

			Ext.getCmp("s4aUpdateLevel").show();
		}
		// 定义删除的初始化方法
		function deleteInit() {
			var node = s4aMenuTree.getSelectionModel().getSelectedNode();
			// 在action判断itemId是否为空不为空则删除项目为空则删除等级
			var itemId = "";
			var levelId = "";
			node.expand();
			Ext.Msg.confirm('提示', '确认要删除吗？',
					function(btn) {
						if (btn == "yes") {
							if (node.getDepth() == 1 || node.getDepth() == 2) {
								itemId = node.id;
							} else {
								levelId = node.id;
							}
							deleteS4aNode(levelId, itemId);
						}
					});

		}
		// 定义保存S4a的方法
		function saveS4aAll() {
			var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				msg : '正在向服务器提交数据...',
				removeMask : true
					// 完成后移除
				});
			waitMsg.show();
			Ext.Ajax.request({
						url : saveS4A,
						method : "POST",
						params : {
							ROOTMMA : ROOTMMA
						},
						success : function(response, action) {
							waitMsg.hide();
							var msg = Ext.util.JSON
									.decode(response.responseText);
							if (msg.checkS45 == false) {
								alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
							} else {
								alert('保存成功！');
								var node = s4aMenuTree.getRootNode();
							}
						},
						failure : function(response, action) {
							waitMsg.hide();
							alert('保存失败！');
						}
					});
		}

		// 定义保存项目的方法
		function saveRecord(flag) {
			var form = Ext.getCmp("s4aAddItemFormPanel").getForm();
			if (!form.isValid()) {
				if (Ext.getCmp("s4aAddItemFormPanel").find("name",
						"itemAlgorithm")[0].isVisible()) { // 算法可见
					var algValue = Ext.getCmp("s4aAddItemFormPanel").find(
							"name", "itemAlgorithm")[0].getValue();
					if (algValue == "" || algValue == null) {
						alert('表单输入有问题，请检查！');
						return;
					}
				}
				alert('表单输入有问题，请检查！');
				return;
			}
			var node = s4aMenuTree.getSelectionModel().getSelectedNode();
			if (flag == "save") {
				if (node.lastChild != null) {
					var itemSort = node.lastChild.attributes.itemSort;
				} else {
					var itemSort = 1;
				}
				Ext.getCmp("s4aAddItemFormPanel").find("name", "itemFlg")[0]
						.setValue(node.id);
				Ext.getCmp("s4aAddItemFormPanel").find("name", "itemSort")[0]
						.setValue(itemSort + 1);
			} else if (flag == 'update') {
				Ext.getCmp("s4aAddItemFormPanel").find("name", "itemId")[0]
						.setValue(node.id);
			}
			form.submit({
						url : saveOrUpdateItem,
						waitTitle : '提示',
						method : 'post',
						waitMsg : '正在向服务器提交数据...',
						params : {
							itemAlg : Ext.getCmp('itemAlgorithm').getValue()
						},
						success : function(form, action) {
							var msg = Ext.util.JSON
									.decode(action.response.responseText);
							if (msg.success == "count") {
								alert('一级项目超过了数据库承载的数量或者一级二级项目数量总和超过了数据库承载的数量');
								Ext.getCmp("s4aAddItemWindow").close();
								return;
							}
							if (msg.checkS45 == false) {
								alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
								Ext.getCmp("s4aAddItemWindow").close();
							} else {
								alert('保存成功！');
								Ext.getCmp("s4aAddItemWindow").close();
								if (flag == 'update') { // 更新刷新选中结点的父节点
									node.parentNode.reload();
								} else {
									node.reload(); // 添加刷新选中的该结点
								}
							}
						},
						failure : function(form, action) {
							alert('保存失败！');
						}
					});

		}
		// 定义保存/更新级别的方法
		function saveLevelRecord(flag) {
			var form = Ext.getCmp("s4aAddLevelFormPanel").getForm();
			if (!form.isValid()) {
				alert('表单输入有问题，请检查！');
				return;
			}
			var node = s4aMenuTree.getSelectionModel().getSelectedNode();
			if (flag == 'save') {
				Ext.getCmp("s4aAddLevelFormPanel").find("name", "itemId")[0]
						.setValue(node.id);
			} else if (flag == 'update') {
				Ext.getCmp("s4aAddLevelFormPanel").find("name", "levelId")[0]
						.setValue(node.id);
				Ext.getCmp("s4aAddLevelFormPanel").find("name", "itemId")[0]
						.setValue(node.parentNode.id);
			}
			form.submit({
						url : saveOrUpdateLevel,
						method : 'post',
						waitTitle : '提示',
						waitMsg : '正在向服务器提交数据...',
						success : function(form, action) {
						    //alert(action.response.responseText);
							var msg = Ext.util.JSON
									.decode(action.response.responseText);
							
							if (msg.checkS45 == false) {
								alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
								Ext.getCmp("s4aAddLevelWindow").close();
							} else {
								if (msg.checkLevel == false) {
									alert('数据库已经存在该级别值的级别信息');
									return;
								}
								if (msg.checkLevelNum == false){
								    alert('级别不能超过5级!');
								    Ext.getCmp("s4aAddLevelWindow").close();
								    return ;
								}
								alert('保存成功！');
								Ext.getCmp("s4aAddLevelWindow").close();
								if (flag == 'update') {// 是否是"更新"操作。注：更新操作完成刷新其父节点，
									node.parentNode.reload();
								} else if (flag == 'save') {
									node.reload();
								}
							}
						},
						failure : function(form, action) {
							alert('保存失败！');
						}
					});
		}
		// 定义一个删除的方法
		function deleteS4aNode(levelId, itemId) {
			var node = s4aMenuTree.getSelectionModel().getSelectedNode();
			var nodeP = node.parentNode;
			if (node.hasChildNodes()) {
				alert('请先删除子节点！');
				return;
			}
			var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				msg : '正在向服务器提交数据...',
				removeMask : true
					// 完成后移除
				});
			waitMsg.show();
			Ext.Ajax.request({
						url : deleteNode,
						method : "POST",
						params : {
							levelId : levelId,
							itemId : itemId
						},
						success : function(response, action) {
							waitMsg.hide();
							var msg = Ext.util.JSON
									.decode(response.responseText);
							if (msg.checkS45 == false) {
								alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
							} else {
								alert('删除成功！');
								if (node.getDepth() == 1) {
									root.reload(); // 刷新根节点
								} else {
									nodeP.parentNode.reload(); // 不需要刷新所有的节点只需要刷新上上级节点
								}
							}
						},
						failure : function(response, action) {
							waitMsg.hide();
							alert('数据删除失败 !');
						}
					});
		}
		return s4aMenuTree;
	}
	/** *******************************显示S4A的算法*************************************************** */
	Ext.getCmp("s4a_algorithm").setValue(S4AROOTMMA, true);

	/** *******************************显示S4B的算法*************************************************** */
	Ext.getCmp("s4b_algorithm").setValue(S4BROOTMMA, true);

	/** *******************************显示S5A的算法*************************************************** */
	Ext.getCmp("s5a_algorithm").setValue(S5AROOTMMA, true);

	/** *******************************显示S5B的算法*************************************************** */
	Ext.getCmp("s5b_algorithm").setValue(S5BROOTMMA, true);
	
	/** *******************************显示SYA的算法(金属应力)******************************************** */
	Ext.getCmp("sya_algorithm").setValue(SYAROOTMMA, true);
	
	/** *******************************显示SYA的算法(非金属应力)******************************************** */
	Ext.getCmp("syb_algorithm").setValue(SYBROOTMMA, true);

});
