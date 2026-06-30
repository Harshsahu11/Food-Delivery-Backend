package com.FoodDelivery.FoodDeliveryBackend.service;

import com.FoodDelivery.FoodDeliveryBackend.io.CartRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.CartResponse;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    CartResponse getCart();

    void clearCart();

    CartResponse removeFromCart(CartRequest request);

}
