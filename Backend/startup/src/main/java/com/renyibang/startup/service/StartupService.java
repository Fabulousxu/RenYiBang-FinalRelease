package com.renyibang.startup.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface StartupService {
  String startup(int port) throws IOException;
}
