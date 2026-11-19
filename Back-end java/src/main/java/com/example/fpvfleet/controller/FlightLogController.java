package com.example.fpvfleet.controller;

import com.example.fpvfleet.dto.FlightLogDto;
import com.example.fpvfleet.service.FlightLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flight-logs")
@RequiredArgsConstructor
public class FlightLogController {

    private final FlightLogService flightLogService;

    @GetMapping
    public ResponseEntity<List<FlightLogDto>> getAllLogs() {
        return ResponseEntity.ok(flightLogService.getAllLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightLogDto> getLogById(@PathVariable Long id) {
        return ResponseEntity.ok(flightLogService.getLogById(id));
    }

    @PostMapping
    public ResponseEntity<FlightLogDto> createLog(@RequestBody FlightLogDto flightLogDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightLogService.createLog(flightLogDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightLogDto> updateLog(@PathVariable Long id, @RequestBody FlightLogDto flightLogDto) {
        return ResponseEntity.ok(flightLogService.updateLog(id, flightLogDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        flightLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}
