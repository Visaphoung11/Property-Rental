package com.properties.property.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.properties.property.apiResponse.ApiResponse;
import com.properties.property.dto.AuthenticationRequest;
import com.properties.property.dto.RegisterUserDto;
import com.properties.property.dto.RoleAssignmentRequest;
import com.properties.property.model.UserModel;
import com.properties.property.repository.UserReposiitory;
import com.properties.property.service.AuthenticationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth-service")

public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserReposiitory userReposiitory;

    public AuthenticationController(AuthenticationService authenticationService, UserReposiitory userReposiitory) {
        this.authenticationService = authenticationService;
        this.userReposiitory = userReposiitory;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody RegisterUserDto registerDto) {
        try {
            UserModel userModel = userReposiitory.findByEmail(registerDto.getEmail());
            
            if (userModel == null) {
                authenticationService.register(registerDto);
                
                ApiResponse apiResponse = ApiResponse.builder()
                        .message("User registered successfully!")
                        .statusCode(HttpStatus.CREATED.value()) // Use 201
                        .build();
                        
                return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
            } else {
                // A duplicate email is a Conflict (409), not a Server Error (500)
                ApiResponse apiResponse = ApiResponse.builder()
                        .message("User with email already exists")
                        .statusCode(HttpStatus.CONFLICT.value())
                        .build();
                        
                return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
            }

        } catch (Exception ex) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .message("Failed: " + ex.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse<UserModel>> assignRole(@RequestBody RoleAssignmentRequest request) {
        try {
            ApiResponse<UserModel> response = authenticationService.assignRoleToUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            // Return a parameterized ApiResponseWithSuccess even in case of error
            ApiResponse<UserModel> errorResponse = ApiResponse.<UserModel>builder()
                    .message("Failed: " + ex.getMessage())
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @SuppressWarnings("rawtypes")
	@PostMapping("/authenticate")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            ApiResponse res = authenticationService.authenticate(authenticationRequest);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            ApiResponse apiResponse = ApiResponse.builder().message("Failed: " + ex.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }

}