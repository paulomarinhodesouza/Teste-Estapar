package br.com.estapar.garage.repository;

import br.com.estapar.garage.entities.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SectorRepository extends JpaRepository<Sector, UUID> {
    Sector findByName(String name);
}
