package com.koron.wxwork.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koron.wxwork.util.AbstractWXWorkSetting;
import com.koron.wxwork.util.BaseResultBean;
import com.koron.wxwork.util.HttpRequest;
import com.koron.wxwork.util.WXWorkSetting;
/**
 * 发送消息基础bean
 * @author Administrator
 *
 */
public abstract class BaseMessageBean {
	/**
	 * UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 */
	protected String touser;
	/**
	 * PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数
	 */
	protected String toparty;
	/**
	 * 企业应用的id，整型。可在应用的设置页面查看
	 */
	protected Integer agentid;
	
	
	transient private String keyName;
	transient private AbstractWXWorkSetting wxWorkSetting;
	
	public BaseMessageBean() {
		this(AbstractWXWorkSetting.AGENTKEYNAME_DEFAULT, WXWorkSetting.getInstance()); 
	}

	public BaseMessageBean(String keyName) {
		this(keyName, WXWorkSetting.getInstance()); 
	}
	
	public BaseMessageBean(String keyName, AbstractWXWorkSetting wxWorkSetting) {
		super();
		this.keyName = keyName;
		this.wxWorkSetting = wxWorkSetting;
	}
	
	public String getTouser() {
		return touser;
	}
	/**
	 * UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 */
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getToparty() {
		return toparty;
	}
	/**
	 * PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数
	 */
	public void setToparty(String toparty) {
		this.toparty = toparty;
	}
	public Integer getAgentid() {
		return agentid;
	}
	/**
	 * 企业应用的id，整型。可在应用的设置页面查看
	 */
	public void setAgentid(Integer agentid) {
		this.agentid = agentid;
	}
	
	public String getRemoteUrl() {
		return this.wxWorkSetting.getRemoteUrl(keyName);
	}
	/**
	 * 发送标示为key的企业消息
	 * @param key
	 * @return
	 */
	public final  BaseResultBean send()
	{
		this.agentid = wxWorkSetting.getAgentid(keyName); 
		String url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+wxWorkSetting.getAccessToken(keyName);
		Gson gson=new GsonBuilder().disableHtmlEscaping().create();
		String data = gson.toJson(this);
		String result=HttpRequest.post(url, data);
		if(result.indexOf("\"errcode\":42001") != -1) {// acccess_token过期
			url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" +wxWorkSetting.getAccessToken(true, keyName);
			return reSend(url, data, gson);
		}
		return gson.fromJson(result, BaseResultBean.class);
	}
	
	private final BaseResultBean reSend(String url, String data, Gson gson) {
		String result=HttpRequest.post(url, data); 
		return gson.fromJson(result, BaseResultBean.class);
	}
	
	public static void main(String[] args) {
//		SendTextBean sendTextBean = new SendTextBean();
//		sendTextBean.setTouser("2b2bc533_1506300815386964050");
//		sendTextBean.setContent("你好，测试");
//		sendTextBean.send();
	}
}
