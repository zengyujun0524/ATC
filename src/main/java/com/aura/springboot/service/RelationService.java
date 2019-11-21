package com.aura.springboot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.Constant;
import com.aura.springboot.entity.Equipment;
import com.aura.springboot.entity.Friend;
import com.aura.springboot.entity.Relation;
import com.aura.springboot.entity.User;
import com.aura.springboot.mapper.EquipmentMapper;
import com.aura.springboot.mapper.FriendMapper;
import com.aura.springboot.mapper.RelationMapper;
import com.aura.springboot.mapper.UserMapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class RelationService {

	private static Logger log = LoggerFactory.getLogger(RelationService.class);
	@Autowired
	private FriendMapper friendsMapper;

	@Autowired
	private RelationMapper relationMapper;

	@Autowired
	private EquipmentMapper equipmentMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private EquipmentService equipmentService;

	@Autowired
	private Constant constant;
	/**
	 * 绑定设备
	 * 
	 * @param relation
	 * @return
	 * @throws Exception
	 */
//	@Transactional
//         
//	public ResponseResult binding(Relation relation) throws Exception {
//		//查询是否存在该平板id
//		Integer indexEq = equipmentMapper.selectEquById(relation.getProId());
//		if(indexEq < 1)
//			return ResponseResult.failure(ResultCode.EQUIPMENT_PROID_NULL);
//		//查询是否存在该手机id
//		User user = userMapper.selectUserById(relation.getUserId());
//		if(user == null)
//			return ResponseResult.failure(ResultCode.USERID_NULL);
//		selectCount(relation.getUserId(),0);
//		Relation result = relationMapper.selectCount(relation.getUserId(),0);
//		//如果传进的proId和userId能查到数据，证明已经绑定，不能再重复绑定
//		if(result != null)
//			return ResponseResult.failure(ResultCode.EQUIPMENT_UNDEVICEDUPLICATE);
//		relation.setRelationProId(num);
//		log.info("-----------------开始绑定---------------");
//		Integer index = relationMapper.binding(relation);
//		if(index > 0) {
//			return ResponseResult.success();
//		}
//		log.info("-------------绑定失败---------------");
//		return ResponseResult.failure(ResultCode.BINDING_ERR);
//	}

	/**
	 * 利用mac地址进行绑定
	 * 
	 * @param macAddress  mac地址
	 * @param userId      用户id
	 * @param bindingTime 绑定时间
	 * @return 其实 要
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult macBinding(String macAddress, Integer userId, String bindingTime) throws Exception {
		// 打印日志
		equipmentService.logPrintMac(macAddress);
		Equipment equipment = equipmentMapper.selectEquipmentByMacAddress(macAddress);
		Relation relation = new Relation();
		if (equipment == null)
			return ResponseResult.failure(ResultCode.MACADDRESS_NULL);
		// 查询是否存在该手机用户的id
		User user = userMapper.selectUserById(userId);
		if (user == null)
			return ResponseResult.failure(ResultCode.USERID_NULL);
		// 打印日志
		selectCount(userId);
		Relation result = relationMapper.selectCount(userId);
		// 如果传进的proId和userId能查到数据，证明已经绑定，不能再重复绑定
		if (result != null)
			return ResponseResult.failure(ResultCode.EQUIPMENT_UNDEVICEDUPLICATE);

		// 查出已经绑定的手机的序号（11-99）
		List<Integer> list = relationMapper.numList(macAddress);
		log.info("--------------正在绑定---------------");
		Integer num = null;
		int j = 10;
		if (null == list || list.size() < 1) {
			num = 10;
			log.info("-------绑定的序号为--------------》-" + j);
		} else {
			// 将list数据传给arr 28627
			int[] arr = new int[list.size() + 1];
			for (int i = 0; i < list.size(); i++) {
				arr[i] = list.get(i);
			}

			// 如果手机绑定的数据超过了99 则提示手机用户绑定超过最大值
			if (list.get(list.size() - 1) > 99) {
				return ResponseResult.failure(ResultCode.BINDINGPHONE_MAXOVERFLOW);
			}
			// 循环遍历已经使用的序号，找出未使用的序号，赋值给绑定列表
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] != j) {
					log.info("-------绑定的序号为--------------》-" + j);
					num = j;
					break;
				}
				j++;
			}
		}
		relation.setRelationProId(num);
		relation.setProId(equipment.getProId());
		relation.setBindingTime(bindingTime);
		relation.setUserId(userId);
		log.info("-----------------开始绑定---------------");
		log.info("insert into relation\r\n" + "		(proId,userId,bindingTime,bindingStatus,relationProId)\r\n"
				+ "		values(" + relation.getProId() + "," + relation.getUserId() + "," + relation.getBindingTime()
				+ ",0," + num + ");");
		Integer index = relationMapper.binding(relation);
		if (index > 0) {
			log.info("--------------绑定成功---------------");
			User userInfo = userMapper.selectUserByMacAddress(macAddress);
			// 推送消息
			userService.pushInfo(userInfo.getImConnect(), 3, 1088);
			log.info("--------------绑定成功---------imc--" + userInfo.getImConnect() + "----");
			return ResponseResult.success();
		}
		log.info("-------------绑定失败---------------");
		return ResponseResult.failure(ResultCode.BINDING_ERR);
	}

	/**
	 * 解绑
	 * 
	 * @param userId 用户id
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult unBinding(Integer userId) throws Exception {
		log.info("-----------------开始解绑---------------");
		log.info("delete from relation\r\n" + "		where userId = " + userId + "");
		// 获取到用户信息
		User user = userMapper.selectUserById(userId);
		if (user != null) {
			// 确定用户的类型
			if (0 == user.getAccType()) { // 如果是手机用户
				String imc = user.getImConnect(); // 先获取到对应的im账号
				log.info("--------------解绑进行推送---------------");
				userService.pushInfo(imc, 3, 1088);
				Integer index = relationMapper.unBinding(userId); // 对用户进行解绑
				if (index > 0) {
					log.info("--------------解绑成功---------------");
					return ResponseResult.success();
				}
			} else if (1 == user.getAccType()) { // 如果是平板用户
				String imc = user.getImConnect(); // 先获取到对应的im账号
				Equipment eq = equipmentMapper.selectEquipmentByMacAddress(user.getMacAddress()); // 获取平板proid

				if (eq != null) {
					log.info("--------------解绑进行推送---------------");
					userService.pushInfo(imc, 3, 1088);
					Integer index = relationMapper.unBindingPro(eq.getProId()); // 对平板进行解绑
					if (index > 0) {
						log.info("--------------解绑成功---------------");
						return ResponseResult.success();
					}
				}

			}

		}
		log.info("-------------解绑失败---------------");
		return ResponseResult.failure(ResultCode.UNBINDING_ERR);
	}

	List<User> lists = null;

	/**
	 * 根据userId查询设备列表 (accType 0：手机、1：平板)
	 * 
	 * @param proId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectRelationByUserId(Integer userId, Integer accType) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		/*
		 * log.info("-----------------开始获取列表---------------"); log.info("select\r\n" +
		 * "		u.userId,u.username,u.`password`,u.phone,u.macAddress,u.email,u.birthday,u.sex,u.phone,u.accType,u.countryCode,u.imConnect,u.createTime,u.appTypeId,u.logStatus,u.callStatus\r\n"
		 * + "		from relation r,user u\r\n" + "		where r.userId = u.userId\r\n" +
		 * "		and r.bindingStatus = 0\r\n" +
		 * "		<if test=\"accType == 1\">\r\n" +
		 * "			and proId = (select proId from equipment where macAddress\r\n" +
		 * "			= (select\r\n" +
		 * "			macAddress from user where userId = " + userId + " ))\r\n" +
		 * "		</if>\r\n" + "		<if test=\"accType == 0\">\r\n" +
		 * "			and r.proId = (select r1.proId from relation r1 where\r\n" +
		 * "			r1.userId =\r\n" + "			" + userId + " and\r\n" +
		 * "			r1.bindingStatus = 0)\r\n" + "		</if>");
		 */
		List<User> list = relationMapper.selectRelationByUserId(userId, accType);

		log.info("-----------------SpringBoot配置值---------------" + constant.getServer());
		/*
		 * if (list !=null &&list.size() > 0) { // 给绑定列表设置序号 for (int i = 0; i <
		 * list.size(); i++) { list.get(i).setRelationProId(10+i); } }
		 */

		List<User> noRopeMachine = new ArrayList<User>();
		if (accType == 1) { // 如果是平板查看设备列表，则查出对应的信息
			User user = userMapper.selectUserById(userId); // 获取到平板信息
			noRopeMachine = userMapper.selectProRelationNoRopeMachine(user.getMacAddress());

			if (user != null) {
				user.setRelationProId(0); // 平板序号默认为0
				// 查询无绳子机
				noRopeMachine = userMapper.selectProRelationNoRopeMachine(user.getMacAddress());
				if (noRopeMachine.size() > 0) {
					// 给无绳子机设置序号
					for (int i = 0; i < noRopeMachine.size(); i++) {
//						Integer index = Integer.parseInt((userMapper.selectUserById(noRopeMachine.get(i).getUserId())).getPhone());
//						log.info("----------给无绳子机设置序号---------------》" + noRopeMachine.get(i).getUserId())).getPhone());
						if (noRopeMachine.get(i).getAccType() == 2) {
							noRopeMachine.get(i).setRelationProId(
									Integer.parseInt(noRopeMachine.get(i).getPhone().replace(" ", "")));
							noRopeMachine.get(i).setPhone(noRopeMachine.get(i).getPhone().replace(" ", ""));

						}
					}
				}
			}
			data.put("proInfo", user);
			data.put("noRopeMachine", noRopeMachine);
		} else { // 如果是手机查看设备列表，则查出对应的平板信息
			User user = relationMapper.selectProByUserId(userId);
			if (user != null) {
				user.setRelationProId(0); // 平板序号默认为0
				// 查询无绳子机
				noRopeMachine = userMapper.selectProRelationNoRopeMachine(user.getMacAddress());
				/*
				 * if (noRopeMachine.size() > 0) { // 给无绳子机设置序号 for (int i = 0; i <
				 * noRopeMachine.size(); i++) { Integer index =
				 * Integer.parseInt((userMapper.selectUserById(noRopeMachine.get(i).getUserId())
				 * ).getPhone()); log.info("----------给无绳子机设置序号---------------》" + index);
				 * noRopeMachine.get(i).setRelationProId(index); } }
				 */
				for (int i = 0; i < noRopeMachine.size(); i++) {
//					Integer index = Integer.parseInt((userMapper.selectUserById(noRopeMachine.get(i).getUserId())).getPhone());
//					log.info("----------给无绳子机设置序号---------------》" + noRopeMachine.get(i).getUserId())).getPhone());
					if (noRopeMachine.get(i).getAccType() == 2) {
						noRopeMachine.get(i)
								.setRelationProId(Integer.parseInt(noRopeMachine.get(i).getPhone().replace(" ", "")));
					}
				}
			}
			data.put("proInfo", user);
			data.put("noRopeMachine", noRopeMachine);
		}
		data.put("list", list);
		log.info("-----------------end 获取列表---------------");
		return ResponseResult.success(data);
	}

	/**
	 * 根据mac地址查询平板关联的无绳子机
	 * 
	 * @param proId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectProRelationNoRopeMachine(String macAddress) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		log.info("----------根据mac地址查询平板关联的无绳子机---------------");
		log.info("select\r\n"
				+ "		userId,username,password,phone,macAddress,email,birthday,sex,photo,accType,countryCode,imConnect,logStatus,appTypeId\r\n"
				+ "		from user where macAddress = " + macAddress + " and accType = 2");
		List<User> list = userMapper.selectProRelationNoRopeMachine(macAddress);
		if (list != null && list.size() > 0) {
			data.put("list", list);
			return ResponseResult.success(data);
		}
		return ResponseResult.failure(ResultCode.EQUIPMENT_LIST_FAIL);
	}

	/**
	 * 根据userId查出对应的平板信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * 
	 */
	@Transactional
	public ResponseResult selectProByUserId(Integer userId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		log.info("---------- 根据userId查出对应的平板信息 ---------------");
		User user = new User();
		user = relationMapper.selectProByUserId(userId);
		if (user != null) {
			user.setRelationProId(0);
			data.put("user", user);
			return ResponseResult.success(data);
		}
		return ResponseResult.failure(ResultCode.EQUI_NO_BINDING);
	}

	/**
	 * 添加亲友关系
	 * 
	 * 
	 * @param friend
	 * @return
	 */
	@Transactional
	public ResponseResult addFriendRequest(Friend friend) {
		log.info("------------添加亲友信息--------------》" + friend);
		Map<String, Object> data = new HashMap<String, Object>();
		try {

			if (friend.getMsgType() == 1) {
				Friend friend2 = friendsMapper.getFriend(friend.getUserId(), friend.getFriendsId());
				if (friend2 != null) {
					if (friend2.getIsDelete() != 1) {
						log.info("------------添加亲友信息2--------------》");
						if (friend2.getMsgType() == 1) {
							if (friend2.getMsgResult() == 0) {
								log.info("------------添加亲友信息2.1--------------》");
								return ResponseResult.success();
							}
						}
					}
				}
				/*
				 * 我就是 耐不住寂寞的 if (friend3 != null) {
				 * log.info("------------添加亲友信息3--------------》" + friend); if
				 * (friend3.getMsgResult() == 1 || friend3.getMsgType() == 1) { if
				 * (friend3.getMsgType() == 1) {
				 * 
				 * log.info("------------添加亲友信息2.1--------------》"); return
				 * ResponseResult.success();
				 * 
				 * } } }
				 */

			} else {
				Friend friend2 = friendsMapper.getFriend(friend.getUserId(), friend.getFriendsId());

				if (friend2 != null) {
					if (friend2.getIsDelete() != 1) {
						log.info("------------添加亲友信息2--------------》");
						if (friend2.getMsgResult() == 1 || friend2.getMsgResult() == 0) {
							if (friend2.getMsgType() != 1) {
								log.info("------------添加亲友信息3.1--------------》");
								return ResponseResult.success();

							}
						}
					}
				}
				Friend friend3 = friendsMapper.getFriend(friend.getFriendsId(), friend.getUserId());
				if (friend3 != null) {
					if (friend3.getIsDelete() != 1) {
						log.info("------------添加亲友信息3--------------》" + friend);
						if (friend3.getMsgResult() == 1) {
							if (friend3.getMsgType() != 1) {
								log.info("------------添加亲友信息3.1--------------》");
								return ResponseResult.success();
							}
						}
					}
				}
			}

			User user = userMapper.selectUserById(friend.getFriendsId());
			Long nowTime = System.currentTimeMillis();
			String s2 = String.valueOf(nowTime);
			/*
			 * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			 * String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
			 */ friend.setBindingTime(s2);
			friend.setFriendName(user.getUsername());
			friend.setFriendPhone(user.getPhone());
			log.info("------------user.getPhoto()--------------》" + user.getPhoto());
			friend.setFriendPhoto(user.getPhoto());
			int index = friendsMapper.addFriendRequest(friend);
			if (index > 0) {
				data.put("friend", friend);
				return ResponseResult.success(data);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseResult.failure(ResultCode.FRIEND_NULL);
	}

	/**
	 * 获取好友列表
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public ResponseResult getFriendList(int userId, int type, int index) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		log.info("------------获取好友列表--------------------");
		try {
			if (type == 1) {
				log.info("--------------受邀------------------");
				List<Friend> list = friendsMapper.getFriendList(userId, type);
				/*
				 * 
				 * User user = userMapper.selectUserById(list.get(0).getUserId()); Friend friend
				 * = new Friend(); friend.setFriendName(user.getUsername());
				 * friend.setPhoto(user.getPhoto()); friend.setFriendPhone(user.getPhone());
				 * friend.setMsgId(list.get(0).getMsgId());
				 *
				 *
				 * List<Friend> list2 = new ArrayList(); list2.add(friend);
				 */
				data.put("list", list);
			} else {
				List<Friend> list = friendsMapper.getFriendList(userId, type);
				data.put("list", list);
				if (index == 1) {
					boolean isResult = friendsMapper.updateProcessingResults(userId, 1, 1, 1);
					if (isResult) {
						log.info("------------已读消息--------------------");
					}
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return ResponseResult.success(data);
	}

	/**
	 * 获取好友列表
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public ResponseResult getFriendList2(int userId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();

		log.info("------------获取好友列表--------------------");
		try {
			List<Friend> list = friendsMapper.getFriendList2(userId);
			/*
			 * log.info("------------获取好友列表--------------------"); List<Friend> list2 = new
			 * ArrayList<>(); for (int i = 0; list.size() > i; i++) {
			 * log.info("------------进入循环--------------------" + i); User user =
			 * userMapper.selectUserById(list.get(i).getUserId()); Friend friend = new
			 * Friend(); friend.setFriendName(user.getUsername());
			 * friend.setPhoto(user.getPhoto()); friend.setFriendPhone(user.getPhone());
			 * list2.add(friend); }
			 */
			data.put("list", list);
//			if (type == 1) {  
//				log.info("--------------受邀------------------");
//				List<Friend> list = friendsMapper.getFriendList(userId, type);
//				User user = userMapper.selectUserById(list.get(0).getUserId());
//				Friend friend = new Friend();
//				friend.setFriendName(user.getUsername());
//				friend.setPhoto(user.getPhoto());
//				friend.setFriendPhone(user.getPhone());
//				List<Friend> list2 = new ArrayList();
//				list.add(friend);  
//				data.put("list", list2);
//			} else {
//				List<Friend> list = friendsMapper.getFriendList(userId, type);
//				data.put("list", list);
//				boolean isResult = friendsMapper.updateProcessingResults(userId, 1, 1, 1);
//				if (isResult) {
//					log.info("------------已读消息--------------------");
//				}
//			} 

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return ResponseResult.success(data);
	}

	/**
	 * 更新结果状态
	 * 
	 * @param msgId
	 * @param treat
	 * @return
	 * @throws Exception
	 */
	public ResponseResult updateProcessingResults(int msgId, int msgResult, int msgType) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		log.info("------------获取好友列表--------------------msgId " + msgId + "treat " + msgResult);
		boolean isResult = false;
		try {
			Friend friend = friendsMapper.selectFriendByMsgId(msgId);

			if (friend != null) {

				if (friend.getMsgResult() != 3) {

					log.info("--------------friend有值------------------");
					// 通过ID值去查找最新 买毛毛
					Friend friend2 = friendsMapper.getFriend(friend.getUserId(), friend.getFriendsId());
					if (friend2 != null) {
						if (friend2.getMsgResult() == 1 && friend2.getMsgType() != 1) {

							log.info("--------------friend2 查到了------------------");
							isResult = friendsMapper.updateProcessingResults(msgId, 3, 0, msgType);
							if (isResult) {
								return ResponseResult.success();
							}

						}
					}
					Friend friend3 = friendsMapper.getFriend(friend.getFriendsId(), friend.getUserId());
					if (friend3 != null) {
						if (friend3.getMsgResult() == 1 && friend3.getMsgType() != 1) {
							log.info("--------------friend3 查到了------------------");
							isResult = friendsMapper.updateProcessingResults(msgId, 3, 0, msgType);
							if (isResult) {
								return ResponseResult.success();
							}
						}
					}
				}
			}

			isResult = friendsMapper.updateProcessingResults(msgId, msgResult, 0, msgType);
			if (isResult) {
				return ResponseResult.success();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return ResponseResult.failure(ResultCode.FRIEND_ERR);
	}

	/**
	 * 
	 * 删除好友关系
	 * 
	 * @param userId
	 * @param friendsId
	 * @return
	 * @throws Exception
	 */
	public ResponseResult deleteAssociatedFriends(int msgId) throws Exception {
		log.info("------------删除好友关系--------------------msgId " + msgId);
		boolean isResult = false;
		try {
			isResult = friendsMapper.deleteAssociatedFriends(msgId);
			if (isResult) {
				return ResponseResult.success();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return ResponseResult.failure(ResultCode.FRIEND_DEL_ERR);
	}

	/**
	 * 验证
	 * 
	 * @param userId
	 * @param friendsId
	 * @return
	 * @throws Exception
	 */
	public ResponseResult aidedVerification(int userId, int friendsId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			Friend friend = friendsMapper.aidedVerification(userId, friendsId);
			if (friend != null) {
				data.put("msgResult", friend.getMsgResult());
				return ResponseResult.success(data);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseResult.failure(ResultCode.FRIEND_VER);
	}

	/**
	 * 删除消息
	 * 
	 * @param msgId
	 * @return
	 * @throws Exception
	 */
	public ResponseResult delMsg(int msgId) throws Exception {
		boolean isResult = false;
		try {
			isResult = friendsMapper.delMsg(msgId);
			if (isResult) {
				log.info("------------删除消息-------------------- ");
				return ResponseResult.success();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return ResponseResult.failure(ResultCode.MSG_ERR);
	}

	/**
	 * 清空所以消息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public ResponseResult allDelMsg(int userId) throws Exception {
		boolean isResult = false;
		try {
			isResult = friendsMapper.allDelMsg(userId);
			if (isResult) {
				log.info("------------删除所以消息-------------------- ");
				return ResponseResult.success();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return ResponseResult.failure(ResultCode.MSG_ERR);
	}

	/**
	 * 
	 * 根据userId查询设备列表（手机端查看设备列表）
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
//	@Transactional 
//	public ResponseResult selectRelationByProId_phone(Integer userId) throws Exception {
//		log.info("-----------------开始获取列表---------------");
//		log.info("select\r\n" +   
//				"		u.userId,u.username,u.`password`,u.phone,u.macAddress,u.email,u.birthday,u.sex,u.phone,u.accType,u.countryCode,u.imConnect,u.createTime,u.appTypeId,u.logStatus,u.callStatus\r\n" + 
//				"		from relation r,user u\r\n" + 
//				"		where r.userId = u.userId and r.bindingStatus = 0 and proId = (select r1.proId from relation r1 where r1.userId = #{"+userId+"} and r1.bindingStatus = 0)");
//		List<User> list = relationMapper.selectRelationByProId_phone(userId);
//		if(list != null && list.size() > 0) {
//			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("user", list); 
//			return ResponseResult.success(data);  
//		}    
//        
//		return ResponseResult.failure(ResultCode.EQUIPMENT_LIST_FAIL);
//	} 
	/**
	 * 日志：查看是否已绑定信息
	 * 
	 * @param userId
	 * @param bindingStatus
	 */
	public void selectCount(Integer userId) {
		log.info("-----------------查看是否已绑定信息---------------");
		log.info("select relationId,proId,userId,bindingTime,\r\n"
				+ "		bindingStatus,relationProId from relation\r\n" + "		where userId = " + userId);
	}

}
