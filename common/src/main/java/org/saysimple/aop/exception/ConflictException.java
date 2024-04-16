package org.saysimple.aop.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException{

    public ConflictException(String message) {
        super(message);
    }
}
