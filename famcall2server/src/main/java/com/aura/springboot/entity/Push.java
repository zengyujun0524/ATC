package com.aura.springboot.entity;

import javax.persistence.Entity;
/**
 * 
 * @author Carry
 *
 */
@Entity
public class Push {
	private Integer userId;				// 自增id（主键）
	private String pushToken;			// 离线推送token
	private Integer sysType;			// 系统型号（0：安卓、1：苹果）
	private String token;				// 密文
	private String pushKit;              // 最新苹果推送
	private Integer news;               //  消息数量（1-99）   
	
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
	public Push(Integer userId, String pushToken, Integer sysType, String token, String pushKit, Integer news) {
		super();
		this.userId = userId;
		this.pushToken = pushToken;
		this.sysType = sysType;
		this.token = token;
		this.pushKit = pushKit;
		this.news = news;
	}
	public Push() {
		super();
		// TODO Auto-generated constructor stub
	}

	 
	
	
}
