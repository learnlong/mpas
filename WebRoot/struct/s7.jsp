<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/common/struct_headerstep1.jsp"%> 
<script type="text/javascript">
	var remark='${remark}';
	var remarkId='${remarkId}';
	var ssiId='${ssiId}';
	var isMaintain=${isMaintain};
</script>
<script src="${contextPath}/struct/s7.js"></script>
<script src="${contextPath}/common/coordinationOfSign.js"></script>
<script type="text/javascript">
var rId='';
var	tempRecords;
//自动获取选中行的记录
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
	var taskDesc = rec.get("taskDesc");
	var area = rec.get("oneZone"); 
	var id=rec.get("taskId");
	var type ="STR_TO_Z";
	var content = rec.get("taskCode")+"--"+taskDesc;
	var taskCode=rec.get("taskCode");
	if(rec.get("needTransferStr") == 1){
		if(area!="" && area != null) {
			coordination(id,type,content,area);
		} else {
			alert('区域为空，不能填写协调单');
		}
	}else{
		alert('只有转区域任务才能填写和修改协调单');
	}
}

</script>
