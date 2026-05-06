package com.example.energy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facility {
    private String id;
    private String address;
    private String userId; // ID власника (користувача)
}