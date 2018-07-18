/**
 * @class Ext.form.TextField
 * @override Ext.form.TextField
 * @description 重写TextField的验证函数,并支持中文长度验证
 */

/**
 * 判断字符串长度（含中文，一个汉字两个字符）
 * @param {} strTemp 字符串
 * @return {}
 * @author 常毅飞
 * createdate 2012-10-15 
 */
function checkLengthExtd(strTemp) {
	var i, sum;
	sum = 0;
	for (i = 0; i < strTemp.length; i++) {
		if ((strTemp.charCodeAt(i) >= 0) && (strTemp.charCodeAt(i) <= 255))
			sum = sum + 1;
		else
			sum = sum + 2;
	}
	return sum;
} 

Ext.override(Ext.form.TextField, {
	// 重写验证涵数
	validateValue : function(a) {
		if (Ext.isFunction(this.validator)) {
			var c = this.validator(a);
			if (c !== true) {
				this.markInvalid(c);
				return false
			}
		}
		
		// 获取一下字符串长度，一个汉字两个字符
		var len = checkLengthExtd(a);
		
		if (len < 1 || a === this.emptyText) {
			if (this.allowBlank) {
				this.clearInvalid();
				return true
			} else {
				this.markInvalid(this.blankText);
				return false
			}
		}
		if (len < this.minLength) {
			this.markInvalid(String.format(this.minLengthText, this.minLength));
			return false
		}
		if (len > this.maxLength) {
			this.markInvalid(String.format(this.maxLengthText, this.maxLength));
			return false
		}
		if (this.vtype) {
			var b = Ext.form.VTypes;
			if (!b[this.vtype](a, this)) {
				this.markInvalid(this.vtypeText || b[this.vtype + "Text"]);
				return false
			}
		}
		if (this.regex && !this.regex.test(a)) {
			this.markInvalid(this.regexText);
			return false
		}
		return true;
	}
});