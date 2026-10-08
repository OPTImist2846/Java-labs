package com.example.fpvfleet.controller;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    public ResponseEntity<List<DroneDto>> getAllDrones() {
        return ResponseEntity.ok(droneService.getAllDrones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DroneDto> getDroneById(@PathVariable Long id) {
        return ResponseEntity.ok(droneService.getDroneById(id));
    }

    @GetMapping("/pilot/{pilotId}")
    public ResponseEntity<List<DroneDto>> getDronesByPilotId(@PathVariable Long pilotId) {
        return ResponseEntity.ok(droneService.getDronesByPilotId(pilotId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DroneDto> createDrone(@RequestBody DroneDto droneDto) {
        DroneDto createdDrone = droneService.createDrone(droneDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDrone);
    }

}
