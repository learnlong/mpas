package com.rskytech.struct.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S5;

public interface IS5Bo extends IBo {
	public int getItemCount(String itemName,String modelId,String aOrb) throws BusinessException;
	public List getItemName(String itemName,String modelId,String aOrb) throws BusinessException;
	public List<S1> getS1(String  sssiId,int isMetal,int inOrOut) throws BusinessException;
	public List getLevelCount(String  itemId,String modelId,String aOrb) throws BusinessException;
	public List<S5> getS5BySsiId(String ssiId,Integer inOrOut) throws BusinessException;
	public boolean saveList(List<Object[]> list,ComUser user,String flag) throws BusinessException;
	public S5 isExistForS5(String ssiId,String s1Id,String inOrOut) throws BusinessException;
}
