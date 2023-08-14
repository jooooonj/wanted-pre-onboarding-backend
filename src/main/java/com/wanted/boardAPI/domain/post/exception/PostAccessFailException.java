package com.wanted.boardAPI.domain.post.exception;

import com.wanted.boardAPI.global.exception.BaseException;
import com.wanted.boardAPI.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class PostAccessFailException extends BaseException {
    private static final ErrorCode code = ErrorCode.MEMBER_NOT_FOUND;

    public PostAccessFailException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}

