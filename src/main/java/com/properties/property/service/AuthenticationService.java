package com.properties.property.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.properties.property.apiResponse.ApiResponse;
import com.properties.property.dto.AuthenticationRequest;
import com.properties.property.dto.RegisterUserDto;
import com.properties.property.dto.RoleAssignmentRequest;
import com.properties.property.enums.enums; 
import com.properties.property.model.Role;
import com.properties.property.model.UserModel;
import com.properties.property.repository.RoleRepository;
import com.properties.property.repository.UserReposiitory;
import org.springframework.transaction.annotation.Transactional;
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
	@Transactional // This ensures the data is actually written to the DB

    public ApiResponse<UserModel> register(RegisterUserDto registerDto) {
        // 1. Create the user object
        UserModel user = UserModel.builder()
                .fullName(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .phone(registerDto.getContactNumber())
                .status("ACTIVE")
                .roles(new HashSet<>()) 
                .build();

        // 2. Assign default USER role
        Role userRole = roleRepository.findByName(enums.USER);
        if (userRole != null) {
            user.getRoles().add(userRole);
        }

        // 3. Assign AGENT role if requested
        if (registerDto.getRole() != null && "AGENT".equalsIgnoreCase(registerDto.getRole())) {
            Role agentRole = roleRepository.findByName(enums.AGENT);
            if (agentRole != null) {
                user.getRoles().add(agentRole);
            }
        }

        userReposiitory.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefresh(new HashMap<>(), user);

        return ApiResponse.<UserModel>builder()
                .message("User registered successfully")
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .statusCode(HttpStatus.CREATED.value())
                .build();
    }

    public ApiResponse<UserModel> assignRoleToUser(RoleAssignmentRequest request) {
        UserModel user = userReposiitory.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userReposiitory.save(user);

        return ApiResponse.<UserModel>builder()
                .message("Role assigned successfully")
                .statusCode(200)
                .userId(user.getId())
                .role(user.getRoles().stream()
                        .map((Role r) -> r.getName().name()) 
                        .collect(Collectors.joining(", ")))
                .accessToken(jwtService.generateToken(user))
                .build();
    }
    
    public ApiResponse<UserModel> authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()));

        UserModel user = userReposiitory.findByEmail(authenticationRequest.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefresh(new HashMap<>(), user);

        String rolesString = user.getRoles().stream()
                .map((Role r) -> r.getName().name()) 
                .collect(Collectors.joining(","));

        return ApiResponse.<UserModel>builder()
                .statusCode(HttpStatus.OK.value())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(rolesString) 
                .userId(user.getId())
                .build();
    }
}