package com.fin4bol.fin4bolbackend.controller;

import com.fin4bol.fin4bolbackend.controller.json.PerformanceRapportJson;
import com.fin4bol.fin4bolbackend.service.PerformanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    PerformanceService performanceService;

    public SpecificationController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping()
    public ResponseEntity<List<PerformanceRapportJson>> getAllSpecifications(@RequestHeader(AUTHORIZATION) String token) {
        return ResponseEntity.ok(performanceService.getAllSpecifications(token));
    }

    @GetMapping("{specification-id}")
    public ResponseEntity<PerformanceRapportJson> getSpecification(@RequestHeader(AUTHORIZATION) String token,
                                                                   @PathVariable("specification-id") String specificationId) {
        return ResponseEntity.ok(performanceService.getSpecification(token, specificationId));
    }

    @PostMapping("/upload")
    public ResponseEntity<PerformanceRapportJson> uploadSpecificationFile(@RequestHeader(AUTHORIZATION) String token,
                                                                          @RequestParam("specification-file") MultipartFile specification) {
        PerformanceRapportJson analysisResult = performanceService.handleUploadSpecification(token, specification);
        return ResponseEntity.ok(analysisResult);
    }

    @DeleteMapping("{specification-id}")
    public ResponseEntity<List<PerformanceRapportJson>> deleteSpecification(@RequestHeader(AUTHORIZATION) String token,
                                                                            @PathVariable("specification-id") String specificationId) {
        return ResponseEntity.ok(performanceService.deleteSpecification(token, specificationId));
    }
}
