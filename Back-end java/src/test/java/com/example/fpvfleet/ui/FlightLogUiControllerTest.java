package com.example.fpvfleet.ui;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.dto.FlightLogDto;
import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.DroneService;
import com.example.fpvfleet.service.FlightLogService;
import com.example.fpvfleet.service.PilotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class FlightLogUiControllerTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2026-11-19T10:15:42Z"), ZoneOffset.UTC);

    @Mock
    private FlightLogService flightLogService;

    @Mock
    private PilotService pilotService;

    @Mock
    private DroneService droneService;

    private FlightLogUiController controller;

    @BeforeEach
    void setUp() {
        controller = new FlightLogUiController(flightLogService, pilotService, droneService, FIXED_CLOCK);
    }

    @Test
    void list_newForm_prefillsFlightDateWithCurrentMinute() {
        given(flightLogService.getAllLogs()).willReturn(List.of());
        given(pilotService.getAllPilots()).willReturn(List.of(pilot(1L, "ace")));
        given(droneService.getAllDrones()).willReturn(List.of(drone(10L, "Nazgul")));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.list(model);

        assertThat(view).isEqualTo("flight-logs");
        FlightLogDto form = (FlightLogDto) model.get("form");
        assertThat(form.getFlightDate()).isEqualTo(LocalDateTime.of(2026, 11, 19, 10, 15));
        assertThat(model.get("pilotNames")).isEqualTo(Map.of(1L, "ace"));
        assertThat(model.get("droneNames")).isEqualTo(Map.of(10L, "Nazgul"));
    }

    @Test
    void edit_existingLog_rendersFormFilledWithLog() {
        FlightLogDto log = log(30L);
        given(flightLogService.getLogById(30L)).willReturn(log);
        given(flightLogService.getAllLogs()).willReturn(List.of(log));
        given(pilotService.getAllPilots()).willReturn(List.of());
        given(droneService.getAllDrones()).willReturn(List.of());
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.edit(30L, model);

        assertThat(view).isEqualTo("flight-logs");
        assertThat(model.get("form")).isSameAs(log);
    }

    @Test
    void create_validForm_redirectsWithSuccessMessage() {
        FlightLogDto form = log(null);
        given(flightLogService.createLog(form)).willReturn(log(30L));
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.create(form, redirect);

        assertThat(view).isEqualTo("redirect:/ui/flight-logs");
        assertThat(redirect.getFlashAttributes().get("message")).isEqualTo("Журнал польоту #30 створено");
    }

    @Test
    void update_validForm_delegatesAndRedirects() {
        FlightLogDto form = log(null);
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.update(30L, form, redirect);

        assertThat(view).isEqualTo("redirect:/ui/flight-logs");
        then(flightLogService).should().updateLog(30L, form);
    }

    @Test
    void delete_existingLog_delegatesAndRedirects() {
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.delete(30L, redirect);

        assertThat(view).isEqualTo("redirect:/ui/flight-logs");
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

    private static PilotDto pilot(Long id, String username) {
        PilotDto dto = new PilotDto();
        dto.setId(id);
        dto.setUsername(username);
        return dto;
    }

    private static DroneDto drone(Long id, String name) {
        DroneDto dto = new DroneDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }
}
