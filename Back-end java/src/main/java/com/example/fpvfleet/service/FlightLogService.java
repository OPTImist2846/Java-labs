package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.FlightLogDto;
import com.example.fpvfleet.entity.FlightLog;
import com.example.fpvfleet.mapper.FlightLogMapper;
import com.example.fpvfleet.repository.DroneRepository;
import com.example.fpvfleet.repository.FlightLogRepository;
import com.example.fpvfleet.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightLogService {

    private final FlightLogRepository flightLogRepository;
    private final PilotRepository pilotRepository;
    private final DroneRepository droneRepository;
    private final FlightLogMapper flightLogMapper;

    @Transactional(readOnly = true)
    public List<FlightLogDto> getAllLogs() {
        return flightLogRepository.findAll().stream()
                .map(flightLogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FlightLogDto getLogById(Long id) {
        FlightLog log = flightLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Журнал польоту з ID " + id + " не знайдено"));
        return flightLogMapper.toDto(log);
    }

    @Transactional
    public FlightLogDto createLog(FlightLogDto flightLogDto) {
        if (!pilotRepository.existsById(flightLogDto.getPilotId())) {
            throw new RuntimeException("Пілота з ID " + flightLogDto.getPilotId() + " не знайдено.");
        }
        if (!droneRepository.existsById(flightLogDto.getDroneId())) {
            throw new RuntimeException("Дрона з ID " + flightLogDto.getDroneId() + " не знайдено.");
        }

        FlightLog log = flightLogMapper.toEntity(flightLogDto);
        FlightLog savedLog = flightLogRepository.save(log);
        return flightLogMapper.toDto(savedLog);
    }
}