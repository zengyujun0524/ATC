package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 
 * @author Carry
 *
 */
@Entity
public class Push {
	private Integer userId; // 自增id（主键）
	private String pushToken; // 离线推送token
	private Integer sysType; // 系统型号（0：安卓、1：苹果）
	private String token; // 密文
	private String pushKit; // 最新苹果推送
	private Integer news; // 消息数量（1-99）
	private Integer accType;// 账户类型：0:手机、1平板、2无绳子机
	private String imConnect; // IM关联
	private Integer control;

	public Integer getControl() {
		return control;
	}

	public void setControl(Integer control) {
		this.control = control;
	}

	public String getImConnect() {
		return imConnect;
	}

	public void setImConnect(String imConnect) {
		this.imConnect = imConnect;
	}

	public Integer getSysType() {
		return sysType;
	}

	public void setSysType(Integer sysType) {
		this.sysType = sysType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPushKit() {
		return pushKit;
	}

	public void setPushKit(String pushKit) {
		this.pushKit = pushKit;
	}

	public Integer getNews() {
		return news;
	}

	public void setNews(Integer news) {
		this.news = news;
	}

	public Integer getAccType() {
		return accType;
	}

	public void setAccType(Integer accType) {
		this.accType = accType;
	}

	public Push(Integer userId, String pushToken, Integer sysType, String token, String pushKit, Integer news,
			Integer accType, String imConnect) {
		super();
		this.userId = userId;
		this.pushToken = pushToken;
		this.sysType = sysType;
		this.token = token;
		this.pushKit = pushKit;
		this.news = news;
		this.accType = accType;
		this.imConnect = imConnect;
	}

	public Push() {
		super();
		// TODO Auto-generated constructor stub
	}

}
