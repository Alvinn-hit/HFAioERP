package com.koron.wxwork.user;

import java.util.List;

import com.koron.wxwork.util.BaseResultBean;

/**
 * 员工返回结果bean
 * @author Administrator
 *
 */
public class WXWorkUserResultBean extends BaseResultBean{
	/**
	 * 获取部门成员
	 */
	private List<WXWorkUserBean> userlist;

	public List<WXWorkUserBean> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<WXWorkUserBean> userlist) {
		this.userlist = userlist;
	}
	
	
}
