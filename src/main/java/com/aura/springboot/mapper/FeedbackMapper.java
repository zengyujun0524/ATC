package com.aura.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aura.springboot.entity.Feedback;

@Mapper
public interface FeedbackMapper {
	
	//反馈信息
	int insertFeedback(Feedback feedback);
}
