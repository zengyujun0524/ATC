package com.aura.springboot.web;

import java.io.IOException;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.aura.springboot.entity.User;
import com.aura.springboot.service.UserService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;
import com.aura.springboot.utils.UploadPhoto;
import com.github.qcloudsms.httpclient.HTTPException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户模块
 * 
 * @author Carry
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {
	private static Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserService userService;
	public static Long date = null; // 验证码发送时间戳
	public static Integer random = null; // 验证码
	public static String oldPhone = null; // 发送的手机号

	/**
	 * 手机用户注册
	 * 
	 * @param phoneModel
	 * @param countryCode
	 * @param phone
	 * @param password
	 * @param registerCode
	 * @param appTypeId
	 * @param snNum
	 * @param sysType
	 * @returnupdLogStatus
	 * @throws Exception
	 */
	@RequestMapping(value = "/registerPhone", method = RequestMethod.POST)
	@ApiOperation(value = "手机用户注册", notes = "根据url信息注册手机用户")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "phoneModel", value = "手机型号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "countryCode", value = "国家码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "手机号码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "registerCode", value = "短信注册码", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "appTypeId", value = "项目类型", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "snNum", value = "登录标识", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysType", value = "系统型号（0：安卓、1：苹果）", dataType = "int", paramType = "query") })
	public ResponseResult userPhoneRegister(String phoneModel, String countryCode, String phone, String password,
			Integer registerCode, Integer appTypeId, String snNum, Integer sysType) throws Exception {
		// 非空判断传入信息

		if (countryCode == null || StringUtils.isEmpty(countryCode)) {
			return ResponseResult.failure(ResultCode.COUNTRYCODE_NULL);
		}
		if (password == null || StringUtils.isEmpty(password)) {
			return ResponseResult.failure(ResultCode.PASSWORD_NULL);
		}
		if (registerCode == null) {
			return ResponseResult.failure(ResultCode.REGISTERCODE_NULL);
		}
		if (phone == null || StringUtils.isEmpty(phone) || phone.trim().length() == 0) {
			return ResponseResult.failure(ResultCode.PHONE_NULL);
		}
		if (snNum == null || StringUtils.isEmpty(snNum)) {
			return ResponseResult.failure(ResultCode.SNNUM_NULL);
		}

		// 短信验证
//		if (UserController.date == null) { // 如果random是null 证明没有发送短信验证码
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}
//		if (null == UserController.oldPhone || !(UserController.oldPhone).equals(phone)) { // 验证注册的手机号码和发送短信的手机号码是否一致
//			return ResponseResult.failure(ResultCode.REGISTERPHONEANDSENDPHONE_DIFF);
//		}
//		// 判断验证码的时效性和准确性（发送时间、发送的验证码、用户传入的验证码）
//		Map<String, String> result = VerificationUtils.SMSVerification(UserController.date, UserController.random, 
//				registerCode);
//		if ("success".equals(result.get("result"))) {
//			log.info("--------验证码通过--------random：" + UserController.random + ",registerCode：" + registerCode);
//		} else if ("TimeOut".equals(result.get("result"))) {
//			log.info("--------验证超时-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE_OVERDUE);
//		} else if ("registerCodeFail".equals(result.get("result"))) {
//			log.info("--------验证码错误-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}
		// 开始注册
		User user = new User();
		user.setPhoneModel(phoneModel);
		user.setCountryCode(countryCode);
		user.setPhone(phone);
		user.setPassword(password);
		user.setLogStatus(0); // 注 册默认为离线状态
		user.setCallStatus(0); // 0代表可通话
		user.setAppTypeId(appTypeId);
		user.setCreateTime(System.currentTimeMillis() + "");
		user.setSysType(sysType);
		return userService.registerPhone(user, snNum);
	}

	/**
	 * 发送验证码给用户
	 * 
	 * @param countryCode
	 * @param phone
	 * @return
	 * @throws IOException
	 * @throws HTTPException
	 * @throws JSONException
	 */
	@RequestMapping(value = "/sendRegister", method = RequestMethod.POST)
	@ApiOperation(value = "发送验证码", notes = "发送验证码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "countryCode", value = "国家码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "手机号码", dataType = "String", paramType = "query") })
	public ResponseResult sendRegister(String countryCode, String phone)
			throws JSONException, HTTPException, IOException {
//		// 调用短信注册接口
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (countryCode == null || StringUtils.isEmpty(countryCode)) {
//			return ResponseResult.failure(ResultCode.COUNTRYCODE_NULL);
//		}
//		if (phone == null || StringUtils.isEmpty(phone)) {
//			return ResponseResult.failure(ResultCode.PHONE_NULL);
//		}
//		try {
//			// 发送短信验证码给用户，并返回四位数验证码和发送时间
//			map = VerificationUtils.getRandom(countryCode, phone);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		UserController.oldPhone = phone;
//		UserController.date = (Long) map.get("time");
//		UserController.random = (Integer) map.get("random");
		return ResponseResult.success();
	}

//	//im注册测试接口    邮箱、密码、性别、昵称 not null
//	@RequestMapping(value = "/registerIM", method = RequestMethod.POST)
//	public void registerIM(User user, Integer requestType, Integer device) {
//		UserRegisterDTO redto = new UserRegisterDTO();
//		redto.setNickname("abc");
//		redto.setUser_mail("123");
//		redto.setUser_psw("123");
//		redto.setUser_sex("1");updateLogStatu
//		// 提交请求到http rest服务端
//		DataFromClient dataFromClient = DataFromClient.n().setProcessorId(1008).setJobDispatchId(1).setActionId(7)
//				.setNewData(new Gson().toJson(redto));
//		String ss = new Gson().toJson(dataFromClient);
//		log.info("json 格式--------------------------------------" + ss);
//		String result = HttpClient.doPostJson("http://192.168.1.168:8080/rainbowchat_pro/rest_post", ss);
//		log.info("请求im注册返回信息--------------------------------------" + result);
//
//		JSONObject object = JSONObject.parseObject(result);
//		String aa = object.getString("success");
//		System.out.println("----------------测试SUCCESS返回值------------------------success===》-" + aa);
//
//	}

	/**
	 * 平板用户注册
	 * 
	 * @param macAddress mac地址
	 * @param appTypeId  项目类型
	 * @return
	 */
//	@RequestMapping(value = "/registerPro", method = RequestMethod.POST)
//	@ApiOperation(value = "平板用户注册", notes = "根据url信息注册平板用户")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
//			@ApiImplicitParam(name = "appTypeId", value = "项目类型", dataType = "int", paramType = "query"),
//			@ApiImplicitParam(name = "createTime", value = "创建时间", dataType = "String", paramType = "query") })
//	public ResponseResult userMacAddressRegister(String macAddress, Integer appTypeId, String createTime) {
//		if (macAddress == null || StringUtils.isEmpty(macAddress) || macAddress.trim() == "") {
//			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
//		}
//		return userService.userMacAddressRegister(macAddress, appTypeId, createTime);
//	}

	/**
	 * 更新无绳子机名称
	 * 
	 * @param macAddress mac地址
	 * @param username   项目类型
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateNoRopeMachineName", method = RequestMethod.POST)
	@ApiOperation(value = "更新无绳子机名称", notes = "更新无绳子机名称")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "username", value = "无绳子机名称", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "无绳子机序号（该字段用户是手机号码，无绳子机就是序号）", dataType = "String", paramType = "query") })
	public ResponseResult updateNoRopeMachineName(String macAddress, String username, String phone) throws Exception {
		if (macAddress == null || StringUtils.isEmpty(macAddress) || macAddress.trim() == "") {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (phone == null || StringUtils.isEmpty(phone)) {
			return ResponseResult.failure(ResultCode.PHONE_NULL);
		}
		return userService.updateNoRopeMachineName(macAddress, username, phone);
	}

	/**
	 * 获取平板状态
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectTableStatusByMac", method = RequestMethod.POST)
	@ApiOperation(value = "获取平板状态", notes = "获取平板状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query") })
	public ResponseResult selectTableStatusByMac(String macAddress) throws Exception {
		if (macAddress == null || StringUtils.isEmpty(macAddress) || macAddress.trim() == "") {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		return userService.selectTableStatusByMac(macAddress);
	}

	/**
	 * 根据userId查询对应的手机编号
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectRelationProIdByUserId", method = RequestMethod.POST)
	@ApiOperation(value = "根据userId查询对应的手机编号 ", notes = "根据userId查询对应的手机编号 ")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query") })
	public ResponseResult selectRelationProIdByUserId(Integer userId) throws Exception {
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		return userService.selectRelationProIdByUserId(userId);
	}

	/**
	 * 根据mac地址查询平板用户信息
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectUserByMacAddress", method = RequestMethod.POST)
	@ApiOperation(value = "根据mac地址查询平板用户信息 ", notes = "根据mac地址查询平板用户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query") })
	public ResponseResult selectUserByMacAddress(String macAddress) throws Exception {
		if (macAddress == null) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		return userService.selectUserByMacAddress(macAddress);
	}

	/**
	 * 插入无绳子机
	 * 
	 * @param macAddress
	 * @param username
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertNoRopeMachine", method = RequestMethod.POST)
	@ApiOperation(value = "插入无绳子机", notes = "插入无绳子机")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "username", value = "无绳子机名称", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "无绳子机序号（该字段用户是手机号码，无绳子机就是序号）", dataType = "String", paramType = "query") })
	public ResponseResult insertNoRopeMachine(String macAddress, String username, String phone) throws Exception {
		if (macAddress == null || StringUtils.isEmpty(macAddress) || macAddress.trim() == "") {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (phone == null || StringUtils.isEmpty(phone)) {
			return ResponseResult.failure(ResultCode.PHONE_NULL);
		}
		return userService.insertNoRopeMachine(macAddress, username, phone);
	}

	/**
	 * 清空无绳子机
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/emptySubmachineList", method = RequestMethod.POST)
	@ApiOperation(value = "清空无绳子机", notes = "清空无绳子机")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query") })
	public ResponseResult emptySubmachineList(String macAddress) throws Exception {
		if (macAddress == null || StringUtils.isEmpty(macAddress) || macAddress.trim() == "") {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		return userService.emptySubmachineList(macAddress);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param userId      用户id
	 * @param oldPassword 原始密码
	 * @param newPassword 新密码
	 * @param appTypeId   项目类型
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateUserPwd", method = RequestMethod.POST)
	@ApiOperation(value = "修改用户密码", notes = "根据userId修改用户密码")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "oldPassword", value = "原始密码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "newPassword", value = "新密码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "appTypeId", value = "项目类型", dataType = "int", paramType = "query") })
	public ResponseResult updateUserPwd(Integer userId, String oldPassword, String newPassword, Integer appTypeId)
			throws Exception {
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		if (oldPassword == null || StringUtils.isEmpty(oldPassword)) {
			return ResponseResult.failure(ResultCode.PASSWORD_NULL);
		}
		if (newPassword == null || StringUtils.isEmpty(newPassword)) {
			return ResponseResult.failure(ResultCode.NEWPASSWORD_NULL);
		}
		return userService.updateUserPwd(userId, oldPassword, newPassword, appTypeId);
	}

	//
	/**
	 * 修改在离线状态（im远程更改专用）
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateLogStatus", method = RequestMethod.GET)
	@ApiOperation(value = "修改在离线状态", notes = "根据传入的json数据解析为userId和logStatus修改在离线状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "imConnect", value = "用户im的uid", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "logStatus", value = "登录状态( 0：离线、1：在线、2：登出、3：用户内线在线)", dataType = "int", paramType = "query") })
	public ResponseResult updateLogStatus(String imConnect, Integer logStatus) throws Exception {
		log.info("-------------进入userController修改在离线状态----------------");
		log.info("-------------imConnect---------------->" + imConnect);
		if (imConnect == null)
			return ResponseResult.failure(ResultCode.IMCONNECT_NULL);
		return userService.updateLogStatus(imConnect, logStatus);
	}

	/**
	 * 修改登录状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updLogStatus", method = RequestMethod.POST)
	@ApiOperation(value = "修改登录状态", notes = "修改登录状态")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户uid", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "logStatus", value = "登录状态( 0：离线、1：在线、2：登出、3：用户内线在线)", dataType = "int", paramType = "query") })
	public ResponseResult updLogStatus(Integer userId, Integer logStatus) throws Exception {
		if (userId == null)
			return ResponseResult.failure(ResultCode.USERID_NULL);
		if (logStatus == null)
			return ResponseResult.failure(ResultCode.LOGSTATUS_NULL);
		return userService.updateLogStatus(userId, logStatus);
	}

	/**
	 * 修改通话状态
	 * 
	 * @param phone
	 * @param countryCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateCallStatus", method = RequestMethod.POST)
	@ApiOperation(value = "修改通话状态", notes = "通话状态")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "callStatus", value = "通话状态：0：可通话、1：内呼、2：外呼", dataType = "int", paramType = "query") })
	public ResponseResult updateCallStatus(Integer userId, Integer callStatus) throws Exception {
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		return userService.updateCallStatus(userId, callStatus);
	}

	/**
	 * 手机用户登录
	 * 
	 * @param phone       手机号
	 * @param password    密码
	 * @param countryCode 国家码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/phoneLoginUp", method = RequestMethod.POST)
	@ApiOperation(value = "手机用户登录", notes = "根据手机号码，国家码和密码手机用户登录")
	@ApiImplicitParams({ @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "countryCode", value = "国家码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phoneModel", value = "手机型号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "snNum", value = "登录标识", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysType", value = "系统型号（0：安卓、1：苹果）", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "0 更新token ，1原koten", dataType = "int", paramType = "query") })
	public ResponseResult phoneLoginUp(String phone, String password, String countryCode, String phoneModel,
			String snNum, Integer sysType, int type) throws Exception {
		// 手机号码不能为空
		if (phone == null || StringUtils.isEmpty(phone)) {
			return ResponseResult.failure(ResultCode.PHONE_NULL);
		}
		if (password == null || StringUtils.isEmpty(password)) {
			return ResponseResult.failure(ResultCode.PASSWORD_NULL);
		}
		if (countryCode == null || StringUtils.isEmpty(countryCode)) {
			return ResponseResult.failure(ResultCode.COUNTRYCODE_NULL);
		}
		if (phoneModel == null || StringUtils.isEmpty(phoneModel)) {
			return ResponseResult.failure(ResultCode.PHONEMODEL_NULL);
		}
		if (snNum == null) {
			return ResponseResult.failure(ResultCode.SNNUM_NULL);
		}
		return userService.phoneLoginUp(phone, password, countryCode, phoneModel, snNum, sysType, type);
	}

	@RequestMapping(value = "/offlinePush", method = RequestMethod.POST)
	@ApiOperation(value = "离线推送", notes = "离线推送")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "imConnect", value = "用户im的uid", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "index", value = "呼入类型（1、来电 2、未接来电 3、内呼来电 4、转接来电）", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "呼入号码", dataType = "String", paramType = "query") })
	public void offlinePush(String imConnect, Integer index, String phone) throws Exception {
		if (imConnect == null || StringUtils.isEmpty(imConnect)) {
			log.info("-----imConnect为空----");
			return;

		}
		if (index == null) {
			log.info("-----呼入类型Index为空----");
			return;
		}
		// 手机号码不能为空
		userService.offlinePush(imConnect, index, phone);
	}

	@RequestMapping(value = "/iosOfflinePush", method = RequestMethod.POST)
	@ApiOperation(value = "ios离线推送", notes = "离线推送")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "imConnect", value = "用户im的uid", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "content", value = "推送内容", dataType = "String", paramType = "query") })
	public void iosOfflinePush(String imConnect, String content) throws Exception {
		if (imConnect == null || StringUtils.isEmpty(imConnect)) {
			log.info("-----imConnect为空----");
			return;

		}
		// im不能为空
		userService.iosOfflinePush(imConnect, content);

	}

	//
	@RequestMapping(value = "/updatePushToken", method = RequestMethod.POST)
	@ApiOperation(value = "更新用户离线推送token", notes = "更新用户离线推送token")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "pushToken", value = "离线推送token", dataType = "String", paramType = "query") })
	public ResponseResult updatePushToken(Integer userId, String pushToken) throws Exception {
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		if (pushToken == null || StringUtils.isEmpty(pushToken)) {
			return ResponseResult.failure(ResultCode.PUSHTOKEN_NULL);
		}
		// 手机号码不能为空
		return userService.updatePushToken(userId, pushToken);
	}

	/**
	 * 二次验证登录
	 * 
	 * @param phone
	 * @param countryCode
	 * @param phoneModel
	 * @param snNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/secondVali", method = RequestMethod.POST)
	@ApiOperation(value = "手机用户登录", notes = "根据手机号码，国家码和密码手机用户登录")
	@ApiImplicitParams({ @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "countryCode", value = "国家码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "phoneModel", value = "手机型号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "snNum", value = "登录标识", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysType", value = "系统型号（0：安卓、1：苹果）", dataType = "int", paramType = "query") })
	public ResponseResult secondVali(String phone, String countryCode, String phoneModel, String snNum)
			throws Exception {
		// 手机号码不能为空
		if (phone == null || StringUtils.isEmpty(phone)) {
			return ResponseResult.failure(ResultCode.PHONE_NULL);
		}
		if (countryCode == null || StringUtils.isEmpty(countryCode)) {
			return ResponseResult.failure(ResultCode.COUNTRYCODE_NULL);
		}
		if (phoneModel == null || StringUtils.isEmpty(phoneModel)) {
			return ResponseResult.failure(ResultCode.PHONEMODEL_NULL);
		}
		if (snNum == null) {
			return ResponseResult.failure(ResultCode.SNNUM_NULL);
		}
		return userService.secondVali(phone, countryCode, phoneModel, snNum);
	}

	/**
	 * 平板用户登录
	 * 
	 * @param phone 手机号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/proLoginUp", method = RequestMethod.POST)
	@ApiOperation(value = "平板用户登录", notes = "根据mac地址进行平板登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sysType", value = "系统型号（0：安卓、1：苹果）", dataType = "int", paramType = "query") })
	public ResponseResult proLoginUp(String macAddress, Integer sysType) throws Exception {
		// mac地址不能为空
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.PHONE_NULL);
		}
		return userService.proLoginUp(macAddress, sysType);
	}

	/**
	 * 用户登出
	 * 
	 * @param userId
	 * @param logStatus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logOut", method = RequestMethod.POST)
	@ApiOperation(value = "用户登出", notes = "用户登出")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query") })
	public ResponseResult logOut(Integer userId) throws Exception {
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		return userService.logOut(userId);
	}

	// 上传用户头像
//	@RequestMapping(value = "/upload", method = RequestMethod.POST)
//	@ApiOperation(value = "上传用户头像", notes = "根据userId更换用户头像")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "file", value = "上传图片", dataType = "MultipartFile", paramType = "query"),
//			@ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
//			@ApiImplicitParam(name = "folder", value = "图片路径（传入images）", dataType = "String", paramType = "query") })
//	public ResponseResult upload(@RequestParam(value = "file", required = false) MultipartFile file, Integer userId,
//			String folder) throws Exception {
//		String path = "";
//		if (userId == null)
//			return ResponseResult.failure(ResultCode.USERID_NULL);
//		if (!file.isEmpty()) {
//			// 上传头像 folder传入images
//			// 反馈图片 传入feedback
//			path = UploadPhoto.upload(file, folder, userId);
//			return userService.updateUserPhotoById(userId, path);
//		} else {
//			return ResponseResult.failure(ResultCode.UPLOAD_NULL);
//		}
//	}

	/**
	 * 删除图片
	 * 
	 * @param path
	 * @return
	 */
//	@ApiOperation(value = "删除图片", notes = "根据传入的json对象删除图片")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "path", value = "图片的json格式[{\"p1\":\"1.png\"},{\"p2\":\"2.png\"},{\"p3\":\"3.png\"},{\"p4\":\"4.png\"}]", dataType = "String", paramType = "query") })
//	@RequestMapping(value = "/delPhoto", method = RequestMethod.POST)
//	public ResponseResult delFile(String path) {
//		System.out.println(path);
//		JSONArray arr = JSONArray.parseArray(path);
//		for (int i = 1; i < 5; i++) {
//			JSONObject obj = arr.getJSONObject(i - 1);
//			String imgUrl = (String) obj.get("p" + i);
//			if (imgUrl != null) {
//				int lastIndexOf = path.lastIndexOf("/");
//				String sb = imgUrl.substring(lastIndexOf + 1, imgUrl.length());
//				sb = "E:/tomcat/tomcat8/webapps/AC7FamCall2.0/feedback/" + sb;
//				File file = new File(sb);
//				if (file.exists()) {
//					if (file.delete()) {
//						continue;
//					} else {
//						return ResponseResult.failure(ResultCode.DELETE_FAIL);
//					}
//				} else {
//					return ResponseResult.failure(ResultCode.FILE_NOEXIST);
//				}
//			}
//		}
//		return ResponseResult.success();
//	}

	/**
	 * 根据用户id查看用户信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectUserById", method = RequestMethod.POST)
	@ApiOperation(value = "根据用户id查看用户信息", notes = "根据用户id查看用户信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query") })
	public ResponseResult selectUserById(Integer userId) throws Exception {
		if (userId != null) {
			return userService.selectUserById(userId);
		} else {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
	}

	/**
	 * 忘记/重设密码
	 * 
	 * @param userId       用户id
	 * @param phone        手机号
	 * @param countryCode  国家码
	 * @param password     确认密码
	 * @param registerCode 注册码
	 * @param appTypeId    软件类型（判断是否关联im）
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
	@ApiOperation(value = "忘记/重设密码", notes = "根据用户传入信息重设密码")
	@ApiImplicitParams({ @ApiImplicitParam(name = "phone", value = "手机号码", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "countryCode", value = "国家码", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "password", value = "密码", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "registerCode", value = "短信注册码", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "appTypeId", value = "软件类型（判断是否关联im）", dataType = "int", paramType = "query") })
	public ResponseResult forgetPwd(String phone, String countryCode, String password, Integer registerCode,
			Integer appTypeId) throws Exception {
		Long date = UserController.date; // 获取发送短信的时间
		Integer random = UserController.random; // 获取发送的验证码
		String oldPhone = UserController.oldPhone; // 获取发送接收短信的手机
//		if (phone == null || StringUtils.isEmpty(phone) || phone.trim().length() == 0)
//			return ResponseResult.failure(ResultCode.PHONE_NULL);
//		if (countryCode == null || StringUtils.isEmpty(countryCode) || phone.trim().length() == 0)
//			return ResponseResult.failure(ResultCode.COUNTRYCODE_NULL);
//		if (password == null || StringUtils.isEmpty(password) || phone.trim().length() == 0)
//			return ResponseResult.failure(ResultCode.PASSWORD_NULL);
//		if (registerCode == null)
//			return ResponseResult.failure(ResultCode.REGISTERCODE_NULL);
//		if (appTypeId == null)
//			return ResponseResult.failure(ResultCode.APPTYPEID_NULL);
//		if (random == null) { // 如果random是null 证明没有发送短信验证码
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}
//		if (null == oldPhone || !(oldPhone).equals(phone)) { // 验证注册的手机号码和发送短信的手机号码是否一致
//			return ResponseResult.failure(ResultCode.REGISTERPHONEANDSENDPHONE_DIFF);
//		}
////		 判断验证码的时效性和准确性（发送时间、发送的验证码、用户传入的验证码）
//		Map<String, String> result = VerificationUtils.SMSVerification(date, random, registerCode);
//		if ("success".equals(result.get("result"))) {
//			log.info("--------验证码通过--------random：" + random + ",registerCode：" + registerCode);
//		} else if ("TimeOut".equals(result.get("result"))) {
//			log.info("--------验证超时-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE_OVERDUE);
//		} else if ("registerCodeFail".equals(result.get("result"))) {
//			log.info("--------验证码错误-----------");
//			return ResponseResult.failure(ResultCode.VERIFICATIONCODE);
//		}

		// 重设密码
		return userService.rebuildPwd(phone, countryCode, password, appTypeId);
	}

	/**
	 * 更新/完善用户信息
	 * 
	 * @param userId   用户id
	 * @param userName 用户昵称
	 * @param sex      性别
	 * @param age      年龄
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateUserById", method = RequestMethod.POST)
	@ApiOperation(value = " 更新/完善用户信息", notes = "根据用户传入信息 更新/完善用户信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "username", value = "用户昵称", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "sex", value = "性别", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "birthday", value = "出生日期", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "appTypeId", value = "软件类型（判断是否关联im）", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "file", value = "上传图片", dataType = "MultipartFile", paramType = "query"),
			@ApiImplicitParam(name = "folder", value = "图片路径（传入images）", dataType = "String", paramType = "query") })
	public ResponseResult updateUserById(Integer userId, String username, Integer sex, String birthday,
			Integer appTypeId, @RequestParam(value = "file", required = false) MultipartFile file, String folder)
			throws Exception {
		// 更新用户头像
		String path = "";
		// 更新用户基本信息
		User user = new User();
		if (file != null && !file.isEmpty()) {
			log.info("-------------上传文件--------------");
			// 上传头像 folder传入images
			// 反馈图片 传入feedback
			path = UploadPhoto.upload(file, folder, userId);
			user.setPhoto(path);
		}
		user.setUserId(userId);
		user.setUsername(username);
		user.setSex(sex);
		user.setBirthday(birthday);
		return userService.updateUserInfo(user, appTypeId);
	}

}
