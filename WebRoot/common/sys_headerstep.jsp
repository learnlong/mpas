<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.rskytech.pojo.ComAta"%>
<%@page import="org.springframework.web.util.JavaScriptUtils"%>
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
<style type="text/css">
#header td {
	font-size: 12px;
	padding: 0px 0px 4px 8px;
	color: #4f6b72;
}

#headerstep td {
	padding: 6px 6px 3px 6px;
}
</style>
<body>
	<div id="headerStepDiv">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			id="header">
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<b><font id="MsiCode1">MSI号:</font>${showAta.ataCode}</b>
							</td>
							<td>
								<b><font id="msiName1">MSI名称:</font>
								</b><b id="msiName"></b>
							</td>
							<td>
								<b><font id="subCode1">ATA章节号:</font>
								</b><b id="subCode"></b>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			id="headerstep">
			<tr>
				<td width="1"></td>
				<td id="step0" onclick="nextOrSave(0);">
					MSI确定
				</td>
				<td id="step1" onclick="nextOrSave(1);">
					功能描述
				</td>
				<td id="step2" onclick="nextOrSave(2);">
					系统组成
				</td>
				<td id="step8" onclick="nextOrSave(10);">
					故障描述
				</td>
				<td id="step3" onclick="nextOrSave(3);">
					原因分析
				</td>
				<td id="step4" onclick="nextOrSave(4);">
					上层决断
				</td>
				<td id="step5" onclick="nextOrSave(5);">
					下层决断
				</td>
				<td id="step6" onclick="nextOrSave(6);">
					间隔确定
				</td>
				<td id="step7" onclick="nextOrSave(7);">
					工作汇总
				</td>
				<td class="x-rc-form-table-td-sysnext" onclick="nextOrSave(null);"
					id="next">
					下一步
				</td>
				<td></td>
			</tr>
		</table>
	</div>
</body>

<script type="text/javascript">
	var msiCodeShow = '${showAta.ataCode}';
	var ataId='${showAta.ataId}';
	var msiId='${msiId}';
	var msiName;
	var noM12 = "${noM12}";
	var noM2 = "${noM2}";
	
	isMaintain = '${isMaintain}';
	//判断是否有修改权限
	if(isMaintain =='1'){
	      flag = true;
	}else{
		flag = false;
	}
	msiName = '<%=JavaScriptUtils.javaScriptEscape(((ComAta)request.getAttribute("showAta")).getAtaName()==null?"":((ComAta)request.getAttribute("showAta")).getAtaName())%>';
	document.getElementById('msiName').title = msiName;
	 if(msiName.length > 13){
	 document.getElementById('msiName').innerHTML=msiName.substr(0,13)+"...";
	 }else{
	   document.getElementById('msiName').innerHTML  = msiName;
	}
	document.getElementById('subCode').innerHTML = '${showAta.ataCode}'.substr(0,2);
	var step = new Array();
	step[0] ='${pagename}';
			//载入数据
	function loadStep(){
		Ext.Ajax.request( {
			url : '${contextPath}/sys/mStep/init.do',
			async:false,
			
			params : {					
				msiId : msiId
			},
			method : "POST",
			callback: function(options, success, response){
				var all = Ext.util.JSON.decode(response.responseText);
				step[1] = all.step[0];
				step[2] = all.step[1];
				step[3] = all.step[2];
				step[4] = all.step[3];
				step[5] = all.step[4];
				step[6] = all.step[5];
				step[7] = all.step[6] ;
				step[8] = all.step[7] ;
               	tdClass();
			}
		});
	}
	 loadStep();
	function tdClass(){
	 
		document.getElementById("step0").className = "x-rc-form-table-td-sys-complete";
		for (var i = 1; i < step.length; i++){
			if (step[i] == 1){
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-complete";
			} else if (step[i] == 2){
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-maintain";
			} else if (step[i] == 0){
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-disable";
			} else {
				document.getElementById("step" + i).style.display="none";
			}
		}
		if ('${pagename}'== 'M0'){
			document.getElementById("step0").innerHTML = "<font color='red'><b>MSI确定</b></font>";
		}
		if ('${pagename}'== 'M11'){
			document.getElementById("step1").innerHTML = "<font color='red'><b>功能描述</b></font>";
		}
		if ('${pagename}'== 'M12'){
			document.getElementById("step2").innerHTML = "<font color='red'><b>系统组成</b></font>";
		}
		if ('${pagename}'== 'MSET'){
			document.getElementById("step8").innerHTML = "<font color='red'><b>故障描述</b></font>";
		}
		if ('${pagename}'== 'M13'){
			document.getElementById("step3").innerHTML = "<font color='red'><b>原因分析</b></font>";
		}
		if ('${pagename}'== 'M2'){
			document.getElementById("step4").innerHTML = "<font color='red'><b>上层决断</b></font>";
		}
		if ('${pagename}'== 'M3'){
			document.getElementById("step5").innerHTML = "<font color='red'><b>下层决断</b></font>";
		}
		if ('${pagename}'== 'M4'){
			document.getElementById("step6").innerHTML = "<font color='red'><b>间隔确定</b></font>";
		}
		if ('${pagename}'== 'M5'){
			document.getElementById("step7").innerHTML = "<font color='red'><b>工作汇总</b></font>";
		}		
	}
	

	function goNext(value){	
	  if(noM12=="false"&&value ==2){
		   alert("供应商信息没有维护，不能进入M1.2");
		   return false;
	      }
	  if(noM2=="false"&&value ==4){
			   alert("MMEL信息没有维护，不能进入M2");
			   return false;
	      }	
	      if(value==3||value ==10||value == 6||value == 4){
		      if (value ==3 && step[8] == 0){
			alert("前面的步骤没有分析完成，不能进入下一步骤");
			return false;
		}else if (value ==10 && step[8] == 0){
			alert("前面的步骤没有分析完成，不能进入下一步骤");
			return false;
		}else if (value == 4 && step[3] == 0){
			alert("前面的步骤没有分析完成，不能进入下一步骤");
			return false;
		}else if (value == 6 && step[6] == 3){
			alert("分析已完成");
			return false;
		}
		}else{
		 if(value != 0 && step[value] == 0){
			alert("前面的步骤没有分析完成，不能进入下一步骤");
			return false;
			}
		}
		successNext(value);
	}
	
	function successNext(value){

		waitMsg.show();
		if (value == 0){		    
			location.href='${contextPath}/sys/m0/init.do?msiId=' + msiId+'&ataId='+ataId+'&isMaintain='+isMaintain;
		} else if (value == 1){
			location.href='${contextPath}/sys/m11/init.do?msiId=' + msiId+'&ataId='+ataId+'&isMaintain='+isMaintain;
		} else if (value == 2){
			location.href='${contextPath}/sys/m12/init.do?msiId=' + msiId +'&ataId='+ataId+'&isMaintain='+isMaintain;
		} else if (value == 3){
			location.href='${contextPath}/sys/m13/init.do?msiId=' + msiId +'&ataId='+ataId+'&isMaintain='+isMaintain;
		} else if (value == 4){
			location.href='${contextPath}/sys/m2/init.do?msiId=' + msiId +'&ataId='+ataId+'&isMaintain='+isMaintain;
		} else if (value == 5){
			location.href='${contextPath}/sys/m3/init.do?msiId=' + msiId +'&ataId='+ataId+'&isMaintain='+isMaintain;
		} else if (value == 6){
			location.href='${contextPath}/sys/m4/init.do?msiId=' + msiId+'&ataId='+ataId+'&isMaintain='+isMaintain;
		} else if (value == 7){
			location.href='${contextPath}/sys/m5/init.do?msiId=' + msiId+'&ataId='+ataId+'&isMaintain='+isMaintain;
		} else if (value == 10){
			location.href='${contextPath}/sys/mset/init.do?msiId=' + msiId+'&ataId='+ataId+'&isMaintain='+isMaintain;
		}
	}	
   var waitMsg = new Ext.LoadMask(Ext.getBody(), {
                  msg:commonality_waitMsg,
                  removeMask : true
						// 完成后移除
              });
</script>