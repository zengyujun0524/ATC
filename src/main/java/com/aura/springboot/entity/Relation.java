package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 	设备关系表
 * 
 * @author Carry
 *
 */
@Entity
public class Relation {
	private Integer relationId;		//设备id
	private Integer proId;			//平板id
	private Integer userId;			//用户id
	private String bindingTime;		//绑定时间
	private String relieveTime;		//解绑时间
	private Integer bindingStatus;	//绑定状态（0绑定、1解除绑定）
	private Integer relationProId;	//关联序号（10-99）
	private User user;				//用户信息
	
	
	
	public Relation() {
		
	}
	
	public Relation(Integer relationId, Integer proId, Integer userId, String bindingTime, String relieveTime,
			Integer bindingStatus, Integer relationProId) {
		this.relationId = relationId;
		this.proId = proId;
		this.userId = userId;
		this.bindingTime = bindingTime;
		this.relieveTime = relieveTime;
		this.bindingStatus = bindingStatus;
		this.relationProId = relationProId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getRelationId() {
		return relationId;
	}
	public Integer getRelationProId() {
		return relationProId;
	}


	public void setRelationProId(Integer relationProId) {
		this.relationProId = relationProId;
	}


	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}
	public Integer getProId() {
		return proId;
	}
	public void setProId(Integer proId) {
		this.proId = proId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getBindingTime() {
		return bindingTime;
	}
	public void setBindingTime(String bindingTime) {
		this.bindingTime = bindingTime;
	}
	public String getRelieveTime() {
		return relieveTime;
	}
	public void setRelieveTime(String relieveTime) {
		this.relieveTime = relieveTime;
	}
	public Integer getBindingStatus() {
		return bindingStatus;
	}
	public void setBindingStatus(Integer bindingStatus) {
		this.bindingStatus = bindingStatus;
	}
}
