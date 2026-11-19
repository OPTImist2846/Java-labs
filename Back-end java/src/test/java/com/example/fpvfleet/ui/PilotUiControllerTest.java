package com.example.fpvfleet.ui;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.PilotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PilotUiControllerTest {

    @Mock
    private PilotService pilotService;

    @InjectMocks
    private PilotUiController controller;

    @Test
    void list_rendersPilotsWithEmptyForm() {
        List<PilotDto> pilots = List.of(pilot(1L));
        given(pilotService.getAllPilots()).willReturn(pilots);
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.list(model);

        assertThat(view).isEqualTo("pilots");
        assertThat(model.get("pilots")).isSameAs(pilots);
        assertThat(((PilotDto) model.get("form")).getId()).isNull();
    }

    @Test
    void edit_existingPilot_rendersFormFilledWithPilot() {
        PilotDto pilot = pilot(1L);
        given(pilotService.getPilotById(1L)).willReturn(pilot);
        given(pilotService.getAllPilots()).willReturn(List.of(pilot));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.edit(1L, model);

        assertThat(view).isEqualTo("pilots");
        assertThat(model.get("form")).isSameAs(pilot);
    }

    @Test
    void create_validForm_redirectsWithSuccessMessage() {
        PilotDto form = pilot(null);
        given(pilotService.createPilot(form)).willReturn(pilot(7L));
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.create(form, redirect);

        assertThat(view).isEqualTo("redirect:/ui/pilots");
        assertThat(redirect.getFlashAttributes().get("message")).isEqualTo("Пілота #7 створено");
    }

    @Test
    void update_validForm_delegatesAndRedirects() {
        PilotDto form = pilot(null);
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.update(3L, form, redirect);

        assertThat(view).isEqualTo("redirect:/ui/pilots");
        then(pilotService).should().updatePilot(3L, form);
        assertThat(redirect.getFlashAttributes().get("message")).isEqualTo("Пілота #3 оновлено");
    }

    @Test
    void delete_existingPilot_delegatesAndRedirects() {
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.delete(3L, redirect);

        assertThat(view).isEqualTo("redirect:/ui/pilots");
        then(pilotService).should().deletePilot(3L);
        assertThat(redirect.getFlashAttributes().get("message")).isEqualTo("Пілота #3 видалено");
    }

    private static PilotDto pilot(Long id) {
        PilotDto dto = new PilotDto();
        dto.setId(id);
        dto.setUsername("ace");
        return dto;
    }
}
