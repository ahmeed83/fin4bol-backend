package com.fin4bol.fin4bolbackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/version")
public class VersionController {

    @Value("${application.version}")
    private String version;

    @GetMapping()
    public ResponseEntity<String> getProducts() {
        return ResponseEntity.ok(version != null ? version : "No version found");
    }
}
