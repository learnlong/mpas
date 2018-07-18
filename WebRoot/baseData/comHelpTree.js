function returnRoot() {
	var root = new Ext.tree.TreeNode({
		text : "MPAS",
		expanded : true,
		id : '0'
	});
	
	childSys = new Ext.tree.TreeNode({
		id : "childSys",
		text : "系统分析"
	});
	root.appendChild(childSys);
	
	childSys1 = new Ext.tree.TreeNode({
		id : "msiSelect",
		text : "MSI选择"
	});
	childSys.appendChild(childSys1);
	
	childSys2 = new Ext.tree.TreeNode({
		id : "msiAnalysis",
		text : "MSI分析"
	});
	childSys.appendChild(childSys2);
	
	childSys3 = new Ext.tree.TreeNode({
		id : "m_0",
		text : "m0"
	});
	childSys2.appendChild(childSys3);
	
	childSys4 = new Ext.tree.TreeNode({
		id : "m_11",
		text : "m1.1"
	});
	childSys2.appendChild(childSys4);
	
	childSys5 = new Ext.tree.TreeNode({
		id : "m_12",
		text : "m1.2"
	});
	childSys2.appendChild(childSys5);
	
	childSys6 = new Ext.tree.TreeNode({
		id : "m_13",
		text : "m1.3"
	});
	childSys2.appendChild(childSys6);
	
	childSys7 = new Ext.tree.TreeNode({
		id : "m_2",
		text : "m2"
	});
	childSys2.appendChild(childSys7);
	
	childSys8 = new Ext.tree.TreeNode({
		id : "m_3",
		text : "m3"
	});
	childSys2.appendChild(childSys8);
	
	childSys9 = new Ext.tree.TreeNode({
		id : "m_4",
		text : "m4"
	});
	childSys2.appendChild(childSys9);
	
	childSys10 = new Ext.tree.TreeNode({
		id : "m_5",
		text : "m5"
	});
	childSys2.appendChild(childSys10);
	
	childSys11 = new Ext.tree.TreeNode({
		id : "comReport",
		text : "系统报告管理"
	});
	childSys.appendChild(childSys11);
	
	childSys12 = new Ext.tree.TreeNode({
		id : "recheckmsi",
		text : "复查故障参考原因"
	});
	childSys.appendChild(childSys12);
	
	childSys13 = new Ext.tree.TreeNode({
		id : "recheckafm",
		text : "复查AFM"
	});
	childSys.appendChild(childSys13);
	
	childSys14 = new Ext.tree.TreeNode({
		id : "recheckmmel",
		text : "复查MMEL"
	});
	childSys.appendChild(childSys14);
	
	childSys15 = new Ext.tree.TreeNode({
		id : "is_NoMsi",
		text : "MSI查询"
	});
	childSys.appendChild(childSys15);
	
	childSys16 = new Ext.tree.TreeNode({
		id : "sys_task",
		text : "系统转区域任务查询"
	});
	childSys.appendChild(childSys16);
	
	childStructure = new Ext.tree.TreeNode({
		id : "childStructure",
		text : "结构分析"
	});
	root.appendChild(childStructure);
	
	childStructure1 = new Ext.tree.TreeNode({
		id : "structSelect",
		text : "SSI选择"
	});
	childStructure.appendChild(childStructure1);
	
	childStructure2 = new Ext.tree.TreeNode({
		id : "ssiAnalysis",
		text : "SSI分析"
	});
	childStructure.appendChild(childStructure2);
	
	childStructure3 = new Ext.tree.TreeNode({
		id : "s1",
		text : "S1"
	});
	childStructure2.appendChild(childStructure3);
	
	childStructure4 = new Ext.tree.TreeNode({
		id : "s2",
		text : "S2"
	});
	childStructure2.appendChild(childStructure4);
	
	childStructure5 = new Ext.tree.TreeNode({
		id : "s3",
		text : "S3"
	});
	childStructure2.appendChild(childStructure5);
	
	childStructure6 = new Ext.tree.TreeNode({
		id : "s4",
		text : "S4"
	});
	childStructure2.appendChild(childStructure6);
	
	childStructure7 = new Ext.tree.TreeNode({
		id : "s5",
		text : "S5"
	});
	childStructure2.appendChild(childStructure7);
	
	childStructure8 = new Ext.tree.TreeNode({
		id : "s6",
		text : "S6"
	});
	childStructure2.appendChild(childStructure8);
	
	childStructure9 = new Ext.tree.TreeNode({
		id : "s7",
		text : "S7"
	});
	childStructure2.appendChild(childStructure9);
	
	childStructure10 = new Ext.tree.TreeNode({
		id : "s8",
		text : "S8"
	});
	childStructure2.appendChild(childStructure10);
	
	childStructure11 = new Ext.tree.TreeNode({
		id : "unSsi",
		text : "非重要结构项目的检查要求"
	});
	childStructure2.appendChild(childStructure11);
	
	childStructure12 = new Ext.tree.TreeNode({
		id : "comReport",
		text : "结构报告管理"
	});
	childStructure.appendChild(childStructure12);
	
	childStructure13 = new Ext.tree.TreeNode({
		id : "ssiSearch",
		text : "SSI查询"
	});
	childStructure.appendChild(childStructure13);
	
	childStructure14 = new Ext.tree.TreeNode({
		id : "struct_task",
		text : "结构转区域任务查询"
	});
	childStructure.appendChild(childStructure14);
	
	childArea = new Ext.tree.TreeNode({
		id : "childArea",
		text : "区域分析"
	});
	root.appendChild(childArea);
	
	childArea1 = new Ext.tree.TreeNode({
		id : "areaAnalysis",
		text : "区域分析"
	});
	childArea.appendChild(childArea1);
	
	childArea2 = new Ext.tree.TreeNode({
		id : "za1",
		text : "ZA1"
	});
	childArea1.appendChild(childArea2);
	
	childArea3 = new Ext.tree.TreeNode({
		id : "za2",
		text : "ZA2"
	});
	childArea1.appendChild(childArea3);
	
	childArea4 = new Ext.tree.TreeNode({
		id : "za41",
		text : "ZA41"
	});
	childArea1.appendChild(childArea4);
	
	childArea5 = new Ext.tree.TreeNode({
		id : "za42",
		text : "ZA42"
	});
	childArea1.appendChild(childArea5);
	
	childArea6 = new Ext.tree.TreeNode({
		id : "za43",
		text : "ZA43"
	});
	childArea1.appendChild(childArea6);
	
	childArea8 = new Ext.tree.TreeNode({
		id : "za5",
		text : "ZA5"
	});
	childArea1.appendChild(childArea8);
	
	childArea9 = new Ext.tree.TreeNode({
		id : "za6",
		text : "ZA6"
	});
	childArea1.appendChild(childArea9);
	
	childArea10 = new Ext.tree.TreeNode({
		id : "za7",
		text : "ZA7"
	});
	childArea1.appendChild(childArea10);
	
	childArea11 = new Ext.tree.TreeNode({
		id : "za8",
		text : "ZA8"
	});
	childArea1.appendChild(childArea11);

	childArea12 = new Ext.tree.TreeNode({
		id : "za9",
		text : "ZA9"
	});
	childArea1.appendChild(childArea12);
	
	childArea12 = new Ext.tree.TreeNode({
		id : "comReport",
		text : "区域报告管理"
	});
	childArea.appendChild(childArea12);
	
	childArea13 = new Ext.tree.TreeNode({
		id : "areaCandidate",
		text : "区域合并任务查询"
	});
	childArea.appendChild(childArea13);
	
	childArea14 = new Ext.tree.TreeNode({
		id : "areaTaskTrackSearch",
		text : "区域任务综合查询"
	});
	childArea.appendChild(childArea14);
	
	childLhirf = new Ext.tree.TreeNode({
		id : "childLhirf",
		text : "L/HIRF分析"
	});
	root.appendChild(childLhirf);
	
	childLhirf1 = new Ext.tree.TreeNode({
		id : "lh_0",
		text : "HSI选择"
	});
	childLhirf.appendChild(childLhirf1);
	
	childLhirf2 = new Ext.tree.TreeNode({
		id : "lhAnalysis",
		text : "L/HIRF分析"
	});
	childLhirf.appendChild(childLhirf2);
	
	childLhirf3 = new Ext.tree.TreeNode({
		id : "1h_1",
		text : "LH1"
	});
	childLhirf2.appendChild(childLhirf3);
	
	childLhirf4 = new Ext.tree.TreeNode({
		id : "1h_1a",
		text : "LH1A"
	});
	childLhirf2.appendChild(childLhirf4);
	
	childLhirf5 = new Ext.tree.TreeNode({
		id : "lh_2",
		text : "LH2"
	});
	childLhirf2.appendChild(childLhirf5);
	
	childLhirf6 = new Ext.tree.TreeNode({
		id : "lh_3",
		text : "LH3"
	});
	childLhirf2.appendChild(childLhirf6);
	
	childLhirf7 = new Ext.tree.TreeNode({
		id : "lh_4",
		text : "LH4"
	});
	childLhirf2.appendChild(childLhirf7);
	
	childLhirf8 = new Ext.tree.TreeNode({
		id : "lh_5",
		text : "LH5"
	});
	childLhirf2.appendChild(childLhirf8);
	
	childLhirf9 = new Ext.tree.TreeNode({
		id : "lh_6",
		text : "LH6"
	});
	childLhirf2.appendChild(childLhirf9);
	
	childLhirf10 = new Ext.tree.TreeNode({
		id : "lh_mrb",
		text : "L/HIRF MRB维护"
	});
	childLhirf.appendChild(childLhirf10);
	
	childLhirf11 = new Ext.tree.TreeNode({
		id : "comReport",
		text : "L/HIRF报告管理"
	});
	childLhirf.appendChild(childLhirf11);
	
	childLhirf12 = new Ext.tree.TreeNode({
		id : "searchHsi",
		text : "HSI查询"
	});
	childLhirf.appendChild(childLhirf12);
	
	childLhirf13 = new Ext.tree.TreeNode({
		id : "lh_task",
		text : "L/HIRF转区域任务查询"
	});
	childLhirf.appendChild(childLhirf13);
	
	childMrb = new Ext.tree.TreeNode({
		id : "childMrb",
		text : "MRB"
	});
	root.appendChild(childMrb);
	
	childMrb1 = new Ext.tree.TreeNode({
		id : "mrbMaintain",
		text : "MRB维护"
	});
	childMrb.appendChild(childMrb1);
	
	childMrb2 = new Ext.tree.TreeNode({
		id : "comReport",
		text : "MRB报告管理"
	});
	childMrb.appendChild(childMrb2);
	
	childMpd = new Ext.tree.TreeNode({
		id : "childMpd",
		text : "MPD"
	});
	root.appendChild(childMpd);
	
	childMpd1 = new Ext.tree.TreeNode({
		id : "mpdMaintain",
		text : "MPD维护"
	});
	childMpd.appendChild(childMpd1);
	
	childMpd2 = new Ext.tree.TreeNode({
		id : "comReport",
		text : "MPD报告管理"
	});
	childMpd.appendChild(childMpd2);
	
	childSearch = new Ext.tree.TreeNode({
		id : "childSearch",
		text : "统计/查询"
	});
	root.appendChild(childSearch);
	
	childSearch1 = new Ext.tree.TreeNode({
		id : "msgSearch",
		text : "MSG-3查询"
	});
	childSearch.appendChild(childSearch1);
	
	childSearch2 = new Ext.tree.TreeNode({
		id : "mrbSearch",
		text : "MRB查询"
	});
	childSearch.appendChild(childSearch2);
	
	childSearch3 = new Ext.tree.TreeNode({
		id : "mpdSearch",
		text : "MPD查询"
	});
	childSearch.appendChild(childSearch3);
	
	childApprove = new Ext.tree.TreeNode({
		id : "childApprove",
		text : "审批管理"
	});
	root.appendChild(childApprove);
	
	childApprove1 = new Ext.tree.TreeNode({
		id : "startApprove",
		text : "发起审批"
	});
	childApprove.appendChild(childApprove1);
	
	childApprove2 = new Ext.tree.TreeNode({
		id : "approve",
		text : "审批"
	});
	childApprove.appendChild(childApprove2);
	
	childApprove3 = new Ext.tree.TreeNode({
		id : "approveSearch",
		text : "审批查询"
	});
	childApprove.appendChild(childApprove3);
	
	childApprove4 = new Ext.tree.TreeNode({
		id : "coordinationSearch",
		text : "协调单查询"
	});
	childApprove.appendChild(childApprove4);
	
	childFile = new Ext.tree.TreeNode({
		id : "childFile",
		text : "文档管理"
	});
	root.appendChild(childFile);
	
	childFile1 = new Ext.tree.TreeNode({
		id : "fileManage",
		text : "文档管理"
	});
	childFile.appendChild(childFile1);
	
	childBase = new Ext.tree.TreeNode({
		id : "childBase",
		text : "基础数据管理"
	});
	root.appendChild(childBase);
	
	childBase1 = new Ext.tree.TreeNode({
		id : "comModelSeries",
		text : "机型管理"
	});
	childBase.appendChild(childBase1);
	
	childBase2 = new Ext.tree.TreeNode({
		id : "area",
		text : "区域信息管理"
	});
	childBase.appendChild(childBase2);
	
	childBase3 = new Ext.tree.TreeNode({
		id : "ata",
		text : "ATA信息管理"
	});
	childBase.appendChild(childBase3);
	
	childBase4 = new Ext.tree.TreeNode({
		id : "comVendor",
		text : "供应商信息管理"
	});
	childBase.appendChild(childBase4);
	
	childBase5 = new Ext.tree.TreeNode({
		id : "comMmel",
		text : "MMEL信息管理"
	});
	childBase.appendChild(childBase5);
	
	childBase6 = new Ext.tree.TreeNode({
		id : "comHelp",
		text : "帮助信息管理"
	});
	childBase.appendChild(childBase6);
	
	childParam = new Ext.tree.TreeNode({
		id : "childParam",
		text : "参数自定义管理"
	});
	root.appendChild(childParam);
	
	childParam1 = new Ext.tree.TreeNode({
		id : "cusBaseCrackLen",
		text : "自定义基本裂纹长度"
	});
	childParam.appendChild(childParam1);
	
	childParam2 = new Ext.tree.TreeNode({
		id : "s45Matrix",
		text : "自定义结构参数"
	});
	childParam.appendChild(childParam2);
	
	childParam3 = new Ext.tree.TreeNode({
		id : "structureGrade",
		text : "自定义结构评级表"
	});
	childParam.appendChild(childParam3);
	
	childParam4 = new Ext.tree.TreeNode({
		id : "increaseRegionParam",
		text : "自定义增强区域参数"
	});
	childParam.appendChild(childParam4);
	
	childParam5 = new Ext.tree.TreeNode({
		id : "standardRegionParam",
		text : "自定义标准区域参数"
	});
	childParam.appendChild(childParam5);
	
	childParam6 = new Ext.tree.TreeNode({
		id : "lhirfParam",
		text : "自定义L/HIRF分析参数"
	});
	childParam.appendChild(childParam6);
	
	/**
	childParam7 = new Ext.tree.TreeNode({
		id : "mrbrReport",
		text : "自定义MRBR报告"
	});
	childParam.appendChild(childParam7);
	
	childParam8 = new Ext.tree.TreeNode({
		id : "mpdReport",
		text : "自定义MPD报告"
	});
	childParam.appendChild(childParam8);
	*/

	childUser = new Ext.tree.TreeNode({
		id : "childUser",
		text : "用户管理"
	});
	root.appendChild(childUser);
	
	childUser1 = new Ext.tree.TreeNode({
		id : "userManage",
		text : "用户管理"
	});
	childUser.appendChild(childUser1);
	
	childUser2 = new Ext.tree.TreeNode({
		id : "professionManage",
		text : "专业室管理"
	});
	childUser.appendChild(childUser2);
	
	childUser3 = new Ext.tree.TreeNode({
		id : "professionUser",
		text : "专业室用户分配"
	});
	childUser.appendChild(childUser3);
	
	childUser4 = new Ext.tree.TreeNode({
		id : "professionAuthority",
		text : "专业室权限分配"
	});
	childUser.appendChild(childUser4);
	
	childUser5 = new Ext.tree.TreeNode({
		id : "userAuthority",
		text : "用户权限分配"
	});
	childUser.appendChild(childUser5);
	return root;
}