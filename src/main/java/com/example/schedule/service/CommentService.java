package com.example.schedule.service;

import com.example.schedule.dto.CreateCommentRequestDto;
import com.example.schedule.dto.CreateCommentResponseDto;
import com.example.schedule.entity.Comment;
import com.example.schedule.repository.CommentRepository;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateCommentResponseDto save(Long scheduleId,CreateCommentRequestDto requestDto) {
        scheduleRepository.findById(scheduleId);
        long count = commentRepository.countByScheduleId(scheduleId);
        if (count>=10){
            throw new IllegalStateException("일정당 댓글 10개 제한");
        }
        Comment comment = new Comment(
                requestDto.getContent(),
                requestDto.getAuthor(),
                requestDto.getPassword(),
                scheduleId
        );
        Comment savedComment = commentRepository.save(comment);
        return new CreateCommentResponseDto(
                savedComment.getId(),
                savedComment.getAuthor(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getUpdatedAt()
        );
    }
}
