<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
		<title>MpasIndex</title>
<link rel="stylesheet" href="${contextPath}/css/homePage.css" type="text/css" />
<script type="text/javascript" src="${contextPath }/newIndex.js"></script>
<script type="text/javascript">
	//四大分析
	var zonalCode = '<%=ComacConstants.ZONAL_CODE%>';
	var zonalCodeCn = '<%=ComacConstants.ZONAL_SHOW%>';
	var lhrifCode = '<%=ComacConstants.LHIRF_CODE%>';
	var lhrifCodeCn = '<%=ComacConstants.LHIRF_SHOW%>';
	var structureCode = '<%=ComacConstants.STRUCTURE_CODE%>';
	var structureCodeCn = '<%=ComacConstants.STRUCTURE_SHOW%>';
	var systemCode = '<%=ComacConstants.SYSTEM_CODE%>';
	var systemCodeCn = '<%=ComacConstants.SYSTEM_SHOW%>';
	//流程状态
	var conWaitCheckCode = '<%=ComacConstants.PROCESS_STATUS_WAIT_CHECK%>';
	var conWaitCheckName = '<%=ComacConstants.PROCESS_STATUS_WAIT_CHECK_SHOW%>';
	var conCheckingCode = '<%=ComacConstants.PROCESS_STATUS_CHECKING%>';
	var conCheckingName = '<%=ComacConstants.PROCESS_STATUS_CHECKING_SHOW%>';
	var conFinishCheckCode = '<%=ComacConstants.PROCESS_STATUS_FINISH_CHECK%>';
	var conFinishCheckName = '<%=ComacConstants.PROCESS_STATUS_FINISH_CHECK_SHOW%>';
	var conCancelCheckCode = '<%=ComacConstants.PROCESS_STATUS_CANCEL_CHECK%>';
	var conCancelCheckName = '<%=ComacConstants.PROCESS_STATUS_CANCEL_CHECK_SHOW%>';
	//是否通过
	var conIsOkNullCode = '<%=ComacConstants.PROCESS_CHECK_STATUS_NULL%>';
	var conIsOkNullName = '<%=ComacConstants.PROCESS_CHECK_STATUS_NULL_SHOW%>';
	var conIsOkYesCode = '<%=ComacConstants.PROCESS_CHECK_STATUS_YES%>';
	var conIsOkYesName = '<%=ComacConstants.PROCESS_CHECK_STATUS_YES_SHOW%>';
	var conIsOkNoCode = '<%=ComacConstants.PROCESS_CHECK_STATUS_NO%>';
	var conIsOkNoName = '<%=ComacConstants.PROCESS_CHECK_STATUS_NO_SHOW%>';
	var conIsOkCancelCode = '<%=ComacConstants.PROCESS_CHECK_STATUS_CANCEL%>';
	var conIsOkCancelName = '<%=ComacConstants.PROCESS_CHECK_STATUS_CANCEL_SHOW%>';
</script>
		<script type="text/javascript">
document.write("<div id=\"ts\" style=\"position:absolute;background-color:#FFFFE6;font-size: 11px;padding: 3px; border: 1px solid #FFCC99;display:none\"></div>");
function mmove(o, s) {
	var evt = arguments.callee.caller.arguments[0]|| window.event;
	var x = evt.clientX + 1;
	var y = evt.clientY + (-30);
	document.getElementById("ts").style.left = x + "px";
	document.getElementById("ts").style.top = y + "px";
	if (s == "" || typeof (s) == "undefined") {
		document.getElementById("ts").innerHTML = o.innerHTML;
	} else {
		document.getElementById("ts").innerHTML = s
	}
	document.getElementById("ts").style.display = "";
}
 function mout() {
	document.getElementById("ts").style.display = "none";
}
function lookTask() {
	/**
	var winDai = new Ext.Window(
			{
				width : 600,
				height : 400,
				title : index_daiban,
				resizable : false,
				layout : 'fit',
				bodyStyle : 'padding: 5px;',
				items : [ {
					region : 'center',
					id : 'rightcenter',
					html : '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="' + contextPath + '/homepage/init.do"> </iframe>'
				} ],
				modal : true
			});
	winDai.show();
	**/
}
/**
Ext.onReady(function() {
	setTimeout(loadWaitTask, 10);
});

function loadWaitTask() {
	document.getElementById('waitTask_ifrm').src = "${contextPath}/homepage/initWaitTask.do";
}**/
//得到的tbMenu对象遍历隐藏
function fun(){
	
}
</script>
	</head>
	<body onmouseover="fun()">
		<div class="body" id="openCheckGrid">
			<div class="wale"></div>
			<div class="welcomediv" id="welcomediv">
				<div class="welcometext" style="font-family: '微软雅黑';" id="welcometext">
					欢迎使用维修大纲辅助分析系统!
				</div>
				<div class="feijidiv"></div>
			</div>
			<div class="onediv" style="">
				<!--左			-->
				<div class="leftdiv" >
					<div style="width: 100%; height: 450px;">
						<div class="machinediv"></div>
						<!--               cbr      -->
						<div class="cbrdiv" id="cbr" style="">
							<div class="cbr1">
								<div class="cbrimg" id="cankao" onMouseMove="mmove(this,'CBR参考')" onMouseOut="mout();"></div>
								<div class="cbrword" id="cbr1" style="font-family: 'Time New Rom';">CBR参考</div>
							</div>
						</div>
						<!--              mpd        -->
						<div class="mpddiv" id="mpd">
							<div class="mpd1">
								<div class="mpdimg" onMouseMove="mmove(this,'MPD')" onMouseOut="mout();"></div>
								<div class="mpdword" style="font-family: 'Time New Rom';">MPD</div>
							</div>
						</div>
						<div class="blowdiv" >
							<div class="kekaoandmrbdiv" >
								<div class="kekaodiv">
									<div class="kekao1" id="kekao">
										<div class="kekao2">
											<div class="kekaoimg" align="center" onMouseMove="mmove(this,'可靠性分析')" onMouseOut="mout();"></div>
											<div class="kekaoword" id="kekao1" style="font-family: 'Time New Rom';">
												可靠性分析
											</div>
										</div>
									</div>
								</div>
								<div class="mrbdiv" id="mrb1">
									<div class="mrb2">
										<div class="mrb3">
											<div class="mrbimg" onMouseMove="mmove(this,'MRB')" onMouseOut="mout();"></div>
											<div class="mrbword" style="font-family: 'Time New Rom';">
												MRB
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="msgandknowledgediv" id="msgandku" >
								<div class="msgdiv">
									<div class="msgimg" onMouseMove="mmove(this,'MSG-3分析')" onMouseOut="mout();"></div>
									<div class="msgword" id="msg1" style="font-family: 'Time New Rom';">
										MSG-3分析
									</div>
								</div>
								<div class="knowledgediv">
									<div class="knowledgeimg" onmousemove="mmove(this,'知识库')" onmouseout="mout();"></div>
									<div class="msgword" id="knowledge1" style="font-family: 'Time New Rom';">
										知识库
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!--			   右-->
				<div class="rightdiv" style="border:solid 2px #99bbe8;<c:if test='${SESSION_USER_KEY.professionEngineer == false && SESSION_USER_KEY.professionAnalysis == false && SESSION_USER_KEY.admin == false}'>display:none</c:if>">
					<div class="dianji" id="dianji" >
						<font id="dianji1">待办任务</font>
					</div>
					<div class="circlediv">
						<iframe name="waitTask_ifrm" id="waitTask_ifrm" frameborder="0" scrolling="no" allowTransparency="true" style="width: 100%; height: 100%;" src="${contextPath}/homepage/initWaitTask.do"></iframe>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
