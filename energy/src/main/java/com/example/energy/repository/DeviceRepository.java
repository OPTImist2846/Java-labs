package com.example.energy.repository;

import com.example.energy.model.Device;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DeviceRepository {
    private final Map<String, Device> devices = new ConcurrentHashMap<>();

    public List<Device> findAll() { return new ArrayList<>(devices.values()); }
    public Device findById(String id) { return devices.get(id); }
    public Device save(Device device) { devices.put(device.getId(), device); return device; }
    public void deleteById(String id) { devices.remove(id); }
    public boolean existsById(String id) { return devices.containsKey(id); }
}