Ext.namespace('standardRegionParam');
// 初始化画面
var initPage = contextPath + "/paramDefineManage/standardRegionParam/treeLoad.do";
// 保存主矩阵项目
var saveItem = contextPath + "/paramDefineManage/standardRegionParam/saveMatrixTree.do";
// 保存主矩阵级别
var saveLevel = contextPath + "/paramDefineManage/standardRegionParam/saveLevel.do?type=ZA5";
// 删除主矩阵项目
var delItem = contextPath + "/paramDefineManage/standardRegionParam/delItemNode.do";
// 删除主矩阵级别
var delLevel = contextPath + "/paramDefineManage/standardRegionParam/delLevelNode.do?type=ZA5";
// 确认主矩阵自定义完成
var confirmMainMatrix = contextPath + "/paramDefineManage/standardRegionParam/confirmMainMatrix.do";
// 确认第一副矩阵自定义完成
var confirmFirstMatrix = contextPath + "/paramDefineManage/standardRegionParam/confirmFristMatrix.do";
// 确认第二副矩阵自定义完成
var confirmSecondMatrix = contextPath + "/paramDefineManage/standardRegionParam/confirmSecondMatrix.do";
// 确认检查间隔矩阵自定义完成
var confirmFinalMatrix = contextPath + "/paramDefineManage/standardRegionParam/confirmFinalMatrix.do";
// 撤销矩阵 
var revokeMatrix = contextPath + "/paramDefineManage/standardRegionParam/revokeMatrix.do";

Ext.onReady(function() { 
	
	Ext.QuickTips.init();// 初始化显示提示信息。没有它提示信息出不来。  
    Ext.form.Field.prototype.msgTarget = "qtip";
	
	// 公共方法,共用的树的根节点
	var root = new Ext.tree.AsyncTreeNode({
				text : '根节点',
				id : '00'
			});
			
	// 主矩阵树数据源		
	treeLoader = new Ext.tree.TreeLoader({
							dataUrl : initPage
						});	
	// 在树节点加载完后再设置主矩阵部分是否可用					
    /*treeLoader.on('load',function(node,response){
    		treeOuterPanel.setDisabled(TREE_DISABLED);			
    })*/
	// 主矩阵树		
	var itemTree = new Ext.tree.TreePanel({
				loader : treeLoader,
				root : root,
				autoScroll : false,
				animate : false,
				useArrows : false,
				border : false,				
				autoScroll : true,
				region: 'center',
				tbar : ["->", {
					id : 'treeConfirm',
					text : commonality_affirm,					
					xtype : "button",
					iconCls : "icon_gif_accept",
					disabled : TREE_DISABLED,
					handler : function() {
						// 执行确认主矩阵自定义完成
						confirmEvent(confirmMainMatrixEvent,'确认主矩阵自定义完成?');
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
		        // 当前主矩阵数据已经确认过后则不可修改
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
				
				if (node.id == 00){// 根节点时
					Ext.getCmp('updateItem').hide();
					Ext.getCmp('delItem').hide();				
					Ext.getCmp('addParameter').hide();
					Ext.getCmp('updateParameter').hide();
					Ext.getCmp('delParameter').hide();
				}else if (node.attributes.levelValue == undefined){// 触发当前事件的节点是项目节点时
					// 当前项目节点存在级别节点或者是第四级节点时只能追加级别节点
					if (node.attributes.hasLevel || node.getDepth() == 4){
						// 隐藏追加项目选项
						Ext.getCmp('addItem').hide();						
					}else if(!node.isLeaf()){// 非叶节点时
						Ext.getCmp('addParameter').hide();
					}										
					// 隐藏修改级别选项
					Ext.getCmp('updateParameter').hide();
					// 隐藏删除级别选项
				    Ext.getCmp('delParameter').hide();
				}else{// 触发当前事件的节点是级别节点时
					// 隐藏追加项目选项
					Ext.getCmp('addItem').hide();
					// 隐藏修改项目选项
					Ext.getCmp('updateItem').hide();
					// 隐藏删除项目选项
					Ext.getCmp('delItem').hide();
					// 隐藏追加级别选项
					Ext.getCmp('addParameter').hide();
				}
			
				contextMenu.showAt(e.getXY());
			});
		


	/*
	 * 显示项目窗体
	 * flg是用来初始化算法下拉框的显示属性
	 */ 
	function returnItemWin(flg) {	
		
		var algorithmCombo = new Ext.form.ComboBox({
					xtype : "combo",
					name : 'za5_level1_algorithm',
					id : 'za5_level1_algorithm',
					fieldLabel : '算法' + commonality_mustInput,
					valueField : "retrunValue",
					displayField : "displayText",
					mode : 'local',
					triggerAction : 'all',
					hideLabel : flg,
					hidden : flg,
					anchor : '95%',
					editable : false,
					listWidth : 260,
					store : new Ext.data.SimpleStore({
								fields : ["retrunValue", "displayText"],
								data : [[MAX, commonality_max],
										[MIN, commonality_min]]
							})
				})
		
		/**
		 * 追加/修改项目的formPanel
		 */
		var addItemFormPanel = new Ext.form.FormPanel({
					id : 'addItemFormPanel',
					name : 'addItemFormPanel',
					defaultType : 'textfield',
					labelWidth : 100,
					labelAlign : 'right',
					frame : false,
					bodyStyle : "padding:10px 10px",
					items : [{
								fieldLabel : '项目名' + commonality_mustInput,
								name : 'za5_item_nm_cn',
								allowBlank : false,
								anchor : '99%',
								maxLength : 50,
								maxLengthText : commonality_MaxLengthText + 50
							}, algorithmCombo,{// 隐藏域用于存放当前节点级别
								name : 'za5_level',
								hidden : true,
								anchor : '99%'
							}, {// 隐藏域用于存放当前节点是否是叶节点
								name : 'za5_is_leafnode',
								hidden : true,
								anchor : '99%'
							}, {// 隐藏域用于存放当前项目id
								name : 'itemId',
								hidden : true
							}, {// 隐藏域用于存放当前项目排序号
								name : 'item_code',
								hidden : true
							}, {// 隐藏域用于存放当前节点父节点编号
								name : 'za5_parent_id',
								hidden : true
							}]
				});

		var addItemWindow = new Ext.Window({
			        id : 'addItemWindow',
					layout : 'fit',
					width : 420,
					height : 285,
					resizable : false,
					draggable : true,
					//closeAction : 'hide',
					title : '<span class="commoncss">' + '维护项目'
							+ '</span>',
					// iconCls : 'page_addIcon',
					modal : true,
					//collapsible : true,
					titleCollapse : true,
					closable : true,
					maximizable : false,
					buttonAlign : 'right',
					border : false,
					animCollapse : true,
//					pageY : 20,
//					pageX : document.body.clientWidth / 2 - 420 / 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addItemFormPanel],
					buttons : [{
								text : commonality_save,
								handler : function() {
									saveRecord();
								}
							}, {
								text : commonality_reset,
								id : 'btnReset',
								handler : function() {
									addItemFormPanel.getForm().getEl().dom
											.reset();
								}
							}, {
								text : commonality_close,
								handler : function() {
									addItemWindow.close();
								}
							}]
				});

		return addItemWindow;
	}		
	
	/**
	 * 显示级别窗体
	 */
	function returnLevelWin(){	
	/*
	 * 追加/修改级别的formpanel
	 */
	var addLevelFormPanel = new Ext.form.FormPanel({
				id : 'addLevelFormPanel',
				name : 'addLevelFormPanel',
				labelWidth : 100,
				labelAlign : 'right',
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
					title : '<span class="commoncss">' + '维护级别' + '</span>',
					// iconCls : 'page_addIcon',
					modal : true,
					//collapsible : true,
					titleCollapse : true,
					closable : true,
					maximizable : false,
					buttonAlign : 'right',
					border : false,
					animCollapse : true,
//					pageY : 20,
//					pageX : document.body.clientWidth / 2 - 420 / 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addLevelFormPanel],
					buttons : [{
								text : commonality_save,
								handler : function() {
									saveLevelRecord();
								}
							}, {
								text : commonality_reset,
								id : 'btnReset',
								handler : function() {
									addLevelFormPanel.getForm().getEl().dom.reset();
								}
							}, {
								text : commonality_close,
								handler : function() {
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
				width : 240,
				items : [{
							title : '主矩阵',
							layout :'border',
							//	 iconCls : 'layout_contentIcon',
							tools : [{
										id : 'refresh',
										handler : function() {
											itemTree.root.reload()
										}
									}],
							collapsible : false,
							width : 240,
							minSize : 160,
							id : "treepanel",
							maxSize : 280,
							split : true,
							region : 'west',							
							items : [itemTree]
						}, new Ext.Panel({
									region : 'center',
									width : 240
								})]
			})		
    	
	/**
	 * 第一矩阵panel
	 */		
	var firstMatrixPanel = new Ext.Panel({
				region : 'center',
				layout : 'fit',
				width : 300,
				autoScroll : true,
				html : FIRST_MATRIX_HTML,
				// disabled : FIRST_MATRIX_DISABLED,
				title : '第一副矩阵',
				tbar : ["->", {
							id : "fmBack",
							text : commonality_back,
							xtype : "button",
							iconCls : "icon_gif_back",
							disabled : FIRST_MATRIX_DISABLED,
							handler : function(){
								confirmEvent(revokeFirstMatrixEvent,commonality_confirmRevoke);
							}
						}, {
							id : "fmConfirm",
							text : commonality_affirm,
							xtype : "button",
							iconCls : "icon_gif_accept",
							disabled : FIRST_MATRIX_DISABLED,
							handler : function() {
								// 执行确认第一副矩阵自定义完成
								confirmEvent(confirmFirstMatrixEvent,
										'确认第一副阵自定义完成?');
							}
						}]
			})      	
    
    /**
     * 第二矩阵panel
     */
    var secondMatrixPanel = new Ext.Panel({
				region : 'east',
				layout : 'fit',
				width : 500,
				autoScroll : true,
				html : SECOND_MATRIX_HTML,
				// disabled : SECOND_MATRIX_DISABLED,
				title : '第二副矩阵',
				tbar : ["->", {
							id : "smBack",
							text : commonality_back,
							xtype : "button",
							iconCls : "icon_gif_back",
							disabled : SECOND_MATRIX_DISABLED,
							handler : function(){
								confirmEvent(revokeSecondMatrixEvent,commonality_confirmRevoke);
							}
						}, {
							id : "smConfirm",
							text : commonality_affirm,
							xtype : "button",
							iconCls : "icon_gif_accept",
							disabled : SECOND_MATRIX_DISABLED,
							handler : function() {
								// 执行确认第一副矩阵自定义完成
								confirmEvent(confirmSecondMatrixEvent,
										'确认第二副阵自定义完成?');
							}
						}]
			})
    
    /**
     * 检查间隔矩阵
     */
    var finalMatrixPanel = new Ext.Panel({
				region : 'south',
				layout : 'fit',
				height : 250,
				autoScroll : true,
				html : FINAL_MATRIX_HTML,
				// disabled : FINAL_MATRIX_DISABLED,
				title : '检查间隔矩阵',
				tbar : ["->", {
							id : "finalBack",
							text : commonality_back,
							xtype : "button",
							iconCls : "icon_gif_back",
							disabled : FINAL_MATRIX_DISABLED,
							handler : function(){
								confirmEvent(revokeFinalMatrixEvent,commonality_confirmRevoke);
							}
						}, {
							id : "finalConfirm",
							text : commonality_affirm,
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
     * 矩阵部分panel
     */
    var matrixPanel = new Ext.Panel({
       region: 'center',
       layout : 'border',
       items : [secondMatrixPanel,firstMatrixPanel,finalMatrixPanel]
    })  

	/**
	 * 主窗体
	 */
	var win = new Ext.Window({
				layout : 'border',
				title : returnPageTitle('自定义标准区域参数', 'standardRegionParam'),
				renderTo : Ext.getBody(), // 呈现在 Html Body标签中
				border : false,
				resizable : true,
				closable : false,
				maximized : true,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				items : [treeOuterPanel, matrixPanel]
			});	
	win.show();
			
	/**
	 * 追加项目画面事件
	 * 
	 * @param operateFlg
	 *            操作区分 add/update
	 */
	function loadItemInit(operateFlg) {		
		// 当前选中节点
		var selectNode = itemTree.getSelectionModel().getSelectedNode();
		var thisItemWin;
		if (operateFlg == "add"){//追加状态
			var itemCode = 1;
			// 选中节点的子节点个数大于1时
			if (selectNode.childNodes.length > 0) {
				// 因为树上的项目节点是通过item_code排序的，所以childNodes中下标最大的节点item_code最大
				// 所以待追加的节点的item_code应该在此基础上+1
				var strSort = selectNode.childNodes[selectNode.childNodes.length - 1].attributes.sort;
				itemCode = parseInt(strSort) + 1;
			}
			// 触发当前追加事件的节点为根节点时
			if (selectNode.id == 00 && itemTree.getRootNode().childNodes.length == 3) {
				alert('第一层节点数量不可大于3个!');
				return;
			};
			
			// 追加第一级节点时显示算法下拉框(还有存在问题！！！)
			if (selectNode.getDepth() != 0){
				thisItemWin = returnItemWin(true);
				thisItemWin.show();
			}else{
				thisItemWin = returnItemWin(false);
				thisItemWin.show();
			}
			
			thisItemWin.find("name", "item_code")[0].setValue(itemCode);
			thisItemWin.find("name", "za5_parent_id")[0].setValue(selectNode.id);
			thisItemWin.find("name", "za5_level")[0].setValue(selectNode.getDepth());						
		}else if (operateFlg == "update"){// 修改状态			
					
			// 修改第一级项目节点时显示算法下拉框(还有存在问题！！！)			
			if (selectNode.getDepth() != 1){
				thisItemWin = returnItemWin(true);
				thisItemWin.show();
			}else{
				thisItemWin = returnItemWin(false);
				thisItemWin.show();
				// 下拉框赋选中值			
				thisItemWin.findById("za5_level1_algorithm").setValue(selectNode.attributes.itemAlgorithm,true);
			}
			thisItemWin.find("name", "itemId")[0].setValue(selectNode.id);
			thisItemWin.find("name","za5_item_nm_cn")[0].setValue(selectNode.attributes.itemNmCn);
            thisItemWin.find("name", "za5_level")[0].setValue(selectNode.getDepth());
		}
	}

	/**
	 * 追加级别画面事件
	 */
	function loadLevelInit(operateFlg) {
		
		var selectNode = itemTree.getSelectionModel().getSelectedNode();	
		
		var thisLevelWin = returnLevelWin();
		
		thisLevelWin.show();
		
		if (operateFlg == "add"){// 追加操作时
			thisLevelWin.find("name", "item_id")[0].setValue(selectNode.id);			
		}else if(operateFlg == "update"){// 修改状态
			thisLevelWin.find("name", "level_nm_cn")[0].setValue(selectNode.attributes.levelNmCn);
	
			thisLevelWin.find("name", "level_value")[0].setValue(selectNode.attributes.levelValue);
		    thisLevelWin.find("name", "item_id")[0].setValue(selectNode.parentId);
		    thisLevelWin.find("name", "level_id")[0].setValue(selectNode.id);
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
			alert('当前节点存在子节点!');
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
								alert(commonality_messageDelMsg);
								// 刷新树
								itemTree.root.reload();
							} else {
								var responseObj = Ext.util.JSON
										.decode(response.responseText);
								if (responseObj.success != undefined
										&& responseObj.success == false) {
									alert(commonality_alreadyModified);
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
						waitMsg.hide();
						if (response.responseText == "") {
							// 提示删除成功
							alert(commonality_messageDelMsg);
							// 刷新树
							itemTree.root.reload();
						} else {
							var responseObj = Ext.util.JSON
									.decode(response.responseText);
							if (responseObj.success != undefined
									&& responseObj.success == false) {
								alert(commonality_alreadyModified);
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
		var form = Ext.getCmp("addItemFormPanel");
		
		var level = form.find("name", "za5_level")[0];
		var nmCn = form.find("name", "za5_item_nm_cn")[0];
	
		
		if (nmCn.getValue() == ''){
		   alert('项目名称不能为空!');
		   return;
		}
		if (nmCn.getValue().length > nmCn.maxLength){
		   alert(nmCn.maxLengthText);
		   return;
		}
        
			
		// 一级节点算法未选择时
		if (level.getValue() == 0 && Ext.getCmp('za5_level1_algorithm').getValue() == "") {
			alert('请选择算法!');
			return;
		}

		form.getForm().submit({
					url : saveItem,
					waitTitle : commonality_waitTitle,
					params : {
						algorithm : Ext.getCmp('za5_level1_algorithm')
								.getValue()
					},
					waitMsg : commonality_waitMsg,
					success : function(form, action) {
							alert(commonality_messageSaveMsg);
							Ext.getCmp('addItemWindow').close();
							root.reload();
							itemTree.expandAll();
					},
					failure : function(form, action) {
						alert(commonality_alreadyModified);
						this.disabled = false;
					}
				})
	}
	
	/**
	 * 执行保存级别节点操作
	 */
	function saveLevelRecord() {
		var form = Ext.getCmp("addLevelFormPanel");

		var nmCn = form.find("name", "level_nm_cn")[0];
	
		
		if (nmCn.getValue() == ''){
		   alert('级别名称不能为空!');
		   return;
		}
		if (nmCn.getValue().length > nmCn.maxLength){
		   alert(nmCn.maxLengthText);
		   return;
		}
        
		
		var levelValue = form.find("name", "level_value")[0]
				.getValue();
		// 判断级别值是否为整数
		if (!checkInt(levelValue)) {
			alert('级别值不是整数!');
			return;
		}
		if (levelValue.length > 2){
		    alert('级别值长度超过2位，请重新输入!');
		    return;
		}
		form.getForm().submit({
					url : saveLevel,
					waitTitle : commonality_caution,
					waitMsg : commonality_waitMsg,
					success : function(form, action) {						
							alert('保存成功!');
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
						
						alert('保存失败!');
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
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
			});

		// 显示等待消息
		waitMsg.show();
		
		Ext.Ajax.request({
					url : confirmMainMatrix,
					method : 'POST',
					success : function(response, action) {
						if (response.responseText == '') {
							waitMsg.hide();
							return;
						}
						var responseObj = Ext.util.JSON
								.decode(response.responseText);
						waitMsg.hide();
						if (responseObj.mainMatrix != undefined
								&& responseObj.mainMatrix != false) {
							// 禁用主矩阵的确认按钮
							Ext.getCmp('treeConfirm').setDisabled(true);							
							// 禁用主矩阵的邮件菜单
							TREE_DISABLED = true;
							// 第一副矩阵的确认可用
							Ext.getCmp('fmConfirm').setDisabled(false);
							Ext.getCmp('fmBack').setDisabled(false);
							firstMatrixPanel.body
									.update(responseObj.mainMatrix);
						} else if (responseObj.success != undefined
								&& responseObj.success == false) {
							alert(commonality_alreadyModified);
						} else {
							alert('主矩阵属性结构自定义不完整,或当前数据已被他人修改，请刷新数据后再做检查!');
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('主矩阵属性结构自定义不完整,或当前数据已被他人修改，请刷新数据后再做检查!');
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
				alert('级别值必须输入!');
				return;
			}else if (thisCell.value.trim().length > 3){
				alert('级别值长度过大，请重新输入!');
				return;
			}else if (!checkInt(thisCell.value)) {
				alert('级别值不是整数!');
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
						// 第二矩阵数据存在时生成第二矩阵
						if (responseObj.secondMatrix != undefined
								&& responseObj.secondMatrix != "") {
							// firstMatrixPanel.setDisabled(true);
							// secondMatrixPanel.setDisabled(false);
							Ext.getCmp('fmConfirm').setDisabled(true);
							Ext.getCmp('fmBack').setDisabled(true);
							Ext.getCmp('smConfirm').setDisabled(false);
							Ext.getCmp('smBack').setDisabled(false);
							secondMatrixPanel.body
									.update(responseObj.secondMatrix);
						} else if (responseObj.finalMatrix != undefined
								&& responseObj.finalMatrix != "") {
							// 第二矩阵数据不存在时生成检查间隔矩阵
							// firstMatrixPanel.setDisabled(true);
							// finalMatrixPanel.setDisabled(false);
							Ext.getCmp('fmConfirm').setDisabled(true);
							Ext.getCmp('fmBack').setDisabled(true);
							Ext.getCmp('smConfirm').setDisabled(true);
							Ext.getCmp('smBack').setDisabled(true);
							Ext.getCmp('finalConfirm').setDisabled(false);
							Ext.getCmp('finalBack').setDisabled(false);
							finalMatrixPanel.body
									.update(responseObj.finalMatrix);
						} else if (responseObj.success != undefined
								&& responseObj.success == false) {
							alert(commonality_alreadyModified);
						} else {
							alert('第一副矩阵级别值定义不完整!');
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('第一副矩阵级别值定义不完整!');
					}
				});
	}

	/**
	 * 确认第二副矩阵
	 */
	function confirmSecondMatrixEvent() {
		var json = "{'root':[";
		var fmCellArray = document.getElementsByName('2fmCell');
		for (var i = 0; i < fmCellArray.length; i++) {
			var thisCell = fmCellArray[i];
			if (thisCell.value.trim() == "") {
				alert('级别值必须输入!')
				return;
			} else if (thisCell.value.trim().length > 3){
				alert('级别值长度过大，请重新输入!');
				return;
			} else if (!checkInt(thisCell.value)) {
				alert('级别值不是整数!')
				return;
			}
			var temp = "{'id' : '" + thisCell.id.replace("2fmCell", "") + "',";
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
					url : confirmSecondMatrix,
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
							Ext.getCmp('smConfirm').setDisabled(true);
							Ext.getCmp('smBack').setDisabled(true);
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
							alert('第二副矩阵级别值定义不完整!');
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('第二副矩阵级别值定义不完整!');
					}
				});
	}

    function confirmFinalMatrixEvent() {
		var json = "{'root':[";
		var fmCellArray = document.getElementsByName('fnCell');
		for (var i = 0; i < fmCellArray.length; i++) {
			var thisCell = fmCellArray[i];
			if (thisCell.value.trim() == "") {
				alert('级别值必须输入!')
				return;
			}else if (thisCell.value.trim().length >= 200){
				alert('级别值长度过大，请重新输入!');
				return;
			} /*else if (!checkInt(thisCell.value)) {
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
							alert('当前矩阵存在分析数据，不能更新!');
						} else if (responseObj.success != undefined
								&& responseObj.success == false) {
							alert(commonality_alreadyModified);
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert('检查间隔矩阵级别值定义不完整!');
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
							alert('当前矩阵存在分析数据，不能更新!');
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
							alert('当前矩阵存在分析数据，不能更新!');
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
      * 撤销第二矩阵
      */
     function revokeSecondMatrixEvent(){
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
						matrixFlg : 2 // 第二矩阵
					},
					success : function(response, action) {
						waitMsg.hide();
						var responseObj = Ext.util.JSON
								.decode(response.responseText);
						if (responseObj.revokeFlg == false) {
							// 提示存在分析数据
							alert('当前矩阵存在分析数据，不能更新!');
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
