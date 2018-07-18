Ext.namespace('is_NoMsi');
/**
 * msi查询界面
 */
is_NoMsi.app = function() {
	return {
		init : function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';
			// 定义数据
			var store = new Ext.data.Store({
						url : is_NoMsi.app.loadMsi,
						reader : new Ext.data.JsonReader({
									root : "msi",
									fields : [
											{name : 'ataId'},      
      								 	    {name : 'proCode'},
										 	{name : 'proName'},
										 	{name : 'safetyAnswer'},
										 	{name : 'detectableAnswer'},
										 	{name : 'functionAnswer'},
										 	{name : 'economicAnswer'},
										 	{name : 'isMsi'},
										 	{name : 'highestLevel'},
										 	{name : 'remark'}
						
					 	            ]
								}),
								sortInfo : {
									field : 'proCode',
									direction : 'ASC'
								}
					});
			// 是否下拉框
			var chooseCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'choose',
						id : 'choose',
						store : new Ext.data.SimpleStore({
									fields : ["retrunValue", "displayText"],
									data : [['1', commonality_shi],
											['0', commonality_fou]]
								}),
						valueField : "retrunValue",
						displayField : "displayText",
						mode : 'local',
						editable : false,
						triggerAction : 'all',
						width : 50
					});
					var isNoMsiCombo = new Ext.form.ComboBox({
						xtype : 'combo',
						name : 'isNoMsi',
						id : 'isNoMsi',
						store : new Ext.data.SimpleStore({
									fields : ["retrunValue", "displayText"],
									data : [[null, commonality_all],
									       ['1', commonality_shi],
											['0', commonality_fou]]
								}),
						valueField : "retrunValue",
						displayField : "displayText",
						mode : 'local',
						editable : false,
						emptyText : '--全部--',
						triggerAction : 'all',
						width : 100
					});
		var ataName = new Ext.form.TextField({
						name : 'ataName',
						id : 'ataName',
						emptyText : '--全部--',
						maxLength : 20,
						width : 100
					});
			var colM = new Ext.grid.ColumnModel([{
						header : "Msi号",
						dataIndex : "id",
						hidden : true
					}, {
						header : '编号',//"编号",
						dataIndex : "proCode",
						width : 70,
						sortable :true
					},{
						header :"名称",// "名称",
						dataIndex : "proName",
						width : 70,
						renderer : changeBR,
						editor : new Ext.form.TextArea({})
					},{
						header : "<div align='center'>故障影响安全性吗？<br>（包括地面或空中）</div>",
						dataIndex : "safetyAnswer",
						width : 180,
						renderer : changeBR,
						editor : new Ext.form.TextArea({})

					},{
						header : "<div align='center'>在正常职责范围，故障对使用<br>飞机人员来说" +
						"是无法发现或不<br>易察觉吗？</div>",
						dataIndex : "detectableAnswer",
						width : 180,
						renderer : changeBR,
						editor : new Ext.form.TextArea({})
					},{
						header : "<div align='center'>故障影响任务完成吗？</div>",
						dataIndex : "functionAnswer",
						width : 180,
						renderer : changeBR,
						editor : new Ext.form.TextArea({})
					},{
						header : "<div align='center'>故障可能导致重大的经济损<br>失吗？</div>",
						dataIndex : "economicAnswer",
						width : 180,
						renderer : changeBR,
						editor : new Ext.form.TextArea({})

					},{
						header :"<div align='center'>是重要维修项目吗确定？</div>",
						dataIndex : "isMsi",
						width : 180,
						editor : chooseCombo,
			    	    renderer : function(value, cellmeta, record){
					    var index = chooseCombo.store.find(Ext.getCmp('choose').valueField,value);				    
					    var record = chooseCombo.store.getAt(index);
					    var returnvalue = "";
					    if (record){
					        returnvalue = record.data.displayText;
					    }
					    return returnvalue;
					}
					},{
						header : "最高可管理层",//"最高可管理<br>层",
						dataIndex : "highestLevel",
						width : 100
					},{
						header :"<div align='center'>备注</div>",// "备注",
						dataIndex : "remark",
						width : 180,
						renderer : changeBR,
						editor : new Ext.form.TextArea({})
					}
					]);
				var grid = new Ext.grid.GridPanel({
				cm : colM,
				sm : new Ext.grid.RowSelectionModel({singleSelect:true}),
				region : 'center',
				store : store,
				loadMask : {
							msg : commonality_waitMsg
						},
				stripeRows : true,
				tbar : [
				'是否MSI ',":" ,isNoMsiCombo,
				 '-',
				 new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				 '名称' ," :" ,ataName,
				  '-',
				new Ext.Toolbar.TextItem("&nbsp;&nbsp;&nbsp;&nbsp;"),
				new Ext.Button({
							text : commonality_search,
							iconCls : 'icon_gif_search',
							handler : search
						}),
						 '-',
						new Ext.Button({  //重置
								text : commonality_reset,//重置
						       xtype : "button",
					            iconCls : "icon_gif_reset",
							    handler : function() {
								Ext.getCmp('ataName').setValue('') ;
								Ext.getCmp('isNoMsi').clearValue();
						}
						})
			    ]
			});
			function search(){
				var	isNoMsi = Ext.getCmp('isNoMsi').getValue();
				var   ataName = Ext.getCmp('ataName').getValue();
				store.load({
							params : {
								isNoMsi : isNoMsi,
								ataName : ataName
							}
						})
				
			}
			
			grid.addListener("beforeedit", beforeedit);
			function beforeedit(val){
				var field = val.field;
				if ( field == 'isMsi'){
				val.cancel = true;
				}
			}
			var win = new Ext.Window({
				layout : 'border',
				border : false, 
				resizable : true,
				closable : false,
				maximized : true,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				title : returnPageTitle('MSI查询','is_NoMsi'),//msi_title,//'MSI和非MSI查询',
				items : [grid]
			});
				
			win.show();	
			store.load();

		}
	};
}();