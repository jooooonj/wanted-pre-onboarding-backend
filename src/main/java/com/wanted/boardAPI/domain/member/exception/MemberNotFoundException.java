package com.wanted.boardAPI.domain.member.exception;

import com.wanted.boardAPI.global.exception.BaseException;
import com.wanted.boardAPI.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends BaseException {
    private static final ErrorCode code = ErrorCode.MEMBER_NOT_FOUND;

    public MemberNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
