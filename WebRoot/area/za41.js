Ext.namespace('za41');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/za41/";
	var dataOld;
	
	// 检查必输项长度错误提示信息
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();

	var imageHtmlStr = 
		'<table width="100%" border="0px" cellspacing="0" cellpadding="2" class="imageHtml" > '+
		'<tr> <td colspan="17" id="question1" class="td4">1.区域包含导线吗？</td> </tr> '+
		'<tr> <td colspan="3" rowspan="3">&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> '+
			'<td colspan="10" rowspan="3">&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> </tr> '+
		'<tr> <td colspan="2" id="radio1Yes" class="td4">是</td> <td colspan="2" id="radio1No" class="td4">否</td> </tr> '+
		'<tr> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td rowspan="17" class="td1">&nbsp;</td> '+
			'<td rowspan="17">&nbsp;</td> </tr> '+
		'<tr> <td colspan="15" id="question0" class="td4">导线部分</td> </tr> '+
		'<tr> <td rowspan="3">&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td colspan="10" rowspan="3">&nbsp;</td> '+
			'<td class="td1">&nbsp;</td> <td>&nbsp;</td> </tr> '+
		'<tr> <td colspan="2" id="radio0Yes" class="td4">是</td> <td colspan="2" id="radio0No" class="td4">否</td> </tr> '+
		'<tr> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td rowspan="13" class="td1">&nbsp;</td> <td rowspan="13">&nbsp;</td> </tr> '+
		'<tr> <td colspan="10" id="question2" class="td4">2.区域中有可燃材料吗？</td> <td colspan="3" rowspan="4">&nbsp;</td> </tr> '+
		'<tr> <td rowspan="3">&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td colspan="4" rowspan="3">&nbsp;</td> '+
			'<td rowspan="11">&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> </tr> '+
		'<tr> <td colspan="2" id="radio2Yes" class="td4">是</td> <td colspan="2" id="radio2No" class="td4">否</td> </tr> '+
		'<tr> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> </tr>'+
		'<tr> <td colspan="7" id="question4" class="td4">4.是否有有效的工作可以明显降低可燃材料堆积的可能性？</td> '+
			'<td colspan="5" id="question3" class="td4">3.导线是否同时靠近（距离在2IN/50mm内）飞控系统的主要及备份系统？</td> </tr> '+
		'<tr> <td rowspan="3">&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td colspan="2" rowspan="3">&nbsp;</td> '+
			'<td class="td1">&nbsp;</td> <td>&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td rowspan="7">&nbsp;</td> '+
			'<td class="td1">&nbsp;</td> <td>&nbsp;</td> </tr> '+
		'<tr> <td colspan="2" id="radio4Yes" class="td4">是</td> <td colspan="2" id="radio4No" class="td4">否</td> '+
			'<td colspan="2" id="radio3Yes" class="td4">是</td> <td colspan="2" id="radio3No" class="td4">否</td> </tr> '+
		'<tr> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td rowspan="5" class="td1">&nbsp;</td> <td rowspan="5">&nbsp;</td> '+
			'<td rowspan="5" class="td1">&nbsp;</td> <td rowspan="5">&nbsp;</td> <td rowspan="5" class="td1">&nbsp;</td> '+
			'<td rowspan="5">&nbsp;</td> </tr> '+
		'<tr> <td colspan="5" id="question5" class="td4">5.确定维修工作和间隔</td> </tr>'+
		'<tr> <td rowspan="3">&nbsp;</td> <td class="td1">&nbsp;</td> <td>&nbsp;</td> <td colspan="2" rowspan="3">&nbsp;</td> </tr> '+
		'<tr> <td colspan="2" id="question6" class="td4">继续分析</td> </tr> <tr> <td class="td1">&nbsp;</td> <td>&nbsp;</td> </tr> '+
		'<tr> <td colspan="10" id="question7" class="td4">确定检查工作；确定线检查级别<br>验证检查级别；确定线检查间隔</td> '+
			'<td>&nbsp;</td> <td colspan="6"  id="question8" class="td4">进行标准区域分析</td> </tr> </table>';

	/**
	 * 绘制ZA42分析步骤的逻辑判断图的样式
	 */
	var imgForm = new Ext.form.FormPanel({
		region : 'west',
		width : '52%',
		frame : true,
		autoScroll : true,
		html : imageHtmlStr
	});

	/**
	 * 绘制右边问题一栏的form表达样式
	 */
	var centerForm = new Ext.form.FormPanel({
		id : 'centerForm',
		labelWidth : 40,
		labelAlign : "left",
		autoScroll : true,
		region : 'center',
		frame : true,
		items : [ 
			{
				xtype : "hidden",
				id : "za41Id",
				name : "za41.za41Id"
			}, 
			new Ext.form.RadioGroup({
				width : 120,
				fieldLabel : '(1)&nbsp;',
				id : "radioOne",
				name : "radio1",
				items : [ 
					new Ext.form.Radio({
						id : 'stepStatus11',
						boxLabel : '是',
						name : "za41.step1",
						height : 20,
						inputValue : 1,
						listeners : {
							'check' : function(checkbox, checked){ 
								 if(checked){
									document.getElementById('question1').className = 'questionHighlight';
									document.getElementById('question0').className = 'questionHighlight';
									document.getElementById('radio1Yes').className = 'optionHighlight';
									document.getElementById('radio1No').className = 'td4';
								}
							}
						}
					}), 
					new Ext.form.Radio({
						id : 'stepStatus12',
						boxLabel : '否',
						name : "za41.step1",
						height : 20,
						inputValue : 0,
						listeners : {
							'check' : function(checkbox, checked){ 
								 if(checked){
									document.getElementById('question1').className = 'questionHighlight';
									document.getElementById('question0').className = 'td4';
									document.getElementById('radio1Yes').className = 'td4';
									document.getElementById('radio1No').className = 'optionHighlight';
									document.getElementById('question8').className = 'questionHighlight';
								}
							}
						}
					}), 
					new Ext.form.Radio({
						id : 'stepStatus13',
						boxLabel : '无效',
						name : "za41.step1",
						inputValue : -1,
						height : 20,
						hidden : 'true',
						listeners : {
			      	      	'check' : function(checkbox, checked){ 
		                         if(checked){
								  	document.getElementById('question1').className = 'td4';
								  	document.getElementById('radio1Yes').className = 'td4';
									document.getElementById('radio1No').className = 'td4';
								}
						    }
						}
					})
				]
			}),
			new Ext.form.TextArea({
				name : 'za41.step1Desc',
				id : 'step1Desc',
				width : '95%',
				height : 50
			}),
			new Ext.form.RadioGroup({
				width : 120,
				fieldLabel : '(2)&nbsp;', 
				id : 'radioTwo',
				name : "radio2",
				items : [ 
					new Ext.form.Radio({
						id : 'stepStatus21',
						boxLabel : '是',
						name : "za41.step2",
						inputValue : 1,
						height : 20,
						listeners : {
							'check' : function(checkbox, checked){ 
								 if(checked){
									document.getElementById('radio0Yes').className = 'optionHighlight';
									document.getElementById('radio0No').className = 'td4';
									document.getElementById('question2').className = 'questionHighlight';
									document.getElementById('radio2Yes').className = 'optionHighlight';
									document.getElementById('radio2No').className = 'td4';
								}
							}
						}
					}), 
					new Ext.form.Radio({
						id : 'stepStatus22',
						boxLabel : '否',
						name : "za41.step2",
						inputValue : 0,
						height : 20,
						listeners : {
							'check' : function(checkbox, checked){ 
								 if(checked){
									document.getElementById('radio0Yes').className = 'optionHighlight';
									document.getElementById('radio0No').className = 'td4';
									document.getElementById('question2').className = 'questionHighlight';
									document.getElementById('question5').className = 'td4';
									document.getElementById('question6').className = 'td4';
									document.getElementById('radio2Yes').className = 'td4';
									document.getElementById('radio2No').className = 'optionHighlight';
								}
							}
						}
					}), 
					new Ext.form.Radio({
						id : 'stepStatus23',
						boxLabel : '无效',
						name : "za41.step2",
						inputValue : -1,
						hidden : 'true',
						height : 20,
						listeners : {
			      	      	'check' : function(checkbox, checked){ 
		                         if(checked){
								  	document.getElementById('radio0Yes').className = 'td4';
		                         	document.getElementById('radio0No').className = 'td4';
								  	document.getElementById('question2').className = 'td4';
								  	document.getElementById('radio2Yes').className = 'td4';
									document.getElementById('radio2No').className = 'td4';
								}
						    }
						}
					})
				]
			}), 
			new Ext.form.TextArea({
				name : 'za41.step2Desc',
				id : 'step2Desc',
				width : '95%',
				height : 50
			}),				
			new Ext.form.RadioGroup({
				width : 120,
				fieldLabel : '(3)&nbsp;',
				id : 'radioThree',
				name : "radio3",
				items : [ 
					new Ext.form.Radio({
						boxLabel : '是',
						name : "za41.step3",
						id : 'stepStatus31',
						inputValue : 1,
						height : 20,
						listeners : {
							'check' : function(checkbox, checked){ 
								 if(checked){
									document.getElementById('question3').className = 'questionHighlight';
									document.getElementById('radio3Yes').className = 'optionHighlight';
									document.getElementById('radio3No').className = 'td4';
									document.getElementById('question7').className = 'questionHighlight';
									document.getElementById('question8').className = 'td4';
								}
							}
						}
					}), new Ext.form.Radio({
						boxLabel : '否',
						id : 'stepStatus32',
						name : "za41.step3",
						inputValue : 0,
						height : 20,
						listeners : {
							'check' : function(checkbox, checked){ 
								 if(checked){
									document.getElementById('question3').className = 'questionHighlight';
									document.getElementById('radio3Yes').className = 'td4';
									document.getElementById('radio3No').className = 'optionHighlight';
									document.getElementById('question8').className = 'questionHighlight';
									document.getElementById('question7').className = 'td4';
								}
							}
						}
					}), 
					new Ext.form.Radio({
						boxLabel : '无效',
						name : "za41.step3",
						id : 'stepStatus33',
						inputValue : -1,
						hidden : 'true',
						height : 20,
						listeners : {
			      	      	'check' : function(checkbox, checked){ 
		                         if(checked){
								  	document.getElementById('question3').className = 'td4';
								  	document.getElementById('radio3Yes').className = 'td4';
									document.getElementById('radio3No').className = 'td4';
									document.getElementById('question7').className = 'td4';
									document.getElementById('question8').className = 'td4';
								}
						    }
						}
					})
				]
			}), 
			new Ext.form.TextArea({
				name : 'za41.step3Desc',
				id : 'step3Desc',
				width : '95%',
				height : 50
			}),		
			new Ext.form.RadioGroup({
				width : 120,
				fieldLabel : '(4)&nbsp;', 
				id : 'radioFour',
				name : "radio4",
				items : [ 
					new Ext.form.Radio({
						boxLabel : '是',
						name : "za41.step4",
						id : 'stepStatus41',
						inputValue : 1,
						height : 20,
						listeners : {
							'check' : function(checkbox, checked){ 
								 if(checked){
									document.getElementById('question4').className = 'questionHighlight';
									document.getElementById('question5').className = 'questionHighlight';
									document.getElementById('question6').className = 'questionHighlight';
									document.getElementById('question7').className = 'questionHighlight';
									document.getElementById('radio4Yes').className = 'optionHighlight';
									document.getElementById('radio4No').className = 'td4';
								}
							}
						}
					}), new Ext.form.Radio({
						boxLabel : '否',
						name : "za41.step4",
						id : 'stepStatus42',
						inputValue : 0,
						height : 20,
						listeners : {
							'check' : function(checkbox, checked){ 
								 if(checked){
									document.getElementById('question4').className = 'questionHighlight';
									document.getElementById('question5').className = 'td4';
									document.getElementById('question6').className = 'td4';
									document.getElementById('question7').className = 'questionHighlight';
									document.getElementById('radio4Yes').className = 'td4';
									document.getElementById('radio4No').className = 'optionHighlight';
								}
							}
						}
					}), 
					new Ext.form.Radio({
						boxLabel : '无效',
						name : "za41.step4",
						inputValue : -1,
						id : 'stepStatus43',
						hidden : 'true',
						height : 20,
						listeners : {
			      	      	'check' : function(checkbox, checked){ 
		                         if(checked){
								  	document.getElementById('question4').className = 'td4';
								  	document.getElementById('question7').className = 'td4';
								  	document.getElementById('radio4Yes').className = 'td4';
									document.getElementById('radio4No').className = 'td4';
								}
						    }
						}
					})
				]					
			}), 
			new Ext.form.TextArea({
				name : 'za41.step4Desc',
				id : 'step4Desc',
				width : '95%',
				height : 50
			}),		
			new Ext.form.TextArea({
				fieldLabel : '(5)&nbsp;',
				name : 'za41.step5Desc',
				id : 'step5Desc',
				width : '95%',
				height : 50
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
				title : returnPageTitle('区域分析——增强区域分析', 'za41'),
				layout : 'border',
				region : 'center',
				items : [centerForm, imgForm]
			}
		]
	});

	function onChange(e, obj, v) {
		if (obj.id == 'stepStatus21') {
			var radioThree = Ext.getCmp('radioThree');
			radioThree.eachItem(function(item) {
				item.setValue(item.inputValue == -1);
			});
			radioThree.disable();
			Ext.getCmp('radioFour').enable();
		} else if (obj.id == 'stepStatus22') {
			var radioFour = Ext.getCmp('radioFour');
			radioFour.eachItem(function(item) {
				item.setValue(item.inputValue == -1);
			});
			radioFour.disable();
			Ext.getCmp('radioThree').enable();
		}
	}
	Ext.EventManager.addListener(document.getElementById('stepStatus21'), 'click', onChange);
	Ext.EventManager.addListener(document.getElementById('stepStatus22'), 'click', onChange);

	/**
	 * 加载ZA42分析步骤的数据
	 */
	Ext.Ajax.request({
		url : urlPrefix + "loadZa41.do",
		method : 'POST',
		waitMsg : commonality_waitMsg,
		params : {
			zaId : zaId
		},
		callback : function(options, success, response) {
			var radioOne = Ext.getCmp('radioOne');
			radioOne.eachItem(function(item) {
				item.setValue(item.inputValue == 1);
			});
			radioOne.disable();
			
			var all = Ext.util.JSON.decode(response.responseText);

			if (all.za41 != null){
				Ext.getCmp('za41Id').setValue(all.za41.za41Id);
				Ext.getCmp('step1Desc').setValue(all.za41.step1Desc);
				Ext.getCmp('step2Desc').setValue(all.za41.step2Desc);
				Ext.getCmp('step3Desc').setValue(all.za41.step3Desc);
				Ext.getCmp('step4Desc').setValue(all.za41.step4Desc);
				Ext.getCmp('step5Desc').setValue(all.za41.step5Desc);

				var radioTwo = Ext.getCmp('radioTwo');
				radioTwo.eachItem(function(item) {
					item.setValue(item.inputValue == all.za41.step2);	
				});				

				var radioThree = Ext.getCmp('radioThree');
				radioThree.eachItem(function(item) {
					item.setValue(item.inputValue == all.za41.step3);	
				});

				var radioFour = Ext.getCmp('radioFour');
				radioFour.eachItem(function(item) {
					item.setValue(item.inputValue == all.za41.step4);											
				});

				if (all.za41.step3 == -1){
					radioThree.disable();
				}
				if (all.za41.step4 == -1){
					radioFour.disable();
				}
			}

			dataOld = Ext.util.JSON.encode(centerForm.form.getValues(false));
		}
	});
	
	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			value = 3;
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
		var dataNew = Ext.util.JSON.encode(centerForm.form.getValues(false));
		if (dataNew != dataOld){
			var radioTwoValue;
			var radioThreeValue;
			var radioFourValue;

			Ext.getCmp('radioTwo').eachItem(function (item) {
				radioTwoValue = item.getGroupValue();
			});
			Ext.getCmp('radioThree').eachItem(function (item) {
				radioThreeValue = item.getGroupValue();
			});
			Ext.getCmp('radioFour').eachItem(function (item) {
				radioFourValue = item.getGroupValue();
			});

			if (radioTwoValue == null || radioTwoValue == -1) {
				alert("请选择‘区域中有可燃材料吗？’");
				return false;
			}
			if (radioTwoValue == 1 && (radioFourValue == null || radioFourValue == -1)) {
				alert("请选择‘是否有有效的工作可以明显降低可燃材料堆积的可能性？’");
				return false;
			}
			if (radioTwoValue == 0 && (radioThreeValue == null || radioThreeValue == -1)) {
				alert("请选择‘导线是否同时靠近（距离在2IN/50mm内）飞控系统的主要及备份系统？’");
				return false;
			}
			if (radioTwoValue == 0 && radioThreeValue == 0) {
				alert("当前选择与最初的分析结果不符，请检查");
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

	function saveInfo(nextOrSkip,btnTitl){
		Ext.getCmp('radioOne').enable();
		Ext.getCmp('radioTwo').enable();
		Ext.getCmp('radioThree').enable();
		Ext.getCmp('radioFour').enable();		
		
		centerForm.form.doAction("submit", {
			url : urlPrefix + "saveZa41.do",
			method : "POST",
			waitMsg : commonality_waitMsg,
			params : {
				zaId : zaId
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