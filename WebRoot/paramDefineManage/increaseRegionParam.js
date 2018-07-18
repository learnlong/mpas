Ext.namespace('increaseRegionParam');
// 初始化画面
var loadTree = contextPath + "/paramDefineManage/increaseRegionParam/treeLoad.do";
// 保存主矩阵项目
var saveItem = contextPath + "/paramDefineManage/increaseRegionParam/saveMatrixTree.do";
// 保存主矩阵级别
var saveLevel = contextPath + "/paramDefineManage/standardRegionParam/saveLevel.do?type=ZA43";
// 删除主矩阵项目
var delItem = contextPath + "/paramDefineManage/increaseRegionParam/delItemNode.do";
// 删除主矩阵级别
var delLevel = contextPath + "/paramDefineManage/standardRegionParam/delLevelNode.do?type=ZA43";
// 确认主矩阵自定义完成
var confirmMainMatrix = contextPath + "/paramDefineManage/increaseRegionParam/confirmMainMatrix.do";
// 确认第一副矩阵自定义完成
var confirmFirstMatrix = contextPath + "/paramDefineManage/increaseRegionParam/confirmFristMatrix.do";
// 确认检查间隔矩阵自定义完成
var confirmFinalMatrix = contextPath + "/paramDefineManage/increaseRegionParam/confirmFinalMatrix.do";
// 撤销矩阵 
var revokeMatrix = contextPath + "/paramDefineManage/increaseRegionParam/revokeMatrix.do";

Ext.onReady(function() {
	
	Ext.QuickTips.init();// 初始化显示提示信息。没有它提示信息出不来。  
    Ext.form.Field.prototype.msgTarget = "qtip";
	
    // 公共方法,共用的树的根节点
	var root = new Ext.tree.AsyncTreeNode({
				text : '根节点',
				id : '00',
				leaf : false
			});
			
	// 主矩阵树数据源		
	treeLoader = new Ext.tree.TreeLoader({
							dataUrl : loadTree
						});	
	// 在树节点加载完后再设置主矩阵部分是否可用					
   /* treeLoader.on('load',function(node,response){
    		treeOuterPanel.setDisabled(TREE_DISABLED);			
    })*/
    
    /**
	 * ed算法下拉框store
	 */		
	var edAlgorithmStore = new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
                data : [[MAX,'最大值'],[MIN,'最小值']]
			})
	/**
	 * ad算法下拉框store
	 */
	var adAlgorithmStore = new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
                data : [[MAX,'最大值'],[MIN,'最小值']]
			})		
    
	// 主矩阵树		
	var itemTree = new Ext.tree.TreePanel({
				loader : treeLoader,
				root : root,
				autoScroll : false,
				animate : false,
				useArrows : false,
				border : false,
				autoScroll : true,
				region : 'center',
				width:280,
				tbar : ['ED算法' + commonality_mustInput, {
							name : 'ed_algorithm',
							id : 'ed_algorithm',
							valueField : "retrunValue",
							displayField : "displayText",
							mode : 'local',
							triggerAction : 'all',
							anchor : '95%',
							xtype : "combo",
							editable : false,
							width : 60,
							store : edAlgorithmStore
						}, "-", 'AD算法' + commonality_mustInput, {
							name : 'ad_algorithm',
							id : 'ad_algorithm',
							valueField : "retrunValue",
							displayField : "displayText",
							mode : 'local',
							triggerAction : 'all',
							anchor : '95%',
							xtype : "combo",
							editable : false,
							width : 60,
							store : adAlgorithmStore
						}, "-","->", {
							id : 'treeConfirm',
							text : commonality_affirm,
							xtype : "button",
							iconCls : "icon_gif_accept",
							disabled : TREE_DISABLED,
							handler : function() {
								// 执行确认主矩阵自定义完成
								confirmEvent(confirmMainMatrixEvent,
										'确认主矩阵自定义完成?');
							}
						}]
			});
	
			
	// 展开所有树节点
	itemTree.expandAll();
			
	// 主矩阵树节点右键触发的菜单
	var contextMenu = new Ext.menu.Menu({
				id : 'menuTreeContextMenu',
				items : [{
							text : '新增项目',
							id : 'addItem',
							iconCls : 'page_addIcon',
							handler : function() {
								loadItemInit("add");
							}						
						}, {
							text : '修改项目',
							id : 'updateItem',
							iconCls : 'page_edit_1Icon',
							handler : function() {
								loadItemInit("update");
							}
						},{
							text : '新增级别',
							iconCls : 'page_addIcon',
							id : 'addParameter',
							handler : function() {
								loadLevelInit("add");
							}
						}, {
							text : '修改级别',
                            id : 'updateParameter',
							iconCls : 'page_edit_1Icon',
							handler : function() {
							    loadLevelInit("update");
							}
						}, {
							text : '删除项目',
							id : 'delItem',
							iconCls : 'page_delIcon',
							handler : function() {	
								confirmEvent(delItemNodeEvent,commonality_affirmDelMsg);							
							}
						}, {
							text : '删除级别',
							id : 'delParameter',
							iconCls : 'page_delIcon',
							handler : function() {	
								confirmEvent(delLevelNodeEvent,commonality_affirmDelMsg);	
							}
						}, {
							text : '刷新',
							iconCls : 'page_refreshIcon',
							handler : function() {
								var selectModel = itemTree.getSelectionModel();
								var selectNode = selectModel.getSelectedNode();
								if (selectNode.attributes.leaf) {
									selectNode.parentNode.reload();
								} else {
									selectNode.reload();
								}
							}
						}]
			});
			
	// 菜单触发时执行判断	
	itemTree.on('contextmenu', function(node, e) {
		        // 主矩阵确认完毕后不可修改
		        if (TREE_DISABLED){
		        	return;
		        }
				e.preventDefault();
				menuid = node.attributes.id;
				menuname = node.attributes.text;
				// 选中当前节点
				node.select();
				var arr = e.getXY();
				// 为了防止画面底部的节点触发当前操作时menu显示区域过小的问题
				// 强行将此时出现menu的位置向上调整
				arr[1] = arr[1] - 50;
				Ext.getCmp('addItem').show();
				Ext.getCmp('updateItem').show();
				Ext.getCmp('delItem').show();				
				Ext.getCmp('addParameter').show();
				Ext.getCmp('updateParameter').show();
				Ext.getCmp('delParameter').show();
				
				if (node.getDepth() == 1){// ED/AD节点时
					Ext.getCmp('updateItem').hide();
					Ext.getCmp('delItem').hide();
					Ext.getCmp('addParameter').hide();
					Ext.getCmp('updateParameter').hide(); 
					Ext.getCmp('delParameter').hide();
					contextMenu.showAt(e.getXY());
				} else if (node.getDepth() == 2){// 项目节点时
					Ext.getCmp('addItem').hide();
					Ext.getCmp('updateParameter').hide();
					Ext.getCmp('delParameter').hide();
					contextMenu.showAt(e.getXY());
				} else if (node.getDepth() == 3){// 级别节点时
					Ext.getCmp('addItem').hide();
					Ext.getCmp('updateItem').hide();
					Ext.getCmp('delItem').hide();
					Ext.getCmp('addParameter').hide();
					contextMenu.showAt(e.getXY());
				}
			});
	
	/**
	 * 返回项目窗体
	 */		
	function returnItemWin() {
		/**
		 * 追加/修改项目的formPanel
		 */
		var addItemFormPanel = new Ext.form.FormPanel({
					id : 'addItemFormPanel',
					name : 'addItemFormPanel',
					defaultType : 'textfield',
					labelWidth : 100,
					frame : false,
					bodyStyle : "padding:10px 10px",
					items : [{
								fieldLabel : '项目名' + commonality_mustInput,
								name : 'za43_item_nm_cn',
								allowBlank : false,
								anchor : '99%',
								maxLength : 50,
								maxLengthText : commonality_MaxLengthText + 50
							},  {// 隐藏域用于存放当前项目id
								name : 'itemId',
								hidden : true
							}, {// 隐藏域用于存放当前项目排序号
								name : 'itemSort',
								hidden : true
							}, {// 隐藏域用于存放当前节点属于ED还是AD
								name : 'itemFlg',
								hidden : true
							}]
				});

		/*
		 * 项目窗体
		 */
		var addItemWindow = new Ext.Window({
					id : 'addItemWindow',
					layout : 'fit',
					width : 420,
					height : 285,
					resizable : false,
					draggable : true,
					closeAction : 'hide',
					title : '<span class="commoncss">'
							+ '维护项目' + '</span>',
					// iconCls : 'page_addIcon',
					modal : true,
					// collapsible : true,
					titleCollapse : true,
					closable : true,
					maximizable : false,
					buttonAlign : 'right',
					border : false,
					animCollapse : true,
					pageY : 20,
					pageX : document.body.clientWidth / 2 - 420 / 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addItemFormPanel],
					buttons : [{
								text : '保存',
								handler : function() {
									saveRecord();
								}
							}, {
								text : '重置',
								id : 'btnReset',
								handler : function() {
									addItemFormPanel.getForm().getEl().dom
											.reset();
								}
							}, {
								text : '关闭',
								handler : function() {
									// 清空原始值与输入值
									addItemWindow.close();
								}
							}]
				});

		return addItemWindow;
	}		
	
	/**
	 * 返回级别窗体
	 */
    function returnLevelWin() {
		/*
		 * 追加/修改级别的formpanel
		 */
		var addLevelFormPanel = new Ext.form.FormPanel({
					id : 'addLevelFormPanel',
					name : 'addLevelFormPanel',
					labelWidth : 100,
					defaultType : 'textfield',
					frame : false,
					bodyStyle : "padding:10px 10px",
					items : [{
								fieldLabel : '级别名' + commonality_mustInput,
								name : 'level_nm_cn',
								allowBlank : false,
								anchor : '99%',
								maxLength : 20,
								maxLengthText : commonality_MaxLengthText + 20
							}, {
								fieldLabel : '级别值' + commonality_mustInput,
								name : 'level_value',
								allowBlank : false,
								anchor : '99%',
								maxLength : 10,
								maxLengthText : commonality_MaxLengthText + 10
							}, {// 隐藏域用于存放当前级别节点所属的项目节点
								name : 'item_id',
								hidden : true,
								anchor : '99%'
							}, {// 隐藏域用于存放当前级别节点id
								name : 'level_id',
								hidden : true
							}]
				});

		/**
		 * 级别窗体
		 */
		var addLevelWindow = new Ext.Window({
					id : 'addLevelWindow',
					layout : 'fit',
					width : 420,
					height : 285,
					resizable : false,
					draggable : true,
					closeAction : 'hide',
					title : '<span class="commoncss">'
							+ '维护级别' + '</span>',
					// iconCls : 'page_addIcon',
					modal : true,
					// collapsible : true,
					titleCollapse : true,
					closable : true,
					maximizable : false,
					buttonAlign : 'right',
					border : false,
					animCollapse : true,
					pageY : 20,
					pageX : document.body.clientWidth / 2 - 420 / 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addLevelFormPanel],
					buttons : [{
								text : '保存',
								handler : function() {
									saveLevelRecord();
								}
							}, {
								text : '重置',
								id : 'btnReset',
								handler : function() {
									addLevelFormPanel.getForm().getEl().dom
											.reset();
								}
							}, {
								text : '关闭',
								handler : function() {
									// 清空原始值与输入值									
									addLevelWindow.close();
								}
							}]
				});

		return addLevelWindow;
	}
	/**
	 * 主矩阵树的外层panel,上面有刷新树的按钮
	 */ 
    var treeOuterPanel = new Ext.Panel({
				region: 'west',
				layout : 'border',
				width : 300,
				items : [{
							title : '主矩阵',
							layout :'border',
							tools : [{
										id : 'refresh',
										handler : function() {
											itemTree.root.reload()
										}
									}],
							collapsible : false,
							width : 300,
							minSize : 160,
							id : "treepanel",
							maxSize : 300,
							split : true,
							region : 'west',							
							items : [itemTree]
						}, new Ext.Panel({
									region : 'center',
									width : 280
								})]
			})		
    	
	/**
	 * 第一矩阵panel
	 */		
	var firstMatrixPanel = new Ext.Panel({
		region : 'center',
		layout : 'fit',
		width : 150,
		autoScroll : true,
		html : FIRST_MATRIX_HTML,
		title : '副矩阵',
		tbar : ["->", {
			id : "fmBack",
			text : '回退',
			xtype : "button",
			iconCls : "icon_gif_back",
			disabled : FIRST_MATRIX_DISABLED,
			handler : function() {
				confirmEvent(revokeFirstMatrixEvent, '是否确认回退？回退结果不可逆!');
			}
		}, {
			id : "fmConfirm",
			text : '确认',
			xtype : "button",
			iconCls : "icon_gif_accept",
			disabled : FIRST_MATRIX_DISABLED,
			handler : function() {
				// 执行确认第一副矩阵自定义完成
				confirmEvent(confirmFirstMatrixEvent,
						'确认副阵自定义完成?');
			}
		}]
	})      	
    
    
    /**
	 * 检查间隔矩阵
	 */
    var finalMatrixPanel = new Ext.Panel({
		region : 'east',
		layout : 'fit',
		width : 550,
		autoScroll : true,
		html : FINAL_MATRIX_HTML,
		title : '检查间隔矩阵',
		tbar : ["->", {
			id : "finalBack",
			text : '回退',
			xtype : "button",
			iconCls : "icon_gif_back",
			disabled : FINAL_MATRIX_DISABLED,
			handler : function() {
				confirmEvent(revokeFinalMatrixEvent, '是否确认回退？回退结果不可逆!');
			}
		}, {
			id : "finalConfirm",
			text : '确认',
			xtype : "button",
			iconCls : "icon_gif_accept",
			disabled : FINAL_MATRIX_DISABLED,
			handler : function() {
				confirmEvent(confirmFinalMatrixEvent,
						'确认检查间隔矩阵自定义完成?');
			}
		}]
	})     
    
	/**
	 * 主窗体
	 */
/*
 * var viewport = new Ext.Viewport({ layout : 'border', title :
 * za43Matrix_matrix, items : [treeOuterPanel,
 * firstMatrixPanel,finalMatrixPanel] });
 */
			
	/**
	 * 主窗体
	 */
	var win = new Ext.Window({
				layout : 'border',
				title : returnPageTitle('自定义增强区域参数', 'increaseRegionParam'),
				renderTo : Ext.getBody(), // 呈现在 Html Body标签中
				border : false,
				resizable : true,
				closable : false,
				maximized : true,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				items : [treeOuterPanel, firstMatrixPanel,finalMatrixPanel]
			});	
	win.show();		
			
    // 为算法赋初始选中值
	Ext.getCmp("ed_algorithm").setValue(ED_ALGORITHM,true);
	
	Ext.getCmp("ad_algorithm").setValue(AD_ALGORITHM,true);			
			
	/**
	 * 追加项目画面事件
	 * 
	 * @param operateFlg
	 *            操作区分 add/update
	 */
	function loadItemInit(operateFlg) {		
		// 当前选中节点
		var selectNode = itemTree.getSelectionModel().getSelectedNode();		
		if (operateFlg == "add"){//追加状态
			var itemSort = 1;
			// 选中节点的子节点个数大于1时
			if (selectNode.childNodes.length > 0) {
				// 因为树上的项目节点是通过itemSort排序的，所以childNodes中下标最大的节点itemSort最大
				// 所以待追加的节点的itemSort应该在此基础上+1
				var strSort = selectNode.childNodes[selectNode.childNodes.length - 1].attributes.sort;
				itemSort = parseInt(strSort) + 1;
			}
			var thisWin = returnItemWin();
			thisWin.show();

			thisWin.find("name", "itemSort")[0].setValue(itemSort);
			thisWin.find("name", "itemFlg")[0].setValue(selectNode.id);
		}else if (operateFlg == "update"){// 修改状态
			var thisWin = returnItemWin();
			thisWin.show();			
			thisWin.find("name", "itemId")[0].setValue(selectNode.id);
			thisWin.find("name","za43_item_nm_cn")[0].setValue(selectNode.attributes.itemNmCn);
			
		}
	}

	/**
	 * 追加级别画面事件
	 */
	function loadLevelInit(operateFlg) {
		var selectNode = itemTree.getSelectionModel().getSelectedNode();	
		var thisWin = returnLevelWin();
		thisWin.show();
		if (operateFlg == "add"){// 追加操作时			
			thisWin.find("name", "item_id")[0].setValue(selectNode.id);			
		}else if(operateFlg == "update"){// 修改状态
			thisWin.find("name", "level_nm_cn")[0].setValue(selectNode.attributes.levelNmCn);			
			thisWin.find("name", "level_value")[0].setValue(selectNode.attributes.levelValue);
		    thisWin.find("name", "item_id")[0].setValue(selectNode.parentId);
		    thisWin.find("name", "level_id")[0].setValue(selectNode.id);
		}	
	}
	
	/**
	 * 删除项目节点事件
	 */
	function delItemNodeEvent() {
		var selectModel = itemTree.getSelectionModel();
		var selectNode = selectModel.getSelectedNode();
		// 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});

		// 非叶节点不能删除
		if (!selectNode.isLeaf()) {
			// 提示删除失败
			alert('当前节点存在子节点 !');
		} else {
			// 显示等待消息
			waitMsg.show();
			// 发起请求查询当前页面的帮助信息
			Ext.Ajax.request({
						url : delItem,
						params : {
							itemId : selectNode.attributes.id
						},// 参数页面名称
						method : 'POST',
						success : function(response, action) {
							waitMsg.hide();
							if (response.responseText == "") {
								// 提示删除成功
								alert('删除成功！');
								// 刷新树
								itemTree.root.reload();
							} else {
								var responseObj = Ext.util.JSON
										.decode(response.responseText);
								if (responseObj.success != undefined
										&& responseObj.success == false) {
									alert('当前数据已被他人修改，请刷新画面后重新操作!');
								}
							}
						},
						failure : function(response, action) {
							waitMsg.hide();
							// 提示删除失败
							alert('数据更新失败，请稍后再试！');
						}
					});
		}
	}
	
	/**
	 * 删除级别节点事件
	 */
	function delLevelNodeEvent() {
		var selectModel = itemTree.getSelectionModel();
		var selectNode = selectModel.getSelectedNode();
		// 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});

		// 显示等待消息
		waitMsg.show();
		// 发起请求查询当前页面的帮助信息
		Ext.Ajax.request({
					url : delLevel,
					params : {
						level_id : selectNode.attributes.id
					},// 参数页面名称
					method : 'POST',
					success : function(response, action) {
						if (response.responseText == "") {
							waitMsg.hide();
							// 提示删除成功
							alert('删除成功！');
							// 刷新树
							itemTree.root.reload();
						} else {
							var responseObj = Ext.util.JSON
									.decode(response.responseText);
							if (responseObj.success != undefined
									&& responseObj.success == false) {
								alert('当前数据已被他人修改，请刷新画面后重新操作!');
							}
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						// 提示删除失败
						alert(commonality_cautionMsg);
					}
				});
	}
	
	/**
	 * 执行保存项目节点操作
	 */
	function saveRecord() {
		var form = Ext.getCmp('addItemFormPanel');

		var nmCn = form.find("name","za43_item_nm_cn")[0];

		
		if (nmCn.getValue() == ''){
		   alert('项目名称不能为空 !');
		   return;
		}
		if (nmCn.getValue().length > nmCn.maxLength){
		   alert(nmCn.maxLengthText);
		   return;
		}        
		
		
		form.getForm().submit({
			url : saveItem,
			waitTitle : '提示',		
			waitMsg : '正在向服务器提交数据...',
			success : function(form, action) {
				alert('保存成功！');								
				Ext.getCmp('addItemWindow').close();								
				root.reload();
				itemTree.expandAll();

			},
			failure : function(form, action) {				
				alert('当前数据已被他人修改，请刷新画面后重新操作!');
				// 登录失败，将提交按钮重新设为可操作 
				this.disabled = false;
			}
		})
	}
	
	/**
	 * 执行保存级别节点操作
	 */
	function saveLevelRecord() {
		var form = Ext.getCmp('addLevelFormPanel');
		
		var nmCn = form.find("name","level_nm_cn")[0];
	
		
		if (nmCn.getValue() == ''){
		   alert('级别名称不能为空 !');
		   return;
		}
		if (nmCn.getValue().length > nmCn.maxLength){
		   alert(nmCn.maxLengthText);
		   return;
		}
        

		
		var levelValue = form.find("name", "level_value")[0].getValue();
		// 判断级别值是否为整数
		if(!checkInt(levelValue)){
			alert('级别值不是整数 !');
			return;
		}	
		if (levelValue.length > 2){
		    alert('级别值长度超过2位，请重新输入 !');
		    return;
		}
		form.getForm().submit({
			url : saveLevel,
			waitTitle : commonality_waitTitle,
			waitMsg : commonality_waitMsg,
			success : function(form, action) {
				alert('保存成功！');				
				Ext.getCmp('addLevelWindow').close();
				root.reload();
				itemTree.expandAll();

			},
			failure : function(form, action) {
			    var msg = Ext.util.JSON.decode(action.response.responseText);
				if(msg.doubleLevelErr == true){
				    alert('级别值重复,请重新输入 !');
					return;
				}
				if(msg.levelCountErr == true){
				   alert('级别最多允许5级!');
				   return;
				}				
				
				alert('保存失败！');
				// 登录失败，将提交按钮重新设为可操作 
				this.disabled = false;
			}
		})
	}
	
	/**
	 * 确认主矩阵
	 */
	function confirmMainMatrixEvent() {
		// 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : '正在向服务器提交数据...',
			removeMask : true
				// 完成后移除
			});

		var edAlgorithmValue = Ext.getCmp('ed_algorithm').getValue();
		var adAlgorithmValue = Ext.getCmp('ad_algorithm').getValue();

		if (edAlgorithmValue == null || edAlgorithmValue == "") {
			alert('请选择ED算法 !');
			return;
		}

		if (adAlgorithmValue == null || adAlgorithmValue == "") {
			alert('请选择AD算法 !');
			return;
		}

		// 显示等待消息
		waitMsg.show();
		// 发起请求查询当前页面的帮助信息
		Ext.Ajax.request({
					url : confirmMainMatrix,
					method : 'POST',
					params : {
						edAlgorithm : edAlgorithmValue,
						adAlgorithm : adAlgorithmValue
					},
					success : function(response, action) {
						if (response.responseText == '') {
							waitMsg.hide();
							return;
						}
						var responseObj = Ext.util.JSON
								.decode(response.responseText);
						waitMsg.hide();
						if (responseObj.mainMatrix != ""
								&& responseObj.mainMatrix != false) {
							// 禁用主矩阵的确认按钮
							Ext.getCmp('treeConfirm').setDisabled(true);
							// 禁用主矩阵的邮件菜单
							TREE_DISABLED = true;
							// 第一副矩阵的确认可用
							Ext.getCmp('fmConfirm').setDisabled(false);
							Ext.getCmp('fmBack').setDisabled(false);
							firstMatrixPanel.body.update(responseObj.mainMatrix);
						} else if (responseObj.success != undefined
								&& responseObj.success == false) {
							alert('当前数据已被他人修改，请刷新画面后重新操作!');
						} else {
							alert('主矩阵属性结构自定义不完整 !');
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('主矩阵属性结构自定义不完整 !');
						// 刷新树
						itemTree.root.reload();
					}
				});
	}
    
	/**
	 * 确认第一副矩阵
	 */
	function confirmFirstMatrixEvent() {
		var json = "{'root':[";
		var fmCellArray = document.getElementsByName('1fmCell');
		for (var i = 0; i < fmCellArray.length; i++) {
			var thisCell = fmCellArray[i];
			if (thisCell.value.trim() == "") {
				alert('级别值必须输入 !')
				return;
			} else if (thisCell.value.trim().length >= 3){
				alert('级别值长度过大，请重新输入 !');
				return;
			} else if (!checkInt(thisCell.value)) {
				alert('级别值不是整数 !')
				return;
			}
			var temp = "{'id' : '" + thisCell.id.replace("1fmCell", "") + "',";
			temp = temp + "'value' : '" + thisCell.value.trim() + "'}";
			json += temp;
			if (i != fmCellArray.length - 1) {
				json += ",";
			}
		}
		json += "]}";
		// 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		// 显示等待消息
		waitMsg.show();
		// 发起请求查询当前页面的帮助信息
		Ext.Ajax.request({
					url : confirmFirstMatrix,
					method : 'POST',
					params : {
						matrixJson : json
					},
					success : function(response, action) {
						if (response.responseText == '') {
							waitMsg.hide();
							return;
						}
						var responseObj = Ext.util.JSON
								.decode(response.responseText);
						waitMsg.hide();

						if (responseObj.finalMatrix != undefined
								&& responseObj.finalMatrix != "") {
							// 生成检查间隔矩阵
							Ext.getCmp('fmConfirm').setDisabled(true);
							Ext.getCmp('fmBack').setDisabled(true);
							Ext.getCmp('finalConfirm').setDisabled(false);
							Ext.getCmp('finalBack').setDisabled(false);
							finalMatrixPanel.body
									.update(responseObj.finalMatrix);
						} else if (responseObj.success != undefined
								&& responseObj.success == false) {
							alert(commonality_alreadyModified);
						} else {
							alert('副矩阵级别值定义不完整 !');
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('副矩阵级别值定义不完整 !');
					}
				});
	}

	/**
	 * 确认检查间隔矩阵
	 */
    function confirmFinalMatrixEvent() {
		var json = "{'root':[";
		var fmCellArray = document.getElementsByName('fnCell');
		for (var i = 0; i < fmCellArray.length; i++) {
			var thisCell = fmCellArray[i];
			if (thisCell.value.trim() == "") {
				alert('级别值必须输入 !')
				return;
			}else if (thisCell.value.trim().length >= 200){
				alert('级别值长度超过200，请重新输入 !');
				return;
			} /* else if (!checkInt(thisCell.value)) {
			Ext.Msg.alert("警告", "级别值必须为整数！")
			return;
			}*/
			var temp = "{'id' : '" + thisCell.id.replace("fnCell", "") + "',";
			temp = temp + "'value' : '" + thisCell.value.trim() + "'}";
			json += temp;
			if (i != fmCellArray.length - 1) {
				json += ",";
			}
		}
		json += "]}";
		// 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		// 显示等待消息	
		waitMsg.show();
		// 发起请求查询当前页面的帮助信息
		Ext.Ajax.request({
					url : confirmFinalMatrix,
					method : 'POST',
					params : {
						matrixJson : json
					},
					success : function(response, action) {
						waitMsg.hide();
						if (response.responseText == ""){
							alert(commonality_messageSaveMsg);
							return;
						}
						var responseObj = Ext.util.JSON
								.decode(response.responseText);
						if (responseObj.finalMatrix != undefined
								&& responseObj.finalMatrix == false) {
							alert('当前矩阵存在分析数据，不能更新！');
						} else if (responseObj.success != undefined
								&& responseObj.success == false) {
							alert(commonality_alreadyModified);
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('检查间隔矩阵级别值定义不完整 !');
					}
				});
	}

    /**
	 * 撤销检查间隔矩阵
	 */
    function revokeFinalMatrixEvent(){
    	// 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		// 显示等待消息	
		waitMsg.show();
		// 发起请求查询当前页面的帮助信息
		Ext.Ajax.request({
					url : revokeMatrix,
					method : 'POST',	
					params : {
						matrixFlg : 3 // 检查间隔矩阵
					},
					success : function(response, action) {
						waitMsg.hide();
						var responseObj = Ext.util.JSON
								.decode(response.responseText);
						if (responseObj.revokeFlg == false) {
							// 提示存在分析数据
							alert('当前矩阵存在分析数据，不能更新！');
						} else {
						  window.location.reload(true);	
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert(commonality_cautionMsg);
					}
				});
    }
    
     /**
      * 撤销第一矩阵
      */
     function revokeFirstMatrixEvent(){
    	// 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});
		// 显示等待消息	
		waitMsg.show();
		// 发起请求查询当前页面的帮助信息
		Ext.Ajax.request({
					url : revokeMatrix,
					method : 'POST',	
					params : {
						matrixFlg : 1 // 第一矩阵
					},
					success : function(response, action) {
						waitMsg.hide();
						var responseObj = Ext.util.JSON
								.decode(response.responseText);
						if (responseObj.revokeFlg == false) {
							// 提示存在分析数据
							alert('当前矩阵存在分析数据，不能更新！');
						} else {
						  window.location.reload(true);	
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert(commonality_cautionMsg);
					}
				});
    }
})
