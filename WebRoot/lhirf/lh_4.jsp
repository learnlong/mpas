<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/lh_headerstep.jsp"%>

<!-- 导入js文件 -->
<script type="text/javascript">
	var remark = '${remark}';
    var  remarkContent = remark ;
 	var safeReason ='${safeReason}';
 	var  safeRContent = safeReason ;
</script>

<script src="${contextPath}/lhirf/lh_4.js"></script>
<script src="${contextPath}/common/array.js"></script>
<style type="text/css">
#EDADtable td {
	border-right: 1px solid #C1DAD7;
	border-bottom: 1px solid #C1DAD7;
	font-size: 12px;
	text-align: center;
	padding: 4px 4px 4px 4px;
	color: black;
	cursor: default;
}

#EDADtable table {
	border-left: 1px solid #C1DAD7;
	border-top: 1px solid #C1DAD7;
}
</style>

<head>

	<title></title>
</head>

<body onload="inittableShow()">
	<div id="edad">
	</div>
</body>
<script type="text/javascript">
     oldhelpValues = '';
     oldSafeValues = '';
    var hsiId ='${hsiId}'
  	var EDADTable = "${LH4_CUSTOM_MATRIX}";

  	///得到自定义表中的显示备注信息
  	var lh4Display = '${lh4Display}';
  	
  	var needLhTask = '${needLhTask}';
  	var isSafe = '${isSafe}';
  	
  	   	///文本域 备注信息问题  
	Ext.apply(lh4.app);
	Ext.onReady(lh4.app.init,lh4.app);	
</script>
<script type="text/javascript">
  	var EDArr = new Array();
  	var ADArr = new Array();
  	var levelOld = new Array();			//数据库中的level数组
  	var isNoSaveLh4 = 0 ;			//数据库中的level数组
   	var initAryAd = Ext.util.JSON.encode(ADArr);
   	var tempEDResult = 0;	//存放ED的结果
	var tempADResult = 0;	//存放AD的结果
  	//选中的等级,注：flag：区别ED、AD（1表示ED，2表示AD）；tr:表示在等级矩阵中是第几行，td则表示是的第几列
	function checkedLevel(flag,tr,td){	
	  	var levelCount = Number(document.getElementById("levelCount").value);		//从隐藏文本框中获取到级别的数量，用于遍历级别选择是否完整。
	  	var EDCount = Number(document.getElementById("EDCount").value);	
	  	var ADCount = Number(document.getElementById("ADCount").value);
	  	var EDADALG = document.getElementById("LH4ALG").value;		//获取LH4的算法
  		//对选中的级别进行染色
  		for(var i=1;i<levelCount+1;i++){
  			document.getElementById(flag+"tr_"+i+"td_"+td).style.backgroundColor='';
  		}
  			document.getElementById(flag+"tr_"+tr+"td_"+td).style.backgroundColor = '#00ff99';
  		if(flag == 1){
  			EDArr=iterMatrix(flag,2*EDCount,levelCount,EDArr);
  			var EDALG = document.getElementById("EDALG").value;			//获取ED的算法
  			var EDCount = Number(document.getElementById("EDCount").value);	
  			//判断ED中ER是否全部被选中
  			var tempArr = new Array();
  			tempArr = getER_Arr(EDArr);
  			tempArr = fiterArr(tempArr);		//数组中去空值,如: 1,2,,3  计算后为:1,2,3
  			if(tempArr.length == EDCount){
  				var newArr = new Array();
  				newArr=tempArr;
  				tempEDResult = arrResult(newArr,EDALG);
	  			document.getElementById("EDResult").innerHTML="<span>"+tempEDResult+"</span>";	//设置ED敏感度等级
  			}
  		}else if(flag == 2){
  			ADArr=iterMatrix(flag,2*ADCount,levelCount,ADArr);
  			var ADALG = document.getElementById("ADALG").value;			//获取AD的算法
		  	var ADCount = Number(document.getElementById("ADCount").value);
		  	//判断ED中ER是否全部被选中
  			var tempArr = new Array();
  			tempArr = getER_Arr(ADArr);
  			tempArr = fiterArr(tempArr);		//数组中去空值,如: 1,2,,3  计算后为:1,2,3
  			if(tempArr.length == ADCount){
  				var newArr = new Array();
  				newArr=tempArr;
  				tempADResult = arrResult(newArr,ADALG);
  				document.getElementById("ADResult").innerHTML="<span>"+tempADResult+"</span>";	//设置AD敏感度等级
  			}
  		}
  		if(tempADResult != 0 && tempEDResult !=0){
  			var tempEDADArr = new Array(tempEDResult,tempADResult);
  			var tempEDADResult = arrResult(tempEDADArr,EDADALG);
  			document.getElementById("EDADResult").innerHTML="<span>"+tempEDADResult+"</span>";	//设置ED/AD敏感度等级
  		}
  	}
  	

	function getER_Arr(arr){
		var tempArr= new Array();
		for(var j =0 ;j<arr.length ;j++){
  				if(j%2 == 0){
  					tempArr[j/2] =arr[j]; 
  				}
  			}
  		return tempArr;
	}
  	/*
  	*	遍历数组检测已选中的td
  	*/
  	function iterMatrix(flag,tdCount,trCount,arr){
		for(var j =1 ;j<tdCount+1;j++){
  			for(var k =1;k<trCount+1;k++){
  				var color = document.getElementById(flag+"tr_"+k+"td_"+j).style.backgroundColor;
  				if(color=='#00ff99' || color == 'rgb(0, 255, 153)'){
  					arr[j-1] = Number(k);
  				}
  			}
  		}
		return arr;  	
  	}
 	function checkFull(){
	  	var EDCount = Number(document.getElementById("EDCount").value);	
	  	var ADCount = Number(document.getElementById("ADCount").value);
	    if(fiterArr(EDArr).length+fiterArr(ADArr).length!=  2*(EDCount+ADCount)){
		    return false;
		}
		return true;
  	}
  	function initLevel(){
  				Ext.Ajax.request({
				url : "${contextPath}/lhirf/lh_4/loadLh4Level.do",
				method : "POST",
				params :{
				hsiId : '${hsiId}'
				},
				callback : function(options, success, response) {
					// 获取响应的json字符串
					var responseArray = Ext.util.JSON
							.decode(response.responseText);
							if(responseArray.levelList!="fail"){
							setLevelColor(responseArray.levelList);
							levelOld = responseArray.levelList;
							}else{
							   isNoSaveLh4 = 1;///此时没有保存 lh4 进入此画面
							}
				}
			});
  	}
  	
  	function setLevelColor(levelArr){
  		//给级别矩阵染色
  		var levelCount = Number(document.getElementById("levelCount").value);		//从隐藏文本框中获取到级别的数量，用于遍历级别选择是否完整。
	  	var EDCount = Number(document.getElementById("EDCount").value);	
	  	var ADCount = Number(document.getElementById("ADCount").value);
	  	//ED级别
  		for(var j =1 ;j<2*EDCount+1;j++){
  			for(var k =1;k<levelCount+1;k++){
  				if(k == levelArr[j-1]){
  					var value = document.getElementById("1tr_"+k+"td_"+j).style.backgroundColor='#00ff99';
  				}

  			}
  		}
  		//AD级别
  		for(var j =1 ;j<2*ADCount+1;j++){
  			for(var k =1;k<levelCount+1;k++){
  				if(k == levelArr[2*EDCount+j-1]){
  					var value = document.getElementById("2tr_"+k+"td_"+j).style.backgroundColor='#00ff99';
  				}

  			}
  		}
  			EDArr=iterMatrix(1,2*EDCount,levelCount,EDArr);
  			var EDALG = document.getElementById("EDALG").value;			//获取ED的算法
  			document.getElementById("EDResult").innerHTML="<span>"+arrResult(getER_Arr(EDArr),EDALG)+"</span>";	//设置ED敏感度等级
  			ADArr=iterMatrix(2,2*ADCount,levelCount,ADArr);
  			var ADALG = document.getElementById("ADALG").value;			//获取AD的算法
  			document.getElementById("ADResult").innerHTML="<span>"+arrResult(getER_Arr(ADArr),ADALG)+"</span>";	//设置AD敏感度等级

  	}

  	 	//初始化页面
	function initTable(){
	  	var EDAD_Result = '${resultLh4}';
  		document.getElementById("EDADResult").innerHTML="<span>"+EDAD_Result+"</span>";	//设置EDAD敏感度等级
	      Ext.getCmp('twoChoose').setValue(needLhTask);
	       document.getElementById('myRemark').value = remark ;
	       document.getElementById('safeReason').value = safeReason;
	      changeSafe(isSafe);
	    if(needLhTask !=1 ){//当不需要维修任务时, 左侧是否安全其说明都为无效状态
			    Ext.getCmp('safeReason').disable();
				 Ext.getCmp('rad').disable();
	    }
	}
	 setTimeout("initTable()",200);
	 function inittableShow(){
		 setTimeout("initLevel()",100);
	 }
  	//
  	//初始化是否 有任务; 安全按钮
	function changeSafe(value){		
		Ext.getCmp('rad').eachItem(function(item){ 		
		    item.setValue(item.inputValue == value); 
		});
	}
</script>