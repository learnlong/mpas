/*
 * @author  wangyueli
 * createdate 2012-08-26
 */

// 创建命名空间
Ext.namespace('lh1a');

   lh1a.app =function(){ 
           return{

              init:function(){
                                     
              var modified = true ;
              Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
          	function goNext22(){
        		if (isneedRep == 1){
        			goNext(5);
        		} else {
        			goNext(6);
        		}
        		
        	}
           	function goNextTo(value){
        		if (value == 5){
        			goNext(5);
        		} if(value == 6){
        			goNext(6);
        		}if(value == 1){
        		  goNext(1);
        		}
        		
        	}
			
			//导航
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			var graphicHeight = document.documentElement.clientHeight - 242-58;///图文混排高度
			////图文混排//部件信息说明
			 var editgg_form=new Ext.form.FormPanel({  
 		            title : '部件信息说明' + commonality_mustInput,
 		            bodyStyle : 'padding:3px',  
 		            method:"post",  
 		         	region : 'center',
 		            frame:true,  
 		           layout : 'fit',
 		            items:[{  
 		                xtype:'textarea',  
 		                id:'ggcontent',  
 		                width:'auto',
						height:'auto'
 		            },{  
 		               xtype:'hidden', 
 		                id:'id',
 		                name:'id',
 		                value: picId 
 		            }],  
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
			
              //区域说明 
              	var top3From = new Ext.form.FormPanel({
				    bodyStyle:"align:left",
				    labelWidth: 100, 
				    labelAlign : "right",
				    id : 'topForm3',
				    resizable: false,    
				    autoScroll:false,
				    frame:true,	   
				    layout:'fit',
				    items: [
				    	{ layout : 'column',
				    	items:[
					    	{    layout : 'form',
						    	 columnWidth: .33,
						    	 align :'left',
						    	  items:[
						    		{ xtype: 'label',
							    	  html : '部件性能相似性说明' + commonality_mustInput,
							    	  id : 'lable121p'
							    	 },
							    	 { xtype: 'textarea',
							    	  id : 'content1',
							    	  name : 'lhA.content1',  //中文部件性能相似性说明
							    	  width :'96%',
							    	  hidden :false,
							    	  hideLabel: true,
							    	  value : content1
							    	 }]
							    },
					    		{ layout : 'form',
						    	  columnWidth: .01,
						    	  items:[{xtype: 'label',text:'-',width:'100%'}]
						    	},
					    		{ layout : 'form',
						    	  columnWidth: .33,
						    	  items:[
							    	{ xtype: 'label',
							    	  html : '屏蔽/搭接通路相似性说明：' + commonality_mustInput,
							    	  id : 'lable121c'
							    	 },
								      {    xtype: 'textarea',
								    	  id : 'content2',
								    	  name : 'lhA.content2',
								    	  width : '96%',
								    	  hideLabel: true,    //中文 屏蔽/搭接通路相似性说明
								    	  value : content2
						    	     }
					    	  	  ]
					    	  	},				    	  
					    		{ layout : 'form',
						    	  columnWidth: .01,
						    	  items:[{xtype: 'label',text:'-',width:'100%'}]
						    	},
					    		{ layout : 'form',
						    	  columnWidth: .32,
						    	 items:    [
						    	         {    xtype: 'label',
										    	  html : '区域环境相似性说明：' + commonality_mustInput,
										    	  width : 80,
										    	  id : 'lable122e'
										   },{  xtype: 'textarea',
										    	  id : 'content3',
										    	  name : 'lhA.content3',
										    	  width : '98%',
									    	  	  hideLabel: true,    //中文  区域环境相似性说明
										    	  value :content3
									    	 }
								    	 ]
								  }
						    ]
						  }
						 ]
					});
              ///---------------------------------------------------------------------
							     //LH  适用性
		  	var lh_effForm = new Ext.form.FormPanel({  
		            frame:true, 
		            layout : 'fit',
		            items:[{
				    	layout : 'column',
				    	items:[
					    	{    layout : 'fit',
						    	 columnWidth: .99,
						    	 align :'left',
						    	  items:[
						    		{ xtype: 'label',
							    	  html :  '适用性' +":",
							    	  id : 'lable121p'
							    	 },
							    	 { xtype: 'textarea',
							    	  id : 'lheffId',
							    	  name : 'lheffId',  // lhrif  适用性
							    	  width :'100%',
							    	  maxLength : 500,
							    	  value : lheff

							     }]
						    }]
					  }]  
		        })

			//~~~~~~~~初始化 画面   ~22~~~~~
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
						title: returnPageTitle('L/HIRF防护部件信息说明','1h_1a'),
						region : 'center',
						layout : 'border',
						items: [ 
						 {    
						       region : 'north',
						       layout : 'border',
						       height : 140,//165,
						       items :[{
						       	 region : 'center',
						       	 layout : 'fit',
						       	 height : 80,///100,
						      	 items : [top3From]
						       },{
						       	 region : 'north',
						       	 height : 60,//65
						       	 layout : "fit",
						       	 items : [lh_effForm]
						       }]
						 }, {
						      region : 'center',
						      layout : 'fit',
						      items: [ editgg_form]
						}]
					}
				]
			});
              	//点击下一步执行的方法
			nextOrSave = function (value){
				if (isMaintain!='1'){
						if( value == null){
							goNext22();
						}else{
							goNextTo(value)
						}
					   return false;
				       }
				if( !modifiedChange()){
				     Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg :commonality_affirmSaveMsg,
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){
							if (!test()) {
								return false;
							}
							save(value);
						} else if (id == 'no'){
								if( value == null){
								goNext22();
								}else{
									goNextTo(value)
								}
						}
				    }
				});
				}else{
						if( value == null){
							goNext22();
						}else{
							goNextTo(value)
						}
				}
			}
			
			//判断是否修改    
			function  modifiedChange(){
			     var modified = true ;
			     var newcont1 = Ext.getCmp("content1").getValue();
			     var newcont2 = Ext.getCmp("content2").getValue();
			     var newcont3 = Ext.getCmp("content3").getValue();
			     var lh_eff = Ext.getCmp("lheffId").getValue() ;//lhrif适用性
			      var newpic = null;
				  if(KE.app.getEditor("ggcontent")){  ///图文混排被渲染了
					    newpic = KE.app.getEditor("ggcontent").html() ;
				    }else{
				         newpic = Ext.getCmp("ggcontent").value;
				    }
				     ///判断中文 是否改变
				    if(picContent != null && newpic != null){
					 if( picContent.replace(/\s+/gi,"") != newpic.replace(/\s+/gi,"")){
						       modified = false ;
						    }
				   }else  {
					    if(picContent == null && newpic != ''){
					        modified =false;
					   }
				   }
				   ///文本域相似转换 1>   
				    var oldContent1 = '';
				    if( content1 != null){
				    	oldContent1 = content1.replace(/\s+/gi,"");
				    }
				     var newnewCont1 = '';
				    if( newcont1 != null){
				    	newnewCont1 = newcont1.replace(/\s+/gi,"");
				    }
				    
				    ///文本域相似转换 2>   
				     var oldContent2 = '';
				    if( content2 != null){
				    	oldContent2 = content2.replace(/\s+/gi,"");
				    }
				     var newnewCont2 = '';
				    if( newcont2 != null){
				    	newnewCont2 = newcont2.replace(/\s+/gi,"");
				    }
				     ///文本域相似转换 3>   
				     var oldContent33 = '';
				    if( content3 != null){
				    	oldContent33 = content3.replace(/\s+/gi,"");
				    }
				     var newnewCont33 = '';
				    if( newcont3 != null){
				    	newnewCont33 = newcont3.replace(/\s+/gi,"");
				    }
				    
				    ///适用性
				    var oldLh_eff = '';
				    if( lheff != null){
				    	oldLh_eff = lheff.replace(/\s+/gi,"");
				    }
				    var newLh_eff = '';
				    if( lh_eff != null){
				    	newLh_eff = lh_eff.replace(/\s+/gi,"");
				    }
				    
				    ///
				    if(oldContent1 != newnewCont1){
				        modified = false ;  ///部件性能相似 中英文有变化
				    }
				    if(oldContent2 != newnewCont2){
				        modified = false ;  ///屏蔽/搭接通路相似性说明
				    }
				    if(oldContent33 != newnewCont33){
				        modified = false ;  ///区域环境相似性说明 有修改
				    }
				     if( oldLh_eff != newLh_eff){
				        modified = false ;  ///适用性 有修改
				    }
				   return modified ;
			
			}
			//验证  
				function test(){
				var flag = 0;
			    var verificationUser =1
			    //
		        var  newPicContent = null;
					
                if(KE.app.getEditor("ggcontent")){
				    newPicContent = KE.app.getEditor("ggcontent").text();
				}else{
					newPicContent = Ext.getCmp("ggcontent").value;
				}
				if(newPicContent != null){
					if(newPicContent.length>=maxRichTextLength){
						alert(commonality_MaxLengthText);  //输入信息超过规定长度
						return false;
					}
				}
				
				if( newPicContent == ''){
					alert('部件信息说明不能为空 !');
					return false ;
				}
				var lhacontent_1 = '';
					 if(Ext.getCmp("content1").getValue() != null){
					   lhacontent_1 = Ext.getCmp("content1").getValue().trim() ;
					 }
			    if(lhacontent_1 ==''){   ///不可同时为空 1
			         flag = 1;
			         alert('部件性能相似性说明不可为空 !');
					return false;
			    }
			    
			    var lhacontent_2 = '';
				if(Ext.getCmp("content2").getValue() != null){
				   lhacontent_2 = Ext.getCmp("content2").getValue().trim() ;
				 }
		 	     if(lhacontent_2 == ''){///不可同时为空2
					 	  flag = 1;
			            alert('屏蔽/搭接通路相似性说明不可为空!');
					    return false;
			 	   }
			 	   
		         var lhacontent_3 = '';
				 if(Ext.getCmp("content3").getValue() != null){
					   lhacontent_3 = Ext.getCmp("content3").getValue().trim() ;
					 }
				if(lhacontent_3 == '' ){///不可同时为空 3
				     flag = 1;
			         alert('区域环境相似性说明不可为空 !');
					return false;
				}
				if(flag == 0){
				    return true;
				}if(flag == 1){
				   return false;
				}
				
			}
            function save(value){
					var ggcontent = null;
					var content1 = null;
					var content2 = null;
					var content3 = null;
					if(KE.app.getEditor("ggcontent")){
					    ggcontent = KE.app.getEditor("ggcontent").html();
					}else{
					    ggcontent = Ext.getCmp("ggcontent").getValue();
					}
					if(Ext.getCmp("content1")){
					  content1 = Ext.getCmp("content1").getValue()
					  
					}
					if(Ext.getCmp("content2")){
					   content2 = Ext.getCmp("content2").getValue()
					}
					if(Ext.getCmp("content3")){
					   content3=Ext.getCmp("content3").getValue()
					}
					Ext.Ajax.request( {
							url : lh1a.app.g_lh1aSaveUrl,
							method : 'post',
							waitTitle :commonality_waitTitle,
							waitMsg : commonality_waitMsg,
							params:{
								picContent : ggcontent,
								content1 :   content1,
								content2 :   content2,
								content3 :   content3,
								lheff :Ext.getCmp("lheffId").getValue(),
								areaId :  areaId,
								hsiId :     hsiId,
								picId :   Ext.getCmp("id").getValue()
							},
							success : function(response,action){
							        var  res = Ext.util.JSON.decode(response.responseText);
							    
							        if(res.msg == 'fail'){
							        	  alert('所参见的HSI还没有分析完成! 本HSI不可操作!');
							        }
							        if(res.msg == 'success'){
								        	alert(commonality_messageSaveMsg);
								        	if(step[5] == 0){ //保存成功后 把 lh5未分析  ==正在分析
								        		step[5]=2;
								        	}
								        	if( value == null){
								        		goNext22();
								        	}else{
								        		goNextTo(value)
								        	}
								        	parent.refreshTreeNode();
							        }	 
							         
							},
							failure : function(response,action) {
							   if(action.result!==undefined){
			      			   		alert(commonality_saveMsg_fail+action.result.msg);
			      			   	}else{
			      			   		alert(commonality_cautionMsg);
			      			   	} 
					        }
						});
				 
			}
			//页面加载结束图文混排内容后读取数据	
			Ext.Ajax.request({
				url :  contextPath+"/lhirf/lh_1a/getLh1aRecords.do",
				async : false,
				params:{
							hsiId:hsiId
						},
				success : function(response) {
							var text=Ext.decode(response.responseText);
							if(text.length){
							Ext.getCmp("ggcontent").setValue((text[0].cn));
							}
						}
		  })
              ///~~~~~~~~~~~~~~~ 画面结束  ~~~~~~~~~~~~~~~~~~~
           }
              
       }
 }();