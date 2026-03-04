package br.com.estapar.garage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SpotDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("sector")
    private String sector;
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;
    @JsonProperty("occupied")
    private Boolean occupied;
}
