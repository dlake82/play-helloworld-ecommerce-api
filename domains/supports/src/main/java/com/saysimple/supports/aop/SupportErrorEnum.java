package com.saysimple.supports.aop;

import lombok.Getter;

@Getter
public enum SupportErrorEnum {
    SUPPORT_NOT_FOUND("문의내역을 찾을 수 없습니다."),
    ;

    private final String msg;

    SupportErrorEnum(String msg) {
        this.msg = msg;
    }
}
