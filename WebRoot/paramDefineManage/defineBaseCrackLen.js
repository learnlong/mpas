/**
 * author: hanjinlun;
 * create time: 2014-11-11;
 * use: 绘制"自定义基本裂纹长度"页面并实现其功能;
 */
// 创建命名空间
Ext.namespace('cusBaseCrackLen');

// 创建应用程序
cusBaseCrackLen.app = function() {	
	return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
			
			/**
			 * 用于初始化"自定义评级内容列表"的数据
			 */
			var storeGradeList = new Ext.data.Store({
				url : cusBaseCrackLen.app.loadGradeListUrl,
				reader : new Ext.data.JsonReader({
					 root : "gradeList",
					 fields : [
					           	{name : 'displayId'},
							 	{name : 'gradeName'},
							 	{name : 'grade'},
							 	{name : 'gradeContent'}						
							  ]
				})
			});
			storeGradeList.load();	
			
			/**
			 * 用于绘制"自定义评级内容列表"的表样式和字段
			 */
			var colLeft = new Ext.grid.ColumnModel([{
		    	header :"显示信息ID", 
		    	dataIndex : 'displayId', 
		    	hidden : true
	    	   },
      			{header : "评级名称", 
  				 dataIndex : "gradeName",
  				 width : 120 
      	    	},{header : "评级",
      			   dataIndex : "grade",
      			   width : 80
      	    	},{header : "评级内容",
      			   dataIndex : "gradeContent",
      			   width : 400,
      			   renderer:changeBR,
      			   editor : new Ext.form.TextArea({
      				   	allowBlank : false,
						grow : true     				         				   	      				     				   	 						 
		    	   })
      	    	}
      		]);	
			
			/**
			 * 用于绘制"自定义评级内容列表"的表头样式和保存按钮
			 */
			var westGrid=new Ext.grid.EditorGridPanel({
				sm:new Ext.grid.RowSelectionModel({singleSelect:true}),
				cm:colLeft,
				split : false,
				clicksToEdit : 2, 
				border : false, 
				store:storeGradeList,
				region:'west',
				height:605,				
				tbar:["自定义评级内容列表", "->", 
			          new Ext.Button({
						              text:"保存",
						              iconCls:"icon_gif_save",
						              handler:saveGradeListAffirm
			         })
				]
			});
			
			westGrid.addListener("beforeedit", beforeedit);
			function beforeedit(val){
				var field = val.field;
				if (field =='gradeName' || field =='grade') {
					val.cancel = true;		//通过设置事件为取消状态，来达到不可点击的目的
					return;
				}
			}
			
			/**
			 * 用于绘制"自定义矩阵图"的表样式和字段等功能,
			 * 以及表头样式和保存按钮
			 */
			var eastMatrix = new Ext.Panel({
				id:'panelEast',
				border : false, 
				tbar:["自定义矩阵图", "->", 
					new Ext.Button({
						id : "matrixSaveBtn",
						text : "保存",
						iconCls : "icon_gif_save",
					    handler:saveMatrixAffirm
					})
				],
				height:605,	
				html:'<div class="matrixDiv">' +
					'<table id="matrixTable" class="matrixTable" border="2" bordercolordark="#6498EE" bordercolorlight="#FFFFFF" cellpadding="0" cellspacing="0">' +
		   			'<tr><td rowspan="2" colspan="2" class="grade" bgcolor="#E2F3F2" style="font-size:15px;">'+'评级'+'4</td>' +
		   			'<td colspan="4" bgcolor="#E2F3F2"><input type="text" id="gradeCol" name="gradeCol" class="gradeCol" disabled="true" readOnly="true"/></td></tr>' +//条件等级,值取自数据库
		   			'<tr><td bgcolor="#E2F3F2">1</td><td bgcolor="#E2F3F2">2</td><td bgcolor="#E2F3F2">3</td><td bgcolor="#E2F3F2">4</td></tr>' +
		   			'<tr><td rowspan="5" bgcolor="#E2F3F2" align="left"><input type="text" id="gradeRow" class="gradeRow" name="gradeRow" disabled="true" readOnly="true"/></td>' +//实用性等级,值取自数据库
		   			'<td bgcolor="#E2F3F2">&nbsp;&nbsp;1&nbsp;&nbsp;</td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt1" name="cp_gradeNumTxt1" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt2" name="cp_gradeNumTxt2" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt3" name="cp_gradeNumTxt3" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt4" name="cp_gradeNumTxt4" class="inputTxt" maxlength="4"/></td></tr>' +
		   			'<tr><td bgcolor="#E2F3F2">&nbsp;&nbsp;2&nbsp;&nbsp;</td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt5" name="cp_gradeNumTxt5" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt6" name="cp_gradeNumTxt6" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt7" name="cp_gradeNumTxt7" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt8" name="cp_gradeNumTxt8" class="inputTxt" maxlength="4"/></td></tr>' +
		   			'<tr><td bgcolor="#E2F3F2">&nbsp;&nbsp;3&nbsp;&nbsp;</td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt9" name="cp_gradeNumTxt9" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt10" name="cp_gradeNumTxt10" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt11" name="cp_gradeNumTxt11" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt12" name="cp_gradeNumTxt12" class="inputTxt" maxlength="4"/></td></tr>' +
		   			'<tr><td bgcolor="#E2F3F2">&nbsp;&nbsp;4&nbsp;&nbsp;</td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt13" name="cp_gradeNumTxt13" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt14" name="cp_gradeNumTxt14" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt15" name="cp_gradeNumTxt15" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt16" name="cp_gradeNumTxt16" class="inputTxt" maxlength="4"/></td></tr>' +
		   			'<tr><td bgcolor="#E2F3F2">&nbsp;&nbsp;5&nbsp;&nbsp;</td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt17" name="cp_gradeNumTxt17" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt18" name="cp_gradeNumTxt18" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt19" name="cp_gradeNumTxt19" class="inputTxt" maxlength="4"/></td>' +
		   			'<td bgcolor="#FFFFF"><input type="text" id="gradeNumTxt20" name="cp_gradeNumTxt20" class="inputTxt" maxlength="4"/></td></tr></table></div>'
			}); 

			/**
			 * 用于合并左部分的"自定义评级内容列表"和右部分的"自定义矩阵图"
			 */
			var win = new Ext.Window({ 
				layout : 'fit', 
				renderTo : Ext.getBody(), // 呈现在 Html Body标签中
				plain : false, 
				bodyStyle : 'padding:0px;', 
				title : returnPageTitle("定义基本裂纹长度",'cusBaseCrackLen'), 
				border : false, 
				resizable : true, 
				closable : false, 
				maximized : true, 
				items:[{xtype:"panel", 
						layout:"column", 
						items:[{ 
								columnWidth: .6, 
								layout:'fit',
								items: [westGrid] 
							  },{ 
								columnWidth: .4, 
								layout:'fit',
								items: [eastMatrix] 
						}] 				
				}] 
			}); 
			win.show();	
			/**
			 * 定义一个全局数组变量,
			 * 用于保存数据库中的数据源
			 * 保存从数据库传到前台的矩阵数据
			 * 保存前台修改过的矩阵数据
			 */
			var dataValues = new Array();
			var newDataValues = new Array();
			
			/**
			 * 初始化"自定义矩阵图"的数据
			 */
			Ext.Ajax.request( {
				url : cusBaseCrackLen.app.loadMatrixUrl,
				method : "POST",
				callback: function(options, success, response){
					var all = Ext.util.JSON.decode(response.responseText);
					if((all.matrixData == null) || (all.matrixData == "")){
						//alert("没有取得指定机型系列ID数据,矩阵图为空");
					}else{
						
							document.getElementById('gradeRow').className = "gradeRow gradeRowCn";//给"条件等级"单元格添加中文样式
							document.getElementById('gradeCol').className = "gradeCol gradeColCn";//给"实用性等级"单元格添加中文样式
							var gradeRowValue = all.matrixData[1].matrixRowName;//条件等级,值取自数据库
							var gradeColValue = all.matrixData[1].matrixColName;//实用性等级,值取自数据库
						
						
						document.getElementById('gradeRow').value = gradeRowValue;//给"条件等级"单元格赋值
						document.getElementById('gradeCol').value = gradeColValue;//给"实用性等级"单元格赋值
						for(var i=0; i < 20; i++){
							var row = all.matrixData[i].matrixRow;
							var col = all.matrixData[i].matrixCol;
							var elementId = (row - 1)*4 + col;
							document.getElementById('gradeNumTxt' + elementId).value = all.matrixData[i].matrixValue;
						}
						
						for(var coui=1 ;coui<21;coui++){
							dataValues.push(document.getElementById("gradeNumTxt"+coui).value);
						}
						
						//判断矩阵是否可分析,0为不可分析
						if(all.isAnalyseMatrix == 0){
							for(var i=1; i < 21; i++){
								document.getElementById('gradeNumTxt' + i).readOnly = true;
							}
							document.getElementById("matrixSaveBtn").disabled = true;
						}
					}
				}
			});
			
			/**
			 * 确认
			 * "自定义评级内容列表"保存事件是否执行,
			 * @returns boolean
			 */
			function saveGradeListAffirm(){
				Ext.MessageBox.show({
				    title : "确认",
				    msg: "确认要保存吗？",
				    buttons: Ext.Msg.YESNO,
				    fn: function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){
							if (!gradeListAffirm()){
								return false;
							}
							saveGradeListData();
							storeGradeList.modified = [];
						}
				    }
				});		
			}

			/**
			 * 验证
			 * "自定义评级内容列表"提交的数据信息是否合理,
			 * @returns boolean
			 */
			function gradeListAffirm(){	
				var temp = 0;
				Ext.each(storeGradeList.modified, function(item) {
					if(item.data.gradeContent == ''){
						temp = 1;
						alert("没有需要更新的数据!");
					}
				});
				return (temp == 0)?true:false;
			}	
			
			/**
			 * 保存
			 * "自定义评级内容列表"提交的合理数据信息
			 */
			function saveGradeListData(){
				var json = [];				
				Ext.each(storeGradeList.modified, function(item) {
					json.push(item.data);
				});
				if (json.length > 0){
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : "正在向服务器提交数据...",
				    removeMask : true// 完成后移除
					});	
					waitMsg.show();
					Ext.Ajax.request( {
						url : cusBaseCrackLen.app.saveGradeListUrl,
						params : {
							jsonData : Ext.util.JSON.encode(json)
						},
						method : "POST",
						success : function(form,action) {
							storeGradeList.load();
							waitMsg.hide();
							alert("保存成功！" );
						},
						failure : function(form,action) {
							alert("数据更新失败，请稍后再试！");
						}
					});
				} else {
					alert("没有需要保存的数据 !");
					return;
				}
			}
			
			/**
			 * 确认
			 * "自定义评级内容列表"保存事件是否执行,
			 * @returns boolean
			 */
			function saveMatrixAffirm(){
				if (!matrixAffirm()){
					return;
				}else{
					Ext.MessageBox.show({
					    title : "确认",
					    msg : "确认要保存吗？",
					    buttons : Ext.Msg.YESNO,
					    fn : function(id){
					    	if (id == 'cancel'){
								return;
							} else if (id == 'yes'){
								saveMatrix();
							}
					    }
					});			
				}
			}
			
			/**
			 * 验证
			 * "自定义矩阵图"提交的数据信息是否合理,
			 * @returns boolean
			 */
			function matrixAffirm(){
				newDataValues = new Array();
				for(var coui=1 ;coui<21;coui++){
					newDataValues.push(document.getElementById("gradeNumTxt"+coui).value.trim());
				}
				if(newDataValues.length == 0){
					alert("没有需要更新的数据!");
					return false;
				}else{
					var countNewOld = 0;//临时统计
					for(var i=0; i<20; i++){
						//检查是否与原来的数据相同
						if(newDataValues[i] == dataValues[i]){
							countNewOld += 1;//相同加1
						}
					}
					if(countNewOld == 20){//完全相同时提示没有新数据需要更新
						alert("没有需要更新的数据!");
						return false;
					}else{
						for(var i=0; i<20; i++){
							if(newDataValues[i] == ""){
								alert("请完善矩阵!");
								return false;
							}
							if(isUnsignedInteger(newDataValues[i]) == false){
								newDataValues[i] = "";
								var row = parseInt((i+1)/4)+1;
								var col = (i+1)%4;
								if((i+1)%4 == 0){
									row -= 1;
									col = 4;
								}
								alert("第"+row+"行第"+col+"列,输入数值不合理 !");
								return false;
							}else if(isTooBig(newDataValues[i])){
								newDataValues[i] = "";
								var row = parseInt((i+1)/4)+1;
								var col = (i+1)%4;
								if((i+1)%4 == 0){
									row -= 1;
									col = 4;
								}
								alert("第"+row+"行第"+col+"应小于999.");
								return false;
							}
						}
						return true;
					}
				}
			}
			
			//检查是否为正整数 
			function isUnsignedInteger(strInteger)   { 
				var newPar=/^\d+$/;
				return newPar.test(strInteger); 
			}
			
			//检查值是否大于999 
			function isTooBig(strInteger)   { 
				return (strInteger > 999)?true:false;//true表示值大于999
			} 
			
			/**
			 * 保存
			 * "自定义矩阵图"提交的合理数据信息
			 */
			function saveMatrix(){
				var jsonValue = [];
				var jsonID = [];
				
				for(var i=0; i < 20; i++){
					if(newDataValues[i] != dataValues[i]){
						jsonValue.push(newDataValues[i]);
						jsonID.push((i+1));
					}
				}
				if (jsonValue.length > 0){
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : "正在向服务器提交数据...",
				    removeMask : true// 完成后移除
					});		
					waitMsg.show();
					Ext.Ajax.request( {
						url : cusBaseCrackLen.app.saveMatrixUrl,
						params : {
							values : jsonValue,
							id : jsonID
						},
						method : "POST",
						success : function(form,action) {
							window.location.href = contextPath + '/paramDefineManage/defineBaseCrackLen.jsp';
							waitMsg.hide();
							alert("保存成功！");
						},
						failure : function(form,action) {
							alert("数据更新失败，请稍后再试！");
						}
					});
				} else {
					alert("没有需要保存的数据 !");
					return;
				}		
			}
		}
	};
}();