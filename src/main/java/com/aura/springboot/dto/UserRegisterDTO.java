package com.aura.springboot.dto;

/**
 * 用户信息DTO.
 * 
 * @author Jack Jiang(http://www.52im.net/space-uid-1.html)
 * @version 1.0
 */
public class UserRegisterDTO implements java.io.Serializable
{
	
//	/** 修改密码时 输入的当前密码与登录密码不一致*/
//	public static final int UPDATE_PSW_ERROR_OLD_NOT_EQ_LOGIN_PSW = -1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user_uid, user_mail, nickname, user_psw, user_sex;

	public String getUser_uid()
	{
		return user_uid;
	}

	public void setUser_uid(String user_uid)
	{
		this.user_uid = user_uid;
	}

	public String getUser_mail()
	{
		return user_mail;
	}

	public void setUser_mail(String user_mail)
	{
		this.user_mail = user_mail;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getUser_psw()
	{
		return user_psw;
	}

	public void setUser_psw(String user_psw)
	{
		this.user_psw = user_psw;
	}

	public String getUser_sex()
	{
		return user_sex;
	}

	public void setUser_sex(String user_sex)
	{
		this.user_sex = user_sex;
	}
}
