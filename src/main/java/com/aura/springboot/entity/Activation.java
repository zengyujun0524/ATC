package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 激活
 * @author Carry
 *
 */
@Entity
public class Activation {
	private Integer actId;		//ID
	private String actCode;		//激活码
	private String actTime;	//激活时间
	private Integer proId;		//平板id
	private Integer actStatus;	//激活状态 0未激活、1已激活
	
	public Activation(Integer actId, String actCode, String actTime, Integer proId, Integer actStatus) {
		this.actId = actId;
		this.actCode = actCode;
		this.actTime = actTime;
		this.proId = proId;
		this.actStatus = actStatus;
	}
	  
	public Activation() {
		
	}
	
	public Integer getActId() {
		return actId;
	}
	public void setActId(Integer actId) {
		this.actId = actId;
	}
	public String getActCode() {
		return actCode;
	}
	public void setActCode(String actCode) {
		this.actCode = actCode;
	}
	public String getActTime() {
		return actTime;
	}
	public void setActTime(String actTime) {
		this.actTime = actTime;
	}
	public Integer getProId() {
		return proId;
	}
	public void setProId(Integer proId) {
		this.proId = proId;
	}
	public Integer getActStatus() {
		return actStatus;
	}
	public void setActStatus(Integer actStatus) {
		this.actStatus = actStatus;
	}
}
