package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 	多语言表
 * 
 * @author Carry
 *
 */
@Entity
public class Language {
	private Integer lanId; 			//多语言id
	private String tipsMsg;			//提示标题
	private String tipsContent;		//提示内容
	private String AddTime;			//添加时间
	private String languageType;	//语言类型
	
	public Language() {
		
	}
	
	public Language(Integer lanId, String tipsMsg, String tipsContent, String addTime, String languageType) {
		this.lanId = lanId;
		this.tipsMsg = tipsMsg;
		this.tipsContent = tipsContent;
		AddTime = addTime;
		this.languageType = languageType;
	}
	public Integer getLanId() {
		return lanId;
	}
	public void setLanId(Integer lanId) {
		this.lanId = lanId;
	}
	public String getTipsMsg() {
		return tipsMsg;
	}
	public void setTipsMsg(String tipsMsg) {
		this.tipsMsg = tipsMsg;
	}
	public String getTipsContent() {
		return tipsContent;
	}
	public void setTipsContent(String tipsContent) {
		this.tipsContent = tipsContent;
	}
	public String getAddTime() {
		return AddTime;
	}
	public void setAddTime(String addTime) {
		AddTime = addTime;
	}
	public String getLanguageType() {
		return languageType;
	}
	public void setLanguageType(String languageType) {
		this.languageType = languageType;
	}
	
	
}
