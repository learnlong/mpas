/**
 * 此页面用来显示用户信息 可进行 增 删 改 查操作。
 * 
 * @authour zhouli createDate 2012-08-1
 * 
 * 
 */
Ext.namespace('newIndex');
var selectProcessId = '';
var selectAnalysisType = '';
var rendToDivName = 'openCheckGrid';
Ext.onReady(function() {
	
	Ext.form.Field.prototype.msgTarget = 'qtip'; // 用来显示验证信息
	Ext.QuickTips.init();

	//----------------分析开始--------------------------------------------------------------------------------------
	doAnalysisOpenWindow = function(analysisType, urlParam){
		//alert(urlParam);
		openAnalysisWin.show();
		var openUrl = contextPath;
		if(zonalCode == analysisType){//区域
			openUrl = openUrl + '/area/za1/init.do?isMaintain=1&areaId=' + urlParam ;
		} else if(lhrifCode == analysisType){//
			var parms = urlParam.split(',');
			if(parms[0] == null || parms[0] == '' || parms[0] == 'N/A'){
				openUrl = openUrl + '/lhirf/lh_1/init.do?isMaintain=1&hsiId=' + parms[1] ;
			}else{
				openUrl = openUrl + '/lhirf/lh_1a/init.do?isMaintain=1&hsiId=' + parms[1] ;
			}
		} else if(systemCode == analysisType){
			var parms = urlParam.split(',');
			openUrl = openUrl + '/sys/m0/init.do?isMaintain=1&msiId=' + parms[0] + '&ataId=' + parms[1];
		} else if(structureCode == analysisType){
			var parms = urlParam.split(',');
			if('0' == parms[0]){
				openUrl = openUrl + '/struct/unSsi/init.do?isMaintain=1&ssiId=' + parms[1] ;
			}else if('1' == parms[0]){
				openUrl = openUrl + '/struct/initS1.do?isMaintain=1&ssiId=' + parms[1] ;
			}
		} else{
			openUrl = openUrl + '/analysWelcome.jsp';
		}
		document.getElementById('ifrm_analysis').src = openUrl;
	};

	//底部的panel
	openAnalysisPanel = new Ext.Panel({
		region : 'center',
		id : "openAnalysisPanel",
		autoScroll : true,
		width : '100%',
		height : '100%',
		Style : "background-image:url(../images/252-1)", 
		html :'<iframe id="ifrm_analysis" name="ifrm_analysis" scrolling="auto"  frameborder="0" width="100%" height="100%" src=""></iframe>'
	});

	openAnalysisWin = new Ext.Window({
        title: '审批',
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
		id:'openAnalysiscnm',
        items: [{
        		region:'center',
      			layout:'fit',
      			split:true,
      			height:'100%',
  	      		items:[openAnalysisPanel]}]
   
    });
	
	openAnalysisWin.on('hide',function(){
		//checkstore.reload();
		//alert(isOkCombo.store.getCount());
		document.getElementById('ifrm_analysis').src = '';
		if(typeof document.frames['waitTask_ifrm'] !== 'undefined' && typeof document.frames['waitTask_ifrm'].reFlashAnalysisGrid === 'function'){
			document.frames['waitTask_ifrm'].reFlashAnalysisGrid(); //Call the function available in the iframe window from the parent window.
		}
	});
	//----------------分析结束--------------------------------------------------------------------------------------
	
	//----------------审核开始--------------------------------------------------------------------------------------
	//---审核弹出开始---------------------------------------------
	//点审核按钮，操作
	checkSingleOpenWindow = function(processId, analysisType, processStatus){
		//alert(processId + '--' + analysisType + '--' + processStatus);
		//alert(isOkCombo.store.getCount());
		//alert((Ext.get('grid1').dom.outerHTML).substring(1000));
		//得到流程信息
		selectProcessId = '';
		selectAnalysisType = '';
		opensm.clearSelections();
		opengrid.show();
		Ext.getCmp('isOkType').clearValue();
		Ext.getCmp('isOkCheckpinion').setValue("");
		if(conWaitCheckCode == processStatus || conCheckingCode == processStatus){
			Ext.getCmp('isOkType').setDisabled(false);
			Ext.getCmp('isOkCheckpinion').setDisabled(false);
			Ext.getCmp('singleCheckDetailBtn').setDisabled(false);
			Ext.getCmp('allCheckDetailBtn').setDisabled(false);
		} else {
			Ext.getCmp('isOkType').setDisabled(true);
			Ext.getCmp('isOkCheckpinion').setDisabled(true);
			Ext.getCmp('singleCheckDetailBtn').setDisabled(true);
			Ext.getCmp('allCheckDetailBtn').setDisabled(true);
		}
		Ext.Ajax.request({
			url : contextPath + '/com/process/getComPrcessInfo.do',
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
	
	Ext.MessageBox.getDialog().getEl().setStyle('z-index', '9013');
	
	//是否通过
	 isOkCombo = new Ext.form.ComboBox({
				xtype : 'combo',
				name : 'isOkType',
				id : 'isOkType',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [[null, '请选择'],
							        [conIsOkYesCode, conIsOkYesName],
									[conIsOkNoCode, conIsOkNoName],
									[conIsOkCancelCode, conIsOkCancelName]]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				typeAhead : true,
				mode : 'local',
				fieldLabel : conIsOkYesName,
				triggerAction : 'all',
				width : 100,
				emptyText : "请选择",
				editable : false,
				selectOnFocus : true,
				listeners : {
					"select" : function() {
						//comboSelect();
						//alert(isOkCombo.store.getCount());
					}
				}
			});
	
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
	opensm = new Ext.grid.CheckboxSelectionModel({
				//handleMouseDown:Ext.emptyFn,
				listeners : {
					beforerowselect : function(SelectionModel, rowIndex, keepExisting, record) {
//						var status = rstatus = record.get("ProofUserNameCn");
//						if (status = record.get("ProofUserNameCn") == '' || record.get("ProofUserNameEn") == '') {
//							alert(startProcess_msg);
//							return false;
//						}
					}
				}
			});

	function getAnalysisNameBycode(analysisCode){
		if(analysisCode == null || analysisCode == ""){
			return "";
		}else if(zonalCode == analysisCode){
			return zonalCodeCn;
		}else if(lhrifCode == analysisCode){
			return lhrifCodeCn;
		}else if(structureCode == analysisCode){
			return structureCodeCn;
		}else if(systemCode == analysisCode){
			return systemCodeCn;
		}else{
			return "";
		}
	}
	
	opencm = new Ext.grid.ColumnModel([opensm, {
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
				renderer : function(value, cellmeta, record) {
					return getAnalysisNameBycode(value);
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
							url : contextPath + '/com/process/getProcessDetail.do'
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
	var opengrid = new Ext.grid.EditorGridPanel({
		store : openstore,
		cm : opencm,
		sm : opensm,
		header : false,
		id : 'opengrid',
		renderTo : Ext.getBody(),
//		autoRender :false,
		split : true,
		collapsible : true,
		frame : true,
		stripeRows : true,
		clicksToEdit : 2,
		hidden : true,
		tbar : new Ext.Toolbar({
					items : ['是否通过'+ commonality_mustInput+'：',
							 isOkCombo,
							 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							 '审批意见：',
							 {xtype : "textarea",
								width : 250,
								height : 40,
								name : 'isOkCheckpinion',
								id : 'isOkCheckpinion'
							 },
							 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							 {
									text : '选中审核',
									iconCls : "icon_gif_save",
									id : "singleCheckDetailBtn",
									handler: function() {
										var sel = opensm.getSelections();// 多选框取值问题....!
										var choosedIds = '';
										if (sel != '' && sel != null) {
											Ext.each(sel, function(s) {
												choosedIds = choosedIds + s.get('detailId') + ',';
											});
											if (choosedIds == "" || choosedIds == null) {
												alert('请选择要审核的数据 !');
												return;
											}else{
												choosedIds = choosedIds.substring(0, choosedIds.length -1);
											}
											if (Ext.getCmp('isOkType').getValue() == "" || Ext.getCmp('isOkType').getValue() == null) {
												alert('是否通过不能为空!');
												return;
											}
											Ext.Msg.confirm(commonality_affirm,
													'确认审核选中分析吗？',
													function(btn) {
														if (btn == 'yes') {
															var waitMsg = new Ext.LoadMask(
																	Ext.getCmp('opencnm').body, {
																		msg : commonality_waitMsg,
																		// 完成后移除
																		removeMask : true
																	});
															waitMsg.show();
															Ext.Ajax.request({
																url : contextPath + '/com/process/singleCheckProcessDetail.do',
																params : {
																	isOkType : Ext.getCmp('isOkType').getValue(),
																	isOkCheckpinion : Ext.getCmp('isOkCheckpinion').getValue(),
																	processId :selectProcessId,
																	analysisType : selectAnalysisType,
																	choosedIds : choosedIds
																},
																method : "POST",
																// /waitMsg : commonality_waitMsg,
																success : function(response) {
																	waitMsg.hide();
																	var result = Ext.util.JSON.decode(response.responseText);

																	if (result.success == 'true' || result.success === true) {
																		alert('审核成功');
																		opensm.clearSelections();
																		openstore.reload();
																		Ext.getCmp('isOkType').clearValue();
																		Ext.getCmp('isOkCheckpinion').setValue("");
																	} else {
																		alert(commonality_cautionMsg);
																	}
																},
																failure : function(response) {
																	waitMsg.hide();
																	alert(commonality_cautionMsg);
																}
															});
													}
											});
										} else {
											alert('请选择要审核的数据 !');
										}
									}
								},
							 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
							 {
									text : '全部审核',
									iconCls : "icon_gif_save",
									id : "allCheckDetailBtn",
									handler: function() {
										if (Ext.getCmp('isOkType').getValue() == "" || Ext.getCmp('isOkType').getValue() == null) {
											alert('是否通过不能为空!');
											return;
										}
										Ext.Msg.confirm(commonality_affirm,
												'确认审核全部分析吗？',
												function(btn) {
													if (btn == 'yes') {
														var waitMsg = new Ext.LoadMask(
																Ext.getCmp('opencnm').body, {
																	msg : commonality_waitMsg,
																	// 完成后移除
																	removeMask : true
																});
														waitMsg.show();
														Ext.Ajax.request({
															url : contextPath + '/com/process/allCheckProcessDetail.do',
															params : {
																isOkType : Ext.getCmp('isOkType').getValue(),
																isOkCheckpinion : Ext.getCmp('isOkCheckpinion').getValue(),
																processId :selectProcessId,
																analysisType : selectAnalysisType
															},
															method : "POST",
															// /waitMsg : commonality_waitMsg,
															success : function(response) {
																waitMsg.hide();
																var result = Ext.util.JSON.decode(response.responseText);

																if (result.success == 'true' || result.success === true) {
																	alert('审核成功');
																	opensm.clearSelections();
																	openstore.reload();
																	Ext.getCmp('isOkType').clearValue();
																	Ext.getCmp('isOkCheckpinion').setValue("");
																} else {
																	alert(commonality_cautionMsg);
																}
															},
															failure : function(response) {
																waitMsg.hide();
																alert(commonality_cautionMsg);
															}
														});
												}
										});
									
									}
								}]
					})
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
        title: '审批',
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
	openWin.on('hide',function(){
		//checkstore.reload();
		//alert(isOkCombo.store.getCount());
		if(typeof document.frames['waitTask_ifrm'] !== 'undefined' && typeof document.frames['waitTask_ifrm'].reFlashCheckGrid === 'function'){
			document.frames['waitTask_ifrm'].reFlashCheckGrid(); //Call the function available in the iframe window from the parent window.
		}
	});
	//---审核弹出结束---------------------------------------------
	//----------------审核结束--------------------------------------------------------------------------------------
	refreshTreeNode = function(){
		
	};
});
