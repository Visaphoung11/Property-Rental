package com.properties.property.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private Long userId;
    private String gender;
    private String contactNumber;
    private String role;

    private String accessToken;
    private String refreshToken;
}
