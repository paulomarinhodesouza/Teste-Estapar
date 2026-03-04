package br.com.estapar.garage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RevenueRequestDTO {
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("sector")
    private String sector;
}
