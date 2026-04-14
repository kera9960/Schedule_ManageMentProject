package com.example.schedule.service;

import com.example.schedule.dto.*;
import com.example.schedule.entity.Comment;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.CommentRepository;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    private void checkBlank(String value,String message){
        if (value==null||value.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
        }
    }

    private void checkLength(String value,int maxLength,String message){
        if (value.length() > maxLength){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
        }
    }

    private void validateCreateSchedule(CreateScheduleRequestDto requestDto){
        checkBlank(requestDto.getTitle(),"제목은 필수입니다.");
        checkBlank(requestDto.getContent(),"내용은 필수입니다.");
        checkBlank(requestDto.getAuthor(),"작성자명은 필수입니다.");
        checkBlank(requestDto.getPassword(),"비밀번호는 필수입니다.");
        checkLength(requestDto.getTitle(),30,"제목은 30자 이내로 작성해야합니다.");
        checkLength(requestDto.getContent(),200,"내용은 200자 이내로 작성해야합니다.");
    }

    private void validateUpdateSchedule(UpdateScheduleRequestDto requestDto){
        if (requestDto.getTitle()!=null){
            checkBlank(requestDto.getTitle(),"제목은 공백으로 쓸 수 없습니다.");
            checkLength(requestDto.getTitle(),30,"제목은 30자 이내로 작성해야합니다.");
        }
        if (requestDto.getAuthor()!=null){
            checkBlank(requestDto.getAuthor(),"작성자명은 공백으로 쓸 수 없습니다.");
        }
        checkBlank(requestDto.getPassword(),"비밀번호는 필수입니다.");
    }


    @Transactional
    public CreateScheduleResponseDto save(CreateScheduleRequestDto requestDto) {
        validateCreateSchedule(requestDto);
        Schedule schedule = new Schedule(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getAuthor(),
                requestDto.getPassword());
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new CreateScheduleResponseDto(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getContent(),
                savedSchedule.getAuthor(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public GetScheduleResponseDto findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                ()-> new IllegalStateException("없는 일정입니다.")
        );
        List<Comment> comments = commentRepository.findAllByScheduleId(scheduleId);
        List<GetCommentResponseDto> commentDtos = new ArrayList<>();
        for(Comment comment : comments){
            commentDtos.add(new GetCommentResponseDto(
                    comment.getId(),
                    comment.getContent(),
                    comment.getAuthor(),
                    comment.getCreatedAt(),
                    comment.getUpdatedAt()
                )
            );
        }

        return new GetScheduleResponseDto(
                schedule.getId(),
                schedule.getContent(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt(),
                commentDtos
        );
    }
    @Transactional(readOnly = true)
    public List<GetScheduleListResponseDto> findAll(String author) {
        List<Schedule> schedules;
        Sort sort = Sort.by(Sort.Direction.DESC,"updatedAt");
        if(author ==null){
            schedules  = scheduleRepository.findAll(sort);
        }else {
            schedules = scheduleRepository.findByAuthor(author,sort);
        }
        List<GetScheduleListResponseDto> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            dtos.add(
                    new GetScheduleListResponseDto(
                            schedule.getId(),
                            schedule.getContent(),
                            schedule.getAuthor(),
                            schedule.getCreatedAt(),
                            schedule.getUpdatedAt()
                    )
            );
        }
        return dtos;
    }
    @Transactional
    public UpdateScheduleResponseDto update(Long scheduleId, UpdateScheduleRequestDto requestDto) {
        validateUpdateSchedule(requestDto);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                ()-> new IllegalStateException("없는 일정입니다.")
        );
        if(schedule.getPassword().equals(requestDto.getPassword())) {
            schedule.update(requestDto.getTitle(),
                    requestDto.getAuthor());
            return new UpdateScheduleResponseDto(
                    schedule.getId(),
                    schedule.getTitle(),
                    schedule.getAuthor(),
                    schedule.getContent(),
                    schedule.getCreatedAt(),
                    schedule.getUpdatedAt()
            );
        } else{
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }
    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                ()-> new IllegalStateException("없는 일정입니다.")
        );
        if(schedule.getPassword().equals(requestDto.getPassword())){
            scheduleRepository.deleteById(scheduleId);
        }else {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }
}
