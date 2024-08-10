package com.renyibang.userapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.renyibang.global.dto.UserDTO;
import com.renyibang.global.util.Response;
import com.renyibang.userapi.dao.UserRepository;
import com.renyibang.userapi.dto.Update;
import com.renyibang.userapi.entity.User;
import com.renyibang.userapi.service.serviceImpl.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
  @InjectMocks private UserServiceImpl userService;
  @Mock private UserRepository userRepository;

  @Test
  public void getUserInfoTest1() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    Response response = userService.getUserInfo(1);
  }

  @Test
  public void getUserInfoTest2() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    Response response = userService.getUserInfo(1);
  }

  @Test
  public void updateUserInfoTest1() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    when(userRepository.save(any(User.class))).thenReturn(new User());
    Response response = userService.updateUserInfo(1, new Update());
  }

  @Test
  public void updateUserInfoTest2() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    Response response = userService.updateUserInfo(1, new Update());
  }

  @Test
  public void existsByIdTest() {
    when(userRepository.existsById(anyLong())).thenReturn(true);
    boolean result = userService.existsById(1);
  }

  @Test
  public void getUserInfosTest1() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    Response response = userService.getUserInfos(List.of(1L));
  }

  @Test
  public void getUserInfosTest2() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    Response response = userService.getUserInfos(List.of(1L));
  }

  @Test
  public void getUserDTOTest1() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    Response response = userService.getUserDTO(1);
  }

  @Test
  public void getUserDTOTest2() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    Response response = userService.getUserDTO(1);
  }

  @Test
  public void updateUserInfo_compatibleTest1() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    when(userRepository.save(any(User.class))).thenReturn(new User());
    Response response = userService.updateUserInfo_compatible(new UserDTO());
  }

  @Test
  public void updateUserInfo_compatibleTest2() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    Response response = userService.updateUserInfo_compatible(new UserDTO());
  }

  @Test
  public void getUserInfos_accessorTest1() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    Response response = userService.getUserInfos_accessor(List.of(1L));
  }

  @Test
  public void getUserInfos_accessorTest2() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
    Response response = userService.getUserInfos_accessor(List.of(1L));
  }
}
