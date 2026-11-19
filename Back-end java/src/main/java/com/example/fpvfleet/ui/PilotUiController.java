package com.example.fpvfleet.ui;

import com.example.fpvfleet.dto.PilotDto;
import com.example.fpvfleet.service.PilotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/pilots")
@RequiredArgsConstructor
public class PilotUiController {

    private final PilotService pilotService;

    @GetMapping
    public String list(Model model) {
        return render(model, new PilotDto());
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        return render(model, pilotService.getPilotById(id));
    }

    @PostMapping
    public String create(@ModelAttribute PilotDto form, RedirectAttributes redirect) {
        PilotDto created = pilotService.createPilot(form);
        redirect.addFlashAttribute("message", "Пілота #" + created.getId() + " створено");
        return "redirect:/ui/pilots";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute PilotDto form, RedirectAttributes redirect) {
        pilotService.updatePilot(id, form);
        redirect.addFlashAttribute("message", "Пілота #" + id + " оновлено");
        return "redirect:/ui/pilots";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        pilotService.deletePilot(id);
        redirect.addFlashAttribute("message", "Пілота #" + id + " видалено");
        return "redirect:/ui/pilots";
    }

    private String render(Model model, PilotDto form) {
        model.addAttribute("pilots", pilotService.getAllPilots());
        model.addAttribute("form", form);
        return "pilots";
    }
}
