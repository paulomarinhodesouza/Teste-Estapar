package br.com.estapar.garage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SectorDTO{
    @JsonProperty("sector")
    private String name;
    @JsonProperty("base_price")
    private Double basePrice;
    @JsonProperty("max_capacity")
    private Integer maxCapacity;
}
