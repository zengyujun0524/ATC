package com.aura.springboot.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.aura.springboot.entity.Equipment;
import com.aura.springboot.service.EquipmentService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 设备模块
 * 
 * @author Mccree
 *
 */
@RestController
@RequestMapping("/equi")
public class EquipmentController {

	private static Logger log = LoggerFactory.getLogger(EquipmentController.class);

	@Autowired
	private EquipmentService equipmentService; // 设备服务

	/**
	 * 注册设备
	 * 
	 * @param macAddress         mac地址
	 * @param proNum             平板型号
	 * @param proVersion         设备版本号
	 * @param proSysVersion      设备系统版本（安卓）
	 * @param firmwareSysVersion 固件系统版本
	 * @param adminPwdSwitch     管理密码开关（0：开启，1：关闭）
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/registerEqui", method = RequestMethod.POST)
	@ApiOperation(value = "注册设备", notes = "根据传入参数注册设备")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "proNum", value = "平板型号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "proVersion", value = "设备版本号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "proSysVersion", value = "设备系统版本（安卓）", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "firmwareSysVersion", value = "固件系统版本", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "adminPwdSwitch", value = "管理密码开关（0：开启，1：关闭）", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "activationTime", value = "注册时间（时间戳）", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "appTypeId", value = "项目类型", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "pushToken", value = "离线推送token", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysType", value = "系统型号（0：安卓、1：苹果）", dataType = "int", paramType = "query") })
	public ResponseResult insertEquipment(String macAddress, String proNum, String proVersion, String proSysVersion,
			String firmwareSysVersion, String adminPwdSwitch, String activationTime, String appTypeId, String pushToken,
			Integer sysType) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}

		if (proNum == null || StringUtils.isEmpty(proNum)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PRONUM_NULL);
		}
		if (proVersion == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROVERSION_NULL);
		}
		if (proSysVersion == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROSYSVERSION_NULL);
		}
		if (firmwareSysVersion == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_FIRMWARESYSVERSION_NULL);
		}
		Equipment equipment = new Equipment();

		equipment.setMacAddress(macAddress); //
		equipment.setProName(proNum);
		equipment.setProNum(proNum);
		equipment.setActivationTime(activationTime); // 注册时间
		equipment.setProVersion(proVersion);
		equipment.setProSysVersion(proSysVersion);
		equipment.setFirmwareSysVersion(firmwareSysVersion);
		equipment.setAdminPwdSwitch(Integer.parseInt(adminPwdSwitch));
		equipment.setDoNotDisturb(1); // 勿扰开关 默认关闭
		equipment.setUnblock(1); // 拦截开关 默认关闭
		return equipmentService.insertEquipment(equipment, Integer.parseInt(appTypeId), pushToken, sysType, proNum);
	}

	/**
	 * 白名单勿扰开启/关闭
	 * 
	 * @param macAddress
	 * @param doNotDisturb
	 * @param disturbFromTime
	 * @param distrubToTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doNotDisturbUpdate", method = RequestMethod.POST)
	@ApiOperation(value = "白名单勿扰开启/关闭", notes = "白名单勿扰开启/关闭")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "doNotDisturb", value = "勿扰状态开关", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "disturbFromTime", value = "勿扰开始时间", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "distrubToTime", value = "勿扰结束时间", dataType = "String", paramType = "query") })
	public ResponseResult doNotDisturbUpdate(String macAddress, Integer doNotDisturb, String disturbFromTime,
			String distrubToTime) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (disturbFromTime == "") {
			disturbFromTime = null;
		}
		if (distrubToTime == "") {
			distrubToTime = null;
		}
		return equipmentService.doNotDisturbUpdate(macAddress, doNotDisturb, disturbFromTime, distrubToTime);
	}

	/**
	 * 更新平板状态
	 * 
	 * @param macAddress
	 * @param tableStatus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateTableStauts", method = RequestMethod.POST)
	@ApiOperation(value = "更新平板状态", notes = "更新平板状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "tableStatus", value = "平板状态（0：平板不在底座、1：空闲、2：电话线没插上、3：正在通话、4：平板app未运行）", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "occupier", value = "占用者", dataType = "String", paramType = "query") })
	public ResponseResult updateTableStauts(String macAddress, Integer tableStatus, String occupier) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (tableStatus == null) {
			return ResponseResult.failure(ResultCode.EQUISTATUS_NULL);
		}
		return equipmentService.updateTableStauts(macAddress, tableStatus, occupier);
	}

	/**
	 * 更新国家和地区
	 * 
	 * @param macAddress
	 * @param country
	 * @param region
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/updateCARByMac", method = RequestMethod.POST)
//	@ApiOperation(value = "更新国家和地区", notes = "更新国家和地区")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
//			@ApiImplicitParam(name = "country", value = "国家", dataType = "String", paramType = "query"),
//			@ApiImplicitParam(name = "region", value = "地区", dataType = "String", paramType = "query") })
//	public ResponseResult updateCARByMac(String macAddress,String country,String region) throws Exception {
//		// 非空判断传入信息
//		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
//			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
//		}
//		return equipmentService.updateCARByMac(macAddress, country, region);
//	}

	/**
	 * 黑名单拦截开关
	 * 
	 * @param macAddress
	 * @param Unblock
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/UnblockUpdate", method = RequestMethod.POST)
	@ApiOperation(value = "黑名单拦截开关", notes = "黑名单拦截开关")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "Unblock", value = "拦截开关（黑名单 0：开启 ， 1：关闭）", dataType = "int", paramType = "query") })
	public ResponseResult UnblockUpdate(String macAddress, Integer Unblock) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (Unblock == null) {
			return ResponseResult.failure(ResultCode.UNBLOCK_NULL);
		}
		return equipmentService.UnblockUpdate(macAddress, Unblock);
	}

	/**
	 * 根据mac地址查询设备信息
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectEquByMac", method = RequestMethod.POST)
	@ApiOperation(value = "查询设备信息", notes = "根据mac地址查询设备信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query") })
	public ResponseResult selectEquByMac(String macAddress, HttpServletRequest request) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}

		return equipmentService.selectEquByMac(request, macAddress);
	}

	/**
	 * 根据项目名称获取平板结构图
	 * 
	 * @param appName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectAppStructsBg", method = RequestMethod.POST)
	@ApiOperation(value = "根据项目名称获取平板结构图", notes = "根据项目名称获取平板结构图")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "appName", value = "项目名称（例如：AC7FamCall2.0）", dataType = "String", paramType = "query") })
	public ResponseResult selectAppStructsBg(String appName) throws Exception {
		// 非空判断传入信息
		if (appName == null || StringUtils.isEmpty(appName)) {
			return ResponseResult.failure(ResultCode.APPNAME_NOEXIST);
		}
		return equipmentService.selectAppStructsBg(appName);
	}

	/**
	 * 根据mac地址查询设备白名单信息
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectDoNotDisturbByMac", method = RequestMethod.POST)
	@ApiOperation(value = "查询设备白名单信息", notes = "根据mac地址查询设备白名单信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query") })
	public ResponseResult selectDoNotDisturbByMac(String macAddress) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		return equipmentService.selectDoNotDisturbByMac(macAddress);
	}

	/**
	 * 设置区域码
	 * 
	 * @param macAddress mac地址
	 * @param regionCode 区域码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setRegionCode", method = RequestMethod.POST)
	@ApiOperation(value = "设置区域码/国家/地区", notes = "设置区域码/国家/地区")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "regionCode", value = "区域码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "country", value = "国家", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "region", value = "区域", dataType = "String", paramType = "query") })
	public ResponseResult setRegionCode(String macAddress, String regionCode, String country, String region)
			throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (regionCode == null || StringUtils.isEmpty(regionCode)) {
			return ResponseResult.failure(ResultCode.REGIONCODE_NULL);
		}
		return equipmentService.setRegionCode(macAddress, regionCode, country, region);
	}

	/**
	 * 通过密保手机设置/更新管理密码
	 * 
	 * @param macAddress mac地址
	 * @param adminCode  管理密码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAdminCode", method = RequestMethod.POST)
	@ApiOperation(value = "通过密保手机设置/更新管理密码", notes = "通过密保手机设置/更新管理密码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "adminCode", value = "管理密码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "手机号码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "countryCode", value = "国家码", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "registerCode", value = "短信注册码", dataType = "int", paramType = "query") })
	public ResponseResult updateAdminCode(String macAddress, String adminCode, String phone, String countryCode,
			Integer registerCode) throws Exception {
		Long date = UserController.date; // 获取发送短信的时间
		Integer random = UserController.random; // 获取发送的验证码

		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (adminCode == null || StringUtils.isEmpty(adminCode)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_ADMINCODE_NULL);
		}
		if (registerCode == null) {
			return ResponseResult.failure(ResultCode.REGISTERCODE_NULL);
		}
//		if (random == null) { // 如果random是null 证明没有发送短信验证码
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}
//		 判断验证码的时效性和准确性（发送时间、发送的验证码、用户传入的验证码）
//		Map<String, String> result = VerificationUtils.SMSVerification(date, random,
//				registerCode);
//		if ("success".equals(result.get("result"))) {
//			log.info("--------验证码通过--------random：" + random + ",registerCode：" + registerCode);
//		} else if ("TimeOut".equals(result.get("result"))) {
//			log.info("--------验证超时-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE_OVERDUE);
//		} else if ("registerCodeFail".equals(result.get("result"))) {
//			log.info("--------验证码错误-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}
		return equipmentService.updateAdminCode(macAddress, adminCode);
	}

	/**
	 * 初次设置管理密码
	 * 
	 * @param macAddress mac地址
	 * @param adminCode  管理密码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/setAdminCode", method = RequestMethod.POST)
	@ApiOperation(value = "初次设置管理密码", notes = "初次设置管理密码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "adminCode", value = "管理密码", dataType = "String", paramType = "query") })
	public ResponseResult setAdminCode(String macAddress, String adminCode) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (adminCode == null || StringUtils.isEmpty(adminCode)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_ADMINCODE_NULL);
		}
		return equipmentService.updateAdminCode(macAddress, adminCode);
	}

	/**
	 * 使用原始管理密码修改密码
	 * 
	 * @param macAddress mac地址
	 * @param adminCode  管理密码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAdminCodeByOldAPwd", method = RequestMethod.POST)
	@ApiOperation(value = "使用原始管理密码修改密码", notes = "使用原始管理密码修改密码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "adminCode", value = "新管理密码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "oldadminCode", value = "原始管理密码", dataType = "String", paramType = "query") })
	public ResponseResult updateAdminCodeByOldAPwd(String macAddress, String adminCode, String oldadminCode)
			throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (adminCode == null || StringUtils.isEmpty(adminCode)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_ADMINCODE_NULL);
		}
		if (oldadminCode == null || StringUtils.isEmpty(oldadminCode)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_ADMINCODE_NULL);
		}
		return equipmentService.updateAdminCodeByOldAPwd(macAddress, adminCode, oldadminCode);
	}

	/**
	 * 通过密保问题修改管理密码
	 * 
	 * @param macAddress mac地址
	 * @param adminCode  管理密码
	 * @param answer     密保答案
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAdminCodeByAnswer", method = RequestMethod.POST)
	@ApiOperation(value = "通过密保问题修改管理密码", notes = "根据传入参数通过密保问题修改管理密码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "adminCode", value = "管理密码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "answer", value = "密保答案", dataType = "String", paramType = "query") })
	public ResponseResult updateAdminCodeByAnswer(String macAddress, String adminCode, String answer) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (adminCode == null || StringUtils.isEmpty(adminCode)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_ADMINCODE_NULL);
		}
		if (answer == null || StringUtils.isEmpty(answer)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_ANSWER_NULL);
		}
		return equipmentService.updateAdminCodeByAnswer(macAddress, adminCode, answer);
	}

	/**
	 * 设置/修改密保问题
	 * 
	 * @param macAddress mac地址
	 * @param problem    密保问题
	 * @param answer     密保答案
	 * @return 我
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateProblem", method = RequestMethod.POST)
	@ApiOperation(value = "设置/修改密保问题", notes = "根据传入参数设置/修改密保问题")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "problem", value = "密保问题", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "answer", value = "密保答案", dataType = "String", paramType = "query") })
	public ResponseResult updateProblem(String macAddress, String problem, String answer) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (problem == null || StringUtils.isEmpty(problem)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROBLEM_NULL);
		}
		if (answer == null || StringUtils.isEmpty(answer)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_ANSWER_NULL);
		}
		return equipmentService.updateProblem(macAddress, problem, answer);
	}

	/**
	 * 设置/修改密保手机
	 * 
	 * @param macAddress mac地址
	 * @param proPhone   密保手机
	 * @return 本身 就没多少钱了
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateProPhone", method = RequestMethod.POST)
	@ApiOperation(value = "设置/修改密保手机", notes = "根据传入参数设置/修改密保手机")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "proPhone", value = "密保手机", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "countryCode", value = "国家码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "registerCode", value = "短信注册码", dataType = "int", paramType = "query") })
	public ResponseResult updateProPhone(String macAddress, String proPhone, String countryCode, Integer registerCode)
			throws Exception {
		Long date = UserController.date; // 获取发送短信的时间
		Integer random = UserController.random; // 获取发送的验证码
		String oldPhone = UserController.oldPhone; // 获取发送接收短信的手机

		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (proPhone == null || StringUtils.isEmpty(proPhone)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROPHONE_NULL);
		}
		if (registerCode == null) {
			return ResponseResult.failure(ResultCode.REGISTERCODE_NULL);
		}
//		if (random == null) { // 如果random是null 证明没有发送短信验证码
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}
//		if (null == oldPhone || !(oldPhone).equals(proPhone)) { // 验证注册的手机号码和发送短信的手机号码是否一致
//			return ResponseResult.failure(ResultCode.REGISTERPHONEANDSENDPHONE_DIFF);
//		}
		// 判断验证码的时效性和准确性（发送时间、发送的验证码、用户传入的验证码）
//		Map<String, String> result = VerificationUtils.SMSVerification(date, random,
//				registerCode);
//		if ("success".equals(result.get("result"))) {
//			log.info("--------验证码通过--------random：" + random + ",registerCode：" + registerCode);
//		} else if ("TimeOut".equals(result.get("result"))) {
//			log.info("--------验证超时-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE_OVERDUE);
//		} else if ("registerCodeFail".equals(result.get("result"))) {
//			log.info("--------验证码错误-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}

		return equipmentService.updateProPhone(macAddress, proPhone, countryCode);
	}

	/**
	 * 设置/修改座机号码
	 * 
	 * @param macAddress mac地址
	 * @param localPhone 座机号码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateLocalPhone", method = RequestMethod.POST)
	@ApiOperation(value = "设置/修改座机号码", notes = "根据传入参数设置/修改座机号码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "localPhone", value = "座机号码", dataType = "String", paramType = "query") })
	public ResponseResult updateLocalPhone(String macAddress, String localPhone) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (localPhone == null || StringUtils.isEmpty(localPhone)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATE_LOCALPHONE_NULL);
		}
		return equipmentService.updateLocalPhone(macAddress, localPhone);
	}

	/**
	 * 修改运营商
	 * 
	 * @param macAddress mac地址
	 * @param operatorId 运营商id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateOperator", method = RequestMethod.POST)
	@ApiOperation(value = "修改运营商", notes = "根据传入参数修改运营商")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "operatorId", value = "运营商id", dataType = "int", paramType = "query") })
	public ResponseResult updateOperator(String macAddress, Integer operatorId) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (operatorId == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_OPERATORID_NULL);
		}
		return equipmentService.updateOperator(macAddress, operatorId);
	}

	/**
	 * 设置座机号码和设备名称
	 * 
	 * @param macAddress mac地址
	 * @param localPhone 座机号码
	 * @param proName    座机名称
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateLocalPhoneAndProName", method = RequestMethod.POST)
	@ApiOperation(value = "设置座机号码和设备名称", notes = "根据传入参数设置座机号码和设备名称")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "localPhone", value = "座机号码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "proName", value = "座机名称", dataType = "String", paramType = "query") })
	public ResponseResult updateLocalPhoneAndProName(String macAddress, String localPhone, String proName)
			throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		return equipmentService.updateLocalPhoneAndProName(macAddress, localPhone, proName);
	}

	/**
	 * 修改设备名称
	 * 
	 * @param macAddress
	 * @param proName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateProName", method = RequestMethod.POST)
	@ApiOperation(value = "修改设备名称", notes = "根据传入参数修改设备名称")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "proName", value = "座机名称", dataType = "String", paramType = "query") })
	public ResponseResult updateProName(String macAddress, String proName) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (proName == null || StringUtils.isEmpty(proName)) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PRONAME_NULL);
		}
		return equipmentService.updateProName(macAddress, proName);
	}

	/**
	 * 修改管理密码状态
	 * 
	 * @param macAddress     mac地址
	 * @param adminPwdSwitch 管理密码状态
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAdminCodeStatus", method = RequestMethod.POST)
	@ApiOperation(value = "修改管理密码状态", notes = "根据传入参数修改管理密码状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "adminPwdSwitch", value = "管理密码开关（0：开启，1：关闭）", dataType = "int", paramType = "query") })
	public ResponseResult updateAdminCodeStatus(String macAddress, Integer adminPwdSwitch) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (adminPwdSwitch == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PRONAME_NULL);
		}
		return equipmentService.updateAdminCodeStatus(macAddress, adminPwdSwitch);
	}

	/**
	 * 查询管理密码状态
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectAdminCodeStatus", method = RequestMethod.POST)
	@ApiOperation(value = "查询管理密码状态", notes = "根据传入参数查询管理密码状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query") })
	public ResponseResult selectAdminCodeStatus(String macAddress) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		return equipmentService.selectAdminCodeStatus(macAddress);
	}

	/**
	 * 获取设备图片
	 * 
	 * @param macAddress     mac地址
	 * @param adminPwdSwitch 管理密码状态
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getDevicePictures", method = RequestMethod.POST)
	@ApiOperation(value = "获取设备图片", notes = "根据传入参数获取相应的类型")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "deviceType", value = "设备型号(0:AC5 ,1:AC7 ，2:AC5图片集合，3:AC7图片集合)", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "index", value = "设备图片类型(1:内呼列表，2：内呼去电界面，3：设备码小图 ，4：扫码图，5：底座图片，6平板：图片)", dataType = "int", paramType = "query") })
	public ResponseResult getDevicePictures(Integer deviceType, Integer index) throws Exception {
		// 非空判断传入信息
		if (deviceType == null) {
			return ResponseResult.failure(ResultCode.DEVICE_DEVICETYPE_NULL);
		}
		if (index == null) {
			return ResponseResult.failure(ResultCode.DEVICE_IMAGETYPE_NULL);
		}
		return equipmentService.getDevicePictures(deviceType, index);
	}

}
