package com.wanted.boardAPI.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    REQUEST_VALID_FAIL(404, "REQEUST_VALID_FAIL", "Request 검증 실패, 에러 메시지 확인"),
    //MEMBER
    MEMBER_NOT_FOUND(404, "USER_NOT_FOUND", "회원을 찾을 수 없는 경우"),
    MEMBER_DUPLICATE_EMAIL(404, "MEMBER_DUPLICATE_EMIAL", "중복 이메일인 경우"),
    MEMBER_PASSWORD_NOT_CORRECT(404, "MEMBER_PASSWORD_NOT_CORRECT", "비밀번호가 일치하지 않는 경우"),

    //POST
    POST_NOT_FOUND(404, "POST_NOT_FOUND", "게시글을 찾을 수 없는 경우"),
    POST_ACCESS_FAIL(404, "POST_FORBIDDEN_ACCESS", "게시글에 접근 권한이 없는 경우");

    private final int status;
    private final String code;
    private final String description;

    //상태, 코드, 설명
    ErrorCode(int status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }


}
