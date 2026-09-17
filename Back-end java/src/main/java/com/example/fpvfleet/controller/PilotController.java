package com.example.fpvfleet.controller;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.PilotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pilots")
@RequiredArgsConstructor
public class PilotController {

    private final PilotService pilotService;

    @GetMapping
    public ResponseEntity<List<PilotDto>> getAllPilots() {
        return ResponseEntity.ok(pilotService.getAllPilots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PilotDto> getPilotById(@PathVariable Long id) {
        return ResponseEntity.ok(pilotService.getPilotById(id));
    }

    @PostMapping
    public ResponseEntity<PilotDto> createPilot(@RequestBody PilotDto pilotDto) {
        PilotDto createdPilot = pilotService.createPilot(pilotDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPilot);
    }
}