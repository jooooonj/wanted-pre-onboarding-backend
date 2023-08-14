package com.wanted.boardAPI.global.exception;

import com.wanted.boardAPI.domain.member.exception.MemberDuplicateEmailException;
import com.wanted.boardAPI.domain.member.exception.MemberNotFoundException;
import com.wanted.boardAPI.domain.member.exception.MemberPasswordNotCorrectException;
import com.wanted.boardAPI.domain.post.exception.PostAccessFailException;
import com.wanted.boardAPI.domain.post.exception.PostNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {


    //MEMBER
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> MemberNotfoundExceptionHandler(MemberNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MemberDuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> MemberDuplicateEmailExceptionHandler(MemberDuplicateEmailException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MemberPasswordNotCorrectException.class)
    public ResponseEntity<ErrorResponse> MemberPasswordNotCorrectExceptionHandler(MemberPasswordNotCorrectException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    //POST
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> PostNotFoundExceptionHandler(PostNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(PostAccessFailException.class)
    public ResponseEntity<ErrorResponse> PostAccessFailExceptionHandler(PostAccessFailException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.REQUEST_VALID_FAIL.getCode()
                ,e.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
