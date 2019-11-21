package com.aura.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Security;

@Mapper
public interface SecurityMapper {
	// 插入密保问题
	int insertSecuityQuestion(Security security) throws Exception;

	// 查询密保问题
	Security selectSecuityQuestion(@Param("userId") int userId) throws Exception;

	// 更新密保问题
	int updateSecuityQuestion(Security security) throws Exception;

}
