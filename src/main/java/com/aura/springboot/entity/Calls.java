package com.aura.springboot.entity;

import javax.persistence.Entity;

@Entity
public class Calls {

	private Integer callId; // 自动增长列 主键
	private Integer userId; // 用户id
	private String callNumber; // 来电号码
	private String macAddress; // 设备地址
	private String callTime; // 来电时间
	private String imConnect; // IM关联
	private Integer type; // 未接类型 (1.外线未接，2.转接未接 ,3.内呼未接)
	private Integer proId; // 设备ID

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCallId() {
		return callId;
	}

	public void setCallId(Integer callId) {
		this.callId = callId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId; //
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getCallTime() {
		return callTime;
	}

	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}

	public String getImConnect() {
		return imConnect;
	}

	public void setImConnect(String imConnect) {
		this.imConnect = imConnect;
	}

	public Calls() {
		super();
	}

}
