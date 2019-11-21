package com.aura.springboot.vo.equipment;

public class EquipmentModiyTableStausVo {
	private int proId; // 设备关联Id
	private int accType; // 平板型号
	private String imConnect; // IM关联

	public int getProId() {
		return proId;
	}

	public void setProId(int proId) {
		this.proId = proId;
	}

	public int getAccType() {
		return accType;
	}

	public void setAccType(int accType) {
		this.accType = accType;
	}

	public String getImConnect() {
		return imConnect;
	}

	public void setImConnect(String imConnect) {
		this.imConnect = imConnect;
	}

	public EquipmentModiyTableStausVo(int proId, int accType, String imConnect) {
		super();
		this.proId = proId;
		this.accType = accType;
		this.imConnect = imConnect;
	}

	public EquipmentModiyTableStausVo() {
		super();
	}

}
