package com.example.fpvfleet.mapper;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.entity.Drone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DroneMapper {

    @Mapping(source = "pilot.id", target = "pilotId")
    DroneDto toDto(Drone drone);

    @Mapping(source = "pilotId", target = "pilot.id")
    Drone toEntity(DroneDto droneDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pilot", ignore = true)
    void updateEntity(DroneDto droneDto, @MappingTarget Drone drone);
}
