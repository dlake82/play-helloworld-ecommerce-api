package org.saysimple.aop.exception;

import lombok.Getter;

@Getter
public class UnAuthorizationException extends RuntimeException{

    public UnAuthorizationException(String message) {
        super(message);
    }
}
