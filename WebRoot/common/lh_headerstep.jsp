﻿<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<style type="text/css">	
   #header td { 	
		font-size:12px; 
		padding: 0px 0px 4px 8px; 
		color: #4f6b72; 
	}	
	#headerstep td {		
		padding: 6px 6px 3px 6px;		
	}
</style>
<body>
	<div id="headerStepDiv" style= "height:100px">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" id="header">
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td><b><font id="HSICode1">HSI编号:</font>${lhHsi.hsiCode}</b></td>
							<td><b><font id="HSIname1">HSI名称:</font><span id="HSIname" >${hsiName}</span></b></td>
							<td><b><font id="areaCode1">区域编号:</font>${comArea.areaCode}</b></td>
							<td><b><font id="areaName1">区域名称:</font><span id="areaName" >${areaName}</span></b></td>
							<td id="rehsi"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" id="headerstep">		
			<tr>
				<td width="1"></td>
				<td id="step0" onclick="nextOrSave(0);">LH1</td>
				<td id="step1" onclick="nextOrSave(1);">LH1A</td>
				<td id="step2" onclick="nextOrSave(2);">LH2</td>
				<td id="step3" onclick="nextOrSave(3);">LH3</td>
				<td id="step4" onclick="nextOrSave(4);">LH4</td>
				<td id="step5" onclick="nextOrSave(5);">LH5</td>
				<td id="step6" onclick="nextOrSave(6);">LH6</td>
				<td class="x-rc-form-table-td-sysnext" onclick="nextOrSave(null);" id="next" >下一步</td>	
				<td></td>			
			</tr>
		</table>
	</div>
</body>
<script type="text/javascript">

///HSI名称 长度 过长问题 
	var hsiName ='${hsiName}';
	 if(hsiName.length > 13){
	   var  hsiNameLitle = hsiName.substr(0,13);
	   document.getElementById("HSIname").innerHTML = hsiNameLitle+"...";
	   document.getElementById("HSIname").title = hsiName;
	 
	}else{
	   document.getElementById("HSIname").innerHTML = hsiName;
	}
	///区域名称 长度 过长问题
	var areaName ='${areaName}';
	 if(areaName.length > 11){
	   var  areaNameLitle = areaName.substr(0,11);
	   document.getElementById("areaName").innerHTML = areaNameLitle+"...";
	   document.getElementById("areaName").title = areaName;
	 
	}else{
	   document.getElementById("areaName").innerHTML = areaName;
	}
	
</script>
<script type="text/javascript">	
	var refHsiCode = '${lhHsi.refHsiCode}';
	var NA = '<%=ComacConstants.EMPTY%>'; //表示 没有参见的HSI  N/A
	if (refHsiCode != NA) {
	    document.getElementById('rehsi').innerHTML = '<b>所参见的HSI:' + refHsiCode + '</b>';
	}
	if (refHsiCode == NA || "" == refHsiCode) {
	    document.getElementById('rehsi').style.display = 'none';
	}

	var hsiId = '${lhHsi.hsiId}';
	
	var step = new Array();
	step[0] = '${lhStep.lh1}';
	step[1] = '${lhStep.lh1a}';
	step[2] = '${lhStep.lh2}';
	step[3] = '${lhStep.lh3}';
	step[4] = '${lhStep.lh4}';
	step[5] = '${lhStep.lh5}';
	step[6] = '${lhStep.lh6}';
	
	isMaintain = '${isMaintain}';
	if (isMaintain == '1') {
	    authorityFlag = true;
	} else {
	    authorityFlag = false;
	}
	function tdClass() {
	
	    for (var i = 0; i < step.length; i++) {
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
	    if ('${pagename}' == 'LH1') {
	        document.getElementById("step0").innerHTML = "<font color='red'><b>LH1</b></font>";
	    }
	    if ('${pagename}' == 'LH1A') {
	        document.getElementById("step1").innerHTML = "<font color='red'><b>LH1A</b></font>";
	    }
	    if ('${pagename}' == 'LH2') {
	        document.getElementById("step2").innerHTML = "<font color='red'><b>LH2</b></font>";
	    }
	    if ('${pagename}' == 'LH3') {
	        document.getElementById("step3").innerHTML = "<font color='red'><b>LH3</b></font>";
	    }
	    if ('${pagename}' == 'LH4') {
	        document.getElementById("step4").innerHTML = "<font color='red'><b>LH4</b></font>";
	    }
	    if ('${pagename}' == 'LH5') {
	        document.getElementById("step5").innerHTML = "<font color='red'><b>LH5</b></font>";
	    }
	    if ('${pagename}' == 'LH6') {
	        document.getElementById("step6").innerHTML = "<font color='red'><b>LH6</b></font>";
	    }
	
	}
	tdClass();
	
	function goNext(value) {
	    if (value == 4) {
	        if ((step[3] == 1 && step[4] == 0) || step[4] == 1 || step[4] == 2) {
	            successNext(value);
	        } else {
	            alert('该模块不能进入!');
	             return false;
	        }
	    }
	
	    if (value != 4) {
	        if (step[1] != 3) {   ///这个流程是 lh1a---lh5,lh6或者  lh1a, lh6
	           if( step[value] == 0){
	              alert('该模块不能进入!');
	               return false;
	           }else{
	              successNext(value);
	           }
	        } else {   
	            if (value != 0 && step[value] == 0) {
	                alert('该模块不能进入!');
	                return false;
	            }
	            successNext(value);
	        }
	    }
	
	}
	
	function successNext(value) {
	    waitMsg11.show();    
	    if (value == 0) {
	        location.href = '${contextPath}/lhirf/lh_1/init.do?hsiId=' + hsiId + '&isMaintain=' + isMaintain;
	    } else if (value == 1) {
	        location.href = '${contextPath}/lhirf/lh_1a/init.do?hsiId=' + hsiId + '&isMaintain=' + isMaintain;
	    } else if (value == 2) {
	        location.href = '${contextPath}/lhirf/lh_2/init.do?hsiId=' + hsiId + '&isMaintain=' + isMaintain;
	    } else if (value == 3) {
	        location.href = '${contextPath}/lhirf/lh_3/init.do?hsiId=' + hsiId + '&isMaintain=' + isMaintain;
	    } else if (value == 4) {
	        if (customIsFull() == 1) {
	            location.href = '${contextPath}/lhirf/lh_4/init.do?hsiId=' + hsiId + '&isMaintain=' + isMaintain;
	        } else {
	           if (customIsFull() == 0) {
	               alert('LH4的自定义矩阵数据不完整和自定义显示信息没有维护，请添加完整之后再进行该分析！');
	            }
	           if (customIsFull() == 2) {
	               alert('LH4的自定义矩阵数据不完整，请添加完整之后再进行该分析！');
	            }
	           if (customIsFull() == 3) {
	               alert('自定义显示信息没有维护，请添加完整之后再进行该分析！');
	            }
	       }
	    } else if (value == 5) {
	        location.href = '${contextPath}/lhirf/lh_5/init.do?hsiId=' + hsiId + '&isMaintain=' + isMaintain;
	    } else if (value == 6) {
	        location.href = '${contextPath}/lhirf/lh_6/init.do?hsiId=' + hsiId + '&isMaintain=' + isMaintain;
	    }
	}
	var waitMsg11 = new Ext.LoadMask(Ext.getBody(), {
	    msg: commonality_waitMsg,
	    removeMask: true
	    // 完成后移除
	});
	/*************************************************Ajax同步检测自定义矩阵数据是否完整******************************************** */
	function customIsFull() {
	    var tempFlag = 0;
	    var remarkFlag = 0;
	    var waitMsg = new Ext.LoadMask(Ext.getBody(), {
	        msg: commonality_transferWaitMsg,
	        removeMask: true
	        // 完成后移除
	    });
	    waitMsg.show();
	    Ext.Ajax.request({
	        url: "${contextPath}/lhirf/lh_4/checkCustomIsFull.do",
	        method: "POST",
	        async: false,
	        //是否异步提交。false：同步。true：异步。默认异步，在Ext3.0中此属性需要ext-basex.rar，解压，引入工程才可以使用
	        success: function(response, action) {
	            waitMsg.hide();
	            var msg = Ext.util.JSON.decode(response.responseText);
	            if (msg.isOk == 'yes' && msg.isRemark == 'yes') {
	                tempFlag = 1;
	              
	            }else{
		            if(msg.isOk == 'no' && msg.isRemark == 'no') {
		                     tempFlag = 0;
		                       return ;
		            }if(msg.isOk == 'no'){
		                  tempFlag = 2;
		                   return ;
	                     
		            }if(msg.isRemark == 'no'){
		                 tempFlag = 3;
		                  return ;
		            }
	            }
	        },
	        failure: function(response, action) {
	            waitMsg.hide();
	            alert(commonality_messageGuZhang);
	        }
	    });
	
	   return tempFlag;
	}
</script>