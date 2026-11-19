package com.example.fpvfleet.controller;

import com.example.fpvfleet.dto.FlightLogDto;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.service.FlightLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FlightLogControllerTest {

    @Mock
    private FlightLogService flightLogService;

    @InjectMocks
    private FlightLogController flightLogController;

    @Test
    void getAllLogs_returnsOkWithServiceResult() {
        List<FlightLogDto> logs = List.of(log(30L));
        given(flightLogService.getAllLogs()).willReturn(logs);

        ResponseEntity<List<FlightLogDto>> response = flightLogController.getAllLogs();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(logs);
    }

    @Test
    void getLogById_existingId_returnsOkWithLog() {
        FlightLogDto log = log(30L);
        given(flightLogService.getLogById(30L)).willReturn(log);

        ResponseEntity<FlightLogDto> response = flightLogController.getLogById(30L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(log);
    }

    @Test
    void getLogById_unknownId_propagatesNotFound() {
        given(flightLogService.getLogById(99L)).willThrow(new ResourceNotFoundException("missing"));

        assertThatThrownBy(() -> flightLogController.getLogById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createLog_validBody_returnsCreated() {
        FlightLogDto request = log(null);
        FlightLogDto created = log(30L);
        given(flightLogService.createLog(request)).willReturn(created);

        ResponseEntity<FlightLogDto> response = flightLogController.createLog(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(created);
    }

    @Test
    void updateLog_validBody_returnsOk() {
        FlightLogDto request = log(null);
        FlightLogDto updated = log(30L);
        given(flightLogService.updateLog(30L, request)).willReturn(updated);

        ResponseEntity<FlightLogDto> response = flightLogController.updateLog(30L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(updated);
    }

    @Test
    void deleteLog_existingId_returnsNoContentAndDelegates() {
        ResponseEntity<Void> response = flightLogController.deleteLog(30L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        then(flightLogService).should().deleteLog(30L);
    }

    private static FlightLogDto log(Long id) {
        FlightLogDto dto = new FlightLogDto();
        dto.setId(id);
        dto.setPilotId(1L);
        dto.setEnvironment("OUTDOOR");
        dto.setDurationMinutes(12);
        return dto;
    }
}
