package com.example.energy.controller;

import com.example.energy.model.Tariff;
import com.example.energy.service.TariffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
public class TariffController {
    private final TariffService service;

    @GetMapping
    public List<Tariff> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Tariff> getById(@PathVariable String id) {
        Tariff item = service.getById(id);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Tariff> create(@RequestBody Tariff item) {
        return new ResponseEntity<>(service.create(item), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tariff> update(@PathVariable String id, @RequestBody Tariff item) {
        Tariff updated = service.Tariff(id, item); // Використовуємо метод, як ми його назвали в сервісі
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}