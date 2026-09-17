package com.example.fpvfleet.dto;

import lombok.Data;

@Data
public class DroneDto {
    private Long id;
    private String name;
    private String frameSize;
    private Long pilotId;
}