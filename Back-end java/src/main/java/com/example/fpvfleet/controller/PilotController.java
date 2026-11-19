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
        return ResponseEntity.status(HttpStatus.CREATED).body(pilotService.createPilot(pilotDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PilotDto> updatePilot(@PathVariable Long id, @RequestBody PilotDto pilotDto) {
        return ResponseEntity.ok(pilotService.updatePilot(id, pilotDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePilot(@PathVariable Long id) {
        pilotService.deletePilot(id);
        return ResponseEntity.noContent().build();
    }
}
