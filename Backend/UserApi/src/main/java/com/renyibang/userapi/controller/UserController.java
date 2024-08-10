package com.renyibang.userapi.controller;

import com.renyibang.global.dto.UserDTO;
import com.renyibang.global.util.Response;
import com.renyibang.userapi.dto.Update;
import com.renyibang.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
  @Autowired private UserService userService;

  @GetMapping("/self")
  public Response getSelfInfo(@RequestHeader long userId) {
    return userService.getUserInfo(userId);
  }

  @GetMapping("/{userId}/dto")
  public Response getUserDTO(@PathVariable long userId) {
    return userService.getUserDTO(userId);
  }

  @GetMapping("/{userId}")
  public Response getUserInfo(@PathVariable long userId) {
    return userService.getUserInfo(userId);
  }

  @PostMapping("/self/update")
  public Response updateUserInfo(@RequestBody Update update, @RequestHeader long userId) {
    return userService.updateUserInfo(userId, update);
  }

  @GetMapping("/{userId}/exist")
  boolean getUserExist(@PathVariable long userId) {
    return userService.existsById(userId);
  }

  @GetMapping("/list/{userIds}")
  public Response getUserInfos(@PathVariable List<Long> userIds) {
    return userService.getUserInfos(userIds);
  }

  @PostMapping("/update")
  public Response updateUserInfo(@RequestBody UserDTO userDTO) {
    return userService.updateUserInfo_compatible(userDTO);
  }

  @GetMapping("/accessor/{userIds}")
  public Response getUserInfos_accessor(@PathVariable List<Long> userIds) {
    return userService.getUserInfos_accessor(userIds);
  }

  @GetMapping("/health")
  public Response health() {
    return Response.success();
  }
}
