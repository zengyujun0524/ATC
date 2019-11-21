package com.aura.springboot.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aura.springboot.entity.History;
import com.aura.springboot.mapper.HistoryMapper;
import com.aura.springboot.service.HistoryService;
import com.aura.springboot.service.UserService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;
import com.aura.springboot.utils.VerificationUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	private HistoryService historyService;

	@Autowired
	private HistoryMapper historyMapper;

	private static Logger log = LoggerFactory.getLogger(HistoryController.class);

	/**
	 * 记录手机登录历史
	 * 
	 * @param userId
	 * @param phoneModel
	 * @param uuId
	 * @param deviceLock
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertHistory", method = RequestMethod.POST)
	@ApiOperation(value = "记录手机登录历史", notes = "记录手机登录历史")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "phoneModel", value = "手机型号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "snNum", value = "手机唯一标识", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "deviceLock", value = "设备锁状态（0：关闭、1：开启）", dataType = "int", paramType = "query") })
	public ResponseResult insertHistory(Integer userId, String phoneModel, String snNum, Integer deviceLock)
			throws Exception {
		if (userId == null)
			return ResponseResult.failure(ResultCode.USERID_NULL);
		if (phoneModel == null)
			return ResponseResult.failure(ResultCode.PHONEMODEL_NULL);
		if (snNum == null)
			return ResponseResult.failure(ResultCode.SNNUM_NULL);
		if (deviceLock == null)
			return ResponseResult.failure(ResultCode.DEVICELOCK_NULL);
		History hi = new History();
		hi.setUserId(userId);
		hi.setPhoneModel(phoneModel);
		hi.setSnNum(snNum);
		hi.setDeviceLock(deviceLock);
		hi.setLastLogonTime(System.currentTimeMillis() + "");
		return historyService.insertHistory(hi);
	}

	/**
	 * 查看设备锁状态
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectDeviceLock", method = RequestMethod.POST)
	@ApiOperation(value = "查看设备锁状态", notes = "查看设备锁状态")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query") })
	public ResponseResult selectDeviceLock(Integer userId) throws Exception {
		if (userId == null)
			return ResponseResult.failure(ResultCode.USERID_NULL);
		return historyService.selectDeviceLock(userId);
	}

	/**
	 * 确定设备锁验证码是否正确
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/valiCode", method = RequestMethod.POST)
	@ApiOperation(value = "确定设备锁验证码是否正确", notes = "确定设备锁验证码是否正确")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "registerCode", value = "短信验证码", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "phoneModel", value = "手机类型", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "snNum", value = "sn号", dataType = "String", paramType = "query") })
	public ResponseResult valiCode(Integer registerCode, Integer userId, String phoneModel, String snNum)
			throws Exception {
		if (registerCode == null) {
			return ResponseResult.failure(ResultCode.REGISTERCODE_NULL);
		}
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		if (phoneModel == null) {
			return ResponseResult.failure(ResultCode.PHONEMODEL_NULL);
		}
		if (snNum == null) {
			return ResponseResult.failure(ResultCode.SNNUM_NULL);
		}
		// 短信验证
//		if (UserController.date == null) { // 如果random是null 证明没有发送短信验证码
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}
//		if (null == UserController.oldPhone || !(UserController.oldPhone).equals(phone)) { // 验证注册的手机号码和发送短信的手机号码是否一致
//			return ResponseResult.failure(ResultCode.REGISTERPHONEANDSENDPHONE_DIFF);
//		}
		// 判断验证码的时效性和准确性（发送时间、发送的验证码、用户传入的验证码）
		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, String> result = VerificationUtils.SMSVerification(UserController.date, UserController.random,
//				registerCode);
//		if ("success".equals(result.get("result"))) {
//			log.info("--------验证码通过--------random：" + UserController.random + ",registerCode：" + registerCode);
//		} else if ("TimeOut".equals(result.get("result"))) {
//			log.info("--------验证超时-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE_OVERDUE);
//		} else if ("registerCodeFail".equals(result.get("result"))) {
//			log.info("--------验证码错误-----------");
//			data.put("status", 0);
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}
//		 如果不存在相同的snNum，则添加新的历史记录
		log.info("------不存在手机历史记录-----");
		History history = new History();
		history.setUserId(userId);
		history.setLastLogonTime(System.currentTimeMillis() + "");
		history.setPhoneModel(phoneModel);
		history.setDeviceLock(1);
		history.setSnNum(snNum);
		Integer in = historyMapper.insertHistory(history);
		if (in > 0) {
			log.info("------新添加了手机登录历史记录-----");
		}
		data.put("status", 1);
		return ResponseResult.success(data);
	}

	/**
	 * 修改设备锁状态
	 * 
	 * @param userId
	 * @param DeviceLock
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateDev", method = RequestMethod.POST)
	@ApiOperation(value = "修改设备锁状态", notes = "查看设备锁状态")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "deviceLock", value = "设备锁状态（0：关闭、1：开启）", dataType = "int", paramType = "query") })
	public ResponseResult updateDev(Integer userId, Integer deviceLock) throws Exception {
		if (userId == null)
			return ResponseResult.failure(ResultCode.USERID_NULL);
		if (deviceLock == null)
			return ResponseResult.failure(ResultCode.DEVICELOCK_NULL);
		return historyService.updateDev(userId, deviceLock);
	}

	/**
	 * 查看手机用户所有登录过的手机
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectHistoryByUserId", method = RequestMethod.POST)
	@ApiOperation(value = "查看手机用户所有登录过的手机", notes = "查看手机用户所有登录过的手机")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query") })
	public ResponseResult selectHistoryByUserId(Integer userId) throws Exception {
		if (userId == null)
			return ResponseResult.failure(ResultCode.USERID_NULL);
		return historyService.selectHistoryByUserId(userId);
	}

	/**
	 * 查看手机用户所有登录过的手机
	 * 
	 * @param historyId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delHistoryById", method = RequestMethod.POST)
	@ApiOperation(value = "查看手机用户所有登录过的手机", notes = "查看手机用户所有登录过的手机")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "historyId", value = "登录历史记录id", dataType = "int", paramType = "query") })
	public ResponseResult delHistoryById(Integer historyId) throws Exception {
		if (historyId == null)
			return ResponseResult.failure(ResultCode.USERID_NULL);
		return historyService.delHistoryById(historyId);
	}

}
