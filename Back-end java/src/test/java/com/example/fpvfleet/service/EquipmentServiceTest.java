package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.EquipmentDto;
import com.example.fpvfleet.entity.Equipment;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.mapper.EquipmentMapper;
import com.example.fpvfleet.repository.EquipmentRepository;
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
class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private PilotRepository pilotRepository;

    @Spy
    private EquipmentMapper equipmentMapper = Mappers.getMapper(EquipmentMapper.class);

    @InjectMocks
    private EquipmentService equipmentService;

    @Captor
    private ArgumentCaptor<Equipment> equipmentCaptor;

    @Test
    void getAllEquipment_whenItemsExist_returnsMappedDtos() {
        Pilot owner = pilot(1L);
        given(equipmentRepository.findAll()).willReturn(List.of(item(20L, "Goggles", "GOGGLES", owner), item(21L, "TX16S", "RADIO", owner)));

        List<EquipmentDto> result = equipmentService.getAllEquipment();

        assertThat(result).extracting(EquipmentDto::getType).containsExactly("GOGGLES", "RADIO");
        assertThat(result).extracting(EquipmentDto::getPilotId).containsOnly(1L);
    }

    @Test
    void getAllEquipment_whenNoItems_returnsEmptyList() {
        given(equipmentRepository.findAll()).willReturn(List.of());

        List<EquipmentDto> result = equipmentService.getAllEquipment();

        assertThat(result).isEmpty();
    }

    @Test
    void getEquipmentById_existingId_returnsDto() {
        given(equipmentRepository.findById(20L)).willReturn(Optional.of(item(20L, "Goggles", "GOGGLES", pilot(1L))));

        EquipmentDto result = equipmentService.getEquipmentById(20L);

        assertThat(result.getName()).isEqualTo("Goggles");
        assertThat(result.getType()).isEqualTo("GOGGLES");
        assertThat(result.getPilotId()).isEqualTo(1L);
    }

    @Test
    void getEquipmentById_unknownId_throwsResourceNotFound() {
        given(equipmentRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> equipmentService.getEquipmentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getEquipmentByPilotId_pilotWithItems_returnsHisItems() {
        given(equipmentRepository.findAllByPilotId(1L)).willReturn(List.of(item(20L, "Goggles", "GOGGLES", pilot(1L))));

        List<EquipmentDto> result = equipmentService.getEquipmentByPilotId(1L);

        assertThat(result).singleElement().extracting(EquipmentDto::getId).isEqualTo(20L);
    }

    @Test
    void getEquipmentByPilotId_pilotWithoutItems_returnsEmptyList() {
        given(equipmentRepository.findAllByPilotId(2L)).willReturn(List.of());

        List<EquipmentDto> result = equipmentService.getEquipmentByPilotId(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void createEquipment_existingPilot_savesItemLinkedToLoadedPilot() {
        Pilot owner = pilot(1L);
        given(pilotRepository.findById(1L)).willReturn(Optional.of(owner));
        given(equipmentRepository.save(any(Equipment.class))).willAnswer(invocation -> {
            Equipment saved = invocation.getArgument(0);
            saved.setId(20L);
            return saved;
        });

        EquipmentDto result = equipmentService.createEquipment(dto(77L, "Goggles", "GOGGLES", 1L));

        then(equipmentRepository).should().save(equipmentCaptor.capture());
        Equipment saved = equipmentCaptor.getValue();
        assertThat(saved.getName()).isEqualTo("Goggles");
        assertThat(saved.getType()).isEqualTo("GOGGLES");
        assertThat(saved.getPilot()).isSameAs(owner);
        assertThat(result.getId()).isEqualTo(20L);
    }

    @Test
    void createEquipment_nullPilotId_throwsAndDoesNotSave() {
        assertThatThrownBy(() -> equipmentService.createEquipment(dto(null, "Goggles", "GOGGLES", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pilotId");

        then(equipmentRepository).should(never()).save(any());
    }

    @Test
    void createEquipment_unknownPilot_throwsAndDoesNotSave() {
        given(pilotRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> equipmentService.createEquipment(dto(null, "Goggles", "GOGGLES", 99L)))
                .isInstanceOf(ResourceNotFoundException.class);

        then(equipmentRepository).should(never()).save(any());
    }

    @Test
    void updateEquipment_newValuesAndNewPilot_updatesManagedEntity() {
        Equipment existing = item(20L, "Goggles", "GOGGLES", pilot(1L));
        Pilot newOwner = pilot(2L);
        given(equipmentRepository.findById(20L)).willReturn(Optional.of(existing));
        given(pilotRepository.findById(2L)).willReturn(Optional.of(newOwner));

        EquipmentDto result = equipmentService.updateEquipment(20L, dto(999L, "TX16S", "RADIO", 2L));

        assertThat(existing.getId()).isEqualTo(20L);
        assertThat(existing.getName()).isEqualTo("TX16S");
        assertThat(existing.getType()).isEqualTo("RADIO");
        assertThat(existing.getPilot()).isSameAs(newOwner);
        assertThat(result.getPilotId()).isEqualTo(2L);
    }

    @Test
    void updateEquipment_unknownItem_throwsResourceNotFound() {
        given(equipmentRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> equipmentService.updateEquipment(99L, dto(null, "X", "RADIO", 1L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateEquipment_nullPilotId_throwsAndKeepsOldPilot() {
        Pilot owner = pilot(1L);
        Equipment existing = item(20L, "Goggles", "GOGGLES", owner);
        given(equipmentRepository.findById(20L)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> equipmentService.updateEquipment(20L, dto(null, "Goggles", "GOGGLES", null)))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(existing.getPilot()).isSameAs(owner);
    }

    @Test
    void deleteEquipment_existingId_deletesThatItem() {
        Equipment existing = item(20L, "Goggles", "GOGGLES", pilot(1L));
        given(equipmentRepository.findById(20L)).willReturn(Optional.of(existing));

        equipmentService.deleteEquipment(20L);

        then(equipmentRepository).should().delete(equipmentCaptor.capture());
        assertThat(equipmentCaptor.getValue()).isSameAs(existing);
    }

    @Test
    void deleteEquipment_unknownId_throwsAndDoesNotDelete() {
        given(equipmentRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> equipmentService.deleteEquipment(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        then(equipmentRepository).should(never()).delete(any());
    }

    private static Pilot pilot(Long id) {
        return Pilot.builder().id(id).username("pilot" + id).email("pilot" + id + "@fpv.local").role("PILOT").build();
    }

    private static Equipment item(Long id, String name, String type, Pilot owner) {
        return Equipment.builder().id(id).name(name).type(type).pilot(owner).build();
    }

    private static EquipmentDto dto(Long id, String name, String type, Long pilotId) {
        EquipmentDto dto = new EquipmentDto();
        dto.setId(id);
        dto.setName(name);
        dto.setType(type);
        dto.setPilotId(pilotId);
        return dto;
    }
}
