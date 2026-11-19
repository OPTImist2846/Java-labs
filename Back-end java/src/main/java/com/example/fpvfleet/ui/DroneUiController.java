package com.example.fpvfleet.ui;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.DroneService;
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
@RequestMapping("/ui/drones")
@RequiredArgsConstructor
public class DroneUiController {

    private final DroneService droneService;
    private final PilotService pilotService;

    @GetMapping
    public String list(Model model) {
        return render(model, new DroneDto());
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        return render(model, droneService.getDroneById(id));
    }

    @PostMapping
    public String create(@ModelAttribute DroneDto form, RedirectAttributes redirect) {
        DroneDto created = droneService.createDrone(form);
        redirect.addFlashAttribute("message", "Дрон #" + created.getId() + " створено");
        return "redirect:/ui/drones";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute DroneDto form, RedirectAttributes redirect) {
        droneService.updateDrone(id, form);
        redirect.addFlashAttribute("message", "Дрон #" + id + " оновлено");
        return "redirect:/ui/drones";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        droneService.deleteDrone(id);
        redirect.addFlashAttribute("message", "Дрон #" + id + " видалено");
        return "redirect:/ui/drones";
    }

    private String render(Model model, DroneDto form) {
        List<PilotDto> pilots = pilotService.getAllPilots();
        Map<Long, String> pilotNames = pilots.stream()
                .collect(Collectors.toMap(PilotDto::getId, PilotDto::getUsername));
        model.addAttribute("drones", droneService.getAllDrones());
        model.addAttribute("pilots", pilots);
        model.addAttribute("pilotNames", pilotNames);
        model.addAttribute("form", form);
        return "drones";
    }
}
