//扩展字符的方法
	String.prototype.endWith=function(str){
		if(str==null||str==""||this.length==0||str.length>this.length)
  			return false;
		if(this.substring(this.length-str.length)==str)
  			return true;
		else
  			return false;
		return true;
	}

	String.prototype.startWith=function(str){
		if(str==null||str==""||this.length==0||str.length>this.length)
  			return false;
		if(this.substr(0,str.length)==str)
  			return true;
		else
  			return false;
		return true;
	}
	
	//js数据校验
	//是否为整数
    function checkInt(value) {   
       	var re = /^[\d]+$/;
        return re.test(value);   
    }   

	//是否为小数
	function checkFloat(value,round){
     	var re = /^[0-9]+\.?[0-9]{0,15}$/;
     	var strs = value.split('.');
     	if(strs.length>1){
     	   if(strs[1].length>round){
     	      return false;
     	   }
     	}
     	return re.test(value);
    }

	//是否为日期
	function checkDate(value){
     	var re =/^[1-2]\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[0-1])$/;
     	return re.test(value);
    }

	//长度检查
    function checkStringLenth(str, lessLen, moreLen) {   
        var strLen = str.length;   
        if(moreLen<1) moreLen=255;
        if (lessLen != "") {   
            if (strLen < lessLen)   
                return false;   
        }   
        if (moreLen != "") {   
            if (strLen > moreLen)   
                return false;   
        }   
        return true;   
    } 
    
	Ext.form.Radio2 = Ext.extend(Ext.form.Radio,  {
	    onRender : function(ct, position){
	        Ext.form.Checkbox.superclass.onRender.call(this, ct, position);
	        if(this.inputValue !== undefined){
	            this.el.dom.value = this.inputValue;
	        }
	        this.wrap = this.el.wrap({cls: 'x-form-check-wrap'});
	        if(this.boxLabel){
	            this.wrap.createChild({tag: 'label', htmlFor: this.el.id, cls: 'x-form-cb-label2', html: this.boxLabel});
	        }
	        if(this.checked){
	            this.setValue(true);
	        }else{
	            this.checked = this.el.dom.checked;
	        }
	        // Need to repaint for IE, otherwise positioning is broken
	        if(Ext.isIE){
	            this.wrap.repaint();
	        }
	        this.resizeEl = this.positionEl = this.wrap;
	    }
	});

	
	//标准分页记录数
	var stdPageSize = 50;
	//标准表格宽度
	var gridWidth = 860;
	//标准表格高度
	var gridHeight = 450;
	//窗口上方位置
	var posTop = 95;

/**
 * 取最大
 * @type String
 */
var MAX = 'MAX';
/**
 * 取最小
 * @type String
 */
var MIN = 'MIN';
/**
 * 取平均
 * @type String
 */
var AVG = 'AVG';
/**
 * 求和
 * @type String
 */
var SUM = 'SUM';
/**
 * 求乘积
 * @type String
 */
var MULT = 'MULT';

/**
 * 富文本编辑器长度限制
 */
var maxRichTextLength = 50000;

/**
 * 判断富文本编辑器长度是否满足条件
 * @param text
 * @returns {Boolean} 满足返回true
 */
function isRichTextValid(text){
	if(maxRichTextLength < 0){
		return true;
	}
	if(text && text.length > 0){
		return text.length <= maxRichTextLength;
	}
	return true;
}

/**
 * 隐藏fieldlabel属性
 * @param {} field
 */
function hideFieldLabel(field){
	field.disable();
	field.hide();
	field.getEl().up('.x-form-item').setDisplayed(false);
}

/**
 * 显示fieldlabel属性
 * @param {} field
 */
function showFieldLabel(field){
	field.enable();
	field.show();
	field.getEl().up('.x-form-item').setDisplayed(true);
}

/**
 * 将KindEditor4.1.2 功能封装到命名空间“KE“。
 * @author shuyuan
 */
Ext.namespace("KE");
KE.app = (function() {
	return {
		/**
		 * 初始化editor
		 * @param initParam 初始参数。
		 * @returns
		 */
		init : function (initParam){	
			if(initParam && initParam.renderTo && initParam.delayTime){
				setTimeout(function(){
					KindEditor.create('#' + initParam.renderTo, initParam);
				}, (initParam.delayTime <= 0 ? 5 : initParam.delayTime));
			}
		},
		/**
		 * 获取创建后的editor对象。
		 * @param renderTO textarea的ID，根据此参数查找已创建的editor对象
		 * @returns
		 */
		getEditor : function(renderTO) {
			var editors = KindEditor.instances;
			for(var i = 0; i < editors.length; i++){
				if(editors[i].renderTo && editors[i].renderTo === renderTO){
					return editors[i];
				}		
			}	
		}
	};
}) ();
/**
 * 扩展Vtypes方法,不能输入html标签
 * @author 赵涛
 */
Ext.apply(Ext.form.VTypes,{ 

	'htmlTag' : function(_v) {
			return !/<([^>]*)>/.test(_v)
			
	},
	'htmlTagText':commonality_htmlTagText

}); 

/**
 * 确认执行操作
 */
function confirmEvent(event,msg) {
	Ext.MessageBox.show({
				title : commonality_affirm,
				msg : msg,
				buttons : Ext.Msg.YESNO,
				fn : function(id) {
					if (id == 'cancel') {
						return;
					} else if (id == 'yes') {
						// 执行操作
						event();
					}
				}
			})
}


/**
 * 扩展Vtypes方法,不能输入中文
 * @author 赵涛
 */
Ext.apply(Ext.form.VTypes,{ 

	'Cn' : function(_v) {
			return /[u4e00-u9fa5]/.test(_v)
			
	},
	'CnText':commonality_CnText

}); 

/**
 * 扩展Vtypes方法,不能输入中文和html标签
 */
Ext.apply(Ext.form.VTypes,{ 

	'htmlTagAndCn' : function(_v) {
		
			return (!/<([^>]*)>/.test(_v)&& !/[\u4E00-\u9FFF]/.test(_v))
			
	},
	'CnText':commonality_CnText
}); 

/**
 * 返回页面title部分
 * @param title 页面标题
 * @param page 页面名称
 * @author changyf createdate 2012-08-12
 */
function returnPageTitle(title, pageNm) {
	var html = '<table style="width:100%;font-size:11px;">';
	html += '<tr>';
	html += '<td style="width:30%">';
	html += '<font  color="#15428B" style="font-weight:bold" >';
	html += title;
	html += '</font>';
	html += '</td>';
	html += '<td style="width:70%;" align="right">';
	html += '<font  color="#15428B" style="font-weight:bold" >';
	html += commonality_help + '&nbsp;';
	html += '</font>';
	html += "<img onclick=showHelp('" + pageNm + "') alt='' src='"
			+ contextPath
			+ "/js/extjs/images/icons/help.gif' style='cursor:pointer;'/>"
	html += '</td>';
	html += '</tr>';
	html += '</table>';
	return html;
}
/**
 * 文本换行显示
 */
function changeBR(value, meta, record) {     
	meta.attr = 'style="white-space:normal;word-wrap:break-word;"';
	if (value != null) value = value.replace(/\n/g, '<br/>');     
	return value;      
}	

function showHelp(pageNm) {
	var myMask = new Ext.LoadMask(Ext.getBody(), {//等待提示信息！
		msg : commonality_waitMsg
	});	
	myMask.show();

	Ext.Ajax.request({// 发起请求查询当前页面的帮助信息
		url : contextPath+"/baseData/help/getHelp.do",
		params : {
			helpWhere : pageNm// 参数页面名称
		},
		method : 'POST',
		success : function (response) {
			myMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);// 解析json字符串

			var showHtml = new Ext.form.Label({
				id : "html",
				html : result.content
			});

			// 帮助显示区域的window
			var showHelpWin = new Ext.Window({
				layout : 'border',
				border : false,
				resizable : true,
				closable : true,
				//maximized : true,
				width : 500,
				height : 400,
				plain : false,
				bodyStyle : 'padding:0px;',
				buttonAlign : 'center',
				title : '帮助',
				items : [{
					autoScroll:true,
					region:'center',
					bodyStyle:"border:0px;padding:3px",
					layout:'fit',
					split:true,
					items:[showHtml]
				}],
				buttons : [
					new Ext.Button({
						text : commonality_close,
						handler : function () {
							showHelpWin.close();
						}
					})
				],
				x : 5,
				y : 5,
				modal : true
			});
			showHelpWin.show();
		},
		failure : function () {
			Ext.Msg.alert(commonality_caution, commonality_cautionMsg);
		}
	});
}