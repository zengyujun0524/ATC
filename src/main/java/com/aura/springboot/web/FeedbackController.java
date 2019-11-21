package com.aura.springboot.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aura.springboot.entity.Feedback;
import com.aura.springboot.service.FeedbackService;
import com.aura.springboot.service.UserService;
import com.aura.springboot.utils.ResponseResult;

import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 	反馈信息
 * 
 * @author Carry
 *
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

	private static Logger log = LoggerFactory.getLogger(FeedbackController.class);
	@Autowired
	private FeedbackService feedbackService;
	
	@RequestMapping(value = "/feedbackInfo", method = RequestMethod.POST)
	@ApiOperation(value = "反馈信息", notes = "反馈信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "phone", value = "手机号码", dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "email", value = "邮箱", dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "feedbackMsg", value = "返回信息", dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "feedbackTime", value = "反馈时间", dataType = "String", paramType = "query")
		})
	public ResponseResult feedbackInfo(String phone, String email, String feedbackMsg, String feedbackTime,HttpServletRequest request) throws Exception {
		//反馈信息
		Feedback feedback = new Feedback();
		feedback.setPhone(phone);
		feedback.setEmail(email);
		feedback.setFeedbackMsg(feedbackMsg);
		feedback.setFeedbackTime(feedbackTime);
		feedback.setType(0);
		feedback.setCompleteStatus(0);
		
		//插入图片
        FileUploadController fu = new FileUploadController();
        Map<String,Object> result = fu.uploadFile(request, email);
        
        //图片路径传给数据库
        if(result.get("result") != null) {
        	List<String> msgList = (List<String>) result.get("result");
        	for(int i = 0; i < msgList.size(); i++) {
        		log.info("----------------------->" + msgList.get(i));
        		if(i == 0)
        			feedback.setFeedbackPhoto(msgList.get(i));
        		if(i == 1)
        			feedback.setFeedbackPhoto2(msgList.get(i));
        		if(i == 2)
        			feedback.setFeedbackPhoto3(msgList.get(i));
        		if(i == 3)
        			feedback.setFeedbackPhoto4(msgList.get(i));
        	}
        }
        // 录入反馈信息
		return feedbackService.insertFeedback(feedback);
	}
	
	
    public List<String> saveFile(HttpServletRequest request,
            MultipartFile file, List<String> list,String email) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 保存的文件路径(如果用的是Tomcat服务器，文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
                // )
                String filePath = request.getSession().getServletContext()
                        .getRealPath("/")
                        + "upload/" + file.getOriginalFilename();
                log.info("---------------反馈图片存储的路径是------------》" + filePath);
                list.add(file.getOriginalFilename());
                File saveDir = new File(filePath);
                if (!saveDir.getParentFile().exists())
                    saveDir.getParentFile().mkdirs();

                // 转存文件
                file.transferTo(saveDir);
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
