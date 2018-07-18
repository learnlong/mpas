/*
 * @author  wangyueli
 * createdate 2012-08-21
 */

// 创建命名空间
Ext.namespace('lh1');

lh1.app = function() {
	return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
			//导航
			var headerStepForm = new Ext.form.Label({
						applyTo : 'headerStepDiv'
					});
			var picContent = "";
			var graphicHeight = document.documentElement.clientHeight - 140-63; ///图文混排高度
         	
			var editgg_form = new Ext.form.FormPanel({
						title : '部件性能描述与屏蔽/搭接通路描述信息说明' + commonality_mustInput,
						bodyStyle : 'padding:3px',
						method : "post",
						region : 'center',
						frame : true,
						layout : 'fit',
						items : [{
									xtype : 'textarea',
									id : 'ggcontent',
									width : 'auto',
									height : 'auto'
									//value : picContent
								}],
						listeners : {
							'render' : function() {
								keId = 'ggcontent', KE.app.init({
											renderTo : "ggcontent",
											delayTime : 1,
											resizeType : 0,
											width : '100%',
											height : graphicHeight,
											uploadJson : contextPath
													+ '/baseData/upload/uploadImg.do'
										});
							}
						}
					});
			
         			     //LH  适用性
				 var lh_effForm = new Ext.form.FormPanel({  
					            frame:true, 
					            layout : "fit",
					            items:[{
							    	layout : 'column',
							    	items:[
								    	{    layout : 'fit',
									    	 columnWidth: .99,
									    	 align :'left',
									    	  items:[
									    		{ 
									    		  xtype: 'label',
										    	  html : '适用性'+":" ,
										    	  id : 'lable121p'
										    	 },{
										    	  xtype: 'textarea',
										    	  id : 'lheffId',
										    	  name : 'lheffId',  // lhrif  适用性
										    	  width :'100%',
										    	  maxLength : 500,
										    	  value : lheff
										 	    }]
										    }]
									  }]  
	       						 })
				 
					var view = new Ext.Viewport({
						layout : 'border',
						items : [{
									region : 'north',
									height : 60,
									frame : true,
									items : [headerStepForm]
								}, {
						       		title: returnPageTitle('L/HIRF防护部件信息说明(一)','1h_1'),
						     		region : 'center',
						   			layout : 'border',
						    items: [ 
							   {    
							       region : 'north',
							       height : 65,
							       layout : 'fit',
							       items :[lh_effForm]
							   }, {
					          	region : 'center',
								layout : 'fit',
								items : [  editgg_form]///
								}]
						}]
					});
			

			//点击下一步执行的方法
			nextOrSave = function(value) {
				if (value == null) {
					value = 2;
				}
				
				if (isMaintain != '1') {
					goNext(value);
					return false;
				}
				if (!modifiedChange()) {
					Ext.MessageBox.show({
								title : commonality_affirm,
								msg : commonality_affirmSaveMsg,
								buttons : Ext.Msg.YESNOCANCEL,
								fn : function(id) {
									if (id == 'cancel') {
										return;
									} else if (id == 'yes') {
										if (!test()) {
											return false;
										}
										save(value);
									} else if (id == 'no') {
										goNext(value);
									}
								}
							});
				} else { //没有修改 页面 直接跳转
					goNext(value);

				}
			}

			function test() {
				//				//页面中英文不可同时为空
				var newPicContent = null;
                var isOk = true;
				if (KE.app.getEditor("ggcontent")) {
					newPicContent = KE.app.getEditor("ggcontent").text();
				} else {
					newPicContent = Ext.getCmp("ggcontent").value;
				}

				if (newPicContent == '') {
					alert('填写项不可同时为空  ! ');
					isOk = false;
					
				} 
				
				if(newPicContent != null){
					if(newPicContent.length>=maxRichTextLength){
						alert(commonality_MaxLengthText);
						isOk = false;
					}
				}
				return isOk;
			}
			//判断是否修改

			function modifiedChange() {
				var modified = true;
				var newPicContent = null;
				var newpic ="";
				var lh_eff =Ext.getCmp("lheffId").getValue();
				 if(KE.app.getEditor("ggcontent")){
				 	///图文混排被渲染了
					     newpic = KE.app.getEditor("ggcontent").html() ;
				   }else{
				      newpic = Ext.getCmp("ggcontent").value;
				   }
				 
			    if(picContent != null && newpic != null){
					   if( picContent .replace(/\s+/gi,"") != newpic.replace(/\s+/gi,"")){
							       modified = false ;
						}
				}else{
				       if( picContent == null && newpic != ''){
					        modified =false;
					   }
				}//
			    if(lheff != null && lh_eff != null){
						 if( lheff .replace(/\s+/gi,"") != lh_eff.replace(/\s+/gi,"")){
							       modified = false ;
						}
				}else{
				       if( lheff == null && lh_eff != ''){
					        modified =false;
					   }
				}
				return modified;
			}

			function save(value) {
				var ggcontent = null;
				if (KE.app.getEditor("ggcontent")) {
					ggcontent = KE.app.getEditor("ggcontent").html();
				} else {
					ggcontent = Ext.getCmp("ggcontent").getValue();
				}
				var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					msg : commonality_waitMsg,
					removeMask : true
						// 完成后移除
					});

				waitMsg.show();
				Ext.Ajax.request({
							url : contextPath + "/lhirf/lh_1/saveLh1Records.do",
							method : 'post',
							params : {
								picContent : ggcontent,
								lheff:Ext.getCmp("lheffId").getValue(),
								hsiId : hsiId
							},
							success : function(response, action) {
								waitMsg.hide();
								alert(commonality_messageSaveMsg);
								if (step[2] == 0) {
									step[2] = 2;
								}
								parent.refreshTreeNode();
								goNext(value);
							},
							failure : function(response, action) {
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});

			}
			
			
				//页面加载结束图文混排内容后读取数据
			Ext.Ajax.request({
					url :  contextPath+"/lhirf/lh_1/getLh1Records.do",
					async : false,
					params:{
								hsiId:hsiId
							},
					success : function(response) {
								var text=Ext.decode(response.responseText);
								if(text.length){
									picContent = text[0].cn;
									Ext.getCmp("ggcontent").setValue(picContent);
								}
							}
			})
			//

		}

	}
}();