package com.github.alideweb.stuffshop.modules.health;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health", description = "Simple health check endpoint for uptime monitoring")
public class HealthController {
    @GetMapping
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("Backend is up and running!");
    }
}
