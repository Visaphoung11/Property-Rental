package com.properties.property.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String location;
    private AgentDTO agent; 
    private List<PropertyImageDTO> images;
}