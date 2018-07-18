// 创建命名空间
 	
Ext.namespace('lhSelect');
   lhSelect.app=function(){ 
           return{
  		init:function(){
			Ext.form.Field.prototype.msgTarget = 'qtip';
            Ext.QuickTips.init();
            var currentRowIndex = -1;
			var currentRecord;  

			// combox 选择
			var threeChooseCombo = new Ext.form.ComboBox({   
				xtype : 'combo',
				name : 'threeChoose', id : 'threeChoose',
				store : new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
					data : [['IPV','IPV'],['OPVP','OPVP'],['OPVE','OPVE']]
				}),  
				valueField : "retrunValue", displayField : "displayText",
				typeAhead : true, mode : 'local',
				triggerAction : 'all', width : 80,
				editable : false, selectOnFocus : true
			});

			   // 任务状态 combox
			var statusChooseCombo = new Ext.form.ComboBox({   
				xtype : 'combo',
				name : 'statusChoose', id : 'statusChoose',
				store : new Ext.data.SimpleStore({
				fields : ["retrunValue", "displayText"],
					data : [[statusNew,statusNewShow],[statusMaintain,statusMaintainShow],
					        [statusMinOK,statusMinOKShow],[statusApproved,statusApprovedShow]]
				}),  
				valueField : "retrunValue", displayField : "displayText",
				typeAhead : true, mode : 'local',
				triggerAction : 'all', width : 80,
				editable : false, selectOnFocus : true
			});
			
			// 数据源 store
				var store = new Ext.data.Store({
				url : lhSelect.app.g_lhSelectListUrl,
				reader : new Ext.data.JsonReader({
					 root : "lhSelect",
					 fields : [
					 	{name : 'hsiId'},
					 	{name : 'ataCode'},
					 	{name : 'hsiCode'},
					 	{name : 'hsiName'},
					 	{name : 'lhCompName'},
					 	{name : 'ipvOpvpOpve'},
					 	{name : 'refHsiCode'},
					 	{name : 'anaUserName'},
					 	{name : 'status'},
					 	{name :'isAuthorHsi'}
					 	
				 	]
				})
			});
			// **********************
			
			var maxLength = 500;
			// 数据列coM
				var colM = new Ext.grid.ColumnModel([
			    {
			    	header : "LhMain ID", 
			    	dataIndex : "hsiId", 
			    	hidden : true
		      },{
					header : "<div align='center'>"+'HSI 编号' + commonality_mustInput +"</div>",
					dataIndex : "hsiCode",
					width : 81,
			    	editor : new Ext.form.TextField({
			    		  	maxLength : 12,
			    		  	maxLengthText : "编号长度不能超过12位",
			    			allowBlank : false
			    	})
		    	},{
					header : "<div align='center'>"+'HSI 名称' + commonality_mustInput +"</div>",
					dataIndex : "hsiName",    // hsi 名称
					width : 150,
					editor :new Ext.form.TextArea({
						maxLength : 500,
		    		  	maxLengthText : "HSI名称长度不能超过500",
						allowBlank : false
					}),
					editor :new Ext.form.TextArea({allowBlank : false,
						grow : true}),
         		    renderer : changeBR
		    	},{
		    		header : "<div align='center'>"+'防护系统/部件名称' + commonality_mustInput +"</div>",
		    		dataIndex : "lhCompName",  // 防护系统/部件名称 中文
		    		width : 150,
					editor : new Ext.form.TextArea({
						maxLength : 500,
		    		  	maxLengthText : "防护系统/部件名称长度不能超过500",
						allowBlank : false
					}),
					editor : new Ext.form.TextArea({allowBlank : false,
						grow : true}),
	         		renderer : changeBR
		    	},{
		    		header : "<div align='center'>"+ 'ATA章节号' +"</div>",
		    		dataIndex : "ataCode",
		    		align:'center',
		    		width : 110,
		    		editor : new Ext.form.TextField({
		    			maxLength : 20,
			    		maxLengthText : "ATA章节号长度不能超过20"
		    		})
		    	},{
		    		header : "IPV/OPVP/OPVE" + commonality_mustInput,
		    		dataIndex : "ipvOpvpOpve",
		    		align:'center',
		    		width : 97,
					editor : threeChooseCombo,
					renderer: function(value, cellmeta, record){
					    var index = threeChooseCombo.store.find(Ext.getCmp('threeChoose').valueField,value);	
					    var record = threeChooseCombo.store.getAt(index);
					    var returnvalue = "";
					    if (record){
					        returnvalue = record.data.displayText;
					    }
					    return returnvalue;
					}
		    	},{
		    		header : "<div align='center'>"+'参见的HSI编号' + commonality_mustInput +"</div>",
		    		dataIndex : "refHsiCode",
		    		width : 80,
					editor : new Ext.form.TextField({
						maxLength : 12,
					 	maxLengthText : "编号长度不能超过12位",
			    		allowBlank : false
			    	})
		    	},{
		    		header : "<div align='center'>"+'状态'+"</div>",
		    		dataIndex : "status",
		    		align:'center',
		    		width : 70,
					renderer: function(value, cellmeta, record){
					    var index = statusChooseCombo.store.find(Ext.getCmp('statusChoose').valueField,value);	
					    var record = statusChooseCombo.store.getAt(index);
					    var returnvalue = "";
					    if (record){
					        returnvalue = record.data.displayText;
					    }
					    return returnvalue;
					}
		    	},{
		    		header : "<div align='center'>"+'分析人'+"</div>",// 分析人
		    		dataIndex : "anaUserName",
		    		width : 120
		    	},{
			    	header : "isAuthorHsi", 
			    	dataIndex : "isAuthorHsi", 
			    	hidden : true
		      }
			]);	
			
			
			// ***grid
			var grid = new Ext.grid.EditorGridPanel({
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				loadMask: {msg:commonality_waitMsg},
				region : 'center',
				store : store,
				clicksToEdit : 2,   
				stripeRows : true,
				tbar : [
					    new Ext.form.TextField({
					    	id : 'hsiCodeId',
					    	emptyText :'-请输入HSI号-' 
					    }),'-',
					    new Ext.form.TextField({
					    	id : 'hsiNameId',
					    	emptyText :'-请输入HSI名称-' 
					    }),'-',
						new Ext.Button({
							//text : '<font size=1 >'+commonality_search+'</font>',
							text : commonality_search,
							hidden : false,
							iconCls : "icon_gif_search",
							handler : function() {
								 grid.getStore().load({
							　　       params:{
					                    areaId:areaId,
					                   	hsiCode : Ext.getCmp("hsiCodeId").getValue(),
						                hsiName : Ext.getCmp("hsiNameId").getValue()
						　　　　　　　　　}
	　　　　　　　　　　       	});
							}
						}),
						"-",
					new Ext.Button({
						text : commonality_add,
						iconCls : "icon_gif_add",
						disabled : !authorityFlag,
						handler : add
			        }),
			        "-",			       
			        new Ext.Button({
						text : commonality_del,
						id : 'test1',
						iconCls : 'icon_gif_delete',
						disabled : !authorityFlag,
						handler :deleteLhSelect
			        }),
			        "-",			       
			        new Ext.Button({
						text : '认领HSI',
						id : 'doReplaceId',
						disabled : !authorityFlag,
						iconCls : 'icon_gif_add',
						handler : replaceHSI
			        }),
			        "-",			       
			        new Ext.Button({
						text : commonality_save,
						id : "save1",
						iconCls : "icon_gif_save",
						disabled : !authorityFlag,
						handler : saveLhSelect
						
			        }) 
			       
				]
			});
				// *****
			   var viewport = new Ext.Viewport( {
				id : 'viewportId',
				layout : 'border',
				items : [
					{
						region : 'center',
						title: returnPageTitle('HSI选择'+' ---->  '+'区域号:'+areaCode,'lh_0'),
						layout : 'fit',
						split : true,  
						items : [grid]
					}
				]
			});
			// 传参数
			 grid.getStore().load({
					params:{
				        areaId:areaId
					　　　　}
　　　　　　　　　　    	});
            grid.addListener("cellclick", cellclick);
			grid.addListener("afteredit", afteredit);
			grid.addListener("click",click);
			function click(){
				var rec = grid.getSelectionModel().getSelected();
				if(rec){
					if(rec.get("isAuthorHsi")=='0'){
						Ext.getCmp("save1").setDisabled(true);
					}else{
						Ext.getCmp("save1").setDisabled(false);
					}
					if(rec.get("hsiId")==''||rec.get('anaUserName')==userName){
						Ext.getCmp("doReplaceId").setDisabled(true);
					}else{
						Ext.getCmp("doReplaceId").setDisabled(false);
					}
				}
			}
        /**
		 * 追加 "LH_Hsi"追加事件执行,追加一条记录
		 */   
		function add(){
				var index = store.getCount();
				var rec = grid.getStore().recordType;
				var p = new rec({
			 	        hsiId :'',
					 	ataCode:'',
					 	hsiCode:'',
					 	hsiName :'',
					 	lhCompName:'',
					 	ipvOpvpOpve :'',
					 	refHsiCode :NA,
					 	anaUserName:'',
					    status:''
			        });
				  store.insert(index, p);
			}
					/**
					 * 保存 ""提交的合理数据信息
					 */ 
			function saveLhSelect(){
				Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,
				    buttons : Ext.Msg.YESNO,
				    fn : function(id){
				    	if (id == 'cancel' || id == 'no' ){
							return;
						} else if (id == 'yes'){
							if (!testStore()){
								return false;
							}
							var modified = store.modified;											
							var result = updateDataDetail(modified);
							if (result == 0 ){
							 store.modified = [];
							 }
						}
						
				    }
				});		
			}
			
			/*
			 * 验证保存时的数据
			 * 
			 */

		function testStore(){
				var temp = 0;
				Ext.each(store.modified, function(item) {
					//
					if ( item.data.isAuthorHsi=='0'){
						temp = 1;
						alert('登陆者与 HSI分析人不一致，无权对其修改 !');// 与 HSI分析人不一致 无权对其修改
						return false;
					}
					if(item.data.hsiCode == null || item.data.hsiCode.trim() ==''){
						temp = 1;
						alert('HSI编号不能为空');
						return false;
					}
					 var hsiName = '';
					 if(item.data.hsiName != null){
					   hsiName = item.data.hsiName.trim() ;
					 }
					if(hsiName == ''){
						temp = 1;
						alert('HSI名称不能为空 !');
						return false;
					}	
					
					var lhCompName = '';
					 if(item.data.lhCompName != null){
					   lhCompName = item.data.lhCompName.trim() ;
					 }
					if (lhCompName== ''){
						temp = 1;
						alert('防护部件不能为空!');
						return false;
					}
					if (item.data.ipvOpvpOpve== null || item.data.ipvOpvpOpve==''){
						temp = 1;
						alert('请选择IPV、OPVP、OPVE !');
						return false;
					}
					if ( item.data.refHsiCode==''){
						temp = 1;
						alert('没有参见HSI需可以填写  "N/A" !');
						return false;
					}
				});
				if (temp == 0){
					return true;
				} else {
					return false;
				}
			}
			
			// 保存具体操作
			  function updateDataDetail(modified) {
				var json = [];
				var result = 0;
				Ext.each(modified, function(item) {
					json.push(item.data);
				});
				if (json.length > 0) {
					var waitMsg = new Ext.LoadMask(Ext.getBody(), {
						    msg : commonality_waitMsg,
						    removeMask : true// 完成后移除
						});		
					waitMsg.show();
					
					Ext.Ajax.request( {
						url : lhSelect.app.g_lhSelectSaveUrl,
						params : {
							jsonData : Ext.util.JSON.encode(json),
							areaId : areaId
						},
						method : "POST",
						success : function(response, action) {
							waitMsg.hide();
							var text = Ext.util.JSON.decode(response.responseText);
							if (text.success == true) {
								alert(commonality_messageSaveMsg);
								store.modified = [];
								grid.getStore().load({
										params:{
											 areaId:areaId
									    }
								});
								parent.refreshTreeNode();
							}else{
								alert( commonality_saveMsg_fail);
							}
						},
						failure : function(response, action) {
							waitMsg.hide();
							alert(commonality_cautionMsg);
							result = 1;
						}
					});
				} else {
					alert( commonality_alertUpdate);
					result = 1;
				}
				return result;
			}
				
			// ****************************************************************
			function cellclick(grid, rowIndex, columnIndex, e) {
				if(currentRowIndex != rowIndex){
					currentRowIndex = rowIndex;
					currentRecord = grid.getStore().getAt(rowIndex);
				}
			}
			
			
			function afteredit(val){
			    currentRecord = val.record;
				if (val.field == 'hsiCode'){
						if(val.value=='N/A'){
							alert("HSI编号不能为N/A！！！");
							currentRecord.set('hsiCode', val.originalValue);
							return;
						}
						var cou = 0;
						for (var i = 0; i < store.getCount(); i++){
							if (store.getAt(i).get('hsiCode') == val.value){
								cou = cou + 1;
							}
						}
						if (cou > 1){
							alert("HSI编号:"+val.value+"已存在，请重新填写");// HSI编号不能重复
							currentRecord.set('hsiCode', val.originalValue);
							return false;
						}
						Ext.Ajax.request({
								url : contextPath+"/lhirf/lhSelect/verifyHsiCodeExist.do",
								params : {						
									  hsiCode : val.value
								},
								method : "POST",
								success : function(response, action) {
									if (response.responseText) {
										alert("HSI编号:"+response.responseText+"已存在，请重新填写");
										currentRecord.set('hsiCode', val.originalValue);
									}
								}
						});
						// 如果修改了HSI 同时本HSI也有其他HSI参见此 测拦截判断
						// /
						var oldHsiCode = val.originalValue;
						var newHsiCode = val.value;
						var couRef = 0;// /判断 有多少个hsi参见此修改的HSI
						for (var i = 0; i < store.getCount(); i++){
							var recorded = store.getAt(i);
							if( recorded.get('refHsiCode') == oldHsiCode){
							   couRef = couRef +1;   // /如果修改HSIcode
														// 并且此条记录有其他hsi参见
							 }
						}
						if(couRef > 0){// /有其他HSI参见此HSI 是否统一修改
							// -----------------------------
							  Ext.MessageBox.show({
									    title : commonality_affirm,// /此HSI被其他参见,如修改需统一修改
									    msg : '此HSI被其他参见,如修改需统一变更 !',// //'此HSI被其他参见,如修改需统一修改!
									    buttons : Ext.Msg.YESNOCANCEL,
									    fn : function(id){
									    	if (id == 'cancel'){
									    		currentRecord.set('hsiCode', val.originalValue);
												return;
											} else if (id == 'yes'){
												 currentRecord.set('hsiCode', newHsiCode);
													for (var i = 0; i < store.getCount(); i++){
													 var recd = store.getAt(i);
														if( recd.get('refHsiCode') == oldHsiCode){
														    recd.set('refHsiCode',newHsiCode)
													 	 }
												    }
												 
											}else if (id == 'no') {
												    currentRecord.set('hsiCode', val.originalValue);
												    return;
												}
									    }
									});	
							// ----------------------------------
							return ;
						}
				}
				if (val.field == 'ipvOpvpOpve'){     // 不是 "新建" 状态 不可修改
			        if((val.record.get('hsiId') != '' && val.record.get('status') != statusNew) || val.record.get('isAuthorHsi') =='0'){
			        	alert('HSI此状态情况下不可修改 !');
			        	currentRecord.set('ipvOpvpOpve', val.originalValue);
			        	return;
			        }
				}
				if (val.field == 'refHsiCode'){     // 不是 "新建" 状态 不可修改
					if (val.record.get('hsiId') != '' && val.record.get('status') != statusNew){
						 alert('当前记录不能变更为参见的HSI号!');// 当前记录不能变为更参见的HSI号
						currentRecord.set('refHsiCode', val.originalValue);
						return;
					}
					
					if (val.value == val.record.get('hsiCode')){
						alert('同记录中HSI编号和参见的HSI号不能相同!');
						currentRecord.set('refHsiCode', val.originalValue);
						return;
					}
					
					var isRef = 0;
					var couyy = 0;  // /判断参见的refHsiCode 是否在左侧 hsiCode存在
					var nowHsiCode = val.record.get('hsiCode');
					var hsiRefCode  =  val.value ; // /右侧参见的HSICode
					if(hsiRefCode === 'N/A'){
						couyy = 1;
					}else{
						for (var i = 0; i < store.getCount(); i++){
							var rec = store.getAt(i);
							     // /参见的主 HSI refHsiCode 必须是 N/A
							if (rec.get('hsiCode') == hsiRefCode && rec.get('refHsiCode')!= NA ){  // alert("此状态不可参见");
								isRef = 1;
							}
							
							if(rec.get('hsiCode') == hsiRefCode && currentRowIndex !== i){
								couyy = 1;
								if(hsiRefCode != 'N/A'){
									for (var j = 0; j < store.getCount(); j++){
										var recJ = store.getAt(j);
										    var hsiCodeNow = currentRecord.get('hsiCode');// /选择这行的
																							// hsiCode
										if(hsiCodeNow == recJ.get('refHsiCode')){
											isRef = 1;
										}
									}
								}
								break;
							}
						}
					}
					if (couyy == 0){
						alert('输入的相似HSI不存在!');// /输入的相似HSI不存在!
						currentRecord.set('refHsiCode', val.originalValue);
						return;
					}
					if (isRef == 1){
						alert('该HSI已经参见其它HSI相似，不能再做相似!');// /该HSI已经参见其它HSI相似，不能再做相似!
						currentRecord.set('refHsiCode', val.originalValue);
						return;
					}
				}
				
			}
			
		     /**
			 * 删除 ""选择一条数据记录
			 */
			function deleteLhSelect(){
				var record = grid.getSelectionModel().getSelected();
				if (record == null){
					alert(commonality_alertDel);
					return;
				}
				Ext.MessageBox.show({
				    title : commonality_affirm,
				    msg : commonality_affirmDelMsg,
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){
							if (record.get('hsiId') == ''){
								store.remove(record);
							    store.modified.remove(record);
								return;
							} else {
								var nowCode = record.get('hsiCode');
								var isdel = 0 ;
								for (var i = 0; i < store.getCount(); i++){
									var rec = store.getAt(i);
									if (rec.get('refHsiCode') == nowCode ){							
										isdel = 1;
									}
								}
								if(isdel ==1){
									alert('此HSI 已经被别的HSI参见不可删除 !');// 已经有其他HSI参见的不可删除
									return false ;
								}
								doDeleteLhSelect(record);
								 store.modified = [];
							}
						}else if(id == 'no') {
							     return;
							}
				    }
				});				
			}
			
			function doDeleteLhSelect(record){
				var waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : commonality_waitMsg,
				    removeMask : true// 完成后移除
					});		
				waitMsg.show();
				Ext.Ajax.request( {
				url : lhSelect.app.g_lhSelectDeleteUrl,
					params : {						
						  deleteHsiId : record.get('hsiId')
					},
					method : "POST",
					success : function(response, action) {
						waitMsg.hide();
						var text = Ext.util.JSON.decode(response.responseText);
						if (text.success == true) {
							grid.getStore().load({
						　　　　　          params:{
								           areaId : areaId
					　　　　　　　　　　    　 }
	　　　              	        });
							parent.refreshTreeNode();
						    store.modified = [];
						    alert( commonality_messageDelMsg);
						}else{
							 alert( commonality_messageDelMsgFail);
						}
					},
					failure : function(response, action) {
						waitMsg.hide();
						alert(commonality_cautionMsg);
					}
				});
			}
			
			// /HSI 的认领 修改MSG-3 MRB LH_HSI的创建人
				function replaceHSI(){
					var record = grid.getSelectionModel().getSelected();
					if (record == null){
						alert('请选择要认领HSI的行 !');// 请选择认领HSI行
						return;
					}
					if(record.get('anaUserName')== ""||record.get('anaUserName')!=userName){
							Ext.MessageBox.show({
								title : commonality_affirm,
								msg : '确定认领本 HSI 吗?',
								buttons : Ext.Msg.YESNOCANCEL,
								fn : function(id){
								if (id == 'cancel'){
									return;
								} else if (id == 'yes'){
									if (record.get('hsiId') == ''){
										alert('本HSI已经为本登陆者,不需认领 !');
										return;
									} else {
										var waitMsg = new Ext.LoadMask(Ext.getBody(), {
											msg : commonality_waitMsg,
											removeMask : true// 完成后移除
										});		
										waitMsg.show();
										Ext.Ajax.request( {
											url : lhSelect.app.g_lhSelectReplaceUrl,
											params : {						
											deleteHsiId : record.get('hsiId')
											},
											method : "POST",
											success : function(response, action) {
												waitMsg.hide();
												var text = Ext.util.JSON.decode(response.responseText);
													if (text.success == true) {
														grid.getStore().load({
															params:{
															areaId : areaId
															}
														});
														store.modified = [];
														alert('HSI认领成功 !') ;
													}else{
														 alert( 'HSI认领失败 !'); // /认领失败
													}
											},
											failure : function(response, action) {
												waitMsg.hide();
												alert(commonality_cautionMsg);
											}
										});	
									}
								}else if (id == 'no') {
											return;
								}
							}
							});	
					}
				}
				
              }
           }
       }();

