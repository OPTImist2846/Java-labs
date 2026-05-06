package com.example.energy.repository;

import com.example.energy.model.Facility;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FacilityRepository {
    private final Map<String, Facility> facilities = new ConcurrentHashMap<>();

    public List<Facility> findAll() {
        return new ArrayList<>(facilities.values());
    }

    public Facility findById(String id) {
        return facilities.get(id);
    }

    public Facility save(Facility facility) {
        facilities.put(facility.getId(), facility);
        return facility;
    }

    public void deleteById(String id) {
        facilities.remove(id);
    }

    public boolean existsById(String id) {
        return facilities.containsKey(id);
    }
}