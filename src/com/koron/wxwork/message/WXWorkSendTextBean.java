package com.koron.wxwork.message;

import java.util.HashMap;
import java.util.Map;

import com.koron.wxwork.util.AbstractWXWorkSetting;

/**
 * 发送文本消息bean
 * @author Administrator
 *
 */
public class WXWorkSendTextBean extends BaseMessageBean {
	
	/**
	 * 消息类型，此时固定为：text
	 */
	private String msgtype="text";
	
	/**
	 * 消息内容
	 */
	private Map<String, String> text=new HashMap<String, String>();
    
	public WXWorkSendTextBean() {
		super(); 
	}

	public WXWorkSendTextBean(String keyName) {
		 super(keyName);
	}
	
	public WXWorkSendTextBean(String keyName, AbstractWXWorkSetting wxWorkSetting) {
		super(keyName, wxWorkSetting);
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getContent() {
		return text.get("content");
	}
	/**
	 * 消息内容
	 */
	public void setContent(String content) {
		text.put("content",content);
	}

}
