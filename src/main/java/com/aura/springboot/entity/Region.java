package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 	地区表
 * 
 * @author Carry 
 *
 */
@Entity
public class Region {
	private Integer regionId;	// 地区id
	private String name_cn;		// 地区名称_中文
	private String name_en;		// 地区名称_英文
	private Integer parentId;	// 父级id
	private String regionCode;	// 区域码
	
	public Region() {
		
	}
	public Region(Integer regionId, String name_cn, String name_en, Integer parentId, String regionCode) {
		this.regionId = regionId;
		this.name_cn = name_cn;
		this.name_en = name_en;
		this.parentId = parentId;
		this.regionCode = regionCode;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public String getName_cn() {
		return name_cn;
	}
	public void setName_cn(String name_cn) {
		this.name_cn = name_cn;
	}
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	
}
