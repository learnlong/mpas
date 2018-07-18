/**
 * 此页面用来显示文件和会议纪要的文件,可进行文件上传保存操作。
 * 
 * @authour zhouli createDate 2012-08-6
 * 
 */
Ext.namespace('comFile');
var urlPrefix = contextPath + "/com/file/";
var loadDataUrl = "getDataComFileList.do";
var loadTreeDataUrl = "jsonComDirectoryTreeLoader.do";
var deleteOrUpdateDirUrl = "jsonManDirectoryUpdate.do";
var deletefile = "SysFileDelete.do";
var uploadFileUrl = "uploadSysFile.do";
var fid = 0;
var currentRowIndex = -1;
var currentRecord;

Ext.form.Field.prototype.msgTarget = 'side'; // 用来显示验证信息
Ext.QuickTips.init();

Ext.onReady(function() {
	var gridTitle = '目录文件列表';
	var treeRoot = '文档管理';
	var dirIdClicked = 0;
	var dirNameClicked = "";

	// Ext.QuickTips.init();
	/** 目录树* */
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : treeRoot,
		id : '0'
	});
	var loader = new Ext.tree.TreeLoader({
		url : urlPrefix + loadTreeDataUrl
	});
	loader.on('beforeload', function(loader, node) {
		node.on('append', function(tree, thiz, newNode, index) {
			if (newNode.isLeaf()) {
				newNode.on('click', function(thiz, e) {
					
				});
			}
		});
		if (!node.isLeaf()) {
			loader.baseParams['cid'] = node.id;
		}
	});
	var treePanel = new Ext.tree.TreePanel({
		// rootVisible:false,
		frame : false,
		renderTo : 'treeDiv',
		width : 200,
		height : 400,
		title : '目录一览',
		header : true,
		loader : loader,
		root : rootNode,
		autoScroll : true,
		region : 'west',
		listeners : {
			"click" :function(node){
				selectGridByTree(node);
			}
		}
	});
	
	//点击树查询grid
	 function selectGridByTree(node) {
			dirIdClickedStr = "";
			dirIdClicked = node.id;
			dirNameClicked = node.text;
			baseParams = {
				fileName : '',
				start : 0,
				limit : 18
			};
			// 获取所有子节点
			node.cascade(function(node) {
				dirIdClickedStr += node.id + ",";
			});
			store.load();
		}
	 
	// 根据当前节点选中父节点及其子节点
	treePanel.on('checkchange', function(node, flag) {
		// 获取所有子节点
		node.cascade(function(node) {

		});
	});
	
	// 定义右键菜单
	var rightClick = new Ext.menu.Menu({
		id : 'rightClickCont',
		items : [{
			id : 'addNode',
			text : commonality_add,
			// 增加菜单点击事件
			menu : [{
				id : 'insertNode',
				text : '增加同级目录',
				handler : function(tree) {
					appendNodeAction(0);
				}
			}, {
				id : 'appendNode',
				text : '增加下级目录',
				handler : function(tree) {
					appendNodeAction(1);
				}
			}]
		}, '-', {
			id : 'delNode',
			text : commonality_del,
			handler : function(tree) {
				Ext.Msg.confirm(commonality_affirm, commonality_affirmDelMsg,
						function(btn) {
							if (btn == 'yes') {
								delNodeAction();
							}
						})
			}
		}, {
			id : 'modifNode',
			text : commonality_modify,
			handler : function() {
				appendNodeAction(2);
			}
		}]
	});

	// 添加点击事件
	treePanel.on('click', function(node) {
		if (node.id != 'root') {
			currentRecord=null;
			currentRowIndex=-1;
		}
	});

	// 增加右键弹出事件
	treePanel.on('contextmenu', function(node, event) {// 声明菜单类型
				//alert(node.value);
				if(node.id==undefined||""==node.id){
					return;
				}
				
				event.preventDefault();// 这行是必须的，使用preventDefault方法可防止浏览器的默认事件操作发生。
				selectGridByTree(node);
				node.select();
				rightClick.showAt(event.getXY());// 取得鼠标点击坐标，展示菜单
				if (node.id != '0') {
					Ext.getCmp('insertNode').enable();
					Ext.getCmp('modifNode').enable();
					Ext.getCmp('delNode').enable();
				} else {
					Ext.getCmp('insertNode').disable();
					Ext.getCmp('modifNode').disable();
					Ext.getCmp('delNode').disable();
				}

			});
	rootNode.expand();// 默认展开一级节点

	// 添加子节点事件实现
	// mode : 0 增加同级 1 增加下级 2 修改
	function appendNodeAction(mode) {
		var node = treePanel.getSelectionModel().getSelectedNode();
		var pNode = node.parentNode;
		if (mode == 0) {
			Ext.getCmp("parentId").setValue(pNode.id);
			Ext.getCmp("parentName").setValue(pNode.text);
			Ext.getCmp("id2").setValue("");
			Ext.getCmp("name").setValue("");
		} else if (mode == 1) {
			Ext.getCmp("parentId").setValue(node.id);
			Ext.getCmp("parentName").setValue(node.text);
			Ext.getCmp("id2").setValue("");
			Ext.getCmp("name").setValue("");
		} else if (mode == 2) {
			Ext.getCmp("parentId").setValue(pNode.id);
			Ext.getCmp("parentName").setValue(pNode.text);
			Ext.getCmp("id2").setValue(node.id);
			Ext.getCmp("name").setValue(node.text);
		}
		winAddDir.show();
	}

	// 删除节点事件实现
	function delNodeAction() {
		var selectedNode = treePanel.getSelectionModel().getSelectedNode(); // 得到选中的节点
		var processEntityId = selectedNode.id;
		Ext.Ajax.request({
			url : urlPrefix + deleteOrUpdateDirUrl,
			params : {
				method : 'delete',
				processEntityId : processEntityId
			},
			waitMsg : commonality_waitMsg,
			success : function(form, action) {
				var resultJson = Ext.util.JSON.decode(form.responseText);
				if (resultJson.msg == "isHavaChild") {
					alert('该目录下包含子文件,请重新选择 !');
				} else if (resultJson.msg == "isHaveDirectory") {
					alert('该目录下包含子目录,请重新选择 !');
				} else {
					if (resultJson.msg == "true") {
						alert(commonality_messageDelMsg);
						treePanel.root.reload();
						treePanel.root.expand(true);
					} else {
						alert(commonality_messageDelMsgFail);
					}
				}

			},
			failure : function(response) {
				alert(commonality_messageDelMsgFail);
			}
		});
	}

	var addDirForm = new Ext.FormPanel({
		header : false,
		plain : true,
		frame : true,
		height : 500,
		items : [{
			xtype : 'hidden',
			fieldLabel : '上级目录ID',
			name : 'parentId',
			id : 'parentId'
		}, {
			xtype : 'textfield',
			fieldLabel : '上级目录' + commonality_mustInput,
			name : 'parentName',
			readOnly : true,
			width : 150,
			id : 'parentName'
		}, {
			xtype : 'hidden',
			fieldLabel : '目录ID',
			name : 'id2',
			id : 'id2'
		}, {
			xtype : 'textfield',
			fieldLabel : '目录名称' + commonality_mustInput,
			name : 'name',
			width : 150,
			maxLength : 50,
			maxLengthText : commonality_MaxLengthText,
			id : 'name'
		}]
	});

	var winAddDir = new Ext.Window({
		title : '维护目录',
		maskDisabled : false,
		id : 'add-dir-window',
		modal : true, // 是否为模式窗口
		constrain : true, // 窗口只能在viewport指定的范围
		closable : true, // 窗口是否可以关闭
		closeAction : 'hide',
		layout : 'fit',
		width : 300,
		height : 150,
		draggable : false,
		plain : true,
		buttonAlign : 'center',
		items : [addDirForm],
		buttons : [{
			text : commonality_save,
			handler : function() {
				var judgeEmpty3 = 0;
				if (LTrim(Ext.getCmp("name").getValue()) == ""
						|| LTrim(Ext.getCmp("name").getValue()) == undefined) {
					alert('目录名不能为空');
					judgeEmpty3 = 1;
					return;
				}
				if (Ext.getCmp("name").getValue().length > 50) {
					var length = Ext.getCmp("name").getValue().length;
					alert('目录名称过长' + ","
							+ '最大目录长度为50,目前是 :' + length);
					judgeEmpty3 = 1;
					return;
				}
				var re = /^[-0-9a-zA-Z\u4e00-\u9fa5]+$/;
				if (!re.test(Ext.getCmp("name").getValue())) {
					alert('名称不合法 !');
					return;
				}
				if (judgeEmpty3 == 0) {
					Ext.Msg.confirm(commonality_affirm,
							commonality_affirmSaveMsg, function(btn) {
								if (btn == 'yes') {
									Ext.Ajax.request({
										url : urlPrefix + deleteOrUpdateDirUrl,
										params : {
											docType : "docManager",
											method : 'update',
											jsonData : Ext.util.JSON.encode(addDirForm.form.getValues(false))
										},
										waitMsg : commonality_waitMsg,
										success : function(response) {
											 treePanel.getSelectionModel().clearSelections();
											var text = Ext.util.JSON
													.decode(response.responseText);
											if (text.success) {
												alert(commonality_messageSaveMsg);
												dirNameClicked = Ext
														.getCmp("name")
														.getValue();
												treePanel.root.reload();
												treePanel.root.expand(true);
												store.removeAll();
											} else {
												alert('同级别下目录名已经存在');
											}

										},
										failure : function(response) {
											alert(commonality_cautionMsg);
										}
									});
									winAddDir.hide();
								}
							})
				}
			}
		}, {
			text : commonality_close,
			handler : function() {
				winAddDir.hide();
			}
		}]
	});

	

	function cellclick(grid, rowIndex, columnIndex, e) {
		currentRowIndex = rowIndex;
		currentRecord = grid.getStore().getAt(rowIndex);
		if (columnIndex == 0) {
			grid.getSelectionModel().selectAll();
		}
		fid = currentRecord.get('fileId');
	}

	var columns = [{
		id : 'fileId',
		header : '文件ID',
		dataIndex : 'fileId',
		width : 100,
		hidden : true
	}, {
		id : 'fileName',
		header : '文件名称' + commonality_mustInput,
		dataIndex : 'fileName',
		width : 130,
		maxLength : 200,
		allowBlank : false,
		sortable : true
	}, {
		id : 'appName',
		header : '附件' + commonality_mustInput,
		dataIndex : 'appName',
		width : 140
	}, {
		id : 'docId',
		header : '目录ID',
		dataIndex : 'docId',
		width : 100,
		hidden : true
	}];
	var fields = ["fileId", "fileName", "appName", "docId"];

	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : urlPrefix + loadDataUrl
		}),
		reader : new Ext.data.JsonReader({
			root : 'comFile',
			// a Record constructor
			fields : fields
		})
	});
	store.on("beforeload",function(){
		store.baseParams = {
			docType : "docManager"
		};
	});
	var grid = new Ext.grid.EditorGridPanel({
		id : 'mygrid',
		renderTo : 'dataDiv',
		store : store,
		cm : new Ext.grid.ColumnModel(columns),
		sm : new Ext.grid.RowSelectionModel({
									singleSelect : true
								}),
		title : gridTitle,
		header : true,
		height : 430,
		stripeRows : true,
		viewConfig : {
			forceFit : true
		}, // 自动分配列宽
		clicksToEdit : 2,
		frame : true,
		region : 'center',
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : commonality_turnPage,
			emptyMsg : commonality_noRecords
		}),

		tbar : new Ext.Toolbar({
			items : ['文件名称', "：", {
				xtype : 'textfield',
				id : 'fileNameQuery',
				width : 100
			},  new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"), {
						text : commonality_search,
						iconCls : "icon_gif_search",
						id : 'sericon',
						handler : function() {
							var sel=treePanel.getSelectionModel().getSelectedNode();
							if(sel==undefined){
								alert('请选择“文档管理”下的目录！');
								return;
							}
							store.load({
								params : {
									start : 0,
									limit : 18,
									fileName : Ext.get('fileNameQuery').dom.value,
									action : urlPrefix + loadDataUrl
								}
							})
						}
					}, "-", {
						text : commonality_add,
						iconCls : "icon_gif_add",
						handler : function() {
							var sel=treePanel.getSelectionModel().getSelectedNode();
							if(sel==undefined){
								alert('请选择“文档管理”下的目录！');
								return;
							}
							processDocument(dirIdClicked, dirNameClicked, currentRecord, store);
						}
					}, '-', {
						text : commonality_modify,
						iconCls : "icon_gif_edit",
						waitMsg : commonality_waitMsg,
						handler : function() {
							var sel=treePanel.getSelectionModel().getSelectedNode();
							if(sel==undefined){
								alert('请选择“文档管理”下的目录！');
								return;
							}
							currentRecord = Ext.getCmp("mygrid").getStore().getAt(currentRowIndex);
							if ( currentRecord == null||currentRowIndex==undefined) {
								alert('请先点击要操作的文件');
								return;
							}
							updateProcessDocument(dirIdClicked, dirNameClicked, currentRecord, store);
						}
					}, '-', {
						text : commonality_del,
						iconCls : "icon_gif_delete",
						waitMsg : commonality_waitMsg,
						handler : function() {
							var sel=treePanel.getSelectionModel().getSelectedNode();
							if(sel==undefined){
								alert('请选择“文档管理”下的目录！');
								return;
							}
							currentRecord = Ext.getCmp("mygrid").getStore().getAt(currentRowIndex);
							if ( currentRecord == null||currentRowIndex==undefined) {
								alert('请先点击要操作的文件');
								return;
							}
							Ext.Msg.confirm(commonality_affirm,
									commonality_affirmDelMsg, function(btn) {
										if (btn == 'yes') {
											deletefileData(currentRecord, store);
										}
									});
						}
					}]
		})
	});

	grid.render();
	grid.addListener("rowclick", cellclick);

	store.on('beforeload', function() {
		Ext.apply(this.baseParams, baseParams);
		store.baseParams['cid'] = dirIdClicked;
	});

	store.load();

	var panel = new Ext.Window({
		title : returnPageTitle('文档管理', 'fileManage'),
		width : gridWidth,
		height : 470,
		layout : 'border',
		closable : false,
		// draggable:false,
		maximized : true,
		// draggable:false,
		plain : true,
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		items : [{
			region : 'west',
			layout : 'fit',
			split : true,
			width : 250,
			height : 450,
			items : [treePanel]
		}, {
			region : 'center',
			layout : 'fit',
			split : true,
			height : 450,
			items : [grid]
		}]
	});
	panel.show();

	/* 删除按钮 */
	function deletefileData(currentRecord, store) {
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true
				// 完成后移除
				});
		waitMsg.show();
		// 执行删除操作
		Ext.Ajax.request({
			url : urlPrefix + deletefile,
			params : {
				method : 'delete',
				processEntityId : currentRecord.get('fileId')
			},
			waitMsg : commonality_waitMsg,
			success : function(response) {
				var text = Ext.util.JSON.decode(response.responseText);
				waitMsg.hide();
				if (text.success) {
					alert(commonality_messageDelMsg);
					store.reload();
					currentRowIndex=-1;
				} else {
					alert(commonality_messageDelMsgFail);
				}
			},
			failure : function(response) {
				waitMsg.hide();
				alert(commonality_messageGuZhang);
			}
		});
	}
	
	var fieldDatas = [{
		fieldLabel : '目录ID',
		name : 'cid',
		id : 'cid',
		xtype : 'hidden',
		allowBlank : false,
		anchor : '90%'
	}, {
		fieldLabel : '目录' + commonality_mustInput,
		name : 'dir',
		id : 'dir',
		allowBlank : false,
		anchor : '90%'
	}, {
		fieldLabel : '文件ID',
		name : 'processEntityId',
		id : 'processEntityId',
		xtype : 'hidden',
		anchor : '90%'
	}, {
		fieldLabel : '文件名称' + commonality_mustInput,
		name : 'fileName',
		id : 'fileName',
		allowBlank : false,
		maxLength : 200,
		maxLengthText : '输入信息超过200长度 !',
		anchor : '90%'
	}, {
		fieldLabel : '上传附件' + commonality_mustInput,
		inputType : 'file',
		name : 'docFile',
		id : 'docFile',
		anchor : '90%'
	}, {
		fieldLabel : '是否选择了文件',
		name : 'isChooseFile',
		id : 'isChooseFile',
		xtype : 'hidden',
		anchor : '90%'
	}, {
		xtype : 'panel',
		layout : 'column',
		anchor : '98%',
		fieldLabel : '已上传文件',
		items : [{
			xtype : 'label',
			name : 'fileUrl',
			text : '已上传文件',
			width : '90%',
			id : 'fileUrl'
		}]
	}];

	var addOrUpdateFileForm = new Ext.form.FormPanel({
		labelWidth : 80,
		defaultType : 'textfield',
		fileUpload : true,
		labelAlign : 'right',
		method : 'POST',
		enctype : 'multipart/form-data',
		frame : true,
		items : [fieldDatas],
		buttonAlign : 'center',
		buttons : [{
			text : commonality_save,
			id : 'saveBtn',
			handler : function() {
				var validateFileName = Ext.getCmp("fileName").getValue();
				if (!addOrUpdateFileForm.getForm().isValid()) {
					if (Length(validateFileName) > 200) {//检查文件名称长度
						alert('文件名过长');
					} else if (LTrim(validateFileName) == "" || LTrim(validateFileName) == undefined) {// 验证文件名称是否为空
						alert('文件名不能为空');
					}
					return;
				}

				var re = /^[0-9a-zA-Z\u4e00-\u9fa5]+$/;
				if (!re.test(Ext.getCmp("fileName").getValue())) {
					alert('文件名不合法 !');
					return;
				}
				
				//如果是新增，一点要选择文件
				var strProcessEntityId = Ext.getCmp('processEntityId').getValue();
				if(strProcessEntityId == null || strProcessEntityId == ''){
					if(Ext.getCmp('docFile').getValue() == null || Ext.getCmp('docFile').getValue() == ''){
						alert('请选择上传附件');
						return;
					}
				}
				Ext.Msg.confirm(commonality_affirm, commonality_affirmSaveMsg,
						function(btn) {
							if (btn == 'yes') {
								//input type=flie是setValue('')在后台不生效，转用isChooseFile判断是否上传了文件
								if(Ext.getCmp('docFile').getValue() == null || Ext.getCmp('docFile').getValue() == ''){
									Ext.getCmp('isChooseFile').setValue("0");
								}else{
									Ext.getCmp('isChooseFile').setValue("1");
								}
								addOrUpdateFileForm.form.submit({
									url : urlPrefix + uploadFileUrl,
									method : 'post',
									params :{
										docType:"docManager"
									},
									waitMsg : commonality_waitMsg,
									success : function(form, action) {
										//alert(commonality_messageSaveMsg);
										addOrUpdateFileForm.form.getEl().dom.reset();
										addOrUpdateFileWin.hide();
										store.load({
											params : {
												start : 0,
												limit : 18
											}
										});
									},
									failure : function(form, action) {
										var msg = action.result.msg // commonality_messageFailMsg;
										if (msg == 'uploadFile') {
											alert('上传的附件大小不能超过10M！');
										} else if (msg == 'specifyFile') {
											alert('请指定要上传的附件！');
										} else if (msg == 'duplicatedNaming') {
											alert('不能重复命名');
										} else if (msg == 'fileNameSoLong') {
											alert('附件名太长');
										} else if (msg == 'fileBugName') {
											alert('附件名后缀只能是pdf|doc|docx|txt');
										} else if (msg == 'fileSoSmall') {
											alert('附件大小为0KB，不可以上传 !');
										} else {
											addOrUpdateFileWin.hide();
										}
									}
								});
							}
						})
			}
		}, {
			text : commonality_close,
			handler : function() {
				addOrUpdateFileWin.hide();
			}
		}]
	});

	var addOrUpdateFileWin = new Ext.Window({
		title : '增加文件',
		width : 400,
		y : 15,
		autoHeight : true,
		draggable : false,
		resizable : false,
		closeAction:'hide',
		// bodyStyle : 'padding: 5px;',
		items : [addOrUpdateFileForm],
		modal : true
	});
	
	/* 新增按钮 */
	function processDocument(dirIdClicked, dirNameClicked, currentRecord, store) {
		/* 增加文件 */
		if (dirIdClicked < 1) {
			alert('请选择“文档管理”下的目录！');
			return;
		}
		addOrUpdateFileWin.setTitle('增加文件');
		addOrUpdateFileWin.show();
		addOrUpdateFileForm.form.getEl().dom.reset();
		Ext.getCmp('cid').setValue(dirIdClicked);
		Ext.getCmp('dir').setValue(dirNameClicked);
		Ext.getCmp('dir').readOnly = true;
		Ext.getCmp('processEntityId').setValue(null);
		Ext.getCmp('fileName').setValue(null);
		Ext.getCmp('docFile').setValue(null);
		Ext.getCmp('fileUrl').getEl().update('');

	}
	/* 修改按钮 */
	function updateProcessDocument(dirIdClicked, dirNameClicked, currentRecord, store) {
		// Ext.getCmp("mygrid");
		/* 修改文件 */
		currentRecord = Ext.getCmp("mygrid").getStore().getAt(currentRowIndex);
		addOrUpdateFileWin.setTitle('修改文件');
		addOrUpdateFileWin.show();
		addOrUpdateFileForm.form.getEl().dom.reset();
		Ext.getCmp('cid').setValue(currentRecord.get('docId'));
		Ext.getCmp('dir').setValue(dirNameClicked);
		Ext.getCmp('dir').readOnly = true;
		Ext.getCmp('processEntityId').setValue(currentRecord.get('fileId'));
		Ext.getCmp('fileName').setValue(currentRecord.get('fileName'));
		Ext.getCmp('docFile').setValue(null);
		Ext.getCmp('fileUrl').getEl().update(currentRecord.get('appName'));
	}
	
});

// 去左空格
function LTrim(str) {
	return str.replace(/^\s*/g, "");
}
// 檢查文本框輸入字符串的長度
function Length(remark) {
	return remark.replace(/[^\x00-\xff]/g, 'AA').length;
}

