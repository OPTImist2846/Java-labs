package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.FlightLogDto;
import com.example.fpvfleet.entity.Drone;
import com.example.fpvfleet.entity.FlightLog;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.mapper.FlightLogMapper;
import com.example.fpvfleet.repository.DroneRepository;
import com.example.fpvfleet.repository.FlightLogRepository;
import com.example.fpvfleet.repository.PilotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class FlightLogServiceTest {

    private static final LocalDateTime FLIGHT_DATE = LocalDateTime.of(2026, 11, 19, 10, 30);

    @Mock
    private FlightLogRepository flightLogRepository;

    @Mock
    private PilotRepository pilotRepository;

    @Mock
    private DroneRepository droneRepository;

    @Spy
    private FlightLogMapper flightLogMapper = Mappers.getMapper(FlightLogMapper.class);

    @InjectMocks
    private FlightLogService flightLogService;

    @Captor
    private ArgumentCaptor<FlightLog> logCaptor;

    @Test
    void getAllLogs_whenLogsExist_returnsDtosWithPilotAndDroneIds() {
        Pilot owner = pilot(1L);
        given(flightLogRepository.findAll()).willReturn(List.of(log(30L, owner, drone(10L, owner)), log(31L, owner, null)));

        List<FlightLogDto> result = flightLogService.getAllLogs();

        assertThat(result).extracting(FlightLogDto::getId).containsExactly(30L, 31L);
        assertThat(result).extracting(FlightLogDto::getDroneId).containsExactly(10L, null);
    }

    @Test
    void getAllLogs_whenNoLogs_returnsEmptyList() {
        given(flightLogRepository.findAll()).willReturn(List.of());

        List<FlightLogDto> result = flightLogService.getAllLogs();

        assertThat(result).isEmpty();
    }

    @Test
    void getLogById_existingId_returnsDtoWithAllFields() {
        Pilot owner = pilot(1L);
        given(flightLogRepository.findById(30L)).willReturn(Optional.of(log(30L, owner, drone(10L, owner))));

        FlightLogDto result = flightLogService.getLogById(30L);

        assertThat(result.getPilotId()).isEqualTo(1L);
        assertThat(result.getDroneId()).isEqualTo(10L);
        assertThat(result.getEnvironment()).isEqualTo("OUTDOOR");
        assertThat(result.getDurationMinutes()).isEqualTo(12);
        assertThat(result.getFlightDate()).isEqualTo(FLIGHT_DATE);
    }

    @Test
    void getLogById_unknownId_throwsResourceNotFound() {
        given(flightLogRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> flightLogService.getLogById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createLog_withDrone_savesLogLinkedToLoadedPilotAndDrone() {
        Pilot owner = pilot(1L);
        Drone drone = drone(10L, owner);
        given(pilotRepository.findById(1L)).willReturn(Optional.of(owner));
        given(droneRepository.findById(10L)).willReturn(Optional.of(drone));
        given(flightLogRepository.save(any(FlightLog.class))).willAnswer(invocation -> {
            FlightLog saved = invocation.getArgument(0);
            saved.setId(30L);
            return saved;
        });

        FlightLogDto result = flightLogService.createLog(dto(77L, 1L, 10L));

        then(flightLogRepository).should().save(logCaptor.capture());
        FlightLog saved = logCaptor.getValue();
        assertThat(saved.getPilot()).isSameAs(owner);
        assertThat(saved.getDrone()).isSameAs(drone);
        assertThat(saved.getDurationMinutes()).isEqualTo(12);
        assertThat(saved.getFlightDate()).isEqualTo(FLIGHT_DATE);
        assertThat(result.getId()).isEqualTo(30L);
    }

    @Test
    void createLog_withoutDrone_savesLogWithNullDrone() {
        given(pilotRepository.findById(1L)).willReturn(Optional.of(pilot(1L)));
        given(flightLogRepository.save(any(FlightLog.class))).willAnswer(invocation -> invocation.getArgument(0));

        FlightLogDto result = flightLogService.createLog(dto(null, 1L, null));

        then(flightLogRepository).should().save(logCaptor.capture());
        assertThat(logCaptor.getValue().getDrone()).isNull();
        assertThat(result.getDroneId()).isNull();
        then(droneRepository).shouldHaveNoInteractions();
    }

    @Test
    void createLog_unknownDrone_throwsAndDoesNotSave() {
        given(pilotRepository.findById(1L)).willReturn(Optional.of(pilot(1L)));
        given(droneRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> flightLogService.createLog(dto(null, 1L, 99L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        then(flightLogRepository).should(never()).save(any());
    }

    @Test
    void createLog_nullPilotId_throwsAndDoesNotSave() {
        assertThatThrownBy(() -> flightLogService.createLog(dto(null, null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pilotId");

        then(flightLogRepository).should(never()).save(any());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {0, -5})
    void createLog_nonPositiveDuration_throwsAndDoesNotSave(Integer duration) {
        FlightLogDto request = dto(null, 1L, null);
        request.setDurationMinutes(duration);

        assertThatThrownBy(() -> flightLogService.createLog(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Тривалість");

        then(flightLogRepository).should(never()).save(any());
    }

    @Test
    void createLog_minimalPositiveDuration_isAccepted() {
        FlightLogDto request = dto(null, 1L, null);
        request.setDurationMinutes(1);
        given(pilotRepository.findById(1L)).willReturn(Optional.of(pilot(1L)));
        given(flightLogRepository.save(any(FlightLog.class))).willAnswer(invocation -> invocation.getArgument(0));

        FlightLogDto result = flightLogService.createLog(request);

        assertThat(result.getDurationMinutes()).isEqualTo(1);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void createLog_blankEnvironment_throwsAndDoesNotSave(String environment) {
        FlightLogDto request = dto(null, 1L, null);
        request.setEnvironment(environment);

        assertThatThrownBy(() -> flightLogService.createLog(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Середовище");

        then(flightLogRepository).should(never()).save(any());
    }

    @Test
    void createLog_missingFlightDate_throwsAndDoesNotSave() {
        FlightLogDto request = dto(null, 1L, null);
        request.setFlightDate(null);

        assertThatThrownBy(() -> flightLogService.createLog(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Дата");

        then(flightLogRepository).should(never()).save(any());
    }

    @Test
    void updateLog_newValues_updatesManagedEntityAndClearsDrone() {
        Pilot owner = pilot(1L);
        FlightLog existing = log(30L, owner, drone(10L, owner));
        given(flightLogRepository.findById(30L)).willReturn(Optional.of(existing));
        given(pilotRepository.findById(1L)).willReturn(Optional.of(owner));
        FlightLogDto request = dto(999L, 1L, null);
        request.setEnvironment("INDOOR");
        request.setDurationMinutes(25);

        FlightLogDto result = flightLogService.updateLog(30L, request);

        assertThat(existing.getId()).isEqualTo(30L);
        assertThat(existing.getEnvironment()).isEqualTo("INDOOR");
        assertThat(existing.getDurationMinutes()).isEqualTo(25);
        assertThat(existing.getDrone()).isNull();
        assertThat(result.getDroneId()).isNull();
    }

    @Test
    void updateLog_unknownId_throwsResourceNotFound() {
        given(flightLogRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> flightLogService.updateLog(99L, dto(null, 1L, null)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateLog_invalidDuration_throwsAndKeepsEntity() {
        Pilot owner = pilot(1L);
        FlightLog existing = log(30L, owner, null);
        given(flightLogRepository.findById(30L)).willReturn(Optional.of(existing));
        FlightLogDto request = dto(null, 1L, null);
        request.setDurationMinutes(-1);

        assertThatThrownBy(() -> flightLogService.updateLog(30L, request))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(existing.getDurationMinutes()).isEqualTo(12);
    }

    @Test
    void deleteLog_existingId_deletesThatLog() {
        FlightLog existing = log(30L, pilot(1L), null);
        given(flightLogRepository.findById(30L)).willReturn(Optional.of(existing));

        flightLogService.deleteLog(30L);

        then(flightLogRepository).should().delete(logCaptor.capture());
        assertThat(logCaptor.getValue()).isSameAs(existing);
    }

    @Test
    void deleteLog_unknownId_throwsAndDoesNotDelete() {
        given(flightLogRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> flightLogService.deleteLog(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        then(flightLogRepository).should(never()).delete(any());
    }

    private static Pilot pilot(Long id) {
        return Pilot.builder().id(id).username("pilot" + id).email("pilot" + id + "@fpv.local").role("PILOT").build();
    }

    private static Drone drone(Long id, Pilot owner) {
        return Drone.builder().id(id).name("Nazgul").frameSize("5 inch").pilot(owner).build();
    }

    private static FlightLog log(Long id, Pilot owner, Drone drone) {
        return FlightLog.builder().id(id).pilot(owner).drone(drone).environment("OUTDOOR")
                .durationMinutes(12).flightDate(FLIGHT_DATE).notes("Freestyle").build();
    }

    private static FlightLogDto dto(Long id, Long pilotId, Long droneId) {
        FlightLogDto dto = new FlightLogDto();
        dto.setId(id);
        dto.setPilotId(pilotId);
        dto.setDroneId(droneId);
        dto.setEnvironment("OUTDOOR");
        dto.setDurationMinutes(12);
        dto.setFlightDate(FLIGHT_DATE);
        dto.setNotes("Freestyle");
        return dto;
    }
}
