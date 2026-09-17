package com.example.fpvfleet.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pilot_id", nullable = false)
    @ToString.Exclude
    private Pilot pilot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id")
    @ToString.Exclude
    private Drone drone;

    @Column(nullable = false)
    private String environment;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private LocalDateTime flightDate;

    private String notes;
}