package com.aura.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HelpMapper {

	// 根据语言和包名查询对应的使用指南
	String selectUserManual(@Param("appType") String appPcgName, @Param("language") String language);
}
