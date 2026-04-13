package com.example.schedule.controller;

import com.example.schedule.dto.CreateCommentRequestDto;
import com.example.schedule.dto.CreateCommentResponseDto;
import com.example.schedule.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{scheduleId}")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CreateCommentResponseDto> createComment(
            @PathVariable Long scheduleId,
            @RequestBody CreateCommentRequestDto requestDto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(scheduleId, requestDto));
    }
}
