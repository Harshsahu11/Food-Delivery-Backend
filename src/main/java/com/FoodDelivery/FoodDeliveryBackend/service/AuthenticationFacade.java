package com.FoodDelivery.FoodDeliveryBackend.service;


import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

    Authentication getAuthentication();

}
