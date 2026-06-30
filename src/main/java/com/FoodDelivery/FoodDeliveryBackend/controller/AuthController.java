package com.FoodDelivery.FoodDeliveryBackend.controller;

import com.FoodDelivery.FoodDeliveryBackend.io.AuthenticationRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.AuthenticationResponse;
import com.FoodDelivery.FoodDeliveryBackend.serviceImpl.AppUserDetailService;
import com.FoodDelivery.FoodDeliveryBackend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Authentication APIs", description = "APIs for user authentication and JWT token generation")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "User Login",
            description = "Authenticates the user using email and password and returns a JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailService.loadUserByUsername(request.getEmail());

        String jwtToken = jwtUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse(
                request.getEmail(),
                jwtToken
        );

        return ResponseEntity.ok(response);
    }
}