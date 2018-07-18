
/** ********************************************S4B自定义矩阵树******************************************** */
function s4b() {
	// 加载Ext树上面的节点数据
	var initTreeStore = contextPath
			+ "/paramDefineManage/defineStructureParameter/loadCustom.do?stepFlg=S4B";
	// 检测自定义矩阵树是否完整
	var checkTreeIsFull = contextPath
			+ "/paramDefineManage/defineStructureParameter/checkFull.do?stepFlg=S4B";
	// 保存S5B
	var saveS4B = contextPath
			+ "/paramDefineManage/defineStructureParameter/saveAll.do?stepFlg=S4B";
	// 保存/更新项目
	var saveOrUpdateItem = contextPath
			+ "/paramDefineManage/defineStructureParameter/saveS45Item.do?stepFlg=S4B";
	// 保存/更新级别
	var saveOrUpdateLevel = contextPath
			+ "/paramDefineManage/defineStructureParameter/saveS45Level.do?stepFlg=S4B";
	// 删除节点
	var deleteNode = contextPath
			+ "/paramDefineManage/defineStructureParameter/deleteNode.do?stepFlg=S4B";
	// 定义前两级的树节点（固定的）
	var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				text : "非金属ED参数"// 节点名称
			});

	var s4bMenuTree = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
							baseAttrs : {},
							dataUrl : initTreeStore
						}),
				root : root,
				autoScroll : false,
				animate : false,
				useArrows : false,
				border : false,
				autoScroll : true,
				region : 'center',
				tbar : ["非金属ED参数" + commonality_mustInput, {
							name : 's4b_algorithm',
							id : 's4b_algorithm',
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
							text : commonality_affirm,
							xtype : "button",
							iconCls : "icon_gif_accept",
							handler : function() {
								saveS4bAllInit();
							}
						}]
			});

	// menuTree.getRootNode().reload();
	s4bMenuTree.expandAll();
	// menuTree.collapse(true);
	// 创建右键弹出的菜单
	var s4bContextMenu = new Ext.menu.Menu({
				id : 's4bMenuTreeContextMenu',
				items : [{
							text : '新增项目',
							id : 's4bAddItem',
							iconCls : 'page_addIcon',
							handler : function() {
								s4bAddItemInit();
							}

						}, {
							text : '新增级别',
							iconCls : 'page_addIcon',
							id : 's4bAddLevel',
							handler : function() {
								addLevelInit();
							}
						}, {
							text : '编辑项目',
							iconCls : 'page_edit_1Icon',
							handler : function() {
								s4bEditItemInit();
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
								var selectModel = s4bMenuTree
										.getSelectionModel();
								var selectNode = selectModel.getSelectedNode();
								if (selectNode.attributes.leaf) {
									selectNode.parentNode.reload();
								} else {
									selectNode.reload();
								}
							}
						}]
			});
	// 给节点添加点击右键的事件
	s4bMenuTree.on('contextmenu', function(node, e) {
				e.preventDefault();
				menuid = node.attributes.id;
				menuname = node.attributes.text;
				node.select();
				var arr = e.getXY();
				arr[1] = arr[1] - 50;
				
				if (node.getDepth() == 0) { // 根节点右键
					s4bContextMenu.items.item(0).show();
					s4bContextMenu.items.item(1).hide();
					s4bContextMenu.items.item(2).hide();
					s4bContextMenu.items.item(3).hide();
					s4bContextMenu.items.item(4).hide();
					s4bContextMenu.items.item(5).show();
					s4bContextMenu.showAt(e.getXY());
				} else if (node.getDepth() == 1) { // 第一级结点
					s4bContextMenu.items.item(0).show();
					s4bContextMenu.items.item(1).hide();
					s4bContextMenu.items.item(2).show();
					s4bContextMenu.items.item(3).hide();
					s4bContextMenu.items.item(4).show();
					s4bContextMenu.items.item(5).show();
				} else if (node.getDepth() == 2) { // 第二级结点
					s4bContextMenu.items.item(0).hide();
					s4bContextMenu.items.item(1).show();
					s4bContextMenu.items.item(2).show();
					s4bContextMenu.items.item(3).hide();
					s4bContextMenu.items.item(4).show();
					s4bContextMenu.items.item(5).show();
				} else if (node.getDepth() == 3) { // 第三级结点
					s4bContextMenu.items.item(0).hide();
					s4bContextMenu.items.item(1).hide();
					s4bContextMenu.items.item(2).hide();
					s4bContextMenu.items.item(3).show();
					s4bContextMenu.items.item(4).show();
					s4bContextMenu.items.item(5).show();
				}
				
				
					
				   s4bContextMenu.showAt(e.getXY());
			});
	// var node=;
	// 创建增加项目的面板
	function s4bAddItemFormPanel() {
		var s4bAddItemFormPanel = new Ext.form.FormPanel({
					id : 's4bAddItemFormPanel',
					name : 's4bAddItemFormPanel',
					defaultType : 'textfield',
					labelAlign : 'right',
					labelWidth : 100,
					frame : false,
					bodyStyle : "padding:10px 10px",
					// bodyStyle : 'padding:5 5 0',
					items : [{
								fieldLabel : '项目名' + commonality_mustInput,
								name : 'itemName',
								allowBlank : false,
								maxLength : 50,
								maxLengthText : commonality_MaxLengthText,
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
								forceSelection : true,
								allowBlank : algHidden ? true : false,
								hidden : algHidden,
								anchor : '91%',
								xtype : "combo",
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
		return s4bAddItemFormPanel;
	}
	// 创建增加级别的面板
	function s4bAddLevelFormPanel() {
		var s4bAddLevelFormPanel = new Ext.form.FormPanel({
			id : 's4bAddLevelFormPanel',
			name : 's4bAddLevelFormPanel',
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
								fieldLabel : '级别值' + commonality_mustInput,
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
							fieldLabel : '级别名' + commonality_mustInput,
							allowBlank : false,
							maxLength : 9,
							maxLengthText : commonality_MaxLengthText,
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
		return s4bAddLevelFormPanel;
	}
	// 创建项目的窗口
	function s4bAddItemWindowFunc() {
		s4bAddItemWindow = new Ext.Window({
			id : 's4bAddItemWindow',
			name : 's4bAddItemWindow',
			layout : 'fit',
			width : 420,
			height : 285,
			resizable : false,
			draggable : true,
			closeAction : 'close',
			title : '<span class="commoncss">' + '新增项目' + '</span>',
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
			items : [s4bAddItemFormPanel()],
			buttons : [{
						id : 's4bSaveItem',
						text : commonality_save,
						handler : function() {
							saveRecord("save");
						}
					}, {
						id : 's4bUpdateItem',
						text : commonality_modify,
						handler : function() {
							saveRecord("update");
						}
					}, {
						text : commonality_reset,
						id : 'btnReset',
						handler : function() {
							Ext.getCmp("s4bAddItemFormPanel").getForm().getEl().dom
									.reset();
						}
					}, {
						text : commonality_close,
						handler : function() {
							s4bAddItemWindow.close();
						}
					}]
		});
		return s4bAddItemWindow;
	}
	// 创建级别弹出的级别窗口
	function s4bAddLevelWindowFunc() {
		var s4bAddLevelWindow = new Ext.Window({
					name : 's4bAddLevelWindow',
					id : 's4bAddLevelWindow',
					layout : 'fit',
					width : 300,
					height : 200,
					resizable : false,
					draggable : true,
					closeAction : 'close',
					title : '<span class="commoncss">' + '新增级别' + '</span>',
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
					items : [s4bAddLevelFormPanel()],
					buttons : [{
								id : 's4bSaveLevel',
								pageX : 10,
								text : commonality_save,
								handler : function() {
									saveLevelRecord("save");
								}
							}, {
								id : 's4bUpdateLevel',
								pageX : 10,
								text : commonality_modify,
								handler : function() {
									saveLevelRecord("update");
								}
							}, {
								text : commonality_reset,
								id : 'btnReset',
								handler : function() {
									Ext.getCmp("s4bAddLevelFormPanel")
											.getForm().getEl().dom.reset();
								}
							}, {
								text : commonality_close,
								handler : function() {
									s4bAddLevelWindow.close();
								}
							}]
				});
		return s4bAddLevelWindow;
	}

	// 定义保存S4b的初始化方法
	function saveS4bAllInit() {
		// 检测算法是否填写
		ROOTMMA = Ext.getCmp("s4b_algorithm").getValue();
		if (ROOTMMA == null || ROOTMMA == "") {
			alert('请填写非金属ED的算法！');
			return;
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
							alert('您录入的数据不完整，请录入完整!');
							return;
						} else {
							saveS4bAll();
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('检测数据完整性时，出现故障。请稍后再试！');
					}
				});
	}

	// 定义增加项目的初始化方法
	function s4bAddItemInit() {
		var node = s4bMenuTree.getSelectionModel().getSelectedNode();
		if (node.getDepth() != 0) { // 选中的0级节点
			algHidden = true;
		} else {
			algHidden = false;
		}
		s4bAddItemWindowFunc().show(); // 此方法放在前面不可后移
		Ext.getCmp("s4bSaveItem").show();
		Ext.getCmp("s4bUpdateItem").hide();
	}
	// 定义编辑项目的初始化方法
	function s4bEditItemInit() {
		var node = s4bMenuTree.getSelectionModel().getSelectedNode();
		if (node.getDepth() != 1) { // 选中的1级节点
			algHidden = true;
		} else {
			algHidden = false;
		}
		s4bAddItemWindowFunc().show();
		Ext.getCmp("s4bAddItemFormPanel").find("name", "itemName")[0]
				.setValue(node.attributes.itemName);
		
		Ext.getCmp("s4bAddItemFormPanel").find("name", "itemAlgorithm")[0]
				.setValue(node.attributes.itemAlgorithm);
		Ext.getCmp("s4bSaveItem").hide();
		// 将弹出的s4bAddItemWindow窗口的标题改成编辑项目
		Ext.getCmp("s4bAddItemWindow").setTitle('编辑项目');
		Ext.getCmp("s4bUpdateItem").show();

	}

	// 定义增加级别的初始化方法
	function addLevelInit() {
		var node = s4bMenuTree.getSelectionModel().getSelectedNode();
		s4bAddLevelWindowFunc().show(); // 此方法放在前面不可后移
		Ext.getCmp("s4bUpdateLevel").hide();
		Ext.getCmp("s4bSaveLevel").show();
	}
	// 定义编辑级别的初始化方法
	function editLevelInit() { // 编辑级别
		var node = s4bMenuTree.getSelectionModel().getSelectedNode();
		s4bAddLevelWindowFunc().show();
		var tempPanel = Ext.getCmp("s4bAddLevelFormPanel");
		tempPanel.find("name", "levelValue")[0].setValue(node.attributes.sort);
		tempPanel.find("name", "levelName")[0]
				.setValue(node.attributes.levelName);
		
		tempPanel.find("name", "itemId")[0].setValue(node.attributes.itemId);
		Ext.getCmp("s4bSaveLevel").hide();
		// 将弹出的s4bAddLevelWindow窗口的标题改成编辑级别
		Ext.getCmp("s4bAddLevelWindow").setTitle('编辑级别');

		Ext.getCmp("s4bUpdateLevel").show();
	}
	// 定义删除的初始化方法
	function deleteInit() {
		var node = s4bMenuTree.getSelectionModel().getSelectedNode();
		// 在action判断itemId是否为空不为空则删除项目为空则删除等级
		var itemId = "";
		var levelId = "";
		node.expand();
		Ext.Msg.confirm(commonality_waitTitle, commonality_affirmDelMsg,
				function(btn) {
					if (btn == "yes") {
						if (node.getDepth() == 1 || node.getDepth() == 2) {
							itemId = node.id;
						} else {
							levelId = node.id;
						}
						deleteS4bNode(levelId, itemId);
					}
				});

	}
	// 定义保存S4b的方法
	function saveS4bAll() {
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		waitMsg.show();
		Ext.Ajax.request({
					url : saveS4B,
					method : "POST",
					params : {
						ROOTMMA : ROOTMMA
					},
					success : function(response, action) {
						waitMsg.hide();
						var msg = Ext.util.JSON.decode(response.responseText);
						if (msg.checkS45 == false) {
							alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
						} else {
							alert(commonality_messageSaveMsg);
							var node = s4bMenuTree.getRootNode();
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert(commonality_saveMsg_fail);
					}
				});
	}

	// 定义保存项目的方法
	function saveRecord(flag) {
		var form = Ext.getCmp("s4bAddItemFormPanel").getForm();
		if (!form.isValid()) {
			alert('表单输入有问题，请检查！');
			return;
		}
		if (Ext.getCmp("s4bAddItemFormPanel").find("name", "itemAlgorithm")[0]
				.isVisible()) { // 算法可见
			var algValue = Ext.getCmp("s4bAddItemFormPanel").find("name",
					"itemAlgorithm")[0].getValue();
			if (algValue == "" || algValue == null) {
				alert('表单输入有问题，请检查！');
				return;
			}
		}

		var node = s4bMenuTree.getSelectionModel().getSelectedNode();
		if (flag == "save") {
			if (node.lastChild != null) {
				var itemSort = node.lastChild.attributes.itemSort;
			} else {
				var itemSort = 1;
			}
			Ext.getCmp("s4bAddItemFormPanel").find("name", "itemFlg")[0]
					.setValue(node.id);
			Ext.getCmp("s4bAddItemFormPanel").find("name", "itemSort")[0]
					.setValue(itemSort + 1);
		} else if (flag == 'update') {
			Ext.getCmp("s4bAddItemFormPanel").find("name", "itemId")[0]
					.setValue(node.id);
		}
		form.submit({
					url : saveOrUpdateItem,
					waitTitle : commonality_waitTitle,
					method : 'post',
					waitMsg : commonality_waitMsg,
					params : {
						itemAlg : Ext.getCmp('itemAlgorithm').getValue()
					},
					success : function(form, action) {
						var msg = Ext.util.JSON
								.decode(action.response.responseText);
						if (msg.success == "count") {
							alert('一级项目超过了数据库承载的数量或者一级二级项目数量总和超过了数据库承载的数量');
							Ext.getCmp("s4bAddItemWindow").close();
							return;
						}
						if (msg.checkS45 == false) {
							alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
							Ext.getCmp("s4bAddItemWindow").close();
						} else {
							alert(commonality_messageSaveMsg);
							Ext.getCmp("s4bAddItemWindow").close();
							if (flag == 'update') { // 更新刷新选中结点的父节点
								node.parentNode.reload();
							} else {
								node.reload(); // 添加刷新选中的该结点
							}
						}
					},
					failure : function(form, action) {
						alert(commonality_saveMsg_fail);
					}
				})

	}
	// 定义保存/更新级别的方法
	function saveLevelRecord(flag) {
		var form = Ext.getCmp("s4bAddLevelFormPanel").getForm();
		if (!form.isValid()) {
			alert('表单输入有问题，请检查！');
			return;
		}
		var node = s4bMenuTree.getSelectionModel().getSelectedNode();
		if (flag == 'save') {
			Ext.getCmp("s4bAddLevelFormPanel").find("name", "itemId")[0]
					.setValue(node.id);
		} else if (flag == 'update') {
			Ext.getCmp("s4bAddLevelFormPanel").find("name", "levelId")[0]
					.setValue(node.id);
					Ext.getCmp("s4bAddLevelFormPanel").find("name", "itemId")[0]
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
						if (msg.checkS45 == false) {
							alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
							Ext.getCmp("s4bAddLevelWindow").close();
						} else {
							if (msg.checkLevel == false) {
								alert('数据库已经存在该级别值的级别信息');
								return;
							}
							if (msg.checkLevelNum == false){
								 alert('级别不能超过5级!');
								 Ext.getCmp("s4bAddLevelWindow").close();
								 return ;
							}
								
							alert(commonality_messageSaveMsg);
							Ext.getCmp("s4bAddLevelWindow").close();

							if (flag == 'update') {// 是否是"更新"操作。注：更新操作完成刷新其父节点，
								node.parentNode.reload();
							} else if (flag == 'save') {
								node.reload();
							}
						}
					},
					failure : function(form, action) {
						alert(commonality_saveMsg_fail);
					}
				})
	}
	// 定义一个删除的方法
	function deleteS4bNode(levelId, itemId) {
		var node = s4bMenuTree.getSelectionModel().getSelectedNode();
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
						var msg = Ext.util.JSON.decode(response.responseText);
						if (msg.checkS45 == false) {
							alert('此机型已经存在自定义的分析数据，无法进行修改数据！');
						} else {
							alert(commonality_messageDelMsg);
							if (node.getDepth() == 1) {
								root.reload(); // 刷新根节点
							} else {
								nodeP.parentNode.reload(); // 不需要刷新所有的节点只需要刷新上上级节点
							}
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert(commonality_messageDelMsgFail);
					}
				});
	}
	return s4bMenuTree;
}