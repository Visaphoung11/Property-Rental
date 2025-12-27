package com.properties.property.service;
import com.properties.property.dto.ListResponseDTO;
import com.properties.property.dto.PropertyRequest;
import com.properties.property.dto.PropertyResponseDTO;
import com.properties.property.model.Property;

public interface PropertyService {
    Property createProperty(PropertyRequest request, String email);
    ListResponseDTO<PropertyResponseDTO> getAllProperties(int page, int size);
    PropertyResponseDTO getPropertyById(Long id);
    
    PropertyResponseDTO updateProperty(Long id, PropertyRequest request, String email);
    void deleteProperty(Long id, String email);

}