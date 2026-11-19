package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.entity.Drone;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.mapper.DroneMapper;
import com.example.fpvfleet.repository.DroneRepository;
import com.example.fpvfleet.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .toList();
    }

    @Transactional(readOnly = true)
    public DroneDto getDroneById(Long id) {
        return droneMapper.toDto(findDrone(id));
    }

    @Transactional(readOnly = true)
    public List<DroneDto> getDronesByPilotId(Long pilotId) {
        return droneRepository.findAllByPilotId(pilotId).stream()
                .map(droneMapper::toDto)
                .toList();
    }

    @Transactional
    public DroneDto createDrone(DroneDto droneDto) {
        Drone drone = droneMapper.toEntity(droneDto);
        drone.setId(null);
        drone.setPilot(findPilot(droneDto.getPilotId()));
        return droneMapper.toDto(droneRepository.save(drone));
    }

    @Transactional
    public DroneDto updateDrone(Long id, DroneDto droneDto) {
        Drone drone = findDrone(id);
        droneMapper.updateEntity(droneDto, drone);
        drone.setPilot(findPilot(droneDto.getPilotId()));
        return droneMapper.toDto(drone);
    }

    @Transactional
    public void deleteDrone(Long id) {
        droneRepository.delete(findDrone(id));
    }

    private Drone findDrone(Long id) {
        return droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Дрон з ID " + id + " не знайдено"));
    }

    private Pilot findPilot(Long pilotId) {
        if (pilotId == null) {
            throw new IllegalArgumentException("pilotId є обов'язковим");
        }
        return pilotRepository.findById(pilotId)
                .orElseThrow(() -> new ResourceNotFoundException("Пілота з ID " + pilotId + " не знайдено"));
    }
}
