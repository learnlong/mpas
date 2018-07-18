Ext.namespace('ext_sys_index');

ext_sys_index.app = function() {
	return {
		init : function() {	
			Ext.QuickTips.init(); 
			Ext.form.Field.prototype.msgTarget = 'qtip';
			
			var store = new Ext.data.Store({
				url : ext_sys_index.app.g_loadReferMsiUrl,
				reader : new Ext.data.JsonReader({
					root : "refMsi",
					fields : [
						{name : 'id'},
						{name : 'yuanMsiCode'},
						{name : 'yuanFunctionDesc'},
						{name : 'yuanFailureDesc'},
						{name : 'yuanEffectDesc'},
						{name : 'yuanCauseDesc'},
						{name : 'yuanResult'},
						{name : 'msiCode'},
						{name : 'functionalDesc'},
						{name : 'failureDesc'},
						{name : 'effectDesc'},
						{name : 'causeDesc'},
						{name : 'isAna'},
						{name : 'remark'},
						{name : 'functionalFailureCode'},
						{name : 'failureEffectCode'},
						{name : 'failureCauseCode'}
				 	]
				}),
				sortInfo : {
					field : 'yuanMsiCode',
					direction : 'ASC'
			}
			});

			var colM = new Ext.grid.ColumnModel([
			    {
					header : "原MSI号",//"原MSI号.", 
					dataIndex : "yuanMsiCode",
					defaultSortable:true,
					width : 80
		    	},{
					header : "原功能",//"原功能",
					dataIndex : "yuanFunctionDesc",
					width : 100,
					renderer :changeBR
		    	},{
					header : "原功能故障",// "原功能故障",
					dataIndex : "yuanFailureDesc",
					width : 100,
					renderer :changeBR
		    	},{
					header : "原功能影响",// "原功能影响",
					dataIndex : "yuanEffectDesc",
					width : 100,
					renderer :changeBR
		    	},{
					header : "原功能原因",//"原功能原因",
					dataIndex : "yuanCauseDesc",
					width : 100,
					renderer :changeBR
		    	},{
					header : "故障影响类别 ",//"故障影响类别 ",
					dataIndex : "yuanResult",
					width : 100
		    	},{
					header : "参考MSI号 ",// "参考MSI号 ", 
					dataIndex : "msiCode",
					width : 80
		    	},{
					header : '参考功能',//"参考功能 Function(s)",
					dataIndex : "functionalDesc",
					width : 100,
					renderer :changeBR
		    	},{
					header : "参考功能故障",//"参考功能故障 Function Failure(s)",
					dataIndex : "failureDesc",
					width : 100,
					renderer :changeBR
		    	},{
					header : "参考功能影响",//"参考功能影响 Failure Effect(s)",
					dataIndex : "effectDesc",
					width : 100,
					renderer :changeBR
		    	},{
					header : "参考功能原因 ",//"参考功能原因 Failure Cause(s)",
					dataIndex : "causeDesc",
					width : 100,
					renderer :changeBR
		    	},{
					header : '被参考的MSI是否已进行分析',//"被参考的MSI是否已进行分析 Whether the reference MSI is already analyzed(If not analyze, explain how to deal with it)?",
					dataIndex : "isAna",
					width : 180,
					renderer :changeBR,
					editor : new Ext.form.TextArea({grow : true})
		    	},{
					header : "备注",//"备注 Remarks",
					dataIndex : "remark",
					width : 100,
					renderer :changeBR,
					editor : new Ext.form.TextArea({grow : true})
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
				clicksToEdit : 2,   
				stripeRows : true,
				tbar : [
					new Ext.Button({
						text : commonality_save,//'保存',
						iconCls : "icon_gif_save",
						disabled : !flag,
						handler : save
			        })
			    ]
			});
			
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
						url : ext_sys_index.app.g_saveReferMsiUrl,
						params : {
							jsonData : Ext.util.JSON.encode(json)
						},
						method : "POST",
						success : function(form,action) {
							store.load();
							alert(commonality_messageSaveMsg)//("保存成功！");
						},
						failure : function(form,action) {
							alert(commonality_cautionMsg);// "数据更新失败，请稍后再试！");
						}
					});
				} else {
					alert(commonality_alertSave);//('没有需要保存的数据');
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
				title : returnPageTitle("复查故障参考原因",'recheckmsi'),//msi_title,//'复查MSI',
				items : [grid]
			});
				
			win.show();		
			store.load();
		}
	};
}();