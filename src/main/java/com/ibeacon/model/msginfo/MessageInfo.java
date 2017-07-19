package com.ibeacon.model.msginfo;

import org.json.JSONObject;

/**
 *
 *
 * @author xudan
 * @version 1.0, 2016-9-28
 */
public class MessageInfo {


	/**
	 * 错误码
	 * 0代表成功，非0代码失败
	 */
	private int code;

	private String msg;

	public MessageInfo(){
		code  = 0;
		msg = new String();
	}

	public MessageInfo(int code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public void reset(int code, String msg){
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString(){
		JSONObject json = new JSONObject();
		json.put("code", this.code);
		json.put("msg", this.msg);

		return json.toString();
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param message the message to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean error(){
		return this.code != 0;
	}

	public boolean success(){
		return this.code == 0;
	}
}
