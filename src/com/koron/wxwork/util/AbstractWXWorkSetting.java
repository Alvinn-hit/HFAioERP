package com.koron.wxwork.util;

import java.util.Map;

import com.google.gson.Gson;

public abstract class AbstractWXWorkSetting {
	/**
	 * 刷新间隔
	 */
	public int refreshInterval = 100 * 60 * 1000;
    public static String AGENTKEYNAME_DEFAULT = "default";
    public static String AGENTKEYNAME_CONTACTS = "contacts";
	public String getAccessToken() {
		return this.getAccessToken(false, AGENTKEYNAME_DEFAULT);
	}

	public String getAccessToken(boolean force) {
		return this.getAccessToken(force, AGENTKEYNAME_DEFAULT);
	}
	
	public String getAccessToken(String keyName) {
		return this.getAccessToken(false, keyName);
	}

	@SuppressWarnings("unchecked")
	protected String fetchAccessToken(String corpid, String corpsecret) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="
				+ corpid + "&corpsecret=" + corpsecret;
		String result = HttpRequest.get(url);
		Map<String, String> map = new Gson().fromJson(result, Map.class);
		return map.get("access_token");
	}

	public abstract String getAccessToken(boolean force, String keyName);
	
	public abstract String getCorpid(String keyName);
	
	public abstract Integer getAgentid(String keyName);
	
	public abstract String getRemoteUrl(String keyName);
}
