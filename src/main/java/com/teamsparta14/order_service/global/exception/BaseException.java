package com.teamsparta14.order_service.global.exception;
import com.teamsparta14.order_service.global.response.ResponseCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ResponseCode errorCode;

    // 기본 생성자에서는 일반적인 에러 코드를 사용
    public BaseException() {
        super(ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        this.errorCode = ResponseCode.INTERNAL_SERVER_ERROR;
    }

    // 에러 메시지를 받는 생성자
    public BaseException(String message) {
        super(message);
        this.errorCode = ResponseCode.INTERNAL_SERVER_ERROR;
    }

    // 에러 메시지와 원인을 받는 생성자
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ResponseCode.INTERNAL_SERVER_ERROR;
    }

    // 원인만을 받는 생성자
    public BaseException(Throwable cause) {
        super(cause);
        this.errorCode = ResponseCode.INTERNAL_SERVER_ERROR;
    }

    // 에러 코드를 지정하는 생성자
    public BaseException(ResponseCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 에러 코드와 메시지를 받는 생성자
    public BaseException(ResponseCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // 에러 코드, 메시지, 원인을 받는 생성자
    public BaseException(ResponseCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // 에러 코드와 원인을 받는 생성자
    public BaseException(ResponseCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

}
