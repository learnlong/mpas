<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/za_headerstep.jsp"%>
<!-- 
	author: ZhangJianMin
	createdate: 2014-12-02
	use: 区域分析ZA42:增强区域任务类型确定表
	ps: 评级为一级时，问题6为“否”，问题7的文本域为“有效”，任务列表为“有效”，可以增加删除GVI和DET任务，自动添加一条GVI任务；
		评级为二级时，问题6为“否”，问题7的文本域为“有效”，任务列表为“有效”，可以增加删除GVI和DET任务，自动添加一条GVI任务；
		评级为三级时，问题6为“是”，问题7的文本域为“无效”，任务列表为“无效”，自动添加一条GVI任务；
 -->
<style type="text/css" media="all"> 
	#imageTable{
		margin:-1px 13px!important;
		padding:0px;
	}  
	.imageHtml td { 
		font-size:12px;
		margin:-2px -2px -2px -3px!important;
	}
	.questionHighlight{	
		margin:-2px -2px -2px -3px!important;
		font-size:12px;
	    background-color: #ffffa0;
	    color:black;
	}
	.optionHighlight{	
		margin:-2px -2px -2px -3px!important;
		font-size:13px;
		font-weight:bold;
	    background-color: #ffffa0;
	    color:#ff6000;
	}
</style>
<body style="overflow-x:hidden">
</body>
<script type="text/javascript">
	var za42Id = '${za42Id}';
	var ary = [0,0,0,0,0];
	var areaSizeValue = 0;//区域大小
	var denseDegValue = 0;//稠密度
	var fireProValue = 0;//潜在原因
	var lastResultValue = 0;//注释矩阵的值
	var oldAreaSizeValue;//初始的区域大小
	var oldDenseDegValue;//初始的稠密度
	var oldFireProValue;//初始的潜在原因
	var oldLastResultValue;//初始的注释矩阵的值

	//初始化矩阵图的样式
	function select(row, cell){
		//第一个矩阵
		var table1obj = document.getElementById('table1');		
		for (var i = 1; i < 4; i++){
			table1obj.rows[i].cells[cell].style.background = '#FFFFFF';//初始化第一个矩阵图的样式,白色背景
		}
		table1obj.rows[row].cells[cell].style.background = '#00FF99';//给选中的单元格注色,变成绿色
		//根据选中的单元格,给相关的属性赋值
		if(cell == 1){
			if(row == 1){
				areaSizeValue = 1;//区域大小
			}else if(row == 2){
				areaSizeValue = 2;
			}else if(row == 3){
				areaSizeValue = 3;
			}
		}else if(cell == 2){
			if(row == 1){
				denseDegValue = 1;//稠密度
			}else if(row == 2){
				denseDegValue = 2;
			}else if(row == 3){
				denseDegValue = 3;
			}
		}else if(cell == 3){
			if(row == 1){
				fireProValue = 1;//起火的潜在原因
			}else if(row == 2){
				fireProValue = 2;
			}else if(row == 3){
				fireProValue = 3;
			}
		}
		
		//第二个矩阵
		var table2obj=document.getElementById('table2');
		for(var i=0;i<table2obj.rows.length;i++){
			for(var j=0;j<table2obj.rows[i].cells.length;j++){
				table2obj.rows[i].cells[j].style.background='#DFE8F6';//初始化第二个矩阵图的样式,灰色背景
			}
		}
		//第三个矩阵
		var table3obj=document.getElementById('table3');
		for(var i=0;i<table3obj.rows.length;i++){
			for(var j=0;j<table3obj.rows[i].cells.length;j++){
				table3obj.rows[i].cells[j].style.background='#DFE8F6';//初始化第三个矩阵图的样式,灰色背景
			}
		}
		//第四个矩阵
		var table4obj=document.getElementById('table4');
		for(var i=0;i<table4obj.rows.length;i++){
			for(var j=0;j<table4obj.rows[i].cells.length;j++){
				table4obj.rows[i].cells[j].style.background='#DFE8F6';//初始化第四个矩阵图的样式,灰色背景
			}
		}
		
		
		if(table1obj.rows[row].cells[cell].innerHTML=='小'||table1obj.rows[row].cells[cell].innerHTML=='低'){	
			ary[cell - 1] = 3;
		}else if(table1obj.rows[row].cells[cell].innerHTML=='中'){
			ary[cell - 1] = 2;
		}else if(table1obj.rows[row].cells[cell].innerHTML=='高'||table1obj.rows[row].cells[cell].innerHTML=='大'){
			ary[cell - 1] = 1;
		}
		
		showtable2(table2obj);
		showtable3(table3obj);
		showtable4(table4obj);
	}
		
	/**
	 * 对第四个矩阵的应用,ary[i]表示"等级"
	 * 当评级表的最终结果是:
	 * 1级时，默认问题6为“否”，问题7为有效状态；
	 * 2级时，默认问题6为“否”，问题7为有效状态；
	 * 3级时，默认问题6为“是”，问题7为无效状态。
	 */
	function showtable4(table4obj){
		if(ary[4]==1){//一级
			lastResultValue = 1;//注释矩阵的值,等级1
			table4obj.rows[0].cells[2].style.background='#00FF99';//改变第四矩阵的第一行第三列的单元格的颜色
			//根据第四矩阵的最终"等级",显示不同的业务逻辑,改变问题6显示结果
			var stepStatus6 = Ext.getCmp('radioOne'); //问题6按钮
			stepStatus6.eachItem(function(item){ 		
				item.setValue(item.inputValue=='N'); //选择"否"
			});
			stepStatus6.disable();//问题6按钮,无效状态

			//根据第四矩阵的最终"等级",显示不同的业务逻辑
			Ext.getCmp('step7Desc').enable();//问题7为有效状态
			Ext.getCmp('add').enable();//"追加"按钮处于有效状态
			Ext.getCmp('del').enable();//"删除"按钮处于有效状态
						
			//根据第四矩阵的最终"等级",显示不同的业务逻辑,及修改"动态逻辑图"的样式
			document.getElementById('question1').className = 'questionHighlight';
			document.getElementById('question6').className = 'questionHighlight';
			document.getElementById('radio1Yes').className = '';
			document.getElementById('radio1No').className = 'optionHighlight';
			document.getElementById('question2').className = 'questionHighlight';
			document.getElementById('question7').className = '';
			document.getElementById('question8').className = 'questionHighlight';
			document.getElementById('question9').className = 'questionHighlight';			

			
		}else if(ary[4]==2){//二级
			lastResultValue = 2;//注释矩阵的值,等级2
			table4obj.rows[1].cells[1].style.background='#00FF99';

			var stepStatus6 = Ext.getCmp('radioOne'); //问题6按钮
			stepStatus6.eachItem(function(item){ 		
				item.setValue(item.inputValue=='N'); //选择"否"
			});
			stepStatus6.disable();//问题6按钮,无效状态

			//根据第四矩阵的最终"等级",显示不同的业务逻辑
			Ext.getCmp('step7Desc').enable();//问题7为有效状态
			Ext.getCmp('add').enable();//"追加"按钮处于有效状态
			Ext.getCmp('del').enable();//"删除"按钮处于有效状态
			
			document.getElementById('question1').className = 'questionHighlight';
			document.getElementById('question6').className = 'questionHighlight';
			document.getElementById('radio1Yes').className = '';
			document.getElementById('radio1No').className = 'optionHighlight';
			document.getElementById('question2').className = 'questionHighlight';
			document.getElementById('question7').className = '';
			document.getElementById('question8').className = 'questionHighlight';
			document.getElementById('question9').className = 'questionHighlight';			
		}else if(ary[4]==3){//三级
			lastResultValue = 3;//注释矩阵的值,等级3
			table4obj.rows[2].cells[1].style.background='#00FF99';

			var stepStatus6 = Ext.getCmp('radioOne'); //问题6按钮
			stepStatus6.eachItem(function(item){ 		
				item.setValue(item.inputValue=='Y'); //选择"否"
			});
			stepStatus6.disable();//问题6按钮,无效状态
			
			//根据第四矩阵的最终"等级",显示不同的业务逻辑
			Ext.getCmp('step7Desc').disable();//问题7为无效状态
			Ext.getCmp('add').disable();//"追加"按钮处于无效状态
			Ext.getCmp('del').disable();//"删除"按钮处于无效状态
			
			document.getElementById('question1').className = 'questionHighlight';//"验证检查级别"的样式,高亮变黄
			document.getElementById('question6').className = 'questionHighlight';//"问题6"的样式,高亮变黄
			document.getElementById('radio1Yes').className = 'optionHighlight';//"是"的样式,高亮变黄
			document.getElementById('radio1No').className = '';//"否"的样式,不显示高亮变黄
			document.getElementById('question2').className = '';//"必须增加Stand-alone GVI和/或DET检查 "的样式,不显示高亮变黄
			document.getElementById('question7').className = 'questionHighlight';//"问题7"的样式,高亮变黄
			document.getElementById('question8').className = '';//"问题8"的样式,不显示高亮变黄
			document.getElementById('question9').className = '';//"问题8"的样式,不显示高亮变黄
		}
	}
		
	/**
	 * 对第三个矩阵的应用
	 */
	function showtable3(table3obj){
		if(ary[3]==1){
			if(ary[2]==1){
				table3obj.rows[1].cells[0].style.background='#00FF99';
				table3obj.rows[2].cells[1].style.background='#00FF99';
				table3obj.rows[2].cells[2].style.background='#00FF99';
				ary[4]=table3obj.rows[2].cells[2].innerHTML;
			}else if(ary[2]==2){
				table3obj.rows[1].cells[1].style.background='#00FF99';
				table3obj.rows[2].cells[1].style.background='#00FF99';
				table3obj.rows[2].cells[3].style.background='#00FF99';
				ary[4]=table3obj.rows[2].cells[3].innerHTML;
			}else if(ary[2]==3){
				table3obj.rows[1].cells[2].style.background='#00FF99';
				table3obj.rows[2].cells[1].style.background='#00FF99';
				table3obj.rows[2].cells[4].style.background='#00FF99';
				ary[4]=table3obj.rows[2].cells[4].innerHTML;
			}else if(ary[2]==0){
				table3obj.rows[2].cells[1].style.background='#00FF99';
			}
		}else if(ary[3]==2){
			if(ary[2]==1){
				table3obj.rows[1].cells[0].style.background='#00FF99';
				table3obj.rows[3].cells[0].style.background='#00FF99';
				table3obj.rows[3].cells[1].style.background='#00FF99';
				ary[4]=table3obj.rows[3].cells[1].innerHTML;
			}else if(ary[2]==2){
				table3obj.rows[1].cells[1].style.background='#00FF99';
				table3obj.rows[3].cells[0].style.background='#00FF99';
				table3obj.rows[3].cells[2].style.background='#00FF99';
				ary[4]=table3obj.rows[3].cells[2].innerHTML;
			}else if(ary[2]==3){
				table3obj.rows[1].cells[2].style.background='#00FF99';
				table3obj.rows[3].cells[0].style.background='#00FF99';
				table3obj.rows[3].cells[3].style.background='#00FF99';
				ary[4]=table3obj.rows[3].cells[3].innerHTML;
			}else if(ary[2]==0){
				table3obj.rows[3].cells[0].style.background='#00FF99';
			}
		}else if(ary[3]==3){
			if(ary[2]==1){
				table3obj.rows[1].cells[0].style.background='#00FF99';
				table3obj.rows[4].cells[0].style.background='#00FF99';
				table3obj.rows[4].cells[1].style.background='#00FF99';
				ary[4]=table3obj.rows[4].cells[1].innerHTML;
			}else if(ary[2]==2){
				table3obj.rows[1].cells[1].style.background='#00FF99';
				table3obj.rows[4].cells[0].style.background='#00FF99';
				table3obj.rows[4].cells[2].style.background='#00FF99';
				ary[4]=table3obj.rows[4].cells[2].innerHTML;
			}else if(ary[2]==3){
				table3obj.rows[1].cells[2].style.background='#00FF99';
				table3obj.rows[4].cells[0].style.background='#00FF99';
				table3obj.rows[4].cells[3].style.background='#00FF99';
				ary[4]=table3obj.rows[4].cells[3].innerHTML;
			}else if(ary[2]==0){
				table3obj.rows[4].cells[0].style.background='#00FF99';
			}
		}
	}
	
	/**
	 * 对第二个矩阵的应用
	 */
	function showtable2(table2obj){
		if(ary[0]==1){
			if(ary[1]==1){
				table2obj.rows[1].cells[0].style.background='#00FF99';
				table2obj.rows[2].cells[1].style.background='#00FF99';
				table2obj.rows[2].cells[2].style.background='#00FF99';
				ary[3]=table2obj.rows[2].cells[2].innerHTML;
			}else if(ary[1]==2){
				table2obj.rows[1].cells[0].style.background='#00FF99';
				table2obj.rows[3].cells[0].style.background='#00FF99';
				table2obj.rows[3].cells[1].style.background='#00FF99';
				ary[3]=table2obj.rows[3].cells[1].innerHTML;
			}else if(ary[1]==3){
				table2obj.rows[1].cells[0].style.background='#00FF99';
				table2obj.rows[4].cells[0].style.background='#00FF99';
				table2obj.rows[4].cells[1].style.background='#00FF99';
				ary[3]=table2obj.rows[4].cells[1].innerHTML;
			}else if(ary[1]==0){
				table2obj.rows[1].cells[0].style.background='#00FF99';
			}
		}else if(ary[0]==2){
			if(ary[1]==1){
				table2obj.rows[1].cells[1].style.background='#00FF99';
				table2obj.rows[2].cells[1].style.background='#00FF99';
				table2obj.rows[2].cells[3].style.background='#00FF99';
				ary[3]=table2obj.rows[2].cells[3].innerHTML;
			}else if(ary[1]==2){
				table2obj.rows[1].cells[1].style.background='#00FF99';
				table2obj.rows[3].cells[0].style.background='#00FF99';
				table2obj.rows[3].cells[2].style.background='#00FF99';
				ary[3]=table2obj.rows[3].cells[2].innerHTML;
			}else if(ary[1]==3){
				table2obj.rows[1].cells[1].style.background='#00FF99';
				table2obj.rows[4].cells[0].style.background='#00FF99';
				table2obj.rows[4].cells[2].style.background='#00FF99';
				ary[3]=table2obj.rows[4].cells[2].innerHTML;
			}else if(ary[1]==0){
				table2obj.rows[1].cells[1].style.background='#00FF99';
			}
		}else if(ary[0]==3){
			if(ary[1]==1){
				table2obj.rows[1].cells[2].style.background='#00FF99';
				table2obj.rows[2].cells[1].style.background='#00FF99';
				table2obj.rows[2].cells[4].style.background='#00FF99';
				ary[3]=table2obj.rows[2].cells[4].innerHTML;
			}else if(ary[1]==2){
				table2obj.rows[1].cells[2].style.background='#00FF99';
				table2obj.rows[3].cells[0].style.background='#00FF99';
				table2obj.rows[3].cells[3].style.background='#00FF99';
				ary[3]=table2obj.rows[3].cells[3].innerHTML;
			}else if(ary[1]==3){
				table2obj.rows[1].cells[2].style.background='#00FF99';
				table2obj.rows[4].cells[0].style.background='#00FF99';
				table2obj.rows[4].cells[3].style.background='#00FF99';
				ary[3]=table2obj.rows[4].cells[3].innerHTML;
			}else if(ary[1]==0){
				table2obj.rows[1].cells[2].style.background='#00FF99';
			}
		}
	}
</script>
<script type="text/javascript" src="${contextPath}/area/za42.js"></script>