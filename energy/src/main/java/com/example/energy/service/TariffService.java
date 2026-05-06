package com.example.energy.service;

import com.example.energy.model.Tariff;
import com.example.energy.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository repository;

    public List<Tariff> getAll() { return repository.findAll(); }
    public Tariff getById(String id) { return repository.findById(id); }

    public Tariff create(Tariff item) {
        if (item.getId() == null) item.setId(UUID.randomUUID().toString());
        return repository.save(item);
    }

    public Tariff Tariff(String id, Tariff item) { // Виправлено назву методу для консистентності, хоча можна було б і update
        if (!repository.existsById(id)) return null;
        item.setId(id);
        return repository.save(item);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) { repository.deleteById(id); return true; }
        return false;
    }
}