package com.properties.property.controller;

import java.security.Principal;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.properties.property.dto.ApiResponseWithSuccess;
import com.properties.property.dto.ListResponseDTO;
import com.properties.property.dto.PropertyRequest;
import com.properties.property.dto.PropertyResponseDTO;
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
    
    @GetMapping ("/get-all")
    @PreAuthorize("hasRole('AGENT')")
    public HttpEntity<ListResponseDTO<PropertyResponseDTO>> getAll(Principal principal){
    	ListResponseDTO<PropertyResponseDTO> getAllProperties = propertyService.getAllProperties(0,10);
    	return ResponseEntity.ok(getAllProperties);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<PropertyResponseDTO> getPropertyById(@PathVariable Long id,
	        Principal principal){
    	    PropertyResponseDTO getById = propertyService.getPropertyById(id);
    	
    	return ResponseEntity.ok(getById);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<PropertyResponseDTO> updatePropertyById ( @PathVariable Long id,
            @RequestBody PropertyRequest request, Principal principal){
    	     
    	PropertyResponseDTO updateById = propertyService.updateProperty(id, request, principal.getName());
    	
    	return ResponseEntity.ok(updateById);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApiResponseWithSuccess> deletePropertyById (@PathVariable Long id,
            Principal principal){
    	return  ResponseEntity.ok(
    			propertyService.deleteProperty(id, principal.getName()));
    }
    
    
}