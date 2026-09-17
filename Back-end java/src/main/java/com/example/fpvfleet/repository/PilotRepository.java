package com.example.fpvfleet.repository;

import com.example.fpvfleet.entity.Pilot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Long> {
    Optional<Pilot> findByUsername(String username);
    Optional<Pilot> findByEmail(String email);
}