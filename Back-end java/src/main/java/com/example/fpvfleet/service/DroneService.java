package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.entity.Drone;
import com.example.fpvfleet.mapper.DroneMapper;
import com.example.fpvfleet.repository.DroneRepository;
import com.example.fpvfleet.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;
    private final PilotRepository pilotRepository;
    private final DroneMapper droneMapper;

    @Transactional(readOnly = true)
    public List<DroneDto> getAllDrones() {
        return droneRepository.findAll().stream()
                .map(droneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DroneDto getDroneById(Long id) {
        Drone drone = droneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Дрон з ID " + id + " не знайдено"));
        return droneMapper.toDto(drone);
    }

    @Transactional(readOnly = true)
    public List<DroneDto> getDronesByPilotId(Long pilotId) {
        return droneRepository.findAllByPilotId(pilotId).stream()
                .map(droneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DroneDto createDrone(DroneDto droneDto) {
        if (!pilotRepository.existsById(droneDto.getPilotId())) {
            throw new RuntimeException("Пілота з ID " + droneDto.getPilotId() + " не знайдено. Неможливо додати дрон.");
        }

        Drone drone = droneMapper.toEntity(droneDto);
        Drone savedDrone = droneRepository.save(drone);
        return droneMapper.toDto(savedDrone);
    }
}