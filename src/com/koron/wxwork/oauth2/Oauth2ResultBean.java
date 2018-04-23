package com.koron.wxwork.oauth2;

import com.koron.wxwork.util.BaseResultBean;

/**
 * 根据code获取成员信息结果bean
 * @author Administrator
 *
 */
public class Oauth2ResultBean extends BaseResultBean{
	/**
	 * 员工UserID
	 */
	private String UserId;
	/**
	 * 手机设备号(由微信在安装时随机生成)
	 */
	private String DeviceId;
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getDeviceId() {
		return DeviceId;
	}
	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}
}
