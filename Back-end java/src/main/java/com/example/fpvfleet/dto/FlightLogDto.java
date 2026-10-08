package com.example.fpvfleet.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FlightLogDto {
    private Long id;
    private Long pilotId;
    private Long droneId;
    private String environment;
    private Integer durationMinutes;
    private LocalDateTime flightDate;
    private String notes;
}