package com.example.energy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tariff {
    private String id;
    private String name; // Назва тарифу (наприклад "Денний", "Нічний")
    private double pricePerKwh; // Ціна за 1 кВт
}