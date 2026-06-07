package com.gametest.springprojekt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ActiveVehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_vehicle_id", nullable = false)
    private BaseVehicleEntity baseVehicle;

    @Column(nullable = false)
    private Instant expiryTime;


    public boolean isExpired() {
        if (expiryTime == null) return true;
        return Instant.now().isAfter(expiryTime);
    }
}
