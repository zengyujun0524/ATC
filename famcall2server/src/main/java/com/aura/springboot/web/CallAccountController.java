package com.aura.springboot.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import java.util.Date;
//
//import org.joda.time.Interval;
//import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.aura.springboot.entity.CallAccount;
import com.aura.springboot.service.CallAccountService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 设备模块
 * 
 * @author Carry
 *
 */
@RestController
@RequestMapping("/call")
public class CallAccountController {

	@Autowired
	private CallAccountService callAccountService;

	
	private static Logger log = LoggerFactory.getLogger(CallAccountController.class);
	
	/**
	 * 	插入通话记录
	 * @param dialPhone
	 * @param userId
	 * @param callType
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addCallAccount", method = RequestMethod.POST)
	@ApiOperation(value = "插入通话记录", notes = "根据传入参数插入通话记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "callFrom", value = "呼出方（userId）", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "callTo", value = "接入方", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "IncomingOutgoing", value = "来去电（0：呼入、1：呼出）", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "callType", value = "通话类型（0：内呼、1：外呼）", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query") })
	public ResponseResult addCallAccount(String callFrom, Integer callTo,Integer IncomingOutgoing, Integer callType, String startTime,
			String endTime) throws Exception {
		// 非空判断传入信息
		if (callFrom == null || StringUtils.isEmpty(callFrom)) {
			return ResponseResult.failure(ResultCode.CALLFROM_NULL);
		}
		if (callTo == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		if (callType == null) {
			return ResponseResult.failure(ResultCode.CALLTYPE_NULL);
		}
		if (startTime == null || StringUtils.isEmpty(startTime)) {
			return ResponseResult.failure(ResultCode.STARTTIME_NULL);
		}
		if (endTime == null || StringUtils.isEmpty(endTime)) {
			return ResponseResult.failure(ResultCode.ENDTIME_NULL);
		}
		log.info("---------endTime--->"+endTime+",-----startTime---->"+startTime);
		Long callTime = (Long.parseLong(endTime) - Long.parseLong(startTime))/60000;//计算两时间之间的分钟差
		CallAccount callAccount = new CallAccount();
		callAccount.setCallFrom(callFrom);
		callAccount.setCallTo(callTo);
		callAccount.setIncomingOutgoing(IncomingOutgoing);
		callAccount.setCallType(callType);
		callAccount.setStartTime(startTime);
		callAccount.setEndTime(endTime);
		callAccount.setCallTime(callTime);
		return callAccountService.addCallAccount(callAccount);
	}
	
	/**
	 * 查询通话记录
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectCallAccount", method = RequestMethod.POST)
	@ApiOperation(value = "查询通话记录", notes = "根据呼出方查询通话记录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "callFrom", value = "呼出方（userId）", dataType = "Integer", paramType = "query") })
	public ResponseResult updateUserById(Integer callFrom) throws Exception {
		if (callFrom == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		return callAccountService.selectCallAccount(callFrom);
	}
	
	
	/**
	 * 计算两时间之间的分钟差
	 * @param date1
	 * @param date2
	 * @return
	 */
//	private Integer diff(Date date1, Date date2) throws Exception{
//		Interval  interval = new Interval(date1.getTime(),date2.getTime());
//		Period period = interval.toPeriod();
//		return period.getMinutes();
//	}
}
