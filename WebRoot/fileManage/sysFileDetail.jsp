<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>文件管理</title>
<script type="text/javascript">
		//var allType="[{'0':''}]";
		var baseParams;
		
		//得到的tbMenu对象遍历隐藏
		function fun(){
			var parentTd=getParentTd(window.parent);
			if(parentTd!=null){
				Ext.each(parentTd.items.items,function(item){
					item.hideMenu();
				});
			}
			
		}
		//递归得到父窗口的tb对象,只递归5次,防止死循环
		var i=0;
		function getParentTd(winParent){
			if(winParent.Ext.getCmp('tb')==null){
				if(i==5){
					return;
				}
				i++;
				getParentTd(winParent.parent);
			}else{
				return winParent.Ext.getCmp('tb');
			}
		}
</script>
</head>

<body onmouseover="fun()">
	<script type="text/javascript" src="${contextPath }/fileManage/sysFileDetail.js"></script>

	<center>
		<div id="panelDiv" style="width:980px; text-align:left;"></div>
		<div id="treeDiv" style="width:980px; text-align:left;"></div>
		<div id="dataDiv" style="width:980px; text-align:left;"></div>
	</center>
</body>
</html>
