package com.renyibang.startup.controller;

import com.renyibang.startup.service.StartupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class StartupController {
  @Autowired private StartupService startupService;

  @PostMapping("/startup/{port}")
  public String startup(@PathVariable int port) throws IOException {
    return startupService.startup(port);
  }
}
