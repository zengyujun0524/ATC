package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 *  	 通话流水记录
 * @author Carry
 *
 */
@Entity
public class CallAccount {
	private Integer callId;				//自增主键
	private String callFrom;			//呼出方（userId）
	private Integer callTo;				//接入方
	private Integer IncomingOutgoing;	//来去电（0：呼入、1：呼出）
	private Integer callType;			//通话类型（0：内呼、1：外呼）
	private String startTime;			//开始时间
	private String endTime;				//结束时间
	private Long callTime;			//通话时长(h)
	public Integer getCallId() {
		return callId;
	}
	public void setCallId(Integer callId) {
		this.callId = callId;
	}
	public String getCallFrom() {
		return callFrom;
	}
	public void setCallFrom(String callFrom) {
		this.callFrom = callFrom;
	}
	public Integer getCallTo() {
		return callTo;
	}
	public void setCallTo(Integer callTo) {
		this.callTo = callTo;
	}
	public Integer getIncomingOutgoing() {
		return IncomingOutgoing;
	}
	public void setIncomingOutgoing(Integer incomingOutgoing) {
		IncomingOutgoing = incomingOutgoing;
	}
	public Integer getCallType() {
		return callType;
	}
	public void setCallType(Integer callType) {
		this.callType = callType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Long getCallTime() {
		return callTime;
	}
	public void setCallTime(Long callTime) {
		this.callTime = callTime;
	}
	
	public CallAccount() {
	}
	
	public CallAccount(Integer callId, String callFrom, Integer callTo, Integer incomingOutgoing, Integer callType,
			String startTime, String endTime, Long callTime) {
		this.callId = callId;
		this.callFrom = callFrom;
		this.callTo = callTo;
		IncomingOutgoing = incomingOutgoing;
		this.callType = callType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.callTime = callTime;
	}
	
	

}
