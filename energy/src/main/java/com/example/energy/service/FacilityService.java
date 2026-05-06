package com.example.energy.service;

import com.example.energy.model.Facility;
import com.example.energy.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository repository;

    public List<Facility> getAll() {
        return repository.findAll();
    }

    public Facility getById(String id) {
        return repository.findById(id);
    }

    public Facility create(Facility item) {
        if (item.getId() == null) {
            item.setId(UUID.randomUUID().toString());
        }
        return repository.save(item);
    }

    public Facility update(String id, Facility item) {
        if (!repository.existsById(id)) return null;
        item.setId(id);
        return repository.save(item);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}