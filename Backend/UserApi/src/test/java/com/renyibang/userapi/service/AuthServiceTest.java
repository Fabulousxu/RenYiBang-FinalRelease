package com.renyibang.userapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.renyibang.global.util.Response;
import com.renyibang.userapi.dao.AuthRepository;
import com.renyibang.userapi.dao.UserRepository;
import com.renyibang.userapi.dto.Login;
import com.renyibang.userapi.dto.Register;
import com.renyibang.userapi.entity.Auth;
import com.renyibang.userapi.entity.User;
import com.renyibang.userapi.service.serviceImpl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {
  @InjectMocks private AuthServiceImpl authService;
  @Mock private AuthRepository authRepository;
  @Mock private UserRepository userRepository;

  @Test
  public void loginTest1() {
    when(authRepository.existsById(anyLong())).thenReturn(true);
    when(authRepository.existsByUserIdAndPassword(anyLong(), anyString())).thenReturn(true);
    Login login = new Login();
    login.setPassword("1");
    Response response = authService.login(login);
  }

  @Test
  public void loginTest2() {
    when(authRepository.existsById(anyLong())).thenReturn(false);
    Response response = authService.login(new Login());
  }

  @Test
  public void loginTest3() {
    when(authRepository.existsById(anyLong())).thenReturn(true);
    when(authRepository.existsByUserIdAndPassword(anyLong(), anyString())).thenReturn(false);
    Response response = authService.login(new Login());
  }

  @Test
  public void registerTest() {
    when(userRepository.save(any(User.class))).thenReturn(new User());
    when(authRepository.save(any(Auth.class))).thenReturn(new Auth());
    Response response = authService.register(new Register());
  }
}
