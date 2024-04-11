package com.saysimple.users.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    Environment env;

    @Autowired
    public FeignErrorDecoder(Environment env) {
        this.env = env;
    }

    @Override
    public Exception decode(String methodKey, Response res) {
        switch (res.status()) {
            case 400:
                break;
            case 404:
                if (methodKey.contains("get")) {
                    return new ResponseStatusException(HttpStatus.valueOf(res.status()),
                            env.getProperty("orders.exception.orders_is_empty"));
                }
                break;
            default:
                return new Exception(res.reason());
        }
        return null;
    }
}
