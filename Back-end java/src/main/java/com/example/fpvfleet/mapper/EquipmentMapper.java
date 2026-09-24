package com.example.fpvfleet.mapper;

import com.example.fpvfleet.dto.EquipmentDto;
import com.example.fpvfleet.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(source = "pilot.id", target = "pilotId")
    EquipmentDto toDto(Equipment equipment);

    @Mapping(source = "pilotId", target = "pilot.id")
    Equipment toEntity(EquipmentDto equipmentDto);
}