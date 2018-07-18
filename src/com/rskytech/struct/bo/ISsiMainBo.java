package com.rskytech.struct.bo;

import java.util.List;

import com.richong.arch.bo.IBo;

public interface ISsiMainBo extends IBo {
	/**
	 * 根据机型id查询所有Ssi
	 * @param modelSeriesId
	 * @return
	 */
	public  List<Object[]> getSSsiListByModelSeriesId(String modelSeriesId);

}
