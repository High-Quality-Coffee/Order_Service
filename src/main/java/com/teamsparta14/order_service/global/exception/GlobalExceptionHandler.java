package com.teamsparta14.order_service.global.exception;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.global.response.ResponseCode;
import org.hibernate.MappingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 데이터 무결성 예외 처리 (중복된 이메일, 유저명 등)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.fail(ResponseCode.USER_ALREADY_EXIST,"이미 존재하는 회원입니다."));
    }

    // ✅ 모델 매핑 오류 처리
    @ExceptionHandler(MappingException.class)
    public ResponseEntity<ApiResponse<String>> handleMappingException(MappingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ResponseCode.BAD_REQUEST,"회원 정보 매핑 중 오류 발생"));
    }


    //기본 예외처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<String>> handleBaseException(BaseException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(ApiResponse.fail(ex.getErrorCode(), ex.getMessage()));
    }

    // ✅ 모든 예외에 대한 처리 (최후의 방어)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(ResponseCode.INTERNAL_SERVER_ERROR ,"서버 오류가 발생했습니다."));
    }


}