Ext.namespace('ext_lhirfParam');
var ROOTMMA = ''; // 根节点算法
var algHidden = false; // 是否隐藏项目中的算法
/**
 * Lh4算法下拉框数据
 */
var itemAlgorithmStore = new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [[MAX, '最大值'], [MIN, '最小值']]
		})

// 加载Ext树上面的节点数据
var initTreeStore = contextPath
		+ "/paramDefineManage/lhirfParam/loadCustom.do?stemFlg=LH4";
// 检测自定义矩阵树是否完整
var checkTreeIsFull = contextPath
		+ "/paramDefineManage/lhirfParam/checkFull.do?stemFlg=LH4";
// 保存S5B
var saveLH4 = contextPath
		+ "/paramDefineManage/lhirfParam/saveAll.do?stemFlg=LH4";
// 保存/更新项目
var saveOrUpdateItem = contextPath
		+ "/paramDefineManage/lhirfParam/saveS45Item.do?stemFlg=LH4";
// 保存/更新级别
var saveOrUpdateLevel = contextPath
		+ "/paramDefineManage/lhirfParam/saveS45Level.do?stemFlg=LH4";
// 删除节点
var deleteNode = contextPath
		+ "/paramDefineManage/lhirfParam/deleteNode.do?stemFlg=LH4";
var h=window.innerHeight
|| document.documentElement.clientHeight
|| document.body.clientHeight;
Ext.onReady(function() {
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();
	var allTreePanel = new Ext.Panel({
				region : 'center',
				layout : 'border',
				width : 'auto',
				items : [{
							region : 'west',
							collapsible : true,
							width : 300,
							split : true,
							layout : 'fit',
							items : [lh4()]
						}, new Ext.Panel({
								region : 'center',
								layout : 'border',
								width : 'auto',
								items : [{
										region : 'center',
//										collapsible : true,
//										split : true,
										layout : 'fit',
										items : [lh5()]
									},{
										region : 'south',
//										collapsible : true,
										height : h/4,
										split : true,
										layout : 'fit',
										items : [displayForm]
									}]
							})
						]
			})
	
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
		title : returnPageTitle('自定义LHIRF分析参数', 'lhirfParam'),
		collapsible : true,
		items : [allTreePanel]
	});
	window.show();

	/** ********************************************LH4自定义矩阵树******************************************** */
	function lh4() {
		// 定义前两级的树节点（固定的）
		var root = new Ext.tree.AsyncTreeNode({
					id : "lh4Root",
					text : "LH4",// 节点名称
					expanded : true,// 展开
					leaf : false,// 是否为叶子节点
					children : [{
								id : 'ED',
								text : '环境损伤（ED）',
								sort : ''
							}, {
								id : 'AD',
								text : '偶然损伤（AD）'
							}]
				});

		/**
		 * LH4算法下拉框store
		 */
		var lh4Algorithm = new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
					data : [[MAX, '最大值'], [MIN, '最小值']]
				});
		var lh4ItemAlgorithm = new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
					data : [[MAX, '最大值'], [MIN, '最小值']]
				});
		// 后两级节点从后台抓取数据
		var lh4MenuTree = new Ext.tree.TreePanel({
					loader : new Ext.tree.TreeLoader({
								baseAttrs : {},
								dataUrl : initTreeStore
							}),
					root : root,
					autoScroll : true,
					animate : false,
					useArrows : false,
					border : false,
					title : '自定义矩阵LHIRF',
					tbar : ["LHIRF" + commonality_mustInput, {
								name : 'lh4_algorithm',
								id : 'lh4_algorithm',
								valueField : "retrunValue",
								displayField : "displayText",
								mode : 'local',
								triggerAction : 'all',
								anchor : '95%',
								xtype : "combo",
								editable : false,
								width : 90,
								store : lh4Algorithm
							}, "->", {
								id : 'treeConfirm',
								text : commonality_affirm,
								xtype : "button",
								disabled : lh4Isfull,
								iconCls : "icon_gif_accept",
								handler : function() {
									saveLh4AllInit();
								}
							}],
					listeners : {
						'render' : function() {
							var tbar = new Ext.Toolbar({
										items : [
												"ED" + commonality_mustInput,
												{
													name : 'lh4ed_algorithm',
													id : 'lh4ed_algorithm',
													valueField : "retrunValue",
													displayField : "displayText",
													mode : 'local',
													triggerAction : 'all',
													anchor : '95%',
													xtype : "combo",
													editable : false,
													width : 75,
													store : lh4ItemAlgorithm
												},
												"AD" + commonality_mustInput, {
													name : 'lh4ad_algorithm',
													id : 'lh4ad_algorithm',
													valueField : "retrunValue",
													displayField : "displayText",
													mode : 'local',
													triggerAction : 'all',
													anchor : '95%',
													xtype : "combo",
													editable : false,
													width : 75,
													store : lh4ItemAlgorithm
												}]
									});
							tbar.render(lh4MenuTree.tbar);
						}
					}
				});
		// menuTree.getRootNode().reload();
		// menuTree.expandAll();
		// menuTree.collapse(true);
		// 创建右键弹出的菜单
		var lh4ContextMenu = new Ext.menu.Menu({
					id : 'lh4MenuTreeContextMenu',
					items : [{
								text : '新增项目',
								id : 'lh4AddItem',
								iconCls : 'page_addIcon',
								handler : function() {
									lh4AddItemInit();
								}

							}, {
								text : '新增级别',
								iconCls : 'page_addIcon',
								id : 'lh4AddLevel',
								handler : function() {
									addLevelInit();
								}
							}, {
								text : '编辑项目',
								iconCls : 'page_edit_1Icon',
								handler : function() {
									lh4EditItemInit();
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
									var selectModel = lh4MenuTree
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
		lh4MenuTree.on('contextmenu', function(node, e) {
					e.preventDefault();
					menuid = node.attributes.id;
					menuname = node.attributes.text;
					node.select();
					var arr = e.getXY();
					arr[1] = arr[1] - 50;
					if (node.getDepth() == 0) { // 根节点右键
						lh4ContextMenu.items.item(0).hide();
						lh4ContextMenu.items.item(1).hide();
						lh4ContextMenu.items.item(2).hide();
						lh4ContextMenu.items.item(3).hide();
						lh4ContextMenu.items.item(4).hide();
						lh4ContextMenu.items.item(5).show();
					} else if (node.getDepth() == 1) { // 第一级结点
						lh4ContextMenu.items.item(0).show();
						lh4ContextMenu.items.item(1).hide();
						lh4ContextMenu.items.item(2).hide();
						lh4ContextMenu.items.item(3).hide();
						lh4ContextMenu.items.item(4).hide();
						lh4ContextMenu.items.item(5).show();
					} else if (node.getDepth() == 2) { // 第二级结点
						lh4ContextMenu.items.item(0).hide();
						lh4ContextMenu.items.item(1).show();
						lh4ContextMenu.items.item(2).show();
						lh4ContextMenu.items.item(3).hide();
						lh4ContextMenu.items.item(4).show();
						lh4ContextMenu.items.item(5).show();
					} else if (node.getDepth() == 3) { // 第三级结点
						lh4ContextMenu.items.item(0).hide();
						lh4ContextMenu.items.item(1).hide();
						lh4ContextMenu.items.item(2).hide();
						lh4ContextMenu.items.item(3).show();
						lh4ContextMenu.items.item(4).show();
						lh4ContextMenu.items.item(5).show();
					}
					if (!lh4Isfull) {
						lh4ContextMenu.showAt(e.getXY());
					}
				});
		// var node=;
		// 创建增加项目的面板
		function lh4AddItemFormPanel() {
			var lh4AddItemFormPanel = new Ext.form.FormPanel({
						id : 'lh4AddItemFormPanel',
						name : 'lh4AddItemFormPanel',
						defaultType : 'textfield',
						labelAlign : 'right',
						labelWidth : 100,
						frame : false,
						bodyStyle : "padding:10px 10px",
						// bodyStyle : 'padding:5 5 0',
						items : [{
									fieldLabel : '项目名'
											+ commonality_mustInput,
									name : 'itemNameCn',
									allowBlank : false,
									maxLength : 50,
									///maxLengthText : commonality_MaxLengthText,
									anchor : '95%'
								},  {
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
			return lh4AddItemFormPanel;
		}
		// 创建增加级别的面板
		function lh4AddLevelFormPanel() {
			var lh4AddLevelFormPanel = new Ext.form.FormPanel({
						id : 'lh4AddLevelFormPanel',
						name : 'lh4AddLevelFormPanel',
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
									//maxLengthText : levelMaxLengthText,
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
								//maxLengthText : commonality_MaxLengthText,
								name : 'levelNameCn',
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
			return lh4AddLevelFormPanel;
		}
		// 创建项目的窗口
		function lh4AddItemWindowFunc() {
			lh4AddItemWindow = new Ext.Window({
						id : 'lh4AddItemWindow',
						name : 'lh4AddItemWindow',
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
						items : [lh4AddItemFormPanel()],
						buttons : [{
									id : 'lh4SaveItem',
									text : commonality_save,
									handler : function() {
										saveRecord("save");
									}
								}, {
									id : 'lh4UpdateItem',
									text : commonality_modify,
									handler : function() {
										saveRecord("update");
									}
								}, {
									text : commonality_reset,
									id : 'btnReset',
									handler : function() {
										Ext.getCmp("lh4AddItemFormPanel")
												.getForm().getEl().dom.reset();
									}
								}, {
									text : commonality_close,
									handler : function() {
										lh4AddItemWindow.close();
									}
								}]
					});
			return lh4AddItemWindow;
		}
		// 创建级别弹出的级别窗口
		function lh4AddLevelWindowFunc() {
			var lh4AddLevelWindow = new Ext.Window({
						name : 'lh4AddLevelWindow',
						id : 'lh4AddLevelWindow',
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
						items : [lh4AddLevelFormPanel()],
						buttons : [{
									id : 'lh4SaveLevel',
									pageX : 10,
									text : commonality_save,
									handler : function() {
										saveLevelRecord("save");
									}
								}, {
									id : 'lh4UpdateLevel',
									pageX : 10,
									text : commonality_modify,
									handler : function() {
										saveLevelRecord("update");
									}
								}, {
									text : commonality_reset,
									id : 'btnReset',
									handler : function() {
										Ext.getCmp("lh4AddLevelFormPanel")
												.getForm().getEl().dom.reset();
									}
								}, {
									text : commonality_close,
									handler : function() {
										lh4AddLevelWindow.close();
									}
								}]
					});
			return lh4AddLevelWindow;
		}

		// 定义增加项目的初始化方法
		function lh4AddItemInit() {
			var node = lh4MenuTree.getSelectionModel().getSelectedNode();
			lh4AddItemWindowFunc().show(); // 此方法放在前面不可后移
			Ext.getCmp("lh4SaveItem").show();
			Ext.getCmp("lh4UpdateItem").hide();
		}
		// 定义编辑项目的初始化方法
		function lh4EditItemInit() {
			var node = lh4MenuTree.getSelectionModel().getSelectedNode();
			lh4AddItemWindowFunc().show();
			Ext.getCmp("lh4AddItemFormPanel").find("name", "itemNameCn")[0]
					.setValue(node.attributes.itemNameCn);
		
			Ext.getCmp("lh4SaveItem").hide();
			// 将弹出的lh4AddItemWindow窗口的标题改成编辑项目
			Ext.getCmp("lh4AddItemWindow").setTitle(editItem);
			Ext.getCmp("lh4UpdateItem").show();

		}

		// 定义增加级别的初始化方法
		function addLevelInit() {
			var node = lh4MenuTree.getSelectionModel().getSelectedNode();
			lh4AddLevelWindowFunc().show(); // 此方法放在前面不可后移
			Ext.getCmp("lh4UpdateLevel").hide();
			Ext.getCmp("lh4SaveLevel").show();
		}
		// 定义编辑级别的初始化方法
		function editLevelInit() { // 编辑级别
			var node = lh4MenuTree.getSelectionModel().getSelectedNode();
			lh4AddLevelWindowFunc().show();
			var tempPanel = Ext.getCmp("lh4AddLevelFormPanel");
			tempPanel.find("name", "levelValue")[0]
					.setValue(node.attributes.sort);
			tempPanel.find("name", "levelNameCn")[0]
					.setValue(node.attributes.levelNameCn);
			
			tempPanel.find("name", "itemId")[0]
					.setValue(node.attributes.itemId);
			Ext.getCmp("lh4SaveLevel").hide();
			// 将弹出的lh4AddLevelWindow窗口的标题改成编辑级别
			Ext.getCmp("lh4AddLevelWindow").setTitle(editLevel);

			Ext.getCmp("lh4UpdateLevel").show();
		}
		// 定义删除的初始化方法
		function deleteInit() {
			var node = lh4MenuTree.getSelectionModel().getSelectedNode();
			// 在action判断itemId是否为空不为空则删除项目为空则删除等级
			var itemId = "";
			var levelId = "";
			node.expand();
			Ext.Msg.confirm(commonality_waitTitle, commonality_affirmDelMsg,
					function(btn) {
						if (btn == "yes") {
							if (node.getDepth() == 2) {
								itemId = node.id;
							} else {
								levelId = node.id;
							}
							deleteLh4Node(levelId, itemId);
						}
					});

		}

		// 定义保存项目的方法
		function saveRecord(flag) {
			var form = Ext.getCmp("lh4AddItemFormPanel").getForm();
			if (!form.isValid()) {
				alert('表单输入有问题，请检查！');
				return;
			}
			var node = lh4MenuTree.getSelectionModel().getSelectedNode();
			if (flag == "save") {
				if (node.lastChild != null) {
					var itemSort = node.lastChild.attributes.itemSort;
				} else {
					var itemSort = 1;
				}
				Ext.getCmp("lh4AddItemFormPanel").find("name", "itemFlg")[0]
						.setValue(node.id);
				Ext.getCmp("lh4AddItemFormPanel").find("name", "itemSort")[0]
						.setValue(itemSort + 1);
			} else if (flag == 'update') {
				Ext.getCmp("lh4AddItemFormPanel").find("name", "itemId")[0]
						.setValue(node.id);
			}
			form.submit({
						url : saveOrUpdateItem,
						waitTitle : commonality_waitTitle,
						method : 'post',
						waitMsg : commonality_waitMsg,
						success : function(form, action) {
							var msg = Ext.util.JSON
									.decode(action.response.responseText);
							alert(commonality_messageSaveMsg);
							Ext.getCmp("lh4AddItemWindow").close();
							if (flag == 'update') { // 更新刷新选中结点的父节点
								node.parentNode.reload();
							} else {
								node.reload(); // 添加刷新选中的该结点
							}
						},
						failure : function(form, action) {
							alert(commonality_saveMsg_fail);
						}
					})

		}
		// 定义保存/更新级别的方法
		function saveLevelRecord(flag) {
			var form = Ext.getCmp("lh4AddLevelFormPanel").getForm();
			if (!form.isValid()) {
				alert('表单输入有问题，请检查！');
				return;
			}
			var node = lh4MenuTree.getSelectionModel().getSelectedNode();
			if (flag == 'save') {
				Ext.getCmp("lh4AddLevelFormPanel").find("name", "itemId")[0]
						.setValue(node.id);
			} else if (flag == 'update') {
				Ext.getCmp("lh4AddLevelFormPanel").find("name", "levelId")[0]
						.setValue(node.id);
				Ext.getCmp("lh4AddLevelFormPanel").find("name", "itemId")[0]
						.setValue(node.parentNode.id);
			}
			form.submit({
						url : saveOrUpdateLevel,
						method : 'post',
						waitTitle : commonality_waitTitle,
						waitMsg : commonality_waitMsg,
						success : function(form, action) {
							var msg = Ext.util.JSON
									.decode(action.response.responseText);
							if (msg.checkLevel == false) {
								alert('数据库已经存在该级别值的级别信息');
								return;
							}
							alert(commonality_messageSaveMsg);
							Ext.getCmp("lh4AddLevelWindow").close();

							if (flag == 'update') {// 是否是"更新"操作。注：更新操作完成刷新其父节点，
								node.parentNode.reload();
							} else if (flag == 'save') {
								node.reload();
							}
						},
						failure : function(form, action) {
							alert(commonality_saveMsg_fail);
						}
					})
		}
		// 定义一个删除的方法
		function deleteLh4Node(levelId, itemId) {
			var node = lh4MenuTree.getSelectionModel().getSelectedNode();
			var nodeP = node.parentNode;
			if (node.hasChildNodes()) {
				alert('请先删除子节点！');
				return;
			}
			var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				msg : commonality_waitMsg,
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
							alert(commonality_messageDelMsg);
							if (node.getDepth() == 1) {
								root.reload(); // 刷新根节点
							} else {
								nodeP.parentNode.reload(); // 不需要刷新所有的节点只需要刷新上上级节点
							}
						},
						failure : function(response, action) {
							waitMsg.hide();
							alert(commonality_messageDelMsgFail);
						}
					});
		}
		return lh4MenuTree;
	}

	// 定义保存Lh4的初始化方法
	function saveLh4AllInit() {
		// 检测算法是否填写
		ROOTMMA = Ext.getCmp("lh4_algorithm").getValue();
		EDMMA = Ext.getCmp("lh4ed_algorithm").getValue();
		ADMMA = Ext.getCmp("lh4ad_algorithm").getValue();
		if (ROOTMMA == null || ROOTMMA == "") {
			alert('请填写LHIRF的算法！');
			return false;
		}
		if (EDMMA == null || EDMMA == "") {
			alert('请填写ED的算法');
			return false;
		}
		if (ADMMA == null || ADMMA == "") {
			alert('请填写ED的算法');
			return false;
		}
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
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
						var msg = Ext.util.JSON.decode(response.responseText);
						if (msg.success == false) {
							alert('数据不完整，请填写完整再试！');
							return;
						} else { // levels 是级别的数量
							var levels = msg.success;
						}
						saveLh4All(levels);
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('检测数据完整性时，出现故障。请稍后再试！');
						return false;
					}
				});
	}
	// 定义保存Lh4的方法
	function saveLh4All(levels) {
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		waitMsg.show();
		Ext.Ajax.request({
					url : saveLH4,
					method : "POST",
					params : {
						ROOTMMA : ROOTMMA,
						EDMMA : EDMMA,
						ADMMA : ADMMA
					},
					success : function(response, action) {
						waitMsg.hide();
						var msg = Ext.util.JSON.decode(response.responseText);
						addLh5(levels);
						lh4Isfull = true;
						Ext.getCmp("treeConfirm").setDisabled(true);
						Ext.getCmp("finalBack").setDisabled(false);
						Ext.getCmp("gridConfirm").setDisabled(false);

					},
					failure : function(response, action) {
						alert(commonality_saveMsg_fail);
						waitMsg.hide();
					}
				});
	}

	/** *******************************显示LH4的算法*************************************************** */
	Ext.getCmp("lh4_algorithm").setValue(LH4MMA, true);
	Ext.getCmp("lh4ed_algorithm").setValue(EDMMA, true);
	Ext.getCmp("lh4ad_algorithm").setValue(ADMMA, true);
})

/** ********************************************LH5****************************************************************** */
function lh5() {
	/*
	 * 初始化"自定义评级列表LH5区域"的数据
	 */
	storeLH5 = new Ext.data.Store({
				url : loadCusIntUrl,
				reader : new Ext.data.JsonReader({
							root : "rootLh5",
							fields : [{
										name : 'valueA'
									}, {
										name : 'valueB'
									}, {
										name : 'level'
									}, {
										name : 'intervalIdA'
									}, {
										name : 'intervalIdB'
									}

							]
						})
			});
	storeLH5.load();

	/*
	 * 用于绘制"自定义评级列表LH5"的表样式和字段
	 */
	var colMlh5 = new Ext.grid.ColumnModel([{
				header : '敏感等级' + commonality_mustInput,
				dataIndex : "level",
				width : 156
			}, {
				header : '妨碍飞机持续安全飞行或着陆 ( 是 )' + commonality_mustInput,
				dataIndex : "valueA",
				width : 230,
				editor : new Ext.form.TextField({
							allowBlank : false,
							maxLength : 200
							///maxLengthText : commonality_MaxLengthText
						})

			}, {
				header : '妨碍飞机持续安全飞行或着陆 ( 否 )' + commonality_mustInput,
				dataIndex : "valueB",
				width : 235,
				editor : new Ext.form.TextField({
							allowBlank : false,
							maxLength : 200
							//maxLengthText : commonality_MaxLengthText
						})

			}, {
				header : "A Id",
				dataIndex : "intervalIdA",
				hidden : true
			}, {
				header : "B Id",
				dataIndex : "intervalIdB",
				hidden : true
			}]);

	var smLh5 = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	/**
	 * 用于绘制"自定义评级表LH4区域"的表头样式和追加,保存,删除按钮.
	 */
	gridLh5 = new Ext.grid.EditorGridPanel({
				title : 'LHIRF  是否妨碍飞机持续安全飞行或着陆 自定义评级维护',
				sm : smLh5,
				cm : colMlh5,
				split : false,
				clicksToEdit : 2,
				border : true,
				store : storeLH5,
				region : 'center',
				stripeRows : true,
				width : 512,
				tbar : ["->", {
							id : "finalBack",
							text : commonality_back,
							xtype : "button",
							iconCls : "icon_gif_back",
							disabled : !lh4Isfull,
							handler : function() {
								finalBack();
							}
						},

						"-", {
							id : 'gridConfirm',
							text : commonality_affirm,
							xtype : "button",
							iconCls : "icon_gif_accept",
							disabled : !lh4Isfull,
							handler : function() {
								saveLh5();
							}
						}]
			});

	/** **********************操作方法******************************* */
	/**
	 * 回退
	 */
	function finalBack() {
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		waitMsg.show();
		Ext.Ajax.request({
					url : finalBackUrl,
					method : "POST",
				//	async : false,	//同步
					success : function(response, action) {
						waitMsg.hide();
						var msg = Ext.util.JSON
									.decode(response.responseText);
							if (msg.checkS45 == false) {
								alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
								return;
							}
						storeLH5.load();
						lh4Isfull = false;
						Ext.getCmp("treeConfirm").setDisabled(false);
						Ext.getCmp("finalBack").setDisabled(true);
						Ext.getCmp("gridConfirm").setDisabled(true);
					},
					failure : function(form, action) {
						alert('回退失败,请稍候再试!');
						waitMsg.hide();
					}
				});
	}
	/**
	 * 验证 "自定义评级表LH5"提交的数据信息是否为空,是否重复
	 * 
	 * @returns boolean
	 */
	function saveLh5(levelCount) {
		Ext.MessageBox.show({
					title : commonality_affirm,
					msg : commonality_affirmSaveMsg,
					buttons : Ext.Msg.YESNO,
					fn : function(id) {
						if (id == 'cancel') {
							return;
						} else if (id == 'yes') {
							// 检测输入的内容是否存在空值和是否存在重复的敏感级别
							for (var i = 0; i < storeLH5.modified.length; i++) {
								var record = storeLH5.modified[i];
								if (record.get('valueA') == null
										|| record.get('valueA') == '') {
									temp = 1;
									alert('请填写完整带“*”的必输项');
									return false;
								}
								if (record.get('valueB') == null
										|| record.get('valueB') == '') {
									temp = 1;
									alert('请填写完整带“*”的必输项');
									return false;
								}
							}
							saveData();
						}
					}
				});
	}

	/*
	 * 保存敏感级别列表
	 */
	function saveData() {
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		waitMsg.show();
		var tempT = false;
		var json = [];
		Ext.each(storeLH5.modified, function(item) {
					json.push(item.data);
				});
		if (json.length > 0) {
			Ext.Ajax.request({
						url : saveCusIntUrl,
						params : {
							jsonData : Ext.util.JSON.encode(json)
						},
						method : "POST",
						success : function(response, action) {
							waitMsg.hide();
							var msg = Ext.util.JSON
									.decode(response.responseText);
							if (msg.checkS45 == false) {
								alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
								return;
							}
							storeLH5.load();
							storeLH5.modified = [];
							tempT = true;
							alert('保存成功！');
						},
						failure : function(response, action) {
							waitMsg.hide();
							alert('数据更新失败，请稍后再试！');
						}
					});
		} else {
			alert('没有需要保存的数据 !');
			tempT = true;
			waitMsg.hide();
		}
		return tempT;
	}

	return gridLh5;
}

function addLh5(levels) {
	var index = storeLH5.getCount();
	if (index != levels) {
		for (var i = 0; i < levels; i++) {
			var index = storeLH5.getCount();
			var rec = gridLh5.getStore().recordType;
			var p = new rec({
						valueA : '',
						valueB : '',
						level : index + 1,
						intervalIdA : '',
						intervalIdB : ''
					});
			storeLH5.insert(index, p);
			storeLH5.modified.push(p);
		}
	}
}
var displayForm = new Ext.form.FormPanel({
				title : "自定义LH4显示信息",
				labelWidth : 160, // 标题宽度
				labelAlign : "right",
				height : "auto",
				bodyStyle : "padding:10px 10px",
				items : [ {
					xtype : 'hidden',
					name : 'displayId'
				}, new Ext.form.TextArea({
					fieldLabel : "描述信息",// 中文描述
					name : 'displayContent',
					id : 'displayContent',
					width : 640,
					height : 100,
					maxLength : 2500,
					maxLengthText : commonality_MaxLengthText,
					value : ''
				})],
				reader : new Ext.data.JsonReader({
					root : 'cusDisplay'
				}, [ {
					name : 'displayId',
					mapping : 'id'
				}, {
					name : 'displayContent',
					mapping : 'displayContent'
				}]),
				tbar : [ new Ext.Button({
					text : commonality_save,
					iconCls : 'icon_gif_save',
					handler : function() {
						save(displayForm);
					}
				}) ]
			});
			displayForm.form.load({
				url : contextPath+"/paramDefineManage/defineBaseCrackLen/loadCusDisplay.do"
			});
			function save() {
				if (displayForm.form.isValid()) {
					displayForm.form.submit({
						url : contextPath+"/paramDefineManage/defineBaseCrackLen/saveCusDisplay.do", // 处理修改后台地址
						waitTitle : commonality_waitTitle,
						waitMsg : commonality_waitMsg, // "正在向服务器提交数据...",
						method : 'post',
						success : function(form, action) {
							alert(commonality_messageSaveMsg);// ("信息","保存成功！");
							displayForm.form.load({
									url : contextPath+"/paramDefineManage/defineBaseCrackLen/loadCusDisplay.do"
								});
						},
						failure : function(form, action) {
							alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
						}
					})
				}
			}
// Ext.getCmp("lh4ed_algorithm").setValue(LH4EDMMA,true);
// Ext.getCmp("lh4ad_algorithm").setValue(LH4ADMMA,true);

