package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.CusDisplay;
import com.rskytech.pojo.CusMatrix;

public interface IDefineBaseCrackLenBo extends IBo {

	/**
	 * 查询CUS_Display表数据
	 * @param modelSeriesId
	 * @return List<CusDisplay>
	 */
	public List<CusDisplay> getCusBaseCrackLenGradeList(String modelSeriesId);
	
	/**
	 * 查询CUS_CRACK表数据
	 * @param modelSeriesId
	 * @return List<CusCrack>
	 */
	public List<CusMatrix> getCusBaseCrackLenMatrixData(String modelSeriesId);
	
	/**
	 * 判断S3分析状态
	 * @param modelSeriesId
	 * @return Boolean 是表示可以进行矩阵分析
	 */
	public Boolean s3AnalyseState(String modelSeriesId);
	public CusMatrix getCusMatrixData(Integer matrixNumber, String modelSeriesId);

	/**
	 * 根据机型查询LH4显示信息
	 * @param modelSeriesId
	 * @return
	 */
	public CusDisplay getLH4CusDisplay(String modelSeriesId);
	
	
	
	
	
	
}
