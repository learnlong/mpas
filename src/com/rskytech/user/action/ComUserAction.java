package com.rskytech.user.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.user.bo.IComUserBo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ComUserAction extends BaseAction {

	private static final long serialVersionUID = 3430185520335033386L;

	private IComUserBo comUserBo;

	private String userCode; // 查询条件：用户编号
	private String userId; // 得到要初始化密码的用户ID
	private String password; // 得到要修改后的密码
	private String yuanPassword; // 得到用户原来的密码
	private String oldPassWord;// 老密码
	private String newPassWord;// 新密码
	private String queryValidFlag;//是否有用

	public String init(){
		return SUCCESS;
	}
	

	/**
	 * 得到用户列表
	* @Title: getDataComUserList
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月10日 下午12:29:42
	* @throws
	 */
	public String getDataComUserList() {
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		List<HashMap> listJsonFV = new ArrayList();
		List<ComUser> comUserLists = comUserBo.getComUserList(userCode, keyword, page, queryValidFlag);
		for (ComUser user : comUserLists) {
			HashMap hm = new HashMap();
			hm.put("userId", user.getUserId());
			hm.put("userCode", user.getUserCode());
			hm.put("userName", user.getUserName());
			hm.put("password", "******");
			hm.put("post", user.getPost());
			hm.put("plone", user.getPlone());
			hm.put("EMail", user.getEMail());
			hm.put("validFlag", user.getValidFlag());
			listJsonFV.add(hm);
		}
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("comUser", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 修改用户信息（包括新增和修改）
	* @Title: jsonComUserUpdate
	* @Description:
	* @return
	* @throws Exception
	* @author samual
	* @date 2014年11月10日 下午1:12:47
	* @throws
	 */
	public String jsonComUserUpdate() throws Exception {
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		JSONObject jsonObject = new JSONObject();
		boolean isOk = true;
		String noQuitCode = "";// 退职失败的用户编号
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
//		System.out.println(jsonArray.size());
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String gridUserId = jsonObject.getString("userId");
			String gridUserCode = jsonObject.getString("userCode");
			ComUser comUser;
			// db操作区分
			String dbOperate = "";
			//判断是否usercode相同
			DetachedCriteria dc = DetachedCriteria.forClass(ComUser.class); // 检查用户编号是否存在(新增数据)
			dc.add(Restrictions.eq("userCode", gridUserCode));
			List<ComUser> comUserList = this.comUserBo.findByCritera(dc);
			// 修改操作时
			if (!BasicTypeUtils.isNullorBlank(gridUserId)) {// && !"0".equals(id)) {
				//判断是否存在的code
				if (comUserList.size() == 1 && !comUserList.get(0).getUserId().equals(gridUserId)) {
					isOk = false;
					json.put("success", "exits");
					writeToResponse(json.toString());
					return null;
				}
				dbOperate = ComacConstants.DB_UPDATE;
				comUser = (ComUser) comUserBo.loadById(ComUser.class, gridUserId);
			} else {// 追加操作时
				//判断是否存在的code
				if (comUserList.size() > 0) {
					isOk = false;
					json.put("success", "exits");
					writeToResponse(json.toString());
					return null;
				}
				dbOperate = ComacConstants.DB_INSERT;
				comUser = new ComUser();
				comUser.setPassword(ComacConstants.DEFAULT_PASSWORD);
			}

			comUser.setUserCode(gridUserCode);
			comUser.setUserName(jsonObject.getString("userName"));
			comUser.setPost(jsonObject.getString("post"));
			comUser.setPlone(jsonObject.getString("plone"));
			comUser.setEMail(jsonObject.getString("EMail"));
			comUser.setValidFlag(Integer.parseInt(jsonObject.getString("validFlag")));
			comUserBo.saveOrUpdate(comUser, dbOperate, getSysUser().getUserId());
		}

		json.put("success", isOk);
		json.put("noQuitCode", noQuitCode);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 修改密码
	* @Title: changeUserPassWord
	* @Description:
	* @return
	* @throws Exception
	* @author samual
	* @date 2014年11月10日 下午1:13:08
	* @throws
	 */
	public String changeUserPassWord() throws Exception {
		boolean flag = comUserBo.changeUserPassWordByAdmin(userId, newPassWord, getSysUser().getUserId());
		writeToResponse(this.putJsonOKFlag(null, flag).toString());
		return null;
	}

	/**
	 * 用户密码初始化或密码修改
	 * 
	 * @return
	 * @throws Exception
	 */
	public String jsonUserPassWordUpdate() throws Exception {
		boolean flag = comUserBo.jsonChangeUserPassWord(userId, getSysUser().getUserId(), yuanPassword, password);
		JSONObject json = new JSONObject();
		json.put("success", flag);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 确认密码是否正确
	* @Title: checkPassWord
	* @Description:
	* @return
	* @throws Exception
	* @author samual
	* @date 2014年11月10日 下午1:13:19
	* @throws
	 */
	public String checkPassWord() throws Exception {
		boolean flag = comUserBo.checkPassWord(userId, oldPassWord);
		if (flag) {
			writeToResponse("true");
			return null;
		} else {
			writeToResponse("false");
			return null;
		}
	}

	/**
	 * 根据用户ID设置用户无效
	* @Title: deleteComUserByUserId
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月10日 下午1:13:55
	* @throws
	 */
	public String deleteComUserByUserId() {
		if (this.userId != null && !"".equals(this.userId)) {
			if (userId.equals(getSysUser().getUserId())) {
				writeToResponse(userId);
				return null;
			}
			ComUser cu = (ComUser) comUserBo.loadById(ComUser.class,this.userId);
			if (cu != null) {
				cu.setValidFlag(ComacConstants.NO);
				comUserBo.saveOrUpdate(cu, ComacConstants.DB_DELETE, getSysUser().getUserId());
			}
		}
		return null;
	}
	
	/**
	 * 重设密码
	* @Title: jsonResetUserPassWord
	* @Description:
	* @return
	* @throws Exception
	* @author samual
	* @date 2014年11月10日 下午1:14:50
	* @throws
	 */
	public String jsonResetUserPassWord() throws Exception {
		boolean flag = comUserBo.resetUserPassWord(userId, getSysUser().getUserId());
		if (flag) {
			writeToResponse("true");
			return null;
		} else {
			writeToResponse("false");
			return null;
		}
	}


	public IComUserBo getComUserBo() {
		return comUserBo;
	}


	public void setComUserBo(IComUserBo comUserBo) {
		this.comUserBo = comUserBo;
	}


	public String getUserCode() {
		return userCode;
	}


	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getYuanPassword() {
		return yuanPassword;
	}


	public void setYuanPassword(String yuanPassword) {
		this.yuanPassword = yuanPassword;
	}

	public String getQueryValidFlag() {
		return queryValidFlag;
	}


	public void setQueryValidFlag(String queryValidFlag) {
		this.queryValidFlag = queryValidFlag;
	}


	public String getOldPassWord() {
		return oldPassWord;
	}


	public void setOldPassWord(String oldPassWord) {
		this.oldPassWord = oldPassWord;
	}


	public String getNewPassWord() {
		return newPassWord;
	}


	public void setNewPassWord(String newPassWord) {
		this.newPassWord = newPassWord;
	}
	
}
