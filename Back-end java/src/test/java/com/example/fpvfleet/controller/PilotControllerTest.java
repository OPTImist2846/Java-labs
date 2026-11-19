package com.example.fpvfleet.controller;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.service.PilotService;
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
class PilotControllerTest {

    @Mock
    private PilotService pilotService;

    @InjectMocks
    private PilotController pilotController;

    @Test
    void getAllPilots_returnsOkWithServiceResult() {
        List<PilotDto> pilots = List.of(pilot(1L), pilot(2L));
        given(pilotService.getAllPilots()).willReturn(pilots);

        ResponseEntity<List<PilotDto>> response = pilotController.getAllPilots();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(pilots);
    }

    @Test
    void getPilotById_existingId_returnsOkWithPilot() {
        PilotDto pilot = pilot(1L);
        given(pilotService.getPilotById(1L)).willReturn(pilot);

        ResponseEntity<PilotDto> response = pilotController.getPilotById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(pilot);
    }

    @Test
    void getPilotById_unknownId_propagatesNotFound() {
        given(pilotService.getPilotById(99L)).willThrow(new ResourceNotFoundException("missing"));

        assertThatThrownBy(() -> pilotController.getPilotById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createPilot_validBody_returnsCreatedWithSavedPilot() {
        PilotDto request = pilot(null);
        PilotDto created = pilot(1L);
        given(pilotService.createPilot(request)).willReturn(created);

        ResponseEntity<PilotDto> response = pilotController.createPilot(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(created);
    }

    @Test
    void updatePilot_validBody_returnsOkAndPassesIdToService() {
        PilotDto request = pilot(null);
        PilotDto updated = pilot(1L);
        given(pilotService.updatePilot(1L, request)).willReturn(updated);

        ResponseEntity<PilotDto> response = pilotController.updatePilot(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(updated);
    }

    @Test
    void deletePilot_existingId_returnsNoContentAndDelegates() {
        ResponseEntity<Void> response = pilotController.deletePilot(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        then(pilotService).should().deletePilot(1L);
    }

    private static PilotDto pilot(Long id) {
        PilotDto dto = new PilotDto();
        dto.setId(id);
        dto.setUsername("ace");
        return dto;
    }
}
