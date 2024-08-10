package com.renyibang.userapi.service;

import com.renyibang.global.util.Response;
import com.renyibang.userapi.dto.Login;
import com.renyibang.userapi.dto.Register;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
  Response login(Login login);

  Response register(Register register);
}
