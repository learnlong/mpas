Ext.namespace('comHelp');
comHelp.app = function() {
	return {
		init : function() {
			var urlPrefix = contextPath + "/baseData/help/";
			
			Ext.QuickTips.init();
			
			var menuTree = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
					dataUrl : "comHelpTree.js"
				}),
				root : returnRoot(),
				rootVisible : true,
				autoScroll : false,
				animate : true,
				useArrows : false,
				border : false,
				listeners : {
					click : function(node, e) {
						e.preventDefault();
						menuid = node.id;
						var editor = KE.app.getEditor("content");
						var btnSubmit = Ext.getCmp("btnEditContent");
						// 点击了根节点(和文件夹) 不做任何的处理
						if (menuid === '0' || menuid === 'childSys' || menuid === 'msiAnalysis' || menuid === 'childStructure'
								|| menuid === 'ssiAnalysis' || menuid === 'childArea' || menuid === 'areaAnalysis'
								|| menuid === 'childLhirf' || menuid === 'lhAnalysis' || menuid === 'childMrb'
								|| menuid === 'childMpd' || menuid === 'childSearch' || menuid === 'childApprove'
								|| menuid === 'childFile' || menuid === 'childBase' || menuid === 'childParam'
								|| menuid === 'childUser') {
							Ext.getCmp("editContentPanel").setTitle(comHelp.app.mpdPdfButton("内容编辑"));
							btnSubmit.disable();
							
							Ext.getCmp("content").setValue("");
							if (KE.app.getEditor("content")) {
								KE.app.getEditor("content").html("");
								KE.app.getEditor("content").readonly();
							}
							return;
						} else {
							btnSubmit.enable();
							if (KE.app.getEditor("content")) {
								KE.app.getEditor("content").readonly(false);
							}
							
							var waitMsg = new Ext.LoadMask(Ext.getBody(), {
								msg : commonality_waitMsg,
								removeMask : true  // 完成后移除
							});
							waitMsg.show();
							
							Ext.Ajax.request({
								url : urlPrefix + "getHelp.do",
								method : "POST",
								params : {
									helpWhere : menuid
								},
								success : function(response, action) {
									var msg = Ext.util.JSON.decode(response.responseText);// 解析json字符串
									Ext.getCmp("content").setValue(msg.content);
									if (KE.app.getEditor("content")) {
										KE.app.getEditor("content").html(msg.content);
									}
									waitMsg.hide();
								},
								failure : function(response, action) {
									alert("数据加载失败!");
								}
							});
						}
						Ext.getCmp("editContentPanel").setTitle(comHelp.app.mpdPdfButton("内容编辑" + "(" + node.text + ")"));
					}
				}
			});
			menuTree.expand();
			
			var tempPanel = new Ext.Panel({
				layout : 'fit',
				bodyStyle : "border:0px;padding:0px",
				items : [{
					title : "帮助页面树状列表",
					collapsible : false,
					id : "treepanel",
					split : true,
					region : 'center',
					autoScroll : true,
					bodyStyle : "border:0px;padding:0px",
					items : [menuTree]
				}]
			});
			
			var contentFormPanel = new Ext.form.FormPanel({
				id : 'contentFormPanel',
				layout : 'fit',
				bodyStyle : "border:0px;padding:0px",
				defaultType : 'textfield',
				items : [{
					xtype : 'textarea',
					id : 'content',
					width : 'auto',
					height : 'auto'
				}],
				listeners : {
					'render' : function() {
						KE.app.init({
							renderTo : "content",
							delayTime : 1,
							resizeType : 0,
							width : '100%',
							minChangeSize : 20,
							imageTabIndex : 1,
							uploadJson : contextPath + '/baseData/upload/uploadImg.do'
								// readonlyMode : true
						});
					}
				},
				buttons : [{
					text : commonality_save,// 保存
					id : "btnEditContent",
					disabled : true,
					handler : save
				}],
				buttonAlign : 'center'
			});
			
			function save() {
				Ext.MessageBox.show({
					title : commonality_affirm,
					msg : commonality_affirmSaveMsg,
					buttons : Ext.Msg.YESNO,
					fn : function(id) {
						if (id == 'cancel') {
							return;
						} else if (id == 'yes') {
							saveData();
						}
					}
				});
			}
			
			function saveData() {
				var html = KE.app.getEditor('content').html();// 取值
				if (menuTree.getSelectionModel().getSelectedNode() == null) {
					alert("请先点击左边树的节点!");
					return;
				}
				if (html != null) {
					if (html.length >= maxRichTextLength) {
						alert(commonality_MaxLengthText); // 输入信息超过规定长度
						return;
					}
				}
				
				var node = menuTree.getSelectionModel().getSelectedNode().id;
				
				var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					msg : commonality_waitMsg,
					removeMask : true // 完成后移除
				});
				waitMsg.show();
				
				Ext.Ajax.request({
					url : urlPrefix + "saveHelp.do",
					method : 'post',
					params : {
						helpWhere : node,
						content : html
					},
					success : function(form, action) {
						waitMsg.hide();
						alert(commonality_messageSaveMsg);
					},
					failure : function(form, action) {
						alert(commonality_saveMsg_fail);
					}
				})
			}
			
			var mainWin = new Ext.Window({
				renderTo : Ext.getBody(),
				layout : 'border',
				border : false,
				resizable : true,
				closeAction : 'close',
				closable : false,
				maximized : true,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				collapsible : true,
				title : returnPageTitle("帮助管理", "comHelp"),
				items : [
				    {
						region : 'west',
						layout : 'fit',
						width : 210,
						split : true,
						maxSize : 250,
						minSize : 160,
						items : [tempPanel]
					}, {
						id : 'editContentPanel',
						region : 'center',
						title : comHelp.app.mpdPdfButton("内容编辑"),
						layout : 'fit',
						split : true,
						items : [contentFormPanel]
					}
				]
			});
			mainWin.show();
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
			html += '</td>';
			html += '</tr></table>';
			return html;
		}
	};
}();
// 开始运行
Ext.onReady(comHelp.app.init, comHelp.app);
