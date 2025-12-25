package com.properties.property.dto;
import lombok.Data;

@Data
public class RoleAssignmentRequest {
    private Long userId;
    private Long roleId;
}