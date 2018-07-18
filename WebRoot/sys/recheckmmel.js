Ext.namespace('ext_sys_index');

ext_sys_index.app = function() {
	return {
		init : function() {	
			Ext.QuickTips.init(); 
			Ext.form.Field.prototype.msgTarget = 'qtip';
			
			var store = new Ext.data.Store({
				url : ext_sys_index.app.g_loadReferMmelUrl,
				reader : new Ext.data.JsonReader({
					totalProperty : "totleCount",
					root : "refMmel",
					fields : [
						{name : 'id'},
						{name : 'msiCode'},
						{name : 'msiName'},
						{name : 'effectCode'},
						{name : 'failureCauseType'},
						{name : 'q4Desc'},
						{name : 'isRefPmmel'},
						{name : 'reviewResult'},
						{name : 'remark'},
						{name : 'pmmelId'},
						{name : 'reviewDate'}
				 	]
				})
			});
				//是否下拉框
			var chooseCombo = new Ext.form.ComboBox({   
				xtype : 'combo', name : 'choose', id : 'choose',
				store : new Ext.data.SimpleStore({
					fields : ["retrunValue", "displayText"],
						data : [['1',commonality_shi],['0',commonality_fou]]
				}),  
				valueField : "retrunValue", displayField : "displayText",
				mode : 'local', editable : false,
				triggerAction : 'all', width : 50				
			});
	               //MMEL下拉框
			var mmelCombo = new Ext.form.ComboBox({
			name : 'mmelCom', id : 'mmelCom',
			store : new Ext.data.Store({
				url :  ext_sys_index.app.g_mmelComboUrl,
				reader : new Ext.data.JsonReader({
					root : 'mmelCombo',
					fields : [
					 	{name : 'id'},
					 	{name : 'name'}
				 	]
				})
			}),
			valueField : "id", displayField : "name",
			mode : 'local', triggerAction : 'all', width : 300, editable : false
		});
		 mmelCombo.store.load();
			var colM = new Ext.grid.ColumnModel([
			    {
					header : 'MSI号',//"MSI号 MSI No.", 
					dataIndex : "msiCode",
					defaultSortable:true,
					width : 80
		    	},{
					header :'MSI名称',// "MSI名称 MSI Title", 
					dataIndex : "msiName",
					width : 120,
					renderer: changeBR
		    	},{
					header : "F_FF_FE",
					dataIndex : "effectCode",
					width : 80
		    	},{
					header : '故障影响类别',//"故障影响类别 Failure Effect Category",
					dataIndex : "failureCauseType",
					width : 120
		    	},{
					header : '上层分析问题4的回答描述',// "上层分析问题4的回答描述  Answer and Description of Lever 1 Question 4",
					dataIndex : "q4Desc",
					width : 150,
					renderer: changeBR
		    	},{
					header : '是否参考MMEL',//"是否参考MMEL MMEL Assumption Results(Yes/No)",
					dataIndex : "isRefPmmel",
					width : 100,
				    //editor : chooseCombo,
				    renderer: doChoose
		    	},{
					header : '参照的PMMEL号',//"参照的PMMEL号 MMEL Reference No.",
					dataIndex : "pmmelId",
					width : 110,
					//editor : mmelCombo,
					renderer : function(value, cellmeta, record){
					var index = mmelCombo.store.findBy(function (record, id){
						if (record.get('id') == value){
							return true;
						}
					});	
				    var record = mmelCombo.store.getAt(index);					    
				    var returnvalue = "";
				    if (record){
				        returnvalue = record.data.name;
				    }
				    return returnvalue;
				}
		    	},{
					header : '复查结果',//"复查结果 Review Result",
					dataIndex : "reviewResult",
					width : 160,
					renderer: changeBR,
					editor : new Ext.form.TextArea({grow:true})
		    	},{
					header : '复查时间',//"复查时间 Review Date",
					dataIndex : "reviewDate",
					width : 100,
				    editor :new Ext.form.DateField({      
				           // emptyText:'请选择',   
				            format:'Y-m-d',   
				            disabledDays:[0,6]  
                    })
		    	},{
					header : '备注',//"备注 Remarks",
					dataIndex : "remark",
					width : 200,
					renderer : changeBR,
					editor : new Ext.form.TextArea({grow:true})
		    	}
			]);			
			
			var grid = new Ext.grid.EditorGridPanel({
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				region : 'center',
				loadMask : {
							msg : commonality_waitMsg
						},
				store : store,
				clicksToEdit :2,   
				stripeRows : true,
				tbar : [
					new Ext.Button({
						text :commonality_save,// '保存',
						iconCls : "icon_gif_save",
						disabled : !flag,
						handler : save
			        })
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
			//设置DateField 返回值的格式
			Date.prototype.toString=function(){ 
					  return this.format("Y-m-d"); 
					};
			
			function save(){
				Ext.MessageBox.show({
				    title : commonality_affirm,//"确认",
				    msg : commonality_affirmSaveMsg,//"确认要保存吗？",
				    buttons : Ext.Msg.YESNO,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){										
							saveData();
							store.modified = [];
						}
				    }
				});		
			}
						
			function saveData(){
				var json = [];
				Ext.each(store.modified, function(item) {
					json.push(item.data);
				});
				if (json.length > 0){
					Ext.Ajax.request( {
						url : ext_sys_index.app.g_saveReferMmelUrl,
						params : {
							jsonData : Ext.util.JSON.encode(json)
						},
						method : "POST",
						success : function(form,action) {
							store.load();
							alert(commonality_messageSaveMsg);//("保存成功！");
						},
						failure : function(form,action) {
							alert(commonality_cautionMsg);//("数据更新失败，请稍后再试！");
						}
					});
				} else {
					alert(commonality_alertUpdate);//('没有需要保存的数据');
					return;
				}
			}			
			
		
			
			var win = new Ext.Window({
				layout : 'border',
				border : false, 
				resizable : true,
				closable : false,
				maximized : true,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				title : returnPageTitle('复查MMEL','recheckmmel'),//mmel_title,//'复查MMEL',
				items : [grid]
			});
				
			win.show();		
			store.load();
		}
	};
}();