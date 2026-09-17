package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.mapper.PilotMapper;
import com.example.fpvfleet.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PilotService {

    private final PilotRepository pilotRepository;
    private final PilotMapper pilotMapper;

    @Transactional(readOnly = true)
    public List<PilotDto> getAllPilots() {
        return pilotRepository.findAll().stream()
                .map(pilotMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PilotDto getPilotById(Long id) {
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пілота з ID " + id + " не знайдено"));
        return pilotMapper.toDto(pilot);
    }

    @Transactional
    public PilotDto createPilot(PilotDto pilotDto) {
        if (pilotRepository.findByUsername(pilotDto.getUsername()).isPresent()) {
            throw new RuntimeException("Пілот з таким username вже існує");
        }

        Pilot pilot = pilotMapper.toEntity(pilotDto);
        Pilot savedPilot = pilotRepository.save(pilot);
        return pilotMapper.toDto(savedPilot);
    }
}