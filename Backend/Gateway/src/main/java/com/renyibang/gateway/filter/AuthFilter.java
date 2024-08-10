package com.renyibang.gateway.filter;

import com.renyibang.global.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();
    if (request.getURI().getPath().equals("/api/user/login")
      || request.getURI().getPath().equals("/api/user/register"))
      return chain.filter(exchange);
    HttpHeaders headers = request.getHeaders();
    try {
      String jwt = headers.getFirst("jwt");
      if (jwt == null || jwt.isEmpty()) throw new Exception();
      String userId = JwtUtil.parse(jwt);
      if (userId == null || userId.isEmpty()) throw new Exception();
      ServerHttpRequest mutatedRequest = request.mutate().header("userId", userId).build();
      return chain.filter(exchange.mutate().request(mutatedRequest).build());
    } catch (Exception e) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return response.setComplete();
    }
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
