package com.example.fpvfleet.ui;

import com.example.fpvfleet.dto.EquipmentDto;
import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.EquipmentService;
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
class EquipmentUiControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @Mock
    private PilotService pilotService;

    @InjectMocks
    private EquipmentUiController controller;

    @Test
    void list_rendersEquipmentWithPilotNameLookup() {
        List<EquipmentDto> items = List.of(item(20L));
        given(equipmentService.getAllEquipment()).willReturn(items);
        given(pilotService.getAllPilots()).willReturn(List.of(pilot(1L, "ace")));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.list(model);

        assertThat(view).isEqualTo("equipment");
        assertThat(model.get("equipmentList")).isSameAs(items);
        assertThat(model.get("pilotNames")).isEqualTo(Map.of(1L, "ace"));
    }

    @Test
    void edit_existingItem_rendersFormFilledWithItem() {
        EquipmentDto item = item(20L);
        given(equipmentService.getEquipmentById(20L)).willReturn(item);
        given(equipmentService.getAllEquipment()).willReturn(List.of(item));
        given(pilotService.getAllPilots()).willReturn(List.of(pilot(1L, "ace")));
        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.edit(20L, model);

        assertThat(view).isEqualTo("equipment");
        assertThat(model.get("form")).isSameAs(item);
    }

    @Test
    void create_validForm_redirectsWithSuccessMessage() {
        EquipmentDto form = item(null);
        given(equipmentService.createEquipment(form)).willReturn(item(20L));
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.create(form, redirect);

        assertThat(view).isEqualTo("redirect:/ui/equipment");
        assertThat(redirect.getFlashAttributes().get("message")).isEqualTo("Обладнання #20 створено");
    }

    @Test
    void update_validForm_delegatesAndRedirects() {
        EquipmentDto form = item(null);
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.update(20L, form, redirect);

        assertThat(view).isEqualTo("redirect:/ui/equipment");
        then(equipmentService).should().updateEquipment(20L, form);
    }

    @Test
    void delete_existingItem_delegatesAndRedirects() {
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String view = controller.delete(20L, redirect);

        assertThat(view).isEqualTo("redirect:/ui/equipment");
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

    private static PilotDto pilot(Long id, String username) {
        PilotDto dto = new PilotDto();
        dto.setId(id);
        dto.setUsername(username);
        return dto;
    }
}
