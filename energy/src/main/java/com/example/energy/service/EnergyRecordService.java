package com.example.energy.service;

import com.example.energy.model.EnergyRecord;
import com.example.energy.repository.EnergyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnergyRecordService {
    private final EnergyRecordRepository repository;

    public List<EnergyRecord> getAll() { return repository.findAll(); }
    public EnergyRecord getById(String id) { return repository.findById(id); }

    public EnergyRecord create(EnergyRecord item) {
        if (item.getId() == null) item.setId(UUID.randomUUID().toString());
        if (item.getTimestamp() == null) item.setTimestamp(LocalDateTime.now()); // Автоматично ставимо час
        return repository.save(item);
    }

    public EnergyRecord update(String id, EnergyRecord item) {
        if (!repository.existsById(id)) return null;
        item.setId(id);
        return repository.save(item);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) { repository.deleteById(id); return true; }
        return false;
    }
}