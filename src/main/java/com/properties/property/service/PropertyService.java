package com.properties.property.service;
import com.properties.property.dto.PropertyRequest;
import com.properties.property.model.Property;
import java.util.List;

public interface PropertyService {
    Property createProperty(PropertyRequest request, String email);
    List<Property> getAllProperties();
    Property getPropertyById(Long id);
}