package com.koron.wxwork.util;

import java.util.HashMap;
import java.util.Map;

public class WXWorkSetting extends AbstractWXWorkSetting {
	private static WXWorkSetting instance;
	private Map<String, WXWork> wxWorkMap;

	private WXWorkSetting() {}

	public static synchronized WXWorkSetting getInstance() {
		if (instance == null) {
			instance = new WXWorkSetting();
			 instance.wxWorkMap = new HashMap<String, WXWork>();
//			 WXWork defaultWXWork = new WXWork(AGENTKEYNAME_DEFAULT, "WW076c82a714484a28", "w7zaWqTuLxDKNwG6TU3oslaCxMpVaT4kmPd_2mIaUVs", "", System.currentTimeMillis(), 1000009, "http://hsh.dev.krrj.cn");
			 WXWork defaultWXWork = new WXWork(AGENTKEYNAME_DEFAULT, "WW076c82a714484a28", "Z8QqsQx2U-PlTJ6DPeTcVsZFbwyL4RIAVkL1fH510_E", "", System.currentTimeMillis(), 1000002, "http://aio.koronsoft.com");
			 WXWork contactWXWork = new WXWork(AGENTKEYNAME_CONTACTS, "WW076c82a714484a28", "PB2wXIq5CcBPD2sSoORtgjmXCrbr-pWnwe-1IPSZyeI", "", System.currentTimeMillis(), 0, "");
			 instance.wxWorkMap.put(defaultWXWork.keyName, defaultWXWork);
			 instance.wxWorkMap.put(contactWXWork.keyName, contactWXWork);
		}
		return instance;
	}

	@Override
	public String getAccessToken(boolean force, String keyName) {
		WXWork work = wxWorkMap.get(keyName);
		String token = null;
		if (!force && work.expirestime > System.currentTimeMillis())
			token = work.access_token;
		else {
			token = this.fetchAccessToken(work.corpid, work.corpsecret);
			if (token != null) {
				work.access_token = token;
				work.expirestime = System.currentTimeMillis() + this.refreshInterval; 
			}			
		}
		return token;
	}

	@Override
	public String getCorpid(String keyName) {
		return getInstance().wxWorkMap.get(keyName).corpid;
	}

	@Override
	public Integer getAgentid(String keyName) {
		return getInstance().wxWorkMap.get(keyName).agentid;
	}

	@Override
	public String getRemoteUrl(String keyName) {
		return getInstance().wxWorkMap.get(keyName).remoteurl;
	}
}
class WXWork {
	String keyName;
	String corpid;
	String corpsecret;
	String access_token;
	Long expirestime;
	Integer agentid;
	String remoteurl;
	public WXWork(String keyName, String corpid, String corpsecret,
			String access_token, Long expirestime, Integer agentid, String remoteurl) {
		super();
		this.keyName = keyName;
		this.corpid = corpid;
		this.corpsecret = corpsecret;
		this.access_token = access_token;
		this.expirestime = expirestime;
		this.agentid = agentid;
		this.remoteurl = remoteurl;
	}		
}
