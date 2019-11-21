package com.aura.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Ipv4;

@Mapper
public interface Ipv4Mapper {
	Integer insertIpv4(@Param("macAddress") String macAddress, @Param("imConnect") String imConnect) throws Exception;;

	Integer updateIpv4(@Param("macAddress") String macAdderss, @Param("imConnect") String imConnect,
			@Param("bssId") String bssId, @Param("ssId") String ssId) throws Exception;;

	Ipv4 selectIpv4(@Param("imConnect") String imConnect) throws Exception;;

}
