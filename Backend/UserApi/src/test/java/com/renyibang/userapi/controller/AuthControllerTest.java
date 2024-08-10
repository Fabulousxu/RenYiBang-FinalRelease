package com.renyibang.userapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.renyibang.global.util.Response;
import com.renyibang.userapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class AuthControllerTest {
  private MockMvc mockMvc;
  @InjectMocks private AuthController authController;
  @Mock private AuthService authService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
  }

  @Test
  public void loginTest() throws Exception {
    when(authService.login(any())).thenReturn(Response.success());
    mockMvc
        .perform(
            post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"password\":\"1\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }

  @Test
  public void registerTest() throws Exception {
    when(authService.register(any())).thenReturn(Response.success());
    mockMvc
        .perform(
            post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nickname\":\"\",\"phone\":\"\",\"email\":\"\",\"password\":\"\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.ok").value(true));
  }
}
