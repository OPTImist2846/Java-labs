package com.example.fpvfleet.dto;

import lombok.Data;

@Data
public class EquipmentDto {
    private Long id;
    private String name;
    private String type;
    private Long pilotId;
}