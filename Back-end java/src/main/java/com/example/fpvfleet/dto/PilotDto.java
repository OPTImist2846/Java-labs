package com.example.fpvfleet.dto;

import lombok.Data;

@Data
public class PilotDto {
    private Long id;
    private String username;
    private String email;
    private String role;
}