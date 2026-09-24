package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.EquipmentDto;
import com.example.fpvfleet.entity.Equipment;
import com.example.fpvfleet.mapper.EquipmentMapper;
import com.example.fpvfleet.repository.EquipmentRepository;
import com.example.fpvfleet.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final PilotRepository pilotRepository;
    private final EquipmentMapper equipmentMapper;

    @Transactional(readOnly = true)
    public List<EquipmentDto> getAllEquipment() {
        return equipmentRepository.findAll().stream()
                .map(equipmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipmentDto getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Обладнання з ID " + id + " не знайдено"));
        return equipmentMapper.toDto(equipment);
    }

    @Transactional(readOnly = true)
    public List<EquipmentDto> getEquipmentByPilotId(Long pilotId) {
        return equipmentRepository.findAllByPilotId(pilotId).stream()
                .map(equipmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EquipmentDto createEquipment(EquipmentDto equipmentDto) {
        if (!pilotRepository.existsById(equipmentDto.getPilotId())) {
            throw new RuntimeException("Пілота з ID " + equipmentDto.getPilotId() + " не знайдено. Неможливо додати обладнання.");
        }

        Equipment equipment = equipmentMapper.toEntity(equipmentDto);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toDto(savedEquipment);
    }
}
