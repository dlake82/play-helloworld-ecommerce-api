package com.saysimple.supports.aop;

import lombok.Getter;

@Getter
public enum SupportErrorEnum {
    REVIEW_NOT_FOUND("리뷰를 찾을 수 없습니다."),
    ;

    private final String msg;

    SupportErrorEnum(String msg) {
        this.msg = msg;
    }
}
