package com.aura.springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aura.springboot.service.Ipv4Service;
import com.aura.springboot.utils.ResponseResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/Ipv4")
public class Ipv4Controller {
	@Autowired
	private Ipv4Service ipv4Service;

	/**
	 * 输入局域网配置信息
	 * 
	 * @param imConnect
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertIpv4", method = RequestMethod.POST)
	@ApiOperation(value = "输入局域网配置信息", notes = "输入局域网配置信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "imConnect", value = "im账号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query") })
	public ResponseResult insertIpv4(String imConnect, String macAddress) throws Exception {

		return ipv4Service.insertIpv4(imConnect, macAddress);

	}

	/**
	 * 获取局域网配置信息
	 * 
	 * @param imConnect
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectIpv4ByImConnect", method = RequestMethod.POST)
	@ApiOperation(value = "获取局域网配置信息", notes = "获取局域网配置信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "imConnect", value = "im账号", dataType = "Integer", paramType = "query") })
	public ResponseResult selectIpv4ByimConnet(String imConnect) throws Exception {

		return ipv4Service.selectIpv4(imConnect);
	}

	/**
	 * 修改局域网配置信息
	 * 
	 * @param imConnect
	 * @param macAddress
	 * @param bssId
	 * @param ssId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateIpv4", method = RequestMethod.POST)
	@ApiOperation(value = "修改局域网配置信息", notes = "修改局域网配置信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "imConnect", value = "im账号", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "bssId", value = "路由器mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "ssId", value = "路由器名称", dataType = "String", paramType = "query") })
	public ResponseResult updateIpv4(String imConnect, String macAddress, String bssId, String ssId) throws Exception {
		return ipv4Service.updateIpv4(macAddress, imConnect, bssId, ssId);
	}

}
