package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.S2;

public interface IS2Dao extends IDAO {
	public List<S2> getS2BySssId(String sssId) throws BusinessException;
}
