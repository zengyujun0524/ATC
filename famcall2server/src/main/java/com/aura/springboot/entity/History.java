package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 	 手机用户登录历史记录
 * @author Carry
 *
 */
@Entity
public class History {
	
	private Integer historyId;		//历史记录id
	private Integer userId;			//用户id
	private String phoneModel;		//手机型号
	private String lastLogonTime;	//最后登录时间
	private String snNum;			//手机唯一标识uuid
	private Integer DeviceLock;		//设备锁状态（0：关闭、1：开启）
	public Integer getHistoryId() {
		return historyId;
	}
	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPhoneModel() {
		return phoneModel;
	}
	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}
	public String getLastLogonTime() {
		return lastLogonTime;
	}
	public void setLastLogonTime(String lastLogonTime) {
		this.lastLogonTime = lastLogonTime;
	}
	public Integer getDeviceLock() {
		return DeviceLock;
	}
	public void setDeviceLock(Integer deviceLock) {
		DeviceLock = deviceLock;
	}
	public String getSnNum() {
		return snNum;
	}
	public void setSnNum(String snNum) {
		this.snNum = snNum;
	}
	public History() {
	}
	public History(Integer historyId, Integer userId, String phoneModel, String lastLogonTime, String snNum,
			Integer deviceLock) {
		this.historyId = historyId;
		this.userId = userId;
		this.phoneModel = phoneModel;
		this.lastLogonTime = lastLogonTime;
		this.snNum = snNum;
		DeviceLock = deviceLock;
	}

	
	
}
