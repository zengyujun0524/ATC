package com.aura.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aura.springboot.entity.Friend;

@Mapper
public interface FriendMapper {
	// 添加亲友
	int addFriendRequest(Friend friend);

	// 获取好友列表
	List<Friend> getFriendList(@Param("userId") int userId, @Param("type") int type);

	// 更新结果状态
	boolean updateProcessingResults(@Param("msgId") int msgId, @Param("msgResult") int msgResult,
			@Param("type") int type, @Param("msgType") int msgType);

	// 删除好友关系
	boolean deleteAssociatedFriends(@Param("msgId") int msgId);

	Friend getFriend(@Param("userId") int userId, @Param("friendsId") int friendsId);

	// 查询好友
	List<Friend> getFriendList2(@Param("userId") int userId);

	// 查询验证结果
	Friend aidedVerification(@Param("userId") int userId, @Param("friendsId") int friendsId);

	// 根据msgId查询好友
	Friend selectFriendByMsgId(@Param("msgId") int msgId);

	// 删除信息
	boolean delMsg(@Param("msgId") int msgId);

	// 清空所有消息
	boolean allDelMsg(@Param("userId") int userId);

}
