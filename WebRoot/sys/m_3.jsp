<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/sys_headerstep.jsp" %>
<script type="text/javascript" src="${contextPath}/js/extjs/multicombo.js"></script>
<script type="text/javascript">	
	var msiId = '${msiId}';
	var msiEff ='${msiEff}';
	var isCreateUser = '${isCreateUser}';
	var areaCodes ='';
	var causeId;
	var m2Id;
	var m3Id;
	var effectResult;
	var radiosValue;
	var isDiffer = 0;
	var nowcode = '0';
</script>
<script src="${contextPath}/sys/sysintertree.js"></script>
<style type="text/css">
	#abc table{
		border-left: 1px solid #C1DAD7; 
		border-top: 1px solid #C1DAD7;
		background-color : #DFE8F6;
	}
	#abc td { 
		border-right: 1px solid #C1DAD7; 
		border-bottom: 1px solid #C1DAD7; 
		background-color : #DFE8F6;
		font-size:12px; 
		padding: 3px 3px 3px 4px; 
		color: #4f6b72; 
	}
	.green {background-color : green;}
	.white {color : white;}
</style>


<div id="myDiv" style="overflow:auto;">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" id="abc">
	  <tr>
	    <td colspan="6" rowspan="4" align="center"><b>故障影响类别</b></td>
	    <td colspan="5"><b id="function">功能：</b><b id="fun"></b></td>
	  </tr>
	  <tr>
	    <td colspan="5"><b id="fault">故障：</b><b id="fail"></b></td>
	  </tr>
	  <tr>
	    <td colspan="5"><b id="effect">影响：</b><b id="eff"></b></td>
	  </tr>
	  <tr>
	    <td colspan="5"><b id="eason">原因：</b><b id="cause"></b></td>
	  </tr>
	  <tr>
	    <td width="20" align="center" id="td61"><font id="font61">6</font></td>
	    <td width="20" align="center" id="td71"><font id="font71">7</font></td>
	    <td width="20" align="center" id="td81"><font id="font81">8</font></td>
	    <td width="20" align="center" id="td91"><font id="font91">9</font></td>
	    <td width="20" align="center" id="td101"><font id="font101">10</font></td>
	    <td width="20" align="center" id="td111"><font id="font111">11</font></td>
	    <td width="391" align="center"><b id="question">问题</b></td>
	    <td width="30" align="center"><b id="yes">是</b></td>
	    <td width="30" align="center"><b id="no">否</b></td>
	    <td width="50" align="center"><b id="inapplicability">不适用</b></td>
	    <td width="319" align="center"><b id="answer">所选维修工作类型</b></td>
	  </tr>
	  <tr>
	    <td align="center" id="td62"><font id="font62">A</font></td>
	    <td align="center" id="td72"><font id="font72">A</font></td>
	    <td align="center" id="td82"><font id="font82">A</font></td>
	    <td align="center" id="td92"><font id="font92">A</font></td>
	    <td align="center" id="td102"><font id="font102">A</font></td>
	    <td align="center" id="td112"><font id="font112">A</font></td>
	    <td id="question1">保养是适用和有效的吗？</td>
	    <td align="center"><input type="radio" name="radio1" value="1" onchange="selectRadio(1,1);"  onclick = "endIeStatus(1)"/></td>
	    <td align="center"><input type="radio" name="radio1" value="0" onchange="selectRadio(1,0);" onclick = "endIeStatus(1)"/></td>
	    <td align="center"><input type="radio" name="radio1" value="2" onchange="selectRadio(1,2);" onclick = "endIeStatus(1)"/></td>
	    <td>
	    <textarea name="area1" id="area1" rows="3" cols="20"></textarea>
	    </td>
	  </tr>
	  <tr>
	    <td align="center" id="td63"><font id="font63">&nbsp;</font></td>
	    <td align="center" id="td73"><font id="font73">&nbsp;</font></td>
	    <td align="center" id="td83"><font id="font83">&nbsp;</font></td>
	    <td align="center" id="td93"><font id="font93">B</font></td>
	    <td align="center" id="td103"><font id="font103">B*</font></td>
	    <td align="center" id="td113"><font id="font113">B*</font></td>
	    <td id="question2">使用状态的检验是适用和有效的吗？</td>
	    <td align="center"><input type="radio" name="radio2" value="1" onchange="selectRadio(2,1)" onclick = "endIeStatus(2)"/></td>
	    <td align="center"><input type="radio" name="radio2" value="0" onchange="selectRadio(2,0)" onclick = "endIeStatus(2)"/></td>
	    <td align="center"><input type="radio" name="radio2" value="2" onchange="selectRadio(2,2)" onclick = "endIeStatus(2)"/></td>
	    <td><textarea name="area2" id="area2" rows="3" cols="20"  ></textarea>
	  	</td>
	  </tr>
	  <tr>
	    <td align="center" id="td64"><font id="font64">B</font></td>
	    <td align="center" id="td74"><font id="font74">B*</font></td>
	    <td align="center" id="td84"><font id="font84">B*</font></td>
	    <td align="center" id="td94"><font id="font94">&nbsp;</font></td>
	    <td align="center" id="td104"><font id="font104">&nbsp;</font></td>
	    <td align="center" id="td114"><font id="font114">&nbsp;</font></td>
	    <td id="question3">用正常的操作人员监控来探测功能恶化是适用和有效的吗？</td>
	    <td align="center"><input type="radio" name="radio3" value="1" onchange="selectRadio(3,1)" onclick = "endIeStatus(3)"/></td>
	    <td align="center"><input type="radio" name="radio3" value="0" onchange="selectRadio(3,0)" onclick = "endIeStatus(3)"/></td>
	    <td align="center"><input type="radio" name="radio3" value="2" onchange="selectRadio(3,2)" onclick = "endIeStatus(3)"/></td>
	    <td><textarea name="area3" id="area3" rows="3" cols="20" ></textarea>
	    </td>
	  </tr>
	  <tr>
	    <td align="center" id="td65"><font id="font65">C</font></td>
	    <td align="center" id="td75"><font id="font75">C*</font></td>
	    <td align="center" id="td85"><font id="font85">C*</font></td>
	    <td align="center" id="td95"><font id="font95">C</font></td>
	    <td align="center" id="td105"><font id="font105">C*</font></td>
	    <td align="center" id="td115"><font id="font115">C*</font></td>
	    <td id="question4">用原位或离位检查来探测功能恶化是适用和有效的吗？</td>
	    <td align="center"><input type="radio" name="radio4" value="1" onchange="selectRadio(4,1)" onclick = "endIeStatus(4)"/></td>
	    <td align="center"><input type="radio" name="radio4" value="0" onchange="selectRadio(4,0)" onclick = "endIeStatus(4)"/></td>
	    <td align="center"><input type="radio" name="radio4" value="2" onchange="selectRadio(4,2)" onclick = "endIeStatus(4)"/></td>
	    <td> <textarea name="area4" id="area4" rows="3" cols="20"></textarea>
	     </td>
	  </tr>
	  <tr>
	    <td align="center" id="td66"><font id="font66">D</font></td>
	    <td align="center" id="td76"><font id="font76">D*</font></td>
	    <td align="center" id="td86"><font id="font86">D*</font></td>
	    <td align="center" id="td96"><font id="font96">D</font></td>
	    <td align="center" id="td106"><font id="font106">D*</font></td>
	    <td align="center" id="td116"><font id="font116">D*</font></td>
	    <td id="question5">定时拆修是适用和有效的吗？</td>
	    <td align="center"><input type="radio" name="radio5" value="1" onchange="selectRadio(5,1)" onclick = "endIeStatus(5)"/></td>
	    <td align="center"><input type="radio" name="radio5" value="0" onchange="selectRadio(5,0)" onclick = "endIeStatus(5)"/></td>
	    <td align="center"><input type="radio" name="radio5" value="2" onchange="selectRadio(5,2)" onclick = "endIeStatus(5)"/></td>
	    <td> <textarea name="area5" id="area5" rows="3" cols="20" ></textarea>
	     </td>
	  </tr>
	  <tr>
	    <td align="center" id="td67"><font id="font67">E</font></td>
	    <td align="center" id="td77"><font id="font77">E*</font></td>
	    <td align="center" id="td87"><font id="font87">E*</font></td>
	    <td align="center" id="td97"><font id="font97">E</font></td>
	    <td align="center" id="td107"><font id="font107">E*</font></td>
	    <td align="center" id="td117"><font id="font117">E*</font></td>
	    <td id="question6">定时报废是适用和有效的吗？</td>
	    <td align="center"><input type="radio" name="radio6" value="1" onchange="selectRadio(6,1)" onclick = "endIeStatus(6)"/></td>
	    <td align="center"><input type="radio" name="radio6" value="0" onchange="selectRadio(6,0)" onclick = "endIeStatus(6)"/></td>
	    <td align="center"><input type="radio" name="radio6" value="2" onchange="selectRadio(6,2)" onclick = "endIeStatus(6)"/></td>
	    <td> <textarea name="area6" id="area6" rows="3" cols="20"></textarea>
	     </td>
	  </tr>
	  <tr>
	    <td align="center" id="td68"><font id="font68">F</font></td>
	    <td align="center" id="td78"><font id="font78">F</font></td>
	    <td align="center" id="td88"><font id="font88">&nbsp;</font></td>
	    <td align="center" id="td98"><font id="font98">F</font></td>
	    <td align="center" id="td108"><font id="font108">F</font></td>
	    <td align="center" id="td118"><font id="font118">&nbsp;</font></td>
	    <td id="question7">有一种工作或综合工作是适用和有效的吗？</td>
	    <td align="center"><input type="radio" name="radio7" value="1" onchange="selectRadio(7,1)" onclick = "endIeStatus(7)"/></td>
	    <td align="center"><input type="radio" name="radio7" value="0" onchange="selectRadio(7,0)" onclick = "endIeStatus(7)"/></td>
	    <td align="center"><input type="radio" name="radio7" value="2" onchange="selectRadio(7,2)" onclick = "endIeStatus(7)"/></td>
	    <td> <textarea name="area7" id="area7" rows="3" cols="20"></textarea>
		</td>
	  </tr>
	</table>
	<table width="100%" border="1" cellpadding="0" cellspacing="0" id="abc">
	<tr>
		<td style="width:30%;">是否需要改进设计？</td>
	 	<td><input type="radio" name="radio8" value="1"/><font id="radio8Yes" >是</font></td>
	 	<td><input type="radio" name="radio8" value="0"/><font id="radio8No">否</font></td>
	</tr>
	</table>
	<table width="100%" border="1" cellpadding="0" cellspacing="0" id="abc">
		<tr>
			<td width="50" id="remark">备注：</td>		
		</tr>
		<tr>
			<td> <textarea name="area8" id="area8" rows="3" cols="50" ></textarea>
	    </td>
		</tr>
	</table>
	<div id='endIeStatus' ></div>
</div>
<script src="${contextPath}/sys/m_3.js"></script>
<script src="${contextPath}/sys/m_3_1.js"></script>