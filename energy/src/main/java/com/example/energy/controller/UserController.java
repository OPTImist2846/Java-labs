package com.example.energy.controller;

import com.example.energy.model.User;
import com.example.energy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> getAll() { return service.getAll(); }
}