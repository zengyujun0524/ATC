package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.User;
import com.aura.springboot.utils.Lock;
import com.aura.springboot.utils.LockModeType;

@Mapper
public interface UserMapper {
	// 根据mac地址 修改登录状态
	Integer updateLogsByMac(@Param("macAddress") String macAddress, @Param("logStatus") Integer logStatus)
			throws Exception;

	// 根据im账号查询用户实体
	User selectUserByIm(@Param("imConnect") String imConnect) throws Exception;

	// 修改登录状态
	Integer updateLogStatusByUserId(@Param("userId") Integer userId, @Param("logStatus") Integer logStatus)
			throws Exception;

	// 根据手机号码和国家码修改手机类型
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	Integer updatePhoneModel(@Param("userId") Integer userId, @Param("phoneModel") String phoneModel) throws Exception;

	// 根据userId查询对应的手机编号
	Integer selectRelationProIdByUserId(@Param("userId") Integer userId) throws Exception;

	// 手机用户注册
	Integer registerPhone(User user) throws Exception;

	// 清空子机列表 如何
	Integer emptySubmachineList(@Param("macAddress") String macAddress) throws Exception;

	// 平板用户注册
	Integer registerPro(User user) throws Exception;

	// 更新无绳子机名称
	Integer updateNoRopeMachineName(@Param("macAddress") String macAddress, @Param("username") String username,
			@Param("phone") String phone) throws Exception;

	// 插入无绳子机
	Integer insertNoRopeMachine(@Param("macAddress") String macAddress, @Param("username") String username,
			@Param("phone") String phone, @Param("createTime") String createTime, @Param("logStatus") Integer logStatus)
			throws Exception;

	// 根据mac地址查询平板关联的无绳子机
	List<User> selectProRelationNoRopeMachine(@Param("macAddress") String macAddress) throws Exception;

	// 查询平板关联的无绳子机是否已经绑定

	Integer selectNoRopeMachineCount(@Param("macAddress") String macAddress, @Param("phone") String phone)
			throws Exception;

	// 修改or完善信息信息
	Integer updateUser(User user) throws Exception;

	// 更新单点登录token

	Integer updateToken(@Param("userId") Integer userId, @Param("token") String token) throws Exception;

	// 修改密码
	Integer updatePwd(@Param("userId") Integer userId, @Param("password") String password) throws Exception;

	// 根据用户id查询信息
	User selectUserById(@Param("userId") Integer userId) throws Exception;

	// 根据手机号码查询信息 行的
	User selectUserByPhoneAndCountryCode(@Param("phone") String phone, @Param("countryCode") String countryCode)
			throws Exception;

	// 根据手机号码删除对应的用户
	Integer deleteUserByPhone(@Param("phone") String phone) throws Exception;

	// 根据mac地址删除对应的用户
	Integer deleteUserByMacAddress(@Param("macAddress") String macAddress) throws Exception;

	// 更新imConnect
	Integer updateImConnect(@Param("userId") Integer userId, @Param("imConnect") String imConnect) throws Exception;

	// 更新头像信息
	Integer updateUserPhotoById(@Param("userId") Integer userId, @Param("photo") String photo) throws Exception;

	// 根据mac地址查询信息
	User selectUserByMacAddress(@Param("macAddress") String macAddress) throws Exception;

	// 修改通话状态 是啊
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	Integer updateCallStatus(@Param("userId") Integer userId, @Param("callStatus") Integer callStatus) throws Exception;

	// 通过传入手机 imConnec
	// 查找出对应的userId，然后通过userId在设备绑定表中查找对应的具有绑定关系的设备，以此查找出他们的imConnect 返回数据，用于Im推送消息
	String[] selectImConnect(@Param("imConnect") String imConnect) throws Exception;

	// 通过传入平板 imConnect
	String[] selectImConnectByMac(@Param("imConnect") String imConnect) throws Exception;

	// 修改所有无绳子机通话状态
	Integer updateNoRopeMachineCallStatus(@Param("macAddress") String macAddress,
			@Param("callStatus") Integer callStatus);

	// 修改单个无绳子机通话状态 五一 坐了回火车
	Integer updateSingNoRopeMachineCallStatus(@Param("macAddress") String macAddress, @Param("phone") String phone,
			@Param("callStatus") Integer callStatus);

	// 更新离线token和系统类型
	Integer updatePushTokenAndSysType(@Param("userId") Integer userId, @Param("pushToken") String pushToken,
			@Param("sysType") Integer sysType);

	// 通过Im记录消息数量（1-99）
	Integer updateNewsByIm(@Param("imConnect") String imConnect, @Param("news") Integer news);

	// 根据userId删除数据
	Integer deleteByUserId(@Param("userId") Integer userId);

	// 更新局域网配置
	Integer updateUserIpv4(@Param("imConnect") String imConnect, @Param("bssId") String bssId,
			@Param("ssId") String ssId, @Param("ip") String ip);

	User selectIpv4ByMacAddress(@Param("macAddress") String macAddress) throws Exception;

	// 一次性更新子机显示状态
	Integer updateUserDisplay(@Param("macAddress") String macAddress, @Param("accType") int accType,
			@Param("password") String password, @Param("phone") String phone);

	// 根据电话号码查询
	User selectUserByPhone(@Param("phone") String phone) throws Exception;

	// 修改密码
	int changePassword(@Param("phone") String phone, @Param("password") String newPwd) throws Exception;

	// 添加好友
	int addFriendRequest(@Param("userId") String userId, @Param("friendsId") String friendsId,
			@Param("friendPhone") String friendPhone, @Param("friendName") String friendName,
			@Param("photo") String photo);

}
