<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.rskytech.pojo.ComUser"%>
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<link rel="stylesheet" href="${contextPath}/css/analysWelcome.css" type="text/css" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
		<!-- 获取全局变量值 -->
	<script type="text/javascript">
		var msiSelectIsHave = '${msiSelectIsHave}';
		if(msiSelectIsHave == 1){
			alert("尚未维护MSI或该ATA不是最高可管理层，不能进行分析");
		}
		var welcomeIsHaveS45 = '${welcomeIsHaveS45}';
		if( welcomeIsHaveS45 == 2){
		  alert("请先维护自定义 矩阵s4, s5");//维护自定义 矩阵s4, s5
		}
		var isSsiChoosed = '${isSsiChoosed}';
		if(isSsiChoosed){
			alert("尚未维护SSI，不能进行分析");
		}
		
	</script>
    <base href="<%=basePath%>">    
    <title>My JSP 'sysIndex.jsp' starting page</title>    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
	 <% 
	String s = "zh";
	%>
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
   Ext.onReady(function() {
	     setInterval(changefontsize, 100);		
	});
	function changefontsize() {
		var width = document.body.clientWidth;
		var height = document.body.clientHeight;
	if (parseInt(width) < 810) {
		document.getElementById("shiyong").style.fontSize = "28px";
		document.getElementById("welcome").style.fontSize = "26px";
		document.getElementById("feiji").style.width = "74%";
		document.getElementById("zuo").style.width = "50%";
		document.getElementById("you").style.width = "49%";
	}
	if (parseInt(width) > 1010 && parseInt(width) < 1280) {
<%--		document.getElementById("cbrcankao").style.width="55%";--%>
		document.getElementById("mpd").style.width="60%";
		document.getElementById("kekao").style.width="78%";
        document.getElementById("youkuang").style.width="76%";
        document.getElementById("msg").style.width="37%";
        document.getElementById("mrbandknowledge").style.width="47%";
	}
}
 </script>
 </head>
  <body>
     <div class="body">
        <div class="zuodiv" id="zuo">
            <div class="welcomediv">
              <div class="zong">
                <div class="shiyongdiv">
                   <div class="shiyong" style="font-family: '微软雅黑';" id="shiyong"><font id="huanying">欢迎使用</font></div>
                </div>
                    <div class="feijidiv">              
                        <div class="feiji" id="feiji"></div>
                    </div>
               </div>
              <div class="welcome" style="font-family: '微软雅黑';" id="welcome"><font id="yitihua">维修大纲辅助分析系统！</font></div>
<!--              <div class="logintime" id="logintime">最后访问时间：<span id="timespan"	style="color: #595B5C; font-family: ' 微 软雅黑 ';"></span></div>-->
            </div>           
        </div>    
      <div class="youdiv" id="you">
        <div class="jiqidiv">
          <div class="jiqiq" id="youkuang">
            <div class="jiqi"></div>            
<%--              <div class="cbrdiv" id="cbrcankao">--%>
<%--                  <div class="cbrimg">--%>
<%--                  <div class="cbr" onMouseMove="mmove(this,'CBR参考')" onMouseOut="mout();">--%>
<%--                  </div>--%>
<%--                  <div class="ziti" id="cbr1">CBR参考</div>--%>
<%--                  </div>--%>
<%--              </div>           --%>
            <div class="mpddiv" id="mpd">
               <div class="mpd2">
                  <div class="mpd" onMouseMove="mmove(this,'MPD')" onMouseOut="mout();"></div>
                 <div class="ziti">MPD</div>
               </div>
            </div>
             <div class="kekaodiv" id="kekao">
                <div class="kekaodiv1">
                  <div class="kekao" onMouseMove="mmove(this,'可靠性分析')" onMouseOut="mout();"></div>
                   <div class="ziti" id="kekao1">可靠性分析</div>
                </div>
             </div>            
             <div class="msgdiv" id="msg">
                <div class="msg1">
                    <div class="msg"  onMouseMove="mmove(this,'MSG-3分析')" onMouseOut="mout();"></div>
                    <div class="ziti" id="msg1">MSG-3分析</div>
               </div>
            </div>            
             <div class="mrbdiv" id="mrbandknowledge">
                <div class="mrb1">
                    <div class="mrb" onMouseMove="mmove(this,'MRB')" onMouseOut="mout();"></div>
                    <div class="ziti">MRB</div>
                </div>
                <div class="zhishi1">  
                  <div class="knowledge" onMouseMove="mmove(this,'MRB')" onMouseOut="mout();"></div>
                  <div class="ziti" id="zhishi">知识库</div>
                </div>
            </div>            
          </div>            
        </div>
      </div>     
     </div>
  </body>
</html>
<script type="text/javascript">
// 中英文转换
    document.getElementById("huanying").innerHTML = '欢迎使用';
    document.getElementById("yitihua").innerHTML = '维修大纲辅助分析系统！';
<%--    document.getElementById("cbr1").innerHTML = 'CBR参考';--%>
    document.getElementById("kekao1").innerHTML = '可靠性分析';
    document.getElementById("msg1").innerHTML = 'MSG-3分析';
    document.getElementById("zhishi").innerHTML = '知识库';
</script>






