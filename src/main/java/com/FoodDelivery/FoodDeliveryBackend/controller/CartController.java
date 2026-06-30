package com.FoodDelivery.FoodDeliveryBackend.controller;

import com.FoodDelivery.FoodDeliveryBackend.io.CartRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.CartResponse;
import com.FoodDelivery.FoodDeliveryBackend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
@Tag(name = "Cart APIs", description = "APIs for managing the authenticated user's shopping cart")
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Add food to cart",
            description = "Adds a food item to the authenticated user's cart. If the item already exists, its quantity is updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Food added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid food ID"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request) {

        String foodId = request.getFoodId();

        if (foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FoodId Not found");
        }

        CartResponse response = cartService.addToCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get user's cart",
            description = "Returns the complete shopping cart of the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {

        CartResponse response = cartService.getCart();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Clear cart",
            description = "Removes all items from the authenticated user's cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart cleared successfully")
    })
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {

        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remove food from cart",
            description = "Removes a specific food item or decreases its quantity in the authenticated user's cart."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food removed from cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid food ID"),
            @ApiResponse(responseCode = "404", description = "Food item not found in cart")
    })
    @PostMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(
            @Parameter(description = "Food item to remove from cart")
            @RequestBody CartRequest request) {

        String foodId = request.getFoodId();

        if (foodId == null || foodId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FoodId Not found");
        }

        CartResponse response = cartService.removeFromCart(request);
        return ResponseEntity.ok(response);
    }
}