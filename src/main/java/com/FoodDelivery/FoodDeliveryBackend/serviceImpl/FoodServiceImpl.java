package com.FoodDelivery.FoodDeliveryBackend.serviceImpl;

import com.FoodDelivery.FoodDeliveryBackend.io.FoodRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.FoodResponse;
import com.FoodDelivery.FoodDeliveryBackend.model.Food;
import com.FoodDelivery.FoodDeliveryBackend.repository.FoodRepository;
import com.FoodDelivery.FoodDeliveryBackend.service.FoodService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {

        try {

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "food-delivery"
                    )
            );

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload image to Cloudinary"
            );
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {

        try {

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "food-delivery"
                    )
            );

            String imageUrl = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            Food food = mapToModel(request);
            food.setImageUrl(imageUrl);
            food.setPublicId(publicId);

            food = foodRepository.save(food);

            return convertToResponse(food);

        } catch (IOException e) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to upload image"
            );
        }
    }

    @Override
    public List<FoodResponse> getAllFoods() {

        return foodRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FoodResponse getFoodById(String id) {

        Food food = foodRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Food not found"));

        return convertToResponse(food);
    }

    @Override
    public boolean deleteFile(String publicId) {

        try {

            Map<?, ?> result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

            return "ok".equals(result.get("result"));

        } catch (Exception e) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to delete image"
            );
        }
    }

    @Override
    public void deleteById(String id) {

        Food food = foodRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Food not found"));

        deleteFile(food.getPublicId());

        foodRepository.delete(food);
    }

    private Food mapToModel(FoodRequest request) {

        return Food.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }

    private FoodResponse convertToResponse(Food food) {

        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .description(food.getDescription())
                .category(food.getCategory())
                .price(food.getPrice())
                .imageUrl(food.getImageUrl())
                .build();
    }
}