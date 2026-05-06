package com.example.energy.controller;

import com.example.energy.model.EnergyRecord;
import com.example.energy.service.EnergyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class EnergyRecordController {
    private final EnergyRecordService service;

    @GetMapping
    public List<EnergyRecord> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<EnergyRecord> getById(@PathVariable String id) {
        EnergyRecord item = service.getById(id);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EnergyRecord> create(@RequestBody EnergyRecord item) {
        return new ResponseEntity<>(service.create(item), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnergyRecord> update(@PathVariable String id, @RequestBody EnergyRecord item) {
        EnergyRecord updated = service.update(id, item);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}