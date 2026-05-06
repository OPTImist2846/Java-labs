package com.example.energy.repository;

import com.example.energy.model.User;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
    // Наша імпровізована база даних у пам'яті
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(String id) {
        return users.get(id);
    }

    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void deleteById(String id) {
        users.remove(id);
    }

    public boolean existsById(String id) {
        return users.containsKey(id);
    }
}