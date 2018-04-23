package com.koron.wxwork.department;
/**
 * 通讯录部门bean
 * @author Administrator
 *
 */
public class WXWorkDepartmentBean {
	/**
	 * 部门id。创建部门的时候留空
	 */
	private Integer id;
	/**
	 * 部门名称。长度限制为1~64个字符
	 */
	private String name;
	/**
	 * 父亲部门id。根部门id为1
	 */
	private Integer parentid;
	/**
	 * 在父部门中的次序。从1开始，数字越大排序越靠后
	 */
	private String order;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
