package com.example.energy.controller;

import com.example.energy.model.Device;
import com.example.energy.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService service;

    @GetMapping
    public List<Device> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getById(@PathVariable String id) {
        Device item = service.getById(id);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Device> create(@RequestBody Device item) {
        return new ResponseEntity<>(service.create(item), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> update(@PathVariable String id, @RequestBody Device item) {
        Device updated = service.update(id, item);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}