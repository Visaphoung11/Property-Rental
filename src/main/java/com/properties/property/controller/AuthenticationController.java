package com.properties.property.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.properties.property.apiResponse.ApiResponse;
import com.properties.property.dto.AuthenticationRequest;
import com.properties.property.dto.RegisterUserDto;
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
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody RegisterUserDto registerTeacherDto) {
        try {
            UserModel userModel = userReposiitory.findByEmail(registerTeacherDto.getEmail());
            System.out.println(
                    "Resandmdnfjjsxbasjkjdbascx cdx kjnbacksc jscbd dazx c b cxbz :" + registerTeacherDto.getName()
                            + registerTeacherDto.getRole());
            if (userModel == null) {
                authenticationService.register(registerTeacherDto);
                ApiResponse apiResponse = ApiResponse.builder().message("User registered successfully!")
                        .statusCode(HttpStatus.OK.value()).build();
                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
            } else {
                ApiResponse apiResponse = ApiResponse.builder().message("User with email already exist")
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
            }

        } catch (Exception ex) {
            ApiResponse apiResponse = ApiResponse.builder().message("Failed: " + ex.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

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