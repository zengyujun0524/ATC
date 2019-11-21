package com.aura.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.Operator;
import com.aura.springboot.mapper.OperatorMapper;
import com.aura.springboot.utils.ResponseResult;

@Service
public class OperatorService {


	private static Logger log = LoggerFactory.getLogger(OperatorService.class);
	@Autowired
	private OperatorMapper operatorMapper;
	
	/**
	 * 	根据国家选择运营商
	 * @param regionId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectOperatorByCountry(Integer countryId)
			throws Exception {
		log.info("------------查询运营商-----------");
		log.info("select\r\n" + 
				"		operatorId,operatorName_ch,operatorName_en,rule,countryId,AddTime from\r\n" + 
				"		operator\r\n" + 
				"		where countryId = "+countryId+"");
		List<Operator> list = operatorMapper.selectOperatorByCountry(countryId);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user", list);
		return ResponseResult.success(data);
	}
	
}
