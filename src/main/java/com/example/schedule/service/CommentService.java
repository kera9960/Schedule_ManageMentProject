package com.example.schedule.service;

import com.example.schedule.dto.CreateCommentRequestDto;
import com.example.schedule.dto.CreateCommentResponseDto;
import com.example.schedule.entity.Comment;
import com.example.schedule.repository.CommentRepository;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private void checkBlank(String value,String message){
        if (value ==null||value.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
        }
    }
    private void checkLength(String value,int maxLength,String message){
        if (value.length()>maxLength){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
        }
    }
    private void validateCreateComment(CreateCommentRequestDto requestDto){
        checkBlank(requestDto.getContent(),"내용은 필수입니다.");
        checkBlank(requestDto.getAuthor(),"작성자명은 필수입니다.");
        checkBlank(requestDto.getPassword(),"비밀번호는 필수입니다.");
        checkLength(requestDto.getContent(),100,"내용은 100자 이내로 작성해야합니다.");
    }

    @Transactional
    public CreateCommentResponseDto save(Long scheduleId,CreateCommentRequestDto requestDto) {
        validateCreateComment(requestDto);
        scheduleRepository.findById(scheduleId).orElseThrow(
                ()-> new IllegalStateException("없는 일정입니다.")
        );
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
