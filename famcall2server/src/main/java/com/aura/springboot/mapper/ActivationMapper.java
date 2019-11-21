package com.aura.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Activation;

@Mapper
public interface ActivationMapper {
	public Activation selectAct(@Param("actCode") String actCode);
	
	//更新激活码状态
	public int updateActType(Activation activation);
	
}
