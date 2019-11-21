package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 *	 使用指南
 * 
 * @author Carry
 *
 */
@Entity
public class Help {
	
	private Integer helpId;		 // 自增id（主键）
	private String language;	 // 语言
	private String appPcgName;	 // APP包名
	private String helpAddress;	 // 指南地址

	public Help() {
		
	}

	public Help(Integer helpId, String language, String appPcgName, String helpAddress) {
		this.helpId = helpId;
		this.language = language;
		this.appPcgName = appPcgName;
		this.helpAddress = helpAddress;
	}

	public Integer getHelpId() {
		return helpId;
	}

	public void setHelpId(Integer helpId) {
		this.helpId = helpId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAppPcgName() {
		return appPcgName;
	}

	public void setAppPcgName(String appPcgName) {
		this.appPcgName = appPcgName;
	}

	public String getHelpAddress() {
		return helpAddress;
	}

	public void setHelpAddress(String helpAddress) {
		this.helpAddress = helpAddress;
	}

}
