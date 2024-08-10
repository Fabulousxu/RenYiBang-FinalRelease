package com.renyibang.userapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.renyibang.global.util.Response;
import com.renyibang.userapi.dto.Update;
import com.renyibang.userapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class UserControllerTest {
  private MockMvc mockMvc;
  @InjectMocks private UserController userController;
  @Mock private UserService userService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  public void getSelfInfoTest() throws Exception {
    when(userService.getUserInfo(anyLong())).thenReturn(Response.success());
    mockMvc
        .perform(get("/api/user/self").header("userId", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getUserDTOTest() throws Exception {
    when(userService.getUserDTO(anyLong())).thenReturn(Response.success());
    mockMvc
        .perform(get("/api/user/1/dto"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getUserInfoTest() throws Exception {
    when(userService.getUserInfo(anyLong())).thenReturn(Response.success());
    mockMvc
        .perform(get("/api/user/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void updateUserInfoTest() throws Exception {
    when(userService.updateUserInfo(anyLong(), any(Update.class))).thenReturn(Response.success());
    mockMvc
        .perform(
            post("/api/user/self/update")
                .header("userId", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"nickname\":\"nickname\",\"avatar\":\"avatar\",\"intro\":\"intro\",\"phone\":\"phone\",\"email\":\"email\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getUserExistTest() throws Exception {
    when(userService.existsById(anyLong())).thenReturn(true);
    mockMvc
        .perform(get("/api/user/1/exist"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(true));
  }

  @Test
  public void getUserInfosTest() throws Exception {
    when(userService.getUserInfos(any())).thenReturn(Response.success());
    mockMvc
        .perform(get("/api/user/list/1,2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void updateUserInfo_compatibleTest() throws Exception {
    when(userService.updateUserInfo_compatible(any())).thenReturn(Response.success());
    mockMvc
        .perform(
            post("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"nickname\":\"nickname\",\"avatar\":\"avatar\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void getUserInfos_accessorTest() throws Exception {
    when(userService.getUserInfos_accessor(any())).thenReturn(Response.success());
    mockMvc
        .perform(get("/api/user/accessor/1,2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }
}
