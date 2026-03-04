package br.com.estapar.garage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class WebhookEventDTO {
    @JsonProperty("license_plate")
    private String licensePlate;

    @JsonProperty("entry_time")
    private LocalDateTime entryTime;

    @JsonProperty("exit_time")
    private LocalDateTime exitTime;

    private Double lat;
    private Double lng;

    @JsonProperty("event_type")
    private String eventType;

}
