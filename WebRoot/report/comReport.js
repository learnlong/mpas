/**
 * 此页面用来显示用户信息 可进行 增 删 改 查操作。
 * 
 * @authour zhouli createDate 2012-08-1
 * 
 * 
 */
Ext.namespace('comReport');
Ext.onReady(function() {
	var urlPrefix = contextPath + "/com/report/";
	var loadComReportListUrl = "loadComReportList.do";
	var updateComReportsUrl = "updateComReports.do";
	var deleteComReportUrl = 'deleteComReportById.do';
	var generateComReportUrl = 'generateComReport.do';
	var rendToDivName = 'reportGrid';

	var currentRowIndex = -1;
	var currentRecord;

	function cellclick(grid, rowIndex, columnIndex, e) {
		if (currentRowIndex != rowIndex) {
			currentRowIndex = rowIndex;
			currentRecord = grid.getStore().getAt(rowIndex);
		}
	}
	
	Ext.form.Field.prototype.msgTarget = 'qtip'; // 用来显示验证信息
	Ext.QuickTips.init();

	// 修改与新增保存
	function updateData(aurl, astore, json) {
		var result = 0;
		if (json.length > 0) {
			var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : commonality_waitMsg,
						removeMask : true
					});
			myMask.show();
			Ext.Ajax.request({
						url : aurl,
						params : {
							jsonData : Ext.util.JSON.encode(json),
							generateId : generateId,
							reportType : reportType,
							isMaintain : isMaintain,
							method : 'update'
						},
						async : false, // 是否为异步请求
						method : "POST",
						waitMsg : commonality_waitMsg,
						success : function(response) {
							myMask.hide();
							if (response.responseText !== null && response.responseText !== '') {
								var result = Ext.util.JSON.decode(response.responseText);
								if (result.success == true) {
									astore.reload();
									astore.modified = [];
								}
							}
						},
						failure : function(response) {
							myMask.hide();
						}
					});
		}
	}

	// shorthand alias
	var fm = Ext.form;
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [{
					header : 'ID',
					dataIndex : 'reportId',
					hidden : true
				}, {
					header : '报告名' + commonality_mustInput,
					dataIndex : 'reportName',
					width : 300,
					editor : new fm.TextField({
								allowBlank : false,
								disabled : !isMaintainFlage,
								maxLength : 100,
								maxLengthText : '输入信息超过100长度!'
							})

				}, {
					header : '版本号' + commonality_mustInput,
					dataIndex : 'versionNo',
					width : 50,
					align : 'center',
					editor : new fm.TextField({
								allowBlank : false,
								disabled : !isMaintainFlage,
								maxLength : 10,
								maxLengthText : '输入信息超过10长度!'
							})
				}, {
					header : '创建人Id',
					dataIndex : 'versionUserId',
					hidden : true
				}, {
					header : '创建人',
					dataIndex : 'versionUserName',
					width : 60,
					align : 'center'
				}, {
					header : '创建时间',
					dataIndex : 'versionDate',
					width : 80,
					align : 'center'
				}, {
					header : '状态Id',
					dataIndex : 'reportStatusCode',
					hidden : true
				}, {
					header : '状态',
					dataIndex : 'reportStatusShow',
					width : 60,
					align : 'center'
				}, {
					header : wordOrExcel + '报告',
					dataIndex : 'reportId',
					width : 100,
					renderer : function(value, cellmeta, record){
						if(value==""){
							return "";
						}else{
							return "<a href='javascript:void(0)' title ='下载" + wordOrExcel + "报告'>"
							  + "<img src='"+ contextPath+ "/images/download.jpg'"
							  +"onclick=\"downloadReport('"+value+"', '" + wordOrExcel + "');\"/></a>";
						}
					}
				}/*, {
					header : 'pdf报告',
					dataIndex : 'reportPdfUrl',
					width : 100,
					hidden : true
				}*/, {
					header : '更改记录',
					dataIndex : 'versionDesc',
					width : 200,
					editor : new fm.TextArea({
						allowBlank : true,
						disabled : !isMaintainFlage,
						grow : true
					})
				}]
	});

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : urlPrefix + loadComReportListUrl
						}),
				reader : new Ext.data.JsonReader({
							root : 'comReport',
							fields : [{
										name : 'reportId',
										type : 'string'
									}, {
										name : 'reportName',
										type : 'string'
									}, {
										name : 'versionNo',
										type : 'string'
									}, {
										name : 'versionUserId',
										type : 'string'
									}, {
										name : 'versionUserName',
										type : 'string'
									}, {
										name : 'versionDate',
										type : 'string'
									}, {
										name : 'reportStatusCode',
										type : 'string'
									}, {
										name : 'reportStatusShow',
										type : 'string'
									}, {
										name : 'reportWordUrl',
										type : 'string'
									}, {
										name : 'reportPdfUrl',
										type : 'string'
									}, {
										name : 'versionDesc',
										type : 'string'
									}]
						})
			});

	store.on("beforeload",function(){
		store.baseParams = {
				generateId : generateId,
				reportType : reportType,
				isMaintain : isMaintain
		};
	});
	
	function getCurDate(){
        var now = new Date();
        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
        var clock = year + "-";
        if(month < 10){
        	clock += "0";
        }
        clock += month + "-";
        if(day < 10){
            clock += "0";
        }
        clock += day + " ";
        return(clock); 
	}
	
	// create the editor grid
	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		cm : cm,
		sm : sm,
		header : false,
		id : 'grid',
		stripeRows : true,
		renderTo : rendToDivName,
		split : true,
		collapsible : true,
		frame : true,
		clicksToEdit : 2,
		bbar : new Ext.PagingToolbar({
					pageSize : 16,
					store : store,
					displayInfo : true,
					displayMsg : commonality_turnPage,
					emptyMsg : commonality_noRecords
				}),
		tbar : new Ext.Toolbar({

			items : [{
						text : commonality_add,
						iconCls : "icon_gif_add",
						disabled : !isMaintainFlage,
						handler : function() {
							var index = store.getCount();
							var rec = grid.getStore().recordType;
							var p = new rec({
											reportId : '',
											reportName : '',
											versionNo : '',
											versionUserId : curUserId,
											versionUserName : curUserName,
											versionDate : getCurDate(),
											reportStatusCode : status_code_new,
											reportStatusShow : status_show_new,
											reportWordUrl : '',
											reportPdfUrl : '',
											versionDesc : ''
									});
							store.insert(index, p);
						}
					},
					'-',
					{
						text : commonality_save,
						iconCls : "icon_gif_save",
						disabled : !isMaintainFlage,
						waitMsg : commonality_waitMsg,
						handler : function() {
							var json = [];
							var temp = 1;
							Ext.each(store.modified, function(item) {
								if (LTrim(item.data.reportName) == '' || LTrim(item.data.reportName) == null) {
									alert('报告名不能为空');
									temp = 2;
									return false;
								}
								if (LTrim(item.data.versionNo) == '' || LTrim(item.data.versionNo) == null) {
									alert('版本号不能为空');
									temp = 2;
									return false;
								}
								json.push(item.data);
							});
							if (temp == 1) {
								if (json <= 0) {
									result = 1;
									alert(commonality_alertUpdate);
									store.load();
									return;
								}
								Ext.Msg.confirm(commonality_affirm,
										commonality_affirmSaveMsg,
										function(btn) {
											if (btn === 'yes') {
												updateData(urlPrefix + updateComReportsUrl, store, json);
											}
										});
							}
							currentRecord = null;
						}
					},
					'-',
					{
						text : commonality_del,
						iconCls : "icon_gif_delete",
						disabled : !isMaintainFlage,
						waitMsg : commonality_waitMsg,
						handler : function() {
							currentRecord = grid.getSelectionModel().getSelected();
							if (currentRecord == null) {
								alert(commonality_alertDel);
								return;
							}
							Ext.MessageBox.show({
										title : commonality_affirm,
										msg : commonality_affirmDelMsg,
										buttons : Ext.Msg.YESNO,
										fn : function(id) {
											if (id == 'cancel') {
												return;
											} else if (id == 'yes') {
												var flag = currentRecord.get('reportStatusCode');// 判断是否是新建状态
												if (status_code_new != flag) {
													alert('只有在新建状态才可以删除！');
													return;
												}
												if (currentRecord.get('reportId') == '') {
													store.remove(currentRecord);
													store.modified.remove(currentRecord);
													return;
												} else {
													doDelete(currentRecord);
													store.modified = [];
												//store.remove(currentRecord);
												}
												store.modified.remove();
											}
										}
									});
						}
					},
					/*'-',
					{
						text : '生成pdf报告',
						iconCls : "icon_gif_update",
						disabled : !isMaintainFlage,
						waitMsg : commonality_waitMsg,
						hidden : true,
						handler : function() {
							currentRecord = grid.getSelectionModel().getSelected();
							if (currentRecord != null) {
								
							} else {
								alert('请选择记录！');
							}
						}
					},*/
					'-',
					{
						text : '生成' + wordOrExcel + '报告',
						iconCls : "icon_gif_update",
						disabled : !isMaintainFlage,
						handler : function() {
							var waitMsgNow = new Ext.LoadMask('windows', {
							    msg : "正在生成报告，请稍后...",
							    removeMask : true// 完成后移除
							});
							currentRecord = grid.getSelectionModel().getSelected();
							if (currentRecord != null) {
								var reportId = currentRecord.get('reportId');
								if(reportId!=null&&reportId!=""){
									waitMsgNow.show();
									Ext.Ajax.request({
										url : urlPrefix + generateComReportUrl,
										params : {
											reportId : reportId,
											generateId : generateId,
											reportType : reportType
										},
										method : "POST",
										success : function(form, action) {
											waitMsgNow.hide();
											var msg = Ext.util.JSON.decode(form.responseText);
											if(msg.success=='yes'){
												alert("创建报告成功");
											}else if(msg.success=="noPermission"){
												alert("分析尚未完成，不能生成报告！！");
											}else{
												alert("报告文件正在使用，请关闭后再生成！！");
											}
										    store.reload();
										},
										failure : function(form, action) {
											waitMsgNow.hide();
											alert(commonality_cautionMsg);
										}
									});
								}else{
									alert("请先保存当前记录");
								}
							} else {
								alert('请选择记录！');
							}
						}
					}]
		})
			// tbar over
	}

	// grid function over
	); // grid over

	// 删除方法
	function doDelete(record) {
		Ext.Ajax.request({
					url : urlPrefix + deleteComReportUrl,
					params : {
						reportId : record.get('reportId')
					},
					method : "POST",
					success : function(form, action) {
					    store.reload();
					},
					failure : function(form, action) {
						// myMask.hide();
						alert(commonality_cautionMsg);
					}
				});

	}

	grid.render();
	grid.addListener("cellclick", cellclick);
	store.load({
				params : {
					start : 0,
					limit : 20,
					generateId : generateId,
					reportType : reportType,
					isMaintain : isMaintain
				}
			});

	var window = new Ext.Window({
				layout : 'fit',
				id:"windows",
				border : false,
				resizable : true,
				closable : false,
				maximized : true,
				plain : true,
				buttonAlign : 'center',
				title : returnPageTitle(showTitle, 'comReport'),
				items : [grid]
			});
	window.show();
});
/* 去前后空格 */
function LTrim(str) {
	// return str.replace(/^\s*/g,"");
	return str.replace(/(^\s*)|(\s*$)/g, "");
}