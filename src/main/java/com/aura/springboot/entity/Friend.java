package com.aura.springboot.entity;

import javax.persistence.Entity;

/**
 * 亲友表
 * 
 * @author Carry
 * 
 */
@Entity
public class Friend {
	int msgId;// 消息ID
	int userId;// 用户ID
	int msgType;// 消息类型
	int friendsId;// 亲友的ID
	String bindingTime; // 关联时间
	int isRead; // 这些消息是否阅读 0:未阅读 1:已阅读
	int msgResult;// (发送消息处理结果: 0:待处理 1:同意 2:忽略)
	String name; // 姓名
	String photo; // 头像
	String phone; // 电话
	String friendPhone; // 好友的电话
	String friendName;// 好友姓名
	String friendPhoto; // 好友头像路径
	int isDelete; // 不客气

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getFriendsId() {
		return friendsId;
	}

	public void setFriendsId(int friendsId) {
		this.friendsId = friendsId;
	}

	public String getBindingTime() {
		return bindingTime;
	}

	public void setBindingTime(String bindingTime) {
		this.bindingTime = bindingTime;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getMsgResult() {
		return msgResult;
	}

	public void setMsgResult(int msgResult) {
		this.msgResult = msgResult;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFriendPhone() {
		return friendPhone;
	}

	public void setFriendPhone(String friendPhone) {
		this.friendPhone = friendPhone;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getFriendPhoto() {
		return friendPhoto;
	}

	public void setFriendPhoto(String friendPhoto) {
		this.friendPhoto = friendPhoto;
	}

	public Friend(int userId, int friendsId, String bindingTime) {
		this.userId = userId;
		this.friendsId = friendsId;
		this.bindingTime = bindingTime;
	}

	public Friend() {

	}

}
