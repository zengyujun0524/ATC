package com.aura.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.Application;
import com.aura.springboot.mapper.ApplicationMapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class ApplicationService {

	private static Logger log = LoggerFactory.getLogger(ApplicationService.class);

	@Autowired
	private ApplicationMapper applicationMapper;

	/**
	 * 查看app版本状态
	 * 
	 * @param appPcgName 包名 
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectVersionByAppPcgName(String appPcgName) throws Exception {
		log.info("------------查询当前app版本状态------------");
		log.info("select appId,appName,appPcgName,`describe`,version,apkAddress,createTime,mandatoryUpString");
		log.info("from application where appPcgName = "+appPcgName+" order by version DESC limit 0,1 ");
		Application app = applicationMapper.selectVersionByAppPcgName(appPcgName);
		if(app == null)
			
			return ResponseResult.failure(ResultCode.APPPCGNAME_NOEXIST);
		return ResponseResult.success(app);
	}
}
