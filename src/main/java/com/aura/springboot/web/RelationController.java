package com.aura.springboot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.aura.springboot.entity.Friend;
import com.aura.springboot.service.RelationService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 设备关联
 * 
 * @author Carry
 *
 */
@RestController
@RequestMapping("/relation")
public class RelationController {

	@Autowired
	private RelationService relationService;

	/**
	 * 绑定设备 （暂时不用）
	 * 
	 * @param proId  平板id
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/binding", method = RequestMethod.POST)
//	@ApiOperation(value = "绑定设备", notes = "根据url信息绑定设备")  
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "proId", value = "平板id", dataType = "Integer", paramType = "query"),
//			@ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer", paramType = "query"),
//			@ApiImplicitParam(name = "bindingTime", value = "绑定时间", dataType = "String", paramType = "query") })
//	public ResponseResult binding(Integer proId, Integer userId,String bindingTime) throws Exception {
//		// 非空判断传入信息
//		if (proId == null) {
//			return ResponseResult.failure(ResultCode.EQUIPMENT_PROID_NULL);
//		}
//		if (userId == null) {
//			return ResponseResult.failure(ResultCode.USERID_NULL);
//		}
//		Relation relation = new Relation();
//		relation.setProId(proId);
//		relation.setUserId(userId);
//		relation.setBindingTime(bindingTime);
//		return relationService.binding(relation);
//	}

	/**
	 * 利用mac地址进行绑定
	 * 
	 * @param macAddress 平板id
	 * @param userId     用户id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/macBinding", method = RequestMethod.POST)
	@ApiOperation(value = "绑定设备", notes = "根据url信息绑定设备")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "bindingTime", value = "绑定时间", dataType = "String", paramType = "query") })
	public ResponseResult macBinding(String macAddress, Integer userId, String bindingTime) throws Exception {
		// 非空判断传入信息
		if (macAddress == null || StringUtils.isEmpty(macAddress)) {
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		}
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		return relationService.macBinding(macAddress, userId, bindingTime);
	}

	/**
	 * 解绑
	 * 
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/unBinding", method = RequestMethod.POST)
	@ApiOperation(value = "解绑", notes = "根据url信息解绑设备")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer", paramType = "query") })
	public ResponseResult unBinding(Integer userId) throws Exception {
		// 非空判断传入信息
		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		return relationService.unBinding(userId);
	}

	/**
	 * 根据userId查询设备列表
	 * 
	 * @param proId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectRelationByUserId", method = RequestMethod.POST)
	@ApiOperation(value = "根据userId查询设备列表", notes = "根据userId查询设备列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "accType", value = "用户类型 0：手机、1：平板", dataType = "int", paramType = "query") })
	public ResponseResult selectRelationByUserId(Integer userId, Integer accType) throws Exception {
		// 非空判断传入信息
		if (userId == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROID_NULL);
		}
		if (accType == null || (accType != 1 && accType != 0)) {
			return ResponseResult.failure(ResultCode.ACCOUNT_TYPE_NULL);
		}

		return relationService.selectRelationByUserId(userId, accType);
	}

	/**
	 * 根据userId查出对应的平板信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectProByUserId", method = RequestMethod.POST)
	@ApiOperation(value = "根据userId查出对应的平板信息", notes = "根据userId查出对应的平板信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query") })
	public ResponseResult selectProByUserId(Integer userId) throws Exception {
		// 非空判断传入信息
		if (userId == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROID_NULL);
		}
		return relationService.selectProByUserId(userId);
	}

	@RequestMapping(value = "/addFriendRequest", method = RequestMethod.POST)
	@ApiOperation(value = "添加亲友信息", notes = "添加亲友信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "friendsId", value = "好友ID", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "好友电话", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "name", value = "好友姓名", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "photo", value = "头像路径", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "bindingTime", value = "绑定时间", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "msgType", value = "0好友申请，1好友辅助", dataType = "String", paramType = "query") })
	public ResponseResult insertFriendsInfo(Integer userId, Integer friendsId, String phone, String name, String photo,
			String bindingTime, Integer msgType) throws Exception {
		// 非空判断传入信息
		if (userId == null) {
			return ResponseResult.failure(ResultCode.EQUIPMENT_PROID_NULL);
		}
		Friend friend = new Friend();
		friend.setName(name);
		friend.setUserId(userId);
		friend.setFriendsId(friendsId);
		friend.setPhone(phone);
		friend.setPhoto(photo);
		friend.setMsgType(msgType);
		return relationService.addFriendRequest(friend);
	}

	/**
	 * 获取好友列表
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getMessage", method = RequestMethod.POST)
	@ApiOperation(value = "获取消息", notes = "获取消息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "type", value = "type:0是发出邀请方，1是受邀请方", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "index", value = "临时值 为1 置为已阅读 ", dataType = "Integer", paramType = "query") })
	public ResponseResult getMessage(Integer userId, Integer type, Integer index) throws Exception {

		if (userId == null) {
			return ResponseResult.failure(ResultCode.USERID_NULL);
		}
		return relationService.getFriendList(userId, type, index);
	}

	/**
	 * 获取好友列表
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFriendList", method = RequestMethod.POST)
	@ApiOperation(value = "获取好友列表", notes = "获取好友列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户id", dataType = "Integer", paramType = "query") })
	public ResponseResult getFriendList2(Integer userId) throws Exception {
		return relationService.getFriendList2(userId);
	}

	/**
	 * 更新结果状态
	 * 
	 * @param msgId
	 * @param treat
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateProcessingResults", method = RequestMethod.POST)
	@ApiOperation(value = "更新结果状态", notes = "更新结果状态")
	@ApiImplicitParams({ @ApiImplicitParam(name = "msgId", value = "消息ID", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "msgResult", value = "处理结果:1同意，2拒绝", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "msgType", value = "处理结果:0申请，1辅助", dataType = "Integer", paramType = "query") })
	public ResponseResult updateProcessingResults(Integer msgId, Integer msgResult, Integer msgType) throws Exception {
		return relationService.updateProcessingResults(msgId, msgResult, msgType);
	}

	/**
	 * 删除好友关系
	 * 
	 * @param userId
	 * @param friendsId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteAssociatedFriends", method = RequestMethod.POST)
	@ApiOperation(value = "删除好友关系", notes = "删除好友关系")
	@ApiImplicitParams({ @ApiImplicitParam(name = "msgId", value = "消息ID", dataType = "Integer", paramType = "query") })
	public ResponseResult deleteAssociatedFriends(Integer msgId) throws Exception {
		return relationService.deleteAssociatedFriends(msgId);
	}

	/**
	 * 辅助验证
	 * 
	 * @param msgId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/aidedVerification", method = RequestMethod.POST)
	@ApiOperation(value = "辅助验证", notes = "辅助验证")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "自己的ID", dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "friendsId", value = "亲友ID", dataType = "Integer", paramType = "query") })
	public ResponseResult aidedVerification(Integer userId, Integer friendsId) throws Exception {
		return relationService.aidedVerification(userId, friendsId);
	}

	/**
	 * 删除消息
	 * 
	 * @param msgId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delMsg", method = RequestMethod.POST)
	@ApiOperation(value = "删除消息", notes = "删除消息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "msgId", value = "消息ID", dataType = "Integer", paramType = "query") })
	public ResponseResult delMsg(Integer msgId) throws Exception {
		return relationService.delMsg(msgId);
	}

	/**
	 * 清空所有消息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/allDelMsg", method = RequestMethod.POST)
	@ApiOperation(value = "删除消息", notes = "删除消息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Integer", paramType = "query") })
	public ResponseResult allDelMsg(Integer userId) throws Exception {
		return relationService.allDelMsg(userId);
	}

	/**
	 * deleteAssociatedFriends 根据mac地址查询平板关联的无绳子机（暂时不用）
	 * 
	 * @param macAddress
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/selectProRelationNoRopeMachine", method = RequestMethod.POST)
//	@ApiOperation(value = "根据mac地址查询平板关联的无绳子机（暂时不用）", notes = "根据mac地址查询平板关联的无绳子机（暂时不用）")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "macAddress", value = "mac地址", dataType = "String", paramType = "query") })
//	public ResponseResult selectProRelationNoRopeMachine(String macAddress) throws Exception {
//		// 非空判断传入信息   
//		if (macAddress == null) { 这个时候你就要化身 奥斯卡演帝 去要微信 说 是你的迷弟
//			return ResponseResult.failure(ResultCode.EQUIPMENT_PROID_NULL);
//		}   
//		return relationService.selectProRelationNoRopeMachine(macAddress);
//	}
//	
	/**
	 * 根据userId查询设备列表（手机端查看设备列表）
	 * 
	 * @param proId
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/selectRelationByProId_phone", method = RequestMethod.POST)
//	@ApiOperation(value = "根据userId查询设备列表（手机端查看设备列表）", notes = "根据userId查询设备列表（手机端查看设备列表）")
//	@ApiImplicitParams({  
//			@ApiImplicitParam(name = "userId", value = "用户id", dataType = "int", paramType = "query") })
//	public ResponseResult selectRelationByProId_phone(Integer userId) throws Exception {
//		// 非空判断传入信息 
//		if (userId == null) {
//			return ResponseResult.failure(ResultCode.USERID_NULL);
//		} 
//		return relationService.selectRelationByProId_phone(userId);
//	}
}
