package com.example.schedule.service;

import com.example.schedule.dto.CreateScheduleRequestDto;
import com.example.schedule.dto.CreateScheduleResponseDto;
import com.example.schedule.dto.GetScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateScheduleResponseDto save(CreateScheduleRequestDto requestDto) {
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
        return new GetScheduleResponseDto(
                schedule.getId(),
                schedule.getContent(),
                schedule.getAuthor(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }
    @Transactional(readOnly = true)
    public List<GetScheduleResponseDto> findAll(String author) {
        List<Schedule> schedules;
        Sort sort = Sort.by(Sort.Direction.DESC,"updatedAt");
        if(author ==null){
            schedules  = scheduleRepository.findAll(sort);
        }else {
            schedules = scheduleRepository.findByAuthor(author,sort);
        }
        List<GetScheduleResponseDto> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            dtos.add(
                    new GetScheduleResponseDto(
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
}
