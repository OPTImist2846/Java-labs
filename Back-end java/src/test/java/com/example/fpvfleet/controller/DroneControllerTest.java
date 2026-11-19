package com.example.fpvfleet.controller;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.service.DroneService;
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
class DroneControllerTest {

    @Mock
    private DroneService droneService;

    @InjectMocks
    private DroneController droneController;

    @Test
    void getAllDrones_returnsOkWithServiceResult() {
        List<DroneDto> drones = List.of(drone(10L));
        given(droneService.getAllDrones()).willReturn(drones);

        ResponseEntity<List<DroneDto>> response = droneController.getAllDrones();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(drones);
    }

    @Test
    void getDroneById_existingId_returnsOkWithDrone() {
        DroneDto drone = drone(10L);
        given(droneService.getDroneById(10L)).willReturn(drone);

        ResponseEntity<DroneDto> response = droneController.getDroneById(10L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(drone);
    }

    @Test
    void getDroneById_unknownId_propagatesNotFound() {
        given(droneService.getDroneById(99L)).willThrow(new ResourceNotFoundException("missing"));

        assertThatThrownBy(() -> droneController.getDroneById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getDronesByPilotId_returnsOkWithPilotDrones() {
        List<DroneDto> drones = List.of(drone(10L));
        given(droneService.getDronesByPilotId(1L)).willReturn(drones);

        ResponseEntity<List<DroneDto>> response = droneController.getDronesByPilotId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(drones);
    }

    @Test
    void createDrone_validBody_returnsCreated() {
        DroneDto request = drone(null);
        DroneDto created = drone(10L);
        given(droneService.createDrone(request)).willReturn(created);

        ResponseEntity<DroneDto> response = droneController.createDrone(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(created);
    }

    @Test
    void updateDrone_validBody_returnsOk() {
        DroneDto request = drone(null);
        DroneDto updated = drone(10L);
        given(droneService.updateDrone(10L, request)).willReturn(updated);

        ResponseEntity<DroneDto> response = droneController.updateDrone(10L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(updated);
    }

    @Test
    void deleteDrone_existingId_returnsNoContentAndDelegates() {
        ResponseEntity<Void> response = droneController.deleteDrone(10L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        then(droneService).should().deleteDrone(10L);
    }

    private static DroneDto drone(Long id) {
        DroneDto dto = new DroneDto();
        dto.setId(id);
        dto.setName("Nazgul");
        dto.setPilotId(1L);
        return dto;
    }
}
