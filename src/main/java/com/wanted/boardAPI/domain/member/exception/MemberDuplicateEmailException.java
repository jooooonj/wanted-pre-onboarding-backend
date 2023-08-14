package com.wanted.boardAPI.domain.member.exception;

import com.wanted.boardAPI.global.exception.BaseException;
import com.wanted.boardAPI.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class MemberDuplicateEmailException extends BaseException {
    private static final ErrorCode code = ErrorCode.MEMBER_DUPLICATE_EMAIL;

    public MemberDuplicateEmailException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
