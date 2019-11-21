package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.CallAccount;

@Mapper
public interface CallAccountMapper {

	// 添加通话记录
	int addCallAccount(CallAccount callAccount);

	// 查看通话记录
	List<CallAccount> selectCallAccount(@Param("callFrom") Integer callFrom);

}
