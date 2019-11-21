package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 用户表
 * 
 * @author Carry
 *
 */
@Entity
public class User {
	private Integer userId; // 自增id（主键）
	private String username; // 昵称
	private String password; // 密码
	private String phone; // 该字段用户是手机号码，无绳子机就是序号
	private String macAddress; // mac地址
	private String email; // 邮箱
	private Integer sex; // 性别
	private String birthday; // 出生日期
	private String photo; // 头像
	private Integer accType; // 账户类型：0:手机、1平板、2无绳子机
	private String countryCode; // 国家码
	private String imConnect; // IM关联
	private Integer logStatus; // 登录状态( 0：离线、1：在线、2：登出、3：用户内线在线)
	private String createTime; // 创建时间
	private Integer appTypeId; // 项目类型
	private Integer callStatus; // 通话状态：0：可通话、1：内呼、2：外呼、3：呼入、4：呼出
	private String token; // 密文
	private String phoneModel; // 手机型号
	private Integer relationProId; // 关联序号（10-99） 默认：0 是平板状态
	private String pushToken; // 离线推送token
	private Integer sysType; // 系统型号（0：安卓、1：苹果）
	private Integer news; // 消息数量（1-99）
	private String bssId; // 路由器唯一标识
	private String ssId; // 路由器名称
	private String ip; // ip地址

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getAccType() {
		return accType;
	}

	public void setAccType(Integer accType) {
		this.accType = accType;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getImConnect() {
		return imConnect;
	}

	public void setImConnect(String imConnect) {
		this.imConnect = imConnect;
	}

	public Integer getLogStatus() {
		return logStatus;
	}

	public void setLogStatus(Integer logStatus) {
		this.logStatus = logStatus;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getAppTypeId() {
		return appTypeId;
	}

	public void setAppTypeId(Integer appTypeId) {
		this.appTypeId = appTypeId;
	}

	public Integer getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(Integer callStatus) {
		this.callStatus = callStatus;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public Integer getRelationProId() {
		return relationProId;
	}

	public void setRelationProId(Integer relationProId) {
		this.relationProId = relationProId;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public Integer getSysType() {
		return sysType;
	}

	public void setSysType(Integer sysType) {
		this.sysType = sysType;
	}

	public Integer getNews() {
		return news;
	}

	public void setNews(Integer news) {
		this.news = news;
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

	public User(Integer userId, String username, String password, String phone, String macAddress, String email,
			Integer sex, String birthday, String photo, Integer accType, String countryCode, String imConnect,
			Integer logStatus, String createTime, Integer appTypeId, Integer callStatus, String token,
			String phoneModel, Integer relationProId, String pushToken, Integer sysType, Integer news, String bssId,
			String ssId) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.macAddress = macAddress;
		this.email = email;
		this.sex = sex;
		this.birthday = birthday;
		this.photo = photo;
		this.accType = accType;
		this.countryCode = countryCode;
		this.imConnect = imConnect;
		this.logStatus = logStatus;
		this.createTime = createTime;
		this.appTypeId = appTypeId;
		this.callStatus = callStatus;
		this.token = token;
		this.phoneModel = phoneModel;
		this.relationProId = relationProId;
		this.pushToken = pushToken;
		this.sysType = sysType;
		this.news = news;
		this.bssId = bssId;
		this.ssId = ssId;
	}

	public User() {
		super();
	}

}
