package com.renyibang.userapi.service.serviceImpl;

import com.alibaba.fastjson2.JSONObject;
import com.renyibang.global.dto.UserDTO;
import com.renyibang.global.util.Response;
import com.renyibang.userapi.dao.UserRepository;
import com.renyibang.userapi.dto.Update;
import com.renyibang.userapi.entity.User;
import com.renyibang.userapi.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired private UserRepository userRepository;

  @Override
  public Response getUserInfo(long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) return Response.error("用户不存在");
    return Response.success(user);
  }

  @Override
  public Response updateUserInfo(long userId, Update update) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) return Response.error("用户不存在");
    user.setNickname(update.getNickname());
    user.setAvatar(update.getAvatar());
    user.setIntro(update.getIntro());
    user.setPhone(update.getPhone());
    user.setEmail(update.getEmail());
    userRepository.save(user);
    return Response.success("更新成功");
  }

  @Override
  public boolean existsById(long userId) {
    return userRepository.existsById(userId);
  }

  @Override
  public Response getUserInfos(List<Long> userIds) {
    List<User> users = new ArrayList<>();
    for (long userId : userIds) {
      User user = userRepository.findById(userId).orElse(null);
      if (user == null) return Response.error("用户不存在");
      users.add(userRepository.findById(userId).orElse(null));
    }
    return Response.success(users);
  }

  @Override
  public Response updateUserInfo_compatible(UserDTO userDTO) {
    User user = userRepository.findById(userDTO.getId()).orElse(null);
    if (user == null) return Response.error("用户不存在");
    user.setNickname(userDTO.getNickname());
    user.setAvatar(userDTO.getAvatar());
    userRepository.save(user);
    return Response.success("更新成功");
  }

  @Override
  public Response getUserDTO(long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) return Response.error("用户不存在");
    UserDTO userDTO = new UserDTO();
    userDTO.setId(user.getUserId());
    userDTO.setNickname(user.getNickname());
    userDTO.setAvatar(user.getAvatar());
    return Response.success(userDTO);
  }

  @Override
  public Response getUserInfos_accessor(List<Long> userIds) {
    List<JSONObject> users = new ArrayList<>();
    for (long userId : userIds) {
      User user = userRepository.findById(userId).orElse(null);
      if (user == null) return Response.error("用户不存在");
      JSONObject userJson = new JSONObject();
      userJson.put("userId", user.getUserId());
      userJson.put("nickname", user.getNickname());
      userJson.put("avatar", user.getAvatar());
      userJson.put("rating", user.getRating());
      users.add(userJson);
    }
    return Response.success(users);
  }
}
