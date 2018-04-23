package com.koron.wxwork.oauth2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.koron.wxwork.util.AbstractWXWorkSetting;
import com.koron.wxwork.util.HttpRequest;
import com.koron.wxwork.util.WXWorkSetting;
/**
 * oauth2验证处理
 * @author Administrator
 *
 */
public class Oauth2 {
	private String keyName;
	private AbstractWXWorkSetting wxWorkSetting;
	/**
	 * 创建标示为keyoauth2验证处理实例
	 * @param keyName
	 */
	public Oauth2() {
		this(AbstractWXWorkSetting.AGENTKEYNAME_DEFAULT, WXWorkSetting.getInstance()); 
	}
	/**
	 * 创建标示为keyoauth2验证处理实例
	 * @param keyName
	 */
	public Oauth2(String keyName) {
		this(keyName, WXWorkSetting.getInstance()); 
	}
	
	public Oauth2(String keyName, AbstractWXWorkSetting wxWorkSetting) {
		super();
		this.keyName = keyName;
		this.wxWorkSetting = wxWorkSetting;
	}
	/**
	 * 获取outh2验证的code
	 * @param redirect_uri 回调地址
	 * @param state 重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值
	 * @return
	 */
	public String  getCodeUrl(String redirect_uri,String state) {
		try {
			redirect_uri=URLEncoder.encode(redirect_uri,"utf-8");
			String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxWorkSetting.getCorpid(keyName)+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_base&state="+state+"#wechat_redirect";
			return url;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取outh2验证的code
	 * @param redirect_uri 回调地址
	 * @return
	 */
	public String  getCodeUrl(String redirect_uri) {
		redirect_uri = wxWorkSetting.getRemoteUrl(keyName)+redirect_uri;
		return getCodeUrl(redirect_uri,"");
	}
	/**
	 * 根据code获取用户userid
	 * @param code
	 * @return
	 */
	public Oauth2ResultBean getUserIdByCode(String code) {
		String url="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+wxWorkSetting.getAccessToken(keyName)+"&code="+code;
		String result=HttpRequest.get(url);
		return new Gson().fromJson(result, Oauth2ResultBean.class);
	}
}
