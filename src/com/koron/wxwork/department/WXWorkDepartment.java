package com.koron.wxwork.department;

import com.google.gson.Gson;
import com.koron.wxwork.util.AbstractWXWorkSetting;
import com.koron.wxwork.util.HttpRequest;
import com.koron.wxwork.util.WXWorkSetting;
/**
 * 部门管理
 * @author Administrator
 *
 */
public class WXWorkDepartment {
	private String keyName;
	private AbstractWXWorkSetting wxWorkSetting;
	
	public WXWorkDepartment() {
		this(AbstractWXWorkSetting.AGENTKEYNAME_CONTACTS, WXWorkSetting.getInstance()); 
	}

	public WXWorkDepartment(String keyName) {
		this(keyName, WXWorkSetting.getInstance()); 
	}
	
	public WXWorkDepartment(String keyName, AbstractWXWorkSetting wxWorkSetting) {
		super();
		this.keyName = keyName;
		this.wxWorkSetting = wxWorkSetting;
	}
	
	/**
	 * 创建部门
	 * @param name 部门名称。长度限制为1~64个字符
	 * @param parentid 父亲部门id。根部门id为1
	 * @param order 在父部门中的次序。从1开始，数字越大排序越靠后
	 * @return
	 */
	public WXWorkDepartmentResultBean create(String name,Integer parentid,String order) {
		WXWorkDepartmentBean bean =new WXWorkDepartmentBean();
		bean.setName(name);
		bean.setOrder(order);
		bean.setParentid(parentid);
		return create(bean);
	}
	/**
	 * 创建部门
	 * @param bean
	 * @return
	 */
	public WXWorkDepartmentResultBean create(WXWorkDepartmentBean bean) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token="+wxWorkSetting.getAccessToken(keyName);
		Gson gson =new Gson();
		String result=HttpRequest.post(url, gson.toJson(bean));
		return gson.fromJson(result,WXWorkDepartmentResultBean.class);
	}
	/**
	 * 更新部门（如果非必须的字段未指定，则不更新该字段之前的设置值）
	 * @param bean
	 * @return
	 */
	public WXWorkDepartmentResultBean update(WXWorkDepartmentBean bean) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token="+wxWorkSetting.getAccessToken(keyName);
		Gson gson =new Gson();
		String result=HttpRequest.post(url, gson.toJson(bean));
		return gson.fromJson(result,WXWorkDepartmentResultBean.class);
	}
	/**
	 * 删除部门
	 * @param id 部门id。（注：不能删除根部门；不能删除含有子部门、成员的部门）
	 * @return
	 */
	public WXWorkDepartmentResultBean delete(Integer id) {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token="+wxWorkSetting.getAccessToken(keyName)+"&id="+id;
		String result=HttpRequest.get(url);
		return new Gson().fromJson(result,WXWorkDepartmentResultBean.class);
	}
	/**
	 * 获取部门列表
	 * @return
	 */
	public WXWorkDepartmentResultBean list() {
		String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token="+wxWorkSetting.getAccessToken(keyName);
		String result=HttpRequest.get(url);
		return new Gson().fromJson(result,WXWorkDepartmentResultBean.class);
	}
}
