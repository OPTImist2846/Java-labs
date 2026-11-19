package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.EquipmentDto;
import com.example.fpvfleet.entity.Equipment;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.mapper.EquipmentMapper;
import com.example.fpvfleet.repository.EquipmentRepository;
import com.example.fpvfleet.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final PilotRepository pilotRepository;
    private final EquipmentMapper equipmentMapper;

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public List<EquipmentDto> getAllEquipment() {
        return equipmentRepository.findAll().stream()
                .map(equipmentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public EquipmentDto getEquipmentById(Long id) {
        return equipmentMapper.toDto(findEquipment(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public List<EquipmentDto> getEquipmentByPilotId(Long pilotId) {
        return equipmentRepository.findAllByPilotId(pilotId).stream()
                .map(equipmentMapper::toDto)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public EquipmentDto createEquipment(EquipmentDto equipmentDto) {
        Equipment equipment = equipmentMapper.toEntity(equipmentDto);
        equipment.setId(null);
        equipment.setPilot(findPilot(equipmentDto.getPilotId()));
        return equipmentMapper.toDto(equipmentRepository.save(equipment));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'PILOT')")
    public EquipmentDto updateEquipment(Long id, EquipmentDto equipmentDto) {
        Equipment equipment = findEquipment(id);
        equipmentMapper.updateEntity(equipmentDto, equipment);
        equipment.setPilot(findPilot(equipmentDto.getPilotId()));
        return equipmentMapper.toDto(equipment);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEquipment(Long id) {
        equipmentRepository.delete(findEquipment(id));
    }

    private Equipment findEquipment(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Обладнання з ID " + id + " не знайдено"));
    }

    private Pilot findPilot(Long pilotId) {
        if (pilotId == null) {
            throw new IllegalArgumentException("pilotId є обов'язковим");
        }
        return pilotRepository.findById(pilotId)
                .orElseThrow(() -> new ResourceNotFoundException("Пілота з ID " + pilotId + " не знайдено"));
    }
}
