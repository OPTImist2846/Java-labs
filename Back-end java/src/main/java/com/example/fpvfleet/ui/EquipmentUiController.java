package com.example.fpvfleet.ui;

import com.example.fpvfleet.dto.EquipmentDto;
import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.EquipmentService;
import com.example.fpvfleet.service.PilotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/equipment")
@RequiredArgsConstructor
public class EquipmentUiController {

    private final EquipmentService equipmentService;
    private final PilotService pilotService;

    @GetMapping
    public String list(Model model) {
        return render(model, new EquipmentDto());
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        return render(model, equipmentService.getEquipmentById(id));
    }

    @PostMapping
    public String create(@ModelAttribute EquipmentDto form, RedirectAttributes redirect) {
        EquipmentDto created = equipmentService.createEquipment(form);
        redirect.addFlashAttribute("message", "Обладнання #" + created.getId() + " створено");
        return "redirect:/ui/equipment";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute EquipmentDto form, RedirectAttributes redirect) {
        equipmentService.updateEquipment(id, form);
        redirect.addFlashAttribute("message", "Обладнання #" + id + " оновлено");
        return "redirect:/ui/equipment";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        equipmentService.deleteEquipment(id);
        redirect.addFlashAttribute("message", "Обладнання #" + id + " видалено");
        return "redirect:/ui/equipment";
    }

    private String render(Model model, EquipmentDto form) {
        List<PilotDto> pilots = pilotService.getAllPilots();
        Map<Long, String> pilotNames = pilots.stream()
                .collect(Collectors.toMap(PilotDto::getId, PilotDto::getUsername));
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("pilots", pilots);
        model.addAttribute("pilotNames", pilotNames);
        model.addAttribute("form", form);
        return "equipment";
    }
}
