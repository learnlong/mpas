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
<body>
	<div id="headerStepDiv">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="header">
		<tr>
			<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td><b>非SSI号：${siCode }</b></td>
						<td><b>非SSI名称：${siTitle }</b></td>
						<td><b></b></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</div>
</body>
