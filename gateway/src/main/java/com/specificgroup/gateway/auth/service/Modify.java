package com.specificgroup.gateway.auth.service;

import java.util.List;
import java.util.Map;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class Modify implements GlobalFilter, Ordered {

//2023-11-21 23:29:02 [transfer-encoding:"chunked", Vary:"Origin", '
//        '"Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin",
//        "Access-Control-Request-Method", "Access-Control-Request-Headers",
//    Access-Control-Allow-Origin:"http://localhost:3000", "http://localhost:3000",
//    Content-Type:"application/json", Date:"Tue, 21 Nov 2023 20:29:02 GMT",
//    Cache-Control:"no-cache, no-store, max-age=0, must-revalidate",
//    Pragma:"no-cache", Expires:"0", X-Content-Type-Options:"nosniff",
//    X-Frame-Options:"DENY", X-XSS-Protection:"1 ; mode=block", Referrer-Policy:"no-referrer"]
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();

            System.out.println("Headers: ");
            System.out.println(headers);
            Map<String, String> stringStringMap = headers.toSingleValueMap();
            System.out.println("Strings: ");
            System.out.println(stringStringMap);
            headers.clear();
            headers.setAll(stringStringMap);
            System.out.println("Headers: ");
            System.out.println(headers);

        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
