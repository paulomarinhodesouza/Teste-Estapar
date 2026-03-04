package br.com.estapar.garage.repository;

import br.com.estapar.garage.entities.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GarageRepository extends JpaRepository<Garage, UUID> {
}
