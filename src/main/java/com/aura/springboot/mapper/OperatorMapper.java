package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Operator;

@Mapper
public interface OperatorMapper {
	
	//根据国家选择运营商
	List<Operator> selectOperatorByCountry(@Param("countryId")Integer countryId)throws Exception;
}
