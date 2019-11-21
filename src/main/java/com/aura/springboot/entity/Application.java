package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * APP类
 * 
 * @author Carry
 *
 */
@Entity
public class Application {
	private Integer appId; // app自增id（主键）
	private String appName; // 软件名称
	private String appPcgName; // 包名
	private String describe; // 软件描述
	private Integer version; // 版本号
	private String apkAddress; // apk地址
	private String createTime; // 创建时间
	private Integer mandatoryUpString; // 是否强制更新(0：是、1：否）
	private String versionName; // 版本名称

	public Application() {
		super();
	}

	public Application(Integer appId, String appName, String appPcgName, String describe, Integer version,
			String apkAddress, String createTime, Integer mandatoryUpString, String versionName) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.appPcgName = appPcgName;
		this.describe = describe;
		this.version = version;
		this.apkAddress = apkAddress;
		this.createTime = createTime;
		this.mandatoryUpString = mandatoryUpString;
		this.versionName = versionName;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPcgName() {
		return appPcgName;
	}

	public void setAppPcgName(String appPcgName) {
		this.appPcgName = appPcgName;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getApkAddress() {
		return apkAddress;
	}

	public void setApkAddress(String apkAddress) {
		this.apkAddress = apkAddress;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getMandatoryUpString() {
		return mandatoryUpString;
	}

	public void setMandatoryUpString(Integer mandatoryUpString) {
		this.mandatoryUpString = mandatoryUpString;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

}
