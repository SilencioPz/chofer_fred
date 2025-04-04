package org.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @Value("${google.maps.api.key}")
    private String mapsKey;

    @GetMapping("/debug")
    public String debug() {
        return "API Key: " + mapsKey;
    }
}