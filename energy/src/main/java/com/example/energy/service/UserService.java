package com.example.energy.service;

import com.example.energy.model.User;
import com.example.energy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> getAll() { return repository.findAll(); }

    public User getById(String id) { return repository.findById(id); }

    public User create(User item) {
        if (item.getId() == null) item.setId(UUID.randomUUID().toString());
        return repository.save(item);
    }

    public User update(String id, User item) {
        if (!repository.existsById(id)) return null;
        item.setId(id);
        return repository.save(item);
    }

    public boolean delete(String id) {
        if (repository.existsById(id)) { repository.deleteById(id); return true; }
        return false;
    }
}