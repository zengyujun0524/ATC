package com.aura.springboot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.aura.springboot.service.CallsService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/calls")
public class CallsController {
	private static Logger log = LoggerFactory.getLogger(CallsController.class);

	@Autowired
	private CallsService callsService;

	/**
	 * 插入云端未接电话记录
	 * 
	 * @param userId
	 * @param callNumber
	 * @param macAddress
	 * @param callTime
	 * @param imConnect
	 * @param proId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertCalls", method = RequestMethod.POST)
	@ApiOperation(value = "插入云端未接电话记录", notes = "根据传入参数插入通话记录")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "macAddress", value = "设备地址", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "callNumber", value = "来电号码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "callTime", value = "来电时间", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "imConnect", value = "IM关联账号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "proId", value = "设备ID", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "未接类型 (1.外线未接，2.转接未接 ,3.内呼未接)", dataType = "Integer", paramType = "query") })
	public ResponseResult insertCalls(Integer userId, String callNumber, String macAddress, String callTime,
			String imConnect, Integer proId, Integer type) throws Exception {
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		} else if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		} else if (callNumber == null || StringUtils.isEmpty(callNumber)) {
			return ResponseResult.failure(ResultCode.PHONE_NULL);
		} else if (callTime == null || StringUtils.isEmpty(callTime)) {
			return ResponseResult.failure(ResultCode.ENDTIME_NULL);
		} else if (proId == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROID_NULL);
		} else if (imConnect == null || StringUtils.isEmpty(imConnect)) {
			return ResponseResult.failure(ResultCode.IMCONNECT_NULL);
		}

		return callsService.cachedCalls(userId, callNumber, macAddress, callTime, imConnect, proId, type);
	}

	/**
	 * 查询未接电话
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectCalls", method = RequestMethod.POST)
	@ApiOperation(value = "查询未接电话", notes = "查询未接电话")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Integer", paramType = "query") })
	public ResponseResult selectCalls(Integer userId) throws Exception {
		log.info("dsdssd");
		return callsService.queryCalls(userId);
	}
}