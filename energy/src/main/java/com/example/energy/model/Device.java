package com.example.energy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private String id;
    private String modelName;
    private String facilityId; // ID будинку/квартири
}