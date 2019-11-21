package com.aura.springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aura.springboot.service.ActivationService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 	App模块
 * 
 * @author Carry
 *
 */
@RestController
@RequestMapping("/act")
public class ActivationController {
	
	@Autowired
	private ActivationService activationService;
	@RequestMapping(value = "/updateActType", method = RequestMethod.POST)
	@ApiOperation(value = "激活平板设备", notes = "激活平板设备")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "actCode", value = "激活码", dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "proId", value = "设备id", dataType = "int", paramType = "query") })
	public ResponseResult updateActType(String actCode,Integer proId) throws Exception {
		if (actCode == null) {
			return ResponseResult.failure(ResultCode.ACTCODE_NULL);
		}
		if (proId == null) {                                                                                                                                                                    
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROID_NULL);
		}
		return activationService.updateActType(actCode,proId);
	}
      //GET /user/updateLogStatus
}
