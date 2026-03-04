package br.com.estapar.garage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GarageResponseDTO {
    @JsonProperty("garage")
    private List<SectorDTO> sector;
    @JsonProperty("spots")
    private List<SpotDTO> spots;
}
