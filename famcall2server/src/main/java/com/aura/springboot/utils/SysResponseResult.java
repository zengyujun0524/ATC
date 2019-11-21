package com.aura.springboot.utils;
/**
 * 
 * ClassName: SysResponseResult
 * (全局自定义系统错误--HttpStatus).
 * @author Mccree
 * @version
 */
public class SysResponseResult{
	
	/**
     * 
     * failure:(执行失败,无数据返回,返回错误信息).
     * @return
     */
    public static  ResponseResult failure(SysErrorCode SysErrorCode) {
    	ResponseResult result = new ResponseResult();
        result.setSysErrorCode(SysErrorCode);
        return result;
    }
    
    /**
     * 
     * failure:(执行失败,有数据返回,返回错误信息).
     * @return
     */
    public static  ResponseResult failure(SysErrorCode SysErrorCode, Object data) {
    	ResponseResult result = new ResponseResult();
        result.setSysErrorCode(SysErrorCode);
        result.setData(data);
        return result;
    }
    
}
