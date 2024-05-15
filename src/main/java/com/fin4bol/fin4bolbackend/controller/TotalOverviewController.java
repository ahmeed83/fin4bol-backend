package com.fin4bol.fin4bolbackend.controller;

import com.fin4bol.fin4bolbackend.controller.json.TotalOverviewJson;
import com.fin4bol.fin4bolbackend.service.TotalOverviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController()
@RequestMapping("/total-overview")
public class TotalOverviewController {

    private final TotalOverviewService totalOverviewService;

    public TotalOverviewController(final TotalOverviewService totalOverviewService) {
        this.totalOverviewService = totalOverviewService;
    }

    @GetMapping("{specification-id}")
    public ResponseEntity<TotalOverviewJson> getSpecification(@RequestHeader(AUTHORIZATION) String token,
                                                              @PathVariable("specification-id") String specificationId) {
        return ResponseEntity.ok(totalOverviewService.getTotalOverview(token, specificationId));
    }
}
