Ext.namespace('mrb');
var addSectionWindow = null;
var addMpdPsWindow = null;
var tempValue = "";
mrb.app = function() {
	return {
		init : function() {

			Ext.QuickTips.init();

			var root = new Ext.tree.AsyncTreeNode({
				text : 'MRB目录',
				expanded : true,
				id : '0'
			});

			var menuTree = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
					dataUrl : mrb.app.loadTree
				}),
				root : root,
				rootVisible : true,
				autoScroll : false,
				animate : true,
				useArrows : false,
				border : false,
				listeners : {
					contextmenu : function(node, e) {
						e.preventDefault();
						menuid = node.attributes.id;
						menuname = node.attributes.text;
						node.select();
						var depth = node.getDepth();
						if (depth == 0) {
							Ext.getCmp('addSection').hide();
							Ext.getCmp('modifySection').hide();
							Ext.getCmp('deleteSection').hide();
						} else if (depth === 1) {
							Ext.getCmp('addSection').show();
							Ext.getCmp('modifySection').hide();
							Ext.getCmp('deleteSection').hide();

							node.expand();
							contextMenu.showAt(e.getXY());
						} else if (depth === 2) {
							Ext.getCmp('addSection').hide();
							Ext.getCmp('modifySection').show();
							Ext.getCmp('deleteSection').show();

							node.expand();
							contextMenu.showAt(e.getXY());
						}
					},
					click : function(node, e) {
						e.preventDefault();
						menuid = node.id;
						// var editor = null;
						// var btnSubmit = null;
						// if(language === 'Cn'){
						// editor = KE.app.getEditor("contentCn");
						// btnSubmit = Ext.getCmp("btnEditContentCn");
						// }else{
						// editor = KE.app.getEditor("contentEn");
						// btnSubmit = Ext.getCmp("btnEditContentEn");
						// }
						var editorCn = KE.app.getEditor("contentCn");
					
						var btnSubmitCn = Ext.getCmp("btnEditContentCn");
					
						// 点击了根节点
						if (menuid === '0') {
							Ext.getCmp("editContentPanel").setTitle(mrb.app.mpdPdfButton('内容编辑'));
							btnSubmitCn.disable();
						
							editorCn.html("");
						
							editorCn.readonly();
						
							return;
						}
						Ext.getCmp("editContentPanel").setTitle(mrb.app.mpdPdfButton('内容编辑'+ "(" + node.text + ")"));
						var id = menuid.substring(1);
						var nodeType = menuid.substr(0, 1);
						var isChapter = (nodeType === "c" || nodeType === "t");
						if (nodeType === "t") {
							// 数据库中还没有这个章
							editorCn.html("");
							
							// if(language === 'Cn'){
							contentFormPanelCn.findById("chapterId")
									.setValue(menuid);
							contentFormPanelCn.findById("sectionId")
									.setValue("");
							// }else{
							
							// }
							btnSubmitCn.enable();
					
							editorCn.readonly(false);
					
						} else {
							Ext.Ajax.request({
								url : isChapter
										? mrb.app.loadChapter
										: mrb.app.loadSection,
								params : {
									chapterId : menuid,
									sectionId : menuid
								},
								success : function(res) {
									res = Ext.util.JSON
											.decode(res.responseText);
									var contentCn = res.contentCn ? res.contentCn : "";
								
									// tempValue =
									// content.replace(/\r|\n|\t/g,"");;
									if (editorCn != null
											&& editorCn != 'undefined') {
										editorCn.html(contentCn);
										editorCn.readonly(false);
									}
								
								
									if (isChapter) {
										contentFormPanelCn
												.findById("chapterId")
												.setValue(id);
										contentFormPanelCn
												.findById("sectionId")
												.setValue("");
									} else {
										contentFormPanelCn
												.findById("chapterId")
												.setValue(node.parentNode.id
														.substring(1));
										contentFormPanelCn
												.findById("sectionId")
												.setValue(id);
									}
									
									btnSubmitCn.enable();
								
								},
								failure : function() {
									alert('加载数据失败！');
								}
							});
						}
					}
				}
			});
			menuTree.expandAll();

			// MRB右键菜单
			var contextMenu = new Ext.menu.Menu({
				id : 'menuTreeContextMenu',
				items : [{
					text : '新增节',
					iconCls : 'page_addIcon',
					id : 'addSection',
					handler : function() {
						if (addSectionWindow != null) {
							addSectionFormPanel.getForm().reset();
						}
						addSectionFormPanel.findById("section_chapterId")
								.setValue("");
						addSectionFormPanel.findById("chapter_NameCn")
								.setValue("");
						addSectionFormPanel.findById("section_sectionId")
								.setValue("");
						addSectionFormPanel.findById("sectionCode")
								.setValue("");
						addSectionFormPanel.findById("sectionNameCn")
								.setValue("");
						

						sectionWindow('新增节').show();
						var node = menuTree.getSelectionModel()
								.getSelectedNode();
						addSectionFormPanel.findById("chapter_NameCn")
								.setValue(node.text);
						addSectionFormPanel.findById("section_chapterId")
								.setValue(node.id);
					}
				}, {
					text : '修改节',
					iconCls : 'page_addIcon',
					id : 'modifySection',
					handler : function() {
						var node = menuTree.getSelectionModel()
								.getSelectedNode();
						if (addSectionWindow != null) {
							addSectionFormPanel.getForm().reset();
						}
						sectionWindow('修改节');
						addSectionWindow.show();
						Ext.Ajax.request({
							url : mrb.app.loadSection,
							params : {
								chapterId : node.parentNode,
								sectionId : node.id
							},
							success : function(res) {
								res = Ext.util.JSON.decode(res.responseText);
								addSectionFormPanel
										.findById("section_chapterId")
										.setValue(node.parentNode.id);
								addSectionFormPanel.findById("chapter_NameCn")
										.setValue(node.parentNode.text);
								addSectionFormPanel
										.findById("section_sectionId")
										.setValue(node.id);
								addSectionFormPanel.findById("sectionCode")
										.setValue(res.code);
								addSectionFormPanel.findById("sectionNameCn")
										.setValue(res.nameCn);
								
							},
							failure : function() {
								alert('加载数据失败！');
							}
						});
					}
				}, {
					text : commonality_del,// 删除
					id : "deleteSection",
					iconCls : 'page_delIcon',
					handler : function() {
						var selectModel = menuTree.getSelectionModel();
						var selectNode = selectModel.getSelectedNode();
						if (selectNode.hasChildNodes()) {
							alert('该节点下具有多个子节点，不能删除！');// "该节点下具有多个子节点，不能删除！"
							return false;
						}
						var empty = 0;

						var depth = selectNode.getDepth();
						if (depth === 2) {
							var id = selectNode.attributes.id.substring(1);
							var panelId;
							
								panelId = contentFormPanelCn
										.findById("sectionId").getValue();
							
							if (id === panelId) {
								empty = 1;
							}
						}
						mrb.app.confirm({
							msg : commonality_affirmDelMsg,// 确认要删除吗？
							event : function() {
								Ext.Msg.wait(commonality_waitMsg,
										commonality_waitTitle);
								Ext.Ajax.request({
									url : selectNode.leaf
											? mrb.app.deleteSection
											: mrb.app.deleteChapter,
									params : {
										sectionId : selectNode.id.substring(1),
										chapterId : selectNode.id.substring(1)
									},
									success : function() {
										var btnSubmit = null;
										var editor = null;
										
											editor = KE.app
													.getEditor("contentCn");
											btnSubmit = Ext
													.getCmp("btnEditContentCn");
										
										if (empty === 1) {
											Ext
													.getCmp("editContentPanel")
													.setTitle(mrb.app
															.mpdPdfButton('内容编辑'));
											btnSubmit.disable();
											editor.html("");
											editor.readonly();
										}
										Ext.Msg.hide();
										menuTree.root.reload();
										menuTree.expandAll();
										alert(commonality_messageDelMsg);
									},
									failure : function() {

									}
								});
							}
						});
					}
				}]
			});

			// MRB附录树根节点
			var mpdPsRoot = new Ext.tree.AsyncTreeNode({
				text : 'MRB附录',
				expanded : true,
				id : '0'
			});
			// MRB附录树
			var mpdPsTree = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
					dataUrl : mrb.app.loadMrbPsTree
				}),
				root : mpdPsRoot,
				rootVisible : true,
				autoScroll : false,
				animate : true,
				useArrows : false,
				border : false,
				listeners : {
					contextmenu : function(node, e) {
						e.preventDefault();
						node.select();
						if (node.leaf) {
							Ext.getCmp("uploadPs").hide();
							Ext.getCmp("deletePs").show();
							Ext.getCmp("modifyPs").show();
						} else {
							Ext.getCmp("uploadPs").show();
							Ext.getCmp("deletePs").hide();
							Ext.getCmp("modifyPs").hide();
						}
						contextMpdPsMenu.showAt(e.getXY());
					}
				}
			});
			mpdPsTree.expandAll();
			// MPD附录右键菜单
			var contextMpdPsMenu = new Ext.menu.Menu({
				id : "contextMpdPsMenu",
				items : [{
					id : 'uploadPs',
					text : '上传附录',// 上传附录
					iconCls : 'page_refreshIcon',
					handler : function() {
						if (addMpdPsWindow != null) {
							addMrbPsFormPanel.getForm().reset();
							// var pdfFile = document.getElementById("pdfFile");
							// pdfFile.outerHTML=pdfFile.outerHTML;
						}
						addMrbPsFormPanel.findById("psId").setValue("");
						addMrbPsFormPanel.findById("psSort").setValue("");
						addMrbPsFormPanel.findById("psNameCn").setValue("");
						

						mrbPsWindow('上传附录').show();
					}
				}, {
					id : "modifyPs",
					text : '修改附录',// '修改附录',
					iconCls : 'page_refreshIcon',
					handler : function() {
						var node = mpdPsTree.getSelectionModel()
								.getSelectedNode();
						if (addMpdPsWindow != null) {
							addMrbPsFormPanel.getForm().reset();
							// var pdfFile = document.getElementById("pdfFile");
							// pdfFile.outerHTML=pdfFile.outerHTML;
						}
						mrbPsWindow('修改附录').show();
						Ext.Ajax.request({
							url : mrb.app.loadMrbPs,
							params : {
								psId : node.id.substring(2)
							},
							success : function(res) {
								res = Ext.util.JSON.decode(res.responseText);
								addMrbPsFormPanel.findById("psId")
										.setValue(res.psId);
								addMrbPsFormPanel.findById("psSort")
										.setValue(res.psSort);
								addMrbPsFormPanel.findById("psNameCn")
										.setValue(res.psNameCn);
								
								addMrbPsFormPanel.findById("psFlgGroup")
										.eachItem(function(item) {
											item.setValue(item.inputValue == res.psFlg);
										});
							},
							failure : function() {
								alert('加载数据失败！');
							}
						});
					}
				}, {
					id : 'deletePs',
					text : commonality_del,// 删除
					iconCls : 'page_refreshIcon',
					handler : function() {
						var node = mpdPsTree.getSelectionModel()
								.getSelectedNode();
						mrb.app.confirm({
							msg : commonality_affirmDelMsg,// "确认删除吗?",
							event : function(id) {
								Ext.Msg.wait(commonality_waitMsg,
										commonality_waitTitle);
								Ext.Ajax.request({
									url : mrb.app.deleteMrbPs,
									params : {
										psId : node.id.substring(2)
									},
									success : function() {
										Ext.Msg.hide();
										mpdPsTree.root.reload();
										mpdPsTree.expandAll();
										alert(commonality_messageDelMsg);
									},
									failure : function() {

									}
								});
							}
						});
					}
				}]
			});
			// menuTree.expandAll();
			var tempPanel = new Ext.Panel({
				layout : 'fit',
				bodyStyle : "border:0px;padding:0px",
				items : [{
					title : '  ',
					tools : [{
						id : 'refresh',
						handler : function() {
							menuTree.root.reload();
							menuTree.expandAll();
						}
					}],
					collapsible : false,
					id : "treepanel",
					split : true,
					region : 'center',
					autoScroll : true,
					bodyStyle : "border:0px;padding:0px",
					items : [menuTree]
				}]
			});
			var tempMpdPsPanel = new Ext.Panel({
				layout : 'fit',
				bodyStyle : "border:0px;padding:0px",
				items : [{
					title : '  ',
					tools : [{
						id : 'refresh',
						handler : function() {
							mpdPsTree.root.reload();
							mpdPsTree.expandAll();
						}
					}],
					collapsible : false,
					id : "pstreepanel",
					split : true,
					region : 'center',
					autoScroll : true,
					bodyStyle : "border:0px;padding:0px",
					items : [mpdPsTree]
				}]
			});

			/** 内容* */
			var documentHeight = document.documentElement.clientHeight - 130;
			var contentFormPanelCn = new Ext.form.FormPanel({
				id : 'contentFormPanelCn',
				title : '中文', // '中文',
				layout : 'fit',
				bodyStyle : "border:0px;padding:0px",
				defaultType : 'textfield',
				items : [{
					xtype : 'textarea',
					id : 'contentCn'
				}, {
					name : 'sectionId',
					id : 'sectionId',
					hidden : true
				}, {
					name : 'chapterId',
					id : 'chapterId',
					hidden : true
				}, {
					name : 'chapterContentCn',
					id : 'chapterContentCn',
					hidden : true
				}, {
					name : 'sectionContentCn',
					id : 'sectionContentCn',
					hidden : true
				}],
				listeners : {
					'render' : function() {
						KE.app.init({
							renderTo : "contentCn",
							delayTime : 1,
							resizeType : 0,
							width : '100%',
							height : documentHeight,
							minChangeSize : 20,
							imageTabIndex : 1,
							uploadJson : mrb.app.uploadImg,
							readonlyMode : true
								// ,
								});
					}
				},
				buttons : [{
					text : commonality_save,// 保存
					id : "btnEditContentCn",
					handler : function() {
						saveContent();
					}
				}],
				buttonAlign : 'center'
			});
			
			
			function saveContent() {
				var htmlCn = KE.app.getEditor('contentCn').html();// 取值
			
				/*
				 * if(htmlCn.length > maxRichTextLength){
				 * alert(mpdLang.cn_length_limit); return false; }
				 * if(htmlEn.length > maxRichTextLength){
				 * alert(mpdLang.en_length_limit);return false; }
				 */
				if (!isRichTextValid(htmlCn)) {
					alert('中文长度超过限制');
					return false;
				}
				
				var node = menuTree.getSelectionModel().getSelectedNode();
				var id = node.id;
				var isChapter = true;
				contentFormPanelCn.findById("chapterContentCn")
						.setValue(htmlCn);
				
				contentFormPanelCn.findById("sectionContentCn")
						.setValue(htmlCn);
				
				if (id.charAt(0) == 't' || id.charAt(0) == 'c') {
					contentFormPanelCn.findById("chapterId").setValue(id);
					
				} else {
					var chapterId = node.parentNode.id;
					contentFormPanelCn.findById("chapterId")
							.setValue(chapterId);
				
					contentFormPanelCn.findById("sectionId").setValue(id);
				
					isChapter = false;
				}
				var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					msg : commonality_waitMsg,
					removeMask : true
						// 完成后移除
						});
				waitMsg.show();
				Ext.Ajax.request({
					url : isChapter
							? mrb.app.updateChapter
							: mrb.app.updateSection,
					method : 'post',
					params : {
						sectionId : contentFormPanelCn.findById("sectionId")
								.getValue(),
						chapterId : contentFormPanelCn.findById("chapterId")
								.getValue(),
						chapterContentCn : contentFormPanelCn
								.findById("chapterContentCn").getValue(),
						
						sectionContentCn : contentFormPanelCn
								.findById("sectionContentCn").getValue(),
						
					},
					success : function(response, action) {
						waitMsg.hide();
						alert(commonality_messageSaveMsg);
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert(commonality_cautionMsg);
					}
				});
			}
			/** 内容结束* */

			var mainWin = new Ext.Window({
				layout : 'border',
				border : false,
				resizable : true,
				closable : false,
				maximized : true,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				title : returnPageTitle('自定义MRBR报告', "mrbrReport"),
				items : [{
					region : 'west',
					layout : 'fit',
					width : 210,
					split : true,
					maxSize : 250,
					minSize : 160,
					items : [tempPanel]
						// 左边目录树
						}, {
							id : 'editContentPanel',
							region : 'center',
							title : mrb.app
									.mpdPdfButton('内容编辑'),
							layout : 'fit',
							split : true,
							items : [new Ext.TabPanel({
								deferredRender : false,// 初始化渲染所有的Tab
								activeTab : 0,
								region : 'north',
								width : 200,
								split : true,
								border : false,
								items : [contentFormPanelCn]
							})]
						// 中间编辑区contentFormPanelCn
						}, {
							region : 'east',
							layout : 'fit',
							width : 210,
							maxSize : 250,
							minSize : 160,
							split : true,
							items : [tempMpdPsPanel]
						// 右边上传的附录树
						}]
			});
			mainWin.show();
			Ext.getCmp("btnEditContentCn").disable();
	

			/** 节的框* */
			// 节
			var sectionWindow = function(title) {
				addSectionWindow = new Ext.Window({
					layout : 'fit',
					width : 420,
					height : 286,
					resizable : false,
					draggable : true,
					closeAction : 'hide',
					title : '<span class="commoncss">' + title + '</span>',
					modal : true,
					collapsible : false,
					titleCollapse : true,
					closable : true,
					maximizable : false,
					buttonAlign : 'center',
					border : false,
					animCollapse : true,
					pageY : (document.documentElement.clientHeight - this.height)
							/ 2,
					pageX : (document.documentElement.clientWidth - this.width)
							/ 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addSectionFormPanel],
					buttons : [{
						text : commonality_save,// '保存',
						handler : function() {
							if (Ext.getCmp('sectionCode').getValue() == '') {
								alert('节编号不为空');
								return;
							}
							if (!/^\d+$/.test(Ext.getCmp('sectionCode')
									.getValue())) {
								alert('编号必须为数字');
								return;
							}

							if (Ext.getCmp('sectionNameCn').getValue() == '') {
								alert('名称不能为空');
								return;
							}
							

							Ext.Ajax.request({
								url : mrb.app.checkSectionCode,
								async:false,
								params : {
									sectionId : addSectionFormPanel
											.findById("section_sectionId")
											.getValue().substring(1),
									chapterId : addSectionFormPanel
											.findById("section_chapterId")
											.getValue().substring(1),
									sectionCode : addSectionFormPanel
											.findById("sectionCode").getValue()
								},
								method : "POST",
								waitMsg : commonality_waitMsg,
								success : function(response) {
									if (response.responseText == "false") {
										alert('章的编号已存在,请更改');
										return ;
									} else {
										addSectionFormPanel.getForm().submit({
											url : mrb.app.saveSection,
											waitTitle : commonality_waitTitle,// "提示",
											waitMsg : commonality_waitMsg,// "正在向服务器提交数据...",
											success : function(form, action) {
												menuTree.root.reload();
												menuTree.expandAll();
												addSectionWindow.hide();
												alert(commonality_messageSaveMsg);
											},
											failure : function(form, action) {
												alert(commonality_cautionMsg);
											}
										});
									}
								}
							});

						}
					}, {
						text : commonality_close,// '关闭',
						handler : function() {
							addSectionWindow.hide();
						}
					}]
				});
				return addSectionWindow;
			}
			var addSectionFormPanel = new Ext.form.FormPanel({
				id : 'addSectionFormPanel',
				name : 'addSectionFormPanel',
				defaultType : 'textfield',
				labelWidth : 100,
				labelAlign : "right",
				frame : false,
				bodyStyle : "padding:10px 10px",
				items : [{
					fieldLabel : '章名称' + commonality_mustInput,// 章目录ID
					disabled : true,
					name : 'chapter_NameCn',
					id : 'chapter_NameCn',
					allowBlank : false,
					anchor : '99%'
				}, {
					fieldLabel : '节编号' + commonality_mustInput,
					name : 'sectionCode',
					id : 'sectionCode',
					maxLength : 10,
					// allowBlank : false,
					anchor : '99%',
					listeners : {
						change : function(tf, newValue, oldValue) {
							/*
							mrb.app.trim(addSectionFormPanel, "sectionCode");
							if (newValue == oldValue) {
								return;
							}
							if (!/^\d+$/.test(newValue)) {
								alert(mpdLang.alert_code);
								return;
							}
							Ext.Ajax.request({
								url : mrb.app.checkSectionCode,
								params : {
									sectionId : addSectionFormPanel
											.findById("section_sectionId")
											.getValue().substring(1),
									chapterId : addSectionFormPanel
											.findById("section_chapterId")
											.getValue().substring(1),
									sectionCode : newValue
								},
								method : "POST",
								waitMsg : commonality_waitMsg,
								success : function(response) {
									if (response.responseText == "false") {
										alert(mpdLang.alert_checkCode);
										return ;
									}
								}
							});*/
						}
					}
				}, {
					fieldLabel : '节名称' + commonality_mustInput,
					name : 'sectionNameCn',
					id : 'sectionNameCn',
					maxLength : 50,
					// allowBlank : false,
					anchor : '99%',
					listeners : {
						blur : function() {
							mrb.app.trim(addSectionFormPanel, "sectionNameCn");
						}
					}
				}, {
					name : 'section_sectionId',
					id : 'section_sectionId',
					hidden : true
				}, {
					name : 'section_chapterId',
					id : 'section_chapterId',
					hidden : true
				}]
			});

			// MRB的附录
			var mrbPsWindow = function(title) {
				addMpdPsWindow = new Ext.Window({
					layout : 'fit',
					width : 420,
					height : 286,
					resizable : false,
					draggable : true,
					closeAction : 'hide',
					title : '<span class="commoncss">' + title + '</span>',
					modal : true,
					collapsible : false,
					titleCollapse : true,
					closable : true,
					maximizable : false,
					buttonAlign : 'center',
					border : false,
					animCollapse : true,
					pageY : (document.documentElement.clientHeight - this.height)
							/ 2,
					pageX : (document.documentElement.clientWidth - this.width)
							/ 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addMrbPsFormPanel],
					buttons : [{
						text : commonality_save,// '保存',
						handler : function() {
							if (Ext.getCmp('psSort').getValue() == '') {
								alert('附录序号不为空');
								return;
							} else if (!/^\d+$/.test(Ext.getCmp('psSort')
									.getValue())) { // 验证是否是数字
								alert('编号必须为数字');
								return;
							}

							if (Ext.getCmp('psNameCn').getValue() == '') {
								alert('名称不能为空');
								return;
							}

							Ext.Ajax.request({
								url : mrb.app.checkMrbPsCode,
								params : {
									mrbPsId : addMrbPsFormPanel
											.findById("psId").getValue(),
									mrbPsCode : addMrbPsFormPanel
											.findById("psSort").getValue()
								},
								method : "POST",
								waitMsg : commonality_waitMsg,
								success : function(response) {
									if (response.responseText == "false") {
										alert('章的编号已存在,请更改');
										return;
									} else {
										var nameCn = addMrbPsFormPanel
												.findById("psNameCn")
												.getValue();
										
										if (nameCn.length <= 50) {
											addMrbPsFormPanel.getForm()
													.submit({
														url : mrb.app.saveMrbPs,//
														waitTitle : commonality_waitTitle,// "提示",
														waitMsg : commonality_waitMsg,// "正在向服务器提交数据...",
														success : function(
																form, action) {
															mpdPsTree.root
																	.reload();
															mpdPsTree
																	.expandAll();
															addMpdPsWindow
																	.hide();
															alert(commonality_messageSaveMsg);
														},
														failure : function(
																form, action) {
															var message = Ext.util.JSON
																	.decode(action.response.responseText);
															alert(message.message);
														}
													});
										} else {
											alert('请按条件输入数据');
											return;
										}
									}
								}
							});
						}
					}, {
						text : commonality_close,// '关闭',
						handler : function() {
							addMpdPsWindow.hide();
						}
					}]
				});
				return addMpdPsWindow;
			};
			var addMrbPsFormPanel = new Ext.form.FormPanel({
				id : 'addMrbPsFormPanel',
				name : 'addMrbPsFormPanel',
				defaultType : 'textfield',
				labelWidth : 120,
				labelAlign : "right",
				frame : false,
				fileUpload : true,
				bodyStyle : "padding:10px 10px",
				items : [{
					fieldLabel : '附录序号' + commonality_mustInput,
					name : 'psSort',
					id : 'psSort',
					maxLength : 10,
					// allowBlank : false,
					anchor : '99%',
					listeners : {
						change : function(tf, newValue, oldValue) {/*
							mrb.app.trim(addMrbPsFormPanel, "psSort");
							if (newValue == oldValue) {
								return;
							}
							if (!/^\d+$/.test(newValue)) {
								alert(mpdLang.alert_code);
								return;
							}
							
							Ext.Ajax.request({
								url : mrb.app.checkMrbPsCode,
								params : {
									mrbPsId : addMrbPsFormPanel
											.findById("psId").getValue()
											.substring(2),
									mrbPsCode : newValue
								},
								method : "POST",
								waitMsg : commonality_waitMsg,
								success : function(response) {
									if (response.responseText == "false") {
										alert(mpdLang.alert_checkCode);
										return ;
									}
								}
							});
						*/}
					}
				}, {
					fieldLabel : '附录名称'
							+ commonality_mustInput,
					name : 'psNameCn',
					id : 'psNameCn',
					maxLength : 50,
					// allowBlank : false,
					anchor : '99%',
					listeners : {
						blur : function() {
							mrb.app.trim(addMrbPsFormPanel, "psNameCn");
						}
					}
				}, {
					fieldLabel : '附录区分' + commonality_mustInput,
					id : 'psFlgGroup',
					name : 'psFlgGroup',
					xtype : 'radiogroup',
					columns : 2,
					style : 'margin:5px 0px',
					items : [{
						boxLabel : '报表首页',
						name : 'psFlg',
						inputValue : 0
					}, {
						boxLabel : '报表附录',
						name : 'psFlg',
						inputValue : 1,
						checked : true
					}]
				}, {
					xtype : 'textfield',
					fieldLabel : '附录文件',
					name : 'pdfFile',
					id : 'pdfFile',
					inputType : 'file',
					anchor : '100%'
				}, {
					name : 'psId',
					id : 'psId',
					hidden : true
				}]
			});
		},
		trim : function(panel, fieldId) {
			var field = panel.findById(fieldId);
			field.setValue(field.getValue().trim());
		},
		confirm : function(config) {
			Ext.MessageBox.show({
				title : commonality_affirm,
				msg : config.msg,
				buttons : Ext.Msg.YESNO,
				fn : function(id) {
					if (id == 'cancel') {
						return;
					} else if (id == 'yes') {
						config.event();
					}
				}
			});
		},
		mpdPdfButton : function(title) {
			var html = '<table style="width:100%;font-size:11px;">';
			html += '<tr>';
			html += '<td style="width:50%">';
			html += title;
			html += '</td>';
			html += '<td style="width:50%;text-align:right;text-decoration:underline">';
			// html += "<span style='cursor:pointer'
			// onclick='mpd.app.exportPdfReport();'>" + mpdLang.mpd_report +
			// "<span>";
			html += '</td>';
			html += '</tr></table>';
			return html;
		}
	};
}();

function getActionUrl(url) {
	return contextPath + url;
}

Ext.apply(mrb.app, {
	loadTree : getActionUrl("/paramDefineManage/mrbrReport/loadTree.do"),
	saveChapter : getActionUrl("/paramDefineManage/mrbrReport/saveChapter.do"),
	loadChapter : getActionUrl("/paramDefineManage/mrbrReport/loadChapter.do"),
	updateChapter : getActionUrl("/paramDefineManage/mrbrReport/updateChapter.do"),
	saveSection : getActionUrl("/paramDefineManage/mrbrReport/saveSection.do"),
	updateSection : getActionUrl("/paramDefineManage/mrbrReport/updateSection.do"),
	loadSection : getActionUrl("/paramDefineManage/mrbrReport/loadSection.do"),
	deleteSection : getActionUrl("/paramDefineManage/mrbrReport/deleteSection.do"),
	uploadImg : getActionUrl("/paramDefineManage/upload/uploadImg.do"),
	loadMrbPsTree : getActionUrl("/paramDefineManage/mrbrReport/loadMrbPsTree.do"),
	saveMrbPs : getActionUrl("/paramDefineManage/mrbrReport/saveMrbPs.do"),
	loadMrbPs : getActionUrl("/paramDefineManage/mrbrReport/loadMrbPs.do"),
	deleteMrbPs : getActionUrl("/paramDefineManage/mrbrReport/deleteMrbPs.do"),
	exportPdf : getActionUrl("/paramDefineManage/mrbrReport/exportPdf.do"),
	checkSectionCode : getActionUrl("/paramDefineManage/mrbrReport/checkSectionCode.do"),
	checkMrbPsCode : getActionUrl("/paramDefineManage/mrbrReport/checkMrbPsCode.do")
});
// 开始运行
Ext.onReady(mrb.app.init, mrb.app);
