package br.com.estapar.garage.controller;

import br.com.estapar.garage.dto.WebhookEventDTO;
import br.com.estapar.garage.service.ParkingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final ParkingService parkingService;

    public WebhookController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping
    public ResponseEntity<Void> receiveEvent(@RequestBody WebhookEventDTO event) {

        parkingService.processEvent(event);
        return ResponseEntity.ok().build();
    }
}
