package me.dfun.common.kit;

import java.io.Serializable;

/**
 * 响应结果
 */
public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code = 0;
	private String msg;
	private int count = 0;
	private Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}