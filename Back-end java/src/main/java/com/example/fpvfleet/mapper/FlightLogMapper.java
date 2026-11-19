package com.example.fpvfleet.mapper;

import com.example.fpvfleet.dto.FlightLogDto;
import com.example.fpvfleet.entity.FlightLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FlightLogMapper {

    @Mapping(source = "pilot.id", target = "pilotId")
    @Mapping(source = "drone.id", target = "droneId")
    FlightLogDto toDto(FlightLog flightLog);

    @Mapping(source = "pilotId", target = "pilot.id")
    @Mapping(source = "droneId", target = "drone.id")
    FlightLog toEntity(FlightLogDto flightLogDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pilot", ignore = true)
    @Mapping(target = "drone", ignore = true)
    void updateEntity(FlightLogDto flightLogDto, @MappingTarget FlightLog flightLog);
}
