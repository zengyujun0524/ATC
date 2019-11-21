package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Calls;

@Mapper
public interface CallsMapper {

	// 添加云端缓存未接来电
	int cachedCalls(@Param("userId") Integer userId, @Param("callNumber") String callNumber,
			@Param("macAddress") String macAddress, @Param("callTime") String callTime,
			@Param("imConnect") String imConnect, @Param("proId") Integer proId, @Param("type") Integer type);

	// 获取未接来电记录
	List<Calls> queryCalls(@Param("userId") Integer userId);

	// 清空当前用户缓存
	int delCallsByUser(@Param("userId") Integer userId);

}
