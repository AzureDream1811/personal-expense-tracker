package com.azure.expensetracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.expensetracker.dto.request.LoginRequest;
import com.azure.expensetracker.dto.request.RegisterRequest;
import com.azure.expensetracker.dto.response.LoginResponse;
import com.azure.expensetracker.dto.response.UserResponse;
import com.azure.expensetracker.entity.User;
import com.azure.expensetracker.security.CurrentUser;
import com.azure.expensetracker.service.JwtService;
import com.azure.expensetracker.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final CurrentUser currentUser;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request);

        String accessToken = jwtService.generateAccessToken(user);

        LoginResponse loginResponse = new LoginResponse(accessToken, "Bearer", 3600L);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/me")
    public ResponseEntity<Long> getCurrentUserId() {
        return ResponseEntity.ok(currentUser.getId());
    }
    

}
