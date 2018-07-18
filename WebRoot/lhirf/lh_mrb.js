
// 创建命名空间
var comboStore=null;
var oldValue=null;
var thisId = null;
var isFlag = false;
var delId = new Array();
///Ext.namespace('lhmrb');
Ext.override(Ext.grid.RowSelectionModel, {
    onEditorKey : function(field, e) {
         
        var k = e.getKey(), newCell, g = this.grid,l = g.lastEdit, ed = g.activeEditor;
        var shift = e.shiftKey;
        
        if(ed){
        }else{
            ed=l;
        }
        
        if (k == e.ENTER) {
            e.stopEvent();
            if(ed){
                //ed.completeEdit();
            }
        } else if (k == e.TAB) {
            e.stopEvent();
            ed.completeEdit();
            if (this.moveEditorOnEnter !== false) {
                if (shift) {
                    newCell = g.walkCells(ed.row - 1, ed.col, -1,
                            this.acceptsNav, this);
                } else {
                    newCell = g.walkCells(ed.row, ed.col + 1, 1,
                        this.acceptsNav, this);
                }
            }
        } 
        if (newCell) {
            g.startEditing(newCell[0], newCell[1]);
        }
    }
});
  Ext.onReady(function() {
        	   
        	   var currentRowIndex = -1;
   			  var currentRecord; 
   			  isMakeMrb = 'domrb' ;
   			  var tempStore = new Ext.data.Store();
   			//任务类型 combox  选择 
  			var taskChooseCombo = new Ext.form.ComboBox({   
  				xtype : 'combo',
  				name : 'taskTypeChoose', id : 'taskTypeChoose',
  				store : new Ext.data.SimpleStore({
  				
  				fields : ["retrunValue", "displayText"],
  					data : [[GVI,GVI],[DET,DET],[FNC,FNC],[DIS,DIS]]
  				}),  
  				valueField : "retrunValue", displayField : "displayText",
  				typeAhead : true, mode : 'local',
  				triggerAction : 'all', width : 100,
  				editable : true, selectOnFocus : true
  			});
  			
  			//IPVcombox  选择 
  			var threeChooseCombo = new Ext.form.ComboBox({   
  				xtype : 'combo',
  				name : 'threeChoose', id : 'threeChoose',
  				store : new Ext.data.SimpleStore({
  				fields : ["retrunValue", "displayText"],
  					data : [['IPV','IPV'],['OPVP','OPVP'],['OPVE','OPVE']]
  				}),  
  				valueField : "retrunValue", displayField : "displayText",
  				typeAhead : true, mode : 'local',
  				triggerAction : 'all', width : 100,
  				editable : true, selectOnFocus : true
  			});
	
		
		 	     ////加载LHIRF mrb任务。
			var storeMrb = new Ext.data.Store({   
			    url : contextPath + "/lhirf/lh_mrb/loadMrbTask.do",
				reader : new Ext.data.JsonReader({
					root : 'mrb',
					fields : [ 
						{name:'mrbId'},
					    {name:'mrbCode'},
			            {name:'taskType'},
			            {name:'ipvOpvpOpve'},
			            {name:'taskInterval'},
			            {name:'reachWay'},
			             {name:'taskDesc'},
			             {name:'mrbownArea'},
			            {name:'effectiveness'}
			          
					]
				})
			});
			    
			    var storeMsg = new Ext.data.Store({
			    url : contextPath + "/lhirf/lh_mrb/loadlhMsgTask.do",
				reader : new Ext.data.JsonReader({
					root : 'lhMsg',
					fields : [ 
						{name:'taskId'},
					    {name:'areaCode'},
					    {name:'hsiCode'},
			            {name:'taskCode'},
			            {name:'taskType'},
			            {name:'ipvOpvpOpve'},
			            {name:'taskInterval'},
			            {name:'reachWay'},
			            {name:'taskDesc'},
			            {name:'mrbId'},
			            {name:'lheff'},
					    {name:'mrbTaskCode'}
					]
				})
				
			});
			/////////////////////////////////////mrb下拉框
			
				     
					comboStore = new Ext.data.Store({
						   url : contextPath + "/lhirf/lh_mrb/getmrbCombox.do",
						    reader : new Ext.data.JsonReader( {
						      root : 'mrbCombox',
						      fields : [ 
						       	{name:'mrbId'},
							    {name:'mrbCode'},
					            {name:'taskType'},
					            {name:'ipvOpvpOpve'},
					            {name:'taskInterval'}
						      ]
						    })
				  	});
				  		var modelSelect = new Ext.form.ComboBox({
					        width :120,
					        name : 'mrbTaskCode',
					        id : 'mrbTaskCode',
					        store : comboStore,
					        mode : 'local',
					        valueField : 'mrbId',
					        displayField : 'mrbCode',
					        bdcd:'taskType',
					        editable : true,
					        selectOnFocus : true
				        	
				            }); 
				            
				    comboStore.on('load',function(e){
				     	Ext.each(comboStore.data.items, function(item) {
									tempStore.add(item);
								});
				   	});  
//				   comboStore.load();
                 	modelSelect.on('specialkey',function(f,e){
   	                    if (e.getKey() == e.ENTER) {
                             f.setValue(f.lastQuery)
                          }else{
			                f.setValue(f.lastQuery)
			            	delRecord(oldValue);
			            }
        	          });  
        	          
    	          	modelSelect.on('select', function(f, e) {
						isFlag = true;
						var record = gridMsg.getSelectionModel().getSelected();
						selectValue=f.lastQuery
						record.set("mrbTaskCode", e.get('mrbCode'));
					});
				    storeMsg.load();
               modelSelect.on('beforequery',function(e){
	   			record = gridMsg.getSelectionModel().getSelected();
		   					modelSelect.getStore().removeAll();
	   				  Ext.each(tempStore.data.items, function(item) {
	   					if(  item.get("taskType")== record.get("taskType") && 
		   					      item.get("ipvOpvpOpve")== record.get("ipvOpvpOpve") && 
	   					      item.get("taskInterval")== record.get("taskInterval") ){
		   						modelSelect.getStore().add(item);
		   					}
					});  
		         });           
		   	comboStore.load();
			
			modelSelect.on('blur', function(newValue) {

			});

				var colMsg = new Ext.grid.ColumnModel([
				{
					header : "taskId",
					dataIndex : "taskId",
					hidden : true
				},{
					header : "<div align='center'>"+'区域'+"</div>",
					dataIndex : "areaCode",
					align:'center',
					width : 70
				},{
					header : "<div align='center'>"+'HSI 编号'+"</div>",
					dataIndex : "hsiCode",
					width : 100
				},{
					header : "<div align='center'>"+'MSG-3任务号'+"</div>",
					dataIndex : "taskCode",
					width : 130
				},{
					header : "<div align='center'>"+'任务类型'+"</div>",
					dataIndex : "taskType",
					width : 75
				}, {
					header : "<div align='center'>IPV/OPVP/OPVE</div>",
					dataIndex : "ipvOpvpOpve",
					width : 100
				},{
					header : "<div align='center'>"+'任务间隔'+"</div>",
					dataIndex : "taskInterval",
					width : 126
				},{
					header : "<div align='center'>"+'接近方式'+"</div>",///接近方式中文
					dataIndex : "reachWay",
					editor : new Ext.form.TextArea({}),
					renderer : changeBR,
					width : 150
				},{
					header : "<div align='center'>"+'任务描述'+"</div>",   ///任务描述( 中文 )
					dataIndex : "taskDesc",
					editor : new Ext.form.TextArea({}),
					renderer : changeBR,
					width : 150
				},{ 
				    header:'MrbId',
					dataIndex :"mrbId",
					hidden:true
				},{ header:'适用性',
					dataIndex :"lheff",
					width : 150,
					editor : new Ext.form.TextArea({})
				},{
					header : "<div align='center'>"+'MRB任务号'+"</div>", 
					dataIndex : "mrbTaskCode",
					width : 110,
					editor : modelSelect,
					renderer: function(value, meta, record) {
					    var index = Ext.getCmp("mrbTaskCode").store.find(Ext.getCmp('mrbTaskCode').valueField,value);	
					    if (index = -1) {
							if (isFlag) {
								isFlag = false;
								return selectValue;
							} else {
								isFlag = false;
								return value;
							}
					   }
					    var record = Ext.getCmp("mrbTaskCode").store.getAt(index);
					    var returnvalue = "";
					    if (record){
					        returnvalue = record.data.mrbCode;
					    }else{
					    	returnvalue=value;
					    }
					    isFlag = false;
				    	return returnvalue;
					}
				}
			]);	
						
			var gridMsg = new Ext.grid.EditorGridPanel({
				cm : colMsg,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				loadMask: {msg:commonality_waitMsg},
				region : 'center',
				store : storeMsg,
				clicksToEdit : 2,   
				stripeRows : true,
				tbar : [
				          '<div style="text-align: center; width: 100%; height: 100%; padding-left: 15%;">'
	                       + '任务类型' + '</div>', "：",
						     taskChooseCombo,
					    '-',
					    "IPV/OPVP/OPVE", " :" , 
						 threeChooseCombo,
					   '-',
					   '任务间隔' , "：",{
								xtype : 'textfield',
								id:'taskInterval',
								name : 'taskInterval',
								width : 100
							},'-',
							new Ext.Toolbar.TextItem("&nbsp;"),
							{
								 text : commonality_search,
								 xtype : "button",
								 width : 80,
								 iconCls : "icon_gif_search",
										 handler : function() {
								         storeMsg.proxy = new Ext.data.HttpProxy({
										    	url : contextPath + "/lhirf/lh_mrb/loadlhMsgTask.do" 
											});
								         storeMsg.on("beforeload", function() {
										 storeMsg.baseParams = {
													taskType : Ext.get('taskTypeChoose').dom.value,	
													ipvOpvpOpve : Ext.get('threeChoose').dom.value,
													taskInterval : Ext.get('taskInterval').dom.value
										};
									});
										storeMsg.load();
								}					 
							 },'-',	
							 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;"),	
								{
								   	text:commonality_save,
									iconCls : "icon_gif_save",
									disabled : !authorityFlag,
									handler: function() {
							            if( storeMsg.modified.length !=0 || storeMrb.modified.length != 0){
							            	saveMrbMsg();
							            }else{
							            	alert(commonality_alertUpdate);
							            }
								    }
								 
								 }
				]
				
				
				 
				
			});
			gridMsg.addListener("cellclick", cellclick);
			gridMsg.addListener("afteredit", afteredit);
		///mrb
				var colMrb = new Ext.grid.ColumnModel([
				{
					header : "mrbId",
					dataIndex : "mrbId",
					hidden : true
				},
				{
					header : "<div align='center'>"+'MRB任务号'+"</div>", 
					dataIndex : "mrbCode",
					align :'center',
					width : 130
				},
				{
					header : "<div align='center'>"+'任务类型'+"</div>", 
					dataIndex : "taskType",
					align :'center',
					width : 70
				}, 
				{
					header : "<div align='center'>IPV/OPVP/OPVE</div>",
					dataIndex : "ipvOpvpOpve",
					align :'center',
					width : 90
				},
				{
					header : "<div align='center'>"+'任务间隔'+"</div>",
					dataIndex : "taskInterval",
					align :'center',
					width : 110
				},{
					header : "<div align='center'>"+'接近方式' + commonality_mustInput +"</div>",///接近方式中文
					dataIndex : "reachWay",
					width : 180,
					editor :new Ext.form.TextArea({
							allowBlank : false,
							grow : true
						}),
	         		renderer : changeBR
				},
				{
					header : "<div align='center'>"+'任务描述' + commonality_mustInput +"</div>",///任务描述(中文)
					dataIndex : "taskDesc",
					width : 180,
					editor :new Ext.form.TextArea({
	         				allowBlank : false,
							grow : true
	         			}),
	         		renderer : changeBR
				},
				{
					header : "<div align='center'>"+'区域' + commonality_mustInput +"</div>",
					dataIndex : "mrbownArea",
					width : 101,
					align :'center',
					editor : new Ext.form.TextField({
			    		   allowBlank : false
			    	    })
				},{
				     header : "<div align='center'>"+'适用性'+"</div>",
					 dataIndex : "effectiveness",
					 align :'center',
					 editor : new Ext.form.TextArea({ grow : true}),
					 width : 150
				}
			]);	
			   	var gridMrb = new Ext.grid.EditorGridPanel({
			   	title : 'L/HRIF  MRB任务' ,
				cm : colMrb,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				//autoExpandColumn :11,
				region : 'center',
				store : storeMrb,
				clicksToEdit : 2,   
				stripeRows : true
			});
			storeMrb.load();
			gridMrb.addListener("afteredit", afteredit1);
			
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
			function afteredit1(val){
				if (val.field == 'mrbownArea'){
					var nowRecord=val.record;
					var zone = val.value.split(",");
					if (val.value == ''){
						return true;
					}
					for (var i = 0; i < zone.length; i++){
						if (zone[i] == ''){
							continue;
						}
					if (!testNum(zone[i])){
							alert('区域输入错误，请用‘,’分隔');//区域输入错误，请用‘,’分隔';
							nowRecord.set('mrbownArea', val.originalValue);
							return false;
							//break;
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
								nowRecord.set('mrbownArea',val.originalValue);
								return ;
							}
							if(msg.unExists){
								alert("区域:"+ msg.unExists+"不存在,请重新修改")
								nowRecord.set('mrbownArea',val.originalValue);
								return ;
							}
							if (msg.success) {
								alert("不能转入区域:"+ msg.success+"，该区域已被冻结或者已经审批完成，请重新修改")
								nowRecord.set('mrbownArea',val.originalValue);
								return ;
							}
						}
						}
					})
				}
			}			
	
		var win = new Ext.Window({
			   ///title : ,
				layout : 'border',
				border : false, 
				resizable : true,
				closable : false,
				maximized : true,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				items : [
					{ 
						region : 'center',
						layout : 'fit',
					    title : returnPageTitle('L/HRIF MRB维护','lh_mrb'),
						split : true,
						items : [gridMsg]
					},
					{ 
						region : 'south',
						layout : 'fit',
						height : 240,
						split : true,
						items : [gridMrb]
					}
				]
			});	
			win.show();	
			//****************************************************************
			function cellclick(grid, rowIndex, columnIndex, e) {
				if(currentRowIndex != rowIndex){
					currentRowIndex = rowIndex;
					currentRecord = grid.getStore().getAt(rowIndex);
				}
			}
			gridMsg.addListener("beforeedit", beforeedit);
			function beforeedit(val){
				if(val.field=='mrbTaskCode'){
					oldValue=val.value;
					thisId=val.record.get('taskId');
				}
			}
			var isNotNull = false;
			function afteredit(val){
				var currentRecordNoW = val.record;
				if(val.field == 'mrbTaskCode'){
					 isNotNull = true;
					if(val.value==''){
						delRecord(oldValue);
						isNotNull=false;
					}
					var index = Ext.getCmp("mrbTaskCode").store.find(Ext.getCmp('mrbTaskCode').valueField,val.value);				    
				    if(index!=-1){
				    var record = Ext.getCmp("mrbTaskCode").store.getAt(index);
						val.value= record.data.mrbCode;
				    	val.record.set("mrbTaskCode",val.value);
				    }
					
		      		if(val.value==''||val.value==null){
		      			return false;
		      		}
		      		var re = /[\A-Za-z0-9\-]+$/;
					if (!re.test(val.value)){
						alert("编号格式不正确，只能输入字母，数字，'-' ");//MRB编号只可以输入汉字 字母或者"-"
						currentRecordNoW.set('mrbTaskCode', val.originalValue);
						return false;
					} 
					changeMrb(val.value,val);
				}
				///
				if (val.field == 'mrbTaskCode'){
					if (isMakeMrb == 'no'){
						currentRecordNoW.set('mrbTaskCode', val.originalValue);
					}
				}
				}	
						var topRecord = Ext.data.Record.create([
	    		                {name:'mrbId'},
							    {name:'mrbCode'},
					            {name:'taskType'},
					            {name:'ipvOpvpOpve'},
					            {name:'taskInterval'}
		]);  
			///		创建MRB 是添加一条记录  
			function add(mrb_Code,now_Record){
				var record  = now_Record ;
				var index = storeMrb.getCount();
				var rec = gridMrb.getStore().recordType;
				var p = new rec({
				     	mrbId: '',
				     	mrbCode:mrb_Code,
					    taskType: record.get('taskType'),
					    ipvOpvpOpve:record.get('ipvOpvpOpve'),
					    taskInterval:record.get('taskInterval'),
					    reachWay:'',
		                taskDesc:'',
			            mrbownArea:'',
			            effectiveness:record.get('lheff')
			        });
				//storeMrb.insert(index, p);
				storeMrb.add(p);
				var tempstore = new topRecord({
					    mrbId:"",
				      	mrbCode:mrb_Code,
					   taskType: record.get('taskType'),
					    ipvOpvpOpve:record.get('ipvOpvpOpve'),
					    taskInterval:record.get('taskInterval')
				});
				tempStore.add(tempstore);
			}
			
			
			//MRB号框失去焦点时进行的操作
			function changeMrb(mrbCode,e){
				var mrbRecord =gridMrb.getStore();
				var nowRecord1  = e.record;
				var taskTemp='';
				var taskIpvOpvpOpve='';
				var taskInterval='';
				if (nowRecord1 != null){
					taskTemp = nowRecord1.get('taskType');
					taskIpvOpvpOpve = nowRecord1.get('ipvOpvpOpve');
					taskInterval = nowRecord1.get('taskInterval');
				}
               if(mrbCode== " "){
            	   return false ;
               }
				if( mrbCode == null|| mrbCode ==''  ){
					return false ;
				}
				//
				var j=0;
				for(var i=0;i<mrbRecord.data.length;i++){
					if(mrbRecord.data.items[i].get("mrbCode")== mrbCode && 
					  mrbRecord.data.items[i].get("taskType")==taskTemp &&  mrbRecord.data.items[i].get("ipvOpvpOpve") == taskIpvOpvpOpve
					  && mrbRecord.data.items[i].get("taskInterval") == taskInterval){
								if(isNotNull){
									delRecord(oldValue);
								}
								return null;
					}
				}
				//
				for(var i=0;i<mrbRecord.data.length;i++){
					if(mrbRecord.data.items[i].get("mrbCode")== mrbCode && 
					  (mrbRecord.data.items[i].get("taskType")!=taskTemp ||  mrbRecord.data.items[i].get("ipvOpvpOpve") != taskIpvOpvpOpve
					   ||  mrbRecord.data.items[i].get("taskInterval") != taskInterval )){
								alert('不同种类型的任务不能合并 !');////'不是同种类型的任务不能合并'
								nowRecord1.set("mrbTaskCode", e.originalValue);
								return null;
					}
				}
				var flag = 0;
					for (var i = 0; i < storeMrb.getCount(); i++){
					     var combMrb = storeMrb.getAt(i).get("mrbCode");
						  if(mrbCode == combMrb ){
						     flag =1 ;
						     break;
				           }
				    }
				if( flag == 0){
					if( confirm('是否创建MRB任务'+mrbCode)){
						 verifyMrbCode(mrbCode, e)
					}else{   //不创建时
						isMakeMrb = 'no';
						nowRecord1.set("mrbTaskCode", e.originalValue);
					}
					
				}
			}
			
			///验证  mrbCode  是唯一的
		function verifyMrbCode(mrbCode, e) {
		  var	recordNow = e.record;
		  var mrbCodeVerifyId = recordNow.get('mrbId');///用来校对mrbCode修改后 再次填写 判断是不是同一个
			Ext.Ajax.request({
				 url : contextPath + "/lhirf/lh_mrb/verifyMrbCode.do",
				  params : {
				  	mrbCodeVerifyId:mrbCodeVerifyId,
					mrbCode : mrbCode
				},
				method : "POST",
				success : function(response) {
					var text = Ext.decode(response.responseText);
					if (text.message) {
						add(mrbCode,e.record);
						if (oldValue) {
							delRecord(oldValue);
							isNotNull = false;
						}
					} else {
						alert('此任务编号可能在其他系统已存在,请修改 !');  //此任务编号可能在其他系统已存在,请修改
						record.set("mrbTaskCode", e.originalValue);
					}
				}
				});
		}
			///验证 mrbCode 是唯一的end\
			
			//验证MRB 保存时不可为空的
			function testMrb(){
				for (var i = 0; i < storeMrb.getCount(); i++){
				       var record = storeMrb.getAt(i);
				          
					     if(record.get('reachWay') == null){
					       alert('接近方式不可为空 !') ;
					       return false ;
					     } 
					     if(record.get('taskDesc') == null){
					       alert('任务描述不可为空  !') ;
					       return false ;
					     }
					     if(record.get('mrbownArea') == null || record.get('mrbownArea') == ''){
					       alert('MRB任务中区域不可为空 !') ;
					        return false ;
					     }  
					     if(record.get('effectiveness') == null || record.get('effectiveness') == ''){
					       alert('MRB任务有效性不可为空 !');
					       return false ;
					     } 
				   }
				 return true ;    
			}
			
			///保存mrb 表 ,以及保存msg添加的code 变动
	function saveMrbMsg(){
		 Ext.MessageBox.show({
			    title : commonality_affirm,
			    msg : commonality_affirmSaveMsg,
			    buttons : Ext.Msg.YESNOCANCEL,
			    fn : function(id){
			    	if (id == 'cancel'){
						return;
					} else if (id == 'yes'){
						if(!testMrb()){
							   return false ;
							  }
						save();
					} else if (id == 'no'){
						return ;
					}
			    }
			 });  
		}		
	function save(){
	   		var json = [];
			Ext.each(storeMrb.modified, function(item) {
				json.push(item.data);
			});
			var jsonmsg = [];
			for(var i=0;i<storeMsg.getCount();i++){
				jsonmsg.push(storeMsg.getAt(i).data);
			}
			var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : commonality_waitMsg,
				    removeMask : true// 完成后移除
				});		
			waitMsg.show();
			Ext.Ajax.request( {
				url :  contextPath + "/lhirf/lh_mrb/saveMrbMsg.do",
				method : "POST",
				params : {
					jsonData : Ext.util.JSON.encode(json),
					deleteMrbId:delId,
					jsonDataMsg : Ext.util.JSON.encode(jsonmsg)
				},
				success : function(form,action) {
					waitMsg.hide();
					//成功 
					if(oldValue){
						delRecord(oldValue);
						isNotNull=false;
					}
					storeMrb.modified =[];
					storeMsg.modified =[];
					storeMsg.load();
					storeMrb.load();
					tempStore.removeAll();
					delId=[];
					modelSelect.getStore().load();
					alert(commonality_messageSaveMsg);
				},
				failure : function(form,action) {
					waitMsg.hide();
					alert(commonality_cautionMsg);
				}
			});	
		}

			//删除一行的record
		function delRecord(oldValue){
			    if(oldValue){
                	var count1=0
                	for(var cc=0;cc<storeMsg.getCount();cc++){
                		if(oldValue == storeMsg.getAt(cc).get("mrbTaskCode")){//mrbTaskCode
                			count1++;
                		}
                	}
                	if(count1==0){
                		for(var cc=0;cc<storeMrb.getCount();cc++){
                			if(oldValue==storeMrb.getAt(cc).get("mrbCode")){
                				delId.push(storeMrb.getAt(cc).get("mrbId"));
                				storeMrb.remove(storeMrb.getAt(cc));
                				for(var c=0;c<tempStore.getCount();c++){
                					if(oldValue==tempStore.getAt(c).get('mrbCode')){
		                				tempStore.remove(tempStore.getAt(c))
                					}
                				}
                				for(var c=0;c<modelSelect.getStore().getCount();c++){
                					if(oldValue==modelSelect.getStore().getAt(c).get('mrbCode')){
		                				modelSelect.getStore().remove(modelSelect.getStore().getAt(c))
                					}
                				}
                			}
                		}
                		for(var cc=0;cc<storeMrb.modified.length;cc++){ 
                			if(oldValue==storeMrb.modified[cc].get('mrbCode')){
                				storeMrb.modified.remove(storeMrb.modified[cc]);
                				storeMrb.modified;
                			}
                		}
                	}
                }
		}
			
               //===========================
          })

