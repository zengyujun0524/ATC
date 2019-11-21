package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 	反馈OR建议信息
 * 
 * @author Carry
 *
 */
@Entity
public class Feedback {

	private Integer feedbackId; 			// 自增id（主键）
	private String phone; 					// 手机号码
	private String email; 					// 邮箱
	private String feedbackMsg;				// 反馈信息
	private String feedbackTime;			// 反馈时间
	private String feedbackPhoto;			// 反馈图片1
	private String feedbackPhoto2;			// 反馈图片2
	private String feedbackPhoto3;			// 反馈图片3
	private String feedbackPhoto4;			// 反馈图片4
	private Integer type;					// 类型（反馈or建议）
	private Integer completeStatus;			// 解决状态
	
	public Feedback() {
		
	}
	
	
	public Feedback(Integer feedbackId, String phone, String email, String feedbackMsg, String feedbackTime,
			String feedbackPhoto, String feedbackPhoto2, String feedbackPhoto3, String feedbackPhoto4, Integer type,
			Integer completeStatus) {
		this.feedbackId = feedbackId;
		this.phone = phone;
		this.email = email;
		this.feedbackMsg = feedbackMsg;
		this.feedbackTime = feedbackTime;
		this.feedbackPhoto = feedbackPhoto;
		this.feedbackPhoto2 = feedbackPhoto2;
		this.feedbackPhoto3 = feedbackPhoto3;
		this.feedbackPhoto4 = feedbackPhoto4;
		this.type = type;
		this.completeStatus = completeStatus;
	}

	public String getFeedbackPhoto3() {
		return feedbackPhoto3;
	}

	public void setFeedbackPhoto3(String feedbackPhoto3) {
		this.feedbackPhoto3 = feedbackPhoto3;
	}

	public String getFeedbackPhoto4() {
		return feedbackPhoto4;
	}

	public void setFeedbackPhoto4(String feedbackPhoto4) {
		this.feedbackPhoto4 = feedbackPhoto4;
	}
	public Integer getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFeedbackMsg() {
		return feedbackMsg;
	}
	public void setFeedbackMsg(String feedbackMsg) {
		this.feedbackMsg = feedbackMsg;
	}
	public String getFeedbackTime() {
		return feedbackTime;
	}
	public void setFeedbackTime(String feedbackTime) {
		this.feedbackTime = feedbackTime;
	}
	public String getFeedbackPhoto() {
		return feedbackPhoto;
	}
	public void setFeedbackPhoto(String feedbackPhoto) {
		this.feedbackPhoto = feedbackPhoto;
	}
	public String getFeedbackPhoto2() {
		return feedbackPhoto2;
	}
	public void setFeedbackPhoto2(String feedbackPhoto2) {
		this.feedbackPhoto2 = feedbackPhoto2;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCompleteStatus() {
		return completeStatus;
	}
	public void setCompleteStatus(Integer completeStatus) {
		this.completeStatus = completeStatus;
	}
	
}
