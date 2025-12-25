package com.properties.property.service;

import com.properties.property.dto.PropertyRequest;
import com.properties.property.model.Property;
import com.properties.property.model.PropertyImage;
import com.properties.property.model.UserModel;
import com.properties.property.repository.PropertyRepository;
import com.properties.property.repository.UserReposiitory;
import com.properties.property.service.PropertyService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserReposiitory userReposiitory;

    @Override
    @Transactional
    public Property createProperty(PropertyRequest request, String email) {
        UserModel agent = userReposiitory.findByEmail(email);
        
        // 1. Build Property
        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .location(request.getLocation())
                .agent(agent)
                .images(new ArrayList<>())
                .build();

        // 2. Add Images if present in the DTO
        if (request.getImageUrls() != null) {
            for (String url : request.getImageUrls()) {
                PropertyImage img = PropertyImage.builder()
                        .imageUrl(url)
                        .property(property)
                        .build();
                property.getImages().add(img);
            }
        }
                
        return propertyRepository.save(property);
    }
    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }
}