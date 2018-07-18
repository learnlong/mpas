<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/sys_headerstep.jsp" %>
<script type="text/javascript" src="${contextPath}/js/extjs/multicombo.js"></script>
<style type="text/css">
	#myDiv .tabClass{
		border-left: 1px solid #C1DAD7; 
		border-top: 1px solid #C1DAD7;
		background-color : #DFE8F6;
	}
	#myDiv .tabClass td { 
		font-size:12px; 
		padding: 3px 3px 3px 4px; 
		color: #4f6b72; 
	}	
	.change1{	
		margin:-2px -2px -2px -3px!important;
		height:20px!important;
		line-height:22px;
		font-size:12px;
		font-weight:bold;
	    background-color: #ffffa0;
	    color:red;
	}
	.questionHighlight{	
		font-size:12px;
	    background-color: #ffffa0;
	    border-color::#000000;
	    color:black;
	}
</style>
<div id="myDiv" style="overflow:auto">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" class='tabClass'>
	  <tr>
	    <td colspan="6"><b  id ="function">功能：</b><b id="fun"></b></td>
	  </tr>
	  <tr>
	    <td colspan="6"><b  id="fault">故障：</b><b id="fail"></b></td>
	  </tr>
	  <tr>
	    <td colspan="4"><b id="effect">影响：</b><b id="eff"></b></td>
	  </tr>
	  <tr>
	    <td width="40%" align="center"><b id="effectQuestion">故障影响问题</b></td>
	    <td width="40" align="center"><b id="question">问题</b></td>
	    <td width="80" align="center"><b id="answer">回答</b></td>
	    <td align="center"><b id = "explain">说明</b></td>
	  </tr>
	  <tr>
	    <td rowspan="9" style="background-color : white" align="center" valign="middle">
				<table width="300" border="1" cellspacing="0" 
					bordercolor="#FFFFFF" >
					<tr bordercolor="#000000">
						<td colspan="25" align="center" id='question1'>
							问题一：功能故障的发生对在履行正常职责的空勤人员来说是明显的吗?
						</td>
					</tr>
					<tr>
						<td colspan="6">
							&nbsp;
						</td>
						<td colspan="13"
							style="border-left-color: #000; border-right-color: #000">
							&nbsp;
						</td>
						<td height="8" colspan="6">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td  colspan="5"
							style="border-left-style: none; border-top-style: none; border-bottom-style: none"></td>
						<td colspan="3" bordercolor="#000000" width="40px">
							<div align="center"  id="radio1Yes">
								是
							</div>
						</td>
						<td colspan="9"></td>
						<td colspan="3" bordercolor="#000000" width="40px">
							<div align="center" id="radio1No">
								否
							</div>
						</td>
						<td colspan="5"></td>
					</tr>
					<tr>
						<td colspan="6">
							&nbsp;
						</td>
						<td colspan="13"
							style="border-left-color: #000; border-right-color: #000">
							&nbsp;
						</td>
						<td colspan="6">
							&nbsp;
						</td>
					</tr>
					<tr >
						<td height="31" colspan="13"  bordercolor="#000000" id="question2">
							问题二：功能故障或由其引起的二次损伤对运行安全有直接有害的影响吗？
						</td>
						<td colspan="2"
						 style="border-top-style: none; border-bottom-style: none"  >
							&nbsp;
						</td>
						<td colspan="10"  bordercolor="#000000"  id="question3">
							问题四：隐蔽功能故障和另一个与系统有关的或备用的功能故障综合对使用安全有有害影响吗？
						</td>
					</tr>
				
					<tr>
						<td height="19" colspan="2">
							&nbsp;
						</td>
						<td colspan="8"
							style="border-left-color: #000; border-right-color: #000">
							&nbsp;
						</td>
						<td colspan="5">
							&nbsp;
						</td>
						<td colspan="2">
							&nbsp;
						</td>
						<td colspan="6"
							style="border-left-color: #000; border-right-color: #000">
							&nbsp;
						</td>
						<td colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td colspan="2" bordercolor="#000000" >
							<div align="center"  id="radio2Yes" >
								是
							</div>
						</td>
				
						<td colspan="5">
							&nbsp;
						</td>
						<td colspan="3" bordercolor="#000000" >
							<div align="center" id="radio2No">
								否
							</div>
						</td>
						<td >
							&nbsp;
						</td>
						<td colspan="2" >
							&nbsp;
						</td>
						<td >
							&nbsp;
						</td>
						<td >
							&nbsp;
						</td>
						<td colspan="3" bordercolor="#000000" >
							<div align="center"  id="radio3Yes"  >
								是
							</div>
						</td>
						<td colspan="3" >
							&nbsp;
						</td>
						<td colspan="2" bordercolor="#000000"  >
							<div align="center" id="radio3No">
								否
							</div>
						</td>
						
					</tr>
					<tr>
						<td rowspan="5">
							&nbsp;
						</td>
						<td rowspan="5" style="border-right-color: #000">
							&nbsp;
						</td>
						<td rowspan="5">
							&nbsp;
						</td>
						<td rowspan="5">
							&nbsp;
						</td>
						<td colspan="6" style="border-right-color: #000">
							&nbsp;
						</td>
						<td colspan="4">
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td  rowspan="5"
						style="border-right-color: #000">
							&nbsp;
						</td>
						<td colspan="6" 
							style="border-right-color: #000">
							&nbsp;
						</td>
						<td colspan="2" rowspan="5">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="9" bordercolor="#000000"  id="question4">
							问题三：功能故障对任务完成有直接有害影响吗？
						</td>
						<td colspan="6">
							&nbsp;
						</td>
						<td colspan="7"  bordercolor="#000000"  id="question5">
							问题五：隐蔽功能故障和另一个与系统有关的或备用的功能故障综合对任务安全有有害影响吗？
						</td>
					</tr>
					<tr>
						<td colspan="2">
							&nbsp;
						</td>
						<td colspan="5"
							style="border-left-color: #000; border-right-color: #000">
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td colspan="8">
							&nbsp;
						</td>
						<td colspan="4"
							style="border-left-color: #000; border-right-color: #000">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td colspan="3" bordercolor="#000000" width="40">
							<div align="center" id="radio4Yes">
								是
							</div>
						</td>
						<td colspan="1">
							&nbsp;
						</td>
						<td colspan="3" bordercolor="#000000" width="40">
							<div align="center" id="radio4No">
								否
							</div>
						</td>
						<td colspan="2" >
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td colspan="3" bordercolor="#000000">
							<div align="center" id="radio5Yes">
								是
							</div>
						</td>
						<td >
							&nbsp;
						</td>
						<td colspan="3" bordercolor="#000000">
							<div align="center" id="radio5No">
								否
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="border-right-color: #000">
							&nbsp;
						</td>
						<td colspan="4">
							&nbsp;
						</td>
						<td style="border-right-color: #000">
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td colspan="2">
							&nbsp;
						</td>
						<td colspan="2">
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td colspan="2" style="border-right-color: #000">
						&nbsp;
						</td>
						<td colspan="2">
							&nbsp;
						</td>
						<td colspan="2" style="border-right-color: #000">
						&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td colspan="2" bordercolor="#000000" id="category11">
							6&nbsp;<font id="category1">明显安全性</font>
						</td>
						<td colspan="2">
							&nbsp;
						</td>
						<td colspan="2" bordercolor="#000000" id="category22">
							7&nbsp;<font id="category2">明显任务性</font>
						</td>
						<td colspan="2">
							&nbsp;
						</td>
						<td colspan="3" bordercolor="#000000" id="category33">
							8&nbsp;<font id="category3">明显经济性</font>
						</td>
						<td colspan="3">
							&nbsp;
						</td>
						<td colspan="3" bordercolor="#000000" id="category44">
							9&nbsp;<font id="category4">隐蔽安全性</font>
						</td>
						<td >
							&nbsp;
						</td>
						<td colspan="2" bordercolor="#000000" id="category55">
							10&nbsp;<font id="category5">隐蔽任务性</font>
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td colspan="2" bordercolor="#000000" id="category66">
							11&nbsp;<font id="category6">隐蔽经济性</font>
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
				</table>
			</td>
	    <td align="center">1</td>
	    <td align="center">
	    	<input type="radio" name="radio1" value="1" onclick="selectRadio(1,1);"/><font id ="yes1">是</font>&nbsp;
	    	<input type="radio" name="radio1" value="0" onclick="selectRadio(1,0);"/><font id ="no1">否</font>
	    </td>
	    <td>
			<textarea name="area1" id="area1" rows="3" cols="20" ></textarea><a>是否参考AFM</a>
			<input type="radio" name="radio5" value="1" onclick="selectRadio(5,1);"/><font id ="yes2">是</font>&nbsp;
	    	<input type="radio" name="radio5" value="0" onclick="selectRadio(5,0);"/><font id ="no2">否</font>
		</td>
	  </tr>
	  <tr>
	    <td align="center">2</td>
	    <td align="center">
	    	<input type="radio" name="radio2" value="1" onclick="selectRadio(2,1);"/><font id ="yes3">是</font>&nbsp;
	    	<input type="radio" name="radio2" value="0" onclick="selectRadio(2,0);"/><font id ="no3">否</font>
	    </td>
	    <td>
	    	<textarea name="area2" id="area2" rows="3" cols="20" ></textarea>
	    </td>
	  </tr>
	  <tr>
	    <td align="center">3</td>
	 	<td align="center">
	    	<input type="radio" name="radio4" value="1" onclick="selectRadio(4,1);"/><font id ="yes5">是</font>&nbsp;
	    	<input type="radio" name="radio4" value="0" onclick="selectRadio(4,0);"/><font id ="no5">否</font>
	    </td>    
	    <td><textarea name="area4" id="area4" rows="3" cols="20" ></textarea><a>是否参考Mmel</a>
			<input type="radio" name="radio6" value="1" onclick="selectRadio(6,1);"/><font id ="yes6">是</font>&nbsp;
	    	<input type="radio" name="radio6" value="0" onclick="selectRadio(6,0);"/><font id ="no6">否</font>
		</td>
	  </tr>
	  <tr>
	    <td align="center">4</td>
	 	<td align="center">
	    	<input type="radio" name="radio3" value="1" onclick="selectRadio(3,1);"/><font id ="yes4">是</font>&nbsp;
	    	<input type="radio" name="radio3" value="0" onclick="selectRadio(3,0);"/><font id ="no4">否</font>
	    </td>    
	    <td>
			<textarea name="area3" id="area3" rows="3" cols="20" ></textarea>
		</td>
	  </tr>
	   <tr>
	    <td align="center">5</td>
	 	<td align="center">
	    	<input type="radio" name="radio7" value="1" onclick="selectRadio(7,1);"/><font id ="yes7">是</font>&nbsp;
	    	<input type="radio" name="radio7" value="0" onclick="selectRadio(7,0);"/><font id ="no7">否</font>
	    </td>    
	    <td><textarea name="area7" id="area7" rows="3" cols="20"></textarea>
		</td>
	  </tr>
	  <tr>
	    <td colspan="2" align="center"><b id="faultCategory">故障影响类别</b></td>
	    <td>&nbsp;&nbsp;<font id="failureCauseType" color="red"></font></td>	    
	  </tr>
	  <tr>
	  	<td colspan="2" align="center"><b>MMEL</b></td>
	    <td id="mmelValue">&nbsp;</td>
	  </tr>
	  <tr>
	  	<td colspan="2" align="center"><b id="remark">备注</b></td>
	    <td><textarea name="area5" id="area5" rows="3" cols="20" ></textarea>
	  </tr>
	  <tr>
	    <td colspan="3">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" style="border : 0px" class='tabClass'>
				<tr>
					<td width="40" align="left" style="border : 0px"><b id="notice">注意：</b></td>
					<td width="15" align="left" valign="top" style="border : 0px"><b>1、</b></td>
					<td align="left" style="border : 0px"><b id="notice1">有指示或报警的项目归为明显类；</b></td>
				</tr>
				<tr>
					<td style="border : 0px">&nbsp;</td>
					<td align="left" valign="top" style="border : 0px"><b>2、</b></td>
					<td style="border : 0px"><b id="notice2">不常使用的系统归为隐蔽类；</b></td>
				</tr>
				<tr>
					<td style="border : 0px">&nbsp;</td>
					<td align="left" valign="top" style="border : 0px"><b>3、</b></td>
					<td style="border : 0px"><b id="notice3">由保护系统带来的附带故障在其影响使用安全性时，要与其附带故障结合起来考虑。</b></td>
				</tr>
			</table>
		</td>
	  </tr>
	</table>
</div>
<script type="text/javascript">
	var msiId = '${msiId}';
	var isCreateUser = '${isCreateUser}';
	var ataCode = '${showAta.ataCode}';
	var showText = '';
	var m13fId = '';
	var m2Id;
	var afmId;
	var mmelId;
	var isSaveAfm;
	var isSaveMmel;
	var annexName='';
	var annexUrl='';
	var oldM2=[];
</script>
<script src="${contextPath}/sys/sysintertree.js"></script>
<script src="${contextPath}/sys/m_2.js"></script>
<script src="${contextPath}/sys/m_2_1.js"></script>