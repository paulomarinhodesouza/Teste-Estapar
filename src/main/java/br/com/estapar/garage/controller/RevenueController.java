package br.com.estapar.garage.controller;

import br.com.estapar.garage.dto.RevenueRequestDTO;
import br.com.estapar.garage.dto.RevenueResponseDTO;
import br.com.estapar.garage.service.RevenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revenue")
public class RevenueController {
    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    public ResponseEntity<RevenueResponseDTO> revenue(@RequestBody RevenueRequestDTO req) {
        return ResponseEntity.ok(revenueService.getRevenue(req));
    }
}
