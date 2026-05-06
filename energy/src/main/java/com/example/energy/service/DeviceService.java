package com.example.energy.service;

import com.example.energy.model.Device;
import com.example.energy.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository repository;

    public List<Device> getAll() { return repository.findAll(); }
    public Device getById(String id) { return repository.findById(id); }

    public Device create(Device item) {
        if (item.getId() == null) item.setId(UUID.randomUUID().toString());
        return repository.save(item);
    }

    public Device update(String id, Device item) {
        if (!repository.existsById(id)) return null;
        item.setId(id);
        return repository.save(item);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) { repository.deleteById(id); return true; }
        return false;
    }
}