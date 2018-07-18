var content=null;
var subGgcontent=null;
Ext.onReady(function(){

			//导航
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			
			
			 var editgg_form=new Ext.form.FormPanel({  
 		            labelAlign : 'right',  
 		            labelWidth : 70,  
 		            bodyStyle : 'padding:5px',  
 		            method:"post",  
 		         	 region : 'center',
 		            frame:true, 
 		            layout:'fit', 
 		            items:[{  
 		                xtype:'textarea',  
 		                id:'content',  
                      	width:1200,  
 		                disbled:isMaintain==0?true:false//,
 		            },{  
 		               xtype:'hidden', 
 		                id:'id',
 		                name:'id',
 		                value:s2Id  
 		            }],  
 		            listeners:{  
 		                'render':function(){ 
 			    			keId='content';
 			    			KE.app.init({
 								renderTo : "content",
 								delayTime : 1000,
 								resizeType : 0,
 								resizeMode:0,
 								uploadJson : contextPath + '/baseData/upload/uploadImg.do'
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
						title :  returnPageTitle('结构维修项目的功能、设计、特征等的描述','s2'),
						region : 'center',
						layout : 'fit',
						items: [ editgg_form ]
					}
				]
			});
			
			//点击下一步执行的方法
			nextOrSave = function (number){
					if (number == null) {
						if(step[3]!=3){
						    number = 3;
						}else if(step[4]!=3){
							 number = 4;
						}else if(step[6]!=3){
							 number = 6;
						}else if(step[9]!=3){
							 number = 9;
						}else if(step[11]!=3){
							 number = 11;
						}else{
							alert("请先完成之前的分析");
						}
			     	}
					if (isMaintain==0){  ///没有权限修改时
							goNext(number);
							return null;
				       }
				//
				if( !test()){///页面有修改时
					 Ext.MessageBox.show({
					    title :commonality_affirm,
					    msg :commonality_affirmSaveMsg,
					    buttons : Ext.Msg.YESNOCANCEL,
					    fn : function(id){
					    	if (id == 'cancel'){
								return;
							} else if (id == 'yes'){	
								save(number);
							} else if (id == 'no'){
									goNext(number);
							}
					    }
					});
				 }else{
				      goNext(number);
				 }      
	}
	function test(){
			var modifiedFlag=true;
			var neWgcontent ="";
			if(KE.app.getEditor("content")){
					neWgcontent=KE.app.getEditor("content").html();
				}else{
				    neWgcontent = Ext.getCmp("content").value;
			}
				///判断中文 是否改变
			    if(content != null ){
					 if( content.replace(/\s+/gi,"") != neWgcontent.replace(/\s+/gi,"")){
						       modifiedFlag = false ;
						    }
				   }else{
					    if(neWgcontent != null&&neWgcontent.trim()!=""){
					        modifiedFlag =false;
					   }
				   }
				return modifiedFlag;
			}
			
			
			function save(number){
				if(KE.app.getEditor("content")){
					subGgcontent=KE.app.getEditor("content").html();
				}else{
				    subGgcontent = Ext.getCmp("content").getValue();
				}
				var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true// 完成后移除
						});
				
					waitMsg.show();
					Ext.Ajax.request( {
							url :  contextPath+"/struct/s2/saveS2Records.do",
							method : 'post',
							waitTitle : commonality_waitTitle,
							waitMsg : commonality_waitMsg,
							params:{
								content:subGgcontent,
								ssiId:ssiId,
								id:Ext.getCmp("id").getValue()
							},
							success : function(form,action) {
								if(number){
									successNext(number);
									return null;
								}
								if(form.responseText){
									successNext(form.responseText);
									parent.refreshTreeNode();
									return null;
								}
								waitMsg.hide();
								alert(commonality_messageSaveMsg);
							},
							failure : function(form,action) {
								alert(commonality_cautionMsg);
							}
						});
				 
			}
			
			
			//页面加载结束后读取数据
			Ext.Ajax.request({
					url :  contextPath+"/struct/s2/getS2Records.do",
					async : false,
					params:{
								ssiId:ssiId
							},
					success : function(response) {
								var text=Ext.decode(response.responseText);
								if(text.length){
								content=text[0].content;
								Ext.getCmp("content").setValue(content);
								}
							}
			})
})