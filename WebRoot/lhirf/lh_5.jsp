<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/lh_headerstep.jsp" %>

<style type="text/css" >   
    .one {color:red;}
    .two {font-size: 12px;}
    .red {background-color : red;}  
    .white {background-color : #ffffff;} 
    .x-grid-record-red5{color:red;};
   #showTable td {		
		padding: 6px 6px 3px 6px;		
	}
	
.processCellRed { 
	background: #FFFF00; 
	color:#000000;
	}
	
</style>
<script type="text/javascript">
var jsonGVI=[];
var jsonDET=[];
var jsonFNC=[];
var jsonDIS=[];
var arryDelCoo = new Array();
var rId='';
var tempRecords;
//自动选中行的记录
  function getRecord(){
 	 setTimeout(function(){
		 if(typeof(tempRecords)!='undefined'){
			  seeCoordination(rId);
		 }else{
			  setTimeout(function(){
				  getRecord();
			  },50);
		  }
  	 },50);
  }
  
  function tempRecord(){
		setTimeout(function(){
			if(typeof(tempRecords)!='undefined'){
				 modifyRecord(); 
			}else{
			  setTimeout(function(){
				  tempRecord();
			  },50);
		   }
		 },50);
	}

	 function modifyRecord(){
		var rec = tempRecords;
		var	taskDesc = rec.get("taskDesc");
		var area = rec.get("ownArea"); 
		var id=rec.get("taskId");
		var type ="LH_TO_Z";
		var content = rec.get("taskCode")+"--"+rec.get("taskType")+"--"+taskDesc;
		var taskType = rec.get("taskType");
		var taskCode = rec.get("taskCode");
		if(isMaintain!='1'){
			alert('您没有修改权限！');
			return;
		}
		if(id !=""&& id != null){
			if(rec.get("needTransfer") == 1){
				if(area!="" && area != null) {
					coordination2(id,type,content,area,taskCode,taskType);
				}
			 }else{
				alert('只有转区域任务才能填写和修改协调单');
			 }
		}else{
			if(rec.get("needTransfer") == 1){
				if(area!="" && area != null) {
					coordination1(type,content,area,taskCode,taskType);
				}
			 }else{
				alert('只有转区域任务才能填写和修改协调单');
			 }
		}  
	}
</script>
	
<script type="text/javascript" src="${contextPath}/js/extjs/multicombo.js"></script>
<script src="${contextPath}/lhirf/lh_5.js"></script>
<script src="${contextPath}/common/coordinationOfSign.js"></script>
<script src="${contextPath}/lhirf/lh_5_coo.js"></script>

<!-- 导入js文件 -->

<head>
   
   <title>lh5Page</title>
  </head>
  <script type="text/javascript">
   var isSafe = '${lh4.isSafe}';
   var resultLh4 ='${lh4.result}';
   var resultLh4Value ='${resultLh4Value}';
   var hsiId = '${hsiId}';
   var  areaId = '${comArea.areaId}';
   var  lh5Id ='${lh5.lh5Id}';
    var hsiCode ='${lhHsi.hsiCode}';
   var refHsiCode='${lhHsi.refHsiCode}';
   var gviAvl='${lh5.gviAvl}';
   var gviDesc = '${gviDesc}';

    var detAvl = '${lh5.detAvl}';
   var detDesc = '${detDesc}';
   
   var fncAvl = '${lh5.fncAvl}';
   var fncDesc = '${fncDesc}';
   var disAvl = '${lh5.disAvl}';
   var disDesc = '${disDesc}';
   var needRedesign = '${lh5.needRedesign}';
   var redesignDesc = '${redesignDesc}';
   
  var  NA = '<%=ComacConstants.EMPTY%>';
	
  ///  开始  
  </script>
  
  
  <body>
    <div id="list_CusList"  >
       <table id="showTable" border ="0" style="font-size: 12px;" >
       <tr >
         <td style="color: block;width: 210px; " align="center" id="rangTing"></td>
		         <s:iterator var="ch" value="listCusList" status="status">
                   <c:set var="resultLh4" value="${lh4.result}" />
		            <c:if test="${intervalLevel == resultLh4}">
		             <td style="color: red; width: 120px;" align="center">
		                 <s:property value="intervalLevel"/>
		             </td>  
		           </c:if>
		          <c:if test="${intervalLevel != resultLh4}">
		             <td style="color: block; width: 120px ;" align="center">
		                <s:property value="intervalLevel"/>
		              </td>  
		           </c:if>
		         </s:iterator>
       </tr>
       <tr >
           <td style="color: block; width:210px;ont-size: 12px;" align="center" id="intValue"></td>
		         <s:iterator var="ch" value="listCusList" status="status">
	                 <c:set var="resultLh4" value="${lh4.result}" />
			            <c:if test="${intervalLevel == resultLh4}">
			             <td style="color: red ;width: 120px ;" align="center">
			                 <s:property value="intervalValue"/>
			             </td>  
			           </c:if>
			          <c:if test="${intervalLevel != resultLh4}">
			             <td style="color: block; width: 120px ;" align="center">
			                 <s:property value="intervalValue"/>
			             </td>  
			           </c:if>
		         </s:iterator>
       </tr>
       </table>
    
    </div>
  </body>
<script type="text/javascript">
	
      //参数 设置
  	Ext.apply(lh5.app, {
			   g_searchMsgUrl : "${contextPath}/lhirf/lh_5/searchMsglh5.do",
			    g_saveLh5Url : "${contextPath}/lhirf/lh_5/saveLh5.do" 
	});
	   //开始运行
	  Ext.onReady(lh5.app.init,lh5.app);	
</script>
  
  
 
  

