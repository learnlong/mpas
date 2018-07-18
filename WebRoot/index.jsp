<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title >维修大纲辅助分析系统</title>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
		<%@include file="/common/top.jsp" %>
		<%@include file="/common/has_ext_top.jsp" %>
		<link rel="stylesheet" type="text/css" media="screen" href="${contextPath}/js/jquery/easyui/themes/default/easyui.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href="${contextPath}/js/jquery/easyui/themes/icon.css"/>
		<link rel="stylesheet" type="text/css" media="screen" href=""/>
		<script type="text/javascript" src="${contextPath}/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="${contextPath}/js/jquery/easyui/jquery.easyui.min.js"></script>
		<script src="${contextPath}/index.js"></script>
	</head>	
	<!-- 确定是否为ie6浏览器-->  
	<script language="JavaScript">  
	var modelSeriesId="${SESSION_NOW_MODEL_SERIES.modelSeriesId}";
	var userId="${SESSION_USER_KEY.userId}";
        if (window.navigator.userAgent.indexOf("MSIE 6.0") >= 1) {  
            setActiveStyleSheet("ie6.css");
        }else{
			setActiveStyleSheet("otherBrowser.css");
        }

        function setActiveStyleSheet(title) {
            //document.getElementById("common").href = "/css/" + title;
            document.getElementsByTagName("link")[2].href="${contextPath}/css/"+title; 
            //这一行中的0是指第一个名为<link>的标签，改为1，则表示第二个名为<link>的标签。href=“”里面就是放置 CSS文件的路径，而title就是CSS文件的文件名  
        }   
    </script>
    
	<script type="text/javascript">
		Ext.BLANK_IMAGE_URL="${contextPath}/js/extjs/resources/images/default/s.gif";
				
		$(function(){
			$.ajax({ url: "${contextPath}/portal/getParentMenu.do?", 
				context: $("#menudiv"), 
				success: function(data){
		    		this.html(data);
		    		$.parser.parse(this)
		  		}
			});
		});
		//add by samual
		Ext.onReady(function() {
			//不延时加载，老是报一个冲突错误，老mpas也有这个错误/** 机型* */		
			setTimeout(loadIfrmContext, 100);
		});
		function loadIfrmContext(){
			document.getElementById('ifrm').src = '${contextPath}/homepage/init.do?times=' + new Date().getTime();
		}
		
		function changeCurModelSeries(the){
			//alert(the.value);
			Ext.Ajax.request({
				url : "${contextPath}/portal/changeCurModelSeries.do",
				params : {
					modelSeriesId : the.value
				},
				async: false, 
				method : "POST",
				waitMsg : commonality_waitMsg,
				success : function(response) {
					if (response.responseText != null && response.responseText != '') {
						var result = Ext.util.JSON.decode(response.responseText);
						if (result.success == true || result.success == 'true') {
							window.location.href="${contextPath}/index.jsp";
						}
					}
				}
			});
		}
	</script>
	
	<style type="text/css">
		#ulmenu{
			float:right;
			width:845px;
		}
		ul li{
			float:right;
		}
		
		.top{
			width:100%;
			height:60px;
			color:#FFF;
			font-weight:bold;
			font-size:12px;
			background-position:left top;
			background-repeat:no-repeat;
			vertical-align:bottom;
			z-index: 9999;
			position:absolute;
		}
		
		ul {padding:0;margin:0;list-style-type:none;float:left;z-index:9999;}
	</style>

<body class="easyui-layout">
	<div region="north" border="false" style="width:100%;height:60px;background:#D8E5F8;">	
		<div class="top">	
			<div id="logodiv" style="height:30px;width:100%">
				<div class="n_top" >
					<div class="dlk2">
						<img src="${contextPath}/images/index.gif" /><a href="${contextPath}/index.jsp">首页</a>
						<img src="${contextPath}/images/login.gif" />欢迎&nbsp;<span>${SESSION_USER_KEY.userName}</span>
						<img src="${contextPath}/images/maintain.gif" />机型选择
							<select id="selectId" class="x-combo x-combo-selected x-combo-list" onchange="changeCurModelSeries(this)">
								<c:forEach items="${SESSION_MODEL_SERIES_LIST}" var ="li" varStatus="status">
									<c:if test="${li.modelSeriesId == SESSION_NOW_MODEL_SERIES.modelSeriesId}">
										<option value="${li.modelSeriesId}" selected = "selected">${li.modelSeriesCode}</option>
									</c:if>
									<c:if test="${li.modelSeriesId != SESSION_NOW_MODEL_SERIES.modelSeriesId}">
										<option value="${li.modelSeriesId}">${li.modelSeriesCode}</option>
									</c:if>
								</c:forEach>
							</select>
						<img src="${contextPath}/images/useradmin.gif"/><a href="javascript:void(0)" onclick="updateUserPassword()">修改密码</a>
					    <img src="${contextPath}/images/logout.gif" /><a href="#" onclick="exit()">退出</a>
					</div>
				</div>
			</div>
			<div id="menudiv">				
			</div>
		</div>
	</div>
	<div region="center" title="" style="overflow:hidden">
		<iframe name="ifrm" id="ifrm" frameborder="0" scrolling="auto" style="width: 100%; height: 100%;" src=""></iframe>
		<iframe id="clearSession" name="clearSession" frameborder="0" src=""></iframe>	
	</div>	
</body>
</html>
