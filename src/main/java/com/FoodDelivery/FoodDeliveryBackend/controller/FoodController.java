package com.FoodDelivery.FoodDeliveryBackend.controller;

import com.FoodDelivery.FoodDeliveryBackend.io.FoodRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.FoodResponse;
import com.FoodDelivery.FoodDeliveryBackend.service.FoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
@Tag(
        name = "Food Management API",
        description = "APIs for managing food items in the Food Delivery application."
)
public class FoodController {

    private final FoodService foodService;

    @Operation(
            summary = "Add a new food item",
            description = "Uploads a food image to Cloudinary and saves the food details into MongoDB."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Food added successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid food JSON or image",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FoodResponse> addFood(

            @Parameter(
                    description = "Food details in JSON format",
                    required = true,
                    example = "{\"name\":\"Burger\",\"description\":\"Cheese Burger\",\"category\":\"Fast Food\",\"price\":199}"
            )
            @RequestPart("food")
            String foodString,

            @Parameter(
                    description = "Food Image",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("file")
            MultipartFile file
    ) {

        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request;

        try {
            request = objectMapper.readValue(foodString, FoodRequest.class);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid Food JSON"
            );
        }

        FoodResponse response = foodService.addFood(request, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get all food items",
            description = "Returns the complete list of available food items."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Food list fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FoodResponse>> getAllFoods() {

        List<FoodResponse> foods = foodService.getAllFoods();
        return ResponseEntity.ok(foods);
    }

    @Operation(
            summary = "Get food by ID",
            description = "Fetches a food item using its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Food found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Food not found",
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodResponse> getFoodById(

            @Parameter(
                    description = "Unique Food ID",
                    example = "687fd11d432c784fd9b31d20"
            )
            @PathVariable String id
    ) {

        FoodResponse response = foodService.getFoodById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete food",
            description = "Deletes a food item and its associated image from Cloudinary."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Food deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Food not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(

            @Parameter(
                    description = "Unique Food ID",
                    example = "687fd11d432c784fd9b31d20"
            )
            @PathVariable String id
    ) {

        foodService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}