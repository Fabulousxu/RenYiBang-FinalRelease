package com.renyibang.startup.service.impl;

import com.renyibang.startup.service.StartupService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StartupServiceImpl implements StartupService {
  private String runService(String service, int instance) {
    return "java -jar /root/newjar/" + service
      + "-0.0.1-SNAPSHOT.jar --spring.profiles.active=instance" + instance + " > /root/newjar/"
      + service + "-0.0.1-SNAPSHOT-instance" + instance + ".log 2>&1 &";
  }

  @Override
  public String startup(int port) throws IOException {
    String cmd = switch (port) {
      case 8081 -> runService("Gateway", 1);
      case 8082 -> runService("Gateway", 2);
      case 8083 -> runService("Gateway", 3);
      case 8084 -> runService("Gateway", 4);
      case 8085 -> runService("UserApi", 1);
      case 8086 -> runService("UserApi", 2);
      case 8087 -> runService("TaskApi", 1);
      case 8088 -> runService("TaskApi", 2);
      case 8091 -> runService("ServiceApi", 1);
      case 8092 -> runService("ServiceApi", 2);
      case 8093 -> runService("OrderApi", 1);
      case 8094 -> runService("OrderApi", 2);
      case 8095 -> runService("ChatApi", 1);
      case 8096 -> runService("ChatApi", 2);
      default -> "";
    };
    ProcessBuilder processBuilder = new ProcessBuilder(List.of("bash", "-c", cmd));
    Process process = processBuilder.start();
    return "Server started on port " + port;
  }
}
