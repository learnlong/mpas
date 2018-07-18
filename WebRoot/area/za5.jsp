<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp"%>
<%@include file="/common/za_headerstep.jsp"%>
<link rel="stylesheet" href="${contextPath}/css/matrix.css"	type="text/css" />
<script src="${contextPath}/common/array.js"></script>
<style type="text/css">
.firstMatrixDiv { /*position: absolute;*/
	margin: auto;
	text-align: left;
	float: left;
}

.firstMatrixTableStyle {
	padding: 0px;
	margin: 0px;
	border: 1px solid #C1DAD7;
	text-align: center;
	margin: auto;
	background-color: #DFE8F6;
}

.firstMatrix1TdClass {
	text-align: center;
	font-size: 13px;
	font-weight: 800;
	color: #4f6b72;
	border-left: 1px solid #C1DAD7;
	border-top: 1px solid #C1DAD7;
	background: #DFE8F6;
	height: 25px;
}

.firstMatrix23TdClass {
	color: #4f6b72;
	font-size: 12px;
	border-left: 1px solid #C1DAD7;
	border-top: 1px solid #C1DAD7;
	background: #DFE8F6;
	height: 25px;
}

.firstMatrixLevelTdClass {
	background: #fff;
	font-size: 11px;
	color: #4f6b72;
	width: 45px;
	height: 25px;
}
</style>
<body>
</body>
<script src="${contextPath}/area/za5.js"></script>
<script type="text/javascript">
	var nowStep = "${step}";
	var levelValueArr = '';
	var reachWay = "${reachWay}";
	var taskDesc = "${taskDesc}";
	var taskInterval = "${taskInterval}";
	var ZA5_FIRST_CUSTOM_MATRIX = "${ZA5_FIRST_CUSTOM_MATRIX}";
	var ZA5_SECOND_CUSTOM_MATRIX = "${ZA5_SECOND_CUSTOM_MATRIX}";
	var ZA5_THIRD_CUSTOM_MATRIX = "${ZA5_THIRD_CUSTOM_MATRIX}";
	var ZA5_LAST_CUSTOM_MATRIX = "${ZA5_LAST_CUSTOM_MATRIX}";

	var LEVEL_1 ; 		//中间等级1
	var LEVEL_2 ; 		//中间等级2
	var LEVEL_3 ;
	var levelCount ;		//每个项目的级别数量
	var lastItemCount ;		//最后一行有多少个项目
	var firstMatrix;		//第一主矩阵
	var matrixTable1 ;		//第一副矩阵
	var matrixTable2 ;		//第二副矩阵
	var finalMatrixTable ;	//最终矩阵
	var algCount ;	//该矩阵有多少个算法
	var algs ;
	var ids ;
	var firstItems ;	//第一级第一个项目
	var secondItems ;	//第一级第二个项目
	var thirdItems ;	//第一级第三个项目
	var checkLevelOld;	//选中的级别旧的
	var checkLevelNew;	//选中的级别新的

	function endIeStatus(){}

	Ext.onReady(za5.app);
	Ext.onReady(function(){
		levelCount = Number(document.getElementById("levelCount").value);
		lastItemCount = Number(document.getElementById("lastItemCount").value);
		firstMatrix = document.getElementById("firstMatrix");			//第一主矩阵
		matrixTable1 = document.getElementById("matrixTable1");		//第一副矩阵
		matrixTable2 = document.getElementById("matrixTable2");		//第二副矩阵
		finalMatrixTable = document.getElementById("finalMatrixTable");	//最终矩阵
		algCount = document.getElementById("algCount").value;	//该矩阵有多少个算法
		algs = new Array();
		ids = new Array();
		ids[2] = '';
		algs[2] = '';
		firstItems = new Array();	//第一级第一个项目
		secondItems = new Array();	//第一级第二个项目
		thirdItems = new Array();	//第一级第三个项目
		checkLevelOld = new Array();
		checkLevelNew = new Array();
		for(var index=0; index<algCount;index++){
			algs[index] = document.getElementById("alg"+index).value;
			ids[index] = document.getElementById("alg"+index).name;
		}
		if(nowStep == 'ZA5A'){
			Ext.get("finalMatrixTable").query('td.rowLevel')[0].style.backgroundColor = '#00ff99';
		}else{
			Ext.get("finalMatrixTable").query('td.rowLevel')[1].style.backgroundColor = '#00ff99';
		}
		//从数据库抓取选中的级别并给级别染色
		Ext.Ajax.request({
			url : "${contextPath}/area/za5/loadLevel.do?step="+nowStep,
			method : "POST",
			async :  false,
			params :{
				zaId :zaId
			},
			callback : function(options, success, response) {
				// 获取响应的json字符串
				var responseArray = Ext.util.JSON.decode(response.responseText);
				if(responseArray.levelList!="fail"){
					setLevelColor(responseArray.levelList,responseArray.level_1,responseArray.level_2,responseArray.level_3);
					checkLevelOld = responseArray.levelList;
					levelValueArr = responseArray.levelList;
				}
			}
		});		
		document.getElementById("endIeStatus").click();
		window.status='OK';
	});

	function setLevelColor(levelList,level1,level2,level3){
		LEVEL_1=level1;
		LEVEL_2=level2;
		LEVEL_3=level3;
		for(var i =1;i<levelList.length+1;i++){
			document.getElementById("level_"+levelList[i-1]+"td_"+i).style.backgroundColor='#00ff99';
		}
		dayTable(matrixTable1,level1,1);
		dayTable(matrixTable1,level2,0);
		var temp1 = dyeTd(matrixTable1);
		if(level3 == undefined){		//不存在第二副矩阵
			var temp2 = dyeTd(matrixTable1);
			dayTable(finalMatrixTable,temp2,0);
			dyeTd(finalMatrixTable);
		}else{
			dayTable(matrixTable2,temp1,1);
			dayTable(matrixTable2,level3,0);
			var temp2 = dyeTd(matrixTable2);
			dayTable(finalMatrixTable,temp2,0);
			dyeTd(finalMatrixTable);
		}
	}

	//选中某个级别触发的事件
	function checkedLevel(tr,td){		//i表示行，j表示列
		for(var i = 1;i<levelCount+1;i++){
		document.getElementById("level_"+i+"td_"+td).style.backgroundColor='';
		}
		document.getElementById("level_"+tr+"td_"+td).style.backgroundColor='#00ff99';
		//判断是否存在checkLevelOld,注意：第一次进入该方法，之后点击不能进入。否则每次点击的数组都会呗覆盖，解决方法第一次进入之后把数组赋值给新的数组，之后删除旧的数组
		if(checkLevelOld.length!=0){	
			for(var i =1;i<checkLevelOld.length+1;i++){
				var tempAbbr = document.getElementById("level_1td_"+i).abbr;
				for(var j=0;j<ids.length;j++){
					if(ids[j]==tempAbbr){		//判断选中的级别属于哪一级节点下面的
						if(j==0){
							firstItems[i-1] = checkLevelOld[i-1];
						}else if(j==1){
							secondItems[i-1] = checkLevelOld[i-1];
						}else if(j==2){
							thirdItems[i-1] = checkLevelOld[i-1];
						}				
					}
				}
			}
			checkLevelOld.length=0;		//通过设置数组的长度来实现数组元素的删除
		}
		var tempAbbr = document.getElementById("level_"+tr+"td_"+td).abbr;	//级别所属一级项目的ID
		for(var i=0;i<ids.length;i++){
			if(ids[i]==tempAbbr){		//判断选中的级别属于那个一级节点下面的
				if(i==0){
					firstItems[td-1] = tr;
				}else if(i==1){
					secondItems[td-1] = tr;
				}else if(i==2){
					thirdItems[td-1] = tr;
				}				
			}
		}
		//数组中去除空值--方法位于array.js中
		if(fiterArr(firstItems).length == Number(firstMatrix.rows[0].cells[1].abbr)){	//判断一级节点下面的所有叶子节点是否都已经选中
			dayTable(matrixTable1,arrResult(firstItems,algs[0]),1);
			LEVEL_1 = arrResult(firstItems,algs[0]);
		}
		if(fiterArr(secondItems).length == Number(firstMatrix.rows[0].cells[2].abbr)){
			dayTable(matrixTable1,arrResult(secondItems,algs[1]),0);
			LEVEL_2 = arrResult(secondItems,algs[1]);
		}
		if(thirdItems.length != 0){
			if(fiterArr(thirdItems).length == Number(firstMatrix.rows[0].cells[3].abbr)){
				dayTable(matrixTable2,arrResult(thirdItems,algs[2]),0);
				LEVEL_3 = arrResult(thirdItems,algs[2]);
			}
		}
		dyeTd(matrixTable1);
		if(dyeTd(matrixTable1)!=false){
			if(firstMatrix.rows[0].cells.length == 3){
				dayTable(finalMatrixTable,dyeTd(matrixTable1),0);
			}else{
				dayTable(matrixTable2,dyeTd(matrixTable1),1);
				if(dyeTd(matrixTable2)!=false){
				dayTable(finalMatrixTable,dyeTd(matrixTable2),0);
				}
			}
				//给任务间隔文本框赋值
				if(dyeTd(finalMatrixTable)){
				document.getElementById("taskTime").value = dyeTd(finalMatrixTable);
				}
		}
	}

	function dayTable(divFlg,flg,row){
		if(row == 1){	//给行染色
			var temp = Ext.get(divFlg).query('td.rowLevel');
				for(var i=0 ;i<temp.length;i++){
					if(temp[i].innerHTML == "<b>"+flg+"</b>" || temp[i].innerHTML == "<B>"+flg+"</B>"){
						//IE下是"<B>",火狐下面是"<b>"
						temp[i].style.backgroundColor='#00ff99';
					}else{
						temp[i].style.backgroundColor='#DFE8F6';
					}
				}
		}else{
			var temp = Ext.get(divFlg).query('td.cellLevel');
				for(var i=0 ;i<temp.length;i++){
					if(temp[i].innerHTML == "<b>"+flg+"</b>" || temp[i].innerHTML == "<B>"+flg+"</B>"){
						temp[i].style.backgroundColor='#00ff99';
					}else{
						temp[i].style.backgroundColor='#DFE8F6';
					}
				}
		}
	}
	
	//给矩阵渲染行和列的交集
	function dyeTd(divFlg){
		var row = -1;
		var cell = -1;
		//循环判断左边哪个被选中
		var temp1 = Ext.get(divFlg).query('td.rowLevel');
			for(var i=0 ;i<temp1.length;i++){
				if(temp1[i].style.backgroundColor=='#00ff99'||temp1[i].style.backgroundColor=='rgb(0, 255, 153)'){
					row =i;
				}
			}
		//循环判断上边哪个被选中
		var temp2 = Ext.get(divFlg).query('td.cellLevel');
			for(var i=0 ;i<temp2.length;i++){
				if(temp2[i].style.backgroundColor=='#00ff99' || temp2[i].style.backgroundColor=='rgb(0, 255, 153)'){
					cell =i;
				}
			}
		//先清空
		var temp3 = Ext.get(divFlg).query('td.cell');
		for(var i=0 ;i<temp3.length;i++){
			temp3[i].style.backgroundColor='';
		}
		if(row !=-1 && cell != -1){
			temp3[row*temp2.length+cell].style.backgroundColor='#00ff99';
			return temp3[row*temp2.length+cell].innerHTML;
		}
		return false;
	}	
</script>