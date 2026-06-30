package com.FoodDelivery.FoodDeliveryBackend.repository;

import com.FoodDelivery.FoodDeliveryBackend.model.Food;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends MongoRepository<Food,String> {

}
