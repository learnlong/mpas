<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/top.jsp"%>
<%@include file="/common/has_ext_top.jsp"%>
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
	<div id="headerStepDiv">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="header">
		<tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
					
						<td><b><font id="sh_ssiCode">SSI编号</font>:${siCode}</b></td>
						<td><b><font id="sh_ssiName">SSI名称</font>：<span id="ssiName" >${siTitle}</span></b></td>
						<td><b></b></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="headerstep">	
		<tr>
			<td width="1"></td>
			<td id="step1" onclick="nextOrSave(1);">S1</td>
			<td id="step2" onclick="nextOrSave(2);">S2</td>
			<c:if test="${step[3] != 3}">
				<td id="step3" onclick="nextOrSave(3);">S3</td>
			</c:if>
			<c:if test="${step[4] != 3}">
				<td id="step4" onclick="nextOrSave(4);">S4a<font name="sh_in">内</font></td>
			</c:if>
			<c:if test="${step[16] != 3}"><!--需求变更添加的 金属应力内部分析页面   -->
				<td id="step16" onclick="nextOrSave(16);">Sya<font name="sh_in">内</font></td>
			</c:if>
			<c:if test="${step[5] != 3}">
			<td id="step5" onclick="nextOrSave(5);">S5a<font name="sh_in" id="s5ain">内</font></td>
			</c:if>
			<c:if test="${step[6] != 3}">
			<td id="step6" onclick="nextOrSave(6);">S4b<font name="sh_in" id="s4bin">内</font></td>
			</c:if>
			<c:if test="${step[17] != 3}"><!--需求变更添加的 非金属应力内部分析页面   -->
				<td id="step17" onclick="nextOrSave(17);">Syb<font name="sh_in">内</font></td>
			</c:if>
			<c:if test="${step[7] != 3}">
			<td id="step7" onclick="nextOrSave(7);">S5b<font name="sh_in" id="s5in">内</font></td>
			</c:if>
			<c:if test="${step[8] != 3}">
			<td id="step8" onclick="nextOrSave(8);">S6<font name="sh_in" id="s6in">内</font></td>
			</c:if>
			<c:if test="${step[9] != 3}">
			<td id="step9" onclick="nextOrSave(9);">S4a<font name="sh_out">外</font></td>
			</c:if>
			<c:if test="${step[18] != 3}"><!--需求变更添加的 金属应力外部分析页面   -->
				<td id="step18" onclick="nextOrSave(18);">Sya<font name="sh_in">外</font></td>
			</c:if>
			<c:if test="${step[10] != 3}">			
			<td id="step10" onclick="nextOrSave(10);">S5a<font name="sh_out">外</font></td>	
			</c:if>
			<c:if test="${step[11] != 3}">
			<td id="step11" onclick="nextOrSave(11);">S4b<font name="sh_out">外</font></td>	
			</c:if>
			<c:if test="${step[19] != 3}"><!--需求变更添加的 非金属应力外部分析页面   -->
				<td id="step19" onclick="nextOrSave(19);">Syb<font name="sh_in">外</font></td>
			</c:if>
			<c:if test="${step[12] != 3}">		
			<td id="step12" onclick="nextOrSave(12);">S5b<font name="sh_out">外</font></td>
			</c:if>
			<c:if test="${step[13] != 3}">			
			<td id="step13" onclick="nextOrSave(13);">S6<font name="sh_out">外</font></td>
			</c:if>
			<c:if test="${step[14] != 3}">
				<td id="step14" onclick="nextOrSave(14);">S7</td>
			</c:if>
			<c:if test="${step[15] != 3}">
				<td id="step15" onclick="nextOrSave(15);">S8</td>
			</c:if>
			<td class="x-rc-form-table-td-sysnext" onclick="nextOrSave(null);"><font id="sh_next">下一步</font></td>	
			<td></td>			
		</tr>
	</table>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr height="2"><td style="background-color:#C1DAD7;"></td></tr>
	</table>
	</div>	
<script type="text/javascript">
var ssiName ='${siTitle}';
 if(ssiName.length > 13){
   var  ssiNameTitle = ssiName.substr(0,13);
   document.getElementById("ssiName").innerHTML = ssiNameTitle+"...";
   document.getElementById("ssiName").title = ssiName;
 
}else{
   document.getElementById("ssiName").innerHTML = ssiName;
   document.getElementById("ssiName").title = ssiName;
}
</script>
<script type="text/javascript">	
	

	var isCreateUser = '${isCreateUser}';

	function nextOrSave_(){
		if (isCreateUser == '0'){
			alert('非管理员或非创建者或已生产正式报告，不能做修改操作！');////'非管理员或非创建者或已生产正式报告，不能做修改操作'
			return false;
		}
		nextOrSave();
	}
	
	var step = new Array();
	step[0] = ${step[0]};
	step[1] = ${step[1]};
	step[2] = ${step[2]};
	step[3] = ${step[3]};
	step[4] = ${step[4]};
	step[5] = ${step[5]};
	step[6] = ${step[6]};
	step[7] = ${step[7]};
	step[8] = ${step[8]};
	step[9] = ${step[9]};
	step[10] = ${step[10]};
	step[11] = ${step[11]};
	step[12] = ${step[12]};
	step[13] = ${step[13]};
	step[14] = ${step[14]};
	step[15] = ${step[15]};
	step[16] = ${step[16]};
	step[17] = ${step[17]};
	step[18] = ${step[18]};
	step[19] = ${step[19]};
	function tdClass(){
		for (var i = 1; i <= step.length; i++){	
			if (step[i] == 1){
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-complete_t";
			} else if (step[i] == 2){
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-maintain_t";
			} else if (step[i] == 0) {
				document.getElementById("step" + i).className = "x-rc-form-table-td-sys-disable_t";
			}
		}
		
		if (step[0] == 1){
			document.getElementById("step1").innerHTML = "<font color='red'><b>S1</b></font>";
		}
		if (step[0] == 2){
			document.getElementById("step2").innerHTML = "<font color='red'><b>S2</b></font>";
		}
		if (step[0] == 3){
			document.getElementById("step3").innerHTML = "<font color='red'><b>S3</b></font>";
		}
		if (step[0] == 4){
			document.getElementById("step4").innerHTML = "<font color='red'>S4a"+"内"+"</font>";
		}
		if (step[0] == 5){
			document.getElementById("step5").innerHTML = "<font color='red'>S5a"+"内"+"</font>";
		}
		if (step[0] == 6){
			document.getElementById("step6").innerHTML = "<font color='red'>S4b"+"内"+"</font>";
		}
		if (step[0] == 7){
			document.getElementById("step7").innerHTML = "<font color='red'>S5b"+"内"+"</font>";
		}
		if (step[0] == 8){
			document.getElementById("step8").innerHTML = "<font color='red'>S6"+"内"+"</font>";
		}
		if (step[0] == 9){
			document.getElementById("step9").innerHTML = "<font color='red'>S4a"+"外"+"</font>";
		}
		if (step[0] == 10){
			document.getElementById("step10").innerHTML = "<font color='red'>S5a"+"外"+"</font>";
		}
		if (step[0] == 11){
			document.getElementById("step11").innerHTML = "<font color='red'>S4b"+"外"+"</font>";
		}
		if (step[0] == 12){
			document.getElementById("step12").innerHTML = "<font color='red'>S5b"+"外"+"</font>";
		}
		if (step[0] == 13){
			document.getElementById("step13").innerHTML = "<font color='red'>S6"+"外"+"</font>";
		}
		if (step[0] == 14){
			document.getElementById("step14").innerHTML = "<font color='red'><b>S7</b></font>";
		}
		if (step[0] == 15){
			document.getElementById("step15").innerHTML = "<font color='red'><b>S8</b></font>";
		}
		if (step[0] == 16){
			document.getElementById("step16").innerHTML = "<font color='red'>Sya"+"内"+"</font>";
		}
		if (step[0] == 17){
			document.getElementById("step17").innerHTML = "<font color='red'>Syb"+"内"+"</font>";
		}
		if (step[0] == 18){
			document.getElementById("step18").innerHTML = "<font color='red'>Sya"+"外"+"</font>";
		}
		if (step[0] == 19){
			document.getElementById("step19").innerHTML = "<font color='red'>Syb"+"外"+"</font>";
		}
	}
	tdClass();
	
	function goNext(value){
//		alert(value);
		if(value==14&&step[14]==3){
/* 			for(var i=1;i<step.length;i++){
				if(step[i]==0||step[i]==2){
					alert('还有步骤未分析完成');
					return false;
				}
			} */
			alert('分析已完成');
			return false;
		}
		if(value==15&&step[15]==3){
/* 			for(var i=1;i<step.length;i++){
				if(step[i]==0||step[i]==2){
					alert('还有步骤未分析完成');
					return false;
				}
			} */
			alert('分析已完成');
			return false;
		}
		if (step[value] == 0|| step[value]==3){
			alert('请先完成之前的步骤！');
			return false;
		}
		successNext(value);
	}
	function successNext(value){
		var ssiId='${ssiId}';
		var isMaintain=${isMaintain};
		if (value == 1){
			location.href='${contextPath}/struct/initS1.do?ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 2){
			location.href='${contextPath}/struct/initS2.do?ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 3){
			location.href='${contextPath}/struct/s3/initS3.do?ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 4){
			location.href='${contextPath}/struct/s4/initS4.do?inOrOut=0&isMetal=1&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 5){
			location.href='${contextPath}/struct/s5/initS5.do?inOrOut=0&isMetal=1&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 6){
			location.href='${contextPath}/struct/s4/initS4b.do?inOrOut=0&isMetal=0&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 7){
			location.href='${contextPath}/struct/s5/initS5b.do?inOrOut=0&isMetal=0&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 8){
			location.href='${contextPath}/struct/s6/initS6.do?ssiId='+ssiId+'&isMaintain='+isMaintain+'&inorOut=IN';
		} else if (value == 9){
			location.href='${contextPath}/struct/s4/initS4aOut.do?inOrOut=1&isMetal=1&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 10){
			location.href='${contextPath}/struct/s5/initS5aOut.do?inOrOut=1&isMetal=1&ssiId='+ssiId+'&isMaintain='+isMaintain;
		}else if (value == 11){
			location.href='${contextPath}/struct/s4/initS4bOut.do?inOrOut=1&isMetal=0&ssiId='+ssiId+'&isMaintain='+isMaintain;
		}else if (value == 12){
			location.href='${contextPath}/struct/s5/initS5bOut.do?inOrOut=1&isMetal=0&ssiId='+ssiId+'&isMaintain='+isMaintain;
		}else if (value == 13){
			location.href='${contextPath}/struct/s6/initS6Out.do?ssiId='+ssiId+'&isMaintain='+isMaintain+'&inorOut=OUT';
		}else if (value == 14){
			location.href='${contextPath}/struct/s7/initS7.do?ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 15){
			location.href='${contextPath}/struct/s8/initS8.do?ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 16){
			location.href='${contextPath}/struct/sy/initSy.do?inOrOut=0&isMetal=1&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 17){
			location.href='${contextPath}/struct/sy/initSyb.do?inOrOut=0&isMetal=0&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 18){
			location.href='${contextPath}/struct/sy/initSyaOut.do?inOrOut=1&isMetal=1&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} else if (value == 19){
			location.href='${contextPath}/struct/sy/initSybOut.do?inOrOut=1&isMetal=0&ssiId='+ssiId+'&isMaintain='+isMaintain;
		} 
	}
</script>