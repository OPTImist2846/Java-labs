package com.example.energy.controller;

import com.example.energy.model.Facility;
import com.example.energy.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService service;

    @GetMapping
    public List<Facility> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getById(@PathVariable String id) {
        Facility item = service.getById(id);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Facility> create(@RequestBody Facility item) {
        return new ResponseEntity<>(service.create(item), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> update(@PathVariable String id, @RequestBody Facility item) {
        Facility updated = service.update(id, item);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}