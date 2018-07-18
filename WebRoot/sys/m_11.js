Ext.namespace('m11');
m11.app = function() {
return {
		init : function() {
			//导航
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			
			
			 var editgg_form=new Ext.form.FormPanel({    
 		            bodyStyle : 'padding:5px',  
 		            method:"post",  
 		         	region : 'center',
 		            frame:true, 
 		            layout : 'fit', 
 		            items:[{  
 		                xtype:'textarea',  
 		                id:'picContent', 		    
 		                width:'auto',
						height:'auto',
 		                value : picContent  
 		            }],  
 		            listeners:{  
 		                'render' : function(){ 
 			    			keId = 'picContent';
 			    			KE.app.init({
 								renderTo : keId,
 								delayTime : 1,
 								resizeType : 0,
 							    width : '100%',
 								uploadJson : contextPath +'/baseData/upload/uploadImg.do'
 							});
 			    		}             
 		            }
 		        })  
 		        
			var view = new Ext.Viewport({
				layout : 'border',
			    items : [
			    	{ 
			    		region : 'north',
			    		height : 60,
			    		frame : true,
			    		items : [headerStepForm]
			    	},
					{
						title: returnPageTitle("系统功能描述和设计特征",'m_11'),
						region : 'center',
						layout : 'fit',
						items: [  editgg_form ]
					}
				]
			});
				modified = false;
			
			//点击下一步执行的方法
			var newPicContent = '';
			nextOrSave = function (value){	
				//如果点击的是下一步
				if(value == null){
					value = 2;
				}
				if(KE.app.getEditor("picContent")){
				    newPicContent = KE.app.getEditor("picContent").html();
				}else{
					newPicContent = Ext.getCmp("picContent").value;
				}
			   newPicContent = newPicContent.replace(/\n/g,"");
			   if(newPicContent == picContent){
					goNext(value);
					return ;
				}
				//判断是否有权限修改
				if(isMaintain!='1'){
					//alert("您没有修改权限，无权修改！");
					goNext(value);
					return ;
				     }
				Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){							
							save(value);
						} else if (id == 'no'){
							goNext(value);
							return;
						}
				    }
				});
			}
	
			function save(value){
			        	waitMsg.show();
					Ext.Ajax.request( {
							url :  m11.app.saveM11Url,
							method : 'post',
							waitTitle : commonality_waitTitle,
							waitMsg : commonality_waitMsg,
							params:{
								picContent : newPicContent,
								m11Id : m11Id,
								msiId :msiId
							},
							success : function(form,action) {
							   waitMsg.hide();
							    step[2] = 2;
							    parent.refreshTreeNode();
								alert(commonality_messageSaveMsg);
								goNext(value);
							},
							failure : function(form,action) {
								waitMsg.hide();
							 alert(commonality_cautionMsg);
					}
						});
						}
				 

}
};
}();