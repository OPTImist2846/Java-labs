package com.example.fpvfleet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "drones")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String frameSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pilot_id", nullable = false)
    @ToString.Exclude
    private Pilot pilot;
}