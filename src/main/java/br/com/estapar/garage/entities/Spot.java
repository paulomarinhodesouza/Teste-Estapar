package br.com.estapar.garage.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
public class Spot {
    @Id
    private Long id;

    @Column
    private Double lat;

    @Column
    private Double lng;

    @Column
    private boolean occupied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @OneToMany(mappedBy = "spot", fetch = FetchType.LAZY)
    private List<Parked> parkeds = new ArrayList<>();
}
