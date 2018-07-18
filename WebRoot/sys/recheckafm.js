Ext.namespace('ext_sys_index');

ext_sys_index.app = function() {
	return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';

			var store = new Ext.data.Store({
						url : ext_sys_index.app.g_loadReferAfmUrl,
						reader : new Ext.data.JsonReader({
									totalProperty : "totleCount",
									root : "refAfm",
									fields : [{
												name : 'id'
											}, {
												name : 'msiCode'
											}, {
												name : 'msiName'
											}, {
												name : 'effectCode'
											}, {
												name : 'failureCauseType'
											}, {
												name : 'q1Desc'
											}, {
												name : 'refAfm'
											}, {
												name : 'reviewResult'
											}, {
												name : 'reviewDate'
											}, {
												name : 'remark'
											}]
								})
					});

			var colM = new Ext.grid.ColumnModel([
			        {
						header : 'MSI名称',//"MSI名称 MSI Title",
						dataIndex : "msiName",
						width : 120,
						renderer : changeBR
					}, {
						header : "F_FF_FE",
						dataIndex : "effectCode",
						width : 80
					}, {
						header : '故障影响类别',//"故障影响类别 Failure Effect Category",
						dataIndex : "failureCauseType",
						width : 100
					}, {
						header : '上层分析问题1的回答描述',//"上层分析问题1的回答描述 Answer and Description of Lever 1 Question 1",
						dataIndex : "q1Desc",
						width : 150,
						renderer: changeBR 
					}, {
						header :'假设参考AFM中的相关内容',// "假设参考AFM中的相关内容 Reference Content of AFM",
						dataIndex : "refAfm",
						width : 160,
						renderer: changeBR ,
						editor : new Ext.form.TextArea({grow:true})
					}, {
						header : '复查结果',//"复查结果 Review Result",
						dataIndex : "reviewResult",
						width : 150,
						renderer: changeBR ,
						editor : new Ext.form.TextArea({grow:true})
					}, {
						header : '复查时间',//"复查时间 Review Date",
						dataIndex : "reviewDate",
						width : 100,
						editor :new Ext.form.DateField({      
				            format:'Y-m-d'
                    })
					}, {
						header : "备注",// "备注 Remarks",
						dataIndex : "remark",
						width : 200,
						renderer: changeBR ,
						editor : new Ext.form.TextArea({grow:true})
					}]);

			var grid = new Ext.grid.EditorGridPanel({
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				loadMask : {
							msg : commonality_waitMsg
						},
				region : 'center',
				store : store,
				clicksToEdit : 2,
				stripeRows : true,
				tbar : [new Ext.Button({
									text : commonality_save,//'保存',
									iconCls : "icon_gif_save",
									disabled : !flag,
									handler : save
								})
						]
				});
				//设置DateField 返回值的格式
				Date.prototype.toString=function(){ 
					  return this.format("Y-m-d"); 
					};

			function save() {
				Ext.MessageBox.show({
							title : commonality_affirm,//"确认",
							msg : commonality_affirmSaveMsg,//"确认要保存吗？",
							buttons : Ext.Msg.YESNO,
							fn : function(id) {
								if (id == 'cancel') {
									return;
								} else if (id == 'yes') {
									saveData();
									store.modified = [];
								}
							}
						});
			}

			function saveData() {
				var json = [];
				Ext.each(store.modified, function(item) {
							json.push(item.data);
						});
				if (json.length > 0) {
					Ext.Ajax.request({
								url : ext_sys_index.app.g_saveReferAfmUrl,
								params : {
									jsonData : Ext.util.JSON.encode(json)
								},
								method : "POST",
								success : function(form, action) {
									store.load();
									alert(commonality_messageSaveMsg);//( "保存成功！");
								},
								failure : function(form, action) {
									alert(commonality_cautionMsg);//( "数据更新失败，请稍后再试！");
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
						title :returnPageTitle('复查AFM','recheckafm'),// afm_title,
						items : [grid]
					});

			win.show();
			store.load();
		}
	};
}();