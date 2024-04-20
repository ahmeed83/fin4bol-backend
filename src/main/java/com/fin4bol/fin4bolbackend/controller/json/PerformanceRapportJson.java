package com.fin4bol.fin4bolbackend.controller.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PerformanceRapportJson {
    private UUID id;
    private String period;
    private String salespersonNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PerformanceJson> performanceList;
}

