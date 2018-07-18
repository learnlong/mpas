Ext.namespace('za1');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/za1/";	
	
	// 检查必输项长度错误提示信息
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();

	var zaForm = new Ext.form.FormPanel({
		bodyStyle : "align:left", 
		id : "topForm", 
		labelWidth : 160, //标题宽度
		labelAlign : "right", 
		region : "center", 
		frame : true, 
		title : returnPageTitle('区域数据', 'za1'),// task_title,
		items : [
			new Ext.form.TextField({
				fieldLabel : "适用性", 
				name : "effectiveness", 
				id : "effectiveness", 							
				width : "95%", 
				value : effectiveness,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			}),
			new Ext.form.TextArea({
				fieldLabel : "边界", 
				name : "za1.border", 
				id : "za1_border", 
				width : "95%", 
				height : 58, 
				value : border,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			}), 
			new Ext.form.TextArea({
				fieldLabel : "环境", 
				name : "za1.env", 
				id : "za1_env", 
				width : "95%", 
				height : 58, 
				value : env,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			}), 
			new Ext.form.TextArea({
				fieldLabel : "口盖",
				name : "za1.reachWay",
				id : "za1_reachWay",
				width : "95%",
				height : 58,
				value : reachWay,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			}), 
			new Ext.form.TextArea({
				fieldLabel : "设备/结构项目",
				name : "za1.equStruc",
				id : "za1_equStruc",
				width : "95%",
				height : 58,
				value : equStruc,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			}), 
			new Ext.form.TextArea({
				fieldLabel : "备注",
				name : "za1.remark",
				id : "za1_remark",
				width : "95%",
				height : 58,
				value : remark,
				maxLength : 4000,
				maxLengthText : '输入信息长度超过4000个字符!'
			}), new Ext.form.TextField({
				name : "za1.zaMain.zaId", 
				value : zaId, 
				hidden : true
			}), new Ext.form.TextField({
				name : "za1.za1Id", 
				value : za1Id, 
				hidden : true
			}), 
			{
				layout : "column", 
				items : [{
					columnWidth : 0.3, 
					layout : "form", 
					labelAlign : "right", 
					labelWidth : 200, //标题宽度
					items : [
						new Ext.form.RadioGroup({
							fieldLabel : "此区域只包含结构吗" + commonality_mustInput, 
							id : "za1_hasStrucOnly", 
							width : 90,//"是","否" 按钮的宽度
							items : [
								new Ext.form.Radio({
									boxLabel : "是", 
									name : "za1.hasStrucOnly", 
									inputValue : "1", 
									checked : false, 
									handler : function (el, checked) {
										checkOptions("hasStrucOnly", checked);
									}
								}), 
								new Ext.form.Radio({
									boxLabel : "否", 
									name : "za1.hasStrucOnly", 
									inputValue : "0", 
									handler : function (el, checked) {
										checkOptions("hasStrucOnly", !checked);
									}
								})
							]
						})
					]
				},  {
					columnWidth : 0.3, 
					labelWidth : 200, //标题宽度
					labelAlign : "left", 
					layout : "form", 
					items : [
						new Ext.form.RadioGroup({
							fieldLabel : "是否需要进行区域分析" + commonality_mustInput, 
							id : "za1_needAreaAnalyze", 
							width : 90, //"是","否" 按钮的宽度
							items : [
								new Ext.form.Radio({
									boxLabel : "是", 
									name : "za1.needAreaAnalyze", 
									inputValue : "1", 
									checked : false, 
									handler : function (el, checked) {
										checkOptions("needAreaAnalyze", checked);
									}
								}), 
								new Ext.form.Radio({
									boxLabel : "否", 
									name : "za1.needAreaAnalyze", 
									inputValue : "0", 
									handler : function (el, checked) {
										checkOptions("needAreaAnalyze", !checked);
									}
								})
							]
						})
					]
				}]
			}, 
			{
				layout : "column", 
				items : [{
					columnWidth : 0.3, 
					layout : "form", 
					labelAlign : "right", 
					labelWidth : 200, //标题宽度
					items : [
						new Ext.form.RadioGroup({
							fieldLabel : "区域包含导线吗" + commonality_mustInput, 
							id : "za1_hasPipe", 
							width : 90,//"是","否" 按钮的宽度
							items : [
								new Ext.form.Radio({
									boxLabel : "是", 
									name : "za1.hasPipe", 
									inputValue : "1", 
									checked : false, 
									handler : function (el, checked) {
										checkOptions("hasPipe", checked);
									}
								}), 
								new Ext.form.Radio({
									boxLabel : "否", 
									name : "za1.hasPipe", 
									inputValue : "0", 
									handler : function (el, checked) {
										checkOptions("hasPipe", !checked);
									}
								})
							]
						})
					]
				},  {
					columnWidth : 0.3, 
					labelWidth : 200, //标题宽度
					labelAlign : "left", 
					layout : "form", 
					items : [
						new Ext.form.RadioGroup({
							fieldLabel : "区域中有可燃材料吗" + commonality_mustInput, 
							id : "za1_hasMaterial", 
							width : 90, //"是","否" 按钮的宽度
							items : [
								new Ext.form.Radio({
									boxLabel : "是", 
									name : "za1.hasMaterial", 
									inputValue : "1", 
									checked : false, 
									handler : function (el, checked) {
										checkOptions("hasMaterial", checked);
									}
								}), 
								new Ext.form.Radio({
									boxLabel : "否", 
									name : "za1.hasMaterial", 
									inputValue : "0", 
									handler : function (el, checked) {
										checkOptions("hasMaterial", !checked);
									}
								})
							]
						})
					]
				},  {
					columnWidth : 0.4, 
					labelWidth : 200, //标题宽度
					labelAlign : "left", 
					layout : "form", 
					items : [
						new Ext.form.RadioGroup({
							fieldLabel : "导线是否同时靠近液压、机械或电飞控系统的主要及备份系统" + commonality_mustInput, 
							id : "za1_closeToSystem", 
							width : 90, //"是","否" 按钮的宽度
							items : [
								new Ext.form.Radio({
									boxLabel : "是", 
									name : "za1.closeToSystem", 
									inputValue : "1", 
									checked : false, 
									handler : function (el, checked) {
										checkOptions("closeToSystem", checked);
									}
								}), 
								new Ext.form.Radio({
									boxLabel : "否", 
									name : "za1.closeToSystem", 
									inputValue : "0", 
									handler : function (el, checked) {
										checkOptions("closeToSystem", !checked);
									}
								})
							]
						})
					]
				}]
			}
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
				layout : 'border',
				region : 'center',
				items : [zaForm]
			}
		]
	});

	var radioHasStrucOnly = Ext.getCmp("za1_hasStrucOnly");
	radioHasStrucOnly.eachItem(function (item) {
		item.setValue(item.inputValue == hasStrucOnly);
	});

	var radioNeedAreaAnalyze = Ext.getCmp("za1_needAreaAnalyze");
	radioNeedAreaAnalyze.eachItem(function (item) {
		item.setValue(item.inputValue == needAreaAnalyze);
	});

	var radioHasPipe = Ext.getCmp("za1_hasPipe");
	radioHasPipe.eachItem(function (item) {
		item.setValue(item.inputValue == hasPipe);
	});

	var radioHasMaterial = Ext.getCmp("za1_hasMaterial");
	radioHasMaterial.eachItem(function (item) {
		item.setValue(item.inputValue == hasMaterial);
	});

	var radioCloseToSystem = Ext.getCmp("za1_closeToSystem");
	radioCloseToSystem.eachItem(function (item) {
		item.setValue(item.inputValue == closeToSystem);
	});

	var dataOld = Ext.util.JSON.encode(zaForm.form.getValues(false));//该字段保存初始加载完成时的所有数据

	//依据按钮的选择，判断后续按钮是否有效
	function checkOptions(type, checked) {
		if (type == "hasStrucOnly") {
			if (checked){
				setRadioStatus(Ext.getCmp("za1_needAreaAnalyze"), true);
				setRadioStatus(Ext.getCmp("za1_hasPipe"), false);
				setRadioStatus(Ext.getCmp("za1_hasMaterial"), false);
				setRadioStatus(Ext.getCmp("za1_closeToSystem"), false);
			} else {
				setRadioStatus(Ext.getCmp("za1_needAreaAnalyze"), false);
				setRadioStatus(Ext.getCmp("za1_hasPipe"), true);
				setRadioStatus(Ext.getCmp("za1_hasMaterial"), false);
				setRadioStatus(Ext.getCmp("za1_closeToSystem"), false);
			}
		}
		if (type == "hasPipe") {
			if (checked){
				setRadioStatus(Ext.getCmp("za1_hasMaterial"), true);
				setRadioStatus(Ext.getCmp("za1_closeToSystem"), false);
			} else {
				setRadioStatus(Ext.getCmp("za1_hasMaterial"), false);
				setRadioStatus(Ext.getCmp("za1_closeToSystem"), false);
			}
		}
		if (type == "hasMaterial") {
			if (checked){
				setRadioStatus(Ext.getCmp("za1_closeToSystem"), false);
			} else {
				setRadioStatus(Ext.getCmp("za1_closeToSystem"), true);
			}
		}
	}

	//设置按钮是否有效
	function setRadioStatus(radioCmp, isEnable){
		if (isEnable){
			radioCmp.enable();
		} else {
			radioCmp.reset();
			radioCmp.disable();
		}
	}

	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			value = 1;
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

	/**
	 * 进行"下一步"操作之前,先进行保存数据的验证
	 */
	function saveData(value) {
		var dataNew = Ext.util.JSON.encode(zaForm.form.getValues(false));
		if (dataNew != dataOld) {			
			var hasStrucOnly;
			var needAreaAnalyze;
			var hasPipe;
			var hasMaterial;
			var closeToSystem;

			radioHasStrucOnly.eachItem(function (item) {
				hasStrucOnly = item.getGroupValue();
			});
			radioNeedAreaAnalyze.eachItem(function (item) {
				needAreaAnalyze = item.getGroupValue();
			});
			radioHasPipe.eachItem(function (item) {
				hasPipe = item.getGroupValue();
			});
			radioHasMaterial.eachItem(function (item) {
				hasMaterial = item.getGroupValue();
			});
			radioCloseToSystem.eachItem(function (item) {
				closeToSystem = item.getGroupValue();
			});
			
			if (hasStrucOnly == null){
				alert("请选择‘此区域只包含结构吗’");
				return false;
			} else if (hasStrucOnly == 1 && needAreaAnalyze == null){
				alert("请选择‘是否需要进行区域分析’");
				return false;
			} else if (hasStrucOnly == 0 && hasPipe == null){
				alert("请选择‘区域包含导线吗’");
				return false;
			} else if (hasPipe == 1 && hasMaterial == null){
				alert("请选择‘区域中有可燃材料吗’");
				return false;
			} else if (hasMaterial == 0 && closeToSystem == null){
				alert("请选择‘导线是否同时靠近液压、机械或电飞控系统的主要及备份系统’");
				return false;
			}
			
			Ext.MessageBox.show({
				title : commonality_affirm, 
				msg : commonality_affirmSaveMsg, 
				buttons : Ext.Msg.YESNOCANCEL,
				fn : function(btn){
					if (btn == 'yes'){
						saveInfo();
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

	/**
	 * 验证成功后,进行保存操作,与后台进行交换
	 */
	function saveInfo() {
		setRadioStatus(Ext.getCmp("za1_needAreaAnalyze"), true);
		setRadioStatus(Ext.getCmp("za1_hasPipe"), true);
		setRadioStatus(Ext.getCmp("za1_hasMaterial"), true);
		setRadioStatus(Ext.getCmp("za1_closeToSystem"), true);

		zaForm.form.doAction("submit", {
			url : urlPrefix + "save.do",
			method:"POST",
			waitMsg:commonality_waitMsg,
			params : {
				analysisType : analysisType
			},
			success:function (form, action) {
				if (action.result.success) {
					parent.refreshTreeNode();
					alert(commonality_messageSaveMsg);
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