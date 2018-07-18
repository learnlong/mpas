Ext.namespace('za2');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/za2/";
	var positionNow = 0;
	var oldPicContent = "";
	var newPicContent = "";

	Ext.form.Field.prototype.msgTarget = 'qtip'; 
	Ext.QuickTips.init(); 

	/**
	 * "部位"一栏的样式;
	 */
	var topForm = new Ext.form.FormPanel({
		bodyStyle : "align:left",
		id : 'topForm',
		resizable : true,
		autoScroll : false,
		frame : true,
		items : [{
			layout : 'column',
			items : [
				{
					columnWidth : .3,
					layout : 'form',
					items : [{
						xtype : 'label',
						html : '部位（内/外）<b><font color="red">&nbsp;*&nbsp;</font><b>&nbsp;:&nbsp;&nbsp;&nbsp;'
					}]
				}, {
					columnWidth : .2,
					layout : 'form',
					items : [ 
						new Ext.form.CheckboxGroup({
							name : 'posGroup',
							width : 120,
							hideLabel : true,
							vertical : false,
							items : [{
								boxLabel : '内部',
								inputValue : 'inner',
								id : 'inner',
								name : 'inner',
								checked : false,
								labelAlign : 'left'
							}, {
								boxLabel : '外部',
								inputValue : 'outer',
								id : 'outer',
								name : 'outer',
								checked : false,
								labelAlign : 'left'
							}]
						})
					]
				}
			]
		}]
	});

	/**
	 * 图文混排插件;
	 * 绘制图文混排样式;
	 * 包含已经实现的插件,直接使用;
	 */
	var editgg_form = new Ext.form.FormPanel({
		region : 'center',
		layout : 'fit', 
		frame : true,  
		items : [{  
			xtype : 'textarea',  
			id : 'picContent', 
			width : 'auto',
			height : 'auto'
		}],  
		listeners:{  
			'render' : function(){ 
				keId = 'picContent';
				KE.app.init({
					renderTo : keId,
					delayTime : 1,
					resizeType : 1,
					width : '100%',
					uploadJson : contextPath + '/baseData/upload/uploadImg.do'
				});
			}             
		}
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
				title : returnPageTitle('区域分析插图', 'za2'),
				layout : 'border',
				region : 'center',
				items : [
					{
						region : 'north',
						height : 35,
						items : [topForm]
					}, 
					{
						region : 'center',
						layout : 'fit',
						items : [editgg_form]
					}
				]
			}
		]
	});

	/**
	 * 显示"部位"复选框组件的值
	 */
	function showData(){
		var innerBox = Ext.getCmp('inner');
		var outerBox = Ext.getCmp('outer');
		if (innerPos == 1){
			innerBox.setValue(1);
		}
		if (outerPos == 1){
			outerBox.setValue(1);
		}
	}
	showData();

	//页面加载结束后读取数据
	Ext.Ajax.request({
		url : urlPrefix + 'loadZa2PicContent.do',
		method : "POST",
		async:false,
		params:{
			za2Id : za2Id
		},
		success : function(response) {
			var text = Ext.decode(response.responseText);
			Ext.getCmp("picContent").setValue(text.picContent);
			oldPicContent = text.picContent;
			if (oldPicContent == null){
				oldPicContent = "";
			}
		}
	});

	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			value = 2;
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
	 * 进行页面数据的验证
	 */
	function comparisonData(){
		var innerBox = Ext.getCmp('inner');//内部复选框
		var outerBox = Ext.getCmp('outer');//外部复选框
		innerValue = 0;//存储内部复选框值的临时变量
		outerValue = 0;//存储外部复选框值的临时变量
		if (innerBox.checked) {
			innerValue = 1;
		}
		if (outerBox.checked) {
			outerValue = 2;
		}
		positionNow = innerValue + outerValue;//部位复选框的值,由内外合并

		//图文混排框(中文)
		if (KE.app.getEditor("picContent")){
			newPicContent = KE.app.getEditor("picContent").html();
		} else {
			newPicContent = Ext.getCmp("picContent").value;
		}
		if(newPicContent == null){
			newPicContent = "";
		}
		
		//页面数据没有修改
		if (positionNow == position && newPicContent.replace(/\s+/gi,"") == oldPicContent.replace(/\s+/gi,"")){
			return 1;//返回1,表示页面数据没有改动
		} else {//页面数据有修改			
			if(positionNow==0){
				alert("请选择内部或者外部");
				return false;
			}
			return -1;//返回-1,表示页面数据有改动
		}		
	}

	/**
	 * 进行跳转操作
	 * 分为"自由跳转"和"下一步"2种跳转操作
	 */
	function saveData(value) {
		var comparisonResult = comparisonData();			
		if (comparisonResult == 1){//画面无修改,直接跳转下一分析步骤
			goNext(value);
		} else if (comparisonResult == -1){//画面修改,检查修改的数据是否正确
			Ext.MessageBox.show({
				title : commonality_affirm,
				msg : commonality_affirmSaveMsg,
				buttons : Ext.MessageBox.YESNOCANCEL,
				fn : function(btn){
					if (btn == 'yes'){
						saveInfo();
					} else if (btn == 'no'){
						goNext();
					} else {
					 	return;
					}
				}
			});			
		}
	}

	/**
	 * 验证成功后,进行保存操作,与后台进行交换
	 */
	function saveInfo(){		
		topForm.form.doAction("submit", {
			url : urlPrefix + 'save.do',
			method : 'POST',
			waitMsg : commonality_waitMsg,
			async : false,
			params : {
				zaId : zaId,
				za2Id : za2Id,
				position : positionNow,
				picContent : newPicContent
			},
			success : function(form, action) {
				if (action.result.success) {
					alert(commonality_messageSaveMsg);
					parent.refreshTreeNode();
					successNext(action.result.nextStep);
				}
			},
			failure : function(form, action) {
				alert(commonality_saveMsg_fail);
			}
		});
	}
});