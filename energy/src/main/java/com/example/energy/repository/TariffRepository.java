package com.example.energy.repository;

import com.example.energy.model.Tariff;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TariffRepository {
    private final Map<String, Tariff> tariffs = new ConcurrentHashMap<>();

    public List<Tariff> findAll() { return new ArrayList<>(tariffs.values()); }
    public Tariff findById(String id) { return tariffs.get(id); }
    public Tariff save(Tariff tariff) { tariffs.put(tariff.getId(), tariff); return tariff; }
    public void deleteById(String id) { tariffs.remove(id); }
    public boolean existsById(String id) { return tariffs.containsKey(id); }
}