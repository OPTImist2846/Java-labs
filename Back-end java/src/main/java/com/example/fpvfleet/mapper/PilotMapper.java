package com.example.fpvfleet.mapper;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.entity.Pilot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PilotMapper {
    PilotDto toDto(Pilot pilot);
    Pilot toEntity(PilotDto pilotDto);
}