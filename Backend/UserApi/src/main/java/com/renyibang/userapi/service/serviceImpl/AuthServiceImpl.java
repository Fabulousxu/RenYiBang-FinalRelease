package com.renyibang.userapi.service.serviceImpl;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.util.JwtUtil;
import com.renyibang.global.util.Response;
import com.renyibang.userapi.dao.AuthRepository;
import com.renyibang.userapi.dao.UserRepository;
import com.renyibang.userapi.dto.Login;
import com.renyibang.userapi.dto.Register;
import com.renyibang.userapi.entity.Auth;
import com.renyibang.userapi.entity.User;
import com.renyibang.userapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired private AuthRepository authRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public Response login(Login login) {
    if (!authRepository.existsById(login.getUserId())) return Response.error("用户不存在");
    if (!authRepository.existsByUserIdAndPassword(login.getUserId(), login.getPassword()))
      return Response.error("密码错误");
    String jwt = JwtUtil.create(Long.toString(login.getUserId()));
    return Response.success("登录成功", JSONObject.of("jwt", jwt));
  }

  @Override
  public Response register(Register register) {
    User user = new User();
    user.setNickname(register.getNickname());
    user.setPhone(register.getPhone());
    user.setEmail(register.getEmail());
    user.setIntro("");
    userRepository.save(user);
    Auth auth = new Auth();
    auth.setUserId(user.getUserId());
    auth.setPassword(register.getPassword());
    authRepository.save(auth);
    return Response.success("注册成功", JSONObject.of("userId", user.getUserId()));
  }
}
