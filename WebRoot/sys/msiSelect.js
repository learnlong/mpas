Ext.namespace('msiSelect');
msiSelect.app = function() {
	return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';

			// 定义数据
			var store = new Ext.data.Store({
						url : msiSelect.app.loadMSelect,
						reader : new Ext.data.JsonReader({
									root : "mSelect",
									fields : [{
												name : 'ataId'
											}, {
												name : 'selectId'
											}, {
												name : 'proCode'
											}, {
												name : 'proName'
											}, {
												name : 'safety'
											}, {
												name : 'safetyAnswer'
											}, {
												name : 'detectable'
											}, {
												name : 'detectableAnswer'
											}, {
												name : 'task'
											}, {
												name : 'taskAnswer'
											}, {
												name : 'economic'
											}, {
												name : 'economicAnswer'
											}, {
												name : 'isMsi'
											}, {
												name : 'highestLevel'
											}, {
												name : 'remark'
											}, {
												name : 'ataLevel'
											}]
								})
					});

			var topForm = new Ext.form.Label({
				html : '<table width="100%" border="0" cellpadding="0" cellspacing="0">'
						+ '<tr>'
						+ '<td width="15%" align="center"><strong>APPLY</strong></td>'
						+ '<td width="60%" align="center"><strong>系统/动力装置MSG-3分析'
						+ '</strong></td>'
						+ '<td width="25%"><strong>ATA章节号：'
						+ ataCode
						+ '</strong></td>'
						+ '</tr>'
						+ '<tr>'
						+ '<td align="center"><strong>M-0</strong></td>'
						+ '<td align="center"><strong>'
						+ '重要维修项目（MSI）确定</strong></td>'
						+ '<td><strong>ATA名称：'
						+ "<font title='"+ataTitle+"'>"+ataName + '</font></strong></td>' + '</tr>' + '</table>'
			});

			// 是否下拉框
			chooseCombo1 = function() {
				var chooseCombo = new Ext.form.ComboBox({
							xtype : 'combo',
							name : 'choose',
							// id : 'choose',
							store : new Ext.data.SimpleStore({
										fields : ["retrunValue", "displayText"],
										data : [['1', commonality_shi],
												['0', commonality_fou]]
									}),
							valueField : "retrunValue",
							displayField : "displayText",
							mode : 'local',
							editable : false,
							triggerAction : 'all',
							width : 50
						});
				return chooseCombo;
			}

			var chooseCombo = chooseCombo1();
			// 加载store
			store.on("beforeload", function() {
						store.baseParams = {
							ataId : ataId
						};
					});
			store.load();
			var colM = new Ext.grid.ColumnModel([
					{
				header : "<div align='center'>项目编号</div>",
				dataIndex : "proCode",
				width : 80
			}, {
				header : "<div align='center'>项目名称</div>",
				dataIndex : "proName",
				width : 100,
				renderer : changeBR
			}, {
				header : "<div align='center'>故障影响安全<br>性吗？（包括<br>地面或空中）</div>",
				dataIndex : "safety",
				width : 90,
				editor : chooseCombo1(),
				renderer : doChoose
			}, {
				header : "<div align='center'>原因</div>",
				dataIndex : "safetyAnswer",
				width : 100,
				editor : new Ext.form.TextArea({
					grow : true
				}),
				renderer : changeBR
			}, {
				header : "<div align='center'>在正常职责范<br>围，故障对使用<br>飞机人员来说<br>" +
						"是无法发现或不<br>易察觉吗？</div>",
				dataIndex : "detectable",
				width : 90,
				editor : chooseCombo1(),
				renderer : doChoose
			}, {
				header : "<div align='center'>原因</div>",
				dataIndex : "detectableAnswer",
				width : 100,
				editor : new Ext.form.TextArea({grow : true}),
				renderer : changeBR
			}, {
				header : "<div align='center'>故障影响任务<br>完成吗？</div>",
				dataIndex : "task",
				width : 90,
				editor : chooseCombo1(),
				renderer : doChoose
			}, {
				header : "<div align='center'>原因</div>",
				dataIndex : "taskAnswer",
				width : 100,
				editor : new Ext.form.TextArea({grow : true}),
				renderer : changeBR
			}, {
				header : "<div align='center'>故障可能导致<br>重大的经济损<br>失吗？</div>",
				dataIndex : "economic",
				width : 90,
				editor : chooseCombo1(),
				renderer : doChoose
			}, {
				header : "<div align='center'>原因</div>",
				dataIndex : "economicAnswer",
				width : 100,
				editor : new Ext.form.TextArea({grow : true}),
				renderer : changeBR
			}, {
				header : "<div align='center'>是重要维修项<br>目吗？</div>",
				dataIndex : "isMsi",
				id : "isMsiId",
				width : 90,
				editor : chooseCombo1(),
				renderer : doChoose
			}, {
				header : "<div align='center'>最高可管理层</div>",
				dataIndex : "highestLevel",
				width : 90,
				editor : new Ext.form.TextField({
					maxLength:20,
					maxLengthText:commonality_MaxLengthText
				})
			}, {
				header : "<div align='center'>备注</div>",
				dataIndex : "remark",
				width : 100,
				editor : new Ext.form.TextArea({grow : true}),
				renderer : changeBR
			}]);
			

			var grid = new Ext.grid.EditorGridPanel({
						cm : colM,
						sm : new Ext.grid.RowSelectionModel({
									singleSelect : true
								}),
						// region : 'center',
						store : store,
						clicksToEdit : 2,
						stripeRows : true,
						tbar : [new Ext.Button({
									text : commonality_save,
									disabled : !isMaintain,
									iconCls : 'icon_gif_save',
									handler : save
								})]
					});
			grid.addListener("beforeedit",beforeedit);
			grid.addListener("afteredit", afteredit);
			function beforeedit(val){
				if(val.field =='isMsi'){
					val.cancel = true;
				}
				var isMsi = val.record.get('isMsi');
				if(isMsi==""||isMsi==0){
					if(val.field =='highestLevel'){
						val.cancel = true;
					}
					if(val.field =='remark'){
						val.cancel = true;
					}
					if(val.field =='safetyAnswer'){
						val.cancel = true;
					}
					if(val.field =='detectableAnswer'){
						val.cancel = true;
					}
					if(val.field =='taskAnswer'){
						val.cancel = true;
					}
					if(val.field =='economicAnswer'){
						val.cancel = true;
					}
				}
			}
			function afteredit(val) {
				if (val.field == 'safety' || val.field == 'detectable'||
					val.field == 'task' || val.field == 'economic') {
					var nowRecord = val.record;
					if (nowRecord.get('safety') == '1'||nowRecord.get('detectable') == '1'||
						nowRecord.get('task') == '1'||nowRecord.get('economic') == '1') {
						nowRecord.set("isMsi","1");
					}else{
						nowRecord.set("isMsi","0");
					}
				}
			}

			var viewport = new Ext.Viewport({
						id : 'viewportId',
						layout : 'border',
						items : [{
									region : 'north',
									layout : 'fit',
									height : 45,
									frame : true,
									items : [topForm]
								}, {
									region : 'center',
									layout : 'fit',
									title : returnPageTitle("重要维修项目（MSI）确定",
											'msiSelect'),
									items : [grid]
								}]
					});

			function doChoose(value, cellmeta, record) {
				var index = chooseCombo.store.find(chooseCombo.valueField,
						value);
				var record = chooseCombo.store.getAt(index);
				var returnvalue = "";
				if (record) {
					returnvalue = record.data.displayText;
				}
				return returnvalue;
			}

			function save() {
				Ext.MessageBox.show({
							title : commonality_affirm,
							msg : commonality_affirmSaveMsg,
							buttons : Ext.Msg.YESNO,
							fn : function(select) {
								if (select == 'cancel') {
									return;
								} else if (select == 'yes') {
										saveData();
								}
							}
						});
			}

			var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				msg : commonality_waitMsg,
				removeMask : true
					// 完成后移除
				});

			function saveData() {
				var flag = true;
				if (store.modified.length > 0){
					Ext.each(store.modified, function(item) {
						if(item.data.isMsi==""||item.data.highestLevel==""){//不是msi分析不验证最高可管理层格式
							return;
						}
						var highestLevel = item.data.highestLevel;
						//验证最高可管理层XX-XX-XX-XX格式
						var allre=/^\d{2}-\d{2}-\d{2}-\d{2}$/;
						if (allre.test(highestLevel) == false) {
							alert('最高可管理层的ATA编号必须是XX-XX-XX-XX格式，并且XX必须是数字!');
							flag = false;
						}
					});
				}
				if(flag==true){
					var json = [];
					Ext.each(store.modified, function(item) {
								json.push(item.data);
							});
					if (json.length > 0) {
						waitMsg.show();
						Ext.Ajax.request({
									url : msiSelect.app.saveMSelect,
									params : {
										jsonData : Ext.util.JSON.encode(json)
									},
									method : 'POST',
									success : function(response, action) {
										var text = Ext.decode(response.responseText);
										if(text.errProCodes!=""){
											alert("项目编号:["+text.errProCodes+"]最高管理层输入的ata编号不符合规则;最高管理层只能是该ata的上级或它自己，如果为上级，且该上级必须同时满足重要维修项目和最高管理层是它自己两个条件！");
											waitMsg.hide();
										}else{
											store.modified = [];
											store.reload();
											parent.refreshTreeNode();
											waitMsg.hide();
											alert(commonality_messageSaveMsg);
										}
									},
									failure : function(form, action) {
										waitMsg.hide();
										alert(commonality_cautionMsg);
									}
								});
					} else {
						alert(commonality_alertSave);
						return;
					}
				}
				
			}
		}
	};
}();