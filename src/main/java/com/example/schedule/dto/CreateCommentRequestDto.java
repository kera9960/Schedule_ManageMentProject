package com.example.schedule.dto;

import lombok.Getter;

@Getter
public class CreateCommentRequestDto {
    private String author;
    private String content;
    private String password;
}
