package com.saysimple.supports.aop;

import lombok.Getter;

@Getter
public enum FaqErrorEnum {
    FAQ_NOT_FOUND("검색내용(FAQ)을 찾을 수 없습니다."),
    ;

    private final String msg;

    FaqErrorEnum(String msg) {
        this.msg = msg;
    }
}
