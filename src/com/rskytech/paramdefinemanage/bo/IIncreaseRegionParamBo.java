package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;

public interface IIncreaseRegionParamBo extends IBo{

	public List<CusItemS45> searchZa43Item(String modelSeriesId,String itemFlg);
	public boolean isFinalMatrixExist(String modelSeriesId);
	public List<CusInterval> searchFinalMatrix(String modelSeriesId);
	public boolean isFirstMatrixExist(String modelSeriesId);
	public List<CusMatrix> searchFirstMatrix(String modelSeriesId);
	public List<CusLevel> getLevelList(String itemId,String stepFlg,String modelSeriesId) ;
	public Integer isLevelNumberSame(String modelSeriesId);
	public List<CusMatrix> generateFirstMatrix(ComModelSeries modelSeries,
			String userId,String edAlgorithm,String adAlgorithm) ;
	public List<CusInterval> generateFinalMatrix(ComModelSeries modelSeries,
			String userId, List<CusMatrix> queryList, List<Integer> valueList) ;
	public boolean isExistMatrixAnalyze(String modelSeriesId, String step) ;
	public void updateFinalMatrix(List<CusInterval> queryList, String userId,
			ComModelSeries modelSeries);
	public void deleteMatrix(String modelSeriesId,String anaFlg,Integer matrixFlg);
	
	public void deleteFinalMatrix(String modelSeriesId,String anaFlg);
	
	
	
	
}
