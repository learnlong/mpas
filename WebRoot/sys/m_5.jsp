<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/sys_headerstep.jsp" %>
<script src="${contextPath}/sys/m_5.js"></script>
<script src="${contextPath}/common/coordinationOfSign.js"></script>
<script type="text/javascript">
	var msiId = '${msiId}';
	var mEff ='${msiEff}';
	var isCreateUser = '${isCreateUser}';
	var rId='';
	var nowRecord;
	//自动获取选中行的记录
	 function getRecord(){
	 	 setTimeout(function(){
			 if(typeof(nowRecord)!='undefined'){
				  seeCoordination(rId);
			 }else{
				  setTimeout(function(){
					  getRecord();
				  },50);
			  }
	  	 },50)
	  }
	
	function tempRecord(){
		setTimeout(function(){
			if(typeof(nowRecord)!='undefined'){
				 modifyRecord(); 
			}else{
				  setTimeout(function(){
					  tempRecord();
				  },50);
		   }
		 },50)
	}
	
	 function modifyRecord(){
		var rec = nowRecord;
		var taskDesc = rec.get("taskDesc");
		var area = rec.get("ownArea"); 
		var id=rec.get("taskId");
		var type ="SYS_TO_Z";
		var content = rec.get("taskCode")+"--"+rec.get("taskType")+"--"+taskDesc;
		if(id !=""&& id != null){
			if(rec.get("needTransfer") == 1){
				if(area!="" && area != null) {
					coordination(id,type,content,area);
				}
			}else{
				alert('只有转区域任务才能填写和修改协调单');
			}
		}else{ 
			alert('在确定转区域之前，请先保存当前数据！！');
		}
  	}
	
	Ext.apply(m5.app, {
		 loadM5 : "${contextPath}/sys/m5/loadM5.do",
		 saveM5 : "${contextPath}/sys/m5/saveM5.do",
		 deleteM5HeBing : "${contextPath}/sys/m5/deleteM5HeBing.do"
	});

	Ext.onReady(m5.app.init,m5.app);
</script>