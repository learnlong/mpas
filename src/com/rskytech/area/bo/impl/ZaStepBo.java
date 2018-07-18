package com.rskytech.area.bo.impl;

import java.util.List;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa1Dao;
import com.rskytech.area.dao.IZa43Dao;
import com.rskytech.area.dao.IZaStepDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;
import com.rskytech.task.dao.ITaskMsgDao;

public class ZaStepBo extends BaseBO implements IZaStepBo {

	private IZaStepDao zaStepDao;
	private IZa1Dao za1Dao;
	private IZa43Dao za43Dao;
	private ITaskMsgDao taskMsgDao;
	
	public ZaStep selectZaStep(String userId, ZaMain zaMain, String pageName){
		ZaStep step = zaStepDao.getZaStep(zaMain.getZaId());
		if (step == null){
			step = new ZaStep();
			step.setZaMain(zaMain);
			setStepValue(step, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
			this.saveOrUpdate(step, ComacConstants.DB_INSERT, userId);
		}
		step.setPageName(pageName);
		step.setAreaStatus(zaMain.getStatus());
		return step;
	}
	
	public void updateZa1StepAndStatus(String userId, String zaId, String type){
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		ZaStep step = zaStepDao.getZaStep(zaId);
		
		if ("no".equals(type)){//不需要区域分析
			setStepValue(step, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3);
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
		} else if ("biaozhun".equals(type)){//标准区域分析
			setStepValue(step, 1, 2, 3, 3, 3, 0, 0, 0, 0, 1, 1);
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		} else {//标准区域分析和增强区域分析
			setStepValue(step, 1, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1);
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		}
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, userId);
		this.saveOrUpdate(zaMain, ComacConstants.DB_UPDATE, userId);
	}
	
	public Integer updateZa2StepAndStatus(String userId, String zaId, Integer position){
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		ZaStep step = zaStepDao.getZaStep(zaId);
		
		ComArea area = zaMain.getComArea();
		String msId = area.getComModelSeries().getModelSeriesId();
		String areaId = area.getAreaId();
		
		Integer za41 = null;
		Integer za5a = null;
		Integer za5b = null;
		Integer za6 = null;
		Integer za7 = null;
		
		za41 = judgeStepValue(step.getZa41(), 2, 1, 2, 3);
		
		if ((za41 == 1 && step.getZa42() == 1 && step.getZa43() == 1) || za41 == 3){
			if (position == 1){//内部
				za5a = judgeStepValue(step.getZa5a(), 2, 1, 2, 2);
				za5b = 3;
			} else if (position == 2){//外部
				za5a = 3;
				za5b = judgeStepValue(step.getZa5b(), 2, 1, 2, 2);
			} else if (position == 3){//内部和外部
				za5a = judgeStepValue(step.getZa5a(), 2, 1, 2, 2);
				za5b = judgeStepValue(step.getZa5b(), 2, 1, 2, 2);
			}
		} else {
			if (position == 1){//内部
				za5a = 0;
				za5b = 3;
			} else if (position == 2){//外部
				za5a = 3;
				za5b = 0;
			} else if (position == 3){//内部和外部
				za5a = 0;
				za5b = 0;
			}
		}
		
		if ((za5a == 1 && za5b == 3) || (za5a == 3 && za5b == 1) || (za5a == 1 && za5b == 1)){//分析完成
			List<TaskMsg> listZa6 = taskMsgDao.getZengQiangAreaTaskNoAccept(msId, zaId);
			if (listZa6 == null || listZa6.size() == 0){//增强任务已经转移或合并
				za6 = 1;
				List<Object[]> listZa7 = taskMsgDao.getToAreaTaskNoAccept(msId, areaId);
				if (listZa7 == null || listZa7.size() == 0){//转区域任务都已经处理
					za7 = 1;
					zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
				} else {//有转区域任务还没有处理
					za7 = 2;
					zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				}
			} else {//有增强任务还没有转移或合并
				za6 = 2;
				za7 = 0;
				zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
			}			
		} else {//ZA5还没有分析完成
			za6 = 0;
			za7 = 0;
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		}
				
		setStepValue(step, null, 1, za41, null, null, za5a, za5b, za6, za7, null, null);
		
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, userId);
		this.saveOrUpdate(zaMain, ComacConstants.DB_UPDATE, userId);
		
		if (za41 == 3){//标准区域分析
			if (za5a == 3){//不需要分析ZA5A
				return 6;//跳转到ZA5B页面
			} else {//需要分析ZA5A
				return 5;//跳转到ZA5A页面
			}
		} else {//增强区域分析
			return 2;//跳转到ZA1页面
		}
	}
	
	public Integer updateZa41StepAndStatus(String userId, String zaId, Integer rstTask){
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		String msId = zaMain.getComModelSeries().getModelSeriesId();
		
		ZaStep step = zaStepDao.getZaStep(zaId);
		
		Integer za5a = null;
		Integer za5b = null;
		
		List<TaskMsg> sixList = taskMsgDao.findAreaTaskMsg(msId, zaId, "ZA_4_2", "6");
		if (sixList == null || sixList.size() == 0){//没有问题6的增强区域任务，表示ZA42还没有被分析，因为只要分析了ZA42，就一定有一条问题6的GVI任务
			za5a = judgeStepValue(step.getZa5a(), 0, 0, 0, 3);
			za5b = judgeStepValue(step.getZa5b(), 0, 0, 0, 3);
			setStepValue(step, null, null, 1, 2, 0, za5a, za5b, 0, 0, null, null);
		} else {//已经分析过ZA42
			if (rstTask == 1){//新增RST任务
				za5a = judgeStepValue(step.getZa5a(), 0, 0, 0, 3);
				za5b = judgeStepValue(step.getZa5b(), 0, 0, 0, 3);
				setStepValue(step, null, null, 1, 1, 2, za5a, za5b, 0, 0, null, null);
				zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				this.saveOrUpdate(zaMain, ComacConstants.DB_UPDATE, userId);
			} else if (rstTask == 2){//删除RST任务
				Integer za43 = 1;
				List<TaskMsg> list = taskMsgDao.getZengQiangAreaTask(msId, zaId);
				for (TaskMsg tm : list){
					if (tm.getTaskCode() == null || "".equals(tm.getTaskCode())){//还没有进行ZA43分析
						za43 = 2;
						za5a = judgeStepValue(step.getZa5a(), 0, 0, 0, 3);
						za5b = judgeStepValue(step.getZa5b(), 0, 0, 0, 3);
						break;
					}
				}
				
				setStepValue(step, null, null, 1, 1, za43, za5a, za5b, null, null, null, null);
				//因为只有做完增强分析，才能做标准分析，所以删除RST任务后，以前的分析是“分析完成”，那么删除后对分析状态没有影响，还是“分析完成”，
				//以前的分析是“分析中”，那么因为该任务是直接转移ATA20的，所以删除后对分析状态也没有影响，还是“分析中”，
				//因此状态不变
			}
		}
		
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, userId);
		return 3;
	}
	
	public Integer updateZa42StepAndStatus(String userId, String zaId, Integer taskStatus){
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		String msId = zaMain.getComModelSeries().getModelSeriesId();
		
		ZaStep step = zaStepDao.getZaStep(zaId);
		
		Integer za43 = 1;
		Integer za5a = null;
		Integer za5b = null;
		Integer za6 = null;
		Integer za7 = null;
		
		if (taskStatus == 1){//新增
			za43 = 2;
			za5a = judgeStepValue(step.getZa5a(), 0, 0, 0, 3);
			za5b = judgeStepValue(step.getZa5b(), 0, 0, 0, 3);
			za6 = judgeStepValue(step.getZa6(), 0, 0, 0, 3);
			za7 = judgeStepValue(step.getZa7(), 0, 0, 0, 3);
			setStepValue(step, null, null, 1, 1, 2, za5a, za5b, za6, za7, null, null);
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		} else if (taskStatus == 2){//删除
			List<TaskMsg> list = taskMsgDao.getZengQiangAreaTask(msId, zaId);
			for (TaskMsg tm : list){
				if (tm.getTaskCode() == null || "".equals(tm.getTaskCode())){//还没有进行ZA43分析
					za43 = 2;
					za5a = judgeStepValue(step.getZa5a(), 0, 0, 0, 3);
					za5b = judgeStepValue(step.getZa5b(), 0, 0, 0, 3);
					za6 = judgeStepValue(step.getZa6(), 0, 0, 0, 3);
					za7 = judgeStepValue(step.getZa7(), 0, 0, 0, 3);
					zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
					break;
				}
			}
			if (za43 == 1){//完成ZA43分析
				if ((step.getZa5a() == 1 && step.getZa5b() == null) || (step.getZa5a() == null && step.getZa5b() == 1) || 
						(step.getZa5a() == 1 && step.getZa5b() == 1)){//标准分析完成
					List<TaskMsg> za6list = taskMsgDao.getZengQiangAreaTaskNoAccept(msId, zaId);
					if (za6list == null || za6list.size() == 0){//增强任务已经转移或合并
						za6 = 1;
						List<Object[]> listZa7 = taskMsgDao.getToAreaTaskNoAccept(msId, zaMain.getComArea().getAreaId());
						if (listZa7 == null || listZa7.size() == 0){//转区域任务都已经处理
							za7 = 1;
							zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
						} else {//有转区域任务还没有处理
							za7 = 2;
							zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
						}
					} else {
						za6 = 2;
						za7 = 0;
						zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
					}
				} else {//标准分析没有完成
					za6 = 0;
					za7 = 0;
					zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				}
			}
		} else if (taskStatus == 0){
			za43 = null;
		}
		
		setStepValue(step, null, null, 1, 1, za43, za5a, za5b, za6, za7, null, null);
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, userId);
		this.saveOrUpdate(zaMain, ComacConstants.DB_UPDATE, userId);
		return 4;
	}
	
	public Integer updateZa43StepAndStatus(String userId, String msId, String zaId){
		ZaStep step = zaStepDao.getZaStep(zaId);
		List<TaskMsg> list = za43Dao.getTaskListNoAna(msId, zaId);
		
		Integer za5a = null;
		Integer za5b = null;
		Integer nextStep = null;
		if (list == null || list.size() == 0){			
			za5a = judgeStepValue(step.getZa5a(), 2, 1, 2, 3);
			za5b = judgeStepValue(step.getZa5b(), 2, 1, 2, 3);
			setStepValue(step, null, null, null, null, 1, za5a, za5b, null, null, null, null);
			
			if (za5a == 3){
				nextStep = 6;
			} else {
				nextStep = 5;
			}
		} else {
			setStepValue(step, null, null, null, null, 2, null, null, null, null, null, null);
			nextStep = 4;
		}
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, userId);		
		return nextStep;
	}
	
	public Integer updateZa5StepAndStatus(String userId, String msId, String zaId, String za5Step){
		ZaStep step = zaStepDao.getZaStep(zaId);
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		
		Integer isFinish = 0;//是否完成标准分析
		Integer za5a = null;
		Integer za5b = null;
		Integer za6 = null;
		Integer za7 = null;
		Integer nextStep = null;

		if ("ZA5A".equals(za5Step)){
			za5a = 1;
			za5b = judgeStepValue(step.getZa5b(), 2, 1, 2, 3);
			
			if (za5b == 3 || za5b == 1){
				isFinish = 1;
			}
		} else {
			za5b = 1;
			isFinish = 1;
		}
		
		if (isFinish == 1){//完成标准分析
			List<TaskMsg> za6list = taskMsgDao.getZengQiangAreaTaskNoAccept(msId, zaId);
			if (za6list == null || za6list.size() == 0){//增强任务已经转移或合并
				za6 = 1;
				List<Object[]> listZa7 = taskMsgDao.getToAreaTaskNoAccept(msId, zaMain.getComArea().getAreaId());
				if (listZa7 == null || listZa7.size() == 0){//转区域任务都已经处理
					za7 = 1;
					zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
				} else {//有转区域任务还没有处理
					za7 = 2;
					zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				}
			} else {
				za6 = 2;
				za7 = 0;
				zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
			}
		}
		setStepValue(step, null, null, null, null, null, za5a, za5b, za6, za7, null, null);
		
		if ("ZA5A".equals(za5Step)){
			if (za5b == 3){
				nextStep = 7;
			} else {
				nextStep = 6;
			}
		} else {
			nextStep = 7;
		}
		
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, userId);
		this.saveOrUpdate(zaMain, ComacConstants.DB_UPDATE, userId);
		return nextStep;
	}
	
	public Integer updateZa6StepAndStatus(String userId, String msId, ZaMain zaMain){
		ZaStep step = zaStepDao.getZaStep(zaMain.getZaId());
		
		Integer za6 = null;
		Integer za7 = null;
		
		List<TaskMsg> za6list = taskMsgDao.getZengQiangAreaTaskNoAccept(msId, zaMain.getZaId());
		if (za6list == null || za6list.size() == 0){//增强任务已经转移或合并
			za6 = 1;
			List<Object[]> listZa7 = taskMsgDao.getToAreaTaskNoAccept(msId, zaMain.getComArea().getAreaId());
			if (listZa7 == null || listZa7.size() == 0){//转区域任务都已经处理
				za7 = 1;
				zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			} else {//有转区域任务还没有处理
				za7 = 2;
				zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
			}
		} else {
			za6 = 2;
			za7 = 0;
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		}
		setStepValue(step, null, null, null, null, null, null, null, za6, za7, null, null);
		
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, userId);
		this.saveOrUpdate(zaMain, ComacConstants.DB_UPDATE, userId);
		return za7;
	}
	
	public Integer updateZa7StepAndStatus(String userId, String msId, ZaMain zaMain){
		ZaStep step = zaStepDao.getZaStep(zaMain.getZaId());
		
		Integer za7 = null;
		
		List<Object[]> listZa7 = taskMsgDao.getToAreaTaskNoAccept(msId, zaMain.getComArea().getAreaId());
		if (listZa7 == null || listZa7.size() == 0){//转区域任务都已经处理
			za7 = 1;
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
		} else {//有转区域任务还没有处理
			if (step.getZa6() == 1){//为了转区域任务的状态和步骤变更用
				za7 = 2;
				zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
			} else {
				za7 = 0;
				zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
			}
		}
		setStepValue(step, null, null, null, null, null, null, null, null, za7, null, null);
		
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, userId);
		this.saveOrUpdate(zaMain, ComacConstants.DB_UPDATE, userId);
		return za7;
	}
	
	public Integer judgeStepValue(Integer nowValue, Integer no, Integer finish, Integer now, Integer invalid){
		switch (nowValue){
		case 0 : 
			return no;
		case 1 : 
			return finish;
		case 2 : 
			return now;
		case 3 : 
			return invalid;
		default :
			return null;
		}
	}
	
	public void setStepValue(ZaStep step, Integer za1, Integer za2, Integer za41, Integer za42, 
			Integer za43, Integer za5a, Integer za5b, Integer za6, Integer za7, Integer za8, Integer za9){
		if (za1 != null) step.setZa1(za1);
		if (za2 != null) step.setZa2(za2);
		if (za41 != null) step.setZa41(za41);
		if (za42 != null) step.setZa42(za42);
		if (za43 != null) step.setZa43(za43);
		if (za5a != null) step.setZa5a(za5a);
		if (za5b != null) step.setZa5b(za5b);
		if (za6 != null) step.setZa6(za6);
		if (za7 != null) step.setZa7(za7);
		if (za8 != null) step.setZa8(za8);
		if (za9 != null) step.setZa9(za9);		
	}

	public IZaStepDao getZaStepDao() {
		return zaStepDao;
	}

	public void setZaStepDao(IZaStepDao zaStepDao) {
		this.zaStepDao = zaStepDao;
	}

	public IZa1Dao getZa1Dao() {
		return za1Dao;
	}

	public void setZa1Dao(IZa1Dao za1Dao) {
		this.za1Dao = za1Dao;
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

	public IZa43Dao getZa43Dao() {
		return za43Dao;
	}

	public void setZa43Dao(IZa43Dao za43Dao) {
		this.za43Dao = za43Dao;
	}
	
}
