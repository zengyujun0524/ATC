package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Region;

@Mapper
public interface RegionMapper {
	
	//查询国家和城市列表
	List<Region> selectCityByCountry(@Param("parentId")Integer parentId)throws Exception;
	
	//根据城市Cyticode查询区域码
	 Region  selectMapByCityCode(@Param("cityCode")String cityCode)throws Exception;
	 
	//根据国家id查询国家名称
	 Region selectCountryByParentId(@Param("parentId")Integer parentId)throws Exception;
	 
	
}
