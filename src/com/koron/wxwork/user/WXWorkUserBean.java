package com.koron.wxwork.user;


/**
 * 员工bean
 * @author Administrator
 *
 */
public class WXWorkUserBean {
	/**
	 * 员工UserID。对应管理端的帐号，企业内必须唯一。长度为1~64个字符
	 */
	private String userid;
	/**
	 * 成员名称。长度为1~64个字符
	 */
	private String name;
	/**
	 * 手机号码。企业内必须唯一
	 */
	private String mobile;
	/**
	 * 成员所属部门id列表,不超过20个
	 */
	private Integer []department;
	/**
	 * 职位信息。长度为0~64个字符
	 */
	private String position;
	/**
	 * 性别。gender=1表示男，=2表示女。默认gender=1
	 */
	private Integer gender;
	/**
	 * 邮箱。长度为0~64个字节。企业内必须唯一
	 */
	private String email;
	/**
	 * 座机。长度0-64个字节
	 */
	private String telephone;
	/**
	 * 成员头像的mediaid，通过多媒体接口上传图片获得的mediaid(创建的时候不用填)
	 */
	private String avatar_mediaid;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer[] getDepartment() {
		return department;
	}
	public void setDepartment(Integer[] department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAvatar_mediaid() {
		return avatar_mediaid;
	}
	public void setAvatar_mediaid(String avatar_mediaid) {
		this.avatar_mediaid = avatar_mediaid;
	}
	
	
}
