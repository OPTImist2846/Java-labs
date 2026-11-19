package com.example.fpvfleet.ui;

import java.util.List;

public record CurrentUser(String username, String fullName, String email, List<String> roles) {

    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}
