package com.renyibang.gateway.controller;

import com.renyibang.global.util.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {
    @GetMapping("/health")
    public Response health() {
        return Response.success();
    }
}
