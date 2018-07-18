Ext.namespace('m0');
m0.app = function() {
return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
		var centerForm = new Ext.form.FormPanel({
					labelWidth: 100,
				    autoScroll : true,  
				    labelAlign : "left",	  
				    frame:true,
				    items: [
				    		 {xtype:'textfield',id:'eff',value:sEff,fieldLabel:commonality_eff,width:660}			
					]
			});
            //定义数据
			var store = new Ext.data.Store({
				url : m0.app.loadM0,
				reader : new Ext.data.JsonReader({
					 root : "m0",
					 fields : [
                        {name : 'm0Id'},      
					 	{name : 'msiId'},
					 	{name : 'proCode'},
					 	{name : 'proName'},
					 	{name : 'safety'},
					 	{name : 'safetyAnswer'},
					 	{name : 'detectable'},
					 	{name : 'detectableAnswer'},
					 	{name : 'task'},
					 	{name : 'taskAnswer'},
					 	{name : 'economic'},
					 	{name : 'economicAnswer'},
					 	{name : 'isMsi'},
					 	{name : 'highestLevel'},
					 	{name : 'remark'},
					 	{name : 'isAddAta'}
				 	]
				})
			});
			
			//加载store
			store.on("beforeload", function() {
				store.baseParams = {
					msiId : msiId
				};
			});
			store.load();

			//是否下拉框
			chooseCombo1=function (id){
			var chooseCombo = new Ext.form.ComboBox({   
				xtype : 'combo', name : 'choose', id : id,
				store : new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
						data : [['1',commonality_shi],['0',commonality_fou]]
				}),  
				valueField : "retrunValue", displayField : "displayText",
				mode : 'local', editable : false,
				triggerAction : 'all', width : 50				
			});
			return chooseCombo;
			}
		
			var chooseCombo = chooseCombo1('choose');
			var colM = new Ext.grid.ColumnModel([
			           {
	         					header : "<div align='center'>项目编号" + commonality_mustInput +"</div>", 
	         					dataIndex : "proCode",
	         					width : 80,
	         					editor : new Ext.form.TextField({
	         						     regex :/^\d{2}-\d{2}-\d{2}-\d{2}$/,
	        						     maxLength : 20,
	        					         maxLengthText :commonality_MaxLengthText
	        					})
	         		    	},{
	         					header : "<div align='center'>项目名称</div>",
	         					dataIndex : "proName",
	         					width : 100,
	         					editor : new Ext.form.TextArea({grow : true}),
	         					renderer : changeBR
	         		    	},{
	         		    		header :  "<div align='center'>故障影响安全<br>性吗？（包括<br>地面或空中）</div>",
	         		    		dataIndex : "safety",
	         		    		width : 90,
	         					editor : chooseCombo1('choose1'),
	         					renderer: doChoose
	         		    	},{
	         					header : "<div align='center'>原因</div>",
	         					dataIndex : "safetyAnswer",
	         					width : 100,
	         					editor : new Ext.form.TextArea({grow : true}),
	         					renderer : changeBR
	         		    	},{
	         		    		header :  "<div align='center'>在正常职责范<br>围，故障对使用<br>飞机人员来说<br>" +
										  "是无法发现或不<br>易察觉吗？</div>",
	         		    		dataIndex : "detectable",
	         		    		width : 90,
	         					editor : chooseCombo1('choose2'),
	         					renderer: doChoose
	         		    	},{
	         					header : "<div align='center'>原因</div>",
	         					dataIndex : "detectableAnswer",
	         					width : 100,
	         					editor : new Ext.form.TextArea({grow : true}),
	         					renderer : changeBR
	         					
	         		    	},{
	         		    		header : "<div align='center'>故障影响任务<br>完成吗？</div>",
	         		    		dataIndex : "task",
	         		    		width : 90,
	         					editor : chooseCombo1('choose3'),
	         					renderer: doChoose
	         		    	},{
	         					header : "<div align='center'>原因</div>",
	         					dataIndex : "taskAnswer",
	         					width : 100,
	         					editor : new Ext.form.TextArea({grow : true}),
	         					renderer : changeBR
	         		    	},{
	         		    		header : "<div align='center'>故障可能导致<br>重大的经济损<br>失吗？</div>",
	         		    		dataIndex : "economic",
	         		    		width : 90,
	         					editor : chooseCombo1('choose4'),
	         					renderer: doChoose
	         		    	},{
	         					header : "<div align='center'>原因</div>",
	         					dataIndex : "economicAnswer",
	         					width : 100,
	         					editor : new Ext.form.TextArea({grow : true}),
	         					renderer : changeBR
	         		    	},{
	         		    		header : "<div align='center'>是重要维修项<br>目吗？</div>",
	         		    		dataIndex : "isMsi",
	         		    		width : 90,
	         					editor : chooseCombo1('choose5'),
	         					renderer: doChoose
	         		    	},{
	         		    		header : "<div align='center'>最高可管理层</div>",
	         		    		dataIndex : "highestLevel",
	         		    		width : 90,
	         		    		editor :  new Ext.form.TextField({
	        						maxLength : 20,
	        						maxLengthText : commonality_MaxLengthText
	        					})
	         		    	},{
	         					header : "<div align='center'>备注</div>",
	         		    		dataIndex : "remark",
	         		    		width : 100,
	         					editor : new Ext.form.TextArea({grow : true}),
	         					renderer : changeBR
	         				},{
	         					header : "<div align='center'>是否是手动添加的msi</div>",
	         		    		dataIndex : "isAddAta",
	         		    		hidden : true,
	         		    		width : 100
	         				}
	         			]);
		

			var grid = new Ext.grid.EditorGridPanel({
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				//region : 'center',
				store : store,
				clicksToEdit : 2,   
				stripeRows : true,
				tbar :  [
							new Ext.Button({
								text : commonality_add,
								iconCls : "icon_gif_add",
								disabled : !flag,
								handler : function() {							
									add();							
								}
					        }),
					        new Ext.Button({
								text : commonality_del,
								disabled : !flag,
								iconCls : "icon_gif_delete",
								handler : function() {							
									del();
								}
					        })
						]
			});
			grid.addListener("afteredit", afteredit);
		
			function afteredit(val){
				//验证编号
				var record1=val.record;
				if (val.field == 'safety' || val.field == 'detectable'||
					val.field == 'task' || val.field == 'economic') {
					var record1 = val.record;
					if (record1.get('safety') == '1'||record1.get('detectable') == '1'||
						record1.get('task') == '1'||record1.get('economic') == '1') {
						record1.set("isMsi","1");
					}else{
						record1.set("isMsi","0");
					}
				}
				if (val.field == 'proCode'){
					var cou = 0;
					for (var i = 0; i < store.getCount(); i++){
						if (store.getAt(i).get('proCode') == val.value){
									cou = cou + 1;
								}
					}
					if(cou>1){
							alert("编号不能重复！");
							record1.set('proCode', val.originalValue);
							return ;
							}
					Ext.Ajax.request({
					
						url : m0.app.checkProCodeExist,
						params : {
							msiCode : val.value
						},
						method : "POST",
						success : function(form,action) {
							var msg = Ext.util.JSON
									.decode(form.responseText);
							if (msg.exist) {
								alert("编号不能重复！");
								record1.set('proCode', val.originalValue);
								return ;
							}
						}
					})
				}
			}
			var headerStepForm = new Ext.form.Label({
				applyTo : 'headerStepDiv'
			});
			var viewport = new Ext.Viewport( {
				id : 'viewportId',
				layout : 'border',
				items : [
					{
						region : 'north',
						height : 60,
						frame : true,
						items : [headerStepForm]
					},{ 
						layout: 'border',
					    region : 'center',
			    	    items : [
							{
								region : 'center',
								layout : 'fit',
								title : returnPageTitle("重要维修项目（MSI）确定",'m_0'),
								items : [grid]
							},{
								layout: 'fit',
								height :35,
								region : 'north',
								items: [centerForm]
							}
			    	    ]
					}
					
				]
			});
			
			function doChoose(value, cellmeta, record){
			    var index = chooseCombo.store.find(Ext.getCmp('choose').valueField,value);				    
			    var record = chooseCombo.store.getAt(index);
			    var returnvalue = "";
			    if (record){
			        returnvalue = record.data.displayText;
			    }
			    return returnvalue;
			}	
			function add(){
				var rec = grid.getStore().recordType;
				var p = new rec({
					m0Id : '',
					msiId : msiId,
					proCode : '',
					proName : '',
					safety : '',
					safetyAnswer : '',
					detectable : '',
					detectableAnswer: '',
					task : '',
					taskAnswer : '',
					economic : '',	
					economicAnswer : '',
					isMsi : '',
					highestLevel : '',
					remark : '',
					isAddAta : '1'
				});
				store.add(p);				
			}
			
			function del(){
				var record  = grid.getSelectionModel().getSelected();
				if (record == null){
					alert(commonality_alertDel);
					return;
				}
				if (record.get('m0Id') != ''){
					Ext.MessageBox.show({
					    title : commonality_affirm,
					    msg : commonality_affirmDelMsg,
					    buttons : Ext.Msg.YESNOCANCEL,
					    fn : function(id){
					    	if (id == 'cancel'){
								return;
							} else if (id == 'yes'){
								doDelete(record);
							
							} else if (id == 'no'){
								return;
							}
					    }
					});
				} else {
					store.remove(record);
					store.modified.remove(record);
				}
			}
			
			function doDelete(record){
				if(record.get('isAddAta')!=1){
					alert("非手动添加，不能删除！");
					return;
				}
				waitMsg.show();
				Ext.Ajax.request( {
					url : m0.app.delM0,
					params : {
						delId : record.get('m0Id')
					},
					method : "POST",
					success : function(form,action) {
						store.load();
						waitMsg.hide();
						store.modified = [];
						alert( commonality_messageDelMsg);
					},
					failure : function(form,action) {
						alert( commonality_cautionMsg);
					}
				});
			}
			
			function saveDate(json,value) {
				Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(yesNo){
				    	if (yesNo == 'cancel'){
							return;
						} else if (yesNo == 'yes'){		
								save(json,value);
						} else if (yesNo == 'no'){
						      goNext(value);
						}
				    }
				});
			}		
			
			function save(json,value){
					var result=true;
					Ext.each(store.modified, function(item) {
			       if(item.data.proCode == ''){
			       	alert("编号不能为空！");
			       	result = false;
			       	return false;
			       }
			       //去除空格
				});
				if(!result ){
					return;
				}
				waitMsg.show();
				Ext.Ajax.request( {
					url : m0.app.saveM0,
					params : {
						jsonData : Ext.util.JSON.encode(json),
						msiId : msiId,
						defaultEff:Ext.getCmp('eff').getValue()
					},
					method : "POST",
					success : function(form,action) {
						waitMsg.hide();
						parent.refreshTreeNode();
						alert(commonality_messageSaveMsg);
						step[1] = 2;
						goNext(value);
					},
					failure : function(form,action) {
						waitMsg.hide();
					alert( commonality_cautionMsg);
					}
				});
			}
			
			//点击导航栏执行的方法
			nextOrSave = function (value){
			
				if(value == null){
					value = 1;
				}
				if (store.modified.length == 0&&sEff==Ext.getCmp('eff').getValue()){
					goNext(value);
					return false;
				}
				if(isMaintain!='1'){
				//	alert("您没有修改权限，无权修改！");
					goNext(value);
					return false;
				}

				var json = [];
				Ext.each(store.data.items, function(item) {
			   
			       //去除空格
			       if(item.data.proName!= null){
			          item.data.proName = item.data.proName.trim();
			        }
				  json.push(item.data);
				});
				if (json.length > 0) {
					saveDate(json,value);
				
				} 				
			}
					
		}
	};
}();