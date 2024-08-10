package com.renyibang.global.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
  long id;
  String title;
  String description;
  String images;
  LocalDateTime time;
}
