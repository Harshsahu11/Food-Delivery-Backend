package com.FoodDelivery.FoodDeliveryBackend.controller;

import com.FoodDelivery.FoodDeliveryBackend.io.OrderRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.OrderResponse;
import com.FoodDelivery.FoodDeliveryBackend.service.OrderService;
import com.razorpay.RazorpayException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
@Tag(name = "Order APIs", description = "APIs for managing food orders and payment")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order and generates a Razorpay payment order."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrderWithPayment(
            @RequestBody OrderRequest request) throws RazorpayException {

        OrderResponse response = orderService.createOrderWithPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Verify payment",
            description = "Verifies Razorpay payment signature and updates order status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment verified successfully"),
            @ApiResponse(responseCode = "400", description = "Payment verification failed")
    })
    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPayment(
            @RequestBody Map<String, String> paymentData) {

        orderService.verifyPayment(paymentData, "paid");

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Get logged-in user's orders",
            description = "Returns all orders placed by the authenticated user."
    )
    @ApiResponse(responseCode = "200", description = "Orders fetched successfully")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {

        List<OrderResponse> orders = orderService.getUserOrders();

        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "Delete an order",
            description = "Deletes an order using its Order ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(

            @Parameter(
                    description = "Order ID",
                    example = "685f9d8d4a34b923456789ab"
            )
            @PathVariable String orderId) {

        orderService.removeOrder(orderId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get all orders",
            description = "Returns orders of all users (Admin only)."
    )
    @ApiResponse(responseCode = "200", description = "Orders fetched successfully")
    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getOrdersOfAllUsers() {

        List<OrderResponse> orders = orderService.getOrdersOfAllUsers();

        return ResponseEntity.ok(orders);
    }

    @Operation(
            summary = "Update order status",
            description = "Updates the status of an order (Admin only)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/status/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(

            @Parameter(
                    description = "Order ID",
                    example = "685f9d8d4a34b923456789ab"
            )
            @PathVariable String orderId,

            @Parameter(
                    description = "New Order Status",
                    example = "DELIVERED"
            )
            @RequestParam String status) {

        orderService.updateOrderStatus(orderId, status);

        return ResponseEntity.ok().build();
    }
}