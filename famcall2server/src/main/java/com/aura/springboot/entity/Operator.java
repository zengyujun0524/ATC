package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 	运营商
 * 
 * @author Carry
 *
 */
@Entity
public class Operator {
	private Integer operatorId; 		// 运营商自增id
	private String operatorName_ch;		// 运营商名称_中文
	private String operatorName_en;		// 运营商名称_英文
	private String rule;				// 拨号规则
	private Integer countryId;			// 国家id
	private String AddTime;				// 添加时间
	
	public Operator() {
		
	}
	
	public Operator(Integer operatorId, String operatorName_ch, String operatorName_en, String rule, Integer countryId,
			String addTime) {
		this.operatorId = operatorId;
		this.operatorName_ch = operatorName_ch;
		this.operatorName_en = operatorName_en;
		this.rule = rule;
		this.countryId = countryId;
		AddTime = addTime;
	}
	
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName_ch() {
		return operatorName_ch;
	}
	public void setOperatorName_ch(String operatorName_ch) {
		this.operatorName_ch = operatorName_ch;
	}
	public String getOperatorName_en() {
		return operatorName_en;
	}
	public void setOperatorName_en(String operatorName_en) {
		this.operatorName_en = operatorName_en;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public String getAddTime() {
		return AddTime;
	}
	public void setAddTime(String addTime) {
		AddTime = addTime;
	}
	
}
