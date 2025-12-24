package com.properties.property.service;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.properties.property.apiResponse.ApiResponse;
import com.properties.property.dto.AuthenticationRequest;
import com.properties.property.dto.RegisterUserDto;
import com.properties.property.enums.enums;
import com.properties.property.model.Role;
import com.properties.property.model.UserModel;
import com.properties.property.repository.RoleRepository;
import com.properties.property.repository.UserReposiitory;

import jakarta.validation.Valid;

@Service
public class AuthenticationService {

    private final UserReposiitory userReposiitory;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserReposiitory userReposiitory, JwtService jwtService,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder,

            AuthenticationManager authenticationManager) {
        this.userReposiitory = userReposiitory;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @SuppressWarnings("rawtypes")
	public ApiResponse register(@Valid RegisterUserDto registerTeacherDto) {
        Role role = roleRepository.findByRole(enums.valueOf(registerTeacherDto.getRole()));
        if (role == null) {
            throw new RuntimeException("Role not found: " + registerTeacherDto.getRole());
        }
        String contact = "+977" + registerTeacherDto.getContactNumber();
        System.out.println("Role = " + registerTeacherDto.getRole());

        UserModel teachers = UserModel.builder()
                .name(registerTeacherDto.getName())
                .phone(contact)
                .email(registerTeacherDto.getEmail())
                .roleName(registerTeacherDto.getRole())
                .password(passwordEncoder.encode(registerTeacherDto.getPassword()))
                .role(role).build();
        userReposiitory.save(teachers);
        var jwtToken = jwtService.generateToken(teachers);
        var refreshToken = jwtService.generateRefresh(new HashMap<>(), teachers);

        return ApiResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    @SuppressWarnings("rawtypes")
	public ApiResponse authenticate(AuthenticationRequest authenticationRequest) {
        System.out.println("getosdhh shakti");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                authenticationRequest.getPassword()));
        System.out.println("getosdhh shakti");

        var user = userReposiitory.findByEmail(authenticationRequest.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefresh(new HashMap<>(), user);
        return ApiResponse.builder().statusCode(HttpStatus.OK.value())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(user.getRoleName())
                .userId(user.getId())
                .build();
    }
}