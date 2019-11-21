package com.aura.springboot.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.aura.springboot.dto.DataFromClient;
import com.aura.springboot.dto.UserRegisterDTO;
import com.aura.springboot.entity.Constant;
import com.aura.springboot.entity.Equipment;
import com.aura.springboot.entity.History;
import com.aura.springboot.entity.Push;
import com.aura.springboot.entity.Security;
import com.aura.springboot.entity.User;
import com.aura.springboot.mapper.EquipmentMapper;
import com.aura.springboot.mapper.HistoryMapper;
import com.aura.springboot.mapper.PushMapper;
import com.aura.springboot.mapper.RelationMapper;
import com.aura.springboot.mapper.SecurityMapper;
import com.aura.springboot.mapper.UserMapper;
import com.aura.springboot.utils.AddressUtils;
import com.aura.springboot.utils.ApnsPushUtils;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.RestHashMap;
import com.aura.springboot.utils.ResultCode;
import com.aura.springboot.utils.UrlSend;
import com.google.gson.Gson;

@Service
public class UserService {

	private static Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserMapper userMapper; // 用户mapper
	@Autowired
	private EquipmentMapper equipmentMapper;
	@Autowired
	private HistoryMapper historyMapper;
	@Autowired
	private PushMapper pushMapper;
	@Autowired
	private RelationMapper relationMapper;
	// 切换服务器
	@Autowired
	private Constant constant;

	@Autowired
	private SecurityMapper securityManager;

	/**
	 * 手机用户注册
	 * 
	 * 
	 * @param user用户信息
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult registerPhone(User user, String snNum, String bssId, String ssId, String problem,
			String answer) throws Exception {
		String phone = user.getPhone(); // 手机号码
		Integer index = 0;
		user.setUsername(phone);
		user.setCreateTime(user.getCreateTime());
		String newUid = ""; // 记录im注册返回id
		// 查看是否存在该数据 我要他等一下
		logSelectUserByPhone(phone, user.getCountryCode());
		User phoneUser = userMapper.selectUserByPhoneAndCountryCode(phone, user.getCountryCode());
		if (phoneUser != null)
			return ResponseResult.failure(ResultCode.PHONE_EXIST);
		log.info("-------用户注册-------");
		;
		log.info("INSERT INTO\r\n "
				+ "		`user`(username,password,phone,accType,countryCode,logStatus,createTime,appTypeId,callStatus,phoneModel,sysType)\r\n"
				+ "		VALUES(" + user.getUsername() + "," + user.getPassword() + "," + user.getPhone() + ",0,"
				+ user.getCountryCode() + "," + user.getLogStatus() + "," + user.getCreateTime() + ","
				+ user.getAppTypeId() + ",0," + user.getPhoneModel() + "," + user.getSysType() + ");");
		try {
			index = userMapper.registerPhone(user);
			if (index > 0) {
				// 1：项目类型为FamCall2.0
				if (1 == user.getAppTypeId()) {
					log.info("new_uid==================================>" + user);
					// 注册im
					String result = registerIM(user);
					JSONObject object = JSONObject.parseObject(result);
					String successResult = object.getString("success");

					String returnValue = object.getString("returnValue"); // 获取{"new_uid":"400130 "}
					JSONObject resultJson = JSONObject.parseObject(returnValue);
					newUid = resultJson.getString("new_uid");
					log.info("new_uid==================================>" + newUid);
					// 0 代表重复注册
					if ("0".equals(newUid)) {
						return ResponseResult.failure(ResultCode.ACCOUNT_EXIST);
					}
					if ("false".equals(successResult)) {
						// 如果im注册失败，则删除用户表中的当前信息
						userMapper.deleteUserByPhone(phone);
						return ResponseResult.failure(ResultCode.IM_REGISTER_FAIL);
					}

					logSelectUserByPhone(phone, user.getCountryCode());

					Integer userId = userMapper.selectUserByPhoneAndCountryCode(phone, user.getCountryCode())
							.getUserId();
					log.info("new_uid==================================>" + newUid);
					userMapper.updateImConnect(userId, newUid);
				}
				// 设置token
				Date date = new Date();
				// 设置要获取到什么样的时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 获取String类型的时间
				String createdate = sdf.format(date);
				String token = user.getPassword() + "#" + createdate;
				// 加密密码
				final Base64.Encoder encoder = Base64.getEncoder();
				final byte[] textByte = token.getBytes("UTF-8");
				// 编码
				final String encodedText = encoder.encodeToString(textByte);
				user.setToken(encodedText); // 设置密文返回
				logSelectUserByPhone(phone, user.getCountryCode());
				User uu = userMapper.selectUserByPhoneAndCountryCode(phone, user.getCountryCode());
				uu.setToken(encodedText);
				// 更新局域网配置信息
				if (bssId != null) {
					/* int index5 = userMapper.updateUserIpv4(newUid, bssId, ssId, null); */
				}
				// 注册成功 录入手机用户登录历史记录44
				History hi = new History();
				hi.setUserId(uu.getUserId());
				hi.setPhoneModel(uu.getPhoneModel());
				hi.setSnNum(snNum);
				hi.setDeviceLock(0);
				hi.setLastLogonTime(System.currentTimeMillis() + "");
				historyMapper.insertHistory(hi);
				log.info("uu.getUserId()" + uu.getUserId() + "newUid>>" + newUid);
				pushMapper.insertPush(uu.getUserId(), Integer.parseInt(newUid), uu.getAccType());
				Security security = new Security(uu.getUserId(), problem, answer);
				securityManager.insertSecuityQuestion(security);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("user", uu);
				return ResponseResult.success(data);
			} else {
				return ResponseResult.failure(ResultCode.REGISTER_FAIL);
			}
		} catch (Exception e) {
			log.info(e.getMessage());
			return ResponseResult.failure(ResultCode.REGISTER_FAIL);
		}
	}

	/**
	 * 平板激活状态推送
	 * 
	 * h * @param imConnect imId
	 * 
	 * @param remoteUid 未激活0、待续费1
	 */
	public void pushAct(String imConnect, Integer remoteUid) {
		String dataContent = "{\"cy\":0,\"f\":\"0\",\"m\":\"{\\\"mParam\\\":\\\"" + imConnect + "\\\",\\\"mType\\\":"
				+ 2054 + ",\\\"myUid\\\":\\\"0\\\",\\\"remoteUid\\\":\\\"" + remoteUid + "\\\"}\",\"t\":\"" + remoteUid
				+ "\",\"ty\":10008}";
		// 连接im 进行消息推送 post请求
		String url = constant.getServer().replaceAll(" ", "") + "/rainbowchat_pro/SendMSG ";
		UrlSend.httpURLConnectionPOST(url, imConnect, dataContent, 3 + "");
		log.info("---用户：" + imConnect + "-----------给：" + imConnect + "-----推送激活状态2054成功-------");
	}

	/**
	 * 修改在离线状态
	 * 
	 * @param userId
	 * @param logStatus
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateLogStatus(String imConnect, Integer logStatus) throws Exception {
		log.info("-------------imConnect----------------> 用户上下线" + imConnect);
		User u = userMapper.selectUserByIm(imConnect);
		log.info("------------修改登录状态------------");
		Integer index = 0;
		if (u != null) {
			log.info("UPDATE `user`\r\n" + "		SET logStatus = " + logStatus + "\r\n" + "		WHERE\r\n"
					+ "		userId\r\n" + "		=\r\n" + u.getUserId());
			if (u.getAccType() == 0) {
//				pushMapper.updateNewsByIm(u.getUserId(), 0);
				log.info("---消息推送清零 ----");
			}
			// 更新登录状态
			if (u.getLogStatus() != 2) {
				index = userMapper.updateLogStatusByUserId(u.getUserId(), logStatus);
				log.info("-----u.getLogStatus()-----" + u.getLogStatus());
				if (logStatus == 0) {
					log.info("----------用户离线后通话状态为0------------------");
					log.info("UPDATE `user`\r\n" + "		SET callStatus = " + "		WHERE\r\n" + "		userId\r\n"
							+ "		=\r\n" + u.getUserId());
					int index2 = userMapper.updateCallStatus(u.getUserId(), 0);
				}
			}
			// 如果改变平板的状态，则无绳子机的状态也发生改变
			if (u.getAccType() == 1) {
				Equipment ee = equipmentMapper.selectEquipmentByMacAddress(u.getMacAddress());
				// 判断平板设备是否过期
				Long nowTime = System.currentTimeMillis(); // 当前时间
				Long endTime = Long.parseLong(ee.getActEndTime()); // 设备到期时间
				if (ee != null) {
					if ("0".equals(ee.getActEndTime())) { // 如果到期时间为0，则是未激活状态
						pushAct(imConnect, 1);
					} else if (nowTime > endTime) { // 如果当前时间大于设备到期时间，则是待续费状态

						pushAct(imConnect, 0);
					} //
				}
				log.info("-------当前平板" + u.getMacAddress() + "的激活状态为--------" + ee.getActStatus());
				if (ee.getActStatus() == 0 && logStatus == 1) {
					// 如果设备未激活、平板在线状态即为3
					Integer nr = userMapper.updateLogsByMac(u.getMacAddress(), 1);
					if (nr > 0) {
						log.info("-----------平板、无绳子机的状态更新成功-------》- " + 3);
					}
				} else {
					Integer nr = userMapper.updateLogsByMac(u.getMacAddress(), logStatus);
					if (nr > 0) {
						log.info("-----------平板、无绳子机的状态更新成功-----------------------");
					}
				}
				// 推送平板底座状态，如果平板离线，则状态为4（平板app未运行）
				if (logStatus == 0) {
					if (ee != null) {
						Integer eq = equipmentMapper.updateTableStauts(ee.getProId(), 4, null);
						if (eq > 0) {
							log.info("-----------平板底座的状态更新成功----------------------   ");
							pushInfo(imConnect, 3, 1087);
							log.info("-----------平板底座的状态推送成功--------------------- ");
						}
					}
				}
			}
			if (index > 0) {
				log.info("-----------用户登录状态状态更新成功----------------------   ");
				// 推送消息
				if (logStatus == 1) {
					log.info("-----用户--》-" + imConnect + "----，正在发送在线推送2050---");
					pushInfo(imConnect, 3, 2050);
				} else if (logStatus == 0) {
					log.info("-----用户--》-" + imConnect + "----，正在发送离线推送2051---");
					pushInfo(imConnect, 3, 2051);
				} else {
					pushInfo(imConnect, 3, 1088);
				}
				return ResponseResult.success();
			}
		}
		return ResponseResult.failure(ResultCode.UPDATEINFO_FAIL);

	}

	/**
	 * 修改登录状态 激活
	 * 
	 * @param userId
	 * @param logStatus
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateLogStatus(Integer userId, Integer logStatus) throws Exception {
		log.info("-------用户id：-->" + userId + "---登录状态为--->" + logStatus);
		Integer index = userMapper.updateLogStatusByUserId(userId, logStatus);
		if (index > 0) {
			log.info("-----登录状态修改成功--进行消息推送--");
			User uu = userMapper.selectUserById(userId);
			if (uu.getAccType() == 0) {
//				pushMapper.updateNewsByIm(uu.getUserId(), 0);
				log.info("---消息推送清零 ----");
			}
			if (uu != null) {
				if (logStatus == 1) {
					pushInfo(uu.getImConnect(), 3, 2050);
					log.info("---消息推送成功---" + 2052);
				} else {
					pushInfo(uu.getImConnect(), 3, 2051);
					log.info("---消息推送成功---" + 2051);
				}

			}
		}
		return ResponseResult.success();
	}

	/**
	 * im消息推送
	 * 
	 * @param imConnect 用户im账号
	 * @param typeu     通知协议
	 * @param mType     内呼列表推送是1088、平板状态更新是1087、用户上线2050、用户离线2051、用户内线在线2052
	 * @throws Exception
	 */
	public void pushInfo(String imConnect, Integer typeu, Integer mType) throws Exception {
		if (imConnect != null) {
			// 需要发送的好友的id
			String[] friendIm_id = null;
			// 先查出当前的状
			User uu = userMapper.selectUserByIm(imConnect);
			if (uu != null) {
				if (1 == uu.getAccType()) {
					friendIm_id = userMapper.selectImConnectByMac(imConnect); // 查询平板相关所有绑定设备imConnect
				} else if (0 == uu.getAccType()) {
					friendIm_id = userMapper.selectImConnect(imConnect); // 查询手机相关所有绑定设备imConnect
				}
			}
			String url = constant.getServer().replaceAll(" ", "") + "/rainbowchat_pro/SendMSG";

			for (int i = 0; i < friendIm_id.length; i++) {
				// 如果是自己本身，则不推送
				/*
				 * if (friendIm_id[i].equals(imConnect)) continue;
				 *
				 */
				log.info("---用户：" + imConnect + "-----------正在给：" + friendIm_id[i] + "-----发推送-------");
				String dataContent = "{\"cy\":0,\"f\":\"0\",\"m\":\"{\\\"mParam\\\":\\\"" + imConnect
						+ "\\\",\\\"mType\\\":" + mType + ",\\\"myUid\\\":\\\"0\\\",\\\"remoteUid\\\":\\\""
						+ friendIm_id[i] + "\\\"}\",\"t\":\"" + friendIm_id[i] + "\",\"ty\":10008}";
				// 连接im 进行消息推送 post请求
				UrlSend.httpURLConnectionPOST(url, friendIm_id[i], dataContent, typeu + "");
				log.info("---用户：" + imConnect + "-----------给：" + friendIm_id[i] + "-----推送成功-------");
			}
			// 给自己做推送
			String dataContent = "{\"cy\":0,\"f\":\"0\",\"m\":\"{\\\"mParam\\\":\\\"" + imConnect
					+ "\\\",\\\"mType\\\":" + mType + ",\\\"myUid\\\":\\\"0\\\",\\\"remoteUid\\\":\\\"" + imConnect
					+ "\\\"}\",\"t\":\"" + imConnect + "\",\"ty\":10008}";
			UrlSend.httpURLConnectionPOST(url, imConnect, dataContent, typeu + "");
		}

	}

	/**
	 * 更新用户离线推送token
	 * 
	 * @param userId
	 * @param pushToken
	 * @return 影响不是很大
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult updatePushToken(Integer userId, String pushToken) throws Exception {
		log.info("--------------更新用户离线token------------");
		String result[] = pushToken.split(" ");
		int index1 = result.length;
		if (index1 == 1) {
			log.info("--------------更新用户离线token---userId>>>>" + userId + "result[0]>>>>" + result[0]);
			Integer index = pushMapper.updatePushToken(userId, result[0], 0);
			return ResponseResult.success();
		} else if (index1 == 2) {
			log.info("--------------更新用户离线token---userId" + userId + "result[0]>>>>" + result[0]);
			Integer index = pushMapper.updatePushToken(userId, result[0], 0);
			pushMapper.updatePushToken(userId, result[1], 1);
		}
		return ResponseResult.failure(ResultCode.UPDATEPUSHTOKEN_FAIL);
		/*
		 * if (index > 0) { log.info("-----------用户：-" + userId +
		 * "----离线token更新成功----");
		 * 
		 * } else { log.info("-----------用户：-" + userId + "----离线token更新失败----"); return
		 * ResponseResult.failure(ResultCode.UPDATEPUSHTOKEN_FAIL); }
		 */

	}

	/**
	 * 离线消息推送
	 * 
	 * @param imConnect 用户im账号
	 * @param mType
	 * @throws Exception
	 */

	public void offlinePush(String imConnect, int index, String phone) throws Exception {
		String callContent = ""; // 离线推送内容 773012267329899 Yu_antou
		UrlSend us = new UrlSend();
		if (1 == index) {
			callContent = "New incoming call";
		} else if (2 == index) {
//	    callContent = "未接来电：" + phone;
			callContent = "Missed Call：" + phone;
		} else if (3 == index) {
//			callContent = "内呼来电 ";
			callContent = "Incoming internal call";
		} else if (4 == index) {
//			callContent = "转接来电";  
			callContent = "Incoming forwarded call";
		} else if (6 == index) {
//			callContent = "内呼未接";  
			callContent = "Missed call from " + phone;
		} else if (7 == index) {
			callContent = "Missed call from " + phone;
//			callContent = "转接未接";  
//			callContent = "Missed call from Mark ";
		} else if (8 == index) {
//          免打扰来电    
			callContent = "New incoming call";

		}

		log.info("-离线推送-------imConnect--------->" + imConnect + ",---index---->" + index + ",-------phone------>"
				+ phone);
		if (imConnect != null) {
			try {
				Push uu = pushMapper.selectUserByIm(imConnect); // 查找对应的用户，需要用到pushToken
				if (index == 8) {
					uu.setControl(1);
				}
				if (uu != null) {
					int news;
					String token = uu.getPushToken();
					String url = null;

					log.info("用户" + imConnect + "的token----->" + token + ",sysType == >" + uu.getSysType() + "-----记录值"
							+ uu.getNews());
					if (index == 2 || index == 7) {
						news = uu.getNews() + 1;
						pushMapper.updateNewsByIm(uu.getUserId(), news);
					} else {
						news = uu.getNews();
						pushMapper.updateNewsByIm(uu.getUserId(), news);
					}

					log.info("记录数量的值" + news);
					log.info("UPDATE `user`\r\n" + "		SET news = " + news + "\r\n" + "		WHERE\r\n"
							+ "		imConnect\r\n" + "		=\r\n" + imConnect);

					// 根据操作系统选择对应的推送方式
					if (uu.getSysType() == 0) { // 安卓推送
						// 安卓推送
						if (uu.getAccType() == 0) { // 平板推送

							if (pushFCMNotification(token, 0, callContent, uu.getControl(), index)) {
								log.info("------安卓(手机)离线推送成功------");
							}

						} else { // 手机（安卓）推送
							log.info("------平板-----");
							if (pushFCMNotification(token, 1, callContent, uu.getControl(), index)) {
								log.info("------安卓(平板)离线推送成功------");
							}
						}

					} else {
						// 苹果推送
						// String token = 怎么还欠
						// "369554c0031f7a85154a661808d3fa56ef3957e2a3c3eda13c762db3de07b91a";
						// 判断铃声
						if (index == 1) {
							token = uu.getPushKit();
							url = "http://150.109.109.10:8080/spring-boot-mybatis-mutil-database-0.0.1-SNAPSHOT/user/test?token="
									+ token + "&callInNum=" + callContent.replace(" ", "") + "&news=" + news
									+ "&control=" + uu.getControl();
							us.httpURLConectionGET(url);
							log.info("-----iosNewOfflinePush推送成功-----pp.getPushKit()=" + uu.getPushKit() + "url>>>"
									+ url);
							/*
							 * ApnsPushUtils.getSingleInstance().pushMsgiOS(token, callContent, 0, news,0);
							 */
						} else if (index == 3) {
							token = uu.getPushKit();
							url = "http://150.109.109.10:8080/spring-boot-mybatis-mutil-database-0.0.1-SNAPSHOT/user/test?token="
									+ token + "&callInNum=" + callContent.replace(" ", "") + "&news=" + news
									+ "&control=" + uu.getControl();
							us.httpURLConectionGET(url);
							log.info("-----iosNewOfflinePush推送成功-----pp.getPushKit()=" + uu.getPushKit() + "url>>>"
									+ url);
						} else if (index == 4) {
							token = uu.getPushKit();
							url = "http://150.109.109.10:8080/spring-boot-mybatis-mutil-database-0.0.1-SNAPSHOT/user/test?token="
									+ token + "&callInNum=" + callContent.replace(" ", "") + "&news=" + news
									+ "&control=" + uu.getControl();
							us.httpURLConectionGET(url);

// 			         	ApnsPushUtils.getSingleInstance().pushMsgiOS(token, callContent, 0, news);    
						} else {
// 						ApnsPushUtils.getSingleInstance().pushMsgiOS(token, callContent, 3);  
							ApnsPushUtils.getSingleInstance().pushMsgiOS(token, callContent, 0, news, uu.getUserId());
						}
						log.info("--------- 原生推送成功--------------");
						log.info("-----OfflinePush推送成功-----pp.getPushKit()=" + uu.getPushKit() + "url>>>" + url);

					}

// 				UrlSend.offPush(url,token);   

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void iosOfflinePush(String imConnect, String content) throws Exception {
		if (imConnect != null) {
			Push pp = pushMapper.selectUserByIm(imConnect); // 查找对应的用户，需要用到pushToken 快递的
			int news = pp.getNews() + 1;
			if (pp != null) {

				String token = pp.getPushKit();
				// http://150.109.109.10:8080/spring-boot-mybatis-mutil-database-0.0.1-SNAPSHOT
				String url = "http://150.109.109.10:8080/spring-boot-mybatis-mutil-database-0.0.1-SNAPSHOT/user/test?token="
						+ token + "&callInNum=" + content + "&news=" + news + "";
				UrlSend uu = new UrlSend();
				uu.httpURLConectionGET(url);
				log.info("-----iosNewOfflinePush推送成功-----pp.getPushKit()=" + pp.getPushKit() + "url>>>" + url);
			}
		}
	}

	/**
	 * 安卓端推送，0：手机、1：平板
	 * 
	 * 
	 * @param deviceToken
	 * @param index
	 * @return
	 */
	public static boolean pushFCMNotification(String deviceToken, Integer index, String callContent, int control,
			int index2) {
		final boolean[] flag = { true };
		if (deviceToken == null) {
			flag[0] = false;
			return flag[0];
		}
		String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
		String AUTH_KEY_FCM = "";
		if (1 == index) {
			AUTH_KEY_FCM = "AIzaSyAvhiYl0boYVMs3PP194kuBikylJiftRTg"; /// 平板
			log.info("进来了---平板 deviceToken》》" + deviceToken);
		} else {
			// AIzaSyAvqVcB3MAZs1t8oxVVkIR9U4myqnCDdds 18975586815
			// 老版本AIzaSyACgZGwUc74lmLADU7isU_HI6aPAAeb9fc
			AUTH_KEY_FCM = "AIzaSyCbnqIjtGRk7kx06HKk8DY2Ordzz0kAb7g"; /// android 手机
		}
		try {
			URL url = new URL(API_URL_FCM);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
			conn.setRequestProperty("Content-Type", "application/json");// 不设置默认发送文本格式。设置就是json
			JSONObject json = new JSONObject();
			json.put("to", deviceToken);// 推送到哪台客户端机器，方法一推一个token,
			// 方法二，批量推送 ，最多1000个token ，此处的tokens是一个token
			// JSONArray数组json.put("registration_ids", tokens);
			JSONObject info = new JSONObject();
			log.info("测试control>>>>>>" + control);
			info.put("title", callContent);
			info.put("body", "New call notification, please check.");
			if (control == 1) {
				log.info("测试silent");
				info.put("android_channel_id", "donotdisturb");
				info.put("sound", "silent");
			} else {
				if (index2 == 1 || index2 == 3 || index2 == 4) {
					log.info("测试opening");
					info.put("android_channel_id", "incoming");
					info.put("sound", "opening");
				} else {
					log.info("测试default");
					info.put("android_channel_id", "default");
					info.put("sound", "default");
				}

			}
			//
//			info.put("sound", "iphone_alamr"); // info.put("icon", "myicon");
			json.put("notification", info);// json 还可以put其他你想要的参数
			json.put("priority", "high");// json 还可以put其他你想要的参数
			json.put("tag", "DownloadNotificationService");

			/*
			 * 对我们这些平民 info.put("Nick", "Mario"); info.put("body", "great match!");
			 * info.put("Room", "PortugalVSDenmark"); json.put("data", info);
			 */
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
			}
			wr.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			flag[0] = false;
			log.info("------安卓离线推送失败------");
		}
		return flag[0];
	}

	/**
	 * 获取平板状态
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectTableStatusByMac(String macAddress) throws Exception {
		log.info("--------------获取平板状态------------");
		Equipment equi = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (equi != null) {
			Integer eqs = equi.getTableStatus();
			log.info("----------当前平板状态为------------》" + eqs);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("result", eqs);
			data.put("occupier", equi.getOccupier());
			return ResponseResult.success(data);
		}
		return ResponseResult.failure(ResultCode.EQUISTATUS_NULL);
	}

	/**
	 * 插入无绳子机
	 * 
	 * @param phone       子机标识
	 * @param countryCode 国家码
	 * @param password
	 * @param appTypeId
	 * @return
	 * @throws Exception
	 */

	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult insertNoRopeMachine(String macAddress, String username, String phone) throws Exception {
		String createTime = System.currentTimeMillis() + "";
		Integer index2 = 0;
		log.info("----------查询平板关联的无绳子机是否已经绑定---------------");
		log.info("----------insert into\r\n"
				+ "		user(macAddress,username,phone,accType,createTime,appTypeId,logStatus,callStatus)\r\n"
				+ "		values(" + macAddress + "," + username + "," + phone + ",2," + createTime
				+ ",3,0,0);---------------");
		log.info("----------字符串拆分--------------- 将phone传成list");
		// Integer index3= userMapper.emptySubmachineList(macAddress); 一次性清楚子机列表
		User carry = userMapper.selectUserByMacAddress(macAddress);
		if (carry == null) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		String[] sourceStrArray = phone.split(",");
		int index = userMapper.updateUserDisplay(macAddress, 2, "0", null);
		if (index > 0) {
			for (int i = 0; i < sourceStrArray.length; i++) {
				userMapper.updateUserDisplay(macAddress, 2, "1", sourceStrArray[i].replace(" ", ""));
			}

			// 推送消息
			pushInfo(carry.getImConnect(), 3, 1088);
		}

		/*
		 * for (int i = 0; i < sourceStrArray.length; i++) { // 查询该无绳子机是否已经存在 Integer
		 * index = userMapper.selectNoRopeMachineCount(macAddress, sourceStrArray[i]);
		 * if (index > 0) { return
		 * ResponseResult.failure(ResultCode.CORDLESSMACHINEISBOUND); } else {
		 * log.info("index3————————————————"+index3);
		 * log.info("sourceStrArray————————————————" + sourceStrArray.toString());
		 * log.info("sourceStrArray[i]————————————————" + sourceStrArray[i]); index2 =
		 * userMapper.insertNoRopeMachine(macAddress, username + sourceStrArray[i],
		 * sourceStrArray[i], createTime, carry.getLogStatus()); // 先获取原始密码 } Integer
		 * index3 = userMapper.selectNoRopeMachineCount(macAddress, sourceStrArray[i]);
		 * if (index3 > 1) { return
		 * ResponseResult.failure(ResultCode.CORDLESSMACHINEISBOUND); }
		 * 
		 * }
		 */

		return ResponseResult.success();
	}

	/*
	 * public ResponseResult insertNoRopeMachine(String macAddress, String username,
	 * String phone) throws Exception { String createTime =
	 * System.currentTimeMillis() + "";
	 * log.info("----------查询平板关联的无绳子机是否已经绑定---------------");
	 * log.info("----------insert into\r\n" +
	 * "		user(macAddress,username,phone,accType,createTime,appTypeId,logStatus,callStatus)\r\n"
	 * + "		values(" + macAddress + "," + username + "," + phone + ",2," +
	 * createTime + ",1,0,0);---------------"); // 查询该无绳子机是否已经存在 Integer index =
	 * userMapper.selectNoRopeMachineCount(macAddress, phone); if (index > 0) {
	 * return ResponseResult.failure(ResultCode.CORDLESSMACHINEISBOUND); } else {
	 * User carry=userMapper.selectUserByMacAddress(macAddress); Integer index2 =
	 * userMapper.insertNoRopeMachine(macAddress, username, phone,
	 * createTime,carry.getLogStatus()); // 先获取原始密码 if (index2 > 0) { User uu =
	 * userMapper.selectUserByMacAddress(macAddress); if (uu != null) { // 推送消息
	 * pushInfo(uu.getImConnect(), 3, 1088); } return ResponseResult.success(); } }
	 * return ResponseResult.failure(ResultCode.REGISTER_FAIL); }
	 */

	/**
	 * 根据userId查询对应的手机编号
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectRelationProIdByUserId(Integer userId) throws Exception {
		log.info("----------根据userId查询对应的编号 ---------------");
		Integer index = 0;
		User user = userMapper.selectUserById(userId);
		if (user != null) {
			if (user.getAccType() == 0) { // 类型为手机
				index = userMapper.selectRelationProIdByUserId(userId);
			} else if (user.getAccType() == 1) { // 类型为平板
				index = 0;
			} else if (user.getAccType() == 2) { // 类型为无绳子机
				index = Integer.parseInt((userMapper.selectUserById(userId)).getPhone());
			}
		}
		log.info("----------userId查询对应的编号是 ---------------》" + index);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", index);
		return ResponseResult.success(data);
	}

	/**
	 * 清空无绳子机
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */

	@Transactional
	public ResponseResult emptySubmachineList(String macAddress) throws Exception {

		try {
			int index = userMapper.updateUserDisplay(macAddress, 2, "0", null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		/*
		 * log.info("----------清空无绳子机---------------");
		 * log.info("----------delete from `user`\r\n" + "		where macAddress = " +
		 * macAddress + " and accType = 2---------------");
		 */
		/*
		 * Integer index = userMapper.emptySubmachineList(macAddress);
		 * 
		 * List<User> list = userMapper.selectProRelationNoRopeMachine(macAddress); for
		 * (User user : list) { index = userMapper.deleteByUserId(user.getUserId()); }
		 */
		/*
		 * if (index > 0) { log.info("----------清空无绳子机成功---------------"); User uu =
		 * userMapper.selectUserByMacAddress(macAddress); if (uu == null) { // 推送消息
		 * pushInfo(uu.getImConnect(), 3, 1088);
		 * log.info("----------清空成功---------------"); return ResponseResult.success(); }
		 * else { userMapper.emptySubmachineList(macAddress); } }
		 */
		return ResponseResult.success();
	}

	/**
	 * 更新无绳子机名称
	 * 
	 * @param macAddress
	 * @param username
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateNoRopeMachineName(String macAddress, String username, String phone) throws Exception {
		log.info("----------更新无绳子机名称---------------");
		log.info("----------update user set username = " + username + " where macAddress = " + macAddress
				+ " and phone = " + phone + "---------------");
		Integer index = userMapper.updateNoRopeMachineName(macAddress, username, phone);
		if (index > 0) {
			return ResponseResult.success();
		}
		return ResponseResult.failure(ResultCode.REGISTER_FAIL);
	}

	/**
	 * 修改通话状态
	 * 
	 * @param userId
	 * @param callStatus
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateCallStatus(Integer userId, Integer callStatus) throws Exception {
		log.info("------------修改通话状态------------");
		log.info("UPDATE `user`\r\n" + "		SET callStatus = " + callStatus + "\r\n" + "		WHERE\r\n"
				+ "		userId\r\n" + "		=\r\n" + "		" + userId + "");
		Integer index = userMapper.updateCallStatus(userId, callStatus);
		if (index > 0) {
			User uu = userMapper.selectUserById(userId);
			if (uu != null) {
				/*
				 * 难受 想哭 constant.setIndex(constant.getIndex() + 1); if (constant.getIndex() % 2
				 * == 0) { log.info("--------constant.getIndex()-------" + constant.getIndex());
				 * // 推送消息 pushInfo(uu.getImConnect(), 3, 1088); }
				 */
				pushInfo(uu.getImConnect(), 3, 1088);
				return ResponseResult.success();
			}
		}
		return ResponseResult.failure(ResultCode.UPDATEINFO_FAIL);
	}

	/**
	 * 平板用户注册
	 * 
	 * @param macAddress
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult userMacAddressRegister(String macAddress, Integer appTypeId, String createTime,
			String pushToken, Integer sysType, String proNum) {
		Integer index = 0;
		try {
			// 查看是否存在该数据
			logSelectUserByMacAddress(macAddress);
			User proUser = userMapper.selectUserByMacAddress(macAddress);
			String newUid = ""; // 记录im注册返回id
			if (proUser != null) {
				log.info("------------mac地址已存在-----------");
				return ResponseResult.failure(ResultCode.MACADDRESS_EXIST);
			}
			// 注册im
			log.info("注册修改值————————————————————");

			User user = new User();
			log.info(proNum + "proNum------------开始注册-----------");
			user.setPhone("0");
			user.setPassword("123456");
			user.setLogStatus(0); // 默认为离线状态
			user.setCallStatus(0);
			user.setAccType(1);
			user.setCreateTime(createTime);
			user.setMacAddress(macAddress);
			user.setAppTypeId(appTypeId);
			user.setPushToken(pushToken);
			user.setSysType(sysType);
			user.setPhoneModel(proNum);
			// 平板默认头像
			log.info(constant.getServer() + "/images/structsBg.png》》》》》》》》》》");
			if ("Smart_Landline_AC5".equals(proNum)) {
				user.setPhoto("http://47.75.47.133:8090/images/AC5.png");
				user.setUsername(proNum);
			} else {
				user.setPhoto("http://47.75.47.133:8090/images/structsBg.png");
				user.setUsername("Air Line AC7 Tablet");
			}
			log.info(">>>>>>>>>>>>>" + pushToken);
			log.info("insert into\r\n"
					+ "			`user`(username,password,sex,macAddress,accType,logStatus,createTime,appTypeId,callStatus,photo,phoneModel)\r\n"
					+ "			values(" + user.getUsername() + "," + user.getPassword() + ",1" + user.getMacAddress()
					+ "," + user.getLogStatus() + "," + user.getCreateTime() + "," + user.getAccType() + ","
					+ user.getCallStatus() + "," + user.getPhoto() + "," + user.getPhoneModel() + ",");
			index = userMapper.registerPro(user);
			log.info("------------开始注册>>>-----------" + index);
			if (index > 0) {
				// 1：项目类型为FamCall2.0
				if (1 == appTypeId) {
					String result = registerIM(user);
					JSONObject object = JSONObject.parseObject(result);
					String successResult = object.getString("success"); // 获取{"success" : "true"}
					String returnValue = object.getString("returnValue"); // 获取{"new_uid":"400130"}
					JSONObject resultJson = JSONObject.parseObject(returnValue);
					newUid = resultJson.getString("new_uid");
					log.info("new_uid==================================>" + newUid);
					if ("false".equals(successResult)) {
						// 如果im注册失败，则删除用户表中的当前信息
						log.info("-------------------根据mac地址删除对应的用户 ------------------");
						log.info("delete from user where macAddress = " + macAddress);
						userMapper.deleteUserByMacAddress(macAddress);
						return ResponseResult.failure(ResultCode.IM_REGISTER_FAIL);
					}

					logSelectUserByMacAddress(macAddress);

					Integer userId = userMapper.selectUserByMacAddress(macAddress).getUserId();

					userMapper.updateImConnect(userId, newUid);

				}

				// 设置token
				Date date = new Date();
				// 设置要获取到什么样的时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 获取String类型的时间
				String createdate = sdf.format(date);
				String token = user.getPassword() + "#" + createdate;
				// 加密密码
				final Base64.Encoder encoder = Base64.getEncoder();
				final byte[] textByte = token.getBytes("UTF-8");
				// 编码
				final String encodedText = encoder.encodeToString(textByte);
				user.setToken(encodedText); // 设置密文返回
				logSelectUserByMacAddress(macAddress);

				User uu = userMapper.selectUserByMacAddress(macAddress);
				// 注册平板push信息
				pushMapper.insertPush(uu.getUserId(), Integer.parseInt(newUid), uu.getAccType());
				uu.setToken(encodedText);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("user", uu);
				// 预留五个子机位
				for (int i = 1; i < 6; i++) {
					userMapper.insertNoRopeMachine(macAddress, "Cordless Handset " + i, i + " ", createTime, 0);
				}

				return ResponseResult.success(data);
			} else {
				return ResponseResult.failure(ResultCode.REGISTER_FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("-----------注册失败-----------");
			return ResponseResult.failure(ResultCode.REGISTER_FAIL);
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param userId      用户id
	 * @param oldPassword 原始密码
	 * @param newPassword 新密码 那没有
	 * @param appTypeId   项目类型（1：FamCall2.0）
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateUserPwd(Integer userId, String oldPassword, String newPassword, Integer appTypeId)
			throws Exception {
		Integer index = 0;
		logSelectUserById(userId);
		User user = userMapper.selectUserById(userId);
		log.info("-------修改密码-------" + user);
		String uid = user.getImConnect();
		log.info("-------修改密码-------" + uid);
		if (!user.getPassword().equals(oldPassword))
			return ResponseResult.failure(ResultCode.OLDPASSWORD_ERR);
		log.info("-------修改密码-------");
		try {
			log.info("UPDATE `user`\r\n" + "		SET password = " + newPassword + "\r\n" + "		WHERE userId =\r\n"
					+ "		" + userId + "");
			index = userMapper.updatePwd(userId, newPassword);
			if (index > 0) {
				if (appTypeId == 1) {
					// 修改im关联信息密码
					String result = updateIMPwd(uid, oldPassword, newPassword);
					JSONObject object = JSONObject.parseObject(result);
					String returnValue = object.getString("returnValue"); // 获取{"success" : "true"}
					if (!"1".equals(returnValue)) {
						return ResponseResult.failure(ResultCode.IM_UPDATEPWD_FAIL, returnValue);
					}
				}
				return ResponseResult.success();
			} else {
				return ResponseResult.failure(ResultCode.UPDATEPASSWORD_FAIL);
			}
		} catch (Exception e) {
			index = userMapper.updatePwd(userId, oldPassword);
			return ResponseResult.failure(ResultCode.UPDATEPASSWORD_FAIL);
		}
	}

	/**
	 * 重设密码
	 * 
	 * @param userId
	 * @param password
	 * @param appTypeId
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult rebuildPwd(String phone, String countryCode, String password, Integer appTypeId)
			throws Exception {
		logSelectUserByPhone(phone, countryCode);
		User user = userMapper.selectUserByPhoneAndCountryCode(phone, countryCode); // 先获取原始密码
		if (user != null) {
			String oldPassword = user.getPassword();
			log.info("-------------------重置密码	----------------");
			log.info("UPDATE `user`\r\n" + "		SET password = " + password + "\r\n" + "		WHERE userId =\r\n"
					+ user.getUserId());
			Integer index = userMapper.updatePwd(user.getUserId(), password);
			if (index > 0) {
				if (appTypeId == 1) {
					// 修改im关联信息密码 不一样的
					String result = updateIMPwd(user.getImConnect(), oldPassword, password);
					JSONObject object = JSONObject.parseObject(result);
					String returnValue = object.getString("returnValue"); // 获取{"success" : "true"}
					if ("0".equals(returnValue)) { // 0 表示im更新失败
						return ResponseResult.failure(ResultCode.IM_UPDATEPWD_FAIL, returnValue);
					}
				}
				return ResponseResult.success();
			}
			return ResponseResult.failure(ResultCode.UPDATEPASSWORD_FAIL);
		}
		return ResponseResult.failure(ResultCode.ACCOUNT_NOTEXIST);
	}

	/**
	 * 手机登录
	 * 
	 * @param phone
	 * @param password
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult phoneLoginUp(String phone, String password, String countryCode, String phoneModel,
			String snNum, Integer sysType, String bssId, String ssId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		User user = new User();
		user = userMapper.selectUserByPhoneAndCountryCode(phone, countryCode);

		// 更新用户登录状态
		try {

			logSelectUserByPhone(phone, countryCode);
			if (user != null) {
				// 更新离线token和系统类型
				Integer pu = userMapper.updatePushTokenAndSysType(user.getUserId(), null, sysType);// token 为什么是为空
				if (pu > 0) {
					log.info("---用户- " + user.getUserId() + "-----更新离线系统类型成功--------  ");
				}
				Integer index = userMapper.updatePhoneModel(user.getUserId(), phoneModel);
				if (index > 0) {

					/* if (index2 > 0) { */
					if (user.getPassword().equals(password)) {
						Integer index2 = userMapper.updateLogStatusByUserId(user.getUserId(), 1);
						Integer relationProId = userMapper.selectRelationProIdByUserId(user.getUserId());
						log.info("-------登录成功-------phone--》" + phone + " --password---->" + password
								+ "---countryCode---->" + countryCode + "---phoneModel------>" + phoneModel);
						// 置空上一次推送token
						History history2 = historyMapper.selectUserIdBySnNum(snNum);
						if (history2 != null) {
							log.info(">>>>>>>>>> " + history2);
							int userId = history2.getUserId();
							if (user.getUserId() != userId) {
								if (userId > 0) {
									log.info("------查询设备最后一次登录userId------" + userId);
									int index3 = pushMapper.updatePushTokenAndpushKit(userId);
									if (index3 > 0) {
										log.info("------置为12345------");
									}
								}

							}
						}
						if (bssId != null) {

							int index5 = userMapper.updateUserIpv4(user.getImConnect(), bssId, ssId, null);
							if (index5 > 0) {
								log.info("更新网络配置成功");
							}
						}

						Date date = new Date();
						// 设置要获取到什么样的时间
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						// 获取String类型的时间
						String createdate = sdf.format(date);
						String token = password + "#" + createdate;
						// 加密密码
						final Base64.Encoder encoder = Base64.getEncoder();
						final byte[] textByte = token.getBytes("UTF-8");
						// 编码
						final String encodedText = encoder.encodeToString(textByte);

						user.setToken(encodedText + ":" + snNum); // 设置密文返回
						Integer to = pushMapper.updateToken(user.getUserId(), encodedText, sysType);
						if (to > 0)
							log.info("--token--" + encodedText + ",更新成功");

						user.setRelationProId(relationProId);
						data.put("user", user);
						// 登录成功 录入手机用户登录历史记录
						// 查看是否登陆过该手机 如果登陆过则不进入安全验证 返回
						log.info("------------登录成功 查看是否有历史记录-----------");
						log.info("SELECT\r\n"
								+ "		historyId,userId,phoneModel,lastLogonTime,snNum,DeviceLock FROM\r\n"
								+ "		history\r\n" + "		WHERE userId = " + user.getUserId() + "\r\n"
								+ "		ORDER BY lastLogonTime DESC\r\n" + "		LIMIT 0,1");
						// 查询最近的设备锁有没有开启POST /user/
						log.info("---SnNum---------------->" + snNum);
						History ss = historyMapper.selectDeviceLock(user.getUserId());
						if (ss != null) {
							List<History> hiss = historyMapper.selectHistoryByUserId(user.getUserId());
							// 如果没开启设备锁 则进行登录记录
							if (ss.getDeviceLock() == 0) {// 判断是否snNum是否存在
								log.info("------当前设备锁为关闭状态-----");
								if (hiss != null && hiss.size() > 0) {
									boolean falg = false;
									for (History s : hiss) {
										// 如果存在相同的snNum，则修改记录
										if (snNum.equals(s.getSnNum())) {
											log.info("---------存在该手机的历史记录，则更新该手机记录的最后登陆时间----------");
											Integer ind = historyMapper.updateHisInfo(s.getHistoryId(),
													System.currentTimeMillis() + "");
											if (ind > 0) {
												log.info("------更新手机登录记录成功-----");
												data.put("status", 0);
											}
											falg = true;
											break;
										}
									}
									// 如果不存在相同的snNum，则添加新的历史记录
									if (!falg) {
										log.info("------不存在手机历史记录----snNum---->" + snNum);
										History history = new History();
										history.setUserId(user.getUserId());
										history.setLastLogonTime(System.currentTimeMillis() + "");
										history.setPhoneModel(phoneModel);
										history.setDeviceLock(0);
										history.setSnNum(snNum);
										Integer in = historyMapper.insertHistory(history);
										if (in > 0) {
											log.info("------新添加了手机登录历史记录-----");
										}
									}
								}
							} else {
								// 返回设备开启设备锁
								boolean falg = false;
								for (History s : hiss) {
									// 如果存在相同的snNum，则修改记录
									if (snNum.equals(s.getSnNum())) {
										Integer ind = historyMapper.updateHisInfo(s.getHistoryId(),
												System.currentTimeMillis() + "");
										falg = true;
										data.put("status", 0);
										if (ind > 0) {
											log.info("------更新手机登录记录成功-----");
										}
										break;
									}
								}
								if (!falg) {
									log.info("------当前设备锁开启状态-------");
									data.put("status", 1);
								}
							}
						}
						log.info("------推送成功-----");
						return ResponseResult.success(data);
					} else if (user != null && !user.getPassword().equals(password)) {
						return ResponseResult.failure(ResultCode.ACCOUNT_PASSWORD_MISMATCH);
					} else {
						return ResponseResult.failure(ResultCode.ACCOUNT_NOTEXIST);
					}
//					}

				}
			} else {
				return ResponseResult.failure(ResultCode.CODEO_OR_ACCOUNT_ERR);
			}
		} catch (Exception e) {
			log.info("-------登录失败------- ");
			e.printStackTrace();
			return ResponseResult.failure(ResultCode.ACCOUNT_NOTEXIST);
		}
		return ResponseResult.failure(ResultCode.LOGIN_ERR);
	}

	/**
	 * 用户登出
	 * 
	 * @param userId
	 * @param logStatus
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult logOut(Integer userId) throws Exception {
		Integer logStatus = 2;
		User uu = userMapper.selectUserById(userId);
		log.info("-------用户" + userId + "正在登出-------   ");
		log.info("-------UPDATE usT logStatus = " + logStatus + "\r\n" + "		WHERE userId = " + userId + ";-------");
		if (uu != null) {
			Integer index = userMapper.updateLogStatusByUserId(userId, logStatus);
			log.info("-------修改用户" + userId + "token为0 正在登出-------");
			if (index > 0) {
				log.info("-------用户:" + userId + "登出成功----进行登出推送---");
				pushInfo(uu.getImConnect(), 3, 1088);
				log.info("-----登出推送成功-----");
			}
		}
		return ResponseResult.success(); // 登出成功
	}

	/**
	 * 二次登录
	 * 
	 * @param phone
	 * @param countryCode
	 * @param phoneModel
	 * @param snNum
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult secondVali(String phone, String countryCode, String phoneModel, String snNum)
			throws Exception {
		log.info("-----------正在进行二次验证---------");
		User user = userMapper.selectUserByPhoneAndCountryCode(phone, countryCode);
		Map<String, Object> data = new HashMap<String, Object>();
		if (user != null) {

			log.info("----------二次验证成功---------");
			log.info("----------本次登录录入到历史记录---------");
			History hi = new History();
			hi = historyMapper.lastSnName(user.getUserId());
			data.put("history", hi);
			/*
			 * hi.setUserId(user. getUserId()); // hi.setDeviceLock(1);
			 * hi.setLastLogonTime(System.currentTimeMillis() + "");
			 * hi.setPhoneModel(phoneModel); hi.setSnNum(snNum); Integer index =
			 * historyMapper.insertHistory(hi);
			 */
			/*
			 * if (index > 0) {
			 * 
			 * log.info("----------本次登录录入到历史记录------成功---" + hi + ">>>");
			 * 
			 * } else { log.info("----------录入到历史记录------失败--- "); }
			 */

			return ResponseResult.success(data);
		}
		log.info("----------二次验证失败---------  ");
		return ResponseResult.failure(ResultCode.SECONDVALI_FAIL);
	}

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateUserInfo(User user, Integer appTypeId) throws Exception {
		logSelectUserById(user.getUserId());
		User userInfo = userMapper.selectUserById(user.getUserId()); // 先获取原始密码
		if (userInfo == null)
			return ResponseResult.failure(ResultCode.USERID_NULL);
		Integer index = 0;
		try {
			log.info("------------------完善信息------------------");
			log.info("UPDATE user\r\n" + "		<set>\r\n" + "			<if test=\"username != null\">\r\n"
					+ "				username = " + user.getUsername() + ",\r\n" + "			</if>\r\n"
					+ "			<if test=\"sex != null\">\r\n" + "				sex = " + user.getSex() + ",\r\n"
					+ "			</if>\r\n" + "			<if test=\"birthday != null\">\r\n" + "				birthday = "
					+ user.getBirthday() + ",\r\n" + "			</if>\r\n"
					+ "			<if test=\"imConnect != null\">\r\n" + "				imConnect= "
					+ user.getImConnect() + "\r\n" + "			</if>\r\n" + "<if test=\"photo != null\">\r\n"
					+ "				photo= " + user.getPhoto() + "\r\n" + "			</if>" + "		</set>\r\n"
					+ "		WHERE userId = " + user.getUserId());
			index = userMapper.updateUser(user);
			if (index > 0) {
				if (appTypeId == 1) {
					User us = new User();
					logSelectUserById(user.getUserId());
					us = userMapper.selectUserById(user.getUserId());
					if (us != null) {
						String result = updateIMInfo(us.getImConnect(), user.getUsername(), user.getSex()); // 更新im用户信息
						JSONObject object = JSONObject.parseObject(result);
						String returnValue = object.getString("returnValue"); // 获取{"success" : "true"}
						if ("0".equals(returnValue)) { // 0 表示im更新失败
							return ResponseResult.failure(ResultCode.IM_UPDATEPWD_FAIL, returnValue);
						}
					}
				}
				// 推送消息
				pushInfo(userInfo.getImConnect(), 3, 1088);
				log.info("-------更新成功-------");
				return ResponseResult.success(); // 用户信息更新成功
			} else {
				return ResponseResult.failure(ResultCode.UPDATEINFO_FAIL);
			}
		} catch (Exception e) {
			log.info("-------更新失败-------");
			e.printStackTrace();
			return ResponseResult.failure(ResultCode.UPDATEINFO_FAIL);
		}
	}

	/**
	 * 根据mac地址查询平板用户信息
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectUserByMacAddress(String macAddress) throws Exception {
		User user = userMapper.selectUserByMacAddress(macAddress);
		if (user != null) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("user", user);
			return ResponseResult.success(data); // 用户信息查询成功
		}
		return ResponseResult.failure(ResultCode.ACCOUNT_NOTEXIST);
	}

	/**
	 * 更新用户头像
	 * 
	 * @param userId 用户id
	 * @param photo  用户头像地址
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult updateUserPhotoById(Integer userId, String photo) throws Exception {
		Integer index = 0;
		try {
			log.info("-------更新头像-------");
			log.info("UPDATE `user`\r\n" + "		SET photo = " + photo + "\r\n" + "		WHERE userId =\r\n"
					+ "		" + userId + "");
			index = userMapper.updateUserPhotoById(userId, photo);
			if (index > 0) {
				log.info("-------更新头像成功-------");
				return ResponseResult.success(); // 用户信息更新成功
			} else {
				log.info("-------更新头像失败-------");
				return ResponseResult.failure(ResultCode.UPLOAD_FILE_FAIL);
			}
		} catch (Exception e) {
			log.info("-------更新头像失败-------");
			e.printStackTrace();
			return ResponseResult.failure(ResultCode.UPLOAD_FILE_FAIL);
		}
	}

	/**
	 * 平板登录
	 * 
	 * @param macAddress mac地址
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult proLoginUp(String macAddress, Integer sysType) throws Exception {
		log.info("--平板用户macAddress--->" + macAddress + "-----正在登录------   ");
		User user = new User();
		Equipment equipment = new Equipment();

		try {
			logSelectUserByMacAddress(macAddress);
			user = userMapper.selectUserByMacAddress(macAddress);
			equipment = equipmentMapper.selectEquipmentByMacAddress(macAddress);
			if (user != null) {
				// 获取当前时间搓
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
				String date1 = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
				// 检测平板是否到期
				if (date1.compareTo(equipment.getActEndTime()) < 0) {
					// 到期
					int index = equipmentMapper.updateActStatus(equipment.getProId(), 0, "0");

				}
				// 更新离线token和系统类型
				Integer pu = userMapper.updatePushTokenAndSysType(user.getUserId(), null, sysType);
				if (pu > 0) {
					log.info("---用户- " + user.getUserId() + "-----更新系统类型成功--------");
				}
				// 修改平板登录状态
				Map<String, Object> data = new HashMap<String, Object>();
				log.info(
						"--平板用户macAd                                                                                                                                                                                                                                                           dress--->"
								+ macAddress + "-----修改登录状态------");
				Integer ii = userMapper.updateLogStatusByUserId(user.getUserId(), 1);
				if (ii > 0) {
					log.info("-------登录成功------macAddress--->" + macAddress);
					String queryToken = UUID.randomUUID().toString().replace("-", "");
					String replyToken = UUID.randomUUID().toString().replace("-", "");
					Integer up = equipmentMapper.updateQueryReply(macAddress, queryToken, replyToken);

					Date date = new Date();
					// 设置要获取到什么样的时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// 获取String类型的时间
					String createdate = sdf.format(date);
					String token = user.getPassword() + "#" + createdate;
					// 加密密码
					final Base64.Encoder encoder = Base64.getEncoder();
					final byte[] textByte = token.getBytes("UTF-8");
					// 编码
					final String encodedText = encoder.encodeToString(textByte);
					user.setToken(encodedText); // 设置密文返回
					data.put("user", user);
					if (up > 0) {
						log.info("-------queryToken------>" + queryToken);
						log.info("-------replyToken------>" + replyToken);
						return ResponseResult.success(data);
					} else {
						log.info("-------更新queryToken，replyToken失败------");
					}
				}
				return ResponseResult.failure(ResultCode.LOGIN_ERR);
			} else {
				return ResponseResult.failure(ResultCode.LOGIN_ERR);
			}
		} catch (Exception e) {
			log.info("-------登录失败-------");
			e.printStackTrace();
			return ResponseResult.failure(ResultCode.LOGIN_ERR);
		}
	}

	/**
	 * 根据用户id查看用户信息
	 * 
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectUserById(Integer userId) throws Exception {
		User user = new User();
		try {
			logSelectUserById(userId);
			user = userMapper.selectUserById(userId);
			if (user != null) {
				log.info("-------查询成功-------");
				Map<String, Object> data = new HashMap<String, Object>();
				Integer relationProId = userMapper.selectRelationProIdByUserId(userId);
				user.setRelationProId(relationProId);
				data.put("user", user);
				return ResponseResult.success(data);
			} else {
				return ResponseResult.failure(ResultCode.ACCOUNT_NOTEXIST);
			}
		} catch (Exception e) {
			log.info("-------登录失败-------");
			e.printStackTrace();
			return ResponseResult.failure(ResultCode.LOGIN_ERR);
		}
	}

	/**
	 * 修改局域网配置信息
	 * 
	 * @param imConnect
	 * @param bssId
	 * @param ssId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult updateUserIpv4(String imConnect, String bssId, String ssId, HttpServletRequest request)
			throws Exception {

		AddressUtils addressUtils = new AddressUtils();
		if (imConnect != null) {
			String ip = addressUtils.getIpAddress(request);
			int Index = 0;
			User user = userMapper.selectUserByIm(imConnect);
			if (user.getAccType() == 1) {
				Index = userMapper.updateUserIpv4(imConnect, bssId, ssId, ip);
				return ResponseResult.success();
			}
			log.info("进来了11  拿个碗 走来走去  ");
			if (bssId.equals("8888")) {
				log.info("进来了222");
				User user1 = relationMapper.selectProByUserId(user.getUserId());
				log.info("进来了44" + user1.getIp());
				log.info("手机端 IP" + ip);
				if (ip.equals(user1.getIp())) {
					Index = userMapper.updateUserIpv4(imConnect, user1.getBssId(), ssId, null);
				}

			} else {
				Index = userMapper.updateUserIpv4(imConnect, bssId, ssId, null);
			}
			if (Index > 0) {
				log.info("修改局域网配置信息成功");
				return ResponseResult.success();
			}
		}
		return ResponseResult.failure(ResultCode.IMCONNECT_NULL);
	}

	/**
	 * 修改单个无绳子机通话状态
	 * 
	 * @param macAddresssedf
	 * @param phone
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult updateSingNoRopeMachineCallStatus(String macAddress, String phone, Integer callStatus)
			throws Exception {
		log.info("-----------修改无绳子机通话状态--------》----" + macAddress + ",phone------>" + phone
				+ ",-----------callStatus-->" + callStatus);
		Integer index = userMapper.updateSingNoRopeMachineCallStatus(macAddress, phone, callStatus);
		if (index > 0) {
			User uu = userMapper.selectUserByMacAddress(macAddress);
			if (uu != null) {
				log.info("-------修改无绳子机通话状态成功--正在进行状态推送-----");
				// 推送消息
				log.info("-------状态推送 成功-----");
			}
			return ResponseResult.success();
		}
		return ResponseResult.failure(ResultCode.LOGIN_ERR);
	}

	/**
	 * 修改单个无绳子机通话状态
	 * 
	 * @param macAddress
	 * @param phone
	 * @param
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult clearPushNews(int userId) throws Exception {
		int index = pushMapper.updateNewsByIm(userId, 0);
		if (index > 0) {
			log.info("消息清零成功");
			return ResponseResult.success();
		}
		log.info("消息清零失败");
		return ResponseResult.failure(ResultCode.ACCOUNT_EXIST);

	}

	/**
	 * 插入密保问题
	 * 
	 * @param security
	 * @return
	 */
	@Transactional
	public ResponseResult insertSecuityQuestion(Security security) {
		// 提交请求到http rest服务端
		/* securityManager */
		try {
			int index = securityManager.insertSecuityQuestion(security);
			if (index > 0) {
				return ResponseResult.success();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return ResponseResult.failure(ResultCode.SECURITY_NULL);
	}

	/**
	 * 查询密保问题
	 * 
	 * @param phone
	 * @return
	 */
	@Transactional
	public ResponseResult selectSecuityQuestion(String phone) {
		// 提交请求到http rest服务端
		/* securityManager */
		try {
			User user = userMapper.selectUserByPhone(phone);
//			 
			if (user != null) {
				Security security = securityManager.selectSecuityQuestion(user.getUserId());
				if (security != null) {
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("security", security);
					return ResponseResult.success(data);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return ResponseResult.failure(ResultCode.SECURITY_EIXT);
	}

	/**
	 * 更新密保问题 中国经济增速
	 * 
	 * @param security
	 * @return
	 */
	@Transactional
	public ResponseResult updateSecuityQuestion(Security security) {
		// 提交请求到http rest服务端
		/* securityManager */
		try {
			int index = securityManager.updateSecuityQuestion(security);
//			  
			if (index > 0) {
				return ResponseResult.success();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

		return ResponseResult.failure(ResultCode.SECURITY_EIXT);
	}

	@Transactional
	public ResponseResult banOffinlePush(String phone) {

		// 提交请求到http rest服务端
		/* securityManager */
		try {

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return ResponseResult.failure(ResultCode.SECURITY_NULL);
	}

	/**
	 * 修改密码
	 * 
	 * @param phone
	 * @return
	 */
	@Transactional
	public ResponseResult changePassword(String phone, String newPwd) {

		// 提交请求到http rest服务端
		/* securityManager */
		try {
			int index = userMapper.changePassword(phone, newPwd);
			if (index > 0) {
				return ResponseResult.success();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return ResponseResult.failure(ResultCode.PHONE_EXIST);
	}

	/**
	 * 设置免打扰
	 * 
	 * @param userId
	 * @param ringTone
	 * @param control
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult dontDisturb(int userId, String ringTone, int control) throws Exception {
		boolean isResult = false;
		try {
			isResult = pushMapper.dontDisturb(userId, control);
			if (isResult) {
				return ResponseResult.success();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseResult.failure(ResultCode.DISTURB_ERR);

	}

	@Transactional
	public ResponseResult selectDisturb(int userId) throws Exception {
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			Push push = pushMapper.selectDisturb(userId);
			if (push != null) {
				data.put("control", push.getControl());
				return ResponseResult.success(data);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseResult.failure(ResultCode.DISTURB_ERR);

	}

	/**
	 *  
	 */

	/**
	 * 注册Im
	 * 
	 * @param user
	 * @param requestType
	 * @param device
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public String registerIM(User user) {
		log.info("json 格式--------------------------------------user" + user);
		UserRegisterDTO redto = new UserRegisterDTO();
		if (user.getMacAddress() != null) { // 平板设备
			redto.setNickname(user.getMacAddress());
			redto.setUser_mail(user.getMacAddress());
			redto.setUser_psw("123456");
		} else {
			redto.setNickname(user.getPhone());
			redto.setUser_mail(user.getPhone());
			redto.setUser_psw(user.getPassword());
		}
		redto.setUser_sex("1");
		// 提交请求到http rest服务端
		DataFromClient dataFromClient = DataFromClient.n().setProcessorId(1008).setJobDispatchId(1).setActionId(7)
				.setNewData(new Gson().toJson(redto));
		String resultJson = new Gson().toJson(dataFromClient);
		log.info("json 格式--------------------------------------carry" + resultJson);
		// 用户注册提交的时候 是不是遗漏了什么

		String result = HttpClient.doPostJson("http://150.109.109.10:8080/rainbowchat_pro/rest_post", resultJson);

//		String result = HttpClient.doPostJson(constant.getServer() + "/rainbowchat_pro/rest_post", resultJson);
		log.info("请求im注册返回信息--------------------------------------carry" + result);
		return result;
	}

	/**
	 * IM修改密码
	 * 
	 * @param uid
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	@Transactional
	public String updateIMPwd(String uid, String oldPwd, String newPwd) {
		// 提交请求到http rest服务端
		DataFromClient dataFromClient = DataFromClient.n().setProcessorId(1008).setJobDispatchId(1).setActionId(9)
				.setNewData(new Gson().toJson(RestHashMap.n().p("uid", uid).p("old_psw", oldPwd).p("psw", newPwd)));
		String resultJson = new Gson().toJson(dataFromClient);
		log.info("json 格式-------------------------1·-------------" + resultJson);
		String result = HttpClient.doPostJson("http://150.109.109.10:8080/rainbowchat_pro/rest_post", resultJson);
//		String result = HttpClient.doPostJson(constant.getServer() + "/rainbowchat_pro/rest_post", resultJson);
		log.info("请求im修改密码返回信息--------------------------------------" + result);
		return result;
	}

	/**
	 * IM基本信息修改
	 * 
	 * @param uid
	 * @param nickName
	 * @param sex
	 * @return
	 */
	@Transactional
	public String updateIMInfo(String uid, String nickName, Integer sex) {
		// 提交请求到http rest服务端 是的嘛
		DataFromClient dataFromClient = DataFromClient.n().setProcessorId(1008).setJobDispatchId(1).setActionId(8)
				.setNewData(new Gson().toJson(RestHashMap.n().p("uid", uid).p("nickName", nickName).p("sex", sex)));
		String resultJson = new Gson().toJson(dataFromClient);
		log.info("json 格式--------------------------------------" + resultJson);
		String result = HttpClient.doPostJson("http://150.109.109.10:8080/rainbowchat_pro/rest_post", resultJson);
//		String result = HttpClient.doPostJson(constant.getServer() + "/rainbowchat_pro/rest_post", resultJson);
		log.info("请求im修改基本信息返回信息-------------------------------------- " + result);
		return result;
	}

	/**
	 * 日志：根据手机号码查询是否存在信息
	 * 
	 * @param phone
	 * @param countryCode
	 */
	public void logSelectUserByPhone(String phone, String countryCode) {
		log.info("select\r\n"
				+ "		userId,username,password,phone,macAddress,email,birthday,sex,photo,accType,countryCode,imConnect,logStatus,appTypeId,createTime,callStatus\r\n"
				+ "		from user\r\n" + "		where phone = " + phone + " and countryCode = " + countryCode + "");
	}

	/**
	 * 日志： 根据mac地址查询是否存在信息
	 * 
	 * @param macAddress
	 */
	public void logSelectUserByMacAddress(String macAddress) {
		log.info("select\r\n"
				+ "		userId,username,password,phone,macAddress,email,birthday,sex,photo,accType,countryCode,imConnect,logStatus,appTypeId,createTime,callStatus\r\n"
				+ "		from user\r\n" + "		where macAddress = " + macAddress + " and accType = 1");
	}

	/**
	 * 
	 * 日志：根据用户id查询信息
	 */
	public void logSelectUserById(Integer userId) {
		log.info("SELECT\r\n"
				+ "		userId,username,password,phone,macAddress,email,birthday,sex,photo,accType,countryCode,imConnect,logStatus,appTypeId\r\n"
				+ "		FROM user\r\n" + "		WHERE userId = " + userId);
	}

}
