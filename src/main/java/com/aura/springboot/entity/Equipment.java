package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 设备表
 * 
 * @author Carry
 *
 */
@Entity
public class Equipment {
	private Integer proId; // 设备id
	private String macAddress; // mac地址
	private String localPhone; // 座机号码
	private String regionCode; // 区域码
	private Integer operatorId; // 运营商自增id
	private String problem; // 密保问题
	private String answer; // 密保答案
	private String proPhone; // 密保手机
	private String adminCode; // 管理密码
	private String proName; // 设备名称
	private String proNum; // 设备型号
	private String activationTime; // 激活日期
	private String proVersion; // 设备版本号
	private String proSysVersion; // 设备系统版本（安卓）
	private String firmwareSysVersion; // 固件系统版本
	private Integer adminPwdSwitch; // 管理密码开关（0：开启，1：关闭）
	private Integer doNotDisturb; // 勿扰开关（0：开启 1：关闭）
	private Integer Unblock; // 拦截开关（黑名单 0：开启 ， 1：关闭）
	private String disturbFromTime; // 勿扰开始时间
	private String distrubToTime; // 勿扰结束时间
	private String countryCode; // 国家码
	private String country; // 国家
	private String region; // 区域
	private Integer tableStatus; // 平板状态（0：平板不在底座、1：空闲、2：电话线没插上、3：正在通话、4：平板app未运行）
	private String queryToken; // 请求的token
	private String replyToken; // 回复的token
	private Integer actStatus; // 激活字段 0未激活、1已激活
	private String occupier; // 占用者
	private String actEndTime; // 设备激活到期时间
	private String bssId; // 路由器唯一标识
	private String ssId; // 路由器名称

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getLocalPhone() {
		return localPhone;
	}

	public void setLocalPhone(String localPhone) {
		this.localPhone = localPhone;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getProPhone() {
		return proPhone;
	}

	public void setProPhone(String proPhone) {
		this.proPhone = proPhone;
	}

	public String getAdminCode() {
		return adminCode;
	}

	public void setAdminCode(String adminCode) {
		this.adminCode = adminCode;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProNum() {
		return proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	public String getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(String activationTime) {
		this.activationTime = activationTime;
	}

	public String getProVersion() {
		return proVersion;
	}

	public void setProVersion(String proVersion) {
		this.proVersion = proVersion;
	}

	public String getProSysVersion() {
		return proSysVersion;
	}

	public void setProSysVersion(String proSysVersion) {
		this.proSysVersion = proSysVersion;
	}

	public String getFirmwareSysVersion() {
		return firmwareSysVersion;
	}

	public void setFirmwareSysVersion(String firmwareSysVersion) {
		this.firmwareSysVersion = firmwareSysVersion;
	}

	public Integer getAdminPwdSwitch() {
		return adminPwdSwitch;
	}

	public void setAdminPwdSwitch(Integer adminPwdSwitch) {
		this.adminPwdSwitch = adminPwdSwitch;
	}

	public Integer getDoNotDisturb() {
		return doNotDisturb;
	}

	public void setDoNotDisturb(Integer doNotDisturb) {
		this.doNotDisturb = doNotDisturb;
	}

	public Integer getUnblock() {
		return Unblock;
	}

	public void setUnblock(Integer unblock) {
		Unblock = unblock;
	}

	public String getDisturbFromTime() {
		return disturbFromTime;
	}

	public void setDisturbFromTime(String disturbFromTime) {
		this.disturbFromTime = disturbFromTime;
	}

	public String getDistrubToTime() {
		return distrubToTime;
	}

	public void setDistrubToTime(String distrubToTime) {
		this.distrubToTime = distrubToTime;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getTableStatus() {
		return tableStatus;
	}

	public void setTableStatus(Integer tableStatus) {
		this.tableStatus = tableStatus;
	}

	public String getQueryToken() {
		return queryToken;
	}

	public void setQueryToken(String queryToken) {
		this.queryToken = queryToken;
	}

	public String getReplyToken() {
		return replyToken;
	}

	public void setReplyToken(String replyToken) {
		this.replyToken = replyToken;
	}

	public Integer getActStatus() {
		return actStatus;
	}

	public void setActStatus(Integer actStatus) {
		this.actStatus = actStatus;
	}

	public String getOccupier() {
		return occupier;
	}

	public void setOccupier(String occupier) {
		this.occupier = occupier;
	}

	public String getActEndTime() {
		return actEndTime;
	}

	public void setActEndTime(String actEndTime) {
		this.actEndTime = actEndTime;
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

	public Equipment(Integer proId, String macAddress, String localPhone, String regionCode, Integer operatorId,
			String problem, String answer, String proPhone, String adminCode, String proName, String proNum,
			String activationTime, String proVersion, String proSysVersion, String firmwareSysVersion,
			Integer adminPwdSwitch, Integer doNotDisturb, Integer unblock, String disturbFromTime, String distrubToTime,
			String countryCode, String country, String region, Integer tableStatus, String queryToken,
			String replyToken, Integer actStatus, String occupier, String actEndTime, String bssId, String ssId) {
		super();
		this.proId = proId;
		this.macAddress = macAddress;
		this.localPhone = localPhone;
		this.regionCode = regionCode;
		this.operatorId = operatorId;
		this.problem = problem;
		this.answer = answer;
		this.proPhone = proPhone;
		this.adminCode = adminCode;
		this.proName = proName;
		this.proNum = proNum;
		this.activationTime = activationTime;
		this.proVersion = proVersion;
		this.proSysVersion = proSysVersion;
		this.firmwareSysVersion = firmwareSysVersion;
		this.adminPwdSwitch = adminPwdSwitch;
		this.doNotDisturb = doNotDisturb;
		Unblock = unblock;
		this.disturbFromTime = disturbFromTime;
		this.distrubToTime = distrubToTime;
		this.countryCode = countryCode;
		this.country = country;
		this.region = region;
		this.tableStatus = tableStatus;
		this.queryToken = queryToken;
		this.replyToken = replyToken;
		this.actStatus = actStatus;
		this.occupier = occupier;
		this.actEndTime = actEndTime;
		this.bssId = bssId;
		this.ssId = ssId;
	}

	public Equipment() {
		super();
	}

}
