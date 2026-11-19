package com.example.fpvfleet.controller;

import com.example.fpvfleet.dto.EquipmentDto;
import com.example.fpvfleet.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<EquipmentDto>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipment());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDto> getEquipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.getEquipmentById(id));
    }

    @GetMapping("/pilot/{pilotId}")
    public ResponseEntity<List<EquipmentDto>> getEquipmentByPilotId(@PathVariable Long pilotId) {
        return ResponseEntity.ok(equipmentService.getEquipmentByPilotId(pilotId));
    }

    @PostMapping
    public ResponseEntity<EquipmentDto> createEquipment(@RequestBody EquipmentDto equipmentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.createEquipment(equipmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDto> updateEquipment(@PathVariable Long id, @RequestBody EquipmentDto equipmentDto) {
        return ResponseEntity.ok(equipmentService.updateEquipment(id, equipmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
