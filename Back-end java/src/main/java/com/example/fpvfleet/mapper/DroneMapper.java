package com.example.fpvfleet.mapper;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.entity.Drone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DroneMapper {
    // Вказуємо, як саме дістати ID пілота з об'єкта Pilot
    @Mapping(source = "pilot.id", target = "pilotId")
    DroneDto toDto(Drone drone);

    @Mapping(source = "pilotId", target = "pilot.id")
    Drone toEntity(DroneDto droneDto);
}