package com.rskytech.struct.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S2;

public interface IS2Bo extends IBo {
	
	public List<S2> getS2BySssId(String sssId) throws BusinessException;
	
	
	public String saveS2Record(String ssiId,String id,ComUser user,String ggcontentCn) throws BusinessException;
}
