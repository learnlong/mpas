var record1 = null;
// 默认加载树上第一个已完成分析的故障影响的第一个原因的分析
treePanelTwo.on("load", treeLoad);
function treeLoad() {
	treePanelTwo.expandAll();
	// var treePanel = Ext.getCmp('treePanelTwo');
	var nodes = treePanelTwo.getRootNode().childNodes;
	for (var i = 0; i < nodes.length; i++) {
		var node = nodes[i].firstChild;
		if (node != null && node.attributes.result != "") {
			loadData(node);
			treePanelTwo.getSelectionModel().select(node);
			treePanelTwo.un("load", treeLoad);
			return;
		} else {
			return;
		}
	}
}
Ext.QuickTips.init();
Ext.form.Field.prototype.msgTarget = 'qtip';
var taskCodeCombo = new Ext.form.ComboBox({
			xtype : 'combo',
			name : 'taskCodeCom',
			id : 'taskCodeCom',
			store : new Ext.data.Store({
						url : "${contextPath}/sys/m3/searchTaskCode.do",
						reader : new Ext.data.JsonReader({
									root : 'root',
									fields : [{
												name : 'id'
											}, // #& + id 表示数据库已保存的任务ID。
											{
												name : 'code'
											}, {
												name : 'taskType'
											}]
								})
					}),
			valueField : "id",
			displayField : "code",
			typeAhead : true,
			mode : 'local',
			triggerAction : 'all',
			width : 50,
			editable : true,
			selectOnFocus : true,
			regex : /[\A-Za-z0-9\-]+$/,
			regexText : "编号格式不正确，只能输入字母，数字，横杠",
			maxLength : 50,
			maxLengthText : commonality_MaxLengthText,
			listeners : {
				"focus" : function(obj) {
					tempStore.filterBy(comboDelTempData);
					comboAddTempData();
				}
			}
		});

var tempStore = new Ext.data.Store();// 用来存放原始的store
taskCodeCombo.store.on('load', function(e) {
			tempStore.removeAll();
			Ext.each(taskCodeCombo.store.data.items, function(item) {
						tempStore.add(item);// 原始的值放在tempstore
					});
		});
var typeCombo = new Ext.form.ComboBox({
			xtype : 'combo',
			name : 'chooseType',
			id : 'chooseType',
			minListWidth : 50,
			store : new Ext.data.SimpleStore({
						fields : ["retrunValue", "displayText"],
						data : [[baoyang, baoyang],[jiancha,jiancha], [jiankong, jiankong], [jiance,jiance],
								[chaixiu, chaixiu], [baofei, baofei], [zonghe, zonghe]]
					}),
			valueField : "retrunValue",
			displayField : "displayText",
			mode : 'local',
			editable : false,
			triggerAction : 'all',
			width : 50,
			listeners : {
				"select" : function(combo, record, index) {
					var rec = grid.getSelectionModel().getSelected();
					var index = store.indexOf(rec);
					var tempCode = rec.get('taskCode');
					var cou=0;
					if(rec.get('taskType')!=combo.getRawValue()){
						for(var i=0;i<store.getCount();i++){
							if(i==index){
								continue;
							}
							var tempRec = store.getAt(i);
							if(tempRec.get('taskCode')==tempCode){
								rec.set("taskCode","");
								rec.set("taskDesc","");
								rec.set("reachWay","");
								rec.set("effectType",effectResult);
								rec.set("effectiveness",msiEff);
								rec.set("zoneTransfer","");
								rec.set('taskId', record.get('id'));
								cou++;
							}
						}
						if(cou==0){//判断它是否参考了其它的
							Ext.Ajax.request({
								url : "${contextPath}/sys/m3/searchChooseTask.do",
								params : {
									jsonTask : tempCode,
									effectResult : effectResult
								},
								method : "POST",
								callback : function(options, success, response) {
									var all = Ext.util.JSON.decode(response.responseText);
									if(all.chooseTask[0].notExist){
										
									}else{
										rec.set("taskCode","");
										rec.set("taskDesc","");
										rec.set("reachWay","");
										rec.set("effectType",effectResult);
										rec.set("effectiveness",msiEff);
										rec.set("zoneTransfer","");
										rec.set('taskId', record.get('id'));
									}
								}
							});
						}
					}
				}
			}
		});

store = new Ext.data.Store({
			url : "${contextPath}/sys/m3/searchTask.do",
			reader : new Ext.data.JsonReader({
						root : "task",
						fields : [{
									name : 'taskId'
								}, {
									name : 'hasSave'
								}, {
									name : 'whichPro'
								}, {
									name : 'taskCode'
								}, {
									name : 'taskType'
								}, {
									name : 'taskDesc'
								}, {
									name : 'reachWay'
								}, {
									name : 'effectType'
								}, {
									name : 'effectiveness'
								}, {
									name : 'zoneTransfer'
								}, {
									name : 'needTransfer'
								}]
					})
		});
var nowRecord = null
var colM = new Ext.grid.ColumnModel([{
			header : "是否已保存",
			dataIndex : "hasSave",
			hidden : true
		}, {
			header : "问题",
			dataIndex : "whichPro",
			width : 40
		}, {
			header : "任务编号" + commonality_mustInput,
			dataIndex : "taskCode",
			width : 80,
			editor : taskCodeCombo,
			renderer : function(value, cellmeta, record) {
				var index = tempStore.findBy(function(record, id) {
							if (record.get('id') == value) {
								return true;
							}
						});

				var record = tempStore.getAt(index);
				var returnvalue = "";
				if (record) {
					returnvalue = record.data.code;
				} else {
					returnvalue = value;
				}
				return returnvalue;
			}
		}, {
			header : "任务类型" + commonality_mustInput,
			dataIndex : "taskType",
			width : 100,
			editor : typeCombo,
			renderer : function(value, cellmeta, record) {

				var index = typeCombo.store.find(
						Ext.getCmp('chooseType').valueField, value);
				var record = typeCombo.store.getAt(index);
				var returnvalue = "";
				if (record) {
					returnvalue = record.data.displayText;
				}
				return returnvalue;
			}
		}, {
			header : "任务描述" + commonality_mustInput,
			dataIndex : "taskDesc",
			width : 200,
			editor : new Ext.form.TextArea({grow : true}),
			renderer : changeBR
		}, {
			header : "接近方式",
			dataIndex : "reachWay",
			width : 200,
			editor : new Ext.form.TextArea({grow : true}),
			renderer : changeBR
		}, {
			header : "故障影响类别",
			dataIndex : "effectType",
			width : 90
		}, {
			header : "适用性",
			dataIndex : "effectiveness",
			width : 75,
			editor : new Ext.form.TextField({
						maxLength : 200,
						maxLengthText : commonality_MaxLengthText
					})
		}, {
			header : "区域",
			dataIndex : "zoneTransfer",
			width : 120,
			editor : new Ext.form.TextField({
						regex : /^(\d+,)*\d+$/,
						regexText : "请输入正确的区域号并用逗号隔开",
						maxLength : 500,
						maxLengthText : commonality_MaxLengthText
					})
		}, {
			header : "是否已经转移到区域",
			dataIndex : "needTransfer",
			hidden : true,
			width : 120
		}]);
var grid = new Ext.grid.EditorGridPanel({
			cm : colM,
			sm : new Ext.grid.RowSelectionModel({
						singleSelect : true
					}),
			region : 'center',
			store : store,
			clicksToEdit : 2,
			stripeRows : true,
			tbar : [new Ext.Button({
								text : "添加附加任务",
								iconCls : "icon_gif_add",
								disabled : !flag,
								handler : addAffixation
							}), "-", new Ext.Button({
								text : "删除附加任务",
								disabled : !flag,
								iconCls : 'icon_gif_delete',
								handler : delAffixation
							})

			]
		});

store.on('load', function() {
			record1 = grid;
		});

var headerStepForm = new Ext.form.Label({
			applyTo : 'headerStepDiv'
		});

var myDivForm = new Ext.form.Label({
			applyTo : 'myDiv'
		});

var view = new Ext.Viewport({
			layout : 'border',
			items : [{
						region : 'north',
						layout : 'fit',
						height : 60,
						frame : true,
						items : [headerStepForm]
					}, {
						title : returnPageTitle("工作选择", 'm_3'),
						region : 'center',
						layout : 'border',
						items : [{
									region : 'west',
									layout : 'fit',
									width : 130,
									items : [treePanelTwo]
								}, {
									region : 'center',
									layout : 'border',
									items : [{
												region : 'center',
												layout : 'fit',
												items : [myDivForm]
											}, {
												region : 'south',
												layout : 'fit',
												height : 120,
												items : [grid]
											}]
								}]
					}]
		});
grid.addListener("beforeedit",beforeedit)
grid.addListener("afteredit", afteredit);
grid.addListener("cellclick", cellclick);
function beforeedit(val){
	var rec = val.record;
	if(rec.get('whichPro')<=7&&val.field == 'taskType'){
			val.cancel =true;
		}
}
function cellclick(grid, rowIndex, columnIndex, e) {
	var nowRecord = grid.getStore().getAt(rowIndex);
	taskCodeCombo.getStore().removeAll();
	Ext.each(tempStore.data.items, function(item) {
				if (item.get("taskType") == nowRecord.get('taskType')) {
					taskCodeCombo.getStore().addSorted(item);
				}
			});
}
function afteredit(val) {
		var nowRecord = val.record;
		if (val.field == 'zoneTransfer') {
			if(nowRecord.get('needTransfer')==1&&(val.value==''||val.value==null)){
				alert("该任务为转区域任务，区域不能为空！！！");
				nowRecord.set('zoneTransfer',val.originalValue);
				return;
			}
		//检查区域编号是否重复
		Ext.Ajax.request({
					url : contextPath + "/struct/verifyArea.do?",
					params : {
						verifyStr : val.value
					},
					success : function(response) {
						if(response.responseText){
							var msg= Ext.util.JSON.decode(response.responseText);
							if(msg.exists){
								alert("区域:"+ msg.exists +"不能重复,请重新修改")
								nowRecord.set('zoneTransfer',val.originalValue);
								return ;
							}
							if(msg.unExists){
								alert("区域:"+ msg.unExists+"不存在,请重新修改")
								nowRecord.set('zoneTransfer',val.originalValue);
								return ;
							}
							if (msg.success) {
								alert("不能转入区域:"+ msg.success+"，该区域已被冻结或者已经审批完成，请重新修改")
								nowRecord.set('zoneTransfer',val.originalValue);
								return ;
							}
						}
					}
				})

	}
	if (val.field == 'taskCode') {
		var count = 0;
		for (var i = 0; i < store.getCount(); i++) {
			record = store.getAt(i);
			if (record.get('taskCode') == nowRecord.get('taskCode')&&record.get('taskType')!=nowRecord.get('taskType')) {
				count++;
			}
		}
		if (count >= 1) {
			nowRecord.set('taskCode', val.originalValue);
			alert("该任务编号已被使用，请重新填写！");
			return;
		}
		// 检查任务编号是否重复
		Ext.Ajax.request({
					url : "${contextPath}/sys/m3/verifyTaskCode.do",
					params : {
						taskCode : val.value,
						msiId : msiId,
						whichPro : nowRecord.get('whichPro')
					},
					success : function(response) {
						if (response.responseText) {
							nowRecord.set('taskCode', val.originalValue);
							alert("该任务编号已被使用，请重新填写！");

						}
					}
				})
	}
	if(val.field == 'taskDesc'||val.field == 'reachWay'||val.field == 'effectiveness'||val.field == 'zoneTransfer'){
		var index = store.indexOf(nowRecord);
		var tempCode = nowRecord.get('taskCode');
		for(var i=0;i<store.getCount();i++){
			if(i==index){
				continue;
			}
			var tempRec = store.getAt(i);
			if(tempRec.get('taskCode')==tempCode){
				tempRec.set("taskDesc",nowRecord.get('taskDesc'));
				tempRec.set("reachWay",nowRecord.get('reachWay'));
				tempRec.set("effectiveness",nowRecord.get('effectiveness'));
				tempRec.set("zoneTransfer",nowRecord.get('zoneTransfer'));
			}
		}
	}
}

// 删除工作号下拉框中所有临时添加的还没有保存数据库的号码
function comboDelTempData(record) {
	if (record.get('id').substr(0, 2) == '#&') {
		return true;
	} else {
		return false;
	}
}

// 工作号下拉框中添加临时的编号

function comboAddTempData() {
	var tempData = new Array();
	var x = 0;
	var tempDataRec = grid.getSelectionModel().getSelected();
	var index = store.indexOf(tempDataRec);
	for (var i = 0; i < store.getCount(); i++) {
		if(i==index){
			continue;
		}
		var tempType = store.getAt(i).get('taskType');
		if(tempType==tempDataRec.get('taskType')){
			var taskRe = store.getAt(i).get('taskCode');
			if (taskRe != '' && taskRe.substr(0, 2) != '#&') {
				var temp = 0;
				for (var k = 0; k < tempData.length; k++) {
					if (tempData[k] == taskRe) {
						temp = 1;
						break;
					}
				}
				if (temp == 0) {
					tempData[x] = taskRe;
					x++;
				}
			}
		}
	}
	var taskCodeComboRec = taskCodeCombo.store.recordType;
	for (var k = 0; k < tempData.length; k++) {
		var p = new taskCodeComboRec({
					id : tempData[k],
					code : tempData[k]
				});
		taskCodeCombo.store.add(p);

	}
}

// 验证数字
function testNum(value) {
	var re = /^[\d]+$/;
	if (re.test(value)) {
		return true;
	} else {
		return false;
	}
	return true;
}

// 点击故障原因后，查询已有的任务，并加载下拉表单
function selectTask() {
	taskCodeCombo.store.on("beforeload", function() {
				taskCodeCombo.store.baseParams = {
					msiId : msiId
				};
			});

	store.on("beforeload", function() {
				store.baseParams = {
					msiId : msiId,
					m13cId : m13cId,
					effectResult : effectResult
				};
			});
	store.load();
	taskCodeCombo.store.load();
	taskCodeCombo.addListener("select", showTask);
}

// 如果选择了参考其他的任务，则该行显示参考任务的数据
function showTask(combo, record, index) {
	var nowRecord = grid.getSelectionModel().getSelected();
	var indexCode = store.find(record.get('code'));
//	if (record.get('id').length >= 2 && record.get('id').substr(0, 2) == '#&') {
	if(indexCode==-1){
		Ext.Ajax.request({
			url : "${contextPath}/sys/m3/searchChooseTask.do",
			params : {
				jsonTask : record.get('id'),
				effectResult : effectResult
			},
			method : "POST",
			callback : function(options, success, response) {
				var all = Ext.util.JSON.decode(response.responseText);
				nowRecord.set('taskId', all.chooseTask[0].taskId);
				nowRecord.set('taskType', all.chooseTask[0].taskType);
				nowRecord.set('taskCode', all.chooseTask[0].taskCode);
				nowRecord.set('taskDesc', all.chooseTask[0].taskDesc);
				nowRecord.set('reachWay', all.chooseTask[0].reachWay);
				nowRecord.set('effectType', all.chooseTask[0].effectType);
				nowRecord.set('effectiveness', all.chooseTask[0].effectiveness);
				nowRecord.set('zoneTransfer', all.chooseTask[0].zoneTransfer);
			}
		});
	} else {
		var index = store.indexOf(nowRecord);
		for (var i = 0; i < store.getCount(); i++) {
			if (i == index) {
				continue;
			}
			var re = store.getAt(i);
			if (re.get('taskCode') == record.get('code')) {
				nowRecord.set('taskId', re.get('taskId'));
				nowRecord.set('taskType', re.get('taskType'));
				nowRecord.set('taskCode', re.get('taskCode'));
				nowRecord.set('taskDesc', re.get('taskDesc'));
				nowRecord.set('reachWay', re.get('reachWay'));
				nowRecord.set('effectType', re.get('effectType'));
				nowRecord.set('effectiveness', re.get('effectiveness'));
				nowRecord.set('zoneTransfer', re.get('zoneTransfer'));

				break;
			}
		}
	}
}

// 处理点击按钮事件，该按钮与后面的按钮“无”关联关系
function checkTaskNo(num, value) {
	if (value == 1) {
		addTask(num);
		checkRadio(num, value,2);
	} else {
		checkRadio(num, value,2);
		deleteTask(num, value);
	}
}

// 处理点击按钮事件，该按钮与后面的按钮“有”关联关系
function checkTaskYes(num, value) {
	if (value == 1) {
		if (!testTask(num) && testTaskHasStore(num)) {
			for (var i = store.getCount() - 1; i >= 0; i--) {
				var re = store.getAt(i);
				if (re.get('whichPro') > num && re.get('whichPro') <= 7) {
					store.remove(re);
				}
			}
			addTask(num);
			checkRadio(num, value,2);
		} else {
			changeRadioCheck(num, 0);
		}
	} else {
		checkRadio(num, value,2);
		deleteTask(num, value);
	}
}

// 查看该问题对应的任务是否存在,true表示存在，false表示不存在
function testTask(num) {
	for (var i = 0; i < store.getCount(); i++) {
		var record = store.getAt(i);
		if (record.get('whichPro') == num) {
			return true;
		}
	}
	return false;
}

// 验证后续问题是否已存在保存过的任务
function testTaskHasStore(num) {
	for (var i = 0; i < store.getCount(); i++) {
		var record = store.getAt(i);
		if (record.get('whichPro') > num && record.get('hasSave') == '1'
				&& record.get('whichPro') <= 7) {
			alert("该问题后面的问题，已有任务存在，请先删除后面的任务");
			return false;
		}
	}
	return true;
}


// 添加任务
function addTask(num) {
	var index = 0;
	if(store.getCount()>0){
		if(num>7){
				var record = store.getAt(store.getCount()-1);
				if (record.get('whichPro') >= num) {
					alert("只能添加一条附加任务");
					return;
				}
			index = store.getCount();
		}else{
			for(var i =0;i<store.getCount();i++){
				var record = store.getAt(i);
				if(record.get('whichPro')>num){
					index = i;
					break;
				}else{
					index = i+1;
				}
			}
		}
	}
	var taskType="";
	if(num==1){
		taskType =baoyang;
	}else if(num==2){
		taskType=jiancha;
	}else if(num==3){
		taskType=jiankong;
	}else if(num==4){
		taskType=jiance;
	}else if(num==5){
		taskType=chaixiu;
	}else if(num==6){
		taskType=baofei;
	}else if(num==7){
		taskType=zonghe;
	}
	//自动生成任务编号
	var tmpTaskCode;
	if(store.getCount()>9||store.getCount()==9){
		tmpTaskCode=msiCodeShow+"-"+(store.getCount()+1);
	}else{
		tmpTaskCode=msiCodeShow+"-0"+(store.getCount()+1);
	}
	var rec = grid.getStore().recordType;
	var p = new rec({
				taskId : '',
				hasSave : '0',
				whichPro : num,
				taskCode : tmpTaskCode,
				taskType : taskType,
				taskDesc : '',
				reachWay : '',
				effectType : effectResult,
				effectiveness : msiEff,
				zoneTransfer : areaCodes,
				needTransfer : ''

			});
	store.insert(index, p);
}

// 删除记录
function deleteTask(num, value) {
	for (var i = 0; i < store.getCount(); i++) {
		var record = store.getAt(i);
		if (record.get('whichPro') == num) {
			if (record.get('hasSave') == '0') {
				store.remove(record);
				store.modified.remove(record);
				checkRadio(num, value);
				break;
			} else {
				Ext.MessageBox.show({
							title : commonality_affirm,
							msg : commonality_affirmDelMsg,
							buttons : Ext.Msg.YESNOCANCEL,
							fn : function(id) {
								if (id == 'cancel') {
									changeRadioCheck(num, 1);
								} else if (id == 'yes') {
									doDelete(record, value);
									store.modified = [];
									checkRadio(num, value);
								} else if (id == 'no') {
									changeRadioCheck(num, 1);
								}
							}
						});
				break;
			}
		}
	}
}

// 执行删除
function doDelete(record, value) {
	Ext.Ajax.request({
				url : "${contextPath}/sys/m3/deleteTask.do",
				params : {
					msiId : msiId,
					m13cId : m13cId,
					taskId : record.get('taskCode').substring(2),
					effectResult : record.get('whichPro'),
					select : value
				},
				method : "POST",
				success : function(form, action) {
					// 删除成功
					 loadStep();
					taskCodeCombo.store.load();
					store.remove(record);
					store.load();
					parent.refreshTreeNode();
					alert(commonality_messageDelMsg);
				},
				failure : function(form, action) {
					alert(commonality_cautionMsg);
				}
			});
}

function addAffixation() {
	addTask(8);
}
function delAffixation() {
	var record = grid.getSelectionModel().getSelected();
	if (record == null) {
		alert(commonality_alertDel);
		return;
	}
	if (record.get('whichPro') > 7) {
		if (record.get('hasSave') == 1) {
			Ext.MessageBox.show({
						title : commonality_affirm,
						msg : commonality_affirmDelMsg,
						buttons : Ext.Msg.YESNOCANCEL,
						fn : function(id) {
							if (id == 'cancel') {
								return;
							} else if (id == 'yes') {
								doDelete(record, null);
								return;
							} else if (id == 'no') {
								return;
							}
						}
					});
		} else {
			store.remove(record);
		}
	} else {
		alert("非附加任务不能手动删除")
	}
}