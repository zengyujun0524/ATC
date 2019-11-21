package com.aura.springboot.utils;

/**
 * 状态返回码
 * 
 * @author Carry
 *
 */
public enum ResultCode {

	/*
	 * 参数错误：10001-19999
	 */
	SUCCESS(1024, "Success"), PARAMETER_INVALID(10001, "Invalid parameter"),
	PARAMETER_NULL(10002, "Parameter does not exist"), PARAMETER_TYPE_ERR(10003, "Incorrect parameter type"),
	PARAMETER_TYPE_LOSE(10004, "Miss parameter"), PARAMETER_NOMOREDATA(10005, "No more data"),

	/*
	 * 用户错误：20001-29999
	 */
	LOGIN_ERR(20001, "Login failed"), LOGIN_OUT_ERR(20002, "Logout failed"),
	ACCOUNT_PASSWORD_MISMATCH(20003, "Account and password do not match"),
	ACCOUNT_PROHIBIT(20004, "Account has been disabled"), ACCOUNT_NOTEXIST(20005, "User doesn't exist"),
	COUNTRYCODE_NULL(20006, "Country code doesn't exist"), MACADDRESS_NULL(20007, "MAC address doesn't exist"),
	ACCOUNT_TYPE_NULL(20008, "Account type doesn't exist"),
	REGISTERCODE_NULL(20009, "SMS verification code doesn't exist"),
	PHONE_NULL(20010, "The phone number doesn't exist"), PHONE_FORMAT_ERR(20011, "Incorrect phone number format"),
	EMAIL_FORMAT_ERR(20012, "Incorrect E-mail address"), ACCOUNT_EXIST(20013, "Username already exists"),
	PASSWORD_NULL(20014, "Password doesn't exist"), PASSWORD_ATYPISM(20015, "Inconsistent password "),
	VERIFICATIONCODE(20016, "Incorrect verification code"),
	VERIFICATIONCODE_OVERDUE(20017, "Verification code has expired"),
	PHONE_EXIST(20018, "The phone number already exists"), EMAIL_EXIST(20019, "The email address already exists"),
	PHONE_UP_FAIL(20020, "User failed to upload image"), OLDPASSWORD_ERR(20021, "Old password is incorrect. Try again"),
	NEWPASSWORD_CONFIRMPASSWORD_ATYPISM(20022, "New password and the confirmed password do not match"),
	UPDATEPASSWORD_FAIL(20023, "Failed to modify password"), REGISTER_FAIL(20024, "Failed to sign up"),
	PHONE_AND_MACADDRESS_MEANWHILENULL(20025, "Phone number and MAC address do not exist"),
	USERID_NULL(20026, "User ID doesn't exist"), NEWPASSWORD_NULL(20027, "New password doesn't exist"),
	PHONE_AND_MACADDRESS_MEANWHILEEXIST(20028, "Phone number and MAC address do exist"),
	MACADDRESS_EXIST(20029, "MAC address already exists"), APPTYPEID_NULL(20030, "Project type doesn't exist"),
	REGISTERPHONEANDSENDPHONE_DIFF(20031,
			"The registered phone number doesn't match the phone number that receive the SMS."),
	FEEDBACK_FAIL(20032, "Feedback failed."), PHONEMODEL_NULL(20033, "Mobile phone model doesn’t exist."),
	USER_LOGGED_IN(20034, "User is logged in."), LOGSTATUS_NULL(20035, "Login status doesn’t exist."),
	LOGHISTORY_FAIL(20036, "Failed to record mobile phone login history."), SNNUM_NULL(20037, "SN number is empty."),
	SECONDVALI_FAIL(20038, "Verification failed."), PUSHTOKEN_NULL(20039, "Offline token doesn’t exist."),
	UPDATEPUSHTOKEN_FAIL(20040, "Failed to update offline token."),
	IMCONNECT_NULL(20041, "User IM's UID doesn’t exist."), INDEX_NULL(20042, "Incoming call type doesn’t exist."),
	CODEO_OR_ACCOUNT_ERR(20043, "Please check if your area code or account is correct."),
	FRIEND_NULL(20044, "Relatives and friends do not exist"), SECURITY_NULL(20045, "Failed to insert security problem"),
	FRIEND_ERR(20045, "Friend update failed"), MSGID_NULL(20045, "Friend update failed"),
	FRIEND_DEL_ERR(20046, "Friend deletion failed"), FRIEND_VER(20047, "Secondary validation failed"),
	SECURITY_EIXT(20048, "Account entry error or account does not exist"),
	/*
	 * 设备管理：30001-39999
	 */
	UPDATE_ADMINPASSWORD_FAIL(30001, "Failed to modify device management password"),
	COUNTRYLIST_ERR(30002, "Country list request failed"), CITYLIST_ERR(30003, "City list request failed"),
	LOCATION_FAIL(30004, "Failed to find current location"), BINDING_ERR(30005, "Failed to bind device"),
	UNBINDING_ERR(30006, "Failed to unbind device"), SEEINFO_FAIL(30007, "Failed to view information"),
	UPDATEINFO_FAIL(30008, "Failed to modify information"), EQUIPMENT_LIST_FAIL(30009, "Failed to get device list"),
	EQUIPMENT_REGISTER_FAIL(30010, "Device registration failed"),
	EQUIPMENT_UPDATE_INFO_FAIL(30011, "Failed to update device information"),
	EQUIPMENT_UPDATE_ADMINCODE_FAIL(30012, "Failed to modify device management password"),
	EQUIPMENT_UPDATE_PROBLEM_FAIL(30013, "Failed to modify security question"),
	EQUIPMENT_UPDATE_PROPHONE_FAIL(30014, "Failed to modify password recovery phone number"),
	EQUIPMENT_UPDATE_LOCALPHONE_FAIL(30015, "Failed to modify Landline number"),
	EQUIPMENT_UPDATE_LOCALPHONE_NULL(30016, "Landline number doesn't exist"),
	EQUIPMENT_PROBLEM_NULL(30017, "Security question doesn't exist"),
	EQUIPMENT_ANSWER_NULL(30018, "Security answer doesn't exist"),
	EQUIPMENT_PROPHONE_NULL(30019, "Password recovery phone number doesn't exist"),
	EQUIPMENT_ADMINCODE_NULL(30020, "Device management password doesn't exist"),
	EQUIPMENT_PRONAME_NULL(30021, "Device name doesn't exist"),
	EQUIPMENT_PRONUM_NULL(30022, "Device model doesn't exist"),
	EQUIPMENT_PROSYSVERSION_NULL(30023, "Device system version (Android) doesn't exist"),
	EQUIPMENT_FIRMWARESYSVERSION_NULL(30024, "Firmware version doesn't exist"),
	EQUIPMENT_ADMINPWDSWITCH_NULL(30025, "Device management password button (0: on, 1: off) doesn't exist"),
	EQUIPMENT_PROVERSION_NULL(30026, "Device version doesn't exist"),
	EQUIPMENT_MACADDRESS_NOEXIST(30027, "MAC address doesn't exist"),
	EQUIPMENT_UPDATEOPERATOR_FAIL(30028, "Failed to change operator"),
	EQUIPMENT_SETLOCALPHONEANDPRONAME_FAIL(30029, "Failed to set Landline number and device name"),
	EQUIPMENT_UPDATEPRONAME_FAIL(30030, "Failed to modify device name"),
	EQUIPMENT_OPERATORID_NULL(30031, "Operator ID doesn't exist"),
	EQUIPMENT_UPDATEADMINCODESTATUS_FAIL(30032, "Failed to change device management password status"),
	EQUIPMENT_UNDEVICEDUPLICATE(30033, "Device cannot be bound repeatedly"),
	EQUIPMENT_PROID_NULL(30034, "Device ID doesn't exist"),
	DONOTDISTURBUPDATE_FAIL(30035, "Failed to change Whitelist status"),
	UNBLOCKUPDATE_FAIL(30036, "Failed to change Blacklist status"),
	UNBLOCK_NULL(30037, "Blacklist status doesn't exist"), DONOTDISTURB_NULL(30038, "Whitelist status doesn't exist"),
	DISTURBFROMTIME_NULL(30039, "Start time of Do-Not-Disturb mode doesn't exist"),
	DISTRUBTOTIME_NULL(30040, "End time of Do-Not Disturb mode doesn't exist"),
	CORDLESSMACHINEISBOUND(30041, "Cordless handset is bound."),
	BINDINGPHONE_MAXOVERFLOW(30042, "The number of bound mobile phone has exceeded the limit."),
	SELECTSBG_FAIL(30043, "Failed to get the Tablet’s structure diagram."),
	EQUI_NO_BINDING(30044, "Device is not bound."), EQUIUPDATE_FAIL(30045, "Failed to update Tablet status."),
	EQUISTATUS_NULL(30046, "Tablet status doesn’t exist."),
	DEVICELOCKUPDATE_FAIL(30047, "Failed to modify device lock."), DELHISTORY_FAIL(30048, "Failed to delete history."),
	DEVICELOCK_NULL(30049, "Device lock doesn’t exist."), ACTCODE_NULL(30050, "Activation code doesn’t exist."),
	ACTCODE_HAS_BEEN_USED(30051, "Activation code is already taken."),
	ACT_FAILE(30052, "Activation code does not exist."), ACTIVATION_CODE_EXPIRED(30053, "Activation code has expired."),
	DEVICE_DEVICETYPE_NULL(30054, "Equipment type does not exist"),
	DEVICE_IMAGETYPE_NULL(30055, "Device image type does not exist"),
	DISTURB_ERR(30056, "Failed to turn on undisturbed"), MSG_ERR(30057, "Message deletion failed"),
	/*
	 * IM服务:40001-49999
	 */
	IM_REGISTER_FAIL(40001, "IM registration failed"), IM_UPDATEPWD_FAIL(40002, "Failed to modify IM"),
	IM_UPDATEINFO_FAIL(40003, "Failed to modify IM information"),

	/*
	 * 文件异常：50001-59999
	 */
	UPLOAD_NULL(50001, "The uploaded file doesn't exist"), UPLOAD_FILE_FAIL(50002, "Failed to upload file"),
	DELETE_FAIL(50003, "Failed to delete"), FILE_NOEXIST(50004, "File doesn't exist"),

	/*
	 * 区域码：60001-69999
	 */
	PARENTID_NULL(60001, "Parent ID doesn't exist"), REGIONID_NULL(60002, "Area ID doesn't exist"),
	SETREGIONCODE_FAIL(60003, "Failed to set area code"), REGIONCODE_NULL(60004, "Area code doesn't exist"),

	/*
	 * 通话记录：70001-79999
	 */
	ADD_CALLACCOUNT_FAIL(70001, "Failed to store call log"), CALLACCOUNT_NULL(70002, "Call log doesn't exist"),
	CALLFROM_NULL(70003, "Device number doesn't exist"), CALLTYPE_NULL(70004, "Call type doesn't exist"),
	STARTTIME_NULL(70005, "Star time doesn't exist"), ENDTIME_NULL(70006, "End time doesn't exist"),

	/*
	 * 使用指南：80001-89999
	 */
	LANGUAGETYPE_NULL(80001, "Language doesn't exist"), SELECTUSERMANUAL_FAIL(80002, "Failed to get user manual"),

	/*
	 * APP：90001-99999
	 */
	APPVERSION_NULL(90001, "Version number doesn't exist"), APPPCGNAME_NOEXIST(90002, "APP包名不存在"),
	APPNAME_NOEXIST(90003, "Project name doesn’t exist.");
	/**
	 * 初始化构造
	 */
	private Integer msgCode;

	private String message;

	ResultCode(Integer msgCode, String message) {
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
		for (ResultCode item : ResultCode.values()) {
			if (item.name().equals(name)) {
				return item.message;
			}
		}
		return name;
	}

	public static Integer getCode(String name) {
		for (ResultCode item : ResultCode.values()) {
			if (item.name().equals(name)) {
				return item.msgCode;
			}
		}
		return null;
	}

	public Integer isSuccess() {

		return SUCCESS.msgCode;
	}

	@Override
	public String toString() {
		return this.name();
	}
}
