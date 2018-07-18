/*
 * @author  wangyueli
 * createdate 2012-08-17
 */

// 创建命名空间

Ext.namespace('lh2');

   lh2.app=function(){ 
           return{

    init:function(){
               Ext.QuickTips.init();
			   Ext.form.Field.prototype.msgTarget = 'qtip';
			//导航
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			var picContent = '';
			var graphicHeight = document.documentElement.clientHeight - 138;///图文混排高度
			 var editgg_form=new Ext.form.FormPanel({  
 		            title : ' 安装位置原理  ' + commonality_mustInput,
 		            bodyStyle : 'padding:3px',  
 		            method:"post",  
 		         	region : 'center',
 		            frame:true,  
 		           layout : "fit", 
 		            items:[{  
 		                xtype:'textarea',  
 		                id:'ggcontent',  
 		                width:'auto',
						height:'auto' 
 		                //value:  picContent  
 		            } ],  
 		            listeners:{  
 		                'render':function(){ 
 			    			keId='ggcontent';
 			    			KE.app.init({
 								renderTo : "ggcontent",
 								delayTime : 1,
 								resizeType : 0,
 								width : '100%',
 								height:graphicHeight,
 								uploadJson : contextPath +'/baseData/upload/uploadImg.do'
 							});
 		            	}          
 		            }
 		        })  
 		        
  			var maxLength=2500;
			 var env_form = new Ext.form.FormPanel({  
		            title: '区域环境说明 ' + commonality_mustInput,
		            bodyStyle : 'padding:3px ',  
		            method:"post",  
		         	region : 'west',
		         	width :"35%",
		            frame:true, 
		            layout : "fit",
		            items:[{  
		                xtype:'textarea',  
		                id :'env',  
	                    width:'auto',
		                height:'auto',
		                maxLength:maxLength,
		                value : env  
		            }]  
		            
		        })  
		        

			//画面
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
					    title : returnPageTitle('L/HIRF防护部件信息说明 ( 二 )','lh_2'),
						region : 'center',
						layout : 'border',
						items: [editgg_form,env_form]
					}
				]
			});
			
			modified = false;
			
			//点击下一步执行的方法
			nextOrSave = function (value){
			      if(value == null){
			         value = 3;
			      }
			
			     if(isMaintain!='1'){
					/// alert(commonality_NoAuthority);
					  goNext(value);
					  return false;
				       }
			     if(!modifiedChange()){  //修改了
			         Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){
							 if(!test()){
							      return  false ;
							      }
							save(value);
						} else if (id == 'no'){
							goNext(value);
						}
				    }
				 });
			     }else{ //没有修改 直接跳转
			        goNext(value);
			     
			     } 
				
			}
			
			function test(){
                 var  newPicContent = null;
                if(KE.app.getEditor("ggcontent")){
				    newPicContent = KE.app.getEditor("ggcontent").text();
				}else{
					newPicContent = Ext.getCmp("ggcontent").value;
				}
				if( newPicContent == ''){
					alert('安装位置原理填写项,不可为空 !');
					return false ;
				}
				var envContent= '';
				if(Ext.getCmp("env") != null){
				    envContent = Ext.getCmp("env").getValue().trim();
				}
				if(envContent== ''){
					alert('区域环境说明填写项不可为空 !');
					return false ;
				}
				///文本域超长
				if(!env_form.form.isValid()){
					   alert('区域环境说明填写项超过规矩长度 :' + maxLength)
					   return false;
					}
				return  true;
			}
			//是否修改
				function  modifiedChange(){
			     var modified = true ;
			     var  newPicContent = '';
			     if(KE.app.getEditor("ggcontent")){
				        newPicContent = KE.app.getEditor("ggcontent").html();
				}else {
					newPicContent = Ext.getCmp("ggcontent").value;
				}
				///
				var oldpicContent ='';
				if(picContent != null){
				  oldpicContent =  picContent.replace(/\s+/gi,"");
				}
				if(newPicContent  != null){
					newPicContent = newPicContent.replace(/\s+/gi,"")
				}
				if(oldpicContent != newPicContent ){
				    modified = false;
				}
				
				   var oldenv = '';
				   if(env != null){
				   	 oldenv = env.replace(/\s+/gi,"");
				   }
				   var newenv = '';
				   if(Ext.getCmp("env").getValue() != null){
				     newenv = Ext.getCmp("env").getValue().replace(/\s+/gi,"")
				   }
				    
				   if(oldenv != newenv){
				      modified = false;
				   }
				   
			   return   modified ;
		}
			
			function save(value){
				var ggcontent=null;
				var envcontent= null;
				if( KE.app.getEditor("ggcontent")){
				  	ggcontent  = KE.app.getEditor("ggcontent").html()
				}else{
				  	ggcontent  = Ext.getCmp("ggcontent").getValue()
				}
				if(Ext.getCmp("env")){
					envcontent=Ext.getCmp("env").getValue()
				}
				var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : commonality_waitMsg,
				    removeMask : true// 完成后移除
					});		
				waitMsg.show();
					
				Ext.Ajax.request( {
						url :  contextPath+"/lhirf/lh_2/saveLh2.do",
						method : 'post',
						waitTitle :commonality_waitTitle,
						waitMsg : commonality_waitMsg,
						params:{
							picContent : ggcontent,
							env :   envcontent,
							hsiId : hsiId
							//lh2Id : Ext.getCmp("id").getValue()
						},
							success : function(response, action) {
								waitMsg.hide();
								alert( commonality_messageSaveMsg);
				                     if(step[3] == 0){
				                        step[3] =2;
				                     }
				                     goNext(value)
						},
						failure : function(response,action) {
							waitMsg.hide();
							alert(commonality_cautionMsg);
			     	 }
				});
			}
				
			//页面加载结束图文混排内容后读取数据	
			Ext.Ajax.request({
				url :  contextPath+"/lhirf/lh_2/getLh2Records.do",
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
			}
          }
     }();