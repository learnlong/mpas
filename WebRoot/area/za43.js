/**
 * 根据主矩阵选中状态变换副矩阵和自定义间隔矩阵的选中状态
 */
function selectLevel(e) {
	var thisNm = e.attributes.name;
	var thisId = e.id;
	var thisFlg = e.attributes.flg;
	var edA = document.getElementById("edAlgorithmTitle").attributes.name;
	var adA = document.getElementById("adAlgorithmTitle").attributes.name;
	var edCount = document.getElementById("edAlgorithmTitle").attributes.colSpan.value;
	var adCount = document.getElementById("adAlgorithmTitle").attributes.colSpan.value;
	var rowSelectFlg = -1;// 副矩阵行选中标志
	var colSelectFlg = -1;// 副矩阵列选中标志
	var cellSelectValue = -1;// 副矩阵单元选中标志
	e.className = "selectCell";
	var levelArr = getElementsByTdName(thisNm.value);
	for (var i = 0; i < levelArr.length; i++) {
		var td = levelArr[i];
		if (td.id != thisId) {
			td.className = "firstMatrix34TdClass";//清空选择的样式//更换样式(原来的)td.className = "";
		}
	}
	var selectArr = Ext.select("td.selectCell");
	var edArr = new Array();
	var adArr = new Array();
	for (var i = 0; i < selectArr.elements.length; i++) {
		var thisElement = selectArr.elements[i];
		if (thisElement.attributes.flg.value == "ED") {
			// levelSort是当前级别值在当前项目下的排序号
			edArr.push(thisElement.attributes.levelSort.value);
		} else if (thisElement.attributes.flg.value == "AD") {
			adArr.push(thisElement.attributes.levelSort.value);
		}
	}
	// 样式为选中的flg为ed的td个数和ed单元格个数相同时表示每一列都有选中项可以开始计算矩阵值
	if (edArr.length.toString() == edCount) {
		// 计算选中值
		var edValue = arrResult(edArr, edA.value);
		// 取选择过的
		var selectMatRowTitleArr = Ext.select("#matrixTable1 .selectRowLevel");
		// 先清空选中状态
		for (var i = 0; i < selectMatRowTitleArr.elements.length; i++) {
			selectMatRowTitleArr.elements[i].className = "rowLevel";
		}
		// 取副矩阵行title
		var matRowTitleArr = Ext.select("#matrixTable1 .rowLevel");
		for (var i = 0; i < matRowTitleArr.elements.length; i++) {
			var thisItem = matRowTitleArr.elements[i];
			// 为了兼容ie
			if (thisItem.innerHTML == "<b>" + edValue + "</b>"
					|| thisItem.innerHTML == "<B>" + edValue + "</B>") {
				thisItem.className = "selectRowLevel";
				// 表示行有选中
				rowSelectFlg = i;
				break;
			}
		}
	}
	// 样式为选中的flg为ad的td个数和ad单元格个数相同时表示每一列都有选中项可以开始计算矩阵值
	if (adArr.length.toString() == adCount) {
		// 计算选中值
		var adValue = arrResult(adArr, adA.value);
		// 取选择过的
		var selectMatColTitleArr = Ext.select("#matrixTable1 .selectCellLevel");
		// 先清空选中状态
		for (var i = 0; i < selectMatColTitleArr.elements.length; i++) {
			selectMatColTitleArr.elements[i].className = "cellLevel";
		}

		// 取副矩阵列title
		var matColTitleArr = Ext.select("#matrixTable1 .cellLevel");
		// var selectMatC
		for (var i = 0; i < matColTitleArr.elements.length; i++) {
			var thisItem = matColTitleArr.elements[i];
			// 为了兼容ie
			if (thisItem.innerHTML == "<b>" + adValue + "</b>"
					|| thisItem.innerHTML == "<B>" + adValue + "</B>") {
				thisItem.className = "selectCellLevel";
				// 表示列有选中
				colSelectFlg = i;
				break;
			}
		}
	}

	// 副矩阵行列都有选中时
	if (rowSelectFlg != -1 && colSelectFlg != -1) {
		// 清除原有选中状态
		var selectCellArr = Ext.select("#matrixTable1 .selectMatrixCell");
		for (var i = 0; i < selectCellArr.elements.length; i++) {
			selectCellArr.elements[i].className = "cell";
		}
		// 选取副矩阵所有内容单元格
		var cellArr = Ext.select("#matrixTable1 .cell");
		for (var i = 0; i < cellArr.elements.length; i++) {
			var rownum = cellArr.elements[i].attributes.rownum;
			var colnum = cellArr.elements[i].attributes.colnum;
			if (rownum.value == (rowSelectFlg + 1).toString()
					&& colnum.value == (colSelectFlg + 1).toString()) {
				cellArr.elements[i].className = "selectMatrixCell";
				cellSelectValue = cellArr.elements[i].innerHTML;
				break;
			}
		}
	}

	// 副矩阵单元格存在选中值时
	if (cellSelectValue != -1) {
		// 清除原有选中状态
		var selectFinalArr = Ext.select("#finalMatrixTable .selectRowLevel");
		for (var i = 0; i < selectCellArr.elements.length; i++) {
			selectFinalArr.elements[i].className = "rowLevel rowLevelEx";
		}
		var finalArr = Ext.select("#finalMatrixTable .rowLevel");
		for (var i = 0; i < finalArr.elements.length; i++) {
			var thisItem = finalArr.elements[i];
			if (cellSelectValue == thisItem.innerHTML) {
				thisItem.className = "selectRowLevel selectRowLevelEx";
				break;
			}
		}
	}
}

/**
 * 查找name相同的td元素（为了支持ie8）	 * 
 * @param {} name
 * @return {}
 */
function getElementsByTdName(name) {
	var returns = new Array();
	var e = document.getElementsByTagName("td");
	for(var i = 0; i < e.length; i++){
		if (e[i].attributes.name != undefined && e[i].attributes.name.value == name) {
			returns[returns.length] = e[i];
		}
	}
	return returns;
}

Ext.namespace('za43');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/za43/";
	var flag = true;//是,没有修改;否,修改过
	var showRecord; //当前显示ED/AD数据的任务
	var dataOld;
	var levelListOld;

	Ext.form.Field.prototype.msgTarget = 'qtip'; 
	Ext.QuickTips.init(); 

	// 列模型
	var colM = new Ext.grid.ColumnModel([
		{
			hidden : true,
			header : "ID",
			dataIndex : 'taskId'
		}, {
			header : '任务编号',
			dataIndex : "taskCode",
			width : 80,
			align : 'center'
		}, {
			header : '任务类型',
			dataIndex : "taskType",
			width : 100,
			align : 'center'
		}
	]);

	var store = new Ext.data.Store({
		url :urlPrefix + "getTaskList.do?zaId="+ zaId,
		reader : new Ext.data.JsonReader({						
			root : "taskMsgList",
			fields : [
				{name : 'taskId'},
				{name : 'taskCode'},
				{name : 'taskType'},
				{name : 'taskDesc'},
				{name : 'reachWay'},
				{name : 'taskInterval'}
			]
		})
	});
	
	// 创建任务面板
	var grid = new Ext.grid.GridPanel({
		cm : colM,
		store : store,
		autoWidth : true,
		autoExpandColumn : 2
	});

	/**
	 * 创建矩阵面板
	 * 可选择的矩阵
	 */
	var matrixPanel = new Ext.Panel({
		region : 'center',
		html : MATRIX_HTML
	});

	/**
	 * 右下方面板,
	 * 显示选中的任务记录
	 */
	var panelCenter = new Ext.form.FormPanel({
		title : "任务(如果下面有两条纪录，最下面挂的任务为RST任务)",
		frame : true,
		items : [{
			layout : 'form',
			items : [
				{//第一行,显示标题
					layout : 'column',
					items : [
						{
							columnWidth : .3,
							layout : 'form',
							items : [{
								xtype : 'label',
								autoWidth : false,
								style : 'font-size:12px',
								text : "接近方式"
							}]
						}, {
							columnWidth: .1, 
							layout:"form", 
							items:[{
								xtype:"label",
								autoWidth : false,
								html :"<div id='approach_taskDes'><font color='#DFE8F6'>&nbsp;.&nbsp;</font></div>"
							}]
						}, {
							columnWidth : .3,
							layout : 'form',
							items : [{
								xtype : 'label',
								autoWidth : false,
								style : 'font-size:12px',
								text : "任务描述"
							}]
						}, {
							columnWidth: .1, 
							layout:"form", 
							items:[{
								xtype:"label",
								autoWidth : false,
								html :"<div id='taskDes_taskInterval'><font color='#DFE8F6'>&nbsp;.&nbsp;</font></div>"
							}]
						}, {
							columnWidth : .2,
							layout : 'form',
							items : [{
								xtype : 'label',
								autoWidth : false,
								style : 'font-size:12px',
								text : "任务间隔"
							}]
						}]
				}, {//第二行,显示选中的任务记录
					layout : 'column',
					items : [
						{
							columnWidth : .3,
							layout : 'form',
							items : [{
								xtype : 'textarea',
								name : 'reachWay',
								id : 'reachWay',
								hideLabel : true,
								width : 160,
								height : 33
							}]
						}, {
							columnWidth: .1, 
							layout:"form", 
							items:[{
								xtype:"label",
								autoWidth : false,
								html :"<div id='panelSeal_taskDesc'><font color='#DFE8F6'>&nbsp;.&nbsp;</font></div>"
							}]
						},  {
							columnWidth : .3,
							layout : 'form',
							items : [{
								xtype : 'textarea',
								name : 'taskDesc',
								id : 'taskDesc',
								hideLabel : true,
								width : 160,
								height : 33
							}]
						},{
							columnWidth: .1, 
							layout:"form", 
							items:[{
								xtype:"label",
								autoWidth : false,
								html :"<div id='taskDesc_taskInterval'><font color='#DFE8F6'>&nbsp;.&nbsp;</font></div>"
							}]
						},  {
							columnWidth : .2,
							layout : 'form',
							items : [{
								xtype : 'textfield',
								name : 'taskInterval',
								id : 'taskInterval',
								width : 80,
								maxLength : 200,
								hideLabel : true
							}]
						}]
				}, {//显示ZA43中,问题7默认产生的GVI任务记录时,会显示ZA42中默认产生的RST任务记录
					layout : 'column',
					items : [
						{
							columnWidth : .3,
							layout : 'form',
							items : [{
								xtype : 'textarea',
								name : 'rstReachWay',
								id : 'rstReachWay',
								hideLabel : true,
								width : 160,
								height : 33
							}]
						}, {
							columnWidth: .1, 
							layout : "form", 
							items:[{
								xtype:"label",
								autoWidth : false,
								html :"<div id='rstPanelSeal_rstTaskDesc'><font color='#DFE8F6'>&nbsp;.&nbsp;</font></div>"
							}]
						}, {
							columnWidth : .3,
							layout : 'form',
							items : [{
								xtype : 'textarea',
								name : 'rstTaskDesc',
								id : 'rstTaskDesc',
								hideLabel : true,
								width : 160,
								height : 33
							}]
						}, {
							columnWidth: .1, 
							layout : "form", 
							items:[{
								xtype:"label",
								autoWidth : false,
								html :"<div id='rstTaskDesc_rstTaskInterval'><font color='#DFE8F6'>&nbsp;.&nbsp;</font></div>"
							}]
						}, {
							columnWidth : .2,
							layout : 'form',
							items : [{
								xtype : 'textfield',
								name : 'rstTaskInterval',
								id : 'rstTaskInterval',
								width : 80,
								maxLength : 200,										
								hideLabel : true
							}]
						}, {
							xtype : 'textfield',
							id : 'rstTaskId',
							hidden : true
						}]
				}
			]
		}]
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
				region : 'center',
				layout : 'border',
				title : returnPageTitle('增强区域分析','za43'),
				items : [
					{
						region : 'west',
						layout : 'fit',
						width : 200,
						items : [grid]//任务记录列表
					}, {
						region : 'center',
						layout : 'border',
						autoScroll : true,
						items : [
							{
								region : 'center',
								layout : 'fit',
								items : [matrixPanel]//矩阵面板
							}, {
								region : 'south',
								layout : 'fit',
								height : 125,
								items : [panelCenter]//显示具体任务记录
							}
						]
					}
				]
			}
		]
	});

	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			value = 5;
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

	function saveData(value){
		if (showRecord){//已经显示过数据			
			if (comparisonRecord()){
				Ext.MessageBox.show({
					title : commonality_affirm,
					msg : commonality_affirmSaveMsg,
					buttons : Ext.Msg.YESNO,
					fn : function(id) {
						if (id == 'yes') {								
							shiftTaskRowAndSave(showRecord, 'goNext');// 保存数据								
						} else if (id == 'no') {
							goNext(value);
						}
					}
				})
			} else {
				goNext(value);
			}					
		} else {//没有显示过数据
			goNext(value);
		}
	}

	//保存后，判断是否需要变更导航栏的状态
	store.on("load", function() {
		if (showRecord) {
			grid.getSelectionModel().selectRow(store.find('taskId', showRecord.get('taskId')));
		}
		
		var count = 0;
		store.each(function(record) {
			if((record.get('taskCode') == null) || (record.get('taskCode') == "")){
				count += 1;
			}
		});

		if(count == 0){
			step[4] = 1;
			document.getElementById("step4").className = "x-rc-form-table-td-sys-complete";

			if (step[5] == 0){
				step[5] = 2;
				document.getElementById("step5").className = "x-rc-form-table-td-sys-maintain";
			}
			if (step[6] == 0){
				step[6] = 2;
				document.getElementById("step6").className = "x-rc-form-table-td-sys-maintain";
			}
		}
		
		
	});
	store.load();

	grid.on('cellmousedown', function(grid, rowIndex, columnIndex, e) {
		var record = grid.getSelectionModel().getSelected();

		if (showRecord){//已经显示过数据
			if (analysisFlag) {//只有有分析权限的用户，才需要验证数据，否则直接显示
				if (comparisonRecord()){
					Ext.MessageBox.show({
						title : commonality_affirm,
						msg : commonality_affirmSaveMsg,
						buttons : Ext.Msg.YESNO,
						fn : function(id) {
							if (id == 'yes') {								
								var flg = shiftTaskRowAndSave(record, 'noGoNext');// 保存数据
								showRecord = record;
								if (flg != -1) {
									store.load();
									shiftTaskRow(record);
								}							
								return;
							} else if (id == 'no') {
								shiftTaskRow(record);//显示选中任务记录信息
								showRecord = record;
								return;
							}
						}
					})
				} else {
					shiftTaskRow(record);//显示选中任务记录信息
					showRecord = record;
					return;
				}			
			} else {
				shiftTaskRow(record);//显示选中任务记录信息
				showRecord = record;
				return;
			}			
		} else {//没有显示过数据
			shiftTaskRow(record);//显示选中任务记录信息
			showRecord = record;
			return;
		}
	})

	/**
	 * 触发的事件
	 * 选中一条任务时触发的事件,查询任务信息;
	 * @param all 待切换的任务信息
	 */
	function shiftTaskRow(record) {
		// 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true// 完成后移除
		});
		waitMsg.show();// 显示等待消息
		
		Ext.Ajax.request({
			url : urlPrefix + "getZa43MatrixResults.do?taskId=" + record.get('taskId') + "&zaId=" + zaId,
			method : 'POST',
			success : function(response, action) {
				waitMsg.hide();
				var all = Ext.util.JSON.decode(response.responseText);
				loadTaskDetail(all);//填充右下方面板,被选中的任务信息
				levelListOld = all.levelList;//记录画面上显示的矩阵信息,数据来自数据库
				setMatrixState(all.levelList);	//矩阵信息
			},
			failure : function(response, action) {
				waitMsg.hide();
				alert(commonality_messageDelMsgFails);// 提示查询失败
			}
		});
	}

	/**
	 * 载入任务详细信息
	 */
	function loadTaskDetail(all){
		Ext.getCmp("taskDesc").setValue(all.taskDesc);
		Ext.getCmp("reachWay").setValue(all.reachWay);
		Ext.getCmp("taskInterval").setValue(all.taskInterval);
		
		// rst任务存在时
	    if (all.rstTaskId != undefined && all.rstTaskId != "") {
			Ext.getCmp("rstTaskDesc").setVisible(true);
			Ext.getCmp("rstReachWay").setVisible(true);
		    Ext.getCmp("rstTaskInterval").setVisible(true);
				
			Ext.getCmp("rstTaskId").setValue(all.rstTaskId);
			Ext.getCmp("rstTaskDesc").setValue(all.rstTaskDesc);
			Ext.getCmp("rstReachWay").setValue(all.rstReachWay);
			Ext.getCmp("rstTaskInterval").setValue(all.rstTaskInterval);
		}else{
			Ext.getCmp("rstTaskId").setValue(null);
			Ext.getCmp("rstTaskDesc").setValue(null);
			Ext.getCmp("rstReachWay").setValue(null);
			Ext.getCmp("rstTaskInterval").setValue(null);

			Ext.getCmp("rstTaskDesc").setVisible(false);
			Ext.getCmp("rstReachWay").setVisible(false);
		    Ext.getCmp("rstTaskInterval").setVisible(false);
		}

		dataOld = Ext.util.JSON.encode(panelCenter.form.getValues(false));
	}

	/**
	 * 设置矩阵选中状态
	 * @param levelList 矩阵选中值数组
	 */
	function setMatrixState(levelList){
		if (levelList == undefined || levelList == null){
			var matrixArr = Ext.select("#mainMatrix .selectCell");
			var matrix1ArrRow =  Ext.select("#matrixTable1 .selectRowLevel");
			var matrix1ArrCell = Ext.select("#matrixTable1 .selectCellLevel");
			var matrix1Arr = Ext.select("#matrixTable1 .selectMatrixCell");
			var finalArr = Ext.select("#finalMatrixTable .selectRowLevel");
			for (var i = 0; i < matrixArr.elements.length; i++){
				matrixArr.elements[i].className = "firstMatrix34TdClass";
			}
			for (var i = 0; i < matrix1ArrRow.elements.length; i++){
				matrix1ArrRow.elements[i].className = "rowLevel";
			}
			for (var i = 0; i < matrix1ArrCell.elements.length; i++){
				matrix1ArrCell.elements[i].className = "cellLevel";
			}
			for (var i = 0; i < matrix1Arr.elements.length; i++){
				matrix1Arr.elements[i].className = "cell";
			}
			for (var i = 0; i < finalArr.elements.length; i++){
				finalArr.elements[i].className = "rowLevel";
			}
			return;
		}
		
		var matrixArr = new Array();
		// 取主矩阵中的所有行
		var trArr = Ext.select("#mainMatrix tr");
		for (var j = 1; j < trArr.elements[2].cells.length; j++) {
			var colArr = new Array();
			matrixArr.push(colArr);
		}
		
		// 按主矩阵级别部分的每一列做个二维数组
		for (var i = 2; i < trArr.elements.length; i++) {
			// 从第二列开始遍历
			for (var j = 1; j < trArr.elements[i].cells.length; j++) {
			    var cell = trArr.elements[i].cells[j];
			    var id = cell.id;
			    var levelValue = cell.attributes.levelValue.value;
				matrixArr[j - 1].push(id + '-' + levelValue);
			}
		}
		
		// 上次的选中状态存在的情况下，根据上次选中的状态更新矩阵的选中状态
		if (levelList.length > 0){
			for(var i = 0; i < levelList.length; i++){
				var tempArr = matrixArr[i];
                for(var j = 0; j < tempArr.length; j++){
                	var idAndValue = tempArr[j].split("-");
                	var id = idAndValue[0];
                	var value = idAndValue[1];
                	// 该单元格的状态是选中时
                	if (value == levelList[i]){
                		var thisTd = document.getElementById(id);
                		selectLevel(thisTd);
                	}
                }
			}
		}	
	}

	/**
	 * 验证当前从画面上抓取的当前任务记录的信息是否与数据库的一样
	 */
	function comparisonRecord(){
		var flagMatrix = comparisonMatrix();//对矩阵的验证,TRUE表示矩阵未修改,FALSE表示修改了
		var flagRecord = true;
		var dataNew = Ext.util.JSON.encode(panelCenter.form.getValues(false));

		if (dataNew != dataOld){
			flagRecord = false;
		} else {
			flagRecord = true;
		}
		
		if (flagMatrix && flagRecord){
			return false;
		} else {
			return true;
		}
	}

	function comparisonMatrix(){
		var flag = true;
		/**
		 * 获取当前页面主矩阵选中的值,用selectValueNew标识;
		 * oldLevelList标识为从数据库中取得的矩阵信息;
		 * 相同表示同一条记录的矩阵信息未改变;反之改变;
		 */
		var selectValueNew = "";		
		var selectArrNew = Ext.select("#mainMatrix tr");	//取得矩阵的行标签	
		var rowNew = selectArrNew.elements[2];// 第三行是矩阵数据行，前两行都是标题,取得单个的表格数据
		
		// 遍历矩阵数据行中的列，从第二列开始遍历，第一列是标题
		for(var i = 1; i < rowNew.cells.length; i++){
			var selectConditionNew = "#mainMatrix .selectCell[name=" + rowNew.cells[i].attributes.name.value + "]";
			var selectItemNew = Ext.select(selectConditionNew);
			if (selectItemNew.elements.length == 0){
				continue;
			}
			if (i == rowNew.cells.length - 1){
				selectValueNew += selectItemNew.elements[0].attributes.levelValue.value;
			}else{
				selectValueNew += selectItemNew.elements[0].attributes.levelValue.value + ",";
			}
		}

		if((levelListOld != "") && (levelListOld != undefined)){
			if(selectValueNew != levelListOld){//初次判断,不等于
				flag = false;//说明selectValueNew和oldLevelList是不相等的
			}		
		}else if(selectValueNew != ""){
			flag = false;//说明levelList是空,首次保存矩阵
		}
		return flag;
	}

	/**
	 * 触发的事件(2)
	 * 取得表单的数据
	 * 保存修改过的任务记录
	 */
	function shiftTaskRowAndSave(record, type) {	
		var selectArr = Ext.select("#finalMatrixTable .selectRowLevel");
        var finalResult = "";
        
        if (selectArr.elements.length == 0){//矩阵为空
			alert("矩阵选择未完成!,任务信息将不会被保存!");// 提示查询失败
			return -1;
        }else{
        	finalResult = selectArr.elements[0].innerHTML;
        }
		//GVI和DET任务记录
        var taskId = showRecord.get('taskId');// 待保存的任务id
        var taskDesc = Ext.getCmp('taskDesc').getValue();
		var reachWay = Ext.getCmp('reachWay').getValue();
        var taskInterval = Ext.getCmp('taskInterval').getValue();
        //RST任务记录
        var rstTaskId = Ext.getCmp('rstTaskId').getValue();
        var rstTaskDesc = Ext.getCmp('rstTaskDesc').getValue();
        var rstReachWay = Ext.getCmp('rstReachWay').getValue();
        var rstTaskInterval = Ext.getCmp('rstTaskInterval').getValue();
        
        // 任务间隔不能为空
		if (taskInterval == "") {
			alert("任务间隔不能为空!");
			return -1;
		}
		
		// rst任务存在时
		if (Ext.getCmp('rstTaskInterval').hidden == false && rstTaskInterval == ""){
			alert("RST任务间隔不能为空!");
			return -1;
		}
		
	    // 等待消息
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true// 完成后移除
		});
		waitMsg.show();// 显示等待消息

		var selectValue = "";		
		var selectArr = Ext.select("#mainMatrix tr");//取得矩阵的行标签
		var row = selectArr.elements[2];// 第三行是矩阵数据行，前两行都是标题,取得单个的表格数据
		
		// 遍历矩阵数据行中的列，从第二列开始遍历，第一列是标题
		for(var i = 1; i < row.cells.length; i++){
			var selectCondition = "#mainMatrix .selectCell[name=" + row.cells[i].attributes.name.value + "]";
			var selectItem = Ext.select(selectCondition);
			if (i == row.cells.length - 1){
				selectValue += selectItem.elements[0].attributes.levelValue.value;
			}else{
				selectValue += selectItem.elements[0].attributes.levelValue.value + ",";//这里在分割值时,最后多加了一个","
			}
		}
		
		Ext.Ajax.request({
			url : contextPath + "/area/za43/saveZa43.do",
			params : {
				areaId : areaId,
				zaId : zaId,
				taskId : taskId,						
				taskDesc : taskDesc,
				reachWay : reachWay,
				taskInterval : taskInterval,
				rstTaskId : rstTaskId,						
				rstTaskDesc : rstTaskDesc,
				rstReachWay : rstReachWay,
				rstTaskInterval : rstTaskInterval,						
				za43Select : selectValue,
				finalResult : finalResult
			},
			async : false,
			method : 'POST',
			success : function(response, action) {
				waitMsg.hide();			
				parent.refreshTreeNode();
				alert('保存成功');				
				var all = Ext.util.JSON.decode(response.responseText);
				if (type == 'goNext') {
					successNext(all.nextStep);
				}
				return all.nextStep;
			},
			failure : function(response, action) {
				waitMsg.hide();
				alert(commonality_messageDelMsgFails);// 提示查询失败
				return -1;
			}
		});
	}

	if(window.screen.width == 800){
		document.getElementById("approach_taskDes").className = "approach_taskDes800";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval800";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc800";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval800";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc800";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval800";
	}else if(window.screen.width == 1024){
		document.getElementById("approach_taskDes").className = "approach_taskDes1024";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval1024";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc1024";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval1024";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc1024";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval1024";
	}else if(window.screen.width == 1152){
		document.getElementById("approach_taskDes").className = "approach_taskDes1152";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval1152";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc1152";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval1152";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc1152";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval1152";
	}else if(window.screen.width == 1280){
		document.getElementById("approach_taskDes").className = "approach_taskDes1280";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval1280";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc1280";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval1280";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc1280";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval1280";
	}else if(window.screen.width == 1360){
		document.getElementById("approach_taskDes").className = "approach_taskDes1360";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval1360";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc1360";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval1360";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc1360";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval1360";
	}else if(window.screen.width == 1366){
		document.getElementById("approach_taskDes").className = "approach_taskDes1366";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval1366";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc1366";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval1366";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc1366";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval1366";
	}else if(window.screen.width == 1400){
		document.getElementById("approach_taskDes").className = "approach_taskDes1400";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval1400";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc1400";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval1400";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc1400";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval1400";
	}else if(window.screen.width == 1440){
		document.getElementById("approach_taskDes").className = "approach_taskDes1440";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval1440";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc1440";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval1440";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc1440";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval1440";
	}else{
		document.getElementById("approach_taskDes").className = "approach_taskDes1024";
		document.getElementById("taskDes_taskInterval").className = "taskDes_taskInterval1024";
		document.getElementById("panelSeal_taskDesc").className = "panelSeal_taskDesc1024";
		document.getElementById("taskDesc_taskInterval").className = "taskDesc_taskInterval1024";
		document.getElementById("rstPanelSeal_rstTaskDesc").className = "rstPanelSeal_rstTaskDesc1024";
		document.getElementById("rstTaskDesc_rstTaskInterval").className = "rstTaskDesc_rstTaskInterval1024";	
	}
});