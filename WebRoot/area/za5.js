Ext.namespace('za5');
Ext.form.Field.prototype.msgTarget = 'qtip'; 
Ext.QuickTips.init(); 
za5.app = function() {
	var ptitle = '区域分析—标准区域分析';
	if (nowStep == 'ZA5A') {
		ptitle = ptitle + '(内部)';
	} else {
		ptitle = ptitle + '(外部)';
	}
			
	var firstForm = new Ext.form.FormPanel({
		labelWidth : 80, 
		labelAlign : "right",
		frame : true,
		autoScroll : true,
		html : ZA5_FIRST_CUSTOM_MATRIX
	});

	var secondForm = new Ext.form.FormPanel({
		labelWidth : 80, 
		labelAlign : "right",
		frame : true,
		autoScroll : true,
		html : ZA5_SECOND_CUSTOM_MATRIX
	});

	var thirdForm = new Ext.form.FormPanel({
		labelWidth : 80, 
		labelAlign : "right",
		frame : true,
		autoScroll : true,
		html : ZA5_THIRD_CUSTOM_MATRIX
	});

	var forthForm = new Ext.form.FormPanel({
		labelWidth : 80, 
		labelAlign : "right",
		frame : true,
		autoScroll : true,
		html : ZA5_LAST_CUSTOM_MATRIX
	});

	var table = '<div id="myDiv" style="overflow:auto">' +
			'<table width="100%" border="0" cellpadding="0" cellspacing="0">' +
			'<tr>' +
				'<td align ="center" width=8%>接近方式&nbsp;&nbsp;</td>'+
				'<td align="left" width=27%> ' +
				'<textarea name="reachWay" id="reachWay" rows="3"></textarea></td>' +
				'<td align ="center" width=10%>任务描述<font color="red">&nbsp;*&nbsp;</font></td>'+
				'<td align="left" width=30%><textarea name="taskDesc" id="taskDesc" rows="3"></textarea></td>' +
				'<td align ="center" width=10%>任务间隔<font color="red">&nbsp;*&nbsp;</font></td>'+
				'<td align="right" width=25%><textarea name="taskTime" id="taskTime"></textarea></td>' +
			'</tr>' +
			'</table>' +
			'</div>';
	
	
	/**
	 * "接近方式"、"任务描述"和"任务间隔"表格
	 */
	var bottonForm = new Ext.form.FormPanel({
		labelWidth : 80, 
		labelAlign : "right",
		id : 'bottonForm',
		frame : true,
		html : table
	});
	var za5BodyWidth = document.body.clientWidth;//当前body 容器的宽度
	var za5BodyHeight = document.body.clientHeight ;//当前body 容器的高度

	var headerStepForm = new Ext.form.Label({
		applyTo : 'headerStepDiv'
	});

	var win = new Ext.Viewport({
		region : 'center',
		layout : 'border',
		border : false,
		plain : true,
		items : [
			{
				region : 'north',
				height : 60,
				frame : true,
				items : [headerStepForm]
			}, {
				region : 'center',
				layout : 'border',
				border : false,
				items : [
					{//矩阵
						region : 'center',
						layout : 'border',
						border : false,
						title : returnPageTitle(ptitle, 'za5'),
						height : 450,
						items : [
							{//右
								region : 'west',
								layout : 'fit',
								border : false,
								autoScroll : true,
								width :za5BodyWidth * 0.5,
								height : 450,
								split : true,
								items : [firstForm]
							}, {//左
								region : 'center',
								layout : 'border',
								border : false,
								split : true,
								height : 450,
								width : za5BodyWidth * 0.5,
								items : [
									{
										region : 'south',
										layout : 'fit',
										border : false,
										height : 110, 
										width : za5BodyWidth * 0.5,
										split : true,
										autoScroll : true,
										items : [forthForm]
									}, {
										region : 'center',
										layout : 'border',
										split : true,
										border : false,
										items : [
											{
												region : 'west',
												layout : 'fit',
												border : false,
												split : true,
												width : za5BodyWidth * 0.25,
												items : [secondForm]
											}, {
												region : 'center',
												layout : 'fit',
												split : true,
												border : false,
												width : za5BodyWidth * 0.25,
												items : [thirdForm]
											}
										]
									}
								]
							}
						]
					}
				]
			}, {// "接近方式"、"任务描述"和"任务间隔"表格
				region : 'south',
				layout : 'fit',
				height : 70,
				items : [bottonForm]
			}
		]
	});
		


	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			if (nowStep == 'ZA5A') {
				value = 6;
			} else {
				value = 7;
			}
			
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

	// 测试选中的级别是否完整
	function saveData(value) {
		if (document.getElementById("taskTime").value == null || document.getElementById("taskTime").value == ''
				|| document.getElementById("taskDesc").value == null || document.getElementById("taskDesc").value == '') {
			alert('录入的数据不完整，无法进行下一步！');//提示数据不完整,无法进行下一步
			return;
		}

		var matrixIsEmpty = false;//表示矩阵是否为空或是不完整,TRUE为为空或是不完整
		var matrixEmptyCount = 0;//统计矩阵框为空的个数,0为空

		/**
		 * 验证矩阵内容
		 */
		for (var i = 1; i < lastItemCount + 1; i++) { // 遍历项目
			for (var j = 1; j < levelCount + 1; j++) { // 遍历级别
				if (document.getElementById("level_" + j + "td_" + i).style.backgroundColor == '#00ff99'
						|| document.getElementById("level_" + j + "td_" + i).style.backgroundColor == 'rgb(0, 255, 153)') {
					checkLevelNew[i - 1] = j;//保存从矩阵中取得的值
				}
			}
		}

		/**
		 * 验证矩阵是否为空
		 * 当checkLevelNew长度动态变化,为确保checkLevelNew每一个元素都有值,
		 * 遍历检查
		 */
		if(checkLevelNew.length > 0){//checkLevelNew长度动态变化,检查是否每一个元素都有值
			for(var i = 0; i < lastItemCount; i++){
				if(checkLevelNew[i] == undefined){//元素为空,表示矩阵值不完整
					matrixIsEmpty = true;//元素为空,表示矩阵值不完整
				}else{
					matrixEmptyCount += 1;//元素不为空
				}
			}
			/**
			 * 特殊情况:
			 * 当checkLevelNew数组只在前面半部分有值时,
			 * 再次检查遍历
			 */
			if((matrixEmptyCount != 0) && (matrixEmptyCount != lastItemCount)){
				matrixIsEmpty = true;//元素为空,表示矩阵值不完整
			}
		}else{
			matrixIsEmpty = true;//表示矩阵值为空
		}

		if (taskDesc.replace(/\n/g, '') != document.getElementById('taskDesc').value.replace(/\r\n/g, '')
				|| reachWay.replace(/\n/g, '') != document.getElementById('reachWay').value.replace(/\r\n/g, '')
				|| taskInterval != document.getElementById('taskTime').value
				|| levelValueArr != checkLevelNew.toString()) {			
			Ext.MessageBox.show({
				title : commonality_affirm,
				msg : commonality_affirmSaveMsg,
				buttons : Ext.MessageBox.YESNOCANCEL,
				fn : function(btn){
					if(btn == 'yes'){
						saveZa5(value);
					}else if(btn == 'no'){
						goNext(value);								
					}else{
						return;
					}
				}
			});
		} else { // 数据没有发生变化直接进入下一步
			goNext(value);
		}			
	}

	// 保存的方法
	function saveZa5(nextOrSkip,btnTitl){
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true// 完成后移除
		});		
	   	waitMsg.show();	

		// 点击“是”按钮执行保存数据的操作
		Ext.Ajax.request({
			url : "${contextPath}/area/za5/saveZa5.do?step=" + nowStep,
			waitMsg: commonality_waitMsg,
			method : "POST",
			params : {
				zaId : zaId,
				checkLevelArr : checkLevelNew,
				reachWay : document.getElementById('reachWay').value, // 接近方式document.getElementById("taskTime").value
				taskDesc : document.getElementById('taskDesc').value, // 任务描述
				taskInterval : document.getElementById('taskTime').value, //任务间隔
				level_1 : LEVEL_1, //中间等级1
				level_2 : LEVEL_2, //中间等级2
				level_3 : LEVEL_3 //中间等级3
			},
			success : function(response, action) {
				waitMsg.hide();				
				alert('保存成功');				
				parent.refreshTreeNode();
				var all = Ext.util.JSON.decode(response.responseText);				
				successNext(all.nextStep);
			},
			failure : function(response, action) {
				alert(commonality_saveMsg_fail);
				waitMsg.hide();
			}
		});
	}
	document.getElementById('reachWay').value=reachWay; // 接近方式document.getElementById("taskTime").value
	document.getElementById('taskDesc').value=taskDesc; // 任务描述
	document.getElementById('taskTime').value=taskInterval; //任务间隔
};