package com.properties.property.service;

import com.properties.property.dto.ApiResponseWithSuccess;
import com.properties.property.dto.ListResponseDTO;
import com.properties.property.dto.PropertyRequest;
import com.properties.property.dto.PropertyResponseDTO;
import com.properties.property.exception.ForbiddenOperationException;
import com.properties.property.exception.ResourceNotFoundException;
import com.properties.property.mapper.PropertyMapper;
import com.properties.property.model.Property;
import com.properties.property.model.PropertyImage;
import com.properties.property.model.UserModel;
import com.properties.property.repository.PropertyRepository;
import com.properties.property.repository.UserReposiitory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional
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
	public ListResponseDTO<PropertyResponseDTO> getAllProperties(int page, int size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size);
        Page<Property> propertyPage = propertyRepository.findAll(pageable);

        List<PropertyResponseDTO> dtoList = propertyPage.getContent()
                .stream()
                .map(PropertyMapper::toResponse) // This is from toResponse
                .collect(Collectors.toList());
        int currentPage = propertyPage.getNumber() + 1;
        int pageSize = propertyPage.getSize();
        int totalPages = propertyPage.getTotalPages();
        long totalItems = propertyPage.getTotalElements();
        boolean hasNext = propertyPage.hasNext();
        boolean hasPrevious = propertyPage.hasPrevious();

        return new ListResponseDTO<>(
                true,
                "Properties fetched successfully",
                dtoList,
                currentPage,
                pageSize,
                totalPages,
                totalItems,
                hasNext,
                hasPrevious
        );
    }


	@Override
	public PropertyResponseDTO getPropertyById(Long id) {
		// TODO Auto-generated method stub
		// Logic to find property by id
		Property property = propertyRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Property not found with this id "+ id));
		return PropertyMapper.toResponse(property);
	}

	@Override
	public PropertyResponseDTO updateProperty(Long id, PropertyRequest request, String email) {
		// TODO Auto-generated method stub
		
		Property property = propertyRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Property not found with this id "+ id));
              if(!property.getAgent().getEmail().equals(email)) {
            	  throw new RuntimeException("You are not allowed to update this property");
              }
              property.setTitle(request.getTitle());
              property.setDescription(request.getDescription());
              property.setPrice(request.getPrice());
              property.setLocation(request.getLocation());
              
              property.getImages().clear();// orphanRemoval = true → old images deleted
              request.getImageUrls().forEach(url ->
                  property.getImages().add(
                      PropertyImage.builder()
                          .imageUrl(url)
                          .property(property)
                          .build()
                  )
            		  );
              
              Property updated = propertyRepository.save(property);
              return PropertyMapper.toResponse(updated);
	}

	
	@Override
	@Transactional
	public ApiResponseWithSuccess deleteProperty(Long id, String email) {
		// TODO Auto-generated method stub
		
		Property property = propertyRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Property not found with this id "+ id));
              if (!property.getAgent().getEmail().equals(email)) {
            	  
            	  throw new ForbiddenOperationException("You are not allowed to delete this property");
              }
		propertyRepository.deleteById(id);
		
		// return deleted ID
		return new ApiResponseWithSuccess(true, "Property deleted successfully", Map.of("id", id));
	}
   
}