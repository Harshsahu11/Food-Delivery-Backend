package com.FoodDelivery.FoodDeliveryBackend.service;

import com.FoodDelivery.FoodDeliveryBackend.io.FoodRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

    String uploadFile(MultipartFile file);

    FoodResponse addFood(FoodRequest request, MultipartFile file);

    List<FoodResponse> getAllFoods();

    FoodResponse getFoodById(String id);

    boolean deleteFile(String file);

    void deleteById(String id);
}
