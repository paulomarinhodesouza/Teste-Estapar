package br.com.estapar.garage.service;

import br.com.estapar.garage.dto.GarageResponseDTO;
import br.com.estapar.garage.dto.SectorDTO;
import br.com.estapar.garage.dto.SpotDTO;
import br.com.estapar.garage.entities.Garage;
import br.com.estapar.garage.entities.Sector;
import br.com.estapar.garage.entities.Spot;
import br.com.estapar.garage.repository.GarageRepository;
import br.com.estapar.garage.repository.SectorRepository;
import br.com.estapar.garage.repository.SpotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GarageService {
    private final RestTemplate restTemplate;
    private final SectorRepository sectorRepository;
    private final SpotRepository spotRepository;
    private final GarageRepository garageRepository;

    public GarageService(RestTemplate restTemplate, GarageRepository garageRepository, SectorRepository sectorRepository, SpotRepository spotRepository) {
        this.restTemplate = restTemplate;
        this.garageRepository = garageRepository;
        this.sectorRepository = sectorRepository;
        this.spotRepository = spotRepository;
    }

    @Transactional
    public void loadGarage() {

        String url = "http://localhost:3000/garage";

        GarageResponseDTO response = restTemplate.getForObject(url, GarageResponseDTO.class);

        if (response == null) return;

        Garage garage = null;

        List<Garage> garages = garageRepository.findAll();

        if(garages.isEmpty()){
            garage = new Garage();
            garageRepository.save(garage);
        }else{
            garage = garageRepository.findAll().getFirst();
        }


        for (SectorDTO dto : response.getSector()) {

            Sector sector = sectorRepository.findByName(dto.getName());

            if(sector == null){
                sector = new Sector();
                sector.setGarage(garage);
                sector.setName(dto.getName());
                sector.setBasePrice(dto.getBasePrice());
                sector.setMaxCapacity(dto.getMaxCapacity());

                sectorRepository.save(sector);
            }else{
                sector.setBasePrice(dto.getBasePrice());
                sector.setMaxCapacity(dto.getMaxCapacity());

                sectorRepository.save(sector);
            }
        }

        for (SpotDTO dto : response.getSpots()) {

            Sector sector = sectorRepository.findByName(dto.getSector());

            if (sector == null) {
                System.err.println("Setor não encontrado para spot: " + dto.getSector());
                continue;
            }

            Spot spot = spotRepository.findByLatAndLng(dto.getLat(), dto.getLng());

            if (spot == null) {
                spot = new Spot();
                spot.setId(dto.getId());
                spot.setSector(sector);
                spot.setLat(dto.getLat());
                spot.setLng(dto.getLng());
                spot.setOccupied(false);

                spotRepository.save(spot);
            }else{
                System.err.println("Vaga já existente");

            }



        }
    }
}
