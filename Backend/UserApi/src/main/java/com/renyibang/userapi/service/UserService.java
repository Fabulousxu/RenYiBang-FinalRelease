package com.renyibang.userapi.service;

import com.renyibang.global.dto.UserDTO;
import com.renyibang.global.util.Response;
import com.renyibang.userapi.dto.Update;
import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  Response getUserInfo(long userId);

  Response updateUserInfo(long userId, Update update);

  boolean existsById(long userId);

  Response getUserInfos(List<Long> userIds);

  Response updateUserInfo_compatible(UserDTO userDTO);

  Response getUserDTO(long userId);

  Response getUserInfos_accessor(List<Long> userIds);
}
