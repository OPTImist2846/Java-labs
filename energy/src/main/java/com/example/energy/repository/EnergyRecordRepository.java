package com.example.energy.repository;

import com.example.energy.model.EnergyRecord;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EnergyRecordRepository {
    private final Map<String, EnergyRecord> records = new ConcurrentHashMap<>();

    public List<EnergyRecord> findAll() { return new ArrayList<>(records.values()); }
    public EnergyRecord findById(String id) { return records.get(id); }
    public EnergyRecord save(EnergyRecord record) { records.put(record.getId(), record); return record; }
    public void deleteById(String id) { records.remove(id); }
    public boolean existsById(String id) { return records.containsKey(id); }
}