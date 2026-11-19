package com.example.fpvfleet.ui;

import com.example.fpvfleet.dto.DroneDto;
import com.example.fpvfleet.dto.FlightLogDto;
import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.DroneService;
import com.example.fpvfleet.service.FlightLogService;
import com.example.fpvfleet.service.PilotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/flight-logs")
@RequiredArgsConstructor
public class FlightLogUiController {

    private final FlightLogService flightLogService;
    private final PilotService pilotService;
    private final DroneService droneService;

    @GetMapping
    public String list(Model model) {
        FlightLogDto form = new FlightLogDto();
        form.setFlightDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        return render(model, form);
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        return render(model, flightLogService.getLogById(id));
    }

    @PostMapping
    public String create(@ModelAttribute FlightLogDto form, RedirectAttributes redirect) {
        FlightLogDto created = flightLogService.createLog(form);
        redirect.addFlashAttribute("message", "Журнал польоту #" + created.getId() + " створено");
        return "redirect:/ui/flight-logs";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute FlightLogDto form, RedirectAttributes redirect) {
        flightLogService.updateLog(id, form);
        redirect.addFlashAttribute("message", "Журнал польоту #" + id + " оновлено");
        return "redirect:/ui/flight-logs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        flightLogService.deleteLog(id);
        redirect.addFlashAttribute("message", "Журнал польоту #" + id + " видалено");
        return "redirect:/ui/flight-logs";
    }

    private String render(Model model, FlightLogDto form) {
        List<PilotDto> pilots = pilotService.getAllPilots();
        List<DroneDto> drones = droneService.getAllDrones();
        model.addAttribute("logs", flightLogService.getAllLogs());
        model.addAttribute("pilots", pilots);
        model.addAttribute("drones", drones);
        model.addAttribute("pilotNames", pilots.stream()
                .collect(Collectors.toMap(PilotDto::getId, PilotDto::getUsername)));
        model.addAttribute("droneNames", drones.stream()
                .collect(Collectors.toMap(DroneDto::getId, DroneDto::getName)));
        model.addAttribute("form", form);
        return "flight-logs";
    }
}
