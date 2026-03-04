package br.com.estapar.garage.repository;

import br.com.estapar.garage.entities.Parked;
import br.com.estapar.garage.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ParkedRepository extends JpaRepository<Parked, UUID> {
    Parked findByVehicleAndVehicleStatusAndExitTimeIsNull(Vehicle vehicle, Vehicle.ParkingStatus vehicleStatus);
    Parked findByVehicleAndVehicleStatusInAndExitTimeIsNull(Vehicle vehicle, List<Vehicle.ParkingStatus> vehicleStatus);
    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Parked p
        JOIN p.spot sp
        JOIN sp.sector s
        WHERE p.exitTime >= :start
          AND p.exitTime < :end
          AND s.name = :sectorName
    """)
    double sumRevenueBySectorAndDate(@Param("sectorName") String sectorName, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
