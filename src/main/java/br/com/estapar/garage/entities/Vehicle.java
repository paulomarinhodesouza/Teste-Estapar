package br.com.estapar.garage.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String licensePlate;

    @Column
    private ParkingStatus status;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parked> parked = new ArrayList<>();

    public enum ParkingStatus {
        ENTRY,
        PARKED,
        EXIT
    }
}
