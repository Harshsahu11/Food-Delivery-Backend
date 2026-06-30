package com.FoodDelivery.FoodDeliveryBackend.controller;

import com.FoodDelivery.FoodDeliveryBackend.io.UserRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.UserResponse;
import com.FoodDelivery.FoodDeliveryBackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "APIs for User Registration and Authentication")
public class UserController {

    @Autowired
    private final UserService userService;


    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account by providing user details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request){
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }
}
