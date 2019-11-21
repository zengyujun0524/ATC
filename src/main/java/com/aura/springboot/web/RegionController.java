package com.aura.springboot.web;

import java.net.Inet4Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aura.springboot.map.GetLocationBaiduMap;
import com.aura.springboot.service.RegionService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 	区域码
 * 
 * @author Carry
 *
 */
@RestController
@RequestMapping("/region")
public class RegionController {

	@Autowired
	private RegionService regionService;
	

	/**
	 * 	根据parentId选择国家/城市 传0是查找国家，传parentId查找到对应的国家
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectRegionList", method = RequestMethod.POST)
	@ApiOperation(value = "选择国家/城市列表", notes = "根据parentId选择国家/城市 传0是查找国家，传parentId查找到对应的国家")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "parentId", value = "父级id（0：返回国家列表）", dataType = "int", paramType = "query") })
	public ResponseResult selectCityByCountry(Integer parentId) throws Exception {
		if(parentId == null)
			return ResponseResult.failure(ResultCode.PARENTID_NULL);
		return regionService.selectCityByCountry(parentId);
	}
	
 
	
@RequestMapping(value="/selectMapInfo", method=RequestMethod.POST)
@ApiOperation(value="选择国家/城市列表", notes="根据cotiycode查询，返回城市区域码")
@ApiImplicitParams({
		@ApiImplicitParam(name="longitude",value="精度",dataType="String",paramType="query"),
		@ApiImplicitParam(name="latitude",value="纬度",dataType="String",paramType="query")
})
	public ResponseResult  selectMapInfo(String longitude,String latitude)throws Exception{
	    GetLocationBaiduMap map = new GetLocationBaiduMap();
		String cityCode =  map.getLocationByBaiduMap( latitude,longitude); 
		return regionService.selectMapByCitycode(cityCode);
	}
//	/**
//	 * 	设置区域码
//	 * 
//	 * @param macAddress	mac地址
//	 * @param regionCode	区域码
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(value = "/setRegionCode", method = RequestMethod.POST)
//	@ApiOperation(value = "设置区域码", notes = "设置区域码")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "macAddress", value = "区域id", dataType = "int", paramType = "query") })
//	public ResponseResult setRegionCode(String macAddress,String regionCode) throws Exception {
//		if(macAddress == null || StringUtils.isEmpty(macAddress))
//			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
//		if(regionCode == null || StringUtils.isEmpty(regionCode))
//			return ResponseResult.failure(ResultCode.REGIONCODE_NULL);
//		return regionService.setRegionCode(macAddress,regionCode);
//	}
}
