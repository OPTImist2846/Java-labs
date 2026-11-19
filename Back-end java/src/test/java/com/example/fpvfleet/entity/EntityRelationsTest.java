package com.example.fpvfleet.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class EntityRelationsTest {

    @Test
    void pilotWithBackReferencedChildren_hashCodeAndToString_doNotRecurse() {
        Pilot pilot = Pilot.builder().id(1L).username("ace").email("ace@fpv.local").role("PILOT").build();
        Drone drone = Drone.builder().id(10L).name("Nazgul").pilot(pilot).build();
        pilot.getDrones().add(drone);
        pilot.getEquipment().add(Equipment.builder().id(20L).name("Goggles").type("GOGGLES").pilot(pilot).build());
        pilot.getFlightLogs().add(FlightLog.builder().id(30L).pilot(pilot).drone(drone).build());

        assertThatCode(() -> {
            pilot.hashCode();
            drone.hashCode();
            pilot.toString();
            drone.toString();
        }).doesNotThrowAnyException();
    }

    @Test
    void entitiesWithSameFields_areDistinctInHashSet() {
        Pilot first = Pilot.builder().username("ace").build();
        Pilot second = Pilot.builder().username("ace").build();

        Set<Pilot> pilots = new HashSet<>(Set.of(first));
        pilots.add(second);

        assertThat(pilots).hasSize(2);
    }
}
