package com.aura.springboot.entity;

public class Ipv4 {
	Integer id; // 自动增长列
	String imConnect; // 对应用户的唯一标识 IMz账号
	String bssId; // 路由器唯一标识
	String ssId; // 路由器名称
	String macAddress; // mac地址

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImConnect() {
		return imConnect;
	}

	public void setImConnect(String imConnect) {
		this.imConnect = imConnect;
	}

	public String getBssId() {
		return bssId;
	}

	public void setBssId(String bssId) {
		this.bssId = bssId;
	}

	public String getSsId() {
		return ssId;
	}

	public void setSsId(String ssId) {
		this.ssId = ssId;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Ipv4(Integer id, String imConnect, String bssId, String ssId, String macAddress) {
		super();
		this.id = id;
		this.imConnect = imConnect;
		this.bssId = bssId;
		this.ssId = ssId;
		this.macAddress = macAddress;
	}

	public Ipv4() {
		super();
	}

}
