/**
 * 此页面用来流程审批操作。
 */
Ext.namespace('processCheck');

var urlPrefix = contextPath + "/com/process/";
var rendToDivName = 'processCheckGrid';
var selectProcessId = '';
var selectAnalysisType = '';
Ext.onReady(function() {
	
	Ext.form.Field.prototype.msgTarget = 'qtip';
	Ext.QuickTips.init(); 
	
	var currentRowIndex = -1;
	var currentRecord;

	function cellclick(grid, rowIndex, columnIndex, e) {
		if (currentRowIndex != rowIndex) {
			currentRowIndex = rowIndex;
			currentRecord = grid.getStore().getAt(rowIndex);
		}
	}
	
	//分析系统
	var analysisTypeCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'queryAnalysisType',
				id : 'queryAnalysisType',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [[null, '全部'],
							        [systemCode, systemCodeCn],
									[structureCode, structureCodeCn],
									[zonalCode, zonalCodeCn],
									[lhrifCode, lhrifCodeCn]]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				fieldLabel : zonalCodeCn,
				triggerAction : 'all',
				width : 100,
				emptyText : "请选择",
				editable : false,
				selectOnFocus : true,
				listeners : {
					"select" : function() {
						//comboSelect();
					}
				}
			});

	//分析系统
	var processStatusCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'queryProcessStatus',
				id : 'queryProcessStatus',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [[null, '全部'],
							        [conWaitCheckCode, conWaitCheckName],
									[conCheckingCode, conCheckingName],
									[conFinishCheckCode, conFinishCheckName],
									[conCancelCheckCode, conCancelCheckName]]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				fieldLabel : zonalCodeCn,
				triggerAction : 'all',
				width : 100,
				emptyText : "请选择",
				editable : false,
				selectOnFocus : true,
				listeners : {
					"select" : function() {
						//comboSelect();
					}
				}
			});
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			sortable : true
		},
		columns : [{
					header : "<div align='center'>流程ID</div>",//id
					hidden : true,
					dataIndex : 'processId'
				}, {
					header : "<div align='center'>分析系统</div>",// 四大分析
					width : 100,
					dataIndex : 'analysisType',
					renderer : function(value, cellmeta, record) {// approval_ReportTypeChoose
						var index = analysisTypeCombo.store.find(Ext.getCmp('queryAnalysisType').valueField,value);
						var record = analysisTypeCombo.store.getAt(index);
						var returnvalue = "";
						if (record) {
							returnvalue = record.data.displayText;
						}
						return returnvalue;
					}
				}, {
					header : "<div align='center'>流程状态</div>",//流程状态
					width : 100,
					dataIndex : 'processStatus',
					renderer : function(value, cellmeta, record) {
						var index = processStatusCombo.store.find(Ext.getCmp('queryProcessStatus').valueField,value);
						var record = processStatusCombo.store.getAt(index);
						var returnvalue = "";
						if (record) {
							returnvalue = record.data.displayText;
						}
						return returnvalue;
					}
				}, {
					header : '对象编号', //用逗号分开
					width : 150,
					dataIndex : 'ataorareaCode'
				}, {
					header : "<div align='center'>发起人</div>", //发起人
					width : 100,
					dataIndex : 'launchUser'
				}, {
					header : "<div align='center'>流程发起时间</div>", //流程发起时间
					width : 150,
					dataIndex : 'launchDate'
				}, {
					header : "<div align='center'>审核人</div>", //发起人
					width : 100,
					dataIndex : 'checkUser'
				}, {
					header : "<div align='center'>审核时间</div>", //流程发起时间
					width : 150,
					dataIndex : 'checkDate'
				}, {
					header : "<div align='center'>详细</div>", //审核操作
					width : 60,
					align : 'center',
					dataIndex : 'checkOperate'
				}]
	});
	
	var checkListStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : urlPrefix + 'loadProcessForQuery.do'
						}),
				reader : new Ext.data.JsonReader({
							root : 'comProcess',
							fields : [{
										name : 'processId',
										mapping : 'processId',
										type : 'string'
									}, {
										name : 'analysisType',
										mapping : 'analysisType',
										type : 'string'
									}, {
										name : 'processStatus',
										mapping : 'processStatus',
										type : 'string'
									}, {
										name : 'launchUser',
										mapping : 'launchUser',
										type : 'string'
									}, {
										name : 'launchDate',
										mapping : 'launchDate',
										type : 'string'
									}, {
										name : 'checkUser',
										mapping : 'checkUser',
										type : 'string'
									}, {
										name : 'checkDate',
										mapping : 'checkDate',
										type : 'string'
									}, {
										name : 'checkOperate',
										mapping : 'checkOperate',
										type : 'string'
									}, {
										name : 'ataorareaCode',
										mapping : 'ataorareaCode',
										type : 'string'
									}]
						})
			});

	// create the editor grid
	var grid = new Ext.grid.EditorGridPanel({
		store : checkListStore,
		cm : cm,
		sm : sm,
		loadMask : {
			msg : commonality_waitMsg
		},
		header : false,
		id : 'grid1',
		renderTo : rendToDivName,
		split : true,
		collapsible : true,
		frame : true,
		stripeRows : true,
		clicksToEdit : 1,
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : checkListStore,
					displayInfo : true,
					displayMsg : commonality_turnPage,
					emptyMsg : commonality_noRecords
				}),
		tbar : new Ext.Toolbar({
			items : []
		})
			// tbar over
	}		// grid function over
	); // grid over
 this.tbar1 =new Ext.Toolbar({
 	        renderTo : grid.tbar,
			items : [
					"分析系统：",
					analysisTypeCombo,
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'-',
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'发起人：',
					{
						xtype : 'textfield',
						id : 'launchUserName',
						width : 100
					},
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'-',
					new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
					'审批人：',
					{
						xtype : 'textfield',
						id : 'checkUserName',
						width : 100
					}]
		});
	this.tbar2 = new Ext.Toolbar({
		renderTo : grid.tbar,
		items : [
				"流程状态：",
				processStatusCombo,
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				'-',
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				"开始时间：",
				new Ext.form.DateField({
							name : 'fromDate',
							id : 'fromDate',
							format : 'Y-m-d'
						}),
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				'-',
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				"结束时间：",
				new Ext.form.DateField({
							name : 'toDate',
							id : 'toDate',
							format : 'Y-m-d'
						}),
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				'-',
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				{
					text : commonality_search,
					iconCls : "icon_gif_search",
					id : 'sericon',
					handler : function() {
						var fromDate = Ext.getCmp('fromDate').getRawValue();
						var toDate = Ext.getCmp('toDate').getRawValue();
						var a=/^(\d{4})-(\d{2})-(\d{2})$/; 
						if(fromDate!=''&&fromDate!=null){
							if (!a.test(fromDate)){
	                            Ext.getCmp('fromDate').setRawValue("");
	                            return;
							}
							
						}
						if(toDate!=''&&toDate!=null){
							if (!a.test(toDate)){ 
								Ext.getCmp('toDate').setRawValue("");
	                            return;
							}
						}
						if(fromDate!=''&&fromDate!=null&&toDate!=''&&toDate!=null){
						    if(toDate<fromDate){
							    alert('结束时间不能小于开始时间!');
							    return;
						    }
						}
						checkListStore.load();
					}
				},
				new Ext.Toolbar.TextItem("&nbsp;"),
				{   
					text : commonality_reset,///重置
							xtype : "button",
							iconCls : "icon_gif_reset",
							handler : function() {
								Ext.getCmp('queryAnalysisType').clearValue();
								Ext.getCmp('queryProcessStatus').clearValue();
								Ext.getCmp('launchUserName').reset();
								Ext.getCmp('checkUserName').reset();
								Ext.getCmp('fromDate').reset();
								Ext.getCmp('toDate').reset();
							}
				}
				]
	});
	grid.render();
	grid.addListener("cellclick", cellclick);
	dblclickListgrid = function(grid,row){
		var record = grid.store.getAt(row);
		var processId = record.get("processId");
		var analysisType = record.get("analysisType");
		var processStatus = record.get("processStatus");
		checkSingleOpenWindow(processId, analysisType, processStatus);
	};
	grid.addListener("rowdblclick", dblclickListgrid);
	
	checkListStore.on("beforeload", function() {
				checkListStore.baseParams = {
					start : 0,
					limit : 18,
					analysisType : analysisTypeCombo.getValue(),
					processStatus : processStatusCombo.getValue(),
					launchUserName : Ext.getCmp('launchUserName').getValue(),
					checkUserName : Ext.getCmp('checkUserName').getValue(),
					fromDate : Ext.getCmp('fromDate').getValue(),
					toDate : Ext.getCmp('toDate').getValue()
				};
			});
	checkListStore.load();

	var win = new Ext.Window({
				title : returnPageTitle('审批查询', 'approveSearch'),
				layout : 'border',
				id : 'cnm',
				closable : false,
				resizable : false,
				maximized : true,
				plain : true,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				items : [{
							region : 'center',
							layout : 'fit',
							split : true,
							items : [grid]
						}]

			});
	win.show();
	//------------------处理弹出审核----------------------------------------------------------------------------
	//点审核按钮，操作
	checkSingleOpenWindow = function(processId, analysisType, processStatus){
		//alert(processId + '--' + analysisType + '--' + processStatus);
		//得到流程信息
		selectProcessId = '';
		selectAnalysisType = '';
		Ext.Ajax.request({
			url : urlPrefix + 'getComPrcessInfo.do',
			params : {
				processId : processId
			},
			method : "POST",
			// /waitMsg : commonality_waitMsg,
			success : function(response) {
				var result = Ext.util.JSON.decode(response.responseText);
				Ext.getCmp('launchOpinion').setValue(result.launchOpinion);
				Ext.getCmp('startShenPiUser').setValue(result.startShenPiUser);
				Ext.getCmp('startShenPiData').setValue(result.startShenPiData);
				selectProcessId = result.processId;
				selectAnalysisType = result.analysisType;
			}
		});
		openstore.load({
					params : {
						processId : processId,
						analysisType : analysisType
					}
				});
		openWin.show();
		document.getElementById('ifrm_area').src = "";
	};
	
	//顶部的panel
	launchOpinionPanel = new Ext.FormPanel({
	    labelAlign: 'left',
	    title: '审批发起信息',
	    buttonAlign:'right',
	    bodyStyle:'padding:5px',
	    width: 600,
	    frame:true,
	    labelWidth:80,
	    items: [{
	        layout:'column',   //定义该元素为布局为列布局方式
	        border:false,
	        labelSeparator:'：',
	        items:[{
	            columnWidth:.6,  //该列占用的宽度，标识为50％
	            layout: 'form',
	            border:false,
	            items: [{xtype : "textarea",
					fieldLabel : '发起审批意见',// 意见
					width : 500,
					height : 60,
         			disabled : true,
					name : 'launchOpinion',
					id : 'launchOpinion',
					listeners : {
					}
				}]
	        },{
	            columnWidth:.15,
	            layout: 'form',
	            border:false,
	            items: [{
         			xtype: 'textfield',   
         			fieldLabel: '发起审批人 ',  ///审批发起人 
         			name: 'startShenPiUser'  ,
         			id:'startShenPiUser',
         			disabled : true,
         			width: 60
                	}]
	        },{
	            columnWidth:.25,
	            layout: 'form',
	            border:false,
	            items: [{
                	xtype: 'textfield',   
         			fieldLabel: '发起审批时间',  ///审批发起人 
         			name: 'startShenPiData'  ,
         			id:'startShenPiData',
         			disabled : true,
         			width: 130
                	}]
	        }]
	    }]
	});
	
	//中间grid
		opensm = new Ext.grid.RowSelectionModel({
			singleSelect : false
		});

	opencm = new Ext.grid.ColumnModel([{
				header : '',
				hidden : true,
				dataIndex : 'detailId'
			}, {
				header : '',
				hidden : true,
				dataIndex : 'mainId'
			}, {
				header : "<div align='center'>分析系统</div>",
				width : 150,
				align : 'center',
				dataIndex : 'analysisType',
				renderer : function(value, cellmeta, record) {// approval_ReportTypeChoose
					var index = analysisTypeCombo.store.find(Ext.getCmp('queryAnalysisType').valueField,value);
					var record = analysisTypeCombo.store.getAt(index);
					var returnvalue = "";
					if (record) {
						returnvalue = record.data.displayText;
					}
					return returnvalue;
				}
			}, {
				header : "<div align='center'>对象编号</div>",
				width : 150,
				align : 'center',
				dataIndex : 'mainCode'
			}, {
				header : "<div align='center'>对象名称</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'mainName'
			}, {
				header : "<div align='center'>是否通过</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'isOk'
			}, {
				header : "<div align='center'>审批意见</div>",// 分析名称
				width : 150,
				align : 'center',
				dataIndex : 'checkOpinion'
			}, {
				header : '',
				hidden : true,
				dataIndex : 'urlParam'
			}]);
	opencm.defaultSortable = true;

	openstore = new Ext.data.Store({
				//baseParams: {analysisType: analysisTypeCombo.getValue()},
				proxy : new Ext.data.HttpProxy({
							url : urlPrefix + 'getProcessDetail.do'
						}),
				reader : new Ext.data.JsonReader({
							root : 'comProcessDetail',
							totalProperty : 'total',
							fields : [{
										name : 'detailId',
										mapping : 'detailId',
										type : 'string'
									}, {
										name : 'mainId',
										mapping : 'mainId',
										type : 'string'
									}, {
										name : 'analysisType',
										mapping : 'analysisType',
										type : 'string'
									}, {
										name : 'mainCode',
										mapping : 'mainCode',
										type : 'string'
									}, {
										name : 'mainName',
										mapping : 'mainName',
										type : 'string'
									}, {
										name : 'isOk',
										mapping : 'isOk',
										type : 'string'
									}, {
										name : 'checkOpinion',
										mapping : 'checkOpinion',
										type : 'string'
									}, {
										name : 'urlParam',
										mapping : 'urlParam',
										type : 'string'
									}]
						})
			});
	//openstore.load();
	
	// create the editor grid
	var opengrid = new Ext.grid.GridPanel({
		id : 'opengrid',
		title : '分析信息',
		store:openstore,
		cm : opencm,
		sm : opensm,
		header : false,
		renderTo : rendToDivName,
		split : true,
		collapsible : true,
		frame : true,
		stripeRows : true,
		clicksToEdit : 1
		});
	opengrid.render();
	//opengrid.addListener("cellclick", cellclick);
	dblclickOpengrid = function(grid,row){
		var record = grid.store.getAt(row);
		var analysisType = record.get("analysisType");
		var urlParam = record.get("urlParam");
		//alert(urlParam);
		var openUrl = contextPath;
		if(zonalCode == analysisType){//区域
			openUrl = openUrl + '/area/za1/init.do?isMaintain=0&areaId=' + urlParam ;
		} else if(lhrifCode == analysisType){//
			var parms = urlParam.split(',');
			if(parms[0] == null || parms[0] == '' || parms[0] == 'N/A'){
				openUrl = openUrl + '/lhirf/lh_1/init.do?isMaintain=0&hsiId=' + parms[1] ;
			}else{
				openUrl = openUrl + '/lhirf/lh_1a/init.do?isMaintain=0&hsiId=' + parms[1] ;
			}
		} else if(systemCode == analysisType){
			var parms = urlParam.split(',');
			openUrl = openUrl + '/sys/m0/init.do?isMaintain=0&msiId=' + parms[0] + '&ataId=' + parms[1];
		} else if(structureCode == analysisType){
			var parms = urlParam.split(',');
			if('0' == parms[0]){
				openUrl = openUrl + '/struct/unSsi/init.do?isMaintain=0&ssiId=' + parms[1] ;
			}else if('1' == parms[0]){
				openUrl = openUrl + '/struct/initS1.do?isMaintain=0&ssiId=' + parms[1] ;
			}
		} else{
			openUrl = openUrl + '/analysWelcome.jsp';
		}
		document.getElementById('ifrm_area').src = openUrl;
	};
	opengrid.addListener("rowdblclick", dblclickOpengrid);
	
	//底部的panel
	openBottomPanel = new Ext.Panel({
		region : 'center',
		id : "openBottomPanel",
		autoScroll : true,
		width : '100%',
		height : 300,
		Style : "background-image:url(../images/252-1)", 
		html :'<iframe id="ifrm_area" name="ifrm_area" scrolling="auto" frameborder="0" width="100%" height="100%" src=""></iframe>'
	});

	openWin = new Ext.Window({
        title: '审批详细信息',
        layout: 'border',
        modal : true,
        draggable : true,
        resizable : false,
        width : 1080,
		height : 600,
        plain:true,
        bodyStyle:'padding:0px;',
        buttonAlign:'center',
		closeAction:'hide',
		id:'opencnm',
        items: [{
        		region:'north',
      			layout:'fit',
      			split:true,
      			height:100,
  	      		items:[launchOpinionPanel]},
  	      		{
        		region:'center',
      			layout:'fit',
      			split:true,
      			height:200,
  	      		items:[opengrid]},
  	      		{
  	      		region:'south',
  	      		layout:'fit',
  	      		split:true,
  	      		height:300,
  	      		items:[openBottomPanel]
	      		  }]
   
    });
});