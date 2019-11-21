package com.aura.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.Calls;
import com.aura.springboot.entity.User;
import com.aura.springboot.mapper.CallsMapper;
import com.aura.springboot.mapper.RelationMapper;
import com.aura.springboot.utils.ResponseResult;

@Service
public class CallsService {
	private static Logger log = LoggerFactory.getLogger(CallsService.class);
	@Autowired
	private CallsMapper callsMapper;
	@Autowired
	private RelationMapper RelationMapper;

	// 把时间差转的
	@Transactional
	public ResponseResult cachedCalls(Integer userId, String callNumber, String macAddress, String callTime,
			String imConnect, Integer proId, Integer type) throws Exception {
		log.info("------------------ 云端缓存未接来电 ----------------------------------------");
		log.info("INSERT INTO\r\n" + "   `calls`(userId,callNumber ,macAddress ,callTime ,imConnect ,proId )\r\n"
				+ "	VALUES(" + callNumber + "," + macAddress + "," + callTime + "," + imConnect + "," + proId + ","
				+ type);
		int index;

		index = callsMapper.cachedCalls(userId, callNumber, macAddress, callTime, imConnect, proId, type);

		if (index > 0) {
			return ResponseResult.success();
		}
		return ResponseResult.success();
	}

	// 这种简单的问题
	@Transactional
	public ResponseResult queryCalls(Integer userId) throws Exception {
		log.info("-------------查询未接来电--------------------------");
		log.info("   SELECT  userId,callNumber,macAddress,callTime,imConnect FROM `calls` \\r \\n " + ""
				+ "where userId=" + userId + "");
		Map<String, Object> data = new HashMap<String, Object>();
		List<User> list = RelationMapper.selectRelationByUserId(userId, 0);
		if (list != null) {
			List<Calls> listCalls = callsMapper.queryCalls(userId);
			data.put("listCalls", listCalls);
			int index = callsMapper.delCallsByUser(userId);
			log.info("-------------删除未接来电index--------------------" + index);
			return ResponseResult.success(data);
		}
		return ResponseResult.success();
	}

}
