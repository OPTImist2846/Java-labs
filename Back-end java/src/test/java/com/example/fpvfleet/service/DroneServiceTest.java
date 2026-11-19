package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.entity.Drone;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.mapper.DroneMapper;
import com.example.fpvfleet.repository.DroneRepository;
import com.example.fpvfleet.repository.PilotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private PilotRepository pilotRepository;

    @Spy
    private DroneMapper droneMapper = Mappers.getMapper(DroneMapper.class);

    @InjectMocks
    private DroneService droneService;

    @Captor
    private ArgumentCaptor<Drone> droneCaptor;

    @Test
    void getAllDrones_whenDronesExist_returnsDtosWithPilotIds() {
        Pilot owner = pilot(1L);
        given(droneRepository.findAll()).willReturn(List.of(drone(10L, "Nazgul", owner), drone(11L, "Cinewhoop", owner)));

        List<DroneDto> result = droneService.getAllDrones();

        assertThat(result).extracting(DroneDto::getName).containsExactly("Nazgul", "Cinewhoop");
        assertThat(result).extracting(DroneDto::getPilotId).containsOnly(1L);
    }

    @Test
    void getAllDrones_whenNoDrones_returnsEmptyList() {
        given(droneRepository.findAll()).willReturn(List.of());

        List<DroneDto> result = droneService.getAllDrones();

        assertThat(result).isEmpty();
    }

    @Test
    void getDroneById_existingId_returnsDto() {
        given(droneRepository.findById(10L)).willReturn(Optional.of(drone(10L, "Nazgul", pilot(1L))));

        DroneDto result = droneService.getDroneById(10L);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getName()).isEqualTo("Nazgul");
        assertThat(result.getFrameSize()).isEqualTo("5 inch");
        assertThat(result.getPilotId()).isEqualTo(1L);
    }

    @Test
    void getDroneById_unknownId_throwsResourceNotFound() {
        given(droneRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> droneService.getDroneById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getDronesByPilotId_pilotWithDrones_returnsOnlyHisDrones() {
        given(droneRepository.findAllByPilotId(1L)).willReturn(List.of(drone(10L, "Nazgul", pilot(1L))));

        List<DroneDto> result = droneService.getDronesByPilotId(1L);

        assertThat(result).singleElement().extracting(DroneDto::getId).isEqualTo(10L);
    }

    @Test
    void getDronesByPilotId_pilotWithoutDrones_returnsEmptyList() {
        given(droneRepository.findAllByPilotId(2L)).willReturn(List.of());

        List<DroneDto> result = droneService.getDronesByPilotId(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void createDrone_existingPilot_savesDroneLinkedToLoadedPilot() {
        Pilot owner = pilot(1L);
        given(pilotRepository.findById(1L)).willReturn(Optional.of(owner));
        given(droneRepository.save(any(Drone.class))).willAnswer(invocation -> {
            Drone saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        DroneDto result = droneService.createDrone(dto(55L, "Nazgul", 1L));

        then(droneRepository).should().save(droneCaptor.capture());
        Drone saved = droneCaptor.getValue();
        assertThat(saved.getName()).isEqualTo("Nazgul");
        assertThat(saved.getPilot()).isSameAs(owner);
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getPilotId()).isEqualTo(1L);
    }

    @Test
    void createDrone_nullPilotId_throwsAndDoesNotSave() {
        assertThatThrownBy(() -> droneService.createDrone(dto(null, "Nazgul", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pilotId");

        then(droneRepository).should(never()).save(any());
    }

    @Test
    void createDrone_unknownPilot_throwsAndDoesNotSave() {
        given(pilotRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> droneService.createDrone(dto(null, "Nazgul", 99L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        then(droneRepository).should(never()).save(any());
    }

    @Test
    void updateDrone_newValuesAndNewPilot_updatesManagedEntity() {
        Drone existing = drone(10L, "Nazgul", pilot(1L));
        Pilot newOwner = pilot(2L);
        given(droneRepository.findById(10L)).willReturn(Optional.of(existing));
        given(pilotRepository.findById(2L)).willReturn(Optional.of(newOwner));

        DroneDto result = droneService.updateDrone(10L, dto(999L, "Nazgul V2", 2L));

        assertThat(existing.getId()).isEqualTo(10L);
        assertThat(existing.getName()).isEqualTo("Nazgul V2");
        assertThat(existing.getPilot()).isSameAs(newOwner);
        assertThat(result.getPilotId()).isEqualTo(2L);
    }

    @Test
    void updateDrone_unknownDrone_throwsResourceNotFound() {
        given(droneRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> droneService.updateDrone(99L, dto(null, "X", 1L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateDrone_unknownPilot_throwsResourceNotFound() {
        given(droneRepository.findById(10L)).willReturn(Optional.of(drone(10L, "Nazgul", pilot(1L))));
        given(pilotRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> droneService.updateDrone(10L, dto(null, "Nazgul", 99L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteDrone_existingId_deletesThatDrone() {
        Drone existing = drone(10L, "Nazgul", pilot(1L));
        given(droneRepository.findById(10L)).willReturn(Optional.of(existing));

        droneService.deleteDrone(10L);

        then(droneRepository).should().delete(droneCaptor.capture());
        assertThat(droneCaptor.getValue()).isSameAs(existing);
    }

    @Test
    void deleteDrone_unknownId_throwsAndDoesNotDelete() {
        given(droneRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> droneService.deleteDrone(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        then(droneRepository).should(never()).delete(any());
    }

    private static Pilot pilot(Long id) {
        return Pilot.builder().id(id).username("pilot" + id).email("pilot" + id + "@fpv.local").role("PILOT").build();
    }

    private static Drone drone(Long id, String name, Pilot owner) {
        return Drone.builder().id(id).name(name).frameSize("5 inch").pilot(owner).build();
    }

    private static DroneDto dto(Long id, String name, Long pilotId) {
        DroneDto dto = new DroneDto();
        dto.setId(id);
        dto.setName(name);
        dto.setFrameSize("5 inch");
        dto.setPilotId(pilotId);
        return dto;
    }
}
