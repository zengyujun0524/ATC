package com.aura.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.CallAccount;
import com.aura.springboot.mapper.CallAccountMapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class CallAccountService {

	private static Logger log = LoggerFactory.getLogger(CallAccountService.class);
	@Autowired
	private CallAccountMapper callAccountMapper;

	/**
	 * 插入通话记录
	 * 
	 * @param callAccount 通话记录对象
	 * @return
	 * @throws Exception
	 */

	@Transactional
	public ResponseResult addCallAccount(CallAccount callAccount) throws Exception {
		log.info("------------插入通话记录开始--------------");
		log.info("insert into callaccount(dialPhone,userId,callType,startTime,endTime,callTime");
		Integer index = callAccountMapper.addCallAccount(callAccount);
		if (index > 0) {
			log.info("------------插入通话记录成功--------------");
			return ResponseResult.success();
		}
		log.info("------------插入通话记录失败--------------");
		return ResponseResult.failure(ResultCode.ADD_CALLACCOUNT_FAIL);
	}

	/**
	 * 查询通话记录
	 * 
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectCallAccount(Integer userId) throws Exception {
		log.info("------------查询通话记录开始--------------");
		log.info("select callId,dialPhone,userId,callType,startTime,endTime,callTime");
		log.info("from callaccount");
		log.info("where userId = " + userId);
		List<CallAccount> list = callAccountMapper.selectCallAccount(userId);
		if (list != null) {
			log.info("------------查询通话记录成功--------------");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("callAccount", list);
			return ResponseResult.success(data);
		}
		log.info("------------查询通话记录失败--------------");
		return ResponseResult.failure(ResultCode.CALLACCOUNT_NULL);
	}

}
