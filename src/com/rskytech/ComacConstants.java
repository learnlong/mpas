package com.rskytech;

public final class ComacConstants {

	// SESSION中保存的用户信息的KEY
	public static final String SESSION_USER_KEY = "SESSION_USER_KEY";
	// SESSION中保存的首页上机型选择的KEY
	public static final String SESSION_MODEL_SERIES_LIST = "SESSION_MODEL_SERIES_LIST";
	// SESSION中保存的当前机型
	public static final String SESSION_NOW_MODEL_SERIES = "SESSION_NOW_MODEL_SERIES";
	
	/*
	 *允许上传的文件类型 
	 */
	public static final String ALLOW_DOC_FILE_TYPE = "application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,text/plain,application/octet-stream";

	/**
	 * 正则表达式关于文件后缀判断
	 */
	public static final String REG_FILE_SUFFIX="^.*.((pdf)|(doc)|(docx)|(txt))$";

	/**
	 * 允许上传的文件大小10M
	 * @return
	 */
	public static final long ALLOW_DOC_FILE_SIZE = 10485760;
	// 有效标志
	public static final int VALIDFLAG_YES = 1; // 有效
	public static final int VALIDFLAG_NO = 0; // 无效
	
	// DB操作的区分
	public static final String DB_DELETE = "delete";//删除
	public static final String DB_INSERT = "insert";//新增
	public static final String DB_UPDATE = "update";//更新
	
	// 四大分析的区分（数据库）
	public static final String ZONAL_CODE = "ZONE"; // 区域分析系统
	public static final String LHIRF_CODE = "LHIRF"; // LHIRF分析系统
	public static final String STRUCTURE_CODE = "STRUCTURE";// 结构分析
	public static final String SYSTEM_CODE = "SYS"; // 系统分析系统
	
	// 四大分析的区分（页面显示）
	public static final String ZONAL_SHOW = "区域"; // 区域分析系统
	public static final String LHIRF_SHOW = "L/HIRF"; // LHIRF分析系统
	public static final String STRUCTURE_SHOW = "结构";// 结构分析
	public static final String SYSTEM_SHOW = "系统"; // 系统分析系统
	
	// 四大分析的状态区分（数据库）
	public static final String ANALYZE_STATUS_NEW = "NEW"; // 新生成分析
	public static final String ANALYZE_STATUS_MAINTAIN = "MAINTAIN"; // 正在分析
	public static final String ANALYZE_STATUS_MAINTAINOK = "MAINTAINOK"; // 分析已完成
	public static final String ANALYZE_STATUS_APPROVED = "APPROVED"; // 审批完成
	public static final String ANALYZE_STATUS_HOLD = "HOLD"; // 版本冻结
	
	// 四大分析的状态区分（页面显示）
	public static final String ANALYZE_STATUS_NEW_SHOW = "新建"; // 新生成分析
	public static final String ANALYZE_STATUS_MAINTAIN_SHOW = "分析中"; // 正在分析
	public static final String ANALYZE_STATUS_MAINTAINOK_SHOW = "分析完成"; // 分析已完成
	public static final String ANALYZE_STATUS_APPROVED_SHOW = "审批完成"; // 审批完成
	public static final String ANALYZE_STATUS_HOLD_SHOW = "版本冻结"; // 版本冻结
	
	//流程状态code
	public static final String PROCESS_STATUS_WAIT_CHECK = "WAITCHECK";//待审批
	public static final String PROCESS_STATUS_CHECKING = "CHECKING";//审批中
	public static final String PROCESS_STATUS_FINISH_CHECK = "FINISHCHECK";//审批完成
	public static final String PROCESS_STATUS_CANCEL_CHECK = "CANCELCHECK";//取消审批
	//流程状态show
	public static final String PROCESS_STATUS_WAIT_CHECK_SHOW = "待审批";//待审批
	public static final String PROCESS_STATUS_CHECKING_SHOW = "审批中";//审批中
	public static final String PROCESS_STATUS_FINISH_CHECK_SHOW = "审批完成";//审批完成
	public static final String PROCESS_STATUS_CANCEL_CHECK_SHOW = "取消审批";//取消审批
	//审核状态
	public static final int PROCESS_CHECK_STATUS_NULL = 0;//未审核
	public static final int PROCESS_CHECK_STATUS_YES = 1;//审核通过
	public static final int PROCESS_CHECK_STATUS_NO = 2;//审核不通过
	public static final int PROCESS_CHECK_STATUS_CANCEL = 3;//取消审核
	public static final String PROCESS_CHECK_STATUS_NULL_SHOW = "未审核";//未审核
	public static final String PROCESS_CHECK_STATUS_YES_SHOW = "审核通过";//审核通过
	public static final String PROCESS_CHECK_STATUS_NO_SHOW = "审核不通过";//审核不通过
	public static final String PROCESS_CHECK_STATUS_CANCEL_SHOW = "取消审核";//审核不通过
	
	
	// 分析状态的颜色区分
	public static final String ANALYZE_COLOR_NEW = ""; // 新生成分析
	public static final String ANALYZE_COLOR_MAINTAIN = "blue"; // 正在分析——蓝色
	public static final String ANALYZE_COLOR_MAINTAINOK = "green"; // 分析已完成——绿色
	public static final String ANALYZE_COLOR_APPROVED = "red"; // 审批完成——红色
	
	//四大分析的分析步骤区分
	public static final Integer STEP_NO = 0;//未完成
	public static final Integer STEP_FINISH = 1;//已经完成
	public static final Integer STEP_NOW = 2;//正在分析
	public static final Integer STEP_INVALID = 3;//无效
	//用户ID
	public static final String USER_ID_ADMIN = "1";
	//专业室ID
	public static final String PROEFSSION_ID_ADMIN = "1";//专业室-id--超级管理
	public static final String PROEFSSION_ID_ZONGBAO = "2";//专业室--id-综保室
	//职务ID
	public static final String POSITION_ID_SUPER_ADMIN = "1";//职位--id--超级管理员
	public static final String POSITION_ID_PROFESSION_ADMIN = "2";//职位--id--专业室管理员
	public static final String POSITION_ID_PROFESSION_ENGINEER = "3";//职位--id--专业室总工程师
	public static final String POSITION_ID_PROFESSION_ANAYIST = "4";//职位--id--专业室分析人员
	
	/**
	 * 自定义基本裂纹长度中矩阵的标识名字
	 * 值:s3
	 */
	public static final String CUSBASECRACKLEN_ANAFLG = "s3";
	/**
	 * 自定义评级内容列表的"评级名称"(中文)
	 * 值:稠密度等级
	 */
	public static final String CONGESTION_RATING = "稠密度等级";
	/**
	 * 自定义评级内容列表的"评级名称"(中文)
	 * 值:目视等级
	 */
	public static final String VIEWING_RATING = "目视等级";
	/**
	 * 自定义评级内容列表的"评级名称"(中文)
	 * 值:尺寸等级
	 */
	public static final String SIZE_RATING = "尺寸等级";
	/**
	 * 自定义评级内容列表的"评级名称"(中文)
	 * 值:光照等级
	 */
	public static final String LIGHTING_RATING = "光照等级";
	/**
	 * 自定义评级内容列表的"评级名称"(中文)
	 * 值:表面等级
	 */
	public static final String SURFACE_RATING = "表面等级";
	
	/**
	 * 自定义基本裂纹长度中矩阵的标识排行
	 * 值:4
	 */
	public static final Integer CUSBASECRACKLEN_MATRIXFLG = 4;
	
	/**
	 * 自定义基本裂纹长度中矩阵行名(中文)
	 * 值:条件等级
	 */
	public static final String CONDITION_RATING_NAME = "条件等级";
	
	/**
	 * 自定义基本裂纹长度中矩阵列名(中文)
	 * 值:实用性等级
	 */
	public static final String PRACTICALITY_RATING_NAME = "实用性等级";

	/*
	 * 自定义矩阵S4,S5,LH4的最大项目数量 
	 */
	public static final Integer S45_ALLITEMMAXCOUNT = 24;
	public static final Integer S45_FIRSTITEMMAXCOUNT = 12;
	/*
	 * 自定义矩阵树的0级节点的id  
	 */
	public static final String MATRIX_ZEROLEVELID = "0";
	/*
	 * CusEdrAdr表中数据是否完整
	 */	
	public static final int CUSEDRADR_Full_YES = 1;
	public static final int CUSEDRADR_Full_NO = 0;
	
	/**
	 * MPD 附录上传路径
	 */
	public static final String MPD_PS_SAVE_PATH = "/paramDefineManage/mpd_ps/";
	/**
	 * MPD pdf文件生成后存放路径
	 */
	public static final String MPD_PS_PDF_PATH = "/paramDefineManage/mpd_pdf/";
	/**
	 * MPD WORD文件生成后存放路径
	 */
	public static final String MPD_PS_WORD_PATH = "/paramDefineManage/word/";
	
	/**
	 * 指定文档管理中文件上传位置，在此位置下按目录进行划分
	 */
	public static final String SYSDOCFILEFOLDER = "/file";
	
	/**
	 * 系统
	 */
	public static final String SYS_CN = "系统";
	/**
	 * 结构
	 */
	public static final String STRUCTURE_CN = "结构";
	/**
	 * 区域
	 */
	public static final String AREA_CN = "区域";
	/**
	 * L/Hirf
	 */
	public static final String LHIRF = "L/Hirf";
	public static final String LHIRF_CN = "L/Hirf";
	public static final String LHIRF_UP = "L/HIRF";
	/**
	 * MRB和MPD版本表 类型
	 */
	public static final String TASK_MPD_VERSION_TYPE_MPD = "MPD";
	public static final String TASK_MPD_VERSION_TYPE_MRB = "MRB";
	public static final String TASK_MPD_VERSION_TYPE_MSG = "MSG";
	// 有效标志
	public static final int VALIDFLAG_TWO = 2; // 有效

	/**
	 * MPD/MRB 版本管理
	 */
	public static final String MPD = "MPD";
	public static final String MRB = "MRB";
	public static final String STATUS_TEST = "测试报告";
	public static final String STATUS_FORMAL = "正式报告";
	
	public static final String STATUS_DB_TEST = "0";
	public static final String STATUS_DB_FORMAL = "1";
	
	/**
	 * 版本状态
	 */
	public static final Integer NEW_VERSION = 1; // 新版本
	
	public static final Integer OLD_VERSION = 2; // 老版本
	
	public static final String ALL = "ALL";
	
	/**
	 * MRB的psFlg
	 */
	public static final Integer PSFLG_0= 0;//报表首页
	public static final Integer PSFLG_1= 1;//报表附录
	
	/**
	 * MRB的固定章
	 */
	public static final String[] MRBCHAPTERCODE = new String[]{"SOC","LEP","SECTION1","SECTION2","SECTION3","SECTION4"};
	public static final String[] MRBCHAPTERNAMECN = new String[]{"更改摘要","有效页清单","第一章","第二章","第三章","第四章"};
	
	
	/**
	 * 区域检查间隔值中文
	 */
	public static final String INTEVAL_TITLE_CN = "区域检查间隔";
	public static final String INNER_TITLE_CN = "内部";
	
	public static final String OUTTER_TITLE_CN = "外部";
	
	/**
	 * 内部flg
	 */
	public static final String INNER = "IN";
	
	/**
	 * 外部flg
	 */
	public static final String OUTTER = "OUT";
	/**
	 * LH4
	 */
	public static final String LH4 = "LH4";
	/**
	 * LH5
	 */
	public static final String LHIRF_LH5 = "LH5";
	/*
	 * 环境损伤
	 */
	public static final String ED = "ED";
	
	/*
	 * 环境损伤(中文)
	 */
	public static final String ED_CN = "环境损伤";
	
	/*
	 * 偶然损伤
	 */
	public static final String AD = "AD";
	
	/*
	 * 偶然损伤(中文)
	 */
	public static final String AD_CN = "偶然性损伤";
	
	/**
	 * 区域分析步骤的标识;
	 * 值:ZA43
	 */
	public static final String ZA43 = "ZA43";
	/**
	 * ZA5
	 */
	public static final String ZA5 = "ZA5";
	
	/**
	 * S4A
	 */
	public static final String S4A = "S4A";
	
	/**
	 * S4B
	 */
	public static final String S4B = "S4B";
	
	/**
	 * S5A
	 */
	public static final String S5A = "S5A";
	
	/**
	 * S5B
	 */
	public static final String S5B = "S5B";
	/**
	 * 第一副矩阵
	 */
	public static final Integer FIRST_MATRIX = 1;
	
	/**
	 * 第二副矩阵
	 */
	public static final Integer SECOND_MATRIX = 2;
	
	/**
	 * 检查间隔矩阵
	 */
	public static final Integer FINAL_MATRIX = 3;
	
	/**
	 * 下划线
	 */
	public static final String UNDER_LINE = "_";

	
	/**
	 * 是
	 * type Integer
	 */
	public static final Integer YES = 1;
	
	/**
	 * 否
	 * type Integer
	 */
	public static final Integer NO = 0;
	
	/**
	 * 用户密码初始化值
	 */
	public static final String DEFAULT_PASSWORD = "000000";
	
	//msg-3任务类型
	public static final String BAOYANG= "保养";
	public static final String JIANCHA= "使用检查";
	public static final String JIANKONG= "操作人员监控";
	public static final String JIANCE = "功能检测";
	public static final String CHAIXIU = "定时拆修";
	public static final String BAOFEI = "定时报废";
	public static final String ZONGHE = "综合工作";
	
	public static final String GVI = "GVI";
	public static final String DET = "DET";
	public static final String SDI = "SDI";
	public static final String FNC = "FNC";
	public static final String DIS = "DIS";
	public static final String RST = "RST";
	
	/**
	 * 空:N/A
	 */
	public static final String EMPTY = "N/A";
	/**
	 * 图文混排图片上传路径
	 */
	public static final String IMAGE_SAVE_PATH = "/upload/image/";
	/**
	 * 文件上传路径
	 */
	public static final String FILE_SAVE_PATH = "/upload/file/";
	/**
	 * 允许上传的图片类型
	 */
	public static final String ALLOW_IMAGE_FILE_TYPE = "image/gif,image/jpeg,image/pjpeg,image/bmp,image/png,image/x-png";
	/**
	 * 允许上传的图片大小,5M
	 */
	public static final long ALLOW_IMAGE_FILE_SIZE = 5242880;
	
	/**
	 * TASK_MSG 任务有效性
	 * 值:0(转ATA20)
	 */	
	public static final Integer TASK_VALID_TOATA20 = 0;
	
	/**
	 * TASK_MSG 任务有效性
	 * 值:1(被合并)
	 */
	public static final Integer TASK_VALID_MERGER = 1;
	/**
	 * TASK_MSG 任务有效性
	 * 值:2(被区域接收)
	 */
	public static final Integer TASK_VALID_AREAACCPET = 2;
	/**
	 * TASK_MSG 任务有效性
	 * 值:3(临时)
	 */
	public static final Integer TASK_VALID_TEMPORARY = 3;
	/**
	 * 选择
	 */
	public static final String CHOOSE = "CHOOSE";
	/**
	 * 分析
	 */
	public static final String ANALYSIS = "ANALYSIS";
	/**
	 * 报告
	 */
	public static final String REPORT = "REPORT";
	//报告装
	public static final String REPORT_STATUS_NEW = "NEW";//新建
	public static final String REPORT_STATUS_NEW_SHOW = "新建";//新建
	
	//增强区域和标准区域任务的区分代码
	public static final String AREA_ZENGQIANG = "EZL";
	public static final String AREA_BIAOZHUN = "ZL";
	/**
	 * 报表文件文件生成路径
	 */
	public static final String REPORT_FILE_PATH = "/download/";
}
