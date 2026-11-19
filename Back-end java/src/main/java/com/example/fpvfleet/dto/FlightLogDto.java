package com.example.fpvfleet.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class FlightLogDto {
    private Long id;
    private Long pilotId;
    private Long droneId;
    private String environment;
    private Integer durationMinutes;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime flightDate;
    private String notes;
}