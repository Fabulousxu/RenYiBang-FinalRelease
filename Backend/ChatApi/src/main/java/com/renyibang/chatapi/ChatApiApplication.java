package com.renyibang.chatapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.renyibang.global.client")
public class ChatApiApplication {

  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.configure().load();
    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    SpringApplication.run(ChatApiApplication.class, args);
  }
}
