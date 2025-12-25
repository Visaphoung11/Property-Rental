package com.properties.property.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.properties.property.dto.PropertyRequest;
import com.properties.property.model.Property;
import com.properties.property.service.PropertyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {
    
    // Inject the Interface
    private final PropertyService propertyService; 

    @PostMapping("/create")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<Property> create(@RequestBody PropertyRequest request, Principal principal) {
        Property createdProperty = propertyService.createProperty(request, principal.getName());
        
        // Change .ok() to .status(HttpStatus.CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
    }
}