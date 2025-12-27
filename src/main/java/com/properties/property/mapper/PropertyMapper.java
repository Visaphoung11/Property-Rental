package com.properties.property.mapper;

import java.util.stream.Collectors;

import com.properties.property.dto.AgentDTO;
import com.properties.property.dto.PropertyImageDTO;
import com.properties.property.dto.PropertyResponseDTO;
import com.properties.property.model.Property;
import com.properties.property.model.PropertyImage;
import com.properties.property.model.UserModel;

public class PropertyMapper {

	public static PropertyResponseDTO toResponse(Property property) {
		        // I instantiate PropertyResponseDTO to create object
		PropertyResponseDTO BongChang = new PropertyResponseDTO();
		BongChang.setId(property.getId());
		BongChang.setTitle(property.getTitle());
		BongChang.setDescription(property.getDescription());
		BongChang.setPrice(property.getPrice());
		BongChang.setLocation(property.getLocation());
	    BongChang.setAgent(toAgentDTO(property.getAgent()));
		
	    
	    if(property.getImages() != null) {
	    	
	    	BongChang.setImages(
	    			property.getImages()
	    			.stream()
	    			.map(PropertyMapper::toImageDTO)
                    .collect(Collectors.toList()));
	    	
	    }
		

		return BongChang;

	}
	
	private static AgentDTO toAgentDTO (UserModel usermodel) {
		AgentDTO agentDtoResponse = new AgentDTO();
		agentDtoResponse.setId(usermodel.getId());
		agentDtoResponse.setName(usermodel.getFullName());
		agentDtoResponse.setEmail(usermodel.getEmail());
		return agentDtoResponse;
	}
	
	private static PropertyImageDTO toImageDTO(PropertyImage image) {
		PropertyImageDTO imageDtoResponse = new PropertyImageDTO();
		imageDtoResponse.setId(image.getId());
		imageDtoResponse.setImageUrl(image.getImageUrl());
        return imageDtoResponse;
    }
	
	
	
}
