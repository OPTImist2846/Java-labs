package com.example.fpvfleet.mapper;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.entity.Pilot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PilotMapper {

    PilotDto toDto(Pilot pilot);

    @Mapping(target = "drones", ignore = true)
    @Mapping(target = "equipment", ignore = true)
    @Mapping(target = "flightLogs", ignore = true)
    Pilot toEntity(PilotDto pilotDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "drones", ignore = true)
    @Mapping(target = "equipment", ignore = true)
    @Mapping(target = "flightLogs", ignore = true)
    void updateEntity(PilotDto pilotDto, @MappingTarget Pilot pilot);
}
