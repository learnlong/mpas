Ext.namespace('za42');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/za42/";
	var oldStep6Desc;
	var oldStep7Desc;
	var storeRemoveId = '';

	//矩阵模块	
	var tableHtmlStr = 
		'<table style="padding:0; margin:0; font-size:11px;">' +
		'<tr>' +
		'<td>' +
		'<table id="table1" border="1" height="100" width="175" cellpadding="1" cellspacing="0" style="text-align:center; background-color:#FFFFFF; padding:0; margin:0;" >'+
		'<tr><td>级号</td><td>区域大小</td><td>稠密度</td><td>潜在影响</td></tr>'+
		'<tr><td>1</td><td onclick="select(1, 1);" style="cursor:pointer">大</td><td onclick="select(1, 2);" style="cursor:pointer">高</td><td onclick="select(1, 3);" style="cursor:pointer">高</td></tr>'+
		'<tr><td>2</td><td onclick="select(2, 1);" style="cursor:pointer">中</td><td onclick="select(2, 2);" style="cursor:pointer">中</td><td onclick="select(2, 3);" style="cursor:pointer">中</td></tr>'+
		'<tr><td>3</td><td onclick="select(3, 1);" style="cursor:pointer">小</td><td onclick="select(3, 2);" style="cursor:pointer">低</td><td onclick="select(3, 3);" style="cursor:pointer">低</td></tr>'+
		'</table>' + 
		'</td>'+
		'<td>'+
		'<table id="table2" border="1" height="100" width="135" cellpadding="1" cellspacing="0" style="text-align:center; background-color:#DFE8F6; padding:0; margin:0;" >'+
		'<tr><td colspan="2" rowspan="2"></td><td colspan="3">区域大小</td></tr>' + 
		'<tr><td width="22">1</td><td width="22">2</td><td width="22">3</td></tr>'+
		'<tr><td width="36" rowspan="3">稠密度</td><td>1</td><td>1</td><td>1</td><td>2</td></tr>' +
		'<tr><td>2</td><td>1</td><td>2</td><td>2</td></tr>' +
		'<tr><td>3</td><td>2</td><td>2</td><td>3</td></tr>' + 
		'</table>' + 
		'</td>'+
		'<td>'+
		'<table id="table3" border="1" height="100" width="150" cellpadding="1" cellspacing="0" style="text-align:center; background-color:#DFE8F6; padding:0; margin:0;" >'+
		'<tr><td colspan="2" rowspan="2"></td><td colspan="3">潜在影响</td></tr>' +
		'<tr><td>1</td><td>2</td><td>3</td></tr>' +
		'<tr><td width="50" rowspan="3">区域大小/稠密度</td><td width="22">1</td><td>1</td><td>1</td><td>2</td></tr>'+
		'<tr><td>2</td><td>1</td><td>2</td><td>2</td></tr>' +
		'<tr><td>3</td><td>2</td><td>2</td><td>3</td></tr>' +
		'</table>' +
		'</td>' +
		'</tr>'+
		'<tr>' +
		'<td colspan="3">'+
		'<table id="table4" border="1" line-height:"18px" height="85" width="465" cellpadding="1" cellspacing="0" style="text-align:left; background-color:#DFE8F6; padding:0; margin:0;" >'+
		'<tr><td rowspan="3" align="center">注:</td><td align="center">1级:</td><td>GVI区域内的所有导线，并对特定部位的导线进行单独的GVI或者DET</td></tr>' +
		'<tr><td align="center">2级:</td><td>GVI区域内的所有导线，并对特定部位的导线进行单独的GVI</td></tr>'+
		'<tr><td align="center">3级:</td><td>GVI区域内的所有导线</td></tr>' +
		'</table>'+
		'</td></tr></table>';
	
	var tableForm = new Ext.form.FormPanel({
		frame : true,
		html : tableHtmlStr
	});	

	//插图模块	
	var imgHtmlStr =
		'<table width="95%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFE8F6" background-color="#DFE8F6" id="imageTable"> '+
		'<tr> <td colspan="15" width="30%"></td><td colspan="20" width="40%" align="center" id="question1" bordercolor="#000">验证检查级别</td> <td colspan="30" width="25%"></td> </tr> '+
		'<tr> <td colspan="25" width="50%" style="border-right-color: #000;"></td> <td colspan="25" width="50%" height="8"></td> </tr> '+
		'<tr> <td colspan="50" id="question6" bordercolor="#000" align="center">6.GVI区域内的所有导线是否有效？</td> </tr> '+
		'<tr> <td colspan="8" width="16%" style="border-right-color: #000;"></td> <td colspan="24" width="48%" height="8"></td> '+
			'<td colspan="18" width="36%" style="border-left-color: #000;"></td> </tr> '+
		'<tr> <td colspan="5" width="10%"></td> <td colspan="6" width="12%" bordercolor="#000"> <div align="center" id="radio1Yes">是</div> </td> '+
			'<td colspan="18" width="36%"></td> <td colspan="6" width="12%" bordercolor="#000"> <div align="center" id="radio1No">否</div> </td> '+
			'<td colspan="15" width="30%"></td> </tr> '+
		'<tr> <td colspan="8" width="16%" style="border-right-color: #000;"></td> <td colspan="24" width="48%" height="8"></td> '+
			'<td colspan="18" width="36%" style="border-left-color: #000;"></td> </tr> '+
		'<tr> <td colspan="8" width="16%" style="border-right-color: #000;"></td> <td colspan="14" width="28%"></td> '+
			'<td colspan="20" width="40%" bordercolor="#000" id="question2" align="center">7.GVI区域内的所有导线，并对特殊部位的导线进行单独的GVI或者DET</td><td colspan="8" width="16%"></td> </tr> ' +
		'<tr> <td colspan="8" width="16%" style="border-right-color: #000;"></td> <td colspan="24" width="48%" height="8"></td> '+
			'<td colspan="18" width="36%" style="border-left-color: #000;"></td> </tr> '+
		'<tr> <td colspan="8" width="16%" style="border-right-color: #000;"></td> <td colspan="17" width="34%" style="border-right-color: #000;"></td> '+
			'<td colspan="17" width="34%" style="border-top-color: #000;">&nbsp;&nbsp;</td> <td colspan="8" width="16%" style="border-left-color: #000;"></td> </tr> '+
		'<tr> <td colspan="16" width="32%" bordercolor="#000" id="question7" align="center">确定对整个区域内导线的GVI间隔</td> <td colspan="1" width="2%"></td> '+
			'<td colspan="16" width="32%" bordercolor="#000" id="question8" align="center">确定对整个区域内导线的GVI间隔</td> <td colspan="1" width="2%"></td> '+
			'<td colspan="16" width="32%" bordercolor="#000" id="question9" align="center">确定需要单独GVI或DET的导线部位，并确定对这些部位导线的单独的GVI或DET的间隔</td> </tr>' +
		'</table>';	
		
	var imgForm = new Ext.form.FormPanel({
		frame : true,
		html : imgHtmlStr
	});	

	// 问题6
	var form6 = new Ext.form.FormPanel({
		id : 'from6',    
		title : '6.GVI区域内的所有导线是否有效？' + commonality_mustInput,
		labelWidth : 5,
		labelAlign : "left",
		region : 'center',
		frame : true,
		items: [
			new Ext.form.RadioGroup({
				width : 120,
				id : 'radioOne',
				items : [
					new Ext.form.Radio({       
						id : 'radio1',
						boxLabel : '是',
						name : 'step6',
						inputValue : 'Y',
						height : 20
					}), 
					new Ext.form.Radio({
						id : 'redio2',
						boxLabel : '否',
						name : 'step6',
						inputValue : 'N',
						height : 20
					})
				]
			}),
			new Ext.form.TextArea({
				name : 'step6Desc',
				id : 'step6Desc',
				width : '95%',
				height : 50
			})
		]
	});

	// 问题7
	var form7 = new Ext.form.FormPanel({
		id : 'from7',    
		title : '7.GVI区域内的所有导线，并对特殊部位的导线进行单独的GVI或者DET',
		labelWidth : 5,
		labelAlign : "left",
		region : 'center',
		frame : true,
		items: [			
			new Ext.form.TextArea({
				name : 'step7Desc',
				id : 'step7Desc',
				width : '95%',
				height : 50
			})
		]
	});

	var typeCombo = new Ext.form.ComboBox({
		name : 'chooseType',
		id : 'chooseType',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['GVI', 'GVI'], ['DET', 'DET']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		editable : false,
		triggerAction : 'all'
	});

	// 问题7的添加任务列表
	var store = new Ext.data.Store({
		url : urlPrefix + "loadTaskMsg.do?zaId=" + zaId,
		reader : new Ext.data.JsonReader({						
			root : "taskMsgList",
			fields : [
				{name : 'taskId'},
				{name : 'taskType'},
				{name : 'taskDesc'}
			]
		})
	});
	store.load();
			
	var colM = new Ext.grid.ColumnModel([
		{
			header : '任务ID',
			hidden : true, 
			dataIndex : 'taskId'
		}, {
			header : '任务类型' + commonality_mustInput,
			dataIndex : "taskType",
			width : 100,
			align : 'center',
			editor : typeCombo,
			renderer : function(value, cellmeta, record) {
				var index = typeCombo.store.find(Ext.getCmp('chooseType').valueField, value);
				var record = typeCombo.store.getAt(index);
				var returnvalue = "";
				if (record) {
					returnvalue = record.data.displayText;
				}
				return returnvalue;
			}
		}, {
			header : '任务描述' + commonality_mustInput,
			dataIndex : "taskDesc",
			width : 200,
			align : 'center',
			renderer: changeBR,
			editor : new Ext.form.TextArea( {
				allowBlank : false,
				grow : true,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			})
		}
	]);
			
	var grid = new Ext.grid.EditorGridPanel({
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({singleSelect:true}),//用于选择一条记录
		title : '7.任务列表',
		store : store,
		clicksToEdit : 1,
		stripeRows : true,
		tbar : [
		    new Ext.Button({
				id : "add",
				text : commonality_add,
				iconCls : "icon_gif_add",
				handler : function() {
		    		var index = store.getCount();
					var rec = store.recordType;
					var p = new rec( {
						taskId : '',
						taskType : '',
						taskDesc : ''
					});
					store.insert(index, p);
				}
			}), "-", 
			new Ext.Button({
				id : "del",
				text : commonality_del,
				iconCls : 'icon_gif_delete',
				handler : function() {
					var record = grid.getSelectionModel().getSelected();
					if (record === undefined || record === null){
						alert(commonality_alertDel);
						return;
					}
					
					if (record.get("taskId") != '') {
						storeRemoveId = storeRemoveId + record.get("taskId") + ";";
					}
					
					store.remove(record);
					store.modified.remove(record);
				}
			})
		]
	});	

	var headerStepForm = new Ext.form.Label({
		applyTo : 'headerStepDiv'
	});

	var viewport = new Ext.Viewport( {
		layout : 'border',
		items : [
			{ 
				region : 'north',
				height : 60,
				frame : true,
				items : [headerStepForm]
			},{
				title : returnPageTitle('增强区域任务类型确定表','za42'),
				layout : 'border',
				region : 'center',
				items : [
					{
						region : 'west',
						layout : 'border', 
						width : '61%',    								              	      		  
						items : [
							{	
								region : 'north',
								layout : 'fit',
								height : 210,  	   								              	      		  
								items : [tableForm]
							},{
								region : 'center',
								layout : 'fit',
								items : [imgForm]
							}
						]
					}, {
						region : 'center',
						layout : 'border',	   								              	      		  
						items : [
							{
								region : 'north',
								layout : 'border',
								height : 205,
								items : [ 
									{
										region : 'north',
										layout : 'fit',
										height : 115,
										items : [form6]
									},{
										region : 'center',
										layout : 'fit',
										items : [form7]
									} 
								]
							}, {
								region : 'center',
								layout : 'fit',
								items : [grid]
							}
						]
					}
				]
			}
		]
	});

	Ext.Ajax.request( {
		url : urlPrefix + "loadZa42.do?za42Id=" + za42Id,
		method : 'POST',
		callback: function(options, success, response){
			var all = Ext.util.JSON.decode(response.responseText);

			if (all.za42 != null){
				ary[0] = all.za42.select1;//"区域大小"赋值
				ary[1] = all.za42.select2;//"稠密度"赋值
				ary[2] = all.za42.select3;//"潜在原因"赋值
				ary[3] = all.za42.result;//"注释"矩阵赋值
				
				oldAreaSizeValue = ary[0];
				oldDenseDegValue = ary[1];
				oldFireProValue = ary[2];
				oldLastResultValue = ary[3];
				
				//"区域大小"赋值
				if(ary[0] == 1){
					select(1,1);
				}else if(ary[0] == 2){
					select(2,1);
				}else if(ary[0] == 3){
					select(3,1);
				}
				//"稠密度"赋值
				if(ary[1] == 1){
					select(1,2);
				}else if(ary[1] == 2){
					select(2,2);
				}else if(ary[1] == 3){
					select(3,2);
				}
				//"潜在原因"赋值
				if(ary[2] == 1){
					select(1,3);
				}else if(ary[2] == 2){
					select(2,3);
				}else if(ary[2] == 3){
					select(3,3);
				}

				Ext.getCmp('step6Desc').setValue(all.za42.step6Desc);
				Ext.getCmp('step7Desc').setValue(all.za42.step7Desc);
				oldStep6Desc = Ext.util.JSON.encode(all.za42.step6Desc);
				oldStep7Desc = Ext.util.JSON.encode(all.za42.step7Desc);
			}
		}
	});

	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			value = 4;
			// 判断是否有权限修改
			if (analysisFlag) {
				saveData(value);
			} else {			
				goNext(value);
			}			
		} else {
			goNext(value);
		}	
	}

	function saveData(value) {
		if(areaSizeValue == 0){
			alert('请选择“区域大小”');
			return false;
		}
		if(denseDegValue == 0){
			alert('请选择“稠密度”');
			return false;
		}
		if(fireProValue == 0){
			alert('请选择“潜在影响”');
			return false;
		}
		
		var isRequir = false;//是否修改过，false没有，true有
		if (oldAreaSizeValue != areaSizeValue || oldDenseDegValue != denseDegValue || oldFireProValue != fireProValue){
			isRequir = true;
		}

		var newStep6Desc = Ext.util.JSON.encode(Ext.getCmp('step6Desc').getValue());
		var newStep7Desc = Ext.util.JSON.encode(Ext.getCmp('step7Desc').getValue());
		
		if (newStep6Desc != oldStep6Desc || newStep7Desc != oldStep7Desc) {
			isRequir = true;
		}

		var json = [];
		Ext.each(store.modified, function(item) {
			json.push(item.data);
		});
		
		if (json.length > 0) {
			isRequir = true;
		}

		if (storeRemoveId != '') {
			isRequir = true;
		}
		
		if (isRequir){		
			Ext.MessageBox.show({
				title : commonality_affirm,
				msg : commonality_affirmSaveMsg,
				buttons : Ext.Msg.YESNOCANCEL,
				fn : function(btn){
					if (btn == 'yes'){
						saveInfo(json);
					} else if (btn == 'no'){
						goNext(value);
					}else{
						return;
					}
				}
			});	
		} else {					
			goNext(value);			
		}
	}
	
	function saveInfo(json){
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true// 完成后移除
		});		
		waitMsg.show();

		form7.form.doAction("submit", {
			url : urlPrefix + "saveZa42.do",
			method : "POST",
			waitMsg : commonality_waitMsg,
			params : {
				'zaId' : zaId,
				'za42.za42Id' : za42Id,
				'za42.select1' : areaSizeValue,
				'za42.select2' : denseDegValue,
				'za42.select3' : fireProValue,
				'za42.result' : lastResultValue,
				'za42.step6Desc' : Ext.getCmp('step6Desc').getValue(),
				'za42.step7Desc' : Ext.getCmp('step7Desc').getValue(),
				'jsonData' : Ext.util.JSON.encode(json),
				'storeRemoveId' : storeRemoveId
			},
			success : function (form, action) {
				if (action.result.success) {
					alert(commonality_messageSaveMsg);
					parent.refreshTreeNode();
					successNext(action.result.nextStep);
				} else {
					alert(commonality_saveMsg_fail);
				}
			}, 
			failure:function (form, action) {				
				alert(commonality_saveMsg_fail);				
			}
		});		
	}
});