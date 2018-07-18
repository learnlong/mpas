<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="/common/top.jsp" %>
<%@include file="/common/has_ext_top.jsp" %>
<script type="text/javascript" src="${contextPath}/js/extjs/multicombo.js"></script>
<script src="${contextPath}/lhirf/lhSelect.js"></script>

  <script type="text/javascript">
     isMaintain = '${isMaintain}';
	if(isMaintain =='1'){
	      authorityFlag = true;
	}else{
		  authorityFlag = false;
	}
    var areaCode = '${areaCode}';
    var areaId = '${areaId}';
    var userName = "${SESSION_USER_KEY.userName}";
   
     statusNew = '<%=ComacConstants.ANALYZE_STATUS_NEW%>';
     statusMaintain = '<%=ComacConstants.ANALYZE_STATUS_MAINTAIN%>';
     statusMinOK = '<%=ComacConstants.ANALYZE_STATUS_MAINTAINOK%>';
     statusApproved = '<%=ComacConstants.ANALYZE_STATUS_APPROVED%>';
     
     statusNewShow = '<%=ComacConstants.ANALYZE_STATUS_NEW_SHOW%>';
     statusMaintainShow = '<%=ComacConstants.ANALYZE_STATUS_MAINTAIN_SHOW%>';
     statusMinOKShow = '<%=ComacConstants.ANALYZE_STATUS_MAINTAINOK_SHOW%>';
     statusApprovedShow = '<%=ComacConstants.ANALYZE_STATUS_APPROVED_SHOW%>';
     //表示 没有参见的HSI  N/A
   var  NA = '<%=ComacConstants.EMPTY%>';

      //参数 设置
  	Ext.apply(lhSelect.app, {
			g_lhSelectListUrl : "${contextPath}/lhirf/lhSelect/sesrchLhHsiList.do",
			g_lhSelectSaveUrl : "${contextPath}/lhirf/lhSelect/saveLhHsiList.do",
			g_lhSelectDeleteUrl : "${contextPath}/lhirf/lhSelect/deleteLhSelectHsi.do",
			g_lhSelectModfiHsiAreaCodeUrl : "${contextPath}/lhirf/lhSelect/getHsiModfiyByArea.do",
			g_lhSelectReplaceUrl : "${contextPath}/lhirf/lhSelect/getReplaceHsi.do"
			
	});
	   //开始运行
      var loadOnFlag = 0;	
	  Ext.onReady(lhSelect.app.init,lhSelect.app);	
	  
   </script>
  <body>

  </body>
