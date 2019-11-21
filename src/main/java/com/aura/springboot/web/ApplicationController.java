package com.aura.springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aura.springboot.service.ApplicationService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 */
/**
 * App模块
 * 
 * @author Carry
 */
@RestController
@RequestMapping("/app")
public class ApplicationController {

	@Autowired
	private ApplicationService applicationService;

	/**
	 * 查看app版本状态
	 * 
	 * @param appPcgName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectVersion", method = RequestMethod.POST)
	@ApiOperation(value = "查看app版本状态", notes = "根据包名查看app版本状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "appPcgName", value = "包名", dataType = "String", paramType = "query") })
	public ResponseResult selectVersionByAppPcgName(String appPcgName) throws Exception {
		if (appPcgName == null) {
			return ResponseResult.failure(ResultCode.APPPCGNAME_NOEXIST);
		}
		return applicationService.selectVersionByAppPcgName(appPcgName);
	}
}
