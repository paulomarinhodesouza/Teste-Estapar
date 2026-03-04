package br.com.estapar.garage.service;

import br.com.estapar.garage.dto.RevenueRequestDTO;
import br.com.estapar.garage.dto.RevenueResponseDTO;
import br.com.estapar.garage.repository.ParkedRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class RevenueService {
    private final ParkedRepository parkedRepository;

    public RevenueService(ParkedRepository parkedRepository) {
        this.parkedRepository = parkedRepository;
    }

    public RevenueResponseDTO getRevenue(RevenueRequestDTO request) {
        LocalDate start = request.getDate();
        LocalDate end = start.plusDays(1);

        double total = parkedRepository.sumRevenueBySectorAndDate(request.getSector(), start.atStartOfDay(), end.atStartOfDay());

        RevenueResponseDTO response = new RevenueResponseDTO();
        response.setAmount(total);
        response.setTimestamp(start.atStartOfDay());
        return response;
    }
}
