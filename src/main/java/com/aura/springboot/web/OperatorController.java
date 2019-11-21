package com.aura.springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aura.springboot.service.OperatorService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 运营商
 * 
 * @author Carry
 *
 */
@RestController
@RequestMapping("/operator")
public class OperatorController {

	@Autowired
	private OperatorService operatorService;

	/**
	 * 根据国家选择运营商
	 * 
	 * @param countryId 国家id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectOperatorByCountryId", method = RequestMethod.POST)
	@ApiOperation(value = "根据国家选择运营商", notes = "根据国家id选择运营商")
	@ApiImplicitParams({ @ApiImplicitParam(name = "countryId", value = "国家id", dataType = "int", paramType = "query") })
	public ResponseResult selectOperatorByCountry(Integer countryId) throws Exception {
		if (countryId == null)
			return ResponseResult.failure(ResultCode.REGIONID_NULL);
		return operatorService.selectOperatorByCountry(countryId);
	}
}
