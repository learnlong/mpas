<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/top.jsp"%>
<link rel="stylesheet" href="${contextPath}/css/login.css"	type="text/css" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
	     <title>维修大纲辅助分析系统</title>
	</head>
	<body onload="check()">
		<script type="text/javascript">
window.moveTo(0, 0);
if (document.all) {
	top.window.resizeTo(screen.availWidth, screen.availHeight);
} else if (document.layers || document.getElementById) {
	if (top.window.outerHeight < screen.availHeight
			|| top.window.outerWidth < screen.availWidth) {
		top.window.outerHeight = screen.availHeight;
		top.window.outerWidth = screen.availWidth;
	}
}

function check() {
	document.getElementById('userCode').focus();
}

if (document.addEventListener) {
	document.addEventListener("keypress", fireFoxHandler, true);
} else {
	document.attachEvent("onkeypress", ieHandler);
}

function fireFoxHandler(evt) {
	if (evt.keyCode == 13) {
		submitform();
	}
}

function ieHandler(evt) {
	if (evt.keyCode == 13) {
		submitform();
	}
}

function submitform() {
	if (document.getElementById("userCode").value == ""
			|| document.getElementById("password").value == "") {		
		alert("请输入用户名或密码!");
		return false;		
	} else {
		return true;
	}
}

</script>
		<div align="center" id="__01">
			<div class="formdiv">
				<!--第一个div-->
				<div class="diyidiv">
					<div class="biaozhi"></div>
					<div class="welcometext" id="welcometext"
						style="font-family: '微软雅黑'">
						维 修 大 纲 辅 助 分 析 系 统
					</div>
				</div>
				<div class="dierdiv">
					<div class="feijidiv">
						<div class="feiji"></div>
						<div class="shu"></div>
					</div>
					<div class="rightdiv">
						<form id="loginForm" class="rightdiv" name="loginForm"
							method="post" action="portal/login.do"
							onsubmit="return submitform();">											
							<div style="margin-top: 10px; float: left;">
								<b style="width: 360px;	height: 26px;line-height: 26px;	text-align: left;float: left;"><span
									style="color: #000000; font-size: 15px;" id="username">用户名&nbsp;&nbsp;&nbsp;&nbsp;：</span>
									<input style="width: 140px;" type="text" name="userCode"
										id="userCode" /> </b>

								<b style="width: 360px;	height: 26px;line-height: 26px;	text-align: left;float: left;margin-top: 10px;"><span
									style="color: #000000; font-size: 15px;" id="password1">密码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：&nbsp;</span>
									<input style="width: 140px;" type="password" name="password"
										id="password" /> </b>
								<p >
									<input class="submitdiv" id="login" name="Submit" type="submit"
										value="登 录"/>
									<input class="resetdiv" id="reset" name="Submit" type="button"
										onclick="resetValue()" value="重 置"/>
								</p>
							</div>
						</form>
					</div>
				</div>
				<!--第二个div结束         -->
			</div>
		</div>
	<script type="text/javascript">
	var msg = "${USERMSG}";
	
	if (msg == 1) {
		alert("该用户名或密码错误,请联系管理员!");	
	}

	function resetValue(){
		document.getElementById("userCode").value='';
		document.getElementById("password").value='';	
	}
	</script>
	
</body>
</html>
