

/*  LH5 分析步骤画面
 * @author  wangyueli
 * createdate 2012-08-27
 */

        

 var tempReachWay;	
 var tempTaskDesc;
Ext.namespace('lh5');

   lh5.app=function(){ 
           return{

              init:function(){
               Ext.QuickTips.init();
			  Ext.form.Field.prototype.msgTarget = 'qtip';

             var newGrid = null;
           //分辨率 改变大小
 			var widthLen=1000;
 			var cWidt = document.body.clientWidth;
 			 cWidt = 1210;
 			var cHeight = document.body.clientHeight;
 			if(cWidt<=1024 ){
 				widthLen=1100;
 			}
 			if(cWidt>1024&&cWidt<1366){
 				widthLen=1300;
 			}
 			if(cWidt>=1366){
 				widthLen=1100;
 			}
			//alert('宽度 cWidt=='+cWidt);
			//导航条
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			//L/HIRF防护部件功能退化（包含局部区域的共同模式）结合L/HIRF事件是否会妨碍飞机持续安全飞行或着陆？font-weight: bold ;
			 isSafeForm = new Ext.form.FormPanel({	    
			    title : '<span style="font-color: block;font-size :11px;">'+'L/HIRF防护部件功能退化（包含局部区域的共同模式）结合L/HIRF事件是否会妨碍飞机持续安全飞行或着陆？'+'</span>',
			    frame : true,
			    items : [
			             {xtype:'label', html : '<div style="text-align: center;font-weight: bold ; width: 100%; height: 100%; padding-top: 1%">'
								+ commonality_shi + '</div>',id:'safe1',hidden:true,cls:'one'},
								   {xtype:'label', html : '<div style="text-align: center; width: 100%; height: 100%; padding-top: 1%;">'
										+ commonality_fou + '</div>',id:'safe2',hidden:true,cls:'one'}]
			    
			});	
			
			
			 HelpForm = new Ext.form.FormPanel({	
			     //baseCls 	:'x-panel',		
			    // title : returnPageTitle('任务间隔确定','lh_5'),
			     frame : true,
			     ///layout: 'fit',
			     contentEl:  'list_CusList',
			     bodyStyle:"border:1px solid #DFE8F6 ;overflow-x:hidden;overflow-y:auto"

			    });
			///~~~~~~~~~~~~~~~~单选按钮  ~~~``
			 GVIRadioGroup = new Ext.form.RadioGroup({
				hideLabel : true, 		
				name : 'GVIRadio',
				id : 'GVIRadio',
				width : 87,
				columns : 2, 
				vertical : true, 
				items : [ 
					{boxLabel:commonality_shi,inputValue:'1',name:'sevType1'}, 
					{boxLabel:commonality_fou,inputValue:'0',name:'sevType1'}]
			});	
			
			 DETRadioGroup = new Ext.form.RadioGroup({
				hideLabel : true, 		
				name : 'DETRadio',
				id : 'DETRadio',
				width : 87,
				columns : 2, 
				vertical : true, 
				items : [ 
					{boxLabel:commonality_shi,inputValue:'1',name:'sevType2'}, 
					{boxLabel:commonality_fou,inputValue:'0',name:'sevType2'}]
			});	
			
			 FNCRadioGroup = new Ext.form.RadioGroup({
				hideLabel : true,
				name : 'FNCRadio',
				id : 'FNCRadio',
				width : 87,
				columns : 2, 
				vertical : true, 
				items : [ 
					{boxLabel:commonality_shi,inputValue:'1',name:'sevType3'}, 
					{boxLabel:commonality_fou,inputValue:'0',name:'sevType3'}]
			});
			
			 DISRadioGroup = new Ext.form.RadioGroup({
				hideLabel : true,
				name : 'DISRadio',
				id : 'DISRadio',
				width : 87,
				columns : 2, 
				vertical : true, 
				items : [ 
					{boxLabel:commonality_shi,inputValue:'1',name:'sevType4'}, 
					{boxLabel:commonality_fou,inputValue:'0',name:'sevType4'}]
			});
		
			 reDesignRadioGroup = new Ext.form.RadioGroup({
				hideLabel : true,
				name : 'reDesignRadio',
				id : 'reDesignRadio',
				width : 87,
				columns : 2, 
				vertical : true, 
				items : [ 
					{boxLabel:commonality_shi,inputValue:'1',name:'sevType5'}, 
					{boxLabel:commonality_fou,inputValue:'0',name:'sevType5'}]
			});
			
			//~~~~~~~~~~~~~~~~~~~单选按钮 end ~~~~
			typeForm = new Ext.form.FormPanel({				
			    title : '任务类型确定',    
			    frame : true,
			     layout:'form',
				bodyStyle:"border:1px solid #DFE8F6 ;overflow-x:hidden;overflow-y:auto",
			   // width:150,	   
			    items : [{
			        layout:'column',
			        items:[{
			            columnWidth:.18,
			             layout: 'form',
			           // width: 100,
			            items: [{xtype:'label',html:'GVI检查是否有效？' + commonality_mustInput}]
			        },{
			            columnWidth:.12,			            
			            layout: 'form',
			            items: [GVIRadioGroup]
			        },{
			            columnWidth:.20,
			            layout: 'form',
			            items:   [
			                      {	xtype:'textarea', 
			                      	hideLabel : true, 
			                      	anchor : '90%', 
				            		height : 30,
				            		value : gviDesc, 
				            		id : 'gviDesc'
			            		 }]
			        },
			        {
			            columnWidth:.18,
			            layout: 'form',
			            items: [{xtype:'label',html:'DET检查是否有效？' + commonality_mustInput}]
			        },{
			            columnWidth:.12,
			            layout: 'form',
			            items: [DETRadioGroup]
			        },{
			            columnWidth:.20,   ///DET中文 输入
			            layout: 'form',
			            items: [{
			            	xtype:'textarea', 
			            	hideLabel : true, 
			            	anchor : '90%', 
	            		    height : 30,
	            		    value : detDesc, 
	            		    id : 'detDesc'
	            			}]
			        }]
			    },{
			        layout:'column',
			        items:[{
			            columnWidth:.18,
			            layout: 'form',
			            items: [{xtype:'label',html:'FNC检查是否有效？' + commonality_mustInput}]
			        },{
			            columnWidth:.12,
			            layout: 'form',
			            items: [FNCRadioGroup]
			        },{
			            columnWidth:.20,
			            layout: 'form',  ///FNC中文
			            items: [{xtype:'textarea', hideLabel : true, anchor : '90%', 
			            		 height : 30, value :  fncDesc, id : 'fncDesc'
			            	  }]
			        },
			        {
			            columnWidth:.18,
			            layout: 'form',
			            items: [{xtype:'label',html:'DIS检查是否有效？' + commonality_mustInput}]
			        },{
			            columnWidth:.12,
			            layout: 'form',
			            items: [DISRadioGroup]
			        },{
			            columnWidth:.20,
			            layout: 'form',   
			            items: [{xtype:'textarea', hideLabel : true, anchor : '90%', 
			            		 height : 30, value : disDesc, id : 'disDesc'
			            		 }]
			        }]
			    },{
			        layout:'column',
			        items:[{
			            columnWidth:.18,
			            layout: 'form',
			            items:   
			                   [{xtype:'label', html : 
								'是否需重新设计？'  + commonality_mustInput } ]
			        },{
			            columnWidth:.12,
			            layout: 'form',
			            items: [reDesignRadioGroup]
			        },{
			            columnWidth:.69,
			            layout: 'form',   /// 重新设计
			            items: [{xtype:'textarea',  hideLabel : true, anchor : '98.5%', 
			            		 height : 30, value : redesignDesc, id : 'redesignDesc'
			            		 }]
			        }]
			    }]
			});
			 //转移区域 combobox
			var orMoveCombo = new Ext.form.ComboBox({   
				name : 'OrMove', id : 'OrMove',
				store : new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
					data : [['1',commonality_shi],['0',commonality_fou]]
				}),  
				valueField : "retrunValue", displayField : "displayText",
				typeAhead : true, mode : 'local',   
				triggerAction : 'all', width : 45,  
				editable : false, selectOnFocus : true
			});
			
		 	var store = new Ext.data.Store({
				url : lh5.app.g_searchMsgUrl,
				reader : new Ext.data.JsonReader({
					root : "taskMsg",
					fields : [
						{name : 'taskId'},
						{name : 'taskCode'},						
						{name : 'taskInterval'},
						{name :  'taskType'},
						{name : 'reachWay'},
						{name : 'taskDesc'},
						{name : 'needTransfer'},
						{name : 'ownArea'},
						{name : 'whyTransfer'}
						
					]     
				})
			});
			/// ///有参照 的时候 lh1a  lh5
			if (refHsiCode != NA){
				    var colM = new Ext.grid.ColumnModel([
						{
							header : 'MSG-3任务号' + commonality_mustInput,
							dataIndex : 'taskCode',////\d+[a-zA-Z_]+|[a-zA-Z_]+\d+/
							width : 120,
							editor : new Ext.form.TextField({
				    		   allowBlank : false,
				    		   regex :/[\A-Za-z0-9\-]+$/,
				    		   regexText: '编号格式不正确，只能输入字母，数字，横杠',
				    		   maxLength : 15,
						       maxLengthText : '输入长度限制15位内 !'
			    	        }),			
			    	        renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
				            	if(value==""){
				            		cellmeta.css = 'processCellRed'; 
				            	}
			            		return value;
			           		 }
						},{
							header : '间隔值' + commonality_mustInput,
							dataIndex : 'taskInterval',
							width : 85
						},{
							header : '任务类型' + commonality_mustInput,
							dataIndex : 'taskType',
							width : 72
						},

						{
						     header :'接近方式' + commonality_mustInput,
						     dataIndex : 'reachWay',   //有参见时的 接近方式 中文
							 width : 350,
							 editor :new Ext.form.TextArea({
								allowBlank : false,
								grow : true
							}),			
					    	renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
									if(record.get("taskCode")==""){
					            		cellmeta.css = 'processCellRed'; 
					            	}
					            	if(tempReachWay && tempReachWay!=value){
					            		cellmeta.css = 'none'; 
					            	}
					            	return value;
					           }
						},
						{
						    header :'任务描述' + commonality_mustInput,
						     dataIndex : 'taskDesc',  //有参见时的 任务描述 中文
						     width : 350,
							 editor :new Ext.form.TextArea({
								 allowBlank : false,
								 grow : true
							 }),			
		 					 renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
									if(record.get("taskCode")==""){
					            		cellmeta.css = 'processCellRed'; 
					            	}
					            	if(tempTaskDesc && tempTaskDesc!=value){
					            		cellmeta.css = 'none'; 
					            	}
					            	return value;
	                        }
						},{
						     header :'转入区域',
						     dataIndex : 'ownArea',
							 width : 90
						},{
							header : '是否区域候选任务' + commonality_mustInput, 
							dataIndex : 'needTransfer', 
							width : 130,						
							renderer : function(value, cellmeta, record){
						     	if (value == 1){
						     		return commonality_shi;
						     	} else if (value == 0){
						     		return commonality_fou;
						     	}
							}
						},{
							header : "协调单",
							dataIndex : "taskId",
							renderer : function(value, cellmeta, record) {
								return returnvalue = "<a href='javascript:void(0)' title='新增协调单'>"
								+ "<img src='"+ contextPath+ "/images/toAuditBtn.gif' onclick='tempRecord()'/></a>"
								+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' title='协调单详情'>"
								+ "<img src='"+ contextPath+ "/images/toCheckBtn.gif'"
								+"onclick='getRecord()'/></a>";
							}
						},{
						     header :'转移原因',      //有参见时的 转移原因 中文
						     dataIndex : 'whyTransfer',
							 width :200,
							 editor : new Ext.form.TextArea({grow : true}),
							 renderer : changeBR
						}
				    ]);  ////if "N/A" columnModel end !
			
			}else{   //没有参见时 colM lh1---lh2 lh3
				var colM = new Ext.grid.ColumnModel([
					{
						header : 'MSG-3任务号' + commonality_mustInput,
						dataIndex : 'taskCode',////\d+[a-zA-Z_]+|[a-zA-Z_]+\d+/
						width : 120,
						editor : new Ext.form.TextField({
			    		   allowBlank : false,
			    		   regex :/[\A-Za-z0-9\-]+$/,
			    		   regexText: '编号格式不正确，只能输入字母，数字，横杠',
			    		   maxLength : 15,
					       maxLengthText : '输入长度限制15位内 !' 
			    	    })
					},{
						header : '间隔值' + commonality_mustInput,
						dataIndex : 'taskInterval',
						width : 85
					},{
						header : '任务类型' + commonality_mustInput,
						dataIndex : 'taskType',
						width : 72
					},
					
					{
					     header :'接近方式' + commonality_mustInput,
					     dataIndex : 'reachWay',    //没有参见时的 接近方式 中文
						 width : 350,
						 editor :new Ext.form.TextArea({allowBlank : false, grow : true}),
	         		  	 renderer : changeBR
					},
					{
					   	 header :'任务描述' + commonality_mustInput,
					     dataIndex : 'taskDesc',      //没有参见时的 任务描述 中文
					     width : 350,//110
						 editor :new Ext.form.TextArea({allowBlank : false, grow : true}),
	         		 	 renderer : changeBR
					},{
					     header :'转入区域',
					     dataIndex : 'ownArea',
					     id :"ownArea",
					     name : "ownArea",
						 width : 90,
						 editor : new Ext.form.TextField({})
					},
					{
					     header :'是否区域候选任务' + commonality_mustInput,
					     dataIndex : 'needTransfer',
						 width : 130,
						 editor : orMoveCombo,
						 renderer : function(value, cellmeta, record){
					     	var index = orMoveCombo.store.find(Ext.getCmp('OrMove').valueField,value);				    
					     	var record = orMoveCombo.store.getAt(index);
					    	var returnvalue = "";
					    	if (record){
					        	returnvalue = record.data.displayText;
							}
					    	return returnvalue;
						}
					},{
							header : "协调单",
							dataIndex : "taskId",
							renderer : function(value, cellmeta, record) {
								return returnvalue = "<a href='javascript:void(0)' title='新增或者修改协调单'>"
								+ "<img src='"+ contextPath+ "/images/toAuditBtn.gif' onclick='tempRecord()'/></a>"
								+"&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' title='协调单详情'>"
								+ "<img src='"+ contextPath+ "/images/toCheckBtn.gif'"
								+"onclick='getRecord()'/></a>";
							}
						},{
					     header :'转移原因',
					     dataIndex : 'whyTransfer',   //没有参见时的 转移原因 中文
						 width :200,
						 editor :new Ext.form.TextArea({ grow : true}),
	         			 renderer : changeBR
					}
					]); /////
			} 
			
		
			var grid = new Ext.grid.EditorGridPanel({
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				title :'任 务',
				store : store,
				//autoWidth : true,
				clicksToEdit : 2,   
				stripeRows : true,
				bodyStyle:"border:1px solid #DFE8F6 ;overflow-x:hidden;overflow-y:auto"
				});
			
				
			//取得所有RadioGroup的值
	function allRadioGroupValues(){
		var allRadio = [null, null, null, null, null];
		allRadio[0] = getRadioGroupValue('GVIRadio');
		allRadio[1] = getRadioGroupValue('DETRadio');
		allRadio[2] = getRadioGroupValue('FNCRadio');
		allRadio[3] = getRadioGroupValue('DISRadio');
		allRadio[4] = getRadioGroupValue('reDesignRadio');
		return allRadio;
	}	
	
	grid.addListener("rowclick",function(grid, rowindex, e){
	    tempRecords = store.getAt(rowindex);
		rId = tempRecords.get("taskId");
	})
			
				///判断任务应该插入的行号
	function insertIndex(type){
		var index = 0;
		var allRadio = allRadioGroupValues();
		if (type == GVI){
			return 0;
		} else if (type == DET){
			if (allRadio[0] == 1){
				index = index + 1;
			}					
		} else if (type == FNC){
			if (allRadio[0] == 1){
				index = index + 1;
			}
			if (allRadio[1] == 1){
				index = index + 1;
			}
		} else if (type == DIS){
			if (allRadio[0] == 1){
				index = index + 1;
			}
			if (allRadio[1] == 1){
				index = index + 1;
			}
			if (allRadio[2] == 1){
				index = index + 1;
			}
		}				
		return index;
	}
			 //添加任务
			function addTask(type){
				var index = insertIndex(type);
				var SystemRole = grid.getStore().recordType;
				var p = new SystemRole({
                    taskId:'',
					taskCode : '',						
					taskInterval: resultLh4Value,	
					taskType: type,	
					reachWay: '',	
					taskDesc: '',	
					needTransfer: '0',	
					ownArea: '',	
					whyTransfer: ''
				});	
					store.insert(index, p);	
			 }
			grid.addListener("afteredit", afteredit);
			grid.addListener("beforeedit", beforeedit);
			function beforeedit(val){
			 	if (val.field == 'reachWay'){
			 		tempReachWay = val.value;
            	}
			 	if (val.field == 'taskDesc'){
			 		tempTaskDesc = val.value;
			 	}
			}
			//创建GRIDPANEL的JSON
			function createGridJSON(){
				var json = [];
				for (var i = 0; i < store.getCount(); i++){
					json.push(store.getAt(i).data);
				}				
				return Ext.util.JSON.encode(json);
			}
			//判断整个页面是否有值改变
			 function testIsChange(){	
				newGrid = createGridJSON();
				var newTypeValues = Ext.util.JSON.encode(typeForm.form.getValues(false));
				if (store.modified.length != 0 || oldTypeValues != newTypeValues
					||jsonGVI.length!=0||jsonDET.length!=0||jsonFNC.length!=0||jsonDIS.length!=0||arryDelCoo.length!=0){
					return false;
				} else {
					return true;
				}
			}
			
			/*
			 * 判断区域字段 填写是否是正确的 区域编号
			 */
						//验证数字
							function testNum(value){
								var re = /^[\d]+$/;
								if (re.test(value)){
									return true;
								} else {
									return false;
								}
								return true;
							}
							//验证任务号
							function testTaskCode(value){
								var re = /[\A-Za-z0-9\-]+$/;
								if (re.test(value)){
									return true;
								} else {
									return false;
								}
								return true;
							}
							
	function afteredit(val){
		var nowRecord = val.record;
		var msg3TaskId = nowRecord.get('taskId');
    	if (val.field == 'taskCode'){
    		if (testTaskCode(val.value)){
				var cou = 0;
				for (var i = 0; i < store.getCount(); i++){
					if (store.getAt(i).get('taskCode') == val.value){
						cou = cou + 1;
					}
				}
				if (cou > 1){
					alert('MSG-3 任务编号不能重复!');
					nowRecord.set('taskCode', val.originalValue);
					return false;
				}
			} else {
				nowRecord.set('taskCode', val.originalValue);
			}
		
			if (val.value == ''){
				return false;
			}
			Ext.Ajax.request({
				url : contextPath+"/lhirf/lh_5/verifyMsgTaskCode.do",
				params:{
				msg3TaskId:msg3TaskId,	
				msgTaskCode:val.value
						},
				success : function(response) {
						if(response.responseText){
							alert('任务编号 :'+response.responseText+' 已经存在请重新填写!');// 任务编号已经存在>
							nowRecord.set('taskCode', val.originalValue);
							return false;
						}	
					}
				})
	    }
		if (val.field == 'ownArea'){
			var nowRecord = val.record;
			var zone = val.value.split(",");
			if(nowRecord.get('needTransfer')==1&&(val.value==null||val.value.trim()=='')){
				alert('是否区域候选任务选择 "是" 时,必须填写转入区域 !');
				nowRecord.set('ownArea',val.originalValue);
				return;
			}
			if (val.value == ''){
				return;
			}
			var zone = val.value.split(",");			
			for (var i = 0; i < zone.length; i++){
				if (zone[i] == ''){
					continue;
				}
			if (!testNum(zone[i])){
					alert('区域编号输入不符合规范 !');// 区域输入错误，请用‘,’分隔';
					nowRecord.set('ownArea', val.originalValue);
					return false;
					// break;
				}
			}
		Ext.Ajax.request({
			url : contextPath+"/struct/verifyArea.do?",
			params:{
				verifyStr:val.value
					},
			success : function(response) {
				if(response.responseText){
					var msg= Ext.util.JSON.decode(response.responseText);
					if(msg.exists){
						alert("区域:"+ msg.exists +"不能重复,请重新修改")
						nowRecord.set('ownArea',val.originalValue);
						return ;
					}
					if(msg.unExists){
						alert("区域:"+ msg.unExists+"不存在,请重新修改")
						nowRecord.set('ownArea',val.originalValue);
						return ;
					}
					if (msg.success) {
						alert("不能转入区域:"+ msg.success+"，该区域已被冻结或者已经审批完成，请重新修改")
						nowRecord.set('ownArea',val.originalValue);
						return ;
					}
				}
				}
			})
		} 
		if(val.field == 'needTransfer'){
			if(val.value==1&&(nowRecord.get('ownArea')==null||nowRecord.get('ownArea').trim()=='')){
				alert('请先填写转入区域 !');
				nowRecord.set('needTransfer',val.originalValue);
				return;
			}
		}
}
	
	
			//判断五个RadioGroup是否都已经选择
	function testAllRadioGroup(){
		var allRadio = allRadioGroupValues();
		for (var i = 0; i <= 4; i++){
			if (allRadio[i] == null){
				return false;
			}
		}
		return true;
	}
			
			function testInput(){
				if (refHsiCode != NA){  //有参照的时候
					if (!testAllRadioGroup()){
						alert('所参考的HSI 中LH5步骤有更改!请等其分析完成 !');
						return false;
					}
				}else{
					if (!testAllRadioGroup()){
						alert('请确定全部的任务类型 !');
						return false;
					}
				}
				for (var i = 0; i < store.getCount(); i++){
					var record = store.getAt(i);
                   if (record.get('taskCode') == null ||record.get('taskCode') =="N/A" || record.get('taskCode') ==''){
						alert('请填写任务编号 !');
						return false;
					}
					var taskDescCont = '';
					if(record.get('taskDesc') != null){
					   taskDescCont = record.get('taskDesc').trim();
					}
					
					if (taskDescCont == '' ){ ///任务接描述 中英文不可同时为空 !
						alert('任务描述填写项不可为空 ! ');
						return false;
					}
					var reachWay = '';
					if(record.get('reachWay') != null){
					   reachWay1 = record.get('reachWay').trim();
					}
					if (reachWay1 == ''){///任务 接近方式中英文 不可同时为空!
						alert('接近方式填写项不可为空 !');
						return false;
					}
					if (record.get('needTransfer') == '1'  &&  (record.get('ownArea') == '' || record.get('ownArea') == null)){
						alert('是否区域候选任务选择 "是" 时,必须填写转入区域 !');   ///转移任务选择 "是" 时,必须填写转入区域!
						record.set('needTransfer',"0")
						 return false;
					}
					
				}
			   return true;
			}
			//点击下一步执行的方法
			nextOrSave = function (value){
				   if( value == null){
				       value =6;
				     }
				    if(isMaintain != '1'){
				   ///  alert(commonality_NoAuthority);
					   goNext(value);
					   return false;
				       }
			     if(!testIsChange()){  //是否修改了呢
			          var radioValue = allRadioGroupValues();
							Ext.MessageBox.show({
							title : commonality_affirm,
							 msg : commonality_affirmSaveMsg,			    
						    buttons : Ext.Msg.YESNOCANCEL,
						    fn : function(id){
						    	if (id == 'cancel'){
									return;
								} else if (id == 'yes'){
									 if(!testInput()){
									       return false ;
									     }
								     var modified = store.modified;
									 save(radioValue,modified,value);
									  
								} else if (id == 'no'){
					    	         goNext(value);
								}
						    }
						});
			     }else{  //没有修改页面直接跳转
			         goNext(value);
			     } 
			}
			 
		function save(radioValue,modified,value){
			//alert(arrtask.length);
			       var json = [];
					Ext.each(modified, function(item) {
						json.push(item.data);
					});
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true
						});	
					
					waitMsg.show();
					Ext.Ajax.request( {
		        	url : lh5.app.g_saveLh5Url,    
		         	method : 'POST',
		         	params : {
						hsiId      : hsiId,
						arryDeltTaskId : arrtask ,
						arryDelCooId : arryDelCoo,
						gviAvl    : radioValue[0],
						gviDesc : Ext.getCmp('gviDesc').getValue(),
						detAvl    : radioValue[1],
						detDesc : Ext.getCmp('detDesc').getValue(),
						fncAvl    : radioValue[2],
						fncDesc : Ext.getCmp('fncDesc').getValue(),
						disAvl    : radioValue[3],
						disDesc : Ext.getCmp('disDesc').getValue(),
						needRedesign : radioValue[4],
						redesignDesc : Ext.getCmp('redesignDesc').getValue(),
						jsonData : Ext.util.JSON.encode(json),
						jsonGVI : Ext.util.JSON.encode(jsonGVI),
						jsonDIS : Ext.util.JSON.encode(jsonDIS),
						jsonFNC : Ext.util.JSON.encode(jsonFNC),
						jsonDET : Ext.util.JSON.encode(jsonDET)
		 			},  
		 			success : function(response, action) {
						waitMsg.hide();
						var text = Ext.util.JSON.decode(response.responseText);
						if (text&&text.msg == 'success') {
							store.load();
							alert(commonality_messageSaveMsg);
							store.modified = [];
							jsonGVI=[];
							jsonDIS=[];
							jsonFNC=[];
							jsonDET=[];
							parent.refreshTreeNode();
						    goNext(value);
						}
					},
					failure : function(form,action) {
						waitMsg.hide();
						alert( commonality_cautionMsg);
					}   
			});
		}	
			 
			   arrtask = new Array() ;  //数组 taskId //删除记录
			function deleteTask(type){  
				for (var i = 0; i < store.getCount(); i++){
					var record = store.getAt(i);
					if (record.get('taskType') == type){
						 if( record.get('taskId')== ''){
							  store.remove(record);  //没有保存数据库的  直接remove 
						 }else{
							 var j = arrtask.length ;
							 arrtask[j] = record.get('taskId');
							 store.remove(record); 
						 }
					}
				}				
				Ext.each(store.modified, function(item) {
					if(item.data.taskType==type){
						store.modified.remove(item);
				    	}
					});	
					if(type==FNC){
						jsonFNC=[];
					}else if(type==GVI){
						jsonGVI=[];
					}else if(type==DIS){
						jsonDIS=[];
					}else{
						jsonDET=[];
					}
			}

			//给RadioGroup赋值
			function setRadioGroupValue(radioGroupName, value){
				Ext.getCmp(radioGroupName).eachItem(function(item){
				    item.setValue(item.inputValue == value);
				});
			}
			
			//取得RadioGroup的值
			function getRadioGroupValue(radioGroupName){
				var result = null;
				Ext.getCmp(radioGroupName).eachItem(function(item){   
				    if (item.checked === true) result = item.inputValue;   				       
				});
				return result;
			}
			
			
			//是否能选择“是”
			function isChangeOK(){
				var reDesignValue = getRadioGroupValue('reDesignRadio');
				if (reDesignValue == 1){
					alert('已选择了重新设计，不能再选择其他项目 !');
					return false;
				}		
				return true;
			}
			
		
			//GVI监听
			function changeGVI(combo ,newValue, oldValue){
				if (newValue.inputValue == 1){
					if (!isChangeOK()){
						  setRadioGroupValue('GVIRadio', 0);
						return false;
					}
					addTask(GVI);
				} else {
					deleteTask(GVI);
					Ext.getCmp('gviDesc').setValue('');
				}
			}
				//DET监听
			function changeDET(combo ,newValue, oldValue){
				if (newValue.inputValue == 1){
					if (!isChangeOK()){
						setRadioGroupValue('DETRadio', 0);
					    //Ext.getCmp('DETRadio').setValue('0');
						return false;
					}
					addTask(DET);
				} else {
					deleteTask(DET);
					Ext.getCmp('detDesc').setValue('');
				}
			}
			//FNC监听
			function changeFNC(combo ,newValue, oldValue){
				if (newValue.inputValue == 1){
					if (!isChangeOK()){
						
						setRadioGroupValue('FNCRadio', 0);		
					      //Ext.getCmp('FNCRadio').setValue('0');
						return false;
					}
					addTask(FNC);	
				} else {
					deleteTask(FNC);
					Ext.getCmp('fncDesc').setValue('');
				}
			}
				//DIS监听
			function changeDIS(combo ,newValue, oldValue){
				if (newValue.inputValue == 1){
					if (!isChangeOK()){
						setRadioGroupValue('DISRadio', 0);
						return false;
					}
					addTask(DIS);
				} else {
					deleteTask(DIS);
				}
			}
			//判断是否能选择重新设计
			function isChangeReDesignOK(){
					var num = 0;
					var allRadio = [null, null, null, null];
					allRadio[0] = getRadioGroupValue('GVIRadio');
					allRadio[1] = getRadioGroupValue('DETRadio');
					allRadio[2] = getRadioGroupValue('FNCRadio');
					allRadio[3] = getRadioGroupValue('DISRadio');
					for (var i = 0; i <= 3; i++){
						if (allRadio[i] == 0){
							num = num + 1;
						}
					}
					if (num == 4){
						return true;
					} else {
						alert('只有其他四项全为“否”时，才能选择重新设计 !');
						return false;
					}
			}
				
			//重新设计监听
			function changeReDesign(combo ,newValue, oldValue){
				if (newValue.inputValue == 1){
					if (!isChangeReDesignOK()){
						setRadioGroupValue('reDesignRadio', 0);
					      ///Ext.getCmp('reDesignRadio').setValue(0);
						return false;
					}					
				}
			}
			//如果是相似HSI，则RadioGroup和TextArea无效
			function isRef(refHsiCode){
				if (refHsiCode != 'N/A'){
					GVIRadioGroup.disable();
					DETRadioGroup.disable();
					FNCRadioGroup.disable();
					DISRadioGroup.disable();
					reDesignRadioGroup.disable();
					Ext.getCmp('gviDesc').disable();
					Ext.getCmp('detDesc').disable();
					Ext.getCmp('fncDesc').disable();
					Ext.getCmp('disDesc').disable();
					Ext.getCmp('redesignDesc').disable();
				}
			}
			
			var view = new Ext.Viewport({		
				layout: 'border',
				items : [	
					{
						region : 'center',
						layout : 'fit',
						items : [grid]
					},{
						region : 'north',
						layout : 'border',
						height : 296,		 
						items : [
							{
								region : 'center',
								layout : 'fit',
								title : returnPageTitle('任务间隔确定','lh_5'),
								height : 50,
								items : [HelpForm]
							},{
								region : 'west',
								layout : 'fit',
								width : 255,  
								height : 50,		 
								items : [isSafeForm]
							},{
								region : 'south',
								layout : 'fit',
								//split : true,  
								height : 148,		 
								items : [typeForm]
							},{
							    layout : 'fit',
								region : 'north',
			    		        height : 60,
			    		        frame : true,
			    		        items : [headerStepForm]
							}
						]
					}
				]
			});
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~画面  end ~~~~~~~~~~~~~~~~
			 //初始化LH5页面参数
			 function initLh5(is_safe,gvi_avl,det_avl,fnc_avl,dis_avl,need_redesign){
			 	    if (is_safe == 1){			
						Ext.getCmp('safe1').show();
					}else {
						Ext.getCmp('safe2').show();
						}
					Ext.getCmp('GVIRadio').setValue(gvi_avl);	
					Ext.getCmp('DETRadio').setValue(det_avl);
					Ext.getCmp('FNCRadio').setValue(fnc_avl);
					Ext.getCmp('DISRadio').setValue(dis_avl);
					Ext.getCmp('reDesignRadio').setValue(need_redesign);
					//jsp中的 中英文切换问题
					 document.getElementById("rangTing").innerHTML = '敏感度等级';
					 document.getElementById("intValue").innerHTML = '间隔值';
					
			        ////setRadioGroupValue('GVIRadio',gvi_avl);	
					
				}
				
				store.on('beforeload', function(){
				store.baseParams = {
				    hsiId :hsiId
				};
				 initLh5(isSafe,gviAvl,detAvl,fncAvl,disAvl,needRedesign);	
			});
		store.on("load",function (){
			      setTimeout("tttt()",1);
		
			});
		tttt= function(){
				GVIRadioGroup.addListener("change", changeGVI);
				DETRadioGroup.addListener("change", changeDET);
				FNCRadioGroup.addListener("change", changeFNC);
				DISRadioGroup.addListener("change", changeDIS);
				reDesignRadioGroup.addListener("change", changeReDesign);
				
			}
			setStoreClass =  function  () {
				//alert("验证染色");
				 oldTypeValues = Ext.util.JSON.encode(typeForm.form.getValues(false));
			/*	var girdcount = 0; 
				for (var i = 0; i < store.getCount(); i++) {
					var record = store.getAt(i);
					if(record.get('taskCode') == ''){ //'#FFFF00'; 
		                  grid.getView().getCell(girdcount,1).style.backgroundColor = '#FFFF00';  
		                  grid.getView().getCell(girdcount,1).style.backgroundColor = '#FFFF00';  
		                  grid.getView().getCell(girdcount,4).style.backgroundColor = '#FFFF00'; //接近方式
		                  grid.getView().getCell(girdcount,5).style.backgroundColor = '#FFFF00';  
		                  grid.getView().getCell(girdcount,6).style.backgroundColor = '#FFFF00'; //任务描述
		                  grid.getView().getCell(girdcount,7).style.backgroundColor = '#FFFF00';
		                  ///
		                  grid.getView().getCell(girdcount,1).style.color = '#000000';  
		                  grid.getView().getCell(girdcount,1).style.color = '#000000';  
		                  grid.getView().getCell(girdcount,4).style.color = '#000000'; //接近方式字体
		                  grid.getView().getCell(girdcount,5).style.color = '#000000';  
		                  grid.getView().getCell(girdcount,6).style.color = '#000000'; //任务描述字体
		                  grid.getView().getCell(girdcount,7).style.color = '#000000';
		            }
		            girdcount = girdcount + 1; 
				}*/
			}
			store.on('load', function(st){	
				 setTimeout("setStoreClass()",20);
				
			});
			store.load();
			isRef(refHsiCode);
			
          ////   ~~~~~~~~~~~~~~ 
           }
          } ;
  }();