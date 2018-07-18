Ext.namespace('za8');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/area/za8/";

	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init();

	var store = new Ext.data.Store({
		url : urlPrefix +  "getZa8List.do?zaId=" + zaId,
		reader : new Ext.data.JsonReader({
			 root : "tasks",
			 fields : [
				{name : "taskId"},
				{name : 'taskCode'},
				{name : 'taskType'},
				{name : 'reachWay'},
				{name : 'taskDesc'},
				{name : 'sumTaskDesc'},
				{name : 'taskInterval'},
				{name : 'mrbId'},
				{name : 'effectiveness'}
			]
		})
	});
	store.load();

	var colM = new Ext.grid.ColumnModel([
		{
			header : 'ID',
			dataIndex : "taskId",
			hidden : true
		}, {
			header : 'MSG-3任务号', 
			dataIndex : "taskCode", 
			width : 120,
            align : 'center'
		}, {
			header : '接近方式',
			dataIndex : "reachWay", 
			width : 200,
            align : 'center',
			renderer: changeBR
		}, {
			header : '任务描述',
			dataIndex : "taskDesc", 
			width : 200,
            align : 'center',
			renderer: changeBR,
			editor : new Ext.form.TextArea({
         		maxLength : 4000,
				grow : true,
				maxLengthText : '输入信息长度超过4000个字符!'
         	})
		}, {
			header : '合并的转区域任务号', 
			dataIndex : "mrbId", 
			width : 200,
            align : 'center'
		}, {
			header : '合并的转区域任务描述',
			dataIndex : "sumTaskDesc", 
			width : 200,
            align : 'center',
			renderer: changeBR
		}, {
			header : '任务间隔', 
			dataIndex : "taskInterval", 
			width : 120,
            align : 'center'
		}, {
			header : '适用性' + commonality_mustInput, 
			dataIndex : "effectiveness", 
			width : 150,
            align : 'center',
			renderer: changeBR,
			editor : new Ext.form.TextArea({
         		maxLength : 4000,
				grow : true,
				maxLengthText : '输入信息长度超过4000个字符!'
         	})
		}
	]);

	var grid = new Ext.grid.EditorGridPanel({
		cm : colM,
		sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
		store : store,
		stripeRows : true,
		clicksToEdit : 2,
		title : returnPageTitle('进入区域检查大纲的任务', 'za8'),
		header : true,
		autoWidth : true
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
				layout : 'fit',
				region : 'center',
				items : [grid]
			}
		]
	});
		
	// 点击下一步执行的方法
	nextOrSave = function(value) {
		if (value == null) {
			value = 10;
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
		var json = [];
		var modified = store.modified;
		Ext.each(modified, function(item) {
			json.push(item.data);
		});

		if (json.length > 0) {
			Ext.MessageBox.show({
				title : commonality_affirm,
				msg : commonality_affirmSaveMsg,
				buttons : Ext.Msg.YESNO,
				fn : function(id){
					if (id == 'yes'){
						updateDataDetail(json, value);
					}
				}
			});
		} else {
			goNext(value);
		}
	}

	// 保存具体操作
	function updateDataDetail(json, value) {
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true// 完成后移除
		});		
		waitMsg.show();
		
		Ext.Ajax.request( {
			url : urlPrefix + "saveZa8.do",
			params : {
				jsonData : Ext.util.JSON.encode(json)
			},
			method : "POST",
			success : function(response, action) {
				waitMsg.hide();
				parent.refreshTreeNode();
				alert(commonality_messageSaveMsg);
				successNext(value);
			},
			failure : function(response, action) {
				waitMsg.hide();
				alert(commonality_cautionMsg);
			}
		});
	}
});