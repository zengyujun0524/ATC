package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Relation;
import com.aura.springboot.entity.User;
import com.aura.springboot.utils.Lock;
import com.aura.springboot.utils.LockModeType;

@Mapper
public interface RelationMapper {

	
	//绑定设备
	Integer binding(Relation relation);

	//手机解绑设备
	Integer unBinding(@Param("userId")Integer userId);
	
	//平板解绑设备手机
	Integer unBindingPro(@Param("proId")Integer proId);

	//根据userId查询设备列表	(accType 0：手机、1：平板)
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	List<User> selectRelationByUserId(@Param("userId")Integer userId,@Param("accType")Integer accType);
	
	//根据userId查出对应的平板信息
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	User selectProByUserId(@Param("userId")Integer userId);
	
	//查看是否已绑定信息
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	Relation selectCount(@Param("userId")Integer userId);
	
	//查看绑定的设备id（手机看平板）
	Relation  selectProIdByUserId(@Param("userId")Integer userId);
	
	//根据mac地址查询绑定手机的序号集合
	List<Integer> numList(@Param("macAddress")String macAddress);
	
}
