package com.example.fpvfleet.repository;

import com.example.fpvfleet.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    List<Drone> findAllByPilotId(Long pilotId);
}