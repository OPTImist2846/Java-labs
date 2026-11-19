package com.example.fpvfleet.controller;

import com.example.fpvfleet.dto.EquipmentDto;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.service.EquipmentService;
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
class EquipmentControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    @Test
    void getAllEquipment_returnsOkWithServiceResult() {
        List<EquipmentDto> items = List.of(item(20L));
        given(equipmentService.getAllEquipment()).willReturn(items);

        ResponseEntity<List<EquipmentDto>> response = equipmentController.getAllEquipment();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(items);
    }

    @Test
    void getEquipmentById_existingId_returnsOkWithItem() {
        EquipmentDto item = item(20L);
        given(equipmentService.getEquipmentById(20L)).willReturn(item);

        ResponseEntity<EquipmentDto> response = equipmentController.getEquipmentById(20L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(item);
    }

    @Test
    void getEquipmentById_unknownId_propagatesNotFound() {
        given(equipmentService.getEquipmentById(99L)).willThrow(new ResourceNotFoundException("missing"));

        assertThatThrownBy(() -> equipmentController.getEquipmentById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getEquipmentByPilotId_returnsOkWithPilotItems() {
        List<EquipmentDto> items = List.of(item(20L));
        given(equipmentService.getEquipmentByPilotId(1L)).willReturn(items);

        ResponseEntity<List<EquipmentDto>> response = equipmentController.getEquipmentByPilotId(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(items);
    }

    @Test
    void createEquipment_validBody_returnsCreated() {
        EquipmentDto request = item(null);
        EquipmentDto created = item(20L);
        given(equipmentService.createEquipment(request)).willReturn(created);

        ResponseEntity<EquipmentDto> response = equipmentController.createEquipment(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(created);
    }

    @Test
    void updateEquipment_validBody_returnsOk() {
        EquipmentDto request = item(null);
        EquipmentDto updated = item(20L);
        given(equipmentService.updateEquipment(20L, request)).willReturn(updated);

        ResponseEntity<EquipmentDto> response = equipmentController.updateEquipment(20L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(updated);
    }

    @Test
    void deleteEquipment_existingId_returnsNoContentAndDelegates() {
        ResponseEntity<Void> response = equipmentController.deleteEquipment(20L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        then(equipmentService).should().deleteEquipment(20L);
    }

    private static EquipmentDto item(Long id) {
        EquipmentDto dto = new EquipmentDto();
        dto.setId(id);
        dto.setName("Goggles");
        dto.setType("GOGGLES");
        dto.setPilotId(1L);
        return dto;
    }
}
