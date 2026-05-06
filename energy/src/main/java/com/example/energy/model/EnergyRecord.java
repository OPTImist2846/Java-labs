package com.example.energy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyRecord {
    private String id;
    private String deviceId; // ID лічильника
    private double consumedKwh; // Скільки кіловат спожито
    private LocalDateTime timestamp; // Час фіксації
}