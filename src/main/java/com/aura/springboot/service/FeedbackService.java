package com.aura.springboot.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aura.springboot.entity.Feedback;
import com.aura.springboot.mapper.FeedbackMapper;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

@Service
public class FeedbackService {

	private static Logger log = LoggerFactory.getLogger(FeedbackService.class);
	
	@Autowired
	private FeedbackMapper feedbackMapper;
	
	/**
	 * 	反馈信息
	 * @param feedback
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public ResponseResult insertFeedback(Feedback feedback)
			throws Exception {
		log.info("------------> 录入反馈信息---------------");
		log.info("insert into feedback(phone,email,feedbackMsg,feedbackTime,feedbackPhoto,feedbackPhoto2,feedbackPhoto3,feedbackPhoto4,type,completeStatus)");
		log.info("values("+feedback.getPhone()+","+feedback.getEmail()+","+feedback.getFeedbackMsg()+","+feedback.getFeedbackTime()+","+feedback.getFeedbackPhoto()+","+feedback.getFeedbackPhoto2()+","+feedback.getFeedbackPhoto3()+","+feedback.getFeedbackPhoto4()+","+feedback.getType()+","+feedback.getCompleteStatus()+") ");
		Integer index = feedbackMapper.insertFeedback(feedback);
		if(index > 0) {
			log.info("------------> 反馈成功---------------");
			return ResponseResult.success();
		}
		log.info("------------> 反馈失败---------------");
		return ResponseResult.failure(ResultCode.FEEDBACK_FAIL);
	}
}
