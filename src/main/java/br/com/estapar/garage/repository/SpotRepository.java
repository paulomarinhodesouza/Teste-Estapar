package br.com.estapar.garage.repository;

import br.com.estapar.garage.entities.Sector;
import br.com.estapar.garage.entities.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpotRepository extends JpaRepository<Spot, UUID> {
    Long countBySectorAndOccupiedTrue(Sector sector);
    Spot findByLatAndLng(Double lat, Double lng);
}
