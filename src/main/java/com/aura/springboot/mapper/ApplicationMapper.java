package com.aura.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Application;

@Mapper
public interface ApplicationMapper {
	
	/**
	 * 	根据包名查询最新的版本号
	 * @param appPcgName
	 * @return
	 */
	public Application selectVersionByAppPcgName(@Param("appPcgName")String appPcgName);
}
