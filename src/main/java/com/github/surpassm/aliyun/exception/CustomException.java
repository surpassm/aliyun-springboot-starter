package com.github.surpassm.aliyun.exception;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author mc
 * Create date 2019/3/25 14:34
 * Version 1.0
 * Description 自定义异常类
 */
public class CustomException extends RuntimeException implements Serializable {

	@Getter
	private int code;

	@Getter
	private String message;

	public CustomException(String message) {
		super(message);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomException(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
