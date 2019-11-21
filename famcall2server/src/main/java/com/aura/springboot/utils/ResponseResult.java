/**
 * Project Name:cloudplatform-model
 * File Name:message.java
 * Package Name:org.cloudplatform.model.baseModel
 * Date:2018年5月15日上午10:48:26
 * Copyright (c) 2018, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.aura.springboot.utils;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * (API 统一返回格式数据).
 * @author Carry
 * @version @param 
 */
@ApiModel
public class ResponseResult implements Serializable {
	
	/**
	 * serialVersionUID:TODO(序列化).
	 */
	private static final long serialVersionUID = 1L;

	//默认成功状态码 msgCode
	@ApiModelProperty(value = "返回状态码；1024：成功；其余失败")
	public Integer msgCode;
	
	//默认成功message
	@ApiModelProperty(value = "返回的详细信息")
	public String message;
	
	//数据体
	@ApiModelProperty(value = "返回的具体数据")
	public Object data;
	
	public Integer getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(Integer msgCode) {
		this.msgCode = msgCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 
	 * 无参构造
	 *
	 */
	public ResponseResult() {}
	
	/**
	 * 
	 * 有参构造
	 *
	 * @param msgCode
	 * @param message
	 */
	public ResponseResult(Integer msgCode, String message) {
        this.msgCode = msgCode;
        this.message = message;
//        this.data = data;
    }

	/**
	 * 
	 * success:(执行成功,无数据返回).
	 * @return
	 */
    
	public static  ResponseResult success() {
    	ResponseResult result = new ResponseResult();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData("");
        return result;
    }
    
	   /**
     * 
     * success:(执行成功,有数据返回).
     * @param data
     * @return 
     * @return
     */
    public static ResponseResult success(Object data) {
    	ResponseResult result = new ResponseResult();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }
    
    /**
     * 
     * failure:(执行失败,无数据返回,返回错误信息).
     * @param resultCode
     * @return
     */
    public static  ResponseResult failure(ResultCode resultCode) {
    	ResponseResult result = new ResponseResult();
        result.setResultCode(resultCode);
        return result;
    }

    /**
     * 
     * failure:(执行失败,有数据返回,返回错误信息).
     * @param resultCode
     * @param data
     * @return
     */
    public static  ResponseResult failure(ResultCode resultCode, Object data) {
    	ResponseResult result = new ResponseResult();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }
    
    /**
     * 
     * failure:(系统运行时异常错误).
     * @param msgCode
     * @param data
     * @return
     */
    public static  ResponseResult failure(Integer msgCode,String message, Object data) {
    	ResponseResult result = new ResponseResult();
        result.setMsgCode(msgCode);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    /**
     * 
     * failure:(自定义异常).
     * @param message 自定义异常信息
     * @return
     */
    public static  ResponseResult failure(String message) {
    	ResponseResult result = new ResponseResult();
        result.setMsgCode(-1);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 
     * setResultCode:(解析参数ResultCode).
     * @param code
     */
    public void setResultCode(ResultCode code) {
        this.msgCode = code.msgCode();
        this.message = code.message();
    }
    
    /**
     * 
     * SysErrorCode:(解析参数SysErrorCode).
     * @param code
     */
    public void setSysErrorCode(SysErrorCode code) {
        this.msgCode = code.msgCode();
        this.message = code.message();
    }
    
}

