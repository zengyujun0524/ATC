package com.aura.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.History;
import com.aura.springboot.mapper.HistoryMapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class HistoryService {

	private static Logger log = LoggerFactory.getLogger(HistoryService.class);

	@Autowired
	private HistoryMapper historyMapper;

	/**
	 * 记录手机登录历史
	 * 
	 * @param history
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult insertHistory(History history) throws Exception {
		Integer index = historyMapper.insertHistory(history);
		log.info("-------------记录手机登录历史--------------");
		log.info("INSERT INTO history(userId,phoneModel,lastLogonTime,snNum,DeviceLock)\r\n" + "		VALUES("
				+ history.getUserId() + "," + history.getPhoneModel() + "," + history.getLastLogonTime() + ","
				+ history.getSnNum() + "," + history.getDeviceLock() + ");");
		if (index > 0) {
			log.info("-------------记录手机登录历史成功--------------");
			return ResponseResult.success();
		}
		return ResponseResult.failure(ResultCode.LOGHISTORY_FAIL);
	}

	/**
	 * 查看设备锁状态
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectDeviceLock(Integer userId) throws Exception {
		History hi = historyMapper.selectDeviceLock(userId);
		Map<String, Object> data = new HashMap<String, Object>();
		log.info("-------------查看设备锁状态--------------");
		log.info("SELECT historyId,userId,phoneModel,lastLogonTime,snNum,DeviceLock FROM history \r\n"
				+ "		WHERE userId = "+userId+"\r\n" + "		ORDER BY lastLogonTime DESC\r\n" + "		LIMIT 0,1");
		if (hi != null) {
			data.put("result", hi.getDeviceLock());
			return ResponseResult.success(data);
		}
		log.info("-----查看设备锁状态失败------");
		data.put("result", 0);
		return ResponseResult.success(data);
	}

	/**
	 * 修改设备锁状态
	 * 
	 * @param userId
	 * @param DeviceLock
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult updateDev(Integer userId, Integer DeviceLock) throws Exception {
		log.info("-------------修改设备锁状态--------------");
		History hi = historyMapper.selectDeviceLock(userId);
		if (hi != null) {
			Integer index = historyMapper.updateDev(hi.getHistoryId(), DeviceLock);
			log.info("update history set DeviceLock = " + DeviceLock + " where\r\n" + "		historyId = "
					+ hi.getHistoryId() + "");
			if (index > 0) {
				log.info("-------------修改设备锁状态成功--------------");
				return ResponseResult.success();
			}
		}
		log.info("-------------修改设备锁状态失败--------------");
		return ResponseResult.failure(ResultCode.DEVICELOCKUPDATE_FAIL);
	}

	/**
	 * 查看手机用户所有登录过的手机
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult selectHistoryByUserId(Integer userId) throws Exception {
		log.info("-------------查看手机用户所有登录过的手机--------------");
		List<History> hi = historyMapper.selectHistoryByUserId(userId);
		Map<String, Object> data = new HashMap<String,Object>();
		if (hi != null) {
			log.info("------------该用户有登录历史----------");
			data.put("history", hi);
			return ResponseResult.success(data);
		}
		log.info("------------该用户无登录历史----------");
		return ResponseResult.success();
	}
	
	/**
	 * 删除该历史记录
	 * @param historyId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult delHistoryById(Integer historyId) throws Exception {
		History hi = historyMapper.selectHistoryById(historyId);
		if(hi != null) {
			List<History> history = historyMapper.selectHistoryByUserId(hi.getUserId());
			if(history.size() > 1) {
				log.info("-------------删除该历史记录--------------");
				Integer index = historyMapper.delHistoryById(historyId);
				if (index > 0) {
					log.info("-------------删除该历史记录成功--------------");
					return ResponseResult.success();
				}
				log.info("-------------删除该历史记录失败--------------");
			}else {
				return ResponseResult.success();
			}
		}
		return ResponseResult.failure(ResultCode.DELHISTORY_FAIL);
	}
	
}
