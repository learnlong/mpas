var count=null;
var oldfinalRemark=""
var oldcpcp=""
var waitMsg=null;
var s6Id=null
var oldValue=null;
var ssiArray=null;
var oldIsCpcp = "";
var oldConsiderWear = "";
var delId = new Array();
var modifiedList = null;
var modifiedResult = null;
var modifiedOther = null;
Ext.override(Ext.form.RadioGroup, {
    getValue : function() {
        var v;
        this.items.each(function(item) {
            if (item.getValue()) {
                v = item.getRawValue();
                return false;
   }
  });
  return v;
 },
 setValue : function(v) {
        if (this.rendered) {
            this.items.each(function(item) {
                item.setValue(item.getRawValue() == v);
   });
        } else {
            for (k in this.items) {
                this.items[k].checked = this.items[k].inputValue == v;
            }
        }
 }
});
Ext.onReady(function(){
	Ext.form.Field.prototype.msgTarget = 'qtip';
			Ext.QuickTips.init(); 
		var cWidt = document.body.clientWidth;
		ssiArray= Ext.decode(ssiList);
      var intervalFStore=new Ext.data.Store( {
     	    url:contextPath+"/struct/s6/getIntervalRecords.do?inorOut=out",
		    reader : new Ext.data.JsonReader( {
		      root : 'intervals',
		      fields : [ 
		        {
		          name : 'intervalName'
		        },{
		          name : 'intervalId'
		        },{
		          name : 'intervalValue'
		        }
		      ]
		    })
  	});


		 var intervalFSelect = new Ext.form.ComboBox({
	       // fieldLabel :index_selectRole,
	       // width :130,
	        id : 'intervalFalse',
	        store : intervalFStore,
	        mode : 'remote',
	        triggerAction : 'all',
	        valueField : 'intervalValue',
	        displayField : 'intervalValue',
	        emptyText :"请选择",
	        editable : false,
	        selectOnFocus : true
	      });
	      
	       var intervalTStore=new Ext.data.Store( {
     	    url:contextPath+"/struct/s6/getIntervalRecords.do?inorOut=out",
		    reader : new Ext.data.JsonReader( {
		      root : 'intervals',
		      fields : [ 
		        {
		          name : 'intervalName'
		        },{
		          name : 'intervalId'
		        },{
		          name : 'intervalValue'
		        }
		      ]
		    })
  	});

		 var intervalTSelect = new Ext.form.ComboBox({
	        id : 'intervalTrue',
	        store : intervalTStore,
	        mode : 'remote',
	        triggerAction : 'all',
	        valueField : 'intervalValue',
	        displayField : 'intervalValue',
			blankText : "不能为空",
			emptyText : '请选择',
	        editable : false,
	        selectOnFocus : true
	      });
			Ext.form.Field.prototype.msgTarget = 'qtip';
			Ext.QuickTips.init(); 
			var centerForm = new Ext.form.FormPanel(
					{
				       split : true,
				       frame : true,
				       layout : "fit", // 整个大的表单是form布局
				       labelWidth : 80,
						items : [{ // 行1
				    	   layout : "column", // 从左往右的布局
				    	   items : [{
				    		   columnWidth : 0.5, // 该列有整行中所占百分比
				    		   layout : "form", // 从上往下的布局
				    		  // anchor : '50%',
				    		   items : [{
				    			   xtype : "textarea",
				    			   anchor : '95%',
				    			   fieldLabel :"最终任务备注",
				    			     maxLength:2500,
				    			     	vtype:'htmlTag',
				    			   id:'finalRemark',
				    			   name:'finalRemark'
				    			   
				    		   }]
				    	   } ]}]
							});

			//ad/ed的
			var listStore = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : contextPath
							+ "/struct/s6/getAdEdRecords.do?region=out&ssiId=" + ssiId
				}),
				reader : new Ext.data.JsonReader( {
					root : "s1List",
					fields : [ {
						name : 'id'
					}, {
						name : 'composition'
					}, {
						name : 'edrFalse'
					}, {
						name : 'adrFalse'
					}, {
						name : 'intervalFalse'
					}, {
						name : 'eaTypeFalse'
					}, {
						name : 'edrTrue'
					}, {
						name : 'adrTrue'
					}, {
						name : 'intervalTrue'
					}, {
						name : 'eaTypeTrue'
					}, {
						name : 'metal'
					}, {
						name : 's1Id'
					}, {
						name : 'ownArea'
					}, {
						name : 'miniLevel'
					},{
						name : 'miniLevelMetal'
					} ,{name:'eff'}]
				}),
				sortInfo : {
					//field : 'composition',
					field : 's1Id',
					direction : 'ASC'
				}
			});
			listStore.load();
			var typeCom = new Ext.form.ComboBox({
				fieldLabel : "任务类型",
				name : 'msg3Task.taskType',
				id : 'taskType',
				store : new Ext.data.SimpleStore( {
					fields : [ 'countryCode', 'countryName' ],
					data : [ [ GVI, 'GVI-一般目视' ], [ DET, 'DET-详细目视' ],
								[ SDI, 'SDI-目视检测' ] ]
				}),
				valueField : 'countryCode',
				displayField : 'countryName',
				lazyRender : true,
				triggerAction : 'all',
				mode : 'local',
				editable:false,
				allowBlank : false,
				blankText : "不能为空",
				emptyText :'请选择'
			});
			var typeCom1 = new Ext.form.ComboBox({
				fieldLabel : "任务类型",
				name : 'msg3Task.taskType',
				id : 'taskType1',
				store : new Ext.data.SimpleStore( {
					fields : [ 'countryCode', 'countryName' ],
					data : [ [ GVI, 'GVI-一般目视' ], [ DET, 'DET-详细目视' ],
								[ SDI, 'SDI-目视检测' ] ]
				}),
				valueField : 'countryCode',
				displayField : 'countryName',
				lazyRender : true,
				triggerAction : 'all',
				mode : 'local',
				editable:false,
				allowBlank : false,
				blankText : "不能为空",
				emptyText :'请选择'
			});
			var colM1 = new Ext.grid.ColumnModel( [ {
				header : "ID",
				dataIndex : "id",
				hidden : true
			},{
				header : "eff",
				dataIndex : "eff",
				hidden : true
			}, {
				header : 'SSI组成',
				dataIndex : "composition",
				width : 100,
				renderer : changeBR
			}, {
				header : "EDR(非金属)",
				dataIndex : "edrFalse",
				width : 80,
				editor : new Ext.form.NumberField({
		    		 	decimalPrecision:2,                 //精确到小数点后2位(执行4舍5入)   
            			allowDecimals:true,                //允许输入小数 
            			value:0,
            			maxValue:9999999.99,
			    		allowBlank : false
		    	})
			}, {
				header : "ADR(非金属)",
				dataIndex : "adrFalse",
				width : 80,
				editor : new Ext.form.NumberField({
		    		 	decimalPrecision:2,                 //精确到小数点后2位(执行4舍5入)   
            			allowDecimals:true,                //允许输入小数 
            			value:0,
            			maxValue:9999999.99,
			    		allowBlank : false
		    	})
			},{
				header : "较小级别(非金属)",
				dataIndex : "miniLevel",
				width : 80
			}, {
				header : "间隔(非金属)",
				dataIndex : "intervalFalse",
				width : 80,
				editor : new Ext.form.TextField({
					readOnly :true
				})
				//editor : intervalFSelect
			}, {
				header : "任务类型(非金属)",
				dataIndex : "eaTypeFalse",
				width : 80,
				editor : typeCom1
			}, {
				header :"EDR(金属)",
				dataIndex : "edrTrue",
				width : 80,
				editor : new Ext.form.NumberField({
		    		 	decimalPrecision:2,                 //精确到小数点后2位(执行4舍5入)   
            			allowDecimals:true,                //允许输入小数 
            			value:0,
            			maxValue:9999999.99,
			    		allowBlank : false
		    	})
			}, {
				header :"ADR(金属)",
				dataIndex : "adrTrue",
				width : 80,
				editor : new Ext.form.NumberField({
		    		 	decimalPrecision:2,                 //精确到小数点后2位(执行4舍5入)   
            			allowDecimals:true,                //允许输入小数 
            			value:0,
            			maxValue:9999999.99,
			    		allowBlank : false
		    	})
			},{
				header : "较小级别(金属)",
				dataIndex : "miniLevelMetal",
				width : 80
			}, {
				header : "间隔(金属)",
				dataIndex : "intervalTrue",
				width : 80,
//				editor : intervalTSelect
				editor : new Ext.form.TextField({
					readOnly :true
				})
			}, {
				header :"任务类型(金属)",
				dataIndex : "eaTypeTrue", 
				width : 80,
				editor : typeCom
			}, {
				header : "s1Id",
				dataIndex : "s1Id", 
				width : 80,
				hidden : true,
				renderer: function(value, meta, record) {     
					return value+"AD/ED"  
			} 
			}, {
				header : "所属区域",
				dataIndex : "ownArea", 
				width : 80,
				hidden : true 
			}, {
				header :"金属",
				dataIndex : "metal",
				hidden : true,
				renderer: function(value, meta, record) {     
						if(value==1){
							return commonality_shi
						}
						if(value==0){
							return commonality_fou
						}    
					}
			} ]);



			var resultStore = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : contextPath
							+ "/struct/s6/getResultRecords.do?region=out&ssiId=" + ssiId
				}),
				reader : new Ext.data.JsonReader( {
					root : "taskList",
					fields : [ {
						name : 'taskId'
					}, {
						name:'taskCode'
					},{
						name : 'taskType'
					}, {
						name : 'occures'
					}, {
						name : 'reachWayn'
					}, {
						name : 'taskDesc'
					}, {
						name : 'intOut'
					} , {
						name : 'ownArea'
					} , {
						name : 'remark'
					},{name:'eff'}]
				})
			});
			resultStore.load();
			var otherStore = new Ext.data.Store( {
				proxy : new Ext.data.HttpProxy( {
					url : contextPath
							+ "/struct/s6/getOtherRecords.do?region=out&ssiId=" + ssiId
				}),
				reader : new Ext.data.JsonReader( {
					root : "otherList",
					fields : [ {
						name : 'taskId'
					},{
						name : 'taskType'
					},{
						name : 'sourceStep'
					},{
						name : 'taskCode'
					}, {
						name : 'occures'
					}, {
						name : 'reachWayn'
					}, {
						name : 'taskDesc'
					}, {
						name : 'mrbId'
					}, {
						name : 's1Id'
					}, {
						name : 'ownArea'
					} ,{
						name:'eff'
					} ,{
						name:'inOrOut'
					}]
				})
			});
			setTimeout(function(){otherStore.load();},'10');
			var colM = new Ext.grid.ColumnModel(
					[
							{
								header : 'ID',
								hidden : true,
								id : "taskId",
								width : 100,
								sortable : true,
								dataIndex : 'taskId'
							},{
								header : 's1Id',
								hidden : true,
								id : "s1Id",
								width : 100,
								sortable : true,
								dataIndex : 's1Id'
							},{
								header : "任务编号",
								width : 100,
								sortable : true,
								dataIndex : 'taskCode'
							},{
								header : "所属区域",
								width : 100,
								sortable : true,
								hidden : true,
								dataIndex : 'ownArea'
							},
							{
								header :"任务类型"+ commonality_mustInput,
								dataIndex : "taskType",
								width : 100,
								sortable : true,
								editor : new Ext.form.ComboBox( {
								editable:false,
									fieldLabel : "任务类型",
									store : new Ext.data.SimpleStore(
											{
												fields : [ 'countryCode',
														'countryName' ],
												data : [ [ GVI, GVI ],
														[ DET, DET ],
														[ SDI, SDI ] ]
											}),
									valueField : 'countryCode',
									displayField : 'countryName',
									lazyRender : true,
									triggerAction : 'all',
									mode : 'local',
									allowBlank : false,
									blankText : "不能为空",
									emptyText :'请选择'
								})
							},{
								header : commonality_eff,
								dataIndex : "eff",
								width : 100,
								sortable : true,
								editor : new Ext.form.TextField( {
									maxLengthL:250
								})
							},
							{
								header : "任务间隔",
								dataIndex : "occures",
								width : 100,
								sortable : true,
								editor : new Ext.form.TextField( {
									maxLengthL:200
								})
							},
							{
								header :"外部/内外部"+ commonality_mustInput,
								width : 120,
								dataIndex : "intOut",
								editor : new Ext.form.ComboBox({
								editable:false,
								allowBlank : false,
								id:"gdintOut",
								xtype : 'combo',
								store : new Ext.data.SimpleStore({
								fields : ["retrunValue", "displayText"],
									data : [['1','外'],['2','内/外']]
								}),  
								valueField : "retrunValue", displayField : "displayText",
								typeAhead : true, mode : 'local',   
								triggerAction : 'all', emptyText : '请选择',   
								editable : true, selectOnFocus : true,
								width : 70
					    		}),
								renderer: function(value, meta, record) {
									    var index = Ext.getCmp("gdintOut").store.find(Ext.getCmp('gdintOut').valueField,value);				    
									    var record = Ext.getCmp("gdintOut").store.getAt(index);
									    var returnvalue = "";
									    if (record){
									        returnvalue = record.data.displayText;
									    }
									    return returnvalue;
									} 
					    	}
							,
							{
								header :"接近方式",
								dataIndex : "reachWayn",
								width : 100,
								sortable : true,
								editor : new Ext.form.TextArea( {grow : true}),
								renderer : changeBR
							},
							{
								header :"任务描述",
								dataIndex : "taskDesc",
								width : 100,
								sortable : true,
								editor : new Ext.form.TextArea( {grow : true}),
								renderer : changeBR
							},{
								header :'备注',
								dataIndex : 'remark',
								width : 200,
								editor : new Ext.form.TextArea({grow : true}),
								renderer : changeBR
								
						}]);
			var colM2 = new Ext.grid.ColumnModel(
					[
							{
								header : 'ID',
								hidden : true,
								id : "taskId",
								width : 100,
								sortable : true,
								dataIndex : 'taskId'
							},
							{
								header : "任务类型",
								dataIndex : "taskType",
								width : 100,
								sortable : true
							},
							{
								header : "任务编号",
								dataIndex : "taskCode",
								width : 100,
								sortable : true
							},
							{
								header :"所属区域",
								dataIndex : "ownArea",
								width : 100,
								sortable : true,
								hidden:true
							},
							{
								header : "任务来源",
								dataIndex : "sourceStep",
								width : 100,
								sortable : true,
								renderer : function(value, meta, record) {
									if(value=='S3'){
										return 'FD'
									}
									return value;
								}
							},
							{
								header :"任务间隔",
								dataIndex : "occures",
								width : 100,
								sortable : true
							},{
								header :commonality_eff,
								dataIndex : "eff",
								width : 100,
								sortable : true 
							},
							{
								header : "接近方式",
								dataIndex : "reachWayn",
								width : 100,
								sortable : true,
								editor : new Ext.form.TextArea( {grow : true}),
								renderer : changeBR
							},
							{
								header : "任务描述",
								dataIndex : "taskDesc",
								width : 100,
								sortable : true,
								editor : new Ext.form.TextArea( {grow : true}),
								renderer : changeBR
							},{
								header : "合并的MSG-3任务编号",
								dataIndex : "mrbId",
								width : 100,
								sortable : true,
								editor : new Ext.form.TextField( {
								maxLength:200,
									id:'mrbId'
								})
								
							},{
								header : "s1Id",
								dataIndex : "s1Id",
								width : 100,
								hidden : true
							}, {
								header : "inOrOut",
								dataIndex : "inOrOut",
								width : 100,
								hidden : true
							} ]);

			var resultGrid = new Ext.grid.EditorGridPanel( {
				title : 'MSG-3任务列表',
				cm : colM,
				sm : new Ext.grid.RowSelectionModel( {
					singleSelect : true
				}),
				region : 'center',
				store : resultStore,
				clicksToEdit : 2,
				stripeRows : true,
				height : 500
			});
			var otherGrid = new Ext.grid.EditorGridPanel( {
				title : '临时任务列表',
				cm : colM2,
				sm : new Ext.grid.RowSelectionModel( {
					singleSelect : true
				}),
				region : 'center',
				store : otherStore,
				clicksToEdit : 2,
				stripeRows : true,
				height : 500
			});
			var listGrid = new Ext.grid.EditorGridPanel( {
				title :  returnPageTitle('外部AD/ED分析汇总','s6'),
				cm : colM1,
				sm : new Ext.grid.RowSelectionModel( {
					singleSelect : true
				}),
				region : 'center',
				store : listStore,
				clicksToEdit : 2,
				stripeRows : true,
				height : 500 
			});
						//创建GRIDPANEL的JSON
			function createGridJSON(s){
				var json = [];
				for (var i = 0; i < s.getCount(); i++){
					json.push(s.getAt(i).data);
				}
				return Ext.util.JSON.encode(json);
			}
		listGrid.addListener("beforeedit", beforeedit);		
		
		listGrid.addListener("afteredit", afteredit);	
		otherGrid.addListener("beforeedit",otherGridBeforeedit);
		otherGrid.addListener("afteredit",otherGridafteredit);
		resultGrid.addListener("beforeedit",resultGridBeforeedit);
		function resultGridBeforeedit(val){
			if(isMaintain==0){
				val.cancel=true;
			}
			var nowRec = val.record;
			for(var ii=0;ii<otherStore.getCount();ii++){
				if(nowRec.get('taskCode')==otherStore.getAt(ii).get("mrbId")){
					if(otherStore.getAt(ii).get('sourceStep')=='S3'||otherStore.getAt(ii).get('sourceStep')=='FD'){
						if(val.field=='intOut'){
							val.cancel = true;
						}
					}
				}
			}
		}
		function afteredit(val){
			//var record=listGrid.getSelectionModel().getSelected();
			var record=val.record
			var metal=record.get("metal");
			var intervalTrue=record.get("intervalTrue");
			var eaTypeTrue=record.get("eaTypeTrue");
			var intervalFalse=record.get("intervalFalse");
			var eaTypeFalse=record.get("eaTypeFalse");
			var notEdr = record.get("edrFalse");
			var notAdr = record.get("adrFalse");
			var isEdr = record.get("edrTrue");
			var isAdr = record.get("adrTrue");
			var miniLevelMetal = record.get("miniLevelMetal");
			var miniLevel = record.get("miniLevel");
			var tempTaskCode=null;
			if(metal==0){
				var miniValue ="";
				if(notEdr>=notAdr){
					record.set("miniLevel",notAdr);
					miniValue = notAdr;
				}else{
					record.set("miniLevel",notEdr);
					miniValue = notEdr;
				}
				if(miniValue!=miniLevel){
					Ext.Ajax.request({
							async : false,
							url : contextPath + '/struct/s6/getMiniValueInterval.do',
							method : "POST",
							params:{
								miniVal:miniValue,
								region : 'out'
							},
							success : function(response) {
								var msg = Ext.util.JSON.decode(response.responseText);
								if(msg.success){
									record.set("intervalFalse",msg.success);
									var list1S1Id=record.get('s1Id')+"AD/ED";
									for(var i=0;i<otherStore.getCount();i++){
										if(otherStore.getAt(i).get("s1Id")==list1S1Id){
											otherStore.getAt(i).set("occures",msg.success)
										}
									}
								}
							}
					})
				}
			}else if(metal==1){
				var miniValue ="";
				if(isEdr>=isAdr){
					record.set("miniLevelMetal",isAdr);
					miniValue=isAdr;
				}else{
					record.set("miniLevelMetal",isEdr);
					miniValue=isEdr;
				}
				if(miniValue!=miniLevelMetal){
					Ext.Ajax.request({
							async : false,
							url : contextPath + '/struct/s6/getMiniValueInterval.do',
							method : "POST",
							params:{
								miniVal:miniValue,
								region : 'out'
							},
							success : function(response) {
								var msg = Ext.util.JSON.decode(response.responseText);
								if(msg.success){
									record.set("intervalTrue",msg.success);
									var list1S1Id=record.get('s1Id')+"AD/ED";
									for(var i=0;i<otherStore.getCount();i++){
										if(otherStore.getAt(i).get("s1Id")==list1S1Id){
											otherStore.getAt(i).set("occures",msg.success)
										}
									}
								}
							}
					})
				}
			}
			if(metal==1&&intervalTrue!=null&&eaTypeTrue!=null){
				if(count==null){
					Ext.Ajax.request({
							async : false,
							url : contextPath + '/struct/s6/getOtherCount.do',
							method : "POST",
							params:{
								ssiId:ssiId
							},
							success : function(response) {
								count=response.responseText;
							}
					})
				}
				var taskCode=null;
				count=++count;
				if(count.toString.length==1){
					taskCode="P"+ssiCode+"-0"+(count);
				}else{
					taskCode="P"+ssiCode+"-"+(count);
				}
				var rec = otherGrid.getStore().recordType;
				var list1S1Id=record.get('s1Id')+"AD/ED";
				var tempCou=0;
				for(var i=0;i<otherStore.getCount();i++){
					if(otherStore.getAt(i).get("s1Id")==list1S1Id){
						otherStore.getAt(i).set("occures",record.get("intervalTrue"));
						otherStore.getAt(i).set("taskType",record.get('eaTypeTrue'));
						otherStore.getAt(i).set("ownArea",record.get('ownArea'));
						tempTaskCode= otherStore.getAt(i).get("mrbId");
						otherStore.getAt(i).set("mrbId","");
						for(var t=0;t<otherStore.getCount();t++){
								if(otherStore.getAt(i).get("mrbId")==tempTaskCode){
									tempCou++;
								}
								
							}
						if(tempCou==0){
							Ext.Ajax.request({//查询内部是否存在编号为tempTaskCode的任务
								async : false,
								url : contextPath + '/struct/s6/checkTempTaskCodeExist.do',
								params:{
									taskCode:tempTaskCode,
									region : 'in'
								},
								method : "POST",
								success : function(response) {
									var msg = Ext.decode(response.responseText);
									if(msg == false){
										for(var i=0;i<ssiArray.length;i++){
											if(ssiArray[i].name==tempTaskCode&&ssiArray[i].number==1){
												for(var j=0;j<resultStore.getCount();j++){
													if(tempTaskCode==resultStore.getAt(j).get("taskCode")){
														delId.push(resultStore.getAt(j).get("taskId"));
														resultStore.remove(resultStore.getAt(j));
														ssiArray.remove(ssiArray[i]);
														return;
													}
												}
											}else if(ssiArray[i].name==oldValue&&ssiArray[i].number>1){
												ssiArray[i].number=ssiArray[i].number-1;
											}
										}
									}
								}
							})
						}
						return ;
					}
				}
					
				var p = new rec( {
					taskId : '',
					taskType : record.get('eaTypeTrue'),
					taskCode : taskCode,
					occures : record.get("intervalTrue"),
					reachWayn : '',
					sourceStep:'AD/ED',
					taskDesc : '',
					ownArea:record.get("ownArea"),
					s1Id:record.get('s1Id')+"AD/ED",
					eff: record.get("eff"),
					inOrOut : "1"
				});
				otherStore.insert(otherGrid.getStore().getCount(), p);
			}
			if(metal==0&&intervalFalse!=null&&eaTypeFalse!=null){
					if(count==null){
						Ext.Ajax.request({
								async : false,
								url : contextPath + '/struct/s6/getOtherCount.do',
								params:{
									ssiId:ssiId
								},
								method : "POST",
								success : function(response) {
									count=response.responseText;
								}
						})
					}
					var taskCode=null;
					count=++count;
					if(count.toString.length==1){
						taskCode="P"+ssiCode+"-0"+(count);
					}else{
						taskCode="P"+ssiCode+"-"+(count);
					}
					var rec = otherGrid.getStore().recordType;
					var list1S1Id=record.get('s1Id')+"AD/ED";
					var tempCou=0;
					for(var i=0;i<otherStore.getCount();i++){
						if(otherStore.getAt(i).get("s1Id")==list1S1Id){
							otherStore.getAt(i).set("occures",record.get("intervalFalse"));
							otherStore.getAt(i).set("taskType",record.get('eaTypeFalse'));
							otherStore.getAt(i).set("ownArea",record.get('ownArea'));
							tempTaskCode= otherStore.getAt(i).get("mrbId");
							otherStore.getAt(i).set("mrbId","");
							for(var t=0;t<otherStore.getCount();t++){
								if(otherStore.getAt(i).get("mrbId")==tempTaskCode){
									tempCou++;
								}
								
							}
							if(tempCou==0){
								Ext.Ajax.request({//查询内部是否存在编号为tempTaskCode的任务
									async : false,
									url : contextPath + '/struct/s6/checkTempTaskCodeExist.do',
									params:{
										taskCode:tempTaskCode,
										region : 'in'
									},
									method : "POST",
									success : function(response) {
										var msg = Ext.decode(response.responseText);
										if(msg == false){
											for(var i=0;i<ssiArray.length;i++){
												if(ssiArray[i].name==tempTaskCode&&ssiArray[i].number==1){
													for(var j=0;j<resultStore.getCount();j++){
														if(tempTaskCode==resultStore.getAt(j).get("taskCode")){
															delId.push(resultStore.getAt(j).get("taskId"));
															resultStore.remove(resultStore.getAt(j));
															ssiArray.remove(ssiArray[i]);
															return;
														}
													}
												}else if(ssiArray[i].name==oldValue&&ssiArray[i].number>1){
													ssiArray[i].number=ssiArray[i].number-1;
												}
											}
										}
									}
								})
							}
							return ;
						}
					}
					
					var p = new rec( {
						taskId : '',
						taskType : record.get('eaTypeFalse'),
						taskCode : taskCode,
						sourceStep:'AD/ED',
						occures : record.get("intervalFalse"),
						reachWayn : '',
						taskDesc : '',
						ownArea:record.get("ownArea"),
						s1Id:record.get('s1Id')+"AD/ED",
						eff: record.get("eff"),
						inOrOut : "1"
					});
					otherStore.insert(otherGrid.getStore().getCount(), p);
				}
		}			
		function beforeedit(val){
			if(isMaintain==0){
				val.cancel = true;
			}
			var field=val.field;
			var record=listGrid.getSelectionModel().getSelected();
			var metal=record.get("metal");
			if(metal==1&&(field=='edrFalse'||field=='adrFalse'||field=='intervalFalse'||field=='eaTypeFalse')){
				val.cancel = true;
				return;
			}else 
			if(metal==0&&(field=='edrTrue'||field=='adrTrue'||field=='intervalTrue'||field=='eaTypeTrue')){
				val.cancel = true;
				return;
			}
		}
			
		function otherGridBeforeedit(val){
			var rec = val.record;
			if(isMaintain==0){
				val.cancel = true;
			}
		/*	if(val.field =='occures'&&rec.get('sourceStep')!='S3'&&rec.get('sourceStep')!='FD'){
				val.cancel = true;
			}*/
			oldValue=val.value;
		}
		function otherGridafteredit(val){
			if(val.field!='mrbId'){
				return null;
			}
		
      		var otherRecord=val.record;
		 	var rec = resultGrid.getStore().recordType;
			var newValue=val.value;
			var oldMrbId=oldValue;
		 	var p1 = new rec( {
				taskId:"",
				taskCode : newValue,
				taskType : otherRecord.get("taskType"),
				occures : otherRecord.get("occures"),
				s1Id:otherRecord.get("s1Id"),
				reachWayn : otherRecord.get("reachWayn"),
				taskDesc :	otherRecord.get("taskDesc"),
				ownArea:otherRecord.get("ownArea"),
				eff:otherRecord.get("eff"),
				remark : "",
				intOut : otherRecord.get("inOrOut")
			});
			var area1=null;//记录全store里面的区域
			var area2=null;//选中行的区域
			var temp1=null;//记录分割后的stroe区域
			var temp2=null;//记录分割后的当前行区域
			if(newValue!=null&&newValue!=''){
				checkNewValue(oldValue,newValue,resultStore,otherRecord,val,p1,resultGrid,otherStore);
				return null;
				}
			if(newValue==''){
				checkOldValue(oldValue,newValue,resultStore,otherRecord,val,p1,resultGrid,otherStore);
				return null;
				
			 };
    	};
			
			
		selectForm = new Ext.form.FormPanel(
				{
					labelWidth : 250, // label settings here cascade
					layout:'fit',
					frame : true,
					items : [ {
						layout : 'column',
						items : [
								{
									columnWidth : 0.5,
//										width:700,
									layout : 'form',
									items : [ new Ext.form.RadioGroup(
											{
												width : 100,
												fieldLabel :'是否满足CPCP要求?', // RadioGroup.fieldLabel
												id : "radioOne",
												anchor : '95%',
												items : [
														new Ext.form.Radio({ // 以上相同
																	id : 'a',
																	name : 'a',
																	boxLabel : commonality_shi,
																	inputValue : commonality_shi
																}),
														new Ext.form.Radio(
																{
																	boxLabel : commonality_fou,
																	inputValue : commonality_fou,
																	name : 'a',
																	id : 'b'
																}) ],
												listeners : {
													change : function(
													combo,newValue,oldValue) {
														Ext.getCmp('radioOne')
																.eachItem(function(item) {
																			if(item.checked === true) {
																				document
																						.getElementById('coverCpcp1').value = item.inputValue;
																			}
//																				checkRadio();
																		});
													}
												}
											}) ]
								},{
									columnWidth : 0.5,
									//width:700,
									layout : 'form',
									items : [ new Ext.form.RadioGroup(
											{
												width : 100,
												fieldLabel :'是否考虑磨损？', // RadioGroup.fieldLabel
												id : "radioTwo",
												anchor : '95%',
												items : [
														new Ext.form.Radio({ // 以上相同
																	id : 'c',
																	name : 'c',
																	boxLabel : commonality_shi,
																	inputValue : commonality_shi
																}),
														new Ext.form.Radio(
																{
																	boxLabel : commonality_fou,
																	inputValue : commonality_fou,
																	name : 'c',
																	id : 'd'
																}) ],
												listeners : {
													change : function(combo,newValue,oldValue) {
														Ext.getCmp('radioTwo').eachItem(
																		function(item) {
																			if (item.checked === true){
																				document
																						.getElementById('coverCpcp2').value = item.inputValue;
																			}
																		});
													}
												}
											}) ]
								},
								{
									columnWidth :0.95,
									layout : 'form',
									items : [
											{
												xtype : 'textfield',
												id : 'cpcp',
												name:'cpcp',
												vtype:'htmlTag',
												anchor : '95%',
												fieldLabel : '附加的CPCP要求'
											}, {
												xtype : 'hidden',
												id : 'coverCpcp2'
											},{
												xtype : 'hidden',
												id : 'coverCpcp1'
											}]
								} ]
					} ]

				});
			var headerStepForm = new Ext.form.Label( {
				applyTo : 'headerStepDiv'
			});
			Ext.getCmp('c').on('focus',function(e,checked){
				Ext.getCmp("radioTwo").setValue(commonality_shi);
					var id =ssiId;
					var type = "STR_TO_SYS";
					var area = "系统分析";;
					var s6OutOrIn = "out";
																										
				 strToSysCoordination(id,s6OutOrIn,type,area,doSave);
			})
			Ext.getCmp('a').on('focus',function(e,checked){
					document.getElementById('cpcp').value = '';
					Ext.getCmp('cpcp').setDisabled(true);
			})
			Ext.getCmp('b').on('focus',function(e,checked){
					Ext.getCmp('cpcp').setDisabled(false);
			})
			function test(number){
				if ((isMaintain == '0')||(modifiedList.length==0&&modifiedResult==0&&modifiedOther==0&&subfinalRemark==oldfinalRemark&&subcpcp==oldcpcp&&considerWear1==oldConsiderWear&&coverCpcp1==oldIsCpcp)){
					if(number){
						goNext(number);
						return true;
					}
					if(step[13]!=1){
						alert("请先完成当前页面");
						return true;
					}else{
						if(step[14]!=3){
							goNext(14);
							return true;
						}
					}
				}
			}
			function doSave(){
				var jsonList = [];
				var jsonResult = [];
				var jsonOther = [];
				modifiedList = listStore.modified;
				modifiedResult = resultStore.modified;
				modifiedOther = otherStore.modified;
				var coverCpcp1 = document.getElementById('coverCpcp1').value;
				var considerWear1 = document.getElementById('coverCpcp2').value;
				var cpcp = document.getElementById('cpcp').value;
				var finalRemark = document.getElementById('finalRemark').value;
				subfinalRemark=Ext.getCmp("finalRemark").getValue();
				if(/<([^>]*)>/.test(subfinalRemark)||/<([^>]*)>/.test(subfinalRemark)){
					alert('最终任务备注内容存在html标签,不可提交,请修改');
					return null;
				}
				if(subfinalRemark.length>2500){
					alert('最终任务备注长度过长,请修改');
					return null;
				}
				subcpcp=Ext.getCmp("cpcp").getValue();
				var status = true;
				for ( var i = 0; i < listStore.getCount(); i++) {
					var re = listStore.getAt(i);
					if (re.get('metal') == '是') {
						if (re.get('intervalTrue') == ''
								|| re.get('eaTypeTrue') == '') {
							status = false;
							break;
						}
					} else if (re.get('metal') == '否') {
						if (re.get('intervalFalse') == ''
								|| re.get('eaTypeFalse') == '') {
							status = false;
							break;
						}
					}
				}
				Ext.each(modifiedList, function(item) {
					jsonList.push(item.data);
				});
				Ext.each(resultStore.data.items, function(item) {
				 	if(Ext.isEmpty(item.get("intOut"))){
				 		status = false;
				 	return;
				 	}
					jsonResult.push(item.data);
				});
				Ext.each(otherStore.data.items, function(item) {
					jsonOther.push(item.data);
				});
					waitMsg = new Ext.LoadMask(Ext.getBody(), {
				    msg : commonality_waitMsg,
				    removeMask : true// 完成后移除
					});
					waitMsg.show();				
					Ext.Ajax.request( {
					url : contextPath + '/struct/s6/saveS6.do',
					params : {
						listJsonData : Ext.util.JSON.encode(jsonList),
						resultJsonData : Ext.util.JSON.encode(jsonResult),
						otherJsonData : Ext.util.JSON.encode(jsonOther),
						ssiId : ssiId,
						s6Id : s6Id,
						inorOut : "out",
						considerWear: considerWear1,
						coverCpcp : coverCpcp1,
						cpcp:cpcp,
						delId:delId,
						finalRemark : finalRemark
					},
					method : "POST",
					success : function(response) {
						waitMsg.hide();
						parent.refreshTreeNode();
						if(response.responseText=='100'){
							alert("分析已完成");
							goNext(13);
							return;
						}
						goNext(13);
						return;
					},
					failure : function(response) {
					waitMsg.hide();
						alert(commonality_cautionMsg);
						return;
					}
				});
			}
			
			
			nextOrSave = function saveOrUpdate(number) {
				if(number){
					if (step[number] == 0){
						alert('请先完成之前的步骤！');
						return false;
					}
				}
				var jsonList = [];
				var jsonResult = [];
				var jsonOther = [];
				modifiedList = listStore.modified;
				modifiedResult = resultStore.modified;
				modifiedOther = otherStore.modified;
				coverCpcp1 = document.getElementById('coverCpcp1').value;
				considerWear1 = document.getElementById('coverCpcp2').value;
				var cpcp = document.getElementById('cpcp').value;
				var finalRemark = document.getElementById('finalRemark').value;
				subfinalRemark=Ext.getCmp("finalRemark").getValue();
				if(/<([^>]*)>/.test(subfinalRemark)||/<([^>]*)>/.test(subfinalRemark)){
					alert('最终任务备注内容存在html标签,不可提交,请修改');
					return null;
				}
				if(subfinalRemark.length>2500){
					alert('最终任务备注长度过长,请修改');
					return null;
				}
				subcpcp=Ext.getCmp("cpcp").getValue();
				if(test(number)){
					return null;
				}
				
				var status = true;
				for ( var i = 0; i < listStore.getCount(); i++) {
					var re = listStore.getAt(i);
					if (re.get('metal') == '是') {
						if (re.get('intervalTrue') == ''
								|| re.get('eaTypeTrue') == '') {
							status = false;
							break;
						}
					} else if (re.get('metal') == '否') {
						if (re.get('intervalFalse') == ''
								|| re.get('eaTypeFalse') == '') {
							status = false;
							break;
						}
					}
				}
				Ext.each(modifiedList, function(item) {
					jsonList.push(item.data);
				});
				Ext.each(resultStore.data.items, function(item) {
				 	if(Ext.isEmpty(item.get("intOut"))){
				 		status = false;
				 		return;
				 	}
					
					jsonResult.push(item.data);
				});
				if (!status) {
					alert( '请填写完成');
					return;
				}
				Ext.each(otherStore.data.items, function(item) {
					jsonOther.push(item.data);
				});

				Ext.MessageBox.show({
					 title : commonality_affirm,
				    msg : commonality_affirmSaveMsg,				    
				    buttons : Ext.Msg.YESNOCANCEL,
				    fn : function(id){
				    	if (id == 'cancel'){
							return;
						} else if (id == 'yes'){			
								waitMsg = new Ext.LoadMask(Ext.getBody(), {
								    msg : commonality_waitMsg,
								    removeMask : true// 完成后移除
									});
							
								waitMsg.show();				
								Ext.Ajax.request( {
								url : contextPath + '/struct/s6/saveS6.do',
								params : {
									listJsonData : Ext.util.JSON.encode(jsonList),
									resultJsonData : Ext.util.JSON.encode(jsonResult),
									otherJsonData : Ext.util.JSON.encode(jsonOther),
									ssiId : ssiId,
									s6Id : s6Id,
									inorOut : "out",
									considerWear: considerWear1,
									coverCpcp : coverCpcp1,
									cpcp:cpcp,
									delId:delId,
									finalRemark : finalRemark
								},
								method : "POST",
								success : function(response) {
								waitMsg.hide();
									if(number){
										goNext(number);
										return null;
									}
									alert(commonality_messageSaveMsg);
									parent.refreshTreeNode();
									if(response.responseText=='100'){
										alert("分析已完成");
										goNext(13);
										return null;
									}
									successNext(response.responseText);
								},
								failure : function(response) {
								waitMsg.hide();
									alert(commonality_cautionMsg);
									return;
								}
							});
						} else if (id == 'no'){
							if(number){
								goNext(number);
								return null;
							}
							if(step[13]!=1&&step[14]!=1&&step[14]!=2){
								alert("请先完成当前步骤");
								return;
							}
							goNext(14);
						}
				    }
				});
			}

			var viewport = new Ext.Viewport( {
				layout : 'border',
				items : [ {
					region : 'north',
					layout : 'border',
					height : 300,
					items : [ {
						region : 'center',
						height : 130,
						layout : 'fit',
						items : [ listGrid ]
					}, {
						region : 'south',
						layout : 'fit',
						height : 70,
						items : [ selectForm ]
					}
					, {
						region : 'north',
						height : 60,
						frame : true,
						items : [ headerStepForm ]
					} 
					]
				}, {
					region : 'center',
					layout : 'border',
					items : [ {
						region : 'west',
						layout : 'fit',
						width : '50%',
						items : [ otherGrid ]
					}, {
						region : 'center',
						layout : 'fit',
						width : '50%',
						items : [  resultGrid]
					} ]
				}, {
					region : 'south',
					layout : 'border',
					height : 85,
					items : [ {
						region : 'center',
						layout : 'fit',
						width : '50%',
						items : [ centerForm  ]
					} ]
				}

				]
			});

			// 增加空白行
			function addRow() {
				var rec = resultGrid.getStore().recordType;
				var p = new rec( {
					taskType : GVI,
					occures : '3000FH/H',
					reachWayn : '接近方式',
					taskDescn : ''
				});
				resultStore.insert(resultGrid.getStore().getCount(), p);
			}
			// addRow();


			// 增加空白行
			function addRow1() {
				var rec = otherGrid.getStore().recordType;
				var p1 = new rec( {
					taskType : GVI,
					occures : '3000FH/H',
					reachWayn : '接近方式',
					taskDescn : ''
				});
				otherStore.insert(otherGrid.getStore().getCount(), p1);
			}

			//增加空白行
			function addRow2() {
				var rec = listGrid.getStore().recordType;
				var p1 = new rec( {
					1 : '',
					2 : 'SSI1',
					3 : '3',
					4 : '3',
					5 : '3000FH/H',
					6 : GVI,
					7 : '',
					8 : '',
					9 : '',
					10 : ''
				});
				listStore.insert(listGrid.getStore().getCount(), p1);
			}
		
			// 删除
			function del() {
				var record = resultGrid.getSelectionModel().getSelected();
				if (!record) {
					alert( commonality_alertDel);
					return;
				}
				Ext.Msg.confirm(commonality_caution, commonality_affirmDelMsg, function(btn) {
					if ('yes' == btn) {
						if (record.get('taskId') == '') {
							resultStore.remove(record);
							return;
						}

						Ext.Ajax.request( {
							url : contextPath
									+ "/struct/s6/deleteResultList.do",
							success : function() {
								alert(commonality_messageDelMsg);
								resultStore.load();
							},
							failure : function() {
								alert(commonality_messageDelMsgFail);
							},
							params : {
								taskId : record.get('taskId')
							}
						});
					}
				});
			}
			
								//页面加载结束后读取数据
			Ext.Ajax.request({
					url :  contextPath+"/struct/s6/getS6Remark.do",
					async : false,
					params:{
								ssiId:ssiId,
								inorOut:'OUT'
							},
					success : function(response) {
								var text=Ext.decode(response.responseText);
								if(text.length>0){
									oldfinalRemark=text[0].finalRemark==null?'':text[0].finalRemark;
									oldcpcp=text[0].cpcp==null?'':text[0].cpcp;
									s6Id=text[0].s6Id;
									Ext.getCmp("finalRemark").setValue(oldfinalRemark);
									Ext.getCmp("coverCpcp1").setValue("");
									isCpcp=text[0].isCpcp;
									considerWear=text[0].considerWear;
									if(considerWear=="0"){
										Ext.getCmp("radioTwo").setValue(commonality_fou);
										oldConsiderWear = commonality_fou;
									}else if(considerWear=="1"){
										Ext.getCmp("radioTwo").setValue(commonality_shi);
										oldConsiderWear = commonality_shi;
									}else{
										oldConsiderWear = "";
									}
									if(isCpcp==0){
										Ext.getCmp("radioOne").setValue('否');
										oldIsCpcp = '否';
									}else if(isCpcp==1){
										Ext.getCmp("radioOne").setValue('是');
										oldIsCpcp = '是';
										Ext.getCmp("cpcp").setDisabled(true);
									}else{
										oldIsCpcp = "";
										Ext.getCmp("cpcp").setDisabled(true);
									}
									Ext.getCmp("cpcp").setValue(oldcpcp);
								}
							}
			})
		
})

function checkNewValue(oldValue,newValue,resultStore,otherRecord,val,p1,resultGrid,otherStore){
											waitMsg = new Ext.LoadMask(Ext.getBody(), {
					    msg : commonality_waitMsg,
					    removeMask : true// 完成后移除
						});
						var obj={ "name": newValue, number: 1};
						var flag=true;
						var newIntOut;
						var oldIntOut;
						for(var i=0;i<resultStore.getCount();i++){
								if(newValue==resultStore.getAt(i).get("taskCode")){
									if(resultStore.getAt(i).get("taskType")==otherRecord.get("taskType")){
										area1=resultStore.getAt(i).get("ownArea");
										area2=otherRecord.get("ownArea");
										newIntOut=otherRecord.get("inOrOut");
										oldIntOut = resultStore.getAt(i).get("intOut");
										if(oldIntOut==2||newIntOut==2){
											resultStore.getAt(i).set("intOut","2");
										}
										temp1=area1.split(",");
										temp2=area2.split(",");
										temp1.sort();
										temp2.sort();
										if(temp1.length==temp2.length){
											for(var ii=0;ii<temp1.length;ii++){
													if(temp1[ii]!=temp2[ii]){
														alert('需要合并的临时任务不属于同一区域,无法合并');
														otherRecord.set("mrbId",val.originalValue);
														return;
													}
													flag=false;
												}
											for(var i=0;i<ssiArray.length;i++){
												if(ssiArray[i].name==newValue){
													ssiArray[i].number=ssiArray[i].number+1;
												}
											}	
											if((oldValue!=null&&oldValue!='')){
												checkOldValue(oldValue,newValue,resultStore,otherRecord,val,p1,resultGrid,otherStore);
											}
												
										}else{
											alert('需要合并的临时任务不属于同一区域,无法合并');
											otherRecord.set("mrbId",val.originalValue);
											flag=false;
											return flag;
										}
									}else{
										alert('不是同种类型的任务不能合并');
										otherRecord.set("mrbId",val.originalValue);
										flag=false;
										return flag;
									}
								}
								if(!flag){
									return true;
								}
						}
						if(flag){
							waitMsg.show();
							Ext.Ajax.request({
								url : contextPath+"/struct/s6/verifyTaskCode.do?",
								async : false,
								params:{
									verifyStr:newValue
										},
								success : function(response) {
											waitMsg.hide();
											if(response.responseText){
												alert("任务编号"+":"+response.responseText+'在其他系统已存在或区域、任务类型不同。');
												otherRecord.set('mrbId', val.originalValue);
												flag=false
												return false;;
												}	
												resultStore.insert(resultGrid.getStore().getCount(), p1);
												ssiArray.push(obj);
												if((oldValue!=null&&oldValue!='')){
													checkOldValue(oldValue,newValue,resultStore,otherRecord,val,p1,resultGrid,otherStore);
												}
												flag=false
												return true;
											},
								failure : function(response) {
										waitMsg.hide();
											alert("网络错误,请重试");
											otherRecord.set('mrbId', val.originalValue);
											flag=false
											return false;
										}
							})
						}
							return flag;
}

function checkOldValue(oldValue,newValue,resultStore,otherRecord,val,p1,resultGrid,otherStore){
	var tempTaskCodeCou = 0;
	for(var y=0;y<otherStore.getCount();y++){
		if(oldValue==otherStore.getAt(y).get('mrbId')){
			tempTaskCodeCou++;
		}
	}
	if(tempTaskCodeCou==0){
		Ext.Ajax.request({//查询外部是否存在编号为tempTaskCode的任务
			async : false,
			url : contextPath + '/struct/s6/checkTempTaskCodeExist.do',
			params:{
				taskCode:oldValue,
				region : 'in'
			},
			method : "POST",
			success : function(response) {
				var msg = Ext.decode(response.responseText);
				if(msg == false){
					for(var i=0;i<ssiArray.length;i++){
						if(ssiArray[i].name==oldValue&&ssiArray[i].number==1){
							for(var j=0;j<resultStore.getCount();j++){
								if(oldValue==resultStore.getAt(j).get("taskCode")){
									delId.push(resultStore.getAt(j).get("taskId"));
									resultStore.remove(resultStore.getAt(j));
									ssiArray.remove(ssiArray[i]);
									return;
								}
							}
						}else if(ssiArray[i].name==oldValue&&ssiArray[i].number>1){
							ssiArray[i].number=ssiArray[i].number-1;
						}
					}
				}
			}
		})
	}
}