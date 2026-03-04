package br.com.estapar.garage.config;

import br.com.estapar.garage.service.GarageService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class GarageInitializer implements ApplicationRunner {
    private final GarageService garageService;

    public GarageInitializer(GarageService garageService) {
        this.garageService = garageService;
    }

    @Override
    public void run(ApplicationArguments args) {
        try{
            garageService.loadGarage();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }

    }
}
