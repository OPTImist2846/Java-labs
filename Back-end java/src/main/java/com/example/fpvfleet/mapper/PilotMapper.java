package com.example.fpvfleet.mapper;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.entity.Pilot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PilotMapper {
    PilotDto toDto(Pilot pilot);

    @Mapping(target = "drones", ignore = true)
    @Mapping(target = "flightLogs", ignore = true)
    Pilot toEntity(PilotDto pilotDto);
}