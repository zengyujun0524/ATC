package com.aura.springboot.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.aura.springboot.service.HelpService;
import com.aura.springboot.utils.AddressUtils;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户使用手册
 * 
 * @author Carry
 *
 */
@RestController
@RequestMapping("/help")
public class HelpController {

	@Autowired
	private HelpService helpService;

	/**
	 * 用户使用手册
	 * 
	 * @param language    语言
	 * @param appType包名图片
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userManual", method = RequestMethod.POST)
	@ApiOperation(value = "用户使用手册", notes = "传入对应的语言选择不同的使用手册")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "language", value = "语言（中文、英文）", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "appType", value = "图片类型（1:AC7. 2:AC5 .3:未绑定）", dataType = "String", paramType = "query") })
	public ResponseResult userMacAddressRegister(String language, String appType) throws Exception {
		if (language == null || StringUtils.isEmpty(language)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		return helpService.selectUserManual(language, appType);
	}

	// 获取外网ip测试
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String test(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		AddressUtils addressUtils = new AddressUtils();
		String ip2 = addressUtils.getV4IP(); // 用于实际判断地址的ip
		System.out.println("外网ip:" + ip2);
		return ip2;
	}
}
