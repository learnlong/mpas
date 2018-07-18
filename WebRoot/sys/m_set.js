Ext.namespace('mset');
mset.app = function() {
	return {
		init : function() {

			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';

			// 定义数据
			var store = new Ext.data.Store({
						url : mset.app.loadMsetUrl,
						reader : new Ext.data.JsonReader({
									root : "mset",
									fields : [{
												name : 'msetId'
											}, {
												name : 'ataId'
											},{
												name : 'ataCode'
											},{
												name : 'ataName'
											},{
												name : 'functionCode'
											}, {
												name : 'functionDesc'
											}, {
												name : 'msetfId'
											}, {
												name : 'failureCode'
											}, {
												name : 'failureDesc'
											},  {
												name : 'level'
											}]
								})
					});

			
			var baColor1 = "#b2dfee";
			var baColor2 = "#ffffa0";
			var mset_color = baColor1;
			// 加载store之后，不同ATA显示不同颜色
			// load.defer(10000,store);
			setStoreClass = function() {
				for (var i = 0; i < store.getCount(); i++) {
					var record = store.getAt(i);
					if (record.get('ataCode') != null
							&& record.get('ataCode') != '') {
						if (mset_color == baColor1) {
							mset_color = baColor2;

						} else {
							mset_color = baColor1;
						}
					}
					grid.getView().getRow(i).style.backgroundColor = mset_color;
				}
			};
			store.on("load", function() {
				setTimeout("setStoreClass()", 1);
					// setStoreClass();
				});
			// 加载store之前
			store.on("beforeload", function() {
						store.baseParams = {
							msiId : msiId,
							ataId : ataId
						};
					});
			//复选框选择模式  
			var checkboxSM = new Ext.grid.CheckboxSelectionModel({  
			    checkOnly: false,  
			    singleSelect: false  
			}); 
			var colM = new Ext.grid.ColumnModel([checkboxSM,
			        {
						header : "功能ID",
						dataIndex : "msetId",
						hidden : true
					}, {
						header : "项目ID",
						dataIndex : "ataId",
						hidden : true
					}, {
						header : "项目编号",
						dataIndex : "ataCode"
					},{
						header : "项目名称",
						dataIndex : "ataName"
					},{
						header : "编号" + commonality_mustInput,
						dataIndex : "functionCode",
						width : 120
					}, {
						header : "功能" + commonality_mustInput,
						dataIndex : "functionDesc",
						width : 200,
						editor : new Ext.form.TextArea({grow : true}),
						renderer : changeBR
					}, {
						header : "功能故障ID",
						dataIndex : "msetfId",
						hidden : true
					}, {
						header : "编号" + commonality_mustInput,
						dataIndex : "failureCode",
						width : 120
					}, {
						header : "功能故障" + commonality_mustInput,
						dataIndex : "failureDesc",
						width : 200,
						editor : new Ext.form.TextArea({grow : true}),
						renderer : changeBR
					
					}]);
			var grid = new Ext.grid.EditorGridPanel({
						cm : colM, sm : checkboxSM,
						/*sm : new Ext.grid.RowSelectionModel({
									singleSelect : true
								}),*/
						// region : 'center',
						store : store,
						clicksToEdit : 2,
						stripeRows : true,
						tbar : [new Ext.Button({
											text : "添加功能",
											iconCls : "icon_gif_add",
											disabled : !flag,
											handler : function() {
												addFunction();
											}
										}), new Ext.Button({
											text : "添加故障",
											iconCls : "icon_gif_add",
											disabled : !flag,
											handler : function() {
												addFailure();
											}
										}), new Ext.Button({
											text : commonality_del,
											iconCls : "icon_gif_delete",
											disabled : !flag,
											handler : function() {
												del();
											}
										}), new Ext.Button({
											text : "暂存",
											iconCls : "icon_gif_save",
											disabled : !flag,
											handler : function() {
												zancun();
											}
										})]
					});



			var headerStepForm = new Ext.form.Label({
						applyTo : 'headerStepDiv'
					});
			var viewport = new Ext.Viewport({
						id : 'viewportId',
						layout : 'border',
						items : [{
									region : 'north',
									height : 60,
									frame : true,
									items : [headerStepForm]
								}, {
									region : 'center',
									layout : 'fit',
									title : returnPageTitle("设置ATA对应的的功能及故障", 'm_set'),
									items : [grid]
								}]
					});
			store.load();
			grid.addListener("beforeedit", beforeedit);
			function beforeedit(val) {
				var functionCode = val.record.get('functionCode');
				var field = val.field;
				if (functionCode == '' || functionCode == null) {
					if (field == 'functionDesc') {
						val.cancel = true;
					}
				}
				var failureCode = val.record.get('failureCode');
				if (failureCode == '' || failureCode == null) {
					if (field == 'failureDesc'|| field == 'effectDesc') {
						val.cancel = true;
					}
				}
			}
			

			// 添加功能行
			function addFunction() {
				var record = grid.getSelectionModel().getSelected();
				if (record == null) {
					alert("请选择所要添加的位置");
					return;
				}
				if(grid.getSelectionModel().getSelections().length>1){
					alert("请选择一条所要添加的位置");
					return;
				}
				var ataCode = record.get('ataCode');
				if (ataCode == null || ataCode == '') {
					alert("该记录下不能插入功能");
					return;
				}
				var index = store.indexOf(record);
				var insertIndex = -1;
				var num = 2;
				for (var i = index + 1; i < store.getCount(); i++) {
					var re = store.getAt(i);
					if (re.get('ataCode') != null
							&& re.get('ataCode') != '') {
						insertIndex = store.indexOf(re);
						break;
					}
					if (re.get('functionCode') != null
							&& re.get('functionCode') != '') {
						num++;
					}
				}

				if (insertIndex == -1) {
					insertIndex = store.getCount();
				}

//				if (num > 26) {
//					alert("所添加的功能故障总数已超过编码要求");
//					return;
//				}
				var func=ataCode+"_"+num;
				var failure = func+"AA";
				addRow(insertIndex, func, failure);
			}
			// 添加故障
			function addFailure() {
				var record = grid.getSelectionModel().getSelected();
				if (record == null) {
					alert("请选择所要添加的位置");
					return;
				}
				if(grid.getSelectionModel().getSelections().length>1){
					alert("请选择一条所要添加的位置");
					return;
				}
				var functionCode = record.get('functionCode');
				if (functionCode == null || functionCode == '') {
					alert("该记录下不能插入功能故障");
					return;
				}
				var index = store.indexOf(record);
				var insertIndex = -1;
				var num = 2;
				var finallFailureCode="AA";//记录最后一条数据的故障编号
				for (var i = index + 1; i < store.getCount(); i++) {
					var re = store.getAt(i);
					if (re.get('functionCode') != null
							&& re.get('functionCode') != '') {
						insertIndex = store.indexOf(re);
						break;
					}
					if (re.get('failureCode') != null
							&& re.get('failureCode') != '') {
						num++;
						finallFailureCode=re.get('failureCode');
					}
				}

				if (insertIndex == -1) {
					insertIndex = store.getCount();
				}

				/*if (num > 26) {
					alert("所添加的功能故障总数已超过编码要求");
					return;
				}*/
				var failure;
				if(finallFailureCode=="AA"||finallFailureCode.substring(finallFailureCode.length-2)=="AA"){
					failure = functionCode+"AB";
				}else{
					if(finallFailureCode.substring(finallFailureCode.length-1)=="Z")
						failure = functionCode+String.fromCharCode((finallFailureCode.substring(finallFailureCode.length-2,finallFailureCode.length-1).charCodeAt(0))+1)+"A";
					else 
						failure = functionCode+finallFailureCode.substring(finallFailureCode.length-2,finallFailureCode.length-1)+String.fromCharCode((finallFailureCode.substring(finallFailureCode.length-1).charCodeAt(0))+1);
					//failure = functionCode//.toString()
					//+ String.fromCharCode(64 + num);
				}
				
				addRow(insertIndex, '', failure);
			}

			// 在指定位置添加空白行
			function addRow(index, func, failure) {
				var rec = grid.getStore().recordType;
				var p = new rec({
							msetId : '',
							ataId : '',
							ataCode : '',
							ataName : '',
							functionCode : func,
							functionDesc : '',
							msetfId : '',
							failureCode : failure,
							failureDesc : ''
						});
				store.insert(index, p);
				store.modified.push(p);
				if (index > 0) {
					mset_color = grid.getView().getRow(index - 1).style.backgroundColor;
					// alert(mset_color);
				} else {
					mset_color = baColor1;
				}
				grid.getView().getRow(index).style.backgroundColor = mset_color;
			}
			function del() {
				var record1 = grid.getSelectionModel().getSelected();
				if (record1 == null) {
					alert(commonality_alertDel);
					return;
				}
				var jsonnoid = [];
				var jsonhaveid = [];
				var json = [];
				for(var i=0;i<grid.getSelectionModel().getSelections().length;i++){
					var record=grid.getSelectionModel().getSelections()[i];
					if(record.get('ataCode') !=null&&record.get('ataCode') !=""){
						alert("不能删除，一个项目编号至少存在一条记录");
						return;
					}
					json.push(record);
					if (record.get('msetId') != '' || record.get('msetfId') != '') {
						jsonhaveid.push(record.data);
					}else{
						jsonnoid.push(record.data);
					}
				}
//				if (record.get('functionCode') == '1') {
//					var functionLen = 0;
//					for (var i = 0; i < store.getCount(); i++) {
//						if (store.getAt(i).get('functionCode') != '') {
//							functionLen++;
//						}
//					}
//					if (functionLen < 2) {
//						alert("不能删除，当前页面至少要保留一个完整的功能分析!");
//						return;
//					}
//				}
				//if (record.get('msetId') != '' || record.get('msetfId') != '') {
					//alert(record.get('msetcId'));
					Ext.MessageBox.show({
								title : commonality_affirm,
								msg : "该记录已保存，如要删除，页面上修改内容都将保存。确认要删除吗？",
								buttons : Ext.Msg.YESNOCANCEL,
								fn : function(id) {
									if (id == 'cancel') {
										return;
									} else if (id == 'yes') {
										//for(var i=0;i<json.length;i++){
											store.modified.remove(json);
											store.remove(json);
										//}
										store.modified = [];
										//changeNext(json);
										changeStoreNum();
										doDelete(jsonhaveid);
									} else if (id == 'no') {
										return;
									}
								}
							});
				/*} else {
					changeNext(record);
					store.modified.remove(record);
					store.remove(record);
				}*/
			}
			function doDelete(record) {
				var json = [];
				Ext.each(store.modified, function(item) {
							json.push(item.data);
						});
				//alert(Ext.util.JSON.encode(json));
				//var deljson = [];
				//deljson.push(record.data);
				waitMsg.show();
				Ext.Ajax.request({
							url : mset.app.delMsetUrl,
							params : {
								deljson : Ext.util.JSON.encode(record),
								jsonData : Ext.util.JSON.encode(json),
								msiId : msiId
							},
							method : "POST",
							success : function(form, action) {
								var text = Ext.decode(form.responseText);
								if(text.errProCodes!=""){
									store.modified = [];
									store.reload();
									waitMsg.hide();
									alert("该功能故障已被M1.4选择为故障原因，不能删除");
								}else{
									loadStep();
									store.reload();
									store.modified = [];
									waitMsg.hide();
									parent.refreshTreeNode();
									alert(commonality_messageDelMsg);
								}
							},
							failure : function(form, action) {
								store.modified = [];
								store.reload();
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});

			}
			
			function changeStoreNum(){
				for(var index=1;index<store.getCount();index++){
					var re = store.getAt(index);
					var newlevel = getLevel(re);
					var str = store.getAt(index - 1).get('failureCode');
					if (newlevel == 1) {
						code = str.substring(0, 12)+(str.substring(12,str.length-2)*1+ 1);
						re.set('functionCode', code);
						code += 'AA';
						re.set('failureCode', code);
					}
					if (newlevel == 2) {
						var failure;
						var finallFailureCode=str;
						if(finallFailureCode=="AA"||finallFailureCode.substring(finallFailureCode.length-2)=="AA"){
							failure = finallFailureCode.substring(0,finallFailureCode.length-2)+"AB";
						}else{
							if(finallFailureCode.substring(finallFailureCode.length-1)=="Z")
								failure = finallFailureCode.substring(0,finallFailureCode.length-2)+String.fromCharCode((finallFailureCode.substring(finallFailureCode.length-2,finallFailureCode.length-1).charCodeAt(0))+1)+"A";
							else 
								failure = finallFailureCode.substring(0,finallFailureCode.length-2)+finallFailureCode.substring(finallFailureCode.length-2,finallFailureCode.length-1)+String.fromCharCode((finallFailureCode.substring(finallFailureCode.length-1).charCodeAt(0))+1);
							//failure = functionCode//.toString()
							//+ String.fromCharCode(64 + num);
						}
						re.set('failureCode', failure);
					}
					mset_color = grid.getView().getRow(index<1?0:index-1).style.backgroundColor;
					grid.getView().getRow(index).style.backgroundColor = mset_color;
				}
			}
			
			// 改变删除行后的与其同级或下级的编号,
			function changeNext(record) {
				record=store.getAt(0);
				//record=grid.getSelectionModel().getSelected();
				var level = getLevel(record);
				var index = store.indexOf(record);
				var tage = false;
				//store.remove(record);
				for (var i = index; i < store.getCount(); i++) {
					var re = store.getAt(i);
					var newlevel = getLevel(re);
					if(re.get('ataCode') !=null&&re.get('ataCode') !=""){
						break;
					}
					if (newlevel > level && tage == false) {
						store.remove(re);
						i--;
						// tage = true;
					} else {
						tage = true;
					}
					if (newlevel >= level && tage == true) {
						getChangeRecord(re, newlevel, i);
					}
				}
				//store.remove(record);
			}
			function getChangeRecord(re, newlevel, index) {
				if (index > 0) {
					var re1 = store.getAt(index - 1);
					str = re1.get('failureCode');
				} else {
					str = store.getAt(0).get('failureCode');
				}
				
				code = '';
				if (newlevel == 1) {
					code = str.substring(0, 12)+(str.substring(12,str.length-2)*1+ 1);
					re.set('functionCode', code);
					code += 'AA';
					re.set('failureCode', code);
				}
				if (newlevel == 2) {
					/*code = str.substring(0, str.length-1);
					code += String.fromCharCode((str.substring(str.length-1)
							.charCodeAt(0) + 1));
					re.set('failureCode', code);*/
					var failure;
					var finallFailureCode=str;
					if(finallFailureCode=="AA"||finallFailureCode.substring(finallFailureCode.length-2)=="AA"){
						failure = finallFailureCode.substring(0,finallFailureCode.length-2)+"AB";
					}else{
						if(finallFailureCode.substring(finallFailureCode.length-1)=="Z")
							failure = finallFailureCode.substring(0,finallFailureCode.length-2)+String.fromCharCode((finallFailureCode.substring(finallFailureCode.length-2,finallFailureCode.length-1).charCodeAt(0))+1)+"A";
						else 
							failure = finallFailureCode.substring(0,finallFailureCode.length-2)+finallFailureCode.substring(finallFailureCode.length-2,finallFailureCode.length-1)+String.fromCharCode((finallFailureCode.substring(finallFailureCode.length-1).charCodeAt(0))+1);
						//failure = functionCode//.toString()
						//+ String.fromCharCode(64 + num);
					}
					re.set('failureCode', failure);
				}
				mset_color = grid.getView().getRow(index<1?0:index-1).style.backgroundColor;
				grid.getView().getRow(index).style.backgroundColor = mset_color;
				return re;
			}
			function getLevel(record) {
				var level;
				if (record.get('functionCode') != ''
						&& record.get('functionCode') != null) {
					level = 1;
				} else if (record.get('failureCode') != ''
						&& record.get('failureCode') != null) {
					level = 2;
				}
				return level;
			}

			function saveDate(json, value) {
				Ext.MessageBox.show({
							title : commonality_affirm,
							msg : commonality_affirmSaveMsg,
							buttons : Ext.Msg.YESNOCANCEL,
							fn : function(yesNo) {
								if (yesNo == 'cancel') {
									return;
								} else if (yesNo == 'yes') {
									save(json, value);
									// store.modified = [];
								} else if (yesNo == 'no') {
									goNext(value);
									return;
								}
							}
						});
			}

			function save(json, value) {
				waitMsg.show();
				Ext.Ajax.request({
							url : mset.app.saveMsetUrl,
							params : {
								jsonData : Ext.util.JSON.encode(json),
								msiId : msiId
							},
							method : "POST",
							success : function(form, action) {
								parent.refreshTreeNode();
								store.modified = [];
								step[3] = 1;
								// Ext.Msg.alert("信 息", "保存成功！");
								waitMsg.hide();
								alert(commonality_messageSaveMsg);
								goNext(value);
								store.reload();

							},
							failure : function(form, action) {
								waitMsg.hide();
								alert(commonality_cautionMsg);
							}
						});
			}

			function zancun() {
				// 判断是否有权限修改
				if (isMaintain != '1') {					
					alert('没有暂存权限');
				} else {
					var json = [];
					Ext.each(store.modified, function(item) {
						// 去除首尾空格
						item.data.functionDesc = dislodge(item.data.functionDesc);
						item.data.failureDesc = dislodge(item.data.failureDesc);
						json.push(item.data);
					});
					if (json.length > 0) {
						Ext.MessageBox.show({
							title : commonality_affirm,
							msg : "是否需要暂存",
							buttons : Ext.Msg.YESNOCANCEL,
							fn : function(yesNo) {
								if (yesNo == 'cancel') {
									return;
								} else if (yesNo == 'yes') {
									zancunSave(json);
								} else if (yesNo == 'no') {
									goNext(value);
									return;
								}
							}
						});
					} else {
						alert('没有需要暂存的数据');
					}
				}				
			}

			function zancunSave(json) {
				waitMsg.show();
				Ext.Ajax.request({
					url : mset.app.saveZanUrl,
					params : {
						jsonData : Ext.util.JSON.encode(json),
						msiId : msiId
					},
					method : "POST",
					success : function(form, action) {
						store.modified = [];
						waitMsg.hide();
						alert(commonality_messageSaveMsg);
						store.reload();

					},
					failure : function(form, action) {
						waitMsg.hide();
						alert(commonality_cautionMsg);
					}
				});
			}

			// 去除首尾空格
			function dislodge(data) {
				if (data != null) {
					return data.trim();
				}
				return '';

			}

			// 点击下一步执行的方法
			nextOrSave = function(value) {
				if (value != null) {
					goNext(value);
					return false;
				}
				if (value == null) {
					value = 3;
				}
				for (var i = 0; i < store.getCount(); i++) {
					var record = store.getAt(i);
					if (record.get("functionCode") != ""&& (record.get("functionDesc") == ""||record.get("functionDesc") == null )) {
						alert("请填写第" + (i + 1) + "行的功能说明！");
						return;
					}
					if (record.get("failureCode") != ""&& (record.get("failureDesc") == ""||record.get("failureDesc") == null  )) {
						alert("请填写第"+ (i + 1) + "行的功能故障！");
						return;
					}
				}
				if (store.modified.length == 0) {
					goNext(value);
					return false;
				}
				// 判断是否有权限修改
				if (isMaintain != '1') {
					// alert("您没有修改权限，无权修改！");
					goNext(value);
					return;
				}

				

				var result = true;
				var json = [];
				var highestLevel = store.getAt(0).get('proCode');
				// alert(highestLevel);
				Ext.each(store.modified, function(item) {
					// 去除首尾空格
					item.data.functionDesc = dislodge(item.data.functionDesc);
					item.data.failureDesc = dislodge(item.data.failureDesc);
					json.push(item.data);
				});
				if (json.length > 0 && result == true) {
					saveDate(json, value);
				}
			};

		}
	};
}();