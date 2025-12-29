package com.properties.property.dto;

import lombok.Data;

@Data
public class CreateReviewRequestDTO {
    private Long propertyId;
    private int rating;
    private String initialComment;
}
