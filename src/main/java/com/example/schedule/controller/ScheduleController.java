package com.example.schedule.controller;

import com.example.schedule.dto.*;
import com.example.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponseDto> createSchedule(@RequestBody CreateScheduleRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(requestDto));
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponseDto> getSchedule(@PathVariable Long scheduleId){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(scheduleId));
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<GetScheduleListResponseDto>> getSchedules(@RequestParam(required = false) String author){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAll(author));
    }

    @PatchMapping("/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponseDto> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleRequestDto requestDto
            ){
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(scheduleId,requestDto));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId,
            @RequestBody DeleteScheduleRequestDto requestDto){
        scheduleService.delete(scheduleId,requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
