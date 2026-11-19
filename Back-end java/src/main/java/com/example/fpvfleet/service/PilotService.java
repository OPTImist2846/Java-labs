package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.mapper.PilotMapper;
import com.example.fpvfleet.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PilotService {

    private final PilotRepository pilotRepository;
    private final PilotMapper pilotMapper;

    @Transactional(readOnly = true)
    public List<PilotDto> getAllPilots() {
        return pilotRepository.findAll().stream()
                .map(pilotMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PilotDto getPilotById(Long id) {
        return pilotMapper.toDto(findPilot(id));
    }

    @Transactional
    public PilotDto createPilot(PilotDto pilotDto) {
        if (pilotRepository.findByUsername(pilotDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Пілот з таким username вже існує");
        }
        Pilot pilot = pilotMapper.toEntity(pilotDto);
        pilot.setId(null);
        return pilotMapper.toDto(pilotRepository.save(pilot));
    }

    @Transactional
    public PilotDto updatePilot(Long id, PilotDto pilotDto) {
        Pilot pilot = findPilot(id);
        pilotRepository.findByUsername(pilotDto.getUsername())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new IllegalArgumentException("Пілот з таким username вже існує");
                });
        pilotMapper.updateEntity(pilotDto, pilot);
        return pilotMapper.toDto(pilot);
    }

    @Transactional
    public void deletePilot(Long id) {
        pilotRepository.delete(findPilot(id));
    }

    private Pilot findPilot(Long id) {
        return pilotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пілота з ID " + id + " не знайдено"));
    }
}
