package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 项目类型
 * 
 * @author Carry
 *
 */
@Entity
public class AppType {
	private Integer appTypeId; 		// 项目id
	private String appName; 		// 项目名称
	private String appStructsBg; 	// 平板结构图

	public AppType() {
	}

	public AppType(Integer appTypeId, String appName, String appStructsBg) {
		super();
		this.appTypeId = appTypeId;
		this.appName = appName;
		this.appStructsBg = appStructsBg;
	}

	public String getAppStructsBg() {
		return appStructsBg;
	}

	public void setAppStructsBg(String appStructsBg) {
		this.appStructsBg = appStructsBg;
	}

	public Integer getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(Integer appTypeId) {
		this.appTypeId = appTypeId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
