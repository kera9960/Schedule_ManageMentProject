package com.example.schedule.dto;

import lombok.Getter;

@Getter
public class CreateScheduleRequestDto {
    private String title;
    private String content;
    private String author;
    private String password;
}
