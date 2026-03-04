package br.com.estapar.garage.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class RevenueResponseDTO {
    private Double amount;
    private String currency = "BRL";
    private LocalDateTime timestamp;
}
