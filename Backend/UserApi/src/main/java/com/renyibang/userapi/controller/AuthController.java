package com.renyibang.userapi.controller;

import com.renyibang.global.util.Response;
import com.renyibang.userapi.dto.Login;
import com.renyibang.userapi.dto.Register;
import com.renyibang.userapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class AuthController {
  @Autowired private AuthService authService;

  @PostMapping("/login")
  public Response login(@RequestBody Login login) {
    return authService.login(login);
  }

  @PostMapping("/register")
  public Response register(@RequestBody Register register) {
    return authService.register(register);
  }
}
