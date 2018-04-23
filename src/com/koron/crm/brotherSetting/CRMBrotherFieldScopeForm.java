package com.koron.crm.brotherSetting;

import com.menyi.web.util.BaseForm;
import com.menyi.web.util.GlobalsTool;

public class CRMBrotherFieldScopeForm extends BaseForm {
	
	private static final long serialVersionUID = 1L;

	private String fieldsName;//字段名称

	private String readDeptIds;//只读部门IDS

	private String readUserIds;//只读个人IDS

	private String hideDeptIds;//隐藏部门IDS

	private String hideUserIds;//隐藏个人IDS

	private String tableName; //表名

	public String getFieldsName() {
		return fieldsName;
	}

	public void setFieldsName(String fieldsName) {
		this.fieldsName = fieldsName;
	}

	public String getReadDeptIds() {
		return readDeptIds;
	}

	public void setReadDeptIds(String readDeptIds) {
		this.readDeptIds = readDeptIds;
	}

	public String getReadUserIds() {
		return readUserIds;
	}

	public void setReadUserIds(String readUserIds) {
		this.readUserIds = readUserIds;
	}

	public String getHideDeptIds() {
		return hideDeptIds;
	}

	public void setHideDeptIds(String hideDeptIds) {
		this.hideDeptIds = hideDeptIds;
	}

	public String getHideUserIds() {
		return hideUserIds;
	}

	public void setHideUserIds(String hideUserIds) {
		this.hideUserIds = hideUserIds;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	

	
}
