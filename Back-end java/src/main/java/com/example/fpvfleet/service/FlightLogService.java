package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.FlightLogDto;
import com.example.fpvfleet.entity.Drone;
import com.example.fpvfleet.entity.FlightLog;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.mapper.FlightLogMapper;
import com.example.fpvfleet.repository.DroneRepository;
import com.example.fpvfleet.repository.FlightLogRepository;
import com.example.fpvfleet.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightLogService {

    private final FlightLogRepository flightLogRepository;
    private final PilotRepository pilotRepository;
    private final DroneRepository droneRepository;
    private final FlightLogMapper flightLogMapper;

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public List<FlightLogDto> getAllLogs() {
        return flightLogRepository.findAll().stream()
                .map(flightLogMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public FlightLogDto getLogById(Long id) {
        return flightLogMapper.toDto(findLog(id));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public FlightLogDto createLog(FlightLogDto flightLogDto) {
        FlightLog log = flightLogMapper.toEntity(flightLogDto);
        log.setId(null);
        log.setPilot(findPilot(flightLogDto.getPilotId()));
        log.setDrone(findDrone(flightLogDto.getDroneId()));
        return flightLogMapper.toDto(flightLogRepository.save(log));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public FlightLogDto updateLog(Long id, FlightLogDto flightLogDto) {
        FlightLog log = findLog(id);
        flightLogMapper.updateEntity(flightLogDto, log);
        log.setPilot(findPilot(flightLogDto.getPilotId()));
        log.setDrone(findDrone(flightLogDto.getDroneId()));
        return flightLogMapper.toDto(log);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLog(Long id) {
        flightLogRepository.delete(findLog(id));
    }

    private FlightLog findLog(Long id) {
        return flightLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Журнал польоту з ID " + id + " не знайдено"));
    }

    private Pilot findPilot(Long pilotId) {
        if (pilotId == null) {
            throw new IllegalArgumentException("pilotId є обов'язковим");
        }
        return pilotRepository.findById(pilotId)
                .orElseThrow(() -> new ResourceNotFoundException("Пілота з ID " + pilotId + " не знайдено"));
    }

    private Drone findDrone(Long droneId) {
        if (droneId == null) {
            return null;
        }
        return droneRepository.findById(droneId)
                .orElseThrow(() -> new ResourceNotFoundException("Дрона з ID " + droneId + " не знайдено"));
    }
}
