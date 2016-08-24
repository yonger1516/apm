package com.iyonger.apm.web.dto;

/**
 * Created by fuyong on 12/15/15.
 */
public class ConfigResult {
	boolean success;
	String message;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
