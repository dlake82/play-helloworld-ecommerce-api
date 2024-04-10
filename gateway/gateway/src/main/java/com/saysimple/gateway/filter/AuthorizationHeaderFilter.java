package com.saysimple.gateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Objects;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    // login -> token -> users (with token) -> header(include token)
    @Override
    public GatewayFilter apply(Config config) {
        return ((ex, chain) -> {
            ServerHttpRequest req = ex.getRequest();

            if (!req.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(ex, "no authorization header");
            }

            String authorizationHeader = Objects.requireNonNull(req.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isJwtValid(jwt)) {
                return onError(ex, "JWT is not valid");
            }

            return chain.filter(ex);
        });
    }

    private boolean isJwtValid(String jwt) {
        try {
            byte[] secretKeyBytes = Base64.getEncoder().encode(Objects.requireNonNull(env.getProperty("token.secret")).getBytes());
            SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

            String subject = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
            return subject != null && !subject.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private Mono<Void> onError(ServerWebExchange ex, String err) {
        ServerHttpResponse res = ex.getResponse();
        res.setStatusCode(HttpStatus.UNAUTHORIZED);
        log.error(err);
        return res.setComplete();
    }

    public static class Config {

    }
}
