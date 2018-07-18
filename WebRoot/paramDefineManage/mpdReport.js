Ext.namespace('mpd');
var addChapterWindow ;
var addChapterFormPanel;
var addSectionWindow ;
var addMpdPsWindow ;
var tempValue = "";
mpd.app = function() {
	return {
		init : function() {
			var comboStore = new Ext.data.JsonStore({
				url:mpd.app.loadChapterFlg,
				root:'chapter_flg',
				fields:['key','val']
			});	
	
			comboStore.load({});
			
			Ext.QuickTips.init();
			var root = new Ext.tree.AsyncTreeNode({
				text : 'MPD目录',
				expanded : true,
				id : '0'
			});
			var menuTree = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
					dataUrl : mpd.app.loadTree
				}),
				root : root,
				rootVisible:true,
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
						if(depth === 0){
							Ext.getCmp('addChapter').show();
							Ext.getCmp('addSection').hide();
							Ext.getCmp('modifyChapter').hide();
							Ext.getCmp('modifySection').hide();
							Ext.getCmp('deleteChapterOrSection').hide();
						}else if(depth === 1){
							Ext.getCmp('addChapter').hide();
							Ext.getCmp('addSection').show();
							Ext.getCmp('modifyChapter').show();
							Ext.getCmp('modifySection').hide();
							Ext.getCmp('deleteChapterOrSection').show();
						}else if(depth === 2){
							Ext.getCmp('addChapter').hide();
							Ext.getCmp('addSection').hide();
							Ext.getCmp('modifyChapter').hide();
							Ext.getCmp('modifySection').show();
							Ext.getCmp('deleteChapterOrSection').show();
						}
						node.expand();
						contextMenu.showAt(e.getXY());
					},
					click : function(node, e) {
						e.preventDefault();
						menuid = node.id;
//						var editor = null;
//						var btnSubmit = null;
//						if(language === 'Cn'){
//							editor = KE.app.getEditor("contentCn");
//							btnSubmit = Ext.getCmp("btnEditContentCn");
//						}else{
//							editor = KE.app.getEditor("contentEn");
//							btnSubmit = Ext.getCmp("btnEditContentEn");
//						}
						var editorCn = KE.app.getEditor("contentCn");
						
						var btnSubmitCn = Ext.getCmp("btnEditContentCn");
						
						//点击了根节点
						if(menuid === '0'){
							Ext.getCmp("editContentPanel").setTitle(mpd.app.mpdPdfButton('内容编辑'));
							btnSubmitCn.disable();
						
							editorCn.html("");
							
							editorCn.readonly();
							
							return;
						}						
						Ext.getCmp("editContentPanel").setTitle(mpd.app.mpdPdfButton("内容编辑" + "("+ node.text +")"));
						var id = menuid.substring(1);
						var nodeType = menuid.substr(0,1);
						var isChapter = nodeType === "c";
						Ext.Ajax.request({
							url: isChapter ? mpd.app.loadChapter : mpd.app.loadSection,
							params:{
								chapterId:id,
								sectionId:id
							},
							success:function(res){
								res = Ext.util.JSON.decode(res.responseText);
								var contentCn = res.contentCn ? res.contentCn : "";
								
								if(editorCn != null && editorCn != 'undefined'){
									editorCn.html(contentCn);
									editorCn.readonly(false);
								}
								
								//contentCnTextArea.setValue(contentCn);
								//contentEnTextArea.setValue(contentEn);
								
									if(isChapter){
										contentFormPanelCn.findById("chapterId").setValue(id);
										contentFormPanelCn.findById("sectionId").setValue("");
									}else{
										contentFormPanelCn.findById("chapterId").setValue(node.parentNode.id.substring(1));
										contentFormPanelCn.findById("sectionId").setValue(id);
									}
								
								btnSubmitCn.enable();
								
							},
							failure:function(){
								alert('加载数据失败！');
							}
						});
					}
				}
			});
			menuTree.expandAll();
			var contextMenu = new Ext.menu.Menu({
				id : 'menuTreeContextMenu',
				items : [{
					text : '新增章',//新增章
					id : 'addChapter',
					iconCls : 'page_addIcon',
					handler : function(node) {
						chapterWindow('新增章').show();
					}
				}, {
					text : '修改章',//修改章
					id : 'modifyChapter',
					iconCls : 'page_addIcon',
					handler : function() {
						var node =  menuTree.getSelectionModel().getSelectedNode();
						chapterWindow('修改章').show();
						Ext.Ajax.request({
							url:mpd.app.loadChapter,
							params:{
								chapterId:node.id.substring(1)
							},
							success:function(res){
								res = Ext.util.JSON.decode(res.responseText);
								addChapterFormPanel.findById("chapter_chapterId").setValue(node.id.substring(1));
								addChapterFormPanel.findById("chapterCode").setValue(res.code);
								addChapterFormPanel.findById("chapterNameCn").setValue(res.nameCn);
								
								if("undefined"==res.chapterFlg || res.chapterFlg ==null || res.chapterFlg == ""){

								}else{
									addChapterFormPanel.findById("chapterFlg1").setValue(res.chapterFlg);
								}								
							},
							failure:function(){
								alert('加载数据失败！');
							}
						});
					}
				}, {
					text : '新增节',
					iconCls : 'page_addIcon',
					id : 'addSection',
					handler : function() {
						if(addSectionWindow){
							if(addSectionFormPanel){
								addSectionFormPanel.getForm().reset();
							}
							addSectionWindow.show();
						}else{
							sectionWindow('新增节').show();
						}
//						addSectionFormPanel.findById("section_chapterId").setValue("");
//						addSectionFormPanel.findById("chapter_NameCn").setValue("");
//						addSectionFormPanel.findById("section_sectionId").setValue("");
//						addSectionFormPanel.findById("sectionCode").setValue("");
//						addSectionFormPanel.findById("sectionNameCn").setValue("");
//						addSectionFormPanel.findById("sectionNameEn").setValue("");
//						sectionWindow(mpdLang.add_section).show();
						var node =  menuTree.getSelectionModel().getSelectedNode();
						addSectionFormPanel.findById("chapter_NameCn").setValue(node.text);
						addSectionFormPanel.findById("section_chapterId").setValue(node.id.substring(1));
					}
				}, {
					text : '修改节',
					iconCls : 'page_addIcon',
					id : 'modifySection',
					handler : function() {
						var node =  menuTree.getSelectionModel().getSelectedNode();
						if(addSectionWindow != null){
							addSectionFormPanel.getForm().reset();
						}
						sectionWindow('修改节');
						addSectionWindow.show();
						Ext.Ajax.request({
							url:mpd.app.loadSection,
							params:{
								chapterId:node.parentNode.id.substring(1),
								sectionId:node.id.substring(1)
							},
							success:function(res){
								res = Ext.util.JSON.decode(res.responseText);
								addSectionFormPanel.findById("section_chapterId").setValue(node.parentNode.id.substring(1));
								addSectionFormPanel.findById("chapter_NameCn").setValue(node.parentNode.text);
								addSectionFormPanel.findById("section_sectionId").setValue(node.id.substring(1));
								addSectionFormPanel.findById("sectionCode").setValue(res.code);
								addSectionFormPanel.findById("sectionNameCn").setValue(res.nameCn);
								
							},
							failure:function(){
								alert('加载数据失败！');
							}
						});
					}
				},{
					text : commonality_del,//删除
					id:"deleteChapterOrSection",
					iconCls : 'page_delIcon',
					handler : function() {
						var selectModel = menuTree.getSelectionModel();
						var selectNode = selectModel.getSelectedNode();
						if(selectNode.hasChildNodes()){
							alert('该节点下具有多个子节点，不能删除！');//"该节点下具有多个子节点，不能删除！"
							return false;
						}
						var empty = 0;
						
						var depth = selectNode.getDepth();
						if(depth === 1){//章
							var id = selectNode.attributes.id.substring(1);
							var panelId; 
							
								panelId = contentFormPanelCn.findById("chapterId").getValue();
							
							if(id === panelId){
								empty = 1;
							}
						}else if(depth === 2){
							var id = selectNode.attributes.id.substring(1);
							var panelId; 
							
								panelId = contentFormPanelCn.findById("sectionId").getValue();
							
							if(id === panelId){
								empty = 1;
							}
						}
						mpd.app.confirm({
							msg : commonality_affirmDelMsg,//确认要删除吗？
							event : function(){		
								Ext.Msg.wait(commonality_waitMsg,commonality_waitTitle);
								Ext.Ajax.request({
									url: selectNode.leaf ? mpd.app.deleteSection : mpd.app.deleteChapter,
									params:{
										sectionId:selectNode.id.substring(1),
										chapterId:selectNode.id.substring(1)
									},
									success:function(){
										var btnSubmit = null;
										var editor = null;
										
											editor = KE.app.getEditor("contentCn");
											btnSubmit = Ext.getCmp("btnEditContentCn");
										
										if(empty === 1){
											Ext.getCmp("editContentPanel").setTitle(mpd.app.mpdPdfButton('内容编辑'));
											btnSubmit.disable();
											editor.html("");
											editor.readonly();
										}
										Ext.Msg.hide();
										menuTree.root.reload();
										menuTree.expandAll();
										alert(commonality_messageDelMsg);
									},
									failure:function(){
										
									}
								});
							}
						});
					}
				}]
			});
			//MPD附录树根节点
			var mpdPsRoot = new Ext.tree.AsyncTreeNode({
				text : 'MPD附录',
				expanded : true,
				id : '0'
			});
			//MPD附录树
			var mpdPsTree = new Ext.tree.TreePanel({
				loader : new Ext.tree.TreeLoader({
					dataUrl : mpd.app.loadMpdPsTree
				}),
				root : mpdPsRoot,
				rootVisible:true,
				autoScroll : false,
				animate : true,
				useArrows : false,
				border : false,
				listeners:{
					contextmenu:function(node,e){
						e.preventDefault();
						node.select();
						if(node.leaf){
							Ext.getCmp("uploadPs").hide();
							Ext.getCmp("deletePs").show();
							Ext.getCmp("modifyPs").show();
						}else{
							Ext.getCmp("uploadPs").show();
							Ext.getCmp("deletePs").hide();
							Ext.getCmp("modifyPs").hide();
						}
						contextMpdPsMenu.showAt(e.getXY());
					}
				}
			});
			mpdPsTree.expandAll();
			//MPD附录右键菜单
			var contextMpdPsMenu = new Ext.menu.Menu({
				id : "contextMpdPsMenu",
				items : [{
					id : 'uploadPs',
					text : '上传附录',//上传附录
					iconCls : 'page_refreshIcon',
					handler : function(){
						if(addMpdPsWindow != null){
							addMpdPsFormPanel.getForm().reset();
//							var pdfFile = document.getElementById("pdfFile");
//							pdfFile.outerHTML=pdfFile.outerHTML;
						}
						addMpdPsFormPanel.findById("psId").setValue("");
						addMpdPsFormPanel.findById("psSort").setValue("");
						addMpdPsFormPanel.findById("psNameCn").setValue("");
						
						
						mpdPsWindow('上传附录').show();
					}
				}, {
					id : "modifyPs",
					text : '修改附录',//'修改附录',
					iconCls : 'page_refreshIcon',
					handler : function(){
						var node =  mpdPsTree.getSelectionModel().getSelectedNode();
						if(addMpdPsWindow != null){
							addMpdPsFormPanel.getForm().reset();
//							var pdfFile = document.getElementById("pdfFile");
//							pdfFile.outerHTML=pdfFile.outerHTML;  
						}
						mpdPsWindow('修改附录').show();
						Ext.Ajax.request({
							url:mpd.app.loadMpdPs,
							params:{
								psId:node.id.substring(2)
							},
							success:function(res){
								res = Ext.util.JSON.decode(res.responseText);
								addMpdPsFormPanel.findById("psId").setValue(res.psId);
								addMpdPsFormPanel.findById("psSort").setValue(res.psSort);
								addMpdPsFormPanel.findById("psNameCn").setValue(res.psNameCn);
								
								addMpdPsFormPanel.findById("psFlgGroup").eachItem(function(item){
									item.setValue(item.inputValue == res.psFlg);
								});
							},
							failure:function(){
								alert('加载数据失败！');
							}
						});						
					}
				}, {
					id : 'deletePs',
					text : commonality_del,//删除
					iconCls : 'page_refreshIcon',
					handler:function(){
						var node =  mpdPsTree.getSelectionModel().getSelectedNode();
						mpd.app.confirm({
							msg : commonality_affirmDelMsg,//"确认删除吗?",
							event : function(id){
								Ext.Msg.wait(commonality_waitMsg,commonality_waitTitle);  
								Ext.Ajax.request({
									url: mpd.app.deleteMpdPs,
									params:{
										psId:node.id.substring(2)
									},
									success:function(){
										Ext.Msg.hide();
										mpdPsTree.root.reload();
										mpdPsTree.expandAll();
										alert(commonality_messageDelMsg);
									},
									failure:function(){
										
									}
								});
							}
						});
					}
				}]
			});
			//menuTree.expandAll();	
			var tempPanel = new Ext.Panel({
				layout : 'fit',
				bodyStyle:"border:0px;padding:0px",
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
					bodyStyle:"border:0px;padding:0px",
					items : [menuTree]
				}]
			});
			var tempMpdPsPanel = new Ext.Panel({
				layout : 'fit',
				bodyStyle:"border:0px;padding:0px",
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
					bodyStyle:"border:0px;padding:0px",
					items : [mpdPsTree]
				}]
			});
			var documentHeight = document.documentElement.clientHeight - 130; 
			var contentFormPanelCn = new Ext.form.FormPanel({
				id : 'contentFormPanelCn',
				title:'图文混排信息', //'中文',
				layout : 'fit',
				bodyStyle:"border:0px;padding:0px",
				defaultType : 'textfield',
				items : [{
					xtype:'textarea',
					id:'contentCn'
				},{
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
				},{
					name : 'sectionContentCn',
					id : 'sectionContentCn',
					hidden : true
				}],
				listeners:{
					'render':function(){
						KE.app.init({
							renderTo : "contentCn",
							delayTime : 1,
							resizeType : 0,
							width : '100%',
							height:documentHeight,
//							height:560,
							minChangeSize : 20,
							imageTabIndex : 1,
							uploadJson : mpd.app.uploadImg,
							readonlyMode : true
						});
					}			
				},
				buttons:[{
					text:commonality_save,//保存
					id:"btnEditContentCn",
					handler : function() {
						saveContent();
					}
				}],
				buttonAlign:'center'
			});
		
			function saveContent(){
				var htmlCn = KE.app.getEditor('contentCn').html();//取值
				
				/*if(htmlCn.length > maxRichTextLength){
					alert(mpdLang.cn_length_limit);
					return false;
				}
				if(htmlEn.length > maxRichTextLength){
					alert(mpdLang.en_length_limit);return false;
				}*/
				if(!isRichTextValid(htmlCn)){
					alert('中文长度超过限制');
					return false;
				}
				
				var node =  menuTree.getSelectionModel().getSelectedNode();
				var isChapter = true;
				var id = node.id;
				contentFormPanelCn.findById("chapterContentCn").setValue(htmlCn);
				
				contentFormPanelCn.findById("sectionContentCn").setValue(htmlCn);
				
				if(id.charAt(0) == 'c'){
					contentFormPanelCn.findById("chapterId").setValue(id.substring(1));
					
				}else{
					var chapterId = node.parentNode.id.substring(1);
					contentFormPanelCn.findById("chapterId").setValue(chapterId);
				
					contentFormPanelCn.findById("sectionId").setValue(id.substring(1));
					
					isChapter = false;
				}
				var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : commonality_waitMsg,
				    removeMask : true// 完成后移除
				});
				waitMsg.show();
				Ext.Ajax.request({
					url :  isChapter ? mpd.app.updateChapter : mpd.app.updateSection,
					method : 'post',
					params:{
						sectionId : contentFormPanelCn.findById("sectionId").getValue(),
						chapterId :	contentFormPanelCn.findById("chapterId").getValue(),
						chapterContentCn : contentFormPanelCn.findById("chapterContentCn").getValue(),
					
						sectionContentCn : contentFormPanelCn.findById("sectionContentCn").getValue(),
											
					},
					success : function(response, action) {
						waitMsg.hide();
						alert( commonality_messageSaveMsg);
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert( commonality_cautionMsg);
					}								
				});
			}
			var mainWin = new Ext.Window({
				layout : 'border',
				border : false,
				resizable : true,
				closable : false,
				maximized : true,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				title :returnPageTitle('自定义MPD报告',"mpdReport"),
				items : [{
					region : 'west',
					layout : 'fit',
					width : 210,
					split : true,
					maxSize :250,
					minSize:160,
					items : [tempPanel]//左边目录树
				}, {
					id:'editContentPanel',
					region : 'center',
					title:mpd.app.mpdPdfButton('内容编辑'),
					layout : 'fit',
					split : true,
					items : [
					    new Ext.TabPanel({
					    	deferredRender:false,//初始化渲染所有的Tab
							activeTab : 0,
							region : 'north',
							width :200,
							split : true,
							border : false,
							items : [ 
						         contentFormPanelCn
						        						    
							]
					    })
				    ]//中间编辑区contentFormPanelCn
				},{
					region : 'east',
					layout : 'fit',
					width : 210,
					maxSize :250,
					minSize:160,
					split : true,
					items : [tempMpdPsPanel]//右边上传的附录树
				}]
			});
			mainWin.show();
			Ext.getCmp("btnEditContentCn").disable();
			
			
			//'新增章'弹框
			var chapterWindow = function(title) {
				addChapterFormPanel = new Ext.form.FormPanel({
					id : 'addChapterFormPanel',
					name : 'addChapterFormPanel',
					defaultType : 'textfield',
					labelWidth : 125,
					labelAlign : "right",
					frame : false,
					bodyStyle : "padding:10px 10px",
					items : [{
						fieldLabel : '章编号' + commonality_mustInput,
						name : 'chapterCode',
						id : 'chapterCode',
						maxLength : 10,
						allowBlank : false,
						anchor : '99%',
						listeners:{
							change : function(tf , newValue , oldValue){
								mpd.app.trim(addChapterFormPanel,"chapterCode");
								if(newValue == oldValue){
									return;
								}			
								if(!/^\d+$/.test(newValue)){
									alert('编号必须为数字');
									return;
								}				
								Ext.Ajax.request({
									url :mpd.app.checkChapterCode,
									params : {
										chapterId : Ext.getCmp('chapter_chapterId').getValue(),
										chapterCode : newValue																			
									},
									method : "POST",
									waitMsg :commonality_waitMsg,
									success : function(response) {
										if(response.responseText=="false"){
											alert('章的编号已存在,请更改');	
										}									
									}
								});	
							}						
						}
					}, {
						fieldLabel : '章名称' + commonality_mustInput,
						name : 'chapterNameCn',
						id : 'chapterNameCn',
						maxLength : 50,
						allowBlank : false,
						anchor : '99%',
						listeners:{
							blur:function(){
								mpd.app.trim(addChapterFormPanel,"chapterNameCn");
							}
						}
					}, {
						fieldLabel : '章节内容区分',
						name : 'chapterFlg1',
						hiddenName:'chapterFlg',
						id : 'chapterFlg1',
						width:125,
						xtype : 'combo',
						triggerAction : 'all',
						displayField : 'key',
						valueField : 'val',
						listWidth : 100,
						store : comboStore, 
						editable:false,  
						emptyText:'请选择',
						listeners:{
							'expand':function(){
							}
						}
					}, {
						name : 'chapter_chapterId',
						id : 'chapter_chapterId',
						hidden : true
					}]
				});
				addChapterWindow =  new Ext.Window({
					layout : 'fit',
					width : 420,
					height : 286,
					resizable : false,
					draggable : true,
					closeAction : 'close',
					title : '<span class="commoncss">' + title + '</span>',
					modal : true,
					collapsible : false,
					titleCollapse : true,
					closable : true,
					maximizable : false,
					buttonAlign : 'center',
					border : false,
					animCollapse : true,
					pageY : (document.documentElement.clientHeight - this.height) / 2,
					pageX : (document.documentElement.clientWidth - this.width) / 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addChapterFormPanel],
					buttons : [{
						text : commonality_save,//'保存',
						handler : function() {		
							if(!/^\d+$/.test(Ext.getCmp('chapterCode').getValue())){
								alert('编号必须为数字');
								return;
							}								
							if(Ext.getCmp('chapterNameCn').getValue()==''){
								alert('名称不能为空');
								return;
							}
							//验证章的code
							Ext.Ajax.request({
								url :mpd.app.checkChapterCode,
								params : {
									chapterId : Ext.getCmp('chapter_chapterId').getValue(),
									chapterCode : Ext.getCmp('chapterCode').getValue(),
									chapterFlg : Ext.getCmp("chapterFlg1").getValue()
								},
								method : "POST",
								waitMsg :commonality_waitMsg,
								success : function(response) {
									var message = Ext.util.JSON.decode(response.responseText);
									if(!message.success){
										alert(message.message);
									}else{
										//验证成功，提交
										var chapter_chapterId = addChapterFormPanel.findById('chapter_chapterId').getValue();
										var chapterCode = addChapterFormPanel.findById('chapterCode').getValue();	
										var chapterNameCn = addChapterFormPanel.findById('chapterNameCn').getValue();
									
										var chapterFlg = addChapterFormPanel.findById('chapterFlg1').getValue() ;
										Ext.Ajax.request({
											url : mpd.app.saveChapter,
											params : {
												'chapter_chapterId' : chapter_chapterId,
												'chapterCode' :chapterCode ,	
												'chapterNameCn' : chapterNameCn,
												
												'chapterFlg' : chapterFlg
											},
											method : "POST",
											waitTitle : commonality_waitTitle,//"提示",
											waitMsg : commonality_waitMsg,//"正在向服务器提交数据...",
											success : function(response) {
												clearTextArea();
												menuTree.root.reload();
												menuTree.expandAll();
												addChapterWindow.close();
												alert( commonality_messageSaveMsg);									
											}
										});	
									}									
								}
							});	
						}
					}, /*{
						text : '重置',
						id : 'btnReset',
						handler : function() {
							addChapterFormPanel.getForm().reset();
						}
					}, */{
						text : commonality_close,//关闭
						handler : function() {
							addChapterWindow.close();
						}
					}]
				});
				return addChapterWindow;
			};
			

			
			//节
			var sectionWindow = function(title) {

				if(addSectionWindow){
					return addSectionWindow;
				}
				addSectionWindow =  new Ext.Window({
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
					pageY : (document.documentElement.clientHeight - this.height) / 2,
					pageX : (document.documentElement.clientWidth - this.width) / 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addSectionFormPanel],
					buttons : [{
						text : commonality_save,//'保存',
						handler : function() {
							if(!/^\d+$/.test(Ext.getCmp('sectionCode').getValue())){
								alert('编号必须为数字');
								return;
							}	
							
							if(Ext.getCmp('sectionNameCn').getValue()==''){
								alert('名称不能为空');
								return;
							}							
							Ext.Ajax.request({
								url :mpd.app.checkSectionCode,
								params : {
									sectionId : addSectionFormPanel.findById("section_sectionId").getValue(),
									chapterId : addSectionFormPanel.findById("section_chapterId").getValue(),
									sectionCode : addSectionFormPanel.findById("sectionCode").getValue()													
								},
								method : "POST",
								waitMsg :commonality_waitMsg,
								success : function(response) {
									if(response.responseText=="false"){
										alert('章的编号已存在,请更改');	
									}else{
										var node =  menuTree.getSelectionModel().getSelectedNode();
										addSectionFormPanel.getForm().submit({
											url : mpd.app.saveSection,
											waitTitle : commonality_waitTitle,//"提示",
											waitMsg : commonality_waitMsg,//"正在向服务器提交数据...",
											success : function(form, action) {
												clearTextArea();
												menuTree.root.reload();
												menuTree.expandAll();
												addSectionWindow.hide();
												alert( commonality_messageSaveMsg);
											},
											failure : function(form, action) {
												alert( commonality_cautionMsg);
											}
										});
									}									
								}
							});
						}
					}/*, {
						text : '重置',
						id : 'btnReset',
						handler : function() {
							addSectionFormPanel.getForm().reset();
						}
					}*/, {
						text : commonality_close,//'关闭',
						handler : function() {
							addSectionWindow.hide();
						}
					}]
				});
				return addSectionWindow;
			}
			//'新增节'弹框表单
			var addSectionFormPanel = new Ext.form.FormPanel({
				id : 'addSectionFormPanel',
				name : 'addSectionFormPanel',
				defaultType : 'textfield',
				labelWidth : 125,
				labelAlign : "right",
				frame : false,
				bodyStyle : "padding:10px 10px",
				items : [{
					fieldLabel : '章名称'   + commonality_mustInput,//章目录ID
					disabled:true,
					name : 'chapter_NameCn',
					id : 'chapter_NameCn',
					allowBlank : false,
					anchor : '99%'
				},{
					fieldLabel : '节编号' + commonality_mustInput,
					name : 'sectionCode',
					id : 'sectionCode',
					maxLength : 10,
					allowBlank : false,
					anchor : '99%',
					listeners:{
						change : function(tf , newValue , oldValue){
									/*mpd.app.trim(addSectionFormPanel,"sectionCode");
									if(newValue == oldValue){
										return;
									}			
									if(!/^\d+$/.test(newValue)){
										alert(mpdLang.alert_code);
										return;
									}							
				        		    Ext.Ajax.request({
										url :mpd.app.checkSectionCode,
										params : {
											sectionId : addSectionFormPanel.findById("section_sectionId").getValue(),
											chapterId : addSectionFormPanel.findById("section_chapterId").getValue(),
											sectionCode : newValue																			
										},
										method : "POST",
										waitMsg :commonality_waitMsg,
										success : function(response) {
											if(response.responseText=="false"){
												alert(mpdLang.alert_checkCode);	
											}										
										}
									});	*/						
							}
					}
				}, {
					fieldLabel : '节名称' + commonality_mustInput,
					name : 'sectionNameCn',
					id : 'sectionNameCn',
					maxLength : 50,
					allowBlank : false,
					anchor : '99%',
					listeners:{
						blur:function(){
							mpd.app.trim(addSectionFormPanel,"sectionNameCn");
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
			var mpdPsWindow = function(title) {
				addMpdPsWindow =  new Ext.Window({
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
					pageY : (document.documentElement.clientHeight - this.height) / 2,
					pageX : (document.documentElement.clientWidth - this.width) / 2,
					animateTarget : Ext.getBody(),
					constrain : true,
					items : [addMpdPsFormPanel],
					buttons : [{
						text : commonality_save,//'保存',
						handler : function() {
							//验证
							if(!/^\d+$/.test(Ext.getCmp('psSort').getValue())){
								alert('编号必须为数字');
								return;
							}	
							
							if(Ext.getCmp('psNameCn').getValue()=='' ){
								alert('名称不能为空');
								return;
							}
							
							Ext.Ajax.request({
										url :mpd.app.checkMrbPsCode,
										params : {
											mrbPsId : addMpdPsFormPanel.findById("psId").getValue(),
											mrbPsCode : addMpdPsFormPanel.findById("psSort").getValue()																			
										},
										method : "POST",
										waitMsg :commonality_waitMsg,
										success : function(response) {
											if(response.responseText=="false"){
												alert('章的编号已存在,请更改');	
												return;
											}else{
												var nameCn = addMpdPsFormPanel.findById("psNameCn").getValue();
												
												if(nameCn.length<=50 ){
													addMpdPsFormPanel.getForm().submit({
														url : mpd.app.saveMpdPs,//
														waitTitle : commonality_waitTitle,//"提示",
														waitMsg : commonality_waitMsg,//"正在向服务器提交数据...",
														success : function(form, action) {
															mpdPsTree.root.reload();
															mpdPsTree.expandAll();
															addMpdPsWindow.hide();
															alert(commonality_messageSaveMsg);
														},
														failure : function(form, action) {
															var message = Ext.util.JSON.decode(action.response.responseText);
															alert( message.message);
														}
													});
												}else{
													alert('请按条件输入数据');
													return;
												}
											}									
										}
									});		
						}
					}, {
						text : commonality_close,//'关闭',
						handler : function() {
							addMpdPsWindow.hide();
						}
					}]
				});
				return addMpdPsWindow;
			};
			//'上传附录'弹框表单
			var addMpdPsFormPanel = new Ext.form.FormPanel({
				id : 'addMpdPsFormPanel',
				name : 'addMpdPsFormPanel',
				defaultType : 'textfield',
				labelWidth : 150,
				labelAlign : "right",
				frame : false, 
				fileUpload : true,
				bodyStyle : "padding:10px 10px",
				items : [{
					fieldLabel : '附录序号' + commonality_mustInput,
					name : 'psSort',
					id : 'psSort',
					maxLength : 10,
					//allowBlank : false,
					anchor : '99%',
					listeners:{
						change : function(tf , newValue , oldValue){
							mpd.app.trim(addMpdPsFormPanel,"psSort");
							if(newValue == oldValue){
								return;
							}			
							if(!/^\d+$/.test(newValue)){
								alert('编号必须为数字');
								return;
							}							
						}
					}
				}, {
					fieldLabel : '附录名称' + commonality_mustInput,
					name : 'psNameCn',
					id : 'psNameCn',
					maxLength : 50,
					//allowBlank : false,
					anchor : '99%',
					listeners:{
						blur:function(){
							mpd.app.trim(addMpdPsFormPanel,"psNameCn");
						}
					}
				}, {
					fieldLabel : '附录区分' + commonality_mustInput,
					id : 'psFlgGroup',
					name:'psFlgGroup',
					xtype : 'radiogroup',
					columns : 2,
					style:'margin:5px 0px',
					items : [
					    {boxLabel : '报表首页', name : 'psFlg', inputValue : 0},
					    {boxLabel : '报表附录', name : 'psFlg', inputValue : 1, checked : true}
					]
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
		trim:function(panel,fieldId){
			var field = panel.findById(fieldId);
			field.setValue(field.getValue().trim());
		},
		confirm:function(config){
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
		mpdPdfButton : function(title){
			var html = '<table style="width:100%;font-size:11px;">';
			html += '<tr>';
			html += '<td style="width:50%">';
			html += title;
			html += '</td>';
			html += '<td style="width:50%;text-align:right;text-decoration:underline">';
			//html += "<input type='button' onclick='mpd.app.exportPdfReport();'/>";//+ mpdLang.mpd_report + "";
			html += '</td>';						
			html += '</tr></table>';
			return html;
		}
		/*,
		exportPdfReport:function(){
			Ext.Msg.wait(commonality_waitMsg,commonality_waitTitle);  
			Ext.Ajax.request({
				url:mpd.app.exportPdf,
				params:{},
				success:function(res){
					Ext.Msg.hide();
					res = Ext.util.JSON.decode(res.responseText);
					alert(commonality_waitTitle,"成功");
				},
				failure:function(){
					Ext.Msg.hide();
					alert("失败！");
				}
			});						
		}*/
	};
}();

function getActionUrl(url){
	return contextPath + url;
}

//清空文本框
function clearTextArea(){
	var btnSubmit = null;
	var editor = null;
	
		editor = KE.app.getEditor("contentCn");
		btnSubmit = Ext.getCmp("btnEditContentCn");
	
	Ext.getCmp("editContentPanel").setTitle(mpd.app.mpdPdfButton('内容编辑'));
	btnSubmit.disable();
	editor.html("");
	editor.readonly();
}

Ext.apply(mpd.app, {
	 loadTree : getActionUrl("/paramDefineManage/mpdReport/loadTree.do"),
	 loadChapterFlg : getActionUrl("/paramDefineManage/mpdReport/loadChapterFlg.do"),
	 saveChapter : getActionUrl("/paramDefineManage/mpdReport/saveChapter.do"),
	 updateChapter : getActionUrl("/paramDefineManage/mpdReport/updateChapter.do"),
	 loadChapter : getActionUrl("/paramDefineManage/mpdReport/loadChapter.do"),
	 deleteChapter : getActionUrl("/paramDefineManage/mpdReport/deleteChapter.do"),
	 saveSection : getActionUrl("/paramDefineManage/mpdReport/saveSection.do"),
	 updateSection : getActionUrl("/paramDefineManage/mpdReport/updateSection.do"),
	 loadSection : getActionUrl("/paramDefineManage/mpdReport/loadSection.do"),
	 deleteSection : getActionUrl("/paramDefineManage/mpdReport/deleteSection.do"),
	 uploadImg : getActionUrl("/paramDefineManage/upload/uploadImg.do"),
	 loadMpdPsTree : getActionUrl("/paramDefineManage/mpdReport/loadMpdPsTree.do"),
	 saveMpdPs : getActionUrl("/paramDefineManage/mpdReport/saveMpdPs.do"),
	 loadMpdPs : getActionUrl("/paramDefineManage/mpdReport/loadMpdPs.do"),
	 deleteMpdPs : getActionUrl("/paramDefineManage/mpdReport/deleteMpdPs.do"),
	 exportPdf : getActionUrl("/paramDefineManage/mpdReport/exportPdf.do"),
	 checkChapterCode : getActionUrl("/paramDefineManage/mpdReport/checkChapterCode.do"),
	 checkSectionCode : getActionUrl("/paramDefineManage/mpdReport/checkSectionCode.do"),
	 checkMrbPsCode : getActionUrl("/paramDefineManage/mpdReport/checkMrbPsCode.do")
});
//开始运行  
Ext.onReady(mpd.app.init,mpd.app);