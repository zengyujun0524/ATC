package com.aura.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.Region;
import com.aura.springboot.mapper.RegionMapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class RegionService {

	private static Logger log = LoggerFactory.getLogger(RegionService.class);
	
	@Autowired
	private RegionMapper regionMapper;
	
	
	/**
	 * 	根据parentId选择国家/城市 传0是查找国家，传parentId查找到对应的国家
	 * @param parentId 父级id
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public ResponseResult selectCityByCountry(Integer parentId) throws Exception{
		Map<String, Object> data = new HashMap<String, Object>();
		log.info("------------获取国家、城市列表--------------》");
		log.info("select regionId,name_cn,name_en,parentId,regionCode\r\n" + 
				"		from\r\n" + 
				"		region\r\n" + 
				"		where parentId="+parentId+"");
		List<Region> list = regionMapper.selectCityByCountry(parentId);
		if(list != null && list.size() > 0) { 
			data.put("region", list);
			return ResponseResult.success(data);
		}
		return ResponseResult.failure(ResultCode.CITYLIST_ERR);
	}
	@Transactional
	public  ResponseResult selectMapByCitycode(String cityCode)throws Exception{
		log.info("------------获取城市编码--------------》" + cityCode);
		
		log.info("------------获取城市区域码--------------》");
		log.info("select regionId,name_cn,name_en,parentId,regionCode\r\n" + 
				"		from\r\n" + 
				"		region\r\n" + 
				"		where citycode="+cityCode+"");
		 Region region =regionMapper.selectMapByCityCode(cityCode);
		 if (region!=null) {
			 Region region2 =regionMapper.selectCountryByParentId(region.getParentId());
		     Map<String, Object> data = new HashMap<String, Object>();
		     
		        /*data.put("name_en", region.getName_en());*/
		       /* data.put("cityCode", region.getRegionCode());
		        data.put("cityname", region.getName_cn());*/
		     /* data.put("regionId", region.getParentId());*/
		     
		      data.put("region",region);
		     data.put("city_cn",region2.getName_cn() );
		     data.put("city_en",region2.getName_en());
		      
			return ResponseResult.success(data);
		}
		 return ResponseResult.failure("");
	}
	
	
}
