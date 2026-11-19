package com.example.fpvfleet.service;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.entity.Pilot;
import com.example.fpvfleet.exception.ResourceNotFoundException;
import com.example.fpvfleet.mapper.PilotMapper;
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
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PilotServiceTest {

    @Mock
    private PilotRepository pilotRepository;

    @Spy
    private PilotMapper pilotMapper = Mappers.getMapper(PilotMapper.class);

    @InjectMocks
    private PilotService pilotService;

    @Captor
    private ArgumentCaptor<Pilot> pilotCaptor;

    @Test
    void getAllPilots_whenPilotsExist_returnsAllMappedDtos() {
        given(pilotRepository.findAll()).willReturn(List.of(
                pilot(1L, "ace", "ace@fpv.local"),
                pilot(2L, "rookie", "rookie@fpv.local")));

        List<PilotDto> result = pilotService.getAllPilots();

        assertThat(result).extracting(PilotDto::getId, PilotDto::getUsername)
                .containsExactly(
                        tuple(1L, "ace"),
                        tuple(2L, "rookie"));
    }

    @Test
    void getAllPilots_whenNoPilots_returnsEmptyList() {
        given(pilotRepository.findAll()).willReturn(List.of());

        List<PilotDto> result = pilotService.getAllPilots();

        assertThat(result).isEmpty();
    }

    @Test
    void getPilotById_existingId_returnsDtoWithAllFields() {
        given(pilotRepository.findById(1L)).willReturn(Optional.of(pilot(1L, "ace", "ace@fpv.local")));

        PilotDto result = pilotService.getPilotById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("ace");
        assertThat(result.getEmail()).isEqualTo("ace@fpv.local");
        assertThat(result.getRole()).isEqualTo("PILOT");
    }

    @Test
    void getPilotById_unknownId_throwsResourceNotFound() {
        given(pilotRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> pilotService.getPilotById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createPilot_uniqueUsernameAndEmail_savesPilotIgnoringClientId() {
        PilotDto request = dto(77L, "ace", "ace@fpv.local");
        given(pilotRepository.findByUsername("ace")).willReturn(Optional.empty());
        given(pilotRepository.findByEmail("ace@fpv.local")).willReturn(Optional.empty());
        given(pilotRepository.save(any(Pilot.class))).willAnswer(invocation -> {
            Pilot saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        PilotDto result = pilotService.createPilot(request);

        then(pilotRepository).should().save(pilotCaptor.capture());
        Pilot saved = pilotCaptor.getValue();
        assertThat(saved.getUsername()).isEqualTo("ace");
        assertThat(saved.getEmail()).isEqualTo("ace@fpv.local");
        assertThat(saved.getRole()).isEqualTo("PILOT");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void createPilot_duplicateUsername_throwsAndDoesNotSave() {
        given(pilotRepository.findByUsername("ace")).willReturn(Optional.of(pilot(5L, "ace", "other@fpv.local")));

        assertThatThrownBy(() -> pilotService.createPilot(dto(null, "ace", "ace@fpv.local")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("username");

        then(pilotRepository).should(never()).save(any());
    }

    @Test
    void createPilot_duplicateEmail_throwsAndDoesNotSave() {
        given(pilotRepository.findByUsername("ace")).willReturn(Optional.empty());
        given(pilotRepository.findByEmail("ace@fpv.local")).willReturn(Optional.of(pilot(5L, "other", "ace@fpv.local")));

        assertThatThrownBy(() -> pilotService.createPilot(dto(null, "ace", "ace@fpv.local")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");

        then(pilotRepository).should(never()).save(any());
    }

    @Test
    void updatePilot_newValues_updatesManagedEntity() {
        Pilot existing = pilot(1L, "ace", "ace@fpv.local");
        given(pilotRepository.findById(1L)).willReturn(Optional.of(existing));
        given(pilotRepository.findByUsername("ace2")).willReturn(Optional.empty());
        given(pilotRepository.findByEmail("ace2@fpv.local")).willReturn(Optional.empty());
        PilotDto request = dto(999L, "ace2", "ace2@fpv.local");
        request.setRole("INSTRUCTOR");

        PilotDto result = pilotService.updatePilot(1L, request);

        assertThat(existing.getId()).isEqualTo(1L);
        assertThat(existing.getUsername()).isEqualTo("ace2");
        assertThat(existing.getEmail()).isEqualTo("ace2@fpv.local");
        assertThat(existing.getRole()).isEqualTo("INSTRUCTOR");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void updatePilot_keepsOwnUsernameAndEmail_succeeds() {
        Pilot existing = pilot(1L, "ace", "ace@fpv.local");
        given(pilotRepository.findById(1L)).willReturn(Optional.of(existing));
        given(pilotRepository.findByUsername("ace")).willReturn(Optional.of(existing));
        given(pilotRepository.findByEmail("ace@fpv.local")).willReturn(Optional.of(existing));

        PilotDto result = pilotService.updatePilot(1L, dto(null, "ace", "ace@fpv.local"));

        assertThat(result.getUsername()).isEqualTo("ace");
    }

    @Test
    void updatePilot_usernameTakenByAnotherPilot_throwsAndKeepsEntity() {
        Pilot existing = pilot(1L, "ace", "ace@fpv.local");
        given(pilotRepository.findById(1L)).willReturn(Optional.of(existing));
        given(pilotRepository.findByUsername("taken")).willReturn(Optional.of(pilot(2L, "taken", "t@fpv.local")));

        assertThatThrownBy(() -> pilotService.updatePilot(1L, dto(null, "taken", "ace@fpv.local")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("username");

        assertThat(existing.getUsername()).isEqualTo("ace");
    }

    @Test
    void updatePilot_emailTakenByAnotherPilot_throwsAndKeepsEntity() {
        Pilot existing = pilot(1L, "ace", "ace@fpv.local");
        given(pilotRepository.findById(1L)).willReturn(Optional.of(existing));
        given(pilotRepository.findByUsername("ace")).willReturn(Optional.of(existing));
        given(pilotRepository.findByEmail("t@fpv.local")).willReturn(Optional.of(pilot(2L, "taken", "t@fpv.local")));

        assertThatThrownBy(() -> pilotService.updatePilot(1L, dto(null, "ace", "t@fpv.local")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");

        assertThat(existing.getEmail()).isEqualTo("ace@fpv.local");
    }

    @Test
    void updatePilot_unknownId_throwsResourceNotFound() {
        given(pilotRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> pilotService.updatePilot(99L, dto(null, "ace", "ace@fpv.local")))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deletePilot_existingId_deletesThatPilot() {
        Pilot existing = pilot(1L, "ace", "ace@fpv.local");
        given(pilotRepository.findById(1L)).willReturn(Optional.of(existing));

        pilotService.deletePilot(1L);

        then(pilotRepository).should().delete(pilotCaptor.capture());
        assertThat(pilotCaptor.getValue()).isSameAs(existing);
    }

    @Test
    void deletePilot_unknownId_throwsAndDoesNotDelete() {
        given(pilotRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> pilotService.deletePilot(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        then(pilotRepository).should(never()).delete(any());
    }

    private static Pilot pilot(Long id, String username, String email) {
        return Pilot.builder().id(id).username(username).email(email).role("PILOT").build();
    }

    private static PilotDto dto(Long id, String username, String email) {
        PilotDto dto = new PilotDto();
        dto.setId(id);
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setRole("PILOT");
        return dto;
    }
}
