<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.rskytech.pojo.ComArea"%>
<%@page import="org.springframework.web.util.JavaScriptUtils"%>
<%@ include file="/common/has_ext_top.jsp"%>
<style type="text/css">
#header td {
	font-size: 12px;
	padding: 0px 0px 0px 0px;
	color: #4f6b72;
}

#headerstep td {
	padding: 6px 6px 3px 6px;
}
</style>
<body>
	<div id="headerStepDiv">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" id="header">
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td><b><font>区域编号:</font>${area.areaCode}</b></td>
							<td><b><font>区域名称:</font></b><b id="areaName"></b></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" id="headerstep">
			<tr>
				<td width="1"></td>
				<td id="step0" onclick="nextOrSave(0);">区域数据</td>
				<td id="step1" onclick="nextOrSave(1);">区域描述</td>
				<td id="step2" onclick="nextOrSave(2);">是否标准</td>
				<td id="step3" onclick="nextOrSave(3);">增强任务</td>
				<td id="step4" onclick="nextOrSave(4);">增强周期</td>
				<td id="step5" onclick="nextOrSave(5);">标准内部</td>
				<td id="step6" onclick="nextOrSave(6);">标准外部</td>
				<td id="step7" onclick="nextOrSave(7);">任务汇总</td>
				<td id="step8" onclick="nextOrSave(8);">候选合并</td>
				<td id="step9" onclick="nextOrSave(9);">转入大纲</td>
				<td id="step10" onclick="nextOrSave(10);">转ATA20</td>
				<td class="x-rc-form-table-td-sysnext" onclick="nextOrSave(null);" id="next">下一步</td>
				<td></td>
			</tr>
		</table>
	</div>
</body>

<script type="text/javascript">
	var areaId = '${areaId}';
	var areaName =  '<%=JavaScriptUtils.javaScriptEscape(((ComArea)request.getAttribute("area")).getAreaName())%>';
	var areaStatus = '${zaStep.areaStatus}';
	var zaId = '${zaId}';
	var isMaintain = '${isMaintain}';
	var analysisFlag = false;
	//判断是否有修改权限
	if (isMaintain == '1' && areaStatus != 'APPROVED'){
		analysisFlag = true;
	} else {
		analysisFlag = false;
	}
		
	if (areaName.length > 20){
		document.getElementById('areaName').innerHTML = areaName.substr(0, 20) + "...";
	} else {
		document.getElementById('areaName').innerHTML = areaName;
	}
	
	var step = new Array();
	step[0] = '${zaStep.za1}';
	step[1] = '${zaStep.za2}';
	step[2] = '${zaStep.za41}';
	step[3] = '${zaStep.za42}';
	step[4] = '${zaStep.za43}';
	step[5] = '${zaStep.za5a}';
	step[6] = '${zaStep.za5b}';
	step[7] = '${zaStep.za6}';
	step[8] = '${zaStep.za7}';
	step[9] = '${zaStep.za8}';
	step[10] = '${zaStep.za9}';
	var pageName = '${zaStep.pageName}';

	function tdClass(){
		for (var i = 0; i < step.length; i++){
			if (step[i] == 1) {
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-complete";
			}
			if (step[i] == 2) {
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-maintain";
			}
			if (step[i] == 0) {
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-disable";
			}
			if (step[i] == 3) {
				document.getElementById("step" + i).style.display = 'none';
			}
		}
		if (pageName == 'ZA1'){
			document.getElementById("step0").innerHTML = "<font color='red'><b>区域数据</b></font>";
		}
		if (pageName == 'ZA2'){
			document.getElementById("step1").innerHTML = "<font color='red'><b>区域描述</b></font>";
		}
		if (pageName == 'ZA41'){
			document.getElementById("step2").innerHTML = "<font color='red'><b>是否标准</b></font>";
		}
		if (pageName == 'ZA42'){
			document.getElementById("step3").innerHTML = "<font color='red'><b>增强任务</b></font>";
		}
		if (pageName == 'ZA43'){
			document.getElementById("step4").innerHTML = "<font color='red'><b>增强周期</b></font>";
		}
		if (pageName == 'ZA5A'){
			document.getElementById("step5").innerHTML = "<font color='red'><b>标准内部</b></font>";
		}
		if (pageName == 'ZA5B'){
			document.getElementById("step6").innerHTML = "<font color='red'><b>标准外部</b></font>";
		}
		if (pageName == 'ZA6'){
			document.getElementById("step7").innerHTML = "<font color='red'><b>任务汇总</b></font>";
		}
		if (pageName == 'ZA7'){
			document.getElementById("step8").innerHTML = "<font color='red'><b>候选合并</b></font>";
		}
		if (pageName == 'ZA8'){
			document.getElementById("step9").innerHTML = "<font color='red'><b>转入大纲</b></font>";
		}
		if (pageName == 'ZA9'){
			document.getElementById("step10").innerHTML = "<font color='red'><b>转ATA20</b></font>";
		}
	}
	tdClass();

	function goNext(value){
		var x = 0;
		for (var i = value; i < step.length; i++){
			if (step[i] == 1) {
				successNext(i);
				x = 1;
				return true;
			}
			if (step[i] == 2) {
				successNext(i);
				x = 1;
				return true;
			}
			if (step[i] == 0) {
				alert('请先完成之前的步骤');
				x = 1;
				return false;
			}
			if (step[i] == 3) {
				continue;
			}
		}
		if (x == 0){
			alert('没有可跳转的页面');
			return false;
		}
	}
	
	function successNext(value){
		var waitMsg = new Ext.LoadMask(Ext.getBody(), {
			msg : commonality_waitMsg,
			removeMask : true// 完成后移除
		});
		waitMsg.show();
		
		if (value == 0){
			location.href = '${contextPath}/area/za1/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		} else if (value == 1){
			location.href = '${contextPath}/area/za2/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		} else if (value == 2){
			location.href = '${contextPath}/area/za41/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		} else if (value == 3){
			location.href = '${contextPath}/area/za42/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		} else if (value == 4){
			location.href = '${contextPath}/area/za43/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		} else if (value == 5){
			location.href = '${contextPath}/area/za5/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain + '&step=ZA5A';
		} else if (value == 6){
			location.href = '${contextPath}/area/za5/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain + '&step=ZA5B';
		} else if (value == 7){
			location.href = '${contextPath}/area/za6/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		} else if (value == 8){
			location.href = '${contextPath}/area/za7/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		} else if (value == 9){
			location.href = '${contextPath}/area/za8/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		} else if (value == 10){
			location.href = '${contextPath}/area/za9/init.do?areaId=' + areaId + '&zaId=' + zaId + '&isMaintain=' + isMaintain;
		}
	}	
</script>