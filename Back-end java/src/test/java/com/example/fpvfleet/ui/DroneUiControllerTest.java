package com.example.fpvfleet.ui;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.DroneService;
import com.example.fpvfleet.service.PilotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DroneUiControllerTest {

    @Mock
    private DroneService droneService;

    @Mock
    private PilotService pilotService;

    @InjectMocks
    private DroneUiController controller;

    @Test
    void list_rendersDronesWithPilotNameLookup() {
        given(droneService.getAllDrones()).willReturn(List.of(drone(10L)));
        given(pilotService.getAllPilots()).willReturn(List.of(pilot(1L, "ace"), pilot(2L, "rookie")));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.list(model);

        assertThat(view).isEqualTo("drones");
        assertThat(model.get("pilotNames")).isEqualTo(Map.of(1L, "ace", 2L, "rookie"));
        assertThat(((DroneDto) model.get("form")).getId()).isNull();
    }

    @Test
    void edit_existingDrone_rendersFormFilledWithDrone() {
        DroneDto drone = drone(10L);
        given(droneService.getDroneById(10L)).willReturn(drone);
        given(droneService.getAllDrones()).willReturn(List.of(drone));
        given(pilotService.getAllPilots()).willReturn(List.of(pilot(1L, "ace")));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.edit(10L, model);

        assertThat(view).isEqualTo("drones");
        assertThat(model.get("form")).isSameAs(drone);
    }

    @Test
    void create_validForm_redirectsWithSuccessMessage() {
        DroneDto form = drone(null);
        given(droneService.createDrone(form)).willReturn(drone(10L));
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.create(form, redirect);

        assertThat(view).isEqualTo("redirect:/ui/drones");
        assertThat(redirect.getFlashAttributes().get("message")).isEqualTo("Дрон #10 створено");
    }

    @Test
    void update_validForm_delegatesAndRedirects() {
        DroneDto form = drone(null);
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.update(10L, form, redirect);

        assertThat(view).isEqualTo("redirect:/ui/drones");
        then(droneService).should().updateDrone(10L, form);
    }

    @Test
    void delete_existingDrone_delegatesAndRedirects() {
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.delete(10L, redirect);

        assertThat(view).isEqualTo("redirect:/ui/drones");
        then(droneService).should().deleteDrone(10L);
        assertThat(redirect.getFlashAttributes().get("message")).isEqualTo("Дрон #10 видалено");
    }

    private static DroneDto drone(Long id) {
        DroneDto dto = new DroneDto();
        dto.setId(id);
        dto.setName("Nazgul");
        dto.setPilotId(1L);
        return dto;
    }

    private static PilotDto pilot(Long id, String username) {
        PilotDto dto = new PilotDto();
        dto.setId(id);
        dto.setUsername(username);
        return dto;
    }
}
