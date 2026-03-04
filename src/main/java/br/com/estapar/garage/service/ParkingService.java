package br.com.estapar.garage.service;

import br.com.estapar.garage.dto.WebhookEventDTO;
import br.com.estapar.garage.entities.Parked;
import br.com.estapar.garage.entities.Sector;
import br.com.estapar.garage.entities.Spot;
import br.com.estapar.garage.entities.Vehicle;
import br.com.estapar.garage.repository.ParkedRepository;
import br.com.estapar.garage.repository.SectorRepository;
import br.com.estapar.garage.repository.SpotRepository;
import br.com.estapar.garage.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingService {

    private final SectorRepository sectorRepository;
    private final VehicleRepository vehicleRepository;
    private final SpotRepository spotRepository;
    private final ParkedRepository parkedRepository;


    public ParkingService(SectorRepository sectorRepository, VehicleRepository vehicleRepository, SpotRepository spotRepository, ParkedRepository parkedRepository) {
        this.sectorRepository = sectorRepository;
        this.vehicleRepository = vehicleRepository;
        this.spotRepository = spotRepository;
        this.parkedRepository = parkedRepository;
    }

    public void processEvent(WebhookEventDTO event) {

        switch (event.getEventType()) {

            case "ENTRY" -> handleEntry(event);

            case "PARKED" -> handleParked(event);

            case "EXIT" -> handleExit(event);

            default -> throw new IllegalArgumentException("Evento inválido");
        }
    }

    @Transactional
    public void handleEntry(WebhookEventDTO event) {
        List<Sector> sectors = sectorRepository.findAll();

        Spot freeSpot = null;
        Sector selectedSector = null;
        Long occupied = 0L;

        for (Sector sector : sectors) {
            Long countOccupied = spotRepository.countBySectorAndOccupiedTrue(sector);

            if (countOccupied < sector.getMaxCapacity()) {
                List<Spot> spots = sector.getSpots();
                for (Spot spot : spots) {
                    if(!spot.isOccupied()){
                        freeSpot = spot;
                        selectedSector = sector;
                        occupied = countOccupied;
                        break;
                    }
                }
            }else{
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Setor lotado");
            }
            if(freeSpot != null){
                break;
            }

        }

        if (freeSpot == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estacionamento lotado");
        }

        Double occupancyRate = occupied / Double.valueOf(selectedSector.getMaxCapacity());

        Double dynamicPrice = calculateDynamicPrice(selectedSector.getBasePrice(), occupancyRate);


        Vehicle vehicle = vehicleRepository.findByLicensePlate(event.getLicensePlate());

        if(vehicle == null){
            vehicle = new Vehicle();
            vehicle.setLicensePlate(event.getLicensePlate());
            vehicleRepository.save(vehicle);
        }


        Parked parked = new Parked();
        parked.setVehicle(vehicle);
        parked.setEntryTime(event.getEntryTime());
        parked.setAmount(dynamicPrice);
        parked.getVehicle().setStatus(Vehicle.ParkingStatus.ENTRY);
        parkedRepository.save(parked);
    }

    @Transactional
    public void handleParked(WebhookEventDTO event) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(event.getLicensePlate());

        if (vehicle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado");
        }

        Parked parked = parkedRepository.findByVehicleAndVehicleStatusAndExitTimeIsNull(vehicle, Vehicle.ParkingStatus.ENTRY);

        if (parked == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de entrada não encontrado");
        }

        Spot spot = spotRepository.findByLatAndLng(event.getLat(), event.getLng());

        if (spot == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vaga não encontrada");
        }

        if(spot.isOccupied()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vaga ocupada");
        }

        spot.setOccupied(true);
        spotRepository.save(spot);
        parked.setSpot(spot);
        parked.getVehicle().setStatus(Vehicle.ParkingStatus.PARKED);

        parkedRepository.save(parked);
    }

    @Transactional
    public void handleExit(WebhookEventDTO event) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(event.getLicensePlate());

        if (vehicle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado");
        }

        Parked parked = parkedRepository.findByVehicleAndVehicleStatusInAndExitTimeIsNull(vehicle, List.of(Vehicle.ParkingStatus.ENTRY, Vehicle.ParkingStatus.PARKED));

        if (parked == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado");
        }

        LocalDateTime exitTime = event.getExitTime();
        parked.setExitTime(exitTime);

        Duration duration = Duration.between(parked.getEntryTime(), exitTime);

        long totalMinutes = duration.toMinutes();

        Double finalAmount = 0.0;

        if (totalMinutes > 30) {

            long hours = (long) Math.ceil(totalMinutes / 60.0);

            finalAmount = parked.getAmount() * hours;
        }

        parked.setAmount(finalAmount);
        parked.getVehicle().setStatus(Vehicle.ParkingStatus.EXIT);

        Spot spot = parked.getSpot();
        if(spot != null){
            spot.setOccupied(false);
            spotRepository.save(spot);
        }

        parkedRepository.save(parked);
    }

    private Double calculateDynamicPrice(Double basePrice, Double occupancyRate) {

        if (occupancyRate < 0.25) {
            basePrice = basePrice - (basePrice * 0.1);
            return basePrice;
        }

        if (occupancyRate < 0.50) {
            return basePrice;
        }

        if (occupancyRate < 0.75) {
            basePrice = basePrice + (basePrice * 0.1);
            return basePrice;
        }

        basePrice = basePrice + (basePrice * 0.25);
        return basePrice;
    }

}