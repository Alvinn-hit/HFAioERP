package com.koron.wxwork.user;

import com.google.gson.Gson;
import com.koron.wxwork.util.AbstractWXWorkSetting;
import com.koron.wxwork.util.HttpRequest;
import com.koron.wxwork.util.WXWorkSetting;
/**
 * 成员管理
 * @author Administrator
 *
 */
public class WXWorkUser {
	private String keyName;
	private AbstractWXWorkSetting wxWorkSetting;
	
	public WXWorkUser() {
		this(AbstractWXWorkSetting.AGENTKEYNAME_CONTACTS, WXWorkSetting.getInstance()); 
	}

	public WXWorkUser(String keyName) {
		this(keyName, WXWorkSetting.getInstance()); 
	}
	
	public WXWorkUser(String keyName, AbstractWXWorkSetting wxWorkSetting) {
		super();
		this.keyName = keyName;
		this.wxWorkSetting = wxWorkSetting;
	}
	
	/**
	 * 创建成员
	 * @param bean
	 * @return
	 */
	public WXWorkUserResultBean create(WXWorkUserBean bean) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token="+wxWorkSetting.getAccessToken(keyName);
		Gson gson =new Gson();
		String result=HttpRequest.post(url, gson.toJson(bean));
		return gson.fromJson(result,WXWorkUserResultBean.class);
	}
	/**
	 * 更新成员（如果非必须的字段未指定，则不更新该字段之前的设置值）
	 * @param bean
	 * @return
	 */
	public WXWorkUserResultBean update(WXWorkUserBean bean) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token="+wxWorkSetting.getAccessToken(keyName);
		Gson gson =new Gson();
		String result=HttpRequest.post(url, gson.toJson(bean));
		return gson.fromJson(result,WXWorkUserResultBean.class);
	}
	/**
	 * 删除成员
	 * @param userid 员工userid。对应管理端的帐号
	 * @return
	 */
	public WXWorkUserResultBean delete(String userid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token="+wxWorkSetting.getAccessToken(keyName)+"&userid="+userid;
		String result=HttpRequest.get(url);
		return new Gson().fromJson(result,WXWorkUserResultBean.class);
	}
	/**
	 * 获取成员
	 * @return
	 */
	public WXWorkUserBean get(String userid) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+wxWorkSetting.getAccessToken(keyName)+"&userid="+userid;
		String result=HttpRequest.get(url);
		return new Gson().fromJson(result,WXWorkUserBean.class);
	}
	/**
	 * 获取部门成员
	 * @param department_id 获取的部门id
	 * @param fetch_child 1/0：是否递归获取子部门下面的成员
	 * @return
	 */
	public WXWorkUserResultBean simpleList(String department_id,String fetch_child) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token="+wxWorkSetting.getAccessToken(keyName)+"&department_id="+department_id+"&fetch_child="+fetch_child;
		String result=HttpRequest.get(url);
		return new Gson().fromJson(result,WXWorkUserResultBean.class);
	}
}
