package com.github.surpassm.aliyun.exception;

import lombok.Getter;

/**
 * @author mc
 * Create date 2019/9/11 15:30
 * Version 1.0
 * Description 统一返回的异常信息的格式
 */
public class ExceptionResponseEntity {
	@Getter
	private int code;
	@Getter
	private String message;

	public ExceptionResponseEntity() {

	}

	public ExceptionResponseEntity(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
