package com.FoodDelivery.FoodDeliveryBackend.serviceImpl;

import com.FoodDelivery.FoodDeliveryBackend.io.OrderRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.OrderResponse;
import com.FoodDelivery.FoodDeliveryBackend.model.Order;
import com.FoodDelivery.FoodDeliveryBackend.repository.CartRepository;
import com.FoodDelivery.FoodDeliveryBackend.repository.OrderRepository;
import com.FoodDelivery.FoodDeliveryBackend.service.OrderService;
import com.FoodDelivery.FoodDeliveryBackend.service.UserService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final CartRepository cartRepository;

    @Value("${razorpay_key}")
    private String RAZORPAY_KEY;

    @Value("${razorpay_secret}")
    private String RAZORPAY_SECRET;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {

        Order order = mapToModel(request);
        order = orderRepository.save(order);

        // Create Razorpay Order
        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", order.getAmount() * 100);
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);

        com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        // Save Razorpay Order Id
        order.setRazorpayOrderId(razorpayOrder.get("id").toString());

        String loggedInUserId = userService.findByUserId();
        order.setUserId(loggedInUserId);

        order = orderRepository.save(order);

        return convertToResponse(order);
    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {

        String razorpayOrderId = paymentData.get("razorpay_order_id");

        Order existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));

        orderRepository.save(existingOrder);

        if ("paid".equalsIgnoreCase(status)) {
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }
    }

    @Override
    public List<OrderResponse> getUserOrders() {

        String loggedInUser = userService.findByUserId();

        List<Order> list = orderRepository.findByUserId(loggedInUser);

        return list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {

        List<Order> list = orderRepository.findAll();

        return list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        order.setOrderStatus(status);

        orderRepository.save(order);
    }

    private OrderResponse convertToResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .amount(order.getAmount())
                .userAddress(order.getUserAddress())
                .userId(order.getUserId())
                .razorpayOrderId(order.getRazorpayOrderId())
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .orderedItems(order.getOrderedItems())
                .build();
    }

    private Order mapToModel(OrderRequest request) {

        return Order.builder()
                .userAddress(request.getUserAddress())
                .amount(request.getAmount())
                .orderedItems(request.getOrderItems())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .orderStatus(request.getOrderStatus())
                .build();
    }
}