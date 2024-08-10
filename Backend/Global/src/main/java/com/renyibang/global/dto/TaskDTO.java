package com.renyibang.global.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    long id;
    String title;
    String description;
    String images;
    LocalDateTime time;

    public TaskDTO(long id, String title, String description, String images, LocalDateTime time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.images = images;
        this.time = time;
    }

    public TaskDTO() {
    }
}
