package com.koron.wxwork.department;

import java.util.List;

import com.koron.wxwork.util.BaseResultBean;

/**
 * 创建部门返回结果bean
 * @author Administrator
 *
 */
public class WXWorkDepartmentResultBean extends BaseResultBean{
	/**
	 * 创建的部门id
	 */
	private String id;
	/**
	 * 部门列表数据。以部门的order字段从小到大排列
	 */
	private List<WXWorkDepartmentBean> department;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<WXWorkDepartmentBean> getDepartment() {
		return department;
	}
	public void setDepartment(List<WXWorkDepartmentBean> department) {
		this.department = department;
	}
}
