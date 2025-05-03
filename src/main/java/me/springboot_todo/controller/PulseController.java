package me.springboot_todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/health")
public class PulseController {

    @Value("${cors.origins}")
    private List<String> allowedDomains;

    @GetMapping
    public ResponseEntity<HealthResponse> checkHealth(HttpServletRequest request) {

        String origin = request.getHeader("Origin");

        if (allowedDomains.contains(origin)) {
            return ResponseEntity.ok()
                    .header("Access-Control-Allow-Origin", origin)
                    .body(new HealthResponse("OK", LocalDateTime.now()));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    record HealthResponse(String status, LocalDateTime timestamp) {
    }
}
