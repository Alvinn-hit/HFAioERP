package com.koron.wxwork.util;

/**
 * 返回结果bean
 * @author Administrator
 *
 */
public class BaseResultBean {
	/**
	 * 返回码
	 */
	private String errcode;
	/**
	 * 对返回码的文本描述内容
	 */
	private String errmsg;
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	/**
	 * 生成返回结果bean
	 * @param errcode
	 * @param errmsg
	 * @return
	 */
	public static BaseResultBean generate(String errcode,String errmsg) {
		BaseResultBean bean =new BaseResultBean();
		bean.setErrcode(errcode);
		bean.setErrmsg(errmsg);
		return bean;
	}
}
