package com.aura.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aura.springboot.entity.Ipv4;
import com.aura.springboot.mapper.Ipv4Mapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class Ipv4Service {
	private static Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private Ipv4Mapper ipv4Mapper; // 用户mapper

	/**
	 * 插入网络配置信息
	 * 
	 * @param imConnect imId
	 * @param remoteUid 未激活0、待续费1
	 */

	public ResponseResult insertIpv4(String imConnect, String macAddress) throws Exception {
		// im账号不能为空(唯一标识)
		if (imConnect != null) {
			try {
				int index = ipv4Mapper.insertIpv4(macAddress, imConnect);
				if (index > 1) {
					return ResponseResult.success();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return ResponseResult.failure(ResultCode.IMCONNECT_NULL);

	}

	/**
	 * 获取局域网络配置配置信息接口
	 * 
	 * @param imConnect
	 * @return
	 * @throws Exception
	 */
	public ResponseResult selectIpv4(String imConnect) throws Exception {
		if (imConnect != null) {
			try {
				Ipv4 ipv4 = ipv4Mapper.selectIpv4(imConnect);
				if (ipv4 != null) {
					return ResponseResult.success();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return ResponseResult.failure(ResultCode.IMCONNECT_NULL);
	}

	public ResponseResult updateIpv4(String macAdderss, String imConnect, String bssId, String ssId) throws Exception {
		if (imConnect != null) {
			try {
				int index = ipv4Mapper.updateIpv4(macAdderss, imConnect, bssId, ssId);
				if (index > 0) {
					return ResponseResult.success();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return ResponseResult.failure(ResultCode.IMCONNECT_NULL);
	}

}
