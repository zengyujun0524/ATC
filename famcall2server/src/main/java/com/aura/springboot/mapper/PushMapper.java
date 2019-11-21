package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Push;
import com.aura.springboot.utils.Lock;
import com.aura.springboot.utils.LockModeType;

@Mapper
public interface PushMapper {
	
	//更新离线token
	Integer updatePushToken(@Param("userId")Integer userId,@Param("pushToken")String pushToken,@Param("Type")Integer Type);
	//通过Im记录消息数量（1-99）
	Integer updateNewsByIm(@Param("userId")Integer userId ,@Param("news")Integer news);
	//根据userId删除数据
	Integer deleteByUserId(@Param("userId")Integer userId);
	//增加推送数据
	Integer insertPush(@Param("userId")Integer userId,@Param("imConnect")Integer imConnect);
	//更新离线token和类型
   Integer updatePushTokenAndSysType(@Param("userId")Integer userId,@Param("pushToken")String pushToken,@Param("sysType")Integer sysType);
	//更新单点登录token
	Integer updateToken(@Param("userId")Integer userId,@Param("token")String token) throws Exception;
	//根据im账号查询用户实体
	Push selectUserByIm(@Param("imConnect")String imConnect)throws Exception; 
	
}
