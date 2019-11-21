package com.aura.springboot.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.Activation;
import com.aura.springboot.entity.Equipment;
import com.aura.springboot.mapper.ActivationMapper;
import com.aura.springboot.mapper.EquipmentMapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class ActivationService {

	private static Logger log = LoggerFactory.getLogger(ActivationService.class);

	@Autowired
	private ActivationMapper activationMapper;

	@Autowired
	private EquipmentMapper equipmentMapper;

	/**
	 * 激活平板设备
	 * 
	 * @param actCode 激活码
	 * @param proId   设备Id
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public ResponseResult updateActType(String actCode, Integer proId) throws Exception {
		Activation activation = activationMapper.selectAct(actCode);
		if (activation != null) {
			// 判断改激活码是否被使用
			if (activation.getActStatus() == 1) { // 1代表激活码已经被使用
				return ResponseResult.failure(ResultCode.ACTCODE_HAS_BEEN_USED);
			} else {
				Activation av = new Activation();
				av.setProId(proId);
				av.setActStatus(1);
				av.setActCode(actCode);
				Long actTime = System.currentTimeMillis();
				av.setActTime(actTime + "");
				Integer index = activationMapper.updateActType(av); // 更新激活码状态
				log.info("-----激活码为：---》--" + actCode + ",--激活平板设备--->" + proId);
				if (index > 0) {
					// 获取到期时长
					Equipment equi = equipmentMapper.selectEquiById(proId);
					String actEndTime = ""; // 设备的激活到期时间
					if ("0".equals(equi.getActEndTime())) { // 如果设备的到期时间为0，则是未激活状态
						actEndTime = (actTime + Long.parseLong("2592000000")) + ""; // 30 * 24 * 60 * 60 * 1000 30天的剩余时长
					} else { // 如果不是第一次激活，则累计时间
						actEndTime = (Long.parseLong(equi.getActEndTime()) + Long.parseLong("2592000000")) + ""; // 30 *
																													// 24
																													// *
																													// 60
																													// *
																													// 60
																													// *
																													// 1000
																													// 30天的剩余时长
					}
					// 修改平板激活信息
					Integer ind = equipmentMapper.updateActStatus(proId, 1, actEndTime);
					// carry修改子机激活信息
					log.info("-----激活平板设备剩余时长--->" + actEndTime);
					if (ind > 0) {
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("actEndTime", actEndTime);
						return ResponseResult.success(data);
					}
				}

				return ResponseResult.failure(ResultCode.ACT_FAILE); // 激活失败
			}
		} else {
			return ResponseResult.failure(ResultCode.ACTCODE_NULL);
		}
	}

}
