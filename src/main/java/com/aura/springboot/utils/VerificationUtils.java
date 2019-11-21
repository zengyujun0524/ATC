package com.aura.springboot.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aura.springboot.utils.sms.SmsSingleSender;

/**
 * 验证码工具类
 * 
 * @author Administrator
 *
 */
public class VerificationUtils {

	private static Logger log = LoggerFactory.getLogger(VerificationUtils.class);

	/**
	 * 获取4位数验证码、并发送
	 * 
	 * @param countryCode 国家码
	 * @param phone       手机号码
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getRandom(String countryCode, String phone) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();

		// 生成4位数验证码
		Integer ran = (int) (Math.random() * 900000) + 100000;
		// 发送短信
		// MainApp.sendRegister(countryCode, phone,ran);
		SmsSingleSender ss = new SmsSingleSender(1400175049, "cff989587e52fc8b537eac56cdcd9131");
		if ("86".equals(countryCode)) {
			// 国内
			ss.send(0, countryCode, phone, ran + "为您的注册验证码，请于5分钟内填写。如非本人操作，请忽略本短信。", "", "");
			log.info("------国内短信发送成功！----国家码-》-" + countryCode + "--手机号-》----" + phone);
		} else {
			// 国际
			ss.send(0, countryCode, phone, ran
					+ " is the registration verification code. Please enter this code in 5 minutes. If you didn't initiate the request, please disregard this message.",
					"", "");
			log.info("------国际短信发送成功！----国家码-》-" + countryCode + "--手机号-》----" + phone);
		}
		data.put("random", ran);
		// 生成当前时间
		data.put("time", System.currentTimeMillis());
		return data;
	}

	// 判断验证码时效性、
	public static Map<String, String> SMSVerification(Long sendTime, Integer OldRandom, Integer newRandom)
			throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		if (OldRandom.equals(newRandom)) { // 传入的验证码是否和发送验证码一致 我tm 15 号 还 信用卡 要还
			long nowDate = (long) System.currentTimeMillis();
			double diff = (nowDate - sendTime) / 60000; // 计算差值是否大于6分钟，否则验证码失效
			log.info("-------发送时间的差值为" + diff + "分钟-------");
			if (diff < 6) {
				log.info("-------验证码正确-------");

				result.put("result", "success");
			} else {
				result.put("result", "TimeOut");
			}
		} else {
			result.put("result", "registerCodeFail");
		}
		return result;
	}

}
