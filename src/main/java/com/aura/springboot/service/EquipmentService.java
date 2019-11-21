package com.aura.springboot.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.Equipment;
import com.aura.springboot.entity.User;
import com.aura.springboot.mapper.EquipmentMapper;
import com.aura.springboot.mapper.UserMapper;
import com.aura.springboot.utils.AddressUtils;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class EquipmentService {

	private static Logger log = LoggerFactory.getLogger(EquipmentService.class);

	@Autowired
	private EquipmentMapper equipmentMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserService userService;

	/**
	 * 使用原始管理密码修改密码
	 * 
	 * @param macAddress
	 * @param newPwd
	 * @param oldPwd
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateAdminCodeByOldAPwd(String macAddress, String adminCode, String oldadminCode)
			throws Exception {
		logPrintMac(macAddress);
		Equipment equipment = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (equipment != null) {
			if ((equipment.getAdminCode()).equals(oldadminCode)) {
				log.info("-------------更新管理密码成功---------------》 ");
				log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n"
						+ "		adminCode=#{adminCode}\r\n" + "		WHERE\r\n" + "		proId = "
						+ equipment.getProId());
				Integer index = equipmentMapper.updateAdminCode(equipment.getProId(), adminCode);
				if (index > 0) {
					log.info("----------------------------》 更新管理密码成功");
					return ResponseResult.success();
				}
			}
		}
		log.info("----------------------------》 更新管理密码失败");
		return ResponseResult.failure(ResultCode.UPDATE_ADMINPASSWORD_FAIL);
	}

	/**
	 * 更新平板状态
	 * 
	 * @param macAddress
	 * @param tableStatus
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateTableStauts(String macAddress, Integer tableStatus, String occupier) throws Exception {
		logPrintMac(macAddress);

		User uu = userMapper.selectUserByMacAddress(macAddress);
		Equipment e = equipmentMapper.selectEquipmentByMacAddress(macAddress);

		/*
		 * EquipmentModiyTableStausVo eqStausVo =
		 * equipmentMapper.selectTableStautsVo(macAddress);
		 */
		if (uu != null && uu.getAccType() == 1 && e != null) {
			log.info("-------------更新平板状态--------------使用者为-----》 " + occupier);
			log.info("update equipment\r\n" + "		set tableStatus = " + tableStatus + "\r\n" + "		where\r\n 搞一个"
					+ "		proId = " + e.getProId());
			Integer index = equipmentMapper.updateTableStauts(e.getProId(), tableStatus, occupier);

			if (index > 0) {
				log.info("----------------------------》 更新平板状态成功");
				log.info("------------ 平板的状态为---------------》" + uu.getImConnect());
				/*
				 * if(3 == tableStatus) {
				 * 
				 * log.info("----------如果平板状态为正在通话，则更改平板、无绳子机通话状态--------"); Integer inr =
				 * userMapper.updateNoRopeMachineCallStatus(macAddress, 1); if(inr > 0) {
				 * log.info("----------平板、无绳子机通话状态更新成功--------");
				 * userService.pushInfo(uu.getImConnect(), 3, 1088);
				 * log.info("----------平板、无绳子机通话状态更新推送成功--------"); } }
				 */
				// 推送平板状态
				userService.pushInfo(uu.getImConnect(), 3, 1087);
				return ResponseResult.success();
			}
		}
		log.info("----------------------------》  更新平板状态失败");
		return ResponseResult.failure(ResultCode.EQUIUPDATE_FAIL);
	}

	/**
	 * 根据项目名称获取平板结构图
	 * 
	 * @param appName 项目名称
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectAppStructsBg(String appName) throws Exception {
		// 查询平板结构图
		String sbg = equipmentMapper.selectAppStructsBg(appName);
		if (sbg != null) {
			log.info("----------------------------》 获取平板结构图成功");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("result", sbg);
			return ResponseResult.success(data);
		}
		log.info("----------------------------》 获取平板结构图失败");
		return ResponseResult.failure(ResultCode.SELECTSBG_FAIL);
	}

	/**
	 * 按mac地址查询设备信息
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectEquByMac(HttpServletRequest request, String macAddress) throws Exception {
		AddressUtils addressUtils = new AddressUtils();
		log.info(">>>>>>>>>>" + addressUtils.getIpAddress(request));

		logPrintMac(macAddress);
		Equipment equipment = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (equipment != null) {
			log.info("----------------------------》 设备信息查询成功");
			Map<String, Object> data = new HashMap<String, Object>();

			User user = userMapper.selectIpv4ByMacAddress(macAddress);
			equipment.setSsId(user.getSsId());
			equipment.setBssId(user.getBssId());
			data.put("result", equipment);
			return ResponseResult.success(data);
		}
		log.info("----------------------------》 设备信息查询失败");

		return ResponseResult.failure(ResultCode.SEEINFO_FAIL);

	}

	/**
	 * 更新国家和地区
	 * 
	 * @param macAddress
	 * @param country
	 * @param region
	 * @return
	 * @throws Exception
	 */
//	@Transactional
//	public ResponseResult updateCARByMac(String macAddress,String country,String region) throws Exception {
//		logPrintMac(macAddress);
//		Integer index = equipmentMapper.updateCARByMac(macAddress,country,region); 
//		if (index > 0) {
//			log.info("----------------------------》 更新国家/区域成功");
//			log.info("UPDATE\r\n" + 
//					"		`equipment`\r\n" + 
//					"		<set>\r\n" + 
//					"			<if test=\"country != null\">\r\n" + 
//					"				country = "+country+",\r\n" + 
//					"			</if>\r\n" + 
//					"			<if test=\"region != null\">\r\n" + 
//					"				region = "+region+",\r\n" + 
//					"			</if>\r\n" + 
//					"		</set>\r\n" + 
//					"		WHERE\r\n" + 
//					"		macAddress = "+macAddress+"");
//			return ResponseResult.success();
//		} 

//		log.info("----------------------------》 更新国家/区域失败");
//		return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATE_INFO_FAIL);
//	}

	/**
	 * 按mac地址查询设备白名单信息
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectDoNotDisturbByMac(String macAddress) throws Exception {
		log.info("-----------------按mac地址查询设备白名单信息-----------");
		log.info("select doNotDisturb,disturbFromTime,distrubToTime");
		log.info("from equipment where macAddress = " + macAddress);
		Equipment equipment = equipmentMapper.selectDoNotDisturbByMac(macAddress);
		if (equipment != null) {
			log.info("----------------------------》 设备白名单信息查询成功");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("doNotDisturb", equipment.getDoNotDisturb());
			data.put("disturbFromTime", equipment.getDisturbFromTime());
			data.put("distrubToTime", equipment.getDistrubToTime());
			return ResponseResult.success(data);
		}
		log.info("----------------------------》 设备白名单信息查询失败");
		return ResponseResult.failure(ResultCode.SEEINFO_FAIL);
	}

	/**
	 * 注册设备
	 * 
	 * @param equipment 设备实体
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult insertEquipment(Equipment equipment, Integer appTypeId, String pushToken, Integer sysType,
			String proNum) throws Exception {
		// 根据传入mac地址查询，是否已注册
		logPrintMac(equipment.getMacAddress());
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(equipment.getMacAddress());
		if (result != null)
			return ResponseResult.failure(ResultCode.MACADDRESS_EXIST);
		log.info("----------------------------》 设备注册开始");
		log.info(
				"INSERT INTO `equipment`(macAddress,proName,proNum,activationTime,proVersion,proSysVersion,firmwareSysVersion,adminPwdSwitch,doNotDisturb,Unblock,disturbFromTime,distrubToTime,tableStatus)");
		log.info("VALUES(" + equipment.getMacAddress() + "," + equipment.getProName() + "," + equipment.getProNum()
				+ "," + equipment.getActivationTime() + "," + equipment.getProVersion() + ","
				+ equipment.getProSysVersion() + "," + equipment.getFirmwareSysVersion() + ","
				+ equipment.getAdminPwdSwitch() + "," + equipment.getDoNotDisturb() + "," + equipment.getUnblock()
				+ ",23:59,7:00,0) ");
		Integer index = equipmentMapper.insertEquipment(equipment);
		if (index > 0) {
			log.info("----------------------------》 设备注册成功");
			// 注册平板用户
			log.info("----------------------------》 注册平板用户");
			log.info("---MacAddress------>" + equipment.getMacAddress() + "----appTypeId----->" + appTypeId
					+ "-------》 注册平板用户");
			return userService.userMacAddressRegister(equipment.getMacAddress(), appTypeId,
					System.currentTimeMillis() + "", pushToken, sysType, proNum);
		} else {
			log.info("----------------------------》 设备注册失败");
			return ResponseResult.failure(ResultCode.EQUIPMENT_REGISTER_FAIL);
		}
	}

	/**
	 * 白名单勿扰开启/关闭
	 * 
	 * @param macAddress
	 * @param doNotDisturb
	 * @param disturbFromTime
	 * @param distrubToTime
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult doNotDisturbUpdate(String macAddress, Integer doNotDisturb, String disturbFromTime,
			String distrubToTime) throws Exception {
		Equipment e = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		log.info("------------》 白名单勿扰开启/关闭----------------");
		log.info("update equipment\r\n" + "		<set>\r\n" + "			<if test=\"doNotDisturb!=null\">\r\n"
				+ "				doNotDisturb = " + doNotDisturb + " ,\r\n" + "			</if>\r\n"
				+ "			<if test=\"disturbFromTime!=null\">\r\n" + "				disturbFromTime = "
				+ disturbFromTime + ",\r\n" + "			</if>\r\n" + "			<if test=\"distrubToTime!=null\">\r\n"
				+ "				distrubToTime = " + distrubToTime + " ,\r\n" + "			</if>\r\n"
				+ "		</set>\r\n" + "		where proId = " + e.getProId() + "");
		if (e != null) {
			Integer index = equipmentMapper.doNotDisturbUpdate(e.getProId(), doNotDisturb, disturbFromTime,
					distrubToTime);
			if (index > 0) {
				log.info("----------------------------》 白名单勿扰开启/关闭");
				return ResponseResult.success();
			}
		}
		log.info("----------------------------》 白名单勿扰开启/关闭失败");
		return ResponseResult.failure(ResultCode.DONOTDISTURBUPDATE_FAIL);
	}

	/**
	 * 黑名单拦截开关
	 * 
	 * @param macAddress
	 * @param Unblock
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult UnblockUpdate(String macAddress, Integer Unblock) throws Exception {
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("-----------》 黑名单拦截开关-----------------");
		log.info("update equipment\r\n" + "		set Unblock = " + Unblock + "\r\n" + "		where\r\n"
				+ "		proId = " + result.getProId() + "");
		Integer index = equipmentMapper.UnblockUpdate(result.getProId(), Unblock);
		if (index > 0) {
			log.info("----------------------------》 黑名单拦截开关设置成功");
			return ResponseResult.success();
		}
		log.info("----------------------------》 黑名单拦截开关设置失败");
		return ResponseResult.failure(ResultCode.UNBLOCKUPDATE_FAIL);
	}

	/**
	 * 设置区域码
	 * 
	 * @param equipment 设备实体
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult setRegionCode(String macAddress, String regionCode, String country, String region)
			throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("------------》  设置区域码开始----------------");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n" + "		regionCode=" + regionCode
				+ "\r\n" + "		WHERE\r\n" + "		proId = " + result.getProId() + "");
		Integer index = equipmentMapper.setRegionCode(macAddress, regionCode, country, region);
		if (index > 0) {
			log.info("----------------------------》  设置区域码成功");
			return ResponseResult.success();
		} else {
			log.info("----------------------------》 设置区域码失败");
			return ResponseResult.failure(ResultCode.SETREGIONCODE_FAIL);
		}
	}

	/**
	 * 设置/修改管理密码
	 * 
	 * @param macAddress mac地址
	 * @param adminCode  管理密码
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateAdminCode(String macAddress, String adminCode) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.EQUIPMENT_MACADDRESS_NOEXIST);
		log.info("------------》  设置/修改管理密码开始----------------");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n" + "		adminCode=" + adminCode + "\r\n"
				+ "		WHERE\r\n" + "		proId = " + result.getProId() + "");
		Integer index = equipmentMapper.updateAdminCode(result.getProId(), adminCode);
		if (index > 0) {
			log.info("----------------------------》  设置/修改管理密码成功");
			return ResponseResult.success();
		} else {
			log.info("----------------------------》  设置/修改管理密码失败");
			return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATE_ADMINCODE_FAIL);
		}
	}

	/**
	 * 通过密保问题修改管理密码
	 * 
	 * @param macAddress mac地址
	 * @param adminCode  管理密码
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateAdminCodeByAnswer(String macAddress, String adminCode, String answer) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.EQUIPMENT_MACADDRESS_NOEXIST);
		/* if ((result.getAnswer()).equals(answer)) { */
		log.info("------------》  通过密保问题修改管理密码----------------");
		log.info("update equipment \r\n" + "		set adminCode = " + adminCode + "\r\n" + "		where proId = "
				+ result.getProId());
		Integer index = equipmentMapper.updateAdminCodeByAnswer(result.getProId(), adminCode);
		if (index > 0) {
			log.info("----------------------------》 通过密保问题修改管理密码成功");
			return ResponseResult.success();
		}
		/* } */
		log.info("----------------------------》  通过密保问题修改管理密码失败");
		return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATE_ADMINCODE_FAIL);

	}

	/**
	 * 设置/修改密保问题
	 * 
	 * @param macAddress mac地址
	 * @param problem    密保问题
	 * @param answer     密保答案
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateProblem(String macAddress, String problem, String answer) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("-------------》 设置/修改密保问题开始---------------");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n" + "		problem=" + problem + ",\r\n"
				+ "		answer=" + answer + "\r\n" + "		WHERE proId = " + result.getProId() + "");
		Integer index = equipmentMapper.updateProblem(result.getProId(), problem, answer);
		if (index > 0) {
			log.info("----------------------------》 设置/修改密保问题成功");
			return ResponseResult.success();
		} else {
			log.info("----------------------------》  设置/修改密保问题失败");
			return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATE_PROBLEM_FAIL);
		}
	}

	/**
	 * 设置/修改密保手机
	 * 
	 * @param macAddress mac地址
	 * @param proPhone   密保手机
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateProPhone(String macAddress, String proPhone, String countryCode) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("------------》 设置/修改密保手机开始----------------");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n" + "		proPhone=" + proPhone + "\r\n"
				+ "		WHERE\r\n" + "		proId = " + result.getProId() + "");
		Integer index = equipmentMapper.updateProPhone(result.getProId(), proPhone, countryCode);
		if (index > 0) {
			log.info("----------------------------》  设置/修改密保手机成功");
			return ResponseResult.success();
		} else {
			log.info("----------------------------》  设置/修改密保手机失败");
			return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATE_PROPHONE_FAIL);
		}
	}

	/**
	 * 设置/修改座机号码
	 * 
	 * @param macAddress mac地址
	 * @param localPhone 座机号码
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateLocalPhone(String macAddress, String localPhone) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("-----------》 设置/修改座机号码开始-----------------");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n" + "		localPhone=" + localPhone
				+ "\r\n" + "		WHERE\r\n" + "		proId = " + result.getProId() + "");
		Integer index = equipmentMapper.updateLocalPhone(result.getProId(), localPhone);
		if (index > 0) {
			log.info("----------------------------》  设置/修改座机号码成功");
			return ResponseResult.success();
		} else {
			log.info("----------------------------》  设置/修改座机号码失败");
			return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATE_LOCALPHONE_FAIL);
		}
	}

	/**
	 * 修改运营商
	 * 
	 * @param macAddress mac地址
	 * @param operatorId 运营商id
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateOperator(String macAddress, Integer operatorId) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("----------------------------》 修改运营商开始");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n" + "		operatorId=#{operatorId}\r\n"
				+ "		WHERE\r\n" + "		proId = " + result.getProId() + "");
		Integer index = equipmentMapper.updateOperator(result.getProId(), operatorId);
		if (index > 0) {
			log.info("----------------------------》  修改运营商成功");
			return ResponseResult.success();
		} else {
			log.info("----------------------------》  修改运营商失败");
			return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATEOPERATOR_FAIL);
		}
	}

	/**
	 * 设置座机号码和设备名称
	 * 
	 * @param macAddress
	 * @param localPhone
	 * @param proName
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateLocalPhoneAndProName(String macAddress, String localPhone, String proName)
			throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("------------》设置座机号码和设备名称开始----------------");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		<set>\r\n"
				+ "			<if test=\"localPhone!=null\">\r\n" + "				localPhone=" + localPhone + ",\r\n"
				+ "			</if>\r\n" + "			<if test=\"proName!=null\">\r\n" + "				proName = "
				+ proName + ",\r\n" + "			</if>\r\n" + "		</set>\r\n" + "		WHERE proId = "
				+ result.getProId() + "");
		Integer index = equipmentMapper.updateLocalPhoneAndProName(result.getProId(), localPhone, proName);
		if (index > 0) {
			log.info("----------------------------》  设置座机号码和设备名称成功");
			User userInfo = userMapper.selectUserByMacAddress(macAddress);
			userInfo.setUsername(proName);
			Integer inde = userMapper.updateUser(userInfo);
			if (inde > 0) {
				log.info("----------------------------》 修改设备名称成功");
				// 推送消息
				userService.pushInfo(userInfo.getImConnect(), 3, 1088);
			}
			return ResponseResult.success();
		} else {
			log.info("----------------------------》  设置座机号码和设备名称失败");
			return ResponseResult.failure(ResultCode.EQUIPMENT_SETLOCALPHONEANDPRONAME_FAIL);
		}
	}

	/**
	 * 修改设备名称
	 * 
	 * @param macAddress
	 * @param proName
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateProName(String macAddress, String proName) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("-------------》修改设备名称开始---------------");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n" + "		proName = " + proName + "\r\n"
				+ "		WHERE\r\n" + "		proId = " + result.getProId() + "");
		Integer index = equipmentMapper.updateProName(result.getProId(), proName);
		if (index > 0) {
			User userInfo = userMapper.selectUserByMacAddress(macAddress);
			userInfo.setUsername(proName);
			Integer inde = userMapper.updateUser(userInfo);
			if (inde > 0) {
				log.info("----------------------------》 修改设备名称成功");
				// 推送消息
				userService.pushInfo(userInfo.getImConnect(), 3, 1088);
			}
			return ResponseResult.success();
		} else {
			log.info("----------------------------》  修改设备名称失败");
			return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATEPRONAME_FAIL);
		}
	}

	/**
	 * 修改管理密码状态
	 * 
	 * @param macAddress     mac地址
	 * @param adminPwdSwitch 管理密码状态
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateAdminCodeStatus(String macAddress, Integer adminPwdSwitch) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息 都快超过我了
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("------------》修改管理密码状态开始----------------");
		log.info("UPDATE\r\n" + "		`equipment`\r\n" + "		SET\r\n" + "		adminPwdSwitch="
				+ adminPwdSwitch + "\r\n" + "		WHERE\r\n" + "		proId = " + result.getProId() + "");
		Integer index = equipmentMapper.updateAdminCodeStatus(result.getProId(), adminPwdSwitch);
		if (index > 0) {
			log.info("----------------------------》 修改管理密码状态成功");
			return ResponseResult.success();
		} else {
			log.info("----------------------------》  修改管理密码状态失败  ");
			return ResponseResult.failure(ResultCode.EQUIPMENT_UPDATEADMINCODESTATUS_FAIL);
		}
	}

	public ResponseResult getDevicePictures(Integer deviceType, Integer index) {
		Map<String, Object> data = new HashMap<String, Object>();
		String url = null;
		if (deviceType == 0) {
			if (index == 1) {
				//
				url = "http://150.109.109.10:8080/Device/AC5Internal%20call%20list.png";
			} else if (index == 2) {
				url = "http://150.109.109.10:8080/Device/AC5Inward%20Call-Out%20Interface.png";
			} else if (index == 3) {
				url = "http://150.109.109.10:8080/Device/AC5Equipment%20code%20plot.png";
			} else if (index == 4) {
				url = "http://150.109.109.10:8080/Device/AC5Set%20sweep%20code.png";
			} else if (index == 5) {
				url = "http://150.109.109.10:8080/Device/AC5_Base.png";
			}
			data.put("url", url);
		} else if (deviceType == 1) {
			if (index == 1) {
				url = "http://150.109.109.10:8080/Device/AC7Internal%20call%20list.png";
			} else if (index == 2) {
				url = "http://150.109.109.10:8080/Device/AC7Inward%20Call-Out%20Interface.png";
			} else if (index == 3) {
				url = "http://150.109.109.10:8080/Device/AC7Equipment%20code%20plot.png";
			} else if (index == 4) {
				url = "http://150.109.109.10:8080/Device/AC7Set%20sweep%20code.png";
			} else if (index == 5) {
				url = "http://150.109.109.10:8080/Device/AC7_Base.png";
			} else if (index == 6) {
				url = "http://150.109.109.10:8080/Device/AC7_Flat.png";
			}
			//
			data.put("url", url);
		} else if (deviceType == 2) {
			data.put("1", "http://150.109.109.10:8080/Device/AC5Internal%20call%20list.png");
			data.put("2", "http://150.109.109.10:8080/Device/AC5Inward%20Call-Out%20Interface.png");
			data.put("3", "http://150.109.109.10:8080/Device/AC5Equipment%20code%20plot.png");
			data.put("4", "http://150.109.109.10:8080/Device/AC5Set%20sweep%20code.png");
			data.put("5", "http://150.109.109.10:8080/Device/AC5_Base.png");
		} else if (deviceType == 3) {
			data.put("1", "http://150.109.109.10:8080/Device/AC7Internal%20call%20list.png");
			data.put("2", "http://150.109.109.10:8080/Device/AC7Inward%20Call-Out%20Interface.png");
			data.put("3", "http://150.109.109.10:8080/Device/AC7Equipment%20code%20plot.png");
			data.put("4", "http://150.109.109.10:8080/Device/AC7Set%20sweep%20code.png");
			data.put("5", "http://150.109.109.10:8080/Device/AC7_Base.png");
			data.put("6", "http://150.109.109.10:8080/Device/AC7_Flat.png");
		}

		return ResponseResult.success(data);
	}

	/**
	 * 查询管理密码状态
	 * 
	 * @param macAddress mac地址
	 * @return
	 * @throws Exception
	 * 
	 */
	@Transactional
	public ResponseResult selectAdminCodeStatus(String macAddress) throws Exception {
		logPrintMac(macAddress);
		// 根据传入mac地址查询，是否存在该信息
		Equipment result = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		if (result == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		log.info("-------------》查询管理密码状态---------------");
		log.info("select adminPwdSwitch from equipment where macAddress" + macAddress);
		Integer index = equipmentMapper.selectAdminCodeStatus(macAddress);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", index);
		return ResponseResult.success(data);
	}

	/**
	 * 日志：按mac地址查询设备信息
	 * 
	 * 
	 * @param macAddress
	 */
	public void logPrintMac(String macAddress) {
		log.info("---------按mac地址查询设备信息-------------");
		log.info(
				"select proId,macAddress,localPhone,problem,answer,proPhone,adminCode,proName,proNum,activationTime,proVersion,proSysVersion,firmwareSysVersion,adminPwdSwitch,regionCode,operatorId,doNotDisturb,disturbFromTime,distrubToTime,Unblock,countryCode,country,region,tableStatus");
		log.info("from equipment");
		log.info("where macAddress = " + macAddress);
	}
}
