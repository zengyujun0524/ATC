package com.aura.springboot.utils;

public enum SysErrorCode {

	/**
	 * 系统运行时异常
	 */
	SYSTEM_ERROR(-1, "系统异常,请稍候再试"),
	/**
	 * 成功状态码 
	 */
	HTTPSTATUS_SUCCESS(200, "成功"),
    /**
     * 400	请求出现语法错误(注意是否参数类型有误)
     */
	HTTPSTATUS_400(400, "请求出现语法错误(注意是否参数类型有误)"),
	/**
	 * 403	禁止/服务器拒绝请求
	 */
	HTTPSTATUS_403(403, "禁止/服务器拒绝请求"),
    
    ;
    

	/**
	 * 初始化构造
	 */
	private Integer msgCode;

    private String message;

    SysErrorCode(Integer msgCode, String message) {
        this.msgCode = msgCode;
        this.message = message;
    }

    public Integer msgCode() {
        return this.msgCode;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (SysErrorCode item : SysErrorCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (SysErrorCode item : SysErrorCode.values()) {
            if (item.name().equals(name)) {
                return item.msgCode;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
    
    
}
