package com.aura.springboot.dto;

import java.io.Serializable;

/**
 * <p>
 * 为了简化系统逻辑，本对象是EVA.EPC框架发送给服务端的唯一对像类型，所以要发送
 * 给服务器的对象都必须是此类（及其子类）的实例.<br>
 * 
 * 关于整个MVC框架捕获和处理客户端请求的流程，请参见：{@link com.eva.framework.HttpController}.
 * </p>
 * 
 * @author Jack Jiang(http://www.52im.net/space-uid-1.html)
 * @version 1.0
 */
public class DataFromClient implements Serializable
{
	private static final long serialVersionUID = 336408924788567851L;

	/** 
	 * 客户端是否需要读取服务端返回的数据（对服务端而言就是是否需要写返回写据给客户端
	 * ，服务端将据此决定是否要写回数据），本字段对应于 HttpURLConnection.setDoInput(boolean)
	 * 并与之保持一致。
	 * 注：本字段仅用于底层数据通信，请勿作其它用途！ */
	protected boolean doInput = true;
	
	/** 业务处理器id
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	protected int processorId = -9999999;
	/** 作业调度器id 
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	protected int jobDispatchId = -9999999;
	/** 动作id 
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	protected int actionId = -9999999;
	
	/** 具体业务中：客端发送过来的本次修改新数据(可能为空，理论上与oldData不会同时空）*/
	protected Object newData;
	/** 具体业务中：客端发送过来的本次修改前的老数据(可能为空，理论上与newData不会同时空）*/
	protected Object oldData;
	
	/** 
	 * 可用于REST请求时客户端携带到服务端作为身份验证之用。
	 * <p>
	 * 本字段可由框架使用者按需使用，非EVA框架必须的。
	 * 
	 * @since 20170223
	 */
	protected String token = null;
	
	/**
	 * 发起HTTP请求的设备类型（默认值为-1，表示未定义）.
	 * 此字段为保留字段，具体意义由开发者可自行决定。
	 * <p>
	 * 当前默认的约定是：0 android平台、1 ios平台、2 web平台。
	 */
	protected int device = -1;
	
	/** 一个方便的短方法名的新建本实例的类方法 */
	public static DataFromClient n()
	{
		return new DataFromClient();
	}
	
	/**
	 * 获得具体业务中，客端发送过来的本次修改新数据(可能为空，理论上与oldData不会同时空）.
	 * 
	 * @return
	 */
	public Object getNewData()
	{
		return newData;
	}
	/**
	 * 设置具体业务中，客端发送过来的本次修改新数据(可能为空，理论上与oldData不会同时空）.
	 * 
	 * @param newData
	 * @return
	 */
	public DataFromClient setNewData(Object newData)
	{
		this.newData = newData;
		return this;
	}
	
	/**
	 * 获得具体业务中，客端发送过来的本次修改前的老数据(可能为空，理论上与newData不会同时空）.
	 * 
	 * @return
	 */
	public Object getOldData()
	{
		return oldData;
	}
	/**
	 * 设置具体业务中，客端发送过来的本次修改前的老数据(可能为空，理论上与newData不会同时空）.
	 * 
	 * @param oldData
	 * @return
	 */
	public DataFromClient setOldData(Object oldData)
	{
		this.oldData = oldData;
		return this;
	}
	
	/** 业务处理器id
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	public int getProcessorId()
	{
		return processorId;
	}
	/** 业务处理器id
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	public DataFromClient setProcessorId(int processorId)
	{
		this.processorId = processorId;
		return this;
	}
	
	/** 作业调度器id 
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	public int getJobDispatchId()
	{
		return jobDispatchId;
	}
	/** 作业调度器id 
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	public DataFromClient setJobDispatchId(int jobDispatchId)
	{
		this.jobDispatchId = jobDispatchId;
		return this;
	}
	
	/** 动作id 
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	public int getActionId()
	{
		return actionId;
	}
	/** 动作id 
	 * @see  com.eva.framework.HttpController.ends.core.Controller */
	public DataFromClient setActionId(int actionId)
	{
		this.actionId = actionId;
		return this;
	}

	/** doInput字段用于底层数据通信时，请勿操作本方法！ */
	public boolean isDoInput()
	{
		return doInput;
	}
	/** doInput字段用于底层数据通信时，请勿操作本方法！ */
	public void setDoInput(boolean doInput)
	{
		this.doInput = doInput;
	}

	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}

	public int getDevice()
	{
		return device;
	}
	public void setDevice(int device)
	{
		this.device = device;
	}
}
