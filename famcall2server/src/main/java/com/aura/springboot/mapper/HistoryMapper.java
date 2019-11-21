package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.History;

@Mapper
public interface HistoryMapper {
	
	//记录手机登录历史
	Integer insertHistory(History history);
	
	//更新同一手机最近登录的时间
	Integer updateHisInfo(@Param("historyId")Integer historyId,@Param("lastLogonTime")String lastLogonTime)throws Exception;

	//查看设备锁状态
	History selectDeviceLock(@Param("userId")Integer userId)throws Exception;
	
	//根据historyid查询记录
	History selectHistoryById(@Param("historyId")Integer historyId)throws Exception;
	
	//查看手机用户所有登录过的手机
	List<History> selectHistoryByUserId(@Param("userId")Integer userId)throws Exception;
	
	//删除该历史记录
	Integer delHistoryById(@Param("historyId")Integer historyId)throws Exception;
	
	//修改设备锁状态  设备锁状态（0：关闭、1：开启）
	Integer updateDev(@Param("historyId")Integer historyId,@Param("DeviceLock")Integer DeviceLock)throws Exception;
}
