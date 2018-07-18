	Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'qtip';
	
    //MMEL下拉框
		var mmelCombo = new Ext.form.ComboBox({ 
		name : 'mmelCom', id : 'mmelCom',
		fieldLabel : "参考的MMEL",
		store : new Ext.data.Store({
			url : contextPath+"/sys/m2/mmelCombo.do",
			reader : new Ext.data.JsonReader({
				root : 'mmelCombo',
				fields : [
				 	{name : 'id'},
				 	{name : 'name'} 
			 	]
			})
		}),
		valueField : "id", displayField : "name",
		mode : 'local', triggerAction : 'all', width : 300, editable : false
	});
	//默认加载树上第一个故障影响的的分析
    treePanelTwo.on("load", function() {
    	treePanelTwo.expandAll();
        var node = treePanelTwo.getRootNode().firstChild;
        loadData(node);
        treePanelTwo.getSelectionModel().select(node);		
		});
    mmelCombo.store.load(); 
    
    

var formAfm = new Ext.form.FormPanel({
	baseCls : 'x-plain',
	labelWidth : 100,
	labelAlign : "right",
	buttonAlign : "center",
	//autoHeight : true,
	fileUpload : true,  
	items : [
		new Ext.form.TextField({
			fieldLabel : "MSI号",
			name : 'msiCodeAfm',
			id : 'msiCodeAfm',
			value : '',
			readOnly : true, 
			allowBlank : false,
			maxLength : 200
		}),			
		new Ext.form.TextField({
			fieldLabel : "F_FF_FE",
			name : 'fFfFeAfm',
			id : 'fFfFeAfm',
			readOnly : true, 
			value : ''
		}),			
		new Ext.form.TextArea({
			fieldLabel : "上层分析问题1的回答描述",
			name : 'answer1Afm',
			id : 'answer1Afm',
			//readOnly : true,
			disabled:true,
			width : 300,
			height : 50,
			value : ''
		}),
	
		new Ext.form.TextArea({
			fieldLabel : "假设参考AFM中的相关内容",
			name : 'refAfm',
			id : 'refAfm',
			maxLength :2500,
			width : 300,
			height : 50
		}),
		new Ext.form.TextArea({
			fieldLabel : "复查结果",
			name : 'reviewResult1',
			id : 'reviewResult1',
			maxLength :2500,
			width : 300,
			height : 50,
			value : ''
		}),
		new Ext.form.DateField({   
                fieldLabel : "复查时间", 
                name : 'reviewDate',
			    id : 'reviewDate1',  
                emptyText : "请选择",   
                format:'Y-m-d',
                editable : false
         }),
	
		new Ext.form.TextArea({
			fieldLabel : "备注",
			name : 'remark1',
			id : 'remark1',
			maxLength :2500,
			width : 300,
			height : 50,
			value : ''
		})
	],
	buttons : [
		new Ext.Button({
			text : commonality_save,
			disabled : !flag,
			handler : function(){			
				isSaveAfm = 1;
				winAfm.hide();
			}
		}),
		new Ext.Button({
			text : commonality_close,
			handler : function(){
				Ext.getCmp('refAfm').setValue(afmldate.refAfm);
				Ext.getCmp('reviewResult1').setValue(afmldate.reviewResult);
				Ext.getCmp('reviewDate1').setValue(afmldate.reviewDate);
				Ext.getCmp('remark1').setValue(afmldate.remark);
				if((afmId==null||afmId=="")&&isSaveAfm!=1){
					document.getElementsByName("radio5")[1].checked=true;
				}
				winAfm.hide();
			}
		})
	]
});

var winAfm = new Ext.Window({
	title : "添加参考AFM",
	x : 100,
	y : 30,
	closable : false,
	autoWidht : true,
	autoHeight : true,
	resizable : false,
	bodyStyle : 'padding:5px;',
	modal : true,
	items : [formAfm]
});

var formMmel = new Ext.form.FormPanel({
	baseCls : 'x-plain',
	labelWidth : 100,
	labelAlign : "right",
	buttonAlign : "center",
	fileUpload : true,  
	items : [
		new Ext.form.TextField({
			fieldLabel :"MSI号",
			name : 'msiCodeMmel',
			id : 'msiCodeMmel',
			readOnly : true, 
			value : ''
		}),		
		new Ext.form.TextField({
			fieldLabel : "F_FF_FE",
			name : 'feMmel',
			readOnly : true, 
			id : 'feMmel',
			value : ''
		}),
		new Ext.form.TextArea({
			fieldLabel : "上层分析问题4的回答描述",
			name : 'answer4Mmel',
			id : 'answer4Mmel',
			//readOnly : true, 
			disabled:true,
			width : 300,
			height : 50,
			value : ''
		}),
		new Ext.form.ComboBox({
			fieldLabel : "是否参考PMMEL", 
			name : 'isRefPmmel', id : 'isRefPmmel',
			store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
				data : [['1',commonality_shi],['0',commonality_fou]]
			}),  
			valueField : "retrunValue", displayField : "displayText",
			typeAhead : true, mode : 'local',
			triggerAction : 'all', width : 50,
			editable : true, selectOnFocus : true
		}),				
		mmelCombo,
		new Ext.form.TextArea({
			fieldLabel : "复查结果",
			name : 'reviewResult2',
			id : 'reviewResult2',
			maxLength :2500,
			width : 300,
			height : 50,
			value : ''
		}),
		new Ext.form.DateField({   
                fieldLabel : "复查时间",   
                emptyText : "请选择",  
                name : 'reviewDate2',
			    id : 'reviewDate2', 
                format:'Y-m-d',
                editable : false
                //disabledDays:[0,6]  
         }),
		new Ext.form.TextArea({
			fieldLabel : "备注",
			name : 'remark2',
			id : 'remark2',
			maxLength :2500,
			width : 300,
			height : 50,
			value : ''
		})
	],
	buttons : [

		new Ext.Button({
			text : commonality_save,
			width : 50,
			disabled : !flag,
			handler : function(){
			//	if (isCreateUser == '0'){
			//		alert('非管理员或非创建者或已生产正式报告，不能做修改操作');
			//		return false;
			//	}				
				isSaveMmel = 1;
				if (Ext.getCmp('isRefPmmel').getValue() == 1){
					if (Ext.getCmp('mmelCom').getValue() == ''){
						alert("请填写参考的PMMEL号");
						return;
					} else {
						document.getElementById('mmelValue').innerHTML = 
							 Ext.get("mmelCom").getValue();
							 //Ext.getCmp('mmelCom').getValue();
					}					
				} else if (Ext.getCmp('isRefPmmel').getValue() == 0){
					document.getElementById('mmelValue').innerHTML = "No";
				}
				winMmel.hide();
			}
		}),
		new Ext.Button({
			width : 50,
			text : commonality_close,
			handler : function(){
			    Ext.getCmp('isRefPmmel').setValue(mmeldate["isRefPmmel"]);
				Ext.getCmp('reviewResult2').setValue(mmeldate["reviewResult"]);
				Ext.getCmp('reviewDate2').setValue(mmeldate.reviewDate);
				Ext.getCmp('remark2').setValue(mmeldate.remark);
				Ext.getCmp('mmelCom').setValue(mmeldate.pmmelId);
				if((mmelId==null||mmelId=="")&&isSaveMmel!=1){
					document.getElementsByName("radio6")[1].checked=true;
				}
				winMmel.hide();
			}
		})
	]
});
var winMmel = new Ext.Window({
	title : "添加参考MMEL",
	x : 100,
	y : 30,
	closable : false,
	autoWidht : true,
	autoHeight : true,
	resizable : false,
	bodyStyle : 'padding:5px;',
	modal : true,
	items : [formMmel]
});
var mmeldate={};
var afmldate={};
function addAFM(){
	afmldate = {};
	afmldate["afmId"] = afmId;
	afmldate["refAfm"] = Ext.getCmp('refAfm').getValue();
	afmldate["reviewResult"] = Ext.getCmp('reviewResult1').getValue();
	afmldate["reviewDate"] = Ext.getCmp('reviewDate1').getValue();
	afmldate["remark"] = Ext.getCmp('remark1').getValue();
	winAfm.show();	
	Ext.getCmp('answer1Afm').setValue(document.getElementById('area1').value);	
}

function addMMEL(){
	mmeldate = {};
	mmeldate["isRefPmmel"] = Ext.getCmp('isRefPmmel').getValue();
	mmeldate["reviewResult"] = Ext.getCmp('reviewResult2').getValue();
	mmeldate["reviewDate"] = Ext.getCmp('reviewDate2').getValue();
	mmeldate["remark"] = Ext.getCmp('remark2').getValue();
	mmeldate["pmmelId"] = Ext.getCmp('mmelCom').getValue();
	winMmel.show();
	Ext.getCmp('answer4Mmel').setValue(document.getElementById('area4').value);	
	
}
