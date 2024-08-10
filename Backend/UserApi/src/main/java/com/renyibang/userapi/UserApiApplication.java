package com.renyibang.userapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.renyibang.global.client")
public class UserApiApplication {

  public static void main(String[] args) throws Exception {
    Dotenv dotenv = Dotenv.configure().load();
    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    SpringApplication.run(UserApiApplication.class, args);
  }
}
