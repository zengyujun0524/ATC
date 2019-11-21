package com.aura.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Equipment;
import com.aura.springboot.vo.equipment.EquipmentModiyTableStausVo;

@Mapper
public interface EquipmentMapper {
	// 根据proId查询信息
	Equipment selectEquiById(@Param("proId") Integer proId) throws Exception;

	// 根据mac地址 更新回复和查询token
	Integer updateQueryReply(@Param("macAddress") String macAddress, @Param("queryToken") String queryToken,
			@Param("replyToken") String replyToken);

	// 根据proId更新激活状态、激活时间
	Integer updateActStatus(@Param("proId") Integer proId, @Param("actStatus") Integer actStatus,
			@Param("actEndTime") String actEndTime);

	// 更新平板状态
	Integer updateTableStauts(@Param("proId") Integer proId, @Param("tableStatus") Integer tableStatus,
			@Param("occupier") String occupier) throws Exception;

	// 根据项目名称获取平板结构图
	String selectAppStructsBg(@Param("appName") String appName) throws Exception;

	// 根据mac地址查询白名单状态信息
	Equipment selectDoNotDisturbByMac(@Param("macAddress") String macAddress) throws Exception;

	// 根据id查询是否存在数据
	int selectEquById(@Param("proId") Integer proId) throws Exception;

	// 通过密保问题修改管理密码
	int updateAdminCodeByAnswer(@Param("proId") Integer proId, @Param("adminCode") String adminCode) throws Exception;

	// 根据macAddress查询信息
	Equipment selectEquipmentByMacAddress(@Param("macAddress") String macAddress) throws Exception;

	// 注册设备
	int insertEquipment(Equipment equipment) throws Exception;

	// 设置区域码
	int setRegionCode(@Param("macAddress") String macAddress, @Param("regionCode") String regionCode,
			@Param("country") String country, @Param("region") String region) throws Exception;

	// 设置/改修管理密码
	int updateAdminCode(@Param("proId") Integer proId, @Param("adminCode") String adminCode) throws Exception;

	// 设置/修改密保问题
	int updateProblem(@Param("proId") Integer proId, @Param("problem") String problem, @Param("answer") String answer)
			throws Exception;

	// 设置/修改密保手机
	int updateProPhone(@Param("proId") Integer proId, @Param("proPhone") String proPhone,
			@Param("countryCode") String countryCode) throws Exception;

	// 设置/修改座机号码
	int updateLocalPhone(@Param("proId") Integer proId, @Param("localPhone") String localPhone) throws Exception;

	// 修改运营商
	int updateOperator(@Param("proId") Integer proId, @Param("operatorId") Integer operatorId) throws Exception;

	// 设置座机号码和设备名称
	int updateLocalPhoneAndProName(@Param("proId") Integer proId, @Param("localPhone") String localPhone,
			@Param("proName") String proName) throws Exception;

	// 修改设备名称
	int updateProName(@Param("proId") Integer proId, @Param("proName") String proName) throws Exception;

	// 修改管理密码状态
	int updateAdminCodeStatus(@Param("proId") Integer proId, @Param("adminPwdSwitch") Integer adminPwdSwitch)
			throws Exception;

	// 查询管理密码状态
	int selectAdminCodeStatus(@Param("macAddress") String macAddress) throws Exception;

	// 白名单勿扰开启/关闭
	int doNotDisturbUpdate(@Param("proId") Integer proId, @Param("doNotDisturb") Integer doNotDisturb,
			@Param("disturbFromTime") String disturbFromTime, @Param("distrubToTime") String distrubToTime)
			throws Exception;

	// 黑名单拦截开关
	int UnblockUpdate(@Param("proId") Integer proId, @Param("Unblock") Integer Unblock) throws Exception;

	// 查询平板状态Vo
	EquipmentModiyTableStausVo selectTableStautsVo(@Param("macAddress") String macAddress) throws Exception;

}
