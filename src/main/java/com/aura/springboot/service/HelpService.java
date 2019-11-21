package com.aura.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.mapper.HelpMapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class HelpService {

	private static Logger log = LoggerFactory.getLogger(HelpService.class);

	@Autowired
	private HelpMapper helpMapper;

	/**
	 * 根据语言和包名查询对应的使用指南
	 * 
	 * @param language   语言
	 * @param appPcgName 包名
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectUserManual(String language, String appType) throws Exception {
		log.info("------------查询使用指南开始--------------");
		log.info("select helpAddress\r\n" + "		from `help`\r\n" + "		where language = " + language
				+ " and appPcgName = " + appType + "");
		String url = helpMapper.selectUserManual(appType, language);
		/*
		 * String urlHelp="http://192.168.1.238:8090/rainbowchat_pro/introduction.jsp";
		 */
		if (url != null) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("url", url);
			log.info("------------查询使用指南成功--------------");
			return ResponseResult.success(data);
		}
		log.info("------------查询使用指南失败--------------");
		return ResponseResult.failure(ResultCode.SELECTUSERMANUAL_FAIL);
	}
}
