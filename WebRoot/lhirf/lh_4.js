/*
 * @author  wangyueli
 * createdate 2012-08-23
 */

// 创建命名空间
Ext.namespace('lh4');

   lh4.app=function(){ 
   
           return{
              
          init: function(){
               	Ext.form.Field.prototype.msgTarget = 'qtip';
			    Ext.QuickTips.init();   

		      
              	//导航
			  var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			    });
			 
			var EDADFrom = new Ext.form.FormPanel({
				frame : true,
				applyTo : 'edad',
				html :EDADTable,
				bodyStyle:"border:1px solid #DFE8F6 ;overflow-x:hidden;overflow-y:auto"
				});
				
			//备注 ED/AD敏感性
			 HelpForm = new Ext.form.FormPanel({
			    title : 'ED/AD敏感性', 
			    labelAlign : "right",	
			    labelWidth: 130,
			   // layout : "fit",    
			    frame:true,	   
			    items: [{
			    	  value : lh4Display,
					  xtype:'textarea',
					  fieldLabel : '综合的ED/AD( 备注 )',
					  id :'lh4Display',
                      height : 40,
                      readOnly  : true,
                      width : 640
			      }
			      ]
			   });	

			
			//*******************说明安全
			 radio = new Ext.form.RadioGroup({
				hideLabel : true, 
				columns : 1, 
				vertical : true, 
				name : 'rad',
				id : 'rad',
				items : [ 
					{boxLabel:'是',inputValue:'1',name:'sevType'}, 
					{boxLabel:'否',inputValue:'0',name:'sevType'}] 
		
			});	
			  
			  /////备注信息
			  isSafeForm = new Ext.form.FormPanel({
					labelWidth: 8,
				    labelAlign : "left",
				    border : false,
				    title : 'L/HIRF防护部件功能退化（包含局部区域的共同模式）结合L/HIRF事件是否会妨碍飞机持续安全飞行或着陆？',
				    frame : true,
				     layout : "fit", 
				    items : [{
				        layout:'column',
				        items:[{
				             columnWidth:.09,
				            layout: 'form',
				            items: [radio]
				        },{
				        	 layout : 'form',
					    	  columnWidth: .04, ///说明理由  中文 和英文
					    	  items:[{xtype: 'label',html:'说   明  理由 ',width:'100%'}]
				        },{
				            columnWidth:.87,
				        	layout: 'form',
				        	  border : false,
				              //width:600,
				            items: [{
				                xtype:'textarea',
				                hidden : false, ///说明理由  中文 和英文
				                anchor : '99.7%',
				            	 height:76,
				            	 id:'safeReason',
				            	 name :'safeReason',
				            	 value:safeRContent
							}]
									
				        }]
				    }]
				});	
			   //combox  选择 
		  twoChooseCombo = new Ext.form.ComboBox({   
			    emptyText: '-请选择-',
			    forceSelection: true,
				xtype : 'combo',
				minListWidth :70,
				name : 'twoChoose', id : 'twoChoose',
				store : new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
					data : [['1','是'],['0','否']]
				}),  
				valueField : "retrunValue", displayField : "displayText",
				typeAhead : true, mode : 'local',
				triggerAction : 'all', width : 70,
				editable : false, selectOnFocus : true,
				listeners :{        //监听 是否有 LH5 步骤
				            "select" : function(combo) {
			         	              var chooseValue =   combo.getValue();   
			         	              if(chooseValue == 1){  //设置 是否 needLhTask = 1;
		                                  Ext.getCmp('safeReason').enable();
					                      Ext.getCmp('rad').enable();
			         	              }
			         	              if(chooseValue == 0){//设置 是否 needLhTask = 0
			         	                 Ext.getCmp('safeReason').disable();
					                     Ext.getCmp('rad').disable();
			         	              }
	         	               	
	         				}
				}
			});
			//选择 是否维修
			 isRepairForm = new Ext.form.FormPanel({	    
			    title : '是否需要L/HIRF维修任务？' + commonality_mustInput,
			    labelWidth: 80,
			    labelAlign : "right",	
			    frame : true,
			    layout:'column',
			    items: [twoChooseCombo]     
			    
			});	
			//~~~~~~~~~    画面初始化 ~~~~~~~~~~~~~~~
			var view = new Ext.Viewport({
				layout : 'border',
				items : [
				 {
					region : 'south',
					layout : 'border',
					height : 120,		 
					items : [
								{
									region : 'center',
									layout : 'fit',
									height : 30,	
									items : [isSafeForm]
								},{
									region : 'west',
									layout : 'fit',
									width : 90,				 
									items : [isRepairForm]
								}
					         ]
				 },{
						region : 'center',
						layout : 'border',
						items : [
							{   title : returnPageTitle('L/HIRF防护项目的ED/AD敏感性确定' + commonality_mustInput,'lh_4'),
								region : 'center',
								layout : 'fit',
								items : [EDADFrom]
							},{
								region : 'south',
								layout : 'fit',
								height : 75,		 
								items : [HelpForm]
							},	{ 
			    		         region : 'north',
			    		         height : 60,
			    		         frame : true,
			    		        items : [headerStepForm]
			    	       }
						]
					}
				]
			});
				///画面结束~~~~~~~~~~~~~~~~~~~~~
				
				function testInput(value){
				    ///人员权限 没添加呢???
				     if(!checkFull()){
				          alert('请填写AD 和ED 完整 !');
				      return false;							
				     }
					///alert(isNeedtask); return ;
				     if(isNeedtask ==''){
				        	alert('请选择是否需要L/HIRF 维修任务 !') ; 
				        	return false;					        	
				        }
					 if(isNeedtask == 1){
						 var radSafe = Ext.getCmp('rad'); 
				         var radValue = -1;  
						 radSafe.eachItem(function(item){   
						    if (item.checked === true){   
						        radValue = item.inputValue;   
						    }   
						 });
						 if (radValue == -1){
							 alert('请选择是否安全飞行和着路 !');
							 return false;
						 }
					 }
				   return true;
				
				}    //自定义矩阵，表头中英文切换
				    document.getElementById("ltitle").innerHTML ='环境损伤（ED)ER:环境等级&nbsp;SR:敏感度等级';
					document.getElementById("laccidental").innerHTML = '偶然损伤（AD）';
					document.getElementById("lsetting").innerHTML = '环境/敏感度等级';
					document.getElementById("lLevel1").innerHTML = '敏感度等级：';
					document.getElementById("lLevel2").innerHTML = '敏感度等级：';
					document.getElementById("lRemark").innerHTML = '备注信息';
				
function modfiedChange(){
		      var EDArr2 =  EDArr.concat(ADArr) ; 
			  for(var i=0;i<levelOld.length;i++){
			      if(levelOld[i]!= EDArr2[i]){
			           return  true;
			       }
			   }
				   
			   if(isNoSaveLh4 == 1 && EDArr2.length > 0){ ///如果lh4 是第一次保存时,  levelOld= null 用个isNoSaveLh4标志下 ,判断是否被修改了
	          		 return  true;
	 			  }
	 			  
				if(needLhTask != isNeedtask){
				       return  true;
				}
				
				if(remark != remarkContent){
						return  true;
				}
					
		      var rad_Value="";
              Ext.getCmp('rad').eachItem(function(item){   
				   	 if (item.checked === true){   
						 rad_Value = item.inputValue;   
					  }   
			  });
			  
			if(isSafe != rad_Value || safeReason != safeRContent){
						 return  true;
			    }	
			 return  false;
}
			
		   nextOrSave = function (value){
		        isNeedtask = Ext.getCmp("twoChoose").getValue();
		       if(isNeedtask == 1){
		            if(value == null){
		            //	if(step[5]!=3){
			               value = 5;
		            //	}else{
		            //		alert("请先完成当前步骤");
		            //		return;
		            //	}
		            }
		       }else if(isNeedtask == 0){
		           if(value == null){
		               value = 6;
		            }
		       }
		       
		       	if(isMaintain!='1'){
					  ///alert(commonality_NoAuthority);
					  goNext(value);
					  return false;
				       }
		       	
		      if(modfiedChange()){
			              Ext.MessageBox.show({
								  title : commonality_affirm,
								  msg : commonality_affirmSaveMsg,
								  buttons : Ext.Msg.YESNOCANCEL,
								  fn : function(id){
								  if (id == 'cancel'){
									  return;
									} else if (id == 'yes'){
										 if (!testInput()){
											   return false;
						                     }
									    save(value);
									} else if (id == 'no'){
										goNext(value);
									}
								  }
							});  	
		         }else{  //没有修改 页面直接跳转 
		              goNext(value);
		         }
		}
					//	
					function test(){
					   var verificationUser =1
						if (verificationUser == '0'){
							//goNext(2);
						return false;
						}
						return true;
					}
			
			
			function save(value){
                    var radValue;
                    Ext.getCmp('rad').eachItem(function(item){   
					    if (item.checked === true){   
					        radValue = item.inputValue;   
					     }   
					  });
                  
                    var EDALG = document.getElementById("EDALG").value;			//获取ED的算法
  			        var ADALG = document.getElementById("ADALG").value;			//获取AD的算法
  			        var LH4ALG = document.getElementById("LH4ALG").value;		//获取LH4的算法
  			        var tempArr = new Array(arrResult(EDArr,EDALG),arrResult(ADArr,ADALG));
  			      var resultLh4=null;
  			       if(navigator.userAgent.indexOf("Firefox")>0){//如果是火狐浏览器
  			    	 resultLh4 = document.getElementById("EDADResult").textContent;
  			      	} 
  			     if(navigator.userAgent.indexOf("MSIE")>0) {//如果是ie浏览器
  			    	 resultLh4 = document.getElementById("EDADResult").innerText;
  			     	} 
  			        
  			      var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true// 完成后移除
						});		
					waitMsg.show();
				Ext.Ajax.request({
						        url : contextPath + "/lhirf/lh_4/saveLh4.do",
			                   	method : "POST",
							    params:{
							            edArr : EDArr,
						             	adArr : ADArr,
						 	            hsiId :  hsiId,
							            resultLh4 : resultLh4,
										needLhTask :     Ext.getCmp("twoChoose").getValue(),
										isSafe :         radValue,
										safeReason :   safeRContent,
										remark :  remarkContent
								
 						 },
 						success : function(response, action) {
 							waitMsg.hide();
 							var text = Ext.util.JSON.decode(response.responseText);
 							if (text.success == true) {
 								alert(commonality_messageSaveMsg);
							       if(step[5]== 0){step[5]= 2 ; }
							        parent.refreshTreeNode();
				                     goNext(value) ;
				                     
 							}else{
 								alert( commonality_saveMsg_fail);
 							}
						},
							failure : function(response,action) {
								waitMsg.hide();
								alert( commonality_cautionMsg);
				            }
						});
				 
			}
		}
	};
}();