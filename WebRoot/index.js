
	function exit() {	
					Ext.MessageBox.show( {
					title : commonality_affirm,
					msg : '确定要退出吗？',
					buttons : Ext.Msg.YESNO,
					fn : function(id) {
						if (id == 'yes') {
							doexit();
							return true;
						} else if (id == 'no') {
							return true;
						}
					}
				});
			}
			function doexit() {				
					Ext.Ajax.request( {
						url : contextPath+"/portal/logout.do",						
						method : "POST",
						success : function() {	
						// alert(index_welcomeToUser);
							location.href=contextPath+'/login.jsp';// 跳转登陆页面
						},
						failure:function(){
						}
					});
			}
			// 创建应用程序


/**
 * 修改密码
 * 
 * @return null
 */
function updateUserPassword() {
	Ext.form.Field.prototype.msgTarget = 'qtip';     // 用来显示验证信息
	Ext.QuickTips.init();

	var urlPrefix = contextPath + "/com/user/";
	var jsonUserPassWordUpdateUrl = "jsonUserPassWordUpdate.do";

	var updateform = new Ext.form.FormPanel({			
		labelWidth : 65,
		width : 300,
		frame : true,
		region : 'fit',
		labelAlign : 'center',	
		closeAction : 'close',
		closable : true,
		buttonAlign : "center",
		items : [ 
			{
				xtype : 'textfield',
				fieldLabel : '原密码'  + commonality_mustInput,
				name : 'yuanPassword',
				inputType : 'password',
				id : 'yuanPassword',
				allowBlank : false,
				width : 180
			},{
				xtype : 'textfield',
				fieldLabel : '新密码'  + commonality_mustInput,
				name : 'password',
				id : 'password',
				inputType : 'password',
				allowBlank : false,
				width : 180
			},{
				xtype : 'textfield',
				fieldLabel : '确认密码'  + commonality_mustInput,
				name : 'password2',
				inputType : 'password',
				id : 'password2',
				allowBlank : false,
				width : 180
			}
		],
		buttons : [
			new Ext.Button({
				text : commonality_save,
				width : 55,											
				handler : function() {
					var yuanPassword = Ext.getCmp('yuanPassword').getValue();
					var password = Ext.getCmp('password').getValue();
					var password2 = Ext.getCmp('password2').getValue();

					if(yuanPassword==''){
						alert('原密码不能为空');
						return;
					}

					if(password==''){
						alert('新密码不能为空');
						return;
					}

					if(password2==''){
						alert('确认密码不能为空');
						return ;
					}

					if (password != password2) {
						alert('输入的确认密码跟新密码不一致，请重新输入！');
						return;
					}
					
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
						msg : commonality_waitMsg,
						removeMask : true
					});		
					waitMsg.show();
							
					Ext.Ajax.request( {
						url : urlPrefix	+ jsonUserPassWordUpdateUrl,
						params : {
							yuanPassword : yuanPassword,
							userId : userId,
							password : password,
							method : 'updateUser'
						},
						method : "POST",
						success : function(response) {
							waitMsg.hide();
							if (response.responseText != ""){																		
							   var responseObj = Ext.util.JSON.decode(response.responseText);
							   if (responseObj.success != undefined && responseObj.success == false) {
									alert('输入的原密码不正确！');
							   }else{
									winFile.close();
									alert('密码修改成功！');
							   }
							}
						},
						failure : function(response) {
							waitMsg.hide();
							alert('输入的原密码不正确！');
						}
					});
				}
			}),
			new Ext.Button( {
				text : commonality_reset,
				id : 'btnReset',
				width : 55,
				handler : function() {
					updateform.getForm().getEl().dom.reset();
				}
			}),
			new Ext.Button( {
				text : commonality_close,
				id : 'btnclose',
				width : 55,									
				handler : function() {
					updateform.getForm().getEl().dom.reset();
					winFile.close();
				}
			}) 
		]
	});

	var winFile = new Ext.Window( {
		title : '修改密码',
		border : false,
		resizable : true,
		closable : true,
		plain : false,
		layout:'fit',
		height:150,
		width:300,
		modal:true,
		bodyStyle : 'padding:0px;',
		buttonAlign : 'center',
		items : [ updateform ]
	});
	winFile.show();
}