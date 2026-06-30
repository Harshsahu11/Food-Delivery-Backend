package com.FoodDelivery.FoodDeliveryBackend.service;


import com.FoodDelivery.FoodDeliveryBackend.io.UserRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    String findByUserId();

}
