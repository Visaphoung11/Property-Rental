package com.properties.property.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterUserDto {
    private String name;
    private String email;
    private String contactNumber;
    private String role;
    private String password;
    private String gender;
}
